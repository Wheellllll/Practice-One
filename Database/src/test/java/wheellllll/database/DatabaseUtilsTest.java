package wheellllll.database;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sweet on 5/16/16.
 */
public class DatabaseUtilsTest {
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

    }

    @Test
    public void changeGroupId() throws Exception {

    }

}