package P3.Backend;

//import P3.Backend.DTO.ContainerClass;

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
            System.out.println("CONTAINER DATA !!!!!!!!!!!!");
            System.out.println(containerData.toString(4));

            // Prepare the database.
            Database database = new Database();

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
            JSONObject errorLogs = null;

            database.addDiagnosticsBatch(containerReference, running, ramUsage, systemRamUsage, cpuUsage,
                    systemCpuUsage, diskUsage, systemDiskUsage, threadCount, status, errorLogs);
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
}