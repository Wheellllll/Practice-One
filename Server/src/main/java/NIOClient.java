import com.alibaba.fastjson.JSON;
import utils.DatabaseUtils;
import utils.MessageBuilder;
import utils.PackageHandler;
import utils.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sweet on 3/20/16.
 */
public class NIOClient {
    private static ArrayList<NIOClient> clients = new ArrayList<NIOClient>();
    /*
     * mSocketChannel 绑定的socket
     * mUsername 用户名
     * mPassword 密码
     * mStatus 当前状态->Settings.Status
     * mMsgPerSecond最近1秒发送的消息数
     * mMsgSinceLogin自从登陆起发送的消息数
     * mLastSendTime上次发送的时间戳
     *
     */
    private boolean isWriting = false;
    private AsynchronousSocketChannel mSocketChannel = null;
    private String mUsername = null;
    private String mPassword = null;
    private Settings.Status mStatus = Settings.Status.LOGOUT;
    private int mMsgPerSecond = 0;
    private int mMsgSinceLogin = 0;
    private long mLastSendTime = 0;

    private int localValidLogin = 0;
    private int localInvalidLogin = 0;
    private int localReceiveMsgNum = 0;
    private int localIgnoreMsgNum = 0;
    private int localForwardMsgNum = 0;

    public int getLocalValidLogin() {
        return localValidLogin;
    }

    public int getLocalInvalidLogin() {
        return localInvalidLogin;
    }

    public int getLocalReceiveMsgNum() {
        return localReceiveMsgNum;
    }

    public int getLocalIgnoreMsgNum() {
        return localIgnoreMsgNum;
    }

    public int getLocalForwardMsgNum() {
        return localForwardMsgNum;
    }

    public static ArrayList<NIOClient> getClients() {
        return clients;
    }

    private PackageHandler mPackageHandler = new PackageHandler();

    public NIOClient(AsynchronousSocketChannel socketChannel) {
        this.mSocketChannel = socketChannel;
        /*
         * 触发OnConnect事件
         */
        OnConnect();

        //开始接受消息
        readMessage();
    }

