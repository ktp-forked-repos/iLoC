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
import java.text.DecimalFormat;
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
    public void testBlocksWorldParametricTranslator() {
        try {
            String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("blocks/blocks-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("blocks/blocks-problem.pddl").getPath()), false);
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test of blocks world domain.
     */
    @Test
    public void testBlocksWorldGroundTranslator() {
        try {
            String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("blocks/blocks-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("blocks/blocks-problem.pddl").getPath()), true);
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test of new blocks world domain.
     */
    @Test
    public void testNewBlocksWorldParametricTranslator() {
        try {
            String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("blocks/blocks-domain-new.pddl").getPath()), new File(TranslatorTest.class.getResource("blocks/blocks-problem-new.pddl").getPath()), false);
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test of new blocks world domain.
     */
    @Test
    public void testNewBlocksWorldGroundTranslator() {
        try {
            String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("blocks/blocks-domain-new.pddl").getPath()), new File(TranslatorTest.class.getResource("blocks/blocks-problem-new.pddl").getPath()), true);
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test of depots domain.
     */
    @Test
    public void testDepotsTranslator() {
        try {
            String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("depots/depots-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("depots/depots-problem.pddl").getPath()), false);
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test of new depots domain.
     */
    @Test
    public void testNewDepotsTranslator() {
        try {
            String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("depots/depots-domain-new.pddl").getPath()), new File(TranslatorTest.class.getResource("depots/depots-problem-new.pddl").getPath()), false);
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test of logistics domain.
     */
    @Test
    public void testLogisticsTranslator() {
        try {
            String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("logistics/logistics-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("logistics/logistics-problem.pddl").getPath()), false);
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test of new logistics domain.
     */
    @Test
    public void testNewLogisticsTranslator() {
        try {
            String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("logistics/logistics-domain-new.pddl").getPath()), new File(TranslatorTest.class.getResource("logistics/logistics-problem-new.pddl").getPath()), false);
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testTemporalCrewPlanningStripsTranslator() {
        try {
            for (int i = 1; i <= 30; i++) {
                String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("temporal/crewplanning_strips/p" + new DecimalFormat("00").format(i) + "-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("temporal/crewplanning_strips/p" + new DecimalFormat("00").format(i) + ".pddl").getPath()), false);
            }
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testTemporalElevatorsNumericTranslator() {
        try {
            for (int i = 1; i <= 30; i++) {
                String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("temporal/elevators_numeric/p" + new DecimalFormat("00").format(i) + "-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("temporal/elevators_numeric/p" + new DecimalFormat("00").format(i) + ".pddl").getPath()), false);
            }
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testTemporalElevatorsStripsTranslator() {
        try {
            for (int i = 1; i <= 30; i++) {
                String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("temporal/elevators_strips/p" + new DecimalFormat("00").format(i) + "-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("temporal/elevators_strips/p" + new DecimalFormat("00").format(i) + ".pddl").getPath()), false);
            }
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testTemporalModelTrainNumericTranslator() {
        try {
            for (int i = 1; i <= 30; i++) {
                String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("temporal/modeltrain_numeric/p" + new DecimalFormat("00").format(i) + "-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("temporal/modeltrain_numeric/p" + new DecimalFormat("00").format(i) + ".pddl").getPath()), false);
            }
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testOpenstacksADLTranslator() {
        try {
            for (int i = 1; i <= 30; i++) {
                String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("temporal/openstacks_adl/p" + new DecimalFormat("00").format(i) + "-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("temporal/openstacks_adl/p" + new DecimalFormat("00").format(i) + ".pddl").getPath()), false);
            }
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testOpenstacksNumericTranslator() {
        try {
            for (int i = 1; i <= 30; i++) {
                String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("temporal/openstacks_numeric/p" + new DecimalFormat("00").format(i) + "-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("temporal/openstacks_numeric/p" + new DecimalFormat("00").format(i) + ".pddl").getPath()), false);
            }
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testOpenstacksNumericADLTranslator() {
        try {
            for (int i = 1; i <= 30; i++) {
                String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("temporal/openstacks_numericadl/p" + new DecimalFormat("00").format(i) + "-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("temporal/openstacks_numericadl/p" + new DecimalFormat("00").format(i) + ".pddl").getPath()), false);
            }
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testOpenstacksStripsTranslator() {
        try {
            for (int i = 1; i <= 30; i++) {
                String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("temporal/openstacks_strips/p" + new DecimalFormat("00").format(i) + "-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("temporal/openstacks_strips/p" + new DecimalFormat("00").format(i) + ".pddl").getPath()), false);
            }
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testParcprinterStripsTranslator() {
        try {
            for (int i = 1; i <= 30; i++) {
                String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("temporal/parcprinter_strips/p" + new DecimalFormat("00").format(i) + "-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("temporal/parcprinter_strips/p" + new DecimalFormat("00").format(i) + ".pddl").getPath()), false);
            }
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testPegsolStripsTranslator() {
        try {
            for (int i = 1; i <= 30; i++) {
                String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("temporal/pegsol_strips/p" + new DecimalFormat("00").format(i) + "-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("temporal/pegsol_strips/p" + new DecimalFormat("00").format(i) + ".pddl").getPath()), false);
            }
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testSokobanStripsTranslator() {
        try {
            for (int i = 1; i <= 30; i++) {
                String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("temporal/sokoban_strips/p" + new DecimalFormat("00").format(i) + "-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("temporal/sokoban_strips/p" + new DecimalFormat("00").format(i) + ".pddl").getPath()), false);
            }
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testTransportNumericTranslator() {
        try {
            for (int i = 1; i <= 30; i++) {
                String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("temporal/transport_numeric/p" + new DecimalFormat("00").format(i) + "-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("temporal/transport_numeric/p" + new DecimalFormat("00").format(i) + ".pddl").getPath()), false);
            }
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testWoodworkingNumericTranslator() {
        try {
            for (int i = 1; i <= 30; i++) {
                String translation = PDDLTranslator.translatePDDLInstance(new File(TranslatorTest.class.getResource("temporal/woodworking_numeric/p" + new DecimalFormat("00").format(i) + "-domain.pddl").getPath()), new File(TranslatorTest.class.getResource("temporal/woodworking_numeric/p" + new DecimalFormat("00").format(i) + ".pddl").getPath()), false);
            }
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
