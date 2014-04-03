package se.uc.stat.dimension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import se.uc.stat.utils.DatabaseUtils;

/**
 * This is the base class for all dimensions. A dimension can be seen as a
 * representation of lookup table. This class can be seen as a database
 * backuped hash map performing both read and insert operations in the database.
 * <p/>
 * The methods are thread safe.
 * <p/>
 * <strong>Subclass implementation notes:</strong>
 * All subclass methods are called by one thread at a time.
 * <p/>
 * <strong>Note about the implementation of this class:</strong>
 * Note that the dimensions are chosen in a way that makes adding
 * of new keys very rare which makes performance for adding new keys not an
 * issue. The implementation to get existing keys fast is the performance
 * goal for this class.
 * 
 * @param <Key> is the type of the key.

 * @author Anders Persson (konx40)
 */
public abstract class BaseDimension<Key> {
    /** The string that may be used instead of null in names. */
    public final static String NULL_NAME = "(tom)";
    /** Lock object. */
    private final Object lock = new Object();
    /** The SQL to perform select. */
    private final String selectSql;
    /** The SQL to perform insert. */
    private final String insertSql;
    
    /**
     * Map for storing the dimension. Key is the key for the dimension and
     * value is the id for the particular key.
     */
    private final Map<Key, Integer> ids = new HashMap<Key, Integer>();
    
    /**
     * <code>true</code> if the ids map should be reread.
     * <code>false</code> if the ids map is up-to-date.
     */
    private boolean readIds = true;
    
    /**
     * Create this class.
     * 
     * @param selectSql The SQL to select all instances in the dimension.
     *                  Column 1 have to be the dimension id and the other
     *                  columns will form the key, see
     *                  {@link #extractKey}.
     * @param insertSql The SQL to insert an instance of the dimension.
     *                  The columns should form the key, see
     *                  {@link #insertKey}.
     */
    protected BaseDimension(String selectSql, String insertSql) {
        this.selectSql = selectSql;
        this.insertSql = insertSql;
    }
    
    /**
     * Get the id for the requested key. If the key does not exist,
     * it is created and an id is assigned.
     * 
     * @param key        The requested key. May be <code>null</code>.
     * 
     * @return The id for the requested key.
     * 
     * @throws SQLException if there are any problem communicating with the
     *         database.
     */
    public int getId(Key key) throws SQLException {
        final Key processedKey = preprocessKey(key);
        synchronized(lock) {
            ensureRead();
            Integer id = ids.get(processedKey);
            if (id != null) {
                return id.intValue();
            }
            storeKey(processedKey);
            readIds = true;
            ensureRead();
            id = ids.get(processedKey);
            if (id != null) {
                return id.intValue();
            }
            throw new SQLException("The key '" + key + "' could not be found " +
                    "and the try to create it in the database has failed for " +
                    "unknown reason."); 
        }
    }

    /**
     * Mark the dimension to be reread. This may be useful in case of unknown
     * errors. This method should never be called in case of normal successful
     * execution.
     */
    public void clear() {
        synchronized(lock) {
            readIds = true;
        }
    }
    
    /**
     * Ensure the map is read from the database if necessary.
     *            
     * @throws SQLException if there is an exception reading from the
     *         persistent store.
     */
    private void ensureRead() throws SQLException {
        if (readIds) {
            ids.clear();
            populateMap();
            readIds = false;
        }
    }

    /**
     * Perform a preprocess of the key. This means the possibility to
     * validate the key and to update the key with for example a shorter name.
     * 
     * @param key The incoming key. This is never <code>null</code>.
     * 
     * @return The key to use in the map and in the database. May be the same
     *         as the incoming key (if it is correct) or a new key instance.
     *         
     * @throws IllegalArgumentException if the key is invalid and will not be
     *         updated.
     */
    protected abstract Key preprocessKey(Key key);

    /**
     * Extract the key from the current row in this result set.
     * 
     * @param rs The result set to extract the key from. This is the result
     *           of the query specified as <code>selectSql</code> in the
     *           constructor to this class. 
     *           
     * @return The key extracted. This method must not return <code>null</code>.
     *
     * @throws SQLException if there is an exception reading from the
     *         result set.
     */
    protected abstract Key extractKey(ResultSet rs) throws SQLException;
    
    /**
     * Insert the given key in the prepared statement.
     * 
     * @param ps  The prepared statement to insert the key in. This is the
     *            statement specified as <code>insertSql</code> in the
     *            constructor to this class. 
     * @param key The key insert. This is never <code>null</code>.
     *
     * @throws SQLException if there is an exception inserting the
     *         key in the prepared statement.
     */
    protected abstract void insertKey(PreparedStatement ps, Key key)
            throws SQLException;
    
    /**
     * Populate the dimension map from the persistent store.
     * This method should read all instances in the persistent store
     * and update the <code>ids</code> map with all the key-value pairs.
     *            
     * @throws SQLException if there is an exception reading from the
     *         persistent store.
     */
    private void populateMap() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            connection = DatabaseUtils.getConnection();
            statement = connection.prepareStatement(selectSql);
            result = statement.executeQuery();
            while (result.next()) {
                final int id = result.getInt(1);
                final Key key = extractKey(result);
                ids.put(key, new Integer(id));
            }
        } finally {
            DatabaseUtils.close(connection, statement, result);
        }
    }
    
    /**
     * Store the <code>key</code> in the persistent store.
     * <p/>
     * This method <strong>have to</strong>:
     * <ul>
     * <li>Store the key in the persistent store.</li>
     * <li>Have to return graceful if the key is already in the
     *     persistent store (which may occur in clustered environments when
     *     the key may have been created by another instance).</li>
     * <li>Have to throw exception if it fails to create the key for other
     *     reasons than specified above.</li>
     * </ul>
     * 
     * @param key The key to create. This parameter is never <code>null</code>.
     * 
     * @throws SQLException if the creation fails for other reasons than that
     *         the key already exists.
     */
    // This method has package visibility to allow testing from the test class.
    /* package */ final void storeKey(Key key) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DatabaseUtils.getConnection();
            statement = connection.prepareStatement(insertSql);
            insertKey(statement, key);
            int numRows = statement.executeUpdate();
            if (numRows != 1) {
                throw new SQLException("The number of rows affected when " +
                        "storing the dimension is '" + numRows +
                        "' which is not correct. Class:" +
                        getClass().getName());
            }
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                // This is not a unique constraint, rethrow it
                throw e;
            }
            // This is a unique constraint, ignore it.
        } finally {
            DatabaseUtils.close(connection, statement, null);
        }
    }
}
