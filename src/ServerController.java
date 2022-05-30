import javax.swing.*;
import java.io.PrintWriter;
import java.util.Scanner;

public class ServerController {
    private String ip;
    private int port;
    private Model server;
    private String motd;

    public static void main(String[] args) {
        ServerController serverController = new ServerController();
    }

    public ServerController() {
        // Get IP, port and message of the day from the user
        ip = "localhost";
        port = Integer.parseInt(JOptionPane.showInputDialog(null,
                "Port?","Host a server",JOptionPane.QUESTION_MESSAGE));
        motd = JOptionPane.showInputDialog(null,
                "Message of the day (all users will see this on connect)",
                "Host a server",JOptionPane.QUESTION_MESSAGE);

        // Scanner for writing directly to all clients
        Scanner tgb = new Scanner(System.in);

        // Start server
        server = new Model(ip,port,motd);
        server.ServerStart();

        // Scanner send loop
        while (true) {
            while(tgb.hasNext()) {
                server.send(tgb.nextLine());
            }
        }

    }
}
