import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends javax.swing.JComponent {
    private BufferedImage image;

    // Constructor for JImageDisplay
    public JImageDisplay(int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_RGB);
        this.image = newImage;
        Dimension dim = new Dimension(width, height);
        super.setPreferredSize(dim);
    }

    public BufferedImage GetImage() {
        return image;
    }

    // Draw the graphics image. (Calls parent function).
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    // Set all points to the 0 value to "clear" image.
    public void clearImage() {
        for (int i = 0; i < this.image.getWidth(); i++) {
            for (int j = 0; j < this.image.getHeight(); j++) {
                this.image.setRGB(i, j, 0);
            }
        }
    }

    // Set the color of the pixel to the passed in value.
    public void drawPixel(int x, int y, int rgbColor) {
        this.image.setRGB(x, y, rgbColor);
    }
}
