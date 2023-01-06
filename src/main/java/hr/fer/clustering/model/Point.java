package hr.fer.clustering.model;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

public class Point {

    private final double[] components;

    public Point(double[] components) {
        if(components == null){
            throw new NullPointerException();
        }

        this.components = components;
    }

    public double[] getComponents() {
        return components;
    }

    public double getComponent(int i) {
        return components[i];
    }

    public int dimension(){
        return components.length;
    }

    /**
     * Calculates the euclidean distance to another point.
     * @param other The other point.
     * @return The euclidean distance to another point.
     * @throws NullPointerException If the given point is null.
     * @throws IllegalArgumentException If the dimensions of the points does not match.
     */
    public double euclideanDistance(Point other){
        if(other == null){
            throw new NullPointerException();
        }

        if(other.components.length != this.components.length){
            throw new IllegalArgumentException();
        }

        double distance = 0;
        for(int i = 0; i < components.length; i++){
            distance += pow(pow(this.components[i] - other.components[i],2),2);
        }

        distance = Math.sqrt(distance);

        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Arrays.equals(components, point.components);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(components);
    }

    @Override
    public String toString() {
        return "(" +
                Arrays.stream(components)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(",")) +
                ")";
    }

    public static Point of(double... components){
        return new Point(components);
    }
}
