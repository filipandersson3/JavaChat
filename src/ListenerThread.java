import java.io.BufferedReader;
import java.io.IOException;

public class ListenerThread implements Runnable{
    private BufferedReader in;
    private String msg = null;

    public ListenerThread(BufferedReader in) {
        this.in = in;
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
}