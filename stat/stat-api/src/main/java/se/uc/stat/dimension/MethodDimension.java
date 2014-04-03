package se.uc.stat.dimension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The method dimension. The key for this dimension is the
 * {@link MethodKey} containing service name and method name.
 * 
 * @author Anders Persson (konx40)
 */
public class MethodDimension extends BaseDimension<MethodKey> {
    /**
     * The SQL to insert a row in the dimension table.
     * 
     * param 1: The service name of the dimension.
     * param 1: The method name of the dimension.
     */
    private static final String INSERT_SQL =
            "insert into METHOD_INFO(METHOD_ID, SERVICE_NAME, METHOD_NAME) " +
            "values(METHOD_ID_SEQ.nextval, ?, ?)";
    
    /**
     * The SQL to select the full dimension table.
     * 
     * return 1: The id of the dimension.
     * return 2: The service name of the dimension.
     * return 3: The method name of the dimension.
     */
    private static final String SELECT_SQL =
            "select METHOD_ID, SERVICE_NAME, METHOD_NAME " +
            "from METHOD_INFO";
    
    /**
     * Package constructor to prevent instantiation outside the package.
     */
    /* package */ MethodDimension() {
        super(SELECT_SQL, INSERT_SQL);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected MethodKey preprocessKey(MethodKey key) {
        if (key == null) {
            return new MethodKey(NULL_NAME, NULL_NAME);
        }
        // Service name
        String serviceName = key.getServiceName();
        if (serviceName == null) {
            serviceName = NULL_NAME;
        } else {
            serviceName = serviceName.trim();
            if (serviceName.length() == 0) {
                serviceName = NULL_NAME;
            } else if (serviceName.length() > 100) {
                serviceName = serviceName.substring(0, 100);
            }
        }
        // Method name
        String methodName = key.getMethodName();
        if (methodName == null) {
            methodName = NULL_NAME;
        } else {
            methodName = methodName.trim();
            if (methodName.length() == 0) {
                methodName = NULL_NAME;
            } else if (methodName.length() > 100) {
                methodName = methodName.substring(0, 100);
            }
        }
        return new MethodKey(serviceName, methodName);
    }
    
    /**
     * {@inheritDoc}.
     */
    @Override
    protected MethodKey extractKey(ResultSet rs) throws SQLException {
        return new MethodKey(rs.getString(2), rs.getString(3));
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    protected void insertKey(PreparedStatement ps, MethodKey key)
            throws SQLException {
        ps.setString(1, key.getServiceName());
        ps.setString(2, key.getMethodName());
    }
}
