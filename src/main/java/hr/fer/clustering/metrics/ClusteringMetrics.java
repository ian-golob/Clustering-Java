package hr.fer.clustering.metrics;

public class ClusteringMetrics {

    public static final ClusteringMetric RAND_INDEX = (predictedLabels, trueLabels) -> {
        ClusteringMetric.requireValidLabels(predictedLabels, trueLabels);

        int sameSets = 0;
        int differentSets = 0;
        int n = predictedLabels.size();

        for(int i = 0; i < n; i++){
            for(int j = i+1; j < n; j++){

                if(predictedLabels.get(i) == predictedLabels.get(j) &&
                    trueLabels.get(i) == trueLabels.get(j)){

                    sameSets++;

                } else if(predictedLabels.get(i) != predictedLabels.get(j) &&
                        trueLabels.get(i) != trueLabels.get(j)){

                    differentSets++;

                }
            }
        }

        int numberOfPairs = n * (n-1) / 2;

        return 1.0 * (sameSets + differentSets) / numberOfPairs;
    };

    public static final ClusteringMetric ADJUSTED_RAND_INDEX = (predictedLabels, trueLabels) -> {
        ClusteringMetric.requireValidLabels(predictedLabels, trueLabels);

        int n = predictedLabels.size();
        int[][] contingencyTable = new int[n][n];

        for(int i = 0; i < n; i++){
            contingencyTable[predictedLabels.get(i)][trueLabels.get(i)]++;
        }

        int index = 0;

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                index += contingencyTable[i][j] * (contingencyTable[i][j] - 1) / 2;
            }
        }

        int aBinomSum = 0;
        for(int i = 0; i < n; i++){
            int ai = 0;
            for(int j = 0; j < n; j++){
                ai += contingencyTable[i][j];
            }

            aBinomSum += ai * (ai - 1) / 2;
        }

        int bBinomSum = 0;
        for(int j = 0; j < n; j++){
            int bi = 0;
            for(int i = 0; i < n; i++){
                bi += contingencyTable[i][j];
            }

            bBinomSum += bi * (bi - 1) / 2;
        }

        double expectedIndex = 1.0 * aBinomSum * bBinomSum / (n * (n - 1) / 2);
        double maxIndex = 0.5 * (aBinomSum + bBinomSum);

        return (index - expectedIndex) / (maxIndex - expectedIndex);
    };

}
