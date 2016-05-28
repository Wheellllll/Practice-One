package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import wheellllll.utils.chatrmi.IAuth;
import wheellllll.utils.chatrmi.Network;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by sweet on 5/28/16.
 */
public class AuthServer {
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
            server.bind(Network.AUTH_PORT);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AuthServer() {
        initServer();
    }

    public static void main(String args[]) {
        new AuthServer();
    }
}
