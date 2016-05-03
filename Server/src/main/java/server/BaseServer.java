package server;


import octoteam.tahiti.config.ConfigManager;
import octoteam.tahiti.config.loader.JsonAdapter;
import wheellllll.performance.ArchiveManager;
import wheellllll.performance.IntervalLogger;
import wheellllll.performance.RealtimeLogger;

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
    IntervalLogger intervalLogger = new IntervalLogger();
    RealtimeLogger realtimeLogger = new RealtimeLogger();
    ArchiveManager archiveManager = new ArchiveManager();

    protected static boolean DEBUG = false;

    public static void DEBUG_MODE(boolean flag) {
        DEBUG = flag;
    }

    protected void initPerformance() {
        //初始化intervalLogger
        intervalLogger.setLogDir("./serverlog");
        intervalLogger.setLogPrefix("server");
        intervalLogger.setLogSuffix("log");
        intervalLogger.setDateFormat("yyyy-MM-dd HH_mm");
        intervalLogger.setInitialDelay(1);
        intervalLogger.setInterval(1, TimeUnit.MINUTES);

        intervalLogger.addIndex("Valid Login Number");
        intervalLogger.addIndex("Invalid Login Number");
        intervalLogger.addIndex("Receive Message Number");
        intervalLogger.addIndex("Ignore Message Number");
        intervalLogger.addIndex("Forward Message Number");
        intervalLogger.setFormatPattern(
                "Valid Login Number : ${Valid Login Number}\n" +
                "Invalid Login Number : ${Invalid Login Number}\n" +
                "Receive Message Number : ${Receive Message Number}\n" +
                "Ignore Message Number : ${Ignore Message Number}\n" +
                "Forward Message Number : ${Forward Message Number}\n\n");
        intervalLogger.start();

        //初始化realtimeLogger
        realtimeLogger.setLogDir("./serverlog");
        realtimeLogger.setLogPrefix("server");
        realtimeLogger.setLogSuffix("mlog");
        realtimeLogger.setFormatPattern(
                "Username : ${username}\n" +
                "Time : ${time}\n" +
                "Message : ${message}\n\n");

        //初始化archiveManager
        archiveManager.setArchiveDir("./serverarchive");
        archiveManager.setDatePattern("yyyy-MM-dd");
        archiveManager.addLogger(intervalLogger);
        archiveManager.addLogger(realtimeLogger);
        archiveManager.setInitialDelay(1);
        archiveManager.setInterval(1, TimeUnit.DAYS);
        archiveManager.start();
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
