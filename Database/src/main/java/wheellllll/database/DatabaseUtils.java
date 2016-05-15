package wheellllll.database;

import com.mongodb.*;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

/**
 * This class provide methods for SQLite database.
 *
 */
public class DatabaseUtils {

    /**
     * JDBC_DRIVER of SQLite & database url
     */
    private static String DB_HOST ="localhost";
    private static int DB_PORT = 27017;
    private static String DB_NAME = "software_reuse";
    private static String COLL_ACCOUNT_NAME = "account";
    private static String COLL_MESSAGE_NAME = "message";
    private static DB db = null;

    /**
     * This method create a connection to database use the <code>DB_HOST</code> and <code>DB_PORT</code>.
     * The table will be created if it is not existing.
     *
     * @return Connection to the database
     */
    private static DB getDb() {
        try {
            if (db == null) {
                MongoClient mongoClient = new MongoClient(DB_HOST, DB_PORT);
                db = mongoClient.getDB(DB_NAME);
                /*
                 * account(username, password, groupid, lastupdate)
                 * message(id, from, to, utime)
                 */
                if (!db.collectionExists("account")) db.createCollection("account", new BasicDBObject());
                if (!db.collectionExists("message")) db.createCollection("message", new BasicDBObject());
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return db;
    }

    /**
     * This method check whether a username exist in database.
     *
     * @param username The username that needs to check existence
     * @return boolean Return true if the username exist, otherwise false
     */
    public static boolean isExisted(String username) {
        DBCollection accountColl = getDb().getCollection("account");
        BasicDBObject query = new BasicDBObject("username", username);

        DBCursor cursor = accountColl.find(query);

        try {
            if (cursor.hasNext()) {
                return true;
            } else {
                return false;
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * This method check whether a username and password combination is existed in database.
     *
     * @param username Username of client
     * @param password Password of client, the password is encrypted with md5
     * @return Return the group id of the account if the username and password combination exists in database, otherwise -1
     */
    public static int isValid(String username, String password) {
        DBCollection accountColl = getDb().getCollection("account");
        BasicDBObject query = new BasicDBObject("username", username)
                .append("password", password);

        DBCursor cursor = accountColl.find(query);
        try {
            if (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                int groupId = obj.getInt("groupid");
                return groupId;
            }
        } finally {
            cursor.close();
        }

        return -1;
    }

    /**
     * This method create a new account in database.
     *
     * @param username Username of client
     * @param password Password of client
     * @return boolean Return true if creating account successfully, otherwise false
     */
    public static boolean createAccount(String username, String password, int groupid) {
        DBCollection accountColl = getDb().getCollection("account");

        BasicDBObject doc = new BasicDBObject("username", username)
                .append("password", password)
                .append("groupid", groupid)
                .append("lastupdate", null);
        accountColl.insert(doc);

        return true;
    }

    /**
     * This method create a new message in database.
     *
     * @param message Message to send
     * @param from Username who send the message
     * @return boolean Return true if creating message successfully, otherwise false
     */
    public static boolean createMessage(String message, String from) {
        DBCollection messageColl = getDb().getCollection("message");

        BasicDBObject doc = new BasicDBObject("message", message)
                .append("from", from)
                .append("to", new BasicDBList())
                .append("utime", System.currentTimeMillis());
        messageColl.insert(doc);

        return true;

    }

    public static boolean addUserToMessage(String username, ObjectId messageId) {
        DBCollection messageColl = getDb().getCollection("message");

        BasicDBObject doc = (BasicDBObject)messageColl.findOne(new BasicDBObject("_id", messageId));
        if (doc != null) {
            BasicDBList toList = (BasicDBList)doc.get("to");
            if (!toList.contains(username)) toList.add(username);
            messageColl.update(new BasicDBObject("_id", messageId), new BasicDBObject("to", toList));
        }

        return true;
    }

    public static boolean changeGroupId(String username, String password, int newGroupId) {
        DBCollection accountColl = getDb().getCollection("account");
        accountColl.update(new BasicDBObject("username", username), new BasicDBObject("groupid", newGroupId));

        return true;
    }

}
