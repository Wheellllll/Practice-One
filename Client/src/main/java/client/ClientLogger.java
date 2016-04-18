package client;

import wheellllll.performance.LogUtils;

/**
 * The <code>ClientLogger</code> class implement <code>Runnable</code> interface, it is used for
 * logging the relative data of <code>BaseClient</code>.
 * <p>
 * This class is designed to be called as a parameter of a <code>ScheduledExecutorService</code>
 * to utils the numbers counted during a client running time every minute, including login successfully
 * number, login fail number, send message number, receive message number.
 *
 */
public class ClientLogger implements Runnable {

    /**
     * BaseClient instance that needs logging
     */
    private BaseClient client = null;

    /**
     * Constructor
     */
    public ClientLogger(BaseClient c) {
        client = c;
    }

    /**
     * Override the <code>run</code> method of <code>Runnable</code> interface. In this method
     * static method <code>utils</code> of <code>LogUtils</code> will be called.
     *
     * @see LogUtils#log(LogUtils.LogType, int...)
     */
    @Override
    public void run() {
        LogUtils.log(LogUtils.LogType.CLIENT, client.getLoginSuccessNum(),
                client.getLoginFailNum(), client.getSendMsgNum(), client.getReceiveMsgNum());
    }
}
