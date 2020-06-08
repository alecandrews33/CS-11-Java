import java.awt.event.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FractalExplorer {
    /** The size of the display */
    private int displaySize;
    /** The object that stores the image to be displayed */
    private JImageDisplay display;
    /** The generator that produces the fractal. Is selected in combobox. */
    private FractalGenerator generator;
    /** The range for the fractal, determined by selected fractal */
    private Rectangle2D.Double range;
    /** The combo box that allows user to select fractal */
    private JComboBox comboBox;
    /** The frame to attach buttons to and display image */
    private JFrame frame;
    /** The button to reset the image */
    private JButton resetButton;
    /** The button to save the image */
    private JButton saveButton;
    /** The rows remaining to be drawn */
    private int rowsRemaining;

    // Constructor for Fractal Explorer
    public FractalExplorer(int displaySize) {
        this.displaySize = displaySize;
        generator = new Mandelbrot();
        range = new Rectangle2D.Double();
        display = new JImageDisplay(displaySize, displaySize);
        generator.getInitialRange(range);
        comboBox = new JComboBox();
        frame = new JFrame("Fractal Display");
        saveButton = new JButton("Save Image");
        resetButton = new JButton("Reset");
    }

    public BufferedImage GetDisplayImage() {
        return display.GetImage();
    }

    // Create a frame for the fractal display, add event handlers, and show it!
    public void createAndShowGUI() {
        frame.setLayout(new BorderLayout());

        ClickHandler clickHandler = new ClickHandler();
        display.addMouseListener(clickHandler);
        frame.add(display, BorderLayout.CENTER);

        ActionHandler actionHandler = new ActionHandler();

        // Add first fractal with fractal selection box
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Fractal: ");
        comboBox.addItem(new Mandelbrot());
        comboBox.addItem(new Tricorn());
        comboBox.addItem(new BurningShip());
        comboBox.addActionListener(actionHandler);
        panel.add(label);
        panel.add(comboBox);
        frame.add(panel, BorderLayout.NORTH);

        // Add second panel with save and reset buttons
        JPanel panel2 = new JPanel();

        resetButton.setActionCommand("reset");
        resetButton.addActionListener(actionHandler);

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

    // Draw each pixel in the fractal.
    private void drawFractal() {
        enableUI(false);
        rowsRemaining = displaySize;
        for (int y = 0; y < displaySize; y++) {
            FractalWorker worker = new FractalWorker(y);
            worker.execute();
        }
    }

    // Set up Action Handler
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

    // Set up Click Handler
    public class ClickHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (rowsRemaining != 0) {
                return;
            }
            double xCoord = generator.getCoord(range.x,
                range.x + range.width, displaySize, e.getPoint().x);
            double yCoord = generator.getCoord(range.y,
                range.y + range.height, displaySize, e.getPoint().y);
            generator.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }

    void enableUI(boolean val) {
        comboBox.setEnabled(val);
        saveButton.setEnabled(val);
        resetButton.setEnabled(val);
    }

    private class FractalWorker extends SwingWorker<Object, Object> {
        private int y;
        private int[] row;
        public FractalWorker(int y) {
            this.y = y;
        }

        @Override
        public Object doInBackground() {
            this.row = new int[displaySize];

            // Calculate number of iterations and draw pixel.
            for (int x = 0; x < displaySize; x++) {
                double xCoord = generator.getCoord(range.x,
                    range.x + range.width, displaySize, x);
                double yCoord = generator.getCoord(range.y,
                    range.y + range.height, displaySize, this.y);
                int iters = generator.numIterations(xCoord, yCoord);
                if (iters == -1) {
                    row[x] = 0;
                }
                else {
                    float hue = 0.7f + (float) iters / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    row[x] = rgbColor;
                }
            }
            return null;
        }

        @Override
        public void done() {
            for (int x = 0; x < displaySize; x++) {
                display.drawPixel(x, y, this.row[x]);
            }
            display.repaint(0, 0, y, displaySize, 1);
            rowsRemaining -= 1;
            if (rowsRemaining == 0) {
                enableUI(true);
            }
        }

    }

    // Main function creates and shows GUI and draws fractal.
    public static void main(String[] args) {
        FractalExplorer explorer = new FractalExplorer(800);
        explorer.createAndShowGUI();
        explorer.drawFractal();
    }


}
