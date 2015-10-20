// Generated from C:\Users\pst\Documents\NetBeansProjects\lab\iLoC\src\it\cnr\istc\iloc\ddl\DDL.g4 by ANTLR 4.5.1
package it.cnr.istc.iloc.ddl;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DDLParser}.
 */
interface DDLListener extends ParseTreeListener {

    /**
     * Enter a parse tree produced by {@link DDLParser#compilation_unit}.
     *
     * @param ctx the parse tree
     */
    void enterCompilation_unit(DDLParser.Compilation_unitContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#compilation_unit}.
     *
     * @param ctx the parse tree
     */
    void exitCompilation_unit(DDLParser.Compilation_unitContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#type_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterType_declaration(DDLParser.Type_declarationContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#type_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitType_declaration(DDLParser.Type_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#typedef_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterTypedef_declaration(DDLParser.Typedef_declarationContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#typedef_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitTypedef_declaration(DDLParser.Typedef_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#enum_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterEnum_declaration(DDLParser.Enum_declarationContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#enum_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitEnum_declaration(DDLParser.Enum_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#enum_constants}.
     *
     * @param ctx the parse tree
     */
    void enterEnum_constants(DDLParser.Enum_constantsContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#enum_constants}.
     *
     * @param ctx the parse tree
     */
    void exitEnum_constants(DDLParser.Enum_constantsContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#class_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterClass_declaration(DDLParser.Class_declarationContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#class_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitClass_declaration(DDLParser.Class_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#member}.
     *
     * @param ctx the parse tree
     */
    void enterMember(DDLParser.MemberContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#member}.
     *
     * @param ctx the parse tree
     */
    void exitMember(DDLParser.MemberContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#field_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterField_declaration(DDLParser.Field_declarationContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#field_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitField_declaration(DDLParser.Field_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#variable_dec}.
     *
     * @param ctx the parse tree
     */
    void enterVariable_dec(DDLParser.Variable_decContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#variable_dec}.
     *
     * @param ctx the parse tree
     */
    void exitVariable_dec(DDLParser.Variable_decContext ctx);

    /**
     * Enter a parse tree produced by the {@code void_method_declaration}
     * labeled alternative in {@link DDLParser#method_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterVoid_method_declaration(DDLParser.Void_method_declarationContext ctx);

    /**
     * Exit a parse tree produced by the {@code void_method_declaration} labeled
     * alternative in {@link DDLParser#method_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitVoid_method_declaration(DDLParser.Void_method_declarationContext ctx);

    /**
     * Enter a parse tree produced by the {@code type_method_declaration}
     * labeled alternative in {@link DDLParser#method_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterType_method_declaration(DDLParser.Type_method_declarationContext ctx);

    /**
     * Exit a parse tree produced by the {@code type_method_declaration} labeled
     * alternative in {@link DDLParser#method_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitType_method_declaration(DDLParser.Type_method_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#constructor_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterConstructor_declaration(DDLParser.Constructor_declarationContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#constructor_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitConstructor_declaration(DDLParser.Constructor_declarationContext ctx);

    /**
     * Enter a parse tree produced by the {@code this_constructor_invocation}
     * labeled alternative in {@link DDLParser#explicit_constructor_invocation}.
     *
     * @param ctx the parse tree
     */
    void enterThis_constructor_invocation(DDLParser.This_constructor_invocationContext ctx);

    /**
     * Exit a parse tree produced by the {@code this_constructor_invocation}
     * labeled alternative in {@link DDLParser#explicit_constructor_invocation}.
     *
     * @param ctx the parse tree
     */
    void exitThis_constructor_invocation(DDLParser.This_constructor_invocationContext ctx);

    /**
     * Enter a parse tree produced by the {@code super_constructor_invocation}
     * labeled alternative in {@link DDLParser#explicit_constructor_invocation}.
     *
     * @param ctx the parse tree
     */
    void enterSuper_constructor_invocation(DDLParser.Super_constructor_invocationContext ctx);

    /**
     * Exit a parse tree produced by the {@code super_constructor_invocation}
     * labeled alternative in {@link DDLParser#explicit_constructor_invocation}.
     *
     * @param ctx the parse tree
     */
    void exitSuper_constructor_invocation(DDLParser.Super_constructor_invocationContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#predicate_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterPredicate_declaration(DDLParser.Predicate_declarationContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#predicate_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitPredicate_declaration(DDLParser.Predicate_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterStatement(DDLParser.StatementContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitStatement(DDLParser.StatementContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#block}.
     *
     * @param ctx the parse tree
     */
    void enterBlock(DDLParser.BlockContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#block}.
     *
     * @param ctx the parse tree
     */
    void exitBlock(DDLParser.BlockContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#assignment_statement}.
     *
     * @param ctx the parse tree
     */
    void enterAssignment_statement(DDLParser.Assignment_statementContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#assignment_statement}.
     *
     * @param ctx the parse tree
     */
    void exitAssignment_statement(DDLParser.Assignment_statementContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link DDLParser#local_variable_statement}.
     *
     * @param ctx the parse tree
     */
    void enterLocal_variable_statement(DDLParser.Local_variable_statementContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#local_variable_statement}.
     *
     * @param ctx the parse tree
     */
    void exitLocal_variable_statement(DDLParser.Local_variable_statementContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#expression_statement}.
     *
     * @param ctx the parse tree
     */
    void enterExpression_statement(DDLParser.Expression_statementContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#expression_statement}.
     *
     * @param ctx the parse tree
     */
    void exitExpression_statement(DDLParser.Expression_statementContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#preference_statement}.
     *
     * @param ctx the parse tree
     */
    void enterPreference_statement(DDLParser.Preference_statementContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#preference_statement}.
     *
     * @param ctx the parse tree
     */
    void exitPreference_statement(DDLParser.Preference_statementContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#disjunction_statement}.
     *
     * @param ctx the parse tree
     */
    void enterDisjunction_statement(DDLParser.Disjunction_statementContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#disjunction_statement}.
     *
     * @param ctx the parse tree
     */
    void exitDisjunction_statement(DDLParser.Disjunction_statementContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#disjunct}.
     *
     * @param ctx the parse tree
     */
    void enterDisjunct(DDLParser.DisjunctContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#disjunct}.
     *
     * @param ctx the parse tree
     */
    void exitDisjunct(DDLParser.DisjunctContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#formula_statement}.
     *
     * @param ctx the parse tree
     */
    void enterFormula_statement(DDLParser.Formula_statementContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#formula_statement}.
     *
     * @param ctx the parse tree
     */
    void exitFormula_statement(DDLParser.Formula_statementContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#return_statement}.
     *
     * @param ctx the parse tree
     */
    void enterReturn_statement(DDLParser.Return_statementContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#return_statement}.
     *
     * @param ctx the parse tree
     */
    void exitReturn_statement(DDLParser.Return_statementContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#assignment_list}.
     *
     * @param ctx the parse tree
     */
    void enterAssignment_list(DDLParser.Assignment_listContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#assignment_list}.
     *
     * @param ctx the parse tree
     */
    void exitAssignment_list(DDLParser.Assignment_listContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#assignment}.
     *
     * @param ctx the parse tree
     */
    void enterAssignment(DDLParser.AssignmentContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#assignment}.
     *
     * @param ctx the parse tree
     */
    void exitAssignment(DDLParser.AssignmentContext ctx);

    /**
     * Enter a parse tree produced by the {@code cast_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterCast_expression(DDLParser.Cast_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code cast_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitCast_expression(DDLParser.Cast_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code qualified_id_expression}
     * labeled alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterQualified_id_expression(DDLParser.Qualified_id_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code qualified_id_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitQualified_id_expression(DDLParser.Qualified_id_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code division_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterDivision_expression(DDLParser.Division_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code division_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitDivision_expression(DDLParser.Division_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code subtraction_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterSubtraction_expression(DDLParser.Subtraction_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code subtraction_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitSubtraction_expression(DDLParser.Subtraction_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code plus_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterPlus_expression(DDLParser.Plus_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code plus_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitPlus_expression(DDLParser.Plus_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code function_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterFunction_expression(DDLParser.Function_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code function_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitFunction_expression(DDLParser.Function_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code addition_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterAddition_expression(DDLParser.Addition_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code addition_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitAddition_expression(DDLParser.Addition_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code minus_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterMinus_expression(DDLParser.Minus_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code minus_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitMinus_expression(DDLParser.Minus_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code parentheses_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterParentheses_expression(DDLParser.Parentheses_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code parentheses_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitParentheses_expression(DDLParser.Parentheses_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code implication_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterImplication_expression(DDLParser.Implication_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code implication_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitImplication_expression(DDLParser.Implication_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code lt_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterLt_expression(DDLParser.Lt_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code lt_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitLt_expression(DDLParser.Lt_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code not_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterNot_expression(DDLParser.Not_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code not_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitNot_expression(DDLParser.Not_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code conjunction_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterConjunction_expression(DDLParser.Conjunction_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code conjunction_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitConjunction_expression(DDLParser.Conjunction_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code geq_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterGeq_expression(DDLParser.Geq_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code geq_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitGeq_expression(DDLParser.Geq_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code range_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterRange_expression(DDLParser.Range_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code range_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitRange_expression(DDLParser.Range_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code multiplication_expression}
     * labeled alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterMultiplication_expression(DDLParser.Multiplication_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code multiplication_expression}
     * labeled alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitMultiplication_expression(DDLParser.Multiplication_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code leq_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterLeq_expression(DDLParser.Leq_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code leq_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitLeq_expression(DDLParser.Leq_expressionContext ctx);

    /**
     * Enter a parse tree produced by the
     * {@code exclusive_disjunction_expression} labeled alternative in
     * {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterExclusive_disjunction_expression(DDLParser.Exclusive_disjunction_expressionContext ctx);

    /**
     * Exit a parse tree produced by the
     * {@code exclusive_disjunction_expression} labeled alternative in
     * {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitExclusive_disjunction_expression(DDLParser.Exclusive_disjunction_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code gt_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterGt_expression(DDLParser.Gt_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code gt_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitGt_expression(DDLParser.Gt_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code constructor_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterConstructor_expression(DDLParser.Constructor_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code constructor_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitConstructor_expression(DDLParser.Constructor_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code disjunction_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterDisjunction_expression(DDLParser.Disjunction_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code disjunction_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitDisjunction_expression(DDLParser.Disjunction_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code literal_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterLiteral_expression(DDLParser.Literal_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code literal_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitLiteral_expression(DDLParser.Literal_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code eq_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterEq_expression(DDLParser.Eq_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code eq_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitEq_expression(DDLParser.Eq_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code neq_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterNeq_expression(DDLParser.Neq_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code neq_expression} labeled
     * alternative in {@link DDLParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitNeq_expression(DDLParser.Neq_expressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#expr_list}.
     *
     * @param ctx the parse tree
     */
    void enterExpr_list(DDLParser.Expr_listContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#expr_list}.
     *
     * @param ctx the parse tree
     */
    void exitExpr_list(DDLParser.Expr_listContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#literal}.
     *
     * @param ctx the parse tree
     */
    void enterLiteral(DDLParser.LiteralContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#literal}.
     *
     * @param ctx the parse tree
     */
    void exitLiteral(DDLParser.LiteralContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#qualified_id}.
     *
     * @param ctx the parse tree
     */
    void enterQualified_id(DDLParser.Qualified_idContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#qualified_id}.
     *
     * @param ctx the parse tree
     */
    void exitQualified_id(DDLParser.Qualified_idContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#type}.
     *
     * @param ctx the parse tree
     */
    void enterType(DDLParser.TypeContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#type}.
     *
     * @param ctx the parse tree
     */
    void exitType(DDLParser.TypeContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#class_type}.
     *
     * @param ctx the parse tree
     */
    void enterClass_type(DDLParser.Class_typeContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#class_type}.
     *
     * @param ctx the parse tree
     */
    void exitClass_type(DDLParser.Class_typeContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#primitive_type}.
     *
     * @param ctx the parse tree
     */
    void enterPrimitive_type(DDLParser.Primitive_typeContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#primitive_type}.
     *
     * @param ctx the parse tree
     */
    void exitPrimitive_type(DDLParser.Primitive_typeContext ctx);

    /**
     * Enter a parse tree produced by {@link DDLParser#typed_list}.
     *
     * @param ctx the parse tree
     */
    void enterTyped_list(DDLParser.Typed_listContext ctx);

    /**
     * Exit a parse tree produced by {@link DDLParser#typed_list}.
     *
     * @param ctx the parse tree
     */
    void exitTyped_list(DDLParser.Typed_listContext ctx);
}
