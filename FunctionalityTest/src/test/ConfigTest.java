package test;

import wheellllll.config.Config;

/**
 * This class is written for testing the configuration of server address
 */
public class ConfigTest {
    public static void main(String args[])
    {
        Config conf = new Config();
        conf.setProperty("host","127.0.0.1");
        assert ("127.0.0.1" == conf.getProperty("host"));

    }
}
