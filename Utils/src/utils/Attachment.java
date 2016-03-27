package utils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by sweet on 3/28/16.
 */
public class Attachment {
    public AsynchronousSocketChannel socketChannel;
    public ByteBuffer byteBuffer;

    public Attachment(AsynchronousSocketChannel socketChannel, ByteBuffer byteBuffer) {
        this.socketChannel = socketChannel;
        this.byteBuffer = byteBuffer;
    }
}
