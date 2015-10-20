// Generated from C:\Users\pst\Documents\NetBeansProjects\lab\iLoC\src\it\cnr\istc\iloc\ddl\DDL.g4 by ANTLR 4.5.1
package it.cnr.istc.iloc.ddl;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DDLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
interface DDLVisitor<T> extends ParseTreeVisitor<T> {

    /**
     * Visit a parse tree produced by {@link DDLParser#compilation_unit}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCompilation_unit(DDLParser.Compilation_unitContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#type_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitType_declaration(DDLParser.Type_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#typedef_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTypedef_declaration(DDLParser.Typedef_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#enum_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEnum_declaration(DDLParser.Enum_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#enum_constants}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEnum_constants(DDLParser.Enum_constantsContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#class_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitClass_declaration(DDLParser.Class_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#member}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMember(DDLParser.MemberContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#field_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitField_declaration(DDLParser.Field_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#variable_dec}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitVariable_dec(DDLParser.Variable_decContext ctx);

    /**
     * Visit a parse tree produced by the {@code void_method_declaration}
     * labeled alternative in {@link DDLParser#method_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitVoid_method_declaration(DDLParser.Void_method_declarationContext ctx);

    /**
     * Visit a parse tree produced by the {@code type_method_declaration}
     * labeled alternative in {@link DDLParser#method_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitType_method_declaration(DDLParser.Type_method_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#constructor_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstructor_declaration(DDLParser.Constructor_declarationContext ctx);

    /**
     * Visit a parse tree produced by the {@code this_constructor_invocation}
     * labeled alternative in {@link DDLParser#explicit_constructor_invocation}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitThis_constructor_invocation(DDLParser.This_constructor_invocationContext ctx);

    /**
     * Visit a parse tree produced by the {@code super_constructor_invocation}
     * labeled alternative in {@link DDLParser#explicit_constructor_invocation}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSuper_constructor_invocation(DDLParser.Super_constructor_invocationContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#predicate_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPredicate_declaration(DDLParser.Predicate_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStatement(DDLParser.StatementContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#block}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBlock(DDLParser.BlockContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#assignment_statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAssignment_statement(DDLParser.Assignment_statementContext ctx);

    /**
     * Visit a parse tree produced by
     * {@link DDLParser#local_variable_statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLocal_variable_statement(DDLParser.Local_variable_statementContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#expression_statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExpression_statement(DDLParser.Expression_statementContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#preference_statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPreference_statement(DDLParser.Preference_statementContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#disjunction_statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDisjunction_statement(DDLParser.Disjunction_statementContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#disjunct}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDisjunct(DDLParser.DisjunctContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#formula_statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFormula_statement(DDLParser.Formula_statementContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#return_statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitReturn_statement(DDLParser.Return_statementContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#assignment_list}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAssignment_list(DDLParser.Assignment_listContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#assignment}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAssignment(DDLParser.AssignmentContext ctx);

    /**
     * Visit a parse tree produced by the {@code cast_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCast_expression(DDLParser.Cast_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code qualified_id_expression}
     * labeled alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitQualified_id_expression(DDLParser.Qualified_id_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code division_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDivision_expression(DDLParser.Division_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code subtraction_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSubtraction_expression(DDLParser.Subtraction_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code plus_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPlus_expression(DDLParser.Plus_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code function_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunction_expression(DDLParser.Function_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code addition_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAddition_expression(DDLParser.Addition_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code minus_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMinus_expression(DDLParser.Minus_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code parentheses_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitParentheses_expression(DDLParser.Parentheses_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code implication_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitImplication_expression(DDLParser.Implication_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code lt_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLt_expression(DDLParser.Lt_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code not_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNot_expression(DDLParser.Not_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code conjunction_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConjunction_expression(DDLParser.Conjunction_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code geq_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitGeq_expression(DDLParser.Geq_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code range_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitRange_expression(DDLParser.Range_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code multiplication_expression}
     * labeled alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMultiplication_expression(DDLParser.Multiplication_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code leq_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLeq_expression(DDLParser.Leq_expressionContext ctx);

    /**
     * Visit a parse tree produced by the
     * {@code exclusive_disjunction_expression} labeled alternative in
     * {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExclusive_disjunction_expression(DDLParser.Exclusive_disjunction_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code gt_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitGt_expression(DDLParser.Gt_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code constructor_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstructor_expression(DDLParser.Constructor_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code disjunction_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDisjunction_expression(DDLParser.Disjunction_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code literal_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLiteral_expression(DDLParser.Literal_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code eq_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEq_expression(DDLParser.Eq_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code neq_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNeq_expression(DDLParser.Neq_expressionContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#expr_list}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExpr_list(DDLParser.Expr_listContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#literal}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLiteral(DDLParser.LiteralContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#qualified_id}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitQualified_id(DDLParser.Qualified_idContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#type}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitType(DDLParser.TypeContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#class_type}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitClass_type(DDLParser.Class_typeContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#primitive_type}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPrimitive_type(DDLParser.Primitive_typeContext ctx);

    /**
     * Visit a parse tree produced by {@link DDLParser#typed_list}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTyped_list(DDLParser.Typed_listContext ctx);
}
