package se.uc.stat.timestatistics;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import se.uc.stat.basestatistics.BaseCollectorUtils;
import se.uc.stat.utils.TimeRepresentation;
import se.uc.stat.utils.TimeUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test the class TimeCollector.
 * 
 * @author Anders Persson (konx40)
 */
public class TimeCollectorTest extends TimeTestBase {
    /**
     * Test the method insertInformation and successful test cases of
     * updateInformation.
     * 
     * @throws SQLException if the test fails.
     */
    @Test
    public void testInsertUpdateInformation() throws SQLException {
        cleanCustomerStatDb();
        TimeCollector collector = new TimeCollector();
        int index = 0;
        for (TimeInformation info : getTestData()) {
            final String testCaseInsert =
                "key=" + info.getKey() +
                ", dayOfWeek=" + info.getDayOfWeek() +
                ", hourOfDay=" + info.getHourOfDay() +
                ", numCorrectCalls=" + info.getNumCorrectCalls() +
                ", numInvalidCalls=" + info.getNumInvalidCalls() +
                ", numFailedCalls=" + info.getNumFailedCalls() +
                ", totalTimeCorrectCalls=" + info.getTotalTimeCorrectCalls() +
                ", totalTimeInvalidCalls=" + info.getTotalTimeInvalidCalls() +
                ", totalTimeFailedCalls=" + info.getTotalTimeFailedCalls() +
                ", num10=" + info.getNum10() +
                ", num20=" + info.getNum20() +
                ", num50=" + info.getNum50() +
                ", num100=" + info.getNum100() +
                ", num200=" + info.getNum200() +
                ", num500=" + info.getNum500() +
                ", num1000=" + info.getNum1000() +
                ", num2000=" + info.getNum2000() +
                ", num5000=" + info.getNum5000() +
                ", num10000=" + info.getNum10000() +
                ", num20000=" + info.getNum20000() +
                ", numOver20000=" + info.getNumOver20000();
            assertTrue(testCaseInsert + "insertInformation() first call",
                    BaseCollectorUtils.insertInformation(collector,
                    getConnection(), info));
            assertFalse(testCaseInsert + "insertInformation() second call",
                    BaseCollectorUtils.insertInformation(collector,
                    getConnection(), info));
            assertInfoInDb(testCaseInsert, info);
            final TimeInformation update = new TimeInformation(
                    info.getKey(), info.getDayOfWeek(), info.getHourOfDay());
            // Register correct calls.
            getTestTimes();
            for (int i = 0; i < index; i++) {
                for (Long time : getTestTimes()) {
                    info.registerCorrectCall(time.longValue());
                    update.registerCorrectCall(time.longValue());
                }
            }
            // Register invalid calls.
            for (int i = 0; i < index + 2; i++) {
                for (Long time : getTestTimes()) {
                    info.registerInvalidCall(time.longValue());
                    update.registerInvalidCall(time.longValue());
                }
            }
            // Register failed calls.
            for (int i = 0; i < index; i++) {
                for (Long time : getTestTimes()) {
                    info.registerFailedCall(time.longValue());
                    update.registerFailedCall(time.longValue());
                }
            }
            final String testCaseUpdate =
                "key=" + update.getKey() +
                ", dayOfWeek=" + update.getDayOfWeek() +
                ", hourOfDay=" + update.getHourOfDay() +
                ", numCorrectCalls=" + update.getNumCorrectCalls() +
                ", numInvalidCalls=" + update.getNumInvalidCalls() +
                ", numFailedCalls=" + update.getNumFailedCalls() +
                ", totalTimeCorrectCalls=" + update.getTotalTimeCorrectCalls() +
                ", totalTimeInvalidCalls=" + update.getTotalTimeInvalidCalls() +
                ", totalTimeFailedCalls=" + update.getTotalTimeFailedCalls() +
                ", num10=" + update.getNum10() +
                ", num20=" + update.getNum20() +
                ", num50=" + update.getNum50() +
                ", num100=" + update.getNum100() +
                ", num200=" + update.getNum200() +
                ", num500=" + update.getNum500() +
                ", num1000=" + update.getNum1000() +
                ", num2000=" + update.getNum2000() +
                ", num5000=" + update.getNum5000() +
                ", num10000=" + update.getNum10000() +
                ", num20000=" + update.getNum20000() +
                ", numOver20000=" + update.getNumOver20000();
            assertTrue(testCaseUpdate + "updateInformation() first call",
                    BaseCollectorUtils.updateInformation(collector,
                    getConnection(), update));
            assertInfoInDb(testCaseUpdate, info);
            index++;
        }
        cleanCustomerStatDb();
    }

