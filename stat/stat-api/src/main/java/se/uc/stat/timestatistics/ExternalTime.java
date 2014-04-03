package se.uc.stat.timestatistics;

/**
 * Class representing external time to be removed from a time measure
 * to be able to measure only internal time.
 * <p/>
 * This class is only intended to be input to the method
 * {@link StatisticsInfo#removeExternalTime(ExternalTime)}
 * 
 * @author Anders Persson (konx40)
 */
public class ExternalTime {
    /** The time in milliseconds spent in external execution to remove. */
    private final long time;
    
    /**
     * Create this instance.
     * 
     * @param time The time in milliseconds spent in external execution.
     */
    ExternalTime(long time) {
        this.time = time;
    }
    
    /**
     * Get the time in milliseconds spent in external execution.
     * 
     * @return The time in milliseconds spent in external execution.
     */
    /* package */ long getTime() {
        return time;
    }
}
