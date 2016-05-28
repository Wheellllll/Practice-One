package server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import wheellllll.utils.chatrmi.IForward;
import wheellllll.utils.chatrmi.Network;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by sweet on 5/27/16.
 */
public class AccessServer {
    public static void main(String args[]) {
        try {
            Scanner sc = new Scanner(System.in);

            Client client = new Client();
            client.start();

//            Network.registerDatabase(client);
            Network.register(client);

//            client.connect(1000, Network.DATABASE_HOST, Network.DATABASE_PORT);
            client.connect(1000, Network.FORWARD_HOST, Network.FORWARD_PORT);

            IForward forward = ObjectSpace.getRemoteObject(client, Network.FORWARD, IForward.class);
//            IChatDatabase chatDatabase = ObjectSpace.getRemoteObject(client, Network.DATABASE, IChatDatabase.class);

            while (true) {
                String message = sc.nextLine();
                System.out.println(message);
                forward.forward("127.0.0.1", 12450, null);
//                chatDatabase.saveMessage(message, "q");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
