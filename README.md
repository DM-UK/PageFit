
### PageFit ###
This project is a Java implementation of a modified K-means clustering algorithm to find the least amount of 'pages' through a polyline.

![MapMate](https://github.com/DM-UK/PageFit/blob/master/resources/route3midrange.gif)



### Printing Optimization ###
When printing a map route across multiple pages, it is often useful to minimize the number of pages used. Determining how best to segment the route can be non-trivial and made more complicated by the ability to rotate into landscape/portrait orientation.

___
## Algorithmn ##
Modified K-means clustering: 

- Loop until all coordinates are covered by a clusters page
  - Add new cluster. Starting position is selected as a random uncovered coordinate
  - Iterate x times
    - Assign coordinates to its nearest cluster
    - Calculate cluster centres as the middle of its range (midrange) of assigned coordinates
    - Choose orientation that fits the most uncovered coordinates
___
## Usage ##



```
List<Coordinate> coordinates = CoordinateFile.loadCoordinates(filename);
//A3 paper size @ 25000 scale
PageSize pageSize = new PageSize.A3MapSize(25000);
//10 iterations before adding a new cluster 
KMidRangeClusterer clusterer = new KMidRangeClusterer(10, coordinates, pageSize);
List<PageCluster> clusters = clusterer.fit();
```
___
## Animation ##
An optional .gif animation utility is included to help visualize/debug - each frame is an iteration of the clustering algorithmn.
```
ClusterAnimator animator = new ClusterAnimator("output.gif", points, animationWidth, animationHeight);
clusterer.setListener(animator);
```





