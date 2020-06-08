import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * This Fractal Generator extension produces the burning ship fractal
 */
public class BurningShip extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;
    /** Get initial range, these values are fixed. */
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }

    @Override
    public String toString() {
        return "Burning Ship";
    }

    /**
     * Return the max number of iterations required using Burning Ship function.
     */
    public int numIterations(double x, double y) {
        double re = 0;
        double im = 0;
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double abs_re = Math.abs(re);
            double abs_im = Math.abs(im);
            double squared_re = abs_re * abs_re - abs_im * abs_im;
            double squared_im = 2 * abs_re * abs_im;

            re = squared_re + x;
            im = squared_im + y;

            if (re * re + im * im > 4) {
                return i;
            }

        }
        return -1;
    }
}
