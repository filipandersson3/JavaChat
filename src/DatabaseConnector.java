import javax.swing.*;
import java.sql.*;
import java.util.Objects;

public class DatabaseConnector {
    Connection conn;
    String DBurl;
    String DBport;
    String DBname;
    String DBuser;
    String DBpassword;

    public DatabaseConnector() {
        DBurl = JOptionPane.showInputDialog(null,
                "Database url?","Database setup",JOptionPane.QUESTION_MESSAGE);
        DBport = JOptionPane.showInputDialog(null,
                "Database port?","Database setup",JOptionPane.QUESTION_MESSAGE);
        DBname = JOptionPane.showInputDialog(null,
                "Database name?","Database setup",JOptionPane.QUESTION_MESSAGE);
        DBuser = JOptionPane.showInputDialog(null,
                "Database user?","Database setup",JOptionPane.QUESTION_MESSAGE);
        DBpassword = JOptionPane.showInputDialog(null,
                "Database password?","Database setup",JOptionPane.QUESTION_MESSAGE);
        try {
            // Set up connection to database
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + DBurl + ":" + DBport + "/" +
                            DBname + "? " +
                            "allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                    DBuser, DBpassword);
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
            // check for existing user, otherwise add new to database
            Statement stmt = conn.createStatement();

            String SQLQuery = "SELECT * FROM fipann_users WHERE name=\"" + name + "\";";
            ResultSet rset = stmt.executeQuery(SQLQuery);

            if (!rset.next()) {
                stmt = conn.createStatement();

                SQLQuery = "INSERT INTO fipann_users (name, password) " +
                        "VALUES (\"" + name + "\", \"" + password + "\");";
                int result = stmt.executeUpdate(SQLQuery);
                return result > 0;
            }
            return false;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}