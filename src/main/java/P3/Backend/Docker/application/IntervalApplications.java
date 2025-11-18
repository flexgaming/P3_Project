package P3.Backend.Docker.application;

import static P3.Backend.Docker.Persistent.CURRENT_CONTAINER_PATH;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.HealthStateLog;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.StatsCmd;

import P3.Backend.Docker.classes.IntervalClass;
import P3.Backend.Docker.manager.DockerStatsService;
import P3.Backend.Docker.manager.DockerStatsService.ContainerStats;
import P3.Backend.Docker.classes.ContainerClass;

public class IntervalApplications {
    
    // The path for where the JSON file is stored.
    private static final Path containerListPath = Path.of("" + CURRENT_CONTAINER_PATH); // Replace "" with desired path.

    public static ContainerClass[] containerArr;
    public static IntervalClass[] intervalArr;


    public static void Initiation(DockerClient dockerClient) {
        
        //////////////////////////////////
        //      Maintenance HTTP        // 
        //////////////////////////////////


        // Set up array of container classes (get data from JSON file)
        SetupContainerArray();

        
        // Set up interval array (with id, interval, tempInterval)
        SetupIntervalArray(containerArr);

        // Create an AtomicBoolean to be used in different threads.
        AtomicBoolean userInputDetected = new AtomicBoolean(false);

        // Start thread to check for user input
        checkUserInputThread(userInputDetected);

        // Start interval scheduler to fetch data based on intervals
        checkIntervalScheduler(userInputDetected, dockerClient);
        
    }

    /**
     *  This function is used to set up the containerArr based on the JSON file that contains all of the containers.
     */
    private static void SetupContainerArray() { 
        try {
            // Get all of the content within the file.
            String content = Files.readString(containerListPath);

            // Convert all of the content back into a JSON format.
            JSONObject JSONFileObj = new JSONObject(content);
    
            // Create the length for the array based on the JSON file.
            containerArr = new ContainerClass[JSONFileObj.length()];
    
            int index = 0;
            for (String key : JSONFileObj.keySet()) { // Go through each of the keys in the JSON file.
                JSONObject tempContainer = JSONFileObj.getJSONObject(key); // Get the current container.
                
                // If the current container's state is inactive, it should not get set up (or sent with HTTP).
                if (tempContainer.getString("state").equals("inactive")) continue;

                // Set variables; name, id & interval based on data from JSON file.
                String name = tempContainer.getString("name");
                String id = tempContainer.getString("id");
                Integer interval = tempContainer.getInt("interval");
                
                // Make a new ContainerClass and add it to the containerArr.
                ContainerClass newContainer = new ContainerClass(name, id, interval);
                containerArr[index] = newContainer;
    
                index++; // Make sure to move the index.
            }
            
        } catch (Exception e) {
            // If anything goes wrong, it is printed.
            e.printStackTrace();
        }
    }

    /**
     * This function is used to check if the JSON file that contains all of the containers exists, 
     * if it does not exist then create one.
     */
    public static void checkFileCreated() {
        if (!Files.exists(containerListPath)) {
            // If the file does not exist, then it has to be created.
            try {
                // In order for it to be considered a JSON file, it has to contain {}.
                Files.writeString(containerListPath, "{}");
            } catch (Exception e) {
                // If anything goes wrong, it is printed.
                e.printStackTrace();
            }
        } 
    }

    /**
     * This function is used to set up the intervalArr based on the containerArr.
     * 
     * This can be used in order to keep track of when to fetch data from each container. 
     */
    private static void SetupIntervalArray(ContainerClass[] containerArr) {
        // Create the length for the array based on the containerArr's length.
        intervalArr = new IntervalClass[containerArr.length];

        // Go through each of the elements in the containerArr and set up the intervalArr.
        for (int i = 0; i < containerArr.length; i++) {

            // Set variables; name, id & interval based on data from JSON file.
            String id = containerArr[i].getContainerId();
            Integer interval = containerArr[i].getContainerInterval();

            // Set the new interval element to current index of intervalArr.
            // Interval used twice is correct, the last will be tempInterval.
            IntervalClass newIntervalElement = new IntervalClass(id, interval, interval);
            intervalArr[i] = newIntervalElement;
        }
    }

    private static void checkUserInputThread(AtomicBoolean bool) {

        Thread thread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                scanner.nextLine(); // Go past this if there is user input.
                bool.set(true); // Set the userInputDetected to be true.
                scanner.close();
                break;
            }
        });

        // Make sure that the thread will not prevent JVM shutdown if the process exits.
        thread.setDaemon(true);

        // Start the input watcher thread
        thread.start();
    }

    private static void checkIntervalScheduler(AtomicBoolean bool, DockerClient dockerClient) {
        while (!bool.get()) {
            try {
                // Go through each index in intervalArr and remove 1 second from tempInterval.
                // Fetch container data and put it into containerArr if index's tempInterval is 0.
                for (int i = 0; i < intervalArr.length; i++) {

                    Integer newInterval = intervalArr[i].getTempInterval() - 1;
                    
                    if (newInterval == 0) {
                        newInterval = intervalArr[i].getInterval(); // Reset the interval.
                        intervalArr[i].setTempInterval(newInterval);

                        // Fetch all of the container information and put it into containerArr at current index.
                        fetchAllContainerInformation(containerArr[i], dockerClient);
                        
                        ////////////////////////////////////
                        //       HTTP FUNCTION HERE       //
                        ////////////////////////////////////
                    } else {
                        intervalArr[i].setTempInterval(newInterval); // Set the new tempInterval.
                    }
                    // Wait 1 second.
                    Thread.sleep(1000);
                }
                
            } catch (Exception e) {
                // If anything goes wrong, it is printed.
                e.printStackTrace();
            }
        }
    }

    private static void fetchAllContainerInformation(ContainerClass container, DockerClient dockerClient) {
        
        // Get data from docker container.
        fetchDockerStats(container, dockerClient);
        
        
        // GET ALL OF THE DATA FROM THE SPRING ACTUATOR:
        
        //fetchSpringActuatorStats();
        

        // GO BACK TO FUNCTION BEFORE AND SEND DATA VIA HTTP TO OTHER SERVER
    }

    private static void fetchDockerStats(ContainerClass container, DockerClient dockerClient) {
        
        
        DockerStatsService dockerStatsService = new DockerStatsService(dockerClient);
        
        
        String containerId = container.getContainerId();
        
        try {
            ContainerStats stats = dockerStatsService.getContainerStats(containerId);
            InspectContainerResponse response = dockerClient
                    .inspectContainerCmd(containerId)
                    .withSize(true)
                    .exec();

            container.setContainerRamUsage(stats.getMemoryUsage()); // Current RAM usage of the container.
            container.setContainerCpuUsage(stats.getCpuTotalUsage()); // Current CPU total usage of the container.
            container.setContainerDiskUsage(response.getSizeRootFs());  // Total size (image + writable layer)
            container.setRunningContainer(response.getState().getRunning()); // Is the container running or not.
            container.setContainerStatus(response.getState().getStatus()); // Current status of the container (running or exited).
            
        } catch (Exception e) {
            // If anything goes wrong, it is printed.
            e.printStackTrace();
        }
    }


}
