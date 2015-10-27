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
package it.cnr.istc.iloc.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IObject extends IEnvironment {

    /**
     * Returns the runtime class of this {@code IObject}.
     *
     * @return The {@code IType} object that represents the runtime class of
     * this object.
     */
    public IType getType();

    /**
     * Generates a constraint indicating whether some other object is "equal to"
     * this one.
     *
     * @param expression the reference object with which to compare.
     * @return an {@code IBool} representing whether this object is the same as
     * the expression argument.
     */
    public default IBool eq(IObject expression) {
        IConstraintNetwork constraintNetwork = getSolver().getConstraintNetwork();
        if (this == expression) {
            // Objects are exactly the same..
            return constraintNetwork.newBool("true");
        }
        if (getType() != expression.getType()) {
            // Object types are different, so must be the objects..
            return constraintNetwork.newBool("false");
        }

        if (this instanceof IEnum) {
            if (expression instanceof IEnum) {
                IObject[] this_enums = ((IEnum) this).getEnums();
                IObject[] expression_enums = ((IEnum) expression).getEnums();
                List<IBool> c_terms = new ArrayList<>(this_enums.length * expression_enums.length);
                for (int i = 0; i < this_enums.length; i++) {
                    for (int j = 0; j < expression_enums.length; j++) {
                        if (this_enums[i] instanceof IEnum || expression_enums[j] instanceof IEnum) {
                            c_terms.add(constraintNetwork.and(
                                    constraintNetwork.eq(((IEnum) this).getVar(), constraintNetwork.newInt(Integer.toString(i))),
                                    constraintNetwork.eq(((IEnum) expression).getVar(), constraintNetwork.newInt(Integer.toString(j))),
                                    this_enums[i].eq(expression_enums[j])
                            ));
                        } else if (this_enums[i].equals(expression_enums[j])) {
                            c_terms.add(constraintNetwork.and(
                                    constraintNetwork.eq(((IEnum) this).getVar(), constraintNetwork.newInt(Integer.toString(i))),
                                    constraintNetwork.eq(((IEnum) expression).getVar(), constraintNetwork.newInt(Integer.toString(j)))
                            ));
                        }
                    }
                }
                if (c_terms.isEmpty()) {
                    return constraintNetwork.newBool("false");
                } else if (c_terms.size() == 1) {
                    return c_terms.get(0);
                } else {
                    return constraintNetwork.or(c_terms.toArray(new IBool[c_terms.size()]));
                }
            } else {
                return expression.eq(this);
            }
        } else if (expression instanceof IEnum) {
            IObject[] expression_enums = ((IEnum) expression).getEnums();
            List<IBool> c_terms = new ArrayList<>(expression_enums.length);
            for (int i = 0; i < expression_enums.length; i++) {
                if (expression_enums[i] instanceof IEnum) {
                    c_terms.add(constraintNetwork.and(
                            constraintNetwork.eq(((IEnum) expression).getVar(), constraintNetwork.newInt(Integer.toString(i))),
                            eq(expression_enums[i])
                    ));
                } else if (equals(expression_enums[i])) {
                    c_terms.add(constraintNetwork.eq(((IEnum) expression).getVar(), constraintNetwork.newInt(Integer.toString(i))));
                }
            }
            if (c_terms.isEmpty()) {
                return constraintNetwork.newBool("false");
            } else if (c_terms.size() == 1) {
                return c_terms.get(0);
            } else {
                return constraintNetwork.or(c_terms.toArray(new IBool[c_terms.size()]));
            }
        } else {
            // We have two objects of the same type
            // All the fields of the first object will be equal to all the field of second object
            List<IField> fields = new ArrayList<>();
            IType c_type = getType();
            while (c_type != null) {
                fields.addAll(c_type.getFields().values().stream().filter(field -> !field.isSynthetic()).collect(Collectors.toList()));
                c_type = c_type.getSuperclass();
            }
            assert fields.stream().map(field -> get(field.getName())).noneMatch(Objects::isNull);
            assert fields.stream().map(field -> expression.get(field.getName())).noneMatch(Objects::isNull);
            IBool[] c_terms = fields.stream().map(field -> get(field.getName()).eq(expression.get(field.getName()))).toArray(IBool[]::new);
            if (c_terms.length == 0) {
                // Objects are logically equivalent
                return constraintNetwork.newBool("true");
            } else if (c_terms.length == 1) {
                return c_terms[0];
            } else {
                return constraintNetwork.and(c_terms);
            }
        }
    }
}
