/**
 * Created by LiaoShanhe on 2016/3/21.
 */
public class ClientLogger implements Runnable {

    private Client client = null;

    public ClientLogger(Client c) {
        client = c;
    }

    @Override
    public void run() {
        LogUtils.log(LogUtils.LogType.CLIENTLOGIN, client.getLoginSuccessNum(), client.getLoginFailNum());
        LogUtils.log(LogUtils.LogType.CLIENTMESSAGE, client.getSendMsgNum(), client.getReceiveMsgNum());
    }
}
