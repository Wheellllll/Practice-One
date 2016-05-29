package wheellllll.utils.chatrmi;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

import java.util.HashMap;

/**
 * Created by sweet on 5/27/16.
 */
public class Network {
    static public final short FORWARD = 1;
    static public final short DATABASE = 2;
    static public final short AUTH = 3;
    static public final String REGISTER_CENTER_HOST = "127.0.0.1";
    static public final int REGISTER_CENTER_PORT = 45012;

    // This registers objects that are going to be sent over the network.
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        ObjectSpace.registerClasses(kryo);
        /*
         * Register interface
         */
        kryo.register(IForward.class);
        kryo.register(IChatDatabase.class);
        kryo.register(IAuth.class);
        /*
         * Register model
         */
        kryo.register(Object.class);
        kryo.register(Class.class);
        kryo.register(HashMap.class);
        kryo.register(BasicDBObject.class);
        kryo.register(BasicDBList.class);
        kryo.register(ObjectId.class);
    }
}
