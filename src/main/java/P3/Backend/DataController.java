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
Database database = new Database();

    // Standard message endpoint
    @GetMapping
    public String getMessage() {
        return "DataController is up and running!\nSpecific Docker data can be accessed via /data/container/{id}";
    }

    // GET all regions
    @GetMapping("/regions") //Router continuation
    public Map<String, Object> getAllRegions(){
        JSONObject regions = database.getRegions();
        return regions.toMap();
    }

    // GET companies by region ID
    @GetMapping("/{regionID}/companies") //Router continuation
    public Map<String, Object> getCompaniesByRegion(@PathVariable String regionID) {
        // Get companies by region from DB
        JSONObject companies = database.getCompanies(regionID);
        // System.out.println(companies); // Debug print
        // System.out.println(companies.toMap());
        return companies.toMap();
    }

    // GET company Servers, Dockers and latest Diagnostics by company ID
    @GetMapping("/{region}/{companyID}/contents") //Router continuation
    public Map<String, Object> getRecentCompanyData(@PathVariable String region, @PathVariable String companyID) {
        // Get company contents from DB
        System.out.println(database.getRecentCompanyData(companyID).toString(4));
        JSONObject companyData = database.getRecentCompanyData(companyID);
        return companyData.toMap();
    }

    // GET Container by ID
    @GetMapping("/container/{id}") //Router continuation
    public Map<String, Object> getContainerDiagnosticsById(@PathVariable String id) {
        // Get container diagnostics data from DB
        JSONObject diagnostics = database.getDiagnosticsData(new Container(id));
        return diagnostics.toMap();
    }

    // GET Diagnostics data by container ID
    @GetMapping("/diagnosticsdata/{containerID}")
    public Map<String, Object> getDiagnosticsData(@PathVariable String containerID) {
        System.out.println("I am a man");
        JSONObject diagnosticsData = database.getDiagnosticsData(containerID);

        return diagnosticsData.toMap();
    }

    // GET Dashboard data
    @GetMapping("/dashboard") //Router continuation
    public ArrayList getDashboardData() {
        // Get dashboard data from DB
        return null; //database.getDashboardData();
    }

    // GET Critical Errors data
    @GetMapping("/errors") //Router continuation
    public Map<String, Object> getCriticalErrorsData() {
        // Get critical errors data from DB
        Database database = new Database();
        Map<String, Object> diagnosticsErrors = database.getDiagnosticsErrors().toMap();
        
        // Check if timestamp exists before processing
        /* Object timestampObj = diagnosticsErrors.get("timestamp");
        if (timestampObj != null) {
            String tmstmp = timestampObj.toString();
            diagnosticsErrors.put("timestamp", tmstmp);
            String[] timesStamp = TimeUtils.splitIsoToLocalDateAndTime(tmstmp);
            diagnosticsErrors.put("date", timesStamp[0]);
            diagnosticsErrors.put("time", timesStamp[1]);
        } else {
            System.out.println("Timestamp: " + diagnosticsErrors.get("timestamp"));
            System.out.println("date: " + null);
            System.out.println("time: " + null);
        } */
        
        return diagnosticsErrors;
    }
}