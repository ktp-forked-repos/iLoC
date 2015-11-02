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

import it.cnr.istc.iloc.Constructor;
import it.cnr.istc.iloc.Environment;
import it.cnr.istc.iloc.api.IConstructor;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class DDLConstructor extends Constructor {

    private final DDLParser parser;
    private final ParseTreeProperty<IScope> scopes;
    private final DDLParser.Explicit_constructor_invocationContext explicit_constructor_invocation;
    private final DDLParser.BlockContext context;

    DDLConstructor(ISolver solver, IScope enclosingScope, IField[] parameters, DDLParser parser, ParseTreeProperty<IScope> scopes, DDLParser.Explicit_constructor_invocationContext explicit_constructor_invocation, DDLParser.BlockContext context) {
        super(solver, enclosingScope, parameters);
        this.parser = parser;
        this.scopes = scopes;
        this.explicit_constructor_invocation = explicit_constructor_invocation;
        this.context = context;
    }

    @Override
    public void init(IObject instance) {
        IEnvironment c_environment = new Environment(solver, instance);
        assert getFields().containsKey("this");
        c_environment.set("this", instance);
        if (getFields().containsKey("super")) {
            c_environment.set("super", instance);
        }

        enclosingScope.getFields().values().stream().filter(field -> !field.isSynthetic()).forEach(field -> {
            if (field instanceof DDLField && ((DDLField) field).context != null) {
                // Field level instantiation
                instance.set(field.getName(), new ObjectVisitor(solver, parser, scopes, c_environment).visit(((DDLField) field).context));
            }
        });
    }

    @Override
    public void invoke(IObject instance, IObject... expressions) {
        IEnvironment c_environment = new Environment(solver, instance);
        assert getFields().containsKey("this");
        c_environment.set("this", instance);
        if (getFields().containsKey("super")) {
            c_environment.set("super", instance);
        }
        // We associate to constructor parameters expressions values
        for (int i = 0; i < parameters.length; i++) {
            c_environment.set(parameters[i].getName(), expressions[i]);
        }

        if (explicit_constructor_invocation != null) {
            if (explicit_constructor_invocation instanceof DDLParser.This_constructor_invocationContext) {
                // we have a 'this' constructor invocation
                IType[] c_par_types;
                IObject[] c_exprs;
                if (((DDLParser.This_constructor_invocationContext) explicit_constructor_invocation).expr_list() != null) {
                    c_par_types = ((DDLParser.This_constructor_invocationContext) explicit_constructor_invocation).expr_list().expr().stream().map(expr -> new TypeVisitor(solver, parser, scopes).visit(expr)).toArray(IType[]::new);
                    c_exprs = ((DDLParser.This_constructor_invocationContext) explicit_constructor_invocation).expr_list().expr().stream().map(expr -> new ObjectVisitor(solver, parser, scopes, c_environment).visit(expr)).toArray(IObject[]::new);
                } else {
                    c_par_types = new IType[0];
                    c_exprs = new IObject[0];
                }
                try {
                    IConstructor constructor = ((IType) enclosingScope).getConstructor(c_par_types);
                    constructor.invoke(instance, c_exprs);
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(DDLConstructor.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (explicit_constructor_invocation instanceof DDLParser.Super_constructor_invocationContext) {
                // we have a 'super' constructor invocation
                IType[] c_par_types;
                IObject[] c_exprs;
                if (((DDLParser.Super_constructor_invocationContext) explicit_constructor_invocation).expr_list() != null) {
                    c_par_types = ((DDLParser.Super_constructor_invocationContext) explicit_constructor_invocation).expr_list().expr().stream().map(expr -> new TypeVisitor(solver, parser, scopes).visit(expr)).toArray(IType[]::new);
                    c_exprs = ((DDLParser.Super_constructor_invocationContext) explicit_constructor_invocation).expr_list().expr().stream().map(expr -> new ObjectVisitor(solver, parser, scopes, c_environment).visit(expr)).toArray(IObject[]::new);
                } else {
                    c_par_types = new IType[0];
                    c_exprs = new IObject[0];
                }
                try {
                    IConstructor constructor = ((IType) enclosingScope).getSuperclass().getConstructor(c_par_types);
                    constructor.init(instance);
                    constructor.invoke(instance, c_exprs);
                    constructor.completeInit(instance);
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(DDLConstructor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        StatementExecutor statementExecutor = new StatementExecutor(solver, parser, scopes, c_environment);
        context.statement().forEach(statement -> {
            statementExecutor.visit(statement);
        });
    }
}
