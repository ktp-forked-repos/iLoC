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
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IStaticCausalGraph;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class RelaxedPlanningGraph {

    private final ISolver solver;
    private final IStaticCausalGraph causal_graph;
    private final Context ctx;
    private final Optimize opt;
    private final Map<IStaticCausalGraph.INode, ArithExpr> nodes = new HashMap<>();
    private final Set<IStaticCausalGraph.IPredicateNode> init_state;
    private final Set<IStaticCausalGraph.IPredicateNode> goals;
    private Model model;
    private boolean dynamic = false;

    RelaxedPlanningGraph(ISolver solver, Set<IStaticCausalGraph.INode> nodes, Set<IStaticCausalGraph.IPredicateNode> init_state, Set<IStaticCausalGraph.IPredicateNode> goals) {
        this.solver = solver;
        this.causal_graph = solver.getStaticCausalGraph();

        HashMap<String, String> cfg = new HashMap<>();
        cfg.put("model", "true");
        this.ctx = new Context(cfg);
        this.opt = ctx.mkOptimize();

        this.init_state = init_state;
        this.goals = goals;

        // Initialization..
        nodes.forEach(node -> {
            if (init_state.contains(node)) {
                this.nodes.put(node, ctx.mkReal("0"));
            } else {
                this.nodes.put(node, ctx.mkRealConst(node.toString()));
            }
        });

        // We create the optimization problem..
        nodes.stream().filter(node -> !init_state.contains(node)).forEach(node -> {
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

    Set<IStaticCausalGraph.IPredicateNode> getInitState() {
        return Collections.unmodifiableSet(init_state);
    }

    Set<IStaticCausalGraph.IPredicateNode> getGoals() {
        return Collections.unmodifiableSet(goals);
    }

    void propagate() {
        Status status = opt.Check();
        assert status == Status.SATISFIABLE;

        this.model = opt.getModel();
    }

    void disable(IStaticCausalGraph.INode node) {
        opt.Add(ctx.mkGe(nodes.get(node), ctx.mkReal(Integer.toString(Integer.MAX_VALUE))));
        Status status = opt.Check();
        assert status == Status.SATISFIABLE;

        this.model = opt.getModel();
    }

    void push() {
        opt.Push();
    }

    void pop() {
        opt.Pop();
    }

    /**
     * Returns the level of the given causal node inside the current relaxed
     * planning graph.
     *
     * @param node the causal node whose level is requested.
     * @return the level of the given causal node.
     */
    double level(IStaticCausalGraph.INode node) {
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
