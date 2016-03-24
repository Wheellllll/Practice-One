import com.alibaba.fastjson.JSON;
import ui.ChatRoomForm;
import utils.Config;
import ui.ConfigDialog;
import ui.LoginAndRegisterForm;
import utils.MessageBuilder;
import handler.PackageHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.UnresolvedAddressException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by sweet on 3/16/16.
 */
public class Client extends BaseClient {
    public static void main(String[] args) {
        new Client();
    }

    /*
     * 事件定义
     */
    @Override
    public void OnConnect(HashMap<String, String> args) {
        System.out.println("Connected");
    }

    @Override
    public void OnLogin(HashMap<String,String> msg) {

        if (msg.get("result").equals("success")) {
            incLoginSuccessNum();
            getLoginAndRegisterForm().close();
            initChatRoomUI();
        } else {
            /*
             * 登陆失败，更新UI
             */
            getLoginAndRegisterForm().setError(msg.get("reason"));
            incLoginFailNum();
        }
    }

    @Override
    public void OnRelogin(HashMap<String, String> msg) {
        if (msg.get("result").equals("success")) {
            incLoginSuccessNum();
        } else {
            /*
             * 登陆失败，更新UI
             */
            incLoginFailNum();
            String msgToSend = new MessageBuilder()
                    .add("event", "relogin")
                    .add("username", getUsername())
                    .add("password", getPassword())
                    .build();
            sendMessage(msgToSend);
            //getLoginAndRegisterForm().setError(msg.get("reason"));
        }
    }

    @Override
    public void OnRegister(HashMap<String,String> msg) {

        if (msg.get("result").equals("success")) {
            getLoginAndRegisterForm().close();
            initChatRoomUI();
        } else {
            /*
             * 注册失败，更新UI
             */
            getLoginAndRegisterForm().setError(msg.get("reason"));
        }
    }

    @Override
    public void OnSend(HashMap<String, String> msg) {
        if (msg.get("result").equals("success")) {
            //TODO:成功，记录一下
        } else if (msg.get("reason").equals("relogin")) {
            String msgToSend = new MessageBuilder()
                    .add("event", "relogin")
                    .add("username", getUsername())
                    .add("password", getPassword())
                    .build();
            sendMessage(msgToSend);
        } else {
            //TODO:失败，为什么失败
        }
    }

    @Override
    public void OnForward(HashMap<String,String> msg) {
        String from = msg.get("from");
        String message = msg.get("message");

        incReceiveMsgNum();
        getChatRoomForm().addMessage(from, message);
        //TODO: 通知服务器
    }

    @Override
    public void OnError(HashMap<String, String> msg) {

    }

    @Override
    public void OnDisconnect(HashMap<String, String> args) {
        System.out.println("Disconnect");
    }


}
