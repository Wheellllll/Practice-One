import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by sweet on 3/20/16.
 */
public class NIOClient {
    private static ArrayList<NIOClient> clients = new ArrayList<NIOClient>();

    private AsynchronousSocketChannel mSocketChannel;

    public NIOClient(AsynchronousSocketChannel socketChannel) {
        this.mSocketChannel = socketChannel;

        //开始接受消息
        readMessage();
    }

    private void readMessage() {
        /*
         * 读消息
         */
        final ByteBuffer buf = ByteBuffer.allocate(2048);

        mSocketChannel.read(buf, mSocketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {

            public void completed(Integer result, AsynchronousSocketChannel socketChannel) {
                if (result == -1) {
                    try {
                        System.out.format("Stopped listening to the client %s%n", mSocketChannel.getRemoteAddress());
                        mSocketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                String message = StringUtils.bufToString(buf);
                System.out.println(message);
                dispatchMessage(message);

                /*
                 * 处理下一条信息
                 */
                readMessage();
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel ) {
                System.out.println("fail to read message from client");
            }

        });
    }

    private void dispatchMessage(String message) {
        StringTokenizer st = new StringTokenizer(message, "|");


    }

    private void sendMessage(final String message) {
        /*
         * 发消息到客户端
         */
        ByteBuffer buf = ByteBuffer.allocate(2048);
        buf.put(message.getBytes());
        buf.flip();
        mSocketChannel.write(buf, mSocketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {

            public void completed(Integer result, AsynchronousSocketChannel channel) {
                //Nothing to do
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                System.out.println("Fail to write message to client");
            }

        });
    }


}
