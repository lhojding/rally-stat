package se.uc.stat.basestatistics;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class publishing some of the methods with package visibility to be able to
 * test them.
 * 
 * @author Anders Persson (konx40)
 */
public class BaseCollectorUtils {
    /**
     * Call the method insertInformation.
     * 
     * @param <Key>      The type of the key object.
     * @param <Info>     The type of the information object.
     * 
     * @param collector  The collector to call.
     * @param connection The connection parameter to the method.
     * @param info       The info parameter to the method.
     *             
     * @return The result of the method.
     * 
     * @throws SQLException thrown by the method.
     */
    public static <Key, Info extends BaseInformation<Key, Info>> boolean
            insertInformation(BaseCollector<Key, Info> collector,
            Connection connection, Info info) throws SQLException {
        return collector.insertInformation(connection, info);
    }

    /**
     * Call the method updateInformation.
     * 
     * @param <Key>      The type of the key object.
     * @param <Info>     The type of the information object.
     * 
     * @param collector  The collector to call.
     * @param connection The connection parameter to the method.
     * @param info       The info parameter to the method.
     *             
     * @return The result of the method.
     * 
     * @throws SQLException thrown by the method.
     */
    public static <Key, Info extends BaseInformation<Key, Info>> boolean
            updateInformation(BaseCollector<Key, Info> collector,
            Connection connection, Info info) throws SQLException {
        return collector.updateInformation(connection, info);
    }
}
