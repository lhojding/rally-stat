package se.uc.stat.utils;

/**
 * Class converting a time to a representation of the values of interest
 * in the statistics system.
 * <p/>
 * This class is intended to be used with similar times and it uses
 * a caching strategy to be able to serve requests within the hour fast.
 * 
 * @author Anders Persson (konx40)
 */
public class TimeUtils {
    /**
     * The time an instance may be cached in milliseconds which is one hour
     * minus one millisecond.
     */
    private final static long CACHE_TIME_INTERVAL =
            60 * 60 * 1000 - 1;
    
    /** Lock object for synchronization. */
    private final static Object lock = new Object();
    
    /** Cached instance of the time representation. */
    private static TimeRepresentation cachedTime = null;
    
    /** The minimum time the cached instance is valid for. */
    private static long minValidTime = Long.MAX_VALUE;
    
    /** The maximum time the cached instance is valid for. */
    private static long maxValidTime = Long.MIN_VALUE;
    
    /**
     * Create this class. This constructor is private to prevent instantiation.
     */
    private TimeUtils() {
        // Nothing to do.
    }
    
    /**
     * Get an instance of {@link TimeRepresentation} for the given
     * <code>time</code>.
     * 
     * @param time The time to use.
     * 
     * @return The representation. This method never returns <code>null</code>.
     */
    public static TimeRepresentation getTimeRepresentation(long time) {
        synchronized (lock) {
            if (time < minValidTime || time > maxValidTime) {
                cachedTime = new TimeRepresentation(time);
                minValidTime = cachedTime.getDateHour();
                maxValidTime = minValidTime + CACHE_TIME_INTERVAL;
            }
            return cachedTime;
        }
    }
}
