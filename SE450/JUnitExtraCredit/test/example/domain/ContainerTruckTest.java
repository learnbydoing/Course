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
public class ContainerTruckTest {
    private Point3D testLoc;
    private Point3D testDest;
    private double testSpeed;
    private double testMaxSpeed;
    private double testMaxLoadWeight;
    private ContainerTruck testContainerTruck;
    double distance = 0;
    double expect = 0;
    private static final double delta = 0.001;
    
    public ContainerTruckTest() {
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
            testContainerTruck = new ContainerTruck(testLoc,testDest,testSpeed,testMaxSpeed,testMaxLoadWeight);
        } catch (InvalidDataException ex) {
            fail("Creation of test fixture object in @Before 'setUp' failed: " + ex.getMessage());
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of load method, of class ContainerTruck.
     */
    @Test
    public void testLoad() throws Exception {
        double amount = 500; 
        try {
            testContainerTruck.load(amount);
            fail("InvalidDataException NOT thrown from ContainerTruck load(amount) "
                    +" with a invalid amount: " + amount);
        } catch (InvalidDataException ex) {
            assertEquals("A Container Truck can only be *fully* loaded (" 
                    + testContainerTruck.getMaxLoadWeight()
                    + ")- not partially loaded (" + amount + ")", ex.getMessage());
        }
        
        amount = 1000.0;
        try {
            testContainerTruck.load(amount);
            assertEquals(amount, testContainerTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from ContainerTruck"
                    +" load(amount) with a valid amount: " + amount);
        }         
    }

    /**
     * Test of unLoad method, of class ContainerTruck.
     */
    @Test
    public void testUnLoad() throws Exception {
        double amount = 1000.0;
        testContainerTruck.load(amount);
        
        amount = 300.0;
        try {
            testContainerTruck.unLoad(amount);
            fail("InvalidDataException NOT thrown from ContainerTruck unLoad(amount) "
                    +" with a invalid amount: " + amount);
        } catch (InvalidDataException ex) {
            assertEquals("A Container Truck can only be *fully* unloaded ("
                    + testContainerTruck.getMaxLoadWeight()
                    + ")- not partially unloaded (" + amount + ")", ex.getMessage());
        }
        
        amount = 1000;
        try {
            testContainerTruck.unLoad(amount);
            assertEquals(0, testContainerTruck.getCurrentLoadWeight(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from ContainerTruck"
                    +" unLoad(amount) with a valid amount: " + amount);
        }        
    }

    /**
     * Test of atDestination method, of class ContainerTruck.
     */
    @Test
    public void testAtDestination() {
        try {
            Point3D newlocation = new Point3D(105.5, 106.6, 107.7);
            testContainerTruck.setLocation(newlocation);
            assertEquals(false, testContainerTruck.atDestination());            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException NOT thrown from ContainerTruck AtDestination()");
        }
        
        try {
            Point3D newlocation = new Point3D(77.7, 88.8, 99.9);
            testContainerTruck.setLocation(newlocation);
            assertEquals(true, testContainerTruck.atDestination());
        } catch (InvalidDataException ex) {
            fail("InvalidDataException NOT thrown from ContainerTruck AtDestination()");
        }  
    }

    /**
     * Test of getDestination method, of class ContainerTruck.
     */
    @Test
    public void testGetDestination() {
        Point3D p = testContainerTruck.getDestination();
        assertNotNull(p);
        assertEquals(p, testDest);
        assertNotSame(p, testDest);
    }

    /**
     * Test of getDestinationX method, of class ContainerTruck.
     */
    @Test
    public void testGetDestinationX() {
        assertEquals(testDest.getX(), testContainerTruck.getDestinationX(), delta);
    }

    /**
     * Test of getDestinationY method, of class ContainerTruck.
     */
    @Test
    public void testGetDestinationY() {
        assertEquals(testDest.getY(), testContainerTruck.getDestinationY(), delta);
    }

    /**
     * Test of getDestinationZ method, of class ContainerTruck.
     */
    @Test
    public void testGetDestinationZ() {
        assertEquals(testDest.getZ(), testContainerTruck.getDestinationZ(), delta);
    }

    /**
     * Test of getMaxSpeed method, of class ContainerTruck.
     */
    @Test
    public void testGetMaxSpeed() {
        assertEquals(testMaxSpeed, testContainerTruck.getMaxSpeed(), 0.0);
    }

    /**
     * Test of getSpeed method, of class ContainerTruck.
     */
    @Test
    public void testGetSpeed() {
        assertEquals(testSpeed, testContainerTruck.getSpeed(), 0.0);
    }

    /**
     * Test of setDestination method, of class ContainerTruck.
     */
    @Test
    public void testSetDestination_3args() throws Exception {
        double x = 99.9;
        double y = 88.8;
        double z = 77.7;
        try {
            testContainerTruck.setDestination(x, y, z);
            assertEquals(x, testContainerTruck.getDestinationX(), delta);
            assertEquals(y, testContainerTruck.getDestinationY(), delta);
            assertEquals(z, testContainerTruck.getDestinationZ(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from ContainerTruck"
                    +" setDestination(x,y,z) with a valid x, y, z: " + x + ", " + y + ", " + z);
        }
        
        x = -11.1;
        y = 22.2;
        z = 33.3;
        try {
            testContainerTruck.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from ContainerTruck setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")", ex.getMessage());
            assertTrue(x != testContainerTruck.getDestinationX());
            assertTrue(y != testContainerTruck.getDestinationY());
            assertTrue(z != testContainerTruck.getDestinationZ());
        }
        
        x = 11.1;
        y = -22.2;
        z = 33.3;
        try {
            testContainerTruck.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from ContainerTruck setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")", ex.getMessage());
            assertTrue(x != testContainerTruck.getDestinationX());
            assertTrue(y != testContainerTruck.getDestinationY());
            assertTrue(z != testContainerTruck.getDestinationZ());
        }
        
        x = 11.1;
        y = 22.2;
        z = -33.3;
        try {
            testContainerTruck.setDestination(x, y, z);
            fail("InvalidDataException NOT thrown from ContainerTruck setDestination(x,y,z) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")", ex.getMessage());
            assertTrue(x != testContainerTruck.getDestinationX());
            assertTrue(y != testContainerTruck.getDestinationY());
            assertTrue(z != testContainerTruck.getDestinationZ());
        }
    }

    /**
     * Test of setDestination method, of class ContainerTruck.
     */
    @Test
    public void testSetDestination_Point3D() throws Exception {
        Point3D newPoint = new Point3D(101.1, 202.2, 303.3);
        try {
            testContainerTruck.setDestination(newPoint);
            Point3D p = testContainerTruck.getDestination();
            assertNotNull(p);
            assertEquals(p, newPoint);
            assertNotSame(p, newPoint);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from ContainerTruck "
                    + "setDestination(Point3D) with a valid Point3D: " + newPoint);
        }
        
        newPoint = new Point3D(-505.5, 606.6, 707.7);
        try {
            testContainerTruck.setDestination(newPoint);
            fail("InvalidDataException NOT thrown from ContainerTruck SetDestination(Point3D)"
                    +" with an invalid Point3D: " + newPoint);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setDestination(x,y,z): (" + newPoint.getX() + "," + newPoint.getY() + "," + newPoint.getZ() + ")", ex.getMessage());
            assertFalse(newPoint.equals(testContainerTruck.getLocation()));
        }
        
        newPoint = null;
        try {
            testContainerTruck.setDestination(newPoint);
            fail("InvalidDataException NOT thrown from ContainerTruck SetDestination(Point3D) "
                    +" with a null Point3D");
        } catch (InvalidDataException ex) {
            assertEquals("Null Point3D sent to setDestination(Point3D)", ex.getMessage());
            assertNotNull(testContainerTruck.getDestination());
        }
    }

