package se.uc.stat.customerstatistics;

import java.util.Date;

import se.uc.stat.basestatistics.BaseKey;

/**
 * The key for the customer statistics.
 * 
 * @author Anders Persson (konx40)
 */
/* package */ class CustomerStatisticsKey extends BaseKey {
    /** The customer. */
    private final String customer;

    /** The date with hour, minute, second and millisecond set to 0. */
    private final Date date;

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
     * @param customer    The name of the customer.
     *                    <code>null</code> if not given.
     * @param date        The date with hour, minute, second and millisecond
     *                    set to 0. Must not be <code>null</code>.
     *                    
     * @throws IllegalArgumentException if date is <code>null</code>.
     */
    /* package */ CustomerStatisticsKey(String service, String method,
            String origin, String product, String media, String customer,
            Date date) {
        super(service, method, origin, product, media);
        this.customer = customer;
        if (date == null) {
            throw new IllegalArgumentException("date must not be null");
        }
        this.date = date;
    }
    
    /**
     * Get the customer.
     * 
     * @return The customer or <code>null</code> if not specified.
     */
    public String getCustomer() {
        return customer;
    }
    
    /**
     * Get the date.
     * 
     * @return The date. This method never returns <code>null</code>.
     */
    public Date getDate() {
        return date;
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
        sb.append(", customer=").append(getCustomer());
        sb.append(", date=").append(getDate());
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
        if (!(other instanceof CustomerStatisticsKey)) {
            return false;
        }
        CustomerStatisticsKey o = (CustomerStatisticsKey)other;
        if (!baseEquals(o)) {
            return false;
        }
        if (!stringEquals(customer, o.customer)) {
            return false;
        }
        if (!date.equals(o.date)) {
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
        result += baseHashCode(customer);
        result += date.hashCode();
        return result;
    }
}
