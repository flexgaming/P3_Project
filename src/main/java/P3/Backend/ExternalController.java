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
            String containerReference = containerData.getString("containerId");
            boolean running = containerData.getBoolean("containerRunning");
            Double ramUsage = containerData.getDouble("containerRamUsage");
            Double cpuUsage = containerData.getDouble("containerCpuPercent");
            Double systemCpuUsage = containerData.getDouble("systemCpuUsagePerc");
            Double diskUsage = containerData.getDouble("containerDiskUsage");
            int threadCount = containerData.getInt("jvmthreads");
            String status = containerData.getString("containerStatus");
            JSONObject errorLogs = null;

            database.addDiagnosticsBatch(containerReference, running, ramUsage, cpuUsage, systemCpuUsage, diskUsage,
                    threadCount, status, errorLogs);
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