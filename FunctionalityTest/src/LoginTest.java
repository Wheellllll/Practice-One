<<<<<<< HEAD
import utils.MessageBuilder;

=======
import client.Client;
import server.Server;
import utils.MessageBuilder;

import java.util.HashMap;

>>>>>>> cc087e49055183c57a842ab45cb3530938451d1a
/**
 * Created by summer on 3/27/16.
 */
public class LoginTest {
<<<<<<< HEAD
   public static void Main(String args[])
   {
       Server server = new Server();
       Client client = new Client();

       //注册测试账号
       MessageBuilder msgBuilder = new MessageBuilder();
       msgBuilder.add("event","reg");
       msgBuilder.add("username","funcTest");
       msgBuilder.add("password","123456");
       String msg = msgBuilder.build();
       client.sendMessage(msg);

       //登录
       MessageBuilder loginMsgBuilder = new MessageBuilder();
       loginMsgBuilder.add("event","login");
       loginMsgBuilder.add("username","funcTest");
       loginMsgBuilder.add("password","123456");
       String loginMsg = loginMsgBuilder.build();
       client.sendMessage(loginMsg);

       

=======
    public static void main(String args[])
    {
        Server.DEBUG_MODE(true);
        Client.DEBUG_MODE(true);
        Server server = new Server();
        Client client = new Client() {
            @Override
            public void OnConnect(HashMap<String, String> args) {
                System.out.println("HHH");

            }

            @Override
            public void OnLogin(HashMap<String, String> args) {
                assert("login" == args.get("event"));
                assert("fail" == args.get("result"));
            }
        };

        String msgToSend = new MessageBuilder()
                .add("event", "login")
                .add("username", "test1")
                .add("password", "test")
                .build();
        client.sendMessage(msgToSend);
>>>>>>> cc087e49055183c57a842ab45cb3530938451d1a

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
