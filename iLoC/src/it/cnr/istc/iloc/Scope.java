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
import it.cnr.istc.iloc.api.IPredicate;
import it.cnr.istc.iloc.api.IResolver;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Scope implements IScope {

    private static final Logger LOG = Logger.getLogger(Scope.class.getName());
    protected final ISolver solver;
    protected final IScope enclosingScope;
    protected final Map<String, IField> fields = new LinkedHashMap<>();
    protected final Map<String, Collection<IMethod>> methods = new HashMap<>();
    protected final Map<String, IType> types = new LinkedHashMap<>(0);
    protected final Map<String, IPredicate> predicates = new LinkedHashMap<>(0);

    Scope(ISolver solver, IScope enclosingScope) {
        assert solver != null;
        this.solver = solver;
        this.enclosingScope = enclosingScope;
    }

    @Override
    public ISolver getSolver() {
        return solver;
    }

    @Override
    public IScope getEnclosingScope() {
        return enclosingScope;
    }

    @Override
    public IField getField(String name) throws NoSuchFieldException {
        IField field = fields.get(name);
        if (field != null) {
            return field;
        }

        // if not here, check any enclosing scope
        field = enclosingScope.getField(name);
        if (field != null) {
            return field;
        }

        // not found
        throw new NoSuchFieldException(name);
    }

    @Override
    public Map<String, IField> getFields() {
        return Collections.unmodifiableMap(fields);
    }

    @Override
    public void defineField(IField field) {
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
                if (fields.containsKey(field.getName())) {
                    LOG.log(Level.WARNING, "Field {0} has already been defined", field.getName());
                }
                fields.put(field.getName(), field);
                resolved = true;
            }

            @Override
            public void retract() {
                assert resolved;
                if (!fields.containsKey(field.getName())) {
                    LOG.log(Level.WARNING, "Field {0} has never been defined", field.getName());
                }
                fields.remove(field.getName(), field);
                resolved = false;
            }
        });
    }

    @Override
    public IMethod getMethod(String name, IType... parameterTypes) throws NoSuchMethodException {
        boolean isCorrect;
        if (methods.containsKey(name)) {
            for (IMethod m : methods.get(name)) {
                if (m.getParameters().length == parameterTypes.length) {
                    isCorrect = true;
                    for (int i = 0; i < m.getParameters().length; i++) {
                        if (!m.getParameters()[i].getType().isAssignableFrom(parameterTypes[i])) {
                            isCorrect = false;
                            break;
                        }
                    }
                    if (isCorrect) {
                        return m;
                    }
                }
            }
        }
        // if not here, check any enclosing scope
        IMethod method = enclosingScope.getMethod(name, parameterTypes);
        if (method != null) {
            return method;
        }
        // not found
        throw new NoSuchMethodException(name + "(" + Stream.of(parameterTypes).map(p -> p.getName()).collect(Collectors.joining(", ")) + ")");
    }

    @Override
    public Collection<IMethod> getMethods() {
        Collection<IMethod> c_methods = new ArrayList<>(methods.size());
        methods.values().stream().forEach((ms) -> {
            c_methods.addAll(ms);
        });
        return Collections.unmodifiableCollection(c_methods);
    }

    @Override
    public void defineMethod(IMethod method) {
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
                if (methods.containsKey(method.getName()) && methods.get(method.getName()).contains(method)) {
                    LOG.log(Level.WARNING, "Method {0}({1}) has already been defined", new Object[]{method.getName(), Arrays.stream(method.getParameters()).map(p -> p.getName()).collect(Collectors.joining(", "))});
                }
                if (!methods.containsKey(method.getName())) {
                    methods.put(method.getName(), new ArrayList<>(1));
                }
                methods.get(method.getName()).add(method);
                resolved = true;
            }

            @Override
            public void retract() {
                assert resolved;
                if (!methods.containsKey(method.getName()) || !methods.get(method.getName()).contains(method)) {
                    LOG.log(Level.WARNING, "Method {0}({1}) has never been defined", new Object[]{method.getName(), Arrays.stream(method.getParameters()).map(p -> p.getName()).collect(Collectors.joining(", "))});
                }
                methods.get(method.getName()).remove(method);
                resolved = false;
            }
        });
    }

    @Override
    public IPredicate getPredicate(String name) throws ClassNotFoundException {
        IPredicate predicate = predicates.get(name);
        if (predicate != null) {
            return predicate;
        }
        // if not here, check any enclosing scope
        predicate = enclosingScope.getPredicate(name);
        if (predicate != null) {
            return predicate;
        }
        // not found
        throw new ClassNotFoundException(name);
    }

    @Override
    public Map<String, IPredicate> getPredicates() {
        return Collections.unmodifiableMap(predicates);
    }

    @Override
    public void definePredicate(IPredicate predicate) {
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
                if (predicates.containsKey(predicate.getName())) {
                    LOG.log(Level.WARNING, "Predicate {0} has already been defined", predicate.getName());
                }
                predicates.put(predicate.getName(), predicate);
                solver.getStaticCausalGraph().addNode(predicate);
                predicateDefined(predicate);
                resolved = true;
            }

            @Override
            public void retract() {
                assert resolved;
                if (!predicates.containsKey(predicate.getName())) {
                    LOG.log(Level.WARNING, "Predicate {0} has never been defined", predicate.getName());
                }
                predicates.remove(predicate.getName(), predicate);
                solver.getStaticCausalGraph().removeNode(solver.getStaticCausalGraph().getNode(predicate));
                resolved = false;
            }
        });
    }

    @Override
    public IType getType(String name) throws ClassNotFoundException {
        IType type = types.get(name);
        if (type != null) {
            return type;
        }
        // if not here, check any enclosing scope
        type = enclosingScope.getType(name);
        if (type != null) {
            return type;
        }
        // not found
        throw new ClassNotFoundException(name);
    }

    @Override
    public Map<String, IType> getTypes() {
        return Collections.unmodifiableMap(types);
    }

    @Override
    public void defineType(IType type) {
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
                if (types.containsKey(type.getName())) {
                    LOG.log(Level.WARNING, "Type {0} has already been defined", type.getName());
                }
                types.put(type.getName(), type);
                solver.getStaticCausalGraph().addType(type);
                resolved = true;
            }

            @Override
            public void retract() {
                assert resolved;
                assert type.getPredicates().values().stream().noneMatch(predicate -> predicates.containsKey(predicate.getName()));
                if (!types.containsKey(type.getName())) {
                    LOG.log(Level.WARNING, "Type {0} has never been defined", type.getName());
                }
                types.remove(type.getName(), type);
                solver.getStaticCausalGraph().removeType(type);
                resolved = false;
            }
        });
    }
}
