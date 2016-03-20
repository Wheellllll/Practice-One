package authentication;

import model.SocketWithUser;

import java.nio.channels.AsynchronousSocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by sweet on 3/17/16.
 */
public class Authenticator {
    /*
     * TODO: 管理socket连接池与账户
     */

    private ArrayList<SocketWithUser> socketPools;

    public Authenticator() {
        socketPools = new ArrayList<SocketWithUser>();
    }

    public boolean addSocket(AsynchronousSocketChannel socketChannel) {
        SocketWithUser su = new SocketWithUser();
        su.socketChannel = socketChannel;
        su.status = Status.IGNORE;
        socketPools.add(su);


        return true;
    }

    public boolean deleteSocket(AsynchronousSocketChannel socketChannel) {

        for (SocketWithUser su : socketPools) {
            if (su.socketChannel == socketChannel) {
                return socketPools.remove(su);
            }
        }

        return false;
    }

    public boolean addUser() {
        /*
         * TODO: 在数据库中添加用户
         */

        return true;
    }

    public boolean register(String username, String password) {
        /*
         * TODO: 在数据库中注册用户,password要使用md5加密
         */
        String encryptedPass = md5Hash(password);

        return true;
    }


    /*public void updateMsg(String username, int timeStamp) {
        for (SocketWithUser su : socketPools) {
            if (su.username.equals(username)) {
                su.lastSendTime = timeStamp;
                return;
            }
        }
    }*/

    public boolean login(AsynchronousSocketChannel socketChannel, String username, String password) {
        for (SocketWithUser su : socketPools) {
            if (su.socketChannel == socketChannel) {
                su.username = username;
                su.password = password;
                su.status = Status.LOGIN;
                return true;
            }
        }
        return false;
    }

    public void handleSend(AsynchronousSocketChannel socketChannel) {

    }


    /*
    * TODO: 暂时把需要的方法复制到此
    */
    /*
     * socket状态
     * LOGOUT--已登出
     * LOGIN--已登陆
     * RELOGIN--已发送100条消息，需重新登陆
     * IGNORE--超过5条/秒的限制，忽略
     *
     */
    public static enum Status {
        LOGIN, LOGOUT, RELOGIN, IGNORE
    }

    public static String md5Hash(String plainText) {
        String str = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            byte[] bytes = md5.digest(plainText.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(Integer.toHexString(bt));
            }
            str = stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str;
    }

}
