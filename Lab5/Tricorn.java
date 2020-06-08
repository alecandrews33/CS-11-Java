import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * This Fractal Generator extension produces the tricorn fractal
 */
public class Tricorn extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;
    // Get initial range, these values are fixed.
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    @Override
    public String toString() {
        return "Tricorn";
    }

    /** Return the max number of iterations required using Tricorn function. */
    public int numIterations(double x, double y) {
        double re = 0;
        double im = 0;
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double squared_re = re * re - im * im;
            double squared_im = -2 * re * im;

            re = squared_re + x;
            im = squared_im + y;

            if (re * re + im * im > 4) {
                return i;
            }


        }
        return -1;
    }
}
