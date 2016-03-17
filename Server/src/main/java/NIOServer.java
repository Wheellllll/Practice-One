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

public class NIOServer {
    /*
     * TODO: 管理socket连接池
     */

    public void runServer() throws Exception {
        String host = "localhost";
        int port = 8999;
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

            /**
             * some message is read from client, this callback will be called
             */
            public void completed(Integer result, AsynchronousSocketChannel channel  ) {
                buf.flip();

//                int limits = buf.limit();
//                byte bytes[] = new byte[limits];
//                attach.buffer.get(bytes, 0, limits);
//                Charset cs = Charset.forName("UTF-8");
//                String msg = new String(bytes, cs);
//                System.out.format("Client at  %s  says: %s%n", attach.clientAddr, msg);
//                attach.buffer.rewind();

                // echo the message
                startWrite( channel, buf );

                //start to read next message again
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
                //finish to write message to client, nothing to do
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                //fail to write message to client
                System.out.println( "Fail to write message to client");
            }

        });
    }

    class ConnectionHandler implements
            CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

        public void completed(AsynchronousSocketChannel clientSock, AsynchronousServerSocketChannel serverSock) {
            serverSock.accept(serverSock, this);

            startRead(clientSock);
        }

        public void failed(Throwable e, AsynchronousServerSocketChannel asynchronousServerSocketChannel) {
            e.printStackTrace();
        }
    }

}
//class ReadHandler implements CompletionHandler<Integer, Attachment> {
//    public void completed(Integer result, Attachment attach) {
//        if (result == -1) {
//            try {
//                attach.client.close();
//                System.out.format("Stopped   listening to the   client %s%n", attach.clientAddr);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            return;
//        }
//
//        //读消息
//        attach.buffer.flip();
//        int limits = attach.buffer.limit();
//        byte bytes[] = new byte[limits];
//        attach.buffer.get(bytes, 0, limits);
//        Charset cs = Charset.forName("UTF-8");
//        String msg = new String(bytes, cs);
//        System.out.format("Client at  %s  says: %s%n", attach.clientAddr, msg);
//        attach.buffer.rewind();
//
//        //回复消息
//        String msg = "HHH";
//        attach.buffer.put()
//
//
//        //继续处理下一条read
//        attach.buffer.clear();
//        attach.client.read(attach.buffer, attach, this);
//
//    }
//
//    public void failed(Throwable e, Attachment attach) {
//        e.printStackTrace();
//    }
//}

//class WriteHandler implements CompletionHandler<Integer, Attachment> {
//
//    public void completed(Integer result, Attachment attach) {
//        if (result == -1) {
//            try {
//                attach.client.close();
//                System.out.format("Stopped listening to the client %s%n", attach.clientAddr);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            return;
//        }
//
//        // Write to the client
////        attach.client.write(attach.buffer, attach, this);
//    }
//
//    public void failed(Throwable e, Attachment attachment) {
//        e.printStackTrace();
//    }
//}
