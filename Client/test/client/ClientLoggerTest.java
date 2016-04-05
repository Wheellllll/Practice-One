package client;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by Kris Chan on 10:26 PM 3/27/16 .
 * All right reserved.
 */
public class ClientLoggerTest {

    /**
     * 测试运行状态
     * @throws Exception
     */
    @Test
    public void testRun() throws Exception {
        int[] par1 = {};
        int[] par2 = {};
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

        assertEquals(result1,par1);
        assertEquals(result2,par2);

    }
}
