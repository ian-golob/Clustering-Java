package hr.fer.clustering.demo;

import hr.fer.clustering.methods.ClusteringMethod;
import hr.fer.clustering.methods.Hierarchical;
import hr.fer.clustering.model.Point;
import hr.fer.clustering.swing.ClusterGraphComponent;
import hr.fer.clustering.swing.ClusterGraphModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClusteringDemo extends JFrame {

    public ClusteringDemo(){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 500);

        initGui();
    }

    private void initGui() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        /*
        List<Point> points = new ArrayList<>(List.of(
                Point.of(1, 1),
                Point.of(1, 2),
                Point.of(2, 2),
                Point.of(2, 1)
        ));

        List<Integer> defaultColoring = new ArrayList<>(List.of(
                0,
                1,
                2,
                3
        ));
         */

        List<Point> points = new ArrayList<>();
        List<Integer> defaultColoring = new ArrayList<>();

        ClusterGraphModel model = new ClusterGraphModel(0, 10, 0, 10, points, defaultColoring);
        ClusterGraphComponent graph = new ClusterGraphComponent(model, true);
        cp.add(graph, BorderLayout.CENTER);

        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BorderLayout());
        cp.add(optionPanel, BorderLayout.LINE_END);


        Hierarchical.StoppingRule stoppingRule = new Hierarchical.ClusterNumberStoppingRule(3);
        Hierarchical.MergeRule mergeRule = new Hierarchical.MinimumClusterDistanceMergeRule();
        ClusteringMethod<Point> method = new Hierarchical<>(stoppingRule, mergeRule);

        JButton predictButton = new JButton("Predict");
        optionPanel.add(predictButton, BorderLayout.PAGE_END);
        predictButton.addActionListener(e -> {
            List<Integer> newColoring = method.predict(model.getPoints());

            model.setColoring(newColoring);
            graph.repaint();
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ClusteringDemo().setVisible(true);
        });
    }
}
