import com.alibaba.fastjson.JSON;
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
import java.util.HashMap;
import java.util.Map;
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
            SocketAddress serverAddress = new InetSocketAddress("localhost", 9001);
            AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
            channel.connect(serverAddress, channel, new ConnectionHandler());
            sc = Executors.newScheduledThreadPool(1);
            sc.scheduleAtFixedRate(new ClientLogger(this), 0, 1, TimeUnit.MINUTES);
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

                HashMap<String,String> meg = new HashMap<String,String>();
                meg.put("event","login");
                meg.put("username",username);
                meg.put("password",password);
                String jsonStringMeg = JSON.toJSONString(meg);
                sendMessage(jsonStringMeg);
            }
        });
        mLoginAndRegisterForm.setOnRegisterListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String username = mLoginAndRegisterForm.getUsername();
                String password = mLoginAndRegisterForm.getPassword();
                HashMap<String,String> meg = new HashMap<String, String>();
                meg.put("event","reg");
                meg.put("username",username);
                meg.put("password",password);
                String jsonStringMeg = JSON.toJSONString(meg);

                sendMessage(jsonStringMeg);
            }
        });
    }

    private void initChatRoomUI() {
        //开启聊天室界面
        mChatRoomForm = new ChatRoomForm();
        mChatRoomForm.setOnSendListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String msgToSend = mChatRoomForm.getSendMessage();
                HashMap<String,String> meg = new HashMap<String, String>();
                meg.put("event","send");
                meg.put("message",msgToSend);
                String jsonStringMeg = JSON.toJSONString(meg);
                sendMessage(jsonStringMeg);
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

//<<<<<<< HEAD
                //继续处理下一条消息
                readMessage();
//=======
//                mSt = new StringTokenizer(msg, "|");
//                String event = mSt.nextToken();
//                if (event.equals("success")) {
//                    loginSuccessNum ++;
//                } else if (event.equals("failed")) {
//                    loginFailNum ++;
//                } else if (event.equals("forward")) {
//                    System.out.println("Forwarded message:" + mSt.nextToken());
//                    receiveMsgNum ++;
//                    startWrite(sockChannel, "ack|Receive forwarded message.");
//                } else if (event.equals("Redo login")) {
//                    startWrite(sockChannel, "login|" + username + "|" + password);
//                }

                /*
                 * 继续处理下一条信息
                 */
//                startRead(channel);
//>>>>>>> a0a07e3191c1ba1fad2ac15d799a52d76ef47233
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                System.out.println( "Fail to read message from server");
            }

        });
    }

    private void sendMessage(final String message) {
        //String jsonString = JSON.toJSONString(message);
        ByteBuffer buf = ByteBuffer.allocate(2048);
        buf.put(message.getBytes());
        buf.flip();
        mSocketChannel.write(buf, mSocketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel >() {
            public void completed(Integer result, AsynchronousSocketChannel channel ) {
//<<<<<<< HEAD
                //Nothing to do
//=======
//                if (! message.equals("")) {
//                    mSt = new StringTokenizer(message, "|");
//                    String event = mSt.nextToken();
//                    if (event.equals("send")) {
//                        sendMsgNum ++;
//                    } else if (event.equals("login")) {
//                        username = mSt.nextToken();
//                        password = mSt.nextToken();
//                    }
//                }
//
//                try {
//                    String msgToWrite = getTextFromUser();
//                    startWrite(channel, msgToWrite);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//>>>>>>> a0a07e3191c1ba1fad2ac15d799a52d76ef47233
            }

          //  public void failed(Throwable exc, AsynchronousSocketChannel channel) {
          //      mSt = new StringTokenizer(message, "|");
          //      if (mSt.nextToken().equals("login"))
          //          loginFailNum ++;
          //      System.out.println( "Fail to write the message to server");
          //  }
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
        HashMap<String,String> meg = JSON.parseObject(message, HashMap.class);

        if (meg.get("event").equals("login")) {
            OnLogin(meg);
        } else if (meg.get("event").equals("reg")) {
            OnRegister();
        } else if (meg.get("event").equals("send")) {
            OnSend();
        } else if (meg.get("even").equals("forward")) {
            OnForward();
        } else {
            OnError();
        }
    }

    /*
     * 事件定义
     */
    private void OnLogin(HashMap<String,String> meg) {
        String result =
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

    //消息发送成功
    private void OnSend() {

    }

    //从其他客户端来的消息
    private void OnForward() {
        String from = mSt.nextToken();
        String message = mSt.nextToken();

        mChatRoomForm.addMessage(from, message);
    }

    private void OnError() {

    }
}
