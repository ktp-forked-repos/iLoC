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
package it.cnr.istc.iloc.pddl;

import java.math.BigDecimal;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class TermVisitor extends PDDLBaseVisitor<Term> {

    private static final ParseTreeWalker WALKER = new ParseTreeWalker();
    private final PDDLParser parser;
    private final Domain domain;
    private final Problem problem;
    private Term term = null;

    TermVisitor(PDDLParser parser, Domain domain, Problem problem) {
        this.parser = parser;
        this.domain = domain;
        this.problem = problem;
    }

    @Override
    public Term visitEmptyOr_pre_GD(PDDLParser.EmptyOr_pre_GDContext ctx) {
        return visit(ctx.pre_GD());
    }

    @Override
    public Term visitEmptyOr_effect(PDDLParser.EmptyOr_effectContext ctx) {
        return visit(ctx.effect());
    }

    @Override
    public Term visitEmptyOr_da_GD(PDDLParser.EmptyOr_da_GDContext ctx) {
        return visit(ctx.da_GD());
    }

    @Override
    public Term visitEmptyOr_da_effect(PDDLParser.EmptyOr_da_effectContext ctx) {
        return visit(ctx.da_effect());
    }

    @Override
    public Term visitPre_GD_pref_GD(PDDLParser.Pre_GD_pref_GDContext ctx) {
        return visit(ctx.pref_GD());
    }

    @Override
    public Term visitPre_GD_and(PDDLParser.Pre_GD_andContext ctx) {
        AndTerm and = new AndTerm(term);
        term = and;
        ctx.pre_GD().stream().map(pre_GD -> visit(pre_GD)).forEach(pre_GD -> and.addTerm(pre_GD));
        return and;
    }

    @Override
    public Term visitPre_GD_forall(PDDLParser.Pre_GD_forallContext ctx) {
        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
        WALKER.walk(typedListVariable, ctx.typed_list_variable());
        ForAllTerm for_all_term = new ForAllTerm(term);
        term = for_all_term;
        for_all_term.setFormula(visit(ctx.pre_GD()));
        return for_all_term;
    }

    @Override
    public Term visitPref_GD_preference_gD(PDDLParser.Pref_GD_preference_gDContext ctx) {
        throw new UnsupportedOperationException("Not supported yet: " + ctx.toStringTree(parser));
    }

    @Override
    public Term visitPref_GD_gD(PDDLParser.Pref_GD_gDContext ctx) {
        return visit(ctx.gD());
    }

    @Override
    public Term visitGd_atomic_formula_term(PDDLParser.Gd_atomic_formula_termContext ctx) {
        return visit(ctx.atomic_formula_term());
    }

    @Override
    public Term visitGd_literal_term(PDDLParser.Gd_literal_termContext ctx) {
        return visit(ctx.literal_term());
    }

    @Override
    public Term visitGd_and(PDDLParser.Gd_andContext ctx) {
        AndTerm and_term = new AndTerm(term);
        term = and_term;
        ctx.gD().stream().map(gd -> visit(gd)).forEach(gd -> and_term.addTerm(gd));
        return and_term;
    }

    @Override
    public Term visitGd_or(PDDLParser.Gd_orContext ctx) {
        OrTerm or_term = new OrTerm(term);
        term = or_term;
        ctx.gD().stream().map(gd -> visit(gd)).forEach(gd -> or_term.addTerm(gd));
        return or_term;
    }

    @Override
    public Term visitGd_not(PDDLParser.Gd_notContext ctx) {
        return visit(ctx.gD()).negate(term);
    }

    @Override
    public Term visitGd_imply(PDDLParser.Gd_implyContext ctx) {
        OrTerm or_term = new OrTerm(term);
        term = or_term;
        or_term.addTerm(visit(ctx.gD(0)).negate(or_term));
        or_term.addTerm(visit(ctx.gD(1)));
        ctx.gD().stream().map(gd -> visit(gd)).forEach(gd -> or_term.addTerm(gd));
        return or_term;
    }

    @Override
    public Term visitGd_exists(PDDLParser.Gd_existsContext ctx) {
        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
        WALKER.walk(typedListVariable, ctx.typed_list_variable());
        ExistsTerm exists_term = new ExistsTerm(term);
        term = exists_term;
        typedListVariable.variables.stream().forEach(variable -> exists_term.addVariable(variable));
        exists_term.setFormula(visit(ctx.gD()));
        return exists_term;
    }

    @Override
    public Term visitGd_forall(PDDLParser.Gd_forallContext ctx) {
        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
        WALKER.walk(typedListVariable, ctx.typed_list_variable());
        ForAllTerm for_all_term = new ForAllTerm(term);
        term = for_all_term;
        typedListVariable.variables.stream().forEach(variable -> for_all_term.addVariable(variable));
        for_all_term.setFormula(visit(ctx.gD()));
        return for_all_term;
    }

    @Override
    public Term visitGd_f_comp(PDDLParser.Gd_f_compContext ctx) {
        return visit(ctx.f_comp());
    }

    @Override
    public Term visitF_comp(PDDLParser.F_compContext ctx) {
        ComparisonTerm comparison_term;
        switch (ctx.binary_comp().getText()) {
            case ">":
                comparison_term = new ComparisonTerm(term, ComparisonTerm.Comp.Gt);
                break;
            case "<":
                comparison_term = new ComparisonTerm(term, ComparisonTerm.Comp.Lt);
                break;
            case "=":
                comparison_term = new ComparisonTerm(term, ComparisonTerm.Comp.Eq);
                break;
            case ">=":
                comparison_term = new ComparisonTerm(term, ComparisonTerm.Comp.GEq);
                break;
            case "<=":
                comparison_term = new ComparisonTerm(term, ComparisonTerm.Comp.LEq);
                break;
            default:
                throw new AssertionError(ctx.binary_comp().getText());
        }
        term = comparison_term;
        comparison_term.setFirstTerm(visit(ctx.f_exp(0)));
        comparison_term.setSecondTerm(visit(ctx.f_exp(1)));
        return comparison_term;
    }

    @Override
    public Term visitLiteral_term_atomic_formula_term(PDDLParser.Literal_term_atomic_formula_termContext ctx) {
        return visit(ctx.atomic_formula_term());
    }

    @Override
    public Term visitLiteral_term_not_atomic_formula_term(PDDLParser.Literal_term_not_atomic_formula_termContext ctx) {
        return visit(ctx.atomic_formula_term()).negate(term);
    }

    @Override
    public Term visitLiteral_name_atomic_formula_name(PDDLParser.Literal_name_atomic_formula_nameContext ctx) {
        return visit(ctx.atomic_formula_name());
    }

    @Override
    public Term visitLiteral_name_not_atomic_formula_name(PDDLParser.Literal_name_not_atomic_formula_nameContext ctx) {
        return visit(ctx.atomic_formula_name()).negate(term);
    }

    @Override
    public Term visitAtomic_formula_term_predicate(PDDLParser.Atomic_formula_term_predicateContext ctx) {
        PredicateTerm predicate_term = new PredicateTerm(term, true);
        term = predicate_term;
        predicate_term.setPredicate(domain.getPredicate(Utils.capitalize(ctx.predicate().name().getText())));
        ctx.term().stream().map(t -> visit(t)).forEach(t -> predicate_term.addArgument(t));
        return predicate_term;
    }

    @Override
    public Term visitAtomic_formula_term_eq(PDDLParser.Atomic_formula_term_eqContext ctx) {
        EqTerm eq_term = new EqTerm(term, true);
        term = eq_term;
        eq_term.setFirstTerm(visit(ctx.term(0)));
        eq_term.setSecondTerm(visit(ctx.term(1)));
        return eq_term;
    }

    @Override
    public Term visitAtomic_formula_name_predicate(PDDLParser.Atomic_formula_name_predicateContext ctx) {
        PredicateTerm predicate_term = new PredicateTerm(term, true);
        term = predicate_term;
        predicate_term.setPredicate(domain.getPredicate(Utils.capitalize(ctx.predicate().name().getText())));
        ctx.name().stream().map(name -> new ConstantTerm(predicate_term, Utils.lowercase(name.getText()))).forEach(name -> predicate_term.addArgument(name));
        return predicate_term;
    }

    @Override
    public Term visitAtomic_formula_name_eq(PDDLParser.Atomic_formula_name_eqContext ctx) {
        EqTerm eq_term = new EqTerm(term, true);
        term = eq_term;
        eq_term.setFirstTerm(visit(ctx.name(0)));
        eq_term.setSecondTerm(visit(ctx.name(1)));
        return eq_term;
    }

    @Override
    public Term visitTerm_name(PDDLParser.Term_nameContext ctx) {
        return new ConstantTerm(term, Utils.lowercase(ctx.name().getText()));
    }

    @Override
    public Term visitTerm_variable(PDDLParser.Term_variableContext ctx) {
        return new VariableTerm(term, "?" + Utils.lowercase(ctx.variable().name().getText()));
    }

    @Override
    public Term visitTerm_function(PDDLParser.Term_functionContext ctx) {
        return visit(ctx.function_term());
    }

    @Override
    public Term visitFunction_term(PDDLParser.Function_termContext ctx) {
        FunctionTerm function_term = new FunctionTerm(term);
        term = function_term;
        function_term.setFunction(domain.getFunction(Utils.capitalize(ctx.function_symbol().name().getText())));
        ctx.term().stream().map(t -> visit(t)).forEach(t -> function_term.addArgument(t));
        return function_term;
    }

    @Override
    public Term visitF_exp_number(PDDLParser.F_exp_numberContext ctx) {
        return new NumberTerm(term, new BigDecimal(ctx.NUMBER().getText()));
    }

    @Override
    public Term visitF_exp_binary_op(PDDLParser.F_exp_binary_opContext ctx) {
        switch (ctx.binary_op().getText()) {
            case "-":
                OpTerm minus_op_term = new OpTerm(term, OpTerm.Op.Sub);
                term = minus_op_term;
                minus_op_term.addTerm(visit(ctx.f_exp(0)));
                minus_op_term.addTerm(visit(ctx.f_exp(1)));
                return minus_op_term;
            case "/":
                OpTerm div_op_term = new OpTerm(term, OpTerm.Op.Div);
                term = div_op_term;
                div_op_term.addTerm(visit(ctx.f_exp(0)));
                div_op_term.addTerm(visit(ctx.f_exp(1)));
                return div_op_term;
            case "+":
                OpTerm plus_op_term = new OpTerm(term, OpTerm.Op.Add);
                term = plus_op_term;
                ctx.f_exp().stream().map(f_exp -> visit(f_exp)).forEach(f_exp -> plus_op_term.addTerm(f_exp));
                return plus_op_term;
            case "*":
                OpTerm mul_op_term = new OpTerm(term, OpTerm.Op.Mul);
                term = mul_op_term;
                ctx.f_exp().stream().map(f_exp -> visit(f_exp)).forEach(f_exp -> mul_op_term.addTerm(f_exp));
                return mul_op_term;
            default:
                throw new AssertionError(ctx.binary_op().getText());
        }
    }

    @Override
    public Term visitF_exp_multi_op(PDDLParser.F_exp_multi_opContext ctx) {
        switch (ctx.multi_op().getText()) {
            case "+":
                OpTerm plus_op_term = new OpTerm(term, OpTerm.Op.Add);
                term = plus_op_term;
                ctx.f_exp().stream().map(f_exp -> visit(f_exp)).forEach(f_exp -> plus_op_term.addTerm(f_exp));
                return plus_op_term;
            case "*":
                OpTerm mul_op_term = new OpTerm(term, OpTerm.Op.Mul);
                term = mul_op_term;
                ctx.f_exp().stream().map(f_exp -> visit(f_exp)).forEach(f_exp -> mul_op_term.addTerm(f_exp));
                return mul_op_term;
            default:
                throw new AssertionError(ctx.multi_op().getText());
        }
    }

    @Override
    public Term visitF_exp_minus(PDDLParser.F_exp_minusContext ctx) {
        return visit(ctx.f_exp()).negate(term);
    }

    @Override
    public Term visitF_exp_f_head(PDDLParser.F_exp_f_headContext ctx) {
        return visit(ctx.f_head());
    }

    @Override
    public Term visitF_head_function_symbol_terms(PDDLParser.F_head_function_symbol_termsContext ctx) {
        FunctionTerm function_term = new FunctionTerm(term);
        term = function_term;
        function_term.setFunction(domain.getFunction(Utils.capitalize(ctx.function_symbol().name().getText())));
        ctx.term().stream().map(t -> visit(t)).forEach(t -> function_term.addArgument(t));
        return function_term;
    }

    @Override
    public Term visitF_head_function_symbol(PDDLParser.F_head_function_symbolContext ctx) {
        return new ConstantTerm(term, Utils.lowercase(ctx.function_symbol().name().getText()));
    }

    @Override
    public Term visitEffect_and_c_effect(PDDLParser.Effect_and_c_effectContext ctx) {
        AndTerm and_term = new AndTerm(term);
        term = and_term;
        ctx.c_effect().stream().map(c_effect -> visit(c_effect)).forEach(c_effect -> and_term.addTerm(c_effect));
        return and_term;
    }

    @Override
    public Term visitEffect_c_effect(PDDLParser.Effect_c_effectContext ctx) {
        return visit(ctx.c_effect());
    }

    @Override
    public Term visitC_effect_forall(PDDLParser.C_effect_forallContext ctx) {
        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
        WALKER.walk(typedListVariable, ctx.typed_list_variable());
        ForAllTerm for_all_term = new ForAllTerm(term);
        term = for_all_term;
        typedListVariable.variables.stream().forEach(variable -> for_all_term.addVariable(variable));
        for_all_term.setFormula(visit(ctx.effect()));
        return for_all_term;
    }

    @Override
    public Term visitC_effect_when(PDDLParser.C_effect_whenContext ctx) {
        WhenTerm when_term = new WhenTerm(term);
        term = when_term;
        when_term.setCondition(visit(ctx.gD()));
        when_term.setEffect(visit(ctx.cond_effect()));
        return when_term;
    }

    @Override
    public Term visitC_effect_p_effect(PDDLParser.C_effect_p_effectContext ctx) {
        return visit(ctx.p_effect());
    }

    @Override
    public Term visitP_effect_negated_atomic_formula_term(PDDLParser.P_effect_negated_atomic_formula_termContext ctx) {
        return visit(ctx.atomic_formula_term()).negate(term);
    }

    @Override
    public Term visitP_effect_directed_atomic_formula_term(PDDLParser.P_effect_directed_atomic_formula_termContext ctx) {
        return visit(ctx.atomic_formula_term());
    }

    @Override
    public Term visitP_effect_assign_op_f_head_f_exp(PDDLParser.P_effect_assign_op_f_head_f_expContext ctx) {
        AssignOpTerm assign_op_term;
        switch (ctx.assign_op().getText()) {
            case "assign":
                assign_op_term = new AssignOpTerm(term, AssignOpTerm.AssignOp.Assign);
                break;
            case "scale-up":
                assign_op_term = new AssignOpTerm(term, AssignOpTerm.AssignOp.ScaleUp);
                break;
            case "scale-down":
                assign_op_term = new AssignOpTerm(term, AssignOpTerm.AssignOp.ScaleDown);
                break;
            case "increase":
                assign_op_term = new AssignOpTerm(term, AssignOpTerm.AssignOp.Increase);
                break;
            case "decrease":
                assign_op_term = new AssignOpTerm(term, AssignOpTerm.AssignOp.Decrease);
                break;
            default:
                throw new AssertionError(ctx.assign_op().getText());
        }
        term = assign_op_term;
        assign_op_term.setFunctionTerm((FunctionTerm) visit(ctx.f_head()));
        assign_op_term.setValue(visit(ctx.f_exp()));
        return assign_op_term;
    }

    @Override
    public Term visitP_effect_assign_term(PDDLParser.P_effect_assign_termContext ctx) {
        AssignOpTerm assign_op_term = new AssignOpTerm(term, AssignOpTerm.AssignOp.Assign);
        term = assign_op_term;
        assign_op_term.setFunctionTerm((FunctionTerm) visit(ctx.function_term()));
        assign_op_term.setValue(visit(ctx.term()));
        return assign_op_term;
    }

    @Override
    public Term visitP_effect_assign_undefined(PDDLParser.P_effect_assign_undefinedContext ctx) {
        AssignOpTerm assign_op_term = new AssignOpTerm(term, AssignOpTerm.AssignOp.Assign);
        term = assign_op_term;
        assign_op_term.setFunctionTerm((FunctionTerm) visit(ctx.function_term()));
        assign_op_term.setValue(null);
        return assign_op_term;
    }

    @Override
    public Term visitCond_effect_and_p_effect(PDDLParser.Cond_effect_and_p_effectContext ctx) {
        AndTerm and_term = new AndTerm(term);
        term = and_term;
        ctx.p_effect().stream().map(p_effect -> visit(p_effect)).forEach(p_effect -> and_term.addTerm(p_effect));
        return and_term;
    }

    @Override
    public Term visitCond_effect_p_effect(PDDLParser.Cond_effect_p_effectContext ctx) {
        return visit(ctx.p_effect());
    }

    @Override
    public Term visitDa_GD_pref_timed_GD(PDDLParser.Da_GD_pref_timed_GDContext ctx) {
        return visit(ctx.pref_timed_GD());
    }

    @Override
    public Term visitDa_GD_and(PDDLParser.Da_GD_andContext ctx) {
        AndTerm and_term = new AndTerm(term);
        term = and_term;
        ctx.da_GD().stream().map(da_GD -> visit(da_GD)).forEach(da_GD -> and_term.addTerm(da_GD));
        return and_term;
    }

    @Override
    public Term visitDa_GD_forall(PDDLParser.Da_GD_forallContext ctx) {
        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
        WALKER.walk(typedListVariable, ctx.typed_list_variable());
        ForAllTerm for_all_term = new ForAllTerm(term);
        term = for_all_term;
        typedListVariable.variables.stream().forEach(variable -> for_all_term.addVariable(variable));
        for_all_term.setFormula(visit(ctx.da_GD()));
        return for_all_term;
    }

    @Override
    public Term visitPref_timed_GD_timed_GD(PDDLParser.Pref_timed_GD_timed_GDContext ctx) {
        return visit(ctx.timed_GD());
    }

    @Override
    public Term visitPref_timed_GD_preference_timed_GD(PDDLParser.Pref_timed_GD_preference_timed_GDContext ctx) {
        throw new UnsupportedOperationException("Not supported yet: " + ctx.toStringTree(parser));
    }

    @Override
    public Term visitTimed_GD_at_GD(PDDLParser.Timed_GD_at_GDContext ctx) {
        switch (ctx.time_specifier().getText()) {
            case "start":
                AtStartTerm at_start_term = new AtStartTerm(term);
                term = at_start_term;
                at_start_term.setTerm(visit(ctx.gD()));
                return at_start_term;
            case "end":
                AtEndTerm at_end_term = new AtEndTerm(term);
                term = at_end_term;
                at_end_term.setTerm(visit(ctx.gD()));
                return at_end_term;
            default:
                throw new AssertionError(ctx.time_specifier().getText());
        }
    }

    @Override
    public Term visitTimed_GD_over_GD(PDDLParser.Timed_GD_over_GDContext ctx) {
        return new OverAllTerm(visit(ctx.gD()));
    }

    @Override
    public Term visitDuration_constraint_and(PDDLParser.Duration_constraint_andContext ctx) {
        AndTerm and_term = new AndTerm(term);
        term = and_term;
        ctx.simple_duration_constraint().stream().map(simple_duration_constraint -> visit(simple_duration_constraint)).forEach(simple_duration_constraint -> and_term.addTerm(simple_duration_constraint));
        return and_term;
    }

    @Override
    public Term visitDuration_constraint_empty(PDDLParser.Duration_constraint_emptyContext ctx) {
        return new AndTerm(term);
    }

    @Override
    public Term visitDuration_constraint_duration_constraint(PDDLParser.Duration_constraint_duration_constraintContext ctx) {
        return visit(ctx.simple_duration_constraint());
    }

    @Override
    public Term visitSimple_duration_constraint_d_op(PDDLParser.Simple_duration_constraint_d_opContext ctx) {
        ComparisonTerm comparison_term;
        switch (ctx.d_op().getText()) {
            case "<=":
                comparison_term = new ComparisonTerm(term, ComparisonTerm.Comp.LEq);
                break;
            case ">=":
                comparison_term = new ComparisonTerm(term, ComparisonTerm.Comp.GEq);
                break;
            case "=":
                comparison_term = new ComparisonTerm(term, ComparisonTerm.Comp.Eq);
                break;
            default:
                throw new AssertionError(ctx.d_op().getText());
        }
        comparison_term.setFirstTerm(new VariableTerm(comparison_term, "?duration"));
        comparison_term.setSecondTerm(visit(ctx.d_value()));
        return comparison_term;
    }

    @Override
    public Term visitSimple_duration_constraint_at(PDDLParser.Simple_duration_constraint_atContext ctx) {
        return visit(ctx.simple_duration_constraint());
    }

    @Override
    public Term visitD_value_number(PDDLParser.D_value_numberContext ctx) {
        return new NumberTerm(term, new BigDecimal(ctx.NUMBER().getText()));
    }

    @Override
    public Term visitD_value_f_exp(PDDLParser.D_value_f_expContext ctx) {
        return visit(ctx.f_exp());
    }

    @Override
    public Term visitDa_effect_and(PDDLParser.Da_effect_andContext ctx) {
        AndTerm and_term = new AndTerm(term);
        term = and_term;
        ctx.da_effect().stream().map(da_effect -> visit(da_effect)).forEach(da_effect -> and_term.addTerm(da_effect));
        return and_term;
    }

    @Override
    public Term visitDa_effect_timed_effect(PDDLParser.Da_effect_timed_effectContext ctx) {
        return visit(ctx.timed_effect());
    }

    @Override
    public Term visitDa_effect_forall(PDDLParser.Da_effect_forallContext ctx) {
        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
        WALKER.walk(typedListVariable, ctx.typed_list_variable());
        ForAllTerm for_all_term = new ForAllTerm(term);
        term = for_all_term;
        typedListVariable.variables.stream().forEach(variable -> for_all_term.addVariable(variable));
        for_all_term.setFormula(visit(ctx.da_effect()));
        return for_all_term;
    }

    @Override
    public Term visitDa_effect_when(PDDLParser.Da_effect_whenContext ctx) {
        WhenTerm when_term = new WhenTerm(term);
        term = when_term;
        when_term.setCondition(visit(ctx.da_GD()));
        when_term.setEffect(visit(ctx.timed_effect()));
        return when_term;
    }

    @Override
    public Term visitTimed_effect_cond_effect(PDDLParser.Timed_effect_cond_effectContext ctx) {
        switch (ctx.time_specifier().getText()) {
            case "start":
                AtStartTerm at_start_term = new AtStartTerm(term);
                term = at_start_term;
                at_start_term.setTerm(visit(ctx.cond_effect()));
                return at_start_term;
            case "end":
                AtEndTerm at_end_term = new AtEndTerm(term);
                term = at_end_term;
                at_end_term.setTerm(visit(ctx.cond_effect()));
                return at_end_term;
            default:
                throw new AssertionError(ctx.time_specifier().getText());
        }
    }

    @Override
    public Term visitTimed_effect_f_assign_da(PDDLParser.Timed_effect_f_assign_daContext ctx) {
        switch (ctx.time_specifier().getText()) {
            case "start":
                AtStartTerm at_start_term = new AtStartTerm(term);
                term = at_start_term;
                at_start_term.setTerm(visit(ctx.f_assign_da()));
                return at_start_term;
            case "end":
                AtEndTerm at_end_term = new AtEndTerm(term);
                term = at_end_term;
                at_end_term.setTerm(visit(ctx.f_assign_da()));
                return at_end_term;
            default:
                throw new AssertionError(ctx.time_specifier().getText());
        }
    }

    @Override
    public Term visitTimed_effect_assign_op(PDDLParser.Timed_effect_assign_opContext ctx) {
        throw new UnsupportedOperationException("Not supported yet: " + ctx.toStringTree(parser));
    }

    @Override
    public Term visitF_assign_da(PDDLParser.F_assign_daContext ctx) {
        AssignOpTerm assign_op_term;
        switch (ctx.assign_op().getText()) {
            case "assign":
                assign_op_term = new AssignOpTerm(term, AssignOpTerm.AssignOp.Assign);
                break;
            case "scale-up":
                assign_op_term = new AssignOpTerm(term, AssignOpTerm.AssignOp.ScaleUp);
                break;
            case "scale-down":
                assign_op_term = new AssignOpTerm(term, AssignOpTerm.AssignOp.ScaleDown);
                break;
            case "increase":
                assign_op_term = new AssignOpTerm(term, AssignOpTerm.AssignOp.Increase);
                break;
            case "decrease":
                assign_op_term = new AssignOpTerm(term, AssignOpTerm.AssignOp.Decrease);
                break;
            default:
                throw new AssertionError(ctx.assign_op().getText());
        }
        term = assign_op_term;
        assign_op_term.setFunctionTerm((FunctionTerm) visit(ctx.f_head()));
        assign_op_term.setValue(visit(ctx.f_exp_da()));
        return assign_op_term;
    }

    @Override
    public Term visitF_exp_da_binary(PDDLParser.F_exp_da_binaryContext ctx) {
        throw new UnsupportedOperationException("Not supported yet: " + ctx.toStringTree(parser));
    }

    @Override
    public Term visitF_exp_da_multi(PDDLParser.F_exp_da_multiContext ctx) {
        throw new UnsupportedOperationException("Not supported yet: " + ctx.toStringTree(parser));
    }

    @Override
    public Term visitF_exp_da_minus(PDDLParser.F_exp_da_minusContext ctx) {
        throw new UnsupportedOperationException("Not supported yet: " + ctx.toStringTree(parser));
    }

    @Override
    public Term visitF_exp_da_duration(PDDLParser.F_exp_da_durationContext ctx) {
        return new VariableTerm(term, "?duration");
    }

    @Override
    public Term visitF_exp_da_f_exp(PDDLParser.F_exp_da_f_expContext ctx) {
        throw new UnsupportedOperationException("Not supported yet: " + ctx.toStringTree(parser));
    }

    @Override
    public Term visitF_exp_t(PDDLParser.F_exp_tContext ctx) {
        throw new UnsupportedOperationException("Not supported yet: " + ctx.toStringTree(parser));
    }

    @Override
    public Term visitInit(PDDLParser.InitContext ctx) {
        AndTerm and_term = new AndTerm(term);
        term = and_term;
        ctx.init_el().stream().map(init_el -> visit(init_el)).forEach(init_el -> and_term.addTerm(init_el));
        return and_term;
    }

    @Override
    public Term visitInit_el_literal_name(PDDLParser.Init_el_literal_nameContext ctx) {
        return visit(ctx.literal_name());
    }

    @Override
    public Term visitInit_el_at(PDDLParser.Init_el_atContext ctx) {
        AtTerm at_term = new AtTerm(term, new BigDecimal(ctx.NUMBER().getText()));
        term = at_term;
        at_term.setPredicateTerm((PredicateTerm) visit(ctx.literal_name()));
        return at_term;
    }

    @Override
    public Term visitInit_el_eq_number(PDDLParser.Init_el_eq_numberContext ctx) {
        EqTerm eq_term = new EqTerm(term, true);
        term = eq_term;
        eq_term.setFirstTerm(visit(ctx.basic_function_term()));
        eq_term.setSecondTerm(new NumberTerm(term, new BigDecimal(ctx.NUMBER().getText())));
        return eq_term;
    }

    @Override
    public Term visitInit_el_eq_name(PDDLParser.Init_el_eq_nameContext ctx) {
        EqTerm eq_term = new EqTerm(term, true);
        term = eq_term;
        eq_term.setFirstTerm(visit(ctx.basic_function_term()));
        eq_term.setSecondTerm(new ConstantTerm(term, Utils.lowercase(ctx.name().getText())));
        return eq_term;
    }

    @Override
    public Term visitBasic_function_term_function_symbol(PDDLParser.Basic_function_term_function_symbolContext ctx) {
        return new ConstantTerm(term, Utils.lowercase(ctx.function_symbol().name().getText()));
    }

    @Override
    public Term visitBasic_function_term_function_symbol_names(PDDLParser.Basic_function_term_function_symbol_namesContext ctx) {
        FunctionTerm function_term = new FunctionTerm(term);
        term = function_term;
        function_term.setFunction(domain.getFunction(Utils.capitalize(ctx.function_symbol().name().getText())));
        ctx.name().stream().map(name -> new ConstantTerm(function_term, Utils.lowercase(name.getText()))).forEach(name -> function_term.addArgument(name));
        return function_term;
    }
}
