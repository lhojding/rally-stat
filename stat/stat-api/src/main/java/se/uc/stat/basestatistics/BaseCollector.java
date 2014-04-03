package se.uc.stat.basestatistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import se.uc.stat.dimension.Dimensions;
import se.uc.stat.utils.DatabaseUtils;
import se.uc.stat.log.Log;

/**
 * Base class for classes collecting statistics.
 * <p/>
 * All registration methods have to be constructed in the following way:
 * <pre>
 * Key key = ...;
 * synchronized(cacheLock) {
 *     Info info = get(key);
 *     if (info == null) {
 *         info = new Info(key, ...);
 *         put(key, info);
 *     }
 *     info.registerXxx(...);
 * }
 * allowStore();
 * </pre>
 * <strong>Internal implementation details</strong>
 * These details are not in any way something that the user of the API may
 * rely on. Changes may be done to these details without notice.
 * <p/>
 * <strong>Collecting statistics:</strong>
 * The statistics are collected in an internal <code>Map cache</code>.
 * Each call to register statistics will update the corresponding object
 * within this cache. If the object does not exist, it will be created.
 * <p/>
 * <strong>Copy for storing:</strong>
 * At the time when there is time to store the information, all information
 * objects are copied (cloned) to <code>List storeList</code>. Note that the
 * objects in this list are never updated (they are a clone of the information
 * in the <code>cache</code>.
 * <p/>
 * <strong>Required locks (synchronization):</strong>
 * Access to both these structures must only be done when a lock is held on
 * <code>cacheLock</code>.
 * <p/>
 * <strong>Storing:</strong>
 * To store information, an information is retrieved (and deleted) in the
 * <code>storeList</code>(require a lock on <code>cacheLock</code>).
 * This information is updated in the database. If an update is not possible
 * (no matching row) there is a try to create the information in the database.
 * If that fails due to duplicate key (another server instances has created
 * the record between the update try and the create time), there is a new
 * try to update the information in the database.
 * <p/>
 * If the create/update succeed, the information stored is subtracted from the
 * information in the <code>cache</code> (requires lock on
 * <code>cacheLock</code>) to make sure the cache only holds
 * the information that has not been persisted. If the subtraction result in
 * an empty information object in the cache, it is deleted to prevent the
 * cache from growing.
 * <p/>
 * If the create/update fails, nothing is done which means the information
 * will still be in the <code>cache</code> and will be stored the next time.
 * <p/>
 * The observant reader notice that there is a small chance that
 * information is copied to the <code>storeList</code> at the same time as
 * an information object is stored in the database. If that happens, the
 * information will be stored twice. This will result in negative numbers
 * in the information object in the cache and it will due to that be
 * corrected the next time the information is stored persistent.
 * As a result, the failure will automatically be corrected after a while.
 * Note that strategies for choosing next update time are chosen to prevent
 * this scenario by always introduce a time between the storage of the last
 * information object in <code>storeList</code> and the next copy of new
 * objects to <code>storeList</code>.
 *
 * @param <Key>   The type of the key object. 
 * @param <Info>  The type of the information object.
 * 
 * @author Anders Persson (konx40)
 */
