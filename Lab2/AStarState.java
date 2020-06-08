import java.util.*;


/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/
public class AStarState
{
    /** This is a reference to the map that the A* algorithm is navigating. **/
    private Map2D map;

    /** Collection to store open waypoints **/
    HashMap<Location, Waypoint> openWaypoints = new HashMap<>();

    /** Collection to store closed waypoints **/
    HashMap<Location, Waypoint> closedWaypoints = new HashMap<>();


    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;
    }

    /** Returns the map that the A* pathfinder is navigating. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     * This method scans through all open waypoints, and returns the waypoint
     * with the minimum total cost.  If there are no open waypoints, this method
     * returns <code>null</code>.
     **/
    public Waypoint getMinOpenWaypoint()
    {
        // Iterate through waypoints and find the minimum cost waypoint
        Waypoint minWaypoint = null;
        float minCost = -1;
        for (HashMap.Entry<Location, Waypoint> current :
            openWaypoints.entrySet()) {
            float cost = current.getValue().getPreviousCost();
            if (minCost == -1 || cost < minCost) {
                minCost = cost;
                minWaypoint = current.getValue();
            }
        }

        return minWaypoint;

    }

    /**
     * This method adds a waypoint to (or potentially updates a waypoint already
     * in) the "open waypoints" collection.  If there is not already an open
     * waypoint at the new waypoint's location then the new waypoint is simply
     * added to the collection.  However, if there is already a waypoint at the
     * new waypoint's location, the new waypoint replaces the old one <em>only
     * if</em> the new waypoint's "previous cost" value is less than the current
     * waypoint's "previous cost" value.
     **/
    public boolean addOpenWaypoint(Waypoint newWP)
    {
        // Get location of new Waypoint
        Location location = newWP.loc;

        // If this location is already in openWaypoints, we must see if
        // this offers a better path to get there.
        if (openWaypoints.containsKey(location)) {
            if (openWaypoints.get(location).getPreviousCost() >
                newWP.getPreviousCost()) {
                openWaypoints.put(location, newWP);
                return true;
            }
            else {
                return false;
            }
        }
        // Otherwise, can just add this waypoint.
        else {
            openWaypoints.put(location, newWP);
            return true;
        }

    }


    /** Returns the current number of open waypoints. **/
    public int numOpenWaypoints()
    {
        return openWaypoints.size();
    }


    /**
     * This method moves the waypoint at the specified location from the
     * open list to the closed list.
     **/
    public void closeWaypoint(Location loc)
    {
        Waypoint removedWaypoint = openWaypoints.remove(loc);
        closedWaypoints.put(loc, removedWaypoint);
    }

    /**
     * Returns true if the collection of closed waypoints contains a waypoint
     * for the specified location.
     **/
    public boolean isLocationClosed(Location loc)
    {
        if (closedWaypoints.containsKey(loc)) {
            return true;
        }
        else {
            return false;
        }
    }
}
