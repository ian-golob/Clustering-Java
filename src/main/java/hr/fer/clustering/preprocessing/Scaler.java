package hr.fer.clustering.preprocessing;

import hr.fer.clustering.model.Point;

import java.util.ArrayList;
import java.util.List;

public class Scaler {

    public static List<Point> transform(List<Point> points){
        int n = points.size();
        int dim = points.stream().findAny().get().dimension();

        double[] mean = new double[n];
        points.forEach(point -> {
            for(int i = 0; i < dim; i++){
                mean[i] += point.getComponent(i);
            }
        });
        for(int i = 0; i < dim; i++){
            mean[i] = mean[i] / n;
        }

        double[] stdDev = new double[n];
        points.forEach(point -> {
            for(int i = 0; i < dim; i++){
                stdDev[i] += Math.pow(point.getComponent(i) - mean[i], 2);
            }
        });
        for(int i = 0; i < dim; i++){
            stdDev[i] = Math.sqrt(1.0 / n * stdDev[i]);
        }

        List<Point> scaledPoints = new ArrayList<>(points.size());
        for (Point point : points) {
            double[] scaledDimensions = new double[dim];

            for (int j = 0; j < dim; j++) {
                scaledDimensions[j] = (point.getComponent(j) - mean[j]) / stdDev[j];
            }

            scaledPoints.add(new Point(scaledDimensions));
        }

        return scaledPoints;
    }

}
