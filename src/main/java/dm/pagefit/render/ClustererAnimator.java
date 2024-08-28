package dm.pagefit.render;

import dm.pagefit.clustering.ClusterListener;
import dm.pagefit.clustering.PageClusterable;
import dm.pagefit.map.Coordinate;
import dm.pagefit.map.RectangleUtils;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 * The ClustererAnimator class is responsible for generating an animated GIF that visualizes clustering.
 * It implements the ClusterListener interface, which allows it to respond
 * to updates during the clustering process and render each step as a frame in the GIF.
 */
public class ClustererAnimator implements ClusterListener
{
    // GIF encoder to manage the creation of the animated GIF
    private AnimatedGifEncoder gif = new AnimatedGifEncoder();

    // Dimensions of the animation
    private int animationWidth;
    private int animationHeight;

    // Bounding box of the coordinate set
    private Rectangle2D.Double bounds;

    /**
     * Constructs a ClustererAnimator object with the specified parameters.
     *
     * @param filename        The name of the output GIF file.
     * @param coordinates     The list of coordinates that are being clustered.
     * @param animationWidth  The width of the animation in pixels.
     * @param animationHeight The height of the animation in pixels.
     */
    public ClustererAnimator(String filename, List<Coordinate> coordinates, int animationWidth, int animationHeight)
    {
        this.animationWidth = animationWidth;
        this.animationHeight = animationHeight;

        // Compute the bounding box for the animation
        this.bounds = RectangleUtils.createRectangleFromBounds(coordinates);

        // Initialize the GIF encoder with the specified file name
        gif.start(filename);

        // Set the delay between frames in the GIF (150 ms)
        gif.setDelay(150);

        // Set the quality of the GIF (lower value indicates higher quality)
        gif.setQuality(10);
    }

    /**
     * Called when clustering is updated. This method creates a new frame for the animation.
     *
     * @param clusters The list of clusters to be rendered in this frame.
     */
    @Override
    public void updated(List<? extends PageClusterable> clusters)
    {
        // Create a new image for the current frame
        BufferedImage image = new BufferedImage(animationWidth, animationHeight, BufferedImage.TYPE_INT_RGB);
        ClusterRenderer frame = new ClusterRenderer(image, bounds);

        // Use the same seed for the random number generator to ensure consistent colors across frames
        Random rng = new Random(999);

        // Render each cluster with a unique color
        for (PageClusterable cluster : clusters)
        {
            Color clusterColor = new Color(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
            frame.render(cluster, clusterColor);
        }

        // Add the rendered frame to the GIF
        gif.addFrame(image);
    }

    /**
     * Called when clustering is finished. This method finalizes the GIF creation.
     */
    @Override
    public void finished()
    {
        gif.finish();
    }
}
