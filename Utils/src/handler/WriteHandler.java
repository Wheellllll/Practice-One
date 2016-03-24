package handler;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by sweet on 3/24/16.
 */
public class WriteHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {
    @Override
    public void completed(Integer result, AsynchronousSocketChannel socketChannel) {

    }

    @Override
    public void failed(Throwable throwable, AsynchronousSocketChannel socketChannel) {

    }
}
