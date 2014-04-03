package se.uc.stat.web.statistics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import se.uc.stat.web.BaseContainer;
import se.uc.stat.web.types.ColumnInfo;

/**
 * Class holding the parts of the statistics result that is specific to
 * customer statistics.
 * 
 * @author Anders Persson (konx40)
 */
public class CustomerStatisticsResult extends StatisticsResult {
    /**
     * Create this class.
     * 
     * @param parent The parent container. Must not be <code>null</code>.
     * 
     * @throws IllegalArgumentException if any of the constraints specified
     *         is not met.
     * @throws SQLException if there is a problem accessing the database.
     */
    public CustomerStatisticsResult(BaseContainer parent) throws SQLException {
        super(parent, "CUSTOMER_STAT");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    /* package */ List<ColumnInfo> getColumnInfos() {
        ArrayList<ColumnInfo> result = new ArrayList<ColumnInfo>();
        result.add(ColumnInfo.SERVICE);
        result.add(ColumnInfo.METHOD);
        result.add(ColumnInfo.ORIGIN);
        result.add(ColumnInfo.MEDIA);
        result.add(ColumnInfo.PRODUCT);
        result.add(ColumnInfo.CUSTOMER);
        result.add(ColumnInfo.DATE);
        result.add(ColumnInfo.DAY_OF_WEEK);
        result.add(ColumnInfo.NUM_CORRECT_CALLS);
        result.add(ColumnInfo.NUM_INVALID_CALLS);
        result.add(ColumnInfo.NUM_FAILED_CALLS);

        return result;
    }
}
