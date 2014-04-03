package se.uc.stat.web;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import se.uc.stat.web.statistics.StatisticsResult;
import se.uc.stat.web.statistics.TimeStatisticsResult;
import se.uc.stat.web.types.ColumnInfo;
import se.uc.stat.web.types.HourOfDay;
import se.uc.stat.web.types.LayerInfo;
import se.uc.stat.web.types.SortParameterInfo;
import se.uc.stat.web.webtypes.GUIList;

/**
 * This is an information container for view information.
 * The container is responsible for populating itself using the services.
 * This class holds the parts that are specific for time statistics
 * while the base class holds the parts that are common for with
 * the customer statistics.
 * 
 * @author Anders Persson (konx40)
 */
public class TimeStatistics extends BaseContainer {
    /** The statistics result of <code>null</code> if not initialized. */
    private TimeStatisticsResult statisticsResult = null;
    /** A list of available layers. */
    private final List<LayerInfo> layerInfoList;
    /** A list of available hours of the day. */
    private final List<HourOfDay> hourOfDayList;

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
    /* package */ TimeStatistics(
            @SuppressWarnings("rawtypes") Map requestParameters)
            throws SQLException {
        super(requestParameters);
        layerInfoList = dbServices.getLayerInfoList();
        hourOfDayList = internalServices.getHourOfDayList();
        addGUIList(2, new GUIList(ColumnInfo.LAYER, layerInfoList));
        addGUIList(new GUIList(ColumnInfo.HOUR_OF_DAY, hourOfDayList));
        // Add extra to sort parameters
        addSortParameterInfo(1, new SortParameterInfo(ColumnInfo.LAYER));
        addSortParameterInfo(5, new SortParameterInfo(ColumnInfo.DATE_HOUR));
        addSortParameterInfo(6, new SortParameterInfo(ColumnInfo.HOUR_OF_DAY));
    }
    
    /**
     * Get the type of the statistics.
     * 
     * @return The type of statistics.
     *         This method always returns <code>Type.TIME</code>.
     */
    @Override
    public Type getType() {
        return Type.TIME;
    }
    
    /**
     * Get the list of layer information ordered in a human readable way.
     * 
     * @return The list of layer info.
     *         This method never returns <code>null</code>.
     */
    public Collection<LayerInfo> getLayerInfoList() {
        return layerInfoList;
    }
    
    /**
     * Get the list of hour of the day ordered in a human readable way.
     * 
     * @return The list of hours of the day.
     *         This method never returns <code>null</code>.
     */
    public Collection<HourOfDay> getHourOfDayList() {
        return hourOfDayList;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public StatisticsResult getStatisticsResult() throws SQLException {
        if (statisticsResult == null) {
            statisticsResult = new TimeStatisticsResult(this);
        }
        return statisticsResult;
    }
}
