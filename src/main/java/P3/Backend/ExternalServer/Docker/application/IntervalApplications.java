package P3.Backend.ExternalServer.Docker.application;

import static P3.Backend.ExternalServer.Docker.Persistent.*;
import P3.Backend.ExternalServer.Docker.classes.*;
import P3.Backend.ExternalServer.Docker.manager.DockerStatsService;
import P3.Backend.ExternalServer.Docker.manager.DockerStatsService.ContainerStats;
import P3.Backend.ExternalServer.Docker.manager.WebClientPost;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;

import P3.Backend.ExternalServer.LogFetcher;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

@Component
public class IntervalApplications {
    
    
    private static final Path containerListPath = Path.of(CURRENT_CONTAINER_PATH + CONTAINER_NAME);

    
    private static final Path companyInfoPath = Path.of(CURRENT_CONTAINER_PATH + SERVER_INFO);

    private static ContainerClass[] containerArr;
    private static ContainerClass[] containerArrTemp;
    private static IntervalClass[] intervalArr;

    private static String regionName;
    private static String companyName;
    private static String serverName;

    
    public static void Initiation(DockerClient dockerClient, WebClient webClient, Scanner scanner) {

        
        SetupContainerArray();
        
        
        SetupIntervalArray(containerArr);

        
        AtomicBoolean userInputDetected = new AtomicBoolean(false);

        
        checkUserInputThread(userInputDetected, scanner);

        
        checkIntervalScheduler(userInputDetected, dockerClient, webClient);
    }

    
    private static void SetupContainerArray() { 
        try {
            
            String content = Files.readString(containerListPath);
            
            
            JSONObject JSONFileObj = new JSONObject(content);

            
            String info = Files.readString(companyInfoPath);

            
            JSONObject companyInfoObj = new JSONObject(info);
            
            
            int activeContainerCount = 0;
            for (String key : JSONFileObj.keySet()) {
                JSONObject tempContainer = JSONFileObj.getJSONObject(key);
                if (tempContainer.getString("state").equals("inactive")) continue;
                if (tempContainer.getString("state").equals("unconfigured")) continue;
                activeContainerCount++;
            }

            
            containerArr = new ContainerClass[activeContainerCount];
            containerArrTemp = new ContainerClass[activeContainerCount];

            
            regionName = companyInfoObj.getString("region");
            companyName = companyInfoObj.getString("company");
            serverName = companyInfoObj.getString("server");
    
            int index = 0;
            for (String key : JSONFileObj.keySet()) { 
                JSONObject tempContainer = JSONFileObj.getJSONObject(key); 
                
                
                if (tempContainer.getString("state").equals("inactive")) continue;
                if (tempContainer.getString("state").equals("unconfigured")) continue;

                
                String name = tempContainer.getString("name");
                String id = tempContainer.getString("id");
                Integer interval = tempContainer.getInt("interval");
                Integer publicPort = tempContainer.getInt("publicPort");
                
                
                ContainerClass newContainer = new ContainerClass(name, id, interval, publicPort, 
                                                                    regionName, companyName, serverName);
                containerArr[index] = newContainer;
    
                index++; 
            }
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }

    
    private static void SetupIntervalArray(ContainerClass[] containerArr) {
        
        intervalArr = new IntervalClass[containerArr.length];

        
        for (int i = 0; i < containerArr.length; i++) {

            
            String id = containerArr[i].getContainerId();
            Integer interval = containerArr[i].getContainerInterval();

            
            
            IntervalClass newIntervalElement = new IntervalClass(id, interval, interval);
            intervalArr[i] = newIntervalElement;
        }
    }

    
    private static void checkUserInputThread(AtomicBoolean bool, Scanner scanner) {

        
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    if (scanner.hasNextLine()) {
                        scanner.nextLine(); 
                        bool.set(true); 
                        break;
                    }
                    Thread.sleep(100); 
                }

            } catch (Exception e) {
                
            }
        });

        
        thread.setDaemon(true);

        
        thread.start();
    }

    
    private static void checkIntervalScheduler(AtomicBoolean bool, DockerClient dockerClient, WebClient webClient) {
        int heartbeatTimer = DEFAULT_HEARTBEAT_TIME;

        while (!bool.get()) {
            try {
                
                
                for (int i = 0; i < intervalArr.length; i++) {
                    Integer newInterval = intervalArr[i].getTempInterval() - 1; 
                    
                    if (newInterval == 0) {
                        newInterval = intervalArr[i].getInterval(); 
                        intervalArr[i].setTempInterval(newInterval);

                        
                        fetchAllContainerInformation(containerArr[i], dockerClient, webClient);
                        
                        
                        containerArr[i].setTimestamp(new Date().getTime());

                        
                        try {
                            containerArr[i].setContainerCpuPercent(containerArrTemp[i]);
                            WebClientPost.sendData(webClient, containerArr[i], BACKEND_SERVER_URL + "/upload-json");
                            containerArrTemp[i] = (ContainerClass) containerArr[i].clone();

                            System.out.println("JSON data has been sent from: " + containerArr[i].getContainerName());
                        } catch (Exception e) {
                            
                            System.err.println("Failed to POST container data: " + e.getMessage());
                        }
                        
                    } else {
                        intervalArr[i].setTempInterval(newInterval); 
                    }
                }

                heartbeatTimer--;

                
                if (heartbeatTimer == 0) {
                    heartbeatTimer = DEFAULT_HEARTBEAT_TIME;
                    Map<String, String> requestBody = new HashMap<>();
                    requestBody.put("region", regionName);
                    requestBody.put("company", companyName);
                    requestBody.put("server", serverName);
                    WebClientPost.sendData(webClient, requestBody, BACKEND_SERVER_URL + "/heartbeat");
                }

                
                Thread.sleep(1000);
                
            } catch (Exception e) {
                
                e.printStackTrace();
            }
        }
    }

    
    private static void fetchAllContainerInformation(ContainerClass container, DockerClient dockerClient, WebClient webClient) {
        
        
        fetchDockerStats(container, dockerClient);
        
        
        if (container.getContainerRunning().equals(true)) {

            
            String actuatorUrl = SPRING_ACTUATOR_DEFAULT_ENDPOINT + ":" + container.getPublicPort();

            
            String actuatorStatus = getActuatorRunningStatus(callActuator(webClient, actuatorUrl, "/actuator/health"));
            
            
            if (actuatorStatus != null && actuatorStatus.equals("UP")) {
                fetchSpringActuatorStats(container, webClient, actuatorUrl);
            } else {

                
                container.setJVMRunning(false);

                
                System.out.println("Spring Actuator endpoint not reachable for " + container.getContainerName());
            }
        } else {
            
            System.out.println("Container " + container.getContainerName() + " is not running, skipping Spring Actuator stats fetch.");
        }
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
                    
            
            container.setContainerRamUsage(stats.getMemoryUsage()); 
            container.setContainerCpuUsage(stats.getCpuTotalUsage()); 
            container.setSystemCpuUsage(stats.getSystemCpuTotalUsage()); 
            container.setContainerDiskUsage(response.getSizeRootFs());  
            container.setContainerRunning(response.getState().getRunning()); 
            container.setContainerStatus(response.getState().getStatus()); 
            container.setContainerPid(response.getState().getPidLong()); 
            container.setContainerCpuCount(stats.getCpuCount()); 
            container.setContainerRamLimit(stats.getMemoryLimit()); 

            
            LogFetcher logFetcher = new LogFetcher();
            JSONObject logsJson = logFetcher.fetch(dockerClient, container.getContainerInterval(), container.getContainerId());
            LogsClass logsInfo = toLogsInfo(logsJson);
            container.setLogs(logsInfo);

            
            if (container.getContainerRunning().equals(false))
                container.setContainerExitCode(response.getState().getExitCodeLong()); 
            else 
                container.setContainerExitCode(null); 
            
        } catch (Exception e) {
            container.setContainerRunning(false); 
            container.setContainerStatus("exited"); 
        }
    }

    
    private static void fetchSpringActuatorStats(ContainerClass container, WebClient webClient, String url) {

        
        
        container.setJVMRunning(true);
        container.setJVMRamMax(getDoubleSafe(callActuator(webClient, url, "/actuator/metrics/jvm.memory.max")));
        container.setJVMRamUsage(getDoubleSafe(callActuator(webClient, url, "/actuator/metrics/jvm.memory.used")));
        container.setJVMCpuUsageStart(getDoubleSafe(callActuator(webClient, url, "/actuator/metrics/process.start.time")));
        container.setJVMCpuUsagePerc(getDoubleSafe(callActuator(webClient, url, "/actuator/metrics/process.cpu.usage")));
        container.setJVMThreads(getIntSafe(callActuator(webClient, url, "/actuator/metrics/jvm.threads.live")));
        container.setJVMThreadsStates(getIntSafe(callActuator(webClient, url, "/actuator/metrics/jvm.threads.states")));
        container.setJVMThreadQueued(getIntSafe(callActuator(webClient, url, "/actuator/metrics/executor.queued")));
        container.setJVMUptime(getDoubleSafe(callActuator(webClient, url, "/actuator/metrics/process.uptime")));

        
        container.setSystemDiskFree(getLongSafe(callActuator(webClient, url, "/actuator/metrics/disk.free")));
        container.setSystemDiskTotal(getLongSafe(callActuator(webClient, url, "/actuator/metrics/disk.total")));
        container.setSystemDiskUsage(container.getSystemDiskTotal() - container.getSystemDiskFree());
        
        
        container.setSystemCpuCores(getIntSafe(callActuator(webClient, url, "/actuator/metrics/system.cpu.count")));
        container.setSystemCpuUsagePerc(getDoubleSafe(callActuator(webClient, url, "/actuator/metrics/system.cpu.usage")));

        
        container.setPoolCore(getIntSafe(callActuator(webClient, url, "/actuator/metrics/executor.pool.core")));
        container.setLogbackEvents(getIntSafe(callActuator(webClient, url, "/actuator/metrics/logback.events")));
        container.setLogbackEventsError(getIntSafe(callActuator(webClient, url, "/actuator/metrics/logback.events?tag=level:error")));
        container.setLogbackEventsWarn(getIntSafe(callActuator(webClient, url, "/actuator/metrics/logback.events?tag=level:warn")));
        container.setGarbageCollectSize(getLongSafe(callActuator(webClient, url, "/actuator/metrics/jvm.gc.overhead")));

        
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        long systemTotalMemory = osBean.getTotalPhysicalMemorySize();
        long systemFreeMemory = osBean.getFreePhysicalMemorySize();
        long systemUsedMemory = systemTotalMemory - systemFreeMemory;

        container.setSystemRamUsage(systemUsedMemory);
        container.setSystemRamTotal(systemTotalMemory);
    }

    
    private static JSONObject callActuator(WebClient webClient, String url, String endpoint) {
        
        try {
            String jsonString = webClient.get()
                    .uri(url + endpoint)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
    
            
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

    
    private static Double getDoubleSafe(JSONObject json) {
        try {
            return json
                    .getJSONArray("measurements")
                    .getJSONObject(0)
                    .getDouble("value");

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

    
    private static String getActuatorRunningStatus(JSONObject json) {
        try {
            return json
                .getString("status");

        } catch (Exception e) {
            return null;
        }
    }

    

    private static LogsClass toLogsInfo(JSONObject json) {
        LogsClass info = new LogsClass();
        
        if (json == null) return info;
        info.setError(jsonArrayToList(json.optJSONArray("Error")));
        info.setWarn(jsonArrayToList(json.optJSONArray("Warn")));
        
        return info;
    }

    

    private static List<String> jsonArrayToList(JSONArray arr) {
        List<String> list = new ArrayList<>();
        
        if (arr == null) return list;
        for (int i = 0; i < arr.length(); i++) {
            try {
                
                list.add(arr.getString(i));
            } catch (Exception ignore) {
                
            }
        }
        return list;
    }
}