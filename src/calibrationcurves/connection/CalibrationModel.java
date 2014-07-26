package calibrationcurves.connection;

import Jama.Matrix;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class CalibrationModel {
    private int calibration;
    private final ConnectionBase cb;

    public CalibrationModel() {
        this.calibration = -1;
        this.cb = new ConnectionBase();
    }
    
    public CalibrationModel(int calibration) {
        this.calibration = calibration;
        this.cb = new ConnectionBase();
    }
    
    public int getCalibrationIdPk() {
        return calibration;
    }
    
    public void addCalibration(String calibrationName) {
        ConnectionBase lcb = new ConnectionBase();
        this.calibration = lcb.insertAndGetID(
                    "INSERT INTO \"calibrations\" (\"name\") VALUES (\""+ calibrationName +"\")");
    }
    
    public void addMeasurement(String fibrinogen, String time) {
        cb.izvrsiQueryBezRezultata("INSERT INTO \"measurements\" "
                + "(\"time\", \"fibrinogen\", \"calibration_id_fk\") VALUES "
                + "(\""+ time +"\", \""+ fibrinogen +"\", \""+ calibration +"\")");
    }
    
    public void deleteAllLearnedPoints() {
        cb.izvrsiQueryBezRezultata("DELETE FROM learned_points WHERE "
                + "calibration_id_fk = " + calibration);
    }
    
    public void addLearnedPoint(double x, double y) {
        cb.izvrsiQueryBezRezultata(
                "INSERT INTO learned_points (x, y, calibration_id_fk) VALUES "
                + "("+ x +", "+ y +", "+ calibration +")");
    }
    
    public ResultSet getMeasurements() {
        return cb.izvrsiQuery("SELECT measurements_id_pk, time, fibrinogen "
                + "FROM measurements WHERE calibration_id_fk = " + calibration);
    }
    
    public ResultSet getPoints() {
        return cb.izvrsiQuery("SELECT time, fibrinogen " 
                + "FROM measurements WHERE calibration_id_fk = " + calibration);
    }
    
    public ResultSet getLearnedPoints() {
        return cb.izvrsiQuery("SELECT x, y " 
                + "FROM learned_points WHERE calibration_id_fk = " + calibration);
    }
    
    public void deleteMeasurement(String id) {
        cb.izvrsiQueryBezRezultata("DELETE FROM measurements WHERE "
                + "measurements_id_pk = " + id);
    }
    
    public ResultSet getLearnedFunctionParameters() {
        return cb.izvrsiQuery("SELECT * FROM learned_functions "
                    + "WHERE calibration_id_fk = " + calibration);
    }
    
    public void addLearnedFunction(Matrix theta, double[] means, double[] ranges) {
        //first, delete previously learned functions (if any)
        deleteAllLearnedPoints();
        
        double[] m = theta.getRowPackedCopy();
        cb.izvrsiQueryBezRezultata("INSERT INTO \"learned_functions\" "
                + "(\"theta0\", \"theta1\", \"theta2\", \"theta3\", "
                + "\"calibration_id_fk\", \"mean1\", \"mean2\", \"mean3\", "
                + "\"range1\", \"range2\", \"range3\") VALUES "
                + "(\""+ m[0] +"\", \""+ m[1] +"\", \""+ m[2] +"\", \""+ m[3] +"\", \""
                + calibration +"\", \""+means[1]+"\", \""+means[2]+"\", \""+means[3]+"\" "
                + ", \""+ranges[1]+"\", \""+ranges[2]+"\", \""+ranges[3]+"\")"
        );
    }
    
    public int getNumberOfMeasurements() throws SQLException {
        ResultSet rs = cb.izvrsiQuery("SELECT COUNT(*) "
                + "FROM measurements WHERE calibration_id_fk = " + calibration);
            
        return rs.getInt("COUNT(*)");
    }
    
    public ResultSet getUserPoints() {
        return cb.izvrsiQuery("SELECT time, fibrinogen "
                    + "FROM measurements WHERE calibration_id_fk = " + calibration);
    }
    
    public static ResultSet getListOfCalibrations() {
        ConnectionBase lcb = new ConnectionBase();
        return lcb.izvrsiQuery("SELECT * FROM calibrations "
                + "ORDER BY calibration_id_pk DESC");
    }
    
    public static void deleteCalibration(int calibration_id_pk) {
        ConnectionBase lcb = new ConnectionBase();
        lcb.izvrsiQueryBezRezultata("DELETE FROM calibrations "
                + "WHERE calibration_id_pk = " + calibration_id_pk);
    }
}
