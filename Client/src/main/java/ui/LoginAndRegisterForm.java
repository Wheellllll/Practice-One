package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Created by sweet on 3/21/16.
 */
public class LoginAndRegisterForm {
    private JFrame frame = new JFrame("欢迎界面");
    private Container c = frame.getContentPane();
    private JTextField username = new JTextField();
    private JPasswordField password = new JPasswordField();
    private JButton loginBtn = new JButton("登陆");
    private JButton registerBtn = new JButton("注册");
    private JButton configBtn = new JButton("设置");
    private JLabel errorLabel = new JLabel("错误");
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
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        errorPanel.add(errorLabel);
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

    public void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public String getUsername() {
        return username.getText();
    }

    public String getPassword() {
        return new String(password.getPassword());
    }

    public void setError(String error) {
        errorLabel.setVisible(true);
        errorLabel.setText(error);
    }

    public void setOnLoginListener(ActionListener listener) {
        loginBtn.addActionListener(listener);
    }

    public void setOnRegisterListener(ActionListener listener) {
        registerBtn.addActionListener(listener);
    }

    public void setOnConfigListener(ActionListener listener) {
        configBtn.addActionListener(listener);
    }
}
