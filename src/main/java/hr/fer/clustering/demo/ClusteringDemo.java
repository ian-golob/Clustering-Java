package hr.fer.clustering.demo;

import hr.fer.clustering.methods.ClusteringMethod;
import hr.fer.clustering.methods.Hierarchical;
import hr.fer.clustering.methods.KMeans;
import hr.fer.clustering.model.Point;
import hr.fer.clustering.swing.ClusterGraphComponent;
import hr.fer.clustering.swing.ClusterGraphModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClusteringDemo extends JFrame {

    private static final String KMEANS_PANEL = "KMeans";
    private static final String HIERARCHICAL_PANEL = "Hierarchical";



    public ClusteringDemo(){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setTitle("Clustering demo");

        initGui();
    }

    private void initGui() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        ClusterGraphModel model = new ClusterGraphModel(0, 10, 0, 10, new ArrayList<>(), new ArrayList<>());
        ClusterGraphComponent graph = new ClusterGraphComponent(model, true);
        cp.add(graph, BorderLayout.CENTER);

        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BorderLayout());
        optionPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        cp.add(optionPanel, BorderLayout.LINE_END);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        optionPanel.add(buttonPanel, BorderLayout.PAGE_END);

        JPanel methodPanelWrapper = new JPanel();
        optionPanel.add(methodPanelWrapper, BorderLayout.CENTER);
        JPanel methodPanel = new JPanel();
        methodPanel.setLayout(new GridLayout(0, 1));
        methodPanelWrapper.add(methodPanel);

        JPanel methodChooserWrapper = new JPanel();
        methodPanel.add(methodChooserWrapper);
        JComboBox<String> methodChooser = new JComboBox<>(new String[]{KMEANS_PANEL, HIERARCHICAL_PANEL});
        methodChooserWrapper.add(methodChooser);


        JPanel kmeansPanel = new JPanel(new GridLayout(0, 1));

        kmeansPanel.add(new JLabel("Cluster number:"));
        JSpinner kmeansSpinner = new JSpinner(
                new SpinnerNumberModel(
                        3,
                        2,
                        100
                        , 1)
        );
        kmeansPanel.add(kmeansSpinner);

        kmeansPanel.add(new JLabel());
        kmeansPanel.add(new JLabel());
        JPanel hierarchicalPanel = new JPanel(new GridLayout(0, 1));

        hierarchicalPanel.add(new JLabel("Cluster number:"));
        JSpinner hierarchicalSpinner = new JSpinner(
                new SpinnerNumberModel(
                        3,
                        2,
                        100
                        , 1)
        );
        hierarchicalPanel.add(hierarchicalSpinner);

        hierarchicalPanel.add(new JLabel("Merge type:"));
        JComboBox<String> hierarchicalMerge = new JComboBox<>(new String[]{"Closest centroid", "Closest point"});
        hierarchicalPanel.add(hierarchicalMerge);

        JPanel methodCardPanel = new JPanel(new CardLayout());
        methodCardPanel.add(kmeansPanel, KMEANS_PANEL);
        methodCardPanel.add(hierarchicalPanel, HIERARCHICAL_PANEL);
        methodPanel.add(methodCardPanel);

        methodChooser.addItemListener(e -> {
            CardLayout cl = (CardLayout) methodCardPanel.getLayout();
            cl.show(methodCardPanel, (String) e.getItem());
        });

        JButton predictButton = new JButton("Predict");
        predictButton.addActionListener(e -> {
            String chosenMethodString = (String) methodChooser.getSelectedItem();

            if(chosenMethodString == null){
                JOptionPane.showMessageDialog(this, "You must select a clustering method first.");
                return;
            }

            ClusteringMethod<Point> method;
            switch (chosenMethodString){
                case KMEANS_PANEL:
                    method = new KMeans<>((Integer) kmeansSpinner.getValue());
                    break;

                case HIERARCHICAL_PANEL:
                    Hierarchical.StoppingRule stoppingRule =
                            new Hierarchical.ClusterNumberStoppingRule(
                                    (Integer) hierarchicalSpinner.getValue()
                            );

                    if(hierarchicalMerge.getSelectedItem() == null){
                        JOptionPane.showMessageDialog(this, "You must select a cluster merge method first.");
                        return;
                    }

                    Hierarchical.MergeRule mergeRule;
                    switch ((String) hierarchicalMerge.getSelectedItem()){
                        case "Closest centroid":
                            mergeRule = new Hierarchical.ClosestCentroidsMergeRule();
                            break;
                        case "Closest point":
                            mergeRule = new Hierarchical.MinimumClusterDistanceMergeRule();
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }

                    method = new Hierarchical<>(stoppingRule, mergeRule);
                    break;

                default:
                    throw new IllegalArgumentException();
            }

            if(model.getPoints().size() > 0){
                List<Integer> newColoring = method.predict(model.getPoints());

                model.setColoring(newColoring);
                graph.repaint();
            } else {
                JOptionPane.showMessageDialog(this,"You must add points first (click on the canvas)");
            }

        });
        buttonPanel.add(predictButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            model.getPoints().clear();
            model.getColoring().clear();

            graph.repaint();
        });
        buttonPanel.add(clearButton);
    }

    public static void main(String[] args) {
        /*
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException |
                 ClassNotFoundException ex) {
            ex.printStackTrace();
        }
         */

        SwingUtilities.invokeLater(() -> {
            new ClusteringDemo().setVisible(true);
        });
    }
}
