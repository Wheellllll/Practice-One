import javax.swing.*;

/**
 * Created by sweet on 3/21/16.
 */
public class LoginAndRegisterForm {
    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JButton registerButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("LoginAndRegisterForm");
        frame.setContentPane(new LoginAndRegisterForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
