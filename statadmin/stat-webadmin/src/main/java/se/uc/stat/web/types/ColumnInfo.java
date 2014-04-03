package se.uc.stat.web.types;

/**
 * Class holding static information about the columns.
 * 
 * @author Anders Persson (konx40)
 */
public enum ColumnInfo {
    /** The service column. */
    SERVICE("service", "Tj&auml;nst", "Tj&auml;nst", true, ColumnGroup.METHOD,
            "SERVICE_NAME", JoinInfo.METHOD_INFO, ColumnType.STRING,
            ColumnAggregation.GROUP, null),
    /** The method column. */
    METHOD("method", "Metod", "Metod", true, ColumnGroup.METHOD,
            "METHOD_NAME", JoinInfo.METHOD_INFO, ColumnType.STRING,
            ColumnAggregation.GROUP, null),
    /** The origin column. */
    ORIGIN("origin", "Ursprung", "Urspr.", true, null,
            "ORIGIN_NAME", JoinInfo.ORIGIN_INFO, ColumnType.STRING,
            ColumnAggregation.GROUP, null),
    /** The media column. */
    MEDIA("media", "Media", "Media", true, null,
            "MEDIA_NAME", JoinInfo.MEDIA_INFO, ColumnType.STRING,
            ColumnAggregation.GROUP, null),
    /** The layer column. */
    LAYER("layer", "Lager", "Lager", true, null,
            "LAYER_NAME", JoinInfo.LAYER_INFO, ColumnType.STRING,
            ColumnAggregation.GROUP, null),
    /** The product column. */
    PRODUCT("product", "Produkt", "Produkt", true, null,
            "PRODUCT", null, ColumnType.STRING,
            ColumnAggregation.GROUP, null),
    /** The customer column. */
    CUSTOMER("customer", "Kund", "Kund", true, null,
            "CUSTOMER", null, ColumnType.STRING,
            ColumnAggregation.GROUP, null),
    /** The date column. */
    DATE("date", "Datum", "Datum", true, ColumnGroup.TIME,
            "STATISTICS_DATE", null, ColumnType.DATE,
            ColumnAggregation.GROUP, null),
    /** The date column. */
    DATE_HOUR("dateHour", "Datum", "Datum", true, ColumnGroup.TIME,
            "STATISTICS_TIME", null, ColumnType.DATE_HOUR,
            ColumnAggregation.GROUP, null),
    /** The day of week column. */
    DAY_OF_WEEK("dayOfWeek", "Veckodag", "Dag", true, ColumnGroup.TIME,
            "DAY_OF_WEEK_NAME", JoinInfo.DAY_OF_WEEK, ColumnType.STRING,
            ColumnAggregation.GROUP, null),
    /** The hour of day column. */
    HOUR_OF_DAY("hourOfDay", "Timme p&aring; dagen", "Timme", true,
            ColumnGroup.TIME, "HOUR_OF_DAY", null, ColumnType.LONG,
            ColumnAggregation.GROUP, null),
    /** The number of correct calls column. */
    NUM_CORRECT_CALLS("numCorrectCalls", "Antal korrekta anrop", "Korrekt", false,
            ColumnGroup.NUM_CALLS, "NUM_CORRECT_CALLS", null,
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The number of invalid calls column. */
    NUM_INVALID_CALLS("numInvalidCalls", "Antal kundfel", "Kundfel", false,
            ColumnGroup.NUM_CALLS, "NUM_INVALID_CALLS", null,
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The number of failed calls column. */
    NUM_FAILED_CALLS("numFailedCalls", "Antal tekniska fel", "Teknikfel", false,
            ColumnGroup.NUM_CALLS, "NUM_FAILED_CALLS", null,
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The average time correct calls column. */
    AVERAGE_TIME_CORRECT("averageTimeCorrect", "Medelsvarstid korrekta anrop",
            "Korrekt", false, ColumnGroup.AVERAGE_TIME,
            "TOTAL_TIME_CORRECT_CALLS", null, ColumnType.LONG,
            ColumnAggregation.SUM, NUM_CORRECT_CALLS),
    /** The average time invalid calls column. */
    AVERAGE_TIME_INVALID("averageTimeInvalid", "Medelsvarstid kundfel anrop",
            "Kundfel", false, ColumnGroup.AVERAGE_TIME,
            "TOTAL_TIME_INVALID_CALLS", null, ColumnType.LONG,
            ColumnAggregation.SUM, NUM_INVALID_CALLS),
    /** The average time failed calls column. */
    AVERAGE_TIME_FAILED("averageTimeFailed", "Medelsvarstid tekniskt fel anrop",
            "Teknikfel", false, ColumnGroup.AVERAGE_TIME,
            "TOTAL_TIME_FAILED_CALLS", null, ColumnType.LONG,
            ColumnAggregation.SUM, NUM_FAILED_CALLS),
    /** The number of calls 0-10 ms column. */
    NUM_CALLS_10("numMax10ms", "Antal -10 ms", "&minus;10<br/>ms",
            false, ColumnGroup.NUM_CALLS_TIME_INTERVAL, "NUM_10", null, 
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The number of calls 11-20 ms column. */
    NUM_CALLS_20("numMax20ms", "Antal 11-20 ms", "&minus;20<br/>ms",
            false, ColumnGroup.NUM_CALLS_TIME_INTERVAL, "NUM_20", null, 
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The number of calls 21-50 ms column. */
    NUM_CALLS_50("numMax50ms", "Antal 21-50 ms", "&minus;50<br/>ms",
            false, ColumnGroup.NUM_CALLS_TIME_INTERVAL, "NUM_50", null, 
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The number of calls 51-100 ms column. */
    NUM_CALLS_100("numMax100ms", "Antal 51-100 ms", "&minus;100<br/>ms",
            false, ColumnGroup.NUM_CALLS_TIME_INTERVAL, "NUM_100", null, 
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The number of calls 101-200 ms column. */
    NUM_CALLS_200("numMax200ms", "Antal 101-200 ms", "&minus;200<br/>ms",
            false, ColumnGroup.NUM_CALLS_TIME_INTERVAL, "NUM_200", null, 
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The number of calls 201-500 ms column. */
    NUM_CALLS_500("numMax500ms", "Antal 201-500 ms", "&minus;500<br/>ms",
            false, ColumnGroup.NUM_CALLS_TIME_INTERVAL, "NUM_500", null, 
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The number of calls 501-1000 ms column. */
    NUM_CALLS_1000("numMax1000ms", "Antal 501-1000 ms", "&minus;1<br/>s",
            false, ColumnGroup.NUM_CALLS_TIME_INTERVAL, "NUM_1000", null, 
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The number of calls 1001-2000 ms column. */
    NUM_CALLS_2000("numMax2000ms", "Antal 1001-2000 ms", "&minus;2<br/>s",
            false, ColumnGroup.NUM_CALLS_TIME_INTERVAL, "NUM_2000", null, 
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The number of calls 2001-5000 ms column. */
    NUM_CALLS_5000("numMax5000ms", "Antal 2001-5000 ms", "&minus;5<br/>s",
            false, ColumnGroup.NUM_CALLS_TIME_INTERVAL, "NUM_5000", null, 
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The number of calls 5001-10000 ms column. */
    NUM_CALLS_10000("numMax10000ms", "Antal 5001-10000 ms", "&minus;10<br/>s",
            false, ColumnGroup.NUM_CALLS_TIME_INTERVAL, "NUM_10000", null, 
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The number of calls 10001-20000 ms column. */
    NUM_CALLS_20000("numMax20000ms", "Antal 10001-20000 ms", "&minus;20<br/>s",
            false, ColumnGroup.NUM_CALLS_TIME_INTERVAL, "NUM_20000", null, 
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null),
    /** The number of calls 20001- ms column. */
    NUM_CALLS_OVER_20000("numOver20000ms", "Antal 20001- ms", "20&minus;<br/>s",
            false, ColumnGroup.NUM_CALLS_TIME_INTERVAL, "NUM_OVER_20000", null, 
            ColumnType.LONG_NO_ZEROES, ColumnAggregation.SUM, null);
    
    /** The (computer) name of the column. */
    private final String name;
    /** The label of the column. */
    private final String label;
    /** The headline label of the column. */
    private final String headlineLabel;
    /** <code>true</code> if the parameter may be ignored. */
    private final boolean ignorable;
    /** The column group of <code>true</code> if not part of a group. */
    private final ColumnGroup columnGroup;
    /** The name of the column. */
    private final String columnName;
    /** The join criteria (if applicable) or <code>null</code> otherwise. */
    private final JoinInfo join;
    /** The column type. */
    private final ColumnType columnType;
    /** The column aggregation. */
    private final ColumnAggregation columnAggregation;
    /** The column demominator if applicable or <code>null</code> otherwise. */
    private final ColumnInfo denominator;
    
    /**
     * Create this class.
     * 
     * @param name          The name of the column.
     *                      Must not be <code>null</code>.
     * @param label         The label of the column.
     *                      Must not be <code>null</code>.
     * @param headlineLabel The headline label of the column (when used in a
     *                      table header). Must not be <code>null</code>.
     * @param ignorable     <code>true</code> if the parameter may be ignored.
     * @param columnGroup   The column group or <code>null</code> it this
     *                      column does not belong to a group.
     * @param columnName    The name of the database column.
     *                      Must not be <code>null</code>.
     * @param join          The join criteria to reach the column.
     *                      <code>null</code> if join is not needed.
     * @param columnType    The column type. Must not be <code>null</code>.
     * @param columnAggr    The column aggregation function.
     *                      Must not be <code>null</code>.
     * @param denominator   The column denominator or <code>null</code> if
     *                      not applicable.
     *                     
     * @throws IllegalArgumentException if any of the constraints specified
     *         is not met.
     */
    private ColumnInfo(String name, String label, String headlineLabel,
            boolean ignorable, ColumnGroup columnGroup, String columnName,
            JoinInfo join, ColumnType columnType,
            ColumnAggregation columnAggr, ColumnInfo denominator) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        this.name = name;
        if (label == null) {
            throw new IllegalArgumentException("label must not be null");
        }
        this.label = label;
        if (headlineLabel == null) {
            throw new IllegalArgumentException(
                    "headlineLabel must not be null");
        }
        this.headlineLabel = headlineLabel;
        this.ignorable = ignorable;
        this.columnGroup = columnGroup;
        if (columnName == null) {
            throw new IllegalArgumentException("columnName must not be null");
        }
        this.columnName = columnName;
        this.join = join;
        if (columnType == null) {
            throw new IllegalArgumentException("columnType must not be null");
        }
        this.columnType = columnType;
        if (columnAggr == null) {
            throw new IllegalArgumentException("columnAggr must not be null");
        }
        this.columnAggregation = columnAggr;
        this.denominator = denominator;
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
     * Get the headline label of this column used in table headlines.
     * 
     * @return The headline label of this column.
     *         This method never returns <code>null</code>.
     */
    public String getHeadlineLabel() {
        return headlineLabel;
    }

    /**
     * Get the ignorable flag.
     * 
     * @return <code>true</code> if the parameter may be ignored.
     *         <code>false</code> if the parameter must not be ignored.
     */
    public boolean isIgnorable() {
        return ignorable;
    }

    /**
     * Get the column group.
     * 
     * @return The column group of this column.
     *         <code>null</code> if not part of a column group.
     */
    public ColumnGroup getColumnGroup() {
        return columnGroup;
    }

    /**
     * Get the name of the database column to reach this information.
     * 
     * @return The name of the database column.
     *         This method never returns <code>null</code>.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Get the join criteria to reach the column.
     * 
     * @return The join criteria to reach the column.
     *         <code>null</code> if no join criteria is needed.
     */
    public JoinInfo getJoin() {
        return join;
    }

    /**
     * Get the column type for the database column.
     * 
     * @return The column type for the database column.
     *         This method never returns <code>null</code>.
     */
    public ColumnType getColumnType() {
        return columnType;
    }

    /**
     * Get the column aggregation for the database column.
     * 
     * @return The column aggregation for the database column.
     *         This method never returns <code>null</code>.
     */
    public ColumnAggregation getColumnAggregation() {
        return columnAggregation;
    }

    /**
     * Get the denominator column for average calculation.
     * 
     * @return The denominator column or <code>null</code> if not applicable.
     */
    public ColumnInfo getDenominator() {
        return denominator;
    }
}
