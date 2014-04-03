package se.uc.stat.basestatistics;

/**
 * Test information.
 * 
 * @author Anders Persson (konx40)
 */
/* package */ class BaseInformationImpl
        extends BaseInformation<BaseKeyImpl, BaseInformationImpl>
        implements Cloneable {
    /** The key for this information. */
    private final BaseKeyImpl key;
    
    /**
     * Create this class.
     * 
     * @param key       The key for this information.
     *                  Must not be <code>null</code>.
     * @param dayOfWeek The day of the week. Have to be between 0 and 6
     *                  where 0 is Monday and 6 is Sunday.  
     *                  
     * @throws IllegalArgumentException if <code>dayOfWeek</code>
     *         or <code>key</code> is invalid.
     */
    /* package */ BaseInformationImpl(BaseKeyImpl key, int dayOfWeek) {
        super(dayOfWeek);
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
        this.key = key;
    }
    
    /**
     * Register a correct call.
     * 
     * @param numCalls The number of calls to register.
     */
    public void registerCorrectCalls(int numCalls) {
        for (int i = 0; i < numCalls; i++) {
            increaseNumCorrectCalls();
        }
    }
    
    /**
     * Register an invalid call.
     * 
     * @param numCalls The number of calls to register.
     */
    public void registerInvalidCalls(int numCalls) {
        for (int i = 0; i < numCalls; i++) {
            increaseNumInvalidCalls();
        }
    }
    
    /**
     * Register a failed call.
     * 
     * @param numCalls The number of calls to register.
     */
    public void registerFailedCalls(int numCalls) {
        for (int i = 0; i < numCalls; i++) {
            increaseNumFailedCalls();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseKeyImpl getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean subtract(BaseInformationImpl other) {
        return baseSubtract(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BaseInformationImpl createClone() {
        try {
            return (BaseInformationImpl)super.clone();
        } catch (CloneNotSupportedException e) {
            // Nothing to do.
            // This case will be found in the unit tests.
        }
        return null;
    }
}
