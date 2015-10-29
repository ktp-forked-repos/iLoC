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
public class EnvRenderer implements AttributeRenderer {

    private final STGroupFile file;

    public EnvRenderer(STGroupFile file) {
        this.file = file;
    }

    @Override
    public String toString(Object o, String string, Locale locale) {
        Env env = (Env) o;
        if (env instanceof AND) {
            ST translation = file.getInstanceOf("And");
            translation.add("and", env);
            return translation.render();
        } else if (env instanceof OR) {
            assert ((OR) env).getEnvs().size() > 1;
            ST translation = file.getInstanceOf("Or");
            translation.add("or", env);
            return translation.render();
        }
        return env.toString();
    }
}
