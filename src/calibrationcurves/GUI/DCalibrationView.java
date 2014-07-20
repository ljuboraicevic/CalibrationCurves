package calibrationcurves.GUI;

import Jama.Matrix;
import calibrationcurves.LinearRegression;
import calibrationcurves.connection.ConnectionBase;
import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class DCalibrationView extends javax.swing.JDialog {

    ConnectionBase cb;
    int calibration;
    
    /**
     * Creates new form DCalibrationView
     * 
     * @param calibration
     * @param parent
     * @param modal
     */
    public DCalibrationView(int calibration,java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        cb = new ConnectionBase();
        this.calibration = calibration;
    }
    
    private DefaultTableModel loadMeasurements() {
        ResultSet rs = cb.izvrsiQuery("SELECT measurements_id_pk, time, fibrinogen "
                + "FROM measurements WHERE calibration_id_fk = " + calibration);
        return cb.napraviROTableModel(rs);
    }
    
    private void deleteMeasurement(String id) {
        cb.izvrsiQueryBezRezultata("DELETE FROM measurements WHERE "
                + "measurements_id_pk = " + id);
    }
        
    private void displayChart() {
        try {
            //get points from database
            ResultSet rsPoints = cb.izvrsiQuery("SELECT time, fibrinogen "
                    + "FROM measurements WHERE calibration_id_fk = " + calibration);
            
            //add points to xyseries
            XYSeries points = new XYSeries("Points");
            while (rsPoints.next()) {
                points.add(Double.parseDouble(rsPoints.getObject(1).toString()), 
                        Double.parseDouble(rsPoints.getObject(2).toString()));
            }
            
            //add learned function to xyseries
            //get points from database
            ResultSet rsLearned = cb.izvrsiQuery("SELECT x, y "
                    + "FROM learned_points WHERE calibration_id_fk = " + calibration);
            
            //add points to xyseries
            XYSeries learned = new XYSeries("Learned");
            while (rsLearned.next()) {
                learned.add(Double.parseDouble(rsLearned.getObject(1).toString()), 
                        Double.parseDouble(rsLearned.getObject(2).toString()));
            }
            
            //create a dataset
            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(points);
            dataset.addSeries(learned);
            
            //create the chart
            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Measurements", 
                    "Time", 
                    "Fibrinogen", 
                    dataset, 
                    PlotOrientation.VERTICAL, 
                    false, true, false
            );
            
            XYPlot plot = chart.getXYPlot();
            XYLineAndShapeRenderer r = new XYLineAndShapeRenderer();
            r.setSeriesLinesVisible(0, false);
            r.setSeriesShapesVisible(1, false);
            
            plot.setRenderer(r);
            
            ChartPanel cp = new ChartPanel(chart);
            //cp.setPreferredSize(new java.awt.Dimension(500, 270));
            rightPanel.setLayout(new java.awt.BorderLayout());
            rightPanel.add(cp, BorderLayout.CENTER);
            rightPanel.validate();
            
        } catch (SQLException ex) {
            Logger.getLogger(DCalibrationView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void learn() {
        try {
            ResultSet rs = cb.izvrsiQuery("SELECT COUNT(*) "
                    + "FROM measurements WHERE calibration_id_fk = " + calibration);
            
            int rows = rs.getInt("COUNT(*)");
            
            double[][] X = new double[3][rows];
            double[] y = new double[rows];
            
            rs = cb.izvrsiQuery("SELECT time, fibrinogen "
                    + "FROM measurements WHERE calibration_id_fk = " + calibration);
            
            int iCount = 0;
            
            //populate input
            while (rs.next()) {
                y[iCount] = Double.parseDouble(rs.getString("fibrinogen"));
                double x = Double.parseDouble(rs.getString("time"));
                //adding x to third power
                X[0][iCount] = 1;
                X[1][iCount] = x;
                X[2][iCount] = x*x;
                //X[3][iCount] = x*x*x;
                iCount++;
            }
            
            Matrix theta = LinearRegression.compute(X, y);
            addLearnedFunction(theta);
        } catch (SQLException ex) {
            Logger.getLogger(DCalibrationView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void addLearnedFunction(Matrix theta) {
        //first, delete previously learned functions (if any)
        cb.izvrsiQueryBezRezultata("DELETE FROM learned_functions "
                + "WHERE calibration_id_fk = " + calibration);
        
        double[] m = theta.getRowPackedCopy();
        cb.izvrsiQueryBezRezultata("INSERT INTO \"learned_functions\" "
                //+ "(\"theta0\", \"theta1\", \"theta2\", \"theta3\", \"calibration_id_fk\") VALUES "
                //+ "(\""+ m[0] +"\", \""+ m[1] +"\", \""+ m[2] +"\", \""+ m[3] +"\", \""+ calibration +"\")"
                + "(\"theta0\", \"theta1\", \"theta2\", \"calibration_id_fk\") VALUES "
                + "(\""+ m[0] +"\", \""+ m[1] +"\", \""+ m[2] +"\", \""+ calibration +"\")"
                        );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tMeasurements = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        rightPanel = new javax.swing.JPanel();
        btnLearn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        tMeasurements.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tMeasurements);

        jLabel1.setText("Measurements");

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 493, Short.MAX_VALUE)
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        btnLearn.setText("Learn");
        btnLearn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLearnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAdd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnLearn))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd)
                            .addComponent(btnDelete)
                            .addComponent(btnLearn)))
                    .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        DAddMeasurement dam = new DAddMeasurement(calibration, null, true);
        dam.setLocationRelativeTo(this);
        dam.setVisible(true);
    }//GEN-LAST:event_btnAddActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        tMeasurements.setModel(loadMeasurements());
        tMeasurements.getColumnModel().removeColumn(tMeasurements.getColumnModel().getColumn(0));
        displayChart();
    }//GEN-LAST:event_formWindowActivated

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        //ukoliko je bar neki element tabele izabran
        //i ako korisnik pritisne da kada mu iskoci dialog
        Object[] options = {"Yes","No"};
        
        if (tMeasurements.getSelectedRow() > -1
                && JOptionPane.showOptionDialog(
                        this, 
                        "Delete measurement?", 
                        "Delete measurement", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.WARNING_MESSAGE, 
                        null, 
                        options, 
                        options[1]) == 0
        ) {
            String id = tMeasurements.getModel().getValueAt(tMeasurements.getSelectedRow(), 0).toString();
            deleteMeasurement(id);
        } else {
            JOptionPane.showMessageDialog(null, "No measurement chosen.");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnLearnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLearnActionPerformed
        learn();
    }//GEN-LAST:event_btnLearnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLearn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JTable tMeasurements;
    // End of variables declaration//GEN-END:variables
}
