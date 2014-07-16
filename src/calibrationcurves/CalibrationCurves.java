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
                new double[][]{{1, 1, 1}, {3, 2, 1}}, 
                new double[]{1, 2, 3}
        );
    }
    
}
