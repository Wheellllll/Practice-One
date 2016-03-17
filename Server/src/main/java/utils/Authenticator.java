package utils;

import model.SocketWithUser;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;

/**
 * Created by sweet on 3/17/16.
 */
public class Authenticator {
    /*
     * TODO: 管理socket连接池与账户
     */

    private ArrayList<SocketWithUser> socketPools = new ArrayList<SocketWithUser>();

    public boolean addSocket() {
        /*
         * 添加一个Socket
         */

        return true;
    }

    public boolean addUser() {
        /*
         * 在数据库中添加用户
         */

        return true;
    }

    public boolean register(String username, String password) {
        /*
         * password要使用md5加密
         */

        return true;
    }

    public boolean login(AsynchronousSocketChannel channel, String username, String password) {
        /*
         * 为一个socket登陆
         */

        return true;
    }

}
