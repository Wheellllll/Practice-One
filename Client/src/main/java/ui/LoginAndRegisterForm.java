package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Provide a GUI of the login and registerForward for the client. Users need to login or registerForward in the GUI.
 */
public class LoginAndRegisterForm {
    private JFrame frame = new JFrame("欢迎界面");
    private Container c = frame.getContentPane();
    private JTextField username = new JTextField();
    private JPasswordField password = new JPasswordField();
    private JButton loginBtn = new JButton("登陆");
    private JButton registerBtn = new JButton("注册");
    private JButton configBtn = new JButton("设置");
    private JLabel hintLabel = new JLabel("错误");
    public LoginAndRegisterForm(){
        frame.setSize(300,200);
        c.setLayout(new BorderLayout());
        initFrame();
        frame.setVisible(true);
    }

    private void initFrame() {

        //顶部
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        titlePanel.add(new JLabel("Naive客户端"));
        c.add(titlePanel,"North");

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BorderLayout());
        c.add(formPanel, "Center");

        //中部表单
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(null);
        JLabel l1 = new JLabel("用户名:");
        l1.setBounds(50, 20, 50, 20);
        JLabel l2 = new JLabel("密    码:");
        l2.setBounds(50, 60, 50, 20);
        fieldPanel.add(l1);
        fieldPanel.add(l2);
        username.setBounds(110,20,120,20);
        password.setBounds(110,60,120,20);
        fieldPanel.add(username);
        fieldPanel.add(password);
        formPanel.add(fieldPanel, "Center");

        //中下部错误信息
        JPanel errorPanel = new JPanel();
        errorPanel.setLayout(new FlowLayout());
        hintLabel.setVisible(false);
        errorPanel.add(hintLabel);
        formPanel.add(errorPanel, "South");

        //底部按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        buttonPanel.add(configBtn);
        c.add(buttonPanel,"South");

    }

    public JFrame getFrame() {
        return frame;
    }

    /**
     * Close this form
     */
    public void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Get username that user type in the text field
     * @return String of the username
     */
    public String getUsername() {
        return username.getText();
    }

    /**
     * Get password that user type in the text field
     * @return String of the password
     */
    public String getPassword() {
        return new String(password.getPassword());
    }

    /**
     * Set error notification in the GUI
     * @param error Error notification
     */
    public void setError(String error) {
        hintLabel.setForeground(Color.RED);
        hintLabel.setText(error);
        hintLabel.setVisible(true);
    }

    /**
     * Set correct message in the GUI
     * @param correct correct notification
     */
    public void setCorrect(String correct) {
        hintLabel.setForeground(Color.GREEN);
        hintLabel.setText(correct);
        hintLabel.setVisible(true);
    }

    /**
     * Add a listener to the login event
     * @param listener listener called when login
     */
    public void setOnLoginListener(ActionListener listener) {
        loginBtn.addActionListener(listener);
    }

    /**
     * Add a listener to the registerForward event
     * @param listener listener called when registerForward
     */
    public void setOnRegisterListener(ActionListener listener) {
        registerBtn.addActionListener(listener);
    }

    /**
     * Add a listener to the config event
     * @param listener listener called when config
     */
    public void setOnConfigListener(ActionListener listener) {
        configBtn.addActionListener(listener);
    }
}
