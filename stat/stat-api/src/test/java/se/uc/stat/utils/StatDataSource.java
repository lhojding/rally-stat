package se.uc.stat.utils;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Implementation of a DataSource for test purposes.
 * 
 * @author Anders Persson (konx40)
 */
/* package */ class StatDataSource implements DataSource {
    /** The host hosting the database. */
    private final String dbHost;
    /** The port hosting the database. */
    private final int dbPort;
    /** The SID hosting the database. */
    private final String dbSid;
    /** The database user name. */
    private final String dbUser;
    /** The database password. */
    private final String dbPassword;

    /**
     * Register the database driver.
     */
    static {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    /**
     * Create this datasource.
     * 
     * @param host     The database host to use. Must not be <code>null</code>.
     * @param port     The database port to use.
     * @param sid      The database sid to use. Must not be <code>null</code>.
     * @param user     The database user to use. Must not be <code>null</code>.
     * @param password The database password to use.
     *                 Must not be <code>null</code>.
     * 
     * @throws IllegalArgumentException if any of the parameter does not follows
     *         the conditions specified.
     */
    public StatDataSource(String host, int port, String sid, String user,
            String password) {
        if (host == null) {
            throw new IllegalArgumentException("Host must not be null.");
        }
        dbHost = host;
        dbPort = port;
        if (sid == null) {
            throw new IllegalArgumentException("Sid must not be null.");
        }
        dbSid = sid;
        if (user == null) {
            throw new IllegalArgumentException("User must not be null.");
        }
        dbUser = user;
        if (password == null) {
            throw new IllegalArgumentException("Password must not be null.");
        }
        dbPassword = password;
    }

    /**
     * Create a connection.
     * 
     * @return A connection to the CAS database.
     * 
     * @throws SQLException if the connection can not be created.
     */
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@" + dbHost + ":" +
                dbPort + ":" + dbSid, dbUser, dbPassword);
    }

    /**
     * Method not implemented.
     * 
     * {@inheritDoc}
     * 
     * @throws SQLException always.
     */
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new SQLException("Method not implemented");
    }

    /**
     * Method not implemented.
     * 
     * {@inheritDoc}
     * 
     * @throws SQLException always.
     */
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLException("Method not implemented");
    }

    /**
     * Method not implemented.
     * 
     * {@inheritDoc}
     * 
     * @throws SQLException always.
     */
    @Override
    public int getLoginTimeout() throws SQLException {
        throw new SQLException("Method not implemented");
    }

    /**
     * Method not implemented.
     * 
     * {@inheritDoc}
     * 
     * @throws SQLException always.
     */
    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new SQLException("Method not implemented");
    }

    /**
     * Method not implemented.
     * 
     * {@inheritDoc}
     * 
     * @throws SQLException always.
     */
    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new SQLException("Method not implemented");
    }

    /**
     * Method not implemented.
     * 
     * {@inheritDoc}
     * 
     * @throws SQLException always.
     */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLException("Method not implemented");
    }

    /**
     * Method not implemented.
     * 
     * {@inheritDoc}
     * 
     * @throws SQLException always.
     */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("Method not implemented");
    }
}
