import ui.ChatRoomForm;
import ui.Config;
import ui.ConfigDialog;
import ui.LoginAndRegisterForm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by sweet on 3/16/16.
 */
public class Client {

    private LoginAndRegisterForm mLoginAndRegisterForm = null;
    private ChatRoomForm mChatRoomForm = null;
    private AsynchronousSocketChannel mSocketChannel = null;

    private StringTokenizer mSt = null;

    private ScheduledExecutorService sc = null;
    private int loginSuccessNum = 0;
    private int loginFailNum = 0;
    private int sendMsgNum = 0;
    private int receiveMsgNum = 0;
    private String username = null;
    private String password = null;

    public Client() {
        try {
            initWelcomeUI();
            tryConnect();

            sc = Executors.newScheduledThreadPool(1);
            sc.scheduleAtFixedRate(new ClientLogger(this), 0, 1, TimeUnit.MINUTES);
            Thread.currentThread().join();
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
        mLoginAndRegisterForm.setOnConfigListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new ConfigDialog(mLoginAndRegisterForm.getFrame());
                tryConnect();
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


    public int getLoginSuccessNum() {
        return loginSuccessNum;
    }

    public int getLoginFailNum() {
        return loginFailNum;
    }

    public int getSendMsgNum() {
        return sendMsgNum;
    }

    public int getReceiveMsgNum() {
        return receiveMsgNum;
    }

    private void tryConnect() {
        try {
            String host = Config.getConfig().getProperty("host");
            String port = Config.getConfig().getProperty("port");
            SocketAddress serverAddress = new InetSocketAddress(host, Integer.parseInt(port));
            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
            socketChannel.connect(serverAddress, socketChannel, new ConnectionHandler());
        } catch (IOException e) {
            System.out.format("Fail to connect to server: %s", e.getMessage());
        }
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

                mSt = new StringTokenizer(message, "|");
                String event = mSt.nextToken();
                if (event.equals("send")) {
                    sendMsgNum ++;
                } else if (event.equals("login") || event.equals("reg")) {
                    username = mSt.nextToken();
                    password = mSt.nextToken();
                }

            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                mSt = new StringTokenizer(message, "|");
                if (mSt.nextToken().equals("login"))
                    loginFailNum ++;
                System.out.println( "Fail to write the message to server");
            }
        });
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
        } else if (event.equals("forward")) {
            OnForward();
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
            loginSuccessNum ++;
            mLoginAndRegisterForm.close();
            initChatRoomUI();
        } else {
            /*
             * TODO:登陆失败，更新UI
             * 等改完JSON再写
             */
            loginFailNum ++;
        }
    }

    private void OnRegister() {
        String result = mSt.nextToken();
        if (result.equals("success")) {
            mLoginAndRegisterForm.close();
            initChatRoomUI();
        } else {
            /*
             * TODO:注册失败，更新UI
             * 等改完JSON再写
             */
        }
    }

    //消息发送结果
    private void OnSend() {
        //TODO:重新登录后需更新UI
        /*String result = mSt.nextToken();
        if (result.equals("Redo login")) {
            sendMessage(String.format("login|%s|%s", username, password));
        }*/

    }

    //从其他客户端来的消息
    private void OnForward() {
        String from = mSt.nextToken();
        String message = mSt.nextToken();

        receiveMsgNum ++;
        mChatRoomForm.addMessage(from, message);
        //TODO: acknowledge server
    }

    private void OnError() {

    }
}
