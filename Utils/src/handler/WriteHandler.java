package handler;

import utils.Attachment;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * A write handler for this project which will be used by the socket
 */
public class WriteHandler implements CompletionHandler<Integer, Attachment> {
    @Override
    public void completed(Integer integer, Attachment attachment) {

    }

    @Override
    public void failed(Throwable throwable, Attachment attachment) {

    }
}
