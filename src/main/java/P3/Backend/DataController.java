package P3.Backend;

import P3.Backend.Database.*;

import org.apache.tomcat.util.json.JSONParser;
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
        return "DataController is up and running!\nSpecific Docker data can be accessed via /data/container/{id}";
    }

    // GET Docker by ID
    @GetMapping("/container/{id}") //Router continuation
    public ArrayList getDockerById(@PathVariable String id) {
        // Get docker container diagnostics data from DB
        Database database = new Database();
        Container dockerTst = new Container(id);
        Container testData = database.getDiagnosticsData(dockerTst);
        return testData.getDiagnosticsData();
    }

    // GET server by ID
    @GetMapping("/server/{id}") //Router continuation
    public ArrayList getServerById(@PathVariable String id) {
        // Get server data from DB
        Database database = new Database();
        return null; // Placeholder for actual server data retrieval
    }

    // GET companies by RegionId
    @GetMapping("/regions/{id}") //Router continuation
    public ArrayList getCompaniesByRegion(@PathVariable Integer id) {
        // Get companies by region from DB
        Database database = new Database();
        return database.getCompanies(id);
    }


    @GetMapping("/regions")
    public ArrayList getAllRegions(){
        Database database = new Database();
        System.out.println(database.getRegionsTemp());
        return database.getRegionsTemp();
    }
    
}
