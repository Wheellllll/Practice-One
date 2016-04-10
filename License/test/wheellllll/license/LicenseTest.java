package wheellllll.license;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class is written for testing <code>License</code>
 */
public class LicenseTest {

    @Test
    public void testLicense() {
        License license = new License(License.LicenseType.BOTH, 300, 5, License.TimeUnit.MINUTES);
        assertEquals(true, license.isCapacityOn());
        assertEquals(300, license.getCapacityLimit());
        assertEquals(0, license.getCurrentCapacity());
        assertEquals(0, license.getPreviousCapacity());
        assertEquals(true, license.isThroughputOn());
        assertEquals(5, license.getThroughputLimit());
        assertEquals(License.TimeUnit.MINUTES, license.getTimeUnit());
        assertEquals(0, license.getCurrentThroughput());
        assertEquals(0, license.getPreviousThroughput());
        assertEquals(0, license.getLastTime());
    }

    @Test
    public void testSetDefaultTimeUnit() {
        License.setDefaultTimeUnit(License.TimeUnit.DAYS);
        assertEquals(License.TimeUnit.DAYS, License.getDefaultTimeUnit());
        License.setDefaultTimeUnit(License.TimeUnit.SECONDS);
        assertEquals(License.TimeUnit.SECONDS, License.getDefaultTimeUnit());
    }

    @Test
    public void testEnableCapacity() {
        License license = new License();
        license.enableCapacity(400, false);
        assertEquals(true, license.isCapacityOn());
        assertEquals(400, license.getCapacityLimit());
    }

    @Test
    public void testEnableThroughput() {
        License license = new License();
        license.enableThroughput(60, false, License.TimeUnit.DAYS);
        assertEquals(true, license.isThroughputOn());
        assertEquals(60, license.getThroughputLimit());
        assertEquals(License.TimeUnit.DAYS, license.getTimeUnit());
    }

    @Test
    public void testDisableCapacity() {
        License license = new License(License.LicenseType.CAPACITY, 50);
        for (int i = 0; i < 30; i++)
            license.use();
        license.disableCapacity();
        assertEquals(false, license.isCapacityOn());
        assertEquals(30, license.getPreviousCapacity());
    }

    @Test
    public void testDisableThroughput() {
        License license = new License(License.LicenseType.THROUGHPUT, 10);
        for (int i = 0; i < 5; i++)
            license.use();
        license.disableThroughput();
        assertEquals(false, license.isThroughputOn());
        assertEquals(5, license.getPreviousThroughput());
    }

    @Test
    public void testUse() {
        License license = new License(License.LicenseType.CAPACITY, 100);
        for (int i = 0; i < 100; i++) {
            assertEquals(License.Availability.AVAILABLE, license.use());
        }
        assertEquals(License.Availability.CAPACITYEXCEEDED, license.use());
        license.disableCapacity();

        license.enableThroughput(5, false);
        for (int i = 0; i < 5; i++) {
            assertEquals(License.Availability.AVAILABLE, license.use());
        }
        assertEquals(License.Availability.THROUGHPUTEXCEEDED, license.use());
        license.enableCapacity(0, false);
        license.setThroughputLimit(0);
        assertEquals(License.Availability.BOTHEXCEEDED, license.use());
    }

    @Test
    public void testReset() {
        License license = new License(License.LicenseType.BOTH, 100, 5);
        license.reset(License.LicenseType.BOTH);
        assertEquals(0, license.getCurrentCapacity());
        assertEquals(0, license.getCurrentThroughput());
    }

}
