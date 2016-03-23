import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LiaoShanhe on 2016/3/21.
 */
public class LogUtils {

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
                            "\tValid login number: %d,\n" +
                            "\tInvalid login number: %d,\n" +
                            "\tReceive message number: %d,\n" +
                            "\tIgnore message number: %d,\n" +
                            "\tForward message number: %d.\n\n",
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

    public enum LogType {
        CLIENT, SERVER
    }

}
