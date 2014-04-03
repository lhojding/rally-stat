package se.uc.stat.web.types;

/**
 * Class holding static information about joins.
 * 
 * @author Anders Persson (konx40)
 */
public enum JoinInfo {
    /** The METHOD_INFO table. */
    METHOD_INFO("METHOD_INFO", "METHOD_ID", "METHOD_ID"),
    /** The LAYER_INFO table. */
    LAYER_INFO("LAYER_INFO", "LAYER_ID", "LAYER_ID"),
    /** The ORIGIN_INFO table. */
    ORIGIN_INFO("ORIGIN_INFO", "ORIGIN_ID", "ORIGIN_ID"),
    /** The MEDIA_INFO table. */
    MEDIA_INFO("MEDIA_INFO", "MEDIA_ID", "MEDIA_ID"),
    /** The DAY_OF_WEEK table. */
    DAY_OF_WEEK("DAY_OF_WEEK", "DAY_OF_WEEK_ID", "DAY_OF_WEEK_ID");
    
    /** The name of the table. */
    private final String tableName;
    /** The key column in the table specified by <code>tableName</code>. */
    private final String keyColumn;
    /** The key column in the foreign table. */
    private final String foreignKeyColumn;
    
    /**
     * Create this class.
     * 
     * @param tableName        The name of the table.
     *                         Must not be <code>null</code>.
     * @param keyColumn        The key column in the table specified by
     *                         <code>tableName</code>.
     *                         Must not be <code>null</code>.
     * @param foreignKeyColumn The key column in the foreign table.
     *                         Must not be <code>null</code>.
     *                     
     * @throws IllegalArgumentException if any of the constraints specified
     *         is not met.
     */
    private JoinInfo(String tableName, String keyColumn,
            String foreignKeyColumn) {
        if (tableName == null) {
            throw new IllegalArgumentException("tableName must not be null");
        }
        this.tableName = tableName;
        if (keyColumn == null) {
            throw new IllegalArgumentException("keyColumn must not be null");
        }
        this.keyColumn = keyColumn;
        if (foreignKeyColumn == null) {
            throw new IllegalArgumentException(
                    "foreignKeyColumn must not be null");
        }
        this.foreignKeyColumn = foreignKeyColumn;
    }

    /**
     * Get the table name.
     * 
     * @return The table name.
     *         This method never returns <code>null</code>.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Get the key column for the table <code>getTableName()</code>.
     * 
     * @return The key column.
     *         This method never returns <code>null</code>.
     */
    public String getKeyColumn() {
        return keyColumn;
    }

    /**
     * Get the key column for the foreign table.
     * 
     * @return The key column for the foreign table.
     *         This method never returns <code>null</code>.
     */
    public String getForeignKeyColumn() {
        return foreignKeyColumn;
    }
}
