package hr.fer.clustering.methods;

import hr.fer.clustering.model.Cluster;
import hr.fer.clustering.model.Point;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class KMeans<T> implements ClusteringMethod<T> {

    private final int k;

    public KMeans(int k) {
        if(k < 2){
            throw new IllegalArgumentException();
        }

        this.k = k;
    }

    @Override
    public List<Integer> predict(List<Point> points) {
        checkForValidPoints(points);

        //Init clusters

        List<Point> remainingPoints = new ArrayList<>(points);
        List<Cluster> clusters = new ArrayList<>(k);

        int randomPointIndex = ThreadLocalRandom.current().nextInt(0, points.size());
        Point firstCentroid = points.get(randomPointIndex);
        remainingPoints.remove(randomPointIndex);

        Cluster firstCluster = new Cluster();
        firstCluster.addPoint(firstCentroid);
        firstCluster.setClustroid(firstCentroid);
        clusters.add(firstCluster);

        for(int i = 1; i < k; i++){
            double maxDistance = Double.MIN_VALUE;
            Point chosenCentroid = null;

            for(Point point: remainingPoints){
                double distance = Double.MAX_VALUE;

                for(int j = 0; j < i; j++){
                    distance = Math.min(
                            distance,
                            clusters.get(j).getClustroid().euclideanDistance(point)
                    );
                }

                if(distance > maxDistance){
                    chosenCentroid = point;
                    maxDistance = distance;
                }
            }

            Cluster cluster = new Cluster();
            cluster.addPoint(chosenCentroid);
            cluster.setClustroid(chosenCentroid);
            clusters.add(cluster);

            remainingPoints.remove(chosenCentroid);
        }


        // Cluster points

        for(Point point: remainingPoints){
            double minDistance = Double.MAX_VALUE;
            Cluster chosenCluster = null;

            for(Cluster cluster: clusters){
                double distanceToCentroid = point.euclideanDistance(cluster.getClustroid());

                if(distanceToCentroid < minDistance){
                    chosenCluster = cluster;
                    minDistance = distanceToCentroid;
                }
            }

            chosenCluster.addPoint(point);
            chosenCluster.setClustroid(chosenCluster.calculateCentroid());
        }


        return getClusterNumbers(points, clusters);
    }
}
