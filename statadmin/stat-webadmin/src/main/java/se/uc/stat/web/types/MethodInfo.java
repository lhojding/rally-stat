package se.uc.stat.web.types;

/**
 * Class holding information about the method.
 * 
 * @author Anders Persson (konx40).
 */
public class MethodInfo extends BaseInfo {
    /** The name of the service. */
    private final String serviceName;
    
    /** The name of the method. */
    private final String methodName;
    
    /**
     * Create this object.
     * 
     * @param id          The id of the object.
     * @param serviceName The human readable name of the service.
     *                    Must not be <code>null</code>.
     * @param methodName  The human readable name of the method.
     *                    Must not be <code>null</code>.
     *             
     * @throws IllegalArgumentException if any of the constraints of the
     *         parameters specified above is not met.
     */
    public MethodInfo(int id, String serviceName, String methodName) {
        super(id);
        if (serviceName == null) {
            throw new IllegalArgumentException("serviceName must not be null");
        }
        this.serviceName = serviceName;
        if (methodName == null) {
            throw new IllegalArgumentException("methodName must not be null");
        }
        this.methodName = methodName;
    }

    /**
     * Get the service name.
     * 
     * @return The service name.
     */
    public String getServiceName() {
        return serviceName;
    }
    
    /**
     * Get the method name.
     * 
     * @return The method name.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPresentationName() {
        return serviceName + "." + methodName;
    }
}
