import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionListener implements Runnable{
    private ServerSocket serverSocket;
    private ArrayList<Socket> connections = new ArrayList<>();

    public ConnectionListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        //Listens for and adds connections
        String msg = null;
        while (true) {
            try {
                connections.add(serverSocket.accept());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop()  {

    }

    public ArrayList<Socket> getConnections() {
        return connections;
    }
}