    /**
     * Test of setMaxSpeed method, of class ContainerTruck.
     */
    @Test
    public void testSetMaxSpeed() throws Exception {
        double ms = -22;        
        try {
            testContainerTruck.setMaxSpeed(ms);
            fail("InvalidDataException NOT thrown from ContainerTruck SetMaxSpeed(ms) "
                    +" with a negative max speed value");
        } catch (InvalidDataException ex) {
            assertEquals("Negative maxSpeed sent to setMaxSpeed:" + ms, ex.getMessage());
            assertNotNull(testContainerTruck.getMaxSpeed());
        }
        
        ms = 80;        
        try {
            testContainerTruck.setMaxSpeed(ms);
            fail("InvalidDataException NOT thrown from ContainerTruck SetMaxSpeed(s) "
                    +" with a max speed value smaller than speed");
        } catch (InvalidDataException ex) {
            assertEquals("Attempt to set maxSpeed less than speed in setMaxSpeed: " + ms, ex.getMessage());
            assertNotNull(testContainerTruck.getMaxSpeed());
        }
        
        ms = 400;        
        try {
            testContainerTruck.setMaxSpeed(ms);
            assertEquals(ms, testContainerTruck.getMaxSpeed(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from ContainerTruck "
                    + "setMaxSpeed(ms) with a valid maximum speed: " + ms);
        }
    }

    /**
     * Test of setSpeed method, of class ContainerTruck.
     */
    @Test
    public void testSetSpeed() throws Exception {
        double s = -1;        
        try {
            testContainerTruck.setSpeed(s);
            fail("InvalidDataException NOT thrown from ContainerTruck SetSpeed(s) "
                    +" with a negative speed value");
        } catch (InvalidDataException ex) {
            assertEquals("Negative speed sent to setSpeed:" + s, ex.getMessage());
            assertNotNull(testContainerTruck.getSpeed());
        }
        
        s = 300;        
        try {
            testContainerTruck.setSpeed(s);
            fail("InvalidDataException NOT thrown from ContainerTruck SetSpeed(s) "
                    +" with a speed value larger than maximum speed");
        } catch (InvalidDataException ex) {
            assertEquals("Attempt to set speed (" + s + ") greater than maxSpeed (" 
                    + testContainerTruck.getMaxSpeed() + ") in setSpeed", ex.getMessage());
            assertNotNull(testContainerTruck.getSpeed());
        }
        
        s = 150;        
        try {
            testContainerTruck.setSpeed(s);
            assertEquals(s, testContainerTruck.getSpeed(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from ContainerTruck "
                    + "setSpeed(s) with a valid speed: " + s);
        }
    }

    /**
     * Test of distance method, of class ContainerTruck.
     */
    @Test
    public void testDistance_Point3D() throws Exception {
        Point3D location = new Point3D(99.9, 88.8, 77.7);
        
        Point3D destination = null;
        
        try {
            testContainerTruck.setLocation(location);
            distance = testContainerTruck.distance(destination);
            assertEquals(distance, testContainerTruck.distance(destination), delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Null location sent to distance", ex.getMessage());
        }
        
        destination = new Point3D(11.1, 22.2, 33.3);
        try {
            testContainerTruck.setLocation(location);
            double x= Math.pow(destination.getX()-location.getX(), 2);
            double y= Math.pow(destination.getY()-location.getY(), 2);
            double z= Math.pow(destination.getZ()-location.getZ(), 2);
            distance = Math.sqrt(x+y+z);
            assertEquals(distance, testContainerTruck.distance(destination), delta);            
            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from ContainerTruck "
                    + "distance(Point3D) with a valid Point3D: " + destination);
        }
    }

    /**
     * Test of distance method, of class ContainerTruck.
     */
    @Test
    public void testDistance_3args() throws Exception {
        Point3D location = new Point3D(99.9, 88.8, 77.7);
        double x= 11.1;
        double y= 22.2;
        double z= 33.3;        
        
        try {
            testContainerTruck.setLocation(location);
            distance = testContainerTruck.distance(-x,y,z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Invalid X,Y,Z point sent to distance(x,y,z)", ex.getMessage());
        }
        
        try {
            testContainerTruck.setLocation(location);
            distance = testContainerTruck.distance(x,-y,z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Invalid X,Y,Z point sent to distance(x,y,z)", ex.getMessage());
        }
        
        try {
            testContainerTruck.setLocation(location);
            distance = testContainerTruck.distance(x,y,-z);
            assertEquals(expect, distance, delta);            
        } catch (InvalidDataException ex) {
           assertEquals("Invalid X,Y,Z point sent to distance(x,y,z)", ex.getMessage());
        }

        try {
            testContainerTruck.setLocation(location);
            distance = testContainerTruck.distance(x,y,z);
            x= Math.pow(x-location.getX(), 2);
            y= Math.pow(y-location.getY(), 2);
            z= Math.pow(z-location.getZ(), 2);
            expect = Math.sqrt(x+y+z);
            assertEquals(expect, distance, delta);            
            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from ContainerTruck "
                    + "distance(double x, double y, double z) with a valid xyz.");
        }
    }

    /**
     * Test of getLocation method, of class ContainerTruck.
     */
    @Test
    public void testGetLocation() {
        Point3D p = testContainerTruck.getLocation();
        assertNotNull(p);
        assertEquals(p, testLoc);
        assertNotSame(p, testLoc);
    }

    /**
     * Test of getLocationX method, of class ContainerTruck.
     */
    @Test
    public void testGetLocationX() {
        assertEquals(testLoc.getX(), testContainerTruck.getLocationX(), delta);
    }

    /**
     * Test of getLocationY method, of class ContainerTruck.
     */
    @Test
    public void testGetLocationY() {
        assertEquals(testLoc.getY(), testContainerTruck.getLocationY(), delta);
    }

    /**
     * Test of getLocationZ method, of class ContainerTruck.
     */
    @Test
    public void testGetLocationZ() {
        assertEquals(testLoc.getZ(), testContainerTruck.getLocationZ(), delta);
    }

    /**
     * Test of setLocation method, of class ContainerTruck.
     */
    @Test
    public void testSetLocation_Point3D() throws Exception {
        Point3D newPoint = new Point3D(99.9, 88.8, 77.7);
        try {
            testContainerTruck.setLocation(newPoint);
            Point3D p = testContainerTruck.getLocation();
            assertNotNull(p);
            assertEquals(p, newPoint);
            assertNotSame(p, newPoint);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from ContainerTruck "
                    + "setLocation(Point3D) with a valid Point3D: " + newPoint);
        }
        
        newPoint = new Point3D(-55.5, 66.6, 77.7);
        try {
            testContainerTruck.setLocation(newPoint);
            fail("InvalidDataException NOT thrown from ContainerTruck setLocation(Point3D)"
                    +" with an invalid Point3D: " + newPoint);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertFalse(newPoint.equals(testContainerTruck.getLocation()));
        }
        
        newPoint = null;
        try {
            testContainerTruck.setLocation(newPoint);
            fail("InvalidDataException NOT thrown from ContainerTruck setLocation(Point3D) "
                    +" with a null Point3D");
        } catch (InvalidDataException ex) {
            assertEquals("Null location sent to setLocation", ex.getMessage());
            assertNotNull(testContainerTruck.getLocation());
        }
    }

    /**
     * Test of setLocation method, of class ContainerTruck.
     */
    @Test
    public void testSetLocation_3args() throws Exception {
        double x = 99.9;
        double y = 88.8;
        double z = 77.7;
        try {
            testContainerTruck.setLocation(x, y, z);
            assertEquals(x, testContainerTruck.getLocationX(), delta);
            assertEquals(y, testContainerTruck.getLocationY(), delta);
            assertEquals(z, testContainerTruck.getLocationZ(), delta);
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from ContainerTruck"
                    +" setLocation(x,y,z) with a valid x, y, z: " + x + ", " + y + ", " + z);
        }
        
        x = -11.1;
        y = 22.2;
        z = 33.3;
        try {
            testContainerTruck.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from ContainerTruck setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertTrue(x != testContainerTruck.getLocationX());
            assertTrue(y != testContainerTruck.getLocationY());
            assertTrue(z != testContainerTruck.getLocationZ());
        }
        x = 11.1;
        y = -22.2;
        z = 33.3;
        try {
            testContainerTruck.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from ContainerTruck setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertTrue(x != testContainerTruck.getLocationX());
            assertTrue(y != testContainerTruck.getLocationY());
            assertTrue(z != testContainerTruck.getLocationZ());
        }
        x = 11.1;
        y = 22.2;
        z = -33.3;
        try {
            testContainerTruck.setLocation(x, y, z);
            fail("InvalidDataException NOT thrown from ContainerTruck setLocation(Point3D) "
                    +" with a invalid x, y, z: " + x + ", " + y + ", " + z);
        } catch (InvalidDataException ex) {
            assertEquals("Invalid X,Y,Z point sent to setLocation(x,y,z)", ex.getMessage());
            assertTrue(x != testContainerTruck.getLocationX());
            assertTrue(y != testContainerTruck.getLocationY());
            assertTrue(z != testContainerTruck.getLocationZ());
        }
    }

    /**
     * Test of getIdentifier method, of class ContainerTruck.
     */
    @Test
    public void testGetIdentifier() {
        try {
            testLoc = new Point3D(10.0, 11.1, 12.2);
            testDest = new Point3D(20.2, 30.3, 40.4);
            testSpeed = 200;
            testMaxSpeed = 300;
            testMaxLoadWeight = 4000;
            testContainerTruck = new ContainerTruck(testLoc,testDest,testSpeed,testMaxSpeed,testMaxLoadWeight);
        } catch (InvalidDataException ex) {
            fail("Creation of object of ContainerTruck failed: " + ex.getMessage());
        }
        
        assertNotNull(testContainerTruck);
        String id = testContainerTruck.getIdentifier();
        assertEquals("ID", id.substring(0, 2)); 
    }

    /**
     * Test of getMaxLoadWeight method, of class ContainerTruck.
     */
    @Test
    public void testGetMaxLoadWeight() {
        assertNotNull(testContainerTruck);
        assertEquals(testMaxLoadWeight, testContainerTruck.getMaxLoadWeight(), 0.0);
    }

    /**
     * Test of setCurrentLoadWeight method, of class ContainerTruck.
     */
    @Test
    public void testSetCurrentLoadWeight() {
        double mlw = 1020.0;
        testContainerTruck.setCurrentLoadWeight(mlw);
        assertEquals(mlw, testContainerTruck.getCurrentLoadWeight(), 0.0);
    }

    /**
     * Test of getCurrentLoadWeight method, of class ContainerTruck.
     */
    @Test
    public void testGetCurrentLoadWeight() {
        double amountLoad = 500.0;
        try {
            testContainerTruck.load(amountLoad);
            fail("InvalidDataException NOT thrown from ContainerTruck load(amount) "
                    +" with a invalid amount: " + amountLoad);            
        } catch (InvalidDataException ex) {
            assertEquals(0, testContainerTruck.getCurrentLoadWeight(), delta);
        } catch (CannotFitException ex) {
            assertEquals(0, testContainerTruck.getCurrentLoadWeight(), delta);
        }
        amountLoad = 1000.0;
        try {
            testContainerTruck.load(amountLoad);
            assertEquals(amountLoad, testContainerTruck.getCurrentLoadWeight(), delta);           
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from ContainerTruck"
                    +" load(amount) with a valid amount: " + amountLoad);
        } catch (CannotFitException ex) {
            fail("CannotFitException (" + ex.getMessage() + ") thrown from ContainerTruck"
                    +" load(amount) with a valid amount: " + amountLoad);
        }        
    }

