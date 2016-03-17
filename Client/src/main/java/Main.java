/**
 * Created by sweet on 3/16/16.
 */
public class Main {

    public static NIOClient client = new NIOClient();

    public static void main(String[] args) {
        try {
            client.runClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
