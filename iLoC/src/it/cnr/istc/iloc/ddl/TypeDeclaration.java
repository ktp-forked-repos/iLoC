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

import it.cnr.istc.iloc.Type;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class TypeDeclaration extends DDLBaseListener {

    private final ISolver solver;
    private final DDLParser parser;
    private final ParseTreeProperty<IScope> scopes;
    private IScope scope;

    TypeDeclaration(ISolver solver, DDLParser parser, ParseTreeProperty<IScope> scopes) {
        this.solver = solver;
        this.parser = parser;
        this.scopes = scopes;
    }

    @Override
    public void enterCompilation_unit(DDLParser.Compilation_unitContext ctx) {
        scope = scopes.get(ctx);
    }

    @Override
    public void enterTypedef_declaration(DDLParser.Typedef_declarationContext ctx) {
        IType primitive_type = new TypeVisitor(solver, parser, scopes).visit(ctx.primitive_type());
        if (primitive_type == null) {
            parser.notifyErrorListeners(ctx.expr().start, "cannot find symbol " + ctx.primitive_type().getText(), new RecognitionException(parser, parser.getInputStream(), ctx.expr()));
        }
        if (ctx.expr() instanceof DDLParser.Range_expressionContext) {
            INumber min = (INumber) new ObjectVisitor(solver, parser, scopes, null).visit(((DDLParser.Range_expressionContext) ctx.expr()).min);
            INumber max = (INumber) new ObjectVisitor(solver, parser, scopes, null).visit(((DDLParser.Range_expressionContext) ctx.expr()).max);
            DDLTypeDef type_def = new DDLTypeDef(solver, scope, primitive_type, ctx.name.getText(), min, max);
            scope.defineType(type_def);
        } else if (ctx.expr() instanceof DDLParser.Literal_expressionContext) {
            INumber val = (INumber) new ObjectVisitor(solver, parser, scopes, null).visit(ctx.expr());
            DDLTypeDef type_def = new DDLTypeDef(solver, scope, primitive_type, ctx.name.getText(), val, val);
            scope.defineType(type_def);
        } else {
            parser.notifyErrorListeners(ctx.expr().start, "Incompatible types: expected numeric expression", new RecognitionException(parser, parser.getInputStream(), ctx.expr()));
        }
    }

    @Override
    public void enterEnum_declaration(DDLParser.Enum_declarationContext ctx) {
        DDLEnumType enum_type = new DDLEnumType(solver, scope, ctx.name.getText());
        scopes.put(ctx, enum_type);
        ctx.enum_constants().forEach(ec -> {
            ec.StringLiteral().forEach(tn -> {
                enum_type.addEnum(solver.getConstraintNetwork().newString(tn.getSymbol().getText()));
            });
        });
        scope.defineType(enum_type);
    }

    @Override
    public void enterClass_declaration(DDLParser.Class_declarationContext ctx) {
        IType c_type = new Type(solver, scope, ctx.name.getText());
        scopes.put(ctx, c_type);
        scope.defineType(c_type);
        scope = c_type;
    }

    @Override
    public void exitClass_declaration(DDLParser.Class_declarationContext ctx) {
        scope = scope.getEnclosingScope();
    }

    @Override
    public void enterClass_type(DDLParser.Class_typeContext ctx) {
        scopes.put(ctx, scope);
    }
}
