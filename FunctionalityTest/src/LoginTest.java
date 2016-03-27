import client.Client;
import server.Server;
import utils.MessageBuilder;

import java.util.HashMap;

/**
 * Created by summer on 3/27/16.
 */
public class LoginTest {
    public static void main(String args[])
    {
        Client.DEBUG_MODE(true);
        Client client = new Client() {
            @Override
            public void OnConnect(HashMap<String, String> args) {
                System.out.println("HHH");

            }

            @Override
            public void OnLogin(HashMap<String, String> args) {
                System.out.println("HHH");
            }
        };

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
