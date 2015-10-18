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
package it.cnr.istc.iloc;

import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IMethod;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public abstract class Method extends Scope implements IMethod {

    protected final String name;
    protected final IField[] parameters;
    protected final IType returnType;

    public Method(ISolver solver, IScope enclosingScope, String name, IType returnType, IField... parameters) {
        super(solver, enclosingScope);
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;

        defineField(new Field("this", (IType) enclosingScope, true));
        if (((IType) enclosingScope).getSuperclass() != null) {
            defineField(new Field("super", ((IType) enclosingScope).getSuperclass(), true));
        }
        if (returnType != null) {
            defineField(new Field("return", returnType, true));
        }

        for (IField parameter : parameters) {
            defineField(parameter);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IField[] getParameters() {
        return parameters;
    }

    @Override
    public IType getReturnType() {
        return returnType;
    }

    @Override
    public String toString() {
        return name + "(" + Stream.of(parameters).map(p -> p.getType().getName() + " " + p.getName()).collect(Collectors.joining(", ")) + ")";
    }
}
