package server;
import org.bson.types.ObjectId;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Kris Chan on 3:17 PM 5/31/16 .
 * All right reserved.
 */
public class ChatDatabaseTest {

    @Test
    public void testSaveMessage() throws Exception {
        DatabaseServer databaseServer = new DatabaseServer();
        String message = "test";
        String from = "testUsr";
        DatabaseServer.ChatDatabase test = databaseServer.new ChatDatabase();
        test.saveMessage(message,from);
    }

    @Test
    public void testSyncAccount() throws Exception {
        DatabaseServer databaseServer = new DatabaseServer();
        String message = "test";
        ObjectId objectId = ObjectId.massageToObjectId(1);
        DatabaseServer.ChatDatabase test = databaseServer.new ChatDatabase();
        boolean tmp = test.syncAccount(message,objectId);
        assertTrue(tmp);
    }

    @Test
    public void testAddUserToMessage() throws Exception {
        DatabaseServer databaseServer = new DatabaseServer();
        String message = "test";
        ObjectId objectId = ObjectId.massageToObjectId(1);
        DatabaseServer.ChatDatabase test = databaseServer.new ChatDatabase();
        boolean tmp = test.addUserToMessage(message,objectId);
        assertTrue(true);
    }
}