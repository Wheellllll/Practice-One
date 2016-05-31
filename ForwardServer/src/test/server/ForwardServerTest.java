package server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import org.junit.Test;
import wheellllll.socket.SocketUtils;
import wheellllll.utils.chatrmi.IForward;
import wheellllll.utils.chatrmi.Network;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by Kris Chan on 3:18 PM 5/31/16 .
 * All right reserved.
 */
public class ForwardServerTest {

    @Test
    public void testInitServer() throws Exception {
        Server server = new Server();
        Network.register(server);
        int openPort = SocketUtils.getAvailablePort();
        server.bind(openPort);
        assertNotNull(server);
        server.start();
    }

    @Test
    public void testConnectRegisterCenter() throws Exception {
        int openPort = SocketUtils.getAvailablePort();
        Client client = new Client();
        client.start();
        Network.register(client);
        client.connect(3000, Network.REGISTER_CENTER_HOST, Network.REGISTER_CENTER_PORT);
        HashMap<String, Object> msg = new HashMap<>();
        msg.put("class", IForward.class);
        msg.put("port", openPort);
        client.sendTCP(msg);
        assertNotNull(client);
        client.close();
    }

    @Test
    public void testMain() throws Exception {
    }
}