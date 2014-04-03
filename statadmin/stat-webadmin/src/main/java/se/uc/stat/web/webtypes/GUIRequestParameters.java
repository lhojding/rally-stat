package se.uc.stat.web.webtypes;

/**
 * Interface used to fetch request parameters.
 * 
 * @author Anders Persson (konx40)
 */
public interface GUIRequestParameters {
    /**
     * Get the value of the parameter with the given name.
     * 
     * @param name The name of the parameter.
     * 
     * @return The value of the parameter or <code>null</code> if the
     *         parameter has not been specified. This method never returns
     *         an empty string. The value is always trimmed.
     */
    public String getParameter(String name);
}
