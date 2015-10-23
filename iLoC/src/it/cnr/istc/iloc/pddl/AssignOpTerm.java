/*
 * Copyright (C) 2015 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package it.cnr.istc.iloc.pddl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class AssignOpTerm implements Term {

    private final AssignOp assignOp;
    private final FunctionTerm functionTerm;
    private final Term value;

    AssignOpTerm(AssignOp assignOp, FunctionTerm functionTerm, Term value) {
        assert assignOp != null;
        assert functionTerm != null;
        this.assignOp = assignOp;
        this.functionTerm = functionTerm;
        this.value = value;
    }

    public AssignOp getAssignOp() {
        return assignOp;
    }

    public FunctionTerm getFunctionTerm() {
        return functionTerm;
    }

    public Term getValue() {
        return value;
    }

    @Override
    public Term negate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Term ground(Domain domain, Map<String, Term> known_terms) {
        return new AssignOpTerm(assignOp, (FunctionTerm) functionTerm.ground(domain, known_terms), value.ground(domain, known_terms));
    }

    @Override
    public List<Term> containsPredicate(boolean directed, Predicate predicate) {
        return Collections.emptyList();
    }

    @Override
    public List<Term> containsFunction(Function function) {
        if (!functionTerm.containsFunction(function).isEmpty()) {
            return Arrays.asList(this);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public String toString(STGroupFile file, Map<String, String> known_terms, Set<Term> to_skip, Mode mode) {
        if (to_skip.contains(functionTerm)) {
            return "";
        }
        switch (assignOp) {
            case Assign:
                if (value instanceof FunctionTerm) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(functionTerm.toString(file, known_terms, to_skip, mode)).append("\n");
                    known_terms.put("value", Utils.lowercase(functionTerm.getFunction().getName()) + ".value");
                    sb.append(value.toString(file, known_terms, to_skip, mode));
                    return sb.toString();
                } else {
                    known_terms.put("value", value.toString(file, known_terms, to_skip, mode));
                    return functionTerm.toString(file, known_terms, to_skip, mode);
                }
            case ScaleUp:
                StringBuilder scale_up_sb = new StringBuilder();
                switch (mode) {
                    case Effect:
                        scale_up_sb.append(functionTerm.toString(file, known_terms, to_skip, Mode.Condition)).append("\n");
                        break;
                    case AtStartEffect:
                        scale_up_sb.append(functionTerm.toString(file, known_terms, to_skip, Mode.AtStartCondition)).append("\n");
                        break;
                    case AtEndEffect:
                        scale_up_sb.append(functionTerm.toString(file, known_terms, to_skip, Mode.AtEndCondition)).append("\n");
                        break;
                    default:
                        throw new AssertionError(mode.name());
                }
                if (value instanceof FunctionTerm) {
                    scale_up_sb.append(value.toString(file, known_terms, to_skip, mode));
                    known_terms.put("value", Utils.lowercase(functionTerm.getFunction().getName()) + ".value * " + Utils.lowercase(((FunctionTerm) value).getFunction().getName()) + ".value");
                    scale_up_sb.append(functionTerm.toString(file, known_terms, to_skip, mode)).append("\n");
                    return scale_up_sb.toString();
                } else {
                    known_terms.put("value", Utils.lowercase(functionTerm.getFunction().getName()) + ".value * " + value.toString(file, known_terms, to_skip, mode));
                    scale_up_sb.append(functionTerm.toString(file, known_terms, to_skip, mode));
                    return scale_up_sb.toString();
                }
            case ScaleDown:
                StringBuilder scale_down_sb = new StringBuilder();
                switch (mode) {
                    case Effect:
                        scale_down_sb.append(functionTerm.toString(file, known_terms, to_skip, Mode.Condition)).append("\n");
                        break;
                    case AtStartEffect:
                        scale_down_sb.append(functionTerm.toString(file, known_terms, to_skip, Mode.AtStartCondition)).append("\n");
                        break;
                    case AtEndEffect:
                        scale_down_sb.append(functionTerm.toString(file, known_terms, to_skip, Mode.AtEndCondition)).append("\n");
                        break;
                    default:
                        throw new AssertionError(mode.name());
                }
                if (value instanceof FunctionTerm) {
                    scale_down_sb.append(value.toString(file, known_terms, to_skip, mode));
                    known_terms.put("value", Utils.lowercase(functionTerm.getFunction().getName()) + ".value / " + Utils.lowercase(((FunctionTerm) value).getFunction().getName()) + ".value");
                    scale_down_sb.append(functionTerm.toString(file, known_terms, to_skip, mode)).append("\n");
                    return scale_down_sb.toString();
                } else {
                    known_terms.put("value", Utils.lowercase(functionTerm.getFunction().getName()) + ".value / " + value.toString(file, known_terms, to_skip, mode));
                    scale_down_sb.append(functionTerm.toString(file, known_terms, to_skip, mode));
                    return scale_down_sb.toString();
                }
            case Increase:
                StringBuilder increase_sb = new StringBuilder();
                switch (mode) {
                    case Effect:
                        increase_sb.append(functionTerm.toString(file, known_terms, to_skip, Mode.Condition)).append("\n");
                        break;
                    case AtStartEffect:
                        increase_sb.append(functionTerm.toString(file, known_terms, to_skip, Mode.AtStartCondition)).append("\n");
                        break;
                    case AtEndEffect:
                        increase_sb.append(functionTerm.toString(file, known_terms, to_skip, Mode.AtEndCondition)).append("\n");
                        break;
                    default:
                        throw new AssertionError(mode.name());
                }
                if (value instanceof FunctionTerm) {
                    increase_sb.append(value.toString(file, known_terms, to_skip, mode));
                    known_terms.put("value", Utils.lowercase(functionTerm.getFunction().getName()) + ".value + " + Utils.lowercase(((FunctionTerm) value).getFunction().getName()) + ".value");
                    increase_sb.append(functionTerm.toString(file, known_terms, to_skip, mode)).append("\n");
                    return increase_sb.toString();
                } else {
                    known_terms.put("value", Utils.lowercase(functionTerm.getFunction().getName()) + ".value + " + value.toString(file, known_terms, to_skip, mode));
                    increase_sb.append(functionTerm.toString(file, known_terms, to_skip, mode));
                    return increase_sb.toString();
                }
            case Decrease:
                StringBuilder decrease_sb = new StringBuilder();
                switch (mode) {
                    case Effect:
                        decrease_sb.append(functionTerm.toString(file, known_terms, to_skip, Mode.Condition)).append("\n");
                        break;
                    case AtStartEffect:
                        decrease_sb.append(functionTerm.toString(file, known_terms, to_skip, Mode.AtStartCondition)).append("\n");
                        break;
                    case AtEndEffect:
                        decrease_sb.append(functionTerm.toString(file, known_terms, to_skip, Mode.AtEndCondition)).append("\n");
                        break;
                    default:
                        throw new AssertionError(mode.name());
                }
                if (value instanceof FunctionTerm) {
                    decrease_sb.append(value.toString(file, known_terms, to_skip, mode));
                    known_terms.put("value", Utils.lowercase(functionTerm.getFunction().getName()) + ".value - " + Utils.lowercase(((FunctionTerm) value).getFunction().getName()) + ".value");
                    decrease_sb.append(functionTerm.toString(file, known_terms, to_skip, mode)).append("\n");
                    return decrease_sb.toString();
                } else {
                    known_terms.put("value", Utils.lowercase(functionTerm.getFunction().getName()) + ".value - " + value.toString(file, known_terms, to_skip, mode));
                    decrease_sb.append(functionTerm.toString(file, known_terms, to_skip, mode));
                    return decrease_sb.toString();
                }
            default:
                throw new AssertionError(assignOp.name());
        }
    }

    @Override
    public String toString() {
        switch (assignOp) {
            case Assign:
                return "(assign " + functionTerm.toString() + " " + value.toString() + ")";
            case ScaleUp:
                return "(scale-up " + functionTerm.toString() + " " + value.toString() + ")";
            case ScaleDown:
                return "(scale-down " + functionTerm.toString() + " " + value.toString() + ")";
            case Increase:
                return "(increase " + functionTerm.toString() + " " + value.toString() + ")";
            case Decrease:
                return "(decrease " + functionTerm.toString() + " " + value.toString() + ")";
            default:
                throw new AssertionError(assignOp.name());
        }
    }

    enum AssignOp {

        Assign, ScaleUp, ScaleDown, Increase, Decrease
    }
}
