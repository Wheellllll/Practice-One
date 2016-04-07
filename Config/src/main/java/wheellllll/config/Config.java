package wheellllll.config;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;

import java.io.*;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * This class provide methods for config
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

    /**
     * Set config file name
     * @param configName config file name
     */
    public static void setConfigName(String configName) {
        Config.configName = configName;
    }

    /**
     * Get a singleton config
     * @return Config
     */
    public static final Config getConfig() {
        return ConfigHolder.INSTANCE;
    }


    public Config() {
        try {
            File configFile = new File(String.format("%s.conf", configName));
            FileReader reader = new FileReader(configFile);
            mProps = new Properties();
            mProps.load(reader);
            reader.close();
        } catch (FileNotFoundException e) {
            mProps = new Properties();
            setProperty("host", "localhost");
            setProperty("port", "9001");
            setProperty("MAX_NUMBER_PER_SECOND", "5");
            setProperty("MAX_NUMBER_PER_SESSION", "100");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                FileSystemManager fsManager = VFS.getManager();
                FileObject listendir = null;
                File configFile = new File(String.format("%s.conf", configName));
                listendir = fsManager.resolveFile(configFile.getAbsolutePath());
                DefaultFileMonitor fm = new DefaultFileMonitor(new FileListener() {
                    @Override
                    public void fileCreated(FileChangeEvent fileChangeEvent) throws Exception {
                        // Do nothing
                    }

                    @Override
                    public void fileDeleted(FileChangeEvent fileChangeEvent) throws Exception {
                        // Do nothing
                    }

                    @Override
                    public void fileChanged(FileChangeEvent fileChangeEvent) throws Exception {
                        File configFile = new File(String.format("%s.conf", configName));
                        FileReader reader = new FileReader(configFile);
                        mProps = new Properties();
                        mProps.load(reader);
                        reader.close();
                    }
                });
                fm.setRecursive(true);
                fm.addFile(listendir);
                fm.start();
            } catch (FileSystemException e) {
                e.printStackTrace();
            }

        }
    }

    public String getString(String key) {
        return mProps.getProperty(key);
    }

    public String getString(String key, String defaultValue) {
        return mProps.getProperty(key, defaultValue);
    }

    public Integer getInt(String key) {
        return Integer.parseInt(mProps.getProperty(key));
    }

    /**
     * Get property from config
     * @param key Key for the property
     * @return String value for the property
     */
    public String getProperty(String key) {
        return mProps.getProperty(key);
    }

    /**
     * Get property from config. If not existed, use default value
     * @param key Key for the property
     * @param defaultValue Default value for the property
     * @return String value for the property
     */
    public String getProperty(String key, String defaultValue) {
        return mProps.getProperty(key, defaultValue);
    }

    /**
     * Store a property to the config
     * @param key Key for the property
     * @param value Value for the property
     */
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
