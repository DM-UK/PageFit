package dm.pagefit.clustering;

import dm.pagefit.map.Coordinate;
import dm.pagefit.map.PageSize;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code KMidRangeClusterer} class implements a clustering algorithm that partitions a set of coordinates
 * into clusters based on proximity and page size constraints. The algorithm iteratively adds clusters and
 * refines them until all points are contained within the defined page boundaries.
 */
public class KMidRangeClusterer {

    private int iterationsBeforeAddingNewCluster;
    private PageSize pageSize;
    private List<Coordinate> points;
    private List<PageCluster> clusters = new ArrayList<>();
    private ClusterListener listener;

    /**
     * Constructs a new {@code KMidRangeClusterer} with the specified parameters.
     *
     * @param iterationsBeforeAddingNewCluster the number of iterations to perform before adding a new cluster
     * @param points                           the list of coordinates to be clustered
     * @param pageSize                         the page size constraints for clustering
     */
    public KMidRangeClusterer(int iterationsBeforeAddingNewCluster, List<Coordinate> points, PageSize pageSize) {
        this.iterationsBeforeAddingNewCluster = iterationsBeforeAddingNewCluster;
        this.points = points;
        this.pageSize = pageSize;
    }

    /**
     * Sets a listener to receive updates during the clustering process.
     *
     * @param listener the {@code ClusterListener} to be notified of updates
     */
    public void setListener(ClusterListener listener) {
        this.listener = listener;
    }

    /**
     * Performs the clustering operation by iteratively adding and refining clusters until all points
     * are contained within the clusters' page boundaries. The method also sorts the clusters based on
     * the average index of their points to maintain order.
     *
     * @return a list of sorted {@code PageCluster} objects after clustering is complete
     */
    public List<PageCluster> fit() {
        // Continue clustering until all points are within cluster pages
        while (!getPointsNotInPage().isEmpty()) {
            addCluster();

            for (int i = 0; i < iterationsBeforeAddingNewCluster; i++)
            {
                assignPointsToClusters();

                for (PageCluster cluster : clusters) {
                    cluster.updateCluster();
                }

                // Notify listener of updates if present
                if (listener != null) {
                    //listener.updated(clusters);
                }
            }
        }

        // Calculate the average index for ordering clusters
        for (PageCluster cluster : clusters) {
            cluster.calculateOrderIndex(points);
        }

        // Sort clusters based on the average index of their points
        List<PageCluster> sortedClusters = clusters.stream()
                .sorted(Comparator.comparingDouble(PageCluster::getAverageIndexOfClusterPoints))
                .collect(Collectors.toList());

        // Final update and notification
        if (listener != null) {
            listener.updated(sortedClusters);
            listener.finished();
        }

        return sortedClusters;
    }

    /**
     * Assigns each point to the nearest cluster based on Euclidean distance.
     * Clears previous cluster assignments before reassignment.
     */
    private void assignPointsToClusters() {
        // Clear previous points from all clusters
        for (PageCluster cluster : clusters) {
            cluster.getPoints().clear();
        }

        // Assign each point to the nearest cluster
        for (Coordinate point : points) {
            PageCluster nearestCluster = null;
            double nearestDistance = Double.MAX_VALUE;

            for (PageCluster cluster : clusters) {
                double distance = cluster.distance(point);

                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestCluster = cluster;
                }
            }

            if (nearestCluster != null) {
                nearestCluster.getPoints().add(point);
            }
        }
    }

    /**
     * Retrieves a list of points that are not contained within any of the clusters' page rectangles.
     *
     * @return a list of {@code Coordinate} objects not within any cluster's page boundaries
     */
    public List<Coordinate> getPointsNotInPage() {
        List<Coordinate> pointsNotInPage = new ArrayList<>(points);

        for (PageCluster cluster : clusters) {
            Rectangle2D.Double pageRect = cluster.getPageRectangle();

            if (pageRect != null) {
                pointsNotInPage.removeIf(point -> pageRect.contains(point.getX(), point.getY()));
            }
        }

        return pointsNotInPage;
    }

    /**
     * Adds a new cluster to the list of clusters using a randomly selected point that is not
     * currently within any existing cluster's page rectangle.
     */
    private void addCluster() {
        List<Coordinate> pointsNotInPage = getPointsNotInPage();

        if (!pointsNotInPage.isEmpty()) {
            // Select a random point from the points not in any page
            Coordinate randomPoint = pointsNotInPage.get((int) (Math.random() * pointsNotInPage.size()));

            // Create a copy of the current pageSize to avoid modifying the original
            PageSize pageSizeCopy = new PageSize(
                    pageSize.getUnscaledWidth(),
                    pageSize.getUnscaledHeight(),
                    pageSize.getScale(),
                    pageSize.getOrientation(),
                    pageSize.getPageSize()
            );

            // Create and add the new cluster
            PageCluster newCluster = new PageCluster(randomPoint, pageSizeCopy);
            clusters.add(newCluster);
        }
    }
}