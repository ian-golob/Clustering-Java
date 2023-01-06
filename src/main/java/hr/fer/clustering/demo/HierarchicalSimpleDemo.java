package hr.fer.clustering.demo;

import hr.fer.clustering.methods.ClusteringMethod;
import hr.fer.clustering.methods.Hierarchical;
import hr.fer.clustering.model.Point;

import java.util.List;

public class HierarchicalSimpleDemo {

    public static void main(String[] args) {

        List<Point> points = List.of(
                Point.of(-100, -100),
                Point.of(-100, -90),
                Point.of(-80, -60),
                Point.of(300, 300),
                Point.of(0, 0),
                Point.of(10, 10),
                Point.of(-10, 10)
        );

        Hierarchical.StoppingRule stoppingRule = new Hierarchical.ClusterNumberStoppingRule(3);
        Hierarchical.MergeRule mergeRule = new Hierarchical.MinimumClusterDistanceMergeRule();
        ClusteringMethod<Point> hierarchical = new Hierarchical<>(stoppingRule, mergeRule);

        List<Integer> clustering = hierarchical.predict(points);

        for(int i = 0; i < clustering.size(); i++){
            System.out.println(points.get(i).toString() + " -> " + clustering.get(i));
        }
    }
}
