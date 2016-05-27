package test;

import client.Client;
import server.Server;
import wheellllll.utils.MessageBuilder;

import java.util.HashMap;

/**
 * This class is written for testing client send message limit in 1s
 */
public class SendNumLimitTest {
    public static void main(String args[])
    {
        Server.DEBUG_MODE(true);
        Client.DEBUG_MODE(true);
        Server server = new Server();
        String msgToSend = "test";
        Client client = new Client() {
            @Override
            public void OnConnect(HashMap<String, String> args) {
                System.out.println("HHH");
            }

            @Override
            public void OnSend(HashMap<String, String> msg)
            {
                assert("event" == "send");
                assert("message" == msgToSend);
            }
        };

        MessageBuilder msgBuilder = new MessageBuilder();
        msgBuilder.add("event","login");
        msgBuilder.add("username","funcTest");
        msgBuilder.add("password","123456");
        String msg = msgBuilder.buildString();
        client.sendMessage(msg);

        for(int i = 1; i <= 6; ++i)
        {
            MessageBuilder msgToSendBuilder = new MessageBuilder();
            msgToSendBuilder.add("event","send");
            msgToSendBuilder.add("message",msgToSend);
            String message = msgToSendBuilder.buildString();
            client.sendMessage(message);
        }
    }
}
