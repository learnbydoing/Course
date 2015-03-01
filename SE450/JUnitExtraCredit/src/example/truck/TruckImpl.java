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
    public TruckImpl(double lX, double lY, double lZ, double dX, double dY, double dZ,
            double spd, double mxSpd, double mlw) throws InvalidDataException {
        myMovable = MovableImplFactory.createMovable(lX, lY, lZ, dX, dY, dZ, spd, mxSpd);
        myIdentity = IdentifiableImplFactory.createIdentifiable();
        maxLoadWeight = mlw;

    }

    /**
     * Constructs and initializes a TruckImpl with specified location and destination Point3D.
     * @param loc - the location Point3D
     * @param dest - the destination Point3D
     * @param spd - the speed
     * @param mxSpd - the maximum speed
     * @param mlw - the maximum load weight
     * @throws InvalidDataException 
     */
    public TruckImpl(Point3D loc, Point3D dest, double spd, double mxSpd, double mlw) throws InvalidDataException {
        this(loc.getX(), loc.getY(), loc.getZ(), dest.getX(), dest.getY(), dest.getZ(), spd, mxSpd, mlw);
    }

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

    @Override
    public boolean atDestination() {
        return myMovable.atDestination();
    }

    @Override
    public Point3D getDestination() {
        return myMovable.getDestination();
    }

    @Override
    public double getDestinationX() {
        return myMovable.getDestinationX();
    }

    @Override
    public double getDestinationY() {
        return myMovable.getDestinationY();
    }

    @Override
    public double getDestinationZ() {
        return myMovable.getDestinationZ();
    }

    @Override
    public double getMaxSpeed() {
        return myMovable.getMaxSpeed();
    }

    @Override
    public double getSpeed() {
        return myMovable.getSpeed();
    }

    @Override
    public void setDestination(double x, double y, double z) throws InvalidDataException {
        myMovable.setDestination(x, y, z);
    }

    @Override
    public void setDestination(Point3D aPoint) throws InvalidDataException {
        myMovable.setDestination(aPoint);
    }

    @Override
    public void setMaxSpeed(double ms) throws InvalidDataException {
        myMovable.setMaxSpeed(ms);
    }

    @Override
    public void setSpeed(double s) throws InvalidDataException {
        myMovable.setSpeed(s);
    }

    @Override
    public double distance(Point3D loc) throws InvalidDataException {
        return myMovable.distance(loc);
    }

    @Override
    public double distance(double x, double y, double z) throws InvalidDataException {
        return myMovable.distance(x, y, z);
    }

    @Override
    public Point3D getLocation() {
        return myMovable.getLocation();
    }

    @Override
    public double getLocationX() {
        return myMovable.getLocationX();
    }

    @Override
    public double getLocationY() {
        return myMovable.getLocationY();
    }

    @Override
    public double getLocationZ() {
        return myMovable.getLocationZ();
    }

    @Override
    public void setLocation(Point3D loc) throws InvalidDataException {
        myMovable.setLocation(loc);
    }

    @Override
    public void setLocation(double x, double y, double z) throws InvalidDataException {
        myMovable.setLocation(x, y, z);
    }

    @Override
    public double getMaxLoadWeight() {
        return maxLoadWeight;
    }

    @Override
    public void setCurrentLoadWeight(double mlw) {
        currentLoadWeight = mlw;
    }

    @Override
    public double getCurrentLoadWeight() {
        return currentLoadWeight;
    }

    @Override
    public String getIdentifier() {
        return myIdentity.getIdentifier();
    }

    @Override
    public void update(double millis) throws InvalidDataException {
        myMovable.update(millis);
    }
}
