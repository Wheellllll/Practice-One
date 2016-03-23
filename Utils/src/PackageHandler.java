import java.nio.ByteBuffer;

/**
 * Created by sweet on 3/23/16.
 */
public class PackageHandler {
    /*
     * 处理分包
     */
    private StringBuilder mSb;

    public PackageHandler() {
        mSb = new StringBuilder();
    }

    public boolean hasPackage() {
        return mSb.indexOf("\u0004") >= 0;
    }

    public void addPackage(ByteBuffer buf) {
        mSb.append(StringUtils.bufToString(buf));
    }

    public String getPackage() {
        int offset = mSb.indexOf("\u0004");
        String tempPackage = mSb.substring(0, offset);
        mSb.delete(0, offset + 1);
        return tempPackage;
    }
}
