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

import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IConstructor;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IPredicate;
import it.cnr.istc.iloc.api.IResolver;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Type extends Scope implements IType {

    private static final Logger LOG = Logger.getLogger(Type.class.getName());
    private final String name;
    private IType superclass;
    private final Collection<IConstructor> constructors = new ArrayList<>(1);
    protected final List<IObject> instances = new ArrayList<>();

    public Type(ISolver solver, IScope enclosingScope, String name) {
        super(solver, enclosingScope);
        this.name = name;

        if (!isPrimitive()) {
            defineField(new Field("this", this, true));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isAssignableFrom(IType type) {
        IType c_type = type;
        while (c_type != null) {
            if (c_type == this) {
                return true;
            }
            c_type = c_type.getSuperclass();
        }
        return false;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public IType getSuperclass() {
        return superclass;
    }

    @Override
    public void setSuperclass(IType superclass) {
        this.superclass = superclass;
        if (fields.containsKey("super")) {
            fields.remove("super");
        }
        if (superclass != null) {
            defineField(new Field("super", superclass, true));
        }
    }

    @Override
    public IConstructor getConstructor(IType... parameterTypes) throws NoSuchMethodException {
        boolean isCorrect;
        for (IConstructor c : constructors) {
            if (c.getParameters().length == parameterTypes.length) {
                isCorrect = true;
                for (int i = 0; i < c.getParameters().length; i++) {
                    if (!c.getParameters()[i].getType().isAssignableFrom(parameterTypes[i])) {
                        isCorrect = false;
                        break;
                    }
                }
                if (isCorrect) {
                    return c;
                }
            }
        }
        // not found
        throw new NoSuchMethodException(this.name + ".<init>" + "(" + Stream.of(parameterTypes).map(p -> p.getName()).collect(Collectors.joining(", ")) + ")");
    }

    @Override
    public Collection<IConstructor> getConstructors() {
        return Collections.unmodifiableCollection(constructors);
    }

    @Override
    public void defineConstructor(IConstructor constructor) {
        solver.getCurrentNode().addResolver(new IResolver() {
            private boolean resolved = false;

            @Override
            public double getKnownCost() {
                return 0;
            }

            @Override
            public boolean isResolved() {
                return resolved;
            }

            @Override
            public void resolve() {
                assert !resolved;
                if (constructors.contains(constructor)) {
                    LOG.log(Level.WARNING, "Constructor has already been defined");
                }
                constructors.add(constructor);
                resolved = true;
            }

            @Override
            public void retract() {
                assert resolved;
                if (!constructors.contains(constructor)) {
                    LOG.log(Level.WARNING, "Constructor has never been defined");
                }
                constructors.remove(constructor);
                resolved = false;
            }
        });
    }

    @Override
    public List<IBool> checkConsistency(IModel model) {
        if (superclass != null) {
            return superclass.checkConsistency(model);
        }
        return Collections.emptyList();
    }

    @Override
    public List<IObject> getInstances() {
        return Collections.unmodifiableList(instances);
    }

    @Override
    public IObject newInstance(IEnvironment enclosing_environment) {
        IObject instance = new SimpleObject(solver, enclosing_environment, this);
        solver.getCurrentNode().addResolver(new IResolver() {
            private boolean resolved = false;

            @Override
            public double getKnownCost() {
                return 0;
            }

            @Override
            public void resolve() {
                assert !resolved;
                Type c_type = Type.this;
                while (c_type != null) {
                    c_type.instances.add(instance);
                    c_type = (Type) c_type.getSuperclass();
                }
                resolved = true;
            }

            @Override
            public boolean isResolved() {
                return resolved;
            }

            @Override
            public void retract() {
                assert resolved;
                IType c_type = Type.this;
                while (c_type != null) {
                    instances.remove(instance);
                    c_type = c_type.getSuperclass();
                }
                resolved = false;
            }
        });
        return instance;
    }

    @Override
    public IField getField(String name) throws NoSuchFieldException {
        try {
            return super.getField(name);
        } catch (NoSuchFieldException e) {
            if (superclass != null) {
                return superclass.getField(name);
            }
        }
        // not found
        throw new NoSuchFieldException(name);
    }

    @Override
    public IType getType(String name) throws ClassNotFoundException {
        try {
            return super.getType(name);
        } catch (ClassNotFoundException e) {
            if (superclass != null) {
                return superclass.getType(name);
            }
        }
        // not found
        throw new ClassNotFoundException(name);
    }

    @Override
    public IPredicate getPredicate(String name) throws ClassNotFoundException {
        try {
            return super.getPredicate(name);
        } catch (ClassNotFoundException e) {
            if (superclass != null) {
                return superclass.getPredicate(name);
            }
        }
        // not found
        throw new ClassNotFoundException(name);
    }

    @Override
    public void predicateDefined(IPredicate predicate) {
        if (superclass != null) {
            superclass.predicateDefined(predicate);
        }
    }

    @Override
    public void factCreated(IFormula fact) {
//        solver.getCurrentNode().enqueue(new Fact(fact));
        solver.getCurrentNode().addResolver(new IResolver() {
            private boolean resolved = false;

            @Override
            public double getKnownCost() {
                return Double.MIN_NORMAL;
            }

            @Override
            public void resolve() {
                assert !resolved;
                fact.setActiveState();
                resolved = true;
            }

            @Override
            public boolean isResolved() {
                return resolved;
            }

            @Override
            public void retract() {
                assert resolved;
                fact.setInactiveState();
                resolved = false;
            }
        });
    }

    @Override
    public void goalCreated(IFormula goal) {
        solver.getCurrentNode().enqueue(new Goal(goal));
    }

    @Override
    public void formulaActivated(IFormula formula) {
        if (superclass != null) {
            superclass.formulaActivated(formula);
        }
    }

    @Override
    public void formulaUnified(IFormula formula, List<IFormula> with, List<IBool> terms) {
        if (superclass != null) {
            superclass.formulaUnified(formula, with, terms);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(name).append("\n");
        getFields().keySet().forEach((field) -> {
            sb.append("  ").append(field).append("\n");
        });
        constructors.forEach((c) -> {
            sb.append("  ").append(this.name).append(".<init>").append("(").append(Stream.of(c.getParameters()).map(p -> p.getType().getName() + " " + p.getName()).collect(Collectors.joining(", "))).append(")\n");
        });
        getMethods().forEach((m) -> {
            sb.append("  ").append(this.name).append(".").append(m.getName()).append("(").append(Stream.of(m.getParameters()).map(p -> p.getType().getName() + " " + p.getName()).collect(Collectors.joining(", "))).append(")\n");
        });
        getPredicates().values().forEach((p) -> {
            sb.append("  ").append(this.name).append(".").append(p.getName()).append("(").append(p.getFields().values().stream().filter(field -> !field.isSynthetic()).map(par -> par.getType().getName() + " " + par.getName()).collect(Collectors.joining(", "))).append(")\n");
        });
        sb.append(")");
        return sb.toString();
    }
}
