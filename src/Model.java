import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Model {
    private String ip;
    private int port;
    private Socket socket;
    private ServerSocket serverSocket;
    private ArrayList<PrintWriter> out = new ArrayList<>();
    private ListenerThread in;
    private String motd;

    public Model(String ip, int port, String motd) {
        this.ip = ip;
        this.port = port;
        this.motd = motd;
    }

    public void ClientStart() {
        try {
            socket = new Socket(ip,port);
        } catch (IOException e) {
            System.out.println("Client failed to connect");
            System.exit(0);
        }
        //Connected
        try {
            out.add(new PrintWriter(socket.getOutputStream(),true));
            in = new ListenerThread(new BufferedReader(new InputStreamReader(socket.getInputStream()
            )),false);
            Thread listener = new Thread(in);
            listener.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ServerStart() {
        System.out.println("Server started.");
        DatabaseConnector DBConnector = new DatabaseConnector();
        try {
            // Try to start a server socket on the port
            serverSocket = new ServerSocket(port);
            System.out.println("Waiting for connections!");

            // Start thread for handling clients connecting and sending messages to server
            ClientHandlerThread clientHandlerThread = new ClientHandlerThread(serverSocket,out,DBConnector,motd);
            Thread clientHandler = new Thread(clientHandlerThread);
            clientHandler.start();

        } catch (IOException e) {
            System.out.println("Server fail, try restarting on another port");
        }
    }

    public void send(String msg) {
        // Print to every printwriter
        for (PrintWriter pw:
             out) {
            pw.println(msg);
        }
    }

    public void stop() {
        // Close printwriters and close socket
        for (PrintWriter pw:
             out) {
            pw.close();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done!");
    }

    public ListenerThread getIn() {
        return in;
    }

}
