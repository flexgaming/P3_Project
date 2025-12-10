package P3.Backend;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.json.JSONObject;


@RestController
@RequestMapping("/data") 
public class DataController {
Database database = new Database();

    
    @GetMapping
    public String getMessage() {
        return "DataController is up and running!\nSpecific Docker data can be accessed via /data/container/{id}";
    }

    
    @GetMapping("/regions") 
    public Map<String, Object> getAllRegions(){
        JSONObject regions = database.getRegions();
        return regions.toMap();
    }

    
    @GetMapping("/{regionID}/companies") 
    public Map<String, Object> getCompaniesByRegion(@PathVariable String regionID) {
        
        JSONObject companies = database.getCompanies(regionID);
        
        
        return companies.toMap();
    }

    
    @GetMapping("/{regionID}/{companyID}/contents") 
    public Map<String, Object> getRecentCompanyData(@PathVariable String regionID, @PathVariable String companyID) {
        
        
        JSONObject companyData = database.getRecentCompanyData(companyID);
        return companyData.toMap();
    }

    
    @GetMapping("/container/{id}") 
    public Map<String, Object> getContainerDiagnosticsById(@PathVariable String id) {
        
        JSONObject diagnostics = database.getDiagnosticsData(id, null);
        return diagnostics.toMap();
    }

    
    @PostMapping("/diagnosticsdata/{containerID}")
    public Map<String, Object> getDiagnosticsData(@PathVariable String containerID,
                                                  @RequestBody(required = false) Map<String, Object> payload) {
        
        String timeFrame = "10minutes";
        if (payload != null && payload.containsKey("timeFrame") && payload.get("timeFrame") != null) {
            timeFrame = payload.get("timeFrame").toString();
            
        }

        JSONObject diagnosticsData = new JSONObject();
        diagnosticsData.put("containerData", database.getContainerData(containerID));
        diagnosticsData.put("diagnosticsData", database.getDiagnosticsData(containerID, timeFrame));

        return diagnosticsData.toMap();
    }

    
    @GetMapping("/serverdata/{serverID}")
    public Map<String, Object> getServerData(@PathVariable String serverID) {
        JSONObject serverData = database.getServerData(serverID);

        return serverData.toMap();
    }

    
    @GetMapping("/dashboard") 
    public Map<String, Object> getDashboardData() {
        
        Map<String, Object> dashboardData = database.getDashboardData().toMap();
        return dashboardData;
    }

    
    @PostMapping("/errors") 
    public Map<String, Object> getCriticalErrorsData(@RequestBody Map<String, Object> payload) {
        String timeFrame = null;
        if (payload != null && payload.containsKey("timeFrame") && payload.get("timeFrame") != null) {
            timeFrame = payload.get("timeFrame").toString();
            
        }
        
        Map<String, Object> diagnosticsErrors = database.getDiagnosticsErrors(timeFrame).toMap();
        
        SeverityCalculator severityCalculator = new SeverityCalculator();
        
        
        return severityCalculator.assessSeverity(diagnosticsErrors);
    }
}