    /**
     * Test of update method, of class ContainerTruck.
     */
    @Test
    public void testUpdate() throws Exception {
        double millis = 0.0;
        testContainerTruck.update(millis);
        Point3D p = testContainerTruck.getLocation();
        assertNotNull(p);
        assertEquals(p, testLoc);
        assertFalse(testContainerTruck.atDestination());
                
        millis = 200.0;
        testContainerTruck.update(millis);
        Point3D p2 = testContainerTruck.getLocation();
        assertNotNull(p2);
        assertFalse(testContainerTruck.atDestination());
        
        millis = 100000000.0;
        testContainerTruck.update(millis);
        Point3D p3 = testContainerTruck.getLocation();
        assertNotNull(p3);
        assertEquals(p3, testDest);
        assertTrue(testContainerTruck.atDestination());
    }

    /**
     * Test of toString method, of class ContainerTruck.
     */
    @Test
    public void testToString() {
        try {
            Point3D newloc = new Point3D(44.4, 55.5, 66.6);
            testContainerTruck.setLocation(newloc);
            String expResult = "I am ContainerTruck " + testContainerTruck.getIdentifier() + ".\n\tI am at "
                    + testContainerTruck.getLocation() + " and am heading to " + testContainerTruck.getDestination()
                    + ".\n\tMy load is " + testContainerTruck.getCurrentLoadWeight() + " and my max load is "
                    + testContainerTruck.getMaxLoadWeight() + ".\n\tDistance to my destination is "
                    + String.format("%4.2f", testContainerTruck.distance(testContainerTruck.getDestination())) + ". "
                    + "I'm not there yet";
            String result = testContainerTruck.toString();
            assertEquals(expResult, result);     
            
            newloc = new Point3D(77.7, 88.8, 99.9);
            testContainerTruck.setLocation(newloc);
            expResult = "I am ContainerTruck " + testContainerTruck.getIdentifier() + ".\n\tI am at "
                    + testContainerTruck.getLocation() + " and am heading to " + testContainerTruck.getDestination()
                    + ".\n\tMy load is " + testContainerTruck.getCurrentLoadWeight() + " and my max load is "
                    + testContainerTruck.getMaxLoadWeight() + ".\n\tDistance to my destination is "
                    + String.format("%4.2f", testContainerTruck.distance(testContainerTruck.getDestination())) + ". "
                    + "I am there!";
            result = testContainerTruck.toString();
            assertEquals(expResult, result);      
        } catch (InvalidDataException ex) {
            fail("InvalidDataException (" + ex.getMessage() + ") thrown from ContainerTruck");
        }        
    }    
}
