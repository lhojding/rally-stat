package se.uc.stat.utils;

/**
 * Class acting as a source for time for tests.
 * This class makes the thread halt when the time is close to each
 * hour break (from 1 second before to the hour break has passed).
 * <p/>
 * The reason is that a test call performed within that time is hear to
 * categorize (it is hard to know if it will be registered in the first or
 * second hour). 
 * 
 * @author Anders Persson (konx40)
 */
public class TimeSource {
    /** The number of milliseconds to halt before hour break. */
    private final static long BREAK_LENGTH = 1000;

    /** The time for next halt. */
    private static long nextHalt = Long.MIN_VALUE;
    
    /** Lock object for nextHalt. */
    private final static Object lock = new Object();

    /**
     * Halt the execution if the time is close to an hour break.
     * The time for halting is BREAK_LENGTH ms.
     */
    public static void haltNearHour() {
        long time = System.currentTimeMillis();
        synchronized(lock) {
            if (time < nextHalt) {
                return;
            }
            while (time < nextHalt + BREAK_LENGTH) {
                try {
                    Thread.sleep(nextHalt + BREAK_LENGTH - time);
                } catch (InterruptedException e) {
                    // Nothing to do.
                }
                time = System.currentTimeMillis();
            }
            nextHalt = TimeUtils.getTimeRepresentation(time).getDateHour() +
                    3600000 - BREAK_LENGTH;
        }
    }
}
