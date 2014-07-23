package calibrationcurves;

import Jama.Matrix;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Linear regression.
 * 
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class LinearRegression {
    
    /**
     * Calculates linear regression, given feature set X and target set y
     * 
     * @param paramX - # of rows is number of parameters, # of columns is number of training examples
     * @param paramY - # of rows is number of training examples
     * @param alpha Learning rate
     * @param gradDescRepeat how many times to repeat gradient descent
     * @return Matrix theta which contains one theta parameter for each parameter
     * in X (row in X)
     */
    public static Matrix compute (double[][] paramX , double[] paramY, 
            double alpha, int gradDescRepeat) {
        
        //number of test cases; rows is X
        int m = paramX[0].length;
        //number of parameters
        int n = paramX.length;
        //convergence treshold; currently not used (gradient descent repeats a
        //fixed number of times). TODO implement it with treshold
        //double treshold = 0.5;
        
        //create feature set matrix X, and target set matrix y from arguments
        Matrix X = new Matrix(paramX);
        Matrix y = new Matrix(paramY, 1); //horizontal vector
        
        //theta is set to zeros at first
        Matrix theta = new Matrix(1, paramX.length);
        
        //GRADIENT DESCENT
        //repeat gradient descent a fixed number of times
        for (int iCount = 0; iCount < gradDescRepeat; iCount++){
            try {
                //get the difference between this iteration of gradient descent
                //and expected target set y
                Matrix difference = getDifference(theta, X, y);
                
                //vectorized derivative of the difference
                Matrix tmpM = JAMAMatrixUtils.repmatByRow(difference, n);
                tmpM.arrayTimesEquals(X);
                Matrix M = new Matrix(JAMAMatrixUtils.rowSums(tmpM));
                
                //"move" theta a little closer to optimum
                theta = theta.minus(M.times(alpha/(m*1.0)));
            } catch (Exception ex) {
                Logger.getLogger(LinearRegression.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //END OF GRADIENT DESCENT
        
        return theta;
    }
    
    /**
     * Vectorized calculation of hypothesis
     * 
     * @param paramTheta vector theta
     * @param paramX feature set 
     * @return vector theta times X
     */
    private static Matrix hypothesis(Matrix paramTheta, Matrix paramX) {
        return paramTheta.times(paramX);
    }
    
    /**
     * Find the difference between hypotheses (feature set times theta)
     * and target set y; main part of the cost
     * 
     * @param theta parameters
     * @param X feature set
     * @param y target set
     * @return Difference between this value of theta with expected target set y
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
