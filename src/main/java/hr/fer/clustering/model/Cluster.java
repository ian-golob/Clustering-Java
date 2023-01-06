package hr.fer.clustering.model;

import java.util.HashSet;
import java.util.Set;

public class Cluster {

    private final Set<Point> points = new HashSet<>();

    private Point centroid;

    public Cluster(){}

    public Point getCentroid() {
        return centroid;
    }

    public void setCentroid(Point centroid) {
        this.centroid = centroid;
    }

    public boolean addPoint(Point point){
        return points.add(point);
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
