package se.uc.stat.utils;

import java.util.Calendar;

/**
 * Class representing the time values of interest in the statistics system.
 * <p/>
 * Instances of this class are retrieved from {@link TimeUtils}.
 * 
 * @author Anders Persson (konx40)
 */
public class TimeRepresentation {
    /**
     * The date representation without hours, minutes, seconds and
     * milliseconds.
     */
    private final long date;
    /**
     * The date and hour representation without minutes, seconds and
     * milliseconds.
     */
    private final long dateHour;
    /**
     * The day of the week where 0=Monday, 1=Tuesdag, ... 6=Sunday.
     */
    private int dayOfWeek;
    /**
     * The hour of the day, 0-23.
     */
    private int hourOfDay;

    /**
     * Create this instance.
     * 
     * @param time The time to use.
     */
    /* package */ TimeRepresentation(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        dateHour = calendar.getTimeInMillis();
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        date = calendar.getTimeInMillis();
    }
    
    /**
     * Get the date with hours, minutes, seconds and milliseconds set to 0.
     * 
     * @return The date without time of the day. The common Java date format
     *         with number of milliseconds since start of 1970.
     */
    public long getDate() {
        return date;
    }
    
    /**
     * Get the date and hour with minutes, seconds and milliseconds set to 0.
     * 
     * @return The date and hour without time of the hour. The common Java
     *         date format with number of milliseconds since start of 1970.
     */
    public long getDateHour() {
        return dateHour;
    }
    
    /**
     * Get the day of the week where 0=Monday, 1=Tuesday, ... 6=Sunday.
     * 
     * @return The day of the week.
     */
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    
    /**
     * Get the hour of the day 0-23.
     * 
     * @return The hour of the day.
     */
    public int getHourOfDay() {
        return hourOfDay;
    }
}
