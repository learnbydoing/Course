package example.common;

import java.awt.Point;

/**
 * A 3 element point that is represented by double precision floating 
 * point x,y,z coordinates.
 * @author Rong Zhuang
 */
public class Point3D extends Point.Double {

    /**
     * the z coordinate
     */
    public double z;

    /**
     * Constructs and initializes a Point3D from the specified xyz coordinates.
     * @param xIn the x coordinate
     * @param yIn the y coordinate
     * @param zIn the z coordinate
     * @see #Point3D()
     * @see #Point3D(double, double)
     * @see #Point3D(Point3D)
     */
    public Point3D(double xIn, double yIn, double zIn) {
        setCoordinates(xIn, yIn, zIn);
    }

    /**
     * Constructs and initializes a Point3D from the specified xy coordinates, the z direction is 0.
     * @param xIn the x coordinate
     * @param yIn the y coordinate
     * @see #Point3D()
     * @see #Point3D(double, double, double)
     * @see #Point3D(Point3D)
     */
    public Point3D(double xIn, double yIn) {
        setCoordinates(xIn, yIn, 0.0);
    }

    /**
     * Constructs and initializes a Point3D from the specified Point3D.
     * @param aPoint the Point3D containing the initialization x y z data
     * @see #Point3D()
     * @see #Point3D(double, double)
     * @see #Point3D(double, double, double)
     */
    public Point3D(Point3D aPoint) {
        setCoordinates(aPoint.getX(), aPoint.getY(), aPoint.getZ());
    }

    /**
     * Constructs and initializes a Point3D to (0,0,0).     * 
     * @see #Point3D(double, double)
     * @see #Point3D(double, double, double)
     * @see #Point3D(Point3D)
     */
    public Point3D() {
    }

    /**
     * Get the z coordinate of the this point.
     * @return the z coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * Set the xyz coordinates with the specified xyz coordinates.
     * @param xIn the x coordinate
     * @param yIn the y coordinate
     * @param zIn the z coordinate
     * @see #setCoordinates(Point3D)
     */
    public final void setCoordinates(double xIn, double yIn, double zIn) {
        x = xIn;
        y = yIn;
        z = zIn;
    }

    /**
     * Set the location with the specified Point3D. 
     * @param aPoint the Point3D containing the  x y z data
     * @see #setLocation(double, double, double)
     */
    public void setLocation(Point3D aPoint) {
        setCoordinates(aPoint);
    }

    /**
     * Set the location with the specified xyz coordinates.
     * @param xIn the x coordinate
     * @param yIn the y coordinate
     * @param zIn the z coordinate
     * @see #setLocation(Point3D)
     */
    public void setLocation(double xIn, double yIn, double zIn) {
        setCoordinates(xIn, yIn, zIn);
    }

    /**
     * Set the xyz coordinates with the specified Point3D.
     * @param aPoint the Point3D containing the  x y z data
     * @see #setCoordinates(double, double, double)
     */
    public void setCoordinates(Point3D aPoint) {
        setCoordinates(aPoint.getX(), aPoint.getY(), aPoint.getZ());
    }
   
    /**
     * {@inheritDoc}
     * Returns a string representation of this Point3D, including the three xyz coordinates. 
     * @return information of the Point3D
     */
     @Override
    public String toString() {
        return String.format("[%-1.2f, %-1.2f, %-1.2f]", x, y, z);
    }

    /**
     * Computes the distance between this point and point (xIn, yIn, zIn).
     * @param xIn the x coordinate
     * @param yIn the y coordinate
     * @param zIn the z coordinate
     * @return the distance between this point and point(xIn, yIn, zIn).
     * @see #distance(Point3D)
     */
    public double distance(double xIn, double yIn, double zIn) {
        xIn -= getX();
        yIn -= getY();
        zIn -= getZ();

        return Math.sqrt(xIn * xIn + yIn * yIn + zIn * zIn);
    }

    /**
     * Computes the distance between this point and point aPoint.
     * @param aPoint the other point
     * @return the distance between this point and point aPoint.
     * @see #distance(double, double, double)
     */
    public double distance(Point3D aPoint) {
        return distance(aPoint.getX(), aPoint.getY(), aPoint.getZ());
    }

    /**
     * Determines whether or not this point is equal to point aPoint. 
     * @param aPoint3D the other point
     * @return true if the two points have the same x,y and z value; otherwise, false.
     */
    public boolean equals(Point3D aPoint3D) {
        return (getX() == aPoint3D.getX())
                && (getY() == aPoint3D.getY())
                && (getZ() == aPoint3D.getZ());
    }
}
