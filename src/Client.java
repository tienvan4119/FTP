import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Client extends Component {
    private JTextField txtUsername;
    private JTextField txtHost;
    private JTextField txtPassword;
    private JTextField txtPort;
    private JPanel panelMain;
    private JButton btnLogin;
    private JButton uploadButton;
    private JTree tree2;
    private JTextField txtPath;
    private JButton downloadButton;

    public Client() {
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int returnVal = fc.showOpenDialog(Client.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();

                    //This is where a real application would open the file.
                    System.out.println("Opening: " + file.getAbsolutePath());
                    txtPath.setText(file.getAbsolutePath());
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Client");
        frame.setContentPane(new Client().panelMain);
        frame.setVisible(true);
    }
}
