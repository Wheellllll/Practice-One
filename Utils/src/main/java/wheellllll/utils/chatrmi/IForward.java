package wheellllll.utils.chatrmi;

import java.util.HashMap;

/**
 * Created by sweet on 5/27/16.
 */
public interface IForward {
    public static int RMI_ID = Network.FORWARD;
    public boolean forward(String host, int port, HashMap<String, String> args);
}
