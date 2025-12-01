package P3.Backend;

import P3.Backend.DTO.ContainerClass;
import P3.Backend.Database.*;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class ExternalController {

    @PostMapping(value = "/upload-json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void uploadJson(@RequestBody String json) {
        try {
            JSONObject containerData = new JSONObject(json);

            // Store all of the elements from the json in the ContainerClass.
            ContainerClass container  = storeContainerData(containerData);

            // Prepare the database.
            Database database = new Database();

            // Send data
                // make function for sending all of the data.


        } catch (Exception e) {
            e.printStackTrace();
            
        }
    }

    @PostMapping(value = "/startup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadStartup(@RequestBody String json) {
        try {
            System.out.println("Startup JSON received:\n" + json);
            return ResponseEntity.ok("Startup JSON received. Check console for details.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error reading JSON");
        }
    }

/** This function is used to store all of the data, within the json, that has been recieved from external servers.
 * 
 * @param jsonData Is used to get each of the elements within the json and store them within the containerClass.
 * @return Is the ContainerClass that all of the data is being store in.
 */
private ContainerClass storeContainerData(JSONObject jsonData) { 

    ContainerClass container = new ContainerClass();

    Boolean containerRunning = jsonData.getBoolean("containerRunning");
    Boolean JVMRunning = jsonData.getBoolean("JVMRunning");

    // Go through each of the keys within the json.
    for (String key : jsonData.keySet()) {
        String value = jsonData.get(key).toString();
        
        // Set each respective value from the json file into the container.
        switch (key) {
            // Data from JSON:
            case "containerName":
                container.setContainerName(value);
                break;
            case "containerId":
                container.setContainerId(value);
                break;
            case "publicPort":
                container.setPublicPort(jsonData.getJSONArray(key).getInt(0));
                break;
            case "containerInterval":
                container.setContainerInterval(Integer.parseInt(value));
                break;
            case "companyRegion":
                container.setCompanyRegion(value);
                break;
            case "companyName":
                container.setCompanyName(value);
                break;
            case "companyServer":
                container.setCompanyServer(value);
                break;

            // Data from docker:
            case "containerRunning":
                container.setContainerRunning(Boolean.parseBoolean(value));
                break;
            case "containerRamUsage":
                container.setContainerRamUsage(Long.parseLong(value));
                break;
            case "containerCpuUsage":
                container.setContainerCpuUsage(Long.parseLong(value));
                break;
            case "containerDiskUsage":
                container.setContainerDiskUsage(Long.parseLong(value));
                break;
            case "containerStatus":
                container.setContainerStatus(value);
                break;
            case "containerPid":
                container.setContainerPid(Long.parseLong(value));
                break;
            case "containerExitCode":
                container.setContainerExitCode(Long.parseLong(value));
                break;
            default:
                System.out.println("Unknown field: " + key);
        }
        if (containerRunning.equals(true) && JVMRunning.equals(true)) {
            switch (key) {
                // Data from actuator:
                //JVM Data    
                case "JVMRunning":
                    container.setJVMRunning(Boolean.parseBoolean(value));
                    break;
                case "JVMRamMax":
                    container.setJVMRamMax(Long.parseLong(value));
                    break;
                case "JVMRamUsage":
                    container.setJVMRamUsage(Long.parseLong(value));
                    break;
                case "JVMCpuUsagePerc":
                    container.setJVMCpuUsagePerc(Long.parseLong(value));
                    break;
                case "JVMThreads":
                    container.setJVMThreads(Integer.parseInt(value));
                    break;
                case "JVMThreadsStates":
                    container.setJVMThreadsStates(Integer.parseInt(value));
                    break;
                case "JVMThreadQueued":
                    container.setJVMThreadQueued(Integer.parseInt(value));
                    break;
                case "JVMCpuUsageStart":
                    container.setJVMCpuUsageStart(Long.parseLong(value));
                    break;
                case "JVMUptime":
                    container.setJVMUptime(Long.parseLong(value));
                    break;
    
                //System Data    
                case "systemRamUsage":
                    container.setSystemRamUsage(Long.parseLong(value));
                    break;
                case "systemCpuUsagePerc":
                    container.setSystemCpuUsagePerc(Long.parseLong(value));
                    break;
                case "systemCpuCores":
                    container.setSystemCpuCores(Integer.parseInt(value));
                    break;
                case "systemDiskUsage":
                    container.setSystemDiskUsage(Long.parseLong(value));
                    break;
                case "systemDiskTotal":
                    container.setSystemDiskTotal(Long.parseLong(value));
                    break;
                case "systemDiskFree":
                    container.setSystemDiskFree(Long.parseLong(value));
                    break;
    
                case "timestamp":
                    container.setTimestamp(Long.parseLong(value));
                // Misc Data
                    break;
                case "poolCore":
                    container.setPoolCore(Integer.parseInt(value));
                    break;
                case "logbackEvents":
                    container.setLogbackEvents(Integer.parseInt(value));
                    break;
                case "logbackEventsError":
                    container.setLogbackEventsError(Integer.parseInt(value));
                    break;
                case "logbackEventsWarn":
                    container.setLogbackEventsWarn(Integer.parseInt(value));
                    break;
                case "garbageCollectSize":
                    container.setGarbageCollectSize(Long.parseLong(value));
                    break;
                default:
                    System.out.println("Unknown field: " + key);
                    break;
            }
        }
    }
    // Return the container with everything extracted.
    return container;
}
    
}