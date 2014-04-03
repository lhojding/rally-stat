package se.uc.stat.web.statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Class holding the content of one row in the result set.
 * 
 * @author Anders Persson (konx40)
 */
public class ResultRow {
    /** The left indent in cells. */
    private final int leftIndent;
    /** The cells in the row. */
    private final ArrayList<ResultCell> cells = new ArrayList<ResultCell>();
    
    /**
     * Create this row.
     * 
     * @param leftIndent The left indent in cells.
     */
    public ResultRow(int leftIndent) {
        this.leftIndent = leftIndent;
    }
    
    /**
     * Get the left indent in cells.
     * 
     * @return The left indent in cells.
     */
    public int getLeftIndent() {
        return leftIndent;
    }
    
    /**
     * Add a cell to the row. The cells have to be added in the order
     * from left to right.
     * 
     * @param cell The cell to add. Must not be <code>null</code>.
     * 
     * @throws IllegalArgumentException if cell is <code>null</code>.
     */
    public void addResultCell(ResultCell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("cell must not be null");
        }
        cells.add(cell);
    }

    /**
     * Get the cells in this row.
     * 
     * @return The cells in this row.
     *         This method never returns <code>null</code>.
     */
    public List<ResultCell> getCells() {
        return cells;
    }
}
