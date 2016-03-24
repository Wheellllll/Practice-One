package utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public static String md5Hash(String plainText) {
        String str = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            byte[] bytes = md5.digest(plainText.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(Integer.toHexString(bt));
            }
            str = stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str;
    }
}
