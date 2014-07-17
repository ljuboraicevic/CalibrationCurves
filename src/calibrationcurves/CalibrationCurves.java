package calibrationcurves;

/**
 *
 * @author ljubo
 */
public class CalibrationCurves {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LinearRegression.compute(
//                new double[][]{{1, 1, 1, 1}, {1, 2, 3, 4}}, 
//                new double[]{3, 5, 7, 9}
                new double[][]{{1, 1, 1, 1, 1}, {-2, -0.95, 0.1, 1.05, 2.02}}, 
                new double[]{-15.1, -10.91, -6.94, -3.1, 1.1}
        );
    }
    
}
