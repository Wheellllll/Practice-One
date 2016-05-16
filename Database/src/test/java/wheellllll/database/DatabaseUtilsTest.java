package wheellllll.database;

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
    }

    @Test
    public void syncAccount() throws Exception {

    }

    @Test
    public void isExisted() throws Exception {
        boolean exist = DatabaseUtils.isExisted("q");

    }

    @Test
    public void isValid() throws Exception {

    }

    @Test
    public void createAccount() throws Exception {
        DatabaseUtils.createAccount("q", "qqqqqq", 1);
    }

    @Test
    public void createMessage() throws Exception {
        DatabaseUtils.createMessage("hello", "q");
    }

    @Test
    public void changeGroupId() throws Exception {

    }

}