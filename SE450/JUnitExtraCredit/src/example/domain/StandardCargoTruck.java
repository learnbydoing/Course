package example.domain;

import example.common.CannotFitException;
import example.common.InvalidDataException;
import example.common.Point3D;
import example.common.TruckImplFactory;
import example.truck.Truck;

/**
 * A specific implementation of the {@link Truck} interface, the StandardCargoTruck.
 * The StandardCargoTruck does everything in a “standard” way, including “load” and “unload” behavior.
 * @author Rong Zhuang
 */
public class StandardCargoTruck implements Truck {

    private Truck myTruck; // Delegate - will refer to some implementation object

    /**
     * Constructs and initializes a StandardCargoTruck with specified xyz coordinates of location and destination.
     * @param lX the x coordinate of location
     * @param lY the y coordinate of location
     * @param lZ the z coordinate of location
     * @param dX the x coordinate of destination
     * @param dY the y coordinate of destination
     * @param dZ the z coordinate of destination
     * @param spd the speed
     * @param mxSpd the maximum speed
     * @param mlw the maximum load weight
     * @throws InvalidDataException if the input parameters are invalid to construct a new StandardCargoTruck.
     * @see #StandardCargoTruck(Point3D, Point3D, double, double, double)
     */
    public StandardCargoTruck(double lX, double lY, double lZ, double dX, double dY, double dZ,
            double spd, double mxSpd, double mlw) throws InvalidDataException {
        myTruck = TruckImplFactory.createTruck(lX, lY, lZ, dX, dY, dZ, spd, mxSpd, mlw);
    }

    /**
     * Constructs and initializes a StandardCargoTruck with specified location and destination Point3D.
     * @param loc the location Point3D
     * @param dest the destination Point3D
     * @param spd the speed
     * @param mxSpd the maximum speed
     * @param mlw the maximum load weight
     * @throws InvalidDataException if the input parameters are invalid to construct a new StandardCargoTruck.
     * @see #StandardCargoTruck(double, double, double, double, double, double, double, double, double)
     */
    public StandardCargoTruck(Point3D loc, Point3D dest, double spd, double mxSpd, double mlw) throws InvalidDataException {
        this(loc.getX(), loc.getY(), loc.getZ(), dest.getX(), dest.getY(), dest.getZ(), spd, mxSpd, mlw);
    }

    /**
     * Load goods to the standard cargo truck by the given amount.
     * @param amount the quantity for loading
     * @throws InvalidDataException if the amount is negative.
     * @throws CannotFitException if the amount is larger than the allowed maximum load weight.
     */
    @Override
    public void load(double amount) throws InvalidDataException, CannotFitException {
        myTruck.load(amount);
    }

     /**
     * Unload goods from the standard cargo truck by the given amount.
     * @param amount the quantity for unloading
     * @throws InvalidDataException if the amount is negative.
     * @throws CannotFitException if there is not enough goods for unloading.
     */
    @Override
    public void unLoad(double amount) throws InvalidDataException, CannotFitException {
        myTruck.unLoad(amount);
    }

     /**
     * Check whether the standard cargo truck is at destination.
     * @return True if the standard cargo truck is at destination; otherwise, return false.
     */
    @Override
    public boolean atDestination() {
        return myTruck.atDestination();
    }

    /**
     * Get the destination of this standard cargo truck.
     * @return the Point3D containing the  x y z data
     */
    @Override
    public Point3D getDestination() {
        return myTruck.getDestination();
    }

    /**
     * Get the x coordinate of current destination.
     * @return  the x coordinate of destination
     */
    @Override
    public double getDestinationX() {
        return myTruck.getDestinationX();
    }

    /**
     * Get the y coordinate of current destination.
     * @return  the y coordinate of destination
     */
    @Override
    public double getDestinationY() {
        return myTruck.getDestinationY();
    }

    /**
     * Get the z coordinate of current destination.
     * @return  the z coordinate of destination
     */
    @Override
    public double getDestinationZ() {
        return myTruck.getDestinationZ();
    }

