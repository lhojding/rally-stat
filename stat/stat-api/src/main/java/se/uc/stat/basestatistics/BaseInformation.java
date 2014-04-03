package se.uc.stat.basestatistics;

/**
 * The parts of the collected statistics information that are common for
 * both customer and time statistics.
 * <p/>
 * Note that these methods are not thread safe and require synchronization
 * when they are called.
 * 
 * @param <Key>  The type of the key.
 * @param <Info> The type of the sub class. 
 * 
 * @author Anders Persson (konx40)
 */
public abstract class BaseInformation<Key, Info> {
    /** The day of the week. */
    private final int dayOfWeek;
    
    /** The number of correct calls. */
    private int numCorrectCalls = 0;
    
    /** The number of invalid calls. */
    private int numInvalidCalls = 0;
    
    /** The number of failed calls. */
    private int numFailedCalls = 0;
    
    /**
     * Create this class.
     * 
     * @param dayOfWeek The day of the week. Have to be between 0 and 6
     *                  where 0 is Monday and 6 is Sunday.  
     *                  
     * @throws IllegalArgumentException if <code>dayOfWeek</code> is invalid.
     */
    protected BaseInformation(int dayOfWeek) {
        if (dayOfWeek < 0 || dayOfWeek > 6) {
            throw new IllegalArgumentException("dayOfWeek is invalid (" +
                    dayOfWeek + ")");
        }
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Get the day of the week.
     * 
     * @return The day of the week (0=Monday to 6=Sunday).
     */
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Increase the number of correct calls by one.
     */
    protected void increaseNumCorrectCalls() {
        numCorrectCalls++;
    }
    
    /**
     * Get the number of correct calls.
     * 
     * @return The number of correct calls.
     */
    public int getNumCorrectCalls() {
        return numCorrectCalls;
    }

    /**
     * Increase the number of invalid calls by one.
     */
    protected void increaseNumInvalidCalls() {
        numInvalidCalls++;
    }
    
    /**
     * Get the number of invalid calls.
     * 
     * @return The number of invalid calls.
     */
    public int getNumInvalidCalls() {
        return numInvalidCalls;
    }

    /**
     * Increase the number of failed calls by one.
     */
    protected void increaseNumFailedCalls() {
        numFailedCalls++;
    }
    
    /**
     * Get the number of failed calls.
     * 
     * @return The number of failed calls.
     */
    public int getNumFailedCalls() {
        return numFailedCalls;
    }

    /**
     * Get the key for this information.
     * 
     * @return The key for this information.
     *         This method never returns <code>null</code>.
     */
    public abstract Key getKey();

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
    protected boolean baseSubtract(BaseInformation<Key, Info> other) {
        numCorrectCalls -= other.numCorrectCalls;
        numInvalidCalls -= other.numInvalidCalls;
        numFailedCalls -= other.numFailedCalls;
        
        return (numCorrectCalls == 0) && (numInvalidCalls == 0) &&
                (numFailedCalls == 0);
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
    protected abstract boolean subtract(Info other);

    /**
     * Create a clone of this object.
     * 
     * @return A clone of this object.
     */
    protected abstract Info createClone();
}
