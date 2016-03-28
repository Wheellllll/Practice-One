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
 * Created by sweet on 3/18/16.
 */
public class SocketUtils {
    public static void sendMessage(AsynchronousSocketChannelWrapper socketWrapper, String message, CompletionHandler handler) {
        /*
         * 发消息
         */
        ByteBuffer buf;
        String pkgToSend;
        StringBuilder sb = new StringBuilder(message);

        if (handler == null) {
            handler = new WriteHandler();
        }

        while (sb.length() > 5) {
            pkgToSend = sb.substring(0, 5);
            sb.delete(0, 5);

            buf = ByteBuffer.allocate(2048);
            buf.put(pkgToSend.getBytes());
            buf.flip();
            socketWrapper.write(buf);
        }

        pkgToSend = sb.toString();

        buf = ByteBuffer.allocate(2048);
        buf.put(pkgToSend.getBytes());
        buf.put((byte)4);
        buf.flip();
        socketWrapper.write(buf);
    }

    public static void readMessage(AsynchronousSocketChannel socketChannel, CompletionHandler handler) {
        /*
         * 读消息
         */
        final ByteBuffer buf = ByteBuffer.allocate(2048);

        socketChannel.read(buf, new Attachment(socketChannel, buf), handler);
    }

    public static void dispatchMessage(EventManager eventManager, String message) {
        HashMap<String,String> args = JSON.parseObject(message, HashMap.class);
        String event = args.get("event");
        eventManager.triggerEvent(event, args);
    }

}
