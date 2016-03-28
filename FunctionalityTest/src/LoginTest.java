import utils.MessageBuilder;

/**
 * Created by summer on 3/27/16.
 */
public class LoginTest {
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

       


   }

}
