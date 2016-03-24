import utils.LogUtils;

/**
 * Created by LiaoShanhe on 2016/3/21.
 */
public class ClientLogger implements Runnable {

    private BaseClient client = null;

    public ClientLogger(BaseClient c) {
        client = c;
    }

    @Override
    public void run() {
        LogUtils.log(LogUtils.LogType.CLIENT, client.getLoginSuccessNum(),
                client.getLoginFailNum(), client.getSendMsgNum(), client.getReceiveMsgNum());
    }
}
