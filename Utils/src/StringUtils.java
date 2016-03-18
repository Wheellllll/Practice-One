import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by sweet on 3/18/16.
 */
public class StringUtils {
    public static String bufToString(ByteBuffer buf) {
            buf.flip();
            int limits = buf.limit();
            byte bytes[] = new byte[limits];
            buf.get(bytes);
            Charset cs = Charset.forName("UTF-8");
            return new String(bytes, cs);
    }
}
