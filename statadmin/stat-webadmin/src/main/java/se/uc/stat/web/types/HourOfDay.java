package se.uc.stat.web.types;

/**
 * Class holding information about the hour of the day.
 * 
 * @author Anders Persson (konx40).
 */
public class HourOfDay extends BaseInfo {
    /** The name of the object. */
    private final String name;
    
    /**
     * Create this object.
     * 
     * @param id   The id of the object.
     * @param name The human readable name of the object.
     *             Must not be <code>null</code>.
     *             
     * @throws IllegalArgumentException if any of the constraints of the
     *         parameters specified above is not met.
     */
    public HourOfDay(int id, String name) {
        super(id);
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        this.name = name;
    }
    
    /**
     * Get the name.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPresentationName() {
        return name;
    }
}
