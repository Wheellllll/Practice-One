import com.alibaba.fastjson.JSON;
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
import java.nio.channels.UnresolvedAddressException;
import java.util.HashMap;
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

    private PackageHandler mPackageHandler = new PackageHandler();

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
                String user = mLoginAndRegisterForm.getUsername();
                String pass = mLoginAndRegisterForm.getPassword();

                String msgToSend = new MessageBuilder()
                        .add("event", "login")
                        .add("username", user)
                        .add("password", pass)
                        .build();
                username = user;
                password = pass;

                sendMessage(msgToSend);
            }
        });
        mLoginAndRegisterForm.setOnRegisterListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String user = mLoginAndRegisterForm.getUsername();
                String pass = mLoginAndRegisterForm.getPassword();

                String msgToSend = new MessageBuilder()
                        .add("event", "reg")
                        .add("username", user)
                        .add("password", pass)
                        .build();
                username = user;
                password = pass;

                sendMessage(msgToSend);
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
                msgToSend = new MessageBuilder()
                        .add("event", "send")
                        .add("message", msgToSend)
                        .build();
                sendMessage(msgToSend);
                sendMsgNum ++;
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
            mLoginAndRegisterForm.setError("连接服务器失败");
        } catch (UnresolvedAddressException e) {
            mLoginAndRegisterForm.setError("连接服务器失败");
        }
    }

    class ConnectionHandler implements
            CompletionHandler<Void, AsynchronousSocketChannel> {

        public void completed(Void result, AsynchronousSocketChannel socketChannel) {
            mSocketChannel = socketChannel;
            System.out.println("Connected");
            mLoginAndRegisterForm.setCorrect("成功连接服务器");

            //开始读消息
            readMessage();
        }

        public void failed(Throwable e, AsynchronousSocketChannel asynchronousSocketChannel) {
            mLoginAndRegisterForm.setError("连接服务器失败");
        }
    }

    private void readMessage() {
        final ByteBuffer buf = ByteBuffer.allocate(2048);
        mSocketChannel.read(buf, mSocketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>(){

            public void completed(Integer result, AsynchronousSocketChannel channel) {

                mPackageHandler.addPackage(buf);
                while (mPackageHandler.hasPackage()) {
                    String message = mPackageHandler.getPackage();
                    System.out.println("Read message:" + message);
                    dispatchMessage(message);
                }

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
        //4表示传输结束
        buf.put((byte)4);
        buf.flip();
        mSocketChannel.write(buf, mSocketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel >() {
            public void completed(Integer result, AsynchronousSocketChannel channel ) {
                // Nothing to do
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                //mSt = new StringTokenizer(message, "|");
                //if (mSt.nextToken().equals("login"))
                  //  loginFailNum ++;
                System.out.println( "Fail to write the message to server");
            }
        });
    }


    public static void main(String[] args) {
        new Client();
    }

    private void dispatchMessage(String message) {
        HashMap<String,String> msg = JSON.parseObject(message, HashMap.class);

        if (msg.get("event").equals("login")) {
            OnLogin(msg);
        } else if (msg.get("event").equals("relogin"))  {
            OnRelogin(msg);
        } else if (msg.get("event").equals("reg")) {
            OnRegister(msg);
        } else if (msg.get("event").equals("send")) {
            OnSend(msg);
        } else if (msg.get("event").equals("forward")) {
            OnForward(msg);
        } else {
            OnError(msg);
        }
    }

    /*
     * 事件定义
     */
    private void OnLogin(HashMap<String,String> msg) {

        if (msg.get("result").equals("success")) {
            loginSuccessNum++;
            mLoginAndRegisterForm.close();
            initChatRoomUI();
        } else {
            /*
             * 登陆失败，更新UI
             */
            mLoginAndRegisterForm.setError(msg.get("reason"));
            loginFailNum ++;
        }
    }

    private void OnRelogin(HashMap<String, String> msg) {
        /*
         * TODO:处理重新登陆的结果
         */
        if (msg.get("result").equals("success")) {
            loginSuccessNum++;
        } else {
            /*
             * 登陆失败，更新UI
             */
            loginFailNum ++;
            mLoginAndRegisterForm.setError(msg.get("reason"));
        }
    }

    private void OnRegister(HashMap<String,String> msg) {

        if (msg.get("result").equals("success")) {
            mLoginAndRegisterForm.close();
            initChatRoomUI();
        } else {
            /*
             * 注册失败，更新UI
             */
            mLoginAndRegisterForm.setError(msg.get("reason"));
        }
    }

    //消息发送结果
    private void OnSend(HashMap<String, String> msg) {
        if (msg.get("result").equals("success")) {
            //TODO:成功，记录一下
        } else if (msg.get("reason").equals("relogin")) {
            String msgToSend = new MessageBuilder()
                    .add("event", "relogin")
                    .add("username", username)
                    .add("password", password)
                    .build();
            sendMessage(msgToSend);
        } else {
            //TODO:失败，为什么失败
        }
    }

    //从其他客户端来的消息
    private void OnForward(HashMap<String,String> msg) {
        String from = msg.get("from");
        String message = msg.get("message");

        receiveMsgNum ++;
        mChatRoomForm.addMessage(from, message);
        //TODO: 通知服务器
    }

    private void OnError(HashMap<String, String> msg) {

    }
}
