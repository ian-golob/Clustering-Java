package hr.fer.clustering.methods;

import hr.fer.clustering.model.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a clustering method that clusters objects of type T.
 * @param <T> The type of object to cluster.
 * @author Ian Golob
 */
public interface ClusteringMethod<T> {

    /**
     * Clusters the given list of points.
     * @param points The list of points to cluster.
     * @return A list of clusterIDs corresponding to the given points, in order of the given points list.
     * @throws IllegalArgumentException If the given points have different dimensions.
     * @throws NullPointerException If points, any of the points in the list are null.
     */
    List<Integer> predict(List<Point> points);

    /**
     * Clusters the list of objects of type T
     * by converting the objects to points in an n-dimensional space.
     * @param objects The list of objects to cluster.
     * @param objectToPointMapper The object to point mapper.
     * @return The map of object-clusterID pairs (the clustering prediction).
     * @throws IllegalArgumentException If for the given objects, the mapper returns arrays of different dimensions.
     * @throws NullPointerException If objects, any of the objects in the list or objectToPointMapper are null.
     */
    default Map<T, Integer> predict(List<T> objects, Function<T, Point> objectToPointMapper){
        if(objects == null || objectToPointMapper == null){
            throw new NullPointerException();
        }

        List<Point> points = new ArrayList<>(objects.size());
        boolean first = true;
        int dimension = -1;

        for(T object: objects){
            Point point = objectToPointMapper.apply(object);

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

            points.add(point);
        }

        List<Integer> clustering = predict(points);

        Map<T, Integer> result = new HashMap<>();
        for(int i = 0; i < objects.size(); i++){
            result.put(objects.get(i), clustering.get(i));
        }

        return result;
    }

}
