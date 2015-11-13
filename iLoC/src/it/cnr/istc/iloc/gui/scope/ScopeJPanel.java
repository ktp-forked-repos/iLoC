package it.cnr.istc.iloc.gui.scope;

import it.cnr.istc.iloc.api.IModel;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;

/**
 *
 * @author Riccardo De Benedictis
 */
public class ScopeJPanel extends JPanel {

    /**
     * Creates new form ScopeJPanel
     */
    public ScopeJPanel() {
        initComponents();
        scopeJTree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent tee) throws ExpandVetoException {
                objectTreeModel.createChilds((DefaultMutableTreeNode) tee.getPath().getLastPathComponent());
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent tee) throws ExpandVetoException {
            }
        });
    }

    public void setModel(IModel model) {
        if (model != null) {
            objectTreeCellRenderer.setModel(model);
            objectTreeModel.setModel(model);
        }
        setBorder(model == null ? BorderFactory.createEtchedBorder(Color.red, null) : BorderFactory.createEtchedBorder());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        objectTreeModel = new it.cnr.istc.iloc.gui.scope.ObjectTreeModel();
        objectTreeCellRenderer = new it.cnr.istc.iloc.gui.scope.ObjectTreeCellRenderer();
        scopeJScrollPane = new javax.swing.JScrollPane();
        scopeJTree = new javax.swing.JTree();

        objectTreeCellRenderer.setText("objectTreeCellRenderer1");

        scopeJTree.setModel(objectTreeModel);
        scopeJTree.setCellRenderer(objectTreeCellRenderer);
        scopeJTree.setRootVisible(false);
        scopeJScrollPane.setViewportView(scopeJTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scopeJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scopeJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private it.cnr.istc.iloc.gui.scope.ObjectTreeCellRenderer objectTreeCellRenderer;
    private it.cnr.istc.iloc.gui.scope.ObjectTreeModel objectTreeModel;
    private javax.swing.JScrollPane scopeJScrollPane;
    private javax.swing.JTree scopeJTree;
    // End of variables declaration//GEN-END:variables
}
