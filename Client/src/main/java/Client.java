import ui.ChatRoomForm;
import ui.LoginAndRegisterForm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.StringTokenizer;

/**
 * Created by sweet on 3/16/16.
 */
public class Client {

    private LoginAndRegisterForm mLoginAndRegisterForm = null;
    private ChatRoomForm mChatRoomForm = null;
    private AsynchronousSocketChannel mSocketChannel = null;

    private StringTokenizer mSt;

    public Client() {
        try {
            initWelcomeUI();
            SocketAddress serverAddress = new InetSocketAddress("localhost", 9001);
            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
            socketChannel.connect(serverAddress, socketChannel, new ConnectionHandler());
            Thread.currentThread().join();
        } catch (IOException e) {
            System.out.format("Fail to connect to server: %s", e.getMessage());
        } catch (InterruptedException e) {
            System.out.format("Stop to connect to server");
        }
    }

    private void initWelcomeUI() {
        //开启登陆界面
        mLoginAndRegisterForm = new LoginAndRegisterForm();
        mLoginAndRegisterForm.setOnLoginListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String username = mLoginAndRegisterForm.getUsername();
                String password = mLoginAndRegisterForm.getPassword();
                sendMessage(String.format("login|%s|%s", username, password));
            }
        });
        mLoginAndRegisterForm.setOnRegisterListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String username = mLoginAndRegisterForm.getUsername();
                String password = mLoginAndRegisterForm.getPassword();
                sendMessage(String.format("reg|%s|%s", username, password));
            }
        });
    }

    private void initChatRoomUI() {
        //开启聊天室界面
        mChatRoomForm = new ChatRoomForm();
        mChatRoomForm.setOnSendListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String msgToSend = mChatRoomForm.getSendMessage();
                sendMessage(String.format("send|%s", msgToSend));
                mChatRoomForm.clearChatArea();
            }
        });
    }

    class ConnectionHandler implements
            CompletionHandler<Void, AsynchronousSocketChannel> {

        public void completed(Void result, AsynchronousSocketChannel socketChannel) {
            mSocketChannel = socketChannel;
            System.out.println("Connected");

            //开始读消息
            readMessage();
        }

        public void failed(Throwable e, AsynchronousSocketChannel asynchronousSocketChannel) {
            System.out.println("Fail to connect to server");
        }
    }

    private void readMessage() {
        final ByteBuffer buf = ByteBuffer.allocate(2048);
        mSocketChannel.read(buf, mSocketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>(){

            public void completed(Integer result, AsynchronousSocketChannel channel) {
                String message = StringUtils.bufToString(buf);
                System.out.println("Read message:" + message);

                dispatchMessage(message);

                //继续处理下一条消息
                readMessage();
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                System.out.println( "Fail to read message from server");
            }

        });
    }

    private void sendMessage(final String message) {
        ByteBuffer buf = ByteBuffer.allocate(2048);
        buf.put(message.getBytes());
        buf.flip();
        mSocketChannel.write(buf, mSocketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel >() {
            public void completed(Integer result, AsynchronousSocketChannel channel ) {
                //Nothing to do
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                System.out.println( "Fail to write the message to server");
            }
        });
    }


    private String getTextFromUser() throws Exception{
        System.out.print("Please enter a  message  (Bye  to quit):");
        BufferedReader consoleReader = new BufferedReader(
                new InputStreamReader(System.in));
        String msg = consoleReader.readLine();
        return msg;
    }

    public static void main(String[] args) {
        new Client();
    }

    private void dispatchMessage(String message) {
        mSt = new StringTokenizer(message, "|");
        String event = mSt.nextToken();
        if (event.equals("login")) {
            OnLogin();
        } else if (event.equals("reg")) {
            OnRegister();
        } else if (event.equals("send")) {
            OnSend();
        } else {
            OnError();
        }
    }

    /*
     * 事件定义
     */
    private void OnLogin() {
        String result = mSt.nextToken();
        if (result.equals("success")) {
            mLoginAndRegisterForm.close();
            initChatRoomUI();
        } else {
            //登陆失败，更新错误信息
        }
    }

    private void OnRegister() {
        String result = mSt.nextToken();
        if (result.equals("success")) {
            mLoginAndRegisterForm.close();
            initChatRoomUI();
        } else {
            //注册失败，更新错误信息
        }
    }

    private void OnSend() {
        String from = mSt.nextToken();
        String message = mSt.nextToken();
        mChatRoomForm.addMessage(from, message);
    }

    private void OnError() {

    }
}
