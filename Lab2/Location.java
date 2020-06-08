/**
 * This class represents a specific location in a 2D map.  Coordinates are
 * integer values.
 **/
public class Location
{
    /** X coordinate of this location. **/
    public int xCoord;

    /** Y coordinate of this location. **/
    public int yCoord;


    /** Creates a new location with the specified integer coordinates. **/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }

    /** Creates a new location with coordinates (0, 0). **/
    public Location()
    {
        this(0, 0);
    }

    /** Check if two locations are equal **/
    @Override
    public boolean equals(Object obj) {
        // Check if obj is a Location
        if (obj instanceof Location) {
            Location other = (Location) obj;
            if (xCoord == other.xCoord && yCoord == other.yCoord) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code = 7; // Some prime value

        // Use another prime value to combine
        code = 23 * code + (int) xCoord;
        code = 23 * code + (int) yCoord;

        return code;
    }
}
