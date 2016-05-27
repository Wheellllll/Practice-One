package wheellllll.utils.chatrmi;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import java.util.HashMap;

/**
 * Created by sweet on 5/27/16.
 */
public class Network {
    static public final short FORWARD = 1;

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        ObjectSpace.registerClasses(kryo);
        /*
         * Register interface
         */
        kryo.register(IForward.class);
        /*
         * Register model
         */
        kryo.register(HashMap.class);
    }
}
