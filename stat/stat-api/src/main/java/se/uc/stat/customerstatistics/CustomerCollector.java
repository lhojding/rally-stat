package se.uc.stat.customerstatistics;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import se.uc.stat.basestatistics.BaseCollector;
import se.uc.stat.dimension.BaseDimension;
import se.uc.stat.dimension.Dimensions;
import se.uc.stat.dimension.MethodKey;
import se.uc.stat.utils.DatabaseUtils;
import se.uc.stat.utils.TimeRepresentation;
import se.uc.stat.utils.TimeUtils;

/**
 * Class collecting customer statistics.
 * 
 * @author Anders Persson (konx40)
 */
/* package */ class CustomerCollector extends BaseCollector<
        CustomerStatisticsKey, CustomerInformation> {
    /**
     * The time interval for storing information. This is the time in
     * milliseconds between the last information object is stored and the
     * next start to store information.
     */
    private final static long STORE_INTERVAL = 60 * 1000;
    
    /**
     * The maximum number of objects a single thread may have to store
     * before it is released.
     */
    private final static int MAX_NUMBER_OF_STORAGES = 3;
    
    /**
     * The SQL to update the record in the database table.
     * 
     * param 1: Number of correct calls to add.
     * param 2: Number of invalid calls to add.
     * param 3: Number of failed calls to add.
     * param 4: Method id to search for (reference to METHOD_INFO).
     * param 5: Origin id to search for (reference to ORIGIN_INFO).
     * param 6: Media id to search for (reference to MEDIA_INFO).
     * param 7: Product.
     * param 8: Customer.
     * param 9: Date with hours, minutes, seconds and milliseconds set to 0.
     */
    private final static String UPDATE_SQL = "update CUSTOMER_STAT " +
    		"set NUM_CORRECT_CALLS = NUM_CORRECT_CALLS + ?, " +
    		"NUM_INVALID_CALLS = NUM_INVALID_CALLS + ?, " +
    		"NUM_FAILED_CALLS = NUM_FAILED_CALLS + ? " +
    		"where METHOD_ID = ? and ORIGIN_ID = ? " +
    		"and MEDIA_ID = ? and PRODUCT = ? and CUSTOMER = ? " +
    		"and STATISTICS_DATE = ?";
    
    /**
     * The SQL to insert the record in the database table.
     * 
     * param 1: Method id (reference to METHOD_INFO).
     * param 2: Origin id (reference to ORIGIN_INFO).
     * param 3: Media id (reference to MEDIA_INFO).
     * param 4: Product.
     * param 5: Customer.
     * param 6: Date with hours, minutes, seconds and milliseconds set to 0.
     * param 7: Day of week id (reference to DAY_OF_WEEK).
     * param 8: Number of correct calls.
     * param 9: Number of invalid calls.
     * param 10: Number of failed calls.
     */
    private final static String INSERT_SQL = "insert into CUSTOMER_STAT" +
    		"(METHOD_ID, ORIGIN_ID, MEDIA_ID, PRODUCT, CUSTOMER, " +
    		"STATISTICS_DATE, DAY_OF_WEEK_ID, " +
    		"NUM_CORRECT_CALLS, NUM_INVALID_CALLS, NUM_FAILED_CALLS) " +
    		"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    /**
     * Package constructor to prevent instantiation outside of the package.
     */
    /* package */ CustomerCollector() {
        super(STORE_INTERVAL, MAX_NUMBER_OF_STORAGES, UPDATE_SQL, INSERT_SQL);
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
    /* package */ void registerCorrectServiceCall(String service, String method,
            String origin, String product, String media, String customer) {
        final TimeRepresentation date = TimeUtils.getTimeRepresentation(
                System.currentTimeMillis());
        final CustomerStatisticsKey key = new CustomerStatisticsKey(service,
                method, origin, product, media, customer,
                new Date(date.getDate()));
        synchronized(cacheLock) {
            CustomerInformation info = get(key);
            if (info == null) {
                info = new CustomerInformation(key, date.getDayOfWeek());
                put(info);
            }
            info.registerCorrectCall();
        }
        allowStore();
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
    /* package */ void registerInvalidServiceCall(String service, String method,
            String origin, String product, String media, String customer) {
        final TimeRepresentation date = TimeUtils.getTimeRepresentation(
                System.currentTimeMillis());
        final CustomerStatisticsKey key = new CustomerStatisticsKey(service,
                method, origin, product, media, customer,
                new Date(date.getDate()));
        synchronized(cacheLock) {
            CustomerInformation info = get(key);
            if (info == null) {
                info = new CustomerInformation(key, date.getDayOfWeek());
                put(info);
            }
            info.registerInvalidCall();
        }
        allowStore();
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
    /* package */ void registerFailedServiceCall(String service, String method,
            String origin, String product, String media, String customer) {
        final TimeRepresentation date = TimeUtils.getTimeRepresentation(
                System.currentTimeMillis());
        final CustomerStatisticsKey key = new CustomerStatisticsKey(service,
                method, origin, product, media, customer,
                new Date(date.getDate()));
        synchronized(cacheLock) {
            CustomerInformation info = get(key);
            if (info == null) {
                info = new CustomerInformation(key, date.getDayOfWeek());
                put(info);
            }
            info.registerFailedCall();
        }
        allowStore();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    protected void populateInsert(PreparedStatement ps,
            CustomerInformation info) throws SQLException {
        final CustomerStatisticsKey key = info.getKey();
        final MethodKey methodKey =
                new MethodKey(key.getService(), key.getMethod());
        final int methodId =
            Dimensions.getMethodDimension().getId(methodKey);
        final int originId =
            Dimensions.getOriginDimension().getId(key.getOrigin());
        final int mediaId =
            Dimensions.getMediaDimension().getId(key.getMedia());
        ps.setInt(1, methodId);
        ps.setInt(2, originId);
        ps.setInt(3, mediaId);
        ps.setString(4, prepareProduct(key.getProduct()));
        ps.setString(5, prepareCustomer(key.getCustomer()));
        ps.setTimestamp(6, new Timestamp(key.getDate().getTime()));
        ps.setInt(7, info.getDayOfWeek());
        ps.setInt(8, info.getNumCorrectCalls());
        ps.setInt(9, info.getNumInvalidCalls());
        ps.setInt(10, info.getNumFailedCalls());
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    protected void populateUpdate(PreparedStatement ps,
            CustomerInformation info) throws SQLException {
        final CustomerStatisticsKey key = info.getKey();
        final MethodKey methodKey =
                new MethodKey(key.getService(), key.getMethod());
        final int methodId =
            Dimensions.getMethodDimension().getId(methodKey);
        final int originId =
            Dimensions.getOriginDimension().getId(key.getOrigin());
        final int mediaId =
            Dimensions.getMediaDimension().getId(key.getMedia());
        final String product20 = DatabaseUtils.pad(
                prepareProduct(key.getProduct()), 20);
        final String customer50 = DatabaseUtils.pad(
                prepareCustomer(key.getCustomer()), 50);
        ps.setInt(1, info.getNumCorrectCalls());
        ps.setInt(2, info.getNumInvalidCalls());
        ps.setInt(3, info.getNumFailedCalls());
        ps.setInt(4, methodId);
        ps.setInt(5, originId);
        ps.setInt(6, mediaId);
        ps.setString(7, product20);
        ps.setString(8, customer50);
        ps.setTimestamp(9, new Timestamp(key.getDate().getTime()));
    }

    /**
     * Prepare the product taking care of <code>null</code> name,
     * to long names and so on.
     * 
     * @param product The name to prepare.
     * 
     * @return The prepared name.
     */
    private String prepareProduct(String product) {
        if (product == null) {
            return BaseDimension.NULL_NAME;
        }
        final String trimmed = product.trim();
        if (trimmed.length() == 0) {
            return BaseDimension.NULL_NAME;
        }
        if (trimmed.length() > 20) {
            return trimmed.substring(0, 20);
        }
        return trimmed;
    }

    /**
     * Prepare the customer taking care of <code>null</code> name,
     * to long names and so on.
     * 
     * @param customer The name to prepare.
     * 
     * @return The prepared name.
     */
    private String prepareCustomer(String customer) {
        if (customer == null) {
            return BaseDimension.NULL_NAME;
        }
        final String trimmed = customer.trim();
        if (trimmed.length() == 0) {
            return BaseDimension.NULL_NAME;
        }
        if (trimmed.length() > 50) {
            return trimmed.substring(0, 50);
        }
        return trimmed;
    }
}
