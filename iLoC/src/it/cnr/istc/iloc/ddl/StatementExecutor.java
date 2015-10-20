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

import it.cnr.istc.iloc.DisjunctionFlaw;
import it.cnr.istc.iloc.Field;
import it.cnr.istc.iloc.PreferenceFlaw;
import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IConstructor;
import it.cnr.istc.iloc.api.IDisjunction;
import it.cnr.istc.iloc.api.IEnum;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IInt;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IPredicate;
import it.cnr.istc.iloc.api.IPreference;
import it.cnr.istc.iloc.api.IReal;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class StatementExecutor extends DDLBaseVisitor<Boolean> {

    private final ISolver solver;
    private final DDLParser parser;
    private final ParseTreeProperty<IScope> scopes;
    private IEnvironment environment;

    StatementExecutor(ISolver solver, DDLParser parser, ParseTreeProperty<IScope> scopes, IEnvironment environment) {
        this.solver = solver;
        this.parser = parser;
        this.scopes = scopes;
        this.environment = environment;
    }

    @Override
    public Boolean visitAssignment_statement(DDLParser.Assignment_statementContext ctx) {
        IEnvironment c_environment = environment;
        IScope c_scope = scopes.get(ctx);
        if (ctx.object != null) {
            if (ctx.object.t != null) {
                c_environment = c_environment.get("this");
            } else if (ctx.object.s != null) {
                try {
                    c_environment = c_environment.get("super");
                    c_scope = c_scope.getField("super").getType();
                } catch (NoSuchFieldException ex) {
                    parser.notifyErrorListeners(((TerminalNode) ctx.object.getChild(0)).getSymbol(), ex.getMessage(), new RecognitionException(parser, parser.getInputStream(), ctx.expr()));
                }
            }
            for (TerminalNode tn : ctx.object.ID()) {
                try {
                    IField field = c_scope.getField(tn.getText());
                    c_environment = c_environment.get(tn.getText());
                    c_scope = field.getType();
                } catch (NoSuchFieldException ex) {
                    parser.notifyErrorListeners(tn.getSymbol(), ex.getMessage(), new RecognitionException(parser, parser.getInputStream(), ctx.expr()));
                }
            }
        }
        c_environment.set(ctx.field.getText(), new ObjectVisitor(solver, parser, scopes, environment).visit(ctx.expr()));
        return Boolean.TRUE;
    }

    @Override
    public Boolean visitLocal_variable_statement(DDLParser.Local_variable_statementContext ctx) {
        IType c_type = new TypeVisitor(solver, parser, scopes).visit(ctx.type());
        ctx.variable_dec().stream().forEach(variable_dec -> {
            IObject object;
            if (variable_dec.expr() != null) {
                object = new ObjectVisitor(solver, parser, scopes, environment).visit(variable_dec.expr());
            } else if (c_type.isPrimitive()) {
                object = c_type.newInstance(environment);
            } else {
                object = c_type.newExistential();
            }
            if (c_type.getName().equals(Constants.INT) && object.getType().getName().equals(Constants.REAL)) {
                object = solver.getConstraintNetwork().toInt((IReal) object);
            } else if (c_type.getName().equals(Constants.REAL) && object.getType().getName().equals(Constants.INT)) {
                object = solver.getConstraintNetwork().toReal((IInt) object);
            }
            environment.set(variable_dec.name.getText(), object);
            if (environment == solver) {
                solver.defineField(new Field(variable_dec.name.getText(), c_type));
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public Boolean visitExpression_statement(DDLParser.Expression_statementContext ctx) {
        IBool expr = (IBool) new ObjectVisitor(solver, parser, scopes, environment).visit(ctx.expr());
        solver.getCurrentNode().addResolver(new AssertFactsResolver(solver.getConstraintNetwork(), expr));
        return Boolean.TRUE;
    }

    @Override
    public Boolean visitFormula_statement(DDLParser.Formula_statementContext ctx) {
        // We use the environment for retrieving the scope of the goal
        IEnvironment c_environment = environment;
        // This is the language scope. Do not confuse this scope with the goal scope! This scope is intented for retrieving the goal predicate
        IScope c_scope = scopes.get(ctx);
        if (ctx.object != null) {
            // We are creating a new goal whose scope is given by ctx.object
            if (ctx.object.t != null) {
                // ctx.object starts with "this".
                c_scope = c_environment.get("this").getType();
                c_environment = c_environment.get("this");
            } else if (ctx.object.s != null) {
                // ctx.object starts with "super".
                c_scope = c_environment.get("super").getType();
                c_environment = c_environment.get("super");
            }
            // Neither "this" nor "super" are included inside the ctx.object.ID() hence we can retrieve the goal scope
            for (TerminalNode tn : ctx.object.ID()) {
                c_scope = c_environment.get(tn.getText()).getType();
                c_environment = c_environment.get(tn.getText());
            }
        } else if (c_scope != solver) {
            // Since ctx.object is null, we have an implicit scope for the current goal
            // c_scope can be a disjunction or a preference..
            while (!(c_scope instanceof IPredicate || c_scope instanceof IConstructor)) {
                c_scope = c_scope.getEnclosingScope();
                c_environment = c_environment.getEnclosingEnvironment();
            }
            if (c_scope instanceof IPredicate) {
                // The implicit goal is defined inside a rule
                c_environment = c_environment.getEnclosingEnvironment();
            } else {
                // The implicit goal is defined inside a constructor
            }
            c_environment = c_environment.getEnclosingEnvironment();
            // We will use this variable as the scope of the new formula..
            assert c_environment instanceof IObject;
        }

        assert c_environment instanceof IObject || c_environment instanceof ISolver;
        try {
            IPredicate predicate = c_scope.getPredicate(ctx.predicate.getText());

            Map<String, IObject> assignments = new HashMap<>();
            if (ctx.assignment_list() != null) {
                ctx.assignment_list().assignment().stream().forEach(ac -> {
                    assignments.put(ac.field.getText(), new ObjectVisitor(solver, parser, scopes, environment).visit(ac.value));
                });
            }

            if (c_environment instanceof IObject) {
                if (c_environment instanceof IEnum) {
                    assignments.put(Constants.SCOPE, (IObject) c_environment);
                } else {
                    assignments.put(Constants.SCOPE, solver.getConstraintNetwork().newEnum((IType) c_scope, Arrays.asList((IObject) c_environment)));
                }
            }

            IFormula formula = null;
            IFormula cause = (environment.get("this") instanceof IFormula) ? (IFormula) environment.get("this") : null;

            if (ctx.goal != null) {
                formula = predicate.newGoal(cause, assignments);
            } else if (ctx.fact != null) {
                formula = predicate.newFact(cause, assignments);
            }

            environment.set(ctx.name.getText(), formula);
            if (environment == solver) {
                solver.defineField(new Field(ctx.name.getText(), predicate));
            }
        } catch (ClassNotFoundException ex) {
            parser.notifyErrorListeners(ctx.predicate, ex.getMessage(), new RecognitionException(parser, parser.getInputStream(), ctx));
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean visitReturn_statement(DDLParser.Return_statementContext ctx) {
        environment.set("return", new ObjectVisitor(solver, parser, scopes, environment).visit(ctx.expr()));
        return Boolean.TRUE;
    }

    @Override
    public Boolean visitDisjunction_statement(DDLParser.Disjunction_statementContext ctx) {
        solver.getCurrentNode().enqueue(new DisjunctionFlaw(environment, (IDisjunction) scopes.get(ctx)));
        return Boolean.TRUE;
    }

    @Override
    public Boolean visitPreference_statement(DDLParser.Preference_statementContext ctx) {
        solver.getCurrentNode().enqueue(new PreferenceFlaw(environment, (IPreference) scopes.get(ctx)));
        return Boolean.TRUE;
    }
}
