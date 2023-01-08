package hr.fer.clustering.preprocessing;

import hr.fer.clustering.model.Point;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ScalerTest {

    @Test
    public void transformTest(){

        List<Point> points = List.of(
                Point.of(4, 1),
                Point.of(9, 2),
                Point.of(11, 3),
                Point.of(12, 4),
                Point.of(17, 5),
                Point.of(5, 6),
                Point.of(8, 7),
                Point.of(12, 8),
                Point.of(14, 0)
        );

        List<Point> pointsScaled = Scaler.transform(points);
        List<Point> pointsScaledTwice = Scaler.transform(pointsScaled);

        assertEquals(-1.579, pointsScaled.get(0).getComponent(0), 0.1);

        int n = points.size();
        int dim = points.get(0).dimension();
        for(int i = 0; i < n; i++){
            for(int j = 0; j < dim; j++){
                assertEquals(
                        pointsScaled.get(i).getComponent(j),
                        pointsScaledTwice.get(i).getComponent(j),
                        0.1
                );
            }
        }
    }

}
