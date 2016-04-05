package wheellllll.log;

import junit.framework.TestCase;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * This class is written for testing login wheellllll.utils
 */
public class LogUtilsTest{

    @Test
    public void testLog() throws Exception {
        int[] par1 = {1,1,2,2};
        int[] par2 = {1,1,2,2,1};
        LogUtils.log(LogUtils.LogType.CLIENT, par1);
        LogUtils.log(LogUtils.LogType.SERVER,par2);
        Path filePath1 = Paths.get("clientRecord.utils");
        int[] result1 = new int[4];
        Scanner scanner1 =  new Scanner(filePath1);
        for (int i = 0; i < 4 ; ++i)
        {
            if(scanner1.hasNextInt())
            {
                result1[i] = scanner1.nextInt();
            }
            else
            {
                scanner1.next();
            }
        }

        Path filePath2 = Paths.get("serverRecord.utils");
        int[] result2 = new int[5];
        Scanner scanner2 = new Scanner(filePath2);
        for(int i = 0; i < 5; ++i)
        {
            if(scanner2.hasNextInt())
            {
                result2[i] = scanner2.nextInt();
            }
            else
            {
                scanner2.next();
            }
        }

        TestCase.assertEquals(result1,par1);
        TestCase.assertEquals(result2,par2);

    }
}