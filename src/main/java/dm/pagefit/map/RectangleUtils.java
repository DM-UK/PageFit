package dm.pagefit.map;

import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Utility class for performing operations related to rectangles, such as creating rectangles from bounds,
 * calculating rectangle centers, and creating rectangles based on a center point.
 */
public class RectangleUtils
{
    /**
     * Creates a rectangle from a list of coordinates by calculating the bounding box of the points.
     *
     * @param points the list of coordinates defining the area to be bounded by the rectangle
     * @return a Rectangle2D.Double object representing the bounding box of the given points
     */
    public static Rectangle2D.Double createRectangleFromBounds(List<Coordinate> points)
    {
        CoordinateBounds bounds = new CoordinateBounds(points);
        return new Rectangle2D.Double(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
    }

    /**
     * Calculates and returns the center coordinate of a given rectangle.
     *
     * @param rectangle the rectangle for which the center is to be calculated
     * @return a Coordinate object representing the center of the rectangle
     */
    public static Coordinate getRectangleCentre(Rectangle2D.Double rectangle)
    {
        double centreX = rectangle.getX() + (rectangle.getWidth() / 2.0);
        double centreY = rectangle.getY() + (rectangle.getHeight() / 2.0);
        return new Coordinate(centreX, centreY);
    }

    /**
     * Creates a rectangle centered at a specified coordinate with given width and height.
     *
     * @param centre the center coordinate of the rectangle
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     * @return a Rectangle2D.Double object representing the rectangle with the specified center, width, and height
     */
    public static Rectangle2D.Double createRectangle(Coordinate centre, double width, double height)
    {
        double upperLeftCornerX = centre.getX() - width / 2.0;
        double upperLeftCornerY = centre.getY() - height / 2.0;
        return new Rectangle2D.Double(upperLeftCornerX, upperLeftCornerY, width, height);
    }

    /**
     * Helper class for calculating the bounding box of a list of coordinates.
     */
    public static class CoordinateBounds
    {
        private List<Coordinate> points;

        /**
         * Constructs a CoordinateBounds object for the given list of coordinates.
         *
         * @param points the list of coordinates for which the bounding box is to be calculated
         */
        public CoordinateBounds(List<Coordinate> points) {
            this.points = points;
        }

        /**
         * Returns the minimum x-coordinate among the points.
         *
         * @return the minimum x-coordinate
         */
        public double getMinX() {
            return points.stream()
                    .mapToDouble(Coordinate::getX)
                    .min()
                    .getAsDouble();
        }

        /**
         * Returns the maximum x-coordinate among the points.
         *
         * @return the maximum x-coordinate
         */
        public double getMaxX() {
            return points.stream()
                    .mapToDouble(Coordinate::getX)
                    .max()
                    .getAsDouble();
        }

        /**
         * Returns the minimum y-coordinate among the points.
         *
         * @return the minimum y-coordinate
         */
        public double getMinY() {
            return points.stream()
                    .mapToDouble(Coordinate::getY)
                    .min()
                    .getAsDouble();
        }

        /**
         * Returns the maximum y-coordinate among the points.
         *
         * @return the maximum y-coordinate
         */
        public double getMaxY() {
            return points.stream()
                    .mapToDouble(Coordinate::getY)
                    .max()
                    .getAsDouble();
        }

        /**
         * Calculates and returns the width of the bounding box.
         *
         * @return the width of the bounding box
         */
        public double getWidth() {
            return getMaxX() - getMinX();
        }

        /**
         * Calculates and returns the height of the bounding box.
         *
         * @return the height of the bounding box
         */
        public double getHeight() {
            return getMaxY() - getMinY();
        }
    }
}
