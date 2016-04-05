package wheellllll.utils;

import junit.framework.TestCase;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * This class is written for testing string wheellllll.utils
 */
public class StringUtilsTest extends TestCase {

    @Test
    public void testBufToString() throws Exception {
        ByteBuffer buf = ByteBuffer.allocate(2048);
        String str = "test";
        buf.put(str.getBytes());
        String result = StringUtils.bufToString(buf);
        assertEquals("test",result);

    }

    @Test
    public void testMd5Hash() throws Exception {
        String md5Str = "098f6bcd4621d373cade4e832627b4f6";
        String result = StringUtils.md5Hash("test");
        assertEquals(md5Str,result);
    }
}