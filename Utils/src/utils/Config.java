package utils;

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
    private static String configName = "application";

    private static class ConfigHolder {
        private static final Config INSTANCE = new Config();
    }

    public static void setConfigName(String configName) {
        Config.configName = configName;
    }

    public static final Config getConfig() {
        return ConfigHolder.INSTANCE;
    }


    public Config() {
        try {
            File configFile = new File(String.format("%s.conf", configName));
            FileReader reader = new FileReader(configFile);
            mProps = new Properties();
            mProps.load(reader);
        } catch (FileNotFoundException e) {
            mProps = new Properties();
            setProperty("host", "localhost");
            setProperty("port", "9001");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return mProps.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return mProps.getProperty(key, defaultValue);
    }

    public void setProperty(String key, String value) {
        try {
            mProps.setProperty(key, value);
            File configFile = new File(String.format("%s.conf", configName));
            FileWriter writer = new FileWriter(configFile);
            mProps.store(writer, "host settings");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
