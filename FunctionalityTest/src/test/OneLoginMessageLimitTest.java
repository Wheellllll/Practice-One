package test;

import client.Client;
import server.Server;
import utils.MessageBuilder;

import java.util.HashMap;

/**
 * Created by summer on 3/29/16.
 */
public class OneLoginMessageLimitTest {
    public static void main(String args[])
    {
        Server.DEBUG_MODE(true);
        Client.DEBUG_MODE(true);
        Server server = new Server();
        String msgToSend = "test";
        Client client = new Client() {


            @Override
            public void OnLogin(HashMap<String, String> args) {
                assert("login" == args.get("event"));
                assert("fail" == args.get("result"));


            }

            @Override
            public void OnRelogin(HashMap<String, String> args)
            {
                assert("relogin" == args.get("event"));

            }

        };
        MessageBuilder msgBuilder = new MessageBuilder();
        msgBuilder.add("event","login");
        msgBuilder.add("username","q");
        msgBuilder.add("password","qqqqqq");
        String msg = msgBuilder.build();
        client.setUsername("w");
        client.setPassword("wwwwww");
        client.sendMessage(msg);

        MessageBuilder msgToSendBuilder = new MessageBuilder();
        msgToSendBuilder.add("event","send");
        msgToSendBuilder.add("message",msgToSend);
        String message = msgToSendBuilder.build();
        //发送100条消息
        for (int i = 1; i <= 20; ++i)
        {
            for (int j = 1; j <= 5; ++j ) {
                client.sendMessage(message);
            }
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        //发送第101条消息
        client.sendMessage(message);
        client.sendMessage(message);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //发送第102条消息
        client.sendMessage(message);
    }
}
