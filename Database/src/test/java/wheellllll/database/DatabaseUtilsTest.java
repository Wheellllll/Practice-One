package wheellllll.database;

import org.bson.types.ObjectId;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sweet on 5/16/16.
 */
public class DatabaseUtilsTest {
    @Test
    public void findAccount() throws Exception {


    }

    @Test
    public void addUserToMessage() throws Exception {
        String userName = "test";
        ObjectId messageId = DatabaseUtils.createMessage("test",userName);
        boolean temp = DatabaseUtils.addUserToMessage(userName, messageId);
        assertEquals(temp, true);
    }

    @Test
    public void syncAccount() throws Exception {
        DatabaseUtils.createAccount("222222","111111",1);
        ObjectId message = DatabaseUtils.createMessage("test","222222");
        boolean sync = DatabaseUtils.syncAccount("222222",message);
        assertEquals(sync, true);
    }

    @Test
    public void isExisted() throws Exception {
        boolean exist = DatabaseUtils.isExisted("q");
        assertEquals(exist, true);
    }

    @Test
    public void isValid() throws Exception {
        //假设"111111"是一个合法的账号(为被注册过)
        int temp = DatabaseUtils.isValid("111111","111111");
        assertEquals(temp, -1);
    }

    @Test
    public void createAccount() throws Exception {
        boolean temp = DatabaseUtils.createAccount("q", "qqqqqq", 1);
        assertEquals(temp, true);
    }

    @Test
    public void createMessage() throws Exception {
        ObjectId temp = DatabaseUtils.createMessage("hello", "q");
        assertNotEquals(temp, null);
    }

    @Test
    public void changeGroupId() throws Exception {
        //假设"111111"是一个已注册的账号,且不在group2
        boolean temp = DatabaseUtils.changeGroupId("111111","111111",2);
        assertEquals(temp, true);
    }

}