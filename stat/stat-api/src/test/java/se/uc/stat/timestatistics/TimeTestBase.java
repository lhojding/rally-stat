package se.uc.stat.timestatistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import se.uc.stat.dimension.BaseDimension;
import se.uc.stat.dimension.Dimensions;
import se.uc.stat.dimension.MethodKey;
import se.uc.stat.utils.AbstractTestBase;
import se.uc.stat.utils.DatabaseUtils;

/**
 * Base class with common methods for TimeCollectorTest and
 * TimeStatisticsTest.
 * 
 * @author Anders Persson (konx40)
 */
public class TimeTestBase extends AbstractTestBase {
    /** The name of the test service. */
    protected final static String SERVICE = "statTestService";
    /** The name of the test method. */
    protected final static String METHOD = "statTestMethod";
    /** Prefix of the origins to use. */
    protected final static String ORIGIN_PREFIX = "statTestOrigin";
    /** Prefix of the medias to use. */
    protected final static String MEDIA_PREFIX = "statTestMedia";
    /** Prefix of the layer to use. */
    protected final static String LAYER_PREFIX = "statTestLayer";

    /**
     * Delete all records in the TIME_STAT table coupled to the
     * service {@link #SERVICE} or method {@link #METHOD}.
     * Delete all records in the METHOD_INFO table coupled to the
     * service {@link #SERVICE} or method {@link #METHOD}.
     * Delete all records in the ORIGIN_INFO table starting with
     * {@link #ORIGIN_PREFIX}.
     * Delete all records in the MEDIA_INFO table starting with
     * {@link #MEDIA_PREFIX}.
     * Delete all records in the LAYER_INFO table starting with
     * {@link #LAYER_PREFIX}.
     * 
     * @throws SQLException if the call fails.
     */
    protected void cleanCustomerStatDb() throws SQLException {
        String SQL = "delete TIME_STAT where METHOD_ID in " +
                "(select METHOD_ID from METHOD_INFO " +
                "where SERVICE_NAME = '" + SERVICE + "' or " +
                "METHOD_NAME = '" + METHOD + "')";
        updateInDb(SQL);
        SQL = "delete METHOD_INFO " +
                "where SERVICE_NAME = '" + SERVICE + "' or " +
                "METHOD_NAME = '" + METHOD + "'";
        updateInDb(SQL);
        SQL = "delete ORIGIN_INFO " +
                "where ORIGIN_NAME like '" + ORIGIN_PREFIX + "%'";
        updateInDb(SQL);
        SQL = "delete MEDIA_INFO " +
                "where MEDIA_NAME like '" + MEDIA_PREFIX + "%'";
        updateInDb(SQL);
        SQL = "delete LAYER_INFO " +
                "where LAYER_NAME like '" + LAYER_PREFIX + "%'";
        updateInDb(SQL);
        Dimensions.clear();
    }

    /**
     * Assert that the correct information is stored in the database.
     * 
     * @param testCase The name of the test case.
     * @param info     The information to check to.
     * 
     * @throws SQLException if the test fails.
     */
    protected void assertInfoInDb(String testCase, TimeInformation info)
            throws SQLException {
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            final String sql = "select DAY_OF_WEEK_ID, HOUR_OF_DAY, " +
                    "NUM_CORRECT_CALLS, NUM_INVALID_CALLS, NUM_FAILED_CALLS, " +
                    "TOTAL_TIME_CORRECT_CALLS, TOTAL_TIME_INVALID_CALLS, " +
                    "TOTAL_TIME_FAILED_CALLS, " +
                    "NUM_10, NUM_20, NUM_50, " +
                    "NUM_100, NUM_200, NUM_500, " +
                    "NUM_1000, NUM_2000, NUM_5000, " +
                    "NUM_10000, NUM_20000, NUM_OVER_20000 " +
                    "from TIME_STAT where " +
                    "METHOD_ID = ? and ORIGIN_ID = ? and MEDIA_ID = ? " +
                    "and PRODUCT = ? and LAYER_ID = ? and STATISTICS_TIME = ?";
            statement = getConnection().prepareStatement(sql);
            final TimeStatisticsKey key = info.getKey();
            final MethodKey methodKey = new MethodKey(key.getService(),
                    key.getMethod());
            statement.setInt(1, Dimensions.getMethodDimension().getId(
                    methodKey));
            statement.setInt(2, Dimensions.getOriginDimension().getId(
                    key.getOrigin()));
            statement.setInt(3, Dimensions.getMediaDimension().getId(
                    key.getMedia()));
            String product = key.getProduct();
            if (product == null) {
                product = BaseDimension.NULL_NAME;
            } else {
                product = product.trim();
            }
            statement.setString(4, DatabaseUtils.pad(product, 20));
            statement.setInt(5, Dimensions.getLayerDimension().getId(
                    key.getLayer()));
            statement.setTimestamp(6, new Timestamp(key.getTime().getTime()));
            result = statement.executeQuery();
            if (result.next()) {
                assertEquals(testCase + "DAY_OF_WEEK_ID",
                        info.getDayOfWeek(), result.getInt(1));
                assertEquals(testCase + "HOUR_OF_DAY",
                        info.getHourOfDay(), result.getInt(2));
                assertEquals(testCase + "NUM_CORRECT_CALLS",
                        info.getNumCorrectCalls(), result.getInt(3));
                assertEquals(testCase + "NUM_INVALID_CALLS",
                        info.getNumInvalidCalls(), result.getInt(4));
                assertEquals(testCase + "NUM_FAILED_CALLS",
                        info.getNumFailedCalls(), result.getInt(5));
                assertEquals(testCase + "TOTAL_TIME_CORRECT_CALLS",
                        info.getTotalTimeCorrectCalls(), result.getLong(6));
                assertEquals(testCase + "TOTAL_TIME_INVALID_CALLS",
                        info.getTotalTimeInvalidCalls(), result.getLong(7));
                assertEquals(testCase + "TOTAL_TIME_FAILED_CALLS",
                        info.getTotalTimeFailedCalls(), result.getLong(8));
                assertEquals(testCase + "NUM_10",
                        info.getNum10(), result.getInt(9));
                assertEquals(testCase + "NUM_20",
                        info.getNum20(), result.getInt(10));
                assertEquals(testCase + "NUM_50",
                        info.getNum50(), result.getInt(11));
                assertEquals(testCase + "NUM_100",
                        info.getNum100(), result.getInt(12));
                assertEquals(testCase + "NUM_200",
                        info.getNum200(), result.getInt(13));
                assertEquals(testCase + "NUM_500",
                        info.getNum500(), result.getInt(14));
                assertEquals(testCase + "NUM_1000",
                        info.getNum1000(), result.getInt(15));
                assertEquals(testCase + "NUM_2000",
                        info.getNum2000(), result.getInt(16));
                assertEquals(testCase + "NUM_5000",
                        info.getNum5000(), result.getInt(17));
                assertEquals(testCase + "NUM_10000",
                        info.getNum10000(), result.getInt(18));
                assertEquals(testCase + "NUM_20000",
                        info.getNum20000(), result.getInt(19));
                assertEquals(testCase + "NUM_OVER_20000",
                        info.getNumOver20000(), result.getInt(20));
                if (result.next()) {
                    fail(testCase + "The query returned more than one row");
                }
            } else {
                fail(testCase + "The query did not return any row");
            }
        } finally {
            DatabaseUtils.close(null, statement, result);
        }
    }
}
