package se.uc.stat.web.types;

/**
 * The aggregation function of the column.
 * 
 * @author Anders Persson (konx40)
 */
public enum ColumnAggregation {
    /** Group aggregation. */
    GROUP(),
    /** Sum aggregation. */
    SUM();

    /**
     * Create this class.
     */
    private ColumnAggregation() {
        // Nothing to do.
    }
}
