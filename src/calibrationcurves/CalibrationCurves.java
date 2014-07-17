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
                //y = 2x^2 + 2
                new double[][]{{1, 1, 1, 1, 1}, {-2, -1, 0, 1, 2},  {4, 1, 0, 1, 4}}, 
                new double[]{10.1, 3.94, 2.05, 4.04, 9.91}
        );
    }
    
}
