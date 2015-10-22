package it.cnr.istc.iloc.gui.scope;

import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IField;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Riccardo De Benedictis
 */
class FieldNode extends DefaultMutableTreeNode {

    private final IEnvironment environment;
    private final IField field;

    FieldNode(IEnvironment environment, IField field) {
        super(field, ObjectTreeModel.getFields(environment.get(field.getName()).getType()).values().stream().anyMatch(f -> !f.isSynthetic()));
        this.environment = environment;
        this.field = field;
    }

    @Override
    public boolean isLeaf() {
        return !getAllowsChildren();
    }

    public IEnvironment getEnvironment() {
        return environment;
    }

    public IField getField() {
        return field;
    }
}
