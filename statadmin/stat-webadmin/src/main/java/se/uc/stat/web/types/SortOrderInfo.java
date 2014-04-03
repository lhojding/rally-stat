package se.uc.stat.web.types;

import se.uc.stat.web.webtypes.GUISortOrderListRow;

/**
 * Class representing information about a sort order in the request
 * 
 * @author Anders Persson (konx40)
 */
public class SortOrderInfo implements GUISortOrderListRow {
    /** The sort order type. */
    private final SortType sortType;

    /**
     * Create this object.
     * 
     * @param sortType  The sort type. Must not be <code>null</code>.
     *             
     * @throws IllegalArgumentException if any of the constraints of the
     *         parameters specified above is not met.
     */
    public SortOrderInfo(SortType sortType) {
        if (sortType == null) {
            throw new IllegalArgumentException("sortType must not be null");
        }
        this.sortType = sortType;
    }

    /**
     * Get the (computer) name of the attribute.
     *
     * @return The (computer) name of the attribute.
     *         This method never returns <code>null</code>.
     */
    @Override
    public String getName() {
        return sortType.getName();
    }
    
    /**
     * Get the label of the attribute.
     *
     * @return The human readable name of the attribute.
     *         This method never returns <code>null</code>.
     */
    @Override
    public String getLabel() {
        return sortType.getLabel();
    }

    /**
     * Get the sort type of the attribute.
     *
     * @return The sort type of the attribute.
     *         This method never returns <code>null</code>.
     */
    @Override
    public SortType getSortType() {
        return sortType;
    }
}
