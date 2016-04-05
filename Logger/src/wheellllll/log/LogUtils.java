package wheellllll.log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class provide methods for logging.
 *
 * @author LiaoShanhe
 */

public class LogUtils {
    /**
     * A <code>SimpleDateFormat</code> to format time to customer standard
     */
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * This method is a common entrance for client logger and server logger.
     *
     * @param type    The type of <code>utils</code>
     * @param numbers Parameters that need to be recorded, 4 parameters for <code>CLIENT</code>
     *                logType and 5 parameters for <code>SERVER</code> logType
     * @see LogType
     */
    public static void log(LogType type, int... numbers) {
        switch (type) {
            case CLIENT:
                logForClient(numbers[0], numbers[1], numbers[2], numbers[3]);
                break;
            case SERVER:
                logForServer(numbers[0], numbers[1], numbers[2], numbers[3], numbers[4]);
                break;
            default:
        }
    }

    /**
     * This method is a private method for logging for client.
     *
     * @param loginSuccessNum Login success number of client
     * @param loginFailNum    Login fail number for client
     * @param sendMsgNum      Send message number of client
     * @param receiveMsgNum   Receive message number for client
     */
    private static void logForClient(int loginSuccessNum, int loginFailNum, int sendMsgNum, int receiveMsgNum) {
        File file = null;
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            file = new File("clientRecord.log");
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file.getName(), true);
            bufferedWriter = new BufferedWriter(fileWriter);
            String record = String.format("Log at %s:\n" +
                            "\tLogin successfully number: %d,\n" +
                            "\tLogin failed number: %d,\n" +
                            "\tSend message number: %d,\n" +
                            "\tReceive message number: %d.\n\n",
                    df.format(new Date()), loginSuccessNum, loginFailNum, sendMsgNum, receiveMsgNum);
            bufferedWriter.write(record);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is a private method for logging for server.
     *
     * @param validLoginNum   Valid login number of client
     * @param invalidLoginNum Invalid login number for client
     * @param receiveMsgNum   Received message number from all clients
     * @param ignoreMsgNum    Ignored message number from all clients
     * @param forwardMsgNum   Forwarded message number, adding one for every client every message
     */
    private static void logForServer(int validLoginNum, int invalidLoginNum,
                                     int receiveMsgNum, int ignoreMsgNum, int forwardMsgNum) {
        File file = null;
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            file = new File("serverRecord.log");
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file.getName(), true);
            bufferedWriter = new BufferedWriter(fileWriter);
            String record = String.format("Log at %s:\n" +
                            "\tValid login number: %d ,\n" +
                            "\tInvalid login number: %d ,\n" +
                            "\tReceive message number: %d ,\n" +
                            "\tIgnore message number: %d ,\n" +
                            "\tForward message number: %d .\n\n",
                    df.format(new Date()), validLoginNum, invalidLoginNum, receiveMsgNum, ignoreMsgNum, forwardMsgNum);
            bufferedWriter.write(record);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    protected File OpenFile(String fileName) {
        return new File(fileName);
    }

    /**
     * Type of logging, <code>CLIENT</code> represents logging for client, <code>SERVER</code>
     * represents logging for server.
     */
    public enum LogType {
        CLIENT, SERVER
    }

}
