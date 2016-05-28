package wheellllll.utils.chatrmi;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by sweet on 5/29/16.
 */
public class RMIManager {
    private HashMap<Class, List<Connection>> servers;

    public RMIManager() {
        RMIManager rmiManager = new RMIManager();
    }

    public Object getServer(Class C) {
        try {
            List<Connection> connections = servers.getOrDefault(C, null);
            if (connections != null && !connections.isEmpty()) {
                Random randomizer = new Random();
                Connection connection = connections.get(randomizer.nextInt(connections.size()));
                Field f = C.getField("RMI_ID");
                int RMI_ID = f.getInt(null);
                return ObjectSpace.getRemoteObject(connection, RMI_ID, C);
            } else {
                return null;
            }
        } catch (IllegalAccessException e) {
            return null;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

}
