package dm.pagefit.render;

import dm.pagefit.map.Coordinate;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * The MapRenderer class is responsible for rendering map elements onto a BufferedImage.
 * It provides functionality for drawing functions and conversion between map coordinates
 * into image coordinates.
 */
public class MapRenderer
{
    // The BufferedImage on which the map elements will be rendered
    BufferedImage image;

    // The Graphics2D object used for drawing on the image
    Graphics2D g2d;

    // An instance of CoordinateTransformation used to convert map coordinates to image coordinates
    CoordinateTransformation transformation = new CoordinateTransformation();

    /**
     * Constructs a MapRenderer object with the specified BufferedImage.
     * Initializes the Graphics2D context and sets rendering hints for high-quality output.
     *
     * @param image The BufferedImage onto which the map will be rendered.
     */
    public MapRenderer(BufferedImage image)
    {
        this.image = image;
        this.g2d = image.createGraphics();

        // Set rendering hints for high-quality graphics output
        // NOT SURE HOW MUCH EFFECT THIS HAS? CAN'T DO ANY HARM
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    /**
     * Creates a rectangle based on the provided Rectangle2D object.
     * The rectangle is transformed to image coordinates.
     *
     * @param rectangle The Rectangle2D object representing the area to be drawn.
     * @return A new Rectangle2D.Double object representing the transformed rectangle.
     */
    public Rectangle2D.Double createRectangle(Rectangle2D rectangle)
    {
        double x = transformation.getXCoordinate(rectangle.getX());
        double y = transformation.getYCoordinate(rectangle.getY());

        double width = rectangle.getWidth() * transformation.scaleX;
        double height = rectangle.getHeight() * transformation.scaleY;

        return new Rectangle2D.Double(x, y, width, height);
    }

    /**
     * The CoordinateTransformation class handles the conversion of map coordinates to image coordinates.
     * It applies scaling and translation based on the bounds of the map and the dimensions of the image.
     */
    public class CoordinateTransformation
    {
        // The origin of the map in image coordinates
        private Coordinate origin;

        // Scaling factors for the x and y coordinates
        private double scaleX;
        private double scaleY;

        /**
         * Sets the transformation bounds based on the provided Rectangle2D.Double object.
         * The method calculates the scaling factors and sets the origin.
         *
         * @param bounds The bounds of the map area to be rendered.
         */
        public void setToBounds(Rectangle2D.Double bounds)
        {
            double scaleX = image.getWidth() / bounds.getWidth();
            double scaleY = image.getHeight() / bounds.getHeight();
            double scale = Math.min(scaleX, scaleY);
            this.scaleX = scale;
            this.scaleY = scale;
            this.origin = new Coordinate(bounds.getMinX(), bounds.getMinY());
        }

        /**
         * Converts a map x-coordinate to an image x-coordinate using the current scaling and origin.
         *
         * @param mapCoordinateX The x-coordinate in map space.
         * @return The corresponding x-coordinate in image space.
         */
        public double getXCoordinate(double mapCoordinateX)
        {
            return (mapCoordinateX - origin.getX()) * scaleX;
        }

        /**
         * Converts a map y-coordinate to an image y-coordinate using the current scaling and origin.
         *
         * @param mapCoordinateY The y-coordinate in map space.
         * @return The corresponding y-coordinate in image space.
         */
        public double getYCoordinate(double mapCoordinateY)
        {
            return (mapCoordinateY - origin.getY()) * scaleY;
        }
    }
}