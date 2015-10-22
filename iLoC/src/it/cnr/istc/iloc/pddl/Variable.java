package it.cnr.istc.iloc.pddl;

/**
 *
 * @author Riccardo De Benedictis
 */
class Variable {

    private final String name;
    private final Type type;

    Variable(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.getName() + " " + name;
    }
}
