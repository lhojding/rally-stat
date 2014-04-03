package se.uc.stat.dimension;

import java.io.Serializable;

/**
 * The key for methods.
 * 
 * @author Anders Persson (konx40)
 */
public class MethodKey implements Serializable {
    /** The service name. */
    private final String serviceName;

    /** The method name. */
    private final String methodName;

    /**
     * Create this class.
     * 
     * @param serviceName The name of the service.
     *                    <code>null</code> if not given.
     * @param methodName  The name of the method.
     *                    <code>null</code> if not given.
     */
    public MethodKey(String serviceName, String methodName) {
        this.serviceName = serviceName;
        this.methodName = methodName;
    }
    
    /**
     * Get the service name.
     * 
     * @return The service name or <code>null</code> if not specified.
     */
    public String getServiceName() {
        return serviceName;
    }
    
    /**
     * Get the method name.
     * 
     * @return The method name or <code>null</code> if not specified.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Get the string representation of this instance.
     * 
     * @return The string representation of this instance.
     */
    @Override
    public String toString() {
        return "{serviceName=" + serviceName + ", methodName=" + methodName + "}";
    }
    
    /**
     * Compare this object with another object.
     * 
     * @param other The other object.
     * 
     * @return <code>true</code> if and only if this object is equals to
     *         <code>other</code>. <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MethodKey)) {
            return false;
        }
        MethodKey o = (MethodKey)other;
        // Compare serviceName
        if (serviceName == null) {
            if (o.serviceName != null) {
                return false;
            }
        } else {
            if (o.serviceName == null) {
                return false;
            }
            if (!serviceName.equals(o.serviceName)) {
                return false;
            }
        }
        // Compare methodName
        if (methodName == null) {
            if (o.methodName != null) {
                return false;
            }
        } else {
            if (o.methodName == null) {
                return false;
            }
            if (!methodName.equals(o.methodName)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Get the hash code of this instance.
     * 
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = 0;
        if (serviceName != null) {
            result += serviceName.hashCode();
        }
        if (methodName != null) {
            result += methodName.hashCode();
        }
        return result;
    }
}
