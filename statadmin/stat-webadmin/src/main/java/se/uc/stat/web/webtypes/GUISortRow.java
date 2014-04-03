package se.uc.stat.web.webtypes;

import java.util.List;

import se.uc.stat.web.types.SortType;

/**
 * Class holding what is needed to present one sort parameter.
 * 
 * @author Anders Persson (konx40)
 */
public class GUISortRow implements Comparable<GUISortRow> {
    /** The order id of the row. */
    private int orderId;
    /** The (computer) name of the sort parameter for the row. */
    private String parameterName;
    /** The (computer) name of the sort order parameter for the row. */
    private String orderName;
    /** The label of the row. */
    private String label;
    /** The possible parameter selections. */
    private final List<? extends GUISortParameterListRow> parameterRows;
    /** The possible order selections. */
    private final List<? extends GUISortOrderListRow> orderRows;
    /** The selected value for the parameter. */
    private final String selectedParameter;
    /** The selected value for the order. */
    private final String selectedOrder;

    /**
     * Create this object.
     * 
     * @param orderId           The order of the row.
     * @param parameterRows     The possible parameters to select.
     *                          Must not be <code>null</code>.
     * @param orderRows         The possible orders to select.
     *                          Must not be <code>null</code>.
     * @param requestParameters Interface to read request parameters from.
     *                          <code>null</code> if no request parameters
     *                          should be read.
     *             
     * @throws IllegalArgumentException if any of the constraints of the
     *         parameters specified above is not met.
     */
    /* package */ GUISortRow(int orderId,
            List<? extends GUISortParameterListRow> parameterRows,
            List<? extends GUISortOrderListRow> orderRows,
            GUIRequestParameters requestParameters) {
        setOrderId(orderId);
        if (parameterRows == null) {
            throw new IllegalArgumentException("parameterRows must not be null");
        }
        this.parameterRows = parameterRows;
        if (orderRows == null) {
            throw new IllegalArgumentException("orderRows must not be null");
        }
        this.orderRows = orderRows;
        if (requestParameters == null) {
            selectedParameter = null;
            selectedOrder = null;
            return;
        }
        // Lookup parameter
        final String param = requestParameters.getParameter(getParameterName());
        GUISortParameterListRow parameterRow = null;
        for (GUISortParameterListRow row : parameterRows) {
            if (row.getName().equals(param)) {
                parameterRow = row;
            }
        }
        // Lookup order
        final String order = requestParameters.getParameter(getOrderName());
        GUISortOrderListRow orderRow = null;
        for (GUISortOrderListRow row : orderRows) {
            if (row.getName().equals(order)) {
                orderRow = row;
            }
        }
        if (orderRow == null && orderRows.size() > 0) {
            orderRow = orderRows.get(0);
        }
        // Set selected values
        if (parameterRow == null || orderRow == null) {
            selectedParameter = null;
            selectedOrder = null;
        } else if (orderRow.getSortType().isIgnored() &&
                !parameterRow.isIgnorable()) {
            selectedParameter = null;
            selectedOrder = null;
        } else {
            selectedParameter = parameterRow.getName();
            selectedOrder = orderRow.getName();
        }
    }

    /**
     * Set the order id of this row. The id will have to be set to a
     * new value after reorganizing the sort rows.
     * 
     * @param orderId The order id to set.
     */
    /* package */ void setOrderId(int orderId) {
        this.orderId = orderId;
        parameterName = "sortParam" + orderId;
        orderName = "sortOrder" + orderId;
        label = Integer.toString(orderId);
    }
    
    /**
     * Get the (computer) name of the sort parameter for the row.
     *
     * @return The (computer) name of the sort parameter for the row.
     *         This method never returns <code>null</code>.
     */
    public String getParameterName() {
        return parameterName;
    }
    
    /**
     * Get the (computer) name of the sort order for the row.
     *
     * @return The (computer) name of the sort order for the row.
     *         This method never returns <code>null</code>.
     */
    public String getOrderName() {
        return orderName;
    }
    
    /**
     * Get the label of the attribute.
     *
     * @return The human readable name of the attribute.
     *         This method never returns <code>null</code>.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the rows to select sort parameters from.
     *
     * @return The rows to select sort parameters from
     *         This method never returns <code>null</code>.
     */
    public List<? extends GUISortParameterListRow> getParameterRows() {
        return parameterRows;
    }

    /**
     * Get the rows to select sort orders from.
     *
     * @return The rows to select sort orders from
     *         This method never returns <code>null</code>.
     */
    public List<? extends GUISortOrderListRow> getOrderRows() {
        return orderRows;
    }

    /**
     * Get the selected value for the sort parameter for the row.
     *
     * @return The selected value for the sort parameter for the row.
     *         <code>null</code> if nothing has been selected.
     */
    public String getSelectedParameter() {
        return selectedParameter;
    }

    /**
     * Get the selected value for the sort order for the row.
     *
     * @return The selected value for the sort order for the row.
     *         <code>null</code> if nothing has been selected.
     */
    public String getSelectedOrder() {
        return selectedOrder;
    }

    /**
     * Compare this instance to another instance. The sort order is
     * <ul>
     * <li>grouped</li>
     * <li>sorted</li>
     * <li>empty</li>
     * <li>ignored</li>
     * </ul>
     * The order within each group is the same as the existing order.
     * 
     * @param other The other object
     * 
     * @return a negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object. 
     */
    @Override
    public int compareTo(GUISortRow other) {
        int thisGroupId = calculateGroupId(this);
        int otherGroupId = calculateGroupId(other);
        if (thisGroupId > otherGroupId) {
            return 1;
        }
        if (thisGroupId < otherGroupId) {
            return -1;
        }
        return orderId - other.orderId;
    }
    
    /**
     * Support method for sorting performing the following categorization.
     * <ul>
     * <li>grouped --> 0</li>
     * <li>sorted --> 1</li>
     * <li>empty --> 2</li>
     * <li>ignored --> 3</li>
     * </ul>
     * 
     * @param row The row to perform the calculation for.
     * 
     * @return A group number according to the specification above. 
     */
    private int calculateGroupId(GUISortRow row) {
        if (row.getSelectedParameter() == null) {
            return 2;
        }
        SortType sortType = null;
        for (GUISortOrderListRow orderRow : row.getOrderRows()) {
            if (orderRow.getName().equals(row.getSelectedOrder())) {
                sortType = orderRow.getSortType();
            }
        }
        if (sortType == null) {
            return 2;
        }
        if (sortType.isGrouping()) {
            return 0;
        }
        if (sortType.isIgnored()) {
            return 3;
        }
        return 1;
    }
}
