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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class ComparisonTerm implements Term {

    private final Comp comp;
    private final Term firstTerm, secondTerm;

    ComparisonTerm(Comp comp, Term firstTerm, Term secondTerm) {
        this.comp = comp;
        this.firstTerm = firstTerm;
        this.secondTerm = secondTerm;
    }

    @Override
    public Term negate() {
        switch (comp) {
            case Gt:
                return new ComparisonTerm(Comp.LEq, firstTerm, secondTerm);
            case Lt:
                return new ComparisonTerm(Comp.GEq, firstTerm, secondTerm);
            case Eq:
                return new OrTerm(Arrays.asList(new ComparisonTerm(Comp.Lt, firstTerm, secondTerm), new ComparisonTerm(Comp.Gt, firstTerm, secondTerm)));
            case GEq:
                return new ComparisonTerm(Comp.Lt, firstTerm, secondTerm);
            case LEq:
                return new ComparisonTerm(Comp.Gt, firstTerm, secondTerm);
            default:
                throw new AssertionError(comp.name());
        }
    }

    @Override
    public Term ground(Domain domain, Map<String, Term> known_terms) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Term> containsPredicate(boolean directed, Predicate predicate) {
        return Collections.emptyList();
    }

    @Override
    public List<Term> containsFunction(Function function) {
        return Stream.concat(firstTerm.containsFunction(function).stream(), secondTerm.containsFunction(function).stream()).collect(Collectors.toList());
    }

    @Override
    public String toString(STGroupFile file, Map<String, String> known_terms, Set<Term> to_skip, Mode mode) {
        if (firstTerm instanceof FunctionTerm && secondTerm instanceof FunctionTerm) {
            if (comp == Comp.Eq) {
                StringBuilder sb = new StringBuilder();
                sb.append(firstTerm.toString(file, known_terms, to_skip, mode)).append("\n");
                known_terms.put("value", Utils.lowercase(((FunctionTerm) firstTerm).getFunction().getName()) + ".value");
                sb.append(secondTerm.toString(file, known_terms, to_skip, mode));
                return sb.toString();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(firstTerm.toString(file, known_terms, to_skip, mode)).append(";\n");
                sb.append(secondTerm.toString(file, known_terms, to_skip, mode));
                switch (comp) {
                    case Gt:
                        sb.append(Utils.lowercase(((FunctionTerm) firstTerm).getFunction().getName())).append(".value").append(" > ").append(Utils.lowercase(((FunctionTerm) secondTerm).getFunction().getName())).append(".value").append(";\n");
                        break;
                    case Lt:
                        sb.append(Utils.lowercase(((FunctionTerm) firstTerm).getFunction().getName())).append(".value").append(" < ").append(Utils.lowercase(((FunctionTerm) secondTerm).getFunction().getName())).append(".value").append(";\n");
                        break;
                    case GEq:
                        sb.append(Utils.lowercase(((FunctionTerm) firstTerm).getFunction().getName())).append(".value").append(" >= ").append(Utils.lowercase(((FunctionTerm) secondTerm).getFunction().getName())).append(".value").append(";\n");
                        break;
                    case LEq:
                        sb.append(Utils.lowercase(((FunctionTerm) firstTerm).getFunction().getName())).append(".value").append(" <= ").append(Utils.lowercase(((FunctionTerm) secondTerm).getFunction().getName())).append(".value").append(";\n");
                        break;
                    default:
                        throw new AssertionError(comp.name());
                }
                return sb.toString();
            }
        } else if (firstTerm instanceof FunctionTerm && !(secondTerm instanceof FunctionTerm)) {
            if (comp == Comp.Eq) {
                known_terms.put("value", secondTerm.toString(file, known_terms, to_skip, mode));
                return firstTerm.toString(file, known_terms, to_skip, mode);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(firstTerm.toString(file, known_terms, to_skip, mode)).append(";\n");
                sb.append(Utils.lowercase(((FunctionTerm) firstTerm).getFunction().getName())).append(".value");
                switch (comp) {
                    case Gt:
                        sb.append(" > ");
                        break;
                    case Lt:
                        sb.append(" < ");
                        break;
                    case Eq:
                        sb.append(" == ");
                        break;
                    case GEq:
                        sb.append(" >= ");
                        break;
                    case LEq:
                        sb.append(" <= ");
                        break;
                    default:
                        throw new AssertionError(comp.name());
                }
                sb.append(secondTerm.toString(file, known_terms, to_skip, mode));
                return sb.toString();
            }
        } else if (secondTerm instanceof FunctionTerm && !(firstTerm instanceof FunctionTerm)) {
            if (comp == Comp.Eq) {
                known_terms.put("value", firstTerm.toString(file, known_terms, to_skip, mode));
                return secondTerm.toString(file, known_terms, to_skip, mode);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(secondTerm.toString(file, known_terms, to_skip, mode)).append(";\n");
                sb.append(Utils.lowercase(((FunctionTerm) secondTerm).getFunction().getName())).append(".value");
                switch (comp) {
                    case Gt:
                        sb.append(" > ");
                        break;
                    case Lt:
                        sb.append(" < ");
                        break;
                    case Eq:
                        sb.append(" == ");
                        break;
                    case GEq:
                        sb.append(" >= ");
                        break;
                    case LEq:
                        sb.append(" <= ");
                        break;
                    default:
                        throw new AssertionError(comp.name());
                }
                sb.append(firstTerm.toString(file, known_terms, to_skip, mode));
                return sb.toString();
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(firstTerm.toString(file, known_terms, to_skip, mode));
            switch (comp) {
                case Gt:
                    sb.append(" > ");
                    break;
                case Lt:
                    sb.append(" < ");
                    break;
                case Eq:
                    sb.append(" == ");
                    break;
                case GEq:
                    sb.append(" >= ");
                    break;
                case LEq:
                    sb.append(" <= ");
                    break;
                default:
                    throw new AssertionError(comp.name());
            }
            sb.append(secondTerm.toString(file, known_terms, to_skip, mode));
            return sb.toString();
        }
    }

    enum Comp {

        Gt, Lt, Eq, GEq, LEq;
    }
}
