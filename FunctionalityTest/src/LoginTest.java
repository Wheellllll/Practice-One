import server.Server;


import utils.MessageBuilder;

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



        String msgToSend = new MessageBuilder()
                .add("event", "login")
                .add("username", "summer")
                .add("password", "12346")
                .build();
        client.sendMessage(msgToSend);

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
