package example.movement;

import example.common.InvalidDataException;
import example.common.LocatableImplFactory;
import example.common.Point3D;
import example.location.Locatable;

/**
 * Default implementation of the {@link Movable} interface.
 * @author Rong Zhuang
 */
public class MovableImpl implements Movable {

    private final Point3D destination = new Point3D();
    private double speed;
    private double maxSpeed;
    private Locatable myLocatable;

    /**
     * Constructs and initializes a MovableImpl with specified location and destination Point3D.
     * @param loc the location Point3D
     * @param dest the destination Point3D
     * @param spd the speed
     * @param mxSpd the maximum speed
     * @throws InvalidDataException if the input parameters are invalid to construct a new MovableImpl
     * @see #MovableImpl(double, double, double, double, double, double, double, double)
     */
    public MovableImpl(Point3D loc, Point3D dest, double spd, double mxSpd) throws InvalidDataException {
        setLocatable(LocatableImplFactory.createLocatable(loc));
        setDestination(dest);
        setMaxSpeed(mxSpd);
        setSpeed(spd);
    }

    /**
     * Constructs and initializes a MovableImpl with specified xyz coordinates of location and destination.
     * @param lX - the x coordinate of location
     * @param lY - the y coordinate of location
     * @param lZ - the z coordinate of location
     * @param dX - the x coordinate of destination
     * @param dY - the y coordinate of destination
     * @param dZ - the z coordinate of destination
     * @param spd - the speed
     * @param mxSpd - the maximum speed
     * @throws InvalidDataException if the input parameters are invalid to construct a new MovableImpl.
     * @see #MovableImpl(Point3D, Point3D, double, double)
     */
    public MovableImpl(double lX, double lY, double lZ, double dX, double dY, double dZ,
            double spd, double mxSpd) throws InvalidDataException {
        setLocatable(LocatableImplFactory.createLocatable(new Point3D(lX, lY, lZ)));
        setDestination(dX, dY, dZ);
        setMaxSpeed(mxSpd);
        setSpeed(spd);
    }

    /**
     * Set locatable instance.
     * @param li locatable instance
     * @throws InvalidDataException if the input parameter li is null.
     */
    private void setLocatable(Locatable li) throws InvalidDataException {
        if (li == null) {
            throw new InvalidDataException("Null Locatable sent to setLocatable.");
        }
        myLocatable = li;
    }

    /**
     * Get the locatable instance.
     * @return the locatable instance
     */
    private Locatable getLocatable() {
        return myLocatable;
    }

    /**
     * Get the destination of this MovableImpl.
     * @return the Point3D containing the  x y z data
     */
    @Override
    public Point3D getDestination() {
        if (destination == null) {
            return null;
        }
        return new Point3D(destination);
    }

    /**
     * Get the x coordinate of current destination.
     * @return the x coordinate of destination
     */
    @Override
    public double getDestinationX() {
        return destination.getX();
    }

    /**
     * Get the y coordinate of current destination.
     * @return the y coordinate of destination
     */
    @Override
    public double getDestinationY() {
        return destination.getY();
    }

    /**
     * Get the z coordinate of current destination.
     * @return the z coordinate of destination
     */
    @Override
    public double getDestinationZ() {
        return destination.getZ();
    }

    /**
     * Set the destination with the specified xyz coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @throws InvalidDataException if the specified xyz coordinates are not valid(negative value).
     * @see #setDestination(Point3D)
     */
    @Override
    public final void setDestination(double x, double y, double z) throws InvalidDataException {
        if (x < 0.0 || y < 0.0 || z < 0.0) {
            throw new InvalidDataException(
                    "Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")");
        }
        destination.setLocation(x, y, z);
    }

    /**
     * Set the destination with the specified Point3D. 
     * @param aPoint the Point3D for the new destination
     * @throws InvalidDataException if the input point is null.
     * @see #setDestination(double, double, double)
     */
    @Override
    public final void setDestination(Point3D aPoint) throws InvalidDataException {
        if (aPoint == null) {
            throw new InvalidDataException("Null Point3D sent to setDestination(Point3D)");
        }
        setDestination(aPoint.getX(), aPoint.getY(), aPoint.getZ());
    }

    /**
     * Get the speed of this MovableImpl.
     * @return the speed of this MovableImpl
     */
    @Override
    public double getSpeed() {
        return speed;
    }

