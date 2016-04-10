package client;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by Kris Chan on 9:52 AM 3/27/16 .
 * All right reserved.
 */
public class ClientTest {

    /**
     * 测试主页面
     * @throws Exception
     */
    @Test
    public void testMain() throws Exception {
        Client testClient = new Client();
        assertNotNull(testClient);
    }

    @Test
    public void testOnConnect() throws Exception {
        System.out.println("Connected");
    }

    @Test
    public void testOnLogin() throws Exception {
        System.out.println("Logined");
    }

    @Test
    public void testOnRelogin() throws Exception {
        Client client = new Client();
        HashMap<String,String> msg = new HashMap<>();
        client.OnRelogin(msg);
        boolean tmp = msg.get("result").equals("success");
        assertEquals(tmp,true);
    }

    @Test
    public void testOnRegister() throws Exception {
        Client client = new Client();
        HashMap<String,String> msg = new HashMap<>();
        client.OnRegister(msg);
        boolean tmp = msg.get("result").equals("success");
        assertEquals(tmp,true);
    }

    @Test
    public void testOnSend() throws Exception {
        Client client = new Client();
        HashMap<String,String> msg = new HashMap<>();
        client.OnSend(msg);
        boolean tmp = msg.get("result").equals("success");
        assertEquals(tmp,true);
    }

    @Test
    public void testOnForward() throws Exception {
        Client client = new Client();
        HashMap<String,String> msg = new HashMap<>();
        client.OnForward(msg);
        boolean tmp = msg.get("result").equals("success");
        assertEquals(tmp,true);
    }

    @Test
    public void testOnError() throws Exception {

    }

    @Test
    public void testOnDisconnect() throws Exception {
        System.out.println("Disconnect");
    }
}