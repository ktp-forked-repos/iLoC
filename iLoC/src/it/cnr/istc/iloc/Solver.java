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

import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.IConstraintNetwork;
import it.cnr.istc.iloc.api.IDynamicCausalGraph;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IFlaw;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.ILandmarkGraph;
import it.cnr.istc.iloc.api.IMethod;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INode;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IPredicate;
import it.cnr.istc.iloc.api.IRelaxedPlanningGraph;
import it.cnr.istc.iloc.api.IResolver;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.ISolverListener;
import it.cnr.istc.iloc.api.IStaticCausalGraph;
import it.cnr.istc.iloc.api.IType;
import it.cnr.istc.iloc.ddl.LanguageParser;
import it.cnr.istc.iloc.utils.NativeUtils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Solver implements ISolver {

    private static final Logger LOG = Logger.getLogger(Solver.class.getName());

    static {
        try {
            NativeUtils.addLibraryPath();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    private final Properties properties;
    private final LanguageParser parser;
    private final Deque<INode> fringe = new LinkedList<>();
    private final Collection<INode> breached = new ArrayList<>();
    private INode currentNode;
    private int bound;
    private int n_nodes = 0;
    private final IConstraintNetwork constraintNetwork;
    private final IStaticCausalGraph staticCausalGraph = new StaticCausalGraph();
    private final IDynamicCausalGraph dynamicCausalGraph = new DynamicCausalGraph();
    private final IRelaxedPlanningGraph rpg = new RelaxedPlanningGraph(this);
    private final ILandmarkGraph lm_graph = new LandmarkGraph(this);
    private final Map<String, IField> fields = new LinkedHashMap<>();
    private final Map<String, Collection<IMethod>> methods = new HashMap<>();
    private final Map<String, IType> types = new LinkedHashMap<>(0);
    private final Map<String, IPredicate> predicates = new LinkedHashMap<>(0);
    private final Map<String, IObject> objects = new HashMap<>();
    private final Collection<ISolverListener> listeners = new ArrayList<>();

    public Solver(Properties properties) {
        this.properties = properties;
        this.parser = new LanguageParser(this, properties, new ANTLRErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                LOG.log(Level.SEVERE, "[{0}, {1}] {2}", new Object[]{line, charPositionInLine, msg});
            }

            @Override
            public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
            }

            @Override
            public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
            }

            @Override
            public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
            }
        });

        this.currentNode = new Node(this);
        this.fringe.add(currentNode);

        // We create primitive types ..
        IType boolType = new Type(this, this, Constants.BOOL) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public IObject newInstance(IEnvironment enclosing_environment) {
                return constraintNetwork.newBool();
            }
        };
        IType numberType = new Type(this, this, Constants.NUMBER) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public IObject newInstance(IEnvironment enclosing_environment) {
                return constraintNetwork.newReal();
            }
        };
        IType intType = new Type(this, this, Constants.INT) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public IObject newInstance(IEnvironment enclosing_environment) {
                return constraintNetwork.newInt();
            }
        };
        intType.setSuperclass(numberType);
        IType realType = new Type(this, this, Constants.REAL) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public IObject newInstance(IEnvironment enclosing_environment) {
                return constraintNetwork.newReal();
            }
        };
        realType.setSuperclass(numberType);
        IType stringType = new Type(this, this, Constants.STRING) {
            @Override
            public boolean isPrimitive() {
                return true;
            }

            @Override
            public IObject newInstance(IEnvironment enclosing_environment) {
                return constraintNetwork.newString();
            }
        };

        // .. and define them
        defineType(boolType);
        defineType(numberType);
        defineType(intType);
        defineType(realType);
        defineType(stringType);

        String[] types = properties.getProperty("Types",
                "it.cnr.istc.iloc.types.agent.AgentType:"
                + "it.cnr.istc.iloc.types.impulsiveagent.ImpulsiveAgentType:"
                + "it.cnr.istc.iloc.types.statevariable.StateVariableType:"
                + "it.cnr.istc.iloc.types.reusableresource.ReusableResourceType:"
                + "it.cnr.istc.iloc.types.consumableresource.ConsumableResourceType:"
                + "it.cnr.istc.iloc.types.battery.BatteryType:"
                + "it.cnr.istc.iloc.types.sensor.SensorType:"
                + "it.cnr.istc.iloc.types.propositionalstate.PropositionalStateType:"
                + "it.cnr.istc.iloc.types.propositionalstate.PropositionalImpulsiveAgentType:"
                + "it.cnr.istc.iloc.types.propositionalstate.PropositionalAgentType").split(":");
        for (String type : types) {
            try {
                defineType((IType) Class.forName(type.trim()).getDeclaredConstructor(ISolver.class, Properties.class).newInstance(this, properties));
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }

        this.constraintNetwork = new ConstraintNetwork(this, properties);

        INumber origin = constraintNetwork.newReal();
        INumber horizon = constraintNetwork.newReal();
        defineField(new Field(Constants.ORIGIN, origin.getType()));
        defineField(new Field(Constants.HORIZON, horizon.getType()));

        set(Constants.ORIGIN, origin);
        set(Constants.HORIZON, horizon);

        constraintNetwork.assertFacts(
                constraintNetwork.geq(origin, constraintNetwork.newReal("0")),
                constraintNetwork.geq(horizon, origin)
        );
    }

    @Override
    public IModel read(String script) {
        LOG.info("Parsing script..");
        long starting_parsing_time = System.nanoTime();
        parser.read(script);
        long ending_parsing_time = System.nanoTime();
        LOG.log(Level.INFO, "Script parsed in {0}ms", (ending_parsing_time - starting_parsing_time) / 1000000);

        LOG.info("Computing relaxed planning graph..");
        long starting_rpg_time = System.nanoTime();
        rpg.extract();
        rpg.propagate();
        long ending_rpg_time = System.nanoTime();
        LOG.log(Level.INFO, "Relaxed planning graph computed in {0}ms", (ending_rpg_time - starting_rpg_time) / 1000000);

        LOG.info("Performing initial propagation..");
        long starting_propagation_time = System.nanoTime();
        if (constraintNetwork.propagate()) {
            long ending_propagation_time = System.nanoTime();
            LOG.log(Level.INFO, "Initial propagation performed in {0}ms", (ending_propagation_time - starting_propagation_time) / 1000000);
            // Current node is propagated and is consistent
            IModel model = constraintNetwork.getModel();
            listeners.forEach(listener -> {
                listener.contentChanged(model, currentNode);
            });
            return model;
        } else {
            long ending_propagation_time = System.nanoTime();
            LOG.log(Level.INFO, "Initial propagation performed in {0}ms", (ending_propagation_time - starting_propagation_time) / 1000000);
            // We have found an inconsistent node as a consequence of a constraint propagation on the current node
            listeners.forEach(listener -> {
                listener.inconsistentNode(currentNode);
            });
            return null;
        }
    }

    @Override
    public IModel read(File... files) throws IOException {
        LOG.log(Level.INFO, "Parsing {0} files..", files.length);
        long starting_parsing_time = System.nanoTime();
        parser.read(files);
        long ending_parsing_time = System.nanoTime();
        LOG.log(Level.INFO, "Files parsed in {0}ms", (ending_parsing_time - starting_parsing_time) / 1000000);

        LOG.info("Computing relaxed planning graph..");
        long starting_rpg_time = System.nanoTime();
        rpg.extract();
        rpg.propagate();
        long ending_rpg_time = System.nanoTime();
        LOG.log(Level.INFO, "Relaxed planning graph computed in {0}ms", (ending_rpg_time - starting_rpg_time) / 1000000);

        LOG.info("Performing initial propagation..");
        long starting_propagation_time = System.nanoTime();
        if (constraintNetwork.propagate()) {
            long ending_propagation_time = System.nanoTime();
            LOG.log(Level.INFO, "Initial propagation performed in {0}ms", (ending_propagation_time - starting_propagation_time) / 1000000);
            // Current node is propagated and is consistent
            IModel model = constraintNetwork.getModel();
            listeners.forEach(listener -> {
                listener.contentChanged(model, currentNode);
            });
            return model;
        } else {
            long ending_propagation_time = System.nanoTime();
            LOG.log(Level.INFO, "Initial propagation performed in {0}ms", (ending_propagation_time - starting_propagation_time) / 1000000);
            // We have found an inconsistent node as a consequence of a constraint propagation on the current node
            listeners.forEach(listener -> {
                listener.inconsistentNode(currentNode);
            });
            return null;
        }
    }

    @Override
    public IConstraintNetwork getConstraintNetwork() {
        return constraintNetwork;
    }

    @Override
    public IStaticCausalGraph getStaticCausalGraph() {
        return staticCausalGraph;
    }

    @Override
    public IDynamicCausalGraph getDynamicCausalGraph() {
        return dynamicCausalGraph;
    }

    @Override
    public IRelaxedPlanningGraph getRelaxedPlanningGraph() {
        return rpg;
    }

    @Override
    public ILandmarkGraph getLandmarkGraph() {
        return lm_graph;
    }

    @Override
    public Boolean goTo(INode node) {
        if (currentNode == node) {
            Boolean consistent = currentNode.checkConsistency(bound);
            if (consistent == null) {
                return null;
            } else if (consistent) {
                // Current node is propagated and is consistent
                IModel model = constraintNetwork.getModel();
                listeners.forEach(listener -> {
                    listener.contentChanged(model, currentNode);
                });
                return true;
            } else {
                // We have found an inconsistent node as a consequence of a constraint propagation on the current node
                listeners.forEach(listener -> {
                    listener.inconsistentNode(currentNode);
                });
                return false;
            }
        } else if (node.getParent() == currentNode) {
            constraintNetwork.push();
            currentNode = node;
            Boolean consistent = currentNode.checkConsistency(bound);
            if (consistent == null) {
                return null;
            } else if (consistent) {
                // Target node is propagated and consistent
                listeners.forEach(listener -> {
                    listener.currentNode(currentNode);
                });
                return true;
            } else {
                // We have found an inconsistent node as a consequence of a constraint propagation on the current node
                listeners.forEach(listener -> {
                    listener.inconsistentNode(currentNode);
                });
                return false;
            }
        } else {
            // We look for a common ancestor c_node
            INode c_node = currentNode;
            Set<INode> parents = new HashSet<>(c_node.getLevel());
            while (c_node != null) {
                parents.add(c_node);
                c_node = c_node.getParent();
            }
            c_node = node;
            List<INode> path = new ArrayList<>();
            while (c_node != null && !parents.contains(c_node)) {
                path.add(c_node);
                c_node = c_node.getParent();
            }
            Collections.reverse(path);

            // We pop till the common ancestor
            while (currentNode.getLevel() > c_node.getLevel()) {
                ArrayList<IResolver> revers_resolvers = new ArrayList<>(currentNode.getResolvers());
                Collections.reverse(revers_resolvers);
                revers_resolvers.stream().filter(resolver -> resolver.isResolved()).forEach(resolver -> {
                    resolver.retract();
                });
                currentNode = currentNode.getParent();
                constraintNetwork.pop();
                listeners.forEach(listener -> {
                    listener.currentNode(currentNode);
                });
            }

            // We push till the target node
            path.forEach(n -> {
                constraintNetwork.push();
                currentNode = n;
                n.resolve();
            });
            assert currentNode == node;
            Boolean consistent = currentNode.checkConsistency(bound);
            if (consistent == null) {
                return null;
            } else if (consistent) {
                // Target node is propagated and consistent
                listeners.forEach(listener -> {
                    listener.currentNode(currentNode);
                });
                return true;
            } else {
                // We have found an inconsistent node as a consequence of a constraint propagation on the current node
                listeners.forEach(listener -> {
                    listener.inconsistentNode(currentNode);
                });
                return false;
            }
        }
    }

    @Override
    public boolean solve() {
        LOG.info("Extracting landmarks..");
        long starting_lm_extraction_time = System.nanoTime();
        lm_graph.extractLandmarks();
        long ending_lm_extraction_time = System.nanoTime();
        LOG.log(Level.INFO, "Landmarks extracted in {0}ms", (ending_lm_extraction_time - starting_lm_extraction_time) / 1000000);

        LOG.info("Solving problem..");
        lm_graph.getLandmarks().stream().filter(lm -> lm.getNodes().size() == 1 && lm.getNodes().iterator().next() instanceof IStaticCausalGraph.IPredicateNode).map(lm -> (IStaticCausalGraph.IPredicateNode) lm.getNodes().iterator().next()).filter(lm_goal -> !rpg.getGoals().contains(lm_goal)).forEach(lm_goal -> {
            Map<String, IObject> assignments = new HashMap<>();
            if (lm_goal.getPredicate().getEnclosingScope() instanceof IType) {
                IType type = (IType) lm_goal.getPredicate().getEnclosingScope();
                assignments.put(Constants.SCOPE, constraintNetwork.newEnum(type, type.getInstances()));
            }
            lm_goal.getPredicate().newGoal(null, assignments);
        });
        bound = (int) (properties.containsKey("Bound") ? Integer.parseInt(properties.getProperty("Bound")) : staticCausalGraph.getNodes().stream().filter(node -> !Double.isInfinite(rpg.level(node))).count());
        while (true) {
            while (!fringe.isEmpty()) {
                Boolean reached = goTo(fringe.pollFirst());
                if (reached == null) {
                    breached.add(currentNode);
                } else if (reached) {
                    // Let's select the best flaw to resolve
                    IFlaw flaw = currentNode.selectFlaw();
                    if (flaw == null) {
                        // Hurray!!! We have found a solution!
                        IModel model = constraintNetwork.getModel();
                        listeners.forEach(listener -> {
                            listener.solutionNode(model, currentNode);
                        });

                        // We re-add the solution node into the fringe for subsequent updates
                        fringe.addFirst(currentNode);

                        // We have found a solution..
                        return true;
                    } else {
                        solveFlaw(flaw);
                    }
                }
            }
            if (breached.isEmpty()) {
                // The problem has no solution..
                return false;
            } else {
                bound *= 3 / 2 + 1;
                fringe.addAll(breached);
                breached.clear();
            }
        }
    }

    @Override
    public boolean optimize(INumber objectiveFunction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void solveFlaw(IFlaw flaw) {
        Collection<IResolver> resolvers = flaw.getResolvers();
        List<INode> childs = new ArrayList<>(resolvers.size());
        resolvers.forEach(resolver -> {
            INode child = new Node(currentNode);
            child.addResolver(resolver);
            childs.add(child);
        });
        n_nodes += childs.size();
        listeners.forEach(listener -> {
            listener.branch(currentNode, childs);
        });
        Collections.reverse(childs);
        childs.forEach(child -> fringe.addFirst(child));
    }

    @Override
    public INode getCurrentNode() {
        return currentNode;
    }

    @Override
    public int getSearchSpaceSize() {
        return n_nodes;
    }

    @Override
    public int getFringeSize() {
        return fringe.size();
    }

    @Override
    public void addSolverListener(ISolverListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeSolverListener(ISolverListener listener) {
        listeners.remove(listener);
    }

    @Override
    public ISolver getSolver() {
        return this;
    }

    @Override
    public IScope getEnclosingScope() {
        return null;
    }

    @Override
    public IField getField(String name) throws NoSuchFieldException {
        IField field = fields.get(name);
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
        currentNode.addResolver(new IResolver() {
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

        // not found
        throw new NoSuchMethodException(name + "(" + Stream.of(parameterTypes).map(p -> p.getName()).collect(Collectors.joining(", ")) + ")");
    }

    @Override
    public Collection<IMethod> getMethods() {
        Collection<IMethod> c_methods = new ArrayList<>(methods.size());
        methods.values().forEach((ms) -> {
            c_methods.addAll(ms);
        });
        return Collections.unmodifiableCollection(c_methods);
    }

    @Override
    public void defineMethod(IMethod method) {
        currentNode.addResolver(new IResolver() {
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

        // not found
        throw new ClassNotFoundException(name);
    }

    @Override
    public Map<String, IPredicate> getPredicates() {
        return Collections.unmodifiableMap(predicates);
    }

    @Override
    public void definePredicate(IPredicate predicate) {
        currentNode.addResolver(new IResolver() {
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
                staticCausalGraph.addNode(predicate);
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
                staticCausalGraph.removeNode(staticCausalGraph.getNode(predicate));
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

        // not found
        throw new ClassNotFoundException(name);
    }

    @Override
    public Map<String, IType> getTypes() {
        return Collections.unmodifiableMap(types);
    }

    @Override
    public void defineType(IType type) {
        currentNode.addResolver(new IResolver() {
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
                staticCausalGraph.addType(type);
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
                staticCausalGraph.removeType(type);
                resolved = false;
            }
        });
    }

    @Override
    public IEnvironment getEnclosingEnvironment() {
        return null;
    }

    @Override
    public <T extends IObject> T get(String name) {
        IObject object = objects.get(name);
        if (object != null) {
            return (T) object;
        }

        // not found
        return null;
    }

    @Override
    public Map<String, IObject> getObjects() {
        return Collections.unmodifiableMap(objects);
    }

    @Override
    public void set(String name, IObject object) {
        currentNode.addResolver(new IResolver() {
            private boolean resolved = false;
            private IObject old_value = null;

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
                old_value = objects.put(name, object);
                resolved = true;
            }

            @Override
            public void retract() {
                assert resolved;
                objects.put(name, old_value);
                resolved = false;
            }
        });
    }

    @Override
    public String toString() {
        StringBuilder solver = new StringBuilder();

        Collection<IFormula> all_formulas = new ArrayList<>();
        // We add pure formulas..
        all_formulas.addAll(predicates.values().stream().flatMap(type -> type.getInstances().stream()).map(instance -> (IFormula) instance).collect(Collectors.toList()));
        // We add scoped formulas..
        all_formulas.addAll(types.values().stream().flatMap(type -> type.getPredicates().values().stream()).flatMap(type -> type.getInstances().stream()).map(instance -> (IFormula) instance).collect(Collectors.toList()));

        all_formulas.forEach(formula -> {
            switch (formula.getFormulaState()) {
                case Inactive:
                    solver.append("I ");
                    break;
                case Active:
                    solver.append("A ");
                    break;
                case Unified:
                    solver.append("U ");
                    break;
                default:
                    throw new AssertionError(formula.getFormulaState().name());
            }
            solver.append(formula).append("\n");
        });

        // We add the constraint network..
        solver.append(constraintNetwork);
        return solver.toString();
    }
}
