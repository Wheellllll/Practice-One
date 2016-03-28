package utils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Attachment include some necessary information for socket to use
 */
public class Attachment {
    public AsynchronousSocketChannel socketChannel;
    public ByteBuffer byteBuffer;

    public Attachment(AsynchronousSocketChannel socketChannel, ByteBuffer byteBuffer) {
        this.socketChannel = socketChannel;
        this.byteBuffer = byteBuffer;
    }
}
