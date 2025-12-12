package P3.Backend;

//import P3.Backend.DTO.ContainerClass;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class ExternalController {

    @PostMapping(value = "/upload-json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void uploadJson(@RequestBody String json) {
        try {
            JSONObject containerData = new JSONObject(json);
            /* System.out.println("CONTAINER DATA --------------------");
            System.out.println(containerData.toString(4)); */

            // Prepare the database.
            Database database = new Database();

            // Check if region, company and server exists in the database.
            String regionName = containerData.getString("region");
            String companyName = containerData.getString("company");
            String serverName = containerData.getString("server");
            String containerID = containerData.getString("containerId");
            String containerName = containerData.getString("containerName");

            database.addRegions(regionName);

            JSONObject regions = database.getRegions();
            String regionID = regions.getJSONObject(regionName).getString("regionID");

            database.addCompanies(regionID, companyName);

            JSONObject companies = database.getCompanies(regionID);
            String companyID = companies.getJSONObject(companyName).getString("companyID");

            database.addServers(companyID, serverName);

            JSONObject servers = database.getServers(companyID);
            String serverID = servers.getJSONObject(serverName).getString("serverID");

            database.addContainers(containerID, serverID, containerName);

            // Send data
            String containerReference = containerData.isNull("containerId")
                    ? null : containerData.getString("containerId");
            boolean running = containerData.isNull("containerRunning")
                    ? null : containerData.getBoolean("containerRunning");
            Long ramUsage = containerData.isNull("containerRamUsage")
                    ? null : containerData.getLong("containerRamUsage");
            Long systemRamUsage = containerData.isNull("systemRamUsage")
                    ? null : containerData.getLong("systemRamUsage");
            Double cpuUsage = containerData.isNull("containerCpuPercent")
                    ? null : containerData.getDouble("containerCpuPercent");
            Double systemCpuUsage = containerData.isNull("systemCpuUsagePerc")
                    ? null : containerData.getDouble("systemCpuUsagePerc");
            Long diskUsage = containerData.isNull("containerDiskUsage")
                    ? null : containerData.getLong("containerDiskUsage");
            Long systemDiskUsage = containerData.isNull("systemDiskUsage")
                    ? null : containerData.getLong("systemDiskUsage");
            int threadCount = containerData.isNull("jvmthreads")
                    ? null : containerData.getInt("jvmthreads");
            String status = containerData.isNull("containerStatus")
                    ? null : containerData.getString("containerStatus");

            JSONObject tempErrorLogs = containerData.getJSONObject("logs");
            JSONArray warnings = tempErrorLogs.getJSONArray("warn");
            JSONArray errors = tempErrorLogs.getJSONArray("error");
            JSONObject errorLogs = new JSONObject();
            if (!warnings.isEmpty()) errorLogs.put("warnings", warnings);
            if (!errors.isEmpty()) errorLogs.put("errors", errors);
            if (errorLogs.isEmpty()) errorLogs = null;

            database.addDiagnosticsBatch(containerReference, running, ramUsage, systemRamUsage, cpuUsage,
                    systemCpuUsage, diskUsage, systemDiskUsage, threadCount, status, errorLogs);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @PostMapping(value = "/heartbeat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void heartbeatController(@RequestBody String json) {
        JSONObject serverData = new JSONObject(json);
        /* System.out.println("HEARTBEAT DATA --------------------");
        System.out.println(serverData.toString(4)); */

        // Prepare the database.
        Database database = new Database();

        // Get serverID.
        String regionName = serverData.getString("region");
        String companyName = serverData.getString("company");
        String serverName = serverData.getString("server");

        JSONObject regions = database.getRegions();
        String regionID = regions.getJSONObject(regionName).getString("regionID");

        JSONObject companies = database.getCompanies(regionID);
        String companyID = companies.getJSONObject(companyName).getString("companyID");

        JSONObject servers = database.getServers(companyID);
        String serverID = servers.getJSONObject(serverName).getString("serverID");

        // Update the latest ping in the database.
        database.pingServer(serverID);
    }
}