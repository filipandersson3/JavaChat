import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class ListenerThread implements Runnable{
    private BufferedReader in;
    private String msg = null;
    private Queue<String> msgQueue = new LinkedList<String>();
    private boolean isServer;

    public ListenerThread(BufferedReader in, boolean isServer) {
        this.in = in;
        this.isServer = isServer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                msg = in.readLine();
            } catch (IOException e) {
            }
            if (msg != null) {
                System.out.println(msg);
                msgQueue.add(msg);
            }
        }
    }

    public void stop()  {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMsg() {
        return msg;
    }

    public Queue<String> getMsgQueue() {
        return msgQueue;
    }
}