package se.uc.stat.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;

import se.uc.stat.dimension.Dimensions;

import static org.junit.Assert.fail;

/**
 * Base class for all tests. Provides some common functionality such as
 * retrieving a data source.
 * 
 * @author Anders Persson (konx40)
 */
public abstract class AbstractTestBase {
    /** The host hosting the database. */
    private final String HOST = "br1lvdb1t.intern.uc.se";
    /** The port hosting the database. */
    private final int PORT = 1521;
    /** The SID hosting the database. */
    private final String SID = "UCTEST01";
    /** The database user name. */
    private final String USER = "TEST_STAT";
    /** The database password. */
    private final String PASSWORD = "uc4ever";
    
    /**
     * A database connection (if one has been created) or <code>null</code>
     * otherwise. The connection is created on demand and will be closed by the
     * {@link #tearDown()} method.
     */
    private Connection connection = null;

    /**
     * Get the data source to use for test purposes.
     * 
     * @return The data source to use for test purposes.
     *         This method never returns <code>null</code>.
     */
    private DataSource createDataSource() {
        return new StatDataSource(HOST, PORT, SID, USER, PASSWORD);
    }
    
    /**
     * Set the test data source before performing the tests.
     */
    @Before
    public void setup() {
        DatabaseUtils.setTestDataSource(createDataSource());
        Dimensions.clear();
    }
    
    /**
     * Close the connection held by this test class.
     * 
     * @throws SQLException if the close could not be performed.
     */
    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    /**
     * Get a connection for test class purposes. Note that this connection
     * <strong>must not</strong> be closed after use. The connection is closed
     * when the whole test has been performed. This is to avoid creating
     * a new connection for each separate SQL statement.
     * 
     * @return The database connection to use for test purposes. This
     *         connection <strong>must not</strong> be closed after use.
     *         
     * @throws SQLException If there is an error opening the connection.
     */
    protected Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DatabaseUtils.getConnection();
        }
        return connection;
    }
    
    /**
     * Execute an update SQL statement to manipulate the database.
     * 
     * @param sql The SQL to execute.
     * 
     * @throws SQLException if there is an error executing the SQL.
     */
    protected void updateInDb(String sql) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.executeUpdate();
        } finally {
            DatabaseUtils.close(null, statement, null);
        }
    }

    /**
     * Makes a copy of an object or object hierarchy. The copy is done
     * through serialization and deserialization of the object.
     * This makes a deep copy which ensures no references between
     * the two objects. Useful to test equals/hashCode to make sure
     * an <code>equals</code> comparison and not <code>==</code> is used. 
     * <p/>
     * Fails in a JUnit way if the copy can not be done.
     * 
     * @param <O>    The type of the object to copy.
     * @param object The object to copy
     *
     * @return A copy of the object. It it is <code>null</code>,
     *         <code>null</code> will be returned.
     */
    @SuppressWarnings("unchecked")
    protected <O> O copyObject(O object) {
       ByteArrayOutputStream stream;
       ObjectOutputStream out;
       ObjectInputStream in;
       Object result = null;

       if (object == null) {
          return null;
       }

       try {
          stream = new ByteArrayOutputStream(512);
          out = new ObjectOutputStream(stream);
          out.writeObject(object);
          out.close();
          in = new ObjectInputStream(new ByteArrayInputStream(stream.toByteArray()));
          result = in.readObject();
       }
       catch (ClassNotFoundException e)
       {
          fail("The object copy failed with the following reason:" + e);
       }
       catch (IOException e)
       {
          fail("The object copy failed with the following reason:" + e);
       }
       return (O)result;
    }
}
