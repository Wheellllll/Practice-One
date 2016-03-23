/**
 * Created by sweet on 3/17/16.
 */
public class Settings {
    /*
     * socket状态
     * LOGOUT--已登出
     * LOGIN--已登陆
     * RELOGIN--已发送100条消息，需重新登陆
     * IGNORE--超过5条/秒的限制，忽略
     *
     */

    public static final String HOST = "localhost";
    public static final int PORT = 9001;

    public enum Status {
        LOGIN, LOGOUT, RELOGIN, IGNORE
    }

    public static int maxNumberPerSecond = 5;

    public static int maxNumberPerSession = 10;

}
