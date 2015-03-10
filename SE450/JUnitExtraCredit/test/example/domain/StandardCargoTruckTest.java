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
public class StandardCargoTruckTest {
    private Point3D testLoc;
    private Point3D testDest;
    private double testSpeed;
    private double testMaxSpeed;
    private double testMaxLoadWeight;
    private StandardCargoTruck testStandardCargoTruck;
    double distance = 0;
    double expect = 0;
    private static final double delta = 0.001;
    
    public StandardCargoTruckTest() {
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
            testStandardCargoTruck = new StandardCargoTruck(testLoc,testDest,testSpeed,testMaxSpeed,testMaxLoadWeight);
        } catch (InvalidDataException ex) {
            fail("Creation of test fixture object in @Before 'setUp' failed: " + ex.getMessage());
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of load method, of class StandardCargoTruck.
     */
    @Test
    public void testLoad() throws Exception {
        double amount = -100.0;
        try {
            testStandardCargoTruck.load(amount);
            fail("InvalidDataException NOT thrown from StandardCargoTruck load(amount) "
                    +" with a invalid amount: " + amount);
        } catch (InvalidDataException ex) {
            assertEquals("Negative load amount: " + amount, ex.getMessage());
        }
        
        amount = 200;
        try {
            testStandardCargoTruck.load(amount);
            assertEquals(amount, testStandardCargoTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck"
                    +" load(amount) with a valid amount: " + amount);
        }
        
        amount = 400;
        try {
            testStandardCargoTruck.load(amount);
            assertEquals(amount+200, testStandardCargoTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck"
                    +" load(amount) with a valid amount: " + amount);
        }
        
        amount = 500; //overflow
        try {
            testStandardCargoTruck.load(amount);
            fail("CannotFitException NOT thrown from StandardCargoTruck load(amount) "
                    +" with a invalid amount: " + amount);
        } catch (CannotFitException ex) {
            assertEquals("Additional load of " + amount + " will make the load weight "
                    + (testStandardCargoTruck.getCurrentLoadWeight() + amount) 
                    + " which exceeds the max load weight of " + testStandardCargoTruck.getMaxLoadWeight(), ex.getMessage());
        }  
    }

    /**
     * Test of unLoad method, of class StandardCargoTruck.
     */
    @Test
    public void testUnLoad() throws Exception {
         double amount = 1000.0;
        testStandardCargoTruck.load(amount);
        
        amount = -100.0;
        try {
            testStandardCargoTruck.unLoad(amount);
            fail("InvalidDataException NOT thrown from StandardCargoTruck unLoad(amount) "
                    +" with a invalid amount: " + amount);
        } catch (InvalidDataException ex) {
            assertEquals("Negative unLoad amount: " + amount, ex.getMessage());
        }
        
        amount = 200;
        try {
            testStandardCargoTruck.unLoad(amount);
            assertEquals(800, testStandardCargoTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck"
                    +" unLoad(amount) with a valid amount: " + amount);
        }
        
        amount = 400;
        try {
            testStandardCargoTruck.unLoad(amount);
            assertEquals(400, testStandardCargoTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck"
                    +" unLoad(amount) with a valid amount: " + amount);
        }
        
        amount = 500; //overflow
        try {
            testStandardCargoTruck.unLoad(amount);
            fail("CannotFitException NOT thrown from StandardCargoTruck unLoad(amount) "
                    +" with a invalid amount: " + amount);
        } catch (CannotFitException ex) {
            assertEquals("UnLoading " + amount + " will make the load weight negative: " 
                    + (testStandardCargoTruck.getCurrentLoadWeight() + amount), ex.getMessage());
        }
    }

    /**
     * Test of atDestination method, of class StandardCargoTruck.
     */
    @Test
    public void testAtDestination() {
       try {
            Point3D newlocation = new Point3D(105.5, 106.6, 107.7);
            testStandardCargoTruck.setLocation(newlocation);
            assertEquals(false, testStandardCargoTruck.atDestination());            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException NOT thrown from StandardCargoTruck AtDestination()");
        }
        
        try {
            Point3D newlocation = new Point3D(77.7, 88.8, 99.9);
            testStandardCargoTruck.setLocation(newlocation);
            assertEquals(true, testStandardCargoTruck.atDestination());
        } catch (InvalidDataException ex) {
            fail("InvalidDataException NOT thrown from StandardCargoTruck AtDestination()");
        }
    }

    /**
     * Test of getDestination method, of class StandardCargoTruck.
     */
    @Test
    public void testGetDestination() {
        Point3D p = testStandardCargoTruck.getDestination();
        assertNotNull(p);
        assertEquals(p, testDest);
        assertNotSame(p, testDest);
    }

    /**
     * Test of getDestinationX method, of class StandardCargoTruck.
     */
    @Test
    public void testGetDestinationX() {
        assertEquals(testDest.getX(), testStandardCargoTruck.getDestinationX(), delta);
    }

    /**
     * Test of getDestinationY method, of class StandardCargoTruck.
     */
    @Test
    public void testGetDestinationY() {
        assertEquals(testDest.getY(), testStandardCargoTruck.getDestinationY(), delta);
    }

    /**
     * Test of getDestinationZ method, of class StandardCargoTruck.
     */
    @Test
    public void testGetDestinationZ() {
        assertEquals(testDest.getZ(), testStandardCargoTruck.getDestinationZ(), delta);
    }

    /**
     * Test of getMaxSpeed method, of class StandardCargoTruck.
     */
    @Test
    public void testGetMaxSpeed() {
        assertEquals(testMaxSpeed, testStandardCargoTruck.getMaxSpeed(), 0.0);
    }

    /**
     * Test of getSpeed method, of class StandardCargoTruck.
     */
    @Test
    public void testGetSpeed() {
        assertEquals(testSpeed, testStandardCargoTruck.getSpeed(), 0.0);
    }

    /**
     * Test of setDestination method, of class StandardCargoTruck.
     */
    @Test
    public void testSetDestination_3args() throws Exception {
        double x = 99.9;
        double y = 88.8;
        double z = 77.7;
        try {
            testStandardCargoTruck.setDestination(x, y, z);
            assertEquals(x, testStandardCargoTruck.getDestinationX(), delta);
            assertEquals(y, testStandardCargoTruck.getDestinationY(), delta);
            assertEquals(z, testStandardCargoTruck.getDestinationZ(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck"
                    +" setDestination(x,y,z) with a valid x, y, z: " + x + ", " + y + ", " + z);
        }
        
        x = -11.1;
        y = 22.2;
        z = 33.3;
        try {
            testStandardCargoTruck.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from StandardCargoTruck setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")", ex.getMessage());
            assertTrue(x != testStandardCargoTruck.getDestinationX());
            assertTrue(y != testStandardCargoTruck.getDestinationY());
            assertTrue(z != testStandardCargoTruck.getDestinationZ());
        }
        
        x = 11.1;
        y = -22.2;
        z = 33.3;
        try {
            testStandardCargoTruck.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from StandardCargoTruck setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")", ex.getMessage());
            assertTrue(x != testStandardCargoTruck.getDestinationX());
            assertTrue(y != testStandardCargoTruck.getDestinationY());
            assertTrue(z != testStandardCargoTruck.getDestinationZ());
        }
        
        x = 11.1;
        y = 22.2;
        z = -33.3;
        try {
            testStandardCargoTruck.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from StandardCargoTruck setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")", ex.getMessage());
            assertTrue(x != testStandardCargoTruck.getDestinationX());
            assertTrue(y != testStandardCargoTruck.getDestinationY());
            assertTrue(z != testStandardCargoTruck.getDestinationZ());
        }
    }

    /**
     * Test of setDestination method, of class StandardCargoTruck.
     */
    @Test
    public void testSetDestination_Point3D() throws Exception {
        Point3D newPoint = new Point3D(101.1, 202.2, 303.3);
        try {
            testStandardCargoTruck.setDestination(newPoint);
            Point3D p = testStandardCargoTruck.getDestination();
            assertNotNull(p);
            assertEquals(p, newPoint);
            assertNotSame(p, newPoint);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck "
                    + "setDestination(Point3D) with a valid Point3D: " + newPoint);
        }
        
        newPoint = new Point3D(-505.5, 606.6, 707.7);
        try {
            testStandardCargoTruck.setDestination(newPoint);
            fail("InvalidDataException NOT thrown from StandardCargoTruck SetDestination(Point3D)"
                    +" with an invalid Point3D: " + newPoint);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" 
                    + newPoint.getX() + "," + newPoint.getY() + "," + newPoint.getZ() + ")", ex.getMessage());
            assertFalse(newPoint.equals(testStandardCargoTruck.getLocation()));
        }
        
        newPoint = null;
        try {
            testStandardCargoTruck.setDestination(newPoint);
            fail("InvalidDataException NOT thrown from StandardCargoTruck SetDestination(Point3D) "
                    +" with a null Point3D");
        } catch (InvalidDataException ex) {
            assertEquals("Null Point3D sent to setDestination(Point3D)", ex.getMessage());
            assertNotNull(testStandardCargoTruck.getDestination());
        }
    }

    /**
     * Test of setMaxSpeed method, of class StandardCargoTruck.
     */
    @Test
    public void testSetMaxSpeed() throws Exception {
        double ms = -22;        
        try {
            testStandardCargoTruck.setMaxSpeed(ms);
            fail("InvalidDataException NOT thrown from StandardCargoTruck SetMaxSpeed(ms) "
                    +" with a negative max speed value");
        } catch (InvalidDataException ex) {
            assertEquals("Negative maxSpeed sent to setMaxSpeed:" + ms, ex.getMessage());
            assertNotNull(testStandardCargoTruck.getMaxSpeed());
        }
        
        ms = 80;        
        try {
            testStandardCargoTruck.setMaxSpeed(ms);
            fail("InvalidDataException NOT thrown from StandardCargoTruck SetMaxSpeed(s) "
                    +" with a max speed value smaller than speed");
        } catch (InvalidDataException ex) {
            assertEquals("Attempt to set maxSpeed less than speed in setMaxSpeed: " + ms, ex.getMessage());
            assertNotNull(testStandardCargoTruck.getMaxSpeed());
        }
        
        ms = 400;        
        try {
            testStandardCargoTruck.setMaxSpeed(ms);
            assertEquals(ms, testStandardCargoTruck.getMaxSpeed(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck "
                    + "setMaxSpeed(ms) with a valid maximum speed: " + ms);
        }
    }

    /**
     * Test of setSpeed method, of class StandardCargoTruck.
     */
    @Test
    public void testSetSpeed() throws Exception {
        double s = -1;        
        try {
            testStandardCargoTruck.setSpeed(s);
            fail("InvalidDataException NOT thrown from StandardCargoTruck SetSpeed(s) "
                    +" with a negative speed value");
        } catch (InvalidDataException ex) {
            assertEquals("Negative speed sent to setSpeed:" + s, ex.getMessage());
            assertNotNull(testStandardCargoTruck.getSpeed());
        }
        
        s = 300;        
        try {
            testStandardCargoTruck.setSpeed(s);
            fail("InvalidDataException NOT thrown from StandardCargoTruck SetSpeed(s) "
                    +" with a speed value larger than maximum speed");
        } catch (InvalidDataException ex) {
            assertEquals("Attempt to set speed (" + s + ") greater than maxSpeed (" + testStandardCargoTruck.getMaxSpeed() + ") in setSpeed", ex.getMessage());
            assertNotNull(testStandardCargoTruck.getSpeed());
        }
        
        s = 150;        
        try {
            testStandardCargoTruck.setSpeed(s);
            assertEquals(s, testStandardCargoTruck.getSpeed(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck "
                    + "setSpeed(s) with a valid speed: " + s);
        }
    }

    /**
     * Test of distance method, of class StandardCargoTruck.
     */
    @Test
    public void testDistance_Point3D() throws Exception {
        Point3D location = new Point3D(99.9, 88.8, 77.7);
        
        Point3D destination = null;
        
        try {
            testStandardCargoTruck.setLocation(location);
            distance = testStandardCargoTruck.distance(destination);
            assertEquals(distance, testStandardCargoTruck.distance(destination), delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Null location sent to distance", ex.getMessage());
        }
        
        destination = new Point3D(11.1, 22.2, 33.3);
        try {
            testStandardCargoTruck.setLocation(location);
            double x= Math.pow(destination.getX()-location.getX(), 2);
            double y= Math.pow(destination.getY()-location.getY(), 2);
            double z= Math.pow(destination.getZ()-location.getZ(), 2);
            distance = Math.sqrt(x+y+z);
            assertEquals(distance, testStandardCargoTruck.distance(destination), delta);            
            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck "
                    + "distance(Point3D) with a valid Point3D: " + destination);
        }
    }

    /**
     * Test of distance method, of class StandardCargoTruck.
     */
    @Test
    public void testDistance_3args() throws Exception {
        Point3D location = new Point3D(99.9, 88.8, 77.7);
        double x= 11.1;
        double y= 22.2;
        double z= 33.3;        
        
        try {
            testStandardCargoTruck.setLocation(location);
            distance = testStandardCargoTruck.distance(-x,y,z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Invalid X,Y,Z point sent to distance(x,y,z)", ex.getMessage());
        }
        
        try {
            testStandardCargoTruck.setLocation(location);
            distance = testStandardCargoTruck.distance(x,-y,z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Invalid X,Y,Z point sent to distance(x,y,z)", ex.getMessage());
        }
        
        try {
            testStandardCargoTruck.setLocation(location);
            distance = testStandardCargoTruck.distance(x,y,-z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Invalid X,Y,Z point sent to distance(x,y,z)", ex.getMessage());
        }

        try {
            testStandardCargoTruck.setLocation(location);
            distance = testStandardCargoTruck.distance(x,y,z);
            x= Math.pow(x-location.getX(), 2);
            y= Math.pow(y-location.getY(), 2);
            z= Math.pow(z-location.getZ(), 2);
            expect = Math.sqrt(x+y+z);
            assertEquals(expect, distance, delta);            
            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck "
                    + "distance(double x, double y, double z) with a valid xyz.");
        }
    }

    /**
     * Test of getLocation method, of class StandardCargoTruck.
     */
    @Test
    public void testGetLocation() {
        Point3D p = testStandardCargoTruck.getLocation();
        assertNotNull(p);
        assertEquals(p, testLoc);
        assertNotSame(p, testLoc);
    }

    /**
     * Test of getLocationX method, of class StandardCargoTruck.
     */
    @Test
    public void testGetLocationX() {
        assertEquals(testLoc.getX(), testStandardCargoTruck.getLocationX(), delta);
    }

    /**
     * Test of getLocationY method, of class StandardCargoTruck.
     */
    @Test
    public void testGetLocationY() {
        assertEquals(testLoc.getY(), testStandardCargoTruck.getLocationY(), delta);
    }

    /**
     * Test of getLocationZ method, of class StandardCargoTruck.
     */
    @Test
    public void testGetLocationZ() {
        assertEquals(testLoc.getZ(), testStandardCargoTruck.getLocationZ(), delta);
    }

    /**
     * Test of setLocation method, of class StandardCargoTruck.
     */
    @Test
    public void testSetLocation_Point3D() throws Exception {
        Point3D newPoint = new Point3D(99.9, 88.8, 77.7);
        try {
            testStandardCargoTruck.setLocation(newPoint);
            Point3D p = testStandardCargoTruck.getLocation();
            assertNotNull(p);
            assertEquals(p, newPoint);
            assertNotSame(p, newPoint);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck "
                    + "setLocation(Point3D) with a valid Point3D: " + newPoint);
        }
        
        newPoint = new Point3D(-55.5, 66.6, 77.7);
        try {
            testStandardCargoTruck.setLocation(newPoint);
            fail("InvalidDataException NOT thrown from StandardCargoTruck setLocation(Point3D)"
                    +" with an invalid Point3D: " + newPoint);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertFalse(newPoint.equals(testStandardCargoTruck.getLocation()));
        }
        
        newPoint = null;
        try {
            testStandardCargoTruck.setLocation(newPoint);
            fail("InvalidDataException NOT thrown from StandardCargoTruck setLocation(Point3D) "
                    +" with a null Point3D");
        } catch (InvalidDataException ex) {
            assertEquals("Null location sent to setLocation", ex.getMessage());
            assertNotNull(testStandardCargoTruck.getLocation());
        }
    }

    /**
     * Test of setLocation method, of class StandardCargoTruck.
     */
    @Test
    public void testSetLocation_3args() throws Exception {
        double x = 99.9;
        double y = 88.8;
        double z = 77.7;
        try {
            testStandardCargoTruck.setLocation(x, y, z);
            assertEquals(x, testStandardCargoTruck.getLocationX(), delta);
            assertEquals(y, testStandardCargoTruck.getLocationY(), delta);
            assertEquals(z, testStandardCargoTruck.getLocationZ(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck"
                    +" setLocation(x,y,z) with a valid x, y, z: " + x + ", " + y + ", " + z);
        }
        
        x = -11.1;
        y = 22.2;
        z = 33.3;
        try {
            testStandardCargoTruck.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from StandardCargoTruck setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertTrue(x != testStandardCargoTruck.getLocationX());
            assertTrue(y != testStandardCargoTruck.getLocationY());
            assertTrue(z != testStandardCargoTruck.getLocationZ());
        }
        x = 11.1;
        y = -22.2;
        z = 33.3;
        try {
            testStandardCargoTruck.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from StandardCargoTruck setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertTrue(x != testStandardCargoTruck.getLocationX());
            assertTrue(y != testStandardCargoTruck.getLocationY());
            assertTrue(z != testStandardCargoTruck.getLocationZ());
        }
        x = 11.1;
        y = 22.2;
        z = -33.3;
        try {
            testStandardCargoTruck.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from StandardCargoTruck setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertTrue(x != testStandardCargoTruck.getLocationX());
            assertTrue(y != testStandardCargoTruck.getLocationY());
            assertTrue(z != testStandardCargoTruck.getLocationZ());
        }
    }

    /**
     * Test of getIdentifier method, of class StandardCargoTruck.
     */
    @Test
    public void testGetIdentifier() {
         try {
            testLoc = new Point3D(10.0, 11.1, 12.2);
            testDest = new Point3D(20.2, 30.3, 40.4);
            testSpeed = 200;
            testMaxSpeed = 300;
            testMaxLoadWeight = 4000;
            testStandardCargoTruck = new StandardCargoTruck(testLoc,testDest,testSpeed,testMaxSpeed,testMaxLoadWeight);
        } catch (InvalidDataException ex) {
            fail("Creation of object of StandardCargoTruck failed: " + ex.getMessage());
        }
        
        assertNotNull(testStandardCargoTruck);
        String id = testStandardCargoTruck.getIdentifier();
        assertEquals("ID", id.substring(0, 2)); 
    }

    /**
     * Test of getMaxLoadWeight method, of class StandardCargoTruck.
     */
    @Test
    public void testGetMaxLoadWeight() {
        assertNotNull(testStandardCargoTruck);
        assertEquals(testMaxLoadWeight, testStandardCargoTruck.getMaxLoadWeight(), 0.0);
    }

    /**
     * Test of setCurrentLoadWeight method, of class StandardCargoTruck.
     */
    @Test
    public void testSetCurrentLoadWeight() {
        double mlw = 1020.0;
        testStandardCargoTruck.setCurrentLoadWeight(mlw);
        assertEquals(mlw, testStandardCargoTruck.getCurrentLoadWeight(), 0.0);
    }

    /**
     * Test of getCurrentLoadWeight method, of class StandardCargoTruck.
     */
    @Test
    public void testGetCurrentLoadWeight() {
        double amountLoad = 300.0;
        try {
            testStandardCargoTruck.load(amountLoad);
            assertEquals(amountLoad, testStandardCargoTruck.getCurrentLoadWeight(), delta);           
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck"
                    +" load(amount) with a valid amount: " + amountLoad);
        } catch (CannotFitException ex) {
            fail("CannotFitException (" + ex.getMessage() + ") thrown from StandardCargoTruck"
                    +" load(amount) with a valid amount: " + amountLoad);
        }
        double amountUnload = 100;
        try {
            testStandardCargoTruck.unLoad(amountUnload);
            assertEquals(amountLoad - amountUnload, testStandardCargoTruck.getCurrentLoadWeight(), delta);           
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck"
                    +" unLoad(amount) with a valid amount: " + amountUnload);
        }
        catch (CannotFitException ex) {
            fail("CannotFitException (" + ex.getMessage() + ") thrown from StandardCargoTruck"
                    +" unLoad(amount) with a valid amount: " + amountUnload);
        }
        amountLoad = 800;
        try {
            testStandardCargoTruck.load(amountLoad);
            assertEquals(testStandardCargoTruck.getMaxLoadWeight(), testStandardCargoTruck.getCurrentLoadWeight(), delta);           
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck"
                    +" load(amount) with a valid amount: " + amountLoad);
        }
        catch (CannotFitException ex) {
            fail("CannotFitException (" + ex.getMessage() + ") thrown from StandardCargoTruck"
                    +" load(amount) with a valid amount: " + amountLoad);
        }
    }

    /**
     * Test of update method, of class StandardCargoTruck.
     */
    @Test
    public void testUpdate() throws Exception {
        double millis = 0.0;
        testStandardCargoTruck.update(millis);
        Point3D p = testStandardCargoTruck.getLocation();
        assertNotNull(p);
        assertEquals(p, testLoc);
        assertFalse(testStandardCargoTruck.atDestination());
                
        millis = 200.0;
        testStandardCargoTruck.update(millis);
        Point3D p2 = testStandardCargoTruck.getLocation();
        assertNotNull(p2);
        assertFalse(testStandardCargoTruck.atDestination());
        
        millis = 100000000.0;
        testStandardCargoTruck.update(millis);
        Point3D p3 = testStandardCargoTruck.getLocation();
        assertNotNull(p3);
        assertEquals(p3, testDest);
        assertTrue(testStandardCargoTruck.atDestination());
    }

    /**
     * Test of toString method, of class StandardCargoTruck.
     */
    @Test
    public void testToString() {
        try {
            Point3D newloc = new Point3D(44.4, 55.5, 66.6);
            testStandardCargoTruck.setLocation(newloc);            
            String result = testStandardCargoTruck.toString();
            assertTrue(result.contains("I am StandardCargoTruck"));
            assertTrue(result.contains("I'm not there yet"));
            assertFalse(result.contains("I am there"));
            
            newloc = new Point3D(77.7, 88.8, 99.9);
            testStandardCargoTruck.setLocation(newloc);            
            result = testStandardCargoTruck.toString();
            assertFalse(result.contains("I'm not there yet"));
            assertTrue(result.contains("I am there"));            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from StandardCargoTruck.");
        }        
    }
    
}