    /**
     * Set the speed.
     * @param s the value of the speed
     * @throws InvalidDataException if speed is negative or bigger than max speed.
     */
    @Override
    public final void setSpeed(double s) throws InvalidDataException {
        if (s < 0.0) {
            throw new InvalidDataException("Negative speed sent to setSpeed:" + s);
        }
        if (s > getMaxSpeed()) {
            throw new InvalidDataException("Attempt to set speed (" + s + ") greater than maxSpeed (" + getMaxSpeed()
                    + ") in setSpeed");
        }
        speed = s;
    }

    /**
     * Get the maximum speed.
     * @return the maximum speed
     */
    @Override
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Set the maximum speed.
     * @param ms the value of maximum speed
     * @throws InvalidDataException if the maximum speed is negative or less than speed.
     */
    @Override
    public final void setMaxSpeed(double ms) throws InvalidDataException {
        if (ms < 0.0) {
            throw new InvalidDataException("Negative maxSpeed sent to setMaxSpeed:" + ms);
        }
        if (ms < getSpeed()) {
            throw new InvalidDataException("Attempt to set maxSpeed less than speed in setMaxSpeed: " + ms);
        }
        maxSpeed = ms;
    }

    /**
     * Check whether the current MovableImpl is at destination.
     * @return True if the location is same with destination; otherwise, return false.
     */
    @Override
    public boolean atDestination() {
        return myLocatable.getLocation().equals(destination);
    }

    /**
     * Computes the distance between current location and the specified point.
     * @param loc the Point3D for the target location
     * @return the distance between this location and the specified point.
     * @throws InvalidDataException if the input point is null.
     * @see #distance(double, double, double)
     */    
    @Override
    public double distance(Point3D loc) throws InvalidDataException {
        return myLocatable.distance(loc);
    }

    /**
     * Computes the distance between current location and another location with specified xyz coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the distance between this location and the specified point.
     * @throws InvalidDataException if the specified xyz coordinates are not valid(negative value).
     * @see #distance(Point3D)
     */
    @Override
    public double distance(double x, double y, double z) throws InvalidDataException {
        return myLocatable.distance(x, y, z);
    }

    /**
     * Get the location of the current MovableImpl.
     * @return the Point3D containing the  x y z data
     */
    @Override
    public Point3D getLocation() {
        return myLocatable.getLocation();
    }

    /**
     * Get the x coordinate of current location.
     * @return the x coordinate of location
     */
    @Override
    public double getLocationX() {
        return myLocatable.getLocationX();
    }

    /**
     * Get the y coordinate of current location.
     * @return the y coordinate of location
     */
    @Override
    public double getLocationY() {
        return myLocatable.getLocationY();
    }

    /**
     * Get the z coordinate of current location.
     * @return the z coordinate of location
     */
    @Override
    public double getLocationZ() {
        return myLocatable.getLocationZ();
    }

    /**
     * Set the location with the specified Point3D. 
     * @param loc the Point3D for the new location
     * @throws InvalidDataException if the input point is null.
     * @see #setLocation(double, double, double)
     */
    @Override
    public void setLocation(Point3D loc) throws InvalidDataException {
        myLocatable.setLocation(loc);
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
        myLocatable.setLocation(x, y, z);
    }

    /**
     * Update the location after the current MovableImpl moving for given time.
     * @param millis the time duration for moving
     * @throws InvalidDataException if any invalid data exception occurs.
     */
    @Override
    public void update(double millis) throws InvalidDataException {
        // This is a FAKE update method - NOT what you need for your project.
        double time = millis / 1000.0;

        double distanceTraveled = getSpeed() * time;
        double distance = getLocation().distance(getDestination());

        if (distance == 0.0) {
            return;
        }
        if (distanceTraveled >= distance) {
            setLocation(destination);
            return;
        }
        double delta = distanceTraveled / distance;

        double newX = getLocation().getX() + (getDestination().getX() - getLocation().getX()) * delta;
        double newY = getLocation().getY() + (getDestination().getY() - getLocation().getY()) * delta;
        double newZ = getLocation().getZ() + (getDestination().getZ() - getLocation().getZ()) * delta;

        setLocation(newX, newY, newZ);

    }
}
