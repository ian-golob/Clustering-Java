package hr.fer.clustering.methods;

import hr.fer.clustering.model.Cluster;
import hr.fer.clustering.model.Point;
import hr.fer.clustering.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Hierarchical<T> implements ClusteringMethod<T> {

    private List<Cluster> clusters;

    private final StoppingRule stoppingRule;
    private final MergeRule mergeRule;

    public Hierarchical(StoppingRule stoppingRule, MergeRule mergeRule) {
        this.stoppingRule = stoppingRule;
        this.mergeRule = mergeRule;
    }


    @Override
    public List<Integer> predict(List<Point> points) {
        checkForValidPoints(points);

        // Init clusters
        clusters = new ArrayList<>(points.size());
        points.forEach(point -> {
            Cluster cluster = new Cluster();
            cluster.addPoint(point);
            cluster.setClustroid(point);
            clusters.add(cluster);
        });

        while(!stoppingRule.stop(this)){
            Pair<Integer, Integer> clustersToMerge = mergeRule.clustersToMerge(this);

            Cluster joinedCluster = clusters.get(clustersToMerge.first());
            Cluster otherCluster =clusters.get(clustersToMerge.second());
            joinedCluster.addAllPoints(otherCluster.getPoints());
            joinedCluster.setClustroid(joinedCluster.calculateCentroid());

            clusters.remove(clustersToMerge.second().intValue());
        }

        return getClusterNumbers(points, clusters);
    }



    @FunctionalInterface
    public interface StoppingRule {
        <T> boolean stop(Hierarchical<? extends T> method);
    }

    public static class ClusterNumberStoppingRule implements StoppingRule{

        private final int clusterNumberStop;

        public ClusterNumberStoppingRule(int clusterNumberStop){
            this.clusterNumberStop = clusterNumberStop;
        }

        @Override
        public <T> boolean stop(Hierarchical<? extends T> method) {
            return method.clusters.size() <= clusterNumberStop;
        }

    }

    public interface MergeRule {
        /**
         * Returns the index of clusters to merge.
         * @param method The clustering method.
         * @return The pair of indexes of clusters in the cluster list to merge.
         * @param <T> Cluster object type.
         */
       <T> Pair<Integer, Integer> clustersToMerge(Hierarchical<? extends T> method);
    }

    public static class ClosestCentroidsMergeRule implements MergeRule {

        @Override
        public <T> Pair<Integer, Integer> clustersToMerge(Hierarchical<? extends T> method) {
            Pair<Integer, Integer> closestPair = null;
            double closestDistance = Double.MAX_VALUE;

            int numberOfClusters = method.clusters.size();
            for(int i = 0; i < numberOfClusters; i++){
                for(int j = i + 1; j < numberOfClusters; j++){
                    if(i == j){
                        continue;
                    }

                    double distance =
                            method.clusters.get(i).getClustroid()
                            .euclideanDistance(
                            method.clusters.get(j).getClustroid());

                    if(distance < closestDistance){
                        closestDistance = distance;
                        closestPair = new Pair<>(i, j);
                    }
                }
            }

            return closestPair;
        }
    }

    public static class MinimumClusterDistanceMergeRule implements MergeRule {

        @Override
        public <T> Pair<Integer, Integer> clustersToMerge(Hierarchical<? extends T> method) {
            Pair<Integer, Integer> closestPair = null;
            double closestDistance = Double.MAX_VALUE;

            int numberOfClusters = method.clusters.size();
            for(int i = 0; i < numberOfClusters; i++){
                for(int j = i + 1; j < numberOfClusters; j++){
                    if(i == j){
                        continue;
                    }

                    Cluster cluster1 = method.clusters.get(i);
                    Cluster cluster2 = method.clusters.get(j);

                    double distance = cluster1.minimumEuclideanDistance(cluster2);

                    if(distance < closestDistance){
                        closestDistance = distance;
                        closestPair = new Pair<>(i, j);
                    }
                }
            }

            return closestPair;
        }
    }

}
