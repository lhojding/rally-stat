package se.uc.stat.web.webtypes;

import se.uc.stat.web.types.SortType;

/**
 * Interface specifying the methods needed to be presented in a GUI list
 * for sort orders.
 * This interface is used to be able to handle the different entities in a
 * standardized way in the presentation.
 * 
 * @author Anders Persson (konx40)
 */
public interface GUISortOrderListRow {
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
     * Get the sort type of the attribute.
     *
     * @return The sort type of the attribute.
     *         This method never returns <code>null</code>.
     */
    public SortType getSortType();
}
