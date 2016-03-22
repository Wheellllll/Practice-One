package ui;

import java.io.*;
import java.util.Properties;

/**
 * Created by sweet on 3/22/16.
 */
public class Config {
    /*
     * 单例模式
     */

    private Properties mProps;

    private static class ConfigHolder {
        private static final Config INSTANCE = new Config();
    }

    public static final Config getConfig() {
        return ConfigHolder.INSTANCE;
    }


    public Config() {
        try {
            File configFile = new File("application.conf");
            FileReader reader = new FileReader(configFile);
            mProps = new Properties();
            mProps.load(reader);
        } catch (FileNotFoundException e) {
            mProps = new Properties();
            setProperty("host", "localost");
            setProperty("port", "9001");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return mProps.getProperty(key);
    }

    public String getPropery(String key, String defaultValue) {
        return mProps.getProperty(key, defaultValue);
    }

    public void setProperty(String key, String value) {
        try {
            mProps.setProperty(key, value);
            File configFile = new File("application.conf");
            FileWriter writer = new FileWriter(configFile);
            mProps.store(writer, "host settings");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
