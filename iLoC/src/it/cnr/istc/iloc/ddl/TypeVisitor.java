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
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class TypeVisitor extends DDLBaseVisitor<IType> {

    private static final Logger LOG = Logger.getLogger(TypeVisitor.class.getName());
    private final ISolver solver;
    private final DDLParser parser;
    private final ParseTreeProperty<IScope> scopes;

    TypeVisitor(ISolver solver, DDLParser parser, ParseTreeProperty<IScope> scopes) {
        this.solver = solver;
        this.parser = parser;
        this.scopes = scopes;
    }

    @Override
    public IType visitLiteral_expression(DDLParser.Literal_expressionContext ctx) {
        if (ctx.literal().string != null) {
            try {
                return solver.getType(Constants.STRING);
            } catch (ClassNotFoundException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        } else if (ctx.literal().numeric != null) {
            try {
                if (ctx.literal().NumericLiteral().getText().contains(".")) {
                    return solver.getType(Constants.REAL);
                } else {
                    return solver.getType(Constants.INT);
                }
            } catch (ClassNotFoundException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        } else if (ctx.literal().t != null || ctx.literal().f != null) {
            try {
                return solver.getType(Constants.BOOL);
            } catch (ClassNotFoundException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public IType visitCast_expression(DDLParser.Cast_expressionContext ctx) {
        return visit(ctx.type());
    }

    @Override
    public IType visitPrimitive_type(DDLParser.Primitive_typeContext ctx) {
        try {
            return solver.getType(ctx.getText());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public IType visitClass_type(DDLParser.Class_typeContext ctx) {
        IScope c_type = scopes.get(ctx);
        for (TerminalNode id : ctx.ID()) {
            try {
                c_type = c_type.getType(id.getText());
            } catch (ClassNotFoundException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return (IType) c_type;
    }

    @Override
    public IType visitQualified_id(DDLParser.Qualified_idContext ctx) {
        IScope c_scope = scopes.get(ctx);
        for (TerminalNode id : ctx.ID()) {
            try {
                c_scope = c_scope.getField(id.getText()).getType();
            } catch (NoSuchFieldException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return (IType) c_scope;
    }

    @Override
    public IType visitQualified_id_expression(DDLParser.Qualified_id_expressionContext ctx) {
        IScope c_scope = scopes.get(ctx);
        try {
            if (((TerminalNode) ctx.qualified_id().getChild(0)).getSymbol().getType() == DDLLexer.THIS) {
                c_scope = c_scope.getField("this").getType();
            } else if (((TerminalNode) ctx.qualified_id().getChild(0)).getSymbol().getType() == DDLLexer.SUPER) {
                c_scope = c_scope.getField("super").getType();
            }
        } catch (NoSuchFieldException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        for (TerminalNode id : ctx.qualified_id().ID()) {
            try {
                c_scope = c_scope.getField(id.getText()).getType();
            } catch (NoSuchFieldException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return (IType) c_scope;
    }

    @Override
    public IType visitConstructor_expression(DDLParser.Constructor_expressionContext ctx) {
        return visit(ctx.type());
    }
}
