package server;

import utils.*;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;

/**
 * Client inherited from BaseClient. You may need to implement the event handler.
 */
public class NIOClient extends BaseClient {

    public NIOClient(AsynchronousSocketChannel socketChannel) {
        super(socketChannel);
    }


    /**
     * Triggered when connect to the client
     * @param args Json params passed to this method
     */
    @Override
    public void OnConnect(HashMap<String, String> args) {
        getClients().add(this);
    }

    /**
     * Triggered when received login request from the client
     * @param args Json params passed to this method
     */
    @Override
    public void OnLogin(HashMap<String, String> args) {
        /*
         * 1.判断用户名和密码
         * 2.判断是否已经登陆
         * 3.成功修改状态
         * 4.失败返回错误信息
         */

        String username = args.get("username");
        String encryptedPass = StringUtils.md5Hash(args.get("password"));
        if (DatabaseUtils.isValid(username, encryptedPass)) {
            for (BaseClient client : getClients()) {
                if (client != this && client.getUsername() != null &&
                        client.getUsername().equals(username) && client.getStatus() != Status.LOGOUT) {
                    incLocalInvalidLogin();
                    MessageBuilder megBuilder = new MessageBuilder()
                            .add("event","login")
                            .add("result","fail")
                            .add("reason","Already login on another terminal.");
                    String megToSend = megBuilder.build();
                    sendMessage(megToSend);
                    return;
                }
            }
            if (getStatus() == Status.LOGIN) {
                incLocalInvalidLogin();
                MessageBuilder megBuilder = new MessageBuilder()
                        .add("event","login")
                        .add("result","fail")
                        .add("reason","Already login");
                String megToSend = megBuilder.build();
                sendMessage(megToSend);
            } else if (getStatus() == Status.LOGOUT || getStatus() == Status.RELOGIN) {
                MessageBuilder megBuilder = null;
                if (getStatus() == Status.LOGOUT) {
                    megBuilder = new MessageBuilder()
                            .add("event","login")
                            .add("result","success");
                } else {
                    megBuilder = new MessageBuilder()
                            .add("event","relogin")
                            .add("result","success");
                }
                incLocalValidLogin();
                String megToSend = megBuilder.build();
                sendMessage(megToSend);
                setStatus(Status.LOGIN);
                setUsername(username);
                setPassword(encryptedPass);
            }
        } else {
            incLocalInvalidLogin();
            MessageBuilder megBuilder = new MessageBuilder()
                    .add("event","login")
                    .add("result","fail")
                    .add("reason","Invalid account.");
            String megToSend = megBuilder.build();
            sendMessage(megToSend);
        }
    }

    /**
     * Triggered when received register request from the client
     * @param args Json params passed to this method
     */
    @Override
    public void OnRegister(HashMap<String, String> args) {
       /*
         * 1.判断是否已经注册
         * 2.判断密码是否大于6位
         * 3.加密存储
         * 4.成功则自动登陆
         * 5.失败则返回错误信息
         */

        String username = args.get("username");
        String password = args.get("password");

        if (username == null || username.equals("")) {
            String msgToSend = new MessageBuilder()
                    .add("result", "failt")
                    .add("event", "reg")
                    .add("reason", "The username cannot be empty.")
                    .build();
            sendMessage(msgToSend);
            return;
        }

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
                setUsername(username);
                setPassword(encryptedPass);
                setStatus(Status.LOGIN);
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

    /**
     * Triggered when received relogin request from the client
     * @param args Json params passed to this method
     */
    @Override
    public void OnRelogin(HashMap<String, String> args) {
        OnLogin(args);
    }

    /**
     * Triggered when received send request from the client
     * @param args Json params passed to this method
     */
    @Override
    public void OnSend(HashMap<String, String> args) {
        /*
         * 1.判断用户状态
         * 2.发送消息
         * 3.更新用户状态
         */

        long currentSendTime = System.currentTimeMillis() / 1000;
        if (getLastSendTime() == currentSendTime) {
            incMsgPerSecond();
            if (getMsgPerSecond() >= Integer.parseInt(Config.getConfig().getProperty("MAX_NUMBER_PER_SECOND", "5"))) setStatus(Status.IGNORE);
        } else {
            setMsgPerSecond(0);
            if (getStatus() == Status.IGNORE) setStatus(Status.LOGIN);
        }
        setLastSendTime(currentSendTime);

        incLocalReceiveMsgNum();

        if (getStatus() != Status.LOGIN) {
            incLocalIgnoreMsgNum();
        } else {
            incMsgSinceLogin();
            if (getMsgSinceLogin() >= Integer.parseInt(Config.getConfig().getProperty("MAX_NUMBER_PER_SESSION", "100"))) {
                setStatus(Status.RELOGIN);
                setMsgSinceLogin(0);
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

            String message = args.get("message");
            for (BaseClient client : getClients()) {
                if (client != this) {
                    if (client.getStatus() == Status.LOGIN || client.getStatus() == Status.IGNORE) {
                        String msgToSend = new MessageBuilder()
                                .add("event","forward")
                                .add("from",getUsername())
                                .add("message",message)
                                .build();
                        client.sendMessage(msgToSend);
                        incLocalForwardMsgNum();
                    }
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

    /**
     * Triggered when received forward request from the client
     * @param args Json params passed to this method
     */
    @Override
    public void OnForward(HashMap<String, String> args) {
        System.out.println(String.format("%s has received message", args.get("username")));
    }

    /**
     * Triggered when disconnect from the client
     * @param args Json params passed to this method
     */
    @Override
    public void OnDisconnect(HashMap<String, String> args) {
        try {
            System.out.format("Stopped listening to the client %s%n", getSocketChannel().getRemoteAddress());
            getClients().remove(this);
            getSocketChannel().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Triggered when received error message from the client
     * @param args Json params passed to this method
     */
    @Override
    public void OnError(HashMap<String, String> args) {
        String msgToSend = new MessageBuilder()
                .add("event", "error")
                .add("reason", "error")
                .build();
        sendMessage(msgToSend);
    }
}
