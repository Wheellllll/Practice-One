package client;

import octoteam.tahiti.performance.PerformanceMonitor;
import octoteam.tahiti.performance.recorder.CountingRecorder;
import octoteam.tahiti.performance.reporter.LogReporter;
import octoteam.tahiti.performance.reporter.RollingFileReporter;
import wheellllll.performance.LogUtils;

import java.util.concurrent.TimeUnit;

/**
 * The <code>ClientLogger</code> class implement <code>Runnable</code> interface, it is used for
 * logging the relative data of <code>BaseClient</code>.
 * <p>
 * This class is designed to be called as a parameter of a <code>ScheduledExecutorService</code>
 * to utils the numbers counted during a client running time every minute, including login successfully
 * number, login fail number, send message number, receive message number.
 *
 * @author LiaoShanhe
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
        LogReporter reporter = new RollingFileReporter("./log/client-%d{yyyy-MM-dd_HH-mm}.log");
        PerformanceMonitor monitor = new PerformanceMonitor(reporter);
        CountingRecorder loginSuccessNum = new CountingRecorder("Login success number");
        CountingRecorder sendMsgNum = new CountingRecorder("Send message number");
        CountingRecorder receiveMsgNum = new CountingRecorder("Receive message number");

        monitor
                .addRecorder(loginSuccessNum)
                .addRecorder(sendMsgNum)
                .addRecorder(receiveMsgNum)
                .start(1, TimeUnit.MINUTES);

        /*
        LogUtils.log(LogUtils.LogType.CLIENT, client.getLoginSuccessNum(),
                client.getLoginFailNum(), client.getSendMsgNum(), client.getReceiveMsgNum());*/
    }
}
