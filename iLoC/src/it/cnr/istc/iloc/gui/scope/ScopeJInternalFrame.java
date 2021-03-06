package it.cnr.istc.iloc.gui.scope;

import it.cnr.istc.iloc.api.IModel;
import javax.swing.JInternalFrame;

/**
 *
 * @author Riccardo De Benedictis
 */
public class ScopeJInternalFrame extends JInternalFrame {

    /**
     * Creates new form ScopeJInternalFrame
     */
    public ScopeJInternalFrame() {
        initComponents();
    }

    public void setModel(IModel model) {
        scopeJPanel.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scopeJPanel = new it.cnr.istc.iloc.gui.scope.ScopeJPanel();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Scope");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scopeJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scopeJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private it.cnr.istc.iloc.gui.scope.ScopeJPanel scopeJPanel;
    // End of variables declaration//GEN-END:variables
}
