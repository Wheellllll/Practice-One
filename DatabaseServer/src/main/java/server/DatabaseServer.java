package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import wheellllll.database.DatabaseUtils;
import wheellllll.utils.chatrmi.IChatDatabase;
import wheellllll.utils.chatrmi.Network;

import java.io.IOException;

/**
 * Created by sweet on 5/28/16.
 */
public class DatabaseServer {

    class ChatDatabase extends Connection implements IChatDatabase {
        public ChatDatabase() {
            new ObjectSpace(this).register(Network.DATABASE, this);
        }

        public BasicDBObject saveMessage(String message, String from) {
            return DatabaseUtils.createMessage(message, from);
        }

        public boolean syncAccount(String username, ObjectId messageId) {
            return DatabaseUtils.syncAccount(username, messageId);
        }

        public boolean addUserToMessage(String username, ObjectId messageId) {
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
            server.bind(Network.DATABASE_PORT);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DatabaseServer() {
        initServer();
    }

    public static void main(String args[]) {
        new DatabaseServer();
    }
}
