package se.uc.stat.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the TimeUtils class.
 * 
 * @author Anders Persson (konx40)
 */
public class TimeUtilsTest extends AbstractTestBase {
    /** Previous object tested. */
    private TimeRepresentation previousObject = new TimeRepresentation(0);
    
    /**
     * Test the getTimeRepresentation method.
     * 
     * @throws ParseException if the test fails due to invalid test data.
     */
    @Test
    public void testGetTimeRepresentation() throws ParseException {
        assertTimeRepresentation("2011-01-24 01:24:32.123", false);
        assertTimeRepresentation("2011-01-24 01:00:00.000", true);
        assertTimeRepresentation("2011-01-24 01:59:59.999", true);
        assertTimeRepresentation("2011-01-24 02:00:00.000", false);
        assertTimeRepresentation("2011-01-24 01:59:59.999", false);
        assertTimeRepresentation("2011-01-25 01:24:32.123", false);
        assertTimeRepresentation("2011-01-25 01:12:00.000", true);
        assertTimeRepresentation("2011-01-25 17:24:12.123", false);
        assertTimeRepresentation("2011-01-25 17:37:00.000", true);
    }
    
    /**
     * Perform the test of the getTimeRepresentation.
     * 
     * @param testDate   The test date with the format "yyyy-MM-dd HH:mm:ss.SSS"
     * @param expectSame <code>true</code> if the same instance as the last
     *                   call is expected. <code>false</code> if another
     *                   instance is expected.
     * 
     * @throws ParseException If the testDate is not possible to parse.
     */
    private void assertTimeRepresentation(String testDate, boolean expectSame)
            throws ParseException {
        final String testCase = "test date " + testDate + ". ";
        final SimpleDateFormat format =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        final long testTime = format.parse(testDate).getTime();
        TimeRepresentation rep = TimeUtils.getTimeRepresentation(testTime);

        if (expectSame) {
            assertSame(testCase + "Same expected", previousObject, rep);
        } else {
            assertTrue(testCase + "Different objects expected",
                    previousObject != rep);
            previousObject = rep;
        }
        
        final String expectedDateHour = testDate.substring(0, 13) +
                ":00:00.000";
        final String actualDateHour = format.format(
                new Date(rep.getDateHour()));
        assertEquals(testCase + "Invalid getDateHour",
                expectedDateHour, actualDateHour);
    }
}
