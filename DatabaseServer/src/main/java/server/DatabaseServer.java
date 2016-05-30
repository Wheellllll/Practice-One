package server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import wheellllll.database.DatabaseUtils;
import wheellllll.socket.SocketUtils;
import wheellllll.utils.chatrmi.IChatDatabase;
import wheellllll.utils.chatrmi.IForward;
import wheellllll.utils.chatrmi.Network;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by sweet on 5/28/16.
 */
public class DatabaseServer {
    private int openPort;

    class ChatDatabase extends Connection implements IChatDatabase {
        public ChatDatabase() {
            new ObjectSpace(this).register(Network.DATABASE, this);
        }

        @Override
        public BasicDBObject saveMessage(String message, String from) {
            System.out.println("Message Saved");
            return DatabaseUtils.createMessage(message, from);
        }

        @Override
        public boolean syncAccount(String username, ObjectId messageId) {
            System.out.println("User Synced");
            return DatabaseUtils.syncAccount(username, messageId);
        }

        @Override
        public boolean addUserToMessage(String username, ObjectId messageId) {
            System.out.println("Message Added to User");
            return DatabaseUtils.addUserToMessage(username, messageId);
        }

    }

    protected void initServer() {
        try {
            Server server = new Server() {
                @Override
                protected Connection newConnection() {
                    return new ChatDatabase();
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
            msg.put("class", IChatDatabase.class);
            msg.put("port", openPort);
            client.sendTCP(msg);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DatabaseServer() {
        initServer();
        connectRegisterCenter();
    }

    public static void main(String args[]) {
        new DatabaseServer();
    }
}
