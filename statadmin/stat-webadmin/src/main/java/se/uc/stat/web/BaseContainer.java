package se.uc.stat.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se.uc.stat.web.statistics.StatisticsResult;
import se.uc.stat.web.types.ColumnInfo;
import se.uc.stat.web.types.DayOfWeek;
import se.uc.stat.web.types.MediaInfo;
import se.uc.stat.web.types.MethodInfo;
import se.uc.stat.web.types.OriginInfo;
import se.uc.stat.web.types.ServiceInfo;
import se.uc.stat.web.types.SortOrderInfo;
import se.uc.stat.web.types.SortParameterInfo;
import se.uc.stat.web.types.SortType;
import se.uc.stat.web.webtypes.GUIForm;
import se.uc.stat.web.webtypes.GUIList;

/**
 * This is an information container for view information.
 * The container is responsible for populating itself using the services.
 * This base class holds the parts that are common for for both customer and
 * time statistics.
 * 
 * @author Anders Persson (konx40)
 */
public abstract class BaseContainer {
    /** The type of statistics. */
    public enum Type {
        /** The type of statistics is time statistics. */
        TIME,
        /** The type of statistics is customer statistics. */
        CUSTOMER
    }
    /** The database services to retrieve the information from.*/
    protected final DBServices dbServices;
    /** The internal services to retrieve the information from.*/
    protected final InternalServices internalServices;
    /** A list of available services. */
    private final List<ServiceInfo> serviceInfoList =
        new ArrayList<ServiceInfo>();
    /** A list of available methods. */
    private final List<MethodInfo> methodInfoList;
    /** A list of available origins. */
    private final List<OriginInfo> originInfoList;
    /** A list of available media. */
    private final List<MediaInfo> mediaInfoList;
    /** A list of available days of week. */
    private final List<DayOfWeek> dayOfWeekList;
    /** A list of all option lists this object supports. */
    private final List<GUIList> guiLists = new ArrayList<GUIList>();
    /** A list of sort/group parameters. */
    private final List<SortParameterInfo> sortParameterInfoList = new ArrayList<SortParameterInfo>();
    /** A list of sort/group orders. */
    private final List<SortOrderInfo> sortOrderInfoList = new ArrayList<SortOrderInfo>();
    /** The parameters to the request. */
    private Map<String, String[]> requestParameters = null;
    /**
     * The gui form for this request or
     * <code>null</code> if it has not been initialized.
     */
    private GUIForm guiForm = null;
    
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
    @SuppressWarnings("unchecked")
    /* package */ BaseContainer(
            @SuppressWarnings("rawtypes") Map requestParameters)
            throws SQLException {
        if (requestParameters == null) {
            throw new IllegalArgumentException(
                    "requestParameters must not be null");
        }
        this.requestParameters = requestParameters;
        
        dbServices = new DBServices();
        internalServices = new InternalServices();
        methodInfoList = dbServices.getMethodInfoList();
        originInfoList = dbServices.getOriginInfoList();
        mediaInfoList = dbServices.getMediaInfoList();
        dayOfWeekList = dbServices.getDayOfWeekList();
        
        // Populate serviceInfoList
        ServiceInfo latestService = null;
        for (MethodInfo method : methodInfoList) {
            if (latestService == null || !method.getServiceName().equals(
                    latestService.getServiceName())) {
                latestService = new ServiceInfo(method.getServiceName());
                serviceInfoList.add(latestService);
                latestService.addId(method.getId());
            } else {
                latestService.addId(method.getId());
            }
        }
        
        // Populate guiLists
        addGUIList(new GUIList(ColumnInfo.SERVICE, serviceInfoList));
        addGUIList(new GUIList(ColumnInfo.METHOD, methodInfoList));
        addGUIList(new GUIList(ColumnInfo.ORIGIN, originInfoList));
        addGUIList(new GUIList(ColumnInfo.MEDIA, mediaInfoList));
        addGUIList(new GUIList(ColumnInfo.DAY_OF_WEEK, dayOfWeekList));
        // Add extra to sort parameters
        addSortParameterInfo(new SortParameterInfo(ColumnInfo.SERVICE));
        addSortParameterInfo(new SortParameterInfo(ColumnInfo.METHOD));
        addSortParameterInfo(new SortParameterInfo(ColumnInfo.ORIGIN));
        addSortParameterInfo(new SortParameterInfo(ColumnInfo.MEDIA));
        addSortParameterInfo(new SortParameterInfo(ColumnInfo.DAY_OF_WEEK));
        addSortParameterInfo(new SortParameterInfo(ColumnInfo.PRODUCT));
        addSortParameterInfo(new SortParameterInfo(ColumnInfo.NUM_CORRECT_CALLS));
        addSortParameterInfo(new SortParameterInfo(ColumnInfo.NUM_INVALID_CALLS));
        addSortParameterInfo(new SortParameterInfo(ColumnInfo.NUM_FAILED_CALLS));
        // Populate sort order list
        addSortOrderInfo(new SortOrderInfo(SortType.ASCENDING));
        addSortOrderInfo(new SortOrderInfo(SortType.DESCENDING));
        addSortOrderInfo(new SortOrderInfo(SortType.GROUP_ASCENDING));
        addSortOrderInfo(new SortOrderInfo(SortType.GROUP_DESCENDING));
        addSortOrderInfo(new SortOrderInfo(SortType.IGNORE));
    }

