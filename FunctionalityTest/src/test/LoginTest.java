package test;

import utils.MessageBuilder;
import client.Client;
import server.Server;

import java.util.HashMap;
/**
 * This class is written for testing client login
 */
public class LoginTest {
    public static void main(String args[])
    {
        Server.DEBUG_MODE(true);
        Client.DEBUG_MODE(true);
        Server server = new Server();
        Client client = new Client() {
            @Override
            public void OnConnect(HashMap<String, String> args) {
                System.out.println("HHH");

            }

            @Override
            public void OnLogin(HashMap<String, String> args) {
                assert("login" == args.get("event"));
                assert("fail" == args.get("result"));
            }
        };

       //注册测试账号
       MessageBuilder msgBuilder = new MessageBuilder();
       msgBuilder.add("event","reg");
       msgBuilder.add("username","funcTest");
       msgBuilder.add("password","123456");
       String msg = msgBuilder.build();
       client.sendMessage(msg);

       //登录
       MessageBuilder loginMsgBuilder = new MessageBuilder();
       loginMsgBuilder.add("event","login");
       loginMsgBuilder.add("username","funcTest");
       loginMsgBuilder.add("password","123456");
       String loginMsg = loginMsgBuilder.build();
       client.sendMessage(loginMsg);

        String msgToSend = new MessageBuilder()
                .add("event", "login")
                .add("username", "test1")
                .add("password", "test")
                .build();
        client.sendMessage(msgToSend);

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
