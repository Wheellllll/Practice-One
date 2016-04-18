package server;


import octoteam.tahiti.config.ConfigManager;
import octoteam.tahiti.config.loader.JsonAdapter;
import octoteam.tahiti.performance.PerformanceMonitor;
import octoteam.tahiti.performance.recorder.CountingRecorder;
import octoteam.tahiti.performance.reporter.LogReporter;
import octoteam.tahiti.performance.reporter.RollingFileReporter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

/**
 * Base server which implement the dirty works
 */
public abstract class BaseServer {
    public CountingRecorder validLoginRecorder = new CountingRecorder("Valid Login Number");
    public CountingRecorder invalidLoginRecorder = new CountingRecorder("Invalid Login Number");
    public CountingRecorder receiveMsgRecorder = new CountingRecorder("Receive Message Number");
    public CountingRecorder ignoreMsgRecorder = new CountingRecorder("Ignore Message Number");
    public CountingRecorder forwardMsgRecorder = new CountingRecorder("Forward Message Number");

    protected static boolean DEBUG = false;

    public static void DEBUG_MODE(boolean flag) {
        DEBUG = flag;
    }

    protected void initPerformance() {
        // 首先需要一个报告生成器, 此处我们建立 RollingFileReporter, 即
        // 生成报告到一组文件中. 由于时间输出格式是 yyyy-MM-dd_HH-mm, 因
        // 此将每分钟生成一个新文件.
        LogReporter reporter = new RollingFileReporter("serverRecord-%d{yyyy-MM-dd_HH-mm}.log");

        // 接下来创建性能监控实例
        PerformanceMonitor monitor = new PerformanceMonitor(reporter);

        // 将指标加入监控器, 并开始定时报告, 此处是每 1 分钟统计一次.
        monitor
                .addRecorder(validLoginRecorder)
                .addRecorder(invalidLoginRecorder)
                .addRecorder(receiveMsgRecorder)
                .addRecorder(ignoreMsgRecorder)
                .addRecorder(forwardMsgRecorder)
                .start(1, TimeUnit.MINUTES);
    }

    public BaseServer() {
        try {
            //此处json复用配置管理
            ConfigManager configManager = new ConfigManager(new JsonAdapter(), "./ServerConfig.json");
            ConfigBean config = configManager.loadToBean(ConfigBean.class);

            initPerformance();

            InetSocketAddress socketAddress = new InetSocketAddress(config.getHost(), config.getPort());
            AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel
                    .open()
                    .bind(socketAddress);
            System.out.format("Server is listening at %s%n", socketAddress);
            serverSocketChannel.accept(serverSocketChannel, new ConnectionHandler());

            if (!DEBUG) Thread.currentThread().join();
        } catch (IOException e) {
            System.out.format("Server failed to start: %s", e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Server Stopped");
        }
    }

    class ConnectionHandler implements
            CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

        public void completed(AsynchronousSocketChannel clientSock, AsynchronousServerSocketChannel serverSock) {
            new NIOClient(clientSock, BaseServer.this);
            //处理下一条连接
            serverSock.accept(serverSock, this);
        }

        public void failed(Throwable e, AsynchronousServerSocketChannel asynchronousServerSocketChannel) {
            System.out.println("Fail to connect to client");
        }
    }

}
