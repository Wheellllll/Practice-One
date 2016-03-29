package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Provide a GUI of the chat rom for the client. Users use this GUI to interact with the server.
 */
public class ChatRoomForm {
    private JFrame frame = new JFrame("最简单的聊天室");
    private JTextArea messageArea = new JTextArea();
    private JTextArea chatArea = new JTextArea(3, 30);
    private JButton sendBtn = new JButton("发送");

    public ChatRoomForm() {
        frame.setSize(500, 500);
        initFrame();
        frame.setVisible(true);
    }

    private void initFrame() {
        //底部发送区域
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        bottomPanel.add(chatScrollPane, BorderLayout.CENTER);
        bottomPanel.add(sendBtn, BorderLayout.EAST);

        //顶部消息区域
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messageScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        messageScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        messageArea.setEditable(false);

        container.add(messageScrollPane, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(container);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     * Close this chat room
     */
    public void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Get message that users type in the text field
     * @return String of the message
     */
    public String getSendMessage() {
        return chatArea.getText();
    }

    /**
     * Add message received from the server to the message area
     * @param from user who sends this message
     * @param message message send by the user
     */
    public void addMessage(String from, String message) {
        messageArea.append(from + ":" + message + "\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }

    /**
     * Clear the message that user type in the text field
     */
    public void clearChatArea() {
        chatArea.setText("");
    }

    /**
     * Add a listener to the send event
     * @param listener listener called when send message
     */
    public void setOnSendListener(final ActionListener listener) {
        sendBtn.addActionListener(listener);
        chatArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int code = keyEvent.getKeyCode();
                int modifiers = keyEvent.getModifiers();
                if (code == KeyEvent.VK_ENTER && modifiers == KeyEvent.CTRL_MASK) listener.actionPerformed(null);
            }
        });
    }
}
