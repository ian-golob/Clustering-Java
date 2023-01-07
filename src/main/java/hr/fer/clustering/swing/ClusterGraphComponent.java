package hr.fer.clustering.swing;

import hr.fer.clustering.model.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClusterGraphComponent extends JComponent {

    private final ClusterGraphModel model;

    public static final int POINT_SIZE = 3;

    public ClusterGraphComponent(ClusterGraphModel model, boolean clickable) {
        this.model = model;

        if(clickable){
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    double x = 1.0 * e.getPoint().x / getWidth() *
                            (model.getMaxX() - model.getMinX()) + model.getMinX();
                    double y = 1.0 * e.getPoint().y / getHeight() *
                            (model.getMaxY() - model.getMinY()) + model.getMinY();

                    model.getPoints().add(Point.of(x, y));
                    model.getColoring().add(0);

                    repaint();
                }
            });
        }

        setPreferredSize(new Dimension(500, 500));
    }

    @Override
    protected void paintComponent(Graphics g) {

        Dimension dimension = getSize();
        Graphics2D g2d = (Graphics2D) g;

        Color defaultColor = g.getColor();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, dimension.width, dimension.height);
        g2d.setColor(defaultColor);

        if(model.getPoints().size() > 0){
            float colorPerPoint = (float) (1.0 / (model.getColoring().stream().mapToInt(n -> n).max().getAsInt()));
            for(int i = 0; i < model.getPoints().size(); i++){
                Point p = model.getPoints().get(i);

                int x = (int) Math.round((p.getComponent(0) + model.getMinX()) /
                        (model.getMaxX() - model.getMinX())
                        * dimension.width);
                int y = (int) Math.round((p.getComponent(1) + model.getMinY()) /
                        (model.getMaxY() - model.getMinY())
                        * dimension.height);

                if(model.getColoring().get(i) == 0){
                    g2d.setColor(Color.GRAY);
                } else {
                    float hue = (model.getColoring().get(i)-1) * colorPerPoint;
                    g2d.setColor(Color.getHSBColor(hue,1.0F, 0.85F));
                }

                g2d.fillOval(
                        x - POINT_SIZE,
                        y - POINT_SIZE,
                        POINT_SIZE * 2 + 1,
                        POINT_SIZE * 2 + 1
                );
            }
            g2d.setColor(defaultColor);
        }

        String topLeftString = "(" + model.getMinX() + ","+ model.getMaxY() + ")";
        g2d.drawString(
                topLeftString,
                5,
                getHeight() - 5 - g2d.getFontMetrics().getDescent()
        );

        String topRightString = "(" + model.getMaxX() + ","+ model.getMinY() + ")";
        g2d.drawString(
                topRightString,
                getWidth() - g2d.getFontMetrics().stringWidth(topRightString) - 5,
                g2d.getFontMetrics().getHeight()
        );
    }


}
