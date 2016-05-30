package server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import wheellllll.socket.SocketUtils;
import wheellllll.utils.chatrmi.IAuth;
import wheellllll.utils.chatrmi.IForward;
import wheellllll.utils.chatrmi.Network;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by sweet on 5/28/16.
 */
public class AuthServer {
    private int openPort;

    class Auth extends Connection implements IAuth {
        public Auth() {
            new ObjectSpace(this).register(Network.AUTH, this);
        }

        @Override
        public HashMap login(HashMap<String, String> args) {
            return null;
        }

        @Override
        public HashMap relogin(HashMap<String, String> args) {
            return null;
        }

        @Override
        public HashMap register(HashMap<String, String> args) {
            return null;
        }

    }

    protected void initServer() {
        try {
            Server server = new Server() {
                @Override
                protected Connection newConnection() {
                    return new Auth();
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
            msg.put("class", IAuth.class);
            msg.put("port", openPort);
            client.sendTCP(msg);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AuthServer() {
        initServer();
        connectRegisterCenter();
    }

    public static void main(String args[]) {
        new AuthServer();
    }
}
