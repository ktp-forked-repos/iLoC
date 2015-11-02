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

import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class LanguageParser {

    private static final ParseTreeWalker WALKER = new ParseTreeWalker();
    private final ISolver solver;
    private final Properties properties;
    private final ANTLRErrorListener error_listener;
    private final ParseTreeProperty<IScope> scopes = new ParseTreeProperty<>();

    public LanguageParser(ISolver solver, Properties properties, ANTLRErrorListener error_listener) {
        this.solver = solver;
        this.properties = properties;
        this.error_listener = error_listener;
    }

    public void read(String script) {
        DDLParser parser = new DDLParser(new CommonTokenStream(new DDLLexer(new ANTLRInputStream(script))));
        parser.addErrorListener(error_listener);
        DDLParser.Compilation_unitContext compilation_unit = parser.compilation_unit();
        scopes.put(compilation_unit, solver);

        WALKER.walk(new TypeDeclaration(solver, parser, scopes), compilation_unit);
        WALKER.walk(new TypeRefinement(solver, parser, scopes), compilation_unit);
        WALKER.walk(new Preprocessing(solver, parser, scopes), compilation_unit);
        compilation_unit.statement().forEach(statementContext -> {
            WALKER.walk(new StatementTesting(solver, parser, scopes), statementContext);
        });

        StatementExecutor statementExecutor = new StatementExecutor(solver, parser, scopes, solver);
        compilation_unit.statement().forEach(statement -> {
            statementExecutor.visit(statement);
        });
    }

    public void read(File... files) throws IOException {
        List<File> fs = new ArrayList<>(files.length);
        for (File file : files) {
            fs.addAll(Files.walk(Paths.get(file.toURI())).filter(Files::isRegularFile).map(path -> path.toFile()).collect(Collectors.toList()));
        }
        Collection<CodeSnippet> snippets = new ArrayList<>(fs.size());
        for (File f : fs) {
            DDLParser parser = new DDLParser(new CommonTokenStream(new DDLLexer(new ANTLRFileStream(f.getPath()))));
            parser.addErrorListener(error_listener);
            DDLParser.Compilation_unitContext context = parser.compilation_unit();
            scopes.put(context, solver);
            snippets.add(new CodeSnippet(f, parser, context));
        }

        snippets.forEach(snippet -> {
            WALKER.walk(new TypeDeclaration(solver, snippet.parser, scopes), snippet.context);
        });
        snippets.forEach(snippet -> {
            WALKER.walk(new TypeRefinement(solver, snippet.parser, scopes), snippet.context);
        });
        snippets.forEach(snippet -> {
            WALKER.walk(new Preprocessing(solver, snippet.parser, scopes), snippet.context);
        });
        snippets.forEach(snippet -> {
            snippet.context.statement().forEach(statementContext -> {
                WALKER.walk(new StatementTesting(solver, snippet.parser, scopes), statementContext);
            });
        });
        snippets.forEach(snippet -> {
            StatementExecutor statementExecutor = new StatementExecutor(solver, snippet.parser, scopes, solver);
            snippet.context.statement().forEach(statement -> {
                statementExecutor.visit(statement);
            });
        });
    }

    private static class CodeSnippet {

        private final File file;
        private final DDLParser parser;
        private final DDLParser.Compilation_unitContext context;

        CodeSnippet(File file, DDLParser parser, DDLParser.Compilation_unitContext context) {
            this.file = file;
            this.parser = parser;
            this.context = context;
        }
    }
}
