package se.uc.stat.web.types;

import java.util.ArrayList;
import java.util.Collection;

import se.uc.stat.web.webtypes.GUIListRow;

/**
 * Class holding information about the service.
 * 
 * @author Anders Persson (konx40).
 */
public class ServiceInfo implements GUIListRow {
    /** The method ids contained in this service. */
    private final Collection<Integer> ids = new ArrayList<Integer>();
    /** The name of the service. */
    private final String serviceName;
    
    /**
     * Create this object.
     * 
     * @param serviceName The human readable name of the service.
     *                    Must not be <code>null</code>.
     *             
     * @throws IllegalArgumentException if any of the constraints of the
     *         parameters specified above is not met.
     */
    public ServiceInfo(String serviceName) {
        if (serviceName == null) {
            throw new IllegalArgumentException("serviceName must not be null");
        }
        this.serviceName = serviceName;
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
     * Add a method id to the <code>ids</code> collection.
     * 
     * @param id Id to add.
     */
    public void addId(int id) {
        ids.add(new Integer(id));
    }
    
    /**
     * Get the method ids forming this service.
     * 
     * @return The method ids. This method never returns <code>null</code>.
     */
    public Collection<Integer> getIds() {
        return ids;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPresentationId() {
        StringBuilder result = new StringBuilder();
        for (Integer id : ids) {
            if (result.length() != 0) {
                result.append(';');
            }
            result.append(id.toString());
        }
        return result.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPresentationName() {
        return serviceName;
    }
}
