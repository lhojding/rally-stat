package se.uc.stat.web.statistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import se.uc.stat.web.BaseContainer;
import se.uc.stat.web.DBUtilities;
import se.uc.stat.web.types.ColumnAggregation;
import se.uc.stat.web.types.ColumnInfo;
import se.uc.stat.web.types.JoinInfo;
import se.uc.stat.web.types.SortType;
import se.uc.stat.web.webtypes.GUIForm;
import se.uc.stat.web.webtypes.GUIList;
import se.uc.stat.web.webtypes.GUISortOrderListRow;
import se.uc.stat.web.webtypes.GUISortParameterListRow;
import se.uc.stat.web.webtypes.GUISortRow;

/**
 * Class holding the statistics result.
 * 
 * @author Anders Persson (konx40).
 */
public abstract class StatisticsResult {
    /** The columns to group by in the order they should be grouped. */
    private final ArrayList<ColumnInfo> groupBys = new ArrayList<ColumnInfo>();
    /** The columns to render in the order they should be rendered. */
    private final ArrayList<ColumnInfo> attributes =
            new ArrayList<ColumnInfo>();
    /** The sort attributes of the query. */
    private final ArrayList<SortAttribute> sortAttributes =
            new ArrayList<SortAttribute>();
    /** The columns to ignore. */
    private final List<ColumnInfo> ignores = new ArrayList<ColumnInfo>();
    /**
     * The attributes in the query. The sql query is built to return these
     * columns in the same order.
     */
    private final List<ColumnInfo> queryAttributes =
            new ArrayList<ColumnInfo>();
    /**
     * The denominators. The denominator for the attribute
     * queryAttributes[i] is located in attribute
     * queryAttribute.get(denominators[i]) if there is a denominator.
     * If there is no denominator, denominators[i] < 0;
     */
    private final int denominators[];
    /** The headlines of the result. */
    private final Headlines headlines = new Headlines();
    /** The parent container. */
    private final BaseContainer parent;
    /** The name of the statistics table. */
    private final String statisticsTableName;
    /** The SQL parameters to set in the prepared statement. */
    private final List<Object> queryParameters =
            new ArrayList<Object>();
    /** The search result. */
    private final List<ResultRow> resultRows = new ArrayList<ResultRow>();
    
    /**
     * Create this class.
     * 
     * @param parent              The parent container.
     *                            Must not be <code>null</code>.
     * @param statisticsTableName The name of the statistics table.
     *                            Must not be <code>null</code>.
     * 
     * @throws IllegalArgumentException if any of the constraints specified
     *         is not met.
     *         
     * @throws SQLException if there is a problem accessing the database.
     */
    public StatisticsResult(BaseContainer parent, String statisticsTableName) 
            throws SQLException {
        if (parent == null) {
            throw new IllegalArgumentException("parent must not be null");
        }
        this.parent = parent;
        if (statisticsTableName == null) {
            throw new IllegalArgumentException(
                    "statisticsTableName must not be null");
        }
        this.statisticsTableName = statisticsTableName;
        processColumnInfos();
        populateHeadlines();
        // Calculate query attributes.
        denominators = new int[groupBys.size() + attributes.size()];
        Arrays.fill(denominators, -1);
        int index = 0;
        for (ColumnInfo groupBy : groupBys) {
            queryAttributes.add(groupBy);
            index++;
        }
        for (ColumnInfo attribute : attributes) {
            queryAttributes.add(attribute);
            if (attribute.getDenominator() != null) {
                final int pos = queryAttributes.indexOf(attribute.getDenominator());
                if (pos < 0) {
                    throw new IllegalArgumentException(
                            "The denominator attribute have to exist and " +
                            "be before the average attribute");
                }
                denominators[index] = pos;
            }
            index++;
        }
        runQuery();
    }

