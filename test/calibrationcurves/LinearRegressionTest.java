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

        //y = 2x + 1
        double[][] x1 = new double[][]{{1, 1, 1, 1}, {1, 2, 3, 4}};
        double[]   y1 = new double[]  {3, 5, 7, 9};
        double[]   r1 = new double[]  {0.9999999192857593, 2.0000000274526846};
        double[]   t1 = LinearRegression.compute(x1, y1, 0.1, 1000).getRowPackedCopy();
        assertEquals(r1[0], t1[0], 0.0001);
        assertEquals(r1[1], t1[1], 0.0001);

        //y = 4x -7
        double[][] x2 = new double[][]{{1, 1, 1, 1, 1}, {-2, -0.95, 0.1, 1.05, 2.02}}; 
        double[]   y2 = new double[]  {-15.1, -10.91, -6.94, -3.1, 1.1};
        double[]   r2 = new double[]  {-7.1661414356139135, 4.00321444577085};
        double[]   t2 = LinearRegression.compute(x2, y2, 0.1, 1000).getRowPackedCopy();
        assertEquals(r2[0], t2[0], 0.0001);
        assertEquals(r2[1], t2[1], 0.0001);

        //y = 4x -7 with x and x^2
        double[][] x3 = new double[][]{{1, 1, 1, 1, 1}, {-2, -0.95, 0.1, 1.05, 2.02}, {4, 1, 0.1, 1.05, 4.02}}; 
        double[]   y3 = new double[]  {-15.1, -10.91, -6.94, -3.1, 1.1};
        double[]   r3 = new double[]  {-7.302466332853576, 4.004307750189654, 0.06699940602027288};
        double[]   t3 = LinearRegression.compute(x3, y3, 0.1, 1000).getRowPackedCopy();
        assertEquals(r3[0], t3[0], 0.0001);
        assertEquals(r3[1], t3[1], 0.0001);
        assertEquals(r3[2], t3[2], 0.0001);

        //y = x^2, with x and x^2
        double[][] x4 = new double[][]{{1, 1, 1, 1, 1}, {-2, -1, 0, 1, 2}, {4, 1, 0, 1, 4}};
        double[]   y4 = new double[]  {4, 1, 0, 1, 4};
        double[]   r4 = new double[]  {0.0, 0.0, 1.0};
        double[]   t4 = LinearRegression.compute(x4, y4, 0.1, 1000).getRowPackedCopy();
        assertEquals(r4[0], t4[0], 0.0001);
        assertEquals(r4[1], t4[1], 0.0001);
        assertEquals(r4[2], t4[2], 0.0001);

        //y = x^2 + 2, with x and x^2
        double[][] x5 = new double[][]{{1, 1, 1, 1, 1}, {-2, -1, 0, 1, 2},  {4, 1, 0, 1, 4}}; 
        double[]   y5 = new double[]  {6.1, 2.94, 2.05, 3.04, 6.01};
        double[]   r5 = new double[]  {2.0079999999999947, -0.007999999999999896, 1.0100000000000016};
        double[]   t5 = LinearRegression.compute(x5, y5, 0.1, 1000).getRowPackedCopy();
        assertEquals(r5[0], t5[0], 0.0001);
        assertEquals(r5[1], t5[1], 0.0001);
        assertEquals(r5[2], t5[2], 0.0001);

        //y = 2x^2 + 2, with x and x^2
        double[][] x6 = new double[][]{{1, 1, 1, 1, 1}, {-2, -1, 0, 1, 2},  {4, 1, 0, 1, 4}}; 
        double[]   y6 = new double[]  {10.1, 3.94, 2.05, 4.04, 9.91};
        double[]   r6 = new double[]  {2.0165714285714245, -0.02800000000000007, 1.9957142857142869};
        double[]   t6 = LinearRegression.compute(x6, y6, 0.1, 1000).getRowPackedCopy();
        assertEquals(r6[0], t6[0], 0.0001);
        assertEquals(r6[1], t6[1], 0.0001);
        assertEquals(r6[2], t6[2], 0.0001);
    }
    
    @Test
    public void testRowSums() {
        try {
            //accessing a private method using reflection
            Method method = JAMAMatrixUtils.class.getDeclaredMethod(
                    "rowSums", 
                    Jama.Matrix.class
            );
            method.setAccessible(true);
            
            Matrix a1 = new Matrix(new double[][]{{1, 5}});
            double[][] ra1 = (double[][])method.invoke(Matrix.class, a1);
            assertEquals(6.0, ra1[0][0], 0.00001);
            
            Matrix b1 = new Matrix(new double[][]{{6, 3}});
            double[][] rb1 = (double[][])method.invoke(Matrix.class, b1);
            assertEquals(9.0, rb1[0][0], 0.00001);
            
            Matrix r1 = new Matrix(new double[][]{{6, 15}});
            double[][] rr1 = (double[][])method.invoke(Matrix.class, r1);
            assertEquals(21, rr1[0][0], 0.00001);
            
            Matrix a2 = new Matrix(new double[][]{{1, 5}, {4, 20}});
            double[][] ra2 = (double[][])method.invoke(Matrix.class, a2);
            assertEquals(6, ra2[0][0], 0.00001);
            assertEquals(24, ra2[0][1], 0.00001);
            
            Matrix b2 = new Matrix(new double[][]{{6, 3}, {3,  0}});
            double[][] rb2 = (double[][])method.invoke(Matrix.class, b2);
            assertEquals(9, rb2[0][0], 0.00001);
            assertEquals(3, rb2[0][1], 0.00001);
            
            Matrix r2 = new Matrix(new double[][]{{6, 15}, {12, -100}});
            double[][] rr2 = (double[][])method.invoke(Matrix.class, r2);
            assertEquals(21, rr2[0][0], 0.00001);
            assertEquals(-88, rr2[0][1], 0.00001);
            
        } catch (NoSuchMethodException | SecurityException ex) {
            fail("Error accessing method using reflection.");
            Logger.getLogger(LinearRegressionTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(LinearRegressionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testRepmatByRow() {
        try {
            //accessing a private method using reflection
            Method method = JAMAMatrixUtils.class.getDeclaredMethod(
                    "repmatByRow", 
                    Jama.Matrix.class,
                    int.class
            );
            method.setAccessible(true);
            
            Matrix a1 = new Matrix(new double[][]{{1, 5}});
            int rep1 = 2;
            Matrix r1 = (Matrix)method.invoke(LinearRegression.class, a1, rep1);
            assertEquals(r1.getArray().length, rep1*a1.getArray().length); //should have rep1 rows
            assertEquals(r1.getArray()[0].length, 2); //should have 2 columns
            assertEquals(1, r1.getArray()[0][0], 0.00000000001);
            assertEquals(5, r1.getArray()[0][1], 0.00000000001);
            assertEquals(1, r1.getArray()[1][0], 0.00000000001);
            assertEquals(5, r1.getArray()[1][1], 0.00000000001);
            
            Matrix a2 = new Matrix(new double[][]{{1, 5, 1}});
            int rep2 = 3;
            Matrix r2 = (Matrix)method.invoke(LinearRegression.class, a2, rep2);
            assertEquals(r2.getArray().length, rep2*a2.getArray().length); //should have rep2 rows
            assertEquals(r2.getArray()[0].length, 3); //should have 3 columns
            assertEquals(1, r2.getArray()[0][0], 0.00000000001);
            assertEquals(5, r2.getArray()[0][1], 0.00000000001);
            assertEquals(1, r2.getArray()[0][2], 0.00000000001);
            assertEquals(1, r2.getArray()[1][0], 0.00000000001);
            assertEquals(5, r2.getArray()[1][1], 0.00000000001);
            assertEquals(1, r2.getArray()[1][2], 0.00000000001);
            assertEquals(1, r2.getArray()[2][0], 0.00000000001);
            assertEquals(5, r2.getArray()[2][1], 0.00000000001);
            assertEquals(1, r2.getArray()[2][2], 0.00000000001);
            
            Matrix a3 = new Matrix(new double[][]{{1, 5}, {7.3, -4}});
            int rep3 = 2;
            Matrix r3 = (Matrix)method.invoke(LinearRegression.class, a3, rep3);
            assertEquals(r3.getArray().length, rep3*a3.getArray().length); //should have rep1*rows
            assertEquals(r3.getArray()[0].length, 2); //should have 2 columns
            assertEquals(1, r3.getArray()[0][0], 0.00000000001);
            assertEquals(5, r3.getArray()[0][1], 0.00000000001);
            assertEquals(7.3, r3.getArray()[1][0], 0.00000000001);
            assertEquals(-4, r3.getArray()[1][1], 0.00000000001);
            assertEquals(1, r3.getArray()[2][0], 0.00000000001);
            assertEquals(5, r3.getArray()[2][1], 0.00000000001);
            assertEquals(7.3, r3.getArray()[3][0], 0.00000000001);
            assertEquals(-4, r3.getArray()[3][1], 0.00000000001);
            
        } catch (NoSuchMethodException | SecurityException ex) {
            fail("Error accessing method using reflection.");
            Logger.getLogger(LinearRegressionTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(LinearRegressionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}