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

            Network.register(client);

            client.connect(1000, "127.0.0.1", 12460);

            IForward forward = ObjectSpace.getRemoteObject(client, Network.FORWARD, IForward.class);

            while (true) {
                String message = sc.nextLine();
                System.out.println(message);
                boolean result = forward.forward("127.0.0.1", 12450, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
