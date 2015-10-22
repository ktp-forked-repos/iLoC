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
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
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
        return new AndTerm(ctx.pre_GD().stream().map(pre_GD -> visit(pre_GD)).collect(Collectors.toList()));
    }

    @Override
    public Term visitPre_GD_forall(PDDLParser.Pre_GD_forallContext ctx) {
        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
        WALKER.walk(typedListVariable, ctx.typed_list_variable());
        return new ForAllTerm(typedListVariable.variables, visit(ctx.pre_GD()));
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
        return new AndTerm(ctx.gD().stream().map(gd -> visit(gd)).collect(Collectors.toList()));
    }

    @Override
    public Term visitGd_or(PDDLParser.Gd_orContext ctx) {
        return new OrTerm(ctx.gD().stream().map(gd -> visit(gd)).collect(Collectors.toList()));
    }

    @Override
    public Term visitGd_not(PDDLParser.Gd_notContext ctx) {
        return visit(ctx.gD()).negate();
    }

    @Override
    public Term visitGd_imply(PDDLParser.Gd_implyContext ctx) {
        return new OrTerm(Arrays.asList(visit(ctx.gD(0)).negate(), visit(ctx.gD(1))));
    }

    @Override
    public Term visitGd_exists(PDDLParser.Gd_existsContext ctx) {
        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
        WALKER.walk(typedListVariable, ctx.typed_list_variable());
        return new ExistsTerm(typedListVariable.variables, visit(ctx.gD()));
    }

    @Override
    public Term visitGd_forall(PDDLParser.Gd_forallContext ctx) {
        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
        WALKER.walk(typedListVariable, ctx.typed_list_variable());
        return new ForAllTerm(typedListVariable.variables, visit(ctx.gD()));
    }

    @Override
    public Term visitGd_f_comp(PDDLParser.Gd_f_compContext ctx) {
        return visit(ctx.f_comp());
    }

    @Override
    public Term visitF_comp(PDDLParser.F_compContext ctx) {
        switch (ctx.binary_comp().getText()) {
            case ">":
                return new ComparisonTerm(ComparisonTerm.Comp.Gt, visit(ctx.f_exp(0)), visit(ctx.f_exp(1)));
            case "<":
                return new ComparisonTerm(ComparisonTerm.Comp.Lt, visit(ctx.f_exp(0)), visit(ctx.f_exp(1)));
            case "=":
                return new ComparisonTerm(ComparisonTerm.Comp.Eq, visit(ctx.f_exp(0)), visit(ctx.f_exp(1)));
            case ">=":
                return new ComparisonTerm(ComparisonTerm.Comp.GEq, visit(ctx.f_exp(0)), visit(ctx.f_exp(1)));
            case "<=":
                return new ComparisonTerm(ComparisonTerm.Comp.LEq, visit(ctx.f_exp(0)), visit(ctx.f_exp(1)));
            default:
                throw new AssertionError(ctx.binary_comp().getText());
        }
    }

    @Override
    public Term visitLiteral_term_atomic_formula_term(PDDLParser.Literal_term_atomic_formula_termContext ctx) {
        return visit(ctx.atomic_formula_term());
    }

    @Override
    public Term visitLiteral_term_not_atomic_formula_term(PDDLParser.Literal_term_not_atomic_formula_termContext ctx) {
        return visit(ctx.atomic_formula_term()).negate();
    }

    @Override
    public Term visitLiteral_name_atomic_formula_name(PDDLParser.Literal_name_atomic_formula_nameContext ctx) {
        return visit(ctx.atomic_formula_name());
    }

    @Override
    public Term visitLiteral_name_not_atomic_formula_name(PDDLParser.Literal_name_not_atomic_formula_nameContext ctx) {
        return visit(ctx.atomic_formula_name()).negate();
    }

    @Override
    public Term visitAtomic_formula_term_predicate(PDDLParser.Atomic_formula_term_predicateContext ctx) {
        return new PredicateTerm(true, domain.getPredicate(Utils.capitalize(ctx.predicate().name().getText())), ctx.term().stream().map(term -> visit(term)).collect(Collectors.toList()));
    }

    @Override
    public Term visitAtomic_formula_term_eq(PDDLParser.Atomic_formula_term_eqContext ctx) {
        return new EqTerm(true, visit(ctx.term(0)), visit(ctx.term(1)));
    }

    @Override
    public Term visitAtomic_formula_name_predicate(PDDLParser.Atomic_formula_name_predicateContext ctx) {
        return new PredicateTerm(true, domain.getPredicate(Utils.capitalize(ctx.predicate().name().getText())), ctx.name().stream().map(name -> new ConstantTerm(Utils.lowercase(name.getText()))).collect(Collectors.toList()));
    }

    @Override
    public Term visitAtomic_formula_name_eq(PDDLParser.Atomic_formula_name_eqContext ctx) {
        return new EqTerm(true, visit(ctx.name(0)), visit(ctx.name(1)));
    }

    @Override
    public Term visitTerm_name(PDDLParser.Term_nameContext ctx) {
        return new ConstantTerm(Utils.lowercase(ctx.name().getText()));
    }

    @Override
    public Term visitTerm_variable(PDDLParser.Term_variableContext ctx) {
        return new VariableTerm("?" + Utils.lowercase(ctx.variable().name().getText()));
    }

    @Override
    public Term visitTerm_function(PDDLParser.Term_functionContext ctx) {
        return visit(ctx.function_term());
    }

    @Override
    public Term visitFunction_term(PDDLParser.Function_termContext ctx) {
        return new FunctionTerm(domain.getFunction(Utils.capitalize(ctx.function_symbol().name().getText())), ctx.term().stream().map(term -> visit(term)).collect(Collectors.toList()));
    }

    @Override
    public Term visitF_exp_number(PDDLParser.F_exp_numberContext ctx) {
        return new NumberTerm(new BigDecimal(ctx.NUMBER().getText()));
    }

    @Override
    public Term visitF_exp_binary_op(PDDLParser.F_exp_binary_opContext ctx) {
        switch (ctx.binary_op().getText()) {
            case "-":
                return new OpTerm(OpTerm.Op.Sub, Arrays.asList(visit(ctx.f_exp(0)), visit(ctx.f_exp(1))));
            case "/":
                return new OpTerm(OpTerm.Op.Div, Arrays.asList(visit(ctx.f_exp(0)), visit(ctx.f_exp(1))));
            case "+":
                return new OpTerm(OpTerm.Op.Sub, ctx.f_exp().stream().map(f_exp -> visit(f_exp)).collect(Collectors.toList()));
            case "*":
                return new OpTerm(OpTerm.Op.Div, ctx.f_exp().stream().map(f_exp -> visit(f_exp)).collect(Collectors.toList()));
            default:
                throw new AssertionError(ctx.binary_op().getText());
        }
    }

    @Override
    public Term visitF_exp_multi_op(PDDLParser.F_exp_multi_opContext ctx) {
        switch (ctx.multi_op().getText()) {
            case "+":
                return new OpTerm(OpTerm.Op.Sub, ctx.f_exp().stream().map(f_exp -> visit(f_exp)).collect(Collectors.toList()));
            case "*":
                return new OpTerm(OpTerm.Op.Div, ctx.f_exp().stream().map(f_exp -> visit(f_exp)).collect(Collectors.toList()));
            default:
                throw new AssertionError(ctx.multi_op().getText());
        }
    }

    @Override
    public Term visitF_exp_minus(PDDLParser.F_exp_minusContext ctx) {
        return visit(ctx.f_exp()).negate();
    }

    @Override
    public Term visitF_exp_f_head(PDDLParser.F_exp_f_headContext ctx) {
        return visit(ctx.f_head());
    }

    @Override
    public Term visitF_head_function_symbol_terms(PDDLParser.F_head_function_symbol_termsContext ctx) {
        return new FunctionTerm(domain.getFunction(Utils.capitalize(ctx.function_symbol().name().getText())), ctx.term().stream().map(term -> visit(term)).collect(Collectors.toList()));
    }

    @Override
    public Term visitF_head_function_symbol(PDDLParser.F_head_function_symbolContext ctx) {
        return new ConstantTerm(Utils.lowercase(ctx.function_symbol().name().getText()));
    }

    @Override
    public Term visitEffect_and_c_effect(PDDLParser.Effect_and_c_effectContext ctx) {
        return new AndTerm(ctx.c_effect().stream().map(c_effect -> visit(c_effect)).collect(Collectors.toList()));
    }

    @Override
    public Term visitEffect_c_effect(PDDLParser.Effect_c_effectContext ctx) {
        return visit(ctx.c_effect());
    }

    @Override
    public Term visitC_effect_forall(PDDLParser.C_effect_forallContext ctx) {
        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
        WALKER.walk(typedListVariable, ctx.typed_list_variable());
        return new ForAllTerm(typedListVariable.variables, visit(ctx.effect()));
    }

    @Override
    public Term visitC_effect_when(PDDLParser.C_effect_whenContext ctx) {
        return new WhenTerm(visit(ctx.gD()), visit(ctx.cond_effect()));
    }

    @Override
    public Term visitC_effect_p_effect(PDDLParser.C_effect_p_effectContext ctx) {
        return visit(ctx.p_effect());
    }

    @Override
    public Term visitP_effect_negated_atomic_formula_term(PDDLParser.P_effect_negated_atomic_formula_termContext ctx) {
        return visit(ctx.atomic_formula_term()).negate();
    }

    @Override
    public Term visitP_effect_directed_atomic_formula_term(PDDLParser.P_effect_directed_atomic_formula_termContext ctx) {
        return visit(ctx.atomic_formula_term());
    }

    @Override
    public Term visitP_effect_assign_op_f_head_f_exp(PDDLParser.P_effect_assign_op_f_head_f_expContext ctx) {
        switch (ctx.assign_op().getText()) {
            case "assign":
                return new AssignOpTerm(AssignOpTerm.AssignOp.Assign, (FunctionTerm) visit(ctx.f_head()), visit(ctx.f_exp()));
            case "scale-up":
                return new AssignOpTerm(AssignOpTerm.AssignOp.ScaleUp, (FunctionTerm) visit(ctx.f_head()), visit(ctx.f_exp()));
            case "scale-down":
                return new AssignOpTerm(AssignOpTerm.AssignOp.ScaleDown, (FunctionTerm) visit(ctx.f_head()), visit(ctx.f_exp()));
            case "increase":
                return new AssignOpTerm(AssignOpTerm.AssignOp.Increase, (FunctionTerm) visit(ctx.f_head()), visit(ctx.f_exp()));
            case "decrease":
                return new AssignOpTerm(AssignOpTerm.AssignOp.Decrease, (FunctionTerm) visit(ctx.f_head()), visit(ctx.f_exp()));
            default:
                throw new AssertionError(ctx.assign_op().getText());
        }
    }

    @Override
    public Term visitP_effect_assign_term(PDDLParser.P_effect_assign_termContext ctx) {
        return new AssignOpTerm(AssignOpTerm.AssignOp.Assign, (FunctionTerm) visit(ctx.function_term()), visit(ctx.term()));
    }

    @Override
    public Term visitP_effect_assign_undefined(PDDLParser.P_effect_assign_undefinedContext ctx) {
        return new AssignOpTerm(AssignOpTerm.AssignOp.Assign, (FunctionTerm) visit(ctx.function_term()), null);
    }

    @Override
    public Term visitCond_effect_and_p_effect(PDDLParser.Cond_effect_and_p_effectContext ctx) {
        return new AndTerm(ctx.p_effect().stream().map(p_effect -> visit(p_effect)).collect(Collectors.toList()));
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
        return new AndTerm(ctx.da_GD().stream().map(da_GD -> visit(da_GD)).collect(Collectors.toList()));
    }

    @Override
    public Term visitDa_GD_forall(PDDLParser.Da_GD_forallContext ctx) {
        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
        WALKER.walk(typedListVariable, ctx.typed_list_variable());
        return new ForAllTerm(typedListVariable.variables, visit(ctx.da_GD()));
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
                return new AtStartTerm(visit(ctx.gD()));
            case "end":
                return new AtEndTerm(visit(ctx.gD()));
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
        return new AndTerm(ctx.simple_duration_constraint().stream().map(simple_duration_constraint -> visit(simple_duration_constraint)).collect(Collectors.toList()));
    }

    @Override
    public Term visitDuration_constraint_empty(PDDLParser.Duration_constraint_emptyContext ctx) {
        return new AndTerm(Collections.emptyList());
    }

    @Override
    public Term visitDuration_constraint_duration_constraint(PDDLParser.Duration_constraint_duration_constraintContext ctx) {
        return visit(ctx.simple_duration_constraint());
    }

    @Override
    public Term visitSimple_duration_constraint_d_op(PDDLParser.Simple_duration_constraint_d_opContext ctx) {
        switch (ctx.d_op().getText()) {
            case "<=":
                return new ComparisonTerm(ComparisonTerm.Comp.LEq, new VariableTerm("?duration"), visit(ctx.d_value()));
            case ">=":
                return new ComparisonTerm(ComparisonTerm.Comp.GEq, new VariableTerm("?duration"), visit(ctx.d_value()));
            case "=":
                return new ComparisonTerm(ComparisonTerm.Comp.Eq, new VariableTerm("?duration"), visit(ctx.d_value()));
            default:
                throw new AssertionError(ctx.d_op().getText());
        }
    }

    @Override
    public Term visitSimple_duration_constraint_at(PDDLParser.Simple_duration_constraint_atContext ctx) {
        return visit(ctx.simple_duration_constraint());
    }

    @Override
    public Term visitD_value_number(PDDLParser.D_value_numberContext ctx) {
        return new NumberTerm(new BigDecimal(ctx.NUMBER().getText()));
    }

    @Override
    public Term visitD_value_f_exp(PDDLParser.D_value_f_expContext ctx) {
        return visit(ctx.f_exp());
    }

    @Override
    public Term visitDa_effect_and(PDDLParser.Da_effect_andContext ctx) {
        return new AndTerm(ctx.da_effect().stream().map(da_effect -> visit(da_effect)).collect(Collectors.toList()));
    }

    @Override
    public Term visitDa_effect_timed_effect(PDDLParser.Da_effect_timed_effectContext ctx) {
        return visit(ctx.timed_effect());
    }

    @Override
    public Term visitDa_effect_forall(PDDLParser.Da_effect_forallContext ctx) {
        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
        WALKER.walk(typedListVariable, ctx.typed_list_variable());
        return new ForAllTerm(typedListVariable.variables, visit(ctx.da_effect()));
    }

    @Override
    public Term visitDa_effect_when(PDDLParser.Da_effect_whenContext ctx) {
        return new WhenTerm(visit(ctx.da_GD()), visit(ctx.timed_effect()));
    }

    @Override
    public Term visitTimed_effect_cond_effect(PDDLParser.Timed_effect_cond_effectContext ctx) {
        switch (ctx.time_specifier().getText()) {
            case "start":
                return new AtStartTerm(visit(ctx.cond_effect()));
            case "end":
                return new AtEndTerm(visit(ctx.cond_effect()));
            default:
                throw new AssertionError(ctx.time_specifier().getText());
        }
    }

    @Override
    public Term visitTimed_effect_f_assign_da(PDDLParser.Timed_effect_f_assign_daContext ctx) {
        switch (ctx.time_specifier().getText()) {
            case "start":
                return new AtStartTerm(visit(ctx.f_assign_da()));
            case "end":
                return new AtEndTerm(visit(ctx.f_assign_da()));
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
        switch (ctx.assign_op().getText()) {
            case "assign":
                return new AssignOpTerm(AssignOpTerm.AssignOp.Assign, (FunctionTerm) visit(ctx.f_head()), visit(ctx.f_exp_da()));
            case "scale-up":
                return new AssignOpTerm(AssignOpTerm.AssignOp.ScaleUp, (FunctionTerm) visit(ctx.f_head()), visit(ctx.f_exp_da()));
            case "scale-down":
                return new AssignOpTerm(AssignOpTerm.AssignOp.ScaleDown, (FunctionTerm) visit(ctx.f_head()), visit(ctx.f_exp_da()));
            case "increase":
                return new AssignOpTerm(AssignOpTerm.AssignOp.Increase, (FunctionTerm) visit(ctx.f_head()), visit(ctx.f_exp_da()));
            case "decrease":
                return new AssignOpTerm(AssignOpTerm.AssignOp.Decrease, (FunctionTerm) visit(ctx.f_head()), visit(ctx.f_exp_da()));
            default:
                throw new AssertionError(ctx.assign_op().getText());
        }
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
        return new VariableTerm("?duration");
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
        return new AndTerm(ctx.init_el().stream().map(init_el -> visit(init_el)).collect(Collectors.toList()));
    }

    @Override
    public Term visitInit_el_literal_name(PDDLParser.Init_el_literal_nameContext ctx) {
        return visit(ctx.literal_name());
    }

    @Override
    public Term visitInit_el_at(PDDLParser.Init_el_atContext ctx) {
        return new AtTerm(new BigDecimal(ctx.NUMBER().getText()), (PredicateTerm) visit(ctx.literal_name()));
    }

    @Override
    public Term visitInit_el_eq_number(PDDLParser.Init_el_eq_numberContext ctx) {
        return new EqTerm(true, visit(ctx.basic_function_term()), new NumberTerm(new BigDecimal(ctx.NUMBER().getText())));
    }

    @Override
    public Term visitInit_el_eq_name(PDDLParser.Init_el_eq_nameContext ctx) {
        return new EqTerm(true, visit(ctx.basic_function_term()), new ConstantTerm(Utils.lowercase(ctx.name().getText())));
    }

    @Override
    public Term visitBasic_function_term_function_symbol(PDDLParser.Basic_function_term_function_symbolContext ctx) {
        return new ConstantTerm(Utils.lowercase(ctx.function_symbol().name().getText()));
    }

    @Override
    public Term visitBasic_function_term_function_symbol_names(PDDLParser.Basic_function_term_function_symbol_namesContext ctx) {
        return new FunctionTerm(domain.getFunction(Utils.capitalize(ctx.function_symbol().name().getText())), ctx.name().stream().map(name -> new ConstantTerm(Utils.lowercase(name.getText()))).collect(Collectors.toList()));
    }
}
