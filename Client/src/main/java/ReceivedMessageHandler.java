import java.io.BufferedReader;

/**
 * Created by sweet on 3/17/16.
 */
public class ReceivedMessageHandler extends Thread {
        private BufferedReader in = null;

        public ReceivedMessageHandler(BufferedReader in) {
            this.in = in;
            this.start();
        }

        @Override
        public void start() {

        }

}
