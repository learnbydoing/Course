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
     * @param loc a Point3D represents the location
     * @throws InvalidDataException if the specified Point3D is null.
     * @see #LocatableImpl(double, double, double)
     */
    public LocatableImpl(Point3D loc) throws InvalidDataException {
        setLocation(loc);
    }

    /**
     * Constructs and initializes a LocatableImpl from the specified xyz coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @throws InvalidDataException if the specified xyz coordinates are not valid(negative value).
     * @see #LocatableImpl(Point3D)
     */
    public LocatableImpl(double x, double y, double z) throws InvalidDataException {
        this(new Point3D(x, y, z));
    }

    /**
     * Get the location of this LocatableImpl.
     * @return the Point3D containing the  x y z data
     */
    @Override
    public Point3D getLocation() {
        return new Point3D(location);
    }

    /**
     * Get the x coordinate of current location.
     * @return the x coordinate of location
     */
    @Override
    public double getLocationX() {
        return location.getX();
    }

    /**
     * Get the y coordinate of current location.
     * @return the y coordinate of location
     */
    @Override
    public double getLocationY() {
        return location.getY();
    }

    /**
     * Get the z coordinate of current location.
     * @return the z coordinate of location
     */
    @Override
    public double getLocationZ() {
        return location.getZ();
    }

    /**
     * Set the location with the specified Point3D. 
     * @param loc the Point3D for the new location
     * @throws InvalidDataException if the input point is null.
     * @see #setLocation(double, double, double)
     */
    @Override
    public final void setLocation(Point3D loc) throws InvalidDataException {
        if (loc == null) {
            throw new InvalidDataException("Null location sent to setLocation");
        }

        setLocation(loc.getX(), loc.getY(), loc.getZ());
    }

    /**
     * Set the location with the specified xyz coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @throws InvalidDataException if the specified xyz coordinates are not valid(negative value).
     * @see #setLocation(Point3D)
     */
    @Override
    public void setLocation(double x, double y, double z) throws InvalidDataException {
        if (x < 0.0 || y < 0.0 || z < 0.0) {
            throw new InvalidDataException("Invalid X,Y,Z point sent to setLocation(x,y,z)");
        }
        location.setCoordinates(x, y, z);
    }

    /**
     * Computes the distance between this location and another point.
     * @param loc the Point3D for the target location
     * @return the distance between this location and another point.
     * @throws InvalidDataException if the input point is null.
     * @see #distance(double, double, double)
     */
    @Override
    public double distance(Point3D loc) throws InvalidDataException {
        if (loc == null) {
            throw new InvalidDataException("Null location sent to distance");
        }
        return location.distance(loc);
    }

    /**
     * Computes the distance between this location and another location with specified xyz coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the distance between this location and another point.
     * @throws InvalidDataException if the specified xyz coordinates are not valid(negative value).
     * @see #distance(Point3D)
     */
    @Override
    public double distance(double x, double y, double z) throws InvalidDataException {
        if (x < 0.0 || y < 0.0 || z < 0.0) {
            throw new InvalidDataException("Invalid X,Y,Z point sent to distance(x,y,z)");
        }
        return location.distance(x, y, z);
    }
}
