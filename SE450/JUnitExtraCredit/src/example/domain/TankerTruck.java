package example.domain;

import example.common.CannotFitException;
import example.common.InvalidDataException;
import example.common.Point3D;
import example.common.TruckImplFactory;
import example.truck.Truck;

/**
 * A specific implementation of the {@link Truck} interface, the TankerTruck.
 * @author Rong Zhuang
 */
public class TankerTruck implements Truck {

    private Truck myTruck; // Delegate - will refer to some implementation object
    private static final double TANKER_LOAD_RATE_LIMIT = 2000.0;

    /**
     * Constructs and initializes a TankerTruck with specified xyz coordinates of location and destination.
     * @param lX - the x coordinate of location
     * @param lY - the y coordinate of location
     * @param lZ - the z coordinate of location
     * @param dX - the x coordinate of destination
     * @param dY - the y coordinate of destination
     * @param dZ - the z coordinate of destination
     * @param spd - the speed
     * @param mxSpd - the maximum speed
     * @param mlw - the maximum load weight
     * @throws InvalidDataException 
     */
    public TankerTruck(double lX, double lY, double lZ, double dX, double dY, double dZ,
            double spd, double mxSpd, double mlw) throws InvalidDataException {
        myTruck = TruckImplFactory.createTruck(lX, lY, lZ, dX, dY, dZ, spd, mxSpd, mlw);
    }

    /**
     * Constructs and initializes a TankerTruck with specified location and destination Point3D.
     * @param loc - the location Point3D
     * @param dest - the destination Point3D
     * @param spd - the speed
     * @param mxSpd - the maximum speed
     * @param mlw - the maximum load weight
     * @throws InvalidDataException 
     */
    public TankerTruck(Point3D loc, Point3D dest, double spd, double mxSpd, double mlw) throws InvalidDataException {
        this(loc.getX(), loc.getY(), loc.getZ(), dest.getX(), dest.getY(), dest.getZ(), spd, mxSpd, mlw);
    }

    @Override
    public void load(double amount) throws InvalidDataException, CannotFitException {
        if (amount < 0) {
            throw new InvalidDataException("Negative load amount: " + amount);
        }
        if (amount > TANKER_LOAD_RATE_LIMIT) {
            throw new InvalidDataException("Loading " + amount + " at one time exceeds the TankerTruck load rate limit of " + TANKER_LOAD_RATE_LIMIT + " at a time.");
        }
        if ((myTruck.getCurrentLoadWeight() + amount) > myTruck.getMaxLoadWeight()) {
            throw new CannotFitException("Additional load of " + amount + " will make the load weight "
                    + (myTruck.getCurrentLoadWeight() + amount) + " which exceeds the max load weight of " + myTruck.getMaxLoadWeight());
        }

        setCurrentLoadWeight(myTruck.getCurrentLoadWeight() + amount);
    }

    @Override
    public void unLoad(double amount) throws InvalidDataException, CannotFitException {
        myTruck.unLoad(amount);
    }

    @Override
    public boolean atDestination() {
        return myTruck.atDestination();
    }

    @Override
    public Point3D getDestination() {
        return myTruck.getDestination();
    }

    @Override
    public double getDestinationX() {
        return myTruck.getDestinationX();
    }

    @Override
    public double getDestinationY() {
        return myTruck.getDestinationY();
    }

    @Override
    public double getDestinationZ() {
        return myTruck.getDestinationZ();
    }

    @Override
    public double getMaxSpeed() {
        return myTruck.getMaxSpeed();
    }

    @Override
    public double getSpeed() {
        return myTruck.getSpeed();
    }

    @Override
    public void setDestination(double x, double y, double z) throws InvalidDataException {
        myTruck.setDestination(x, y, z);
    }

    @Override
    public void setDestination(Point3D aPoint) throws InvalidDataException {
        myTruck.setDestination(aPoint);
    }

    @Override
    public void setMaxSpeed(double ms) throws InvalidDataException {
        myTruck.setMaxSpeed(ms);
    }

    @Override
    public void setSpeed(double s) throws InvalidDataException {
        myTruck.setSpeed(s);
    }

    @Override
    public double distance(Point3D loc) throws InvalidDataException {
        return myTruck.distance(loc);
    }

    @Override
    public double distance(double x, double y, double z) throws InvalidDataException {
        return myTruck.distance(x, y, z);
    }

    @Override
    public Point3D getLocation() {
        return myTruck.getLocation();
    }

    @Override
    public double getLocationX() {
        return myTruck.getLocationX();
    }

    @Override
    public double getLocationY() {
        return myTruck.getLocationY();
    }

    @Override
    public double getLocationZ() {
        return myTruck.getLocationZ();
    }

    @Override
    public void setLocation(Point3D loc) throws InvalidDataException {
        myTruck.setLocation(loc);
    }

    @Override
    public void setLocation(double x, double y, double z) throws InvalidDataException {
        myTruck.setLocation(x, y, z);
    }

    @Override
    public String getIdentifier() {
        return myTruck.getIdentifier();
    }

    @Override
    public double getMaxLoadWeight() {
        return myTruck.getMaxLoadWeight();
    }

    @Override
    public void setCurrentLoadWeight(double d) {
        myTruck.setCurrentLoadWeight(d);
    }

    @Override
    public double getCurrentLoadWeight() {
        return myTruck.getCurrentLoadWeight();
    }

    @Override
    public void update(double millis) throws InvalidDataException {
        myTruck.update(millis);
    }

    @Override
    public String toString() {
        try {
            return "I am TankerTruck " + getIdentifier() + ".\n\tI am at "
                    + getLocation() + " and am heading to " + getDestination()
                    + ".\n\tMy load is " + getCurrentLoadWeight() + " and my max load is "
                    + getMaxLoadWeight() + ".\n\tDistance to my destination is "
                    + String.format("%4.2f", distance(getDestination())) + ". "
                    + (atDestination() ? "I am there!" : "I'm not there yet");
        } catch (InvalidDataException ex) {
            return ex.getMessage();
        }
    }
}
