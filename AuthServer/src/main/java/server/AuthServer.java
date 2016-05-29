package server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import wheellllll.database.DatabaseUtils;
import wheellllll.socket.SocketUtils;
import wheellllll.utils.MessageBuilder;
import wheellllll.utils.StringUtils;
import wheellllll.utils.chatrmi.IAuth;
import wheellllll.utils.chatrmi.IForward;
import wheellllll.utils.chatrmi.Network;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sweet on 5/28/16.
 */
public class AuthServer {
    private int openPort;

    class Auth extends Connection implements IAuth {
        public Auth() {
            new ObjectSpace(this).register(Network.AUTH, this);
        }

        @Override
        public HashMap<String, String> login(HashMap<String, String> args) {
            String username = args.get("username");
            String encryptedPass = StringUtils.md5Hash(args.get("password"));

            int groupId = DatabaseUtils.isValid(username, encryptedPass);
            if (groupId != -1) {
                HashMap<String, String> result = new MessageBuilder()
                        .add("event", args.get("event"))
                        .add("groupId", "" + groupId)
                        .add("result", "success")
                        .buildMap();
                return result;
            } else {
                HashMap<String, String> result = new MessageBuilder()
                        .add("event", args.get("event"))
                        .add("result", "fail")
                        .add("reason","登陆失败！请检查用户名和密码")
                        .buildMap();
                return result;
            }
        }

        @Override
        public HashMap<String, String> register(HashMap<String, String> args) {
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
                HashMap msgToSend = new MessageBuilder()
                        .add("result", "fail")
                        .add("event", "reg")
                        .add("reason", "用户名不能为空")
                        .buildMap();
                return msgToSend;
            }

            if (DatabaseUtils.isExisted(username)) {

                HashMap msgToSend = new MessageBuilder()
                        .add("result","fail")
                        .add("event","reg")
                        .add("reason","用户名已存在")
                        .buildMap();
                return msgToSend;
            } else if (password.length() < 6) {
                HashMap msgToSend = new MessageBuilder()
                        .add("result","fail")
                        .add("event","reg")
                        .add("reason","密码太短（至少6位）")
                        .buildMap();
                return msgToSend;
            } else {
                String encryptedPass = StringUtils.md5Hash(password);
                boolean b = DatabaseUtils.createAccount(username, encryptedPass, 1);
                if (b) {
                    HashMap msgToSend = new MessageBuilder()
                            .add("result", "success")
                            .add("event", "reg")
                            .buildMap();
                    return msgToSend;
                } else {
                    HashMap msgToSend = new MessageBuilder()
                            .add("result","fail")
                            .add("event","reg")
                            .add("reason","注册失败！请不要输入奇怪的字符")
                            .buildMap();
                    return msgToSend;
                }
            }
        }
    }

    protected void initServer() {
        try {
            Server server = new Server() {
                @Override
                protected Connection newConnection() {
                    return new Auth();
                }
            };
            Network.register(server);
            openPort = SocketUtils.getAvailablePort();
            server.bind(openPort);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void connectRegisterCenter() {
        try {
            Client client = new Client();
            client.start();
            Network.register(client);
            client.connect(3000, Network.REGISTER_CENTER_HOST, Network.REGISTER_CENTER_PORT);

            HashMap<String, Object> msg = new HashMap<>();
            msg.put("class", IAuth.class);
            msg.put("port", openPort);
            client.sendTCP(msg);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AuthServer() {
        initServer();
        connectRegisterCenter();
    }

    public static void main(String args[]) {
        new AuthServer();
    }
}
