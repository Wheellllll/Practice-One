package model;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by sweet on 3/17/16.
 */
public class SocketWithUser {
    /*
     * socketChannel 绑定的socket
     * username 用户名
     * password 密码
     * status 当前状态->Settings.Status
     * msgPerSecond最近1秒发送的消息数
     * msgSinceLogin自从登陆起发送的消息数
     * lastSendTime上次发送的时间戳
     *
     */

    public AsynchronousSocketChannel socketChannel;
    public String username;
    public String password;
    public int status;
    public int msgPerSecond;
    public int msgSinceLogin;
    public int lastSendTime;

    public void send(String message) {
        /*
         * 发送消息
         */

    }
}
