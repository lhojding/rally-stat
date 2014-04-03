package se.uc.stat.dimension;

/**
 * Class holding references to the dimensions.
 * 
 * @author Anders Persson (konx40)
 */
public class Dimensions {
    /** The media dimension instance. */
    private static final MediaDimension mediaDimension = new MediaDimension();
    
    /** The method dimension instance. */
    private static final MethodDimension methodDimension =
            new MethodDimension();
    
    /** The origin dimension instance. */
    private static final OriginDimension originDimension = new OriginDimension();
    
    /** The layer dimension instance. */
    private static final LayerDimension layerDimension = new LayerDimension();
    
    /**
     * Private constructor to prevent instantiation.
     */
    private Dimensions() {
        // Nothing to do.
    }
    
    /**
     * Get the media dimension instance.
     * 
     * @return The media dimension instance.
     *         This method never returns <code>null</code>.
     */
    public static MediaDimension getMediaDimension() {
        return mediaDimension;
    }
    
    /**
     * Get the method dimension instance.
     * 
     * @return The method dimension instance.
     *         This method never returns <code>null</code>.
     */
    public static MethodDimension getMethodDimension() {
        return methodDimension;
    }
    
    /**
     * Get the origin dimension instance.
     * 
     * @return The origin dimension instance.
     *         This method never returns <code>null</code>.
     */
    public static OriginDimension getOriginDimension() {
        return originDimension;
    }
    
    /**
     * Get the layer dimension instance.
     * 
     * @return The layer dimension instance.
     *         This method never returns <code>null</code>.
     */
    public static LayerDimension getLayerDimension() {
        return layerDimension;
    }

    /**
     * Mark all dimension to be reread. This may be useful in case of unknown
     * errors. This method should never be called in case of normal successful
     * execution.
     */
    public static void clear() {
        mediaDimension.clear();
        methodDimension.clear();
        originDimension.clear();
        layerDimension.clear();
    }
}
