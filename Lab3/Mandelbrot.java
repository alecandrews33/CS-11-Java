import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * This class generates the Mandelbrot fractal.
 */
public class Mandelbrot extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;
    
    // Get initial range, these values are fixed.
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    // Return the max number of iterations required using Mandelbrot function.
    public int numIterations(double x, double y) {
        double re = 0;
        double im = 0;
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double squared_re = re * re - im * im;
            double squared_im = 2 * re * im;

            if (Math.sqrt(squared_re + squared_im) > 4) {
                return i;
            }

            re = squared_re + x;
            im = squared_im + y;
        }
        return -1;
    }
}
