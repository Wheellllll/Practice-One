/**
 * Created by sweet on 3/16/16.
 */
public class Main {

    private static NIOServer server = new NIOServer();

    public static void main(String[] args) {
        try {
            server.runServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
