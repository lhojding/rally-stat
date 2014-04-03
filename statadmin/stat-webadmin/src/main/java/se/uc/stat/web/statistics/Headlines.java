package se.uc.stat.web.statistics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class holding the headlines for the search result.
 * 
 * @author Anders Persson (konx40)
 */
public class Headlines {
    /** The grouped headlines. */
    private final List<Headline> groupedHeadlines = new ArrayList<Headline>();
    /** The normal headlines. */
    private final List<Headline> headlines = new ArrayList<Headline>();
    /**
     * List of first row of table cells.
     * <code>null</code> if not initiated.
     */
    private List<TableCell> tableCellRow1 = null;
    /**
     * List of second row of table cells.
     * <code>null</code> if not initiated.
     */
    private List<TableCell> tableCellRow2 = null;
    
    /**
     * Create this class.
     */
    public Headlines() {
        // Nothing to do.
    }

    /**
     * Add a grouped headline.
     * 
     * @param headline The headline to add. Must not be <code>null</code>.
     * 
     * @throws IllegalArgumentException if any of the constraints specified
     *         is not met.
     */
    /* package */ void addGroupedHeadline(Headline headline) {
        if (headline == null) {
            throw new IllegalArgumentException("headline must not be null");
        }
        groupedHeadlines.add(headline);
        clearTableCells();
    }
    
    /**
     * Add a headline.
     * 
     * @param headline The headline to add. Must not be <code>null</code>.
     * 
     * @throws IllegalArgumentException if any of the constraints specified
     *         is not met.
     */
    /* package */ void addHeadline(Headline headline) {
        if (headline == null) {
            throw new IllegalArgumentException("headline must not be null");
        }
        headlines.add(headline);
        clearTableCells();
    }
    
    /**
     * Get the grouped headlines.
     * 
     * @return The grouped headline.
     *         This method never returns <code>null</code>.
     */
    public List<Headline> getGroupedHeadlines() {
        return groupedHeadlines;
    }
    
    /**
     * Get the normal headlines.
     * 
     * @return The normal headline.
     *         This method never returns <code>null</code>.
     */
    public List<Headline> getHeadlines() {
        return headlines;
    }
    
    /**
     * Clear the table cell structure to perform a recalculation.
     */
    private void clearTableCells() {
        tableCellRow1 = null;
        tableCellRow2 = null;
    }
    
    /**
     * Perform the calculation of table cell structures if not already done.
     */
    private void prepareTableCells() {
        if (tableCellRow1 != null) {
            return;
        }
        tableCellRow1 = new ArrayList<TableCell>(headlines.size());
        tableCellRow2 = new ArrayList<TableCell>(headlines.size());
        // Populate structure and handle columns without group headline.
        for (Headline headline : headlines) {
            final String groupLabel = headline.getGroupLabel();
            TableCell row1;
            TableCell row2;
            if (groupLabel == null) {
                row1 = new TableCell(headline.getLabel());
                row1.increaseRowSpan();
                row2 = new TableCell(headline.getLabel());
                row2.setInvisible();
            } else {
                row1 = new TableCell(groupLabel);
                row2 = new TableCell(headline.getLabel());
            }
            tableCellRow1.add(row1);
            tableCellRow2.add(row2);
        }
        // Take care of groupedHeadlines
        for (int i = 0; i < groupedHeadlines.size(); i++) {
            tableCellRow1.get(0).increaseColSpan();
            tableCellRow2.get(0).increaseColSpan();
        }
        // Calculate col span.
        TableCell lastUniqueCell = null;
        for (TableCell cell : tableCellRow1) {
            if (lastUniqueCell == null) {
                lastUniqueCell = cell;
            } else if (lastUniqueCell.getLabel().equals(cell.getLabel())) {
                // Duplicate found
                cell.setInvisible();
                lastUniqueCell.increaseColSpan();
            } else {
                // Not duplicate
                lastUniqueCell = cell;
            }
        }
        // Remove invisible from row 1
        Iterator<TableCell> rowIterator = tableCellRow1.iterator();
        while (rowIterator.hasNext()) {
            TableCell cell = rowIterator.next();
            if (!cell.isVisible()) {
                rowIterator.remove();
            }
        }
        // Remove invisible from row 2 
        rowIterator = tableCellRow2.iterator();
        while (rowIterator.hasNext()) {
            TableCell cell = rowIterator.next();
            if (!cell.isVisible()) {
                rowIterator.remove();
            }
        }
    }
    
    /**
     * Get the table cells in row 1.
     * 
     * @return The table cells in row 1.
     *         This method never returns <code>null</code>.
     */
    public List<TableCell> getTableCellRow1() {
        prepareTableCells();
        return tableCellRow1;
    }
    
    /**
     * Get the table cells in row 2.
     * 
     * @return The table cells in row 2.
     *         This method never returns <code>null</code>.
     */
    public List<TableCell> getTableCellRow2() {
        prepareTableCells();
        return tableCellRow2;
    }
    
    /**
     * Class representing a table cell when rendered.
     */
    public static class TableCell {
        /** The label of the cell. */
        private final String label;
        /**
         * <code>true</code> if the cell is visible.
         * <code>false</code> if the cell is invisible.
         */
        private boolean visible = true;
        /** The column span of the cell. */
        private int colSpan = 1;
        /** The row span of the cell. */
        private int rowSpan = 1;

        /**
         * Create this class.
         * 
         * @param label The label of the call. May be <code>null</code>.
         */
        /* package */ TableCell(String label) {
            this.label = label;
        }

        /**
         * Get the label of the cell.
         * 
         * @return The label of the cell or <code>null</code>.
         */
        public String getLabel() {
            return label;
        }
        
        /**
         * Set visible to <code>false</code>.
         */
        /* package */ void setInvisible() {
            visible = false;
        }

        /**
         * Checks if this cell is visible.
         * 
         * @return <code>true</code> if this cell is visible.
         *         <code>false</code> if the cell is invisible.
         */
        /* package */ boolean isVisible() {
            return visible;
        }

        /**
         * Increase the col span with 1.
         */
        /* package */ void increaseColSpan() {
            colSpan++;
        }
        
        /**
         * Get the col span of the cell.
         * 
         * @return The col span of the cell.
         */
        public int getColSpan() {
            return colSpan;
        }
        
        /**
         * Increase the row span with 1.
         */
        /* package */ void increaseRowSpan() {
            rowSpan++;
        }
        
        /**
         * Get the row span of the cell.
         * 
         * @return The row span of the cell.
         */
        public int getRowSpan() {
            return rowSpan;
        }
    }
}
