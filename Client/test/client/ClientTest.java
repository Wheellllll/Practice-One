package client;

import org.junit.Test;

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

    }

    @Test
    public void testOnRegister() throws Exception {

    }

    @Test
    public void testOnSend() throws Exception {

    }

    @Test
    public void testOnForward() throws Exception {

    }

    @Test
    public void testOnError() throws Exception {

    }

    @Test
    public void testOnDisconnect() throws Exception {
        System.out.println("Disconnect");
    }
}