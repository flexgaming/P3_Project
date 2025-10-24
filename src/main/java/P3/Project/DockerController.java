package P3.Project;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/dockers")
public class DockerController {

    // GET all Dockers
    @GetMapping
    public List<Docker> getAllDockers() {
        return Arrays.asList(
            new Docker("1L", "Alice"),
            new Docker("2L", "Bob")
        );
    }

    // GET Docker by ID
    @GetMapping("/{id}")
    public Docker getDockerById(@PathVariable String id) {
        return new Docker(id, "Docker " + id);
    }

    // POST new Docker
    @PostMapping
    public Docker createDocker(@RequestBody Docker docker) {
        // In a real app, save to DB
        docker.setId("3L"); // Simulate ID generation
        return docker;
    }
}