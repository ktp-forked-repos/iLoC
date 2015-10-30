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
package it.cnr.istc.iloc.translators.pddl;

import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class TranslatorTest {

    /**
     * Test of blocks world domain.
     */
    @Test
    public void testBlocksWorldTranslator() {
        try {
            PDDLTranslator translator = new PDDLTranslator(new File(TranslatorTest.class.getResource("blocks-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("blocks-problem.pddl").getPath()));
            String translation = translator.translate();
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test of new blocks world domain.
     */
    @Test
    public void testNewBlocksWorldTranslator() {
        try {
            PDDLTranslator translator = new PDDLTranslator(new File(TranslatorTest.class.getResource("blocks-domain-new.pddl").getPath()), new File(TranslatorTest.class.getResource("blocks-problem-new.pddl").getPath()));
            String translation = translator.translate();
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
