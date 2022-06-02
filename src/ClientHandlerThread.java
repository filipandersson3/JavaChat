import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientHandlerThread implements Runnable{
    private ServerSocket serverSocket;
    private ArrayList<Socket> connections = new ArrayList<>();
    private ArrayList<PrintWriter> out = new ArrayList<>();
    private ArrayList<ListenerThread> inList = new ArrayList<>();
    private DatabaseConnector DBConnector;
    private HashMap<Integer, String> loggedInList = new HashMap<Integer, String>();
    private String motd = "";

    public ClientHandlerThread(ServerSocket serverSocket, ArrayList<PrintWriter> out,
                               DatabaseConnector DBConnector, String motd) {
        this.serverSocket = serverSocket;
        this.out = out;
        this.DBConnector = DBConnector;
        this.motd = motd;
    }

    @Override
    public void run() {
        // Keep track of connections
        ConnectionListener connectionListener = new ConnectionListener(serverSocket);
        Thread connectionThread = new Thread(connectionListener);
        connectionThread.start();
        int connectionCount = 1;

        while (true) {
            System.out.print("");
            // When somebody connects, create a listenerthread for them and a way to write to them
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
                inList.add(in);
                try {
                    out.add(new PrintWriter(connection.getOutputStream(), true));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.get(connectionCount-1).println(motd);
                // give client id to identify them
                out.get(connectionCount-1).println("Your ID: " + (connectionCount-1));
                connectionCount++;
            }
            // watch for if anyone has sent a message
            for (ListenerThread in:
                 inList) {
                String msg = in.getMsgQueue().peek();
                if (!in.getMsgQueue().isEmpty() && msg != null) {
                    // login, check with database
                    if (msg.startsWith("/login")) {
                        int id = Integer.parseInt(msg.split("id:")[1].split(" ")[0]);
                        String name = msg.split(" username:")[1];
                        name = name.split(" password:")[0];
                        String password = msg.split(" password:")[1];
                        if (DBConnector.Login(name,password)) {
                            loggedInList.put(id,name);
                            out.get(id).println("Logged in!");
                        } else {
                            out.get(id).println("Failed to login.");
                        }
                    // signup info sent to database and client gets logged in
                    } else if (msg.startsWith("/signup")) {
                        int id = Integer.parseInt(msg.split("id:")[1].split(" ")[0]);
                        String name = msg.split(" username:")[1];
                        name = name.split(" password:")[0];
                        String password = msg.split(" password:")[1];
                        if (DBConnector.Signup(name,password)) {
                            loggedInList.put(id,name);
                            out.get(id).println("Signed up and logged in!");
                        } else {
                            out.get(id).println("Failed to create a new user.");
                        }
                    // send message connected to user to everyone if user logged in
                    } else if(msg.startsWith("/msg")) {
                        int id = Integer.parseInt(msg.split("id:")[1].split(" ")[0]);
                        msg = msg.split("msg:")[1];
                        if (loggedInList.containsKey(id)) {
                            for (PrintWriter clientOut:
                                    out) {
                                clientOut.println(loggedInList.get(id) + ": " + msg);
                            }
                        } else {
                            out.get(id).println("Must be logged in to chat.");
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