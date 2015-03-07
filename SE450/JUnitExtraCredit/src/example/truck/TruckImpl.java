package example.truck;

import example.common.CannotFitException;
import example.common.IdentifiableImplFactory;
import example.common.InvalidDataException;
import example.common.MovableImplFactory;
import example.common.Point3D;
import example.identification.Identifiable;
import example.movement.Movable;

/**
 * Default implementation of the {@link Truck} interface.
 * @author Rong Zhuang
 */
public class TruckImpl implements Truck {

    private double maxLoadWeight;
    private double currentLoadWeight;
    private Movable myMovable;
    private Identifiable myIdentity;

    /**
     * Constructs and initializes a TruckImpl with specified xyz coordinates of location and destination.
     * @param lX the x coordinate of location
     * @param lY the y coordinate of location
     * @param lZ the z coordinate of location
     * @param dX the x coordinate of destination
     * @param dY the y coordinate of destination
     * @param dZ the z coordinate of destination
     * @param spd the speed
     * @param mxSpd the maximum speed
     * @param mlw the maximum load weight
     * @throws InvalidDataException if the input parameters are invalid to construct a new TruckImpl
     * @see #TruckImpl(Point3D, Point3D, double, double, double)
     */
    public TruckImpl(double lX, double lY, double lZ, double dX, double dY, double dZ,
            double spd, double mxSpd, double mlw) throws InvalidDataException {
        myMovable = MovableImplFactory.createMovable(lX, lY, lZ, dX, dY, dZ, spd, mxSpd);
        myIdentity = IdentifiableImplFactory.createIdentifiable();
        maxLoadWeight = mlw;

    }

    /**
     * Constructs and initializes a TruckImpl with specified location and destination Point3D.
     * @param loc the location Point3D
     * @param dest the destination Point3D
     * @param spd the speed
     * @param mxSpd the maximum speed
     * @param mlw the maximum load weight
     * @throws InvalidDataException if the input parameters are invalid to construct a new TruckImpl
     * @see #TruckImpl(double, double, double, double, double, double, double, double, double)
     */
    public TruckImpl(Point3D loc, Point3D dest, double spd, double mxSpd, double mlw) throws InvalidDataException {
        this(loc.getX(), loc.getY(), loc.getZ(), dest.getX(), dest.getY(), dest.getZ(), spd, mxSpd, mlw);
    }

    /**
     * Load goods to the truck by the given amount.
     * @param amount the quantity for loading
     * @throws InvalidDataException if the amount is negative.
     * @throws CannotFitException if the amount is larger than the allowed maximum load weight.
     */
    @Override
    public void load(double amount) throws InvalidDataException, CannotFitException {
        if (amount < 0) {
            throw new InvalidDataException("Negative load amount: " + amount);
        }

        if ((currentLoadWeight + amount) > maxLoadWeight) {
            throw new CannotFitException("Additional load of " + amount + " will make the load weight "
                    + (currentLoadWeight + amount) + " which exceeds the max load weight of " + maxLoadWeight);
        }

        currentLoadWeight += amount;
    }

    /**
     * Unload goods from the truck by the given amount.
     * @param amount the quantity for unloading
     * @throws InvalidDataException if the amount is negative.
     * @throws CannotFitException if there is not enough goods for unloading.
     */
    @Override
    public void unLoad(double amount) throws InvalidDataException, CannotFitException {
        if (amount < 0) {
            throw new InvalidDataException("Negative unLoad amount: " + amount);
        }

        if ((currentLoadWeight - amount) < 0.0) {
            throw new CannotFitException("UnLoading " + amount + " will make the load weight negative: "
                    + (currentLoadWeight + amount));
        }

        currentLoadWeight -= amount;
    }

    /**
     * Check whether the truck is at destination.
     * @return True if the truck is at destination; otherwise, return false.
     */
    @Override
    public boolean atDestination() {
        return myMovable.atDestination();
    }

    /**
     * Get the destination of this truck.
     * @return the Point3D containing the  x y z data
     */
    @Override
    public Point3D getDestination() {
        return myMovable.getDestination();
    }

