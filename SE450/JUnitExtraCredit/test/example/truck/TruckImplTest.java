/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.truck;

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
public class TruckImplTest {
    private Point3D testLoc;
    private Point3D testDest;
    private double testSpeed;
    private double testMaxSpeed;
    private double testMaxLoadWeight;
    private TruckImpl testTruck;
    double distance = 0;
    double expect = 0;
    private static final double delta = 0.001;
    
    public TruckImplTest() {
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
            testTruck = new TruckImpl(testLoc,testDest,testSpeed,testMaxSpeed,testMaxLoadWeight);
        } catch (InvalidDataException ex) {
            fail("Creation of test fixture object in @Before 'setUp' failed: " + ex.getMessage());
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of load method, of class TruckImpl.
     */
    @Test
    public void testLoad() throws Exception {
        double amount = -100.0;
        try {
            testTruck.load(amount);
            fail("InvalidDataException NOT thrown from TruckImpl load(amount) "
                    +" with a invalid amount: " + amount);
        } catch (InvalidDataException ex) {
            assertEquals("Negative load amount: " + amount, ex.getMessage());
        }
        
        amount = 200;
        try {
            testTruck.load(amount);
            assertEquals(amount, testTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl"
                    +" load(amount) with a valid amount: " + amount);
        }
        
        amount = 400;
        try {
            testTruck.load(amount);
            assertEquals(amount+200, testTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl"
                    +" load(amount) with a valid amount: " + amount);
        }
        
        amount = 500; //overflow
        try {
            testTruck.load(amount);
            fail("CannotFitException NOT thrown from TruckImpl load(amount) "
                    +" with a invalid amount: " + amount);
        } catch (CannotFitException ex) {
            assertEquals("Additional load of " + amount + " will make the load weight "
                    + (testTruck.getCurrentLoadWeight() + amount) + " which exceeds the max load weight of " 
                    + testTruck.getMaxLoadWeight(), ex.getMessage());
        }        
    }

    /**
     * Test of unLoad method, of class TruckImpl.
     */
    @Test
    public void testUnLoad() throws Exception {
        double amount = 1000.0;
        testTruck.load(amount);
        
        amount = -100.0;
        try {
            testTruck.unLoad(amount);
            fail("InvalidDataException NOT thrown from TruckImpl unLoad(amount) "
                    +" with a invalid amount: " + amount);
        } catch (InvalidDataException ex) {
            assertEquals("Negative unLoad amount: " + amount, ex.getMessage());
        }
        
        amount = 200;
        try {
            testTruck.unLoad(amount);
            assertEquals(800, testTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl"
                    +" unLoad(amount) with a valid amount: " + amount);
        }
        
        amount = 400;
        try {
            testTruck.unLoad(amount);
            assertEquals(400, testTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl"
                    +" unLoad(amount) with a valid amount: " + amount);
        }
        
        amount = 500; //overflow
        try {
            testTruck.unLoad(amount);
            fail("CannotFitException NOT thrown from TruckImpl unLoad(amount) "
                    +" with a invalid amount: " + amount);
        } catch (CannotFitException ex) {
            assertEquals("UnLoading " + amount + " will make the load weight negative: " 
                    + (testTruck.getCurrentLoadWeight() - amount), ex.getMessage());
        }
    }

    /**
     * Test of atDestination method, of class TruckImpl.
     */
    @Test
    public void testAtDestination() {
        try {
            Point3D newlocation = new Point3D(105.5, 106.6, 107.7);
            testTruck.setLocation(newlocation);
            assertEquals(false, testTruck.atDestination());            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException NOT thrown from TruckImpl AtDestination()");
        }
        
        try {
            Point3D newlocation = new Point3D(77.7, 88.8, 99.9);
            testTruck.setLocation(newlocation);
            assertEquals(true, testTruck.atDestination());
        } catch (InvalidDataException ex) {
            fail("InvalidDataException NOT thrown from TruckImpl AtDestination()");
        }        
    }

    /**
     * Test of getDestination method, of class TruckImpl.
     */
    @Test
    public void testGetDestination() {
        Point3D p = testTruck.getDestination();
        assertNotNull(p);
        assertEquals(p, testDest);
        assertNotSame(p, testDest);         
    }

    /**
     * Test of getDestinationX method, of class TruckImpl.
     */
    @Test
    public void testGetDestinationX() {
        assertEquals(testDest.getX(), testTruck.getDestinationX(), delta);
    }

    /**
     * Test of getDestinationY method, of class TruckImpl.
     */
    @Test
    public void testGetDestinationY() {
        assertEquals(testDest.getY(), testTruck.getDestinationY(), delta);
    }

    /**
     * Test of getDestinationZ method, of class TruckImpl.
     */
    @Test
    public void testGetDestinationZ() {
        assertEquals(testDest.getZ(), testTruck.getDestinationZ(), delta);
    }

    /**
     * Test of getMaxSpeed method, of class TruckImpl.
     */
    @Test
    public void testGetMaxSpeed() {
        assertEquals(testMaxSpeed, testTruck.getMaxSpeed(), 0.0);
    }

    /**
     * Test of getSpeed method, of class TruckImpl.
     */
    @Test
    public void testGetSpeed() {
        assertEquals(testSpeed, testTruck.getSpeed(), 0.0);
    }

    /**
     * Test of setDestination method, of class TruckImpl.
     */
    @Test
    public void testSetDestination_3args() throws Exception {
        double x = 99.9;
        double y = 88.8;
        double z = 77.7;
        try {
            testTruck.setDestination(x, y, z);
            assertEquals(x, testTruck.getDestinationX(), delta);
            assertEquals(y, testTruck.getDestinationY(), delta);
            assertEquals(z, testTruck.getDestinationZ(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl"
                    +" setDestination(x,y,z) with a valid x, y, z: " + x + ", " + y + ", " + z);
        }
        
        x = -11.1;
        y = 22.2;
        z = 33.3;
        try {
            testTruck.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from TruckImpl setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")", ex.getMessage());
            assertTrue(x != testTruck.getDestinationX());
            assertTrue(y != testTruck.getDestinationY());
            assertTrue(z != testTruck.getDestinationZ());
        }
        
        x = 11.1;
        y = -22.2;
        z = 33.3;
        try {
            testTruck.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from TruckImpl setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")", ex.getMessage());
            assertTrue(x != testTruck.getDestinationX());
            assertTrue(y != testTruck.getDestinationY());
            assertTrue(z != testTruck.getDestinationZ());
        }
        
        x = 11.1;
        y = 22.2;
        z = -33.3;
        try {
            testTruck.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from TruckImpl setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")", ex.getMessage());
            assertTrue(x != testTruck.getDestinationX());
            assertTrue(y != testTruck.getDestinationY());
            assertTrue(z != testTruck.getDestinationZ());
        }
    }

    /**
     * Test of setDestination method, of class TruckImpl.
     */
    @Test
    public void testSetDestination_Point3D() throws Exception {
        Point3D newPoint = new Point3D(101.1, 202.2, 303.3);
        try {
            testTruck.setDestination(newPoint);
            Point3D p = testTruck.getDestination();
            assertNotNull(p);
            assertEquals(p, newPoint);
            assertNotSame(p, newPoint);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl "
                    + "setDestination(Point3D) with a valid Point3D: " + newPoint);
        }
        
        newPoint = new Point3D(-505.5, 606.6, 707.7);
        try {
            testTruck.setDestination(newPoint);
            fail("InvalidDataException NOT thrown from TruckImpl SetDestination(Point3D)"
                    +" with an invalid Point3D: " + newPoint);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + newPoint.getX() + "," + newPoint.getY() + "," + newPoint.getZ() + ")", ex.getMessage());
            assertFalse(newPoint.equals(testTruck.getLocation()));
        }
        
        newPoint = null;
        try {
            testTruck.setDestination(newPoint);
            fail("InvalidDataException NOT thrown from TruckImpl SetDestination(Point3D) "
                    +" with a null Point3D");
        } catch (InvalidDataException ex) {
            assertEquals("Null Point3D sent to setDestination(Point3D)", ex.getMessage());
            assertNotNull(testTruck.getDestination());
        }
    }

    /**
     * Test of setMaxSpeed method, of class TruckImpl.
     */
    @Test
    public void testSetMaxSpeed() throws Exception {
        double ms = -22;        
        try {
            testTruck.setMaxSpeed(ms);
            fail("InvalidDataException NOT thrown from TruckImpl SetMaxSpeed(ms) "
                    +" with a negative max speed value");
        } catch (InvalidDataException ex) {
            assertEquals("Negative maxSpeed sent to setMaxSpeed:" + ms, ex.getMessage());
            assertNotNull(testTruck.getMaxSpeed());
        }
        
        ms = 80;        
        try {
            testTruck.setMaxSpeed(ms);
            fail("InvalidDataException NOT thrown from TruckImpl SetMaxSpeed(s) "
                    +" with a max speed value smaller than speed");
        } catch (InvalidDataException ex) {
            assertEquals("Attempt to set maxSpeed less than speed in setMaxSpeed: " + ms, ex.getMessage());
            assertNotNull(testTruck.getMaxSpeed());
        }
        
        ms = 400;        
        try {
            testTruck.setMaxSpeed(ms);
            assertEquals(ms, testTruck.getMaxSpeed(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl "
                    + "setMaxSpeed(ms) with a valid maximum speed: " + ms);
        }
    }

    /**
     * Test of setSpeed method, of class TruckImpl.
     */
    @Test
    public void testSetSpeed() throws Exception {
        double s = -1;        
        try {
            testTruck.setSpeed(s);
            fail("InvalidDataException NOT thrown from TruckImpl SetSpeed(s) "
                    +" with a negative speed value");
        } catch (InvalidDataException ex) {
            assertEquals("Negative speed sent to setSpeed:" + s, ex.getMessage());
            assertNotNull(testTruck.getSpeed());
        }
        
        s = 300;        
        try {
            testTruck.setSpeed(s);
            fail("InvalidDataException NOT thrown from TruckImpl SetSpeed(s) "
                    +" with a speed value larger than maximum speed");
        } catch (InvalidDataException ex) {
            assertEquals("Attempt to set speed (" + s + ") greater than maxSpeed (" + testTruck.getMaxSpeed() + ") in setSpeed", ex.getMessage());
            assertNotNull(testTruck.getSpeed());
        }
        
        s = 150;        
        try {
            testTruck.setSpeed(s);
            assertEquals(s, testTruck.getSpeed(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl "
                    + "setSpeed(s) with a valid speed: " + s);
        }
    }

    /**
     * Test of distance method, of class TruckImpl.
     */
    @Test
    public void testDistance_Point3D() throws Exception {
        Point3D location = new Point3D(99.9, 88.8, 77.7);
        
        Point3D destination = null;
        
        try {
            testTruck.setLocation(location);
            distance = testTruck.distance(destination);
            assertEquals(distance, testTruck.distance(destination), delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Null location sent to distance", ex.getMessage());
        }
        
        destination = new Point3D(11.1, 22.2, 33.3);
        try {
            testTruck.setLocation(location);
            double x= Math.pow(destination.getX()-location.getX(), 2);
            double y= Math.pow(destination.getY()-location.getY(), 2);
            double z= Math.pow(destination.getZ()-location.getZ(), 2);
            distance = Math.sqrt(x+y+z);
            assertEquals(distance, testTruck.distance(destination), delta);            
            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl "
                    + "distance(Point3D) with a valid Point3D: " + destination);
        }
    }

    /**
     * Test of distance method, of class TruckImpl.
     */
    @Test
    public void testDistance_3args() throws Exception {
        Point3D location = new Point3D(99.9, 88.8, 77.7);
        double x= 11.1;
        double y= 22.2;
        double z= 33.3;        
        
        try {
            testTruck.setLocation(location);
            distance = testTruck.distance(-x,y,z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Invalid X,Y,Z point sent to distance(x,y,z)", ex.getMessage());
        }
        
        try {
            testTruck.setLocation(location);
            distance = testTruck.distance(x,-y,z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Invalid X,Y,Z point sent to distance(x,y,z)", ex.getMessage());
        }
        
        try {
            testTruck.setLocation(location);
            distance = testTruck.distance(x,y,-z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Invalid X,Y,Z point sent to distance(x,y,z)", ex.getMessage());
        }

        try {
            testTruck.setLocation(location);
            distance = testTruck.distance(x,y,z);
            x= Math.pow(x-location.getX(), 2);
            y= Math.pow(y-location.getY(), 2);
            z= Math.pow(z-location.getZ(), 2);
            expect = Math.sqrt(x+y+z);
            assertEquals(expect, distance, delta);            
            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl "
                    + "distance(double x, double y, double z) with a valid xyz.");
        }
    }

    /**
     * Test of getLocation method, of class TruckImpl.
     */
    @Test
    public void testGetLocation() {
        Point3D p = testTruck.getLocation();
        assertNotNull(p);
        assertEquals(p, testLoc);
        assertNotSame(p, testLoc);
    }

    /**
     * Test of getLocationX method, of class TruckImpl.
     */
    @Test
    public void testGetLocationX() {
        assertEquals(testLoc.getX(), testTruck.getLocationX(), delta);
    }

    /**
     * Test of getLocationY method, of class TruckImpl.
     */
    @Test
    public void testGetLocationY() {
        assertEquals(testLoc.getY(), testTruck.getLocationY(), delta);
    }

    /**
     * Test of getLocationZ method, of class TruckImpl.
     */
    @Test
    public void testGetLocationZ() {
        assertEquals(testLoc.getZ(), testTruck.getLocationZ(), delta);
    }

    /**
     * Test of setLocation method, of class TruckImpl.
     */
    @Test
    public void testSetLocation_Point3D() throws Exception {
        Point3D newPoint = new Point3D(99.9, 88.8, 77.7);
        try {
            testTruck.setLocation(newPoint);
            Point3D p = testTruck.getLocation();
            assertNotNull(p);
            assertEquals(p, newPoint);
            assertNotSame(p, newPoint);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl "
                    + "setLocation(Point3D) with a valid Point3D: " + newPoint);
        }
        
        newPoint = new Point3D(-55.5, 66.6, 77.7);
        try {
            testTruck.setLocation(newPoint);
            fail("InvalidDataException NOT thrown from TruckImpl setLocation(Point3D)"
                    +" with an invalid Point3D: " + newPoint);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertFalse(newPoint.equals(testTruck.getLocation()));
        }
        
        newPoint = null;
        try {
            testTruck.setLocation(newPoint);
            fail("InvalidDataException NOT thrown from TruckImpl setLocation(Point3D) "
                    +" with a null Point3D");
        } catch (InvalidDataException ex) {
            assertEquals("Null location sent to setLocation", ex.getMessage());
            assertNotNull(testTruck.getLocation());
        }
    }

    /**
     * Test of setLocation method, of class TruckImpl.
     */
    @Test
    public void testSetLocation_3args() throws Exception {
        double x = 99.9;
        double y = 88.8;
        double z = 77.7;
        try {
            testTruck.setLocation(x, y, z);
            assertEquals(x, testTruck.getLocationX(), delta);
            assertEquals(y, testTruck.getLocationY(), delta);
            assertEquals(z, testTruck.getLocationZ(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl"
                    +" setLocation(x,y,z) with a valid x, y, z: " + x + ", " + y + ", " + z);
        }
        
        x = -11.1;
        y = 22.2;
        z = 33.3;
        try {
            testTruck.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from TruckImpl setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertTrue(x != testTruck.getLocationX());
            assertTrue(y != testTruck.getLocationY());
            assertTrue(z != testTruck.getLocationZ());
        }
        x = 11.1;
        y = -22.2;
        z = 33.3;
        try {
            testTruck.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from TruckImpl setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertTrue(x != testTruck.getLocationX());
            assertTrue(y != testTruck.getLocationY());
            assertTrue(z != testTruck.getLocationZ());
        }
        x = 11.1;
        y = 22.2;
        z = -33.3;
        try {
            testTruck.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from TruckImpl setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertTrue(x != testTruck.getLocationX());
            assertTrue(y != testTruck.getLocationY());
            assertTrue(z != testTruck.getLocationZ());
        }
    }

    /**
     * Test of getMaxLoadWeight method, of class TruckImpl.
     */
    @Test
    public void testGetMaxLoadWeight() {
        assertNotNull(testTruck);
        assertEquals(testMaxLoadWeight, testTruck.getMaxLoadWeight(), 0.0);
    }

    /**
     * Test of setCurrentLoadWeight method, of class TruckImpl.
     */
    @Test
    public void testSetCurrentLoadWeight() {
        double mlw = 1020.0;
        testTruck.setCurrentLoadWeight(mlw);
        assertEquals(mlw, testTruck.getCurrentLoadWeight(), 0.0);
    }

    /**
     * Test of getCurrentLoadWeight method, of class TruckImpl.
     */
    @Test
    public void testGetCurrentLoadWeight() throws CannotFitException {
        double amountLoad = 300.0;
        try {
            testTruck.load(amountLoad);
            assertEquals(amountLoad, testTruck.getCurrentLoadWeight(), delta);           
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl"
                    +" load(amount) with a valid amount: " + amountLoad);
        }
        double amountUnload = 100;
        try {
            testTruck.unLoad(amountUnload);
            assertEquals(amountLoad - amountUnload, testTruck.getCurrentLoadWeight(), delta);           
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl"
                    +" unLoad(amount) with a valid amount: " + amountUnload);
        }
        amountLoad = 800;
        try {
            testTruck.load(amountLoad);
            assertEquals(testTruck.getMaxLoadWeight(), testTruck.getCurrentLoadWeight(), delta);           
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from TruckImpl"
                    +" load(amount) with a valid amount: " + amountLoad);
        }
    }

    /**
     * Test of getIdentifier method, of class TruckImpl.
     */
    @Test
    public void testGetIdentifier() {
        try {
            testLoc = new Point3D(10.0, 11.1, 12.2);
            testDest = new Point3D(20.2, 30.3, 40.4);
            testSpeed = 200;
            testMaxSpeed = 300;
            testMaxLoadWeight = 4000;
            testTruck = new TruckImpl(testLoc,testDest,testSpeed,testMaxSpeed,testMaxLoadWeight);
        } catch (InvalidDataException ex) {
            fail("Creation of object of TruckImpl failed: " + ex.getMessage());
        }
        
        assertNotNull(testTruck);
        String id = testTruck.getIdentifier();
        assertEquals("ID", id.substring(0, 2)); 
    }

    /**
     * Test of update method, of class TruckImpl.
     */
    @Test
    public void testUpdate() throws Exception {
        double millis = 0.0;
        testTruck.update(millis);
        Point3D p = testTruck.getLocation();
        assertNotNull(p);
        assertEquals(p, testLoc);
        assertFalse(testTruck.atDestination());
                
        millis = 200.0;
        testTruck.update(millis);
        Point3D p2 = testTruck.getLocation();
        assertNotNull(p2);
        assertFalse(testTruck.atDestination());
        
        millis = 100000000.0;
        testTruck.update(millis);
        Point3D p3 = testTruck.getLocation();
        assertNotNull(p3);
        assertEquals(p3, testDest);
        assertTrue(testTruck.atDestination());
    }
    
}
