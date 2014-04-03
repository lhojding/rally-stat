package se.uc.stat.web.webtypes;

import java.util.List;

import se.uc.stat.web.types.ColumnInfo;

/**
 * Class representing a list (option) in the GUI.
 * 
 * @author Anders Persson (konx40)
 */
public class GUIList {
    /** The column information of this list. */
    private final ColumnInfo columnInfo;
    /** The selected string or <code>null</code> if nothing is selected. */
    private String selected;
    /** The rows in the list. */
    private final List<? extends GUIListRow> list;
    
    /**
     * Create this object. Note that
     * {@link #setRequestParameters(GUIRequestParameters)} should be
     * called before using other methods in this class.
     * 
     * @param columnInfo        The column information of the list.
     *                          Must not be <code>null</code>.
     * @param list              The list.
     *                          Must not be <code>null</code>.
     *             
     * @throws IllegalArgumentException if any of the constraints of the
     *         parameters specified above is not met.
     */
    public GUIList(ColumnInfo columnInfo, List<? extends GUIListRow> list) {
        if (columnInfo == null) {
            throw new IllegalArgumentException("columnInfo must not be null");
        }
        this.columnInfo = columnInfo;
        if (list == null) {
            throw new IllegalArgumentException("list must not be null");
        }
        this.list = list;
    }
    
    /**
     * Use the request parameters to set the selected property.
     * 
     * @param requestParameters Interface to read request parameters from.
     *                          <code>null</code> if no request parameters
     *                          should be read.
     */
    public void setRequestParameters(GUIRequestParameters requestParameters) {
        final String param = requestParameters.getParameter(getName());
        GUIListRow row = null;
        for (GUIListRow listRow : list) {
            if (listRow.getPresentationId().equals(param)) {
                row = listRow;
            }
        }
        if (row == null) {
            selected = null;
        } else {
            selected = row.getPresentationId();
        }
    }
    
    /**
     * Get the (computer) name of the list.
     *
     * @return The (computer) name of the list.
     *         This method never returns <code>null</code>.
     */
    public String getName() {
        return columnInfo.getName();
    }
    
    /**
     * Get the label of the list.
     *
     * @return The human readable name of the list.
     *         This method never returns <code>null</code>.
     */
    public String getLabel() {
        return columnInfo.getLabel();
    }
    
    /**
     * Get the selected value for the row.
     *
     * @return The selected value for the row.
     *         <code>null</code> if nothing has been selected.
     */
    public String getSelected() {
        return selected;
    }

    /**
     * Get the list rows.
     *
     * @return The list rows.
     *         This method never returns <code>null</code>.
     */
    public List<? extends GUIListRow> getList() {
        return list;
    }
    
    /**
     * Get the column information for this parameter.
     * 
     * @return The column info for this parameter.
     *         This method never returns <code>null</code>.
     */
    public ColumnInfo getColumnInfo() {
        return columnInfo;
    }
}
