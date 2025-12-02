package P3.Backend.ExternalServer.Docker.application;

import static P3.Backend.ExternalServer.Docker.Persistent.CONTAINER_NAME;
import static P3.Backend.ExternalServer.Docker.Persistent.COMPANY_INFO;
import static P3.Backend.ExternalServer.Docker.Persistent.CURRENT_CONTAINER_PATH;
import static P3.Backend.ExternalServer.Docker.Persistent.SPRING_ACTUATOR_DEFAULT_ENDPOINT;
import static P3.Backend.ExternalServer.Docker.Persistent.INTERNAL_SERVER_URL;
import P3.Backend.ExternalServer.Docker.classes.IntervalClass;
import P3.Backend.ExternalServer.Docker.manager.DockerStatsService;
import P3.Backend.ExternalServer.Docker.manager.DockerStatsService.ContainerStats;
import P3.Backend.ExternalServer.Docker.manager.WebClientPost;
import P3.Backend.ExternalServer.Docker.classes.ContainerClass;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;

@Component
public class IntervalApplications {
    
    // The path for where the JSON file is stored.
    private static final Path containerListPath = Path.of(CURRENT_CONTAINER_PATH + CONTAINER_NAME);

    // The path for where the company info JSON file is stored.
    private static final Path companyInfoPath = Path.of(CURRENT_CONTAINER_PATH + COMPANY_INFO);

    public static ContainerClass[] containerArr;
    public static IntervalClass[] intervalArr;

    /** This function is used for initiating the interval application.
     * 
     * @param dockerClient Is used for sending and receiving data from the docker.
     * @param webClient Is used for sending and getting HTTP requests.
     */
    public static void Initiation(DockerClient dockerClient, WebClient webClient, Scanner scanner) {

        // Set up array of container classes (get data from JSON file).
        SetupContainerArray();
        
        // Set up interval array (with id, interval, tempInterval)
        SetupIntervalArray(containerArr);

        // Create an AtomicBoolean to be used in different threads.
        AtomicBoolean userInputDetected = new AtomicBoolean(false);

        // Start thread to check for user input.
        checkUserInputThread(userInputDetected, scanner);

        // Start interval scheduler to fetch data based on intervals.
        checkIntervalScheduler(userInputDetected, dockerClient, webClient);
    }

