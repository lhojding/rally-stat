package se.uc.stat.basestatistics;

/**
 * Test key.
 * 
 * @author Anders Persson (konx40)
 */
/* package */ class BaseKeyImpl extends BaseKey {
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
     *                    
     * @throws IllegalArgumentException if date is <code>null</code>.
     */
    /* package */ BaseKeyImpl(String service, String method,
            String origin, String product, String media) {
        super(service, method, origin, product, media);
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
        if (!(other instanceof BaseKeyImpl)) {
            return false;
        }
        BaseKeyImpl o = (BaseKeyImpl)other;
        if (!baseEquals(o)) {
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
        return baseHashCode();
    }
}
