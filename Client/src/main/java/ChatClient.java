import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by sweet on 3/17/16.
 */
public class ChatClient {
    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 8888;

    private Socket mSocket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;

    public void runClient() {
        Scanner s = new Scanner(System.in);

        connectServer();
        new ReceivedMessageHandler(in);

        while (true) {
            String line = s.nextLine();
            out.println(line);
        }
    }

    private void connectServer() {
        try {
            mSocket = new Socket(SERVER_IP, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream())), true);
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
