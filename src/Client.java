import javax.swing.*;

public class Client {
    private JTextField txtUsername;
    private JTextField txtHost;
    private JTextField txtPassword;
    private JTextField txtPort;
    private JPanel panelMain;
    private JButton btnLogin;
    private JTextArea textArea1;
    private JTextArea textArea2;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Client");
        frame.setContentPane(new Client().panelMain);
        frame.setVisible(true);
    }
}
