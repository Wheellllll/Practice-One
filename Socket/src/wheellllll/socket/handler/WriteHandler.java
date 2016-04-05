package wheellllll.socket.handler;


import wheellllll.socket.model.Attachment;

import java.nio.channels.CompletionHandler;

/**
 * A write wheellllll.handler for this project which will be used by the wheellllll.socket
 */
public class WriteHandler implements CompletionHandler<Integer, Attachment> {
    @Override
    public void completed(Integer integer, Attachment attachment) {

    }

    @Override
    public void failed(Throwable throwable, Attachment attachment) {

    }
}
