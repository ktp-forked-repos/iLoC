package it.cnr.istc.iloc.pddl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Riccardo De Benedictis
 */
class Predicate {

    private final String name;
    private final List<Variable> variables = new ArrayList<>();

    Predicate(String name, Variable... variables) {
        this.name = name;
        this.variables.addAll(Arrays.asList(variables));
    }

    public String getName() {
        return name;
    }

    public List<Variable> getVariables() {
        return Collections.unmodifiableList(variables);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(name).append("\n");
        variables.forEach(variable -> {
            sb.append("  ").append(variable.getName()).append(" - ").append(variable.getType().getName()).append("\n");
        });
        sb.append(")");
        return sb.toString();
    }
}
