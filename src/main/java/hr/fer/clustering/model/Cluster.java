package hr.fer.clustering.model;

import java.util.HashSet;
import java.util.Set;

public class Cluster {

    private final Set<Point> points = new HashSet<>();

    private Point clustroid;

    public Cluster(){}

    public Point getClustroid() {
        return clustroid;
    }

    public void setClustroid(Point clustroid) {
        this.clustroid = clustroid;
    }

    public boolean addPoint(Point point){
        return points.add(point);
    }

    public boolean addAllPoints(Set<Point> points){
        return this.points.addAll(points);
    }

    public boolean removePoint(Point point){
        return points.remove(point);
    }

    public Set<Point> getPoints() {
        return points;
    }

    /**
     * Calculates the minimum euclidean distance of any point in the cluster to the given point.
     * @param point The point to calculate the minimum distance to.
     * @return The minimum distance from any point in the cluster to the given point.
     * @throws NullPointerException If the given point is null.
     * @throws IllegalStateException If the cluster has no points.
     */
    public double minimumEuclideanDistance(Point point){
        if(point == null){
            throw new NullPointerException();
        }

        if(points.size() == 0){
            throw new IllegalStateException();
        }

        double minimumDistance = Double.MAX_VALUE;
        for(Point p: points){
            double distance = p.euclideanDistance(point);
            minimumDistance = Math.min(distance, minimumDistance);
        }

        return minimumDistance;
    }

    /**
     * Calculates the minimum euclidean distance of any point in the cluster to any point in the given cluster.
     * @param other The cluster to calculate the minimum distance to.
     * @return The minimum distance from any point in the cluster to any point in the given cluster.
     * @throws NullPointerException If the given cluster is null.
     * @throws IllegalStateException If this or the given cluster have no points.
     */
    public double minimumEuclideanDistance(Cluster other){
        if(other == null){
            throw new NullPointerException();
        }

        if(points.size() == 0 || other.points.size() == 0){
            throw new IllegalStateException();
        }

        double minimumDistance = Double.MAX_VALUE;
        for(Point p: points){
            double distance = other.minimumEuclideanDistance(p);
            minimumDistance = Math.min(distance, minimumDistance);
        }

        return minimumDistance;
    }

    public Point calculateCentroid() {
        int dimension = points.stream()
                .findAny()
                .orElseThrow(IllegalStateException::new)
                .dimension();

        double[] components = new double[dimension];
        for(int i = 0; i < dimension; i++){
            components[i] = 0;
        }

        for(Point point: points){
            for(int i = 0; i < dimension; i++){
                components[i] += point.getComponent(i);
            }
        }

        for(int i = 0; i < dimension; i++){
            components[i] = components[i] / points.size();
        }

        return new Point(components);
    }

}
