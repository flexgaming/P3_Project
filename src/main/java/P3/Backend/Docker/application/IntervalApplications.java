package P3.Backend.Docker.application;

import java.net.http.HttpClient;

import static P3.Backend.Docker.Persistent.CURRENT_CONTAINER_PATH;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.HealthStateLog;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.Container;



import reactor.core.publisher.Mono;

import P3.Backend.Docker.classes.IntervalClass;
import P3.Backend.Docker.manager.DockerStatsService;
import P3.Backend.Docker.manager.DockerStatsService.ContainerStats;
import P3.Backend.Docker.classes.ContainerClass;

import P3.Backend.Docker.Persistent;

@Component
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
                Integer publicPort = tempContainer.getInt("publicPort");
                
                // Make a new ContainerClass and add it to the containerArr.
                ContainerClass newContainer = new ContainerClass(name, id, interval, publicPort);
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
                    
                        if (containerArr[i].getContainerRunning().equals(true)) {
                            containerArr[i].setTimestamp(new Date());

                            // If the server is running - then send all data
                                // Remember to set the timestamp

                        } else {
                            // If the server is closed - then only send docker information
                                // Can we get the error codes? (if the docker container is not running)
                                // Properly not, so send that that container is not running.

                        }
                        
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
        
        //fetchSpringActuatorStats(container, dockerClient, Persistent.SPRING_ACTUATOR_DEFAULT_ENDPOINT);
        
        String currentId = container.getContainerId();

        // Gets all of the containers that is in the system and puts it into an array (containers).
        //List<Container> containersTemp = dockerClient.listContainersCmd().withIdFilter(currentId).exec();
        //JSONArray fetchedContainersArr = new JSONArray(containersTemp);



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
            container.setContainerRunning(response.getState().getRunning()); // Is the container running or not.
            container.setContainerStatus(response.getState().getStatus()); // Current status of the container (running or exited).
            container.setContainerPid(response.getState().getPidLong()); // PID of the container process.
            
            
        } catch (Exception e) {
            // If anything goes wrong, it is printed.
            e.printStackTrace();
        }
    }




    
    // @Autowired
    // private WebClient webClient;

    // public void fetchSpringActuatorStats(String actuatorUrl) {
    //     String url = actuatorUrl + ":9090";
    //     callActuator(url, "/actuator/metrics/jvm.memory.used");
    //     callActuator( url, "/actuator/metrics/jvm.threads.live");
    //     callActuator( url, "/actuator/metrics/process.cpu.usage");
    //     callActuator( url, "/actuator/metrics/system.cpu.count");
    //     callActuator( url, "/actuator/metrics/system.cpu.usage");        
    // }
    // //ENDPOINT = "/actuator/metrics/jvm.memory.used"
    // private void callActuator(String url, String endpoint) {
    //     // Get the raw JSON response as a string
    //     String jsonString = webClient.get()
    //             .uri(url + endpoint)
    //             .retrieve()
    //             .bodyToMono(String.class)
    //             .block();

    //     // Parse the JSON
    //     JSONObject response = new JSONObject(jsonString);

    //     // Extract the numeric value from measurements[0].value
    //     double value = response
    //             .getJSONArray("measurements")
    //             .getJSONObject(0)
    //             .getDouble("value");

    //     // Return as a string
    //     System.out.println("Actuator Call to " + url + endpoint + " returned value: " + value);
    // }






    @Autowired
    private static WebClient webClient;

    private static void fetchSpringActuatorStats(ContainerClass container, DockerClient dockerClient, String actuatorUrl) {
        String url = actuatorUrl + ":" + container.getPublicPort();

        container.setJVMRamMax(callActuator( url, "/actuator/metrics/jvm.memory.max").getJSONArray("measurements").getJSONObject(0).getLong("value"));
        container.setJVMRamUsage(callActuator(url, "/actuator/metrics/jvm.memory.used").getJSONArray("measurements").getJSONObject(0).getLong("value"));
        container.setJVMCpuUsageStart(callActuator( url, "/actuator/metrics/process.start.time").getJSONArray("measurements").getJSONObject(0).getLong("value"));
        container.setJVMCpuUsagePerc(callActuator( url, "/actuator/metrics/process.cpu.usage").getJSONArray("measurements").getJSONObject(0).getLong("value"));
        container.setJVMThreads (callActuator( url, "/actuator/metrics/jvm.threads.live").getJSONArray("measurements").getJSONObject(0).getInt("value"));
        container.setJVMThreadsStates(callActuator( url, "/actuator/metrics/jvm.threads.states").getJSONArray("measurements").getJSONObject(0).getInt("value"));
        container.setJVMThreadQueued(callActuator( url, "/actuator/metrics/executor.queued").getJSONArray("measurements").getJSONObject(0).getInt("value"));
        container.setJVMUptime(callActuator( url, "/actuator/metrics/process.uptime").getJSONArray("measurements").getJSONObject(0).getLong("value"));

        container.setSystemDiskFree(callActuator( url, "/actuator/metrics/disk.free").getJSONArray("measurements").getJSONObject(0).getLong("value"));
        container.setSystemDiskTotal(callActuator( url, "/actuator/metrics/disk.total").getJSONArray("measurements").getJSONObject(0).getLong("value"));
        
        container.setSystemCpuCores(callActuator( url, "/actuator/metrics/system.cpu.count").getJSONArray("measurements").getJSONObject(0).getInt("value"));
        container.setSystemCpuUsagePerc(callActuator( url, "/actuator/metrics/system.cpu.usage").getJSONArray("measurements").getJSONObject(0).getLong("value"));

        container.setPoolCore(callActuator( url, "/actuator/metrics/executor.pool.core").getJSONArray("measurements").getJSONObject(0).getInt("value"));
        container.setLogbackEvents(callActuator( url, "/actuator/metrics/logback.events").getJSONArray("measurements").getJSONObject(0).getInt("value"));
        container.setLogbackEventsError(callActuator( url, "/actuator/metrics/logback.events?tag=level:error").getJSONArray("measurements").getJSONObject(0).getInt("value"));
        container.setLogbackEventsWarn(callActuator( url, "/actuator/metrics/logback.events?tag=level:warn").getJSONArray("measurements").getJSONObject(0).getInt("value"));
        container.setGarbageCollectSize(callActuator( url, "/actuator/metrics/jvm.gc.overhead").getJSONArray("measurements").getJSONObject(0).getLong("value"));

    }
    //ENDPOINT = "/actuator/metrics/jvm.memory.used"
    private static JSONObject callActuator(String url, String endpoint) {
        // Get the raw JSON response as a string
        String jsonString = webClient.get()
                .uri(url + endpoint)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Convert and return the value as a JSON object
        return new JSONObject(jsonString);
    }


//"jvm.memory.used", "jvm.memory.max", "jvm.threads.live", "jvm.threads.states", "system.disk.free", "logback.events", "logback.events?tag=level:warn", "logback.events?tag:level=trace", "logback.events?tag:level=debug", "logback.events?tag:level=error", "logback.events?tag:level=info", "process.cpu.usage","process.start.time", "process.uptime", "system.cpu.count", "system.cpu.usage"


// 	    "executor.pool.core",
// "executor.queued",

// 	"jvm.gc.overhead",

// "jvm.memory.max"
// "jvm.memory.used",

// "jvm.threads.live",
	
// 	    "jvm.threads.states",
	

// "logback.events",


// "process.cpu.usage",	//procentage of cpu usage on java application


// "process.start.time",
// "process.uptime",

// "system.cpu.count", 	//number of cores on system
// "system.cpu.usage",		//procentage of cpu usage on system




//"jvm.cpu.usage",
//"system.memory.used",
}
