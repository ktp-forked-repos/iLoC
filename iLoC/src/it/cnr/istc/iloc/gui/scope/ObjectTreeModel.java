package it.cnr.istc.iloc.gui.scope;

import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import it.cnr.istc.iloc.gui.SolverManager;
import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Riccardo De Benedictis
 */
public class ObjectTreeModel extends DefaultTreeModel {

    private IModel model;

    public ObjectTreeModel() {
        super(new DefaultMutableTreeNode());
    }

    public void setModel(IModel model) {
        if (this.model != model) {
            this.model = model;
            setRoot(new DefaultMutableTreeNode());
            createChilds((DefaultMutableTreeNode) root);
            fireTreeNodesChanged(this, null, null, null);
        }
    }

    public void createChilds(DefaultMutableTreeNode tree_node) {
        tree_node.removeAllChildren();
        if (tree_node instanceof FieldNode) {
            FieldNode field_node = (FieldNode) tree_node;
            IEnvironment environment = field_node.getEnvironment();
            IObject object = environment.get(model, field_node.getField().getName());
            getFields(object.getType()).values().stream().filter(field -> !field.isSynthetic()).forEach(field -> {
                field_node.add(new FieldNode(object, field));
            });
            object.getType().getPredicates().values().stream().flatMap(predicate -> predicate.getInstances().stream().map(instance -> (IFormula) instance)).filter(formula -> formula.get(model, Constants.SCOPE) == object).forEach(formula -> {
                field_node.add(new FormulaNode(formula));
            });
        } else if (tree_node instanceof FormulaNode) {
            FormulaNode formula_node = (FormulaNode) tree_node;
            formula_node.getFormula().getType().getFields().values().stream().filter(field -> !field.isSynthetic()).forEach(field -> {
                tree_node.add(new FieldNode(formula_node.getFormula(), field));
            });
        } else {
            ISolver solver = SolverManager.getInstance().getSolver();
            solver.getFields().values().stream().filter(field -> !field.isSynthetic()).forEach(field -> {
                if (solver.get(field.getName()) instanceof IFormula) {
                    tree_node.add(new FormulaNode(solver.get(field.getName())));
                } else {
                    tree_node.add(new FieldNode(solver, field));
                }
            });
            solver.getPredicates().values().stream().flatMap(predicate -> predicate.getInstances().stream().map(instance -> (IFormula) instance)).forEach(formula -> {
                tree_node.add(new FormulaNode(formula));
            });
        }
        int[] child_indices = new int[tree_node.getChildCount()];
        Object[] children = new Object[tree_node.getChildCount()];
        for (int i = 0; i < child_indices.length; i++) {
            child_indices[i] = i;
            children[i] = tree_node.getChildAt(i);
        }
        fireTreeStructureChanged(this, tree_node.getPath(), child_indices, children);
    }

    static Map<String, IField> getFields(IType type) {
        Map<String, IField> fields = new HashMap<>();
        IType c_type = type;
        while (c_type != null) {
            c_type.getFields().values().stream().filter(field -> !fields.containsKey(field.getName())).forEach(field -> fields.put(field.getName(), field));
            c_type = c_type.getSuperclass();
        }
        return fields;
    }
}
