package se.uc.stat.web;

import java.sql.SQLException;
import java.util.Map;

/**
 * Services for statistics web GUI.
 * 
 * @author Anders Persson (konx40)
 *
 */
public class Services {
    /**
     * Get the view information for customer statistics.
     * 
     * @param requestParameters The parameters to this request.
     *                          The type is chosen to match
     *                          <code>ServletRequest.getParameterMap()</code>.
     *                          Must not be <code>null</code>.
     * 
     * @return The view information for customer statistics.
     *         This method never returns <code>null</code>.
     * 
     * @throws SQLException if there is a problem retrieving the information.
     * @throws IllegalArgumentException if any of the conditions above
     *         is not met.
     */
    public CustomerStatistics getCustomerStatistics(
            @SuppressWarnings("rawtypes") Map requestParameters)
            throws SQLException {
        return new CustomerStatistics(requestParameters);
    }
    
    /**
     * Get the view information for time statistics.
     * 
     * @param requestParameters The parameters to this request.
     *                          The type is chosen to match
     *                          <code>ServletRequest.getParameterMap()</code>.
     *                          Must not be <code>null</code>.
     * 
     * @return The view information for time statistics.
     *         This method never returns <code>null</code>.
     * 
     * @throws SQLException if there is a problem retrieving the information.
     * @throws IllegalArgumentException if any of the conditions above
     *         is not met.
     */
    public TimeStatistics getTimeStatistics(
            @SuppressWarnings("rawtypes") Map requestParameters)
            throws SQLException {
        return new TimeStatistics(requestParameters);
    }
}
