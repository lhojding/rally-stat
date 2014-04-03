package se.uc.stat.web.statistics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import se.uc.stat.web.BaseContainer;
import se.uc.stat.web.types.ColumnInfo;

/**
 * Class holding the parts of the statistics result that is specific to
 * time statistics.
 * 
 * @author Anders Persson (konx40)
 */
public class TimeStatisticsResult extends StatisticsResult {
    /**
     * Create this class.
     * 
     * @param parent The parent container. Must not be <code>null</code>.
     * 
     * @throws IllegalArgumentException if any of the constraints specified
     *         is not met.
     * @throws SQLException if there is a problem accessing the database.
     */
    public TimeStatisticsResult(BaseContainer parent) throws SQLException {
        super(parent, "TIME_STAT");
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
        result.add(ColumnInfo.LAYER);
        result.add(ColumnInfo.PRODUCT);
        result.add(ColumnInfo.DATE_HOUR);
        result.add(ColumnInfo.DAY_OF_WEEK);
        result.add(ColumnInfo.HOUR_OF_DAY);
        result.add(ColumnInfo.NUM_CORRECT_CALLS);
        result.add(ColumnInfo.NUM_INVALID_CALLS);
        result.add(ColumnInfo.NUM_FAILED_CALLS);
        result.add(ColumnInfo.AVERAGE_TIME_CORRECT);
        result.add(ColumnInfo.AVERAGE_TIME_INVALID);
        result.add(ColumnInfo.AVERAGE_TIME_FAILED);
        result.add(ColumnInfo.NUM_CALLS_10);
        result.add(ColumnInfo.NUM_CALLS_20);
        result.add(ColumnInfo.NUM_CALLS_50);
        result.add(ColumnInfo.NUM_CALLS_100);
        result.add(ColumnInfo.NUM_CALLS_200);
        result.add(ColumnInfo.NUM_CALLS_500);
        result.add(ColumnInfo.NUM_CALLS_1000);
        result.add(ColumnInfo.NUM_CALLS_2000);
        result.add(ColumnInfo.NUM_CALLS_5000);
        result.add(ColumnInfo.NUM_CALLS_10000);
        result.add(ColumnInfo.NUM_CALLS_20000);
        result.add(ColumnInfo.NUM_CALLS_OVER_20000);

        return result;
    }
}
