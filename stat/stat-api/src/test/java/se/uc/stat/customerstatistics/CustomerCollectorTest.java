package se.uc.stat.customerstatistics;

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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test the class CustomerCollector.
 * 
 * @author Anders Persson (konx40)
 */
public class CustomerCollectorTest extends CustomerTestBase {
    /**
     * Test the method insertInformation and successful test cases of
     * updateInformation.
     * 
     * @throws SQLException if the test fails.
     */
    @Test
    public void testInsertUpdateInformation() throws SQLException {
        cleanCustomerStatDb();
        CustomerCollector collector = new CustomerCollector();
        int index = 0;
        for (CustomerInformation info : getTestData()) {
            final String testCaseInsert =
                "key=" + info.getKey() +
                ", dayOfWeek=" + info.getDayOfWeek() +
                ", numCorrectCalls=" + info.getNumCorrectCalls() +
                ", numInvalidCalls=" + info.getNumInvalidCalls() +
                ", numFailedCalls=" + info.getNumFailedCalls();
            assertTrue(testCaseInsert + "insertInformation() first call",
                    BaseCollectorUtils.insertInformation(collector,
                    getConnection(), info));
            assertFalse(testCaseInsert + "insertInformation() second call",
                    BaseCollectorUtils.insertInformation(collector,
                    getConnection(), info));
            assertInfoInDb(testCaseInsert, info);
            final CustomerInformation update = new CustomerInformation(
                    info.getKey(), info.getDayOfWeek());
            // Register correct calls.
            for (int i = 0; i < index; i++) {
                info.registerCorrectCall();
                update.registerCorrectCall();
            }
            // Register invalid calls.
            for (int i = 0; i < index + 2; i++) {
                info.registerInvalidCall();
                update.registerInvalidCall();
            }
            // Register failed calls.
            for (int i = 0; i < index; i++) {
                info.registerFailedCall();
                update.registerFailedCall();
            }
            final String testCaseUpdate =
                "key=" + update.getKey() +
                ", dayOfWeek=" + update.getDayOfWeek() +
                ", numCorrectCalls=" + update.getNumCorrectCalls() +
                ", numInvalidCalls=" + update.getNumInvalidCalls() +
                ", numFailedCalls=" + update.getNumFailedCalls();
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
    private List<CustomerInformation> getTestData() {
        ArrayList<CustomerInformation> result = new ArrayList<CustomerInformation>();
        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        TimeRepresentation time1 = null;
        TimeRepresentation time2 = null;
        try {
            time1 = TimeUtils.getTimeRepresentation(
                    formatter.parse("2011-01-25 09:12:35.123").getTime());
            time2 = TimeUtils.getTimeRepresentation(
                formatter.parse("2011-01-27 19:56:12.456").getTime());
        } catch (ParseException e) {
            fail("Could not parse date. This is a programming error in the " +
                    "test code");
            // This will never happens, but the compiler does not know that and
            // gives a warning otherwise.
            return null;
        }
        
        CustomerStatisticsKey key;
        CustomerInformation info;

        key = new CustomerStatisticsKey(SERVICE, METHOD + "1",
                ORIGIN_PREFIX + "1", "p1", MEDIA_PREFIX + "1",
                "c1", new Date(time1.getDate()));
        info = new CustomerInformation(key, time1.getDayOfWeek());
        info.registerCorrectCall();
        info.registerCorrectCall();
        info.registerCorrectCall();
        info.registerInvalidCall();
        info.registerInvalidCall();
        info.registerFailedCall();
        result.add(info);

        key = new CustomerStatisticsKey(SERVICE, METHOD + "1",
                ORIGIN_PREFIX + "1", "p1", MEDIA_PREFIX + "1",
                "c1", new Date(time2.getDate()));
        info = new CustomerInformation(key, time2.getDayOfWeek());
        info.registerCorrectCall();
        result.add(info);

        key = new CustomerStatisticsKey(null, METHOD,
                ORIGIN_PREFIX + "1", "p1", MEDIA_PREFIX + "1",
                "c1", new Date(time1.getDate()));
        info = new CustomerInformation(key, time1.getDayOfWeek());
        info.registerCorrectCall();
        result.add(info);

        key = new CustomerStatisticsKey(SERVICE, null,
                ORIGIN_PREFIX + "1", "p1", MEDIA_PREFIX + "1",
                "c1", new Date(time1.getDate()));
        info = new CustomerInformation(key, time1.getDayOfWeek());
        info.registerInvalidCall();
        result.add(info);

        key = new CustomerStatisticsKey(SERVICE, METHOD,
                null, "p1", MEDIA_PREFIX + "1",
                "c1", new Date(time1.getDate()));
        info = new CustomerInformation(key, time1.getDayOfWeek());
        info.registerFailedCall();
        result.add(info);

        key = new CustomerStatisticsKey(SERVICE, METHOD,
                ORIGIN_PREFIX + "1", null, MEDIA_PREFIX + "1",
                "c1", new Date(time2.getDate()));
        info = new CustomerInformation(key, time2.getDayOfWeek());
        info.registerCorrectCall();
        info.registerCorrectCall();
        info.registerInvalidCall();
        result.add(info);

        key = new CustomerStatisticsKey(SERVICE, METHOD,
                ORIGIN_PREFIX + "1", "p1", null,
                "c1", new Date(time2.getDate()));
        info = new CustomerInformation(key, time2.getDayOfWeek());
        info.registerInvalidCall();
        info.registerInvalidCall();
        info.registerFailedCall();
        result.add(info);

        key = new CustomerStatisticsKey(SERVICE, METHOD,
                ORIGIN_PREFIX + "1", "p1", MEDIA_PREFIX + "1",
                null, new Date(time2.getDate()));
        info = new CustomerInformation(key, time2.getDayOfWeek());
        info.registerFailedCall();
        info.registerFailedCall();
        info.registerCorrectCall();
        result.add(info);

        final String longProduct = "p23456789012345678901234567890";
        key = new CustomerStatisticsKey(SERVICE, METHOD,
                ORIGIN_PREFIX + "1", longProduct, MEDIA_PREFIX + "1",
                "c1", new Date(time2.getDate()));
        info = new CustomerInformation(key, time2.getDayOfWeek());
        info.registerCorrectCall();
        info.registerCorrectCall();
        info.registerCorrectCall();
        result.add(info);

        final String longCustomer = "c234567890123456789012345678901234567890" +
                "12345678901234567890";
        key = new CustomerStatisticsKey(SERVICE, METHOD,
                ORIGIN_PREFIX + "1", "p1", MEDIA_PREFIX + "1",
                longCustomer, new Date(time2.getDate()));
        info = new CustomerInformation(key, time2.getDayOfWeek());
        info.registerCorrectCall();
        info.registerCorrectCall();
        info.registerCorrectCall();
        result.add(info);

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
        CustomerCollector collector = new CustomerCollector();
        CustomerStatisticsKey key = new CustomerStatisticsKey(SERVICE, METHOD,
                ORIGIN_PREFIX + "1", "p1", MEDIA_PREFIX + "1", "c1",
                new java.util.Date());
        CustomerInformation info = new CustomerInformation(key, 0);
        assertFalse("updateInformation() with object not in db",
                BaseCollectorUtils.updateInformation(collector,
                getConnection(), info));
        cleanCustomerStatDb();
    }
}
