package P3.Backend.DB_Testing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PostgresExample {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/P3DB";
        String user = "postgres";
        String password = "SQLvmDBaccess";

        insertStuff2(url, user, password);
        readStuff(url, user, password);
        readStuff2(url, user, password);
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

    static void readStuff2(String url, String user, String password) {
        // SQL query to get servers where the diagnostics network is false
        String sql = "SELECT DISTINCT d.Server " +
                "FROM Docker d " +
                "JOIN Container c ON d.Docker_ID = c.Docker_ID " +
                "JOIN Diagnostics diag ON c.Container_ID = diag.Container_ID " +
                "WHERE diag.Network = FALSE;";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Servers with network = FALSE:");
            while (rs.next()) {
                String server = rs.getString("Server");
                System.out.println(" - " + server);
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

    static void insertStuff2(String url, String user, String password) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false); // so we can commit all inserts together

            // 1️⃣ Insert the new Docker server
            String insertDocker = "INSERT INTO Docker (Server) VALUES (?) RETURNING Docker_ID;";
            int dockerId;

            try (PreparedStatement dockerStmt = conn.prepareStatement(insertDocker)) {
                dockerStmt.setString(1, "Server_I");
                try (ResultSet rs = dockerStmt.executeQuery()) {
                    rs.next();
                    dockerId = rs.getInt("Docker_ID");
                }
            }

            // 2️⃣ Insert multiple Containers linked to that Docker_ID
            String insertContainer = "INSERT INTO Container (Docker_ID) VALUES (?) RETURNING Container_ID;";
            int[] containerIds = new int[3]; // for example, 3 containers

            try (PreparedStatement containerStmt = conn.prepareStatement(insertContainer)) {
                for (int i = 0; i < containerIds.length; i++) {
                    containerStmt.setInt(1, dockerId);
                    try (ResultSet rs = containerStmt.executeQuery()) {
                        rs.next();
                        containerIds[i] = rs.getInt("Container_ID");
                    }
                }
            }

            // 3️⃣ Insert Diagnostics for each container where Network = FALSE
            String insertDiag = "INSERT INTO Diagnostics (" +
                    "Container_ID, Timestamp, Running, Ram_Total, Ram_Free, Network, " +
                    "CPU_Total, CPU_Free, Status, Disk_Usage_Total, Disk_Usage_Free" +
                    ") VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            try (PreparedStatement diagStmt = conn.prepareStatement(insertDiag)) {
                for (int containerId : containerIds) {
                    diagStmt.setInt(1, containerId);
                    diagStmt.setBoolean(2, true); // Running
                    diagStmt.setDouble(3, 16.0);
                    diagStmt.setDouble(4, 10.0);
                    diagStmt.setBoolean(5, false); // Network = FALSE
                    diagStmt.setDouble(6, 4.0);
                    diagStmt.setDouble(7, 1.0);
                    diagStmt.setString(8, "NetworkDown");
                    diagStmt.setDouble(9, 500.0);
                    diagStmt.setDouble(10, 200.0);
                    diagStmt.executeUpdate();
                }
            }

            conn.commit();
            System.out.println("✅ Server 'Server_I' with containers (Network = FALSE) inserted successfully!");

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