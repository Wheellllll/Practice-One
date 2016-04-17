package server;

import server.config.ConfigBean;
import server.config.JsonAdapter;
import server.config.YamlAdapter;
import wheellllll.config.Config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Base server which implement the dirty works
 */
public abstract class BaseServer {
    private ScheduledExecutorService sc = null;

    protected static boolean DEBUG = false;

    public static void DEBUG_MODE(boolean flag) {
        DEBUG = flag;
    }

    public BaseServer() {
        try {
            Config.setConfigName("server");
            //此处json复用配置管理
            //ConfigManager configManager = new ConfigManager(new JsonAdapter(), "./config.json");
            //ConfigBean config = configManager.loadToBean(ConfigBean.class);

            //此处yaml复用配置管理
            ConfigManager configManager = new ConfigManager(new server.config.YamlAdapter(), "./config.yaml");
            ConfigBean config = configManager.loadToBean(ConfigBean.class);

            InetSocketAddress socketAddress = new InetSocketAddress(config.getHost(), config.getPort());
            AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel
                    .open()
                    .bind(socketAddress);
            System.out.format("Server is listening at %s%n", socketAddress);
            serverSocketChannel.accept(serverSocketChannel, new ConnectionHandler());
            sc = Executors.newScheduledThreadPool(1);
            sc.scheduleAtFixedRate(new ServerLogger(NIOClient.getClients()), 0, 1, TimeUnit.MINUTES);

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
            new NIOClient(clientSock);
            //处理下一条连接
            serverSock.accept(serverSock, this);
        }

        public void failed(Throwable e, AsynchronousServerSocketChannel asynchronousServerSocketChannel) {
            System.out.println("Fail to connect to client");
        }
    }

}
