package dm.pagefit.clustering;

import dm.pagefit.map.Coordinate;

import java.awt.geom.Rectangle2D;
import java.util.List;

//to interface with graphics/gui
public interface PageClusterable
{
    double getAverageIndexOfClusterPoints();
    public Coordinate getClusterCentre();
    public Rectangle2D.Double getClusterRectangle();
    public Rectangle2D.Double getPageRectangle();
    public List<Coordinate> getPoints();
}
