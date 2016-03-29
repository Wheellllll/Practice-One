package utils;

import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.junit.Test;
import utils.LogUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * This class is written for testing login utils
 */
public class LogUtilsTest extends TestCase{

    @Test

    public void testLog() throws Exception {
        int[] par1 = {1,1,2,2};
        int[] par2 = {1,1,2,2,1};
        LogUtils.log(LogUtils.LogType.CLIENT, par1);
        LogUtils.log(LogUtils.LogType.SERVER,par2);
        Path filePath1 = Paths.get("clientRecord.log");
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

        Path filePath2 = Paths.get("serverRecord.log");
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

        assertEquals(result1,par1);
        assertEquals(result2,par2);

    }
}