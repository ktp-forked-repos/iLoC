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
package it.cnr.istc.iloc.pddl;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class PDDLTranslator {

    private static final ParseTreeWalker WALKER = new ParseTreeWalker();

    public static String translatePDDLInstance(File pddl_domain, File pddl_problem) throws IOException {
        // We get the requirements
        Set<String> domain_requirements = PDDLRequirements.getRequirements(pddl_domain);
        Set<String> problem_requirements = PDDLRequirements.getRequirements(pddl_problem);
        problem_requirements.addAll(domain_requirements);

        // we create the domain parser..
        PDDLLexer domain_lexer = new PDDLLexer(new CaseInsensitiveFileStream(pddl_domain.getPath()));
        domain_lexer.requirements.addAll(domain_requirements);
        PDDLParser domain_parser = new PDDLParser(new CommonTokenStream(domain_lexer));
        domain_parser.requirements.addAll(domain_requirements);
        domain_parser.addErrorListener(new BaseErrorListener());

        // we create the problem parser..
        PDDLLexer problem_lexer = new PDDLLexer(new CaseInsensitiveFileStream(pddl_problem.getPath()));
        problem_lexer.requirements.addAll(problem_requirements);
        PDDLParser problem_parser = new PDDLParser(new CommonTokenStream(problem_lexer));
        problem_parser.requirements.addAll(problem_requirements);
        problem_parser.addErrorListener(new BaseErrorListener());

        // We parse the domain..
        PDDLParser.DomainContext domain_context = domain_parser.domain();

        // We parse the problem..
        PDDLParser.ProblemContext problem_context = problem_parser.problem();

        Domain domain = new Domain(Utils.capitalize(domain_context.name().NAME().getSymbol().getText()));
        Problem problem = new Problem(domain, Utils.capitalize(problem_context.name(1).NAME().getSymbol().getText()));

        TermVisitor term_visitor = new TermVisitor(domain_parser, domain, problem);

        if (domain_context.types_def() != null) {
            /**
             * We define all the types of the domain.
             */
            WALKER.walk(new PDDLBaseListener() {

                @Override
                public void enterTyped_list_name(PDDLParser.Typed_list_nameContext ctx) {
                    Type c_superclass = null;
                    if (ctx.type() == null) {
                        c_superclass = Type.OBJECT;
                    } else if (ctx.type().primitive_type().size() == 1) {
                        c_superclass = ctx.type().primitive_type(0).name() == null ? Type.OBJECT : domain.getType(Utils.capitalize(ctx.type().primitive_type(0).name().getText()));
                        if (c_superclass == null) {
                            c_superclass = new Type(Utils.capitalize(ctx.type().primitive_type(0).name().getText()));
                            domain.addType(c_superclass);
                        }
                    } else {
                        c_superclass = new EitherType(ctx.type().primitive_type().stream().map(primitive_type -> primitive_type.name() == null ? Type.OBJECT : domain.getType(Utils.capitalize(primitive_type.name().getText()))).collect(Collectors.toList()));
                        domain.addType(c_superclass);
                    }
                    final Type superclass = c_superclass;
                    ctx.name().stream().forEach(type_name -> {
                        Type type = new Type(Utils.capitalize(type_name.getText()));
                        type.setSuperclass(superclass);
                        domain.addType(type);
                    });
                }
            }, domain_context.types_def());
        }

        if (domain_context.constants_def() != null) {
            /**
             * We define the constants.
             */
            WALKER.walk(new PDDLBaseListener() {

                @Override
                public void enterTyped_list_name(PDDLParser.Typed_list_nameContext ctx) {
                    Type type = null;
                    if (ctx.type() == null) {
                        type = Type.OBJECT;
                    } else if (ctx.type().primitive_type().size() == 1) {
                        type = ctx.type().primitive_type(0).name() == null ? Type.OBJECT : domain.getType(Utils.capitalize(ctx.type().primitive_type(0).name().getText()));
                    } else {
                        type = new EitherType(ctx.type().primitive_type().stream().map(primitive_type -> primitive_type.name() == null ? Type.OBJECT : domain.getType(Utils.capitalize(primitive_type.name().getText()))).collect(Collectors.toList()));
                        domain.addType(type);
                    }

                    assert type != null : "Cannot find type " + ctx.type().primitive_type(0).name().getText();
                    Type c_type = type;
                    ctx.name().stream().forEach(name -> {
                        domain.addConstant(c_type.newInstance(Utils.lowercase(name.getText())));
                    });
                }
            }, domain_context.constants_def());
        }

        if (domain_context.predicates_def() != null) {
            /**
             * We define the predicates.
             */
            WALKER.walk(new PDDLBaseListener() {

                @Override
                public void enterAtomic_formula_skeleton(PDDLParser.Atomic_formula_skeletonContext ctx) {
                    Variable[] variables = new Variable[0];
                    if (ctx.typed_list_variable() != null) {
                        // The predicate formula has parameters
                        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
                        WALKER.walk(typedListVariable, ctx.typed_list_variable());
                        variables = typedListVariable.variables.toArray(new Variable[typedListVariable.variables.size()]);
                    }
                    domain.addPredicate(new Predicate(Utils.capitalize(ctx.predicate().name().getText()), variables));
                }
            }, domain_context.predicates_def());
        }

        if (domain_context.functions_def() != null) {
            /**
             * We define the functions.
             */
            WALKER.walk(new PDDLBaseListener() {

                Type type = null;

                @Override
                public void enterFunction_typed_list_atomic_function_skeleton(PDDLParser.Function_typed_list_atomic_function_skeletonContext ctx) {
                    if (ctx.function_type() == null) {
                        type = Type.OBJECT;
                    } else if (ctx.function_type().type() == null) {
                        type = Type.NUMBER;
                    } else if (ctx.function_type().type().primitive_type().size() == 1) {
                        type = ctx.function_type().type().primitive_type(0).name() == null ? Type.OBJECT : domain.getType(Utils.capitalize(ctx.function_type().type().primitive_type(0).name().getText()));
                    } else {
                        type = new EitherType(ctx.function_type().type().primitive_type().stream().map(primitive_type -> primitive_type.name() == null ? Type.OBJECT : domain.getType(Utils.capitalize(primitive_type.name().getText()))).collect(Collectors.toList()));
                        domain.addType(type);
                    }
                }

                @Override
                public void enterAtomic_function_skeleton(PDDLParser.Atomic_function_skeletonContext ctx) {
                    Variable[] variables = new Variable[0];
                    if (ctx.typed_list_variable() != null) {
                        // The predicate formula has parameters
                        TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
                        WALKER.walk(typedListVariable, ctx.typed_list_variable());
                        variables = typedListVariable.variables.toArray(new Variable[typedListVariable.variables.size()]);
                    }
                    domain.addFunction(new Function(Utils.capitalize(ctx.function_symbol().name().getText()), type, variables));
                }
            }, domain_context.functions_def());
        }

        /**
         * We define the structures.
         */
        domain_context.structure_def().stream().forEach(structure_def -> {
            if (structure_def.action_def() != null) {
                WALKER.walk(new PDDLBaseListener() {

                    @Override
                    public void enterAction_def(PDDLParser.Action_defContext ctx) {
                        Variable[] variables = new Variable[0];
                        if (ctx.typed_list_variable() != null) {
                            // The action has parameters
                            TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
                            WALKER.walk(typedListVariable, ctx.typed_list_variable());
                            variables = typedListVariable.variables.toArray(new Variable[typedListVariable.variables.size()]);
                        }
                        domain.addAction(new Action(Utils.capitalize(ctx.action_symbol().name().getText()), variables, ctx.action_def_body().emptyOr_pre_GD() != null ? term_visitor.visit(ctx.action_def_body().emptyOr_pre_GD()) : null, ctx.action_def_body().emptyOr_effect() != null ? term_visitor.visit(ctx.action_def_body().emptyOr_effect()) : null));
                    }
                }, structure_def.action_def());
            } else if (structure_def.durative_action_def() != null) {
                WALKER.walk(new PDDLBaseListener() {

                    @Override
                    public void enterDurative_action_def(PDDLParser.Durative_action_defContext ctx) {
                        Variable[] variables = new Variable[0];
                        if (ctx.typed_list_variable() != null) {
                            // The durative action has parameters
                            TypedListVariableListener typedListVariable = new TypedListVariableListener(domain);
                            WALKER.walk(typedListVariable, ctx.typed_list_variable());
                            variables = typedListVariable.variables.toArray(new Variable[typedListVariable.variables.size()]);
                        }
                        domain.addDurativeAction(new DurativeAction(Utils.capitalize(ctx.da_symbol().name().getText()), variables, term_visitor.visit(ctx.da_def_body().duration_constraint()), term_visitor.visit(ctx.da_def_body().emptyOr_da_GD()), term_visitor.visit(ctx.da_def_body().emptyOr_da_effect())));
                    }
                }, structure_def.durative_action_def());
            }
        });

        /**
         * We define the objects.
         */
        if (problem_context.object_declaration() != null) {
            WALKER.walk(new PDDLBaseListener() {

                @Override
                public void enterTyped_list_name(PDDLParser.Typed_list_nameContext ctx) {
                    Type type = null;
                    if (ctx.type() == null) {
                        type = Type.OBJECT;
                    } else if (ctx.type().primitive_type().size() == 1) {
                        type = ctx.type().primitive_type(0).name() == null ? Type.OBJECT : domain.getType(Utils.capitalize(ctx.type().primitive_type(0).name().getText()));
                    }
                    final Type c_type = type;
                    ctx.name().stream().forEach(object -> {
                        problem.addObject(c_type.newInstance(Utils.lowercase(object.getText())));
                    });
                }
            }, problem_context.object_declaration());
        }

        /**
         * We define the initial state
         */
        problem_context.init().init_el().stream().forEach(init_el -> {
            problem.addInitEl(term_visitor.visit(init_el));
        });

        /**
         * We define the goal
         */
        problem.setGoal(term_visitor.visit(problem_context.goal().pre_GD()));

        STGroupFile file = new STGroupFile(PDDLTranslator.class.getResource("PDDLTemplate.stg").getPath());
        file.registerRenderer(Domain.class, new DomainRenderer(file));
        file.registerRenderer(Problem.class, new ProblemRenderer(file));
        file.registerRenderer(Action.class, new ActionRenderer(file));
        file.registerRenderer(DurativeAction.class, new DurativeActionRenderer(file));
        file.registerRenderer(Predicate.class, new PredicateRenderer(file, domain));
        file.registerRenderer(Function.class, new FunctionRenderer(file, domain));
        ST translation = file.getInstanceOf("PDDL");
        translation.add("domain", domain);
        translation.add("problem", problem);

        return translation.render();
    }

    private PDDLTranslator() {
    }
}
