package se.uc.stat.web.statistics;

import java.util.Date;

/**
 * Class holding the information read from one result set.
 * 
 * @author Anders Persson (konx40)
 */
public class ResultSetCell {
    /** The string representation of the cell. */
    private final String string;
    /** The long representation of the cell (if applicable). */
    private final long longValue;
    /** The date representation of the cell (if applicable). */
    private final Date date;
    
    /**
     * Create this instance.
     * 
     * @param string    The string representation.
     * @param longValue The long representation (if applicable).
     * @param date      The date representation (if applicable).
     */
    public ResultSetCell(String string, long longValue, Date date) {
        this.string = string;
        this.longValue = longValue;
        this.date = date;
    }

    /**
     * Get the string representation of this cell.
     * 
     * @return The string representation of this cell (if applicable).
     */
    public String getString() {
        return string;
    }

    /**
     * Get the long value of this cell (if applicable).
     * 
     * @return The long value of this cell (if applicable).
     */
    public long getLong() {
        return longValue;
    }

    /**
     * Get the date value of this cell (if applicable).
     * 
     * @return The date value of this cell (if applicable).
     */
    public Date getDate() {
        return date;
    }
}
