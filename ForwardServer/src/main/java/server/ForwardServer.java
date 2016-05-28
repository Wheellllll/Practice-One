package server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import wheellllll.utils.chatrmi.IForward;
import wheellllll.utils.chatrmi.Network;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by sweet on 5/27/16.
 */
public class ForwardServer {
    class Forward extends Connection implements IForward {
        public Forward() {
            new ObjectSpace(this).register(Network.FORWARD, this);
        }

        @Override
        public boolean forward(String host, int port, HashMap<String, String> args) {
            try {
                Client client = new Client();
                client.start();

                Network.register(client);

                client.connect(1000, host, port);
                client.sendTCP(args);
                client.close();

                System.out.println("Message Sended");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

    }

    protected void initServer() {
        try {
            Server server = new Server() {
                @Override
                protected Connection newConnection() {
                    return new Forward();
                }
            };
            Network.register(server);
            server.bind(Network.FORWARD_PORT);
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ForwardServer() {
        initServer();
    }

    public static void main(String args[]) {
        new ForwardServer();
    }

}
