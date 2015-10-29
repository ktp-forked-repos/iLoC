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

import it.cnr.istc.iloc.translators.pddl.parser.Domain;
import it.cnr.istc.iloc.translators.pddl.parser.Problem;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class PDDLTranslator {

    public static String translatePDDLInstance(Domain domain, Problem problem) {
        Grounder grounder = new Grounder(domain, problem);
        STGroupFile file = new STGroupFile(PDDLTranslator.class.getResource("PDDLTemplate.stg").getPath());
        file.registerRenderer(String.class, new StringRenderer());
        file.registerRenderer(StateVariable.class, new StateVariableRenderer(file, grounder));
        file.registerRenderer(GroundAction.class, new ActionRenderer(file, grounder));

        ST translation = file.getInstanceOf("PDDL");
        translation.add("grounder", grounder);

        return translation.render();
    }

    private PDDLTranslator() {
    }
}
