package client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import octoteam.tahiti.config.ConfigManager;
import octoteam.tahiti.config.loader.JsonAdapter;
import org.slf4j.LoggerFactory;
import ui.ChatRoomForm;
import ui.ConfigDialog;
import ui.LoginAndRegisterForm;
import wheellllll.event.EventListener;
import wheellllll.event.EventManager;
import wheellllll.performance.ArchiveManager;
import wheellllll.performance.IntervalLogger;
import wheellllll.performance.RealtimeLogger;
import wheellllll.socket.SocketUtils;
import wheellllll.socket.handler.PackageHandler;
import wheellllll.socket.handler.ReadHandler;
import wheellllll.socket.model.AsynchronousSocketChannelWrapper;
import wheellllll.utils.MessageBuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.UnresolvedAddressException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import wheellllll.utils.chatrmi.Network;

/**
 * Base class for client
 */
public abstract class BaseClient {
    private LoginAndRegisterForm mLoginAndRegisterForm = null;
    private ChatRoomForm mChatRoomForm = null;
    private AsynchronousSocketChannel mSocketChannel = null;
    private AsynchronousSocketChannelWrapper mSocketWrapper = null;

    private PackageHandler mPackageHandler = new PackageHandler();
    private EventManager mEventManager = new EventManager();

    protected IntervalLogger intervalLogger = new IntervalLogger();
    protected RealtimeLogger realtimeLogger = new RealtimeLogger();
    protected ArchiveManager archiveManager = new ArchiveManager();
    protected ArchiveManager aarchiveManager = new ArchiveManager();

    protected  Logger logger = null;

    private String username = null;
    private String password = null;
    private int groupId = 0;
    private int udpPort = 0;

    protected static boolean DEBUG = false;

