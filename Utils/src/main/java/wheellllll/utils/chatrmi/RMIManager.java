package wheellllll.utils.chatrmi;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by sweet on 5/29/16.
 */
public class RMIManager {
    private HashMap<Class, List<Connection>> servers;
    private HashMap<Connection, Class> conToClass;

    public RMIManager() {
        servers = new HashMap<>();
        conToClass = new HashMap<>();
        initManager();
    }

    private void initManager() {
        try {
            Server server = new Server();
            Network.register(server);
            server.addListener(new Listener() {
                @Override
                public void received(Connection c, Object o) {
                    if (o instanceof HashMap) {
                        try {
                            HashMap args = (HashMap) o;
                            Class key = (Class) args.get("class");
                            int port = (int)args.get("port");

                            Client client = new Client();
                            client.start();

                            Network.register(client);

                            client.addListener(new Listener() {
                                @Override
                                public void connected(Connection con) {
                                    conToClass.put(client, key);
                                    if (!servers.containsKey(key)) {
                                        servers.put(key, new ArrayList<>());
                                    }

                                    List<Connection> connections = servers.get(key);
                                    connections.add(client);
                                }

                                @Override
                                public void disconnected(Connection con) {
                                    Class cla = conToClass.get(con);
                                    conToClass.remove(cla);
                                    servers.get(cla).remove(con);
                                }
                            });

                            client.connect(3000, c.getRemoteAddressTCP().getAddress().getHostAddress(), port);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
            server.bind(45012);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
