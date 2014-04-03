package se.uc.stat.web.types;

/**
 * Class representing the possible sorting/grouping/ignoring combinations.
 * 
 * @author Anders Persson (konx40)
 */
public enum SortType {
    /** Sort ascending. */
    ASCENDING("ascending", "Stigande", false, false, false),
    /** Sort descending. */
    DESCENDING("descending", "Fallande", true, false, false),
    /** Group and sort ascending. */
    GROUP_ASCENDING("ascendinggroup", "Stigande gruppera", false, true,
            false),
    /** Group and sort descending. */
    GROUP_DESCENDING("descendinggroup", "Fallande gruppera", true, true,
            false),
    /** Ignore. */
    IGNORE("ignore", "Uteslut", false, false, true);
    
    /** The (computer) name of the column. */
    private final String name;

    /** The label of the column. */
    private final String label;

    /**
     * <code>true</code> for descending sorting.
     * <code>false</code> for ascending sorting.
     */
    private final boolean descending;

    /**
     * <code>true</code> for grouping.
     * <code>false</code> for no grouping.
     */
    private final boolean grouping;

    /**
     * <code>true</code> to ignore the parameter.
     * <code>false</code> to show it.
     */
    private final boolean ignoring;
    /**
     * Create this enum.
     * 
     * @param name       The name of the sort type.
     *                   Must not be <code>null</code>.
     * @param label      The label of the sort type.
     *                   Must not be <code>null</code>.
     * @param descending <code>true</code> for descending sorting.
     *                   <code>false</code> for ascending sorting.
     * @param grouping   <code>true</code> for grouping.
     *                   <code>false</code> for no grouping.
     * @param ignoring   <code>true</code> to ignore the parameter.
     *                   <code>false</code> to show it.
     *                     
     * @throws IllegalArgumentException if any of the constraints specified
     *         is not met.
     */
    private SortType(String name, String label, boolean descending,
            boolean grouping, boolean ignoring) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        this.name = name;
        if (label == null) {
            throw new IllegalArgumentException("label must not be null");
        }
        this.label = label;
        this.descending = descending;
        this.grouping = grouping;
        this.ignoring = ignoring;
    }
    
    /**
     * Get the (computer) name of this column.
     * 
     * @return The (computer) name of this column.
     *         This method never returns <code>null</code>.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the label of this column.
     * 
     * @return The label of this column.
     *         This method never returns <code>null</code>.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the sorting.
     * 
     * @return <code>true</code> for descending sorting.
     *         <code>false</code> for ascending sorting.
     */
    public boolean isDescending() {
        return descending;
    }

    /**
     * Get the grouping.
     * 
     * @return <code>true</code> for grouping.
     *         <code>false</code> for no grouping.
     */
    public boolean isGrouping() {
        return grouping;
    }

    /**
     * Get ignoring.
     * 
     * @return <code>true</code> to ignore the parameter.
     *         <code>false</code> to show it.
     */
    public boolean isIgnored() {
        return ignoring;
    }
}