    public static void DEBUG_MODE(boolean flag) {
        DEBUG = flag;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setUdpPort(int port) {
        this.udpPort = port;
    }

    public int getUdpPort() {
        return this.udpPort;
    }

    public LoginAndRegisterForm getLoginAndRegisterForm() {
        return mLoginAndRegisterForm;
    }

    public ChatRoomForm getChatRoomForm() {
        return mChatRoomForm;
    }

    public void sendMessage(String message) {
        SocketUtils.sendMessage(mSocketWrapper, message, null);
    }

    protected void initPerformance() {
        //初始化intervalLogger
        intervalLogger.setLogDir("./clientlog");
        intervalLogger.setLogPrefix("client");
        intervalLogger.setLogSuffix("log");
        intervalLogger.setDateFormat("yyyy-MM-dd HH_mm");
        intervalLogger.setInitialDelay(1);
        intervalLogger.setInterval(1, TimeUnit.MINUTES);

        intervalLogger.addIndex("Login successfully number");
        intervalLogger.addIndex("Login failed number");
        intervalLogger.addIndex("Send message number");
        intervalLogger.addIndex("Receive message number");
        intervalLogger.setFormatPattern(
                "Login successfully number : ${Login successfully number}\n" +
                        "Login failed number : ${Login failed number}\n" +
                        "Send message number : ${Send message number}\n" +
                        "Receive message number : ${Receive message number}\n\n");
        intervalLogger.start();

        //初始化realtimeLogger
        realtimeLogger.setLogDir("./clientlog");
        realtimeLogger.setLogPrefix("client");
        realtimeLogger.setLogSuffix("mlog");
        realtimeLogger.setFormatPattern(
                "Username : ${username}\n" +
                        "Time : ${time}\n" +
                        "Message : ${message}\n\n");

        //初始化archiveManager
        archiveManager.setArchiveDir("./clientarchive");
        archiveManager.setDatePattern("yyyy-MM-dd");
        archiveManager.addLogger(intervalLogger);
        archiveManager.addLogger(realtimeLogger);
        archiveManager.setInitialDelay(1);
        archiveManager.setInterval(1, TimeUnit.DAYS);
        archiveManager.start();

        //初始化archiveManager
        aarchiveManager.setArchiveDir("./clientaarchive");
        aarchiveManager.setDatePattern("yyyy-MM-dd");
        aarchiveManager.addFolder("./clientarchive");
        aarchiveManager.setInterval(7, TimeUnit.DAYS);
        aarchiveManager.setInitialDelay(1);
        aarchiveManager.start();

    }

    protected void initUDPSocket() {
        try {
            Server udpServer = new Server();
            Network.register(udpServer);
            udpServer.addListener(new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    HashMap<String, String> args = (HashMap)object;
                    SocketUtils.dispatchMessage(mEventManager, args);
                }
            });
            udpServer.bind(getUdpPort());
            udpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BaseClient() {
        try {
            initEvent();
            initPerformance();
            logger = LoggerFactory.getLogger("clientlogback");
            if (!DEBUG) initWelcomeUI();
            tryConnect();

            if (!DEBUG) Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.out.format("Stop to connect to server");
        }
    }

    protected void initWelcomeUI() {
        //开启登陆界面
        mLoginAndRegisterForm = new LoginAndRegisterForm();
        mLoginAndRegisterForm.setOnLoginListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String user = mLoginAndRegisterForm.getUsername();
                String pass = mLoginAndRegisterForm.getPassword();

                String msgToSend = new MessageBuilder()
                        .add("event", "login")
                        .add("username", user)
                        .add("password", pass)
                        .buildString();
                username = user;
                password = pass;

                sendMessage(msgToSend);
            }
        });

        mLoginAndRegisterForm.setOnRegisterListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String user = mLoginAndRegisterForm.getUsername();
                String pass = mLoginAndRegisterForm.getPassword();

                String msgToSend = new MessageBuilder()
                        .add("event", "reg")
                        .add("username", user)
                        .add("password", pass)
                        .buildString();
                username = user;
                password = pass;

                sendMessage(msgToSend);
            }
        });
        mLoginAndRegisterForm.setOnConfigListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new ConfigDialog(mLoginAndRegisterForm.getFrame());
                tryConnect();
            }
        });
    }

    protected void initChatRoomUI() {
        //开启聊天室界面
        mChatRoomForm = new ChatRoomForm();
        mChatRoomForm.updateGroupId(getGroupId());
        mChatRoomForm.setOnSendListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String msgToSend = mChatRoomForm.getSendMessage();
                msgToSend = new MessageBuilder()
                        .add("event", "send")
                        .add("message", msgToSend)
                        .buildString();
                sendMessage(msgToSend);
                mChatRoomForm.clearChatArea();
            }
        });
        mChatRoomForm.setOnWindowClosingListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String msgToSend = new MessageBuilder()
                        .add("event", "disconnect")
                        .buildString();
                sendMessage(msgToSend);
            }
        });
    }

    private void initEvent() {
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
        }).addEventListener("group", new EventListener() {
            @Override
            public void run(HashMap<String, String> args) {
                OnGroup(args);
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

    private void tryConnect() {
        try {
            ConfigManager configManager = new ConfigManager(new JsonAdapter(), "./ClientConfig.json");
            ConfigBean configBean = configManager.loadToBean(ConfigBean.class);
            String host = configBean.getHost();
            int port = configBean.getPort();
            SocketAddress serverAddress = new InetSocketAddress(host, port);
            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
            socketChannel.connect(serverAddress, socketChannel, new ConnectionHandler());
        } catch (IOException e) {
            if (!DEBUG) mLoginAndRegisterForm.setError("连接服务器失败");
        } catch (UnresolvedAddressException e) {
            if (!DEBUG) mLoginAndRegisterForm.setError("连接服务器失败");
        }
    }

    class ConnectionHandler implements
            CompletionHandler<Void, AsynchronousSocketChannel> {

        public void completed(Void result, AsynchronousSocketChannel socketChannel) {
            mSocketChannel = socketChannel;
            mSocketWrapper = new AsynchronousSocketChannelWrapper(socketChannel);
            if (!DEBUG) mLoginAndRegisterForm.setCorrect("成功连接服务器");
            OnConnect(null);

            /*
             * 开始读消息
             */
            SocketUtils.readMessage(mSocketChannel, new ReadHandler(mPackageHandler, mEventManager));
        }

        public void failed(Throwable e, AsynchronousSocketChannel asynchronousSocketChannel) {
            if (!DEBUG) mLoginAndRegisterForm.setError("连接服务器失败");
        }
    }

    public abstract void OnConnect(HashMap<String, String> args);
    public abstract void OnRegister(HashMap<String, String> args);
    public abstract void OnLogin(HashMap<String, String> args);
    public abstract void OnRelogin(HashMap<String, String> args);
    public abstract void OnSend(HashMap<String, String> args);
    public abstract void OnForward(HashMap<String, String> args);
    public abstract void OnGroup(HashMap<String, String> args);
    public abstract void OnDisconnect(HashMap<String, String> args);
    public abstract void OnError(HashMap<String, String> args);

}
