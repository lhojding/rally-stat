package se.uc.stat.timestatistics;

import java.util.Date;

import se.uc.stat.basestatistics.BaseKey;

/**
 * The key for the time statistics.
 * 
 * @author Anders Persson (konx40)
 */
/* package */ class TimeStatisticsKey extends BaseKey {
    /** The layer. */
    private final String layer;

    /** The date and hour with minute, second and millisecond set to 0. */
    private final Date time;

    /**
     * Create this class.
     * 
     * @param service     The name of the service.
     *                    <code>null</code> if not given.
     * @param method      The name of the method.
     *                    <code>null</code> if not given.
     * @param origin      The name of the origin.
     *                    <code>null</code> if not given.
     * @param product     The name of the product.
     *                    <code>null</code> if not given.
     * @param media       The name of the media.
     *                    <code>null</code> if not given.
     * @param layer       The name of the layer.
     *                    <code>null</code> if not given.
     * @param time        The date and hour with minute, second and millisecond
     *                    set to 0. Must not be <code>null</code>.
     *                    
     * @throws IllegalArgumentException if date is <code>null</code>.
     */
    /* package */ TimeStatisticsKey(String service, String method, String origin, String product,
            String media, String layer, Date time) {
        super(service, method, origin, product, media);
        this.layer = layer;
        if (time == null) {
            throw new IllegalArgumentException("time must not be null");
        }
        this.time = time;
    }
    
    /**
     * Get the layer.
     * 
     * @return The layer or <code>null</code> if not specified.
     */
    public String getLayer() {
        return layer;
    }
    
    /**
     * Get the time.
     * 
     * @return The time. This method never returns <code>null</code>.
     */
    public Date getTime() {
        return time;
    }
    
    /**
     * Get the string representation of this instance.
     * 
     * @return The string representation of this instance.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        toStringAppend(sb);
        sb.append(", layer=").append(getLayer());
        sb.append(", time=").append(getTime());
        sb.append('}');
        return sb.toString();
    }
    
    /**
     * Compare this object with another object.
     * 
     * @param other The other object.
     * 
     * @return <code>true</code> if and only if this object is equals to
     *         <code>other</code>. <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TimeStatisticsKey)) {
            return false;
        }
        TimeStatisticsKey o = (TimeStatisticsKey)other;
        if (!baseEquals(o)) {
            return false;
        }
        if (!stringEquals(layer, o.layer)) {
            return false;
        }
        if (!time.equals(o.time)) {
            return false;
        }
        return true;
    }
    
    /**
     * Get the hash code of this instance.
     * 
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = baseHashCode();
        result += baseHashCode(layer);
        result += time.hashCode();
        return result;
    }
}
