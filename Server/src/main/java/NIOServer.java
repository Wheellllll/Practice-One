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

                /*
                 * 读消息
                 */
                String msg = StringUtils.bufToString(buf);
                System.out.println(msg);

//                try {
//                    System.out.format("Client at  %s  says: %s%n", channel.getRemoteAddress(), msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//

                /*
                 * 写消息
                 */
                startWrite( channel, msg );

                /*
                 * 继续处理下一条信息
                 */
                startRead( channel );
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel ) {
                System.out.println( "fail to read message from client");
            }

        });
    }

    private void startWrite( AsynchronousSocketChannel sockChannel, final String message) {
        ByteBuffer buf = ByteBuffer.allocate(2048);
        buf.put(message.getBytes());
        buf.flip();
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
