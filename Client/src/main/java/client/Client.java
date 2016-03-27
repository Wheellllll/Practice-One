package client;

import utils.MessageBuilder;

import java.util.HashMap;


/**
 * Created by sweet on 3/16/16.
 */
public class Client extends BaseClient {
    public static void main(String[] args) {
        new Client();
    }

    /*
     * 事件定义
     */
    @Override
    public void OnConnect(HashMap<String, String> args) {
        System.out.println("Connected");
    }

    @Override
    public void OnLogin(HashMap<String,String> msg) {

        if (msg.get("result").equals("success")) {
            incLoginSuccessNum();
            getLoginAndRegisterForm().close();
            if (!DEBUG) initChatRoomUI();
        } else {
            /*
             * 登陆失败，更新UI
             */
            getLoginAndRegisterForm().setError(msg.get("reason"));
            incLoginFailNum();
        }
    }

    @Override
    public void OnRelogin(HashMap<String, String> msg) {
        if (msg.get("result").equals("success")) {
            incLoginSuccessNum();
        } else {
            /*
             * 登陆失败，更新UI
             */
            incLoginFailNum();
            String msgToSend = new MessageBuilder()
                    .add("event", "relogin")
                    .add("username", getUsername())
                    .add("password", getPassword())
                    .build();
            sendMessage(msgToSend);
            //getLoginAndRegisterForm().setError(msg.get("reason"));
        }
    }

    @Override
    public void OnRegister(HashMap<String,String> msg) {

        if (msg.get("result").equals("success")) {
            getLoginAndRegisterForm().close();
            if (!DEBUG) initChatRoomUI();
        } else {
            /*
             * 注册失败，更新UI
             */
            getLoginAndRegisterForm().setError(msg.get("reason"));
        }
    }

    @Override
    public void OnSend(HashMap<String, String> msg) {
        if (msg.get("result").equals("success")) {
            /*
             * 发送成功，记录一下
             */
            incSendMsgNum();
        } else if (msg.get("reason").equals("relogin")) {
            String msgToSend = new MessageBuilder()
                    .add("event", "relogin")
                    .add("username", getUsername())
                    .add("password", getPassword())
                    .build();
            sendMessage(msgToSend);
        } else {
            /*
             * 发送失败，记录一下
             */
            getChatRoomForm().addMessage("管理员", msg.get("reason"));
        }
    }

    @Override
    public void OnForward(HashMap<String,String> msg) {
        String from = msg.get("from");
        String message = msg.get("message");

        incReceiveMsgNum();
        getChatRoomForm().addMessage(from, message);
        String msgToSend = new MessageBuilder()
                .add("event", "forward")
                .add("username", getUsername())
                .add("ack", "success")
                .build();
        sendMessage(msgToSend);
    }

    @Override
    public void OnError(HashMap<String, String> msg) {

    }

    @Override
    public void OnDisconnect(HashMap<String, String> args) {
        System.out.println("Disconnect");
    }


}
