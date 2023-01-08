package hr.fer.clustering.metrics;

import java.util.List;

/**
 * Represents a clustering evaluation metric.
 * @author Ian Golob
 */
public interface ClusteringMetric {

    /**
     * Evaluates the predicted clustering based on the ground truth clustering.
     * @param predictedLabels The point cluster labels returned from the clustering method.
     * @param trueLabels The ground truth point cluster labels.
     * @return A score of the predicted clustering.
     * @throws NullPointerException If the predicted or true labels are null, or if any of the values in the lists are null.
     * @throws IllegalArgumentException If the given list lengths do not match.
     */
    double evaluate(List<Integer> predictedLabels, List<Integer> trueLabels);

    /**
     * @param predictedLabels The point cluster labels returned from the clustering method.
     * @param trueLabels The ground truth point cluster labels.
     * @throws NullPointerException If the predicted or true labels are null, or if any of the values in the lists are null.
     * @throws IllegalArgumentException If the given list lengths do not match.
     */
    static void requireValidLabels(List<Integer> predictedLabels, List<Integer> trueLabels){
        if(predictedLabels == null || trueLabels == null){
            throw new NullPointerException();
        }

        if(predictedLabels.size() != trueLabels.size()){
            throw new IllegalArgumentException();
        }

        for(int i = 0; i < predictedLabels.size(); i++){
            if(predictedLabels.get(i) == null || trueLabels.get(i) == null){
                throw new NullPointerException();
            }
        }
    }

}
