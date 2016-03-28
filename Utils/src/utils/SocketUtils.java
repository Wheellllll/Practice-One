package utils;

import com.alibaba.fastjson.JSON;
import event.EventManager;
import handler.PackageHandler;
import handler.ReadHandler;
import handler.WriteHandler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;

/**
 * This class provide methods for sending message to socket
 * or reading message from socket
 */
public class SocketUtils {
    /**
     * This method send a message to <code>AsynchronousSocketChannel</code>
     *
     * @param socketWrapper Socket where message should be send to
     * @param message A string to send
     * @param handler Handler to invoke after finish reading message from socket
     */
    public static void sendMessage(AsynchronousSocketChannelWrapper socketWrapper, String message, CompletionHandler handler) {
        /*
         * 发消息
         */
        message = message + "\u0004";

        ByteBuffer buf;
        byte[] bytes = message.getBytes();
        byte[] pkgToSend = new byte[2048];
        int pos = 0;

        if (handler == null) {
            handler = new WriteHandler();
        }

        while (bytes.length - pos > 2048) {
            System.arraycopy(bytes, pos, pkgToSend, 0, 2048);
            pos += 2048;

            buf = ByteBuffer.allocate(2048);
            buf.put(pkgToSend);
            buf.flip();
            socketWrapper.write(buf);
        }

        pkgToSend = new byte[bytes.length - pos];
        System.arraycopy(bytes, pos, pkgToSend, 0, bytes.length - pos);

        buf = ByteBuffer.allocate(2048);
        buf.put(pkgToSend);
        buf.flip();
        socketWrapper.write(buf);
    }

    /**
     * This method read a message from <code>AsynchronousSocketChannel</code>
     *
     * @param socketChannel Socket where message should be send to
     * @param handler Handler to invoke after finish reading message from socket
     */
    public static void readMessage(AsynchronousSocketChannel socketChannel, CompletionHandler handler) {
        /*
         * 读消息
         */
        final ByteBuffer buf = ByteBuffer.allocate(2048);

        socketChannel.read(buf, new Attachment(socketChannel, buf), handler);
    }

    /**
     * This method dispatch event depending on the message
     *
     * @param eventManager A event manager that charge for the dispatch
     * @param message A message which include the event
     */
    public static void dispatchMessage(EventManager eventManager, String message) {
        HashMap<String,String> args = JSON.parseObject(message, HashMap.class);
        String event = args.get("event");
        eventManager.triggerEvent(event, args);
    }

}
