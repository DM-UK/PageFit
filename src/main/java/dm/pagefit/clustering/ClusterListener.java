package dm.pagefit.clustering;

import java.util.List;

/**
 * Interface for listening to updates and completion events related to clustering operations.
 */
public interface ClusterListener
{
    /**
     * Called when the cluster is updated.
     *
     * @param clusters a list of updated clusters; the elements in the list implement the PageClusterable interface
     */
    void updated(List<? extends PageClusterable> clusters);

    /**
     * Called when the clustering process has finished.
     */
    void finished();
}