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
import it.cnr.istc.iloc.Predicate;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class DDLPredicate extends Predicate {

    private final DDLParser parser;
    private final ParseTreeProperty<IScope> scopes;
    private final DDLParser.BlockContext context;

    DDLPredicate(ISolver solver, IScope enclosingScope, String name, IField[] fields, DDLParser parser, ParseTreeProperty<IScope> scopes, DDLParser.BlockContext context) {
        super(solver, enclosingScope, name, fields);
        this.parser = parser;
        this.scopes = scopes;
        this.context = context;
    }

    @Override
    public void applyRule(IFormula formula) {
        IEnvironment c_environment = new Environment(solver, formula);
        c_environment.set("this", formula);

        StatementExecutor statementExecutor = new StatementExecutor(solver, parser, scopes, c_environment);
        context.statement().forEach(statement -> {
            statementExecutor.visit(statement);
        });
    }
}
