package client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
        logger.info("Connected to the given host & port ");
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

            logger.info("USER {} from GROUP {} Login the client successful",getUsername(),msg.get("groupid"));

            /*
             * Handle unread message
             */
            String unreadMessageS = msg.get("unreadmessage");
            JSONArray unreadMessageA = JSON.parseArray(unreadMessageS);
            for (int i = 0; i < unreadMessageA.size(); i++) {
                JSONObject o = unreadMessageA.getJSONObject(i);
                String from = o.getString("from");
                String message = o.getString("message");
                String date = o.getString("date");
                if (!DEBUG) getChatRoomForm().addMessage(from, message, date);
            }

        } else {
            /*
             * 登陆失败，更新UI
             * warn 级别的日志警告记录
             */
            if (!DEBUG) getLoginAndRegisterForm().setError(msg.get("reason"));
            intervalLogger.updateIndex("Login failed number", 1);

            logger.warn("User Login Fail ,Reason : {}", msg.get("reason"));
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
             * warn 级别日志
             */
            if (!DEBUG) getChatRoomForm().addMessage("管理员", "登陆失败，重试中...");
            intervalLogger.updateIndex("Login failed number", 1);
            logger.warn("User Relogin Fail ");
            String msgToSend = new MessageBuilder()
                    .add("event", "relogin")
                    .add("username", getUsername())
                    .add("password", getPassword())
                    .buildString();
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
            logger.info("USER {} from GROUP {} Register the account successful", getUsername(), msg.get("groupid"));
            if (!DEBUG) getLoginAndRegisterForm().close();
            initChatRoomUI();
        } else {
            /*
             * 注册失败，更新UI
             * warn 级别日志记录
             */
            if (!DEBUG) getLoginAndRegisterForm().setError(msg.get("reason"));
            logger.warn("User Register Fail ,Reason : {}", msg.get("reason"));
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
             * 同时在日志的Info中给予记录一条
             * Todo : 是否需要将所有的中间消息存于日志中，即从PM中分离？
             */
            intervalLogger.updateIndex("Send message number", 1);
            logger.info("USER {} Send One Msg",getUsername());
        } else if (msg.get("reason").equals("relogin")) {
            String msgToSend = new MessageBuilder()
                    .add("event", "relogin")
                    .add("username", getUsername())
                    .add("password", getPassword())
                    .buildString();
            logger.info("USER {} Unable to send msg now ,Reason: Need to relogin",getUsername());
            sendMessage(msgToSend);
        } else {
            /*
             * 发送失败
             * Error 级别日志
             */
            logger.error("USER {} Unable to send msg , Unknow Reason （Maybe disconnect.....）");
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
        String date = msg.get("date");

        HashMap<String, String> map = new HashMap<>();
        map.put("username", getUsername());
        map.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("message", message);
        realtimeLogger.log(map);

        logger.info("USER：{} , Msg：{} ，be forwarded",getUsername(),message);

        intervalLogger.updateIndex("Receive message number", 1);
        if (!DEBUG) getChatRoomForm().addMessage(from, message, date);
        String msgToSend = new MessageBuilder()
                .add("event", "forward")
                .add("username", getUsername())
                .add("ack", "success")
                .buildString();
        sendMessage(msgToSend);
    }

    @Override
    public void OnGroup(HashMap<String, String> args) {
        String type = args.get("type");
        if (type.equals("change")) {
            String newGId = args.get("groupid");
            getChatRoomForm().updateGroupId(Integer.parseInt(newGId));

            logger.info("USER {} change GROUP TO {}",getUsername(),args.get("groupid"));
        } else if (type.equals("member")) {
            String members = args.get("members");
            String[] ms = members.split("\u0004");
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String s : ms) {
                if (first) {
                    sb.append(s);
                    first = false;
                } else {
                    sb.append("\n").append(s);
                }
            }
            getChatRoomForm().displayGroupMember(sb.toString());

            logger.info("GROUP {} has members include :{}",getGroupId(),sb.toString());
        }
    }

    /**
     * Triggered when disconnect from the server
     * @param args Json params passed to this method
     */
    @Override
    public void OnDisconnect(HashMap<String, String> args) {
        System.out.println("Disconnect");
        logger.warn("DISConnected from the given host & port ");
    }

    /**
     * Triggered when received error message from the server
     * @param msg Json params passed to this method
     */
    @Override
    public void OnError(HashMap<String, String> msg) {
        logger.error("Error happens ");
    }
}