    private void readMessage() {
        /*
         * 读消息
         */
        final ByteBuffer buf = ByteBuffer.allocate(2048);

        mSocketChannel.read(buf, mSocketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {

            public void completed(Integer result, AsynchronousSocketChannel socketChannel) {
                if (result == -1) {
                    try {
                        /*
                         * 触发OnDisconnect事件
                         */
                        OnDisconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                mPackageHandler.addPackage(buf);
                while (mPackageHandler.hasPackage()) {
                    String message = mPackageHandler.getPackage();
                    System.out.println(message);
                    dispatchMessage(message);
                }

                /*
                 * 处理下一条信息
                 */
                readMessage();
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel ) {
                System.out.println("fail to read message from client");
            }

        });
    }

    private void dispatchMessage(String message) {
        /*
         * TODO: 使用RxJava注册事件和分发事件
         */

        HashMap<String,String> msg = JSON.parseObject(message, HashMap.class);

        if (msg.get("event").equals("reg")) {
            /*
             * 触发OnRegister事件
             */
            OnRegister(msg);
        } else if (msg.get("event").equals("login")) {
            /*
             * 触发OnLogin事件
             */
            OnLogin(msg);
        } else if (msg.get("event").equals("send")) {
            /*
             * 触发OnSend事件
             */
            OnSend(msg);
        } else if (msg.get("event").equals("relogin")) {
            /*
             * 触发OnRelogin事件
             */
            OnRelogin(msg);

        } else {
            /*
             * 触发OnError事件
             */
            OnError();
        }
    }

    private void sendMessage(final String message) {
        /*
         * 发消息
         */
        while (isWriting) {
            //loop to wait current writing message finish
            System.out.println("waiting...");
        }
        isWriting = true;
        ByteBuffer buf = ByteBuffer.allocate(2048);
        buf.put(message.getBytes());
        //4表示传输结束
        buf.put((byte)4);
        buf.flip();
        mSocketChannel.write(buf, mSocketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {

            public void completed(Integer result, AsynchronousSocketChannel channel) {
                isWriting = false;
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                isWriting = false;
                System.out.println("Fail to write message to client");
            }
        });
    }

    /*
     * 事件定义
     */

    private void OnConnect() {
        clients.add(this);
    }

    private void OnDisconnect() throws IOException {
        System.out.format("Stopped listening to the client %s%n", mSocketChannel.getRemoteAddress());
        clients.remove(this);
        mSocketChannel.close();
    }

    private void OnRegister(Map<String,String> msg) {
        /*
         * 1.判断是否已经注册
         * 2.判断密码是否大于6位
         * 3.加密存储
         * 4.成功则自动登陆
         * 5.失败则返回错误信息
         */

        String username = msg.get("username");
        String password = msg.get("password");
        if (DatabaseUtils.isExisted(username)) {

            String msgToSend = new MessageBuilder()
                    .add("result","fail")
                    .add("event","reg")
                    .add("reason","Id already exists.")
                    .build();

            sendMessage(msgToSend);

        } else if (password.length() < 6) {
            String msgToSend = new MessageBuilder()
                    .add("result","fail")
                    .add("event","reg")
                    .add("reason","The password is too short (at least six).")
                    .build();
            sendMessage(msgToSend);
        } else {
            String encryptedPass = StringUtils.md5Hash(password);
            boolean b = DatabaseUtils.createAccount(username, encryptedPass);
            if (b) {
                mUsername = username;
                mPassword = encryptedPass;
                mStatus = Settings.Status.LOGIN;
                String msgToSend = new MessageBuilder()
                        .add("result","success")
                        .add("event","reg")
                        .build();
                sendMessage(msgToSend);
            } else {
                String msgToSend = new MessageBuilder()
                        .add("result","fail")
                        .add("event","reg")
                        .add("reason","Registration failed due to an unexpected error.")
                        .build();
                sendMessage(msgToSend);
            }
        }
    }

    private void OnLogin(Map<String,String> msg) {
        /*
         * 1.判断用户名和密码
         * 2.判断是否已经登陆
         * 3.成功修改状态
         * 4.失败返回错误信息
         */

        String username = msg.get("username");
        String encryptedPass = StringUtils.md5Hash(msg.get("password"));
        if (DatabaseUtils.isValid(username, encryptedPass)) {
            for (NIOClient client : clients) {
                if (client != this && client.mUsername != null &&
                        client.mUsername.equals(username) && client.mStatus != Settings.Status.LOGOUT) {
                    localInvalidLogin ++;
                    MessageBuilder megBuilder = new MessageBuilder()
                            .add("event","login")
                            .add("result","fail")
                            .add("reason","Already login on another terminal.");
                    String megToSend = megBuilder.build();
                    sendMessage(megToSend);
                    return;
                }
            }
            if (mStatus == Settings.Status.LOGIN) {
                localInvalidLogin ++;
                MessageBuilder megBuilder = new MessageBuilder()
                        .add("event","login")
                        .add("result","fail")
                        .add("reason","Already login");
                String megToSend = megBuilder.build();
                sendMessage(megToSend);
            } else if (mStatus == Settings.Status.LOGOUT || mStatus == Settings.Status.RELOGIN) {
                localValidLogin ++;
                MessageBuilder megBuilder = new MessageBuilder()
                        .add("event","login")
                        .add("result","success");
                String megToSend = megBuilder.build();
                sendMessage(megToSend);
                mStatus = Settings.Status.LOGIN;
                mUsername = username;
                mPassword = encryptedPass;
            }
        } else {
            localInvalidLogin ++;
            MessageBuilder megBuilder = new MessageBuilder()
                    .add("event","login")
                    .add("result","fail")
                    .add("reason","Invalid account.");
            String megToSend = megBuilder.build();
            sendMessage(megToSend);
        }
    }

    private void OnRelogin(Map<String, String> msg) {
        /*
         * 复用登陆逻辑
         */
        OnLogin(msg);
    }

    private void OnSend(Map<String,String> msg) {
        /*
         * 1.判断用户状态
         * 2.发送消息
         * 3.更新用户状态
         */

        long currentSendTime = System.currentTimeMillis() / 1000;
        if (mLastSendTime == currentSendTime) {
            mMsgPerSecond ++;
            if (mMsgPerSecond >= Settings.maxNumberPerSecond) {
                mStatus = Settings.Status.IGNORE;
            }
        } else {
            mMsgPerSecond = 0;
            if (mStatus == Settings.Status.IGNORE)
                mStatus = Settings.Status.LOGIN;
        }
        mLastSendTime = currentSendTime;

        localReceiveMsgNum ++;

        if (mStatus != Settings.Status.LOGIN) {
            localIgnoreMsgNum ++;
        } else {
            mMsgSinceLogin ++;
            if (mMsgSinceLogin >= Settings.maxNumberPerSession) {
                mStatus = Settings.Status.RELOGIN;
                //mUsername = null;
                //mPassword = null;
                //mMsgPerSecond = 0;
                mMsgSinceLogin = 0;
                //mLastSendTime = 0;
                String msgToSend = new MessageBuilder()
                        .add("event","send")
                        .add("result", "fail")
                        .add("reason", "relogin")
                        .build();
                sendMessage(msgToSend);
            } else {
                String msgToSend = new MessageBuilder()
                        .add("event","send")
                        .add("result","success")
                        .build();
                sendMessage(msgToSend);
            }

            String message = msg.get("message");
            for (NIOClient client : clients) {
                if (client != this &&
                        (client.mStatus == Settings.Status.LOGIN || client.mStatus == Settings.Status.IGNORE)) {
                    String msgToSend = new MessageBuilder()
                            .add("event","forward")
                            .add("from",this.mUsername)
                            .add("message",message)
                            .build();
                    client.sendMessage(msgToSend);
                    localForwardMsgNum ++;
                } else {
                    //发给自己的
                    String msgToSend = new MessageBuilder()
                            .add("event", "forward")
                            .add("from", "你")
                            .add("message", message)
                            .build();
                    sendMessage(msgToSend);
                }
            }
        }
    }

    private void OnError() {
        String msgToSend = new MessageBuilder()
                .add("event", "error")
                .add("reason", "error")
                .build();
        sendMessage(msgToSend);
    }

}
