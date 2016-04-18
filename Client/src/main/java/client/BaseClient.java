package client;

import octoteam.tahiti.performance.PerformanceMonitor;
import octoteam.tahiti.performance.recorder.CountingRecorder;
import octoteam.tahiti.performance.reporter.LogReporter;
import octoteam.tahiti.performance.reporter.RollingFileReporter;
import ui.ChatRoomForm;
import ui.ConfigDialog;
import ui.LoginAndRegisterForm;
import wheellllll.socket.SocketUtils;
import wheellllll.socket.handler.PackageHandler;
import wheellllll.socket.handler.ReadHandler;
import wheellllll.socket.model.AsynchronousSocketChannelWrapper;
import wheellllll.utils.MessageBuilder;
import wheellllll.config.Config;
import wheellllll.event.EventListener;
import wheellllll.event.EventManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.UnresolvedAddressException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private ScheduledExecutorService sc = null;
    private int loginSuccessNum = 0;
    private int loginFailNum = 0;
    private int sendMsgNum = 0;
    private int receiveMsgNum = 0;
    private String username = null;
    private String password = null;
    private CountingRecorder loginSuccessRecorder;
    private CountingRecorder loginFailRecorder;
    private CountingRecorder sendMsgNumRecorder;
    private CountingRecorder receiveMsgRecorder;
    protected static boolean DEBUG = false;

    public static void DEBUG_MODE(boolean flag) {
        DEBUG = flag;
    }

    public int getLoginSuccessNum() {
        return loginSuccessNum;
    }

    public int getLoginFailNum() {
        return loginFailNum;
    }

    public int getSendMsgNum() {
        return sendMsgNum;
    }

    public int getReceiveMsgNum() {
        return receiveMsgNum;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void incLoginSuccessNum() {
        loginSuccessNum++;
    }

    public void incLoginFailNum() {
        loginFailNum++;
    }

    public void incSendMsgNum() {
        sendMsgNum++;
    }

    public void incReceiveMsgNum() {
        receiveMsgNum++;
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

    public BaseClient() {
        try {
            Config.setConfigName("client");
            initEvent();
            if (!DEBUG) initWelcomeUI();
            tryConnect();

            sc = Executors.newScheduledThreadPool(1);
            sc.scheduleAtFixedRate(new ClientLogger(this), 0, 1, TimeUnit.MINUTES);
            if (!DEBUG) Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.out.format("Stop to connect to server");
        }
    }

    protected void initWelcomeUI() {
        //开启登陆界面
        loginSuccessRecorder = new CountingRecorder("Login success times");
        loginFailRecorder = new CountingRecorder("Login fail times");
        sendMsgNumRecorder = new CountingRecorder("Send message number");
        receiveMsgRecorder = new CountingRecorder("Receive message times");

        LogReporter reporter = new RollingFileReporter("./log/client-%d{yyyy-MM-dd_HH-mm}.log");
        PerformanceMonitor monitor = new PerformanceMonitor(reporter);
        monitor
                .addRecorder(loginSuccessRecorder)
                .addRecorder(loginFailRecorder)
                .addRecorder(sendMsgNumRecorder)
                .addRecorder(receiveMsgRecorder)
                .start(1,TimeUnit.MINUTES);


        mLoginAndRegisterForm = new LoginAndRegisterForm();
        mLoginAndRegisterForm.setOnLoginListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String user = mLoginAndRegisterForm.getUsername();
                String pass = mLoginAndRegisterForm.getPassword();

                String msgToSend = new MessageBuilder()
                        .add("event", "login")
                        .add("username", user)
                        .add("password", pass)
                        .build();
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
                        .build();
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
        mChatRoomForm.setOnSendListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String msgToSend = mChatRoomForm.getSendMessage();
                msgToSend = new MessageBuilder()
                        .add("event", "send")
                        .add("message", msgToSend)
                        .build();
                sendMessage(msgToSend);
                mChatRoomForm.clearChatArea();
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
                OnLogin(args,loginSuccessRecorder,loginFailRecorder);
            }
        }).addEventListener("relogin", new EventListener() {
            @Override
            public void run(HashMap<String, String> args) {
                OnRelogin(args,loginSuccessRecorder,loginFailRecorder);
            }
        }).addEventListener("send", new EventListener() {
            @Override
            public void run(HashMap<String, String> args) {
                OnSend(args,sendMsgNumRecorder);
            }
        }).addEventListener("forward", new EventListener() {
            @Override
            public void run(HashMap<String, String> args) {
                OnForward(args,receiveMsgRecorder);
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
            String host = Config.getConfig().getString("host", "localhost");
            int port = Config.getConfig().getInt("port", 9001);
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
    public abstract void OnLogin(HashMap<String, String> args,CountingRecorder successRecorder,CountingRecorder failRecorder);
    public abstract void OnRelogin(HashMap<String, String> args,CountingRecorder successRecorder,CountingRecorder failRecorder);
    public abstract void OnSend(HashMap<String, String> args,CountingRecorder sendRecorder);
    public abstract void OnForward(HashMap<String, String> args,CountingRecorder receiveRecorder);
    public abstract void OnDisconnect(HashMap<String, String> args);
    public abstract void OnError(HashMap<String, String> args);

}
