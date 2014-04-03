package se.uc.stat.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the TimeRespesentation class.
 * 
 * @author Anders Persson (konx40)
 */
public class TimeRepresentationTest extends AbstractTestBase {
    /**
     * Test the constructor and all get methods.
     * 
     * @throws ParseException if the test fails due to invalid test data.
     */
    @Test
    public void testConstructor() throws ParseException {
        assertConstructor("2011-01-24 01:24:32.123", 0);
        assertConstructor("2011-01-25 00:00:00.000", 1);
        assertConstructor("2011-01-26 23:59:59.999", 2);
        assertConstructor("2011-01-27 15:24:32.123", 3);
        assertConstructor("2011-01-28 07:00:00.000", 4);
        assertConstructor("2011-01-29 19:59:59.999", 5);
        assertConstructor("2011-01-30 23:24:32.123", 6);
    }
    
    /**
     * Perform the test of the constructor.
     * 
     * @param testDate  The test date with the format "yyyy-MM-dd HH:mm:ss.SSS"
     * @param dayOfWeek The expected day of the week.
     * 
     * @throws ParseException If the testDate is not possible to parse.
     */
    private void assertConstructor(String testDate, int dayOfWeek)
            throws ParseException {
        final String testCase = "test date " + testDate + ". ";
        final SimpleDateFormat format =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        final long testTime = format.parse(testDate).getTime();
        TimeRepresentation rep = new TimeRepresentation(testTime);
        
        final String expectedDate = testDate.substring(0, 10) + " 00:00:00.000";
        final String actualDate = format.format(new Date(rep.getDate()));
        assertEquals(testCase + "Invalid getDate", expectedDate, actualDate);
        
        final String expectedDateHour = testDate.substring(0, 13) +
                ":00:00.000";
        final String actualDateHour = format.format(
                new Date(rep.getDateHour()));
        assertEquals(testCase + "Invalid getDateHour",
                expectedDateHour, actualDateHour);

        final int expectedHourOfDay =
                Integer.parseInt(testDate.substring(11, 13));
        assertEquals(testCase + "Invalid getHourOfDay",
                expectedHourOfDay, rep.getHourOfDay());
        
        assertEquals(testCase + "Invalid getDayOfWeek",
                dayOfWeek, rep.getDayOfWeek());
    }
}
