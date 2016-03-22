import java.util.ArrayList;

/**
 * Created by LiaoShanhe on 2016/3/21.
 */
public class ServerLogger implements Runnable {

    private ArrayList<NIOClient> clients = null;
    private int validLogin = 0;
    private int invalidLogin = 0;
    private int receiveMsgNum = 0;
    private int ignoreMsgNum = 0;
    private int forwardMsgNum = 0;

    public ServerLogger(ArrayList<NIOClient> c) {
        clients = c;
    }

    @Override
    public void run() {
        for (NIOClient client : clients) {
            validLogin += client.getLocalValidLogin();
            invalidLogin += client.getLocalInvalidLogin();
            receiveMsgNum += client.getLocalReceiveMsgNum();
            ignoreMsgNum += client.getLocalIgnoreMsgNum();
            forwardMsgNum += client.getLocalForwardMsgNum();
        }
        LogUtils.log(LogUtils.LogType.SERVERLOGIN, validLogin, invalidLogin);
        LogUtils.log(LogUtils.LogType.SERVERMESSAGE, receiveMsgNum, ignoreMsgNum, forwardMsgNum);
        validLogin = 0;
        invalidLogin = 0;
        receiveMsgNum = 0;
        ignoreMsgNum = 0;
        forwardMsgNum = 0;
    }
}
