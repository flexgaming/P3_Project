package P3.Backend;

import P3.Backend.Database.*;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/dockers") //Router starting point
public class DockerController {

    // GET all Dockers
    @GetMapping
    public List<Docker> getAllDockers() {
        // In a real app, fetch from DB
        return Arrays.asList(
            new Docker("1L", "Alice"),
            new Docker("2L", "Bob")
        );
    }

    // GET Docker by ID
    @GetMapping("/{id}") //Router continuation
    public JSONObject getDockerById(@PathVariable String id) {
        // In a real app, fetch from DB
        Database database = new Database();
        JSONObject testData = database.getDiagnosticsData(new Container(id));
        return testData;
    }

    // POST new Docker
    @PostMapping
    public Docker createDocker(@RequestBody Docker docker) {
        // In a real app, save to DB
        docker.setId("3L"); // Simulate ID generation
        return docker;
    }
}