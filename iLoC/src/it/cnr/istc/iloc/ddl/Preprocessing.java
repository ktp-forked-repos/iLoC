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
package it.cnr.istc.iloc.ddl;

import it.cnr.istc.iloc.api.IDisjunct;
import it.cnr.istc.iloc.api.IDisjunction;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IPredicate;
import it.cnr.istc.iloc.api.IPreference;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IStaticCausalGraph;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Preprocessing extends DDLBaseListener {

    private static final ParseTreeWalker WALKER = new ParseTreeWalker();
    private final ISolver solver;
    private final DDLParser parser;
    private final ParseTreeProperty<IScope> scopes;
    private CNode c_node;

    Preprocessing(ISolver solver, DDLParser parser, ParseTreeProperty<IScope> scopes) {
        this.solver = solver;
        this.parser = parser;
        this.scopes = scopes;
    }

    @Override
    public void enterPredicate_declaration(DDLParser.Predicate_declarationContext ctx) {
        c_node = new CNode(c_node, solver.getStaticCausalGraph().getNode((IPredicate) scopes.get(ctx)));
    }

    @Override
    public void exitPredicate_declaration(DDLParser.Predicate_declarationContext ctx) {
        c_node = c_node.enclosingNode;
    }

    @Override
    public void enterDisjunction_statement(DDLParser.Disjunction_statementContext ctx) {
        if (c_node != null) {
            IStaticCausalGraph.INode node = solver.getStaticCausalGraph().addNode((IDisjunction) scopes.get(ctx));
            solver.getStaticCausalGraph().addEdge(IStaticCausalGraph.IEdge.Type.Goal, c_node.node, node);
            c_node = new CNode(c_node, node);
        }
    }

    @Override
    public void exitDisjunction_statement(DDLParser.Disjunction_statementContext ctx) {
        if (c_node != null) {
            c_node = c_node.enclosingNode;
        }
    }

    @Override
    public void enterDisjunct(DDLParser.DisjunctContext ctx) {
        if (c_node != null) {
            SubgoalCounter counter = new SubgoalCounter();
            WALKER.walk(counter, ctx);
            if (counter.subgoals == 0) {
                // There is no need for creating a new node..
                IStaticCausalGraph.INode no_op_node = solver.getStaticCausalGraph().addNoOp();
                solver.getStaticCausalGraph().addEdge(IStaticCausalGraph.IEdge.Type.Goal, c_node.node, no_op_node);
                c_node = new CNode(c_node, c_node.node);
            } else {
                // We need to create a new node..
                IStaticCausalGraph.INode node = solver.getStaticCausalGraph().addNode((IDisjunct) scopes.get(ctx));
                solver.getStaticCausalGraph().addEdge(IStaticCausalGraph.IEdge.Type.Goal, c_node.node, node);
                c_node = new CNode(c_node, node);
            }
        }
    }

    @Override
    public void exitDisjunct(DDLParser.DisjunctContext ctx) {
        if (c_node != null) {
            c_node = c_node.enclosingNode;
        }
    }

    @Override
    public void enterPreference_statement(DDLParser.Preference_statementContext ctx) {
        if (c_node != null) {
            IStaticCausalGraph.INode node = solver.getStaticCausalGraph().addNode((IPreference) scopes.get(ctx));
            solver.getStaticCausalGraph().addEdge(IStaticCausalGraph.IEdge.Type.Goal, c_node.node, node);
            c_node = new CNode(c_node, node);
        }
    }

    @Override
    public void exitPreference_statement(DDLParser.Preference_statementContext ctx) {
        if (c_node != null) {
            c_node = c_node.enclosingNode;
        }
    }

    @Override
    public void enterFormula_statement(DDLParser.Formula_statementContext ctx) {
        if (c_node != null) {
            IScope c_scope = scopes.get(ctx);
            if (ctx.object != null) {
                for (TerminalNode tn : ctx.object.ID()) {
                    try {
                        IField field = c_scope.getField(tn.getText());
                        c_scope = field.getType();
                    } catch (NoSuchFieldException ex) {
                        parser.notifyErrorListeners(tn.getSymbol(), ex.getMessage(), new RecognitionException(parser, parser.getInputStream(), ctx));
                    }
                }
            } else if (c_scope != solver) {
                // Since ctx.object is null, we have an implicit scope for the current goal
                c_scope = c_scope.getEnclosingScope();
            }

            IStaticCausalGraph.IEdge.Type type = null;
            if (ctx.goal != null) {
                type = IStaticCausalGraph.IEdge.Type.Goal;
            } else if (ctx.fact != null) {
                type = IStaticCausalGraph.IEdge.Type.Fact;
            }

            try {
                solver.getStaticCausalGraph().addEdge(type, c_node.node, solver.getStaticCausalGraph().getNode(c_scope.getPredicate(ctx.predicate.getText())));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Preprocessing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static class CNode {

        private final CNode enclosingNode;
        private final IStaticCausalGraph.INode node;

        CNode(CNode enclosingNode, IStaticCausalGraph.INode node) {
            this.enclosingNode = enclosingNode;
            this.node = node;
        }
    }

    private static class SubgoalCounter extends DDLBaseListener {

        private int subgoals = 0;

        @Override
        public void enterFormula_statement(DDLParser.Formula_statementContext ctx) {
            subgoals++;
        }
    }
}
