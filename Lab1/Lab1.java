import java.io.*;


public class Lab1 {

  /**
     * Obtains one double-precision floating point number from
     * standard input.
     *
     * @return return the inputted double, or 0 on error.
     */
  public static double getDouble() {
      System.out.println("Enter a floating point value:");
      // There's potential for the input operation to "fail"; hard with a
      // keyboard, though!
      try {
          // Set up a reader tied to standard input.
          BufferedReader br =
              new BufferedReader(new InputStreamReader(System.in));

          // Read in a whole line of text.
          String s = br.readLine();

          // Conversion is more likely to fail, of course, if there's a typo.
          try {
              double d = Double.parseDouble(s);

              //Return the inputted double.
              return d;
          }
          catch (NumberFormatException e) {
              // Bail with a 0.  (Simple solution for now.)
              return 0.0;
          }
      }
      catch (IOException e) {
          // This should never happen with the keyboard, but we'll
          // conform to the other exception case and return 0 here,
          // too.
          return 0.0;
      }
  }

  /**
     * Computes the area of the triangle in 3d space created by 3 3d points
     *
     * @param p1 First 3d point
     * @param p2 Second 3d point
     * @param p3 Third 3d point
     * @return return the inputted double, or 0 on error.
     */
  public static double computeArea(Point3d p1, Point3d p2, Point3d p3) {
      float a = p1.distanceTo(p2);
      float b = p2.distanceTo(p3);
      float c = p3.distanceTo(p1);
      float s = (a + b + c) / 2;

      double A = (double) Math.sqrt(s * (s - a) * (s - b) * (s - c));
      return A;
  }

  // This function runs the main program. It prompts the user for 3 3D points
  // and then calculates and outputs the area of the resuulting triangle.
  public static void main(String[] args) {
      Point3d[] points = new Point3d[3];
      for (int i = 0; i < 3; i++) {
          System.out.println("First, enter the x coordinate of 3d point " +
              String.format("#%d", i + 1));
          double x = getDouble();
          System.out.println("Next, enter the y coordinate of 3d point " +
              String.format("#%d", i + 1));
          double y = getDouble();
          System.out.println("Lastly, enter the z coordinate of 3d point " +
              String.format("#%d", i + 1));
          double z = getDouble();
          Point3d point = new Point3d(x, y, z);
          points[i] = point;
      }
      if (points[0].equals(points[1]) || points[1].equals(points[2]) ||
      points[2].equals(points[0])) {
          System.out.println("You must input 3 distinct points!");
      }
      else {
          System.out.println(computeArea(points[0], points[1], points[2]));
      }
  }
}
