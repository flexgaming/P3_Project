package P3.Backend.Database;

import P3.Backend.Constants;

import java.lang.reflect.Array;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

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
     * @param arrays Input arrays.
     * @return true if they at least one array is of different length than the others, false otherwise.
     */
    public static boolean notSameLength(Object... arrays) {
        // If there are no arrays, return false.
        if (arrays.length == 0) return false;

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
     * @param regionName The name of the region being added.
     */
    public void addRegions(String regionName) {
        // Reformats the input parameters to fit the signature of the below addRegions method.
        addRegions(new String[]{regionName});
    }

    /**
     * Adds regions to the database.
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
     * @param ramTotal The total amount of RAM on the server.
     * @param cpuTotal The total amount of CPU on the server.
     * @param diskUsageTotal The total amount of Disk Usage on the server.
     */
    public void addServers(String serverID, String companyReference, double ramTotal, double cpuTotal,
                           double diskUsageTotal) {
        // Reformats the input parameters to fit the signature of the below addServers method.
        addServers(new String[]{serverID}, new String[]{companyReference}, new double[]{ramTotal}, new double[]{cpuTotal},
                new double[]{diskUsageTotal});
    }

    /**
     * Adds servers to the database.
     * @param serverIDs The IDs of the servers being added.
     * @param companyReferences The IDs of the companies owning the servers.
     * @param ramTotals The total amount of RAM on the servers.
     * @param cpuTotals The total amount of CPU on the servers.
     * @param diskUsageTotals The total amount of Disk Usage on the servers.
     */
    public void addServers(String[] serverIDs, String[] companyReferences, double[] ramTotals, double[] cpuTotals,
                           double[] diskUsageTotals) {
        // Check that the input parameter arrays are of same length.
        if (notSameLength(serverIDs, companyReferences, ramTotals, cpuTotals, diskUsageTotals)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Server (Server_ID, Company_Reference, Ram_Total, CPU_Total, Disk_Usage_Total)" +
                "VALUES (?, ?, ?, ?, ?)";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use Prepared Statement to help format the SQL string to prevent injections.
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Queue all companies to be added.
            for (int i = 0; i < serverIDs.length; i++) {
                preparedStatement.setString(1, serverIDs[i]);
                preparedStatement.setObject(2, UUID.fromString(companyReferences[i]));
                preparedStatement.setDouble(3, ramTotals[i]);
                preparedStatement.setDouble(4, cpuTotals[i]);
                preparedStatement.setDouble(5, diskUsageTotals[i]);
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
     * @param containerID The ID of the container being added.
     * @param serverReference The ID of the server running the container.
     */
    public void addContainers(String containerID, String serverReference) {
        // Reformats the input parameters to fit the signature of the below addContainers method.
        addContainers(new String[]{containerID}, new String[]{serverReference});
    }

    /**
     * Adds containers to the database.
     * @param containerIDs The IDs of the containers being added.
     * @param serverReferences The IDs of the servers running these containers.
     */
    public void addContainers(String[] containerIDs, String[] serverReferences) {
        // Check that the input parameter arrays are of same length.
        if (notSameLength(containerIDs, serverReferences)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Container (Container_ID, Server_Reference) VALUES (?, ?)";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use Prepared Statement to help format the SQL string to prevent injections.
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Queue all companies to be added.
            for (int i = 0; i < serverReferences.length; i++) {
                preparedStatement.setString(1, containerIDs[i]);
                preparedStatement.setString(2, serverReferences[i]);
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
     * @param threadCount How many threads are active.
     * @param processID The ID of the process.
     * @param status The status of the container.
     * @param errorLogs The Error Logs of the container.
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
     * @param threadCounts How many threads are active.
     * @param processIDs The IDs of the processes.
     * @param statuses The statuses of the containers.
     * @param errorLogsList The Error Logs of the containers.
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
     * @return ArrayList of all regions.
     */
    public ArrayList<Region> getRegions() {
        ArrayList<Region> regions = new ArrayList<>();
        String sql = "SELECT * FROM Region";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use a normal Statement. No SQL injection protection is necessary when no user input.
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Reads all the rows in the Region table and adds them as Region classes to the ArrayList.
            while (resultSet.next()) {
                String regionID = resultSet.getString("Region_ID");
                String regionName = resultSet.getString("Region_Name");
                Region region = new Region(regionID, regionName);
                regions.add(region);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        // Adds all companies to the regions before returning.
        getCompanies(regions);

        return regions;
    }

    /**
     * Fetches all the companies saved in the database.
     * @param regions The ArrayList of regions.
     */
    public void getCompanies(ArrayList<Region> regions) {
        String sql = "SELECT * FROM Company";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use a normal Statement. No SQL injection protection is necessary when no user input.
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Reads all the rows in the Company table and adds them as Company classes to their respective region.
            while (resultSet.next()) {
                String companyID = resultSet.getString("Company_ID");
                String regionReference = resultSet.getString("Region_Reference");
                String companyName = resultSet.getString("Company_Name");
                Company company = new Company(companyID, companyName);

                // Finds the respective region of a company, and adds the company to that region.
                for (Region region : regions) {
                    if (region.getRegionID().equals(regionReference)) {
                        region.addCompany(company);
                    }
                }
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        // Adds all servers to the companies before returning.
        getServers(regions);
    }

    /**
     * Fetches all the servers saved in the database.
     * @param regions The ArrayList of regions (that now also contain companies).
     */
    public void getServers(ArrayList<Region> regions) {
        String sql = "SELECT * FROM Server";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use a normal Statement. No SQL injection protection is necessary when no user input.
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Reads all the rows in the Server table and adds them as Server classes to their respective company.
            while (resultSet.next()) {
                String serverID = resultSet.getString("Server_ID");
                String companyReference = resultSet.getString("Company_Reference");
                double ramTotal = resultSet.getDouble("Ram_Total");
                double cpuTotal = resultSet.getDouble("CPU_Total");
                double diskUsageTotal = resultSet.getDouble("Disk_Usage_Total");
                Server server = new Server(serverID, ramTotal, cpuTotal, diskUsageTotal);

                // Finds the respective company of a server, and adds the server to that company.
                for (Region region : regions) {
                    Company company = region.getCompany(companyReference);
                    if (company != null) {
                        company.addServer(server);
                    }
                }
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        // Adds all containers to the servers before returning.
        getContainers(regions);
    }

    /**
     * Fetches all containers saved in the database.
     * @param regions The ArrayList of regions (that now also contain companies and servers).
     */
    public void getContainers(ArrayList<Region> regions) {
        String sql = "SELECT * FROM Container";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use a normal Statement. No SQL injection protection is necessary when no user input.
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Reads all the rows in the Container table and adds them as Container classes to their respective server.
            while (resultSet.next()) {
                String containerID = resultSet.getString("Container_ID");
                String serverReference = resultSet.getString("Server_Reference");
                Container container = new Container(containerID);

                // Finds the respective server of a container, and adds the container to that server.
                for (Region region : regions) {
                    for (Company company : region.getCompanies()) {
                        Server server = company.getServer(serverReference);
                        if (server != null) {
                            server.addContainer(container);
                        }
                    }
                }
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        // Adds all diagnostics data to the containers before returning.
        getDiagnosticsData(regions);
    }

    /**
     * Fetches all diagnostics data saved in the database.
     * @param regions The ArrayList of regions (that now also contain companies, servers and containers).
     */
    public void getDiagnosticsData(ArrayList<Region> regions) {
        String sql = "SELECT * FROM Diagnostics";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use a normal Statement. No SQL injection protection is necessary when no user input.
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Reads all the rows in the Diagnostics table and adds them as Diagnostics classes to their respective
            // container.
            while (resultSet.next()) {
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
                Diagnostics diagnostics = new Diagnostics(timestamp, running, ramFree, cpuFree, diskUsageFree,
                                                          threadCount, processID, status, errorLogs);

                // Finds the respective container of the diagnostics, and adds the diagnostics to that container.
                for (Region region : regions) {
                    for (Company company : region.getCompanies()) {
                        for (Server server : company.getServers()) {
                            Container container = server.getContainer(containerReference);
                            if (container != null) {
                                container.addDiagnostics(diagnostics);
                            }
                        }
                    }
                }
            }

        } catch (SQLException error) {
            errorHandling(error);
        }
    }

    public Container getDiagnosticsData(Container docker) {
        String sql = "SELECT * FROM Diagnostics WHERE Container_Reference = '" + docker.getContainerID() + "';";
        Container dck = new Container(docker.getContainerID());

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             // Use a normal Statement. No SQL injection protection is necessary when no user input.
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Reads all the rows in the Diagnostics table and adds them as Diagnostics classes to their respective
            // container.
            while (resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp("Timestamp");
                boolean running = resultSet.getBoolean("Running");
                double ramFree = resultSet.getDouble("Ram_Free");
                double cpuFree = resultSet.getDouble("CPU_Free");
                double diskUsageFree = resultSet.getDouble("Disk_Usage_Free");
                int threadCount = resultSet.getInt("Thread_Count");
                String processID = resultSet.getString("Process_ID");
                String status = resultSet.getString("Status");
                String errorLogs = resultSet.getString("Error_Logs");
                Diagnostics diagnostics = new Diagnostics(timestamp, running, ramFree, cpuFree, diskUsageFree,
                                                          threadCount, processID, status, errorLogs);

                dck.addDiagnostics(diagnostics);
            }
            return dck;
            
        } catch (SQLException error) {
            errorHandling(error);
            return null;
        }
    }
}