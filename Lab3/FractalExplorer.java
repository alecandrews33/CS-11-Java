import java.awt.event.*;
import java.util.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.geom.Rectangle2D;

/**
 * This class displays a GUI for users to explore fractals. It currently
 * only supports the Mandelbrot fractal. It zooms in on a click on the fractal,
 * and has a reset button for initial image size.
 */
public class FractalExplorer {
    /** The size of the display */
    private int displaySize;
    /** The actual image display that is shown */
    private JImageDisplay display;
    /** Generates the fractal (Currently just Mandelbrot) */
    private FractalGenerator generator;
    /** This determines the range of values to be used for generator */
    private Rectangle2D.Double range;

    // Constructor for Fractal Explorer
    public FractalExplorer(int displaySize) {
        this.displaySize = displaySize;
        generator = new Mandelbrot();
        range = new Rectangle2D.Double();
        display = new JImageDisplay(displaySize, displaySize);
        generator.getInitialRange(range);
    }

    // Create a frame for the fractal display, add event handlers, and show it!
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal Display");
        frame.setLayout(new BorderLayout());

        ClickHandler clickHandler = new ClickHandler();
        display.addMouseListener(clickHandler);
        frame.add(display, BorderLayout.CENTER);

        ActionHandler actionHandler = new ActionHandler();
        JButton button = new JButton("Reset");
        button.addActionListener(actionHandler);

        frame.add(button, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    // Draw each pixel in the fractal.
    private void drawFractal() {
        for (int x = 0; x < displaySize; x++) {
            for (int y = 0; y < displaySize; y++) {
                double xCoord = generator.getCoord(range.x,
                    range.x + range.width, displaySize, x);
                double yCoord = generator.getCoord(range.y,
                    range.y + range.height, displaySize, y);
                int iters = generator.numIterations(xCoord, yCoord);
                if (iters == -1) {
                    display.drawPixel(x, y, 0);
                }
                else {
                    float hue = 0.7f + (float) iters / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    display.drawPixel(x, y, rgbColor);
                }
            }
        }
        display.repaint();
    }

    // Set up Action Handler
    public class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            generator.getInitialRange(range);
            drawFractal();
        }
    }

    // Set up Click Handler
    public class ClickHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            double xCoord = generator.getCoord(range.x,
                range.x + range.width, displaySize, e.getPoint().x);
            double yCoord = generator.getCoord(range.y,
                range.y + range.height, displaySize, e.getPoint().y);
            generator.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }

    // Main function creates and shows GUI and draws fractal.
    public static void main(String[] args) {
        FractalExplorer explorer = new FractalExplorer(800);
        explorer.createAndShowGUI();
        explorer.drawFractal();
    }


}
