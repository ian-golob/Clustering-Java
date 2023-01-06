package hr.fer.clustering.swing;

import hr.fer.clustering.methods.ClusteringMethod;
import hr.fer.clustering.model.Point;

import java.util.ArrayList;
import java.util.List;

public class ClusterGraphModel {

    private final int minX;

    private final int maxX;

    private final int minY;

    private final int maxY;

    private List<Point> points;

    private List<Integer> coloring;

    private ClusteringMethod<Point> method;

    public ClusterGraphModel(int minX, int maxX, int minY, int maxY, List<Point> points) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.points = points;
        this.coloring = new ArrayList<>(points.size());
        for(int i = 0; i < points.size(); i++){
            coloring.add(0);
        }
    }

    public ClusterGraphModel(int minX, int maxX, int minY, int maxY, List<Point> points, List<Integer> coloring) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.points = points;
        this.coloring = coloring;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public List<Integer> getColoring() {
        return coloring;
    }

    public void setColoring(List<Integer> coloring) {
        this.coloring = coloring;
    }

    public ClusteringMethod<Point> getMethod() {
        return method;
    }

    public void setMethod(ClusteringMethod<Point> method) {
        this.method = method;
    }

}
