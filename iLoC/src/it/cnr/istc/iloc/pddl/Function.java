package it.cnr.istc.iloc.pddl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Riccardo De Benedictis
 */
class Function {

    private final String name;
    private final Type type;
    private final List<Variable> variables = new ArrayList<>();

    Function(String name, Type type, Variable... variables) {
        assert type != null;
        this.name = name;
        this.type = type;
        this.variables.addAll(Arrays.asList(variables));
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public List<Variable> getVariables() {
        return Collections.unmodifiableList(variables);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(name).append("\n");
        variables.stream().forEach(variable -> {
            sb.append("  ").append(variable.getName()).append(" - ").append(variable.getType().getName()).append("\n");
        });
        sb.append(")");
        return sb.toString();
    }
}
