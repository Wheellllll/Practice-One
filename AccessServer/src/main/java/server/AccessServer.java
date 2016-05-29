package server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import wheellllll.utils.chatrmi.IForward;
import wheellllll.utils.chatrmi.Network;
import wheellllll.utils.chatrmi.RMIManager;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by sweet on 5/27/16.
 */
public class AccessServer {
    public static void main(String args[]) {
        try {
            RMIManager rmiManager = new RMIManager();
            Thread.sleep(10000);
            IForward forward = (IForward)rmiManager.getServer(IForward.class);
            forward.forward("127.0.0.1", 1000, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
