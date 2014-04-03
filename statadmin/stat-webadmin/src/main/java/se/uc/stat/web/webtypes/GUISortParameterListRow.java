package se.uc.stat.web.webtypes;

import se.uc.stat.web.types.ColumnInfo;

/**
 * Interface specifying the methods needed to be presented in a GUI list
 * for sortable parameters.
 * This interface is used to be able to handle the different entities in a
 * standardized way in the presentation.
 * 
 * @author Anders Persson (konx40)
 */
public interface GUISortParameterListRow {
    /**
     * Get the (computer) name of the attribute.
     *
     * @return The (computer) name of the attribute.
     *         This method never returns <code>null</code>.
     */
    public String getName();
    
    /**
     * Get the label of the attribute.
     *
     * @return The human readable name of the attribute.
     *         This method never returns <code>null</code>.
     */
    public String getLabel();

    /**
     * Checks if this parameter is ignorable.
     *
     * @return <code>true</code> if this parameter may be ignored.
     *         <code>false</code> if the parameter must not be ignored.
     */
    public boolean isIgnorable();

    /**
     * Get the column info representing this parameter.
     *
     * @return The column info representing this parameter.
     */
    public ColumnInfo getColumnInfo();
}
