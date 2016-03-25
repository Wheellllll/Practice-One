package test;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Test;
import utils.MessageBuilder;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by summer on 3/25/16.
 */
public class MessageBuilderTest extends TestCase{

    @Test
    public void testAdd() throws Exception {
        MessageBuilder msgBuilder = new MessageBuilder();
        msgBuilder.add("test","testbuild");
        String result = msgBuilder.build();
        HashMap<String,String> test = new HashMap<String,String>();
        test.put("test","testbuild");
        String expect = JSON.toJSONString(test);
        assertEquals(expect,result);
    }

    @Test
    public void testBuild() throws Exception {
        MessageBuilder msgBuilder = new MessageBuilder();
        String result = msgBuilder.build();
        assertEquals("{}",result);
    }
}