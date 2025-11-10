package P3.Backend.Database;

import P3.Backend.Constants;

import java.lang.reflect.Array;
import java.sql.*;
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
        String sql = "INSERT INTO Region (Name) VALUES (?);";

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
     * 
     * @param regionID The ID of the region in which the company resides.
     * @param name     The name of the company being added.
     */
    public void addCompanies(int regionID, String name) {
        // Reformats the input parameters to fit the signature of the below addCompanies method.
        addCompanies(new int[] { regionID }, new String[] { name });
    }

    /**
     * Adds companies to the database.
     * 
     * @param regionIDs The IDs of the regions in which these companies reside.
     * @param names     The names of the companies being added.
     */
    public void addCompanies(int[] regionIDs, String[] names) {
        // Check that the input parameter arrays are of same length.
        if (notSameLength(regionIDs, names)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Company (Region_ID, Name) VALUES (?, ?)";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use Prepared Statement to help format the SQL string to prevent injections.
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Queue all companies to be added.
            for (int i = 0; i < regionIDs.length; i++) {
                preparedStatement.setInt(1, regionIDs[i]);
                preparedStatement.setString(2, names[i]);
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
     * 
     * @param serverID
     * @param companyID
     * @param ramTotal
     * @param cpuTotal
     * @param diskUsageTotal
     */
    public void addServers(String serverID, int companyID, double ramTotal, double cpuTotal, double diskUsageTotal) {
        // Reformats the input parameters to fit the signature of the below addServers method.
        addServers(new String[] { serverID }, new int[] { companyID }, new double[] { ramTotal },
                new double[] { cpuTotal },
                new double[] { diskUsageTotal });
    }

    /**
     * Adds servers to the database.
     * 
     * @param serverIDs       The IDs of the servers being added.
     * @param companyIDs      The IDs of the companies owning the servers.
     * @param ramTotals       The total amount of RAM on the server.
     * @param cpuTotals       The total amount of CPU on the server.
     * @param diskUsageTotals The total amount of Disk Usage on the server.
     */
    public void addServers(String[] serverIDs, int[] companyIDs, double[] ramTotals, double[] cpuTotals,
            double[] diskUsageTotals) {
        // Check that the input parameter arrays are of same length.
        if (notSameLength(serverIDs, companyIDs, ramTotals, cpuTotals, diskUsageTotals)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Server (Server_ID, Company_ID, Ram_Total, CPU_Total, Disk_Usage_Total)" +
                "VALUES (?, ?, ?, ?, ?)";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use Prepared Statement to help format the SQL string to prevent injections.
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Queue all companies to be added.
            for (int i = 0; i < serverIDs.length; i++) {
                preparedStatement.setString(1, serverIDs[i]);
                preparedStatement.setInt(2, companyIDs[i]);
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
     * 
     * @param containerID The ID of the container being added.
     * @param serverID    The ID of the server running the container.
     */
    public void addContainers(String containerID, String serverID) {
        // Reformats the input parameters to fit the signature of the below addContainers method.
        addContainers(new String[] { containerID }, new String[] { serverID });
    }

    /**
     * Adds containers to the database.
     * 
     * @param containerIDs The IDs of the containers being added.
     * @param serverIDs    The IDs of the servers running these containers.
     */
    public void addContainers(String[] containerIDs, String[] serverIDs) {
        // Check that the input parameter arrays are of same length.
        if (notSameLength(containerIDs, serverIDs)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Container (Container_ID, Server_ID) VALUES (?, ?)";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use Prepared Statement to help format the SQL string to prevent injections.
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Queue all companies to be added.
            for (int i = 0; i < serverIDs.length; i++) {
                preparedStatement.setString(1, containerIDs[i]);
                preparedStatement.setString(2, serverIDs[i]);
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
     * 
     * @param containerID   The ID of the container being diagnosed.
     * @param running       If the container is running.
     * @param ramFree       How much RAM is free.
     * @param cpuFree       How much CPU is free.
     * @param diskUsageFree How much Disk Usage is free.
     * @param threadCount   How many threads are active.
     * @param processID     The ID of the process.
     * @param status        The status of the container.
     * @param errorLogs     The Error Logs of the container.
     */
    public void addDiagnosticsBatch(String containerID, boolean running, double ramFree, double cpuFree,
            double diskUsageFree, int threadCount, String processID, String status,
            String errorLogs) {
        // Reformats the input parameters to fit the signature of the below addDiagnosticsBatch method.
        addDiagnosticsBatch(new String[] { containerID }, new boolean[] { running }, new double[] { ramFree },
                new double[] { cpuFree }, new double[] { diskUsageFree }, new int[] { threadCount },
                new String[] { processID },
                new String[] { status }, new String[] { errorLogs });
    }

    /**
     * Adds a batch of diagnostics to the database.
     * 
     * @param containerIDs   The IDs of the containers that are diagnosed.
     * @param runningList    If the containers are running.
     * @param ramFrees       How much RAM is free.
     * @param cpuFrees       How much CPU is free.
     * @param diskUsageFrees How much Disk Usage is free.
     * @param threadCounts   How many threads are active.
     * @param processIDs     The IDs of the processes.
     * @param statuses       The statuses of the containers.
     * @param errorLogsList  The Error Logs of the containers.
     */
    public void addDiagnosticsBatch(String[] containerIDs, boolean[] runningList, double[] ramFrees, double[] cpuFrees,
            double[] diskUsageFrees, int[] threadCounts, String[] processIDs, String[] statuses,
            String[] errorLogsList) {
        // Check that the input parameter arrays are of same length.
        if (notSameLength(containerIDs, runningList, ramFrees, cpuFrees, diskUsageFrees, threadCounts, processIDs,
                statuses, errorLogsList)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Diagnostics (Container_ID, Timestamp, Running, Ram_Free, CPU_Free, Disk_Usage_Free," +
                "Thread_Count, Process_ID, Status, Error_Logs) VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?)";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use Prepared Statement to help format the SQL string to prevent injections.
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Queue all companies to be added.
            for (int i = 0; i < containerIDs.length; i++) {
                preparedStatement.setString(1, containerIDs[i]);
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
                int id = resultSet.getInt("Region_ID");
                String name = resultSet.getString("Name");
                Region region = new Region(id, name);
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
     * 
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
                int companyID = resultSet.getInt("Company_ID");
                int regionId = resultSet.getInt("Region_ID");
                String name = resultSet.getString("Name");
                Company company = new Company(companyID, name);

                // Finds the respective region of a company, and adds the company to that region.
                for (Region region : regions) {
                    if (region.getRegionID() == regionId) {
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
     * 
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
                int companyID = resultSet.getInt("Company_ID");
                double ramTotal = resultSet.getDouble("Ram_Total");
                double cpuTotal = resultSet.getDouble("CPU_Total");
                double diskUsageTotal = resultSet.getDouble("Disk_Usage_Total");
                Server server = new Server(serverID, ramTotal, cpuTotal, diskUsageTotal);

                // Finds the respective company of a server, and adds the server to that company.
                for (Region region : regions) {
                    Company company = region.getCompany(companyID);
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
     * 
     * @param regions The ArrayList of regions (that now also contain companies and
     *                servers).
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
                String serverID = resultSet.getString("Server_ID");
                Container container = new Container(containerID);

                // Finds the respective server of a container, and adds the container to that server.
                for (Region region : regions) {
                    for (Company company : region.getCompanies()) {
                        Server server = company.getServer(serverID);
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
     * 
     * @param regions The ArrayList of regions (that now also contain companies,
     *                servers and containers).
     */
    public void getDiagnosticsData(ArrayList<Region> regions) {
        String sql = "SELECT * FROM Diagnostics";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use a normal Statement. No SQL injection protection is necessary when no user input.
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            // Reads all the rows in the Diagnostics table and adds them as Diagnostics classes to their respective container.
            while (resultSet.next()) {
                String containerID = resultSet.getString("Container_ID");
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
                            Container container = server.getContainer(containerID);
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

// ========= MAKESHIFT ADDITIONS - NOT FINAL ===============

    public Container getDiagnosticsData(Container docker) {
        String sql = "SELECT * FROM Diagnostics WHERE Container_ID = '" + docker.getContainerID() + "';";
        Container dck = new Container(docker.getContainerID());

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use a normal Statement. No SQL injection protection is necessary when no user input.
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            // Reads all the rows in the Diagnostics table and adds them as Diagnostics classes to their respective container.
            while (resultSet.next()) {
                String containerID = resultSet.getString("Container_ID");
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

    public ArrayList<Region> getRegionsTemp() {
        ArrayList<Region> regions = new ArrayList<>();
        String sql = "SELECT * FROM Region";

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use a normal Statement. No SQL injection protection is necessary when no user input.
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            // Reads all the rows in the Region table and adds them as Region classes to the ArrayList.
            while (resultSet.next()) {
                int id = resultSet.getInt("Region_ID");
                String name = resultSet.getString("Name");
                Region region = new Region(id, name);
                regions.add(region);
            }

        } catch (SQLException error) {
            errorHandling(error);
        }

        return regions;
    }



    public ArrayList<Company> getCompanies(int regionId) {
        String sql = "SELECT * FROM Company WHERE Region_ID = " + regionId;

        // Encapsulate the Database connection in a try-catch to catch any SQL errors.
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                // Use a normal Statement. No SQL injection protection is necessary when no user input.
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            ArrayList<Company> companies = new ArrayList<>();
            // Reads all the rows in the Company table and adds them as Company classes to the ArrayList.
            while (resultSet.next()) {
                int companyID = resultSet.getInt("Company_ID");
                String name = resultSet.getString("Name");
                Company company = new Company(companyID, name);
                companies.add(company);
            }
            return companies;

        } catch (SQLException error) {
            errorHandling(error);
            return null;
        }
    }
}



