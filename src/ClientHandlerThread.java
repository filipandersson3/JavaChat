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
    private ArrayList<ListenerThread> inList = new ArrayList<>();

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
                    in = new ListenerThread(new BufferedReader(new InputStreamReader(connection.getInputStream())),true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread listener = new Thread(in);
                listener.start();
                inList.add(in);
                try {
                    out.add(new PrintWriter(connection.getOutputStream(), true));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.get(connectionCount-1).println("Welcome idiot");
                out.get(connectionCount-1).println("Your ID: " + (connectionCount-1));
                connectionCount++;
            }
            for (ListenerThread in:
                 inList) {
                String msg = in.getMsgQueue().peek();
                if (!in.getMsgQueue().isEmpty() && msg != null) {
                    if (msg.startsWith("/login")) {
                        String name = msg.split(" username:")[1];
                        name = name.split(" password:")[0];
                        String password = msg.split(" password:")[1];
                        System.out.println(name);
                        System.out.println(password);
                    } else {
                        for (PrintWriter clientOut:
                                out) {
                            clientOut.println(in.getMsgQueue().peek());
                            System.out.println("ello");
                        }
                    }
                    in.getMsgQueue().poll();
                }
            }
        }
    }

    public void stop()  {

    }

    public ArrayList<Socket> getConnections() {
        return connections;
    }
}