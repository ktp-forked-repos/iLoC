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
import it.cnr.istc.iloc.Preference;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class DDLPreference extends Preference {

    private final DDLParser parser;
    private final ParseTreeProperty<IScope> scopes;
    private final DDLParser.Preference_statementContext context;

    DDLPreference(ISolver solver, IScope enclosingScope, DDLParser parser, ParseTreeProperty<IScope> scopes, DDLParser.Preference_statementContext context, INumber cost) {
        super(solver, enclosingScope, cost);
        this.parser = parser;
        this.scopes = scopes;
        this.context = context;
    }

    @Override
    public void execute(IEnvironment environment) {
        StatementExecutor statementExecutor = new StatementExecutor(solver, parser, scopes, new Environment(solver, environment));
        context.block().statement().stream().forEach(statement -> {
            statementExecutor.visit(statement);
        });
    }
}
