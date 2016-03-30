package client;

import org.junit.Test;
import ui.LoginAndRegisterForm;

import static org.junit.Assert.*;

/**
 * Created by Kris Chan on 9:52 AM 3/27/16 .
 * All right reserved.
 */
public class BaseClientTest {

    /**
     * 获取登录成功次数
     * @throws Exception
     */
    @Test
    public void testGetLoginSuccessNum() throws Exception {


    }

    /**
     * 获取登录失败次数
     * @throws Exception
     */
    @Test
    public void testGetLoginFailNum() throws Exception {

    }

    /**
     * 获取发送信息数目
     * @throws Exception
     */
    @Test
    public void testGetSendMsgNum() throws Exception {

    }

    /**
     * 获取接受信息数目
     * @throws Exception
     */
    @Test
    public void testGetReceiveMsgNum() throws Exception {

    }

    /**
     * 获取用户姓名
     * @throws Exception
     */
    @Test
    public void testGetUsername() throws Exception {

    }

    /**
     * 获取用户密码
     * @throws Exception
     */
    @Test
    public void testGetPassword() throws Exception {

    }

    /**
     * 获取登录成功次数
     * @throws Exception
     */
    @Test
    public void testIncLoginSuccessNum() throws Exception {

    }

    /**
     * 获取登录失败次数
     * @throws Exception
     */
    @Test
    public void testIncLoginFailNum() throws Exception {

    }

    /**
     * 获取每秒发送消息数
     * @throws Exception
     */
    @Test
    public void testIncSendMsgNum() throws Exception {

    }

    /**
     * 获取每秒发送消息数
     * @throws Exception
     */
    @Test
    public void testIncReceiveMsgNum() throws Exception {

    }

    /**
     * 获取登录注册表单
     * @throws Exception
     */
    @Test
    public void testGetLoginAndRegisterForm() throws Exception {

    }

    /**
     * 获取聊天室表单
     * @throws Exception
     */
    @Test
    public void testGetChatRoomForm() throws Exception {

    }

    /**
     * 测试发送信息数目
     * @throws Exception
     */
    @Test
    public void testSendMessage() throws Exception {

    }

    /**
     * 登录ui界面是否能成功打开
     * @throws Exception
     */
    @Test
    public void testInitWelcomeUI() throws Exception {
        LoginAndRegisterForm mLoginAndRegisterForm = new LoginAndRegisterForm();
        assertNotNull(mLoginAndRegisterForm);
    }

    /**
     * 初始化ui界面能否成功打开
     * @throws Exception
     */
    @Test
    public void testInitChatRoomUI() throws Exception {
        LoginAndRegisterForm mLoginAndRegisterForm = new LoginAndRegisterForm();
        assertNotNull(mLoginAndRegisterForm);
    }

    /**
     * 获取链接数目
     * @throws Exception
     */
    @Test
    public void testOnConnect() throws Exception {

    }

    /**
     * 测试是否注册
     * @throws Exception
     */
    @Test
    public void testOnRegister() throws Exception {

    }

    /**
     * 测试是否登录状态
     * @throws Exception
     */
    @Test
    public void testOnLogin() throws Exception {

    }

    /**
     * 测试重新登录
     * @throws Exception
     */
    @Test
    public void testOnRelogin() throws Exception {

    }

    /**
     * 测试发送信息
     * @throws Exception
     */
    @Test
    public void testOnSend() throws Exception {

    }

    /**
     * 测试转发
     * @throws Exception
     */
    @Test
    public void testOnForward() throws Exception {

    }

    /**
     * 测试连接状态
     * @throws Exception
     */
    @Test
    public void testOnDisconnect() throws Exception {

    }

    @Test
    public void testOnError() throws Exception {

    }
}