import ui.ChatRoomForm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by sweet on 3/16/16.
 */
public class Client {

    public Client() {
        try {
            SocketAddress serverAddress = new InetSocketAddress("localhost", 9001);
            AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
            channel.connect(serverAddress, channel, new ConnectionHandler());
            Thread.currentThread().join();
        } catch (IOException e) {
            System.out.format("Fail to connect to server: %s", e.getMessage());
        } catch (InterruptedException e) {
            System.out.format("Stop to connect to server");
        }

    }

    class ConnectionHandler implements
            CompletionHandler<Void, AsynchronousSocketChannel> {

        public void completed(Void result, AsynchronousSocketChannel channel) {
            System.out.println("Connected");

            startRead(channel);

                /*try {
                    String msgToWrite = getTextFromUser();
                    startWrite(channel, msgToWrite);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            try {
                String msgToWrite = getTextFromUser();
                startWrite(channel, msgToWrite);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void failed(Throwable e, AsynchronousSocketChannel asynchronousSocketChannel) {
            System.out.println("Fail to connect to server");
        }
    }

        private void startRead( final AsynchronousSocketChannel sockChannel) {
        final ByteBuffer buf = ByteBuffer.allocate(2048);

        sockChannel.read( buf, sockChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>(){

            public void completed(Integer result, AsynchronousSocketChannel channel) {
                String msg = StringUtils.bufToString(buf);
                System.out.println("Read message:" + msg);

                /*
                 * 继续处理下一条信息
                 */
                startRead(channel);
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                System.out.println( "Fail to read message from server");
            }

        });

    }

    private void startWrite( final AsynchronousSocketChannel sockChannel, final String message) {
        ByteBuffer buf = ByteBuffer.allocate(2048);
        buf.put(message.getBytes());
        buf.flip();
        sockChannel.write(buf, sockChannel, new CompletionHandler<Integer, AsynchronousSocketChannel >() {
            public void completed(Integer result, AsynchronousSocketChannel channel ) {
                //Nothing to do

                try {
                    String msgToWrite = getTextFromUser();
                    startWrite(channel, msgToWrite);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                System.out.println( "Fail to write the message to server");
            }
        });
    }


    private String getTextFromUser() throws Exception{
        System.out.print("Please enter a  message  (Bye  to quit):");
        BufferedReader consoleReader = new BufferedReader(
                new InputStreamReader(System.in));
        String msg = consoleReader.readLine();
        return msg;
    }

    public static void main(String[] args) {
        new Client();

//        new LoginAndRegisterForm();
//        new ChatRoomForm();

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
