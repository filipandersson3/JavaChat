import javax.swing.*;

public class ClientView {
    private JTextArea chatTextArea;
    private JTextField msgField;
    private JButton sendButton;
    private JButton signupButton;
    private JButton loginButton;
    private JPanel panel1;

    public ClientView() {
        JFrame frame = new JFrame("Chat");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public JButton getSignupButton() {
        return signupButton;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JTextArea getChatTextArea() {
        return chatTextArea;
    }

    public JTextField getMsgField() {
        return msgField;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void showMsg(String msg) {
        chatTextArea.append(msg + "\n");
    }

    public String getInput() {
        return msgField.getText();
    }

    public void clearInput() {
        msgField.setText("");
    }
}
