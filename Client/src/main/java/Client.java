import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by sweet on 3/16/16.
 */
public class Client {

    public Client() {
        try {
            SocketAddress serverAddress = new InetSocketAddress("localhost", 9001);
            AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
            channel.connect(serverAddress, channel, new ConnectionHandler());
            sc = Executors.newScheduledThreadPool(1);
            sc.scheduleAtFixedRate(new ClientLogger(this), 0, 1, TimeUnit.MINUTES);
            Thread.currentThread().join();
        } catch (IOException e) {
            System.out.format("Fail to connect to server: %s", e.getMessage());
        } catch (InterruptedException e) {
            System.out.format("Stop to connect to server");
        }

    }

    private ScheduledExecutorService sc = null;
    private int loginSuccessNum = 0;
    private int loginFailNum = 0;
    private int sendMsgNum = 0;
    private int receiveMsgNum = 0;
    private String username = null;
    private String password = null;

    public int getLoginSuccessNum() {
        return loginSuccessNum;
    }

    public int getLoginFailNum() {
        return loginFailNum;
    }

    public int getSendMsgNum() {
        return sendMsgNum;
    }

    public int getReceiveMsgNum() {
        return receiveMsgNum;
    }

    private StringTokenizer mSt = null;


    class ConnectionHandler implements
            CompletionHandler<Void, AsynchronousSocketChannel> {

        public void completed(Void result, AsynchronousSocketChannel channel) {
            System.out.println("Connected");

            startRead(channel);

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

                mSt = new StringTokenizer(msg, "|");
                String event = mSt.nextToken();
                if (event.equals("success")) {
                    loginSuccessNum ++;
                } else if (event.equals("failed")) {
                    loginFailNum ++;
                } else if (event.equals("forward")) {
                    System.out.println("Forwarded message:" + mSt.nextToken());
                    receiveMsgNum ++;
                    startWrite(sockChannel, "ack|Receive forwarded message.");
                } else if (event.equals("Redo login")) {
                    startWrite(sockChannel, "login|" + username + "|" + password);
                }

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
                if (! message.equals("")) {
                    mSt = new StringTokenizer(message, "|");
                    String event = mSt.nextToken();
                    if (event.equals("send")) {
                        sendMsgNum ++;
                    } else if (event.equals("login")) {
                        username = mSt.nextToken();
                        password = mSt.nextToken();
                    }
                }

                try {
                    String msgToWrite = getTextFromUser();
                    startWrite(channel, msgToWrite);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                mSt = new StringTokenizer(message, "|");
                if (mSt.nextToken().equals("login"))
                    loginFailNum ++;
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
    }
}
