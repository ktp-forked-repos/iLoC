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

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.RatNum;
import com.microsoft.z3.Status;
import it.cnr.istc.iloc.api.FormulaState;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IRelaxedPlanningGraph;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IStaticCausalGraph;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class RelaxedPlanningGraph implements IRelaxedPlanningGraph {

    private final ISolver solver;
    private final IStaticCausalGraph causal_graph;
    private Context ctx;
    private Optimize opt;
    private final Map<IStaticCausalGraph.INode, ArithExpr> nodes = new HashMap<>();
    private Model model;
    private boolean dynamic = false;

    RelaxedPlanningGraph(ISolver solver) {
        this.solver = solver;
        this.causal_graph = solver.getStaticCausalGraph();
    }

    @Override
    public void extract() {
        this.nodes.clear();

        HashMap<String, String> cfg = new HashMap<>();
        cfg.put("model", "true");
        this.ctx = new Context(cfg);
        this.opt = ctx.mkOptimize();

        Set<IStaticCausalGraph.INode> all_nodes = causal_graph.getNodes().stream().collect(Collectors.toSet());
        // We define the initial state ..
        Set<IStaticCausalGraph.IPredicateNode> init_state = all_nodes.stream().filter(node -> node instanceof IStaticCausalGraph.IPredicateNode).map(node -> (IStaticCausalGraph.IPredicateNode) node).flatMap(predicate -> predicate.getPredicate().getInstances().stream().map(instance -> (IFormula) instance).filter(formula -> formula.getFormulaState() == FormulaState.Active)).map(formula -> causal_graph.getNode(formula.getType())).collect(Collectors.toSet());
        // .. and the goal state
        Set<IStaticCausalGraph.IPredicateNode> goals = all_nodes.stream().filter(node -> node instanceof IStaticCausalGraph.IPredicateNode).map(node -> (IStaticCausalGraph.IPredicateNode) node).flatMap(predicate -> predicate.getPredicate().getInstances().stream().map(instance -> (IFormula) instance).filter(formula -> formula.getFormulaState() == FormulaState.Inactive).map(formula -> causal_graph.getNode(formula.getType())).filter(node -> !init_state.contains(node))).collect(Collectors.toSet());

        // Initialization..
        all_nodes.forEach(node -> {
            if (init_state.contains(node)) {
                this.nodes.put(node, ctx.mkReal("0"));
            } else {
                this.nodes.put(node, ctx.mkRealConst(node.toString()));
            }
        });

        // We create the optimization problem..
        all_nodes.stream().filter(node -> !init_state.contains(node)).forEach(node -> {
            if (node instanceof IStaticCausalGraph.IDisjunctionNode) {
                opt.Add(ctx.mkOr(node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).map(target -> ctx.mkGe(this.nodes.get(node), ctx.mkAdd(ctx.mkReal("1"), this.nodes.get(target)))).toArray(BoolExpr[]::new)));
            } else if (node instanceof IStaticCausalGraph.IPreferenceNode) {
                throw new UnsupportedOperationException("Preferences estimation is not supported yet..");
            } else {
                node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).forEach(target -> opt.Add(ctx.mkGe(this.nodes.get(node), ctx.mkAdd(ctx.mkReal("1"), this.nodes.get(target)))));
            }
        });

        opt.MkMinimize(ctx.mkAdd(causal_graph.getNodes().stream().filter(node -> !init_state.contains(node) && !goals.contains(node)).map(node -> this.nodes.get(node)).toArray(ArithExpr[]::new)));
    }

    @Override
    public boolean propagate() {
        Status status = opt.Check();
        this.model = opt.getModel();
        return status == Status.SATISFIABLE;
    }

    @Override
    public void disable(IStaticCausalGraph.INode node) {
        opt.Add(ctx.mkGe(nodes.get(node), ctx.mkReal(Integer.toString(Integer.MAX_VALUE))));
        Status status = opt.Check();
        assert status == Status.SATISFIABLE;

        this.model = opt.getModel();
    }

    @Override
    public void push() {
        opt.Push();
    }

    @Override
    public void pop() {
        opt.Pop();
    }

    @Override
    public double level(IStaticCausalGraph.INode node) {
        Expr evaluate = model.evaluate(nodes.get(node), false);
        RatNum c_real = (RatNum) evaluate;
        double value = new BigDecimal(c_real.toDecimalString(1).replace("?", "")).doubleValue();
        if (value >= Integer.MAX_VALUE) {
            return Double.POSITIVE_INFINITY;
        } else {
            return value;
        }
    }

    @Override
    public String toString() {
        return model.toString();
    }
}
