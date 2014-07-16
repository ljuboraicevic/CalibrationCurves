package calibrationcurves;

import Jama.Matrix;
import java.util.Arrays;

/**
 *
 * @author ljubo
 */
public class LinearRegression {
    public static Matrix compute (double[][] paramX , double[] paramY) {
        Matrix X = new Matrix(paramX);
        Matrix y = new Matrix(paramY, 1);
        Matrix theta = new Matrix(new double[1][paramX.length]);
        //Matrix theta = new Matrix(new double[][]{{1, 1}});
        
        System.out.println(cost(theta, X, y));
        
        return null;
    }
    
    private static Matrix hypothesis(Matrix paramTheta, Matrix paramX) {
        
        Matrix result = paramTheta.times(paramX);
        //System.out.println(Arrays.deepToString(result.getArray()));
        return result;
    }
    
    private static double cost(Matrix paramTheta, Matrix paramX, Matrix paramY) {
        //find the difference between hypothesises and Ys
        Matrix difference = hypothesis(paramTheta, paramX).minus(paramY);
        
        double sum = 0;
        
        //find the sum of squared differences
        double[][] differenceArray = difference.getArray();
        for (int iCount = 0; iCount < differenceArray[0].length; iCount++) {
            sum += differenceArray[0][iCount] * differenceArray[0][iCount];
        }
        
        //get m, number of test cases
        int m = paramX.getArray()[0].length;
        
        //multiply sum by 1/(2m)
        return sum * (1.0 / (2.0 * m));
    }
}
