package se.uc.stat.dimension;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration of the dimension test.
 *
 * @param <Key> The type of the dimension key.
 * 
 * @author Anders Persson (konx40)
 */
public class DimensionTestConfig<Key> {
    /** The dimension implementation to test. */
    public BaseDimension<Key> dimension = null;
    /** The name of the database table. */
    public String tableName = null;
    /** The name of the id column in the table. */
    public String idColumn = null;
    /** The key configurations to test. */
    private List<KeyConfig<Key>> keyConfigs = new ArrayList<KeyConfig<Key>>();
    /** The test key causing an SQL exception. */
    public Key keyInvalid = null;
    /** The SQL to insert the key for the first test case in the database. */
    public String sqlInsertKey0 = null;

    /**
     * Add a key configuration to test.
     * 
     * @param key         The key.
     * @param keyWhere    The where clause to find the key.
     * @param deleteInDb  <code>true</code> if the key should be deleted
     *                    before/after the test.
     * @param sameIdAsKey If <code>sameIdAsKey</code> is given, it is
     *                    expected to have the same id as the id for
     *                    <code>key</code>.
     *                    If <code>null</code>, the id for
     *                    <code>key</code> is expected to be unique.
     */
    @SuppressWarnings("synthetic-access")
    public void addKeyConfig(Key key, String keyWhere, boolean deleteInDb,
            Key sameIdAsKey) {
        if (keyConfigs.size() < 2 && !deleteInDb) {
            throw new RuntimeException("The first and second key " +
            		"configuration have to be possible to delete in the " +
            		"database");
        }
        keyConfigs.add(new KeyConfig<Key>(this, key, keyWhere, deleteInDb,
                sameIdAsKey));
    }
    
    /**
     * Get the test key configurations.
     * 
     * @return A list of the test key configurations.
     *         This method never returns <code>null</code>.
     */
    public List<KeyConfig<Key>> getKeyConfigs() {
        return keyConfigs;
    }
    
    /**
     * Configuration for one test key.
     * 
     * @param <Key> The type of the key.
     */
    public static class KeyConfig<Key> {
        /** The parent container. */
        private final DimensionTestConfig<Key> parent;
        /** The test key. */
        private final Key key;
        /** The where clause to find key. */
        private final String keyWhere;
        /**
         * <code>true</code> if the key should be deleted before/after the test.
         */
        private final boolean deleteInDb;
        /**
         * If <code>sameIdAsKey</code> is given, it is expected to have the
         * same id as the id for <code>key</code>. If <code>null</code>,
         * the id for <code>key</code> is expected to be unique.
         */
        private final Key sameIdAsKey;
        
        /**
         * Create this instance
         * 
         * @param parent      The parent container.
         * @param key         The key.
         * @param keyWhere    The where clause to find the key.
         * @param deleteInDb  <code>true</code> if the key should be deleted
         *                    before/after the test.
         * @param sameIdAsKey If <code>sameIdAsKey</code> is given, it is
         *                    expected to have the same id as the id for
         *                    <code>key</code>.
         *                    If <code>null</code>, the id for
         *                    <code>key</code> is expected to be unique.
         */
        private KeyConfig(DimensionTestConfig<Key> parent, Key key,
                String keyWhere, boolean deleteInDb, Key sameIdAsKey) {
            this.parent = parent;
            this.key = key;
            this.keyWhere = keyWhere;
            this.deleteInDb = deleteInDb;
            this.sameIdAsKey = sameIdAsKey;
        }

        /**
         * Get the key for this test case.
         * 
         * @return The key for this test case.
         */
        public Key getKey() {
            return key;
        }
        
        /**
         * Check if this test case should have a unizue id.
         * 
         * @return If <code>sameIdAsKey</code> is given, it is expected to
         *         have the same id as the id for <code>key</code>.
         *         If <code>null</code>, the id for <code>key</code>
         *         is expected to be unique.
         */
        public Key getSameIdAsKey() {
            return sameIdAsKey;
        }
        
        /**
         * Checks if this should be deleted in the database. Note that the
         * method {@link #sqlDeleteKey()} takes case of this, but that this
         * flag may be used if a check for deletion is done.
         * 
         * @return <code>true</code> if it should be deleted in the database.
         *         <code>false</code> if it should not be deleted in the
         *         database.
         */
        public boolean getDeleteInDb() {
            return deleteInDb;
        }
        
        /**
         * Get the SQL to delete the key. Note that {@link #getDeleteInDb()}
         * have to be checked to see if the key should be deleted.
         * 
         * @return The SQL to delete the key.
         */
        public String sqlDeleteKey() {
            return "delete " + parent.tableName + " where " + keyWhere;
        }

        /**
         * Get the SQL to select the key.
         * 
         * @return The SQL to select the key.
         */
        public String sqlSelectKey() {
            return "select " + parent.idColumn + " FROM " + parent.tableName +
                    " where " + keyWhere;
        }
    }
}
