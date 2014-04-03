package se.uc.stat.web.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import se.uc.stat.web.statistics.ResultSetCell;
import se.uc.stat.web.webtypes.GUIFormatter;

/**
 * The type of column.
 * 
 * @author Anders Persson (konx40)
 */
public enum ColumnType {
    /** String column. */
    STRING(new Read() {
        /**
         * {@inheritDoc}.
         */
        @Override
        public ResultSetCell read(ResultSet rs, int index) throws SQLException {
            final String str = rs.getString(index); 
            return new ResultSetCell(str, 0, null);
        }
    }),
    /** Date column. */
    DATE(new Read() {
        /**
         * Formatter. Must only be used if a lock on <code>format</code>
         * is held.
         */
        private final SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd");
        
        /**
         * {@inheritDoc}.
         */
        @Override
        public ResultSetCell read(ResultSet rs, int index) throws SQLException {
            final Date date = new Date(rs.getTimestamp(index).getTime());
            String str = rs.getString(index);
            synchronized(format) {
                str = format.format(date);
            }
            str = GUIFormatter.toStringNoBreak(str);
            return new ResultSetCell(str, 0, date);
        }
    }),
    /** Date hour column. */
    DATE_HOUR(new Read() {
        /**
         * Formatter. Must only be used if a lock on <code>format</code>
         * is held.
         */
        private final SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd HH");
        
        /**
         * {@inheritDoc}.
         */
        @Override
        public ResultSetCell read(ResultSet rs, int index) throws SQLException {
            final Date date = new Date(rs.getTimestamp(index).getTime());
            String str;
            synchronized(format) {
                str = format.format(date);
            }
            str = GUIFormatter.toStringNoBreak(str);
            return new ResultSetCell(str, 0, date);
        }
    }),
    /** Long column. */
    LONG(new Read() {
        /**
         * {@inheritDoc}.
         */
        @Override
        public ResultSetCell read(ResultSet rs, int index) throws SQLException {
            final long longValue = rs.getLong(index);
            String str = Long.toString(longValue);
            return new ResultSetCell(str, longValue, null);
        }
    }),
    /** Long column without showing zeroes. */
    LONG_NO_ZEROES(new Read() {
        /**
         * {@inheritDoc}.
         */
        @Override
        public ResultSetCell read(ResultSet rs, int index) throws SQLException {
            final long longValue = rs.getLong(index);
            String str = "";
            if (longValue != 0) {
                str = Long.toString(longValue);
            }
            return new ResultSetCell(str, longValue, null);
        }
    });

    /** The column processing rules. */
    private final Read read;
    
    /**
     * Create this class.
     * 
     * @param read The class reading the information from the result set.
     *             Must not be <code>null</code>.
     *                     
     * @throws IllegalArgumentException if any of the constraints specified
     *         is not met.
     */
    private ColumnType(Read read) {
        if (read == null) {
            throw new IllegalArgumentException("read must not be null");
        }
        this.read = read;
    }

    /**
     * Get the class reading this information from the database.
     * 
     * @return The class reading this information from the database.
     *         This method never returns <code>null</code>.
     */
    public Read getRead() {
        return read;
    }

    /**
     * Interface specifying what is needed to read this information from the
     * result set.
     */
    public static interface Read {
        /**
         * Read one column from the record set and return the string
         * representation of the value.
         * 
         * @param rs    The result set to read
         * @param index The index of the attribute in the result set.
         * 
         * @return The information read.
         * 
         * @throws SQLException if there is an error reading from the
         *         result set.
         */
        public ResultSetCell read(ResultSet rs, int index) throws SQLException;
    }
}