    /**
     * Process <code>columnInfos</code> and populate <code>groupBys</code> and
     * <code>attributes</code> and <code>ignores</code> according to the
     * query parameters set in the GUI.
     */
    private void processColumnInfos() {
        List<ColumnInfo> unhandled = getColumnInfos();
        for (GUISortRow guiSortRow :
                getParent().getGUIForm().getSort().getSortRows()) {
            handleGuiSortRow(unhandled, guiSortRow);
        }
        // Add the remaining columns
        for (ColumnInfo columnInfo : unhandled) {
            attributes.add(columnInfo);
        }
    }
    
    /**
     * Get the columns in this result in the expected base order.
     * 
     * @return The columns in this result. 
     *         This method must never return <code>null</code>.
     */
    /* package */ abstract List<ColumnInfo> getColumnInfos();

    /**
     * Populate the headlines structure.
     */
    private void populateHeadlines() {
        for (ColumnInfo groupBy : groupBys) {
            addGroupedHeadline(groupBy);
        }
        for (ColumnInfo attribute : attributes) {
            addHeadline(attribute);
        }
    }

    /**
     * Handle one sort row.
     * 
     * @param columnInfos The remaining columns to "pick from". Must not be
     *                    <code>null</code>. If a column is found, it will
     *                    be removed from this list.
     * @param guiSortRow  The row to process. Must not be <code>null</code>.
     */
    private void handleGuiSortRow(List<ColumnInfo> columnInfos,
            GUISortRow guiSortRow) {
        final String selectedParameter = guiSortRow.getSelectedParameter();
        final String selectedOrder = guiSortRow.getSelectedOrder();
        if (selectedParameter == null || selectedOrder == null) {
            // Nothing to do.
            return;
        }
        // Find the sort type
        SortType sortType = null;
        for (GUISortOrderListRow sortOrder : guiSortRow.getOrderRows()) {
            if (sortOrder.getName().equals(selectedOrder)) {
                sortType = sortOrder.getSortType();
            }
        }
        // Find the column info
        ColumnInfo columnInfo = null;
        for (GUISortParameterListRow sortParameter :
                guiSortRow.getParameterRows()) {
            if (sortParameter.getName().equals(selectedParameter)) {
                columnInfo = sortParameter.getColumnInfo();
            }
        }
        if (columnInfo == null || sortType == null) {
            // Nothing to do.
            return;
        }
        int index = columnInfos.indexOf(columnInfo);
        if (index < 0) {
            // Nothing to do.
            return;
        }
        if (sortType.isGrouping()) {
            groupBys.add(columnInfo);
            sortAttributes.add(new SortAttribute(columnInfo, sortType));
            columnInfos.remove(index);
        } else if (sortType.isIgnored()) {
            ignores.add(columnInfo);
            columnInfos.remove(index);
        } else {
            sortAttributes.add(new SortAttribute(columnInfo, sortType));
        }
    }
    
    /**
     * Add a grouped headline for the statistics result. The headlines should
     * be added in the view order.
     * 
     * @param columnInfo The headline to add. Must not be <code>null</code>.
     */
    private void addGroupedHeadline(ColumnInfo columnInfo) {
        headlines.addGroupedHeadline(new Headline(columnInfo));
    }
    
    /**
     * Add a not grouped headline for the statistics result. The headlines
     * should be added in the view order.
     * 
     * @param columnInfo The headline to add. Must not be <code>null</code>.
     */
    private void addHeadline(ColumnInfo columnInfo) {
        headlines.addHeadline(new Headline(columnInfo));
    }
    
    /**
     * Get the headlines for the statistics.
     * 
     * @return The headlines for the statistics.
     *         This method never returns <code>null</code>.
     */
    public Headlines getHeadlines() {
        return headlines;
    }
    
    /**
     * Get the result rows.
     * 
     * @return The result rows. This method never returns <code>null</code>.
     */
    public List<ResultRow> getResultRows() {
        return resultRows;
    }
    
    /**
     * Get the base container for this result.
     * 
     * @return The parent base container for this result.
     *         This method never returns <code>null</code>.
     */
    /* package */ BaseContainer getParent() {
        return parent;
    }
    
