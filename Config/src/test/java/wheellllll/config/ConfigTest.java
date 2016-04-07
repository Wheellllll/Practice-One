package wheellllll.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class is written for testing configuration
 */
public class ConfigTest {
    @Test
    public void getString() throws Exception {
        Config.getConfig().setProperty("foo1", "bar");
        assertEquals("bar", Config.getConfig().getString("foo1"));
    }

    @Test
    public void getInt() throws Exception {
        Config.getConfig().setProperty("foo2", "1024");
        assertEquals(1024, Config.getConfig().getInt("foo2"));
    }

    @Test
    public void getLong() throws Exception {
        Config.getConfig().setProperty("foo3", "1024");
        assertEquals(1024L, Config.getConfig().getLong("foo3"));
    }

    @Test
    public void getBool() throws Exception {
        Config.getConfig().setProperty("foo4", "true");
        assertEquals(true, Config.getConfig().getBool("foo4"));
    }

    @Test
    public void getFloat() throws Exception {
        Config.getConfig().setProperty("foo5", "10.24");
        assertEquals(10.24, Config.getConfig().getFloat("foo5"), 0.0001);
    }

    @Test
    public void getDouble() throws Exception {
        Config.getConfig().setProperty("foo6", "10.24");
        assertEquals(10.24, Config.getConfig().getDouble("foo6"), 0.0001);

    }

}