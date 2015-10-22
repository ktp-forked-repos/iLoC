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

import it.cnr.istc.iloc.Disjunction;
import it.cnr.istc.iloc.Field;
import it.cnr.istc.iloc.api.IDisjunct;
import it.cnr.istc.iloc.api.IDisjunction;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IPreference;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import java.util.List;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class TypeRefinement extends DDLBaseListener {

    private final ISolver solver;
    private final DDLParser parser;
    private final ParseTreeProperty<IScope> scopes;
    private IScope scope;

    TypeRefinement(ISolver solver, DDLParser parser, ParseTreeProperty<IScope> scopes) {
        this.solver = solver;
        this.parser = parser;
        this.scopes = scopes;
    }

    @Override
    public void enterCompilation_unit(DDLParser.Compilation_unitContext ctx) {
        scope = scopes.get(ctx);
    }

    @Override
    public void enterEnum_declaration(DDLParser.Enum_declarationContext ctx) {
        DDLEnumType enum_type = (DDLEnumType) scopes.get(ctx);
        ctx.enum_constants().stream().filter((ec) -> (ec.type() != null)).map((ec) -> (DDLEnumType) new TypeVisitor(solver, parser, scopes).visit(ec.type())).forEach((base_type) -> {
            enum_type.addEnums(base_type.getEnums());
        });
    }

    @Override
    public void enterClass_declaration(DDLParser.Class_declarationContext ctx) {
        scope = scopes.get(ctx);
        if (ctx.type() != null) {
            ((IType) scope).setSuperclass(new TypeVisitor(solver, parser, scopes).visit(ctx.type()));
        }
    }

    @Override
    public void enterField_declaration(DDLParser.Field_declarationContext ctx) {
        IType c_type = new TypeVisitor(solver, parser, scopes).visit(ctx.type());
        ctx.variable_dec().stream().forEach((variable_dec) -> {
            scope.defineField(new DDLField(variable_dec.expr(), variable_dec.name.getText(), c_type));
        });
    }

    @Override
    public void enterConstructor_declaration(DDLParser.Constructor_declarationContext ctx) {
        IField[] parameters;
        if (ctx.typed_list() != null) {
            List<DDLParser.TypeContext> typed_list = ctx.typed_list().type();
            List<TerminalNode> ids = ctx.typed_list().ID();
            parameters = new IField[typed_list.size()];
            for (int i = 0; i < typed_list.size(); i++) {
                parameters[i] = new Field(ids.get(i).getText(), new TypeVisitor(solver, parser, scopes).visit(typed_list.get(i)));
            }
        } else {
            parameters = new IField[0];
        }
        DDLConstructor constructor = new DDLConstructor(solver, scope, parameters, parser, scopes, ctx.explicit_constructor_invocation(), ctx.block());
        ((IType) scope).defineConstructor(constructor);
        scopes.put(ctx, constructor);
        scope = constructor;
    }

    @Override
    public void exitConstructor_declaration(DDLParser.Constructor_declarationContext ctx) {
        scope = scope.getEnclosingScope();
    }

    @Override
    public void enterVoid_method_declaration(DDLParser.Void_method_declarationContext ctx) {
        IField[] parameters;
        if (ctx.typed_list() != null) {
            List<DDLParser.TypeContext> typed_list = ctx.typed_list().type();
            List<TerminalNode> ids = ctx.typed_list().ID();
            parameters = new IField[typed_list.size()];
            for (int i = 0; i < typed_list.size(); i++) {
                parameters[i] = new Field(ids.get(i).getText(), new TypeVisitor(solver, parser, scopes).visit(typed_list.get(i)));
            }
        } else {
            parameters = new IField[0];
        }
        DDLMethod method = new DDLMethod(solver, scope, ctx.name.getText(), null, parameters, parser, scopes, ctx.block());
        scope.defineMethod(method);
        scopes.put(ctx, method);
        scope = method;
    }

    @Override
    public void exitVoid_method_declaration(DDLParser.Void_method_declarationContext ctx) {
        scope = scope.getEnclosingScope();
    }

    @Override
    public void enterType_method_declaration(DDLParser.Type_method_declarationContext ctx) {
        IField[] parameters;
        if (ctx.typed_list() != null) {
            List<DDLParser.TypeContext> typed_list = ctx.typed_list().type();
            List<TerminalNode> ids = ctx.typed_list().ID();
            parameters = new IField[typed_list.size()];
            for (int i = 0; i < typed_list.size(); i++) {
                parameters[i] = new Field(ids.get(i).getText(), new TypeVisitor(solver, parser, scopes).visit(typed_list.get(i)));
            }
        } else {
            parameters = new IField[0];
        }
        DDLMethod method = new DDLMethod(solver, scope, ctx.name.getText(), new TypeVisitor(solver, parser, scopes).visit(ctx.type()), parameters, parser, scopes, ctx.block());
        scope.defineMethod(method);
        scopes.put(ctx, method);
        scope = method;
    }

    @Override
    public void exitType_method_declaration(DDLParser.Type_method_declarationContext ctx) {
        scope = scope.getEnclosingScope();
    }

    @Override
    public void enterPredicate_declaration(DDLParser.Predicate_declarationContext ctx) {
        IField[] parameters;
        if (ctx.typed_list() != null) {
            List<DDLParser.TypeContext> typed_list = ctx.typed_list().type();
            List<TerminalNode> ids = ctx.typed_list().ID();
            parameters = new IField[typed_list.size()];
            for (int i = 0; i < typed_list.size(); i++) {
                parameters[i] = new Field(ids.get(i).getText(), new TypeVisitor(solver, parser, scopes).visit(typed_list.get(i)));
            }
        } else {
            parameters = new IField[0];
        }
        DDLPredicate predicate = new DDLPredicate(solver, scope, ctx.name.getText(), parameters, parser, scopes, ctx.block());
        scope.definePredicate(predicate);
        scopes.put(ctx, predicate);
        scope = predicate;
    }

    @Override
    public void exitPredicate_declaration(DDLParser.Predicate_declarationContext ctx) {
        scope = scope.getEnclosingScope();
    }

    @Override
    public void exitClass_declaration(DDLParser.Class_declarationContext ctx) {
        if (((IType) scope).getConstructors().isEmpty()) {
            DDLParser c_parser = new DDLParser(new CommonTokenStream(new DDLLexer(new ANTLRInputStream(ctx.name.getText() + "(){}"))));
            DDLParser.Constructor_declarationContext constructor_declaration = c_parser.constructor_declaration();
            ((IType) scope).defineConstructor(new DDLConstructor(solver, scope, new IField[0], parser, scopes, constructor_declaration.explicit_constructor_invocation(), constructor_declaration.block()));
        }
        scope = scope.getEnclosingScope();
    }

    @Override
    public void enterPreference_statement(DDLParser.Preference_statementContext ctx) {
        IPreference preference = new DDLPreference(solver, scope, parser, scopes, ctx, (INumber) new ObjectVisitor(solver, parser, scopes, solver).visit(ctx.cost));
        scopes.put(ctx, preference);
        scope = preference;
    }

    @Override
    public void exitPreference_statement(DDLParser.Preference_statementContext ctx) {
        scope = scope.getEnclosingScope();
    }

    @Override
    public void enterDisjunction_statement(DDLParser.Disjunction_statementContext ctx) {
        IDisjunction disjunction = new Disjunction(solver, scope);
        scopes.put(ctx, disjunction);
        scope = disjunction;
    }

    @Override
    public void exitDisjunction_statement(DDLParser.Disjunction_statementContext ctx) {
        scope = scope.getEnclosingScope();
    }

    @Override
    public void enterDisjunct(DDLParser.DisjunctContext ctx) {
        IDisjunct disjunct = new DDLDisjunct(solver, scope, parser, scopes, ctx);
        ((IDisjunction) scope).addDisjunct(disjunct);
        scopes.put(ctx, disjunct);
        scope = disjunct;
    }

    @Override
    public void exitDisjunct(DDLParser.DisjunctContext ctx) {
        scope = scope.getEnclosingScope();
    }

    //<editor-fold defaultstate="collapsed" desc="statements">
    @Override
    public void enterLocal_variable_statement(DDLParser.Local_variable_statementContext ctx) {
        scopes.put(ctx, scope);
    }

    @Override
    public void enterAssignment_statement(DDLParser.Assignment_statementContext ctx) {
        scopes.put(ctx, scope);
    }

    @Override
    public void enterExpression_statement(DDLParser.Expression_statementContext ctx) {
        scopes.put(ctx, scope);
    }

    @Override
    public void enterFormula_statement(DDLParser.Formula_statementContext ctx) {
        scopes.put(ctx, scope);
    }

    @Override
    public void enterReturn_statement(DDLParser.Return_statementContext ctx) {
        scopes.put(ctx, scope);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="expressions">
    @Override
    public void enterQualified_id_expression(DDLParser.Qualified_id_expressionContext ctx) {
        scopes.put(ctx, scope);
    }

    @Override
    public void enterFunction_expression(DDLParser.Function_expressionContext ctx) {
        scopes.put(ctx, scope);
    }
    //</editor-fold>
}
