package it.cnr.istc.iloc.pddl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis
 */
class TypedListVariableListener extends PDDLBaseListener {

    private final Domain domain;
    final List<Variable> variables = new ArrayList<>();

    TypedListVariableListener(Domain domain) {
        this.domain = domain;
    }

    @Override
    public void enterTyped_list_variable(PDDLParser.Typed_list_variableContext ctx) {
        Type type = null;
        if (ctx.type() == null) {
            type = Type.OBJECT;
        } else if (ctx.type().primitive_type().size() == 1) {
            type = ctx.type().primitive_type(0).name() == null ? Type.OBJECT : domain.getType(Utils.capitalize(ctx.type().primitive_type(0).name().getText()));
        } else {
            type = new EitherType(ctx.type().primitive_type().stream().map(primitive_type -> primitive_type.name() == null ? Type.OBJECT : domain.getType(Utils.capitalize(primitive_type.name().getText()))).collect(Collectors.toList()));
            domain.addType(type);
        }

        assert type != null : "Cannot find type " + ctx.type().primitive_type(0).name().getText();
        Type c_type = type;
        ctx.variable().stream().forEach(variable -> {
            variables.add(new Variable("?" + Utils.lowercase(variable.name().getText()), c_type));
        });
    }
}
