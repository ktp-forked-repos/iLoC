package it.cnr.istc.iloc.gui.timelines;

import it.cnr.istc.iloc.api.IModel;

/**
 *
 * @author Riccardo De Benedictis
 */
public class TimelinesJInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form TimelinesJInternalFrame
     */
    public TimelinesJInternalFrame() {
        initComponents();
    }

    public void setModel(IModel model) {
        timelinesChart.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        timelinesChart = new it.cnr.istc.iloc.gui.timelines.TimelinesChart();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Timelines");

        javax.swing.GroupLayout timelinesChartLayout = new javax.swing.GroupLayout(timelinesChart);
        timelinesChart.setLayout(timelinesChartLayout);
        timelinesChartLayout.setHorizontalGroup(
            timelinesChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        timelinesChartLayout.setVerticalGroup(
            timelinesChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 442, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(timelinesChart, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(timelinesChart, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private it.cnr.istc.iloc.gui.timelines.TimelinesChart timelinesChart;
    // End of variables declaration//GEN-END:variables
}
