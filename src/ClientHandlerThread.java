import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This is a class
 * Created 2021-03-16
 *
 * @author Magnus Silverdal
 */
public class ClientHandlerThread implements Runnable{
    private ServerSocket serverSocket;
    private ArrayList<Socket> connections = new ArrayList<>();
    private ArrayList<PrintWriter> out = new ArrayList<>();

    public ClientHandlerThread(ServerSocket serverSocket, ArrayList<PrintWriter> out) {
        this.serverSocket = serverSocket;
        this.out = out;
    }

    @Override
    public void run() {
        ConnectionListener connectionListener = new ConnectionListener(serverSocket);
        Thread connectionThread = new Thread(connectionListener);
        connectionThread.start();
        int connectionCount = 1;
        while (true) {
            System.out.print("");
            if (connectionListener.getConnections().size() >= connectionCount) {
                System.out.println("connected");
                Socket connection = connectionListener.getConnections().get(connectionCount-1);
                ListenerThread in =
                        null;
                try {
                    in = new ListenerThread(new BufferedReader(new InputStreamReader(connection.getInputStream())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread listener = new Thread(in);
                listener.start();
                try {
                    out.add(new PrintWriter(connection.getOutputStream(), true));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.get(connectionCount-1).println("Welcome idiot");
                out.get(connectionCount-1).println("Your ID: " + (connectionCount-1));
                connectionCount++;
            }
        }
    }

    public void stop()  {

    }

    public ArrayList<Socket> getConnections() {
        return connections;
    }
}