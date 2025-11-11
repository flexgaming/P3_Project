package P3.Backend;

import P3.Backend.Database.*;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import org.json.JSONObject;


@RestController
@RequestMapping("/data") //Router starting point
public class DataController {

    // Standard message endpoint
    @GetMapping
    public String getMessage() {
        return "DataController is up and running!\nSpecific Docker data can be accessed via /data/container/{id}";
    }

    // GET all regions
    @GetMapping("/regions") //Router continuation
    public Map<String, Object> getAllRegions(){
        Database database = new Database();
        JSONObject regions = database.getRegions();
        return regions.toMap();
    }

    // GET companies by region ID
    @GetMapping("/{regionID}/companies") //Router continuation
    public Map<String, Object> getCompaniesByRegion(@PathVariable String regionID) {
        // Get companies by region from DB
        Database database = new Database();
        JSONObject companies = database.getCompanies(regionID);
        System.out.println(companies); // Debug print
        System.out.println(companies.toMap());
        return companies.toMap();
    }

    // GET company Servers, Dockers and latest Diagnostics by company ID
    @GetMapping("/{region}/{companyID}/contents") //Router continuation
    public ArrayList getCompanyContentsByID(@PathVariable String region, @PathVariable String companyID) {
        // Get company contents from DB
        Database database = new Database();
        return database.getCompanyContents(region, companyID);
    }

    // GET Container by ID
    @GetMapping("/container/{id}") //Router continuation
    public ArrayList getContainerById(@PathVariable String id) {
        // Get container diagnostics data from DB
        Database database = new Database();
        Container container = database.getDiagnosticsData(new Container(id));
        return container.getDiagnosticsData();
    }

    // GET Dashboard data
    @GetMapping("/dashboard") //Router continuation
    public ArrayList getDashboardData() {
        // Get dashboard data from DB
        Database database = new Database();
        return null; //database.getDashboardData();
    }

    // GET Critical Errors data
    @GetMapping("/errors") //Router continuation
    public Map<String, Object> getCriticalErrorsData() {
        // Get critical errors data from DB
        Database database = new Database();
        JSONObject diagnosticsErrors = database.getDiagnosticsErrors();
        return diagnosticsErrors.toMap();
    }
}