import BCrypt.BCrypt;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class ClientController {
    private String ip;
    private int port;
    private Model client;
    private ClientView view;
    private String id;

    public static void main(String[] args) {
        ClientController clientController = new ClientController();
    }

    public ClientController() {
        ip = JOptionPane.showInputDialog(null,"IP?","Connect to..",JOptionPane.QUESTION_MESSAGE);
        port = Integer.parseInt(JOptionPane.showInputDialog(null,"Port?","Connect to..",JOptionPane.QUESTION_MESSAGE));
        Scanner tgb = new Scanner(System.in);
        client = new Model(ip,port,"");
        client.ClientStart();
        view = new ClientView();

        view.getSendButton().addActionListener(new SendAL());
        view.getLoginButton().addActionListener(new LoginAL());
        view.getSignupButton().addActionListener(new SignupAL());
        //print server messages, except for id message which is saved as id
        while (true) {
            String msg = client.getIn().getMsgQueue().peek();
            if(!client.getIn().getMsgQueue().isEmpty() && msg != null) {
                if (msg.startsWith("Your ID: ")) {
                    id = msg.split("Your ID: ")[1];
                } else {
                    view.showMsg(msg);
                }
                client.getIn().getMsgQueue().poll();
            }
            System.out.print("");
        }

    }

    private class SendAL implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            client.send("/msg id:" + id + " msg:" + view.getInput());
            view.clearInput();
        }
    }

    private class LoginAL implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String name = JOptionPane.showInputDialog(null,"Enter Name");
            String password = JOptionPane.showInputDialog(null,"Enter Password");
            password = BCrypt.hashpw(password,"$2a$10$eSDBgW/bUUywmjzoJmehuu");
            client.send("/login id:" + id + " username:" + name + " password:" + password);
        }
    }

    private class SignupAL implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String name = JOptionPane.showInputDialog(null,"Enter New Name");
            String password = JOptionPane.showInputDialog(null,"Enter New Password");
            password = BCrypt.hashpw(password,"$2a$10$eSDBgW/bUUywmjzoJmehuu");
            client.send("/signup id:" + id + " username:" + name + " password:" + password);
        }
    }
}
