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
     * @param loc - the location Point3D
     * @param dest - the destination Point3D
     * @param spd - the speed
     * @param mxSpd - the maximum speed
     * @throws InvalidDataException 
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
     * @throws InvalidDataException 
     */
    public MovableImpl(double lX, double lY, double lZ, double dX, double dY, double dZ,
            double spd, double mxSpd) throws InvalidDataException {
        setLocatable(LocatableImplFactory.createLocatable(new Point3D(lX, lY, lZ)));
        setDestination(dX, dY, dZ);
        setMaxSpeed(mxSpd);
        setSpeed(spd);
    }

    private void setLocatable(Locatable li) throws InvalidDataException {
        if (li == null) {
            throw new InvalidDataException("Null Locatable sent to setLocatable.");
        }
        myLocatable = li;
    }

    private Locatable getLocatable() {
        return myLocatable;
    }

    @Override
    public Point3D getDestination() {
        if (destination == null) {
            return null;
        }
        return new Point3D(destination);
    }

    @Override
    public double getDestinationX() {
        return destination.getX();
    }

    @Override
    public double getDestinationY() {
        return destination.getY();
    }

    @Override
    public double getDestinationZ() {
        return destination.getZ();
    }

    @Override
    public final void setDestination(double x, double y, double z) throws InvalidDataException {
        if (x < 0.0 || y < 0.0 || z < 0.0) {
            throw new InvalidDataException(
                    "Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")");
        }
        destination.setLocation(x, y, z);
    }

    @Override
    public final void setDestination(Point3D aPoint) throws InvalidDataException {
        if (aPoint == null) {
            throw new InvalidDataException("Null Point3D sent to setDestination(Point3D)");
        }
        setDestination(aPoint.getX(), aPoint.getY(), aPoint.getZ());
    }

    @Override
    public double getSpeed() {
        return speed;
    }

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

    @Override
    public double getMaxSpeed() {
        return maxSpeed;
    }

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

    @Override
    public boolean atDestination() {
        return myLocatable.getLocation().equals(destination);
    }

    // From Locatable interface
    @Override
    public double distance(Point3D loc) throws InvalidDataException {
        return myLocatable.distance(loc);
    }

    @Override
    public double distance(double x, double y, double z) throws InvalidDataException {
        return myLocatable.distance(x, y, z);
    }

    @Override
    public Point3D getLocation() {
        return myLocatable.getLocation();
    }

    @Override
    public double getLocationX() {
        return myLocatable.getLocationX();
    }

    @Override
    public double getLocationY() {
        return myLocatable.getLocationY();
    }

    @Override
    public double getLocationZ() {
        return myLocatable.getLocationZ();
    }

    @Override
    public void setLocation(Point3D loc) throws InvalidDataException {
        myLocatable.setLocation(loc);
    }

    @Override
    public void setLocation(double x, double y, double z) throws InvalidDataException {
        myLocatable.setLocation(x, y, z);
    }

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
