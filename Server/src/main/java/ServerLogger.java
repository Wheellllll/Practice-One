import utils.LogUtils;

import java.util.ArrayList;

/**
 * The <code>ServerLogger</code> class implement <code>Runnable</code> interface, it is used for
 * logging the relative data of server.
 * <p>
 * This class is designed to be called as a parameter of a <code>ScheduledExecutorService</code>
 * to log the numbers counted during the server running time every minute, including valid login
 * number, invalid login number, received message number, ignored message number, forwarded
 * message number.
 *
 * @author LiaoShanhe
 */
public class ServerLogger implements Runnable {

    /**
     * ArrayList of BaseClient, data will be collected from all elements of the array
     */
    private ArrayList<BaseClient> clients = null;
    /**
     * Total valid login number
     */
    private int validLogin = 0;
    /**
     * Total invalid login number
     */
    private int invalidLogin = 0;
    /**
     * Total received message number
     */
    private int receiveMsgNum = 0;
    /**
     * Total ignored message number
     */
    private int ignoreMsgNum = 0;
    /**
     * Total forwarded message number
     */
    private int forwardMsgNum = 0;

    /**
     * Constructor
     */
    public ServerLogger(ArrayList<BaseClient> c) {
        clients = c;
    }

    /**
     * Override the <code>run</code> method of <code>Runnable</code> interface. In this method
     * static method <code>log</code> of <code>LogUtils</code> will be called.
     * <p>
     * Because for every connect between server and a client there is a <code>NIOClient</code>
     * instance, so the total number must be the sum of all NIOClient instance.
     *
     * @see utils.LogUtils#log(LogUtils.LogType, int...)
     */
    @Override
    public void run() {
        for (BaseClient client : clients) {
            validLogin += client.getLocalValidLogin();
            invalidLogin += client.getLocalInvalidLogin();
            receiveMsgNum += client.getLocalReceiveMsgNum();
            ignoreMsgNum += client.getLocalIgnoreMsgNum();
            forwardMsgNum += client.getLocalForwardMsgNum();
        }
        LogUtils.log(LogUtils.LogType.SERVER, validLogin, invalidLogin, receiveMsgNum, ignoreMsgNum, forwardMsgNum);
        validLogin = 0;
        invalidLogin = 0;
        receiveMsgNum = 0;
        ignoreMsgNum = 0;
        forwardMsgNum = 0;
    }
}
