package se.uc.stat.web.webtypes;

/**
 * Interface specifying the methods needed to be presented in a GUI list box.
 * This interface is used to be able to handle the different entities in a
 * standardized way in the presentation.
 * 
 * @author Anders Persson (konx40)
 */
public interface GUIListRow {
    /**
     * Get the presentation id.
     * 
     * @return The id in the list box.
     */
    public String getPresentationId();
    
    /**
     * Get the presentation name.
     * 
     * @return The name in the list box.
     */
    public String getPresentationName();
}
