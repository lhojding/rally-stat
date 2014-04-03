package se.uc.stat.utils;

import se.uc.stat.log.Log;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Utilities for database handling.
 * 
 * @author Anders Persson (konx40)
 */
public class DatabaseUtils {
    /** Log object for this class. */
    private static final Log log = Log.getLog(DatabaseUtils.class);

    /** Lock object. */
    private static final Object lock = new Object();
    
    /** The datasource to the stat database. */
    private static DataSource dataSource = null;
    
    /**
     * <code>true</code> if this is a test data source.
     * <code>false</code> if this is a production data source.
     */
    private static boolean isTestDataSource = false;
    
    /**
     * Array with spaces to use for padding.
     */
    private static final char[] SPACES = new char[100];
    
    /**
     * Set the spaces char array.
     */
    static {
        Arrays.fill(SPACES, ' ');
    }
    
    /**
     * Private constructor to prevent instantiation.
     */
    private DatabaseUtils() {
        // Nothing to do
    }

    /**
     * Set a test data source to use for running test cases.
     * This method must not be called unless for unit test purposes.
     * 
     * @param dataSource The data source to use.
     *                   Must not be <code>null</code>.
     *                   
     * @throws IllegalArgumentException if any of the constraints specified
     *         are not met.
     */
    public static void setTestDataSource(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource must not be null");
        }
        synchronized(lock) {
            DatabaseUtils.dataSource = dataSource;
            isTestDataSource = true;
        }
    }
    
    /**
     * Get a connection to the database.
     * 
     * @return A connection to the database.
     *         This method never returns <code>null</code>.
     *         
     * @throws SQLException if the connection can not be retrieved.
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null && !isTestDataSource) {
            synchronized(lock) {
                try {
                    InitialContext ic = new InitialContext();
                    dataSource = (DataSource) ic.lookup("jdbc/stat");
                } catch (NamingException e) {
                    log.error("Could not retrieve the data source jdbc/stat " +
                            "from the initial context (local naming directory.",
                            e, null);
                    throw new SQLException("Could not retrieve the data source",
                            e);
                }
            }
        }
        return dataSource.getConnection();
    }

    /**
     * This method is called to report a database error to allow this class
     * to perform actions (for example release cached objects and retrieve
     * new ones).
     */
    public static void reportDatabaseError() {
        if (!isTestDataSource) {
            synchronized(lock) {
                dataSource = null;
            }
        }
    }

    /**
     * Close the connection, statement and resultset (if they are not
     * <code>null</code>).
     * 
     * @param c   The connection to close.
     * @param s   The statement to close.
     * @param rs  The resultset to close.
     * 
     * @throws SQLException if there are and exceptions when performing
     *         the close operation.
     */
    public static void close(Connection c, Statement s, ResultSet rs)
            throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (s != null) {
            s.close();
        }
        if (c != null) {
            c.close();
        }
    }
    
    /**
     * Pad the given string to the given length by adding spaces at the end
     * of the string.
     * 
     * @param string   The string to pad.
     * @param numChars The number of characters requested.
     * 
     * @return The string padded from the right to the given length.
     *         If <code>string</code> is <code>null</code>, <code>null</code>
     *         is returned.
     */
    public static String pad(String string, int numChars) {
        if (string == null) {
            return null;
        }
        if (string.length() > numChars) {
            return string.substring(0, numChars);
        }
        StringBuilder sb = new StringBuilder(numChars);
        sb.append(string);
        while (sb.length() < numChars) {
            final int len = Math.min(SPACES.length, numChars - sb.length());
            sb.append(SPACES, 0, len);
        }
        return sb.toString();
    }
}
