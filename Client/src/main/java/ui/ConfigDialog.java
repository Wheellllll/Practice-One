package ui;

import client.ConfigBean;
import octoteam.tahiti.config.ConfigManager;
import octoteam.tahiti.config.loader.JsonAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Provide a GUI of the dialog for the client. Users can config host and port in this dialog
 */
public class ConfigDialog {
    private JDialog dialog;
    private Container c;
    JTextField hostField = new JTextField();
    JTextField portField = new JTextField();
    JButton okBtn = new JButton("确定");
    JButton cancelBtn = new JButton("取消");


    public ConfigDialog(JFrame frame) {
        dialog = new JDialog(frame, "设置", true);
        dialog.setSize(300, 150);
        c = dialog.getContentPane();
        c.setLayout(new BorderLayout());
        try {
            initDialog();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.setVisible(true);
    }

    private void initDialog() throws IOException {
        //中部表单
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(null);
        JLabel l1 = new JLabel("服务器:");
        l1.setBounds(50, 20, 50, 20);
        JLabel l2 = new JLabel("端     口:");
        l2.setBounds(50, 60, 50, 20);
        fieldPanel.add(l1);
        fieldPanel.add(l2);
        ConfigManager configManager = new ConfigManager(new JsonAdapter(), "./ClientConfig.json");
        ConfigBean configBean = configManager.loadToBean(ConfigBean.class);
        hostField.setBounds(110, 20, 120, 20);
        hostField.setText(configBean.getHost());
        portField.setBounds(110, 60, 120, 20);
        portField.setText(String.valueOf(configBean.getPort()));
        fieldPanel.add(hostField);
        fieldPanel.add(portField);
        c.add(fieldPanel, "Center");

        //底部按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        c.add(buttonPanel, "South");

        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String host = hostField.getText();
                String port = portField.getText();
                configBean.setHost(host);
                configBean.setPort(Integer.parseInt(port));
                try {
                    configManager.writeToFirstCandidate(configBean);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
            }
        });

    }

}
