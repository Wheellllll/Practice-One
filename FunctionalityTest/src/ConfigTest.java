import utils.Config;

/**
 * Created by summer on 3/28/16.
 */
public class ConfigTest {
    public static void main(String args[])
    {
        Config conf = new Config();
        conf.setProperty("host","127.0.0.1");
        assert ("127.0.0.1" == conf.getProperty("host"));

    }
}
