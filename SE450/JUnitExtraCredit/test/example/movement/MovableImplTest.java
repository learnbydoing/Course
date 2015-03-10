/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.movement;

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
public class MovableImplTest {
    
    private Point3D testLoc;
    private Point3D testDest;
    private double testSpeed;
    private double testMaxSpeed;
    private MovableImpl testMovable;
    double distance = 0;
    double expect = 0;
    
    private static final double delta = 0.001;
    
    public MovableImplTest() {
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
            testLoc = new Point3D(44.4, 55.5, 66.6);
            testDest = new Point3D(77.7, 88.8, 99.9);
            testSpeed = 100;
            testMaxSpeed = 200;
            testMovable = new MovableImpl(testLoc,testDest,testSpeed,testMaxSpeed);
        } catch (InvalidDataException ex) {
            fail("Creation of test fixture object in @Before 'setUp' failed: " + ex.getMessage());
        }
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of Constructor method, of class LocatableImpl.
     */
    @Test
    public void testConstructor() {
        MovableImpl movable = null;
        try {
            testLoc = new Point3D(44.4, 55.5, 66.6);
            testDest = new Point3D(77.7, 88.8, 99.9);
            testSpeed = 200.0;
            testMaxSpeed = 100.0; //less than speed
            movable = new MovableImpl(testLoc,testDest,testSpeed,testMaxSpeed);
            fail("InvalidDataException NOT thrown from MovableImpl constructor with a invalid speed:"+testSpeed+" and maxspeed:" + testMaxSpeed);
        } catch (InvalidDataException ex) {
            assertNull(movable);
        }
    }

    /**
     * Test of getDestination method, of class MovableImpl.
     */
    @Test
    public void testGetDestination() {
        Point3D p = testMovable.getDestination();
        assertNotNull(p);
        assertEquals(p, testDest);
        assertNotSame(p, testDest);       
    }

    /**
     * Test of getDestinationX method, of class MovableImpl.
     */
    @Test
    public void testGetDestinationX() {
        assertEquals(testDest.getX(), testMovable.getDestinationX(), delta);
    }

    /**
     * Test of getDestinationY method, of class MovableImpl.
     */
    @Test
    public void testGetDestinationY() {
        assertEquals(testDest.getY(), testMovable.getDestinationY(), delta);
    }

    /**
     * Test of getDestinationZ method, of class MovableImpl.
     */
    @Test
    public void testGetDestinationZ() {
        assertEquals(testDest.getZ(), testMovable.getDestinationZ(), delta);
    }

    /**
     * Test of setDestination method, of class MovableImpl.
     */
    @Test
    public void testSetDestination_3args() throws Exception {
        double x = 99.9;
        double y = 88.8;
        double z = 77.7;
        try {
            testMovable.setDestination(x, y, z);
            assertEquals(x, testMovable.getDestinationX(), delta);
            assertEquals(y, testMovable.getDestinationY(), delta);
            assertEquals(z, testMovable.getDestinationZ(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from MovableImpl"
                    +" setDestination(x,y,z) with a valid x, y, z: " + x + ", " + y + ", " + z);
        }
        
        x = -11.1;
        y = 22.2;
        z = 33.3;
        try {
            testMovable.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from MovableImpl setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")", ex.getMessage());
            assertTrue(x != testMovable.getDestinationX());
            assertTrue(y != testMovable.getDestinationY());
            assertTrue(z != testMovable.getDestinationZ());
        }
        
        x = 11.1;
        y = -22.2;
        z = 33.3;
        try {
            testMovable.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from MovableImpl setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")", ex.getMessage());
            assertTrue(x != testMovable.getDestinationX());
            assertTrue(y != testMovable.getDestinationY());
            assertTrue(z != testMovable.getDestinationZ());
        }
        
