package dm.pagefit.render;

import dm.pagefit.clustering.PageClusterable;
import dm.pagefit.map.Coordinate;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * The ClusterRenderer class extends MapRenderer to provide functionality for rendering clusters
 * on a BufferedImage. It draws centroids, cluster bounds and cluster 'pages' using specified colors.
 */
public class ClusterRenderer extends MapRenderer
{
    // Constants for rendering colors
    private static final Color BACKGROUND_COLOUR = Color.WHITE;
    private static final Color CENTROID_COLOUR = Color.RED;
    private static final Color PAGE_BOUNDING_BOX_COLOUR = Color.BLACK;
    private static final Color CENTROID_BOUNDING_BOX_COLOUR = Color.RED;

    // Diameters for rendering points and centroids
    private int POINT_DIAMETER = 4;
    private int CENTROID_DIAMETER = 6;

    /**
     * Constructs a ClusterRenderer with the specified BufferedImage and bounding box.
     * Initializes the base MapRenderer and sets up the transformation based on the bounding box.
     * Also fills the image background with the specified background color.
     *
     * @param image  The BufferedImage onto which the cluster will be rendered.
     * @param bounds The bounding box defining the map space to be rendered.
     */
    public ClusterRenderer(BufferedImage image, Rectangle2D.Double bounds)
    {
        super(image);
        transformation.setToBounds(bounds);

        // Set background color and fill the image
        g2d.setColor(BACKGROUND_COLOUR);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    /**
     * Renders a cluster onto the image
     *
     * @param cluster     The cluster to be rendered.
     * @param clusterColor The color used to render the cluster points.
     */
    public void render(PageClusterable cluster, Color clusterColor)
    {
        // Set color for cluster points and draw them
        g2d.setColor(clusterColor);
        drawPoints(cluster);

        // Set color for page bounding box and draw it
        g2d.setColor(PAGE_BOUNDING_BOX_COLOUR);
        drawPageBounds(cluster);

        // Set color for cluster bounding box and draw it
        g2d.setColor(CENTROID_BOUNDING_BOX_COLOUR);
        drawClusterBounds(cluster);

        // Set color for centroid and draw it
        g2d.setColor(CENTROID_COLOUR);
        drawCentroid(cluster);
    }

    /**
     * Draws the points of the cluster on the image.
     * Each point is rendered as a filled circle with a diameter defined by POINT_DIAMETER.
     *
     * @param cluster The cluster whose points are to be drawn.
     */
    private void drawPoints(PageClusterable cluster)
    {
        for (Coordinate point : cluster.getPoints())
        {
            double x = transformation.getXCoordinate(point.getX());
            double y = transformation.getYCoordinate(point.getY());
            Shape circle = new Ellipse2D.Double(x - POINT_DIAMETER / 2.0, y - POINT_DIAMETER / 2.0, POINT_DIAMETER, POINT_DIAMETER);
            g2d.fill(circle);
        }
    }

    /**
     * Draws the bounding box of the page on which the cluster is located.
     *
     * @param cluster The cluster used to get the page bounding box.
     */
    private void drawPageBounds(PageClusterable cluster)
    {
        Rectangle2D.Double pageBounds = cluster.getPageRectangle();
        Rectangle2D.Double pageBoundsInMapSpace = createRectangle(pageBounds);
        g2d.draw(pageBoundsInMapSpace);
    }

    /**
     * Draws the bounding box around the cluster.
     *
     * @param cluster The cluster used to get the cluster bounding box.
     */
    private void drawClusterBounds(PageClusterable cluster)
    {
        Rectangle2D.Double clusterBounds = cluster.getClusterRectangle();
        Rectangle2D.Double clusterBoundsInMapSpace = createRectangle(clusterBounds);
        g2d.draw(clusterBoundsInMapSpace);
    }

    /**
     * Draws the centroid of the cluster as a filled circle.
     * The centroid is rendered with a diameter defined by CENTROID_DIAMETER.
     *
     * @param cluster The cluster whose centroid is to be drawn.
     */
    private void drawCentroid(PageClusterable cluster)
    {
        double x = transformation.getXCoordinate(cluster.getClusterCentre().getX());
        double y = transformation.getYCoordinate(cluster.getClusterCentre().getY());
        Shape circle = new Ellipse2D.Double(x - CENTROID_DIAMETER / 2.0, y - CENTROID_DIAMETER / 2.0, CENTROID_DIAMETER, CENTROID_DIAMETER);
        g2d.fill(circle);
    }
}

