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
    public static void sendMessage(AsynchronousSocketChannel socketChannel, String message, CompletionHandler handler) {
        /*
         * 发消息
         */
        ByteBuffer buf = ByteBuffer.allocate(2048);
        buf.put(message.getBytes());
        //4表示传输结束
        buf.put((byte)4);
        buf.flip();

        if (handler == null) {
            handler = new WriteHandler();
        }

        socketChannel.write(buf, new Attachment(socketChannel, buf), handler);
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
