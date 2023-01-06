package hr.fer.clustering.methods;

import hr.fer.clustering.model.Cluster;
import hr.fer.clustering.model.Point;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
        if(points == null){
            throw new NullPointerException();
        }

        int dimension = -1;
        boolean first = true;
        for(Point point: points){
            if(point == null){
                throw new NullPointerException();
            }

            if(first){
                dimension = point.dimension();
                first = false;
            } else {
                if(dimension != point.dimension()){
                    throw new IllegalArgumentException("Point dimensions do not match");
                }
            }
        }

        //Init clusters

        List<Point> remainingPoints = new ArrayList<>(points);
        List<Cluster> clusters = new ArrayList<>(k);

        int randomPointIndex = ThreadLocalRandom.current().nextInt(0, points.size() + 1);
        Point firstCentroid = points.get(randomPointIndex);
        remainingPoints.remove(randomPointIndex);

        Cluster firstCluster = new Cluster();
        firstCluster.addPoint(firstCentroid);
        firstCluster.setCentroid(firstCentroid);
        clusters.add(firstCluster);

        for(int i = 1; i < k; i++){
            double maxDistance = Double.MIN_VALUE;
            Point chosenCentroid = null;

            for(Point point: remainingPoints){
                double distance = Double.MAX_VALUE;

                for(int j = 0; j < i; j++){
                    distance = Math.min(
                            distance,
                            clusters.get(j).minimumEuclideanDistance(point)
                    );
                }

                if(distance > maxDistance){
                    chosenCentroid = point;
                    maxDistance = distance;
                }
            }

            Cluster cluster = new Cluster();
            cluster.addPoint(chosenCentroid);
            cluster.setCentroid(chosenCentroid);
            clusters.add(cluster);

            remainingPoints.remove(chosenCentroid);
        }


        // Cluster points

        for(Point point: remainingPoints){
            double minDistance = Double.MAX_VALUE;
            Cluster chosenCluster = null;

            for(Cluster cluster: clusters){
                double distanceToCentroid = point.euclideanDistance(cluster.getCentroid());

                if(distanceToCentroid < minDistance){
                    chosenCluster = cluster;
                    minDistance = distanceToCentroid;
                }
            }

            chosenCluster.addPoint(point);
            chosenCluster.setCentroid(chosenCluster.calculateCentroid());
        }


        Map<Point, Integer> clusteredPointMap = new HashMap<>();
        for(int i = 0; i < clusters.size(); i++){
            for(Point point: clusters.get(i).getPoints()){
                clusteredPointMap.put(point, i + 1);
            }
        }

        return points.stream()
                .map(clusteredPointMap::get)
                .collect(Collectors.toList());
    }
}
