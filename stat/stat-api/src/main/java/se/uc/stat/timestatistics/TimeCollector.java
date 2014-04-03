package se.uc.stat.timestatistics;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;

import se.uc.stat.basestatistics.BaseCollector;
import se.uc.stat.dimension.BaseDimension;
import se.uc.stat.dimension.Dimensions;
import se.uc.stat.dimension.MethodKey;
import se.uc.stat.utils.DatabaseUtils;
import se.uc.stat.utils.TimeRepresentation;
import se.uc.stat.utils.TimeUtils;

/**
 * Class collecting time statistics.
 * 
 * @author Anders Persson (konx40)
 */
/* package */ class TimeCollector extends BaseCollector<
        TimeStatisticsKey, TimeInformation> {
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
     * param 4: Total time for correct calls to add.
     * param 5: Total time for invalid calls to add.
     * param 6: Total time for failed calls to add.
     * param 7: The number of calls in interval to 10 ms to add.
     * param 8: The number of calls in interval to 20 ms to add.
     * param 9: The number of calls in interval to 50 ms to add.
     * param 10: The number of calls in interval to 100 ms to add.
     * param 11: The number of calls in interval to 200 ms to add.
     * param 12: The number of calls in interval to 500 ms to add.
     * param 13: The number of calls in interval to 1000 ms to add.
     * param 14: The number of calls in interval to 2000 ms to add.
     * param 15: The number of calls in interval to 5000 ms to add.
     * param 16: The number of calls in interval to 10000 ms to add.
     * param 17: The number of calls in interval to 20000 ms to add.
     * param 18: The number of calls in interval over 20000 ms to add.
     * param 19: Method id to search for (reference to METHOD_INFO).
     * param 20: Origin id to search for (reference to ORIGIN_INFO).
     * param 21: Media id to search for (reference to MEDIA_INFO).
     * param 22: Layer id to search for (reference to LAYER_INFO).
     * param 23: Product.
     * param 24: Date and hour with minutes, seconds and milliseconds set to 0.
     */
    private final static String UPDATE_SQL = "update TIME_STAT set " +
            "NUM_CORRECT_CALLS = NUM_CORRECT_CALLS + ?, " +
            "NUM_INVALID_CALLS = NUM_INVALID_CALLS + ?, " +
            "NUM_FAILED_CALLS = NUM_FAILED_CALLS + ?, " +
            "TOTAL_TIME_CORRECT_CALLS = TOTAL_TIME_CORRECT_CALLS + ?, " +
            "TOTAL_TIME_INVALID_CALLS = TOTAL_TIME_INVALID_CALLS + ?, " +
            "TOTAL_TIME_FAILED_CALLS = TOTAL_TIME_FAILED_CALLS + ?, " +
            "NUM_10 = NUM_10 + ?, " +
            "NUM_20 = NUM_20 + ?, " +
            "NUM_50 = NUM_50 + ?, " +
            "NUM_100 = NUM_100 + ?, " +
            "NUM_200 = NUM_200 + ?, " +
            "NUM_500 = NUM_500 + ?, " +
            "NUM_1000 = NUM_1000 + ?, " +
            "NUM_2000 = NUM_2000 + ?, " +
            "NUM_5000 = NUM_5000 + ?, " +
            "NUM_10000 = NUM_10000 + ?, " +
            "NUM_20000 = NUM_20000 + ?, " +
            "NUM_OVER_20000 = NUM_OVER_20000 + ? " +
            "where METHOD_ID = ? and ORIGIN_ID = ? and " +
            "MEDIA_ID = ? and LAYER_ID = ? and " +
            "PRODUCT = ? and STATISTICS_TIME = ?";
    
    /**
     * The SQL to insert the record in the database table.
     * 
     * param 1: Method id (reference to METHOD_INFO).
     * param 2: Origin id (reference to ORIGIN_INFO).
     * param 3: Media id (reference to MEDIA_INFO).
     * param 4: Layer id (reference to LAYER_INFO).
     * param 5: Product.
     * param 6: Date and hour with minutes, seconds and milliseconds set to 0.
     * param 7: Day of week id (reference to DAY_OF_WEEK).
     * param 8: The hour of the day.
     * param 9: Number of correct calls.
     * param 10: Number of invalid calls.
     * param 11: Number of failed calls.
     * param 12: Total time for correct calls.
     * param 13: Total time for incorrect calls.
     * param 14: Total time for failed calls.
     * param 15: The number of calls in interval to 10 ms
     * param 16: The number of calls in interval to 20 ms
     * param 17: The number of calls in interval to 50 ms
     * param 18: The number of calls in interval to 100 ms
     * param 19: The number of calls in interval to 200 ms
     * param 20: The number of calls in interval to 500 ms
     * param 21: The number of calls in interval to 1000 ms
     * param 22: The number of calls in interval to 2000 ms
     * param 23: The number of calls in interval to 5000 ms
     * param 24: The number of calls in interval to 10000 ms
     * param 25: The number of calls in interval to 20000 ms
     * param 26: The number of calls in interval over 20000 ms
     */
    private final static String INSERT_SQL = "insert into TIME_STAT(METHOD_ID," +
            "ORIGIN_ID, MEDIA_ID, LAYER_ID, PRODUCT, STATISTICS_TIME," +
            "DAY_OF_WEEK_ID, HOUR_OF_DAY, NUM_CORRECT_CALLS, " +
            "NUM_INVALID_CALLS, NUM_FAILED_CALLS, TOTAL_TIME_CORRECT_CALLS, " +
            "TOTAL_TIME_INVALID_CALLS, TOTAL_TIME_FAILED_CALLS, " +
            "NUM_10, NUM_20, NUM_50, NUM_100, NUM_200, NUM_500, NUM_1000, " +
            "NUM_2000, NUM_5000, NUM_10000, NUM_20000, NUM_OVER_20000) " +
            "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
            "?, ?, ?, ?, ?, ?, ?, ?)";
    
    /**
     * Package constructor to prevent instantiation outside of the package.
     */
    /* package */ TimeCollector() {
        super(STORE_INTERVAL, MAX_NUMBER_OF_STORAGES, UPDATE_SQL, INSERT_SQL);
    }
    
    /**
     * Register a correct service call in the time statistics.
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
     * @param layer    The name of the layer within the architecture or
     *                 <code>null</code> if not relevant.
     * @param callTime The time in milliseconds the call took.
     */
    /* package */ void registerCorrectServiceCall(String service, String method,
            String origin, String product, String media, String layer,
            long callTime) {
        final TimeRepresentation date = TimeUtils.getTimeRepresentation(
                System.currentTimeMillis());
        final TimeStatisticsKey key = new TimeStatisticsKey(service,
                method, origin, product, media, layer,
                new Date(date.getDateHour()));
        synchronized(cacheLock) {
            TimeInformation info = get(key);
            if (info == null) {
                info = new TimeInformation(key, date.getDayOfWeek(),
                        date.getHourOfDay());
                put(info);
            }
            info.registerCorrectCall(callTime);
        }
        allowStore();
    }

    /**
     * Register an invalid service call in the time statistics. An invalid
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
     * @param layer    The name of the layer within the architecture or
     *                 <code>null</code> if not relevant.
     * @param callTime The time in milliseconds the call took.
     */
    /* package */ void registerInvalidServiceCall(String service, String method,
            String origin, String product, String media, String layer,
            long callTime) {
        final TimeRepresentation date = TimeUtils.getTimeRepresentation(
                System.currentTimeMillis());
        final TimeStatisticsKey key = new TimeStatisticsKey(service,
                method, origin, product, media, layer,
                new Date(date.getDateHour()));
        synchronized(cacheLock) {
            TimeInformation info = get(key);
            if (info == null) {
                info = new TimeInformation(key, date.getDayOfWeek(),
                        date.getHourOfDay());
                put(info);
            }
            info.registerInvalidCall(callTime);
        }
        allowStore();
    }

    /**
     * Register a failed service call in the time statistics. A failed
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
     * @param layer    The name of the layer within the architecture or
     *                 <code>null</code> if not relevant.
     * @param callTime The time in milliseconds the call took.
     */
    /* package */ void registerFailedServiceCall(String service, String method,
            String origin, String product, String media, String layer,
            long callTime) {
        final TimeRepresentation date = TimeUtils.getTimeRepresentation(
                System.currentTimeMillis());
        final TimeStatisticsKey key = new TimeStatisticsKey(service,
                method, origin, product, media, layer,
                new Date(date.getDateHour()));
        synchronized(cacheLock) {
            TimeInformation info = get(key);
            if (info == null) {
                info = new TimeInformation(key, date.getDayOfWeek(),
                        date.getHourOfDay());
                put(info);
            }
            info.registerFailedCall(callTime);
        }
        allowStore();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    protected void populateInsert(PreparedStatement ps,
            TimeInformation info) throws SQLException {
        final TimeStatisticsKey key = info.getKey();
        final MethodKey methodKey =
                new MethodKey(key.getService(), key.getMethod());
        final int methodId =
            Dimensions.getMethodDimension().getId(methodKey);
        final int originId =
            Dimensions.getOriginDimension().getId(key.getOrigin());
        final int mediaId =
            Dimensions.getMediaDimension().getId(key.getMedia());
        final int layerId =
            Dimensions.getLayerDimension().getId(key.getLayer());
        ps.setInt(1, methodId);
        ps.setInt(2, originId);
        ps.setInt(3, mediaId);
        ps.setInt(4, layerId);
        ps.setString(5, prepareProduct(key.getProduct()));
        ps.setTimestamp(6, new Timestamp(key.getTime().getTime()));
        ps.setInt(7, info.getDayOfWeek());
        ps.setInt(8, info.getHourOfDay());
        ps.setInt(9, info.getNumCorrectCalls());
        ps.setInt(10, info.getNumInvalidCalls());
        ps.setInt(11, info.getNumFailedCalls());
        ps.setLong(12, info.getTotalTimeCorrectCalls());
        ps.setLong(13, info.getTotalTimeInvalidCalls());
        ps.setLong(14, info.getTotalTimeFailedCalls());
        ps.setInt(15, info.getNum10());
        ps.setInt(16, info.getNum20());
        ps.setInt(17, info.getNum50());
        ps.setInt(18, info.getNum100());
        ps.setInt(19, info.getNum200());
        ps.setInt(20, info.getNum500());
        ps.setInt(21, info.getNum1000());
        ps.setInt(22, info.getNum2000());
        ps.setInt(23, info.getNum5000());
        ps.setInt(24, info.getNum10000());
        ps.setInt(25, info.getNum20000());
        ps.setInt(26, info.getNumOver20000());
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    protected void populateUpdate(PreparedStatement ps,
            TimeInformation info) throws SQLException {
        final TimeStatisticsKey key = info.getKey();
        final MethodKey methodKey =
                new MethodKey(key.getService(), key.getMethod());
        final int methodId =
            Dimensions.getMethodDimension().getId(methodKey);
        final int originId =
            Dimensions.getOriginDimension().getId(key.getOrigin());
        final int mediaId =
            Dimensions.getMediaDimension().getId(key.getMedia());
        final int layerId =
            Dimensions.getLayerDimension().getId(key.getLayer());
        final String product20 = DatabaseUtils.pad(
                prepareProduct(key.getProduct()), 20);
        ps.setInt(1, info.getNumCorrectCalls());
        ps.setInt(2, info.getNumInvalidCalls());
        ps.setInt(3, info.getNumFailedCalls());
        ps.setLong(4, info.getTotalTimeCorrectCalls());
        ps.setLong(5, info.getTotalTimeInvalidCalls());
        ps.setLong(6, info.getTotalTimeFailedCalls());
        ps.setInt(7, info.getNum10());
        ps.setInt(8, info.getNum20());
        ps.setInt(9, info.getNum50());
        ps.setInt(10, info.getNum100());
        ps.setInt(11, info.getNum200());
        ps.setInt(12, info.getNum500());
        ps.setInt(13, info.getNum1000());
        ps.setInt(14, info.getNum2000());
        ps.setInt(15, info.getNum5000());
        ps.setInt(16, info.getNum10000());
        ps.setInt(17, info.getNum20000());
        ps.setInt(18, info.getNumOver20000());
        ps.setInt(19, methodId);
        ps.setInt(20, originId);
        ps.setInt(21, mediaId);
        ps.setInt(22, layerId);
        ps.setString(23, product20);
        ps.setTimestamp(24, new Timestamp(key.getTime().getTime()));
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
}