    /**
     * Add an object to the GUI list.
     * 
     * @param guiList The GUI list to add. Must not be <code>null</code>.
     *             
     * @throws IllegalArgumentException if any of the constraints of the
     *         parameters specified above is not met.
     */
    protected void addGUIList(GUIList guiList) {
        if (guiList == null) {
            throw new IllegalArgumentException("guiList must not be null");
        }
        guiLists.add(guiLists.size(), guiList);
    }

    /**
     * Add an object to the GUI list.
     * 
     * @param index   The index where the object should be added.
     * @param guiList The GUI list to add. Must not be <code>null</code>.
     *             
     * @throws IllegalArgumentException if any of the constraints of the
     *         parameters specified above is not met.
     */
    protected void addGUIList(int index, GUIList guiList) {
        if (guiList == null) {
            throw new IllegalArgumentException("guiList must not be null");
        }
        guiLists.add(index, guiList);
    }

    /**
     * Add an object to the sort parameter list.
     * 
     * @param sortParameterInfo The sort parameter to add.
     *                          Must not be <code>null</code>.
     *             
     * @throws IllegalArgumentException if any of the constraints of the
     *         parameters specified above is not met.
     */
    protected void addSortParameterInfo(SortParameterInfo sortParameterInfo) {
        if (sortParameterInfo == null) {
            throw new IllegalArgumentException(
                    "sortParameterInfo must not be null");
        }
        addSortParameterInfo(sortParameterInfoList.size(), sortParameterInfo);
    }

    /**
     * Add an object to the sort parameter list.
     * 
     * @param index             The index where the object should be added.
     * @param sortParameterInfo The sort parameter to add.
     *                          Must not be <code>null</code>.
     *             
     * @throws IllegalArgumentException if any of the constraints of the
     *         parameters specified above is not met.
     */
    protected void addSortParameterInfo(int index,
            SortParameterInfo sortParameterInfo) {
        if (sortParameterInfo == null) {
            throw new IllegalArgumentException(
                    "sortParameterInfo must not be null");
        }
        sortParameterInfoList.add(index, sortParameterInfo);
    }

    /**
     * Add an object to the sort order list.
     * 
     * @param sortOrderInfo The sort order to add.
     *                      Must not be <code>null</code>.
     *             
     * @throws IllegalArgumentException if any of the constraints of the
     *         parameters specified above is not met.
     */
    protected void addSortOrderInfo(SortOrderInfo sortOrderInfo) {
        if (sortOrderInfo == null) {
            throw new IllegalArgumentException("sortOrderInfo must not be null");
        }
        sortOrderInfoList.add(sortOrderInfo);
    }

    /**
     * Get the type of the statistics.
     * 
     * @return The type of statistics.
     *         This method never returns <code>null</code>.
     */
    public abstract Type getType();
    
    /**
     * Get the list of service information ordered in a human readable way.
     * 
     * @return The list of service info.
     *         This method never returns <code>null</code>.
     */
    public List<ServiceInfo> getServiceInfoList() {
        return serviceInfoList;
    }

    /**
     * Get the list of method information ordered in a human readable way.
     * 
     * @return The list of method info.
     *         This method never returns <code>null</code>.
     */
    public List<MethodInfo> getMethodInfoList() {
        return methodInfoList;
    }

    /**
     * Get the list of origin information ordered in a human readable way.
     * 
     * @return The list of origin info.
     *         This method never returns <code>null</code>.
     */
    public List<OriginInfo> getOriginInfoList() {
        return originInfoList;
    }

    /**
     * Get the list of media information ordered in a human readable way.
     * 
     * @return The list of media info.
     *         This method never returns <code>null</code>.
     */
    public List<MediaInfo> getMediaInfoList() {
        return mediaInfoList;
    }

    /**
     * Get the list of days of week ordered in a human readable way.
     * 
     * @return The list of day of week.
     *         This method never returns <code>null</code>.
     */
    public List<DayOfWeek> getDayOfWeekList() {
        return dayOfWeekList;
    }

    /**
     * Get the GUI lists.
     * 
     * @return The GUI lists.
     *         This method never returns <code>null</code>.
     */
    public List<GUIList> getGUIListList() {
        return guiLists;
    }
    
    /**
     * Get the sort parameters.
     * 
     * @return The sort parameters. This method never returns <code>null</code>.
     */
    public List<SortParameterInfo> getSortParameterInfoList() {
        return sortParameterInfoList;
    }
    
    /**
     * Get the sort orders.
     * 
     * @return The sort orders. This method never returns <code>null</code>.
     */
    public List<SortOrderInfo> getSortOrderInfoList() {
        return sortOrderInfoList;
    }
    
    /**
     * Get the gui form for the request.
     * 
     * @return The gui form for this request.
     *         This method never returns <code>null</code>.
     */
    public GUIForm getGUIForm() {
        if (guiForm == null) {
            guiForm = new GUIForm(this, requestParameters);
        }
        return guiForm;
    }
    
    /**
     * Get the search result for this request.
     * 
     * @return The search result for this request.
     *         This method never returns <code>null</code>.
     *         
     * @throws SQLException if the database could not be accessed.
     */
    public abstract StatisticsResult getStatisticsResult() throws SQLException;
}
