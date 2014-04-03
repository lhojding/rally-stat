package se.uc.stat.web.webtypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.uc.stat.web.types.SortOrderInfo;
import se.uc.stat.web.types.SortParameterInfo;

/**
 * Class holding and arranging the sort parameters and sort orders.
 * The class performs read of request parameter, validation and rearrangement
 * of sort parameters and sort orders and is a container for the result used
 * by the form.
 * 
 * @author Anders Persson (konx40)
 */
public class GUISort {
    /** The parent GUIForm. */
    private final GUIForm parent;
    /** The number of parameters to render. */
    private final int numParams;
    /** The list of rows for sorting. */
    private final ArrayList<GUISortRow> sortRows;
    
    /**
     * Create this class.
     * 
     * @param parent    The parent gui form. Must not be <code>null</code>.
     * @param numParams The number of parameters that should be rendered.
     * 
     * @throws IllegalArgumentException if any of the constraints of the
     *         parameters specified above is not met.
     */
    /* package */ GUISort(GUIForm parent, int numParams) {
        if (parent == null) {
            throw new IllegalArgumentException("parent must not be null");
        }
        this.parent = parent;
        this.numParams = numParams;
        sortRows = new ArrayList<GUISortRow>(numParams);
        readParameters();
    }
    
    /**
     * Read the parameters from request. This method also performs
     * validation but no rearangement.
     */
    private void readParameters() {
        List<SortOrderInfo> sortOrders =
            parent.getContainer().getSortOrderInfoList();
        List<SortParameterInfo> sortParameters =
            parent.getContainer().getSortParameterInfoList();

        final Set<String> unusedParameters = new HashSet<String>();
        for (GUISortParameterListRow row : sortParameters) {
            unusedParameters.add(row.getName());
        }
        for (int number = 0; number < numParams; number++) {
            GUISortRow sortRow = new GUISortRow(number, sortParameters,
                    sortOrders, parent);
            final String selectedParameter = sortRow.getSelectedParameter();
            if (selectedParameter != null) {
                if (unusedParameters.contains(selectedParameter)) {
                    unusedParameters.remove(selectedParameter);
                } else {
                    // Already used earlier
                    sortRow = new GUISortRow(number, sortParameters,
                            sortOrders, null);
                }
            }
            sortRows.add(sortRow);
        }
        Collections.sort(sortRows);
        int orderId = 0;
        for (GUISortRow row : sortRows) {
            row.setOrderId(orderId);
            orderId++;
        }
    }
    
    /**
     * Get the list of sort parameters.
     * 
     * @return The list of sort parameters.
     *         This method never returns <code>null</code>.
     */
    public List<GUISortRow> getSortRows() {
        return sortRows;
    }
}
