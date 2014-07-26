package calibrationcurves.db;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Class used for connection to a sqlite database. It uses a static Connection
 * object, so that user gets the same instance of the connection
 * 
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class ConnectionBase {
    
    private static final String connectionString = "jdbc:sqlite:fibrinogen_curves.sqlite";
    static Connection conn = null;
    
    /**
     * Creates a new Connection object first time. Every other time it just 
     * returns the same instance of the connection.
     * 
     * @return A connection object
     */
    private static Connection cb() {
        try {
            if (conn == null) {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection(connectionString);
                conn.setAutoCommit(true);
            }
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
    
    public ConnectionBase() {}

    /**
     * Executes a query
     * 
     * @param query to be executed
     * @return 
     */
    public ResultSet executeQuery(String query) {
        try {
            Statement stmt = cb().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionBase.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            System.exit(1);
            return null;
        }
    }
    
    /**
     * Executes an insert query and returns the id assigned to the inserted row
     * 
     * @param query to be executed
     * @return primary key of the newly inserted row
     */
    public int insertAndGetID(String query) {
        int result = -1;
        try {
            Statement stmt = cb().createStatement();
            stmt.execute(query);
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                result = Integer.parseInt(rs.getObject(1).toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionBase.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            System.exit(1);
        }
        
        return result;
    }
    
    /**
     * Executes a query with no results (insert, delete etc)
     * 
     * @param query to be executed
     */
    public void executeQueryNoResults(String query) {
        try {
            Statement stmt = cb().createStatement();
            stmt.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionBase.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            System.exit(1);
        }
    }
}
