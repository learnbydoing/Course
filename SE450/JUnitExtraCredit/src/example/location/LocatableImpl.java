package example.location;

import example.common.InvalidDataException;
import example.common.Point3D;

/**
 * Default implementation of the {@link Locatable} interface.
 * @author Rong Zhuang
 */
public class LocatableImpl implements Locatable {

    private final Point3D location = new Point3D();

    /**
     * Constructs and initializes a LocatableImpl with a specified Point3D.
     * @param loc - a Point3D represents the location
     * @throws InvalidDataException if the specified Point3D is null.
     */
    public LocatableImpl(Point3D loc) throws InvalidDataException {
        setLocation(loc);
    }

    /**
     * Constructs and initializes a LocatableImpl from the specified xyz coordinates.
     * @param x - the x coordinate
     * @param y - the y coordinate
     * @param z - the z coordinate
     * @throws InvalidDataException if the specified xyz coordinates are not valid.
     */
    public LocatableImpl(double x, double y, double z) throws InvalidDataException {
        this(new Point3D(x, y, z));
    }

    @Override
    public Point3D getLocation() {
        return new Point3D(location);
    }

    @Override
    public double getLocationX() {
        return location.getX();
    }

    @Override
    public double getLocationY() {
        return location.getY();
    }

    @Override
    public double getLocationZ() {
        return location.getZ();
    }

    @Override
    public final void setLocation(Point3D loc) throws InvalidDataException {
        if (loc == null) {
            throw new InvalidDataException("Null location sent to setLocation");
        }

        setLocation(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void setLocation(double x, double y, double z) throws InvalidDataException {
        if (x < 0.0 || y < 0.0 || z < 0.0) {
            throw new InvalidDataException("Invalid X,Y,Z point sent to setLocation(x,y,z)");
        }
        location.setCoordinates(x, y, z);
    }

    @Override
    public double distance(Point3D loc) throws InvalidDataException {
        if (loc == null) {
            throw new InvalidDataException("Null location sent to distance");
        }
        return location.distance(loc);
    }

    @Override
    public double distance(double x, double y, double z) throws InvalidDataException {
        if (x < 0.0 || y < 0.0 || z < 0.0) {
            throw new InvalidDataException("Invalid X,Y,Z point sent to distance(x,y,z)");
        }
        return location.distance(x, y, z);
    }
}
