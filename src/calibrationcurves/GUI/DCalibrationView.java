package calibrationcurves.GUI;

import Jama.Matrix;
import calibrationcurves.LinearRegression;
import calibrationcurves.db.CalibrationModel;
import calibrationcurves.db.ViewUtils;
import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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

    CalibrationModel calibration;
    
    /**
     * Creates new form DCalibrationView
     * 
     * @param calibration
     * @param parent
     * @param modal
     */
    public DCalibrationView(CalibrationModel calibration,java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.calibration = calibration;
    }
    
    private void displayChart() {
        try {
            //get points for user points calibration
            ResultSet rsMeasurements = calibration.getMeasurements();
            
            //add points to xyseries
            XYSeries points = new XYSeries("Points");
            while (rsMeasurements.next()) {
                points.add(Double.parseDouble(rsMeasurements.getObject("time").toString()), 
                        Double.parseDouble(rsMeasurements.getObject("fibrinogen").toString()));
            }
            
            //add learned function to xyseries
            //get points from database
            ResultSet rsLearned = calibration.getLearnedPoints();
            
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
            int rows = calibration.getNumberOfMeasurements();
            
            double[][] X = new double[4][rows];
            double[] y = new double[rows];
            
            //get measurements (user input points)
            ResultSet userPoints = calibration.getMeasurements();
            
            int iCount = 0;
            double x1sum = 0;
            double x2sum = 0;
            double x3sum = 0;
            double x1min = Double.POSITIVE_INFINITY;
            double x2min = Double.POSITIVE_INFINITY;
            double x3min = Double.POSITIVE_INFINITY;
            double x1max = Double.NEGATIVE_INFINITY;
            double x2max = Double.NEGATIVE_INFINITY;
            double x3max = Double.NEGATIVE_INFINITY;
            
            //populate input
            while (userPoints.next()) {
                y[iCount] = Double.parseDouble(userPoints.getString("fibrinogen"));
                double x = Double.parseDouble(userPoints.getString("time"));
                //adding x to third power
                X[0][iCount] = 1;
                X[1][iCount] = x;
                X[2][iCount] = x*x;
                X[3][iCount] = x*x*x;
                
                //get the sum, min and max per column (parameter e.g. X1, X2...)
                x1sum += X[1][iCount];
                x2sum += X[2][iCount];
                x3sum += X[3][iCount];
                
                if (X[1][iCount] < x1min) {
                    x1min = X[1][iCount];
                }
                
                if (X[2][iCount] < x2min) {
                    x2min = X[2][iCount];
                }
                
                if (X[3][iCount] < x3min) {
                    x3min = X[3][iCount];
                }
                
                if (X[1][iCount] > x1max) {
                    x1max = X[1][iCount];
                }
                
                if (X[2][iCount] > x2max) {
                    x2max = X[2][iCount];
                }
                
                if (X[3][iCount] > x3max) {
                    x3max = X[3][iCount];
                }

                iCount++;
            }
            
            double x1mean = x1sum / iCount;
            double x2mean = x2sum / iCount;
            double x3mean = x3sum / iCount;
            double x1range = x1max - x1min;
            double x2range = x2max - x2min;
            double x3range = x3max - x3min;
            
            //mean normalization; (x - mean) / range
            for (iCount = 0; iCount < X[0].length; iCount++) {
                X[1][iCount] = (X[1][iCount] - x1mean) / x1range;
                X[2][iCount] = (X[2][iCount] - x2mean) / x2range;
                X[3][iCount] = (X[3][iCount] - x3mean) / x3range;
            }
            
            double[] means  = new double[4];
            means[1] = x1mean;
            means[2] = x2mean;
            means[3] = x3mean;
            
            double[] ranges = new double[4];
            ranges[1] = x1range;
            ranges[2] = x2range;
            ranges[3] = x3range;
            
            Matrix theta = LinearRegression.compute(X, y, 0.6, 5000);
            calibration.addLearnedFunction(theta, means, ranges);
            
            //calculate 20 points for learned function
            double step = (x1max - x1min) / 18;
            int start = (int) (x1min / step);
            double end = start + step * 20.0;
            
            double[][] twentyPoints = calibration.getBatchPredictions(start, end, step);
            
            for (iCount = 0; iCount < twentyPoints.length; iCount++) {
                calibration.addLearnedPoint(twentyPoints[iCount][0], twentyPoints[iCount][1]);
            }
            
            //plot the chart
            displayChart();
        } catch (SQLException ex) {
            Logger.getLogger(DCalibrationView.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        btnCalculator = new javax.swing.JButton();
        btnTable = new javax.swing.JButton();

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
            .addGap(0, 489, Short.MAX_VALUE)
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

        btnCalculator.setText("Calculator");
        btnCalculator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculatorActionPerformed(evt);
            }
        });

        btnTable.setText("Show Table");
        btnTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTableActionPerformed(evt);
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
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btnCalculator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(btnAdd)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnDelete)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(btnLearn))
                                    .addComponent(btnTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd)
                            .addComponent(btnDelete)
                            .addComponent(btnLearn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCalculator)
                            .addComponent(btnTable)))
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
        ResultSet measurements = calibration.getMeasurements();
        tMeasurements.setModel(ViewUtils.napraviROTableModel(measurements));
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
            calibration.deleteMeasurement(id);
        } else {
            JOptionPane.showMessageDialog(null, "No measurement chosen.");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnLearnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLearnActionPerformed
        if (tMeasurements.getModel().getRowCount() < 2) {
            JOptionPane.showMessageDialog(null, "Calibration must contain "
                    + "at least two measurements.");
        } else {
            learn();
        }
    }//GEN-LAST:event_btnLearnActionPerformed

    private void btnCalculatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculatorActionPerformed
        DCalculator dc = new DCalculator(calibration, null, true);
        dc.setLocationRelativeTo(this);
        dc.setVisible(true);
    }//GEN-LAST:event_btnCalculatorActionPerformed

    private void btnTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTableActionPerformed
        DTableValues dtv = new DTableValues(calibration, null, true);
        dtv.setLocationRelativeTo(this);
        dtv.setVisible(true);
    }//GEN-LAST:event_btnTableActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCalculator;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLearn;
    private javax.swing.JButton btnTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JTable tMeasurements;
    // End of variables declaration//GEN-END:variables
}
