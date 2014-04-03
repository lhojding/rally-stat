package se.uc.stat.web.types;

/**
 * Class holding static information about the column groups.
 * 
 * @author Anders Persson (konx40)
 */
public enum ColumnGroup {
    /** The execution method. */
    METHOD("Metod"),
    /** The time. */
    TIME("Tid"),
    /** The number of calls. */
    NUM_CALLS("Antal anrop"),
    /** The average time for calls. */
    AVERAGE_TIME("Medeltid (ms)"),
    /** The number of calls per time interval. */
    NUM_CALLS_TIME_INTERVAL("Antal anrop per intervall");
    
    /** The label of the column. */
    private final String label;
    
    /**
     * Create this class.
     * 
     * @param label        The label of the column.
     *                     Must not be <code>null</code>.
     *                     
     * @throws IllegalArgumentException if any of the constraints specified
     *         is not met.
     */
    private ColumnGroup(String label) {
        if (label == null) {
            throw new IllegalArgumentException("label must not be null");
        }
        this.label = label;
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
}
