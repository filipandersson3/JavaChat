import javax.swing.*;
import java.util.Scanner;

public class ServerController {
    private String ip;
    private int port;
    private Model server;

    public static void main(String[] args) {
        ServerController serverController = new ServerController();
    }

    public ServerController() {
        ip = "localhost";
        port = Integer.parseInt(JOptionPane.showInputDialog(null,"Port?","Connect to..",JOptionPane.QUESTION_MESSAGE));
        Scanner tgb = new Scanner(System.in);
        server = new Model(ip,port);
        server.ServerStart();
        while (true) {
            while(tgb.hasNext()) {
                server.send(tgb.nextLine());
            }
        }

    }
}
