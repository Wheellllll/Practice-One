package test;

import client.Client;
import server.Server;
import wheellllll.utils.MessageBuilder;

import java.util.HashMap;

/**
 * This class is written for tesing client send message
 */
public class ClientSendTest {
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
            public void OnSend(HashMap<String, String> args)
            {
                assert("send" ==  args.get("event"));
                assert(msgToSend == args.get("message"));

            }
        };

        MessageBuilder msgBuilder = new MessageBuilder();
        msgBuilder.add("event","login");
        msgBuilder.add("username","funcTest");
        msgBuilder.add("password","123456");
        String msg = msgBuilder.buildString();
        client.sendMessage(msg);

        MessageBuilder msgToSendBuilder = new MessageBuilder();
        msgToSendBuilder.add("event","send");
        msgToSendBuilder.add("message",msgToSend);
        String message = msgToSendBuilder.buildString();
        client.sendMessage(message);


    }
}
