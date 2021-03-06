package test;

import client.Client;
import server.Server;
import wheellllll.utils.MessageBuilder;

import java.util.HashMap;

/**
 * This class is written for testing server forwardServer message
 */
public class ForwardTest {
    public static void main(String args[])
    {
        Server.DEBUG_MODE(true);
        Client.DEBUG_MODE(true);
        Server server = new Server()
        {

        };
        Client client = new Client() {


            @Override
            public void OnLogin(HashMap<String, String> args) {
                assert("login" == args.get("event"));
                assert("fail" == args.get("result"));
            }

            @Override
            public void OnForward(HashMap<String,String> args) {
                assert ("forwardServer" == args.get("event"));
                assert ("success" == args.get("ack"));

            }

        };

        MessageBuilder msgBuilder = new MessageBuilder();
        msgBuilder.add("event","login");
        msgBuilder.add("username","funcTest");
        msgBuilder.add("password","123456");
        String msg = msgBuilder.buildString();
        client.sendMessage(msg);

        String msgToSend = "test";
        MessageBuilder msgToSendBuilder = new MessageBuilder();
        msgToSendBuilder.add("event","send");
        msgToSendBuilder.add("message",msgToSend);
        String message = msgToSendBuilder.buildString();
        client.sendMessage(message);
    }
}
