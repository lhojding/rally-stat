package se.uc.stat.customerstatistics;

import se.uc.stat.basestatistics.BaseInformation;

/**
 * The collected statistics information for customer statistics.
 * <p/>
 * Note that these methods are not thread safe and require synchronization
 * when they are called.
 * 
 * @author Anders Persson (konx40)
 */
/* package */ class CustomerInformation
        extends BaseInformation<CustomerStatisticsKey, CustomerInformation>
        implements Cloneable {
    /** The key for this information. */
    private final CustomerStatisticsKey key;
    
    /**
     * Create this class.
     * 
     * @param key       The key for this information.
     *                  Must not be <code>null</code>.
     * @param dayOfWeek The day of the week. Have to be between 0 and 6
     *                  where 0 is Monday and 6 is Sunday.  
     *                  
     * @throws IllegalArgumentException if <code>dayOfWeek</code>
     *         or <code>key</code> is invalid.
     */
    /* package */ CustomerInformation(CustomerStatisticsKey key, int dayOfWeek) {
        super(dayOfWeek);
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
        this.key = key;
    }
    
    /**
     * Register a correct call.
     */
    public void registerCorrectCall() {
        increaseNumCorrectCalls();
    }
    
    /**
     * Register an invalid call.
     */
    public void registerInvalidCall() {
        increaseNumInvalidCalls();
    }
    
    /**
     * Register a failed call.
     */
    public void registerFailedCall() {
        increaseNumFailedCalls();
    }

    /**
     * Get the key for this information.
     * 
     * @return The key for this information.
     *         This method never returns <code>null</code>.
     */
    @Override
    public CustomerStatisticsKey getKey() {
        return key;
    }

    /**
     * Update this instance by subtracting the numbers in <code>other</code>.
     * This method is used to decrease the numbers in the cache
     * (this instance) by the numbers that has already been stored persistent
     * (<code>other</code> instance). The method returns information about
     * if this instance becomes empty (in which case it can be removed from
     * the cache) or if it still contains information.
     *   
     * @param other The instance with the numbers to subtract from this
     *              instance.
     *              
     * @return <code>true</code> if this instance only have zeros.
     *         <code>false</code> if at least one number is not zero.
     */
    @Override
    protected boolean subtract(CustomerInformation other) {
        return baseSubtract(other);
    }

    /**
     * Create a clone of this object.
     * 
     * @return A clone of this object.
     */
    @Override
    protected CustomerInformation createClone() {
        try {
            return (CustomerInformation)super.clone();
        } catch (CloneNotSupportedException e) {
            // Nothing to do.
            // This case will be found in the unit tests.
        }
        return null;
    }
}
