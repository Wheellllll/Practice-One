package handler;

import event.EventManager;
import utils.Attachment;
import utils.SocketUtils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * A read handler for this project which will be used by the socket
 */
public class ReadHandler implements CompletionHandler<Integer, Attachment> {
    private PackageHandler mPackageHandler;
    private EventManager mEventManager;

    public ReadHandler(PackageHandler packageHandler, EventManager eventManager) {
        mPackageHandler = packageHandler;
        mEventManager = eventManager;
    }

    @Override
    public void completed(Integer result, Attachment attachment) {
        if (result == -1) {
            mEventManager.triggerEvent("disconnect", null);
            return;
        }

        mPackageHandler.addPackage(attachment.byteBuffer);
        while (mPackageHandler.hasPackage()) {
            String message = mPackageHandler.getPackage();
            System.out.println(message);
            SocketUtils.dispatchMessage(mEventManager, message);
        }

        SocketUtils.readMessage(attachment.socketChannel, this);
    }

    @Override
    public void failed(Throwable throwable, Attachment attachment) {

    }
}
