package server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import wheellllll.utils.chatrmi.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by sweet on 5/27/16.
 */
public class AccessServer {
    public static void main(String args[]) {
        try {
            RMIManager rmiManager = new RMIManager();
            while (true) {
                Thread.sleep(1000);
                IAuth auth = (IAuth) rmiManager.getServer(IAuth.class);
                if (auth != null) {
                    HashMap<String, String> msg = new HashMap<>();
                    auth.login(msg);
                    System.out.println("Done");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
