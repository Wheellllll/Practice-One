package wheellllll.database;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class is written for testing database
 */
public class DatabaseUtilsTest{

    @Test
    public void testIsExisted() throws Exception {

    }

    @Test
    public void testIsValid() throws Exception {

    }

    @Test
    public void testCreateAccount() throws Exception {

    }

    @Test
    public void testChangeGroupId() throws Exception {
        DatabaseUtils.createAccount("testname", "testpass", 5);
        DatabaseUtils.changeGroupId("testname", "testpass", 100);
        int gid = DatabaseUtils.isValid("testname", "testpass");
        assertEquals(100, gid);
    }
}