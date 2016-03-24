import event.EventListener;
import event.EventManager;
import handler.PackageHandler;
import handler.ReadHandler;
import utils.*;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sweet on 3/24/16.
 */
public abstract class BaseClient {
    private static ArrayList<BaseClient> clients = new ArrayList<BaseClient>();
    private EventManager mEventManager = new EventManager();
    /*
     * mSocketChannel 绑定的socket
     * mUsername 用户名
     * mPassword 密码
     * mStatus 当前状态->Settings.Status
     * mMsgPerSecond最近1秒发送的消息数
     * mMsgSinceLogin自从登陆起发送的消息数
     * mLastSendTime上次发送的时间戳
     *
     */
    private AsynchronousSocketChannel mSocketChannel = null;
    private PackageHandler mPackageHandler = new PackageHandler();

    private String mUsername = null;
    private String mPassword = null;
    private Status mStatus = Status.LOGOUT;
    private int mMsgPerSecond = 0;
    private int mMsgSinceLogin = 0;
    private long mLastSendTime = 0;

    private int localValidLogin = 0;
    private int localInvalidLogin = 0;
    private int localReceiveMsgNum = 0;
    private int localIgnoreMsgNum = 0;
    private int localForwardMsgNum = 0;

    public AsynchronousSocketChannel getSocketChannel() {
        return mSocketChannel;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public Status getStatus() {
        return mStatus;
    }

    public int getMsgPerSecond() {
        return mMsgPerSecond;
    }

    public int getMsgSinceLogin() {
        return mMsgSinceLogin;
    }

    public long getLastSendTime() {
        return mLastSendTime;
    }

    public int getLocalValidLogin() {
        return localValidLogin;
    }

    public int getLocalInvalidLogin() {
        return localInvalidLogin;
    }

    public int getLocalReceiveMsgNum() {
        return localReceiveMsgNum;
    }

    public int getLocalIgnoreMsgNum() {
        return localIgnoreMsgNum;
    }

    public int getLocalForwardMsgNum() {
        return localForwardMsgNum;
    }

    public static ArrayList<BaseClient> getClients() {
        return clients;
    }

    public void incLocalValidLogin() {
        localValidLogin++;
    }

    public void incLocalInvalidLogin() {
        localInvalidLogin++;
    }

    public void incLocalReceiveMsgNum() {
        localReceiveMsgNum++;
    }

    public void incLocalIgnoreMsgNum() {
        localIgnoreMsgNum++;
    }

    public void incLocalForwardMsgNum() {
        localForwardMsgNum++;
    }

    public void incMsgPerSecond() {
        mMsgPerSecond++;
    }

    public void incMsgSinceLogin() {
        mMsgSinceLogin++;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public void setStatus(Status mStatus) {
        this.mStatus = mStatus;
    }

    public void setMsgPerSecond(int mMsgPerSecond) {
        this.mMsgPerSecond = mMsgPerSecond;
    }

    public void setMsgSinceLogin(int mMsgSinceLogin) {
        this.mMsgSinceLogin = mMsgSinceLogin;
    }

    public void setLastSendTime(long mLastSendTime) {
        this.mLastSendTime = mLastSendTime;
    }

    public void sendMessage(String message) {
        SocketUtils.sendMessage(mSocketChannel, message, null);
    }


    public BaseClient(AsynchronousSocketChannel socketChannel) {
        initialEvent();
        this.mSocketChannel = socketChannel;
        /*
         * 触发OnConnect事件
         */
        OnConnect(null);

        /*
         * 开始读消息
         */
        SocketUtils.readMessage(mSocketChannel, new ReadHandler(mPackageHandler, mEventManager));
    }


    private void initialEvent() {
        mEventManager.addEventListener("connect", new EventListener() {
            @Override
            public void run(HashMap<String, String> args) {
                OnConnect(args);
            }
        }).addEventListener("reg", new EventListener() {
            @Override
            public void run(HashMap<String, String> args) {
                OnRegister(args);
            }
        }).addEventListener("login", new EventListener() {
            @Override
            public void run(HashMap<String, String> args) {
                OnLogin(args);
            }
        }).addEventListener("relogin", new EventListener() {
            @Override
            public void run(HashMap<String, String> args) {
                OnRelogin(args);
            }
        }).addEventListener("send", new EventListener() {
            @Override
            public void run(HashMap<String, String> args) {
                OnSend(args);
            }
        }).addEventListener("disconnect", new EventListener() {
            @Override
            public void run(HashMap<String, String> args) {
                OnDisconnect(args);
            }
        }).addEventListener("error", new EventListener() {
            @Override
            public void run(HashMap<String, String> args) {
                OnError(args);
            }
        });
    }

    public abstract void OnConnect(HashMap<String, String> args);
    public abstract void OnLogin(HashMap<String, String> args);
    public abstract void OnRegister(HashMap<String, String> args);
    public abstract void OnRelogin(HashMap<String, String> args);
    public abstract void OnSend(HashMap<String, String> args);
    public abstract void OnDisconnect(HashMap<String, String> args);
    public abstract void OnError(HashMap<String, String> args);

}
