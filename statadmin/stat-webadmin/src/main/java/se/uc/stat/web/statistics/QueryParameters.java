package se.uc.stat.web.statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Class holding the information necessary to build the query.
 * 
 * @author Anders Persson (konx40)
 */
public class QueryParameters {
    /**
     * Requested attributes. This may be expressions.
     * Ordered in the way they are expected to be rendered.
     */
    private ArrayList<String> attributes = new ArrayList<String>();
    
    /**
     * Add an attribute to the query parameters. The attributes must be added
     * in the order they are expected to be rendered.
     * 
     * @param attribute The attribute to add.
     *                  Must not be <code>null</code>.
     */
    public void addAttribute(String attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("attribute must not be null");
        }
        attributes.add(attribute);
    }
    
    /**
     * Get the attributes to query in the order they are expected to be
     * rendered.
     * 
     * @return The attributes. This method never returns <code>null</code>.
     */
    public List<String> getAttributes() {
        return attributes;
    }
}
