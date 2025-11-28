package P3.Backend.Database;

import P3.Backend.Constants;
import P3.Backend.HelperFunctions;

import java.lang.reflect.Array;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.tomcat.util.bcel.classfile.Constant;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class Database {
    // The information needed to connect to the Database.
    String url = Constants.DATABASE_URL;
    String user = Constants.DATABASE_USER;
    String password = Constants.DATABASE_PASSWORD;

    private void errorHandling(Throwable error) {
        error.printStackTrace();
    }

    /**
     * Checks if all the given arrays are of the same length.
     * 
     * @param arrays Input arrays.
     * @return true if they at least one array is of different length than the
     *         others, false otherwise.
     */
    public static boolean notSameLength(Object... arrays) {
        // If there are no arrays, return false.
        if (arrays.length == 0)
            return false;

        // The expected length is the length of the first array.
        int expectedLength = Array.getLength(arrays[0]);

        // Checks if all the arrays have the expected length.
        for (Object arr : arrays) {
            if (!arr.getClass().isArray() || Array.getLength(arr) != expectedLength) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a region to the database.
     * 
     * @param regionName The name of the region being added.
     */
    public void addRegions(String regionName) {
        // Reformats the input parameters to fit the signature of the below addRegions method.
        addRegions(new String[] { regionName });
    }

    /**
     * Adds regions to the database.
     * 
     * @param regionNames The names of all the regions being added.
     */
    public void addRegions(String[] regionNames) {
        String sql = "INSERT INTO Region (Region_Name) VALUES (?);";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use Prepared Statement to help format the SQL string to prevent injections.
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Queue all regions to be added.
            for (String regionName : regionNames) {
                preparedStatement.setString(1, regionName);
                // Add the prepared statement to the batch.
                preparedStatement.addBatch();
            }

            // Execute all statements in the batch.
            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException sqlException) {
            errorHandling(sqlException);
        }
    }

    /**
     * Adds a company to the database.
     * @param regionReference The ID of the region in which the company resides.
     * @param companyName The name of the company being added.
     */
    public void addCompanies(String regionReference, String companyName) {
        // Reformats the input parameters to fit the signature of the below addCompanies method.
        addCompanies(new String[]{regionReference}, new String[]{companyName});
    }

    /**
     * Adds companies to the database.
     * @param regionReferences The IDs of the regions in which these companies reside.
     * @param companyNames The names of the companies being added.
     */
    public void addCompanies(String[] regionReferences, String[] companyNames) {
        // Check that the input parameter arrays are of same length.
        if (notSameLength(regionReferences, companyNames)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Company (Region_Reference, Company_Name) VALUES (?, ?)";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use Prepared Statement to help format the SQL string to prevent injections.
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Queue all companies to be added.
            for (int i = 0; i < regionReferences.length; i++) {
                preparedStatement.setObject(1, UUID.fromString(regionReferences[i]));
                preparedStatement.setString(2, companyNames[i]);
                // Add the prepared statement to the batch.
                preparedStatement.addBatch();
            }

            // Execute all statements in the batch.
            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException error) {
            errorHandling(error);
        }
    }

    /**
     * Adds a server to the database.
     * @param serverID The ID of the server being added.
     * @param companyReference The ID of the company owning the server.
     * @param serverName The name of the server being added.
     * @param ramTotal The total amount of RAM on the server.
     * @param cpuTotal The total amount of CPU on the server.
     * @param diskUsageTotal The total amount of Disk Usage on the server.
     */
    public void addServers(String serverID, String companyReference, String serverName, double ramTotal,
                           double cpuTotal, double diskUsageTotal) {
        // Reformats the input parameters to fit the signature of the below addServers method.
        addServers(new String[]{serverID}, new String[]{companyReference}, new String[]{serverName},
                new double[]{ramTotal}, new double[]{cpuTotal}, new double[]{diskUsageTotal});
    }

    /**
     * Adds servers to the database.
     * @param serverIDs The IDs of the servers being added.
     * @param companyReferences The IDs of the companies owning the servers.
     * @param serverNames The names of the servers being added.
     * @param ramTotals The total amount of RAM on the servers.
     * @param cpuTotals The total amount of CPU on the servers.
     * @param diskUsageTotals The total amount of Disk Usage on the servers.
     */
    public void addServers(String[] serverIDs, String[] companyReferences, String[] serverNames, double[] ramTotals,
                           double[] cpuTotals, double[] diskUsageTotals) {
        // Check that the input parameter arrays are of same length.
        if (notSameLength(serverIDs, companyReferences, ramTotals, cpuTotals, diskUsageTotals)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Server (Server_ID, Company_Reference, Server_Name, Ram_Total, CPU_Total, Disk_Usage_Total)" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use Prepared Statement to help format the SQL string to prevent injections.
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Queue all companies to be added.
            for (int i = 0; i < serverIDs.length; i++) {
                preparedStatement.setString(1, serverIDs[i]);
                preparedStatement.setObject(2, UUID.fromString(companyReferences[i]));
                preparedStatement.setString(3, serverNames[i]);
                preparedStatement.setDouble(4, ramTotals[i]);
                preparedStatement.setDouble(5, cpuTotals[i]);
                preparedStatement.setDouble(6, diskUsageTotals[i]);
                // Add the prepared statement to the batch.
                preparedStatement.addBatch();
            }

            // Execute all statements in the batch.
            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException error) {
            errorHandling(error);
        }
    }

    /**
     * Adds a container to the database.
     * 
     * @param containerID The ID of the container being added.
     * @param serverReference The ID of the server running the container.
     * @param containerName The name of the container being added.
     * @param ramMax The max RAM of the container being added.
     * @param cpuMax The max CPU of the container being added.
     * @param diskUsageMax The max Disk Usage of the container being added.
     */
    public void addContainers(String containerID, String serverReference, String containerName, double ramMax,
                              double cpuMax, double diskUsageMax) {
        // Reformats the input parameters to fit the signature of the below addContainers method.
        addContainers(new String[]{containerID}, new String[]{serverReference}, new String[]{containerName},
                new double[]{ramMax}, new double[]{cpuMax}, new double[]{diskUsageMax});
    }

    /**
     * Adds containers to the database.
     * 
     * @param containerIDs The IDs of the containers being added.
     * @param serverReferences The IDs of the servers running these containers.
     * @param containerNames the names of the containers being added.
     * @param ramMaxes The max RAMs of the containers being added.
     * @param cpuMaxes The max CPUs of the containers being added.
     * @param diskUsageMaxes The max Disk Usages of the containers being added.
     */
    public void addContainers(String[] containerIDs, String[] serverReferences, String[] containerNames,
                              double[] ramMaxes, double[] cpuMaxes, double[] diskUsageMaxes) {
        // Check that the input parameter arrays are of same length.
        if (notSameLength(containerIDs, serverReferences)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Container (Container_ID, Server_Reference, Container_Name, Ram_Max, Cpu_Max, " +
                "Disk_Usage_Max) VALUES (?, ?, ?, ?, ?, ?)";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use Prepared Statement to help format the SQL string to prevent injections.
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Queue all containers to be added.
            for (int i = 0; i < serverReferences.length; i++) {
                preparedStatement.setString(1, containerIDs[i]);
                preparedStatement.setString(2, serverReferences[i]);
                preparedStatement.setString(3, containerNames[i]);
                preparedStatement.setDouble(4, ramMaxes[i]);
                preparedStatement.setDouble(5, cpuMaxes[i]);
                preparedStatement.setDouble(6, diskUsageMaxes[i]);
                // Add the prepared statement to the batch.
                preparedStatement.addBatch();
            }

            // Execute all statements in the batch.
            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException error) {
            errorHandling(error);
        }
    }

    /**
     * Adds diagnostics to the database.
     * @param containerReference The ID of the container being diagnosed.
     * @param running If the container is running.
     * @param ramFree How much RAM is free.
     * @param cpuFree How much CPU is free.
     * @param diskUsageFree How much Disk Usage is free.
     * @param threadCount   How many threads are active.
     * @param processID     The ID of the process.
     * @param status        The status of the container.
     * @param errorLogs     The Error Logs of the container.
     */
    public void addDiagnosticsBatch(String containerReference, boolean running, double ramFree, double cpuFree,
                                    double diskUsageFree, int threadCount, String processID, String status,
                                    String errorLogs) {
        // Reformats the input parameters to fit the signature of the below addDiagnosticsBatch method.
        addDiagnosticsBatch(new String[]{containerReference}, new boolean[]{running}, new double[]{ramFree},
                new double[]{cpuFree}, new double[]{diskUsageFree}, new int[]{threadCount}, new String[]{processID},
                new String[]{status}, new String[]{errorLogs});
    }

    /**
     * Adds a batch of diagnostics to the database.
     * @param containerReferences The IDs of the containers that are diagnosed.
     * @param runningList If the containers are running.
     * @param ramFrees How much RAM is free.
     * @param cpuFrees How much CPU is free.
     * @param diskUsageFrees How much Disk Usage is free.
     * @param threadCounts   How many threads are active.
     * @param processIDs     The IDs of the processes.
     * @param statuses       The statuses of the containers.
     * @param errorLogsList  The Error Logs of the containers.
     */
    public void addDiagnosticsBatch(String[] containerReferences, boolean[] runningList, double[] ramFrees,
                                    double[] cpuFrees, double[] diskUsageFrees, int[] threadCounts, String[] processIDs,
                                    String[] statuses, String[] errorLogsList) {
        // Check that the input parameter arrays are of same length.
        if (notSameLength(containerReferences, runningList, ramFrees, cpuFrees, diskUsageFrees, threadCounts, processIDs,
                statuses, errorLogsList)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Diagnostics (Container_Reference, Timestamp, Running, Ram_Free, CPU_Free, " +
                "Disk_Usage_Free, Thread_Count, Process_ID, Status, Error_Logs) VALUES " +
                "(?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?)";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use Prepared Statement to help format the SQL string to prevent injections.
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Queue all companies to be added.
            for (int i = 0; i < containerReferences.length; i++) {
                preparedStatement.setString(1, containerReferences[i]);
                preparedStatement.setBoolean(2, runningList[i]);
                preparedStatement.setDouble(3, ramFrees[i]);
                preparedStatement.setDouble(4, cpuFrees[i]);
                preparedStatement.setDouble(5, diskUsageFrees[i]);
                preparedStatement.setInt(6, threadCounts[i]);
                preparedStatement.setString(7, processIDs[i]);
                preparedStatement.setString(8, statuses[i]);
                preparedStatement.setString(9, errorLogsList[i]);
                // Add the prepared statement to the batch.
                preparedStatement.addBatch();
            }

            // Execute all statements in the batch.
            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException error) {
            errorHandling(error);
        }
    }

    /**
     * Fetches all the regions saved in the database.
     * 
     * @return ArrayList of all regions.
     */
    public JSONObject getRegions() {
        JSONObject regions = new JSONObject();
        String sql = "SELECT * FROM Region";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use a normal Statement. No SQL injection protection is necessary when no user input.
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            // Reads all the rows in the Region table and adds them to the JSON object.
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

    /**
     * Fetches all the companies saved in the database.
     */
    public JSONObject getCompanies(String regionID) {
        JSONObject companies = new JSONObject();
        String sql = "SELECT * FROM Company WHERE Region_Reference = ?";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use Prepared Statement to help format the SQL string to prevent injections.
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setObject(1, UUID.fromString(regionID));
            ResultSet resultSet = preparedStatement.executeQuery();
            // Reads all the rows in the Company table and adds them to the JSON object.
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

    /**
     * Fetches all the servers saved in the database.
     */
    public JSONObject getServers(String companyID) {
        JSONObject servers = new JSONObject();
        String sql = "SELECT * FROM Server WHERE Company_Reference = ?";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use Prepared Statement to help format the SQL string to prevent injections.
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setObject(1, UUID.fromString(companyID));
            ResultSet resultSet = preparedStatement.executeQuery();

            // Reads all the rows in the Server table and adds them to the JSON object.
            while (resultSet.next()) {
                JSONObject server = new JSONObject();
                String serverID = resultSet.getString("Server_ID");
                String companyReference = resultSet.getString("Company_Reference");
                String serverName = resultSet.getString("Server_Name");
                double ramTotal = resultSet.getDouble("Ram_Total");
                double cpuTotal = resultSet.getDouble("CPU_Total");
                double diskUsageTotal = resultSet.getDouble("Disk_Usage_Total");
                server.put("serverID", serverID);
                server.put("companyReference", companyReference);
                server.put("serverName", serverName);
                server.put("ramTotal", ramTotal);
                server.put("cpuTotal", cpuTotal);
                server.put("diskUsageTotal", diskUsageTotal);
                servers.put(serverName, server);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return servers;
    }

    /**
     * Fetches all containers saved in the database.
     */
    public JSONObject getContainers(String serverID) {
        JSONObject containers = new JSONObject();
        // Read all data from View Company_Containers.
        String sql = "SELECT * FROM Container WHERE Server_Reference = ?";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use Prepared Statement to help format the SQL string to prevent injections.
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {;

            preparedStatement.setString(1, serverID);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Reads all the rows in the Company_Containers View and adds them to the JSON object.
            while (resultSet.next()) {
                JSONObject container = new JSONObject();
                String containerID = resultSet.getString("Container_ID");
                String serverReference = resultSet.getString("Server_Reference");
                String containerName = resultSet.getString("Container_Name");
                double ramMax = resultSet.getDouble("Ram_Max");
                double cpuMax = resultSet.getDouble("CPU_Max");
                double diskUsageMax = resultSet.getDouble("Disk_Usage_Max");
                container.put("containerID", containerID);
                container.put("serverReference", serverReference);
                container.put("containerName", containerName);
                container.put("ramMax", ramMax);
                container.put("cpuMax", cpuMax);
                container.put("diskUsageMax", diskUsageMax);
                containers.put(containerName, container);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return containers;
    }

    /**
     * Fetches all diagnostics data saved in the database in the requested time frame.
     */
    public JSONObject getDiagnosticsData(String containerID) {
        JSONObject containerData = new JSONObject();
        JSONObject diagnosticsData = new JSONObject();
        // Read all data from View Company_Diagnostics that are within time frame.
        String sql = "SELECT * FROM Diagnostics WHERE Container_Reference = ? AND Diagnostics.Timestamp >= " +
                "NOW() - make_interval(mins => ?)";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            // Use Prepared Statement to help format the SQL string to prevent injections.
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, containerID);
            preparedStatement.setInt(2, Constants.DIAGNOSTICS_TIME_SCOPE); 
            ResultSet resultSet = preparedStatement.executeQuery();

            // Reads all the rows in the Company_Diagnostics View and adds them to the JSON object.
            while (resultSet.next()) {
                JSONObject diagnostics = new JSONObject();
                String diagnosticsID = resultSet.getString("Diagnostics_ID");
                String containerReference = resultSet.getString("Container_Reference");
                Timestamp timestamp = resultSet.getTimestamp("Timestamp");
                boolean running = resultSet.getBoolean("Running");
                double ramFree = resultSet.getDouble("Ram_Free");
                double cpuFree = resultSet.getDouble("CPU_Free");
                double diskUsageFree = resultSet.getDouble("Disk_Usage_Free");
                int threadCount = resultSet.getInt("Thread_Count");
                String processID = resultSet.getString("Process_ID");
                String status = resultSet.getString("Status");
                String errorLogs = resultSet.getString("Error_Logs");
                diagnostics.put("containerReference", containerReference);
                diagnostics.put("timestamp", timestamp);
                diagnostics.put("running", running);
                diagnostics.put("ramFree", ramFree);
                diagnostics.put("cpuFree", cpuFree);
                diagnostics.put("diskUsageFree", diskUsageFree);
                diagnostics.put("threadCount", threadCount);
                diagnostics.put("processID", processID);
                diagnostics.put("status", status);
                diagnostics.put("errorLogs", errorLogs);
                diagnosticsData.put(diagnosticsID, diagnostics);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        // containerData.put("containerData", getContainerData(containerID));
        containerData.put("diagnosticsData", diagnosticsData);

        return containerData;
    }

    /**
     * Fetches all diagnostics errors saved in the database.
     */
    public JSONObject getDiagnosticsErrors(String timeFrameString) {
        int timeFrameMinutes = Constants.DIAGNOSTICS_TIME_SCOPE;
        if (timeFrameString != null){
            // Use Helper Function to convert time frame string to minutes.
            timeFrameMinutes = HelperFunctions.getMinutesFromTimeFrame(timeFrameString);
            // System.out.println("Converted time frame from string: " + timeFrameString + " -> " + timeFrameMinutes + " minutes.");
        }
        JSONObject diagnosticsErrors = new JSONObject();
        // Read all data from View Diagnostics_Errors in the selected timeframe.
        String sql = "SELECT * FROM Diagnostics_Errors WHERE diagnostics_errors.timestamp >= "+" NOW() - make_interval(mins => ?)";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use a normal Statement. No SQL injection protection is necessary when no user input.
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, timeFrameMinutes);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Reads all the rows in the Diagnostics Errors View and adds them to the JSON object.
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

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use a prepared Statement to help format the SQL string to prevent injections.
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();

            // Reads all the rows in the Region table and adds them to the JSON object.
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
            JSONObject diagnosticsData = getDiagnosticsData(containers.getJSONObject(containerKey).getString("containerID")).getJSONObject("diagnosticsData");
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

    /**
     * Gets max RAM, max CPU and max Disk Usage for the container.
     *
     * @param containerID The ID of the container.
     * @return JSON Object containing max RAM, max CPU and max Disk Usage.
     */
    public JSONObject getContainerData(String containerID) {
        JSONObject container = new JSONObject();
        // Read all data from View Company_Containers in a selected time frame.
        String sql = "SELECT * FROM Container WHERE Container_ID = ?";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use Prepared Statement to help format the SQL string to prevent injections.
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, containerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Reads the data from the container and saves it into the JSON object.
            while (resultSet.next()) {
                String containerName = resultSet.getString("Container_Name");
                String serverReference = resultSet.getString("Server_Reference");
                double ramMax = resultSet.getDouble("Ram_Max");
                double cpuMax = resultSet.getDouble("CPU_Max");
                double diskUsageMax = resultSet.getDouble("Disk_Usage_Max");
                container.put("containerName", containerName);
                container.put("serverReference", serverReference);
                container.put("ramMax", ramMax);
                container.put("cpuMax", cpuMax);
                container.put("diskUsageMax", diskUsageMax);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return container;
    }

    public JSONObject getServerData(String serverID) {
        JSONObject server = new JSONObject();
        String sql = "SELECT * FROM Server WHERE Server_ID = ?";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use Prepared Statement to help format the SQL string to prevent injections.
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, serverID);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Reads the data from the container and saves it into the JSON object.
            while (resultSet.next()) {
                String serverName = resultSet.getString("Server_Name");
                double ramTotal = resultSet.getDouble("Ram_Total");
                double cpuTotal = resultSet.getDouble("CPU_Total");
                double diskUsageTotal = resultSet.getDouble("Disk_Usage_Total");
                server.put("serverName", serverName);
                server.put("ramTotal", ramTotal);
                server.put("cpuTotal", cpuTotal);
                server.put("diskUsageTotal", diskUsageTotal);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return server;
    }

    // ===============================================================================================
    // =============================== MAKESHIFT ADDITIONS - NOT FINAL ===============================
    // ===============================================================================================

    public JSONObject getDiagnosticsData(Container docker, String timeFrameString) {
        int timeFrameMinutes = Constants.DIAGNOSTICS_TIME_SCOPE;
        if (timeFrameString != null){
            // Use Helper Function to convert time frame string to minutes.
            timeFrameMinutes = HelperFunctions.getMinutesFromTimeFrame(timeFrameString);
            // System.out.println("Converted time frame from string: " + timeFrameString + " -> " + timeFrameMinutes + " minutes.");
        }
        String sql = "SELECT * FROM Diagnostics WHERE Container_Reference = ? AND Diagnostics.TimeStamp >= "+" NOW() - make_interval(mins => ?)";
        JSONObject diagnosticsData = new JSONObject();

         try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            // Use Prepared Statement to help format the SQL string to prevent injections.
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, docker.getContainerID());
            preparedStatement.setInt(2, timeFrameMinutes); 
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // Reads all the rows in the Diagnostics table and adds them as Diagnostics classes to their respective container.
            while (resultSet.next()) {
                JSONObject diagnostics = new JSONObject();
                String diagnosticsID = resultSet.getString("Diagnostics_ID");
                String containerReference = resultSet.getString("Container_Reference");
                Timestamp timestamp = resultSet.getTimestamp("Timestamp");
                boolean running = resultSet.getBoolean("Running");
                double ramFree = resultSet.getDouble("Ram_Free");
                double cpuFree = resultSet.getDouble("CPU_Free");
                double diskUsageFree = resultSet.getDouble("Disk_Usage_Free");
                int threadCount = resultSet.getInt("Thread_Count");
                String processID = resultSet.getString("Process_ID");
                String status = resultSet.getString("Status");
                String errorLogs = resultSet.getString("Error_Logs");
                diagnostics.put("containerReference", containerReference);
                diagnostics.put("timestamp", timestamp);
                diagnostics.put("running", running);
                diagnostics.put("ramFree", ramFree);
                diagnostics.put("cpuFree", cpuFree);
                diagnostics.put("diskUsageFree", diskUsageFree);
                diagnostics.put("threadCount", threadCount);
                diagnostics.put("processID", processID);
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

    public ArrayList<String> getRegionsTemp() {
        ArrayList<String> regions = new ArrayList<>();
        String sql = "SELECT * FROM region";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use a normal Statement. No SQL injection protection is necessary when no user input.
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            // Reads all the rows in the Region table and adds them as Region classes to the ArrayList.
            while (resultSet.next()) {
                regions.add(resultSet.getString("Name"));
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return regions;
    }

    public ArrayList<String> getCompanyContents(String regionName, String companyName) {
        companyName = companyName.replaceAll("-", " ");
        // Select from View
        String sql = " ";// TO BE FILLED

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use a normal Statement. No SQL injection protection is necessary when no user input.
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            ArrayList<String> servers = new ArrayList<>();
            // Reads all the rows in the Server table and adds them as Server classes to the ArrayList.
            while (resultSet.next()) {
                String serverID = resultSet.getString("Server_ID");
                servers.add(serverID);
            }
            return servers;

        } catch (SQLException error) {
            errorHandling(error);
            return null;
        }
    }
}



