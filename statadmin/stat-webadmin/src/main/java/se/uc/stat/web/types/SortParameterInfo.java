package se.uc.stat.web.types;

import se.uc.stat.web.webtypes.GUISortParameterListRow;

/**
 * Class representing information about a sort parameter in the request
 * 
 * @author Anders Persson (konx40)
 */
public class SortParameterInfo implements GUISortParameterListRow {
    /** The column information of this list. */
    private final ColumnInfo columnInfo;

    /**
     * Create this object.
     * 
     * @param columnInfo        The column information of the list.
     *                          Must not be <code>null</code>.
     *             
     * @throws IllegalArgumentException if any of the constraints of the
     *         parameters specified above is not met.
     */
    public SortParameterInfo(ColumnInfo columnInfo) {
        if (columnInfo == null) {
            throw new IllegalArgumentException("columnInfo must not be null");
        }
        this.columnInfo = columnInfo;
    }

    /**
     * Get the (computer) name of the attribute.
     *
     * @return The (computer) name of the attribute.
     *         This method never returns <code>null</code>.
     */
    @Override
    public String getName() {
        return columnInfo.getName();
    }
    
    /**
     * Get the label of the attribute.
     *
     * @return The human readable name of the attribute.
     *         This method never returns <code>null</code>.
     */
    @Override
    public String getLabel() {
        return columnInfo.getLabel();
    }

    /**
     * Checks if this parameter is ignorable.
     *
     * @return <code>true</code> if this parameter may be ignored.
     *         <code>false</code> if the parameter must not be ignored.
     */
    @Override
    public boolean isIgnorable() {
        return columnInfo.isIgnorable();
    }

    /**
     * Get the column info representing this parameter.
     *
     * @return The column info representing this parameter.
     */
    @Override
    public ColumnInfo getColumnInfo() {
        return columnInfo;
    }
}
