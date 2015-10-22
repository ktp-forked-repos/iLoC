package it.cnr.istc.iloc.gui.scope;

import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IEnum;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IString;
import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Riccardo De Benedictis
 */
public class ObjectTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final ImageIcon OBJECT_ICON = new ImageIcon(ObjectTreeCellRenderer.class.getResource("../resources/object.png"));
    private static final ImageIcon ACTIVE_FORMULA_ICON = new ImageIcon(ObjectTreeCellRenderer.class.getResource("../resources/formula.png"));
    private static final ImageIcon INACTIVE_FORMULA_ICON = new ImageIcon(ObjectTreeCellRenderer.class.getResource("../resources/inactive_formula.png"));
    private static final ImageIcon UNIFIED_FORMULA_ICON = new ImageIcon(ObjectTreeCellRenderer.class.getResource("../resources/unified_formula.png"));
    private static final ImageIcon ENUM_ICON = new ImageIcon(ObjectTreeCellRenderer.class.getResource("../resources/enum.png"));
    private static final ImageIcon BOOL_ICON = new ImageIcon(ObjectTreeCellRenderer.class.getResource("../resources/bool.png"));
    private static final ImageIcon NUMBER_ICON = new ImageIcon(ObjectTreeCellRenderer.class.getResource("../resources/number.png"));
    private static final ImageIcon STRING_ICON = new ImageIcon(ObjectTreeCellRenderer.class.getResource("../resources/enum.png"));
    private IModel model;

    public void setModel(IModel model) {
        this.model = model;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof FieldNode) {
            FieldNode field_node = (FieldNode) value;
            IField field = field_node.getField();
            IEnvironment environment = field_node.getEnvironment();
            IObject object = environment.get(field.getName());
            switch (field.getType().getName()) {
                case Constants.BOOL:
                    setIcon(BOOL_ICON);
                    setText(field.getName() + " = " + model.evaluate((IBool) object));
                    break;
                case Constants.NUMBER:
                case Constants.INT:
                case Constants.REAL:
                    setIcon(NUMBER_ICON);
                    setText(field.getName() + " = " + new DecimalFormat("#0.##").format(model.evaluate((INumber) object)));
                    break;
                case Constants.STRING:
                    setIcon(STRING_ICON);
                    setText(field.getName() + " = " + model.evaluate((IString) object));
                    break;
                default:
                    if (object instanceof IEnum) {
                        setIcon(ENUM_ICON);
                    } else {
                        setIcon(OBJECT_ICON);
                    }
                    setText(field.getName());
                    break;
            }
        } else if (value instanceof FormulaNode) {
            FormulaNode formula_node = (FormulaNode) value;
            switch (formula_node.getFormula().getFormulaState()) {
                case Inactive:
                    setIcon(INACTIVE_FORMULA_ICON);
                    break;
                case Active:
                    setIcon(ACTIVE_FORMULA_ICON);
                    break;
                case Unified:
                    setIcon(UNIFIED_FORMULA_ICON);
                    break;
                default:
                    throw new AssertionError(formula_node.getFormula().getFormulaState().name());
            }
            setText(formula_node.getFormula().getType().getName());
        }
        return this;
    }
}