    /**
     * Get test data.
     * 
     * @return A list of test data.
     */
    private List<TimeInformation> getTestData() {
        ArrayList<TimeInformation> result = new ArrayList<TimeInformation>();
        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        TimeRepresentation time1 = null;
        TimeRepresentation time2 = null;
        try {
            time1 = TimeUtils.getTimeRepresentation(
                    formatter.parse("2011-01-25 09:12:35.123").getTime());
            time2 = TimeUtils.getTimeRepresentation(
                formatter.parse("2011-01-25 19:56:12.456").getTime());
        } catch (ParseException e) {
            fail("Could not parse date. This is a programming error in the " +
                    "test code");
            // This will never happens, but the compiler does not know that and
            // gives a warning otherwise.
            return null;
        }
        
        TimeStatisticsKey key;
        TimeInformation info;

        key = new TimeStatisticsKey(SERVICE, METHOD + "1",
                ORIGIN_PREFIX + "1", "p1", MEDIA_PREFIX + "1",
                LAYER_PREFIX + "1", new Date(time1.getDateHour()));
        info = new TimeInformation(key, time1.getDayOfWeek(),
                time1.getHourOfDay());
        for (Long time : getTestTimes()) {
            info.registerCorrectCall(time.longValue());
            info.registerCorrectCall(time.longValue());
            info.registerCorrectCall(time.longValue());
            info.registerInvalidCall(time.longValue());
            info.registerInvalidCall(time.longValue());
            info.registerFailedCall(time.longValue());
        }
        result.add(info);

        key = new TimeStatisticsKey(SERVICE, METHOD + "1",
                ORIGIN_PREFIX + "1", "p1", MEDIA_PREFIX + "1",
                LAYER_PREFIX + "1", new Date(time2.getDateHour()));
        info = new TimeInformation(key, time2.getDayOfWeek(),
                time2.getHourOfDay());
        for (Long time : getTestTimes()) {
            info.registerCorrectCall(time.longValue());
        }
        result.add(info);

        key = new TimeStatisticsKey(null, METHOD,
                ORIGIN_PREFIX + "1", "p1", MEDIA_PREFIX + "1",
                LAYER_PREFIX + "1", new Date(time1.getDateHour()));
        info = new TimeInformation(key, time1.getDayOfWeek(),
                time1.getHourOfDay());
        for (Long time : getTestTimes()) {
            info.registerCorrectCall(time.longValue());
        }
        result.add(info);

        key = new TimeStatisticsKey(SERVICE, null,
                ORIGIN_PREFIX + "1", "p1", MEDIA_PREFIX + "1",
                LAYER_PREFIX + "1", new Date(time1.getDate()));
        info = new TimeInformation(key, time1.getDayOfWeek(),
                time1.getHourOfDay());
        for (Long time : getTestTimes()) {
            info.registerInvalidCall(time.longValue());
        }
        result.add(info);

        key = new TimeStatisticsKey(SERVICE, METHOD,
                null, "p1", MEDIA_PREFIX + "1",
                LAYER_PREFIX + "1", new Date(time1.getDate()));
        info = new TimeInformation(key, time1.getDayOfWeek(),
                time1.getHourOfDay());
        for (Long time : getTestTimes()) {
            info.registerFailedCall(time.longValue());
        }
        result.add(info);

        key = new TimeStatisticsKey(SERVICE, METHOD,
                ORIGIN_PREFIX + "1", null, MEDIA_PREFIX + "1",
                LAYER_PREFIX + "1", new Date(time2.getDate()));
        info = new TimeInformation(key, time2.getDayOfWeek(),
                time2.getHourOfDay());
        for (Long time : getTestTimes()) {
            info.registerCorrectCall(time.longValue());
            info.registerCorrectCall(time.longValue());
            info.registerInvalidCall(time.longValue());
        }
        result.add(info);

        key = new TimeStatisticsKey(SERVICE, METHOD,
                ORIGIN_PREFIX + "1", "p1", null,
                LAYER_PREFIX + "1", new Date(time2.getDate()));
        info = new TimeInformation(key, time2.getDayOfWeek(),
                time2.getHourOfDay());
        for (Long time : getTestTimes()) {
            info.registerInvalidCall(time.longValue());
            info.registerInvalidCall(time.longValue());
            info.registerFailedCall(time.longValue());
        }
        result.add(info);

        key = new TimeStatisticsKey(SERVICE, METHOD,
                ORIGIN_PREFIX + "1", "p1", MEDIA_PREFIX + "1",
                null, new Date(time2.getDate()));
        info = new TimeInformation(key, time2.getDayOfWeek(),
                time2.getHourOfDay());
        for (Long time : getTestTimes()) {
            info.registerFailedCall(time.longValue());
            info.registerFailedCall(time.longValue());
            info.registerCorrectCall(time.longValue());
        }
        result.add(info);

        final String longProduct = "p23456789012345678901234567890";
        key = new TimeStatisticsKey(SERVICE, METHOD,
                ORIGIN_PREFIX + "1", longProduct, MEDIA_PREFIX + "1",
                LAYER_PREFIX + "1", new Date(time2.getDate()));
        info = new TimeInformation(key, time2.getDayOfWeek(),
                time2.getHourOfDay());
        for (Long time : getTestTimes()) {
            info.registerCorrectCall(time.longValue());
            info.registerCorrectCall(time.longValue());
            info.registerCorrectCall(time.longValue());
        }
        result.add(info);

        return result;
    }

