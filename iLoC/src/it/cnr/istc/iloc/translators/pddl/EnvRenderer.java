/*
 * Copyright (C) 2015 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.istc.iloc.translators.pddl;

import java.util.Locale;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class EnvRenderer implements AttributeRenderer {

    private final STGroupFile file;

    EnvRenderer(STGroupFile file) {
        this.file = file;
    }

    @Override
    public String toString(Object o, String string, Locale locale) {
        Env env = (Env) o;
        if (env instanceof And) {
            ST translation = file.getInstanceOf("And");
            translation.add("and", env);
            return translation.render();
        } else if (env instanceof Or) {
            assert ((Or) env).getEnvs().size() > 1;
            ST translation = file.getInstanceOf("Or");
            translation.add("or", env);
            return translation.render();
        } else if (env instanceof Precondition) {
            Precondition precondition = (Precondition) env;
            StringBuilder sb = new StringBuilder();
            if (effectContainsStateVariable(precondition.getAction().getEffect(), precondition.getValue().getStateVariable())) {
                sb.append("goal ").append(precondition.getValue().getStateVariable().getName().toLowerCase()).append("_").append(precondition.getValue().getName().toLowerCase()).append(" = new ").append(precondition.getValue().getStateVariable().getName().toLowerCase()).append(".").append(precondition.getValue().getName()).append("(end:at);");
            } else {
                sb.append("goal ").append(precondition.getValue().getStateVariable().getName().toLowerCase()).append("_").append(precondition.getValue().getName().toLowerCase()).append(" = new ").append(precondition.getValue().getStateVariable().getName().toLowerCase()).append(".").append(precondition.getValue().getName()).append("();\n");
                sb.append(precondition.getValue().getStateVariable().getName().toLowerCase()).append("_").append(precondition.getValue().getName().toLowerCase()).append(".start <= at - 1;\n");
                sb.append(precondition.getValue().getStateVariable().getName().toLowerCase()).append("_").append(precondition.getValue().getName().toLowerCase()).append(".end >= at;");
            }
            return sb.toString();
        } else if (env instanceof Effect) {
            Effect effect = (Effect) env;
            StringBuilder sb = new StringBuilder();
            sb.append("fact ").append(effect.getValue().getStateVariable().getName().toLowerCase()).append("_").append(effect.getValue().getName().toLowerCase()).append(" = new ").append(effect.getValue().getStateVariable().getName().toLowerCase()).append(".").append(effect.getValue().getName()).append("(start:at);");
            sb.append(effect.getValue().getStateVariable().getName().toLowerCase()).append("_").append(effect.getValue().getName().toLowerCase()).append(".duration >= 1;\n");
            return sb.toString();
        } else if (env instanceof InitEl) {
            InitEl init_el = (InitEl) env;
            StringBuilder sb = new StringBuilder();
            sb.append("fact ").append(init_el.getValue().getStateVariable().getName().toLowerCase()).append("_").append(init_el.getValue().getName().toLowerCase()).append(" = new agent.").append(init_el.getValue().getStateVariable().getName().toLowerCase()).append(".").append(init_el.getValue().getName()).append("(start:origin);\n");
            sb.append(init_el.getValue().getStateVariable().getName().toLowerCase()).append("_").append(init_el.getValue().getName().toLowerCase()).append(".duration >= 1;");
            return sb.toString();
        } else if (env instanceof Goal) {
            Goal goal = (Goal) env;
            StringBuilder sb = new StringBuilder();
            sb.append("goal ").append(goal.getValue().getStateVariable().getName().toLowerCase()).append("_").append(goal.getValue().getName().toLowerCase()).append(" = new agent.").append(goal.getValue().getStateVariable().getName().toLowerCase()).append(".").append(goal.getValue().getName()).append("(end:horizon);");
            return sb.toString();
        } else {
            throw new UnsupportedOperationException(env.getClass().getName());
        }
    }

    private static boolean effectContainsStateVariable(Env env, StateVariable state_variable) {
        if (env instanceof And) {
            return ((And) env).getEnvs().stream().anyMatch(e -> effectContainsStateVariable(e, state_variable));
        } else if (env instanceof Effect) {
            return ((Effect) env).getValue().getStateVariable() == state_variable;
        } else {
            throw new UnsupportedOperationException(env.getClass().getName());
        }
    }
}
