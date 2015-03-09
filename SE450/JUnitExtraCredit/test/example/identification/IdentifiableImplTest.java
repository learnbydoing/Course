/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.identification;

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
public class IdentifiableImplTest {
    
    private Point3D testPoint3D;
    private IdentifiableImpl testIdentifiable;
    String testId = "789"; 
    
    public IdentifiableImplTest() {
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
            testPoint3D = new Point3D(10.1, 20.2, 30.3);
            testIdentifiable = new IdentifiableImpl(testId);
        } catch (InvalidDataException ex) {
            fail("Creation of test fixture object in @Before 'setUp' failed: " + ex.getMessage());
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getIdentifier method, of class IdentifiableImpl.
     */
    @Test
    public void testGetIdentifier() {
        //valid id
        assertNotNull(testIdentifiable);
        assertEquals(testId, testIdentifiable.getIdentifier());  
        
        //null id
        testId = null;
        try {
            testIdentifiable = new IdentifiableImpl(testId);
            fail("InvalidDataException NOT thrown from IdentifiableImpl constructor");
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Null or empty ID passed to setIdentifier");
        }
        
        //empty id
        testId = "";
        try {
            testIdentifiable = new IdentifiableImpl(testId);
            fail("InvalidDataException NOT thrown from IdentifiableImpl constructor");
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Null or empty ID passed to setIdentifier");
        }                
    }

    /**
     * Test of setIdentifier method, of class IdentifiableImpl.
     */
    @Test
    public void testSetIdentifier() throws Exception {
        
        //null id
        testId = null;
        try {
            testIdentifiable.setIdentifier(testId);
            fail("InvalidDataException NOT thrown from IdentifiableImpl setIdentifier");
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Null or empty ID passed to setIdentifier");
        }
        
        //empty id
        testId = "";
        try {
            testIdentifiable.setIdentifier(testId);
            fail("InvalidDataException NOT thrown from IdentifiableImpl setIdentifier");
        } catch (InvalidDataException ex) {
            assertEquals(ex.getMessage(), "Null or empty ID passed to setIdentifier");
        }
        
        //valid id
        testId = "1234";
        try {
            testIdentifiable.setIdentifier(testId);
            assertEquals(testId, testIdentifiable.getIdentifier());            
        } catch (InvalidDataException ex) {
            fail("InvalidDataException(" + ex.getMessage() + ") thrown from IdentifiableImpl");
        }
    }    
}
