package se.uc.stat.web.types;

/**
 * Class holding information about the day of the week.
 * 
 * @author Anders Persson (konx40).
 */
public class DayOfWeek extends BaseInfo {
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
    public DayOfWeek(int id, String name) {
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
