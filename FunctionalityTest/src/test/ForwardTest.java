package test;

import client.Client;
import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
import server.Server;

import java.util.HashMap;

/**
 * Created by summer on 3/29/16.
 */
public class ForwardTest {
    public static void main(String args[])
    {
        Server.DEBUG_MODE(true);
        Client.DEBUG_MODE(true);
        Server server = new Server();
        Client client = new Client() {


            @Override
            public void OnLogin(HashMap<String, String> args) {
                assert("login" == args.get("event"));
                assert("fail" == args.get("result"));
            }

            @Override
            public void OnForward(HashMap<String,String> args) {
                assert ("forward" == args.get("event"));
                assert ("success" == args.get("ack"));

            }

        };
    }
}
