package se.uc.stat.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Class with utilities for database operations.
 * 
 * @author Anders Persson (konx40)
 */
public class DBUtilities {
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
     * Create a connection to the database using the connectionpool
     * <code>java:comp/env/jdbc/stat</code>
     * 
     * @return The connection created. Note that it have to be closed after usage.
     *         This method never returns <code>null</code>.
     * 
     * @throws SQLException if there are any error creating the connection.
     */
    public static Connection createConnection() throws SQLException {
        try {
            final Context initContext = new InitialContext();
            final Context envContext  = (Context)initContext.lookup("java:/comp/env");
            final DataSource ds = (DataSource)envContext.lookup("jdbc/stat");
            return ds.getConnection();
        } catch (NamingException e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * Close the connection, statement and resultset (if they are not
     * <code>null</code>).
     * 
     * @param c  The connection to close.
     *           <code>null</code> if no connection to close
     * @param s  The statement to close.
     *           <code>null</code> if no statement to close
     * @param rs The result set to close.
     *           <code>null</code> if no result set to close
     * 
     * @throws SQLException if there are and exceptions when performing the
     *         close operation.
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
