import javax.swing.*;
import java.util.Scanner;

public class ClientController {
    private String ip;
    private int port;
    private Model client;

    public static void main(String[] args) {
        ClientController clientController = new ClientController();
    }

    public ClientController() {
        ip = (String) JOptionPane.showInputDialog(null,"IP?","Connect to..",JOptionPane.QUESTION_MESSAGE);
        port = Integer.parseInt(JOptionPane.showInputDialog(null,"Port?","Connect to..",JOptionPane.QUESTION_MESSAGE));
        Scanner tgb = new Scanner(System.in);
        client = new Model(ip,port);
        client.ClientStart();
        while (true) {
            while(tgb.hasNext()) {
                client.send(tgb.nextLine());
            }
        }

    }
}
