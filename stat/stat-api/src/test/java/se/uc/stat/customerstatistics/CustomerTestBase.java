package se.uc.stat.customerstatistics;

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
 * Base class with common methods for CustomerCollectorTest and
 * CustomerStatisticsTest.
 * 
 * @author Anders Persson (konx40)
 */
public class CustomerTestBase extends AbstractTestBase {
    /** The name of the test service. */
    protected final static String SERVICE = "statTestService";
    /** The name of the test method. */
    protected final static String METHOD = "statTestMethod";
    /** Prefix of the origins to use. */
    protected final static String ORIGIN_PREFIX = "statTestOrigin";
    /** Prefix of the medias to use. */
    protected final static String MEDIA_PREFIX = "statTestMedia";

    /**
     * Delete all records in the CUSTOMER_STAT table coupled to the
     * service {@link #SERVICE} or method {@link #METHOD}.
     * Delete all records in the METHOD_INFO table coupled to the
     * service {@link #SERVICE} or method {@link #METHOD}.
     * Delete all records in the ORIGIN_INFO table starting with
     * {@link #ORIGIN_PREFIX}.
     * Delete all records in the MEDIA_INFO table starting with
     * {@link #MEDIA_PREFIX}.
     * 
     * @throws SQLException if the call fails.
     */
    protected void cleanCustomerStatDb() throws SQLException {
        String SQL = "delete CUSTOMER_STAT where METHOD_ID in " +
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
    protected void assertInfoInDb(String testCase, CustomerInformation info)
            throws SQLException {
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            final String sql = "select DAY_OF_WEEK_ID, NUM_CORRECT_CALLS, " +
                    "NUM_INVALID_CALLS, NUM_FAILED_CALLS " +
                    "from CUSTOMER_STAT where " +
                    "METHOD_ID = ? and ORIGIN_ID = ? and MEDIA_ID = ? " +
                    "and PRODUCT = ? and customer = ? and STATISTICS_DATE = ?";
            statement = getConnection().prepareStatement(sql);
            final CustomerStatisticsKey key = info.getKey();
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
            String customer = key.getCustomer();
            if (customer == null) {
                customer = BaseDimension.NULL_NAME;
            } else {
                customer = customer.trim();
            }
            statement.setString(5, DatabaseUtils.pad(customer, 50));
            statement.setTimestamp(6, new Timestamp(key.getDate().getTime()));
            result = statement.executeQuery();
            if (result.next()) {
                assertEquals(testCase + "DAY_OF_WEEK_ID",
                        info.getDayOfWeek(), result.getInt(1));
                assertEquals(testCase + "NUM_CORRECT_CALLS",
                        info.getNumCorrectCalls(), result.getInt(2));
                assertEquals(testCase + "NUM_INVALID_CALLS",
                        info.getNumInvalidCalls(), result.getInt(3));
                assertEquals(testCase + "NUM_FAILED_CALLS",
                        info.getNumFailedCalls(), result.getInt(4));
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
