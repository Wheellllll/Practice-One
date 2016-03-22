import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.observers.Observers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by sweet on 3/16/16.
 */
public class Server {

    private ScheduledExecutorService sc = null;

    public Server() {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(Settings.HOST, Settings.PORT);
            AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel
                    .open()
                    .bind(socketAddress);
            System.out.format("Server is listening at %s%n", socketAddress);
            serverSocketChannel.accept(serverSocketChannel, new ConnectionHandler());
            sc = Executors.newScheduledThreadPool(1);
            sc.scheduleAtFixedRate(new ServerLogger(NIOClient.getClients()), 0, 5, TimeUnit.SECONDS);
            Thread.currentThread().join();
        } catch (IOException e) {
            System.out.format("Server failed to start: %s", e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Server Stopped");
        }
    }

    public static void main(String[] args) {
        new Server();
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
