/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.domain;

import example.common.CannotFitException;
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
public class TankerTruckTest {
    private Point3D testLoc;
    private Point3D testDest;
    private double testSpeed;
    private double testMaxSpeed;
    private double testMaxLoadWeight;
    private TankerTruck testTankerTruck;
    double distance = 0;
    double expect = 0;
    private static final double delta = 0.001;
    
    public TankerTruckTest() {
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
            testMaxLoadWeight = 1000;
            testTankerTruck = new TankerTruck(testLoc,testDest,testSpeed,testMaxSpeed,testMaxLoadWeight);
        } catch (InvalidDataException ex) {
            fail("Creation of test fixture object in @Before 'setUp' failed: " + ex.getMessage());
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of load method, of class TankerTruck.
     */
    @Test
    public void testLoad() throws Exception {
        double amount = -100.0;
        try {
            testTankerTruck.load(amount);
            fail("InvalidDataException NOT thrown from TankerTruck load(amount) "
                    +" with a invalid amount: " + amount);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Negative load amount: " + amount);
        }
        amount = 2001.0;
        try {
            testTankerTruck.load(amount);
            fail("InvalidDataException NOT thrown from TankerTruck load(amount) "
                    +" with a invalid amount: " + amount);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Loading " + amount + " at one time exceeds the TankerTruck load rate limit of 2000.0 at a time.");
        }
        
        amount = 200;
        try {
            testTankerTruck.load(amount);
            assertEquals(amount, testTankerTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck"
                    +" load(amount) with a valid amount: " + amount);
        }
        
        amount = 400;
        try {
            testTankerTruck.load(amount);
            assertEquals(amount+200, testTankerTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck"
                    +" load(amount) with a valid amount: " + amount);
        }
        
        amount = 500; //overflow
        try {
            testTankerTruck.load(amount);
            fail("CannotFitException NOT thrown from TankerTruck load(amount) "
                    +" with a invalid amount: " + amount);
        } catch (CannotFitException ex) {
            assertEquals(ex.getMessage(), "Additional load of " + amount + " will make the load weight "
                    + (testTankerTruck.getCurrentLoadWeight() + amount) + " which exceeds the max load weight of " + testTankerTruck.getMaxLoadWeight());
        }
    }

    /**
     * Test of unLoad method, of class TankerTruck.
     */
    @Test
    public void testUnLoad() throws Exception {
        double amount = 1000.0;
        testTankerTruck.load(amount);
        
        amount = -100.0;
        try {
            testTankerTruck.unLoad(amount);
            fail("InvalidDataException NOT thrown from TankerTruck unLoad(amount) "
                    +" with a invalid amount: " + amount);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Negative unLoad amount: " + amount);
        }
        
        amount = 200;
        try {
            testTankerTruck.unLoad(amount);
            assertEquals(800, testTankerTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck"
                    +" unLoad(amount) with a valid amount: " + amount);
        }
        
        amount = 400;
        try {
            testTankerTruck.unLoad(amount);
            assertEquals(400, testTankerTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck"
                    +" unLoad(amount) with a valid amount: " + amount);
        }
        
        amount = 500; //overflow
        try {
            testTankerTruck.unLoad(amount);
            fail("CannotFitException NOT thrown from TankerTruck unLoad(amount) "
                    +" with a invalid amount: " + amount);
        } catch (CannotFitException ex) {
            assertEquals(ex.getMessage(), "UnLoading " + amount + " will make the load weight negative: " 
                    + (testTankerTruck.getCurrentLoadWeight() + amount));
        }
    }

    /**
     * Test of atDestination method, of class TankerTruck.
     */
    @Test
    public void testAtDestination() {
        try {
            Point3D newlocation = new Point3D(105.5, 106.6, 107.7);
            testTankerTruck.setLocation(newlocation);
            assertEquals(false, testTankerTruck.atDestination());            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException NOT thrown from TankerTruck AtDestination()");
        }
        
        try {
            Point3D newlocation = new Point3D(77.7, 88.8, 99.9);
            testTankerTruck.setLocation(newlocation);
            assertEquals(true, testTankerTruck.atDestination());
        } catch (InvalidDataException ex) {
            fail("InvalidDataException NOT thrown from TankerTruck AtDestination()");
        }  
    }

    /**
     * Test of getDestination method, of class TankerTruck.
     */
    @Test
    public void testGetDestination() {
         Point3D p = testTankerTruck.getDestination();
        assertNotNull(p);
        assertEquals(p, testDest);
        assertNotSame(p, testDest);
    }

    /**
     * Test of getDestinationX method, of class TankerTruck.
     */
    @Test
    public void testGetDestinationX() {
        assertEquals(testTankerTruck.getDestinationX(), testDest.getX(), delta);
    }

    /**
     * Test of getDestinationY method, of class TankerTruck.
     */
    @Test
    public void testGetDestinationY() {
        assertEquals(testTankerTruck.getDestinationY(), testDest.getY(), delta);
    }

    /**
     * Test of getDestinationZ method, of class TankerTruck.
     */
    @Test
    public void testGetDestinationZ() {
        assertEquals(testTankerTruck.getDestinationZ(), testDest.getZ(), delta);
    }

    /**
     * Test of getMaxSpeed method, of class TankerTruck.
     */
    @Test
    public void testGetMaxSpeed() {
        assertEquals(testMaxSpeed, testTankerTruck.getMaxSpeed(), 0.0);
    }

    /**
     * Test of getSpeed method, of class TankerTruck.
     */
    @Test
    public void testGetSpeed() {
        assertEquals(testSpeed, testTankerTruck.getSpeed(), 0.0);
    }

    /**
     * Test of setDestination method, of class TankerTruck.
     */
    @Test
    public void testSetDestination_3args() throws Exception {
        double x = 99.9;
        double y = 88.8;
        double z = 77.7;
        try {
            testTankerTruck.setDestination(x, y, z);
            assertEquals(x, testTankerTruck.getDestinationX(), delta);
            assertEquals(y, testTankerTruck.getDestinationY(), delta);
            assertEquals(z, testTankerTruck.getDestinationZ(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck"
                    +" setDestination(x,y,z) with a valid x, y, z: " + x + ", " + y + ", " + z);
        }
        
        x = -11.1;
        y = 22.2;
        z = 33.3;
        try {
            testTankerTruck.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from TankerTruck setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")");
            assertTrue(x != testTankerTruck.getDestinationX());
            assertTrue(y != testTankerTruck.getDestinationY());
            assertTrue(z != testTankerTruck.getDestinationZ());
        }
        
        x = 11.1;
        y = -22.2;
        z = 33.3;
        try {
            testTankerTruck.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from TankerTruck setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")");
            assertTrue(x != testTankerTruck.getDestinationX());
            assertTrue(y != testTankerTruck.getDestinationY());
            assertTrue(z != testTankerTruck.getDestinationZ());
        }
        
        x = 11.1;
        y = 22.2;
        z = -33.3;
        try {
            testTankerTruck.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from TankerTruck setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")");
            assertTrue(x != testTankerTruck.getDestinationX());
            assertTrue(y != testTankerTruck.getDestinationY());
            assertTrue(z != testTankerTruck.getDestinationZ());
        }
    }

    /**
     * Test of setDestination method, of class TankerTruck.
     */
    @Test
    public void testSetDestination_Point3D() throws Exception {
        Point3D newPoint = new Point3D(101.1, 202.2, 303.3);
        try {
            testTankerTruck.setDestination(newPoint);
            Point3D p = testTankerTruck.getDestination();
            assertNotNull(p);
            assertEquals(p, newPoint);
            assertNotSame(p, newPoint);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck "
                    + "setDestination(Point3D) with a valid Point3D: " + newPoint);
        }
        
        newPoint = new Point3D(-505.5, 606.6, 707.7);
        try {
            testTankerTruck.setDestination(newPoint);
            fail("InvalidDataException NOT thrown from TankerTruck SetDestination(Point3D)"
                    +" with an invalid Point3D: " + newPoint);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to setDestination(x,y,z): (" 
                    + newPoint.getX() + "," + newPoint.getY() + "," + newPoint.getZ() + ")");
            assertFalse(newPoint.equals(testTankerTruck.getLocation()));
        }
        
        newPoint = null;
        try {
            testTankerTruck.setDestination(newPoint);
            fail("InvalidDataException NOT thrown from TankerTruck SetDestination(Point3D) "
                    +" with a null Point3D");
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Null Point3D sent to setDestination(Point3D)");
            assertNotNull(testTankerTruck.getDestination());
        }
    }

    /**
     * Test of setMaxSpeed method, of class TankerTruck.
     */
    @Test
    public void testSetMaxSpeed() throws Exception {
        double ms = -22;        
        try {
            testTankerTruck.setMaxSpeed(ms);
            fail("InvalidDataException NOT thrown from TankerTruck SetMaxSpeed(ms) "
                    +" with a negative max speed value");
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Negative maxSpeed sent to setMaxSpeed:" + ms);
            assertNotNull(testTankerTruck.getMaxSpeed());
        }
        
        ms = 80;        
        try {
            testTankerTruck.setMaxSpeed(ms);
            fail("InvalidDataException NOT thrown from TankerTruck SetMaxSpeed(s) "
                    +" with a max speed value smaller than speed");
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Attempt to set maxSpeed less than speed in setMaxSpeed: " + ms);
            assertNotNull(testTankerTruck.getMaxSpeed());
        }
        
        ms = 400;        
        try {
            testTankerTruck.setMaxSpeed(ms);
            assertEquals(ms, testTankerTruck.getMaxSpeed(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck "
                    + "setMaxSpeed(ms) with a valid maximum speed: " + ms);
        }
    }

    /**
     * Test of setSpeed method, of class TankerTruck.
     */
    @Test
    public void testSetSpeed() throws Exception {
        double s = -1;        
        try {
            testTankerTruck.setSpeed(s);
            fail("InvalidDataException NOT thrown from TankerTruck SetSpeed(s) "
                    +" with a negative speed value");
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Negative speed sent to setSpeed:" + s);
            assertNotNull(testTankerTruck.getSpeed());
        }
        
        s = 300;        
        try {
            testTankerTruck.setSpeed(s);
            fail("InvalidDataException NOT thrown from TankerTruck SetSpeed(s) "
                    +" with a speed value larger than maximum speed");
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Attempt to set speed (" + s + ") greater than maxSpeed (" + testTankerTruck.getMaxSpeed() + ") in setSpeed");
            assertNotNull(testTankerTruck.getSpeed());
        }
        
        s = 150;        
        try {
            testTankerTruck.setSpeed(s);
            assertEquals(s, testTankerTruck.getSpeed(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck "
                    + "setSpeed(s) with a valid speed: " + s);
        }
    }

    /**
     * Test of distance method, of class TankerTruck.
     */
    @Test
    public void testDistance_Point3D() throws Exception {
        Point3D location = new Point3D(99.9, 88.8, 77.7);
        
        Point3D destination = null;
        
        try {
            testTankerTruck.setLocation(location);
            distance = testTankerTruck.distance(destination);
            assertEquals(distance, testTankerTruck.distance(destination), delta);            
        } catch (InvalidDataException ex) {
           assertEquals(ex.getMessage(), "Null location sent to distance");
        }
        
        destination = new Point3D(11.1, 22.2, 33.3);
        try {
            testTankerTruck.setLocation(location);
            double x= Math.pow(destination.getX()-location.getX(), 2);
            double y= Math.pow(destination.getY()-location.getY(), 2);
            double z= Math.pow(destination.getZ()-location.getZ(), 2);
            distance = Math.sqrt(x+y+z);
            assertEquals(distance, testTankerTruck.distance(destination), delta);            
            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck "
                    + "distance(Point3D) with a valid Point3D: " + destination);
        }
    }

    /**
     * Test of distance method, of class TankerTruck.
     */
    @Test
    public void testDistance_3args() throws Exception {
        Point3D location = new Point3D(99.9, 88.8, 77.7);
        double x= 11.1;
        double y= 22.2;
        double z= 33.3;        
        
        try {
            testTankerTruck.setLocation(location);
            distance = testTankerTruck.distance(-x,y,z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to distance(x,y,z)");
        }
        
        try {
            testTankerTruck.setLocation(location);
            distance = testTankerTruck.distance(x,-y,z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to distance(x,y,z)");
        }
        
        try {
            testTankerTruck.setLocation(location);
            distance = testTankerTruck.distance(x,y,-z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to distance(x,y,z)");
        }

        try {
            testTankerTruck.setLocation(location);
            distance = testTankerTruck.distance(x,y,z);
            x= Math.pow(x-location.getX(), 2);
            y= Math.pow(y-location.getY(), 2);
            z= Math.pow(z-location.getZ(), 2);
            expect = Math.sqrt(x+y+z);
            assertEquals(expect, distance, delta);            
            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck "
                    + "distance(double x, double y, double z) with a valid xyz.");
        }
    }

    /**
     * Test of getLocation method, of class TankerTruck.
     */
    @Test
    public void testGetLocation() {
        Point3D p = testTankerTruck.getLocation();
        assertNotNull(p);
        assertEquals(p, testLoc);
        assertNotSame(p, testLoc);
    }

    /**
     * Test of getLocationX method, of class TankerTruck.
     */
    @Test
    public void testGetLocationX() {
        assertEquals(testTankerTruck.getLocationX(), testLoc.getX(), delta);
    }

    /**
     * Test of getLocationY method, of class TankerTruck.
     */
    @Test
    public void testGetLocationY() {
        assertEquals(testTankerTruck.getLocationY(), testLoc.getY(), delta);
    }

    /**
     * Test of getLocationZ method, of class TankerTruck.
     */
    @Test
    public void testGetLocationZ() {
       assertEquals(testTankerTruck.getLocationZ(), testLoc.getZ(), delta);
    }

    /**
     * Test of setLocation method, of class TankerTruck.
     */
    @Test
    public void testSetLocation_Point3D() throws Exception {
        Point3D newPoint = new Point3D(99.9, 88.8, 77.7);
        try {
            testTankerTruck.setLocation(newPoint);
            Point3D p = testTankerTruck.getLocation();
            assertNotNull(p);
            assertEquals(p, newPoint);
            assertNotSame(p, newPoint);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck "
                    + "setLocation(Point3D) with a valid Point3D: " + newPoint);
        }
        
        newPoint = new Point3D(-55.5, 66.6, 77.7);
        try {
            testTankerTruck.setLocation(newPoint);
            fail("InvalidDataException NOT thrown from TankerTruck setLocation(Point3D)"
                    +" with an invalid Point3D: " + newPoint);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to setLocation(x,y,z)");
            assertFalse(newPoint.equals(testTankerTruck.getLocation()));
        }
        
        newPoint = null;
        try {
            testTankerTruck.setLocation(newPoint);
            fail("InvalidDataException NOT thrown from TankerTruck setLocation(Point3D) "
                    +" with a null Point3D");
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Null location sent to setLocation");
            assertNotNull(testTankerTruck.getLocation());
        }
    }

    /**
     * Test of setLocation method, of class TankerTruck.
     */
    @Test
    public void testSetLocation_3args() throws Exception {
        double x = 99.9;
        double y = 88.8;
        double z = 77.7;
        try {
            testTankerTruck.setLocation(x, y, z);
            assertEquals(x, testTankerTruck.getLocationX(), delta);
            assertEquals(y, testTankerTruck.getLocationY(), delta);
            assertEquals(z, testTankerTruck.getLocationZ(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck"
                    +" setLocation(x,y,z) with a valid x, y, z: " + x + ", " + y + ", " + z);
        }
        
        x = -11.1;
        y = 22.2;
        z = 33.3;
        try {
            testTankerTruck.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from TankerTruck setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to setLocation(x,y,z)");
            assertTrue(x != testTankerTruck.getLocationX());
            assertTrue(y != testTankerTruck.getLocationY());
            assertTrue(z != testTankerTruck.getLocationZ());
        }
        x = 11.1;
        y = -22.2;
        z = 33.3;
        try {
            testTankerTruck.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from TankerTruck setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to setLocation(x,y,z)");
            assertTrue(x != testTankerTruck.getLocationX());
            assertTrue(y != testTankerTruck.getLocationY());
            assertTrue(z != testTankerTruck.getLocationZ());
        }
        x = 11.1;
        y = 22.2;
        z = -33.3;
        try {
            testTankerTruck.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from TankerTruck setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Invalid X,Y,Z point sent to setLocation(x,y,z)");
            assertTrue(x != testTankerTruck.getLocationX());
            assertTrue(y != testTankerTruck.getLocationY());
            assertTrue(z != testTankerTruck.getLocationZ());
        }
    }

    /**
     * Test of getIdentifier method, of class TankerTruck.
     */
    @Test
    public void testGetIdentifier() {
        try {
            testLoc = new Point3D(10.0, 11.1, 12.2);
            testDest = new Point3D(20.2, 30.3, 40.4);
            testSpeed = 200;
            testMaxSpeed = 300;
            testMaxLoadWeight = 4000;
            testTankerTruck = new TankerTruck(testLoc,testDest,testSpeed,testMaxSpeed,testMaxLoadWeight);
        } catch (InvalidDataException ex) {
            fail("Creation of object of TankerTruck failed: " + ex.getMessage());
        }
        
        assertNotNull(testTankerTruck);
        //assertEquals("ID1", testTankerTruck.getIdentifier());
    }

    /**
     * Test of getMaxLoadWeight method, of class TankerTruck.
     */
    @Test
    public void testGetMaxLoadWeight() {
        assertNotNull(testTankerTruck);
        assertEquals(testMaxLoadWeight, testTankerTruck.getMaxLoadWeight(), 0.0);
    }

    /**
     * Test of setCurrentLoadWeight method, of class TankerTruck.
     */
    @Test
    public void testSetCurrentLoadWeight() {
        double mlw = 1020.0;
        testTankerTruck.setCurrentLoadWeight(mlw);
        assertEquals(mlw, testTankerTruck.getCurrentLoadWeight(), 0.0);
    }

    /**
     * Test of getCurrentLoadWeight method, of class TankerTruck.
     */
    @Test
    public void testGetCurrentLoadWeight() {
        double amountLoad = 300.0;
        try {
            testTankerTruck.load(amountLoad);
            assertEquals(amountLoad, testTankerTruck.getCurrentLoadWeight(), delta);           
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck"
                    +" load(amount) with a valid amount: " + amountLoad);
        } catch (CannotFitException ex) {
            fail("CannotFitException (" + ex.getMessage() + ") thrown from TankerTruck"
                    +" load(amount) with a valid amount: " + amountLoad);
        }
        double amountUnload = 100;
        try {
            testTankerTruck.unLoad(amountUnload);
            assertEquals(amountLoad - amountUnload, testTankerTruck.getCurrentLoadWeight(), delta);           
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck"
                    +" unLoad(amount) with a valid amount: " + amountUnload);
        }catch (CannotFitException ex) {
            fail("CannotFitException (" + ex.getMessage() + ") thrown from TankerTruck"
                    +" unLoad(amount) with a valid amount: " + amountUnload);
        }
        amountLoad = 800;
        try {
            testTankerTruck.load(amountLoad);
            assertEquals(testTankerTruck.getMaxLoadWeight(), testTankerTruck.getCurrentLoadWeight(), delta);           
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck"
                    +" load(amount) with a valid amount: " + amountLoad);
        }catch (CannotFitException ex) {
            fail("CannotFitException (" + ex.getMessage() + ") thrown from TankerTruck"
                    +" load(amount) with a valid amount: " + amountLoad);
        }
    }

    /**
     * Test of update method, of class TankerTruck.
     */
    @Test
    public void testUpdate() throws Exception {
        double millis = 0.0;
        testTankerTruck.update(millis);
        Point3D p = testTankerTruck.getLocation();
        assertNotNull(p);
        assertEquals(p, testLoc);
        assertFalse(testTankerTruck.atDestination());
                
        millis = 200.0;
        testTankerTruck.update(millis);
        Point3D p2 = testTankerTruck.getLocation();
        assertNotNull(p2);
        assertFalse(testTankerTruck.atDestination());
        
        millis = 100000000.0;
        testTankerTruck.update(millis);
        Point3D p3 = testTankerTruck.getLocation();
        assertNotNull(p3);
        assertEquals(p3, testDest);
        assertTrue(testTankerTruck.atDestination());
    }

    /**
     * Test of toString method, of class TankerTruck.
     */
    @Test
    public void testToString() {
        try {
            String expResult = "I am TankerTruck " + testTankerTruck.getIdentifier() + ".\n\tI am at "
                    + testTankerTruck.getLocation() + " and am heading to " + testTankerTruck.getDestination()
                    + ".\n\tMy load is " + testTankerTruck.getCurrentLoadWeight() + " and my max load is "
                    + testTankerTruck.getMaxLoadWeight() + ".\n\tDistance to my destination is "
                    + String.format("%4.2f", testTankerTruck.distance(testTankerTruck.getDestination())) + ". "
                    + (testTankerTruck.atDestination() ? "I am there!" : "I'm not there yet");
            String result = testTankerTruck.toString();
            assertEquals(expResult, result);      
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TankerTruck.");
        }
    }
    
}
