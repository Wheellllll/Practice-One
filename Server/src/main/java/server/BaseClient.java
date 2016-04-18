package server;

import octoteam.tahiti.config.ConfigManager;
import octoteam.tahiti.config.loader.JsonAdapter;
import octoteam.tahiti.performance.recorder.CountingRecorder;
import octoteam.tahiti.quota.CapacityLimiter;
import octoteam.tahiti.quota.ThroughputLimiter;
import wheellllll.event.EventListener;
import wheellllll.event.EventManager;
import wheellllll.socket.SocketUtils;
import wheellllll.socket.handler.PackageHandler;
import wheellllll.socket.handler.ReadHandler;
import wheellllll.socket.model.AsynchronousSocketChannelWrapper;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Base client which implement the dirty works
 */
public abstract class BaseClient {
    private static ArrayList<BaseClient> clients = new ArrayList<BaseClient>();
    private EventManager mEventManager = new EventManager();
    /*
     * mSocketChannel 绑定的socket
     * mUsername 用户名
     * mPassword 密码
     * mStatus 当前状态->Status
     * mMsgPerSecond最近1秒发送的消息数
     * mMsgSinceLogin自从登陆起发送的消息数
     * mLastSendTime上次发送的时间戳
     *
     */
    private AsynchronousSocketChannel mSocketChannel = null;
    private AsynchronousSocketChannelWrapper mSocketWrapper = null;
    private PackageHandler mPackageHandler = new PackageHandler();

    private String mUsername = null;
    private String mPassword = null;
    private Status mStatus = Status.LOGOUT;

    private BaseServer mServer = null;

    protected BaseServer getServer() {
        return mServer;
    }

    // QuotaLimiters
    private CapacityLimiter capacityLimiter;
    private ThroughputLimiter throughputLimiter;

    public CapacityLimiter getCapacityLimiter() {
        return capacityLimiter;
    }

    public void setCapacityLimiter(CapacityLimiter capacityLimiter) {
        this.capacityLimiter = capacityLimiter;
    }

    public ThroughputLimiter getThroughputLimiter() {
        return throughputLimiter;
    }

    public void setThroughputLimiter(ThroughputLimiter throughputLimiter) {
        this.throughputLimiter = throughputLimiter;
    }

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

    public static ArrayList<BaseClient> getClients() {
        return clients;
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

    public void sendMessage(String message) {
        SocketUtils.sendMessage(mSocketWrapper, message, null);
    }


    public BaseClient(AsynchronousSocketChannel socketChannel) {
        initialEvent();
        this.mSocketChannel = socketChannel;
        this.mSocketWrapper = new AsynchronousSocketChannelWrapper(socketChannel);
        ConfigManager configManager = new ConfigManager(new JsonAdapter(), "./ServerConfig.json");
        ConfigBean config = null;
        try {
            config = configManager.loadToBean(ConfigBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.capacityLimiter = new CapacityLimiter(config.getMAX_NUMBER_PER_SESSION());
        this.throughputLimiter = new ThroughputLimiter(config.getMAX_NUMBER_PER_SECOND());
        /*
         * 触发OnConnect事件
         */
        OnConnect(null);

        /*
         * 开始读消息
         */
        SocketUtils.readMessage(mSocketChannel, new ReadHandler(mPackageHandler, mEventManager));
    }

    public BaseClient(AsynchronousSocketChannel socketChannel, BaseServer server) {
        this(socketChannel);
        this.mServer = server;
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
        }).addEventListener("forward", new EventListener() {
            @Override
            public void run(HashMap<String, String> args) {
                OnForward(args);
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
    public abstract void OnForward(HashMap<String, String> args);
    public abstract void OnDisconnect(HashMap<String, String> args);
    public abstract void OnError(HashMap<String, String> args);

}