        x = 11.1;
        y = 22.2;
        z = -33.3;
        try {
            testMovable.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from MovableImpl setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")", ex.getMessage());
            assertTrue(x != testMovable.getDestinationX());
            assertTrue(y != testMovable.getDestinationY());
            assertTrue(z != testMovable.getDestinationZ());
        }
        
    }

    /**
     * Test of setDestination method, of class MovableImpl.
     */
    @Test
    public void testSetDestination_Point3D() throws Exception {
        Point3D newPoint = new Point3D(101.1, 202.2, 303.3);
        try {
            testMovable.setDestination(newPoint);
            Point3D p = testMovable.getDestination();
            assertNotNull(p);
            assertEquals(p, newPoint);
            assertNotSame(p, newPoint);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from MovableImpl "
                    + "setDestination(Point3D) with a valid Point3D: " + newPoint);
        }
        
        newPoint = new Point3D(-505.5, 606.6, 707.7);
        try {
            testMovable.setDestination(newPoint);
            fail("InvalidDataException NOT thrown from MovableImpl SetDestination(Point3D)"
                    +" with an invalid Point3D: " + newPoint);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" 
                    + newPoint.getX() + "," + newPoint.getY() + "," 
                    + newPoint.getZ() + ")", ex.getMessage());
            assertFalse(newPoint.equals(testMovable.getLocation()));
        }
        
        newPoint = null;
        try {
            testMovable.setDestination(newPoint);
            fail("InvalidDataException NOT thrown from MovableImpl SetDestination(Point3D) "
                    +" with a null Point3D");
        } catch (InvalidDataException ex) {
            assertEquals("Null Point3D sent to setDestination(Point3D)", ex.getMessage());
            assertNotNull(testMovable.getDestination());
        }
    }

    /**
     * Test of getSpeed method, of class MovableImpl.
     */
    @Test
    public void testGetSpeed() {
        assertEquals(testSpeed, testMovable.getSpeed(), 0.0);
    }

    /**
     * Test of setSpeed method, of class MovableImpl.
     */
    @Test
    public void testSetSpeed() throws Exception {
        double s = -1;        
        try {
            testMovable.setSpeed(s);
            fail("InvalidDataException NOT thrown from MovableImpl SetSpeed(s) "
                    +" with a negative speed value");
        } catch (InvalidDataException ex) {
            assertEquals("Negative speed sent to setSpeed:" + s, ex.getMessage());
            assertNotNull(testMovable.getSpeed());
        }
        
        s = 300;        
        try {
            testMovable.setSpeed(s);
            fail("InvalidDataException NOT thrown from MovableImpl SetSpeed(s) "
                    +" with a speed value larger than maximum speed");
        } catch (InvalidDataException ex) {
            assertEquals("Attempt to set speed (" + s + ") greater than maxSpeed (" + testMovable.getMaxSpeed() + ") in setSpeed", ex.getMessage());
            assertNotNull(testMovable.getSpeed());
        }
        
        s = 150;        
        try {
            testMovable.setSpeed(s);
            assertEquals(s, testMovable.getSpeed(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from MovableImpl "
                    + "setSpeed(s) with a valid speed: " + s);
        }
    }

    /**
     * Test of getMaxSpeed method, of class MovableImpl.
     */
    @Test
    public void testGetMaxSpeed() {
        assertEquals(testMaxSpeed, testMovable.getMaxSpeed(), 0.0);
    }

    /**
     * Test of setMaxSpeed method, of class MovableImpl.
     */
    @Test
    public void testSetMaxSpeed() throws Exception {
        double ms = -22;        
        try {
            testMovable.setMaxSpeed(ms);
            fail("InvalidDataException NOT thrown from MovableImpl SetMaxSpeed(ms) "
                    +" with a negative max speed value");
        } catch (InvalidDataException ex) {
            assertEquals("Negative maxSpeed sent to setMaxSpeed:" + ms, ex.getMessage());
            assertNotNull(testMovable.getMaxSpeed());
        }
        
        ms = 80;        
        try {
            testMovable.setMaxSpeed(ms);
            fail("InvalidDataException NOT thrown from MovableImpl SetMaxSpeed(s) "
                    +" with a max speed value smaller than speed");
        } catch (InvalidDataException ex) {
            assertEquals("Attempt to set maxSpeed less than speed in setMaxSpeed: " + ms, ex.getMessage());
            assertNotNull(testMovable.getMaxSpeed());
        }
        
        ms = 400;        
        try {
            testMovable.setMaxSpeed(ms);
            assertEquals(ms, testMovable.getMaxSpeed(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from MovableImpl "
                    + "setMaxSpeed(ms) with a valid maximum speed: " + ms);
        }
    }

    /**
     * Test of atDestination method, of class MovableImpl.
     */
    @Test
    public void testAtDestination() {                        
        try {
            Point3D newlocation = new Point3D(105.5, 106.6, 107.7);
            testMovable.setLocation(newlocation);
            assertEquals(false, testMovable.atDestination());            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException NOT thrown from MovableImpl AtDestination()");
        }
        
        try {
            Point3D newlocation = new Point3D(77.7, 88.8, 99.9);
            testMovable.setLocation(newlocation);
            assertEquals(true, testMovable.atDestination());
        } catch (InvalidDataException ex) {
            fail("InvalidDataException NOT thrown from MovableImpl AtDestination()");
        }
        
    }

    /**
     * Test of distance method, of class MovableImpl.
     */
    @Test
    public void testDistance_Point3D() throws Exception {
        Point3D location = new Point3D(99.9, 88.8, 77.7);
        
        Point3D destination = null;
        
        try {
            testMovable.setLocation(location);
            distance = testMovable.distance(destination);
            assertEquals(distance, testMovable.distance(destination), delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Null location sent to distance", ex.getMessage());
        }
        
        destination = new Point3D(11.1, 22.2, 33.3);
        try {
            testMovable.setLocation(location);
            double x= Math.pow(destination.getX()-location.getX(), 2);
            double y= Math.pow(destination.getY()-location.getY(), 2);
            double z= Math.pow(destination.getZ()-location.getZ(), 2);
            distance = Math.sqrt(x+y+z);
            assertEquals(distance, testMovable.distance(destination), delta);            
            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from MovableImpl "
                    + "distance(Point3D) with a valid Point3D: " + destination);
        }   
    }

    /**
     * Test of distance method, of class MovableImpl.
     */
    @Test
    public void testDistance_3args() throws Exception {
        Point3D location = new Point3D(99.9, 88.8, 77.7);
        double x= 11.1;
        double y= 22.2;
        double z= 33.3;        
        
        try {
            testMovable.setLocation(location);
            distance = testMovable.distance(-x,y,z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Invalid X,Y,Z point sent to distance(x,y,z)", ex.getMessage());
        }
        
        try {
            testMovable.setLocation(location);
            distance = testMovable.distance(x,-y,z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Invalid X,Y,Z point sent to distance(x,y,z)", ex.getMessage());
        }
        
        try {
            testMovable.setLocation(location);
            distance = testMovable.distance(x,y,-z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Invalid X,Y,Z point sent to distance(x,y,z)", ex.getMessage());
        }

        try {
            testMovable.setLocation(location);
            distance = testMovable.distance(x,y,z);
            x= Math.pow(x-location.getX(), 2);
            y= Math.pow(y-location.getY(), 2);
            z= Math.pow(z-location.getZ(), 2);
            expect = Math.sqrt(x+y+z);
            assertEquals(expect, distance, delta);            
            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from MovableImpl "
                    + "distance(double x, double y, double z) with a valid xyz.");
        }
    }

    /**
     * Test of getLocation method, of class MovableImpl.
     */
    @Test
    public void testGetLocation() {
        Point3D p = testMovable.getLocation();
        assertNotNull(p);
        assertEquals(p, testLoc);
        assertNotSame(p, testLoc);
    }

    /**
     * Test of getLocationX method, of class MovableImpl.
     */
    @Test
    public void testGetLocationX() {
        assertEquals(testLoc.getX(), testMovable.getLocationX(), delta);
    }

    /**
     * Test of getLocationY method, of class MovableImpl.
     */
    @Test
    public void testGetLocationY() {
        assertEquals(testLoc.getY(), testMovable.getLocationY(), delta);
    }

    /**
     * Test of getLocationZ method, of class MovableImpl.
     */
    @Test
    public void testGetLocationZ() {
        assertEquals(testLoc.getZ(), testMovable.getLocationZ(), delta);
    }

    /**
     * Test of setLocation method, of class MovableImpl.
     */
    @Test
    public void testSetLocation_Point3D() throws Exception {
        Point3D newPoint = new Point3D(99.9, 88.8, 77.7);
        try {
            testMovable.setLocation(newPoint);
            Point3D p = testMovable.getLocation();
            assertNotNull(p);
            assertEquals(p, newPoint);
            assertNotSame(p, newPoint);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from MovableImpl "
                    + "setLocation(Point3D) with a valid Point3D: " + newPoint);
        }
        
        newPoint = new Point3D(-55.5, 66.6, 77.7);
        try {
            testMovable.setLocation(newPoint);
            fail("InvalidDataException NOT thrown from MovableImpl setLocation(Point3D)"
                    +" with an invalid Point3D: " + newPoint);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertFalse(newPoint.equals(testMovable.getLocation()));
        }
        
        newPoint = null;
        try {
            testMovable.setLocation(newPoint);
            fail("InvalidDataException NOT thrown from MovableImpl setLocation(Point3D) "
                    +" with a null Point3D");
        } catch (InvalidDataException ex) {
            assertEquals("Null location sent to setLocation", ex.getMessage());
            assertNotNull(testMovable.getLocation());
        }
    }

    /**
     * Test of setLocation method, of class MovableImpl.
     */
    @Test
    public void testSetLocation_3args() throws Exception {
        double x = 99.9;
        double y = 88.8;
        double z = 77.7;
        try {
            testMovable.setLocation(x, y, z);
            assertEquals(x, testMovable.getLocationX(), delta);
            assertEquals(y, testMovable.getLocationY(), delta);
            assertEquals(z, testMovable.getLocationZ(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from MovableImpl"
                    +" setLocation(x,y,z) with a valid x, y, z: " + x + ", " + y + ", " + z);
        }
        
        x = -11.1;
        y = 22.2;
        z = 33.3;
        try {
            testMovable.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from MovableImpl setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertTrue(x != testMovable.getLocationX());
            assertTrue(y != testMovable.getLocationY());
            assertTrue(z != testMovable.getLocationZ());
        }
        x = 11.1;
        y = -22.2;
        z = 33.3;
        try {
            testMovable.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from MovableImpl setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertTrue(x != testMovable.getLocationX());
            assertTrue(y != testMovable.getLocationY());
            assertTrue(z != testMovable.getLocationZ());
        }
        x = 11.1;
        y = 22.2;
        z = -33.3;
        try {
            testMovable.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from MovableImpl setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertTrue(x != testMovable.getLocationX());
            assertTrue(y != testMovable.getLocationY());
            assertTrue(z != testMovable.getLocationZ());
        }
    }

    /**
     * Test of update method, of class MovableImpl.
     */
    @Test
    public void testUpdate() throws Exception {
        double millis = 0.0;
        testMovable.update(millis);
        Point3D p = testMovable.getLocation();
        assertNotNull(p);
        assertEquals(p, testLoc);
        assertFalse(testMovable.atDestination());
                
        millis = 200.0;
        testMovable.update(millis);
        Point3D p2 = testMovable.getLocation();
        assertNotNull(p2);
        assertFalse(testMovable.atDestination());
        
        millis = 100000000.0;
        testMovable.update(millis);
        Point3D p3 = testMovable.getLocation();
        assertNotNull(p3);
        assertEquals(p3, testDest);
        assertTrue(testMovable.atDestination());
        
        
        //invalid millis
        double x = 10.1;
        double y = 20.2;
        double z = 30.3;
        try {
            testMovable.setLocation(x, y, z);
            millis = -100.0; //Can not be negetive value;
            testMovable.update(millis);
            fail("InvalidDataException NOT thrown from update setLocation(Point3D) "
                    +" with a invalid millis: " + millis);
        } catch (InvalidDataException ex) {            
            assertEquals(x, testMovable.getLocationX(), delta);
            assertEquals(y, testMovable.getLocationY(), delta);
            assertEquals(z, testMovable.getLocationZ(), delta);             
        }
    }
    
}
