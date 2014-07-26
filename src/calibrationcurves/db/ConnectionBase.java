package calibrationcurves.db;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ljubo
 */
public class ConnectionBase {
    
    private static final String connectionString = "jdbc:sqlite:fibrinogen_curves.sqlite";
    static Connection conn = null;
    
    private static Connection cb() {
        try {
            if (conn == null) {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection(connectionString);
                conn.setAutoCommit(true);
            }
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
    
    public ConnectionBase() {}

    public ResultSet izvrsiQuery(String kveri) {
        try {
            //ovde pravimo statement i izvrsavamo ga pomocu konekcije
            Statement stmt = cb().createStatement();
            ResultSet rs = stmt.executeQuery(kveri);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionBase.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            System.exit(1);
            return null;
        }
    }
    
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
    
    public void izvrsiQueryBezRezultata(String kveri) {
        try {
            //ovde pravimo statement i izvrsavamo ga pomocu konekcije
            Statement stmt = cb().createStatement();
            stmt.execute(kveri);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionBase.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            System.exit(1);
        }
    }
}
