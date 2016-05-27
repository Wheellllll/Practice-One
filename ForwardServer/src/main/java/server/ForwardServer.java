package server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import wheellllll.utils.MessageBuilder;
import wheellllll.utils.Network;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by sweet on 5/27/16.
 */
public class ForwardServer {
    public static void main(String args[]) {
        try {
            Client client = new Client();
            client.start();
            Network.register(client);
            client.connect(1000, "127.0.0.1", 54555, 54556);

            HashMap<String, String> msg = new MessageBuilder()
                    .add("_id", "31627368746831")
                    .add("from", "sweet")
                    .add("message", "hello world")
                    .add("date", "2016-05-27 00:39:40")
                    .buildMap();
            client.sendUDP(msg);
//            client.sendUDP(msg);
//            client.sendUDP(msg);
//            client.sendTCP(msg);
            String a = "aaaa";
            client.sendUDP(a);

            client.addListener(new Listener() {
                public void received (Connection connection, Object object) {
                    System.out.println("bbbbbbbbb");
                    HashMap<String, String> msg = (HashMap)object;
                }
            });

            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
