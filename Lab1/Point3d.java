import java.lang.Math;

/**
 * A three-dimensional point class.
 */

public class Point3d {

  /* X coordinate of the point */
  private double xCoord;

  /* Y coordinate of the point */
  private double yCoord;

  /* Z coordinate of the point */
  private double zCoord;

  /* Constructor to initialize point to (x,y,z) */
  public Point3d(double x, double y, double z) {
      xCoord = x;
      yCoord = y;
      zCoord = z;
  }

  /* Constructor initialize point at origin */
  public Point3d() {
      // Call 3 argument constructor and specify the origin.
      this(0, 0, 0);
  }

  /* Return the X coordinate of the point. */
  public double getX() {
      return xCoord;
  }

  /* Return the Y coordinate of the point. */
  public double getY() {
      return yCoord;
  }

  /* Return the Z coordinate of the point. */
  public double getZ() {
      return zCoord;
  }

  /* Set the X coordinate of the point. */
  public void setX(double val) {
      xCoord = val;
  }

  /* Set the Y coordinate of the point. */
  public void setY(double val) {
      yCoord = val;
  }

  /* Set the Z coordinate of the point. */
  public void setZ(double val) {
      zCoord = val;
  }

  /* Check equality between two Point3ds */
  @Override
  public boolean equals(Object obj) {
      // Check if obj is a Point3d
      if (obj instanceof Point3d) {
          // Cast other object to Point3d type, then compare
          Point3d other = (Point3d) obj;
          if (xCoord == other.getX() && yCoord == other.getY() &&
          zCoord == other.getZ()) {
              return true;
          }
      }
      // If we get here, then they're not equal.
      return false;
  }

  /* Computes distance from this point to passed in point.*/
  public float distanceTo(Point3d p) {
      double dist = 0;
      dist += (xCoord - p.getX()) * (xCoord - p.getX());
      dist += (yCoord - p.getY()) * (yCoord - p.getY());
      dist += (zCoord - p.getZ()) * (zCoord - p.getZ());
      return (float) Math.sqrt(dist);
  }
}
