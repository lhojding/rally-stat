package se.uc.stat.dimension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import se.uc.stat.utils.AbstractTestBase;
import se.uc.stat.utils.DatabaseUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Base class for dimension testing.
 * 
 * @param <Key> The type of the dimension key.
 * 
 * @author Anders Persson (konx40).
 */
public class BaseTestDimension<Key> extends AbstractTestBase {
    /** Configuration for this test. */
    private final DimensionTestConfig<Key> config =
            new DimensionTestConfig<Key>();

    /**
     * Perform a normal read test with a not existing key (first time)
     * and an existing key (second time).
     *  
     * @throws SQLException If the test fails.
     */
    @Test
    public void testGetWithAndWithoutDbInsert() throws SQLException {
        // Clean in database before test.
        for (DimensionTestConfig.KeyConfig<Key> testCase :
                getConfig().getKeyConfigs()) {
            deleteInDb(testCase);
        }
        config.dimension.clear();
        
        // Perform two gets in a row and compare the results.
        for (DimensionTestConfig.KeyConfig<Key> testCase :
                getConfig().getKeyConfigs()) {
            int id = config.dimension.getId(testCase.getKey());
            int idSecond = config.dimension.getId(testCase.getKey());
            assertEquals("Different ids returned when requesting the same key (" +
                    testCase.getKey() + ")", id, idSecond);
            int idDb = selectIdFromDb(testCase.sqlSelectKey());
            assertEquals("The key in the database does not match the key " +
                    "in the dimension (" + testCase.getKey() + ")", id, idDb);
            // Remove at this stage to force creation of all keys (even those
            // that after preprocessing will result in the same database record)
            deleteInDb(testCase);
            config.dimension.clear();
        }
            
        // Compare the ids to make sure they are unique when they should be.
        for (DimensionTestConfig.KeyConfig<Key> testCase1 :
            getConfig().getKeyConfigs()) {
            final int id1 = config.dimension.getId(testCase1.getKey());
            if (testCase1.getSameIdAsKey() != null) {
                // Check if it the same as the expected key.
                final int idSame = config.dimension.getId(
                        testCase1.getSameIdAsKey());
                assertEquals("Ids are expected to be the same but are not, " +
                        "key1=" + testCase1.getKey() + ", key2=" +
                        testCase1.getSameIdAsKey(), id1, idSame);
            } else {
                for (DimensionTestConfig.KeyConfig<Key> testCase2 :
                    getConfig().getKeyConfigs()) {
                    if (testCase2.getSameIdAsKey() == null &&
                            testCase1 != testCase2) {
                        // They are expected to be different.
                        // Note that there is a != comparison that is meant
                        // to be to see that it is not the exact same testcase.
                        final int id2 = config.dimension.getId(
                                testCase2.getKey());
                        assertTrue("Ids are expected to be different but " +
                                "are not, key1=" + testCase1.getKey() +
                                ", key2=" + testCase2.getKey(), id1 != id2);
                    }
                }
            }
        }

        // Clean in database after test.
        for (DimensionTestConfig.KeyConfig<Key> testCase :
                getConfig().getKeyConfigs()) {
            deleteInDb(testCase);
            if (testCase.getDeleteInDb()) {
                ensureNotInDb(testCase.sqlSelectKey());
            }
        }
        config.dimension.clear();
    }

    /**
     * Perform a test of the clean and caching functionality.
     *  
     * @throws SQLException If the test fails.
     */
    @Test
    public void testCleanAndCaching() throws SQLException {
        // Get one test case to use for this test.
        final DimensionTestConfig.KeyConfig<Key> testCase =
            config.getKeyConfigs().get(0);

        // Clean in database before test.
        config.dimension.clear();
        deleteInDb(testCase);
        ensureNotInDb(testCase.sqlSelectKey());
        
        // Perform two gets in a row where the database id is removed
        // between the retrieves.
        int id = config.dimension.getId(testCase.getKey());
        int idDb = selectIdFromDb(testCase.sqlSelectKey());
        deleteInDb(testCase);
        ensureNotInDb(testCase.sqlSelectKey());
        int idAfterDelete = config.dimension.getId(testCase.getKey());
        assertEquals("Different ids returned when requesting the same key (" +
                testCase.getKey() + ")", id, idAfterDelete);
        assertEquals("The key in the database does not match the key " +
                "in the dimension (" + testCase.getKey() + ")", id, idDb);

        // Perform a clear and make sure the record is reread and recreated.
        config.dimension.clear();
        int idAfterClear = config.dimension.getId(testCase.getKey());
        int idDbAfterClear = selectIdFromDb(testCase.sqlSelectKey());
        assertTrue("Same ids returned when requesting the same key (" +
                testCase.getKey() + ") after remove in database and clear",
                id != idAfterClear);
        assertEquals("The key in the database does not match the key " +
                "in the dimension after clear(" + testCase.getKey() + ")",
                idAfterClear, idDbAfterClear);
        
        // Clean in database after test.
        deleteInDb(testCase);
    }

