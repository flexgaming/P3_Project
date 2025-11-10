package P3.Backend;

import P3.Backend.Database.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/data") //Router starting point
public class DataController {

    // Standard message endpoint
    @GetMapping
    public String getMessage() {
        return "DataController is up and running!\nSpecific Docker data can be accessed via /data/{id}";
    }

    // GET Docker by ID
    @GetMapping("/{id}") //Router continuation
    public ArrayList getDockerById(@PathVariable String id) {
        // In a real app, fetch from DB
        Database database = new Database();
        Container dockerTst = new Container(id);
        Container testData = database.getDiagnosticsData(dockerTst);
        return testData.getDiagnosticsData();
    }
}
