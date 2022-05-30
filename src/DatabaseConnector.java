import java.sql.*;
import java.util.Objects;

public class DatabaseConnector {
    Connection conn;

    public DatabaseConnector() {
        try {
            // Set up connection to database
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + DataBaseLoginData.DBurl + ":" + DataBaseLoginData.port + "/" +
                            DataBaseLoginData.DBname + "? " +
                            "allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                    DataBaseLoginData.user, DataBaseLoginData.password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to log in, check your database and credentials and try again. Shutting down...");
            System.exit(0);
        }
    }

    public boolean Login(String name, String password) {
        try {
            // Setup statement
            Statement stmt = conn.createStatement();

            // Create query and execute
            String SQLQuery = "SELECT * FROM fipann_users WHERE name=\"" + name + "\";";
            ResultSet rset = stmt.executeQuery(SQLQuery);

            while (rset.next()) {
                System.out.println(rset.getString("password"));
                if (Objects.equals(password, rset.getString("password"))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Something went wrong, check your tablestructure...");
        }
        return false;
    }


    public boolean Signup(String name, String password) {
        try {
            // Setup statement
            Statement stmt = conn.createStatement();

            // Create query and execute
            String SQLQuery = "INSERT INTO fipann_users (name, password) " +
                    "VALUES (\"" + name + "\", \"" + password + "\");";
            int result = stmt.executeUpdate(SQLQuery);
            return result > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}