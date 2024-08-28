package dm.pagefit.clustering;

import dm.pagefit.map.Coordinate;
import dm.pagefit.map.PageSize;
import dm.pagefit.map.RectangleUtils;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class PageCluster implements PageClusterable
{
    private Coordinate centre; // Center of the cluster
    private Rectangle2D.Double pageRectangle; // Rectangle representing the page size and orientation
    private Rectangle2D.Double clusterRectangle; // Bounding box of the cluster points
    private PageSize pageSize; // Page size and orientation information

    private List<Coordinate> points = new ArrayList<>(); // List of points in the cluster
    private double averageIndexOfClusterPoints = -1; // Average index of the cluster points in a list of all coordinates

    /**
     * Constructs a PageCluster with a specified center and page size.
     *
     * @param centre   the initial center of the cluster
     * @param pageSize the size and orientation of the page for this cluster
     */
    public PageCluster(Coordinate centre, PageSize pageSize)
    {
        this.centre = centre;
        this.pageSize = pageSize;
    }

    /**
     * Returns the PageSize object of this cluster.
     *
     * @return the PageSize of the cluster
     */
    public PageSize getPageSize()
    {
        return pageSize;
    }

    /**
     * Updates the cluster's bounding box and center based on the current list of points.
     */
    public void updateCluster()
    {
        if (!points.isEmpty())
        {
            // Calculate the bounding box of the cluster points
            clusterRectangle = RectangleUtils.createRectangleFromBounds(points);
            // Update the center of the cluster based on the bounding box
            centre = RectangleUtils.getRectangleCentre(clusterRectangle);
        }

        chooseOrientation();
    }

    /**
     * Calculates the average index of the cluster points in the provided list of all coordinates.
     *
     * @param allCoordinates the list of coordinates in which the cluster points are indexed
     */
    public void calculateOrderIndex(List<Coordinate> allCoordinates)
    {
        double sum = 0;

        for (Coordinate clusterCoordinate : points)
            sum += allCoordinates.indexOf(clusterCoordinate);

        // Calculate the average index
        averageIndexOfClusterPoints = sum / points.size();
    }

    /**
     * Returns the average index of the cluster points .
     *
     * @return the average index of the cluster points
     */
    @Override
    public double getAverageIndexOfClusterPoints()
    {
        return averageIndexOfClusterPoints;
    }

    /**
     * Returns the center coordinate of the cluster.
     *
     * @return the center of the cluster
     */
    @Override
    public Coordinate getClusterCentre()
    {
        return centre;
    }

    /**
     * Returns the bounding box of the cluster.
     *
     * @return the bounding box of the cluster points
     */
    @Override
    public Rectangle2D.Double getClusterRectangle()
    {
        return clusterRectangle;
    }

    /**
     * Returns the rectangle representing the page size and orientation.
     *
     * @return the page rectangle
     */
    @Override
    public Rectangle2D.Double getPageRectangle()
    {
        return pageRectangle;
    }

    /**
     * Returns the list of points in the cluster.
     *
     * @return the list of points in the cluster
     */
    @Override
    public List<Coordinate> getPoints()
    {
        return points;
    }

    /**
     * Calculates the squared distance from the center of the cluster to a given point.
     *
     * @param point the point to which the distance is calculated
     * @return the squared distance from the center of the cluster to the point
     */
    public double distance(Coordinate point)
    {
        double dx = getClusterCentre().getX() - point.getX();
        double dy = getClusterCentre().getY() - point.getY();

        return dx * dx + dy * dy; // Return squared distance to avoid computing the square root
    }

    /**
     * Chooses the best orientation (portrait or landscape) for the page based on the number of points
     * that fit within each orientation.
     */
    public void chooseOrientation()
    {
        // Check how many points fit within a portrait-oriented page
        pageSize.setPortrait();
        Rectangle2D.Double portraitRectangle = RectangleUtils.createRectangle(centre, pageSize.getScaledWidth(), pageSize.getScaledHeight());

        // Check how many points fit within a landscape-oriented page
        pageSize.setLandscape();
        Rectangle2D.Double landscapeRectangle = RectangleUtils.createRectangle(centre, pageSize.getScaledWidth(), pageSize.getScaledHeight());

        int pointsInPortrait = 0;
        int pointsInLandscape = 0;

        // Count the number of points in each orientation
        for (Coordinate point : points)
        {
            if (portraitRectangle.contains(point.getX(), point.getY()))
                pointsInPortrait++;

            if (landscapeRectangle.contains(point.getX(), point.getY()))
                pointsInLandscape++;
        }

        // Choose the orientation that contains the most points
        if (pointsInPortrait >= pointsInLandscape)
        {
            pageRectangle = portraitRectangle;
            pageSize.setPortrait();
        }
        else
        {
            pageRectangle = landscapeRectangle;
            pageSize.setLandscape();
        }
    }
}
