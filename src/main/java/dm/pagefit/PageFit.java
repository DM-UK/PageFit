package dm.pagefit;

import dm.pagefit.clustering.KMidRangeClusterer;
import dm.pagefit.clustering.PageCluster;
import dm.pagefit.io.CoordinateFile;
import dm.pagefit.io.FileIO;
import dm.pagefit.map.Coordinate;
import dm.pagefit.map.PageSize;
import dm.pagefit.render.ClustererAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * The PageFit class orchestrates the process of clustering geographic coordinates
 * and optionally generating an animation of the clustering process. It handles
 * loading coordinates, clustering them, and saving the results to files.
 */
public class PageFit
{
    // Path to the file containing route coordinates
    private final static String ROUTE_FILENAME = "resources/route1.txt";

    // Path where the animation will be saved
    private final static String ANIMATION_FILENAME = "resources/animation1.gif";

    // Path to the file where cluster information will be saved
    private final static String CLUSTERS_FILENAME = "resources/route1clusters.txt";

    // Flag indicating whether to save the animation
    private boolean SAVE_ANIMATION = true;

    // Dimensions of the animation
    private final static int ANIMATION_WIDTH = 1200;
    private final static int ANIMATION_HEIGHT = 800;

    // Page size used for clustering
    private final static PageSize PAGE_SIZE = new PageSize.A3MapSize(25000);

    /**
     * Constructs a PageFit instance and starts the fitting process.
     */
    public PageFit()
    {
        fit();
    }

    /**
     * Coordinates the process of clustering and generating results.
     * - Loads coordinates from a file.
     * - Initializes the animator for the clustering process.
     * - Sets up the clusterer and performs clustering.
     * - Saves the clustering output to a file.
     */
    private void fit()
    {
        // Load coordinates from file
        List<Coordinate> coordinates = loadCoordinates();

        // Initialize the animator for the clustering process
        ClustererAnimator animator = initializeAnimator(coordinates);

        // Initialize the clusterer and perform clustering
        KMidRangeClusterer clusterer = initializeClusterer(coordinates, animator);
        List<PageCluster> clusters = clusterer.fit();

        // Save the cluster data to a file
        saveClusters(clusters);
    }

    /**
     * Saves the clustering results to a file.
     * Each cluster's data is formatted as a string array and written to the file.
     *
     * @param clusters The list of clusters to be saved.
     */
    void saveClusters(List<PageCluster> clusters)
    {
        List<String[]> strings = new ArrayList<>();

        for (PageCluster cluster : clusters)
        {
            String[] strArr = new String[]{
                    "" + cluster.getClusterCentre().getX(),
                    "" + cluster.getClusterCentre().getY(),
                    "" + cluster.getPageSize().getPageSize(),
                    "" + cluster.getPageSize().getOrientation(),
                    "" + cluster.getPageSize().getScale()
            };

            strings.add(strArr);
        }

        FileIO.writeFileLines(CLUSTERS_FILENAME, ",", strings);
    }

    /**
     * Loads coordinates from a file.
     *
     * @return A list of coordinates loaded from the file.
     */
    private List<Coordinate> loadCoordinates()
    {
        return CoordinateFile.loadCoordinates(ROUTE_FILENAME);
    }

    /**
     * Initializes the ClustererAnimator with the given coordinates and animation dimensions.
     *
     * @param coordinates The list of coordinates to be used in the animation.
     * @return An instance of ClustererAnimator.
     */
    private ClustererAnimator initializeAnimator(List<Coordinate> coordinates)
    {
        return new ClustererAnimator(ANIMATION_FILENAME, coordinates, ANIMATION_WIDTH, ANIMATION_HEIGHT);
    }

    /**
     * Initializes the KMidRangeClusterer with the given coordinates, page size, and animator.
     * Optionally sets the animator as a listener if SAVE_ANIMATION is true.
     *
     * @param coordinates The list of coordinates to be clustered.
     * @param animator The animator for generating the clustering animation.
     * @return An instance of KMidRangeClusterer.
     */
    private KMidRangeClusterer initializeClusterer(List<Coordinate> coordinates, ClustererAnimator animator)
    {
        KMidRangeClusterer clusterer = new KMidRangeClusterer(10, coordinates, PAGE_SIZE);

        if (SAVE_ANIMATION)
            clusterer.setListener(animator);

        return clusterer;
    }

    public static void main(String[] args)
    {
        new PageFit();
    }
}