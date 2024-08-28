package dm.pagefit.map;

/**
 * Class representing the size of a map, including its dimensions, scale, and orientation.
 */
public class PageSize
{
    private double unscaledPageWidth;
    private double unscaledPageHeight;
    private double scaledPageWidth;
    private double scaledPageHeight;
    private double scale;
    private boolean orientation;
    private String pageSize;

    /**
     * Constructs a PageSize object with the given dimensions, scale, and orientation.
     *
     * @param width       the width of the map page in meters
     * @param height      the height of the map page in meters
     * @param scale       the scale factor to be applied to the map dimensions
     * @param orientation the orientation of the map (true for "LANDSCAPE", false for "PORTRAIT")
     * @param pageSize    the size of the page (e.g., "A4" or "A3")
     */
    public PageSize(double width, double height, double scale, boolean orientation, String pageSize)
    {
        this.unscaledPageWidth = width;
        this.unscaledPageHeight = height;
        this.scale = scale;
        this.orientation = orientation;
        this.pageSize = pageSize;

        // Calculate scaled dimensions based on the scale factor.
        this.scaledPageWidth = width * scale;
        this.scaledPageHeight = height * scale;
    }

    /**
     * Returns the unscaled width of the map, adjusted for orientation.
     *
     * @return the unscaled width of the map
     */
    public double getUnscaledWidth()
    {
        if (!orientation)
            return unscaledPageWidth;
        else
            return unscaledPageHeight;
    }

    /**
     * Returns the unscaled height of the map, adjusted for orientation.
     *
     * @return the unscaled height of the map
     */
    public double getUnscaledHeight()
    {
        if (!orientation)
            return unscaledPageHeight;
        else
            return unscaledPageWidth;
    }

    /**
     * Returns the scaled width of the map, adjusted for orientation.
     *
     * @return the scaled width of the map
     */
    public double getScaledWidth()
    {
        if (!orientation)
            return scaledPageWidth;
        else
            return scaledPageHeight;
    }

    /**
     * Returns the scaled height of the map, adjusted for orientation.
     *
     * @return the scaled height of the map
     */
    public double getScaledHeight()
    {
        if (!orientation)
            return scaledPageHeight;
        else
            return scaledPageWidth;
    }

    /**
     * Returns the scale factor of the map.
     *
     * @return the scale factor of the map
     */
    public double getScale() {
        return scale;
    }

    /**
     * Returns the orientation of the map.
     *
     * @return the orientation of the map (true for "LANDSCAPE", false for "PORTRAIT")
     */
    public boolean getOrientation() {
        return orientation;
    }

    /**
     * Returns the page size of the map.
     *
     * @return the page size of the map (e.g., "A4" or "A3")
     */
    public String getPageSize() {
        return pageSize;
    }

    /**
     * Sets the orientation of the map to portrait.
     */
    public void setPortrait()
    {
        orientation = false;
    }

    /**
     * Sets the orientation of the map to landscape.
     */
    public void setLandscape()
    {
        orientation = true;
    }

    /**
     * Concrete subclass representing the size of an A4 map.
     */
    public static class A4MapSize extends PageSize {

        /**
         * Constructs an A4MapSize object with the given scale and default portrait orientation.
         *
         * @param scale the scale factor to be applied to the map dimensions
         */
        public A4MapSize(double scale) {
            super(0.190, 0.277, scale, false, "A4");
        }
    }

    /**
     * Concrete subclass representing the size of an A3 map.
     */
    public static class A3MapSize extends PageSize {

        /**
         * Constructs an A3MapSize object with the given scale and default portrait orientation.
         *
         * @param scale the scale factor to be applied to the map dimensions
         */
        public A3MapSize(double scale) {
            super(0.297, 0.420, scale, false, "A3");
        }
    }
}
