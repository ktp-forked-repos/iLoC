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
package it.cnr.istc.iloc.translators.pddl.parser;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class ParserTest {

    /**
     * Test of blocks world domain.
     */
    @Test
    public void testBlocksWorldInvariantSynthesisTranslator() {
        try {
            PDDLInstance instance = Parser.parse(new File(ParserTest.class.getResource("blocks-domain.pddl").getPath()), new File(ParserTest.class.getResource("blocks-problem.pddl").getPath()));
            Collection<Invariant> invariants = instance.getDomain().getInvariants();
            invariants.forEach(invariant -> {
                System.out.println(invariant);
            });
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test of logistics domain.
     */
    @Test
    public void testLogisticsInvariantSynthesisTranslator() {
        try {
            PDDLInstance instance = Parser.parse(new File(ParserTest.class.getResource("logistics-domain.pddl").getPath()), new File(ParserTest.class.getResource("logistics-problem.pddl").getPath()));
            Collection<Invariant> invariants = instance.getDomain().getInvariants();
            invariants.forEach(invariant -> {
                System.out.println(invariant);
            });
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
