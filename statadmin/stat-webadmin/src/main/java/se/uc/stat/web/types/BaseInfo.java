package se.uc.stat.web.types;

import se.uc.stat.web.webtypes.GUIListRow;

/**
 * Base class for all info objects.
 * 
 * @author Anders Persson (konx40).
 */
public abstract class BaseInfo implements GUIListRow {
    /** The id of the object. */
    private final int id;
    
    /**
     * Create this object.
     * 
     * @param id   The id of the object.
     */
    protected BaseInfo(int id) {
        this.id = id;
    }

    /**
     * Get the id.
     * 
     * @return The id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPresentationId() {
        return Integer.toString(id);
    }
}
