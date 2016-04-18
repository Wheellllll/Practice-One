package server;

/**
 * Created by Kris Chan on 10:59 AM 4/17/16 .
 * All right reserved.
 */
public class ConfigBean {

    private String host;
    private int port;
    private int MAX_NUMBER_PER_SESSION;
    private int MAX_NUMBER_PER_SECOND;

    public int getMAX_NUMBER_PER_SECOND() {
        return MAX_NUMBER_PER_SECOND;
    }

    public void setMAX_NUMBER_PER_SECOND(int MAX_NUMBER_PER_SECOND) {
        this.MAX_NUMBER_PER_SECOND = MAX_NUMBER_PER_SECOND;
    }

    public int getMAX_NUMBER_PER_SESSION() {
        return MAX_NUMBER_PER_SESSION;
    }

    public void setMAX_NUMBER_PER_SESSION(int MAX_NUMBER_PER_SESSION) {
        this.MAX_NUMBER_PER_SESSION = MAX_NUMBER_PER_SESSION;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
