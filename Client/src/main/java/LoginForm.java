import javax.swing.*;

/**
 * Created by sweet on 3/21/16.
 */
public class LoginForm {
    private JPanel panel1;
    private JTextField textField1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("LoginForm");
        frame.setContentPane(new LoginForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
