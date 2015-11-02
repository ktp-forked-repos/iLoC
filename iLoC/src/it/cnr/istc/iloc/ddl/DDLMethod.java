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

import it.cnr.istc.iloc.Environment;
import it.cnr.istc.iloc.Method;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class DDLMethod extends Method {

    private final DDLParser parser;
    private final ParseTreeProperty<IScope> scopes;
    private final DDLParser.BlockContext context;

    DDLMethod(ISolver solver, IScope enclosingScope, String name, IType returnType, IField[] parameters, DDLParser parser, ParseTreeProperty<IScope> scopes, DDLParser.BlockContext context) {
        super(solver, enclosingScope, name, returnType, parameters);
        this.parser = parser;
        this.scopes = scopes;
        this.context = context;
    }

    @Override
    public IObject invoke(IObject object, IObject... expressions) {
        assert expressions.length == parameters.length;

        IEnvironment c_environment = new Environment(solver, object);
        // We associate to method parameters expressions values
        for (int i = 0; i < parameters.length; i++) {
            c_environment.set(parameters[i].getName(), expressions[i]);
        }

        StatementExecutor statementExecutor = new StatementExecutor(solver, parser, scopes, c_environment);
        context.statement().forEach(statement -> {
            statementExecutor.visit(statement);
        });

        if (returnType != null) {
            return c_environment.get("return");
        } else {
            return solver.getConstraintNetwork().newBool("true");
        }
    }
}