    /**
     * Get the x coordinate of current destination.
     * @return  the x coordinate of destination
     */
    @Override
    public double getDestinationX() {
        return myMovable.getDestinationX();
    }

    /**
     * Get the y coordinate of current destination.
     * @return  the y coordinate of destination
     */
    @Override
    public double getDestinationY() {
        return myMovable.getDestinationY();
    }

    /**
     * Get the z coordinate of current destination.
     * @return  the z coordinate of destination
     */
    @Override
    public double getDestinationZ() {
        return myMovable.getDestinationZ();
    }

    /**
     * Get the maximum speed of this truck.
     * @return the maximum speed
     */
    @Override
    public double getMaxSpeed() {
        return myMovable.getMaxSpeed();
    }

    /**
     * Get the speed of this truck.
     * @return  the speed of this truck
     */
    @Override
    public double getSpeed() {
        return myMovable.getSpeed();
    }

    /**
     * Set the destination of this truck with the specified xyz coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @throws InvalidDataException if the specified xyz coordinates are not valid(negative value).
     * @see #setDestination(Point3D)
     */
    @Override
    public void setDestination(double x, double y, double z) throws InvalidDataException {
        myMovable.setDestination(x, y, z);
    }

    /**
     * Set the destination with the specified Point3D. 
     * @param aPoint the Point3D for the new destination
     * @throws InvalidDataException if the input point is null.
     * @see #setDestination(double, double, double)
     */
    @Override
    public void setDestination(Point3D aPoint) throws InvalidDataException {
        myMovable.setDestination(aPoint);
    }

    /**
     * Set the maximum speed of this truck.
     * @param ms the value of maximum speed
     * @throws InvalidDataException if the maximum speed is negative or less than speed.
     */
    @Override
    public void setMaxSpeed(double ms) throws InvalidDataException {
        myMovable.setMaxSpeed(ms);
    }

    /**
     * Set the speed.
     * @param s the value of the speed
     * @throws InvalidDataException if speed is negative or bigger than max speed.
     */
    @Override
    public void setSpeed(double s) throws InvalidDataException {
        myMovable.setSpeed(s);
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
        return myMovable.distance(loc);
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
        return myMovable.distance(x, y, z);
    }

    /**
     * Get the location of the truck.
     * @return the Point3D containing the  x y z data
     */
    @Override
    public Point3D getLocation() {
        return myMovable.getLocation();
    }

    /**
     * Get the x coordinate of current location.
     * @return the x coordinate of location
     */
    @Override
    public double getLocationX() {
        return myMovable.getLocationX();
    }

    /**
     * Get the y coordinate of current location.
     * @return the y coordinate of location
     */
    @Override
    public double getLocationY() {
        return myMovable.getLocationY();
    }

    /**
     * Get the z coordinate of current location.
     * @return the z coordinate of location
     */
    @Override
    public double getLocationZ() {
        return myMovable.getLocationZ();
    }

    /**
     * Set the location with the specified Point3D. 
     * @param loc the Point3D for the new location
     * @throws InvalidDataException if the input point is null.
     * @see #setLocation(double, double, double)
     */
    @Override
    public void setLocation(Point3D loc) throws InvalidDataException {
        myMovable.setLocation(loc);
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
        myMovable.setLocation(x, y, z);
    }

    /**
     * Get the maximum load weight.
     * @return the value of the maximum load weight
     */
    @Override
    public double getMaxLoadWeight() {
        return maxLoadWeight;
    }

    /**
     * Set the current load weight.
     * @param mlw the value of the new load weight
     */
    @Override
    public void setCurrentLoadWeight(double mlw) {
        currentLoadWeight = mlw;
    }

    /**
     * Get the current load weight.
     * @return the value of the current load weight
     */
    @Override
    public double getCurrentLoadWeight() {
        return currentLoadWeight;
    }

     /**
     * Get the identifier of this truck.
     * @return the identifier
     */
    @Override
    public String getIdentifier() {
        return myIdentity.getIdentifier();
    }

     /**
     * Update the location after this truck moving for given time.
     * @param millis the time duration for moving
     * @throws InvalidDataException if any invalid data exception occurs.
     */
    @Override
    public void update(double millis) throws InvalidDataException {
        myMovable.update(millis);
    }
}
