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

    public enum LogType {
        CLIENTLOGIN, SERVERLOGIN, CLIENTMESSAGE, SERVERMESSAGE
    }

    public static void log(LogType type, int ... numbers) {
        switch (type) {
            case CLIENTLOGIN:
                logForClientLogin(numbers[0], numbers[1]);
                break;
            case SERVERLOGIN:
                logForServerLogin(numbers[0], numbers[1]);
                break;
            case CLIENTMESSAGE:
                logForClientMessage(numbers[0], numbers[1]);
                break;
            case SERVERMESSAGE:
                logForServerMessage(numbers[0], numbers[1], numbers[2]);
                break;
            default:
        }
    }

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static void logForClientLogin(int successfulNum, int failedNum) {
        File file = null;
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            file = new File("clientLogin.log");
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file.getName(), true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Log at " + df.format(new Date()) + ":\nLogin successfully number: " + successfulNum + "\nLogin failed number: " + failedNum + "\n\n");
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

    private static void logForServerLogin(int validNum, int invalidNum) {
        File file = null;
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            file = new File("serverLogin.log");
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file.getName(), true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Log at " + df.format(new Date()) + ":\nValid login number: " + validNum + "\nInvalid login number: " + invalidNum + "\n\n");
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

    private static void logForClientMessage(int sendMsgNum, int receiveMsgNum) {
        File file = null;
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            file = new File("clientMessage.log");
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file.getName(), true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Log at " + df.format(new Date()) + ":\nSend message number: " + sendMsgNum + "\nReceive message number: " + receiveMsgNum + "\n\n");
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

    private static void logForServerMessage(int receiveMsgNum, int ignoreMsgNum, int forwardMsgNum) {
        File file = null;
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            file = new File("serverMessage.log");
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file.getName(), true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Log at " + df.format(new Date()) + ":\nReceive message number: " + receiveMsgNum + "\nIgnore message number: " + ignoreMsgNum + "\nForward message number: " + forwardMsgNum + "\n\n");
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

}
