package P3.Backend.Docker.application;

import java.io.File;
import java.net.http.HttpClient;

import static P3.Backend.Docker.Persistent.CURRENT_CONTAINER_PATH;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.databind.ObjectWriter; 

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


    public static void Initiation(DockerClient dockerClient, WebClient webClient) {
        
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
        checkIntervalScheduler(userInputDetected, dockerClient, webClient);
        
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
            try {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    if (scanner.hasNextLine()) {
                        scanner.nextLine(); // Go past this if there is user input.
                        bool.set(true); // Set the userInputDetected to be true.
                        break;
                    }
                    Thread.sleep(100); // Avoid tight loop
                }
                scanner.close();
                // Don't close scanner - System.in may be needed elsewhere
            } catch (Exception e) {
                // Silently handle if System.in is already closed
            }
        });

        // Make sure that the thread will not prevent JVM shutdown if the process exits.
        thread.setDaemon(true);

        // Start the input watcher thread
        thread.start();
    }

    private static void checkIntervalScheduler(AtomicBoolean bool, DockerClient dockerClient, WebClient webClient) {
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
                        fetchAllContainerInformation(containerArr[i], dockerClient, webClient);
                        
                        ////////////////////////////////////
                        //       HTTP FUNCTION HERE       //
                        ////////////////////////////////////
                        if (containerArr[i].getContainerRunning().equals(true)) {
                            containerArr[i].setTimestamp(new Date().getTime());

                            ObjectMapper mapper = new ObjectMapper();
                            mapper.writeValue(new File("containerData.json"), containerArr[i]);
                            

                             // If the server is running - then send all data
                                // Remember to set the timestamp

                            // Temp for printing all container stats (Actuator + Stats)
                        } else {
                            // Temp for printing all container stats (Actuator + Stats)
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

    private static void fetchAllContainerInformation(ContainerClass container, DockerClient dockerClient, WebClient webClient) {
        
        // Get data from docker container.
        fetchDockerStats(container, dockerClient);
        
        
        // GET ALL OF THE DATA FROM THE SPRING ACTUATOR:
        if (container.getContainerRunning().equals(true)) {
            fetchSpringActuatorStats(container, webClient, Persistent.SPRING_ACTUATOR_DEFAULT_ENDPOINT);
        } else {
            System.out.println("Couldn't fetch data from " + container.getContainerName());
        }

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
            container.setContainerExitCode(response.getState().getExitCodeLong()); // Exit Code for the container
            
            
        } catch (Exception e) {
            // If anything goes wrong, it is printed.
            // e.printStackTrace();
            container.setContainerRunning(false); // Is the container running or not.
            container.setContainerStatus("exited"); // Current status of the container (running or exited).
            //throw new RuntimeException(containerId + " - Error fetching Docker stats.");
        }
    }


    @Autowired
    private static void fetchSpringActuatorStats(ContainerClass container, WebClient webClient, String actuatorUrl) {
        String url = actuatorUrl + ":" + container.getPublicPort();

        container.setJVMRamMax(getLongSafe(callActuator(webClient, url, "/actuator/metrics/jvm.memory.max")));
        container.setJVMRamUsage(getLongSafe(callActuator(webClient, url, "/actuator/metrics/jvm.memory.used")));
        container.setJVMCpuUsageStart(getLongSafe(callActuator(webClient, url, "/actuator/metrics/process.start.time")));
        container.setJVMCpuUsagePerc(getLongSafe(callActuator(webClient, url, "/actuator/metrics/process.cpu.usage")));
        container.setJVMThreads(getIntSafe(callActuator(webClient, url, "/actuator/metrics/jvm.threads.live")));
        container.setJVMThreadsStates(getIntSafe(callActuator(webClient, url, "/actuator/metrics/jvm.threads.states")));
        container.setJVMThreadQueued(getIntSafe(callActuator(webClient, url, "/actuator/metrics/executor.queued")));
        container.setJVMUptime(getLongSafe(callActuator(webClient, url, "/actuator/metrics/process.uptime")));

        container.setSystemDiskFree(getLongSafe(callActuator(webClient, url, "/actuator/metrics/disk.free")));
        container.setSystemDiskTotal(getLongSafe(callActuator(webClient, url, "/actuator/metrics/disk.total")));
        
        container.setSystemCpuCores(getIntSafe(callActuator(webClient, url, "/actuator/metrics/system.cpu.count")));
        container.setSystemCpuUsagePerc(getLongSafe(callActuator(webClient, url, "/actuator/metrics/system.cpu.usage")));

        container.setPoolCore(getIntSafe(callActuator(webClient, url, "/actuator/metrics/executor.pool.core")));
        container.setLogbackEvents(getIntSafe(callActuator(webClient, url, "/actuator/metrics/logback.events")));
        container.setLogbackEventsError(getIntSafe(callActuator(webClient, url, "/actuator/metrics/logback.events?tag=level:error")));
        container.setLogbackEventsWarn(getIntSafe(callActuator(webClient, url, "/actuator/metrics/logback.events?tag=level:warn")));
        container.setGarbageCollectSize(getLongSafe(callActuator(webClient, url, "/actuator/metrics/jvm.gc.overhead")));

    }
    //ENDPOINT = "/actuator/metrics/jvm.memory.used"
    private static JSONObject callActuator(WebClient webClient, String url, String endpoint) {
        // Get the raw JSON response as a string
        try {
            String jsonString = webClient.get()
                    .uri(url + endpoint)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
    
            // Convert and return the value as a JSON object
            return new JSONObject(jsonString);
            
        } catch (Exception e) {
            return null;
        }
    }

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


    /* private static void jsonPut(JSONObject json, String name, String getStr, Integer getInt, Long getLong, Boolean getBool) {
        try {
            if (getStr != null) {
                json.put(name, getStr);
            }
            if (getInt != null) {
                json.put(name, getInt);
            }
            if (getLong != null) {
                json.put(name, getLong);
            }
            if (getBool != null) {
                json.put(name, getBool);
            }    
        } catch (Exception e) {
            json.put(name, "Failure to put value into JSON.");    
        }
    }


    // For now: pretty-print the payload that would be sent
    private static void printContainerData(ContainerClass c) {
        JSONObject root = new JSONObject();
        root.put("name", c.getContainerName());
        root.put("id", c.getContainerId());
        root.put("timestamp", c.getTimestamp());

        JSONObject meta = new JSONObject();
        jsonPut(meta, "name", c.getContainerName(), null, null, null);
        jsonPut(meta, "id", c.getContainerId(), null, null, null);
        jsonPut(meta, "publicPort", null, c.getPublicPort(), null, null);
        jsonPut(meta, "interval", null, c.getContainerInterval(), null, null);
        if (c.getTimestamp() != null) {
            jsonPut(meta, "timestamp", null, null, c.getTimestamp(), null);
        }
        root.put("metadata", meta);

        JSONObject docker = new JSONObject();

        jsonPut(docker, "running", null, null, null, c.getContainerRunning());
        jsonPut(docker, "ramUsage", null, null, c.getContainerRamUsage(), null);
        jsonPut(docker, "cpuUsage", null, null, c.getContainerCpuUsage(), null);
        jsonPut(docker, "diskUsage", null, null, c.getContainerDiskUsage(), null);
        jsonPut(docker, "status", c.getContainerStatus(), null, null, null);
        jsonPut(docker, "pid", null, null, c.getContainerPid(), null);
        jsonPut(docker, "exitCode", null, null, c.getContainerExitCode(), null);
        root.put("docker", docker);

        JSONObject actuator = new JSONObject();
            JSONObject jvm = new JSONObject();
            jsonPut(jvm, "jvmRunning", null, null, null, c.getJVMRunning());
            jsonPut(jvm, "jvmRamMax", null, null, c.getJVMRamMax(), null);
            jsonPut(jvm, "jvmRamUsage", null, null, c.getJVMRamUsage(), null);
            jsonPut(jvm, "jvmCpuUsagePerc", null, null, c.getJVMCpuUsagePerc(), null);
            jsonPut(jvm, "jvmThreads", null, c.getJVMThreads(), null, null);
            jsonPut(jvm, "jvmThreadsStates", null, c.getJVMThreadsStates(), null, null);
            jsonPut(jvm, "jvmThreadQueued", null, c.getJVMThreadQueued(), null, null);
            jsonPut(jvm, "jvmCpuUsageStart", null, null, c.getJVMCpuUsageStart(), null);
            jsonPut(jvm, "jvmUptime", null, null, c.getJVMUptime(), null);
            actuator.put("jvm", jvm);

            JSONObject system = new JSONObject();
            jsonPut(system, "systemRamUsage", null, null, c.getSystemRamUsage(), null);
            jsonPut(system, "systemCpuUsagePerc", null, null, c.getSystemCpuUsagePerc(), null);
            jsonPut(system, "systemCpuCores", null, c.getSystemCpuCores(), null, null);
            jsonPut(system, "systemDiskUsage", null, null, c.getSystemDiskUsage(), null);
            jsonPut(system, "systemDiskTotal", null, null, c.getSystemDiskTotal(), null);
            jsonPut(system, "systemDiskFree", null, null, c.getSystemDiskFree(), null);
            actuator.put("system", system);

            JSONObject misc = new JSONObject();
            jsonPut(misc, "poolCore", null, c.getPoolCore(), null, null);
            jsonPut(misc, "logbackEvents", null, c.getLogbackEvents(), null, null);
            jsonPut(misc, "logbackEventsError", null, c.getLogbackEventsError(), null, null);
            jsonPut(misc, "logbackEventsWarn", null, c.getLogbackEventsWarn(), null, null);
            jsonPut(misc, "garbageCollectSize", null, null, c.getGarbageCollectSize(), null);
            actuator.put("misc", misc);

        root.put("actuator", actuator);

        System.out.println(root.toString(2));
    } */
}
