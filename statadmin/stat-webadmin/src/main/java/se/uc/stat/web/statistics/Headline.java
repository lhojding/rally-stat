package se.uc.stat.web.statistics;

import se.uc.stat.web.types.ColumnInfo;

/**
 * Class holding one headline for the statistics.
 * 
 * @author Anders Persson (konx40)
 */
public class Headline {
    /** The label of the headline. */
    private final String label;
    /**
     * The label of the column group or <code>null</code> if this headline
     * does not belong to a column group.
     */
    private final String groupLabel;
    
    /**
     * Create this class.
     * 
     * @param columnInfo The columnInfo of the headline.
     *                   Must not be <code>null</code>.
     * 
     * @throws IllegalArgumentException if any of the constraints specified
     *         is not met.
     */
    /* package */ Headline(ColumnInfo columnInfo) {
        if (columnInfo == null) {
            throw new IllegalArgumentException("columnInfo must not be null");
        }
        if (columnInfo.getColumnGroup() != null) {
            groupLabel = columnInfo.getColumnGroup().getLabel();
        } else {
            groupLabel = null;
        }
        label = columnInfo.getHeadlineLabel();
    }
    
    /**
     * Get the label of this headline.
     * 
     * @return The label of this headline.
     *         This method never returns <code>null</code>.
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * Get the column group label of this headline.
     * 
     * @return The column group label of this headline.
     *         <code>null</code> if not part of a column group.
     */
    public String getGroupLabel() {
        return groupLabel;
    }
}
