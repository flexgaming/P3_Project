package P3.Backend;

import P3.Backend.DTO.ContainerStatsDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api")
public class ExternalController {

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/upload-json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadJson(@RequestBody String json) {
        try {
            List<ContainerStatsDTO> statsList;
            if (json.trim().startsWith("[")) {
                statsList = objectMapper.readValue(json, new TypeReference<List<ContainerStatsDTO>>() {});
            } else {
                ContainerStatsDTO single = objectMapper.readValue(json, ContainerStatsDTO.class);
                statsList = List.of(single);
            }
            JSONArray data = new JSONArray(statsList);

            // Print container name and id for each container DTO to console
            statsList.forEach(dto -> System.out.println("Container name: " + dto.getContainerName() + ", container id: " + dto.getContainerId()));
            System.out.println(data.toString(2));
            

            return ResponseEntity.ok("Received " + statsList.size() + " container(s). Check console for details.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing JSON: " + e.getMessage());
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