    /**
     * Run the query and populate the <code>resultRows</code>.
     * 
     * @throws SQLException if there is a problem retrieving the information.
     */
    private void runQuery() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            int index = 0;
            int firstSumColumn = 0;
            
            for (ColumnInfo attribute : queryAttributes) {
                if (!ColumnAggregation.GROUP.equals(
                        attribute.getColumnAggregation())) {
                    if (firstSumColumn == 0) {
                        firstSumColumn = index;
                    }
                }
                index++;
            }
            String sql = getSql();
            conn = DBUtilities.createConnection();
            ps = conn.prepareStatement(sql);
            int parameterIndex = 1;
            for (Object param : queryParameters) {
                if (param instanceof String) {
                    ps.setString(parameterIndex, (String)param);
                } else if (param instanceof Long) {
                    ps.setLong(parameterIndex, ((Long)param).longValue());
                } else if (param instanceof Integer) {
                    ps.setInt(parameterIndex, ((Integer)param).intValue());
                } else if (param instanceof Date) {
                    final Timestamp ts = new Timestamp(((Date)param).getTime());
                    ps.setTimestamp(parameterIndex, ts);
                }
                parameterIndex++;
            }
            rs = ps.executeQuery();
            final int maxRows = parent.getGUIForm().getMaxRows();
            int rowNum = 0;
            final Group groups[] = new Group[groupBys.size()];
            while (rs.next() && rowNum < maxRows) {
                // Grouped attributes
                for (int rsIndex = 0; rsIndex < groupBys.size(); rsIndex++) {
                    final ColumnInfo columnInfo = queryAttributes.get(rsIndex);
                    ResultSetCell resultSetCell = columnInfo.getColumnType().
                            getRead().read(rs, rsIndex + 1);
                    if (groups[rsIndex] == null || !groups[rsIndex].getName().
                            equals(resultSetCell.getString())) {
                        for (int groupIndex = rsIndex;
                                groupIndex < groups.length; groupIndex++) {
                            setResultInGroup(groups[groupIndex],
                                    firstSumColumn);
                            groups[groupIndex] = null;
                        }
                        // Create new row.
                        final ResultRow row = new ResultRow(rsIndex);
                        resultRows.add(row);
                        groups[rsIndex] = new Group(resultSetCell.getString(),
                                queryAttributes.size(), row);
                        final ResultCell cell = new ResultCell(
                                firstSumColumn - rsIndex, 
                                columnInfo.getHeadlineLabel() + ": " +
                                resultSetCell.getString(), true, false);
                        row.addResultCell(cell);
                    }
                }
                // Not grouped attributes
                final ResultRow row = new ResultRow(groupBys.size());
                final long numericValues[] = new long[queryAttributes.size()];
                for (int rsIndex = groupBys.size();
                        rsIndex < queryAttributes.size(); rsIndex++) {
                    final ColumnInfo columnInfo = queryAttributes.get(rsIndex);
                    ResultSetCell resultSetCell = columnInfo.getColumnType().
                            getRead().read(rs, rsIndex + 1);
                    numericValues[rsIndex] = resultSetCell.getLong();
                    if (ColumnAggregation.SUM.equals(
                            columnInfo.getColumnAggregation())) {
                        for (Group group : groups) {
                            group.addAttribute(rsIndex,
                                    resultSetCell.getLong());
                        }
                    }
                    String str;
                    if (denominators[rsIndex] >= 0) {
                        final long denominator =
                                numericValues[denominators[rsIndex]];
                        if (denominator > 0) {
                            str = Long.toString(
                                    resultSetCell.getLong() / denominator);
                        } else {
                            str = "";
                        }
                    } else {
                        str = resultSetCell.getString();
                    }
                    final ResultCell cell = new ResultCell(1, str, false,
                            ColumnAggregation.SUM.equals(
                            columnInfo.getColumnAggregation()));
                    row.addResultCell(cell);
                }
                resultRows.add(row);
                rowNum++;
            }
            if (rs.next()) {
                final ResultRow row = new ResultRow(0);
                resultRows.add(row);
                row.addResultCell(new ResultCell(queryAttributes.size(),
                        "Resultatet &auml;r brutet vid " + maxRows + " rader",
                        false, false));
            } else {
                for (int groupIndex = 0; groupIndex < groups.length;
                        groupIndex++) {
                    setResultInGroup(groups[groupIndex],
                            firstSumColumn);
                }
            }
        } finally {
            DBUtilities.close(conn, ps, rs);
        }
    }

    /**
     * Set the remaining cells in the groups row.
     * 
     * @param group          The group. If <code>null</code>, nothing happens.
     * @param firstSumColumn The first column of sum type.
     */
    private void setResultInGroup(Group group, int firstSumColumn) {
        if (group == null) {
            return;
        }
    
        for (int index = firstSumColumn; index < queryAttributes.size();
            index++) {
            long value = group.getAttribute(index);
            String str;
            if (denominators[index] >= 0) {
                final long denominator = group.getAttribute(
                        denominators[index]);
                if (denominator > 0) {
                    value = value / denominator;
                    str = Long.toString(value);
                } else {
                    str = "";
                }
            } else {
                if (value == 0) {
                    str = "";
                } else {
                    str = Long.toString(value);
                }
            }
            group.getRow().addResultCell(
                    new ResultCell(1, str, true, true));
        }
    }
    
    /**
     * Get the SQL query for the search query.
     * 
     * @return The SQL query for the search query.
     */
    public String getSql() {
        StringBuilder result = new StringBuilder(100);     
        HashSet<JoinInfo> joins = new HashSet<JoinInfo>();
        boolean group = false;
        if (ignores.size() > 0) {
            group = true;
        }
        result.append("select ");
        // Add attributes
        boolean firstAttribute = true;
        for (ColumnInfo attribute : queryAttributes) {
            if (!firstAttribute) {
                result.append(", ");
            }
            if (group && attribute.getColumnAggregation().equals(
                    ColumnAggregation.SUM)) {
                result.append("sum(");
                result.append(attribute.getColumnName());
                result.append(')');
            } else {
                result.append(attribute.getColumnName());
            }
            if (attribute.getJoin() != null) {
                joins.add(attribute.getJoin());
            }
            firstAttribute = false;
        }
        // Add from and joins
        result.append(" from ").append(statisticsTableName);
        for (JoinInfo join : joins) {
            result.append(" left join ").append(join.getTableName())
                    .append(" on ").append(statisticsTableName).append('.')
                    .append(join.getForeignKeyColumn()).append(" = ")
                    .append(join.getTableName()).append('.')
                    .append(join.getKeyColumn());
        }
        final String whereClause = getWhereClause();
        if (whereClause.length() > 0) {
            result.append(" where ");
            result.append(whereClause);
        }
        // Add group by
        if (group) {
            firstAttribute = true;
            for (ColumnInfo attribute : queryAttributes) {
                if (attribute.getColumnAggregation().equals(
                        ColumnAggregation.GROUP)) {
                    if (firstAttribute) {
                        result.append(" group by ");
                    } else {
                        result.append(", ");
                    }
                    if (attribute.getColumnName().equals("DAY_OF_WEEK_NAME")) {
                        result.append(statisticsTableName);
                        result.append(".DAY_OF_WEEK_ID, DAY_OF_WEEK_NAME");
                    } else {
                        result.append(attribute.getColumnName());
                    }
                    firstAttribute = false;
                }
            }
            
        }
        final String orderByClause = getOrderByClause(group);
        if (orderByClause.length() > 0) {
            result.append(" order by ");
            result.append(orderByClause);
        }
        return result.toString();
    }
    
    /**
     * Get the where clause of the SQL query.
     * 
     * @return The where clause of the SQL query. Empty string if no
     *         search criteria has been given.
     */
    private String getWhereClause() {
        final StringBuilder where = new StringBuilder();
        final GUIForm form = parent.getGUIForm();
        queryParameters.clear();
        if (form.getCustomer() != null) {
            if (where.length() > 0) {
                where.append(" and ");
            }
            where.append("CUSTOMER = ?");
            queryParameters.add(DBUtilities.pad(form.getCustomer(), 50));
        }
        if (form.getProduct() != null) {
            if (where.length() > 0) {
                where.append(" and ");
            }
            where.append("PRODUCT = ?");
            queryParameters.add(DBUtilities.pad(form.getProduct(), 20));
        }
        if (form.getFromDate() != null) {
            if (where.length() > 0) {
                where.append(" and ");
            }
            if (BaseContainer.Type.CUSTOMER.equals(parent.getType())) {
                where.append("STATISTICS_DATE >= ?");
            } else {
                where.append("STATISTICS_TIME >= ?");
            }
            queryParameters.add(form.getFromDate());
        }
        if (form.getToDate() != null) {
            if (where.length() > 0) {
                where.append(" and ");
            }
            if (BaseContainer.Type.CUSTOMER.equals(parent.getType())) {
                where.append("STATISTICS_DATE < ?");
            } else {
                where.append("STATISTICS_TIME < ?");
            }
            queryParameters.add(form.getToDate());
        }
        for (GUIList guiList : parent.getGUIListList()) {
            if (guiList.getSelected() != null) {
                final ColumnInfo column = guiList.getColumnInfo();
                if (ColumnInfo.SERVICE.equals(column)) {
                    if (where.length() > 0) {
                        where.append(" and ");
                    }
                    where.append(statisticsTableName);
                    where.append(".METHOD_ID in(");
                    boolean isFirst = true;
                    for (String s : guiList.getSelected().split(";")) {
                        final String value = s.trim();
                        if (isFirst) {
                            where.append('?');
                            isFirst = false;
                        } else {
                            where.append(",?");
                        }
                        queryParameters.add(new Integer(value));
                    }
                    where.append(')');
                }
                if (ColumnInfo.METHOD.equals(column)) {
                    if (where.length() > 0) {
                        where.append(" and ");
                    }
                    where.append(statisticsTableName);
                    where.append(".METHOD_ID = ?");
                    queryParameters.add(new Integer(guiList.getSelected()));
                }
                if (ColumnInfo.LAYER.equals(column)) {
                    if (where.length() > 0) {
                        where.append(" and ");
                    }
                    where.append(statisticsTableName);
                    where.append(".LAYER_ID = ?");
                    queryParameters.add(new Integer(guiList.getSelected()));
                }
                if (ColumnInfo.ORIGIN.equals(column)) {
                    if (where.length() > 0) {
                        where.append(" and ");
                    }
                    where.append(statisticsTableName);
                    where.append(".ORIGIN_ID = ?");
                    queryParameters.add(new Integer(guiList.getSelected()));
                }
                if (ColumnInfo.MEDIA.equals(column)) {
                    if (where.length() > 0) {
                        where.append(" and ");
                    }
                    where.append(statisticsTableName);
                    where.append(".MEDIA_ID = ?");
                    queryParameters.add(new Integer(guiList.getSelected()));
                }
                if (ColumnInfo.DAY_OF_WEEK.equals(column)) {
                    if (where.length() > 0) {
                        where.append(" and ");
                    }
                    where.append(statisticsTableName);
                    where.append(".DAY_OF_WEEK_ID = ?");
                    queryParameters.add(new Integer(guiList.getSelected()));
                }
                if (ColumnInfo.HOUR_OF_DAY.equals(column)) {
                    if (where.length() > 0) {
                        where.append(" and ");
                    }
                    where.append(statisticsTableName);
                    where.append(".HOUR_OF_DAY = ?");
                    queryParameters.add(new Integer(guiList.getSelected()));
                }
            }
        }
        return where.toString();
    }
    
    /**
     * Get the order by clause of the SQL query.
     * 
     * @param group <code>true</code> if grouping is used.
     *              <code>false</code> if there is no grouping in the query
     *              
     * @return The order by clause of the SQL query. Empty string if no
     *         order by has been given.
     */
    private String getOrderByClause(boolean group) {
        final StringBuilder orderBy = new StringBuilder();
        for (SortAttribute sort : sortAttributes) {
            if (orderBy.length() > 0) {
                orderBy.append(", ");
            }
            if (group && sort.getColumnInfo().getColumnAggregation().equals(
                    ColumnAggregation.SUM)) {
                orderBy.append("sum(");
                orderBy.append(sort.getColumnInfo().getColumnName());
                orderBy.append(')');
            } else if (sort.getColumnInfo().getColumnName().equals(
                "DAY_OF_WEEK_NAME")){
                orderBy.append(statisticsTableName);
                orderBy.append(".DAY_OF_WEEK_ID");
            } else {
                orderBy.append(sort.getColumnInfo().getColumnName());
            }
            if (sort.getSortType().isDescending()) {
                orderBy.append(" desc");
            }
        }
        return orderBy.toString();
    }
    
    /**
     * Class holding information of the sort orders for attributes.
     */
    private static class SortAttribute {
        /** Information about the column */
        private final ColumnInfo columnInfo;
        
        /** The sorting of the column. */
        private final SortType sortType;

        /**
         * Create this class.
         * 
         * @param columnInfo The column information.
         *                   Must not be <code>null</code>.
         * @param sortType   The type of sorting.
         *                   Must not be <code>null</code>.
         *                   
         * @throws IllegalArgumentException if any of the constraints
         *         specified are not met. 
         */
        /* package */ SortAttribute(ColumnInfo columnInfo, SortType sortType) {
            if (columnInfo == null) {
                throw new IllegalArgumentException(
                        "columnInfo must not be null");
            }
            this.columnInfo = columnInfo;
            if (sortType == null) {
                throw new IllegalArgumentException(
                        "sortType must not be null");
            }
            this.sortType = sortType;
        }
        
        /**
         * Get the column information.
         * 
         * @return The column information.
         *         This method never returns <code>null</code>.
         */
        /* package */ ColumnInfo getColumnInfo() {
            return columnInfo;
        }
        
        /**
         * Get the sort tyoe.
         * 
         * @return The sort type.
         *         This method never returns <code>null</code>.
         */
        /* package */ SortType getSortType() {
            return sortType;
        }
    }

    
    /**
     * Class holding information about one group (the instance of the group).
     */
    private static class Group {
        /** The name of the group. */
        private final String name;
        
        /** The sum attributes of the group. */
        private final long attributes[];

        /** The row. */
        private final ResultRow row;
        
        /**
         * Create this class.
         * 
         * @param name               The name of the group
         *                           Must not be <code>null</code>.
         * @param totalNumAttributes The total number of columns.
         * @param row                The row.
         *                           Must not be <code>null</code>.
         *                   
         * @throws IllegalArgumentException if any of the constraints
         *         specified are not met. 
         */
        /* package */ Group(String name, int totalNumAttributes, ResultRow row) {
            if (name == null) {
                throw new IllegalArgumentException("name must not be null");
            }
            this.name = name;
            this.attributes = new long[totalNumAttributes];
            if (row == null) {
                throw new IllegalArgumentException("row must not be null");
            }
            this.row = row;
        }
        
        /**
         * Get the group name.
         * 
         * @return The name of the group
         *         This method never returns <code>null</code>.
         */
        /* package */ String getName() {
            return name;
        }
        
        /**
         * Get the sum of the attribute with the given index.
         * 
         * @param index The index of the attribute between
         *              <code>0</code> and <code>totalNumAttributes-1</code>.
         *              
         * @return The sum of the attribute with the given index.
         */
        /* package */ long getAttribute(int index) {
            return attributes[index];
        }
        
        /**
         * Increase one attribute with the given index.
         * 
         * @param index The index of the attribute between
         *              <code>0</code> and <code>totalNumAttributes-1</code>.
         * @param value The value to add to the attribute.
         */
        /* package */ void addAttribute(int index, long value) {
            attributes[index] += value;
        }

        /**
         * Get the row
         * 
         * @return The row of the group
         *         This method never returns <code>null</code>.
         */
        /* package */ ResultRow getRow() {
            return row;
        }
        
    }
}
