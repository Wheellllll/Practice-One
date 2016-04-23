package client;

import wheellllll.utils.MessageBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * Client inherited from BaseClient. You may need to implement the event wheellllll.handler.
 */
public class Client extends BaseClient {
    public static void main(String[] args) {
        new Client();
    }

    /**
     * Triggered when connect to the server
     * @param msg Json params passed to this method
     */
    @Override
    public void OnConnect(HashMap<String, String> msg) {
        System.out.println("Connected");
    }

    /**
     * Triggered when received login result from the server
     * @param msg Json params passed to this method
     */
    @Override
    public void OnLogin(HashMap<String,String> msg) {

        if (msg.get("result").equals("success")) {
            loginSuccessRecorder.record();
            loginSuccessRecorder2.record();
            if (!DEBUG) getLoginAndRegisterForm().close();
            if (!DEBUG) initChatRoomUI();
        } else {
            /*
             * 登陆失败，更新UI
             */
            if (!DEBUG) getLoginAndRegisterForm().setError(msg.get("reason"));
            loginFailRecorder.record();
            loginFailRecorder2.record();
        }
    }

    /**
     * Triggered when received relogin result from the server
     * @param msg Json params passed to this method
     */
    @Override
    public void OnRelogin(HashMap<String, String> msg) {
        if (msg.get("result").equals("success")) {
            if (!DEBUG) getChatRoomForm().addMessage("管理员", "登陆成功");
            loginSuccessRecorder.record();
            loginSuccessRecorder2.record();
        } else {
            /*
             * 重新登陆失败，再来一次
             */
            if (!DEBUG) getChatRoomForm().addMessage("管理员", "登陆失败，重试中...");
            loginFailRecorder.record();
            loginFailRecorder2.record();
            String msgToSend = new MessageBuilder()
                    .add("event", "relogin")
                    .add("username", getUsername())
                    .add("password", getPassword())
                    .build();
            sendMessage(msgToSend);
        }
    }

    /**
     * Triggered when received register result from the server
     * @param msg Json params passed to this method
     */
    @Override
    public void OnRegister(HashMap<String,String> msg) {

        if (msg.get("result").equals("success")) {
            if (!DEBUG) getLoginAndRegisterForm().close();
            initChatRoomUI();
        } else {
            /*
             * 注册失败，更新UI
             */
            if (!DEBUG) getLoginAndRegisterForm().setError(msg.get("reason"));
        }
    }

    /**
     * Triggered when received send result from the server
     * @param msg Json params passed to this method
     */
    @Override
    public void OnSend(HashMap<String, String> msg) {
        if (msg.get("result").equals("success")) {
            /*
             * 发送成功，记录一下
             */
            sendMsgRecorder.record();
            sendMsgRecorder2.record();
        } else if (msg.get("reason").equals("relogin")) {
            String msgToSend = new MessageBuilder()
                    .add("event", "relogin")
                    .add("username", getUsername())
                    .add("password", getPassword())
                    .build();
            sendMessage(msgToSend);
        } else {
            /*
             * 发送失败
             */
        }
    }

    /**
     * Triggered when received forward result from the server
     * @param msg Json params passed to this method
     */
    @Override
    public void OnForward(HashMap<String,String> msg) {
        String from = msg.get("from");
        String message = msg.get("message");

//        if (from.equals("你")) {
//            realtimeMessageRecorder.record(String.format("UserName : %s\ntime : %s\nmessage : %s\n",
//                    getUsername(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), message));
//            dailyMessageRecorder.record(String.format("UserName : %s\ntime : %s\nmessage : %s\n",
//                    getUsername(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), message));
//        }

        receiveMsgRecorder.record();
        receiveMsgRecorder2.record();
        if (!DEBUG) getChatRoomForm().addMessage(from, message);
        String msgToSend = new MessageBuilder()
                .add("event", "forward")
                .add("username", getUsername())
                .add("ack", "success")
                .build();
        sendMessage(msgToSend);
    }

    /**
     * Triggered when disconnect from the server
     * @param args Json params passed to this method
     */
    @Override
    public void OnDisconnect(HashMap<String, String> args) {
        System.out.println("Disconnect");
    }

    /**
     * Triggered when received error message from the server
     * @param msg Json params passed to this method
     */
    @Override
    public void OnError(HashMap<String, String> msg) {

    }
}
