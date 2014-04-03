package se.uc.stat.timestatistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test the class StatisticsInfo.
 * 
 * @author Anders Persson (konx40)
 */
public class StatisticsInfoTest extends TimeTestBase {
    /**
     * Test the constructor and get method.
     */
    @Test
    public void testConstructorGet() {
        StatisticsInfo info = new StatisticsInfo("s1", "m1", "l1");
        assertEquals("Invalid response from getService",
                "s1", info.getService());
        assertEquals("Invalid response from getMethod",
                "m1", info.getMethod());
        assertEquals("Invalid response from getLayer",
                "l1", info.getLayer());
        assertNull("Invalid default value of origin", info.getOrigin());
        assertNull("Invalid default value of origin", info.getProduct());
        assertNull("Invalid default value of origin", info.getMedia());

        info = new StatisticsInfo(null, null, null);
        assertEquals("Invalid response from getService",
                null, info.getService());
        assertEquals("Invalid response from getMethod",
                null, info.getMethod());
        assertEquals("Invalid response from getLayer",
                null, info.getLayer());
    }
    
    /**
     * Test set/getOrigin, set/getProduct, set/getMedia.
     */
    @Test
    public void testSetGet() {
        StatisticsInfo info = new StatisticsInfo("s1", "m1", "l1");
        info.setOrigin("o1");
        assertEquals("Invalid response from getOrigin",
                "o1", info.getOrigin());
        info.setOrigin("o2");

        info.setProduct("p1");
        assertEquals("Invalid response from getProduct",
                "p1", info.getProduct());
        info.setProduct("p2");

        info.setMedia("me1");
        assertEquals("Invalid response from getMedia",
                "me1", info.getMedia());
        info.setMedia("me2");

        assertEquals("Invalid response from getOrigin",
                "o2", info.getOrigin());
        assertEquals("Invalid response from getProduct",
                "p2", info.getProduct());
        assertEquals("Invalid response from getMedia",
                "me2", info.getMedia());

        info.setMethod("m2");
        assertEquals("Invalid response from getMethod after setMethod",
                "m2", info.getMethod());
    }
    
    /**
     * Test the stop methods and removeExternalTime.
     * 
     * @throws InterruptedException if the test fails.
     */
    @Test
    public void testStop() throws InterruptedException {
        final long minStart = System.currentTimeMillis();
        StatisticsInfo info1 = new StatisticsInfo(SERVICE, METHOD,
                LAYER_PREFIX + "1");
        StatisticsInfo info2 = new StatisticsInfo(SERVICE, METHOD,
                LAYER_PREFIX + "2");
        StatisticsInfo info3 = new StatisticsInfo(SERVICE, METHOD,
                LAYER_PREFIX + "3");
        StatisticsInfo ext1 = new StatisticsInfo(SERVICE, "ext1",
                LAYER_PREFIX + "1");
        StatisticsInfo ext2 = new StatisticsInfo(SERVICE, "ext2",
                LAYER_PREFIX + "2");
        StatisticsInfo ext3 = new StatisticsInfo(SERVICE, "ext3",
                LAYER_PREFIX + "3");

        final long maxStart = System.currentTimeMillis();
        Thread.sleep(200);
        long minSlut = System.currentTimeMillis();
        ExternalTime time1 = ext1.stopCorrect();
        ExternalTime time2 = ext2.stopInvalid();
        ExternalTime time3 = ext3.stopFailed();
        long maxSlut = System.currentTimeMillis();
        long minTime = minSlut - maxStart;
        long maxTime = maxSlut -minStart;
        assertTrue("ExternalTime correct is invalid",
                time1.getTime() >= minTime && time1.getTime() <= maxTime);
        assertTrue("ExternalTime invalid is invalid",
                time2.getTime() >= minTime && time2.getTime() <= maxTime);
        assertTrue("ExternalTime failed is invalid",
                time3.getTime() >= minTime && time3.getTime() <= maxTime);
        
        info1.removeExternalTime(time1);
        info2.removeExternalTime(time2);
        info3.removeExternalTime(time3);

        Thread.sleep(200);
        minSlut = System.currentTimeMillis();
        ExternalTime time4 = info1.stopCorrect();
        ExternalTime time5 = info2.stopInvalid();
        ExternalTime time6 = info3.stopFailed();
        maxSlut = System.currentTimeMillis();
        minTime = minSlut - maxStart;
        maxTime = maxSlut -minStart;
        assertTrue("Info correct is invalid",
                time4.getTime() >= minTime - time1.getTime() &&
                time4.getTime() <= maxTime - time1.getTime());
        assertTrue("Info invalid is invalid",
                time5.getTime() >= minTime - time2.getTime() &&
                time5.getTime() <= maxTime - time2.getTime());
        assertTrue("Info failed is invalid",
                time6.getTime() >= minTime - time3.getTime() &&
                time6.getTime() <= maxTime - time3.getTime());
    }
}
