package P3.Backend.Database;

import P3.Backend.Constants;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class Database {
    String url = Constants.DATABASE_URL;
    String user = Constants.DATABASE_USER;
    String password = Constants.DATABASE_PASSWORD;

    private void errorHandling(SQLException error) {
        error.printStackTrace();
    }

    public void addRegion(String name) {
        String sql = "INSERT INTO Region (Name) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);

            int rowsInserted = statement.executeUpdate();
            System.out.println(rowsInserted + " row(s) inserted.");

        } catch (SQLException error) {
            errorHandling(error);
        }
    }

    public void addCompany(int regionID, String name) {
        String sql = "INSERT INTO Company (Region_ID, Name) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, regionID);
            statement.setString(2, name);

            int rowsInserted = statement.executeUpdate();
            System.out.println(rowsInserted + " row(s) inserted.");

        } catch (SQLException error) {
            errorHandling(error);
        }
    }

    public void addServer(String serverID, int companyID, double ramTotal, double cpuTotal, double diskUsageTotal) {
        String sql = "INSERT INTO Server (Server_ID, Company_ID, Ram_Total, CPU_Total, Disk_Usage_Total)" +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, serverID);
            statement.setInt(2, companyID);
            statement.setDouble(3, ramTotal);
            statement.setDouble(4, cpuTotal);
            statement.setDouble(5, diskUsageTotal);

            int rowsInserted = statement.executeUpdate();
            System.out.println(rowsInserted + " row(s) inserted.");

        } catch (SQLException error) {
            errorHandling(error);
        }
    }

    public void addContainer(String containerID, String serverID) {
        String sql = "INSERT INTO Container (Container_ID, Server_ID) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, containerID);
            statement.setString(2, serverID);

            int rowsInserted = statement.executeUpdate();
            System.out.println(rowsInserted + " row(s) inserted.");

        } catch (SQLException error) {
            errorHandling(error);
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
            errorHandling(error);
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
            errorHandling(error);
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
            errorHandling(error);
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
            errorHandling(error);
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
            errorHandling(error);
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
            errorHandling(error);
        }
    }
}