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

            // Choice only necessary data to send to database.
            JSONObject finalData = storeFinalData(container);

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

/** This function is used to store the final parameters that would go in the database.
 *
 * @param container Is used to get all of the data from within the container.
 * @return Is the json that the final data, that is going to be in the database, is stored in.
 */
private static JSONObject storeFinalData(ContainerClass container) {
    // Prepare the json for the database.
    JSONObject json = new JSONObject();

    // Meta Data
    json.put("containerName", container.getContainerName());
    json.put("containerId", container.getContainerId());
    //json.put("publicPort", container.getPublicPort());
    //json.put("containerInterval", container.getContainerInterval());
    json.put("companyRegion", container.getCompanyRegion());
    json.put("companyName", container.getCompanyName());
    json.put("companyServer", container.getCompanyServer());

    // Docker Data
    json.put("containerRunning", container.getContainerRunning());
    json.put("containerRamUsage", container.getContainerRamUsage());
    json.put("containerCpuUsage", container.getContainerCpuUsage());
    json.put("containerDiskUsage", container.getContainerDiskUsage());
    json.put("containerStatus", container.getContainerStatus());
    json.put("containerPid", container.getContainerPid());
    json.put("containerExitCode", container.getContainerExitCode());

    // Be sure that the container and JVM are running before storing actuator data.
    if (container.getContainerRunning().equals(true) && container.getJVMRunning().equals(true)) {
        // Actuator Data
        json.put("JVMRunning", container.getJVMRunning());
        json.put("JVMRamMax", container.getJVMRamMax());
        json.put("JVMRamUsage", container.getJVMRamUsage());
        json.put("JVMCpuUsagePerc", container.getJVMCpuUsagePerc());
        //json.put("JVMThreads", container.getJVMThreads());
        //json.put("JVMThreadsStates", container.getJVMThreadsStates());
        //json.put("JVMThreadQueued", container.getJVMThreadQueued());
        json.put("JVMCpuUsageStart", container.getJVMCpuUsageStart());
        json.put("JVMUptime", container.getJVMUptime());

        // System Data
        json.put("systemRamUsage", container.getSystemRamUsage());
        json.put("systemCpuUsagePerc", container.getSystemCpuUsagePerc());
        json.put("systemCpuCores", container.getSystemCpuCores());
        json.put("systemDiskUsage", container.getSystemDiskUsage());
        json.put("systemDiskTotal", container.getSystemDiskTotal());
        json.put("systemDiskFree", container.getSystemDiskFree());

        // Misc Data
        json.put("timestamp", container.getTimestamp());
        //json.put("poolCore", container.getPoolCore());
        //json.put("logbackEvents", container.getLogbackEvents());
        json.put("logbackEventsError", container.getLogbackEventsError());
        json.put("logbackEventsWarn", container.getLogbackEventsWarn());
        //json.put("garbageCollectSize", container.getGarbageCollectSize());
    }
    // Return the result of all of the final parameters sent to the database.
    return json;
}


}