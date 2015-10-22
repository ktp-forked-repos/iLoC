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

import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IConstraintNetwork;
import it.cnr.istc.iloc.api.IEnum;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IString;
import it.cnr.istc.iloc.api.IType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class ObjectVisitor extends DDLBaseVisitor<IObject> {

    private static final Logger LOG = Logger.getLogger(ObjectVisitor.class.getName());
    private final ISolver solver;
    private final DDLParser parser;
    private final ParseTreeProperty<IScope> scopes;
    private final IEnvironment environment;

    ObjectVisitor(ISolver solver, DDLParser parser, ParseTreeProperty<IScope> scopes, IEnvironment environment) {
        this.solver = solver;
        this.parser = parser;
        this.scopes = scopes;
        this.environment = environment;
    }

    @Override
    public IObject visitLiteral_expression(DDLParser.Literal_expressionContext ctx) {
        if (ctx.literal().numeric != null) {
            if (ctx.literal().NumericLiteral().getText().contains(".")) {
                return solver.getConstraintNetwork().newReal(ctx.literal().NumericLiteral().getText());
            } else {
                return solver.getConstraintNetwork().newInt(ctx.literal().NumericLiteral().getText());
            }
        } else if (ctx.literal().string != null) {
            IString string = solver.getConstraintNetwork().newString();
            String value = ctx.literal().string.getText();
            string.setValue(value.substring(1, value.length() - 1));
            return string;
        } else if (ctx.literal().t != null) {
            return solver.getConstraintNetwork().newBool("true");
        } else if (ctx.literal().f != null) {
            return solver.getConstraintNetwork().newBool("false");
        } else {
            return null;
        }
    }

    @Override
    public IObject visitParentheses_expression(DDLParser.Parentheses_expressionContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    @SuppressWarnings("unchecked")
    public IObject visitMultiplication_expression(DDLParser.Multiplication_expressionContext ctx) {
        IObject expr0 = visit(ctx.expr(0));
        IObject expr1 = visit(ctx.expr(1));

        IConstraintNetwork network = solver.getConstraintNetwork();
        if (expr0 instanceof INumber && expr1 instanceof INumber) {
            return network.multiply(((INumber) expr0), (INumber) expr1);
        }

        INumber ret_var = (expr0.getType().getName().equals(Constants.REAL) || expr1.getType().getName().equals(Constants.REAL)) ? network.newReal() : network.newInt();
        List<IBool> vars = new ArrayList<>();
        if (expr0 instanceof INumber) {
            INumber num_expr0 = (INumber) expr0;
            IEnum enum_expr1 = (IEnum) expr1;
            for (int i = 0; i < enum_expr1.getEnums().length; i++) {
                vars.add(network.and(
                        network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(i)))),
                        network.eq(ret_var, network.multiply(num_expr0, (INumber) enum_expr1.getEnums()[i]))
                ));
            }
        } else {
            IEnum enum_expr0 = (IEnum) expr0;
            if (expr1 instanceof INumber) {
                INumber num_expr1 = (INumber) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    vars.add(network.and(
                            network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                            network.eq(ret_var, network.multiply((INumber) enum_expr0.getEnums()[i], num_expr1))
                    ));
                }
            } else {
                IEnum enum_expr1 = (IEnum) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    for (int j = 0; j < enum_expr1.getEnums().length; j++) {
                        vars.add(network.and(
                                network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                                network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(j)))),
                                network.eq(ret_var, network.multiply((INumber) enum_expr0.getEnums()[i], (INumber) enum_expr1.getEnums()[j]))
                        ));
                    }
                }
            }
        }
        solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
        return ret_var;
    }

    @Override
    @SuppressWarnings("unchecked")
    public IObject visitDivision_expression(DDLParser.Division_expressionContext ctx) {
        IObject expr0 = visit(ctx.expr(0));
        IObject expr1 = visit(ctx.expr(1));

        IConstraintNetwork network = solver.getConstraintNetwork();
        if (expr0 instanceof INumber && expr1 instanceof INumber) {
            return network.divide(((INumber) expr0), (INumber) expr1);
        }

        INumber ret_var = (expr0.getType().getName().equals(Constants.REAL) || expr1.getType().getName().equals(Constants.REAL)) ? network.newReal() : network.newInt();
        List<IBool> vars = new ArrayList<>();
        if (expr0 instanceof INumber) {
            INumber num_expr0 = (INumber) expr0;
            IEnum enum_expr1 = (IEnum) expr1;
            for (int i = 0; i < enum_expr1.getEnums().length; i++) {
                vars.add(network.and(
                        network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(i)))),
                        network.eq(ret_var, network.divide(num_expr0, (INumber) enum_expr1.getEnums()[i]))
                ));
            }
        } else {
            IEnum enum_expr0 = (IEnum) expr0;
            if (expr1 instanceof INumber) {
                INumber num_expr1 = (INumber) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    vars.add(network.and(
                            network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                            network.eq(ret_var, network.divide((INumber) enum_expr0.getEnums()[i], num_expr1))
                    ));
                }
            } else {
                IEnum enum_expr1 = (IEnum) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    for (int j = 0; j < enum_expr1.getEnums().length; j++) {
                        vars.add(network.and(
                                network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                                network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(j)))),
                                network.eq(ret_var, network.divide((INumber) enum_expr0.getEnums()[i], (INumber) enum_expr1.getEnums()[j]))
                        ));
                    }
                }
            }
        }
        solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
        return ret_var;
    }

    @Override
    @SuppressWarnings("unchecked")
    public IObject visitAddition_expression(DDLParser.Addition_expressionContext ctx) {
        IObject expr0 = visit(ctx.expr(0));
        IObject expr1 = visit(ctx.expr(1));

        IConstraintNetwork network = solver.getConstraintNetwork();
        if (expr0 instanceof INumber && expr1 instanceof INumber) {
            return network.add(((INumber) expr0), (INumber) expr1);
        }

        INumber ret_var = (expr0.getType().getName().equals(Constants.REAL) || expr1.getType().getName().equals(Constants.REAL)) ? network.newReal() : network.newInt();
        List<IBool> vars = new ArrayList<>();
        if (expr0 instanceof INumber) {
            INumber num_expr0 = (INumber) expr0;
            IEnum enum_expr1 = (IEnum) expr1;
            for (int i = 0; i < enum_expr1.getEnums().length; i++) {
                vars.add(network.and(
                        network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(i)))),
                        network.eq(ret_var, network.add(num_expr0, (INumber) enum_expr1.getEnums()[i]))
                ));
            }
        } else {
            IEnum enum_expr0 = (IEnum) expr0;
            if (expr1 instanceof INumber) {
                INumber num_expr1 = (INumber) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    vars.add(network.and(
                            network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                            network.eq(ret_var, network.add((INumber) enum_expr0.getEnums()[i], num_expr1))
                    ));
                }
            } else {
                IEnum enum_expr1 = (IEnum) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    for (int j = 0; j < enum_expr1.getEnums().length; j++) {
                        vars.add(network.and(
                                network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                                network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(j)))),
                                network.eq(ret_var, network.add((INumber) enum_expr0.getEnums()[i], (INumber) enum_expr1.getEnums()[j]))
                        ));
                    }
                }
            }
        }
        solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
        return ret_var;
    }

    @Override
    @SuppressWarnings("unchecked")
    public IObject visitSubtraction_expression(DDLParser.Subtraction_expressionContext ctx) {
        IObject expr0 = visit(ctx.expr(0));
        IObject expr1 = visit(ctx.expr(1));

        IConstraintNetwork network = solver.getConstraintNetwork();
        if (expr0 instanceof INumber && expr1 instanceof INumber) {
            return network.subtract(((INumber) expr0), (INumber) expr1);
        }

        INumber ret_var = (expr0.getType().getName().equals(Constants.REAL) || expr1.getType().getName().equals(Constants.REAL)) ? network.newReal() : network.newInt();
        List<IBool> vars = new ArrayList<>();
        if (expr0 instanceof INumber) {
            INumber num_expr0 = (INumber) expr0;
            IEnum enum_expr1 = (IEnum) expr1;
            for (int i = 0; i < enum_expr1.getEnums().length; i++) {
                vars.add(network.and(
                        network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(i)))),
                        network.eq(ret_var, network.subtract(num_expr0, (INumber) enum_expr1.getEnums()[i]))
                ));
            }
        } else {
            IEnum enum_expr0 = (IEnum) expr0;
            if (expr1 instanceof INumber) {
                INumber num_expr1 = (INumber) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    vars.add(network.and(
                            network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                            network.eq(ret_var, network.subtract((INumber) enum_expr0.getEnums()[i], num_expr1))
                    ));
                }
            } else {
                IEnum enum_expr1 = (IEnum) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    for (int j = 0; j < enum_expr1.getEnums().length; j++) {
                        vars.add(network.and(
                                network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                                network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(j)))),
                                network.eq(ret_var, network.subtract((INumber) enum_expr0.getEnums()[i], (INumber) enum_expr1.getEnums()[j]))
                        ));
                    }
                }
            }
        }
        solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
        return ret_var;
    }

    @Override
    public IObject visitPlus_expression(DDLParser.Plus_expressionContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    @SuppressWarnings("unchecked")
    public IObject visitMinus_expression(DDLParser.Minus_expressionContext ctx) {
        IObject expr = visit(ctx.expr());

        IConstraintNetwork network = solver.getConstraintNetwork();
        if (expr instanceof INumber) {
            return network.negate((INumber) visit(ctx.expr()));
        } else {
            IEnum enum_expr = (IEnum) expr;
            INumber ret_var = expr.getType().getName().equals(Constants.REAL) ? network.newReal() : network.newInt();

            List<IBool> vars = new ArrayList<>(enum_expr.getEnums().length);
            for (int i = 0; i < enum_expr.getEnums().length; i++) {
                vars.add(network.and(
                        network.not(network.eq(enum_expr.getVar(), network.newInt(Integer.toString(i)))),
                        network.eq(ret_var, network.negate((INumber) enum_expr.getEnums()[i]))
                ));
            }
            solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
            return ret_var;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public IObject visitNot_expression(DDLParser.Not_expressionContext ctx) {
        IObject expr = visit(ctx.expr());

        IConstraintNetwork network = solver.getConstraintNetwork();
        if (expr instanceof IBool) {
            return network.not((IBool) visit(ctx.expr()));
        } else {
            IEnum enum_expr = (IEnum) expr;
            IBool ret_var = network.newBool();

            List<IBool> vars = new ArrayList<>(enum_expr.getEnums().length);
            for (int i = 0; i < enum_expr.getEnums().length; i++) {
                vars.add(network.and(
                        network.not(network.eq(enum_expr.getVar(), network.newInt(Integer.toString(i)))),
                        network.eq(ret_var, network.not((IBool) enum_expr.getEnums()[i]))
                ));
            }
            solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
            return ret_var;
        }
    }

    @Override
    public IObject visitQualified_id_expression(DDLParser.Qualified_id_expressionContext ctx) {
        IEnvironment c_environment = environment;
        if (ctx.qualified_id().t != null) {
            c_environment = c_environment.get("this");
        } else if (ctx.qualified_id().s != null) {
            c_environment = c_environment.get("super");
        }
        for (TerminalNode tn : ctx.qualified_id().ID()) {
            c_environment = c_environment.get(tn.getText());
        }
        return (IObject) c_environment;
    }

    @Override
    public IObject visitFunction_expression(DDLParser.Function_expressionContext ctx) {
        IEnvironment c_environment = environment;
        IScope scope = scopes.get(ctx);
        if (ctx.object.t != null) {
            c_environment = c_environment.get("this");
        } else if (ctx.object.s != null) {
            try {
                c_environment = c_environment.get("super");
                scope = scope.getField("super").getType();
            } catch (NoSuchFieldException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        for (TerminalNode tn : ctx.qualified_id().ID()) {
            try {
                IField field = scope.getField(tn.getText());
                c_environment = c_environment.get(tn.getText());
                scope = field.getType();
            } catch (NoSuchFieldException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        try {
            return scope.getMethod(ctx.function_name.getText(), Objects.isNull(ctx.expr_list()) ? new IType[0] : ctx.expr_list().expr().stream().map(expr -> new TypeVisitor(solver, parser, scopes).visit(expr)).toArray(IType[]::new)).invoke((IObject) c_environment, Objects.isNull(ctx.expr_list()) ? new IObject[0] : ctx.expr_list().expr().stream().map(expr -> new ObjectVisitor(solver, parser, scopes, environment).visit(expr)).toArray(IObject[]::new));
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public INumber visitRange_expression(DDLParser.Range_expressionContext ctx) {
        IConstraintNetwork network = solver.getConstraintNetwork();
        INumber min = (INumber) new ObjectVisitor(solver, parser, scopes, environment).visit(ctx.min);
        INumber max = (INumber) new ObjectVisitor(solver, parser, scopes, environment).visit(ctx.max);
        INumber number = (ctx.min.getText().contains(".") || ctx.max.getText().contains(".")) ? network.newReal() : network.newInt();
        solver.getCurrentNode().addResolver(new AssertFactsResolver(network, network.leq(min, number), network.leq(number, max)));
        return number;
    }

    @Override
    public IObject visitConstructor_expression(DDLParser.Constructor_expressionContext ctx) {
        IType type = new TypeVisitor(solver, parser, scopes).visit(ctx.type());
        try {
            return type.getConstructor(Objects.isNull(ctx.expr_list()) ? new IType[0] : ctx.expr_list().expr().stream().map(expr -> new TypeVisitor(solver, parser, scopes).visit(expr)).toArray(IType[]::new)).newInstance(environment, Objects.isNull(ctx.expr_list()) ? new IObject[0] : ctx.expr_list().expr().stream().map(expr -> new ObjectVisitor(solver, parser, scopes, environment).visit(expr)).toArray(IObject[]::new));
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public IBool visitEq_expression(DDLParser.Eq_expressionContext ctx) {
        return visit(ctx.expr(0)).eq(visit(ctx.expr(1)));
    }

    @Override
    public IObject visitLeq_expression(DDLParser.Leq_expressionContext ctx) {
        IObject expr0 = visit(ctx.expr(0));
        IObject expr1 = visit(ctx.expr(1));

        IConstraintNetwork network = solver.getConstraintNetwork();
        if (expr0 instanceof INumber && expr1 instanceof INumber) {
            return network.leq(((INumber) expr0), (INumber) expr1);
        }

        IBool ret_var = network.newBool();
        List<IBool> vars = new ArrayList<>();
        if (expr0 instanceof INumber) {
            INumber num_expr0 = (INumber) expr0;
            IEnum enum_expr1 = (IEnum) expr1;
            for (int i = 0; i < enum_expr1.getEnums().length; i++) {
                vars.add(network.and(
                        network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(i)))),
                        network.eq(ret_var, network.leq(num_expr0, (INumber) enum_expr1.getEnums()[i]))
                ));
            }
        } else {
            IEnum enum_expr0 = (IEnum) expr0;
            if (expr1 instanceof INumber) {
                INumber num_expr1 = (INumber) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    vars.add(network.and(
                            network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                            network.eq(ret_var, network.leq((INumber) enum_expr0.getEnums()[i], num_expr1))
                    ));
                }
            } else {
                IEnum enum_expr1 = (IEnum) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    for (int j = 0; j < enum_expr1.getEnums().length; j++) {
                        vars.add(network.and(
                                network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                                network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(j)))),
                                network.eq(ret_var, network.leq((INumber) enum_expr0.getEnums()[i], (INumber) enum_expr1.getEnums()[j]))
                        ));
                    }
                }
            }
        }
        solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
        return ret_var;
    }

    @Override
    public IObject visitGeq_expression(DDLParser.Geq_expressionContext ctx) {
        IObject expr0 = visit(ctx.expr(0));
        IObject expr1 = visit(ctx.expr(1));

        IConstraintNetwork network = solver.getConstraintNetwork();
        if (expr0 instanceof INumber && expr1 instanceof INumber) {
            return network.geq(((INumber) expr0), (INumber) expr1);
        }

        IBool ret_var = network.newBool();
        List<IBool> vars = new ArrayList<>();
        if (expr0 instanceof INumber) {
            INumber num_expr0 = (INumber) expr0;
            IEnum enum_expr1 = (IEnum) expr1;
            for (int i = 0; i < enum_expr1.getEnums().length; i++) {
                vars.add(network.and(
                        network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(i)))),
                        network.eq(ret_var, network.geq(num_expr0, (INumber) enum_expr1.getEnums()[i]))
                ));
            }
        } else {
            IEnum enum_expr0 = (IEnum) expr0;
            if (expr1 instanceof INumber) {
                INumber num_expr1 = (INumber) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    vars.add(network.and(
                            network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                            network.eq(ret_var, network.geq((INumber) enum_expr0.getEnums()[i], num_expr1))
                    ));
                }
            } else {
                IEnum enum_expr1 = (IEnum) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    for (int j = 0; j < enum_expr1.getEnums().length; j++) {
                        vars.add(network.and(
                                network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                                network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(j)))),
                                network.eq(ret_var, network.geq((INumber) enum_expr0.getEnums()[i], (INumber) enum_expr1.getEnums()[j]))
                        ));
                    }
                }
            }
        }
        solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
        return ret_var;
    }

    @Override
    public IObject visitLt_expression(DDLParser.Lt_expressionContext ctx) {
        IObject expr0 = visit(ctx.expr(0));
        IObject expr1 = visit(ctx.expr(1));

        IConstraintNetwork network = solver.getConstraintNetwork();
        if (expr0 instanceof INumber && expr1 instanceof INumber) {
            return network.lt(((INumber) expr0), (INumber) expr1);
        }

        IBool ret_var = network.newBool();
        List<IBool> vars = new ArrayList<>();
        if (expr0 instanceof INumber) {
            INumber num_expr0 = (INumber) expr0;
            IEnum enum_expr1 = (IEnum) expr1;
            for (int i = 0; i < enum_expr1.getEnums().length; i++) {
                vars.add(network.and(
                        network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(i)))),
                        network.eq(ret_var, network.lt(num_expr0, (INumber) enum_expr1.getEnums()[i]))
                ));
            }
        } else {
            IEnum enum_expr0 = (IEnum) expr0;
            if (expr1 instanceof INumber) {
                INumber num_expr1 = (INumber) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    vars.add(network.and(
                            network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                            network.eq(ret_var, network.lt((INumber) enum_expr0.getEnums()[i], num_expr1))
                    ));
                }
            } else {
                IEnum enum_expr1 = (IEnum) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    for (int j = 0; j < enum_expr1.getEnums().length; j++) {
                        vars.add(network.and(
                                network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                                network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(j)))),
                                network.eq(ret_var, network.lt((INumber) enum_expr0.getEnums()[i], (INumber) enum_expr1.getEnums()[j]))
                        ));
                    }
                }
            }
        }
        solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
        return ret_var;
    }

    @Override
    public IObject visitGt_expression(DDLParser.Gt_expressionContext ctx) {
        IObject expr0 = visit(ctx.expr(0));
        IObject expr1 = visit(ctx.expr(1));

        IConstraintNetwork network = solver.getConstraintNetwork();
        if (expr0 instanceof INumber && expr1 instanceof INumber) {
            return network.gt(((INumber) expr0), (INumber) expr1);
        }

        IBool ret_var = network.newBool();
        List<IBool> vars = new ArrayList<>();
        if (expr0 instanceof INumber) {
            INumber num_expr0 = (INumber) expr0;
            IEnum enum_expr1 = (IEnum) expr1;
            for (int i = 0; i < enum_expr1.getEnums().length; i++) {
                vars.add(network.and(
                        network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(i)))),
                        network.eq(ret_var, network.gt(num_expr0, (INumber) enum_expr1.getEnums()[i]))
                ));
            }
        } else {
            IEnum enum_expr0 = (IEnum) expr0;
            if (expr1 instanceof INumber) {
                INumber num_expr1 = (INumber) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    vars.add(network.and(
                            network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                            network.eq(ret_var, network.gt((INumber) enum_expr0.getEnums()[i], num_expr1))
                    ));
                }
            } else {
                IEnum enum_expr1 = (IEnum) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    for (int j = 0; j < enum_expr1.getEnums().length; j++) {
                        vars.add(network.and(
                                network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                                network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(j)))),
                                network.eq(ret_var, network.gt((INumber) enum_expr0.getEnums()[i], (INumber) enum_expr1.getEnums()[j]))
                        ));
                    }
                }
            }
        }
        solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
        return ret_var;
    }

    @Override
    public IBool visitNeq_expression(DDLParser.Neq_expressionContext ctx) {
        return solver.getConstraintNetwork().not(visit(ctx.expr(0)).eq(visit(ctx.expr(1))));
    }

    @Override
    @SuppressWarnings("unchecked")
    public IObject visitImplication_expression(DDLParser.Implication_expressionContext ctx) {
        IObject expr0 = visit(ctx.expr(0));
        IObject expr1 = visit(ctx.expr(1));

        IConstraintNetwork network = solver.getConstraintNetwork();
        if (expr0 instanceof IBool && expr1 instanceof IBool) {
            return network.or(network.not(((IBool) expr0)), (IBool) expr1);
        }

        IBool ret_var = network.newBool();
        List<IBool> vars = new ArrayList<>();
        if (expr0 instanceof IBool) {
            IBool bool_expr0 = (IBool) expr0;
            IEnum enum_expr1 = (IEnum) expr1;
            for (int i = 0; i < enum_expr1.getEnums().length; i++) {
                vars.add(network.and(
                        network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(i)))),
                        network.eq(ret_var, network.or(network.not(bool_expr0), (IBool) enum_expr1.getEnums()[i]))
                ));
            }
        } else {
            IEnum enum_expr0 = (IEnum) expr0;
            if (expr1 instanceof IBool) {
                IBool bool_expr1 = (IBool) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    vars.add(network.and(
                            network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                            network.eq(ret_var, network.or(network.not((IBool) enum_expr0.getEnums()[i]), bool_expr1))
                    ));
                }
            } else {
                IEnum enum_expr1 = (IEnum) expr1;
                for (int i = 0; i < enum_expr0.getEnums().length; i++) {
                    for (int j = 0; j < enum_expr1.getEnums().length; j++) {
                        vars.add(network.and(
                                network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(i)))),
                                network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(j)))),
                                network.eq(ret_var, network.or(network.not((IBool) enum_expr0.getEnums()[i]), (IBool) enum_expr1.getEnums()[j]))
                        ));
                    }
                }
            }
        }
        solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
        return ret_var;
    }

    @Override
    public IObject visitDisjunction_expression(DDLParser.Disjunction_expressionContext ctx) {
        boolean all_single = true;
        IObject[] bools = new IObject[ctx.expr().size()];
        for (int i = 0; i < bools.length; i++) {
            bools[i] = visit(ctx.expr(i));
            if (!(bools[i] instanceof IBool)) {
                all_single = false;
            }
        }
        if (all_single) {
            IBool[] c_bools = new IBool[ctx.expr().size()];
            for (int i = 0; i < c_bools.length; i++) {
                c_bools[i] = (IBool) bools[i];
            }
            return solver.getConstraintNetwork().or(c_bools);
        } else {
            IConstraintNetwork network = solver.getConstraintNetwork();
            IObject c_val = bools[0];
            for (int i = 1; i < bools.length; i++) {
                if (c_val instanceof IBool && bools[i] instanceof IBool) {
                    c_val = network.or(((IBool) c_val), (IBool) bools[i]);
                    continue;
                }
                IBool ret_var = network.newBool();
                List<IBool> vars = new ArrayList<>();
                if (c_val instanceof IBool) {
                    IBool bool_expr0 = (IBool) c_val;
                    IEnum enum_expr1 = (IEnum) bools[i];
                    for (int j = 0; j < enum_expr1.getEnums().length; j++) {
                        vars.add(network.and(
                                network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(j)))),
                                network.eq(ret_var, network.or(bool_expr0, (IBool) enum_expr1.getEnums()[j]))
                        ));
                    }
                } else {
                    IEnum enum_expr0 = (IEnum) c_val;
                    if (bools[i] instanceof IBool) {
                        IBool bool_expr1 = (IBool) bools[i];
                        for (int j = 0; j < enum_expr0.getEnums().length; j++) {
                            vars.add(network.and(
                                    network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(j)))),
                                    network.eq(ret_var, network.or((IBool) enum_expr0.getEnums()[j], bool_expr1))
                            ));
                        }
                    } else {
                        IEnum enum_expr1 = (IEnum) bools[i];
                        for (int j = 0; j < enum_expr0.getEnums().length; j++) {
                            for (int k = 0; k < enum_expr1.getEnums().length; k++) {
                                vars.add(network.and(
                                        network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(j)))),
                                        network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(k)))),
                                        network.eq(ret_var, network.or((IBool) enum_expr0.getEnums()[j], (IBool) enum_expr1.getEnums()[k]))
                                ));
                            }
                        }
                    }
                }
                solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
                c_val = ret_var;
            }
            return c_val;
        }
    }

    @Override
    public IObject visitConjunction_expression(DDLParser.Conjunction_expressionContext ctx) {
        boolean all_single = true;
        IObject[] bools = new IObject[ctx.expr().size()];
        for (int i = 0; i < bools.length; i++) {
            bools[i] = visit(ctx.expr(i));
            if (!(bools[i] instanceof IBool)) {
                all_single = false;
            }
        }
        if (all_single) {
            IBool[] c_bools = new IBool[ctx.expr().size()];
            for (int i = 0; i < c_bools.length; i++) {
                c_bools[i] = (IBool) bools[i];
            }
            return solver.getConstraintNetwork().and(c_bools);
        } else {
            IConstraintNetwork network = solver.getConstraintNetwork();
            IObject c_val = bools[0];
            for (int i = 1; i < bools.length; i++) {
                if (c_val instanceof IBool && bools[i] instanceof IBool) {
                    c_val = network.and(((IBool) c_val), (IBool) bools[i]);
                    continue;
                }
                IBool ret_var = network.newBool();
                List<IBool> vars = new ArrayList<>();
                if (c_val instanceof IBool) {
                    IBool bool_expr0 = (IBool) c_val;
                    IEnum enum_expr1 = (IEnum) bools[i];
                    for (int j = 0; j < enum_expr1.getEnums().length; j++) {
                        vars.add(network.and(
                                network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(j)))),
                                network.eq(ret_var, network.and(bool_expr0, (IBool) enum_expr1.getEnums()[j]))
                        ));
                    }
                } else {
                    IEnum enum_expr0 = (IEnum) c_val;
                    if (bools[i] instanceof IBool) {
                        IBool bool_expr1 = (IBool) bools[i];
                        for (int j = 0; j < enum_expr0.getEnums().length; j++) {
                            vars.add(network.and(
                                    network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(j)))),
                                    network.eq(ret_var, network.and((IBool) enum_expr0.getEnums()[j], bool_expr1))
                            ));
                        }
                    } else {
                        IEnum enum_expr1 = (IEnum) bools[i];
                        for (int j = 0; j < enum_expr0.getEnums().length; j++) {
                            for (int k = 0; k < enum_expr1.getEnums().length; k++) {
                                vars.add(network.and(
                                        network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(j)))),
                                        network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(k)))),
                                        network.eq(ret_var, network.and((IBool) enum_expr0.getEnums()[j], (IBool) enum_expr1.getEnums()[k]))
                                ));
                            }
                        }
                    }
                }
                solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
                c_val = ret_var;
            }
            return c_val;
        }
    }

    @Override
    public IObject visitExclusive_disjunction_expression(DDLParser.Exclusive_disjunction_expressionContext ctx) {
        boolean all_single = true;
        IObject[] bools = new IObject[ctx.expr().size()];
        for (int i = 0; i < bools.length; i++) {
            bools[i] = visit(ctx.expr(i));
            if (!(bools[i] instanceof IBool)) {
                all_single = false;
            }
        }
        if (all_single) {
            IBool[] c_bools = new IBool[ctx.expr().size()];
            for (int i = 0; i < c_bools.length; i++) {
                c_bools[i] = (IBool) bools[i];
            }
            return solver.getConstraintNetwork().xor(c_bools);
        } else {
            IConstraintNetwork network = solver.getConstraintNetwork();
            IObject c_val = bools[0];
            for (int i = 1; i < bools.length; i++) {
                if (c_val instanceof IBool && bools[i] instanceof IBool) {
                    c_val = network.xor(((IBool) c_val), (IBool) bools[i]);
                    continue;
                }
                IBool ret_var = network.newBool();
                List<IBool> vars = new ArrayList<>();
                if (c_val instanceof IBool) {
                    IBool bool_expr0 = (IBool) c_val;
                    IEnum enum_expr1 = (IEnum) bools[i];
                    for (int j = 0; j < enum_expr1.getEnums().length; j++) {
                        vars.add(network.and(
                                network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(j)))),
                                network.eq(ret_var, network.xor(bool_expr0, (IBool) enum_expr1.getEnums()[j]))
                        ));
                    }
                } else {
                    IEnum enum_expr0 = (IEnum) c_val;
                    if (bools[i] instanceof IBool) {
                        IBool bool_expr1 = (IBool) bools[i];
                        for (int j = 0; j < enum_expr0.getEnums().length; j++) {
                            vars.add(network.and(
                                    network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(j)))),
                                    network.eq(ret_var, network.xor((IBool) enum_expr0.getEnums()[j], bool_expr1))
                            ));
                        }
                    } else {
                        IEnum enum_expr1 = (IEnum) bools[i];
                        for (int j = 0; j < enum_expr0.getEnums().length; j++) {
                            for (int k = 0; k < enum_expr1.getEnums().length; k++) {
                                vars.add(network.and(
                                        network.not(network.eq(enum_expr0.getVar(), network.newInt(Integer.toString(j)))),
                                        network.not(network.eq(enum_expr1.getVar(), network.newInt(Integer.toString(k)))),
                                        network.eq(ret_var, network.xor((IBool) enum_expr0.getEnums()[j], (IBool) enum_expr1.getEnums()[k]))
                                ));
                            }
                        }
                    }
                }
                solver.getCurrentNode().addResolver(new AssertFactsResolver(network, vars.toArray(new IBool[vars.size()])));
                c_val = ret_var;
            }
            return c_val;
        }
    }
}
