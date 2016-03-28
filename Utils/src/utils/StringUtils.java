package utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class provide methods for transforming strings
 */
public class StringUtils {
    /**
     * This method convert a <code>ByteBuffer</code> to a <code>String</code>
     *
     * @param buf ByteBuffer needs to convert
     * @return String The converted result of input
     */
    public static String bufToString(ByteBuffer buf) {
        buf.flip();
        int limits = buf.limit();
        byte bytes[] = new byte[limits];
        buf.get(bytes);
        Charset cs = Charset.forName("UTF-8");
        return new String(bytes, cs);
    }

    /**
     * This method calculate the md5 value of a input
     *
     * @param plainText The plain text needs to be encrypted
     * @return Encrypted valve of input
     */
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