    /**
     *  This function is used to set up the containerArr based on the JSON file that contains all of the containers.
     */
    private static void SetupContainerArray() { 
        try {
            // Get all of the content within the currentContainers JSON file.
            String content = Files.readString(containerListPath);
            
            // Convert all of the content back into a JSON format.
            JSONObject JSONFileObj = new JSONObject(content);


            // Get all of the company info within currentCompany JSON file.
            String info = Files.readString(companyInfoPath);

            // Convert all of the content back into a JSON format.
            JSONObject companyInfoObj = new JSONObject(info);

            
            // Get the amount of active containers within the JSON file, that is not "inactive" or "unconfigured".
            int activeContainerCount = 0;
            for (String key : JSONFileObj.keySet()) {
                JSONObject tempContainer = JSONFileObj.getJSONObject(key);
                if (tempContainer.getString("state").equals("inactive")) continue;
                if (tempContainer.getString("state").equals("unconfigured")) continue;
                activeContainerCount++;
            }

            // Create the length for the array based on the active container count.
            containerArr = new ContainerClass[activeContainerCount];
    
            int index = 0;
            for (String key : JSONFileObj.keySet()) { // Go through each of the keys in the JSON file.
                JSONObject tempContainer = JSONFileObj.getJSONObject(key); // Get the current container.
                
                // If the current container's state is inactive, it should not get set up (or sent with HTTP).
                if (tempContainer.getString("state").equals("inactive")) continue;
                if (tempContainer.getString("state").equals("unconfigured")) continue;

                // Set variables; name, id & interval based on data from JSON file.
                String name = tempContainer.getString("name");
                String id = tempContainer.getString("id");
                Integer interval = tempContainer.getInt("interval");
                Integer publicPort = tempContainer.getInt("publicPort");

                // Get company info data.
                String companyRegion = companyInfoObj.getString("CompanyRegion");
                String companyName = companyInfoObj.getString("CompanyName");
                String companyServer = companyInfoObj.getString("CompanyServer");
                
                // Make a new ContainerClass and add it to the containerArr.
                ContainerClass newContainer = new ContainerClass(name, id, interval, publicPort, 
                                                                    companyRegion, companyName, companyServer);
                containerArr[index] = newContainer;
    
                index++; // Make sure to move the index.
            }

            
        } catch (Exception e) {
            // If anything goes wrong, it is printed.
            e.printStackTrace();
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

    /**
     * This function is used to check for user input in a separate thread.
     * If user input is detected, the AtomicBoolean is set to true and the main thread can stop.
     * @param bool Is used to check if user input has been detected.
     * @param scanner Is used to read user input.
     */
    private static void checkUserInputThread(AtomicBoolean bool, Scanner scanner) {

        // Create a new thread to monitor user input.
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    if (scanner.hasNextLine()) {
                        scanner.nextLine(); // Go past this if there is user input.
                        bool.set(true); // Set the userInputDetected to be true.
                        break;
                    }
                    Thread.sleep(100); // Avoid tight loop.
                }

            } catch (Exception e) {
                // Silently handle if System.in is already closed.
            }
        });

        // Make sure that the thread will not prevent JVM shutdown if the process exits.
        thread.setDaemon(true);

        // Start the input watcher thread.
        thread.start();
    }

    /** This function is used to check the intervals for each of the containers within containerArr, 
     * and then sending all data after fetching the newest information.
     * 
     * @param bool Is used to check if user input has been detected.
     * @param dockerClient Is used for sending and receiving data from the docker.
     * @param webClient Is used for sending and getting HTTP requests.
     */
    private static void checkIntervalScheduler(AtomicBoolean bool, DockerClient dockerClient, WebClient webClient) {
        while (!bool.get()) {
            try {
                // Go through each index in intervalArr and remove 1 second from tempInterval.
                // Fetch container data and put it into containerArr if index's tempInterval is 0.
                for (int i = 0; i < intervalArr.length; i++) {
                    Integer newInterval = intervalArr[i].getTempInterval() - 1; 
                    
                    if (newInterval == 0) {
                        newInterval = intervalArr[i].getInterval(); // Reset the newInterval to the original interval.
                        intervalArr[i].setTempInterval(newInterval);

                        // Fetch all of the container information and put it into containerArr at current index.
                        fetchAllContainerInformation(containerArr[i], dockerClient, webClient);
                        
                        // Set the timestamp for when the data was fetched.
                        containerArr[i].setTimestamp(new Date().getTime());

                        // Try to send the container data to the backend server.
                        try {
                            WebClientPost.sendData(webClient, containerArr[i], INTERNAL_SERVER_URL);
                            
                            System.out.println("JSON data has been send from: " + containerArr[i].getContainerName());
                        } catch (Exception e) {
                            // Print the error if the POST request fails.
                            System.err.println("Failed to POST container data: " + e.getMessage());
                        }
                        
                    } else {
                        intervalArr[i].setTempInterval(newInterval); // Set the new tempInterval.
                    }
                }
                // Wait 1 second.
                Thread.sleep(1000);
                
            } catch (Exception e) {
                // If anything goes wrong, it is printed.
                e.printStackTrace();
            }
        }
    }

    /** This function is used to fetch all of the container information from both Docker and Spring Actuator.
     * 
     * @param container Is used to store all of the container information.
     * @param dockerClient Is used for sending and receiving data from the docker.
     * @param webClient Is used for getting data from the Spring Actuator endpoint.
     */
    private static void fetchAllContainerInformation(ContainerClass container, DockerClient dockerClient, WebClient webClient) {
        
        // Get data from docker container.
        fetchDockerStats(container, dockerClient);
        
        // Fetch the data from the Spring Actuator endpoint if the container is running.
        if (container.getContainerRunning().equals(true)) {

            // Build the container's actuator URL.
            String actuatorUrl = SPRING_ACTUATOR_DEFAULT_ENDPOINT + ":" + container.getPublicPort();

            // Get the status of the actuator.
            String actuatorStatus = getActuatorRunningStatus(callActuator(webClient, actuatorUrl, "/actuator/health"));
            
            // See if the actuator is up and running.
            if (actuatorStatus != null && actuatorStatus.equals("UP")) {
                fetchSpringActuatorStats(container, webClient, actuatorUrl);
            } else {

                // Set JVMRunning to false if the actuator is not reachable.
                container.setJVMRunning(false);

                // Print that the actuator endpoint is not reachable.
                System.out.println("Spring Actuator endpoint not reachable for " + container.getContainerName());
            }
        } else {
            // If the container is not running, skip fetching Spring Actuator stats.
            System.out.println("Container " + container.getContainerName() + " is not running, skipping Spring Actuator stats fetch.");
        }
    }

    /** This function is used for fetching the Docker stats for a specific container.
     * 
     * @param container Is used to store all of the container information.
     * @param dockerClient Is used for getting data from the docker.
     */
    private static void fetchDockerStats(ContainerClass container, DockerClient dockerClient) {
        
        // Create a DockerStatsService to fetch stats.
        DockerStatsService dockerStatsService = new DockerStatsService(dockerClient);
        
        // Get the container ID.
        String containerId = container.getContainerId();
        
        // Fetch the stats from Docker.
        try {
            ContainerStats stats = dockerStatsService.getContainerStats(containerId);
            InspectContainerResponse response = dockerClient
                    .inspectContainerCmd(containerId)
                    .withSize(true)
                    .exec();
                    
            // Set the container stats in the ContainerClass.
            container.setContainerRamUsage(stats.getMemoryUsage()); // Current RAM usage of the container.
            container.setContainerCpuUsage(stats.getCpuTotalUsage()); // Current CPU total usage of the container.
            container.setContainerDiskUsage(response.getSizeRootFs());  // Total size (image + writable layer).
            container.setContainerRunning(response.getState().getRunning()); // Is the container running or not.
            container.setContainerStatus(response.getState().getStatus()); // Current status of the container (running or exited).
            container.setContainerPid(response.getState().getPidLong()); // PID of the container process.
            
            // Get the exit code only if the container is not running.
            if (container.getContainerRunning().equals(false))
                container.setContainerExitCode(response.getState().getExitCodeLong()); // Exit Code for the container.
            else 
                container.setContainerExitCode(null); // No exit code if the container is running.
            
        } catch (Exception e) {
            // If anything goes wrong, it is printed.
            // e.printStackTrace();
            container.setContainerRunning(false); // Is the container running or not.
            container.setContainerStatus("exited"); // Current status of the container (running or exited).
        }
    }

    /** This function is used for fetching the Spring Actuator stats for a specific container.
     * 
     * @param container Is used to store all of the container information.
     * @param webClient Is used for getting data from the Spring Actuator endpoint.
     * @param url Is the URL for the Spring Actuator endpoint.
     */
    private static void fetchSpringActuatorStats(ContainerClass container, WebClient webClient, String url) {

        // Set all of the data recheved from the Spring Actuator endpoint.
        // JVM stats
        container.setJVMRunning(true);
        container.setJVMRamMax(getLongSafe(callActuator(webClient, url, "/actuator/metrics/jvm.memory.max")));
        container.setJVMRamUsage(getLongSafe(callActuator(webClient, url, "/actuator/metrics/jvm.memory.used")));
        container.setJVMCpuUsageStart(getLongSafe(callActuator(webClient, url, "/actuator/metrics/process.start.time")));
        container.setJVMCpuUsagePerc(getLongSafe(callActuator(webClient, url, "/actuator/metrics/process.cpu.usage")));
        container.setJVMThreads(getIntSafe(callActuator(webClient, url, "/actuator/metrics/jvm.threads.live")));
        container.setJVMThreadsStates(getIntSafe(callActuator(webClient, url, "/actuator/metrics/jvm.threads.states")));
        container.setJVMThreadQueued(getIntSafe(callActuator(webClient, url, "/actuator/metrics/executor.queued")));
        container.setJVMUptime(getLongSafe(callActuator(webClient, url, "/actuator/metrics/process.uptime")));

        // DISK / STORAGE
        container.setSystemDiskFree(getLongSafe(callActuator(webClient, url, "/actuator/metrics/disk.free")));
        container.setSystemDiskTotal(getLongSafe(callActuator(webClient, url, "/actuator/metrics/disk.total")));
        
        // CPU
        container.setSystemCpuCores(getIntSafe(callActuator(webClient, url, "/actuator/metrics/system.cpu.count")));
        container.setSystemCpuUsagePerc(getLongSafe(callActuator(webClient, url, "/actuator/metrics/system.cpu.usage")));

        // ERROR LOGS & MISC
        container.setPoolCore(getIntSafe(callActuator(webClient, url, "/actuator/metrics/executor.pool.core")));
        container.setLogbackEvents(getIntSafe(callActuator(webClient, url, "/actuator/metrics/logback.events")));
        container.setLogbackEventsError(getIntSafe(callActuator(webClient, url, "/actuator/metrics/logback.events?tag=level:error")));
        container.setLogbackEventsWarn(getIntSafe(callActuator(webClient, url, "/actuator/metrics/logback.events?tag=level:warn")));
        container.setGarbageCollectSize(getLongSafe(callActuator(webClient, url, "/actuator/metrics/jvm.gc.overhead")));
    }

    /** This function is used to call the Spring Actuator endpoint and get the JSON response.
     * 
     * @param webClient Is used for getting data from the Spring Actuator endpoint.
     * @param url Is the URL for the Spring Actuator endpoint.
     * @param endpoint Is the specific endpoint to call.
     * @return The JSON response from the Spring Actuator endpoint.
     */
    private static JSONObject callActuator(WebClient webClient, String url, String endpoint) {
        // Get the raw JSON response as a string.
        try {
            String jsonString = webClient.get()
                    .uri(url + endpoint)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
    
            // Convert and return the value as a JSON object.
            return new JSONObject(jsonString);
            
        } catch (Exception e) {
            return null;
        }
    }

    /** This function is used to safely get a Long value from a JSON object.
     * 
     * @param json Is the JSON object to get the value from.
     * @return The Long value from the JSON object, or null if not found.
     */
    private static Long getLongSafe(JSONObject json) {
        try {
            return json
                .getJSONArray("measurements")
                .getJSONObject(0)
                .getLong("value");

        } catch (Exception e) {
            return null;
        }
    }

    /** This function is used to safely get an Integer value from a JSON object.
     * 
     * @param json Is the JSON object to get the value from.
     * @return The Integer value from the JSON object, or null if not found.
     */
    private static Integer getIntSafe(JSONObject json) {
        try {
            return json
                .getJSONArray("measurements")
                .getJSONObject(0)
                .getInt("value");

        } catch (Exception e) {
            return null;
        }
    }

    /** This function is used to get the running status from the actuator health JSON.
     * 
     * @param json Is the JSON object to get the value from.
     * @return The running status from the JSON object, or null if not found.
     */
    private static String getActuatorRunningStatus(JSONObject json) {
        try {
            return json
                .getString("status");

        } catch (Exception e) {
            return null;
        }
    }

}
