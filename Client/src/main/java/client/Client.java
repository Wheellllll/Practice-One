package client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import wheellllll.utils.MessageBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
            setGroupId(Integer.parseInt(msg.get("groupid")));
            intervalLogger.updateIndex("Login successfully number", 1);
            if (!DEBUG) getLoginAndRegisterForm().close();
            if (!DEBUG) initChatRoomUI();

            /*
             * Handle unread message
             */
            String unreadMessageS = msg.get("unreadmessage");
            JSONArray unreadMessageA = JSON.parseArray(unreadMessageS);
            for (int i = 0; i < unreadMessageA.size(); i++) {
                JSONObject o = unreadMessageA.getJSONObject(i);
                String from = o.getString("from");
                String message = o.getString("message");
                if (!DEBUG) getChatRoomForm().addMessage(from, message);
            }

        } else {
            /*
             * 登陆失败，更新UI
             */
            if (!DEBUG) getLoginAndRegisterForm().setError(msg.get("reason"));
            intervalLogger.updateIndex("Login failed number", 1);
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
            intervalLogger.updateIndex("Login successfully number", 1);
        } else {
            /*
             * 重新登陆失败，再来一次
             */
            if (!DEBUG) getChatRoomForm().addMessage("管理员", "登陆失败，重试中...");
            intervalLogger.updateIndex("Login failed number", 1);
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
            setGroupId(Integer.parseInt(msg.get("groupid")));
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
            intervalLogger.updateIndex("Send message number", 1);
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

        HashMap<String, String> map = new HashMap<>();
        map.put("username", getUsername());
        map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("message", message);
        realtimeLogger.log(map);

        intervalLogger.updateIndex("Receive message number", 1);
        if (!DEBUG) getChatRoomForm().addMessage(from, message);
        String msgToSend = new MessageBuilder()
                .add("event", "forward")
                .add("username", getUsername())
                .add("ack", "success")
                .build();
        sendMessage(msgToSend);
    }

    @Override
    public void OnGroupChange(HashMap<String, String> args) {
        String newGId = args.get("groupid");
        getChatRoomForm().updateGroupId(Integer.parseInt(newGId));
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