    /**
     * Perform a test of the insert of duplicate keys.
     *  
     * @throws SQLException If the test fails.
     */
    @Test
    public void testDuplicateKey() throws SQLException {
        // Get one test case to use for this test.
        final DimensionTestConfig.KeyConfig<Key> testCase =
            config.getKeyConfigs().get(0);
        // Get one test case to use to force reading of the cache.
        final DimensionTestConfig.KeyConfig<Key> forceReadCase =
            config.getKeyConfigs().get(1);

        // Clean in database before test.
        config.dimension.clear();
        deleteInDb(testCase);
        ensureNotInDb(testCase.sqlSelectKey());
        
        // Ensure the cache is read after clean.
        config.dimension.getId(forceReadCase.getKey());
        
        // Create in database.
        updateInDb(config.sqlInsertKey0);
        int idDb = selectIdFromDb(testCase.sqlSelectKey());

        // Perform a get operation that will try to insert in the database
        int id = config.dimension.getId(testCase.getKey());
        assertEquals("Invalid id returned when duplicate keys were detected (" +
                testCase.getKey() + ")", idDb, id);
        
        // Clean in database after test.
        deleteInDb(testCase);
        deleteInDb(forceReadCase);
    }

    /**
     * Perform a test that will cause an SQL exception when inserting the record.
     */
    @Test
    public void testInvalidInsertInStoreKey() {
        // Perform the operation
        try {
            config.dimension.storeKey(config.keyInvalid);
            fail("The operation did not throw an exception");
        } catch (SQLException e) {
            // Expected exception.
        }
    }

    /**
     * Get the configuration of this test class.
     * 
     * @return The configuration of this test class.
     */
    protected DimensionTestConfig<Key> getConfig() {
        return config;
    }
    
    /**
     * Delete a test key in the database. The query is only performed
     * if the record should be deleted.
     * 
     * @param testCase The key to delete.
     * 
     * @throws SQLException if there is an error executing the SQL.
     */
    private void deleteInDb(DimensionTestConfig.KeyConfig<Key> testCase)
            throws SQLException {
        if (testCase.getDeleteInDb()) {
            updateInDb(testCase.sqlDeleteKey());
        }
    }

    /**
     * Execute a select SQL statement to retrieve an id. This method fails
     * in a JUnit way if no row is found or if more than one row is returned.
     * 
     * @param sql The SQL to execute. Must result in one row with one column
     *            holding the id.
     *            
     * @return The requested id.
     * 
     * @throws SQLException if there is an error executing the SQL.
     */
    protected int selectIdFromDb(String sql) throws SQLException {
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            int id = 0;
            statement = getConnection().prepareStatement(sql);
            result = statement.executeQuery();
            if (result.next()) {
                id = result.getInt(1);
                if (result.next()) {
                    fail("The query for an id returned more than one row");
                }
            } else {
                fail("The query for an id did not return any id");
            }
            return id;
        } finally {
            DatabaseUtils.close(null, statement, result);
        }
    }

    /**
     * Execute a select SQL statement to ensure a key is not in the database.
     * This method fails in a JUnit way if one or more row is found.
     * 
     * @param sql The SQL to execute. Must not result in any row if the key
     *            can not be found.
     * 
     * @throws SQLException if there is an error executing the SQL.
     */
    protected void ensureNotInDb(String sql) throws SQLException {
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            statement = getConnection().prepareStatement(sql);
            result = statement.executeQuery();
            if (result.next()) {
                fail("The query '" + sql + "' did return a result.");
            }
        } finally {
            DatabaseUtils.close(null, statement, result);
        }
    }
}
