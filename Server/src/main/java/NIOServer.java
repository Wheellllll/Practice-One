/**
 * Created by sweet on 3/17/16.
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class NIOServer {
    public void runServer() throws Exception {
        String host = "localhost";
        int port = 9001;
        InetSocketAddress sockAddr = new InetSocketAddress(host, port);
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel
                .open()
                .bind(sockAddr);

        System.out.format("Server is listening at %s%n", sockAddr);
        server.accept(server, new ConnectionHandler());
        Thread.currentThread().join();
    }

    private void startRead( AsynchronousSocketChannel sockChannel ) {
        final ByteBuffer buf = ByteBuffer.allocate(2048);

        sockChannel.read( buf, sockChannel, new CompletionHandler<Integer, AsynchronousSocketChannel >() {

            public void completed(Integer result, AsynchronousSocketChannel channel  ) {
                if (result == -1) {
                    try {
                        System.out.format("Stopped listening to the client %s%n", channel.getRemoteAddress());
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                buf.flip();

                int limits = buf.limit();
                byte bytes[] = new byte[limits];
                buf.get(bytes);
                Charset cs = Charset.forName("UTF-8");
                String msg = new String(bytes, cs);

                try {
                    System.out.format("Client at  %s  says: %s%n", channel.getRemoteAddress(), msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(msg);
                buf.rewind();

                startWrite( channel, buf );

                startRead( channel );
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel ) {
                System.out.println( "fail to read message from client");
            }

        });
    }

    private void startWrite( AsynchronousSocketChannel sockChannel, final ByteBuffer buf) {
        sockChannel.write(buf, sockChannel, new CompletionHandler<Integer, AsynchronousSocketChannel >() {

            public void completed(Integer result, AsynchronousSocketChannel channel) {
                //Nothing to do
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                System.out.println( "Fail to write message to client");
            }

        });
    }

    class ConnectionHandler implements
            CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

        public void completed(AsynchronousSocketChannel clientSock, AsynchronousServerSocketChannel serverSock) {
            //处理下一条连接
            serverSock.accept(serverSock, this);

            //接受消息
            startRead(clientSock);
        }

        public void failed(Throwable e, AsynchronousServerSocketChannel asynchronousServerSocketChannel) {
            System.out.println("Fail to connect to client");
        }
    }

}
