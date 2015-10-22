package it.cnr.istc.iloc.gui.scope;

import it.cnr.istc.iloc.api.IFormula;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Riccardo De Benedictis
 */
class FormulaNode extends DefaultMutableTreeNode {

    private final IFormula formula;

    FormulaNode(IFormula formula) {
        super(formula, formula.getType().getFields().values().stream().anyMatch(f -> !f.isSynthetic()));
        this.formula = formula;
    }

    @Override
    public boolean isLeaf() {
        return !getAllowsChildren();
    }

    public IFormula getFormula() {
        return formula;
    }
}
