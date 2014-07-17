package calibrationcurves;

import Jama.Matrix;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class LinearRegression {
    
    public static Matrix compute (double[][] paramX , double[] paramY) {
        
        //number of test cases; rows is X
        int m = paramX[0].length;
        //number of parameters
        int n = paramX.length;
        //learning rate
        double alpha = 0.1;
        //convergence treshold
        double treshold = 0.5;
        
        Matrix X = new Matrix(paramX);
        Matrix y = new Matrix(paramY, 1);
        
        //theta is set to zeros at first
        Matrix theta = new Matrix(1, paramX.length);
        //Matrix theta = new Matrix(new double[][]{{1, 5}});
        
        //GRADIENT DESCENT
        double gradientStep = Integer.MAX_VALUE;
        
        //while (gradientStep > treshold) {
        for (int iCount = 0; iCount < 1000; iCount++){
            try {
                Matrix difference = getDifference(theta, X, y);
                Matrix tmpM = repmatByRow(difference, n);
                tmpM = dotProduct(tmpM, X);
                Matrix M = new Matrix(rowSums(tmpM));
                
                theta = theta.minus(M.times(alpha/(m*1.0)));
                //gradientStep = 
            } catch (Exception ex) {
                Logger.getLogger(LinearRegression.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //END OF GRADIENT DESCENT

        System.out.println(Arrays.deepToString(theta.getArray()));
        //System.out.println(cost(getDifference(theta, X, y), X.getArray()[0].length));
        
        return theta;
    }
    
    private static double[][] rowSums(Matrix matrix) {
        double[][] originalArray = matrix.getArray();
        int rows = originalArray.length;
        int cols = originalArray[0].length;
        double[][] result = new double[1][rows];
        
        for (int iCount = 0; iCount < rows; iCount++) {
            double sum = 0.0;
            for (int jCount = 0; jCount < cols; jCount++) {
                sum += originalArray[iCount][jCount];
            }
            result[0][iCount] = sum;
        }
        
        return result;
    }
    
    private static Matrix dotProduct(Matrix a, Matrix b) throws Exception {
        double[][] aArray = a.getArray();
        double[][] bArray = b.getArray();
        
        if (aArray.length != bArray.length || aArray[0].length != bArray[0].length) {
            System.out.println("abcde");
            throw new Exception("Matrices not the same size.");
        }
        
        for (int iCount = 0; iCount < aArray.length; iCount++) {
            for (int jCount = 0; jCount < aArray[0].length; jCount++) {
                aArray[iCount][jCount] *= bArray[iCount][jCount];
            }
        }
        
        return new Matrix(aArray);
    }
    
    private static Matrix repmatByRow(Matrix original, int rows) throws Exception {
        if (rows < 1) {
            throw new Exception("Matrix needs to be multiplied by a number "
                    + "greater than 1");
        }
        
        double[][] originalArray = original.getArray();
        int originalRows = originalArray.length;
        int originalCols = originalArray[0].length;
        double[][] newArray = new double[originalRows * rows][originalCols];
        
        for (int iCount = 0; iCount < originalRows * rows; iCount++) {
            System.arraycopy(originalArray[iCount % originalRows], 0, newArray[iCount], 0, originalCols);
        }
        
        return new Matrix(newArray);
    }
    
    private static Matrix hypothesis(Matrix paramTheta, Matrix paramX) {
        
        Matrix result = paramTheta.times(paramX);
        //System.out.println(Arrays.deepToString(result.getArray()));
        return result;
    }
    
    /**
     * Find the difference between hypotheses and Ys - main part of the cost
     * 
     * @param theta
     * @param X Test cases
     * @param y Real results
     * @return 
     */
    private static Matrix getDifference(Matrix theta, Matrix X, Matrix y) {
        return hypothesis(theta, X).minus(y);
    }
    
    /**
     * Squares a given "difference" vector and returns actual cost for a given
     * difference (difference from real values - y).
     * 
     * @param difference of hypothesis and actual results [(Theta * X) .- y]
     * @param m Number of test cases
     * @return Cost for a single value of vector theta
     */
    private static double cost(Matrix difference, int m) {
        double sum = 0;
        
        //find the sum of squared differences
        double[][] differenceArray = difference.getArray();
        for (int iCount = 0; iCount < differenceArray[0].length; iCount++) {
            sum += differenceArray[0][iCount] * differenceArray[0][iCount];
        }
        
        //multiply sum by 1/(2m)
        return sum * (1.0 / (2.0 * m));
    }
}
