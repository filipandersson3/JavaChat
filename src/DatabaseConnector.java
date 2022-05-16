import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnector {
    Connection conn;

    public DatabaseConnector() {
        try {
            // Set up connection to database
            conn = DriverManager.getConnection(
                    "jdbc:mysql://"+ DataBaseLoginData.DBurl +":" + DataBaseLoginData.port + "/" +
                            DataBaseLoginData.DBname + "? " +
                            "allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                    DataBaseLoginData.user, DataBaseLoginData.password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to log in, check your database and credentials and try again. Shutting down...");
            System.exit(0);
        }
    }


}