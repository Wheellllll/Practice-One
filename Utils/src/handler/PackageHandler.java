package handler;

import utils.StringUtils;

import java.nio.ByteBuffer;

/**
 * This class is used to solve some common issues which may occur in the socket
 */
public class PackageHandler {
    /*
     * 处理分包
     */
    private StringBuilder mSb;

    public PackageHandler() {
        mSb = new StringBuilder();
    }

    /**
     * This method check if there exists complete package
     * @return True if there exists a complete package or False if not
     */
    public boolean hasPackage() {
        return mSb.indexOf("\u0004") >= 0;
    }

    /**
     * Add a package read from the socket to this class
     * @param buf ButeBuffer read from the socket
     */
    public void addPackage(ByteBuffer buf) {
        mSb.append(StringUtils.bufToString(buf));
    }

    /**
     * Get a message in String pattern from this class. You may need to use hasPackage method
     * to check if there exists a complete package
     * @return String transformed from the ByteBuffer
     */
    public String getPackage() {
        int offset = mSb.indexOf("\u0004");
        String tempPackage = mSb.substring(0, offset);
        mSb.delete(0, offset + 1);
        return tempPackage;
    }
}
