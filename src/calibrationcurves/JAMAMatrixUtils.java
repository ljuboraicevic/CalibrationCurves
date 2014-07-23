package calibrationcurves;

import Jama.Matrix;

/**
 * Implements some useful matrix operations not included in Jama library.
 * 
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class JAMAMatrixUtils {
    
    /**
     * Repmats a matrix "vertically" (see octave repmat function). E.g
     * M =
     * a b
     * c d
     * repmatByRow(M, 2) = 
     * a b
     * c d
     * a b
     * c d
     * 
     * @param original Matrix to be repmat-ed.
     * @param times How many times to repmat
     * @return New repmat-ed matrix
     * @throws Exception Cannot repmat by zero or a negative number
     */
    public static Matrix repmatByRow(Matrix original, int times) throws Exception {
        if (times < 1) {
            throw new Exception("Matrix needs to be multiplied by a number "
                    + "greater or equal to 1");
        }
        
        double[][] originalArray = original.getArray();
        int originalRows = originalArray.length;
        int originalCols = originalArray[0].length;
        double[][] newArray = new double[originalRows * times][originalCols];
        
        for (int iCount = 0; iCount < originalRows * times; iCount++) {
            System.arraycopy(originalArray[iCount % originalRows], 0, newArray[iCount], 0, originalCols);
        }
        
        return new Matrix(newArray);
    }
    
    /**
     * Sums per row in a double matrix
     * 
     * @param matrix Input matrix
     * @return double matrix containing sums per row of the input matrix
     */
    public static double[][] rowSums(Matrix matrix) {
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
}
