package calibrationcurves.GUI;

import calibrationcurves.db.CalibrationModel;
import calibrationcurves.db.ViewUtils;
import javax.swing.JOptionPane;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class DAddMeasurement extends javax.swing.JDialog {

    CalibrationModel calibration;
    
    /**
     * Creates new form DAddMeasurement
     */
    public DAddMeasurement(CalibrationModel calibration, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.calibration = calibration;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfFibrinogen = new javax.swing.JTextField();
        tfTime = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tfFibrinogen.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tfFibrinogen.setText("0");

        tfTime.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tfTime.setText("0");

        jLabel1.setText("Fibrinogen:");

        jLabel2.setText("Time:");

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(tfTime)
                    .addComponent(tfFibrinogen))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfFibrinogen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdd)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (tfFibrinogen.getText().length() > 0 &&
                ViewUtils.isDouble(tfFibrinogen.getText()) &&
                tfTime.getText().length() > 0 &&
                ViewUtils.isDouble(tfTime.getText())
        ) {
            calibration.addMeasurement(tfFibrinogen.getText(), tfTime.getText());
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Time and fibrinogen values "
                    + "must be numeric.");
        }
    }//GEN-LAST:event_btnAddActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField tfFibrinogen;
    private javax.swing.JTextField tfTime;
    // End of variables declaration//GEN-END:variables
}
