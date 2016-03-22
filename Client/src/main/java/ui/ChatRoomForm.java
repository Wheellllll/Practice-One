package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Created by sweet on 3/21/16.
 */
public class ChatRoomForm {
    private JFrame frame = new JFrame("最简单的聊天室");
    private JTextArea messageArea = new JTextArea();
    private JTextField chatArea = new JTextField();
    private JButton sendBtn = new JButton("发送");


    public ChatRoomForm() {
        frame.setSize(500, 300);
        initFrame();
        frame.setVisible(true);
    }

    private void initFrame() {
        //底部发送区域
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(chatArea, BorderLayout.CENTER);
        bottomPanel.add(sendBtn, BorderLayout.EAST);

        //顶部消息区域
        messageArea.setEditable(false);

        container.add(messageArea, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(container);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public JFrame getFrame() {
        return frame;
    }

    public void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public String getSendMessage() {
        return chatArea.getText();
    }

    public void addMessage(String from, String message) {
        messageArea.append(from + ":" + message + "\n");
    }

    public void clearChatArea() {
        chatArea.setText("");
    }

    public void setOnSendListener(ActionListener listener) {
        sendBtn.addActionListener(listener);
    }
}