    /**
     * Get test times to use. The list holds from one to twelve times in each
     * interval. The times are randomly spread within the interval.
     * 
     * @return A list with test times.
     *         This method never returns <code>null</code>.
     */
    private List<Long> getTestTimes() {
        ArrayList<Long> result = new ArrayList<Long>();
        long breakTimes[] = new long[] {
            10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 50000
        };
        for (int index = 0; index < breakTimes.length; index++) {
            for (int loopCount = 0; loopCount < breakTimes.length - index; 
                    loopCount++) {
                long minTime = 0;
                long maxTime = breakTimes[index];
                if (index > 0) {
                    minTime = breakTimes[index - 1] + 1;
                }
                long time = minTime + (long)(Math.random() * (maxTime - minTime));
                result.add(new Long(time));
            }
        }
        assertEquals("Invalid number of test times", 78, result.size());
        return result;
    }
    
    /**
     * Test unsuccessful test cases of updateInformation.
     * Successful tests are done in {@link #testInsertUpdateInformation()}.
     * 
     * @throws SQLException if the test fails.
     */
    @Test
    public void testUpdateInformation() throws SQLException {
        cleanCustomerStatDb();
        TimeCollector collector = new TimeCollector();
        TimeStatisticsKey key = new TimeStatisticsKey(SERVICE, METHOD,
                ORIGIN_PREFIX + "1", "p1", MEDIA_PREFIX + "1",
                LAYER_PREFIX + "1", new java.util.Date());
        TimeInformation info = new TimeInformation(key, 0, 0);
        assertFalse("updateInformation() with object not in db",
                BaseCollectorUtils.updateInformation(collector,
                getConnection(), info));
        cleanCustomerStatDb();
    }
}
