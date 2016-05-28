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
    static public final String FORWARD_HOST = "127.0.0.1";
    static public final short FORWARD_PORT = 12450;
    static public final short DATABASE = 2;
    static public final String DATABASE_HOST = "127.0.0.1";
    static public final short DATABASE_PORT = 12460;
    static public final short AUTH = 3;
    static public final String AUTH_HOST = "127.0.0.1";
    static public final short AUTH_PORT = 12470;

    // This registers objects that are going to be sent over the network.
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        ObjectSpace.registerClasses(kryo);
        /*
         * Register interface
         */
        kryo.register(IForward.class);
        kryo.register(IChatDatabase.class);
        /*
         * Register model
         */
        kryo.register(HashMap.class);
        kryo.register(BasicDBObject.class);
        kryo.register(BasicDBList.class);
        kryo.register(ObjectId.class);
    }
}
