package se.uc.stat.customerstatistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import se.uc.stat.timestatistics.StatisticsInfo;
import se.uc.stat.timestatistics.TimeStatistics;
import se.uc.stat.utils.DatabaseUtils;
import se.uc.stat.log.Log;

/**
 * The class used to register customer statistics.
 * 
 * @author Anders Persson (konx40)
 */
public class CustomerStatistics {
    /** The collector instance to use. */
    private final static CustomerCollector collector = new CustomerCollector();
    /** The log instance for this class. */
    private final static Log log = Log.getLog(CustomerStatistics.class);
    
    /**
     * Private constructor to prevent instantiation.
     */
    private CustomerStatistics() {
        // Nothing to do.
    }
    
    /**
     * Register a correct service call in the customer statistics.
     * 
     * @param service  The service called. May be <code>null</code> from a
     *                 technical perspective, but from a business perspective,
     *                 there should always exist a value for service.
     * @param method   The method called. May be <code>null</code> from a
     *                 technical perspective, but from a business perspective,
     *                 there should in most cases exist a value for method.
     * @param origin   The origin for the call, if it is known, or
     *                 <code>null</code> if the origin is unknown or not of
     *                 interest. Origin is especially used for services called
     *                 from many front end applications where origin can be
     *                 used to differentiate the calls from different
     *                 front ends.
     * @param product  The product requested or <code>null</code> if it is not
     *                 of interest. Example of products can be the report type
     *                 or template requested.
     * @param media    The delivery media or <code>null</code> if not of
     *                 interest. Example of delivery medias are XML, HTML
     *                 and a combination of them.
     * @param customer The customer performing the call or <code>null</code>
     *                 if it is not of interest. The field is intended for
     *                 a customer number or organizational number.
     */
    public static void registerCorrectServiceCall(String service, String method,
            String origin, String product, String media, String customer) {
        try {
            collector.registerCorrectServiceCall(service, method, origin, product,
                    media, customer);
        } catch (RuntimeException e) {
            log.error("Unexpected error when registering a correct call " +
                    "in the customer statistics", e, null);
            // This exception is not rethrown because this class should
            // never throw exception to the caller.
        }
    }

    /**
     * Register an invalid service call in the customer statistics. An invalid
     * service call is a call failing due to invalid input. As a result,
     * it is <strong>not</strong> a technical error.
     * 
     * @param service  The service called. May be <code>null</code> from a
     *                 technical perspective, but from a business perspective,
     *                 there should always exist a value for service.
     * @param method   The method called. May be <code>null</code> from a
     *                 technical perspective, but from a business perspective,
     *                 there should in most cases exist a value for method.
     * @param origin   The origin for the call, if it is known, or
     *                 <code>null</code> if the origin is unknown or not of
     *                 interest. Origin is especially used for services called
     *                 from many front end applications where origin can be
     *                 used to differentiate the calls from different
     *                 front ends.
     * @param product  The product requested or <code>null</code> if it is not
     *                 of interest. Example of products can be the report type
     *                 or template requested.
     * @param media    The delivery media or <code>null</code> if not of
     *                 interest. Example of delivery medias are XML, HTML
     *                 and a combination of them.
     * @param customer The customer performing the call or <code>null</code>
     *                 if it is not of interest. The field is intended for
     *                 a customer number or organizational number.
     */
    public static void registerInvalidServiceCall(String service, String method,
            String origin, String product, String media, String customer) {
        try {
            collector.registerInvalidServiceCall(service, method, origin, product,
                    media, customer);
        } catch (RuntimeException e) {
            log.error("Unexpected error when registering an invalid call " +
                    "in the customer statistics", e, null);
            // This exception is not rethrown because this class should
            // never throw exception to the caller.
        }
    }

    /**
     * Register a failed service call in the customer statistics. A failed
     * service call fails due to a technical errors such as programming
     * errors or environment errors (for example the database does not respond).
     * 
     * @param service  The service called. May be <code>null</code> from a
     *                 technical perspective, but from a business perspective,
     *                 there should always exist a value for service.
     * @param method   The method called. May be <code>null</code> from a
     *                 technical perspective, but from a business perspective,
     *                 there should in most cases exist a value for method.
     * @param origin   The origin for the call, if it is known, or
     *                 <code>null</code> if the origin is unknown or not of
     *                 interest. Origin is especially used for services called
     *                 from many front end applications where origin can be
     *                 used to differentiate the calls from different
     *                 front ends.
     * @param product  The product requested or <code>null</code> if it is not
     *                 of interest. Example of products can be the report type
     *                 or template requested.
     * @param media    The delivery media or <code>null</code> if not of
     *                 interest. Example of delivery medias are XML, HTML
     *                 and a combination of them.
     * @param customer The customer performing the call or <code>null</code>
     *                 if it is not of interest. The field is intended for
     *                 a customer number or organizational number.
     */
    public static void registerFailedServiceCall(String service, String method,
            String origin, String product, String media, String customer) {
        try {
            collector.registerFailedServiceCall(service, method, origin, product,
                    media, customer);
        } catch (RuntimeException e) {
            log.error("Unexpected error when registering a failed call " +
                    "in the customer statistics", e, null);
            // This exception is not rethrown because this class should
            // never throw exception to the caller.
        }
    }

    /**
     * Perform a flush of the cache. This means that all information in the
     * cache is stored in the persistent store.
     * <p/>
     * This method is not intended to be called during normal circumstances.
     * It can however be a good idea to call this method when an application
     * is shut down to store the last information in the cache persistently.
     * <p/>
     * This method will perform the whole work and will not return until
     * the information is stored. Observe that information registered while
     * this method is performing will not be stored persistent but of course
     * reside in the cache.
     */
    public static void flush() {
        collector.flush();
    }
    
    /**
     * Perform a health check of the application. Call the database.
     * 
     * @throws SQLException if the check failed.
     */
    public static void healthCheck() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        StatisticsInfo statistics = TimeStatistics.start(
                CustomerStatistics.class.getName(), "healthCheck", null);
        try {
            connection = DatabaseUtils.getConnection();
            statement = connection.prepareStatement("select * from dual");
            result = statement.executeQuery();
            statistics.stopCorrect();
        } catch (SQLException e) {
            statistics.stopFailed();
            throw e;
        } catch (RuntimeException e) {
            statistics.stopFailed();
            throw e;
        } finally {
            DatabaseUtils.close(connection, statement, result);
        }
    }
}
