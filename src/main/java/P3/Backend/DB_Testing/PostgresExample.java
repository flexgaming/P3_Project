package P3.Backend.DB_Testing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class PostgresExample {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/P3DB";
        String user = "postgres";
        String password = "SQLvmDBaccess";

        insertDeeper(url, user, password);
        readStuff(url, user, password);
    }

    static void readStuff(String url, String user, String password) {
        String sql = "SELECT c.Container_ID, d.Docker_ID, d.Server, diag.Timestamp, diag.Network " +
                "FROM Docker d " +
                "JOIN Container c ON d.Docker_ID = c.Docker_ID " +
                "JOIN Diagnostics diag ON c.Container_ID = diag.Container_ID ";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int containerId = rs.getInt("Container_ID");
                int dockerId = rs.getInt("Docker_ID");
                String server = rs.getString("Server");
                java.sql.Timestamp timestamp = rs.getTimestamp("Timestamp");
                boolean network = rs.getBoolean("Network");

                System.out.println("Container_ID: " + containerId +
                        ", Docker_ID: " + dockerId +
                        ", Server: " + server +
                        ", Timestamp: " + timestamp +
                        ", Network: " + network);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void insertStuff(String url, String user, String password) {
        String sql = "INSERT INTO Docker (Server) VALUES " +
                "('Server_E'), " +
                "('Server_F'), " +
                "('Server_G'), " +
                "('Server_H');";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            int rowsInserted = stmt.executeUpdate(sql);
            System.out.println(rowsInserted + " rows inserted successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void insertDeeper(String url, String user, String password) {
        // SQL for inserting into Container
        String containerSql = "INSERT INTO Container (Docker_ID) VALUES " +
                "(9), (5), (6), (7), (7), (8);";

        // SQL for inserting into Diagnostics
        String diagnosticsSql = "INSERT INTO Diagnostics (" +
                "Container_ID, Timestamp, Running, Ram_Total, Ram_Free, Network, " +
                "CPU_Total, CPU_Free, Status, Disk_Usage_Total, Disk_Usage_Free" +
                ") VALUES " +
                "(7, '2025-10-26 09:30:00', TRUE, 32.0, 28.0, TRUE, 8.0, 6.5, 'OK', 1000.0, 700.0), " +
                "(5, '2025-10-26 09:35:00', FALSE, 16.0, 16.0, FALSE, 4.0, 4.0, 'Stopped', 500.0, 500.0), " +
                "(6, '2025-10-26 09:40:00', TRUE, 8.0, 3.5, TRUE, 2.0, 0.8, 'Warning', 250.0, 100.0), " +
                "(8, '2025-10-26 09:45:00', TRUE, 16.0, 12.0, TRUE, 4.0, 3.0, 'OK', 500.0, 300.0), " +
                "(5, '2025-10-26 09:50:00', TRUE, 32.0, 20.0, TRUE, 8.0, 5.0, 'OK', 1000.0, 600.0), " +
                "(7, '2025-10-26 09:55:00', FALSE, 16.0, 16.0, FALSE, 4.0, 4.0, 'Stopped', 500.0, 500.0);";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            int containerRows = stmt.executeUpdate(containerSql);
            System.out.println(containerRows + " rows inserted into Container.");

            int diagnosticsRows = stmt.executeUpdate(diagnosticsSql);
            System.out.println(diagnosticsRows + " rows inserted into Diagnostics.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}