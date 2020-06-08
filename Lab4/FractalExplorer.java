import java.awt.event.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class is what allows the user to view different fractals. It provides
 * a selection box at the top to pick which fractal to view and supports resets
 * and zooming in through clicking.
 */
public class FractalExplorer {
    /** The size of the display */
    private int displaySize;
    /** The object that will store the image to be displayed */
    private JImageDisplay display;
    /** The Fractal Generator that will produce the selected fractal */
    private FractalGenerator generator;
    /** The range which is specified by selected fractal */
    private Rectangle2D.Double range;
    /** The combination box that lets users select their desired fractal */
    private JComboBox comboBox;
    /** The frame that items like buttons will be added to */
    private JFrame frame;

    /** Constructor for Fractal Explorer */
    public FractalExplorer(int displaySize) {
        this.displaySize = displaySize;
        generator = new Mandelbrot();
        range = new Rectangle2D.Double();
        display = new JImageDisplay(displaySize, displaySize);
        generator.getInitialRange(range);
        comboBox = new JComboBox();
        frame = new JFrame("Fractal Display");
    }

    /** Grab the Image that is going to be displayed. */
    public BufferedImage GetDisplayImage() {
        return display.GetImage();
    }

    /**
     * Create a frame for the fractal display, add event handlers, and show it!
     */
    public void createAndShowGUI() {
        frame.setLayout(new BorderLayout());

        ClickHandler clickHandler = new ClickHandler();
        display.addMouseListener(clickHandler);
        frame.add(display, BorderLayout.CENTER);

        ActionHandler actionHandler = new ActionHandler();

        /** Set up top panel and position at top */
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Fractal: ");
        comboBox.addItem(new Mandelbrot());
        comboBox.addItem(new Tricorn());
        comboBox.addItem(new BurningShip());
        comboBox.addActionListener(actionHandler);
        panel.add(label);
        panel.add(comboBox);
        frame.add(panel, BorderLayout.NORTH);

        /** Set up bottom panel with buttons and position at bottom */
        JPanel panel2 = new JPanel();
        JButton resetButton = new JButton("Reset");
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(actionHandler);
        JButton saveButton = new JButton("Save Image");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(actionHandler);
        panel2.add(saveButton);
        panel2.add(resetButton);

        frame.add(panel2, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /** Draw each pixel in the fractal. */
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

    /** Set up Action Handler for combo box and buttons */
    public class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == comboBox) {
                generator = (FractalGenerator)comboBox.getSelectedItem();
                generator.getInitialRange(range);
                drawFractal();
            }
            else if (e.getActionCommand() == "save") {
                JFileChooser chooser = new JFileChooser();
                int result = chooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    FileNameExtensionFilter filter =
                        new FileNameExtensionFilter("PNG Images", "png");
                    chooser.setFileFilter(filter);
                    chooser.setAcceptAllFileFilterUsed(false);
                    try {
                        ImageIO.write(GetDisplayImage(), "png",
                            chooser.getSelectedFile());
                    }
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(frame,
                            exception.getMessage(), "Cannot Save Image",
                            JOptionPane.ERROR_MESSAGE);
                    }

                }
                else {
                    return;
                }
            }
            else if (e.getActionCommand() == "reset") {
                generator.getInitialRange(range);
                drawFractal();
            }

        }
    }

    /** Set up Click Handler (zooms in on click) */
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

    /** Main function creates and shows GUI and draws fractal. */
    public static void main(String[] args) {
        FractalExplorer explorer = new FractalExplorer(800);
        explorer.createAndShowGUI();
        explorer.drawFractal();
    }


}
