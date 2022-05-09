import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class ClientController {
    private String ip;
    private int port;
    private Model client;
    private ClientView view;

    public static void main(String[] args) {
        ClientController clientController = new ClientController();
    }

    public ClientController() {
        ip = (String) JOptionPane.showInputDialog(null,"IP?","Connect to..",JOptionPane.QUESTION_MESSAGE);
        port = Integer.parseInt(JOptionPane.showInputDialog(null,"Port?","Connect to..",JOptionPane.QUESTION_MESSAGE));
        Scanner tgb = new Scanner(System.in);
        client = new Model(ip,port);
        client.ClientStart();
        view = new ClientView();
        JFrame frame = new JFrame("Chat");
        frame.setContentPane(view.getPanel1());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        view.getSendButton().addActionListener(new SendAL());
        view.getLoginButton().addActionListener(new LoginAL());
        view.getSignupButton().addActionListener(new SignupAL());
        while (true) {
            boolean isEmpty = client.getIn().getMsgQueue().isEmpty();
            if(!isEmpty) {
                System.out.println("test");
                view.getChatTextArea().append(client.getIn().getMsgQueue().poll());
            }
            System.out.print("");
        }

    }

    private class SendAL implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            client.send(view.getMsgField().getText());
            view.getMsgField().setText("");
        }
    }

    private class LoginAL implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("login");
        }
    }

    private class SignupAL implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("sign up");
        }
    }
}
