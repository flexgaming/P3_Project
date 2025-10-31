package P3.Backend.DB_Testing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
    String url = "jdbc:postgresql://localhost:5432/P3DB";
    String user = "postgres";
    String password = "SQLvmDBaccess";

    void createRegion(String name) {
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
}