package server;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import wheellllll.database.DatabaseUtils;
import wheellllll.socket.SocketUtils;
import wheellllll.utils.MessageBuilder;
import wheellllll.utils.StringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Client inherited from BaseClient. You may need to implement the event wheellllll.handler.
 */
public class NIOClient extends BaseClient {

    public NIOClient(AsynchronousSocketChannel socketChannel) {
        super(socketChannel);
    }

    public NIOClient(AsynchronousSocketChannel socketChannel, BaseServer server) {
        super(socketChannel, server);
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
     * Triggered when received init event from the client
     * @param args
     */
    @Override
    public void OnInit(HashMap<String, String> args) {
        int availablePort = Integer.parseInt(args.get("port"));
        setUdpPort(availablePort);
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
        int groupId = DatabaseUtils.isValid(username, encryptedPass);
        if (groupId != -1) {
            setGroupId(groupId);
            for (BaseClient client : getClients()) {
                if (client != this && client.getUsername() != null &&
                        client.getUsername().equals(username) && client.getStatus() != Status.LOGOUT) {
                    getServer().intervalLogger.updateIndex("Invalid Login Number", 1);
                    MessageBuilder msgBuilder = new MessageBuilder()
                            .add("event","login")
                            .add("result","fail")
                            .add("reason","该用户已在其他终端登陆");
                    logger.warn("USER: {} login fail,Reason: 该用户已在其他终端登陆",getUsername());
                    String msgToSend = msgBuilder.buildString();
                    sendMessage(msgToSend);
                    return;
                }
            }
            if (getStatus() == Status.LOGIN) {
                getServer().intervalLogger.updateIndex("Invalid Login Number", 1);
                MessageBuilder megBuilder = new MessageBuilder()
                        .add("event","login")
                        .add("result","fail")
                        .add("reason","用户已登陆");
                logger.warn("USER: {} login fail,Reason: 用户已登陆",getUsername());
                String megToSend = megBuilder.buildString();
                sendMessage(megToSend);
            } else if (getStatus() == Status.LOGOUT || getStatus() == Status.RELOGIN) {
                MessageBuilder megBuilder = null;
                if (getStatus() == Status.LOGOUT) {
                    megBuilder = new MessageBuilder()
                            .add("event","login")
                            .add("result","success")
                            .add("groupid", String.valueOf(getGroupId()));
                } else {
                    megBuilder = new MessageBuilder()
                            .add("event","relogin")
                            .add("result","success");
                }

                /*
                 * buildString unread message
                 */
                ArrayList<HashMap<String, String>> m = new ArrayList<>();

                BasicDBObject ac = (BasicDBObject) DatabaseUtils.findOneAccount(new BasicDBObject("username", username));
                ObjectId lastupdate = ac.getObjectId("lastupdate");
                BasicDBObject msg = (BasicDBObject) DatabaseUtils.findOneMessage(new BasicDBObject("_id", lastupdate));

                List<DBObject> msgs;
                if (msg != null) {
                    msgs = DatabaseUtils.findMessage(
                            new BasicDBObject("utime", new BasicDBObject("$gt", msg.getLong("utime")))
                                    .append("to", username),
                            new BasicDBObject("utime", 1)
                    );
                } else {
                    msgs = DatabaseUtils.findMessage(
                            new BasicDBObject("to", username),
                            new BasicDBObject("utime", 1)
                    );
                }

                /*
                 * TODO: Time
                 */
                for (DBObject tempMsg : msgs) {
                    HashMap<String, String> tempMap = new HashMap<>();
                    tempMap.put("from", ((BasicDBObject)tempMsg).getString("from"));
                    tempMap.put("message", ((BasicDBObject)tempMsg).getString("message"));
                    Long utime = ((BasicDBObject)tempMsg).getLong("utime");
                    String date = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(utime));
                    tempMap.put("date", date);
                    m.add(tempMap);
                }

                /*
                 * Ack last unread message
                 */
                if (!msgs.isEmpty()) {
                    BasicDBObject tempMsg = (BasicDBObject) msgs.get(msgs.size() - 1);
                    DatabaseUtils.syncAccount(username, tempMsg.getObjectId("_id"));
                }


                String unreadMessage = JSON.toJSONString(m);
                megBuilder.add("unreadmessage", unreadMessage);

                getServer().intervalLogger.updateIndex("Valid Login Number", 1);
                String megToSend = megBuilder.buildString();
                sendMessage(megToSend);
                setStatus(Status.LOGIN);
                getCapacityLimiter().reset();
                getThroughputLimiter().reset();
                setUsername(username);
                setPassword(encryptedPass);
                setGroupId(groupId);
                OnGroupChanged(groupId, groupId);
            }
        } else {
            getServer().intervalLogger.updateIndex("Invalid Login Number", 1);
            MessageBuilder megBuilder = new MessageBuilder()
                    .add("event","login")
                    .add("result","fail")
                    .add("reason","登陆失败！请检查用户名和密码");
            logger.warn("User Login Fail ,Reason :用户名和密码出现问题 ");
            String megToSend = megBuilder.buildString();
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
                    .add("result", "fail")
                    .add("event", "reg")
                    .add("reason", "用户名不能为空")
                    .buildString();
            sendMessage(msgToSend);
            logger.warn("Register Fail,Reason : 用户名不能为空");
            return;
        }

        if (DatabaseUtils.isExisted(username)) {

            String msgToSend = new MessageBuilder()
                    .add("result","fail")
                    .add("event","reg")
                    .add("reason","用户名已存在")
                    .buildString();
            logger.warn("Register Fail,Reason : 用户名已存在");
            sendMessage(msgToSend);

        } else if (password.length() < 6) {
            String msgToSend = new MessageBuilder()
                    .add("result","fail")
                    .add("event","reg")
                    .add("reason","密码太短（至少6位）")
                    .buildString();
            logger.warn("Register Fail,Reason : 密码太短");
            sendMessage(msgToSend);
        } else {
            String encryptedPass = StringUtils.md5Hash(password);
            boolean b = DatabaseUtils.createAccount(username, encryptedPass, 1);
            if (b) {
                setUsername(username);
                setPassword(encryptedPass);
                setGroupId(1);
                setStatus(Status.LOGIN);
                getCapacityLimiter().reset();
                getThroughputLimiter().reset();
                String msgToSend = new MessageBuilder()
                        .add("result","success")
                        .add("event","reg")
                        .add("groupid", String.valueOf(getGroupId()))
                        .buildString();
                logger.info("USER :{},Register success,", username);
                sendMessage(msgToSend);
                OnGroupChanged(getGroupId(), getGroupId());
            } else {
                String msgToSend = new MessageBuilder()
                        .add("result","fail")
                        .add("event","reg")
                        .add("reason","注册失败！请不要输入奇怪的字符")
                        .buildString();
                logger.warn("Register Fail,Reason : 出现奇怪的字符");
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
        logger.info("USER {}, relogin ",args.get("username"));
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

        String message;
        String msgToSend;

        getServer().intervalLogger.updateIndex("Receive Message Number", 1);

        if (getStatus() == Status.LOGIN || getStatus() == Status.IGNORE) {
            if (! getThroughputLimiter().tryAcquire())
                setStatus(Status.IGNORE);
            else {
                setStatus(Status.LOGIN);
                if (! getCapacityLimiter().tryAcquire())
                    setStatus(Status.RELOGIN);
            }
        }

        switch (getStatus()) {
            case LOGOUT:
                getServer().intervalLogger.updateIndex("Ignore Message Number", 1);
                break;
            case LOGIN:
                message = args.get("message");
                if (message.startsWith("/group")) {

                    int newGId = 0;
                    try {
                        newGId = Integer.parseInt(message.split(" ")[1]);
                    } catch (Exception e) {
                        //输入不合法则不改变 group id
                        HashMap<String, String> msg = new MessageBuilder()
                                .add("event", "forward")
                                .add("from", "管理员")
                                .add("message", "请输入合法的整数以切换到其他组")
                                .buildMap();
                        logger.warn("Group switch Waring, Reson: 整数不合法");
                        getServer().forwardServer.forward(SocketUtils.getIpFromSocketChannel(getSocketChannel()), getUdpPort(), msg);
//                        sendMessage(msgToSend);
                        break;
                    }

                    int oldGId = getGroupId();
                    DatabaseUtils.changeGroupId(getUsername(), getPassword(), newGId);
                    setGroupId(newGId);

                    msgToSend = new MessageBuilder()
                            .add("event", "group")
                            .add("type", "change")
                            .add("groupid", String.valueOf(getGroupId()))
                            .buildString();
                    sendMessage(msgToSend);

                    OnGroupChanged(oldGId, newGId);

                    HashMap<String, String> msg = new MessageBuilder()
                            .add("event", "forward")
                            .add("from", "管理员")
                            .add("message", String.format("切换到第%d组", newGId))
                            .buildMap();
                    getServer().forwardServer.forward(SocketUtils.getIpFromSocketChannel(getSocketChannel()), getUdpPort(), msg);
//                    sendMessage(msgToSend);

                    break;
                }

                /*
                 * Storage message
                 */
                BasicDBObject messageObj = DatabaseUtils.createMessage(message, getUsername());
                ObjectId messageId = messageObj.getObjectId("_id");
                Long utime = messageObj.getLong("utime");
                String date = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(utime));

                List<DBObject> accounts = DatabaseUtils.findAccount(new BasicDBObject("groupid", getGroupId()));

                for (DBObject account : accounts) {
                    BasicDBObject ac = (BasicDBObject) account;
                    if (!ac.getString("username").equals(getUsername()))DatabaseUtils.addUserToMessage(ac.getString("username"), messageId);
                }


                for (BaseClient client : getClients()) {
                    if (client != this && client.getGroupId() == this.getGroupId()) {
                        if (client.getStatus() == Status.LOGIN || client.getStatus() == Status.IGNORE) {
                            msgToSend = new MessageBuilder()
                                    .add("event","forward")
                                    .add("from",getUsername())
                                    .add("message",message)
                                    .add("date", date)
                                    .buildString();
                            client.sendMessage(msgToSend);
                            /*
                             * sync message
                             */
                            DatabaseUtils.syncAccount(client.getUsername(), messageId);
                            getServer().intervalLogger.updateIndex("Forward Message Number", 1);
                        }
                    } else if (client == this) {
                        //发给自己的
                        HashMap<String, String> msg = new MessageBuilder()
                                .add("event", "forward")
                                .add("from", "你")
                                .add("message", message)
                                .add("date", date)
                                .buildMap();
//                        sendMessage(msgToSend);
                        getServer().forwardServer.forward(SocketUtils.getIpFromSocketChannel(getSocketChannel()), getUdpPort(), msg);
                    }
                }

                msgToSend = new MessageBuilder()
                        .add("event","send")
                        .add("result","success")
                        .buildString();
                sendMessage(msgToSend);

                HashMap<String, String> map = new HashMap<>();
                map.put("username", this.getUsername());
                map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                map.put("message", message);
                getServer().realtimeLogger.log(map);

                break;
            case IGNORE:
                msgToSend = new MessageBuilder()
                        .add("event", "send")
                        .add("result", "fail")
                        .add("reason", "You have exceeded message number per second")
                        .buildString();
                logger.warn("USER {} , send msg fail ,Reason :exceeded message number per second",args.get("username"));
                sendMessage(msgToSend);

                msgToSend = new MessageBuilder()
                        .add("event", "forward")
                        .add("from", "管理员")
                        .add("message", "发送的太快了！请休息一下...")
                        .buildString();
                logger.warn("管理员 ,forward msg fail ,reason :发送的太快 ");
                sendMessage(msgToSend);

                getServer().intervalLogger.updateIndex("Ignore Message Number", 1);
                break;
            case RELOGIN:
                msgToSend = new MessageBuilder()
                        .add("event", "send")
                        .add("result", "fail")
                        .add("reason", "relogin")
                        .buildString();
                logger.warn("USER: {} send msg fail ,Reason : need to relogin", args.get("username"));
                sendMessage(msgToSend);

                msgToSend = new MessageBuilder()
                        .add("event", "forward")
                        .add("from", "管理员")
                        .add("message", "超过每个用户发送消息次数，尝试重新登陆中...")
                        .buildString();
                logger.warn("管理员 ,forward msg fail ,reason :超过每个用户发送消息次数，需要重新登陆中 ");
                sendMessage(msgToSend);

                getServer().intervalLogger.updateIndex("Ignore Message Number", 1);
                break;
        }

    }

    /**
     * Triggered when received forward request from the client
     * @param args Json params passed to this method
     */
    @Override
    public void OnForward(HashMap<String, String> args) {
        System.out.println(String.format("%s has received message", args.get("username")));
        logger.info("On Forward messages,USER :{} has received",args.get("username"));
    }

    /**
     * Triggered when disconnect from the client
     * @param args Json params passed to this method
     */
    @Override
    public void OnDisconnect(HashMap<String, String> args) {
        try {
            System.out.format("Stopped listening to the client %s%n", getSocketChannel().getRemoteAddress());
            int oldGId = getGroupId();
            setGroupId(0);
            OnGroupChanged(oldGId, oldGId);
            getClients().remove(this);
            getSocketChannel().close();
            logger.info("Stopped listening to the client");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error happens during disconnect");
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
                .buildString();
        logger.error("Error happens");
        sendMessage(msgToSend);
    }
}