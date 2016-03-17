import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by sweet on 3/16/16.
 */
public class ChatServer {

    private ServerSocket mServerSocket = null;

    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 8888;

    public void runServer() {
        try {
            mServerSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Starting Server...");

            while (true) {
                Socket socket = mServerSocket.accept();
                new SocketHandler(socket);
            }
        } catch (BindException e) {
            System.out.println("Port is already using...");
        } catch (IOException e) {
            System.out.println("Could not start server. " + e.getMessage());
        }
    }

}
