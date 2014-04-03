package se.uc.stat.timestatistics;

import se.uc.stat.basestatistics.BaseInformation;

/**
 * The collected statistics information for time statistics.
 * <p/>
 * Note that these methods are not thread safe and require synchronization
 * when they are called.
 * 
 * @author Anders Persson (konx40)
 */
/* package */ class TimeInformation
        extends BaseInformation<TimeStatisticsKey, TimeInformation>
        implements Cloneable {
    /** The key for this information. */
    private final TimeStatisticsKey key;

    /** The hour of the day. */
    private final int hourOfDay;
    
    /** The total time of correct calls. */
    private long totalTimeCorrectCalls = 0;
    
    /** The total time of invalid calls. */
    private long totalTimeInvalidCalls = 0;
    
    /** The total time of failed calls. */
    private long totalTimeFailedCalls = 0;

    /** The number of calls taking 0-10 ms. */
    private int num10 = 0;

    /** The number of calls taking 11-20 ms. */
    private int num20 = 0;

    /** The number of calls taking 21-50 ms. */
    private int num50 = 0;

    /** The number of calls taking 51-100 ms. */
    private int num100 = 0;

    /** The number of calls taking 101-200 ms. */
    private int num200 = 0;

    /** The number of calls taking 201-500 ms. */
    private int num500 = 0;

    /** The number of calls taking 501-1000 ms. */
    private int num1000 = 0;

    /** The number of calls taking 1001-2000 ms. */
    private int num2000 = 0;

    /** The number of calls taking 2001-5000 ms. */
    private int num5000 = 0;

    /** The number of calls taking 5001-10000 ms. */
    private int num10000 = 0;

    /** The number of calls taking 10001-20000 ms. */
    private int num20000 = 0;

    /** The number of calls taking 20001 ms. */
    private int numOver20000 = 0;
    
    /**
     * Create this class.
     * 
     * @param key       The key for this information.
     *                  Must not be <code>null</code>.
     * @param dayOfWeek The day of the week. Have to be between 0 and 6
     *                  where 0 is Monday and 6 is Sunday.  
     * @param hourOfDay The hour of the day. Have to be between 0 and 23.
     *                  
     * @throws IllegalArgumentException if <code>dayOfWeek</code> or
     *         <code>hourOfDay</code> or <code>key</code> is invalid.
     */
    /* package */ TimeInformation(TimeStatisticsKey key, int dayOfWeek,
            int hourOfDay) {
        super(dayOfWeek);
        if (hourOfDay < 0 || hourOfDay > 23) {
            throw new IllegalArgumentException("hourOfDay is invalid (" +
                    hourOfDay + ")");
        }
        this.hourOfDay = hourOfDay;
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
        this.key = key;
    }

    /**
     * Register a correct call.
     * 
     * @param callTime The time in milliseconds the call took.
     */
    public void registerCorrectCall(long callTime) {
        increaseNumCorrectCalls();
        totalTimeCorrectCalls += callTime;
        registerCallInTimeRange(callTime);
    }
    
    /**
     * Register an invalid call.
     * 
     * @param callTime The time in milliseconds the call took.
     */
    public void registerInvalidCall(long callTime) {
        increaseNumInvalidCalls();
        totalTimeInvalidCalls += callTime;
        registerCallInTimeRange(callTime);
    }
    
    /**
     * Register a failed call.
     * 
     * @param callTime The time in milliseconds the call took.
     */
    public void registerFailedCall(long callTime) {
        increaseNumFailedCalls();
        totalTimeFailedCalls += callTime;
        registerCallInTimeRange(callTime);
    }

    /**
     * Register the call in the correct time range
     * (one of the num10-numOver20000) attributes).
     * 
     * @param callTime The time in milliseconds the call took.
     */
    private void registerCallInTimeRange(long callTime) {
        if (callTime <= 100) {
            if (callTime <= 20) {
                if (callTime <= 10) {
                    num10++;
                } else {
                    num20++;
                }
            } else {
                if (callTime <= 50) {
                    num50++;
                } else {
                    num100++;
                }
            }
        } else if (callTime <= 2000) {
            if (callTime <= 500) {
                if (callTime <= 200) {
                    num200++;
                } else {
                    num500++;
                }
            } else {
                if (callTime <= 1000) {
                    num1000++;
                } else {
                    num2000++;
                }
            }
        } else {
            if (callTime <= 10000) {
                if (callTime <= 5000) {
                    num5000++;
                } else {
                    num10000++;
                }
            } else {
                if (callTime <= 20000) {
                    num20000++;
                } else {
                    numOver20000++;
                }
            }
        }
    }
    
    /**
     * Get the hour of the day.
     * 
     * @return The hour of the day (0-23).
     */
    public int getHourOfDay() {
        return hourOfDay;
    }
    
    /**
     * Get the total time for correct calls in milliseconds.
     * 
     * @return The total time for correct calls in milliseconds.
     */
    public long getTotalTimeCorrectCalls() {
        return totalTimeCorrectCalls;
    }
    
    /**
     * Get the total time for invalid calls in milliseconds.
     * 
     * @return The total time for invalid calls in milliseconds.
     */
    public long getTotalTimeInvalidCalls() {
        return totalTimeInvalidCalls;
    }
    
    /**
     * Get the total time for failed calls in milliseconds.
     * 
     * @return The total time for failed calls in milliseconds.
     */
    public long getTotalTimeFailedCalls() {
        return totalTimeFailedCalls;
    }
    
    /**
     * Get the number of calls taking 0-10 ms.
     * 
     * @return The number of calls in this time range.
     */
    public int getNum10() {
        return num10;
    }
    
    /**
     * Get the number of calls taking 11-20 ms.
     * 
     * @return The number of calls in this time range.
     */
    public int getNum20() {
        return num20;
    }
    
    /**
     * Get the number of calls taking 21-50 ms.
     * 
     * @return The number of calls in this time range.
     */
    public int getNum50() {
        return num50;
    }
    
    /**
     * Get the number of calls taking 51-100 ms.
     * 
     * @return The number of calls in this time range.
     */
    public int getNum100() {
        return num100;
    }
    
    /**
     * Get the number of calls taking 101-200 ms.
     * 
     * @return The number of calls in this time range.
     */
    public int getNum200() {
        return num200;
    }
    
    /**
     * Get the number of calls taking 201-500 ms.
     * 
     * @return The number of calls in this time range.
     */
    public int getNum500() {
        return num500;
    }
    
    /**
     * Get the number of calls taking 501-1000 ms.
     * 
     * @return The number of calls in this time range.
     */
    public int getNum1000() {
        return num1000;
    }
    
    /**
     * Get the number of calls taking 1001-2000 ms.
     * 
     * @return The number of calls in this time range.
     */
    public int getNum2000() {
        return num2000;
    }
    
    /**
     * Get the number of calls taking 2001-5000 ms.
     * 
     * @return The number of calls in this time range.
     */
    public int getNum5000() {
        return num5000;
    }
    
    /**
     * Get the number of calls taking 5001-10000 ms.
     * 
     * @return The number of calls in this time range.
     */
    public int getNum10000() {
        return num10000;
    }
    
    /**
     * Get the number of calls taking 10001-20000 ms.
     * 
     * @return The number of calls in this time range.
     */
    public int getNum20000() {
        return num20000;
    }
    
    /**
     * Get the number of calls taking 20001 ms and above.
     * 
     * @return The number of calls in this time range.
     */
    public int getNumOver20000() {
        return numOver20000;
    }

    /**
     * Get the key for this information.
     * 
     * @return The key for this information.
     *         This method never returns <code>null</code>.
     */
    @Override
    public TimeStatisticsKey getKey() {
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
    protected boolean subtract(TimeInformation other) {
        boolean onlyZeroes = baseSubtract(other);
        
        totalTimeCorrectCalls -= other.totalTimeCorrectCalls;
        totalTimeInvalidCalls -= other.totalTimeInvalidCalls;
        totalTimeFailedCalls -= other.totalTimeFailedCalls;
        num10 -= other.num10;
        num20 -= other.num20;
        num50 -= other.num50;
        num100 -= other.num100;
        num200 -= other.num200;
        num500 -= other.num500;
        num1000 -= other.num1000;
        num2000 -= other.num2000;
        num5000 -= other.num5000;
        num10000 -= other.num10000;
        num20000 -= other.num20000;
        numOver20000 -= other.numOver20000;
        
        onlyZeroes = onlyZeroes && (totalTimeCorrectCalls == 0) &&
                (totalTimeInvalidCalls == 0) && (totalTimeFailedCalls == 0) &&
                (num10 == 0) && (num20 == 0) && (num50 == 0) && (num100 == 0) &&
                (num200 == 0) && (num500 == 0) && (num1000 == 0) &&
                (num2000 == 0) && (num5000 == 0) && (num10000 == 0) &&
                (num20000 == 0) && (numOver20000 == 0);
        return onlyZeroes;
    }
    
    /**
     * Create a clone of this object.
     * 
     * @return A clone of this object.
     */
    @Override
    protected TimeInformation createClone() {
        try {
            return (TimeInformation)super.clone();
        } catch (CloneNotSupportedException e) {
            // Nothing to do.
            // This case will be found in the unit tests.
        }
        return null;
    }
}
