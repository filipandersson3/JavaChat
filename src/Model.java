import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Model {
    private String ip;
    private int port;
    private Socket socket;
    private ServerSocket serverSocket;
    private ArrayList<PrintWriter> out = new ArrayList<>();
    private ClientListenerThread in;

    public Model(String ip, int port) {
        this.ip = ip;
        this.port = port;
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
            in = new ClientListenerThread(new BufferedReader(new InputStreamReader(socket.getInputStream()
            )));
            Thread listener = new Thread(in);
            listener.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ServerStart() {
        System.out.println("Server started.");
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Waiting for connections!");
            // Go
            ClientHandlerThread clientHandlerThread = new ClientHandlerThread(serverSocket,out);
            Thread clientHandler = new Thread(clientHandlerThread);
            clientHandler.start();

                //Protocol
        } catch (IOException e) {
            System.out.println("Server fail");
        }
    }

    public void send(String msg) {
        for (PrintWriter pw:
             out) {
            pw.println(msg);
        }
    }

    public void stop() {
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

    public ClientListenerThread getIn() {
        return in;
    }
}