    /**
     * Get the maximum speed of this standard cargo truck.
     * @return the maximum speed
     */
    @Override
    public double getMaxSpeed() {
        return myTruck.getMaxSpeed();
    }

     /**
     * Get the speed of this standard cargo truck.
     * @return  the speed of this truck
     */
    @Override
    public double getSpeed() {
        return myTruck.getSpeed();
    }

     /**
     * Set the destination of this standard cargo truck with the specified xyz coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @throws InvalidDataException if the specified xyz coordinates are not valid(negative value).
     * @see #setDestination(Point3D)
     */
    @Override
    public void setDestination(double x, double y, double z) throws InvalidDataException {
        myTruck.setDestination(x, y, z);
    }

    /**
     * Set the destination with the specified Point3D. 
     * @param aPoint the Point3D for the new destination
     * @throws InvalidDataException if the input point is null.
     * @see #setDestination(double, double, double)
     */
    @Override
    public void setDestination(Point3D aPoint) throws InvalidDataException {
        myTruck.setDestination(aPoint);
    }

    /**
     * Set the maximum speed of this standard cargo truck.
     * @param ms the value of maximum speed
     * @throws InvalidDataException if the maximum speed is negative or less than speed.
     */
    @Override
    public void setMaxSpeed(double ms) throws InvalidDataException {
        myTruck.setMaxSpeed(ms);
    }

    /**
     * Set the speed.
     * @param s the value of the speed
     * @throws InvalidDataException if speed is negative or bigger than max speed.
     */
    @Override
    public void setSpeed(double s) throws InvalidDataException {
        myTruck.setSpeed(s);
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
        return myTruck.distance(loc);
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
        return myTruck.distance(x, y, z);
    }

    /**
     * Get the location of the standard cargo truck.
     * @return the Point3D containing the  x y z data
     */
    @Override
    public Point3D getLocation() {
        return myTruck.getLocation();
    }

     /**
     * Get the x coordinate of current location.
     * @return the x coordinate of location
     */
    @Override
    public double getLocationX() {
        return myTruck.getLocationX();
    }

     /**
     * Get the y coordinate of current location.
     * @return the y coordinate of location
     */
    @Override
    public double getLocationY() {
        return myTruck.getLocationY();
    }

     /**
     * Get the z coordinate of current location.
     * @return the z coordinate of location
     */
    @Override
    public double getLocationZ() {
        return myTruck.getLocationZ();
    }

    /**
     * Set the location with the specified Point3D. 
     * @param loc the Point3D for the new location
     * @throws InvalidDataException if the input point is null.
     * @see #setLocation(double, double, double)
     */
    @Override
    public void setLocation(Point3D loc) throws InvalidDataException {
        myTruck.setLocation(loc);
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
        myTruck.setLocation(x, y, z);
    }

    /**
     * Get the identifier of this standard cargo truck.
     * @return the identifier
     */
    @Override
    public String getIdentifier() {
        return myTruck.getIdentifier();
    }

    /**
     * Get the maximum load weight.
     * @return the value of the maximum load weight
     */
    @Override
    public double getMaxLoadWeight() {
        return myTruck.getMaxLoadWeight();
    }

    /**
     * Set the current load weight.
     * @param d the value of the new load weight
     */
    @Override
    public void setCurrentLoadWeight(double d) {
        myTruck.setCurrentLoadWeight(d);
    }

    /**
     * Get the current load weight.
     * @return the value of the current load weight
     */
    @Override
    public double getCurrentLoadWeight() {
        return myTruck.getCurrentLoadWeight();
    }

    /**
     * Update the location after this standard cargo truck moving for given time.
     * @param millis the time duration for moving
     * @throws InvalidDataException if any invalid data exception occurs.
     */
    @Override
    public void update(double millis) throws InvalidDataException {
        myTruck.update(millis);
    }

    /**
     * Returns a string representation of this standard cargo truck. 
     * @return information of the standard cargo truck
     */
    @Override
    public String toString() {
        try {
            return "I am StandardCargoTruck " + getIdentifier() + ".\n\tI am at "
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
