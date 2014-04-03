package se.uc.stat.web.statistics;

/**
 * Class holding the content of one cell in the result set.
 * 
 * @author Anders Persson (konx40)
 */
public class ResultCell {
    /** The col span. */
    private final int colSpan;
    /** The cell content. */
    private final String content;
    /**
     * <code>true</code> if this is a group headline.
     * <code>false</code> if this is an ordinary row.
     */
    private final boolean groupHeadline;
    /**
     * <code>true</code> if this cell should be right aligned
     * <code>false</code> if this cell should be left aligned
     */
    private final boolean rightAligned;
    
    /**
     * Create this cell.
     * 
     * @param colSpan       The colSpan to use for the result cell.
     * @param content       The content in the cell. <code>null</code> for empty
     *                      cell.
     * @param groupHeadline <code>true</code> if this is a group headline.
     *                      <code>false</code> if this is an ordinary row.
     * @param rightAligned  <code>true</code> if this cell should be right
     *                      aligned. <code>false</code> if this cell should be
     *                      left aligned.
     */
    public ResultCell(int colSpan, String content, boolean groupHeadline,
            boolean rightAligned) {
        this.colSpan = colSpan;
        this.content = content;
        this.groupHeadline = groupHeadline;
        this.rightAligned = rightAligned;
    }
    
    /**
     * Get the colSpan of the cell.
     * 
     * @return The col span of the cell.
     */
    public int getColSpan() {
        return colSpan;
    }

    /**
     * Get the content of the cell.
     * 
     * @return The content of the cell or <code>null</code> for an empty cell.
     */
    public String getContent() {
        return content;
    }
    
    /**
     * Check if this is a group headline.
     * 
     * @return <code>true</code> if this is a group headline.
     *         <code>false</code> if this is an ordinary row.
     */
    public boolean isGroupHeadline() {
        return groupHeadline;
    }
    
    /**
     * Check if this cell should be right aligned.
     * 
     * @return <code>true</code> if this cell should be right aligned.
     *         <code>false</code> if this cell should be left aligned.
     */
    public boolean isRightAligned() {
        return rightAligned;
    }
}
