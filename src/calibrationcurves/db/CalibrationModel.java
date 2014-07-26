package calibrationcurves.db;

import Jama.Matrix;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Model class representing calibration entity. Contains methods implementing
 * CRUD and other operations using SQL (sqlite).
 * 
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class CalibrationModel {
    private int calibration = -1;
    private final ConnectionBase cb;

    /**
     * No-arg constructor used when making a new calibration.
     */
    public CalibrationModel() {
        this.cb = new ConnectionBase();
    }
    
    /**
     * Constructor. Used when you want to access existing calibration.
     * 
     * @param calibration 
     */
    public CalibrationModel(int calibration) {
        this.calibration = calibration;
        this.cb = new ConnectionBase();
    }
    
    /**
     * Get calibration_id_pk from calibrations table
     * 
     * @return calibration_id_pk or -1 if calibration hasn't been set
     */
    public int getCalibrationIdPk() {
        return calibration;
    }
    
    /**
     * Adds a new calibration to calibrations table
     * 
     * @param calibrationName
     */
    public void addCalibration(String calibrationName) {
        this.calibration = cb.insertAndGetID(
                    "INSERT INTO \"calibrations\" (\"name\") VALUES (\""+ calibrationName +"\")");
    }
    
    /**
     * Adds a measurement (user input point)
     * 
     * @param fibrinogen
     * @param time 
     */
    public void addMeasurement(String fibrinogen, String time) {
        cb.executeQueryNoResults("INSERT INTO \"measurements\" "
                + "(\"time\", \"fibrinogen\", \"calibration_id_fk\") VALUES "
                + "(\""+ time +"\", \""+ fibrinogen +"\", \""+ calibration +"\")");
    }
    
    /**
     * Get all points used to plot the learned function
     * 
     * @return 
     */
    public ResultSet getLearnedPoints() {
        return cb.executeQuery("SELECT x, y " 
                + "FROM learned_points WHERE calibration_id_fk = " + calibration);
    }
    
    /**
     * Deletes all points used to plot the learned function
     */
//    public void deleteAllLearnedPoints() {
//        cb.executeQueryNoResults("DELETE FROM learned_points WHERE "
//                + "calibration_id_fk = " + calibration);
//    }
    
    /**
     * Adds a learned point which is used to plot the learned function
     * 
     * @param x
     * @param y 
     */
    public void addLearnedPoint(double x, double y) {
        cb.executeQueryNoResults(
                "INSERT INTO learned_points (x, y, calibration_id_fk) VALUES "
                + "("+ x +", "+ y +", "+ calibration +")");
    }
    
    /**
     * Get all measurements for this calibration
     * 
     * @return ResultSet with measurements data
     */
    public ResultSet getMeasurements() {
        return cb.executeQuery("SELECT measurements_id_pk, time, fibrinogen "
                + "FROM measurements WHERE calibration_id_fk = " + calibration);
    }
    
    /**
     * Delete a measurements
     * 
     * @param id of the measurement to be deleted
     */
    public void deleteMeasurement(String id) {
        cb.executeQueryNoResults("DELETE FROM measurements WHERE "
                + "measurements_id_pk = " + id);
    }
    
    /**
     * Get parameters of the learned function needed to make a prediction
     * 
     * @return ResultSet that contains thetas, means and ranges
     */
    public ResultSet getLearnedFunctionParameters() {
        return cb.executeQuery("SELECT * FROM learned_functions "
                    + "WHERE calibration_id_fk = " + calibration);
    }
    
    /**
     * Add the learned function
     * 
     * @param theta
     * @param means
     * @param ranges 
     */
    public void addLearnedFunction(Matrix theta, double[] means, double[] ranges) {
        
        double[] m = theta.getRowPackedCopy();
        cb.executeQueryNoResults("INSERT INTO \"learned_functions\" "
                + "(\"theta0\", \"theta1\", \"theta2\", \"theta3\", "
                + "\"calibration_id_fk\", \"mean1\", \"mean2\", \"mean3\", "
                + "\"range1\", \"range2\", \"range3\") VALUES "
                + "(\""+ m[0] +"\", \""+ m[1] +"\", \""+ m[2] +"\", \""+ m[3] +"\", \""
                + calibration +"\", \""+means[1]+"\", \""+means[2]+"\", \""+means[3]+"\" "
                + ", \""+ranges[1]+"\", \""+ranges[2]+"\", \""+ranges[3]+"\")"
        );
    }
    
    /**
     * Get the number of measurements for this calibration
     * 
     * @return
     * @throws SQLException 
     */
    public int getNumberOfMeasurements() throws SQLException {
        ResultSet rs = cb.executeQuery("SELECT COUNT(*) "
                + "FROM measurements WHERE calibration_id_fk = " + calibration);
            
        return rs.getInt("COUNT(*)");
    }
    
    /**
     * Static method used for getting the list of all calibrations
     * 
     * @return ResultSet that contains the list of all calibrations
     */
    public static ResultSet getListOfCalibrations() {
        ConnectionBase lcb = new ConnectionBase();
        return lcb.executeQuery("SELECT * FROM calibrations "
                + "ORDER BY calibration_id_pk DESC");
    }
    
    /**
     * Static method used for deleting a calibration (all other data related to
     * the calibration which are in other tables are deleted using triggers)
     * 
     * @param calibration_id_pk to be deleted
     */
    public static void deleteCalibration(int calibration_id_pk) {
        ConnectionBase lcb = new ConnectionBase();
        lcb.executeQueryNoResults("DELETE FROM calibrations "
                + "WHERE calibration_id_pk = " + calibration_id_pk);
    }
}
