package se.uc.stat.dimension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The layer dimension. The key for this dimension is the layer name.
 * 
 * @author Anders Persson (konx40)
 */
public class LayerDimension extends BaseDimension<String> {
    /**
     * The SQL to insert a row in the dimension table.
     * 
     * param 1: The name of the dimension.
     */
    private static final String INSERT_SQL =
            "insert into LAYER_INFO(LAYER_ID, LAYER_NAME) " +
            "values(LAYER_ID_SEQ.nextval, ?)";
    
    /**
     * The SQL to select the full dimension table.
     * 
     * return 1: The id of the dimension.
     * return 2: The name of the dimension.
     */
    private static final String SELECT_SQL =
            "select LAYER_ID, LAYER_NAME " +
            "from LAYER_INFO";
    
    /**
     * Package constructor to prevent instantiation outside the package.
     */
    /* package */ LayerDimension() {
        super(SELECT_SQL, INSERT_SQL);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected String preprocessKey(String key) {
        if (key == null) {
            return NULL_NAME;
        }
        String processedKey = key.trim();
        if (processedKey.length() == 0) {
            return NULL_NAME;
        }
        if (processedKey.length() > 30) {
            return processedKey.substring(0, 30);
        }
        return processedKey;
    }
    
    /**
     * {@inheritDoc}.
     */
    @Override
    protected String extractKey(ResultSet rs) throws SQLException {
        return rs.getString(2);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    protected void insertKey(PreparedStatement ps, String key)
            throws SQLException {
        ps.setString(1, key);
    }
}
