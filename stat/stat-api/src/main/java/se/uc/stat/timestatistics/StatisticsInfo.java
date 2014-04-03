package se.uc.stat.timestatistics;

import se.uc.stat.log.Log;

/**
 * Class holding information about an ongoing time measure.
 * 
 * @author Anders Persson (konx40)
 */
public class StatisticsInfo {
    /** Log instance for this class. */
    private final static Log log = Log.getLog(StatisticsInfo.class);
    /** The start time for the call. */
    private final long startTime = System.currentTimeMillis();
    /** The external time to remove from the call. */
    private long externalTime = 0;
    /** The name of the service or <code>null</code> if not specified. */
    private final String service;
    /** The name of the method or <code>null</code> if not specified. */
    private String method;
    /** The name of the layer or <code>null</code> if not specified. */
    private final String layer;
    /** The name of the origin or <code>null</code> if not specified. */
    private String origin;
    /** The name of the product or <code>null</code> if not specified. */
    private String product;
    /** The name of the media or <code>null</code> if not specified. */
    private String media;
    
    /**
     * Create this instance.
     * 
     * @param service The name of the service or
     *                <code>null</code> if not specified.
     * @param method  The name of the method or
     *                <code>null</code> if not specified.
     * @param layer   The name of the layer or
     *                <code>null</code> if not specified.
     */
    /* package */ StatisticsInfo(String service, String method, String layer) {
        this.service = service;
        this.method = method;
        this.layer = layer;
    }
    
    /**
     * Get the name of the service.
     * 
     * @return The name of the service or <code>null</code> if not specified.
     */
    /* package */ String getService() {
        return service;
    }

    /**
     * Set the method. This method gives a possibility to set a new method
     * after creation of this class.
     *
     * @param method The name of the method or
     *                <code>null</code> if not specified.
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Get the name of the method.
     * 
     * @return The name of the method or <code>null</code> if not specified.
     */
    /* package */ String getMethod() {
        return method;
    }
    
    /**
     * Get the name of the layer.
     * 
     * @return The name of the layer or <code>null</code> if not specified.
     */
    /* package */ String getLayer() {
        return layer;
    }
    
    /**
     * Set the name of the origin. <code>null</code> is default value.
     * Origin is especially used for services called from many front end
     * applications where origin can be used to differentiate the calls
     * from different front ends.
     * 
     * @param origin The name of the origin or <code>null</code>.
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    
    /**
     * Get the name of the origin.
     * Origin is especially used for services called from many front end
     * applications where origin can be used to differentiate the calls
     * from different front ends.
     * 
     * @return The name of the origin or <code>null</code> if not specified.
     */
    /* package */ String getOrigin() {
        return origin;
    }
    
    /**
     * Set the name of the product. <code>null</code> is default value.
     * Example of products can be the report type or template requested.
     * 
     * @param product The name of the product or <code>null</code>.
     */
    public void setProduct(String product) {
        this.product = product;
    }
    
    /**
     * Get the name of the product.
     * Example of products can be the report type or template requested.
     * 
     * @return The name of the product or <code>null</code> if not specified.
     */
    /* package */ String getProduct() {
        return product;
    }
    
    /**
     * Set the name of the media. <code>null</code> is default value.
     * Example of delivery medias are XML, HTML and a combination of them.
     * 
     * @param media The name of the media or <code>null</code>.
     */
    public void setMedia(String media) {
        this.media = media;
    }
    
    /**
     * Get the name of the media.
     * Example of delivery medias are XML, HTML and a combination of them.
     * 
     * @return The name of the media or <code>null</code> if not specified.
     */
    /* package */ String getMedia() {
        return media;
    }
    
    /**
     * Register external time to be removed. This method provides the
     * possibility to remove time for external calls from the execution time
     * to measure the time spent for internal execution.
     * 
     * @param time The external time to remove.
     */
    public void removeExternalTime(ExternalTime time) {
        externalTime += time.getTime();
    }

    /**
     * Calculate the time this call has been ongoing taking external time
     * into consideration.
     * 
     * @return The time in milliseconds.
     */
    private long calculateTime() {
        return System.currentTimeMillis() - startTime - externalTime;
    }
    
    /**
     * Register the stop of a correct call. Note that a stop method must only
     * be called once for each instance (otherwise the call will be registered
     * twice). 
     * 
     * @return The time spent on the call taking external time
     *         into consideration.
     */
    public ExternalTime stopCorrect() {
        final long time = calculateTime();
        try {
            TimeStatistics.registerCorrectServiceCall(service, method, origin,
                    product, media, layer, time);
        } catch (RuntimeException e) {
            log.error("Unexpected error when registering a correct call " +
                    "in the customer statistics", e, null);
            // This exception is not rethrown because this class should
            // never throw exception to the caller.
        }
        return new ExternalTime(time);
    }
    
    /**
     * Register the stop of an invalid call. Note that a stop method must only
     * be called once for each instance (otherwise the call will be registered
     * twice). 
     * 
     * @return The time spent on the call taking external time
     *         into consideration.
     */
    public ExternalTime stopInvalid() {
        final long time = calculateTime();
        try {
            TimeStatistics.registerInvalidServiceCall(service, method, origin,
                    product, media, layer, time);
        } catch (RuntimeException e) {
            log.error("Unexpected error when registering an invalid call " +
                    "in the customer statistics", e, null);
            // This exception is not rethrown because this class should
            // never throw exception to the caller.
        }
        return new ExternalTime(time);
    }

    /**
     * Register the stop of a failed call. Note that a stop method must only
     * be called once for each instance (otherwise the call will be registered
     * twice). 
     * 
     * @return The time spent on the call taking external time
     *         into consideration.
     */
    public ExternalTime stopFailed() {
        final long time = calculateTime();
        try {
            TimeStatistics.registerFailedServiceCall(service, method, origin,
                    product, media, layer, time);
        } catch (RuntimeException e) {
            log.error("Unexpected error when registering a failed call " +
                    "in the customer statistics", e, null);
            // This exception is not rethrown because this class should
            // never throw exception to the caller.
        }
        return new ExternalTime(time);
    }
}
