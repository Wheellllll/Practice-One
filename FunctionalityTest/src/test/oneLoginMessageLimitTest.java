package test;

import client.Client;
import server.Server;
import utils.MessageBuilder;

import java.util.HashMap;

/**
 * Created by summer on 3/29/16.
 */
public class oneLoginMessageLimitTest {
    public static void main(String args[])
    {
        Server.DEBUG_MODE(true);
        Client.DEBUG_MODE(true);
        Server server = new Server();
        String msgToSend = "test";
        Client client = new Client() {


            @Override
            public void OnRelogin(HashMap<String, String> msg)
            {
                assert("event" == "relogin");

            }
        };
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
    }
}
