package wheellllll.socket.model;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class provide a wrapper for <code>AsynchronousSocketChannel</code>
 */
public class AsynchronousSocketChannelWrapper {
    private final AsynchronousSocketChannel asynchronousSocketChannel;
    private final Handler handler;
    private final ReentrantLock lock;
    private final Queue<ByteBuffer> queue;
    private final ByteBuffer[] iov;
    private boolean closed;

    private class Handler implements CompletionHandler<Long, Integer>
    {
        public void completed( Long result, Integer attachment )
        {
            /* Called when the write operation completed. */
            int iovc = 0;
            lock.lock();
            try {
                /* Remove all sent buffers from the queue. */
                int idx = 0;
                for (;;) {
                    final ByteBuffer byteBuffer = queue.peek();
                    assert( byteBuffer == iov[idx] );
                    if (byteBuffer.remaining() > 0) {
                        /* Nobody knows will it happen or not,
                         * let's assume will not.
                         */
                        assert( false );
                    }

                    iov[idx] = null;
                    queue.poll();

                    if ((++idx == iov.length) || (iov[idx] == null)) {
                        break;
                    }
                }

                if (queue.isEmpty())
                    return;

                /* Queue is not empty, let's schedule new write requests.
                 * Would be stupid to schedule them one by one if more than one,
                 * let's join them though by 16.
                 */
                final Iterator<ByteBuffer> it = queue.iterator();
                do {
                    iov[iovc] = it.next();
                    if (++iovc == iov.length)
                        break;
                }
                while (it.hasNext());
            }
            finally {
                lock.unlock();
            }

            assert( iovc > 0 );
            asynchronousSocketChannel.write(
                    iov, 0, iovc, 0, TimeUnit.SECONDS, null, this );
        }

        public void failed( Throwable exc, Integer attachment )
        {
            /* Called when the write operation failed,
             * most probably the underlying socket is being closed.
             */
            lock.lock();
            try {
                closed = true;
                queue.clear();
            }
            finally {
                lock.unlock();
            }
        }
    }

    public AsynchronousSocketChannelWrapper(
            AsynchronousSocketChannel asynchronousSocketChannel )
    {
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.handler = new Handler();
        this.lock = new ReentrantLock();
        this.queue = new LinkedList<ByteBuffer>();
        this.iov = new ByteBuffer[16];
    }

    /**
     * This method write a <code>ByteBuffer</code> to socket
     * @param byteBuffer ByteBuffer to write
     * @return True if write successful or False if socket is closed
     */
    public boolean write( ByteBuffer byteBuffer )
    {
        lock.lock();
        try {
            if (closed)
                return false;

            final boolean wasEmpty = queue.isEmpty();
            queue.add( byteBuffer );

            if (!wasEmpty)
                return true;
        }
        finally {
            lock.unlock();
        }

        iov[0] = byteBuffer;
        asynchronousSocketChannel.write(
                iov, 0, 1, 0, TimeUnit.SECONDS, null, handler );

        return true;
    }
}
