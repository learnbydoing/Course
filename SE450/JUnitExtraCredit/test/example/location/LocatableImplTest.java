/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.location;

import example.common.InvalidDataException;
import example.common.Point3D;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Johnny
 */
public class LocatableImplTest {
    private Point3D testPoint3D;
    private LocatableImpl testLocatable;
    private static final double delta = 0.001;

    public LocatableImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        try {
            testPoint3D = new Point3D(11.1, 22.2, 33.3);
            testLocatable = new LocatableImpl(testPoint3D);
        } catch (InvalidDataException ex) {
            fail("Creation of test fixture object in @Before 'setUp' failed: " + ex.getMessage());
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getLocation method, of class LocatableImpl.
     */
    @Test
    public void testGetLocation() {
        Point3D p = testLocatable.getLocation();
        assertNotNull(p);
        assertEquals(p, testPoint3D);
        assertNotSame(p, testPoint3D);
    }

    /**
     * Test of getLocationX method, of class LocatableImpl.
     */
    @Test
    public void testGetLocationX() {
        assertEquals(testLocatable.getLocationX(), testPoint3D.getX(), delta);
    }

    /**
     * Test of getLocationY method, of class LocatableImpl.
     */
    @Test
    public void testGetLocationY() {
        assertEquals(testLocatable.getLocationY(), testPoint3D.getY(), delta);
    }

    /**
     * Test of getLocationZ method, of class LocatableImpl.
     */
    @Test
    public void testGetLocationZ() {
        assertEquals(testLocatable.getLocationZ(), testPoint3D.getZ(), delta);
    }

    /**
     * Test of setLocation method, of class LocatableImpl.
     */
    @Test
    public void testSetLocation_Point3D() throws Exception {
        Point3D newPoint = new Point3D(99.9, 88.8, 77.7);
        try {
            testLocatable.setLocation(newPoint);
            Point3D p = testLocatable.getLocation();
            assertNotNull(p);
            assertEquals(p, newPoint);
            assertNotSame(p, newPoint);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from LocatableImpl "
                    + "setLocation(Point3D) with a valid Point3D: " + newPoint);
        }
        
        newPoint = new Point3D(-55.5, 66.6, 77.7);
        try {
            testLocatable.setLocation(newPoint);
            fail("InvalidDataException NOT thrown from LocatableImpl setLocation(Point3D)"
                    +" with an invalid Point3D: " + newPoint);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to setLocation(x,y,z)");
            assertFalse(newPoint.equals(testLocatable.getLocation()));
        }
        
        newPoint = null;
        try {
            testLocatable.setLocation(newPoint);
            fail("InvalidDataException NOT thrown from LocatableImpl setLocation(Point3D) "
                    +" with a null Point3D");
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Null location sent to setLocation");
            assertNotNull(testLocatable.getLocation());
        }
    }

    /**
     * Test of setLocation method, of class LocatableImpl.
     */
    @Test
    public void testSetLocation_3args() throws Exception {
        double x = 99.9;
        double y = 88.8;
        double z = 77.7;
        try {
            testLocatable.setLocation(x, y, z);
            assertEquals(x, testLocatable.getLocationX(), delta);
            assertEquals(y, testLocatable.getLocationY(), delta);
            assertEquals(z, testLocatable.getLocationZ(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from LocatableImpl"
                    +" setLocation(x,y,z) with a valid x, y, z: " + x + ", " + y + ", " + z);
        }
        
        x = -11.1;
        y = 22.2;
        z = 33.3;
        try {
            testLocatable.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from LocatableImpl setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to setLocation(x,y,z)");
            assertTrue(x != testLocatable.getLocationX());
            assertTrue(y != testLocatable.getLocationY());
            assertTrue(z != testLocatable.getLocationZ());
        }
        x = 11.1;
        y = -22.2;
        z = 33.3;
        try {
            testLocatable.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from LocatableImpl setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to setLocation(x,y,z)");
            assertTrue(x != testLocatable.getLocationX());
            assertTrue(y != testLocatable.getLocationY());
            assertTrue(z != testLocatable.getLocationZ());
        }
        x = 11.1;
        y = 22.2;
        z = -33.3;
        try {
            testLocatable.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from LocatableImpl setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to setLocation(x,y,z)");
            assertTrue(x != testLocatable.getLocationX());
            assertTrue(y != testLocatable.getLocationY());
            assertTrue(z != testLocatable.getLocationZ());
        }
    }

    /**
     * Test of distance method, of class LocatableImpl.
     */
    @Test
    public void testDistance_Point3D() throws Exception {
        Point3D location = new Point3D(99.9, 88.8, 77.7);
        
        Point3D destination = null;
        double distance;
        try {
            testLocatable.setLocation(location);
            distance = testLocatable.distance(destination);
            assertEquals(distance, testLocatable.distance(destination), delta);            
        } catch (InvalidDataException ex) {
           assertEquals(ex.getMessage(), "Null location sent to distance");
        }
        
        destination = new Point3D(11.1, 22.2, 33.3);
        try {
            testLocatable.setLocation(location);
            double x= Math.pow(destination.getX()-location.getX(), 2);
            double y= Math.pow(destination.getY()-location.getY(), 2);
            double z= Math.pow(destination.getZ()-location.getZ(), 2);
            distance = Math.sqrt(x+y+z);
            assertEquals(distance, testLocatable.distance(destination), delta);            
            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from LocatableImpl "
                    + "distance(Point3D) with a valid Point3D: " + destination);
        }        
    }

    /**
     * Test of distance method, of class LocatableImpl.
     */
    @Test
    public void testDistance_3args() throws Exception {
        Point3D location = new Point3D(99.9, 88.8, 77.7);
        double x= 11.1;
        double y= 22.2;
        double z= 33.3;
        double distance;
        double expect = 0;
        
        try {
            testLocatable.setLocation(location);
            distance = testLocatable.distance(-x,y,z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to distance(x,y,z)");
        }
        
        try {
            testLocatable.setLocation(location);
            distance = testLocatable.distance(x,-y,z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to distance(x,y,z)");
        }
        
        try {
            testLocatable.setLocation(location);
            distance = testLocatable.distance(x,y,-z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to distance(x,y,z)");
        }

        try {
            testLocatable.setLocation(location);
            distance = testLocatable.distance(x,y,z);
            x= Math.pow(x-location.getX(), 2);
            y= Math.pow(y-location.getY(), 2);
            z= Math.pow(z-location.getZ(), 2);
            expect = Math.sqrt(x+y+z);
            assertEquals(expect, distance, delta);            
            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from LocatableImpl "
                    + "distance(double x, double y, double z) with a valid xyz.");
        }
    }    
}
