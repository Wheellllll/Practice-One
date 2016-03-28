package handler;

import utils.Attachment;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by sweet on 3/24/16.
 */
public class WriteHandler implements CompletionHandler<Integer, Attachment> {
    @Override
    public void completed(Integer integer, Attachment attachment) {

    }

    @Override
    public void failed(Throwable throwable, Attachment attachment) {

    }
}
