package handler;

import event.EventManager;
import utils.SocketUtils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by sweet on 3/24/16.
 */
public class ReadHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {
    private ByteBuffer buf;
    private PackageHandler mPackageHandler;
    private EventManager mEventManager;

    public ReadHandler(PackageHandler packageHandler, EventManager eventManager) {
        mPackageHandler = packageHandler;
        mEventManager = eventManager;
    }

    public void setBuffer(ByteBuffer buffer) {
        buf = buffer;
    }

    @Override
    public void completed(Integer result, AsynchronousSocketChannel socketChannel) {
        if (result == -1) {
            mEventManager.triggerEvent("disconnect", null);
            return;
        }

        mPackageHandler.addPackage(buf);
        while (mPackageHandler.hasPackage()) {
            String message = mPackageHandler.getPackage();
            System.out.println(message);
            SocketUtils.dispatchMessage(mEventManager, message);
        }

        SocketUtils.readMessage(socketChannel, this);
    }

    @Override
    public void failed(Throwable throwable, AsynchronousSocketChannel socketChannel) {

    }
}
