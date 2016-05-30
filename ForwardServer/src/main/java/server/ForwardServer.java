package server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import wheellllll.socket.SocketUtils;
import wheellllll.utils.MessageBuilder;
import wheellllll.utils.chatrmi.IForward;
import wheellllll.utils.chatrmi.Network;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by sweet on 5/27/16.
 */
public class ForwardServer {
    private int openPort;

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
            openPort = SocketUtils.getAvailablePort();
            server.bind(openPort);
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void connectRegisterCenter() {
        try {
            Client client = new Client();
            client.start();
            Network.register(client);
            client.connect(3000, Network.REGISTER_CENTER_HOST, Network.REGISTER_CENTER_PORT);

            HashMap<String, Object> msg = new HashMap<>();
            msg.put("class", IForward.class);
            msg.put("port", openPort);
            client.sendTCP(msg);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ForwardServer() {
        initServer();
        connectRegisterCenter();
    }

    public static void main(String args[]) {
        new ForwardServer();
    }

}
