package hr.fer.clustering.demo;

import hr.fer.clustering.methods.ClusteringMethod;
import hr.fer.clustering.methods.Hierarchical;
import hr.fer.clustering.methods.KMeans;
import hr.fer.clustering.metrics.ClusteringMetric;
import hr.fer.clustering.metrics.ClusteringMetrics;
import hr.fer.clustering.model.Point;
import hr.fer.clustering.preprocessing.Scaler;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DryBeanDatasetDemo {

    public enum BeanType {
        SEKER, BARBUNYA, BOMBAY, CALI, DERMASON, HOROZ, SIRA
    }

    public record Bean(double[] properties, BeanType beanType) {
    }

    public static void main(String[] args) throws Exception {

        // Read all the instances in the file (ARFF, CSV, XRFF, ...)
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("./Dry_Bean_Dataset/Dry_Bean_Dataset.arff");
        Instances instances = source.getDataSet();

        // Make the last attribute be the class
        instances.setClassIndex(instances.numAttributes() - 1);

        List<Bean> beans = new ArrayList<>(instances.size());
        instances.forEach(instance -> {

            double[] propertyArray = Arrays.copyOfRange(
                    instance.toDoubleArray(),
                    0,
                    instance.numAttributes() - 1
            );

            beans.add(new Bean(
                    propertyArray,
                    BeanType.valueOf(instance.stringValue(16).toUpperCase())
            ));
        });

        List<Point> points = beans.stream().map(bean -> new Point(bean.properties)).toList();
        points = Scaler.transform(points);

        List<Integer> trueClustering = beans.stream().map(bean -> bean.beanType.ordinal()).toList();


        int classNumber = BeanType.values().length;

        ClusteringMetric ri = ClusteringMetrics.RAND_INDEX;
        ClusteringMetric ari = ClusteringMetrics.ADJUSTED_RAND_INDEX;

        ClusteringMethod<Bean> kmeans = new KMeans<>(classNumber);
        List<Integer> kmeansPrediction = kmeans.predict(points);

        System.out.println("KMeans");
        System.out.println("RI: " + ri.evaluate(kmeansPrediction, trueClustering));
        System.out.println("ARI: " + ari.evaluate(kmeansPrediction, trueClustering));
        System.out.println();

        /*
        // Too slow for bean data

        Hierarchical.StoppingRule stoppingRule = new Hierarchical.ClusterNumberStoppingRule(classNumber);
        Hierarchical.MergeRule mergeRule  = new Hierarchical.ClosestCentroidsMergeRule();
        ClusteringMethod<Bean> hierarchical = new Hierarchical<>(stoppingRule, mergeRule);
        List<Integer> hierarchicalPrediction = hierarchical.predict(beans, bean -> new Point(bean.properties));

        System.out.println("Hierarchical");
        System.out.println("RI: " + ri.evaluate(hierarchicalPrediction, trueClustering));
        System.out.println("ARI: " + ari.evaluate(hierarchicalPrediction, trueClustering));
         */
    }
}