public abstract class BaseCollector<Key,
        Info extends BaseInformation<Key, Info>> {
    /** The log object for this class. */
    private final Log log = Log.getLog(getClass());

    /**
     * Lock object used at reads and updates of <code>cache</code> and
     * <code>storeList</code>.
     */
    protected final Object cacheLock = new Object();
    
    /**
     * Cache of all key-value pairs not yet stored persistent.
     * All access to this map have to be done when a lock on
     * <code>cacheLock</code> is held.
     */
    private final HashMap<Key, Info> cache = new HashMap<Key, Info>();

    /**
     * List of the information that is marked to be stored.
     * All access to this map have to be done when a lock on
     * <code>cacheLock</code> is held.
     */
    private final ArrayList<Info> storeList = new ArrayList<Info>();

    /**
     * The next time a store should be initiated. The copy is done the first
     * time this time is passed. If the time is passed more times and there
     * exists more information objects to store, those objects are stored.
     * All access to this attribute have to be done when a lock on
     * <code>cacheLock</code> is held.
     */
    private long nextStore;

    /**
     * The time interval for storing information.
     * This is the time in milliseconds between the last information object
     * is stored and the next copy to <code>storeList</code> of information
     * objects to store.
     */
    private final long storeInterval;

    /**
     * The maximum number of objects a single thread may have to store
     * before it is released.
     */
    private final int maxNumberOfStores;
    
    /** The SQL to perform update. */
    private final String updateSql;
    
    /** The SQL to perform insert. */
    private final String insertSql;
    
    /**
     * Create this instance.
     * 
     * @param storeInterval       The time interval for storing information.
     *                            This is the time in milliseconds between the
     *                            last information object is stored and the
     *                            next start to store information.
     * @param maxNumberOfStorages The maximum number of objects a single thread
     *                            may have to store before it is released.
     * @param updateSql           The SQL to update the information in
     *                            persistent store. The parameters are set by
     *                            {@link #populateUpdate(PreparedStatement, BaseInformation)}.
     * @param insertSql           The SQL to insert the information in
     *                            persistent store. The parameters are set by
     *                            {@link #populateInsert(PreparedStatement, BaseInformation)}.
     */
    protected BaseCollector(long storeInterval, int maxNumberOfStorages,
            String updateSql, String insertSql) {
        this.storeInterval = storeInterval;
        nextStore = System.currentTimeMillis() + storeInterval;
        this.maxNumberOfStores = maxNumberOfStorages;
        this.updateSql = updateSql;
        this.insertSql = insertSql;
    }
    
    /**
     * Get the statistics information object for the given key.
     * <p/>
     * Note the constraints regarding calling this method specified in the class
     * documentation for this class.
     * 
     * @param key The key to retrieve the information object for.
     * 
     * @return The information object or <code>null</code> if the information
     *         object does not exist.
     */
    protected final Info get(Key key) {
        return cache.get(key);
    }

    /**
     * Set the statistics information object for the given key.
     * <p/>
     * Note the constraints regarding calling this method specified in the class
     * documentation for this class. Also note that it is never allowed to
     * set an information object that already exists (and that is prevented
     * by the code given in the class documentation).
     * 
     * @param info The information object to set.
     */
    protected final void put(Info info) {
        cache.put(info.getKey(), info);
    }
    
    /**
     * Method called by the sub class <strong>outside</strong> the
     * synchronization block to allow this implementation to store
     * information in the cache if there are information marked to be stored.
     * <p/>
     * Note the constraints regarding calling this method specified in the class
     * documentation for this class. 
     */
    protected final void allowStore() {
        final long time = System.currentTimeMillis();
        synchronized(cacheLock) {
            if (time < nextStore) {
                return;
            }
            if (storeList.size() == 0) {
                copyToStoreList();
                if (storeList.size() == 0) {
                    nextStore = time + storeInterval;
                    return;
                }
            }
        }
        performStores(maxNumberOfStores);
    }

    /**
     * Copy information from the <code>cache</code> to the
     * <code>storeList</code>.
     * <p/>
     * This method should only be called when the <code>storeList</code>
     * is empty (even if the method internally also clear the list).
     * <p/>
     * Note that this method must only be called when a lock on
     * <code>cacheLock</code> is held.
     */
    private void copyToStoreList() {
        storeList.clear();
        for (Info info : cache.values()) {
            storeList.add(info.createClone());
        }
    }
    
    /**
     * <strong>Not allowed for other usage than to flush the cache when
     * the application shuts down</strong>. If used frequently, the performance
     * and possibly stability of the cache will be affected.
     * <p>
     * Perform a flush of the cache. This means that all information in the
     * cache is stored in the persistent store.
     * This method will perform the whole work and will not return until
     * the information is stored. Observe that information registered while
     * this method is performing will not be stored persistent but of course
     * reside in the cache.
     * <p/>
     * Observe that this method expose the possibility to double store
     * some information temporary. See class documentation for more information
     * about this.
     */
    public final void flush() {
        int size;
        synchronized(cacheLock) {
            storeList.clear();
            copyToStoreList();
            nextStore = System.currentTimeMillis() + storeInterval;
            size = storeList.size();
        }
        // This calculation is done to get a number of stores that are
        // expected to fit with good margin within the store interval.
        // One database operation is expected to take 10 ms and a maximum
        // of 3 database operation per object means max 30 ms per object.
        // This gives a factor 10 margin from that calculation.
        // The reason to update nextStore in each loop is to prevent the
        // normal automatic storing to be trigged to start while the flush
        // performs.
        int numberToStore = (int)(storeInterval / 300);
        if (numberToStore < 1) {
            numberToStore = 1;
        }
        while (size > 0) {
            performStores(numberToStore);
            synchronized(cacheLock) {
                nextStore = System.currentTimeMillis() + storeInterval;
                size = storeList.size();
            }
        }
    }

    /**
     * Method called <strong>outside</strong> of a synchronization block to
     * perform storage of at maximum <code>maxStores</code>
     * information objects. This method also update <code>nextStore</code>
     * if it should be updated.
     * 
     * @param maxStores The maximum number of information objects to store.
     * 
     * @return <code>true</code> if all expected information objects are stored.
     *         <code>false</code> if the store sequence ended with a failure.
     */
    private boolean performStores(int maxStores) {
        Connection connection = null;
        try {
            for (int storeNumber = 0; storeNumber < maxStores; storeNumber++) {
                Info infoToStore;
                synchronized(cacheLock) {
                    // Get the last object in list. Handle empty list.
                    if (storeList.size() == 0) {
                        nextStore = System.currentTimeMillis() + storeInterval;
                        return true;
                    }
                    infoToStore = storeList.remove(storeList.size() - 1);
                    if (storeList.size() == 0) {
                        nextStore = System.currentTimeMillis() + storeInterval;
                    }
                }
                boolean successfulStore = false;
                
                try {
                    if (connection == null) {
                        connection = DatabaseUtils.getConnection();
                    }
                    if (updateInformation(connection, infoToStore)) {
                        successfulStore = true;
                    } else if (insertInformation(connection, infoToStore)) {
                        successfulStore = true;
                    } else if (updateInformation(connection, infoToStore)) {
                        successfulStore = true;
                    }
                } catch (SQLException e) {
                    Dimensions.clear();
                    DatabaseUtils.reportDatabaseError();;
                    log.warning("Error when storing information in the " +
                            "database. This should not happens, but if it " +
                            "is not repeated it is not a problem.", e, null);
                    synchronized(cacheLock) {
                        // Stop storing and wait for an interval.
                        // This is to protect the application from
                        // spending time with trying to store in a database
                        // that is not available.
                        nextStore = System.currentTimeMillis() + storeInterval;
                        storeList.clear();
                        return false;
                    }
                }
                if (!successfulStore) {
                    log.warning("For some reason, the information could " +
                            "not be stored (neither updated nor created). " +
                            "This should not happens, but if it is not " +
                            "repeated it is not a problem", null, null);
                    synchronized(cacheLock) {
                        // Stop storing and wait for an interval.
                        // This is to protect the application from
                        // spending time with trying to store in a database
                        // that is not available.
                        nextStore = System.currentTimeMillis() + storeInterval;
                        storeList.clear();
                        return false;
                    }
                }
                synchronized(cacheLock) {
                    // Update after successful store
                    Info cachedInfo = cache.get(infoToStore.getKey());
                    if (cachedInfo == null) {
                        // May happen when a double store has been performed,
                        // because a call to the flush method has been done.
                        cachedInfo = infoToStore.createClone();
                        // Create an empty instance.
                        if (!cachedInfo.subtract(infoToStore)) {
                            log.error("An object subtracted from itself " +
                                    "did not give zero result which is a " +
                                    "programming error.", null, null);
                        }
                        cache.put(cachedInfo.getKey(), cachedInfo);
                    }
                    final boolean removeFromCache =
                            cachedInfo.subtract(infoToStore);
                    if (removeFromCache) {
                        cache.remove(infoToStore.getKey());
                    }
                }
            }
            return true;
        } finally {
            try {
                DatabaseUtils.close(connection, null, null);
            } catch (SQLException e) {
                log.error("Closing the connection generated an exception",
                        e, null);
            }
        }
    }
    
    /**
     * Insert the statistics information in the persistent store.
     * <p/>
     * This method <strong>have to</strong>:
     * <ul>
     * <li>Insert the information in the persistent store and return
     *     <code>true</code> if the information is successfully inserted.</li>
     * <li>Have to return <code>false</code> if the key is already in the
     *     persistent store (which may occur in clustered environments when
     *     the key may have been created by another instance).</li>
     * <li>Have to throw exception if it fails to create the key for other
     *     reasons than specified above.</li>
     * </ul>
     * 
     * @param connection The database connection to use.
     *                   This parameter is never <code>null</code>.
     * @param info       The information to create.
     *                   This parameter is never <code>null</code>.
     *             
     * @return <code>true</code> if the information is correctly inserted.
     *         <code>false</code> if the information fails due to duplicate
     *         key. 
     * 
     * @throws SQLException if the creation fails for other reasons than that
     *         the key already exists.
     */
    // This method has package visibility to allow testing from the test class.
    /* package */ boolean insertInformation(Connection connection,
                Info info) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(insertSql);
            populateInsert(statement, info);
            int numRows = statement.executeUpdate();
            if (numRows != 1) {
                throw new SQLException("The number of rows affected when " +
                        "creating the information is '" + numRows +
                        "' which is not correct. Class:" +
                        getClass().getName());
            }
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                // This is not a unique constraint, rethrow it
                throw e;
            }
            // This is a unique constraint, return false
            return false;
        } finally {
            DatabaseUtils.close(null, statement, null);
        }
    }
    
    /**
     * Insert the given information in the prepared statement.
     * 
     * @param ps   The prepared statement to insert the key in. This is the
     *             statement specified as <code>insertSql</code> in the
     *             constructor to this class. 
     * @param info The information insert. This is never <code>null</code>.
     *
     * @throws SQLException if there is an exception inserting the
     *         information in the prepared statement.
     */
    protected abstract void populateInsert(PreparedStatement ps, Info info)
            throws SQLException;

    /**
     * Update the statistics information in the persistent store.
     * <p/>
     * This method <strong>have to</strong>:
     * <ul>
     * <li>Update the information in the persistent store.</li>
     * <li>Return <code>true</code> if the information is successfully updated
     *     (which means 1 row is updated).</li>
     * <li>Return <code>false</code> if the information is not updated
     *     (which means 0 row is updated).</li>
     * <li>Have to throw exception if it fails to update the information for
     *     other reasons than that the record is not found.</li>
     * </ul>
     * 
     * @param connection The database connection to use.
     *                   This parameter is never <code>null</code>.
     * @param info       The information to update.
     *                   This parameter is never <code>null</code>.
     *             
     * @return <code>true</code> if the information is correctly updated.
     *         <code>false</code> if the record is not found. 
     * 
     * @throws SQLException if the creation fails for other reasons than that
     *         the record can not be found.
     */
    // This method has package visibility to allow testing from the test class.
    /* package */ boolean updateInformation(Connection connection,
            Info info) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(updateSql);
            populateUpdate(statement, info);
            int numRows = statement.executeUpdate();
            if (numRows > 1) {
                throw new SQLException("The number of rows affected when " +
                        "updating the information is '" + numRows +
                        "' which is not correct. Class:" +
                        getClass().getName());
            }
            return (numRows == 1);
        } finally {
            DatabaseUtils.close(null, statement, null);
        }
    }
    
    /**
     * Insert the given information in the prepared statement.
     * 
     * @param ps   The prepared statement to insert the key in. This is the
     *             statement specified as <code>updateSql</code> in the
     *             constructor to this class. 
     * @param info The information insert. This is never <code>null</code>.
     *
     * @throws SQLException if there is an exception updating the
     *         information in the prepared statement.
     */
    protected abstract void populateUpdate(PreparedStatement ps, Info info)
            throws SQLException;

    /**
     * Get the size of the <code>cache</code>.
     * This method is only published for test purposes which is the reason
     * for the package visibility.
     * 
     * @return The size of the cache.
     */
    /* package */ int getCacheSize() {
        synchronized(cacheLock) {
            return cache.size();
        }
    }

    /**
     * Get the size of the <code>storeList</code>.
     * This method is only published for test purposes which is the reason
     * for the package visibility.
     * 
     * @return The size of the storeList.
     */
    /* package */ int getStoreListSize() {
        synchronized(cacheLock) {
            return storeList.size();
        }
    }

    /**
     * Get the value of <code>nextStore</code>.
     * This method is only published for test purposes which is the reason
     * for the package visibility.
     * 
     * @return The value of <code>nextStore</code>.
     */
    /* package */ long getNextStore() {
        synchronized(cacheLock) {
            return nextStore;
        }
    }
}
