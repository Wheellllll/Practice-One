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

    /*
     * mSocketChannel 绑定的socket
     * mUsername 用户名
     * mPassword 密码
     * mStatus 当前状态->Settings.Status
     * mMsgPerSecond最近1秒发送的消息数
     * mMsgSinceLogin自从登陆起发送的消息数
     * mLastSendTime上次发送的时间戳
     *
     */
    private AsynchronousSocketChannel mSocketChannel;
    private String mUsername;
    private String mPassword;
    private int mStatus;
    private int mMsgPerSecond;
    private int mMsgSinceLogin;
    private int mLastSendTime;

    private StringTokenizer mSt;

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
                        /*
                         * 触发OnDisconnect事件
                         */
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                String message = StringUtils.bufToString(buf);
                dispatchMessage(message);
                System.out.println(message);

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
        /*
         * TODO: 使用RxJava注册事件和分发事件
         */
        mSt = new StringTokenizer(message, "|");
        String event = mSt.nextToken();
        if (event.equals("reg")) {
            /*
             * 触发OnRegister事件
             */
            register();
        } else if (event.equals("login")) {
            /*
             * 触发OnLogin事件
             */
            login();
        } else if(event.equals("send")) {
            /*
             * 触发OnSend事件
             */
            send();
        }
    }

    private void sendMessage(final String message) {
        /*
         * 发消息
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

    public void register() {

    }

    public void login() {

    }

    public void send() {

    }

}
