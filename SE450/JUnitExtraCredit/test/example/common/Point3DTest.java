/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.common;

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
public class Point3DTest {

    public Point3DTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getZ method, of class Point3D.
     */
    @Test
    public void testGetZ() {
        System.out.println("getZ");
        Point3D instance = new Point3D(1.1,2.2,3.3);
        double expResult = 3.3;
        double result = instance.getZ();
        assertEquals(expResult, result, 0.0);
        
        Point3D point2 = new Point3D(4.4, 5.5);
        double expResult2 = 0.0;
        double result2 = point2.getZ();
        assertEquals(expResult2, result2, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setCoordinates method, of class Point3D.
     */
    @Test
    public void testSetCoordinates_3args() {
        System.out.println("setCoordinates");
        double xIn = 4.4;
        double yIn = 5.5;
        double zIn = 6.6;
        Point3D instance = new Point3D();
        instance.setCoordinates(xIn, yIn, zIn);
        assertEquals(xIn, instance.getX(), 0.0);
        assertEquals(yIn, instance.getY(), 0.0);
        assertEquals(zIn, instance.getZ(), 0.0);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setLocation method, of class Point3D.
     */
    @Test
    public void testSetLocation_Point3D() {
        System.out.println("setLocation");
        double xIn = 12.3;
        double yIn = 45.6;
        double zIn = 78.9;
        Point3D aPoint = new Point3D(12.3,45.6,78.9);
        Point3D instance = new Point3D();
        instance.setLocation(aPoint);
        assertEquals(xIn, instance.getX(), 0.0);
        assertEquals(yIn, instance.getY(), 0.0);
        assertEquals(zIn, instance.getZ(), 0.0);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setLocation method, of class Point3D.
     */
    @Test
    public void testSetLocation_3args() {
        System.out.println("setLocation");
        double xIn = 11.1;
        double yIn = 12.2;
        double zIn = 13.3;
        Point3D instance = new Point3D();
        instance.setLocation(xIn, yIn, zIn);
        assertEquals(xIn, instance.getX(), 0.0);
        assertEquals(yIn, instance.getY(), 0.0);
        assertEquals(zIn, instance.getZ(), 0.0);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setCoordinates method, of class Point3D.
     */
    @Test
    public void testSetCoordinates_Point3D() {
        System.out.println("setCoordinates");
        double xIn = 32.1;
        double yIn = 65.4;
        double zIn = 98.7;
        Point3D aPoint = new Point3D(xIn, yIn, zIn);
        Point3D instance = new Point3D();
        instance.setCoordinates(aPoint);
        assertEquals(xIn, instance.getX(), 0.0);
        assertEquals(yIn, instance.getY(), 0.0);
        assertEquals(zIn, instance.getZ(), 0.0);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Point3D.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Point3D instance = new Point3D(41.3,56.2,67.1);
        String expResult = "[41.30, 56.20, 67.10]";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of distance method, of class Point3D.
     */
    @Test
    public void testDistance_3args() {
        System.out.println("distance");
        double xIn = 1.1;
        double yIn = 2.2;
        double zIn = 3.3;
        Point3D instance = new Point3D(7.1, 8.2,9.3);
        double expResult = Math.sqrt(36.0+36.0+36.0);
        double result = instance.distance(xIn, yIn, zIn);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of distance method, of class Point3D.
     */
    @Test
    public void testDistance_Point3D() {
        System.out.println("distance");
        Point3D aPoint = new Point3D(2.1, 2.2,2.3);
        Point3D instance = new Point3D(7.1, 8.2,9.3);
        double expResult = Math.sqrt(25.0+36.0+49.0);
        double result = instance.distance(aPoint);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class Point3D.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Point3D aPoint3D = new Point3D(2.1, 2.2,2.3);
        Point3D instance = new Point3D(2.1, 2.4,2.3);
        boolean expResult = false;
        boolean result = instance.equals(aPoint3D);
        assertEquals(expResult, result);
        
        Point3D aPoint3D2 = new Point3D(11.1, 22.2,33.3);
        Point3D instance2 = new Point3D(11.1, 22.2,33.3);
        boolean expResult2 = true;
        boolean result2 = instance2.equals(aPoint3D2);
        assertEquals(expResult2, result2);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }    
}
