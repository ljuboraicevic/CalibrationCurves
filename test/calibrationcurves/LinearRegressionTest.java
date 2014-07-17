package calibrationcurves;

import Jama.Matrix;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class LinearRegressionTest {
    
    public LinearRegressionTest() {
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
     * Test of compute method, of class LinearRegression.
     */
    @Test
    public void testCompute() {
//        System.out.println("compute");
//        double[][] paramX = null;
//        double[] paramY = null;
//        Matrix expResult = null;
//        Matrix result = LinearRegression.compute(paramX, paramY);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
//    @Test
//    public void testDotProduct() {
//        try {
//            Matrix a1 = new Matrix(new double[][]{{1, 5}});
//            Matrix b1 = new Matrix(new double[][]{{6, 3}});
//            Matrix r1 = new Matrix(new double[][]{{6, 15}});
//            
//            Method method = LinearRegression.class.getDeclaredMethod(
//                    "dotProduct", 
//                    Jama.Matrix.class, 
//                    Jama.Matrix.class);
//            
//            method.setAccessible(true);
//            Matrix result1 = (Matrix)method.invoke(Matrix.class, a1, b1);
//            //assertArrayEquals(r1.getRowPackedCopy(), result1.getRowPackedCopy());
////            method.inv
//            
//        } catch (NoSuchMethodException | SecurityException ex) {
//            fail("Error accessing method using reflection.");
//            Logger.getLogger(LinearRegressionTest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//            Logger.getLogger(LinearRegressionTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
}
