package server;

import org.apache.log4j.Level;

/**
 * Server inherited from BaseServer. You can launch a server by instance this class.
 */
public class Server extends BaseServer {
    public static void main(String[] args) {
        logger.setLevel(Level.INFO);
        new Server();
    }
}
