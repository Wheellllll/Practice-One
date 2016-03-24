import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.observers.Observers;
import utils.Config;

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
public class Server extends BaseServer {
    public static void main(String[] args) {
        new Server();
    }
}
