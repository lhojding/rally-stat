package se.uc.stat.basestatistics;

import java.io.Serializable;

/**
 * The base class for customer and time statistics keys holding the common
 * attributes and some help functionality.
 * 
 * @author Anders Persson (konx40)
 */
public abstract class BaseKey implements Serializable {
    /** The service name. */
    private final String service;

    /** The method name. */
    private final String method;

    /** The origin. */
    private final String origin;

    /** The product. */
    private final String product;

    /** The media. */
    private final String media;
    
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
     */
    public BaseKey(String service, String method, String origin, String product,
            String media) {
        this.service = service;
        this.method = method;
        this.origin = origin;
        this.product = product;
        this.media = media;
    }
    
    /**
     * Get the service.
     * 
     * @return The service or <code>null</code> if not specified.
     */
    public String getService() {
        return service;
    }
    
    /**
     * Get the method.
     * 
     * @return The method or <code>null</code> if not specified.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Get the origin.
     * 
     * @return The origin or <code>null</code> if not specified.
     */
    public String getOrigin() {
        return origin;
    }
    
    /**
     * Get the product.
     * 
     * @return The product or <code>null</code> if not specified.
     */
    public String getProduct() {
        return product;
    }
    
    /**
     * Get the media.
     * 
     * @return The media or <code>null</code> if not specified.
     */
    public String getMedia() {
        return media;
    }
    
    /**
     * Append the attributes of this base class to a <code>toString</code>
     * representation of this class. The representation ends with ", "
     * 
     * @param sb The string builder to append the attributes to.
     */
    protected void toStringAppend(StringBuilder sb) {
        sb.append("service=").append(getService());
        sb.append(", method=").append(getMethod());
        sb.append(", origin=").append(getOrigin());
        sb.append(", product=").append(getProduct());
        sb.append(", media=").append(getMedia());
    }
    
    /**
     * Compare the attributes of this base class to another instance.
     * 
     * @param other The other object.
     * 
     * @return <code>true</code> if and only if the attributes of this
     *         base class are equals to the attributes in <code>other</code>.
     *         <code>false</code> otherwise.
     */
    protected boolean baseEquals(BaseKey other) {
        if (!stringEquals(service, other.service)) {
            return false;
        }
        if (!stringEquals(method, other.method)) {
            return false;
        }
        if (!stringEquals(origin, other.origin)) {
            return false;
        }
        if (!stringEquals(product, other.product)) {
            return false;
        }
        if (!stringEquals(media, other.media)) {
            return false;
        }
        return true;
    }

    /**
     * Compares two strings taking <code>null</code> into consideration.
     * 
     * @param s1 One string. May be <code>null</code>.
     * @param s2 The other string. May be <code>null</code>.
     * 
     * @return <code>true</code> if and only if the strings are equals.
     *         <code>null</code> is considered equals to <code>null</code>.
     */
    protected boolean stringEquals(String s1, String s2) {
        if (s1 == null) {
            if (s2 != null) {
                return false;
            }
        } else {
            if (s2 == null) {
                return false;
            }
            if (!s1.equals(s2)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Calculate the hash code for the attributes in this base class.
     * 
     * @return The hash code for the attributes in this base class.
     */
    protected int baseHashCode() {
        int result = 0;
        result += baseHashCode(service);
        result += baseHashCode(method);
        result += baseHashCode(origin);
        result += baseHashCode(product);
        result += baseHashCode(media);
        return result;
    }
    
    /**
     * Calculate the hash code for a string taking <code>null</code> into
     * consideration.
     * 
     * @param s The string or <code>null</code>.
     * 
     * @return The hash code for the string.
     */
    protected int baseHashCode(String s) {
        if (s == null) {
            return 0;
        }
        return s.hashCode();
    }
}
