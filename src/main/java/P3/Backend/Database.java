package P3.Backend;

import java.lang.reflect.Array;
import java.sql.*;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.util.PGobject;

import static java.util.Objects.isNull;

public class Database {
    
    String url = Constants.DATABASE_URL;
    String user = Constants.DATABASE_USER;
    String password = Constants.DATABASE_PASSWORD;

    private void errorHandling(Throwable error) {
        error.printStackTrace();
    }

    
    public static boolean notSameLength(Object... arrays) {
        
        if (arrays.length == 0)
            return false;

        
        int expectedLength = Array.getLength(arrays[0]);

        
        for (Object arr : arrays) {
            if (!arr.getClass().isArray() || Array.getLength(arr) != expectedLength) {
                return true;
            }
        }
        return false;
    }

    
    private boolean regionExists(String regionName) {
        String sql = "SELECT (Region_ID) FROM Region WHERE Region_Name = ?";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, regionName);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException sqlException) {
            errorHandling(sqlException);

            return false;
        }
    }

    
    private boolean companyExists(String companyName, String regionReference) {
        String sql = "SELECT (Company_ID) FROM Company WHERE Company_Name = ? AND Region_Reference = ?";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, companyName);
            preparedStatement.setObject(2, UUID.fromString(regionReference));
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException sqlException) {
            errorHandling(sqlException);

            return false;
        }
    }

    
    private boolean serverExists(String serverName, String companyReference) {
        String sql = "SELECT (Server_ID) FROM Server WHERE Server_Name = ? AND Company_Reference = ?";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, serverName);
            preparedStatement.setObject(2, UUID.fromString(companyReference));
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException sqlException) {
            errorHandling(sqlException);

            return false;
        }
    }

    
    private boolean containerExists(String containerName, String serverReference) {
        String sql = "SELECT (Container_ID) FROM Container WHERE Container_Name = ? AND Server_Reference = ?";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, containerName);
            preparedStatement.setObject(2, UUID.fromString(serverReference));
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException sqlException) {
            errorHandling(sqlException);

            return false;
        }
    }

    
    public void addRegions(String regionName) {
        
        addRegions(new String[] { regionName });
    }

    
    public void addRegions(String[] regionNames) {
        String sql = "INSERT INTO Region (Region_Name) VALUES (?);";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            
            for (String regionName : regionNames) {
                
                if (regionExists(regionName)) continue;

                preparedStatement.setString(1, regionName);
                
                preparedStatement.addBatch();
            }

            
            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException sqlException) {
            errorHandling(sqlException);
        }
    }

    
    public void addCompanies(String regionReference, String companyName) {
        
        addCompanies(new String[]{regionReference}, new String[]{companyName});
    }

    
    public void addCompanies(String[] regionReferences, String[] companyNames) {
        
        if (notSameLength(regionReferences, companyNames)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Company (Region_Reference, Company_Name) VALUES (?, ?)";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            
            for (int i = 0; i < regionReferences.length; i++) {
                
                if (companyExists(companyNames[i], regionReferences[i])) continue;

                preparedStatement.setObject(1, UUID.fromString(regionReferences[i]));
                preparedStatement.setString(2, companyNames[i]);
                
                preparedStatement.addBatch();
            }

            
            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException error) {
            errorHandling(error);
        }
    }

    
    public void addServers(String companyReference, String serverName) {
        
        addServers(new String[]{companyReference}, new String[]{serverName});
    }

    
    public void addServers(String[] companyReferences, String[] serverNames) {
        
        if (notSameLength(companyReferences, serverNames)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Server (Company_Reference, Server_Name) VALUES (?, ?)";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            
            for (int i = 0; i < companyReferences.length; i++) {
                
                if (serverExists(serverNames[i], companyReferences[i])) continue;

                preparedStatement.setObject(1, UUID.fromString(companyReferences[i]));
                preparedStatement.setString(2, serverNames[i]);
                
                preparedStatement.addBatch();
            }

            
            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException error) {
            errorHandling(error);
        }
    }

    
    public void addContainers(String containerID, String serverReference, String containerName) {
        
        addContainers(new String[]{containerID}, new String[]{serverReference}, new String[]{containerName});
    }

    
    public void addContainers(String[] containerIDs, String[] serverReferences, String[] containerNames) {
        
        if (notSameLength(containerIDs, serverReferences)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Container (Container_ID, Server_Reference, Container_Name) VALUES (?, ?, ?)";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            
            for (int i = 0; i < serverReferences.length; i++) {
                
                if (containerExists(containerNames[i], serverReferences[i])) continue;

                preparedStatement.setString(1, containerIDs[i]);
                preparedStatement.setObject(2, UUID.fromString(serverReferences[i]));
                preparedStatement.setString(3, containerNames[i]);
                
                preparedStatement.addBatch();
            }

            
            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException error) {
            errorHandling(error);
        }
    }

    
    public void addDiagnosticsBatch(String containerReference, boolean running, Long ramUsage, Long systemRamUsage,
                                    Double cpuUsage, Double systemCpuUsage, Long diskUsage, Long systemDiskUsage,
                                    int threadCount, String status, JSONObject errorLogs) {
        
        addDiagnosticsBatch(new String[]{containerReference}, new boolean[]{running}, new Long[]{ramUsage},
                new Long[]{systemRamUsage}, new Double[]{cpuUsage}, new Double[]{systemCpuUsage},
                new Long[]{diskUsage}, new Long[]{systemDiskUsage}, new int[]{threadCount}, new String[]{status},
                new JSONObject[]{errorLogs});
    }

    
    public void addDiagnosticsBatch(String[] containerReferences, boolean[] runningList, Long[] ramUsages,
                                    Long[] systemRamUsages, Double[] cpuUsages, Double[] systemCpuUsages,
                                    Long[] diskUsages, Long[] systemDiskUsages, int[] threadCounts, String[] statuses,
                                    JSONObject[] errorLogsList) {
        
        if (notSameLength(containerReferences, runningList, ramUsages, systemRamUsages, cpuUsages, systemCpuUsages,
                diskUsages, systemDiskUsages, threadCounts, statuses, errorLogsList)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Diagnostics (Container_Reference, Timestamp, Running, Ram_Usage, System_Ram_Usage," +
                "CPU_Usage, System_CPU_Usage, Disk_Usage, System_Disk_Usage, Thread_Count, Status, Error_Logs) VALUES" +
                "(?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            
            for (int i = 0; i < containerReferences.length; i++) {
                preparedStatement.setString(1, containerReferences[i]);
                preparedStatement.setBoolean(2, runningList[i]);
                preparedStatement.setLong(3, ramUsages[i]);
                preparedStatement.setLong(4, systemRamUsages[i]);
                preparedStatement.setObject(5, cpuUsages[i], Types.DOUBLE); 
                preparedStatement.setDouble(6, systemCpuUsages[i]);
                preparedStatement.setLong(7, diskUsages[i]);
                preparedStatement.setLong(8, systemDiskUsages[i]);
                preparedStatement.setInt(9, threadCounts[i]);
                preparedStatement.setString(10, statuses[i]);

                PGobject jsonObject;
                if (isNull(errorLogsList[i])) {
                    jsonObject = null;
                } else {
                    jsonObject = new PGobject();
                    jsonObject.setType("json");
                    jsonObject.setValue(errorLogsList[i].toString());
                }

                preparedStatement.setObject(11, jsonObject);
                
                preparedStatement.addBatch();
            }

            
            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException error) {
            errorHandling(error);
        }
    }

    
    public JSONObject getRegions() {
        JSONObject regions = new JSONObject();
        String sql = "SELECT * FROM Region";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            
            while (resultSet.next()) {
                JSONObject region = new JSONObject();
                String regionID = resultSet.getString("Region_ID");
                String regionName = resultSet.getString("Region_Name");
                region.put("regionID", regionID);
                region.put("regionName", regionName);
                regions.put(regionName, region);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return regions;
    }

    
    public JSONObject getCompanies(String regionID) {
        JSONObject companies = new JSONObject();
        String sql = "SELECT * FROM Company WHERE Region_Reference = ?";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setObject(1, UUID.fromString(regionID));
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                JSONObject company = new JSONObject();
                String companyID = resultSet.getString("Company_ID");
                String regionReference = resultSet.getString("Region_Reference");
                String companyName = resultSet.getString("Company_Name");
                company.put("companyID", companyID);
                company.put("regionReference", regionReference);
                company.put("companyName", companyName);
                companies.put(companyName, company);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return companies;
    }

    
    public JSONObject getServers(String companyID) {
        JSONObject servers = new JSONObject();
        String sql = "SELECT * FROM Server WHERE Company_Reference = ?";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setObject(1, UUID.fromString(companyID));
            ResultSet resultSet = preparedStatement.executeQuery();

            
            while (resultSet.next()) {
                JSONObject server = new JSONObject();
                String serverID = resultSet.getString("Server_ID");
                String companyReference = resultSet.getString("Company_Reference");
                String serverName = resultSet.getString("Server_Name");
                Timestamp latestPing = resultSet.getTimestamp("Latest_Ping");
                server.put("serverID", serverID);
                server.put("companyReference", companyReference);
                server.put("serverName", serverName);
                server.put("latestPing", latestPing);
                servers.put(serverName, server);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return servers;
    }

    
    public JSONObject getContainers(String serverID) {
        JSONObject containers = new JSONObject();
        
        String sql = "SELECT * FROM Container WHERE Server_Reference = ?";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {;

            preparedStatement.setObject(1, UUID.fromString(serverID));
            ResultSet resultSet = preparedStatement.executeQuery();

            
            while (resultSet.next()) {
                JSONObject container = new JSONObject();
                String containerID = resultSet.getString("Container_ID");
                String serverReference = resultSet.getString("Server_Reference");
                String containerName = resultSet.getString("Container_Name");
                container.put("containerID", containerID);
                container.put("serverReference", serverReference);
                container.put("containerName", containerName);
                containers.put(containerName, container);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return containers;
    }

    
    public JSONObject getDiagnosticsData(String dockerID, String timeFrameString) {
        int timeFrameMinutes = Constants.DIAGNOSTICS_TIME_SCOPE;
        if (timeFrameString != null){
            
            timeFrameMinutes = HelperFunctions.getMinutesFromTimeFrame(timeFrameString);
            
        }
        String sql = "SELECT * FROM Diagnostics WHERE Container_Reference = ? AND Diagnostics.TimeStamp >= " +
                " NOW() - make_interval(mins => ?)";
        JSONObject diagnosticsData = new JSONObject();

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, dockerID);
            preparedStatement.setInt(2, timeFrameMinutes);
            ResultSet resultSet = preparedStatement.executeQuery();

            
            while (resultSet.next()) {
                JSONObject diagnostics = new JSONObject();
                String diagnosticsID = resultSet.getString("Diagnostics_ID");
                String containerReference = resultSet.getString("Container_Reference");
                Timestamp timestamp = resultSet.getTimestamp("Timestamp");
                boolean running = resultSet.getBoolean("Running");
                long ramUsage = resultSet.getLong("Ram_Usage");
                long systemRamUsage = resultSet.getLong("System_Ram_Usage");
                double cpuUsage = resultSet.getDouble("CPU_Usage");
                double systemCpuUsage = resultSet.getDouble("System_CPU_Usage");
                long diskUsage = resultSet.getLong("Disk_Usage");
                long systemDiskUsage = resultSet.getLong("System_Disk_Usage");
                int threadCount = resultSet.getInt("Thread_Count");
                String status = resultSet.getString("Status");
                String errorLogs = resultSet.getString("Error_Logs");
                diagnostics.put("containerReference", containerReference);
                diagnostics.put("timestamp", timestamp);
                diagnostics.put("running", running);
                diagnostics.put("ramUsage", ramUsage);
                diagnostics.put("systemRamUsage", systemRamUsage);
                diagnostics.put("cpuUsage", cpuUsage);
                diagnostics.put("systemCpuUsage", systemCpuUsage);
                diagnostics.put("diskUsage", diskUsage);
                diagnostics.put("systemDiskUsage", systemDiskUsage);
                diagnostics.put("threadCount", threadCount);
                diagnostics.put("status", status);
                diagnostics.put("errorLogs", errorLogs);
                diagnosticsData.put(diagnosticsID, diagnostics);
            }

        } catch (SQLException error) {
            errorHandling(error);
            return null;
        }
        return diagnosticsData;
    }

    
    public JSONObject getDiagnosticsErrors(String timeFrameString) {
        int timeFrameMinutes = Constants.DIAGNOSTICS_TIME_SCOPE;
        if (timeFrameString != null) {
            
            timeFrameMinutes = HelperFunctions.getMinutesFromTimeFrame(timeFrameString);
        }
        JSONObject diagnosticsErrors = new JSONObject();
        
        String sql = "SELECT * FROM Diagnostics_Errors WHERE diagnostics_errors.timestamp >= " +
                " NOW() - make_interval(mins => ?)";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, timeFrameMinutes);
            ResultSet resultSet = preparedStatement.executeQuery();

            
            while (resultSet.next()) {
                JSONObject diagnosticsError = new JSONObject();
                String regionID = resultSet.getString("Region_ID");
                String regionName = resultSet.getString("Region_Name");
                String companyID = resultSet.getString("Company_ID");
                String companyName = resultSet.getString("Company_Name");
                String serverID = resultSet.getString("Server_ID");
                String serverName = resultSet.getString("Server_Name");
                String containerID = resultSet.getString("Container_ID");
                String containerName = resultSet.getString("Container_Name");
                String diagnosticsID = resultSet.getString("Diagnostics_ID");
                Timestamp timestamp = resultSet.getTimestamp("Timestamp");
                String errorLogs = resultSet.getString("Error_Logs");
                String date = resultSet.getString("diagnosticDate");
                String time = resultSet.getString("diagnosticTime");
                diagnosticsError.put("regionID", regionID);
                diagnosticsError.put("regionName", regionName);
                diagnosticsError.put("companyID", companyID);
                diagnosticsError.put("companyName", companyName);
                diagnosticsError.put("serverID", serverID);
                diagnosticsError.put("serverName", serverName);
                diagnosticsError.put("containerID", containerID);
                diagnosticsError.put("containerName", containerName);
                diagnosticsError.put("timestamp", timestamp);
                diagnosticsError.put("errorLogs", errorLogs);
                diagnosticsError.put("date", date);
                diagnosticsError.put("time", time);
                diagnosticsErrors.put(diagnosticsID, diagnosticsError);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return diagnosticsErrors;
    }

    public JSONObject getDashboardData() {
        JSONObject dashboardData = new JSONObject();
        String sql = "SELECT * FROM Region_Dashboard";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();

            
            while (resultSet.next()) {
                JSONObject regionData = new JSONObject();
                String regionID = resultSet.getString("Region_ID");
                String regionName = resultSet.getString("Region_Name");
                String activeContainers = resultSet.getString("Active_Containers");
                int companies = resultSet.getInt("Companies");
                String uptime = resultSet.getString("Total_Uptime");
                int errors = resultSet.getInt("Errors_Last_Hour");
                regionData.put("regionID", regionID);
                regionData.put("regionName", regionName);
                regionData.put("activeContainers", activeContainers);
                regionData.put("companies", companies);
                regionData.put("uptime", uptime);
                regionData.put("errors", errors);
                dashboardData.put(regionID, regionData);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return dashboardData;
    }

    public JSONObject getRecentCompanyData(String companyID) {
        JSONObject allCompanyData = new JSONObject();

        JSONObject servers = getServers(companyID);
        translateServerData(allCompanyData, servers);

        return allCompanyData;
    }

    private void translateServerData(JSONObject allCompanyData, JSONObject servers) {
        for (String serverKey : servers.keySet()) {
            JSONObject tempServer = servers.getJSONObject(serverKey);
            JSONObject tempServerContainers = new JSONObject();
            JSONObject containers = getContainers(servers.getJSONObject(serverKey).getString("serverID"));
            translateContainerData(tempServerContainers, containers);
            tempServer.put("containers", tempServerContainers);
            allCompanyData.put(tempServer.getString("serverID"), tempServer);
        }
    }

    private void translateContainerData(JSONObject tempServerContainers, JSONObject containers) {
        for (String containerKey : containers.keySet()) {
            JSONObject tempContainer = containers.getJSONObject(containerKey);
            JSONArray tempContainerDiagnosticsData = new JSONArray();
            JSONObject diagnosticsData = getDiagnosticsData(containers.getJSONObject(containerKey).getString("containerID").toString(), null);
            translateDiagnosticsData(tempContainerDiagnosticsData, diagnosticsData);
            tempContainer.put("diagnosticsData", tempContainerDiagnosticsData);
            tempServerContainers.put(tempContainer.getString("containerID"), tempContainer);
        }
    }

    private void translateDiagnosticsData(JSONArray tempContainerDiagnosticsData, JSONObject diagnosticsData) {
        for (String diagnosticsKey : diagnosticsData.keySet()) {
            JSONObject tempDiagnosticsData = diagnosticsData.getJSONObject(diagnosticsKey);
            tempContainerDiagnosticsData.put(tempDiagnosticsData);
        }
    }

    
    public JSONObject getContainerData(String containerID) {
        JSONObject container = new JSONObject();
        
        String sql = "SELECT * FROM Container WHERE Container_ID = ?";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, containerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                String containerName = resultSet.getString("Container_Name");
                String serverReference = resultSet.getString("Server_Reference");
                container.put("containerName", containerName);
                container.put("serverReference", serverReference);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return container;
    }

    
    public JSONObject getServerData(String serverID) {
        JSONObject server = new JSONObject();
        String sql = "SELECT * FROM Server WHERE Server_ID = ?";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, UUID.fromString(serverID));
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                String serverName = resultSet.getString("Server_Name");
                Timestamp latestPing = resultSet.getTimestamp("Latest_Ping");
                server.put("serverName", serverName);
                server.put("latestPing", latestPing);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return server;
    }

    public void pingServer(String serverID) {
        String sql = "UPDATE Server SET Latest_Ping = NOW() WHERE Server_ID = ?";

        
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, UUID.fromString(serverID));
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) System.out.println("Ping Error: Could not find Server ID: " + serverID);
            else System.out.println("Ping updated successfully.");

        } catch (SQLException error) {
            errorHandling(error);
        }
    }
}