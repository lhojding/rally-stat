package se.uc.stat.web;

import java.sql.SQLException;
import java.util.Map;

import se.uc.stat.web.statistics.CustomerStatisticsResult;
import se.uc.stat.web.statistics.StatisticsResult;
import se.uc.stat.web.types.ColumnInfo;
import se.uc.stat.web.types.SortParameterInfo;

/**
 * This is an information container for view information.
 * The container is responsible for populating itself using the services.
 * This class holds the parts that are specific for customer statistics
 * while the base class holds the parts that are common for with
 * the time statistics.
 * 
 * @author Anders Persson (konx40)
 */
public class CustomerStatistics extends BaseContainer {
    /** The statistics result of <code>null</code> if not initialized. */
    private CustomerStatisticsResult statisticsResult = null;
    
    /**
     * Create this class.
     * 
     * @param requestParameters The parameters to this request.
     *                          The type is chosen to match
     *                          <code>ServletRequest.getParameterMap()</code>.
     *                          Must not be <code>null</code>.
     * 
     * @throws SQLException if there is a problem retrieving the information
     *         from the database.
     * @throws IllegalArgumentException if any of the conditions above
     *         is not met.
     */
    /* package */ CustomerStatistics(
            @SuppressWarnings("rawtypes") Map requestParameters)
            throws SQLException {
        super(requestParameters);
        // Add sort parameters
        addSortParameterInfo(4, new SortParameterInfo(ColumnInfo.DATE));
        addSortParameterInfo(6, new SortParameterInfo(ColumnInfo.CUSTOMER));
    }

    /**
     * Get the type of the statistics.
     * 
     * @return The type of statistics.
     *         This method always returns <code>Type.CUSTOMER</code>.
     */
    @Override
    public Type getType() {
        return Type.CUSTOMER;
    }
    
    /**
     * {@inheritDoc}.
     */
    @Override
    public StatisticsResult getStatisticsResult() throws SQLException {
        if (statisticsResult == null) {
            statisticsResult = new CustomerStatisticsResult(this);
        }
        return statisticsResult;
    }
}
