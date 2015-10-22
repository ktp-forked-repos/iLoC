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

import it.cnr.istc.iloc.api.IConstructor;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public abstract class Constructor extends Scope implements IConstructor {

    protected final IField[] parameters;

    public Constructor(ISolver solver, IScope enclosingScope, IField... parameters) {
        super(solver, enclosingScope);
        this.parameters = parameters;

        defineField(new Field("this", (IType) enclosingScope, true));
        if (((IType) enclosingScope).getSuperclass() != null) {
            defineField(new Field("super", ((IType) enclosingScope).getSuperclass(), true));
        }

        for (IField parameter : parameters) {
            defineField(parameter);
        }
    }

    @Override
    public IField[] getParameters() {
        return parameters;
    }

    @Override
    public IObject newInstance(IEnvironment environment, IObject... expressions) {
        assert expressions.length == parameters.length;

        IType type = (IType) getEnclosingScope();
        IObject instance = type.newInstance(environment);

        init(instance);
        invoke(instance, expressions);
        completeInit(instance);

        return instance;
    }

    @Override
    public void completeInit(IObject instance) {
        // Any uninstantiated field will be instantiated
        enclosingScope.getFields().values().stream().filter(field -> !field.isSynthetic() && !instance.getObjects().containsKey(field.getName())).forEach(field -> {
            if (field.getType().isPrimitive()) {
                // The field is a primitive type so it must be instantiated
                instance.set(field.getName(), field.getType().newInstance(instance));
            } else {
                // The field is a non primitive type so it must be instantiated as an existential
                instance.set(field.getName(), field.getType().newExistential());
            }
        });
    }

    @Override
    public String toString() {
        return ((IType) enclosingScope).getName() + ".<init>(" + Stream.of(parameters).map(p -> p.getType().getName() + " " + p.getName()).collect(Collectors.joining(", ")) + ")";
    }
}
