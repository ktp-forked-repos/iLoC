/*
 * Copyright (C) 2015 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.istc.iloc;

import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IDisjunctionFlaw;
import it.cnr.istc.iloc.api.IFlaw;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IGoal;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INode;
import it.cnr.istc.iloc.api.IResolver;
import it.cnr.istc.iloc.api.ISolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Node implements INode {

    private final ISolver solver;
    private final int level;
    private final INode parent;
    private double known_cost = 0;
    private final Collection<IResolver> resolvers = new ConcurrentArrayList<>();
    private final Collection<IFlaw> flaws;

    Node(ISolver solver) {
        this.solver = solver;
        this.level = 1;
        this.parent = null;
        this.flaws = new ConcurrentArrayList<>();
    }

    Node(INode parent) {
        this.solver = parent.getSolver();
        this.level = parent.getLevel() + 1;
        this.parent = parent;
        this.known_cost = parent.getKnownCost();
        this.flaws = new ConcurrentArrayList<>(parent.getFlaws());
    }

    @Override
    public ISolver getSolver() {
        return solver;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public INode getParent() {
        return parent;
    }

    @Override
    public double getKnownCost() {
        return known_cost + resolvers.stream().mapToDouble(resolver -> resolver.getKnownCost()).sum();
    }

    @Override
    public void addResolver(IResolver resolver) {
        resolvers.add(resolver);
        if (solver.getCurrentNode() == this) {
            resolver.resolve();
            known_cost += resolver.getKnownCost();
        }
    }

    @Override
    public Collection<IResolver> getResolvers() {
        return Collections.unmodifiableCollection(resolvers);
    }

    @Override
    public void resolve() {
        resolvers.stream().filter(resolver -> (!resolver.isResolved())).forEach((resolver) -> {
            resolver.resolve();
        });
    }

    @Override
    public Boolean propagate(int bound) {
        while (!flaws.isEmpty()) {
            Collection<IFlaw> solved_flaws = new ArrayList<>(flaws.size());
            // We check for all the flaws having one resolver, we resolve it and we propagate the constraint network..
            for (IFlaw flaw : flaws) {
                if (flaw instanceof IDisjunctionFlaw) {
                    continue;
                } else if (flaw instanceof IGoal) {
                    int c_lvl = 1;
                    IFormula cause = ((IGoal) flaw).getFormula().getCause();
                    while (cause != null) {
                        cause = cause.getCause();
                        c_lvl++;
                    }
                    if (c_lvl > bound) {
                        return null;
                    }
                }
                Collection<IResolver> c_resolvers = flaw.getResolvers();
                assert !c_resolvers.isEmpty() : "Flaws should have at least one resolver..";
                if (c_resolvers.size() == 1) {
                    resolvers.addAll(c_resolvers);
                    IResolver resolver = c_resolvers.iterator().next();
                    resolver.resolve();
                    solved_flaws.add(flaw);
                    if (!solver.getConstraintNetwork().propagate()) {
                        // We have found an inconsistency..
                        return false;
                    }
                }
            }
            if (solved_flaws.isEmpty()) {
                // All the flaws having one resolver have been resolved and the constraint network is consistent..
                break;
            } else {
                // We remove the solved flaws..
                flaws.removeAll(solved_flaws);
            }
        }
        return true;
    }

    @Override
    public void enqueue(IFlaw flaw) {
        flaws.add(flaw);
    }

    @Override
    public IFlaw selectFlaw() {
        if (flaws.isEmpty()) {
            return null;
        } else {
            IFlaw flaw = Collections.min(flaws, (IFlaw f0, IFlaw f1) -> Double.compare(f0.getEstimatedCost(), f1.getEstimatedCost()));
            flaws.remove(flaw);
            return flaw;
        }
    }

    @Override
    public Collection<IFlaw> getFlaws() {
        return Collections.unmodifiableCollection(flaws);
    }

    @Override
    public Boolean checkConsistency(int bound) {
        resolve();
        Boolean propagate = propagate(bound);
        if (propagate == null) {
            // We were looking to deep, we don't know yet if the node is inconsistent or if it may contain a solution..
            // The solver will decide what to do, either check consistency with a higher bound or check for another node..
            return null;
        } else if (!propagate) {
            // The node is inconsistent and can be thrown away..
            return false;
        } else {
            while (true) {
                if (!solver.getConstraintNetwork().propagate()) {
                    return false;
                }
                IModel model = solver.getConstraintNetwork().getModel();
                List<IBool> constraints = solver.getStaticCausalGraph().getTypes().stream().flatMap(type -> type.checkConsistency(model).stream()).collect(Collectors.toList());
                if (constraints.isEmpty()) {
                    return true;
                } else {
                    addResolver(new IResolver() {
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
                            solver.getConstraintNetwork().assertFacts(constraints.toArray(new IBool[constraints.size()]));
                            resolved = true;
                        }

                        @Override
                        public void retract() {
                            assert resolved;
                            resolved = false;
                        }
                    });
                }
            }
        }
    }

    private static class ConcurrentArrayList<T> extends ArrayList<T> {

        ConcurrentArrayList(int i) {
            super(i);
        }

        ConcurrentArrayList() {
        }

        ConcurrentArrayList(Collection<? extends T> clctn) {
            super(clctn);
        }

        @Override
        public Spliterator<T> spliterator() {
            return Spliterators.spliteratorUnknownSize(iterator(), 0);
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                int cursor = 0;

                @Override
                public boolean hasNext() {
                    return cursor != size();
                }

                @Override
                public T next() {
                    int i = cursor;
                    T next = get(i);
                    cursor = i + 1;
                    return next;
                }
            };
        }
    }
}
