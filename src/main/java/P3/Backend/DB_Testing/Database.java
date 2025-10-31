package P3.Backend.DB_Testing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class Database {
    String url = "jdbc:postgresql://localhost:5432/P3DB";
    String user = "postgres";
    String password = "SQLvmDBaccess";

    void addRegion(String name) {
        String sql = "INSERT INTO Region (Name) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);

            int rowsInserted = statement.executeUpdate();
            System.out.println(rowsInserted + " row(s) inserted.");

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    void addCompany(int regionID, String name) {
        String sql = "INSERT INTO Company (Region_ID, Name) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, regionID);
            statement.setString(2, name);

            int rowsInserted = statement.executeUpdate();
            System.out.println(rowsInserted + " row(s) inserted.");

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    void addServer(String serverID, int companyID, double ramTotal, double cpuTotal, double diskUsageTotal) {
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
            error.printStackTrace();
        }
    }

    void addContainer(String containerID, String serverID) {
        String sql = "INSERT INTO Container (Container_ID, Server_ID) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, containerID);
            statement.setString(2, serverID);

            int rowsInserted = statement.executeUpdate();
            System.out.println(rowsInserted + " row(s) inserted.");

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    // Timestamp example: '2025-10-24 10:00:00'
    void addDiagnostics(String containerID, boolean running, double ramFree, double cpuFree,
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
            error.printStackTrace();
        }
    }
}