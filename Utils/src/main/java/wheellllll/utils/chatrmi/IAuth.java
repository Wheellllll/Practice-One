package wheellllll.utils.chatrmi;

import java.util.HashMap;

/**
 * Created by sweet on 5/27/16.
 */
public interface IAuth {
    public static int RMI_ID = Network.AUTH;
    public HashMap<String, String> login(HashMap<String, String> args);
    public HashMap<String, String> register(HashMap<String, String> args);
}
