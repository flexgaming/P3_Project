package P3.Backend.Database;

import java.lang.reflect.Array;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    String url = Constants.DATABASE_URL;
    String user = Constants.DATABASE_USER;
    String password = Constants.DATABASE_PASSWORD;

    private void errorHandling(Error error) {
        error.printStackTrace();
    }

    private void sqlErrorHandling(SQLException sqlException) {
        sqlException.printStackTrace();
    }

    public static boolean notSameLength(Object... arrays) {
        if (arrays.length == 0) return false;

        int expectedLength = Array.getLength(arrays[0]);

        for (Object arr : arrays) {
            if (!arr.getClass().isArray() || Array.getLength(arr) != expectedLength) {
                return true;
            }
        }
        return false;
    }

    public void addRegions(String regionName) {
        addRegions(new String[]{regionName});
    }

    public void addRegions(String[] regionNames) {
        String sql = "INSERT INTO Region (Name) VALUES (?);";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (String regionName : regionNames) {
                preparedStatement.setString(1, regionName);
                preparedStatement.addBatch();
            }

            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException sqlException) {
            sqlErrorHandling(sqlException);
        }
    }

    public void addCompanies(int regionID, String name) {
        addCompanies(new int[]{regionID}, new String[]{name});
    }

    public void addCompanies(int[] regionIDs, String[] names) {
        if (notSameLength(regionIDs, names)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Company (Region_ID, Name) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < regionIDs.length; i++) {
                preparedStatement.setInt(1, regionIDs[i]);
                preparedStatement.setString(2, names[i]);
                preparedStatement.addBatch();
            }

            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException error) {
            sqlErrorHandling(error);
        }
    }

    public void addServers(String serverID, int companyID, double ramTotal, double cpuTotal, double diskUsageTotal) {
        addServers(new String[]{serverID}, new int[]{companyID}, new double[]{ramTotal}, new double[]{cpuTotal},
                new double[]{diskUsageTotal});
    }

    public void addServers(String[] serverIDs, int[] companyIDs, double[] ramTotals, double[] cpuTotals,
                           double[] diskUsageTotals) {
        if (notSameLength(serverIDs, companyIDs, ramTotals, cpuTotals, diskUsageTotals)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Server (Server_ID, Company_ID, Ram_Total, CPU_Total, Disk_Usage_Total)" +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < serverIDs.length; i++) {
                preparedStatement.setString(1, serverIDs[i]);
                preparedStatement.setInt(2, companyIDs[i]);
                preparedStatement.setDouble(3, ramTotals[i]);
                preparedStatement.setDouble(4, cpuTotals[i]);
                preparedStatement.setDouble(5, diskUsageTotals[i]);
                preparedStatement.addBatch();
            }

            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException error) {
            sqlErrorHandling(error);
        }
    }

    public void addContainers(String containerID, String serverID) {
        addContainers(new String[]{containerID}, new String[]{serverID});
    }

    public void addContainers(String[] containerIDs, String[] serverIDs) {
        if (notSameLength(containerIDs, serverIDs)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Container (Container_ID, Server_ID) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < serverIDs.length; i++) {
                preparedStatement.setString(1, containerIDs[i]);
                preparedStatement.setString(2, serverIDs[i]);
                preparedStatement.addBatch();
            }

            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException error) {
            sqlErrorHandling(error);
        }
    }

    public void addDiagnosticsBatch(String[] containerIDs, boolean[] runningList, double[] ramFrees, double[] cpuFrees,
                                   double[] diskUsageFrees, int[] threadCounts, String[] processIDs, String[] statuses,
                                   String[] errorLogsList) {
        if (notSameLength(containerIDs, runningList, ramFrees, cpuFrees, diskUsageFrees, threadCounts, processIDs,
                statuses, errorLogsList)) {
            errorHandling(new Error(Constants.ARRAY_LENGTH_ERROR));
            return;
        }

        String sql = "INSERT INTO Diagnostics (Container_ID, Timestamp, Running, Ram_Free, CPU_Free, Disk_Usage_Free," +
                "Thread_Count, Process_ID, Status, Error_Logs) VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

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
                preparedStatement.addBatch();
            }

            int[] rowsInserted = preparedStatement.executeBatch();
            System.out.println(rowsInserted.length + " row(s) inserted.");

        } catch (SQLException error) {
            sqlErrorHandling(error);
        }
    }

    public void addDiagnostics(String containerID, boolean running, double ramFree, double cpuFree,
                        double diskUsageFree, int threadCount, String processID, String status, String errorLogs) {
        String sql = "INSERT INTO Diagnostics (Container_ID, Timestamp, Running, Ram_Free, CPU_Free, Disk_Usage_Free," +
                     "Thread_Count, Process_ID, Status, Error_Logs) VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, containerID);
            statement.setBoolean(2, running);
            statement.setDouble(3, ramFree);
            statement.setDouble(4, cpuFree);
            statement.setDouble(5, diskUsageFree);
            statement.setInt(6, threadCount);
            statement.setString(7, processID);
            statement.setString(8, status);
            statement.setString(9, errorLogs);

            int rowsInserted = statement.executeUpdate();
            System.out.println(rowsInserted + " row(s) inserted.");

        } catch (SQLException error) {
            sqlErrorHandling(error);
        }
    }

    public ArrayList<Region> getRegions() {
        String sql = "SELECT * FROM Region";
        ArrayList<Region> regions = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("Region_ID");
                String name = resultSet.getString("Name");
                Region region = new Region(id, name);
                regions.add(region);
            }

        } catch (SQLException error) {
            sqlErrorHandling(error);
        }

        getCompanies(regions);

        return regions;
    }

    public void getCompanies(ArrayList<Region> regions) {
        String sql = "SELECT * FROM Company";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int companyID = resultSet.getInt("Company_ID");
                int regionId = resultSet.getInt("Region_ID");
                String name = resultSet.getString("Name");
                Company company = new Company(companyID, name);

                for (Region region : regions) {
                    if (region.getRegionID() == regionId) {
                        region.addCompany(company);
                    }
                }
            }

        } catch (SQLException error) {
            sqlErrorHandling(error);
        }

        getServers(regions);
    }

    public void getServers(ArrayList<Region> regions) {
        String sql = "SELECT * FROM Server";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String serverID = resultSet.getString("Server_ID");
                int companyID = resultSet.getInt("Company_ID");
                double ramTotal = resultSet.getDouble("Ram_Total");
                double cpuTotal = resultSet.getDouble("CPU_Total");
                double diskUsageTotal = resultSet.getDouble("Disk_Usage_Total");
                Server server = new Server(serverID, ramTotal, cpuTotal, diskUsageTotal);

                for (Region region : regions) {
                    Company company = region.getCompany(companyID);
                    if (company != null) {
                        company.addServer(server);
                    }
                }
            }

        } catch (SQLException error) {
            sqlErrorHandling(error);
        }

        getContainers(regions);
    }

    public void getContainers(ArrayList<Region> regions) {
        String sql = "SELECT * FROM Container";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String containerID = resultSet.getString("Container_ID");
                String serverID = resultSet.getString("Server_ID");
                Container container = new Container(containerID);

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
            sqlErrorHandling(error);
        }

        getDiagnosticsData(regions);
    }

    public void getDiagnosticsData(ArrayList<Region> regions) {
        String sql = "SELECT * FROM Diagnostics";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

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
            sqlErrorHandling(error);
        }
    }
}