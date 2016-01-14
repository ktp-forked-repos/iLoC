/*
 * Copyright (C) 2016 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>.
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
package it.cnr.istc.ac;

import it.cnr.istc.ac.api.IBoolVar;
import it.cnr.istc.ac.api.IEnumVar;
import it.cnr.istc.ac.api.IIntVar;
import it.cnr.istc.ac.api.IRealVar;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class ACNetworkTest {

    private static final Logger LOG = Logger.getLogger(ACNetworkTest.class.getName());
    private ACNetwork instance = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        this.instance = new ACNetwork(new Properties());
    }

    @After
    public void tearDown() {
        this.instance = null;
    }

    /**
     * Test of getNbVars method, of class ACNetwork.
     */
    @Test
    public void testGetNbVars() {
        LOG.info("getNbVars");
        int result = instance.getNbVars();
        assertEquals(0, result);

        IBoolVar bool = instance.newBool();
        assertNotNull(bool);
        result = instance.getNbVars();
        assertEquals(1, result);
    }

    /**
     * Test of getNbConstraints method, of class ACNetwork.
     */
    @Test
    public void testGetNbConstraints() {
        LOG.info("getNbConstraints");
        int result = instance.getNbConstraints();
        assertEquals(0, result);

        IBoolVar bool_0 = instance.newBool();
        IBoolVar bool_1 = instance.newBool();

        IBoolVar eq = instance.eq(bool_0, bool_1);
        assertNotNull(eq);

        instance.assertFacts(eq);

        result = instance.getNbConstraints();
        assertEquals(1, result);
    }

    /**
     * Test of newBool method, of class ACNetwork.
     */
    @Test
    public void testNewBool_0args() {
        LOG.info("newBool");
        IBoolVar result = instance.newBool();
        assertNotNull(result);

        assertTrue(result.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(result.getAllowedValues().contains(Boolean.FALSE));
    }

    /**
     * Test of newBool method, of class ACNetwork.
     */
    @Test
    public void testNewBool_String() {
        LOG.info("newBool");
        IBoolVar bool_0 = instance.newBool("true");
        assertNotNull(bool_0);

        assertTrue(bool_0.getAllowedValues().contains(Boolean.TRUE));
        assertFalse(bool_0.getAllowedValues().contains(Boolean.FALSE));

        IBoolVar bool_1 = instance.newBool("false");
        assertNotNull(bool_1);

        assertTrue(bool_1.getAllowedValues().contains(Boolean.FALSE));
        assertFalse(bool_1.getAllowedValues().contains(Boolean.TRUE));
    }

    /**
     * Test of not method, of class ACNetwork.
     */
    @Test
    public void testNot() {
        LOG.info("not");
        IBoolVar bool_0 = instance.newBool();
        assertNotNull(bool_0);

        assertTrue(bool_0.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_0.getAllowedValues().contains(Boolean.FALSE));

        IBoolVar not_0 = instance.not(bool_0);
        assertNotNull(not_0);

        assertTrue(not_0.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(not_0.getAllowedValues().contains(Boolean.FALSE));

        IBoolVar bool_1 = instance.newBool("true");
        assertNotNull(bool_1);

        assertTrue(bool_1.getAllowedValues().contains(Boolean.TRUE));
        assertFalse(bool_1.getAllowedValues().contains(Boolean.FALSE));

        IBoolVar not_1 = instance.not(bool_1);
        assertNotNull(not_1);

        assertTrue(not_1.getAllowedValues().contains(Boolean.FALSE));
        assertFalse(not_1.getAllowedValues().contains(Boolean.TRUE));

        int nb_constraints = instance.getNbConstraints();
        assertEquals(2, nb_constraints);
    }

    /**
     * Test of eq method, of class ACNetwork.
     */
    @Test
    public void testEq_IBoolVar_IBoolVar() {
        LOG.info("eq");
        int result = instance.getNbConstraints();
        assertEquals(0, result);

        IBoolVar bool_0 = instance.newBool();
        IBoolVar bool_1 = instance.newBool();

        IBoolVar eq_0 = instance.eq(bool_0, bool_1);
        assertNotNull(eq_0);

        instance.assertFacts(eq_0);

        result = instance.getNbConstraints();
        assertEquals(1, result);

        IBoolVar bool_2 = instance.newBool("true");
        IBoolVar bool_3 = instance.newBool();

        IBoolVar eq_1 = instance.eq(bool_2, bool_3);
        assertNotNull(eq_1);

        instance.assertFacts(eq_1);

        assertTrue(bool_2.getAllowedValues().contains(Boolean.TRUE));
        assertFalse(bool_2.getAllowedValues().contains(Boolean.FALSE));

        assertTrue(bool_3.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_3.getAllowedValues().contains(Boolean.FALSE));

        assertTrue(eq_1.getAllowedValues().contains(Boolean.TRUE));
        assertFalse(eq_1.getAllowedValues().contains(Boolean.FALSE));

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertTrue(bool_3.getAllowedValues().contains(Boolean.TRUE));
        assertFalse(bool_3.getAllowedValues().contains(Boolean.FALSE));
    }

    /**
     * Test of and method, of class ACNetwork.
     */
    @Test
    public void testAnd() {
        LOG.info("and");
        int result = instance.getNbConstraints();
        assertEquals(0, result);

        IBoolVar bool_0 = instance.newBool();
        IBoolVar bool_1 = instance.newBool();
        IBoolVar bool_2 = instance.newBool();

        IBoolVar and = instance.and(bool_0, bool_1, bool_2);
        assertNotNull(and);

        instance.assertFacts(and);

        assertTrue(bool_0.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_0.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_2.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_2.getAllowedValues().contains(Boolean.FALSE));

        assertTrue(and.getAllowedValues().contains(Boolean.TRUE));
        assertFalse(and.getAllowedValues().contains(Boolean.FALSE));

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertTrue(bool_0.getAllowedValues().contains(Boolean.TRUE));
        assertFalse(bool_0.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.TRUE));
        assertFalse(bool_1.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_2.getAllowedValues().contains(Boolean.TRUE));
        assertFalse(bool_2.getAllowedValues().contains(Boolean.FALSE));
    }

    /**
     * Test of or method, of class ACNetwork.
     */
    @Test
    public void testOr() {
        LOG.info("or");
        int result = instance.getNbConstraints();
        assertEquals(0, result);

        IBoolVar bool_0 = instance.newBool();
        IBoolVar bool_1 = instance.newBool();
        IBoolVar bool_2 = instance.newBool();

        IBoolVar or = instance.or(bool_0, bool_1, bool_2);
        assertNotNull(or);

        instance.assertFacts(or);

        assertTrue(bool_0.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_0.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_2.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_2.getAllowedValues().contains(Boolean.FALSE));

        assertTrue(or.getAllowedValues().contains(Boolean.TRUE));
        assertFalse(or.getAllowedValues().contains(Boolean.FALSE));

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertTrue(bool_0.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_0.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_2.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_2.getAllowedValues().contains(Boolean.FALSE));

        instance.assertFacts(
                instance.eq(bool_0, instance.newBool("false")),
                instance.eq(bool_1, instance.newBool("false"))
        );

        assertTrue(bool_0.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_0.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_2.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_2.getAllowedValues().contains(Boolean.FALSE));

        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(bool_0.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_0.getAllowedValues().contains(Boolean.FALSE));
        assertFalse(bool_1.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_2.getAllowedValues().contains(Boolean.TRUE));
        assertFalse(bool_2.getAllowedValues().contains(Boolean.FALSE));
    }

    /**
     * Test of xor method, of class ACNetwork.
     */
    @Test
    public void testXor() {
        LOG.info("xor");
        int result = instance.getNbConstraints();
        assertEquals(0, result);

        IBoolVar bool_0 = instance.newBool();
        IBoolVar bool_1 = instance.newBool();

        IBoolVar xor = instance.xor(bool_0, bool_1);
        assertNotNull(xor);

        instance.assertFacts(xor);

        assertTrue(bool_0.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_0.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.FALSE));

        assertTrue(xor.getAllowedValues().contains(Boolean.TRUE));
        assertFalse(xor.getAllowedValues().contains(Boolean.FALSE));

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertTrue(bool_0.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_0.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.FALSE));

        instance.assertFacts(
                instance.eq(bool_0, instance.newBool("true"))
        );

        assertTrue(bool_0.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_0.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.TRUE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.FALSE));

        propagate = instance.propagate();
        assertTrue(propagate);

        assertTrue(bool_0.getAllowedValues().contains(Boolean.TRUE));
        assertFalse(bool_0.getAllowedValues().contains(Boolean.FALSE));
        assertTrue(bool_1.getAllowedValues().contains(Boolean.FALSE));
        assertFalse(bool_1.getAllowedValues().contains(Boolean.TRUE));
    }

    /**
     * Test of newInt method, of class ACNetwork.
     */
    @Test
    public void testNewInt_0args() {
        LOG.info("newInt");
        IIntVar int_0 = instance.newInt();
        assertNotNull(int_0);
        IIntVar int_1 = instance.newInt();
        assertNotNull(int_1);

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertTrue(int_0.getLB().isNegativeInifinity());
        assertTrue(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());
    }

    /**
     * Test of newInt method, of class ACNetwork.
     */
    @Test
    public void testNewInt_String() {
        LOG.info("newInt");
        IIntVar int_0 = instance.newInt("0");
        assertNotNull(int_0);
        IIntVar int_1 = instance.newInt("10");
        assertNotNull(int_1);

        assertFalse(int_0.isEmpty());
        assertTrue(int_0.isSingleton());
        assertFalse(int_0.getLB().isNegativeInifinity());
        assertFalse(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());
        assertEquals(0, int_0.getLB().longValue());

        assertFalse(int_1.isEmpty());
        assertTrue(int_1.isSingleton());
        assertFalse(int_1.getLB().isNegativeInifinity());
        assertFalse(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());
        assertEquals(10, int_1.getLB().longValue());
    }

    /**
     * Test of add method, of class ACNetwork.
     */
    @Test
    public void testAdd_IIntVarArr() {
        LOG.info("add");
        IIntVar int_0 = instance.newInt();
        assertNotNull(int_0);
        IIntVar int_1 = instance.newInt();
        assertNotNull(int_1);

        IIntVar add_0 = instance.add(int_0, int_1);
        assertNotNull(add_0);

        assertFalse(add_0.isEmpty());
        assertFalse(add_0.isSingleton());
        assertTrue(add_0.getLB().isNegativeInifinity());
        assertTrue(add_0.getUB().isPositiveInifinity());
        assertFalse(add_0.getLB().isPositiveInifinity());
        assertFalse(add_0.getUB().isNegativeInifinity());

        IIntVar int_2 = instance.newInt("2");
        assertNotNull(int_2);
        IIntVar int_3 = instance.newInt("3");
        assertNotNull(int_3);

        IIntVar add_1 = instance.add(int_2, int_3);
        assertNotNull(add_1);

        assertFalse(add_1.isEmpty());
        assertTrue(add_1.isSingleton());
        assertFalse(add_1.getLB().isNegativeInifinity());
        assertFalse(add_1.getUB().isPositiveInifinity());
        assertFalse(add_1.getLB().isPositiveInifinity());
        assertFalse(add_1.getUB().isNegativeInifinity());
        assertEquals(5, add_1.getLB().longValue());

        IIntVar add_2 = instance.add(add_0, add_1);
        assertNotNull(add_2);

        assertFalse(add_2.isEmpty());
        assertFalse(add_2.isSingleton());
        assertTrue(add_2.getLB().isNegativeInifinity());
        assertTrue(add_2.getUB().isPositiveInifinity());
        assertFalse(add_2.getLB().isPositiveInifinity());
        assertFalse(add_2.getUB().isNegativeInifinity());
    }

    /**
     * Test of divide method, of class ACNetwork.
     */
    @Test
    public void testDivide_IIntVar_IIntVar() {
        LOG.info("divide");
        IIntVar int_0 = instance.newInt();
        assertNotNull(int_0);
        IIntVar int_1 = instance.newInt();
        assertNotNull(int_1);

        IIntVar divide_0 = instance.divide(instance.newInt("2"), int_0);
        assertNotNull(divide_0);

        assertFalse(divide_0.isEmpty());
        assertFalse(divide_0.isSingleton());
        assertTrue(divide_0.getLB().isNegativeInifinity());
        assertTrue(divide_0.getUB().isPositiveInifinity());
        assertFalse(divide_0.getLB().isPositiveInifinity());
        assertFalse(divide_0.getUB().isNegativeInifinity());

        instance.assertFacts(instance.geq(int_0, instance.newInt("-10")), instance.leq(int_0, instance.newInt("10")));
        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertEquals(-10, int_0.getLB().longValue());
        assertEquals(10, int_0.getUB().longValue());

        instance.assertFacts(instance.eq(divide_0, int_1));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());

        instance.assertFacts(instance.geq(int_0, instance.newInt("5")));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertFalse(int_1.getLB().isNegativeInifinity());
        assertFalse(int_1.getUB().isPositiveInifinity());
        assertEquals(0, int_1.getLB().longValue());
        assertEquals(1, int_1.getUB().longValue());
    }

    /**
     * Test of multiply method, of class ACNetwork.
     */
    @Test
    public void testMultiply_IIntVarArr() {
        LOG.info("multiply");
        IIntVar int_0 = instance.newInt();
        assertNotNull(int_0);
        IIntVar int_1 = instance.newInt();
        assertNotNull(int_1);

        IIntVar multiply_0 = instance.multiply(instance.newInt("2"), int_0);
        assertNotNull(multiply_0);

        assertFalse(multiply_0.isEmpty());
        assertFalse(multiply_0.isSingleton());
        assertTrue(multiply_0.getLB().isNegativeInifinity());
        assertTrue(multiply_0.getUB().isPositiveInifinity());
        assertFalse(multiply_0.getLB().isPositiveInifinity());
        assertFalse(multiply_0.getUB().isNegativeInifinity());

        instance.assertFacts(instance.geq(int_0, instance.newInt("-10")), instance.leq(int_0, instance.newInt("10")));
        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertEquals(-10, int_0.getLB().longValue());
        assertEquals(10, int_0.getUB().longValue());

        instance.assertFacts(instance.eq(multiply_0, int_1));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertEquals(-20, int_1.getLB().longValue());
        assertEquals(20, int_1.getUB().longValue());

        instance.assertFacts(instance.geq(int_0, instance.newInt("5")));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertFalse(int_1.getLB().isNegativeInifinity());
        assertFalse(int_1.getUB().isPositiveInifinity());
        assertEquals(10, int_1.getLB().longValue());
        assertEquals(20, int_1.getUB().longValue());
    }

    /**
     * Test of subtract method, of class ACNetwork.
     */
    @Test
    public void testSubtract_IIntVar_IIntVar() {
        LOG.info("subtract");
        IIntVar int_0 = instance.newInt();
        assertNotNull(int_0);

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertTrue(int_0.getLB().isNegativeInifinity());
        assertTrue(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());

        IIntVar int_1 = instance.newInt();
        assertNotNull(int_1);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());

        IIntVar sub = instance.subtract(int_0, int_1);
        assertNotNull(sub);
        assertFalse(sub.isEmpty());
        assertFalse(sub.isSingleton());
        assertTrue(sub.getLB().isNegativeInifinity());
        assertTrue(sub.getUB().isPositiveInifinity());
        assertFalse(sub.getLB().isPositiveInifinity());
        assertFalse(sub.getUB().isNegativeInifinity());

        IBoolVar eq = instance.eq(sub, instance.newInt("10"));
        assertNotNull(eq);

        instance.assertFacts(eq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(eq.isEmpty());
        assertTrue(eq.isSingleton());
        assertTrue(eq.getAllowedValues().contains(Boolean.TRUE));

        assertFalse(sub.isEmpty());
        assertTrue(sub.isSingleton());
        assertFalse(sub.getLB().isNegativeInifinity());
        assertFalse(sub.getUB().isPositiveInifinity());
        assertFalse(sub.getLB().isPositiveInifinity());
        assertFalse(sub.getUB().isNegativeInifinity());
        assertEquals(10, sub.getLB().longValue());
    }

    /**
     * Test of negate method, of class ACNetwork.
     */
    @Test
    public void testNegate_IIntVar() {
        LOG.info("negate");
        IIntVar int_0 = instance.newInt();
        assertNotNull(int_0);
        IIntVar int_1 = instance.newInt("-10");
        assertNotNull(int_1);

        IIntVar negated_val0 = instance.negate(int_0);
        assertNotNull(negated_val0);
        assertFalse(negated_val0.isEmpty());
        assertFalse(negated_val0.isSingleton());
        assertTrue(negated_val0.getLB().isNegativeInifinity());
        assertTrue(negated_val0.getUB().isPositiveInifinity());
        assertFalse(negated_val0.getLB().isPositiveInifinity());
        assertFalse(negated_val0.getUB().isNegativeInifinity());

        IBoolVar eq = instance.eq(negated_val0, int_1);
        assertNotNull(eq);

        instance.assertFacts(eq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(negated_val0.isEmpty());
        assertTrue(negated_val0.isSingleton());
        assertFalse(negated_val0.getLB().isNegativeInifinity());
        assertFalse(negated_val0.getUB().isPositiveInifinity());
        assertFalse(negated_val0.getLB().isPositiveInifinity());
        assertFalse(negated_val0.getUB().isNegativeInifinity());
        assertEquals(-10, negated_val0.getLB().longValue());

        assertFalse(int_0.isEmpty());
        assertTrue(int_0.isSingleton());
        assertFalse(int_0.getLB().isNegativeInifinity());
        assertFalse(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());
        assertEquals(10, int_0.getLB().longValue());
    }

    /**
     * Test of leq method, of class ACNetwork.
     */
    @Test
    public void testLeq_IIntVar_IIntVar() {
        LOG.info("leq");
        IIntVar int_0 = instance.newInt();
        assertNotNull(int_0);

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertTrue(int_0.getLB().isNegativeInifinity());
        assertTrue(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());

        IIntVar int_1 = instance.newInt();
        assertNotNull(int_1);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());

        IBoolVar leq = instance.leq(int_0, instance.newInt("10"));
        assertNotNull(leq);

        instance.assertFacts(leq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(leq.isEmpty());
        assertTrue(leq.isSingleton());
        assertTrue(leq.getAllowedValues().contains(Boolean.TRUE));

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertTrue(int_0.getLB().isNegativeInifinity());
        assertFalse(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());
        assertEquals(10, int_0.getUB().longValue());

        instance.assertFacts(instance.leq(int_1, int_0));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertFalse(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());
        assertEquals(10, int_1.getUB().longValue());
    }

    /**
     * Test of geq method, of class ACNetwork.
     */
    @Test
    public void testGeq_IIntVar_IIntVar() {
        LOG.info("geq");
        IIntVar int_0 = instance.newInt();
        assertNotNull(int_0);

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertTrue(int_0.getLB().isNegativeInifinity());
        assertTrue(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());

        IIntVar int_1 = instance.newInt();
        assertNotNull(int_1);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());

        IBoolVar geq = instance.geq(int_0, instance.newInt("10"));
        assertNotNull(geq);

        instance.assertFacts(geq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(geq.isEmpty());
        assertTrue(geq.isSingleton());
        assertTrue(geq.getAllowedValues().contains(Boolean.TRUE));

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertFalse(int_0.getLB().isNegativeInifinity());
        assertTrue(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());
        assertEquals(10, int_0.getLB().longValue());

        instance.assertFacts(instance.geq(int_1, int_0));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertFalse(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());
        assertEquals(10, int_1.getLB().longValue());
    }

    /**
     * Test of lt method, of class ACNetwork.
     */
    @Test
    public void testLt_IIntVar_IIntVar() {
        LOG.info("lt");
        IIntVar int_0 = instance.newInt();
        assertNotNull(int_0);

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertTrue(int_0.getLB().isNegativeInifinity());
        assertTrue(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());

        IIntVar int_1 = instance.newInt();
        assertNotNull(int_1);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());

        IBoolVar lt = instance.lt(int_0, instance.newInt("10"));
        assertNotNull(lt);

        instance.assertFacts(lt);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(lt.isEmpty());
        assertTrue(lt.isSingleton());
        assertTrue(lt.getAllowedValues().contains(Boolean.TRUE));

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertTrue(int_0.getLB().isNegativeInifinity());
        assertFalse(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());
        assertEquals(9, int_0.getUB().longValue());

        instance.assertFacts(instance.lt(int_1, int_0));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertFalse(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());
        assertEquals(8, int_1.getUB().longValue());
    }

    /**
     * Test of gt method, of class ACNetwork.
     */
    @Test
    public void testGt_IIntVar_IIntVar() {
        LOG.info("gt");
        IIntVar int_0 = instance.newInt();
        assertNotNull(int_0);

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertTrue(int_0.getLB().isNegativeInifinity());
        assertTrue(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());

        IIntVar int_1 = instance.newInt();
        assertNotNull(int_1);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());

        IBoolVar geq = instance.gt(int_0, instance.newInt("10"));
        assertNotNull(geq);

        instance.assertFacts(geq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(geq.isEmpty());
        assertTrue(geq.isSingleton());
        assertTrue(geq.getAllowedValues().contains(Boolean.TRUE));

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertFalse(int_0.getLB().isNegativeInifinity());
        assertTrue(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());
        assertEquals(11, int_0.getLB().longValue());

        instance.assertFacts(instance.gt(int_1, int_0));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertFalse(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());
        assertEquals(12, int_1.getLB().longValue());
    }

    /**
     * Test of eq method, of class ACNetwork.
     */
    @Test
    public void testEq_IIntVar_IIntVar() {
        LOG.info("eq");
        IIntVar int_0 = instance.newInt();
        assertNotNull(int_0);

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertTrue(int_0.getLB().isNegativeInifinity());
        assertTrue(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());

        IIntVar int_1 = instance.newInt();
        assertNotNull(int_1);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());

        IBoolVar eq = instance.eq(int_0, instance.newInt("10"));
        assertNotNull(eq);

        instance.assertFacts(eq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(eq.isEmpty());
        assertTrue(eq.isSingleton());
        assertTrue(eq.getAllowedValues().contains(Boolean.TRUE));

        assertFalse(int_0.isEmpty());
        assertTrue(int_0.isSingleton());
        assertFalse(int_0.getLB().isNegativeInifinity());
        assertFalse(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());
        assertEquals(10, int_0.getLB().longValue());

        instance.assertFacts(instance.eq(int_1, int_0));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(int_1.isEmpty());
        assertTrue(int_1.isSingleton());
        assertFalse(int_1.getLB().isNegativeInifinity());
        assertFalse(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());
        assertEquals(10, int_1.getLB().longValue());
    }

    /**
     * Test of newReal method, of class ACNetwork.
     */
    @Test
    public void testNewReal_0args() {
        LOG.info("newReal");
        IRealVar int_0 = instance.newReal();
        assertNotNull(int_0);
        IRealVar int_1 = instance.newReal();
        assertNotNull(int_1);

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertTrue(int_0.getLB().isNegativeInifinity());
        assertTrue(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());
    }

    /**
     * Test of newReal method, of class ACNetwork.
     */
    @Test
    public void testNewReal_String() {
        LOG.info("newReal");
        IRealVar int_0 = instance.newReal("0");
        assertNotNull(int_0);
        IRealVar int_1 = instance.newReal("10");
        assertNotNull(int_1);

        assertFalse(int_0.isEmpty());
        assertTrue(int_0.isSingleton());
        assertFalse(int_0.getLB().isNegativeInifinity());
        assertFalse(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());
        assertEquals(0, int_0.getLB().longValue());

        assertFalse(int_1.isEmpty());
        assertTrue(int_1.isSingleton());
        assertFalse(int_1.getLB().isNegativeInifinity());
        assertFalse(int_1.getUB().isPositiveInifinity());
        assertFalse(int_1.getLB().isPositiveInifinity());
        assertFalse(int_1.getUB().isNegativeInifinity());
        assertEquals(10, int_1.getLB().longValue());
    }

    /**
     * Test of add method, of class ACNetwork.
     */
    @Test
    public void testAdd_IRealVarArr() {
        LOG.info("add");
        IRealVar real_0 = instance.newReal();
        assertNotNull(real_0);
        IRealVar real_1 = instance.newReal();
        assertNotNull(real_1);

        IRealVar add_0 = instance.add(real_0, real_1);
        assertNotNull(add_0);

        assertFalse(add_0.isEmpty());
        assertFalse(add_0.isSingleton());
        assertTrue(add_0.getLB().isNegativeInifinity());
        assertTrue(add_0.getUB().isPositiveInifinity());
        assertFalse(add_0.getLB().isPositiveInifinity());
        assertFalse(add_0.getUB().isNegativeInifinity());

        IRealVar int_2 = instance.newReal("2");
        assertNotNull(int_2);
        IRealVar int_3 = instance.newReal("3");
        assertNotNull(int_3);

        IRealVar add_1 = instance.add(int_2, int_3);
        assertNotNull(add_1);

        assertFalse(add_1.isEmpty());
        assertTrue(add_1.isSingleton());
        assertFalse(add_1.getLB().isNegativeInifinity());
        assertFalse(add_1.getUB().isPositiveInifinity());
        assertFalse(add_1.getLB().isPositiveInifinity());
        assertFalse(add_1.getUB().isNegativeInifinity());
        assertEquals(5, add_1.getLB().longValue());

        IRealVar add_2 = instance.add(add_0, add_1);
        assertNotNull(add_2);

        assertFalse(add_2.isEmpty());
        assertFalse(add_2.isSingleton());
        assertTrue(add_2.getLB().isNegativeInifinity());
        assertTrue(add_2.getUB().isPositiveInifinity());
        assertFalse(add_2.getLB().isPositiveInifinity());
        assertFalse(add_2.getUB().isNegativeInifinity());
    }

    /**
     * Test of divide method, of class ACNetwork.
     */
    @Test
    public void testDivide_IRealVar_IRealVar() {
        LOG.info("divide");
        IRealVar real_0 = instance.newReal();
        assertNotNull(real_0);
        IRealVar real_1 = instance.newReal();
        assertNotNull(real_1);

        IRealVar divide_0 = instance.divide(instance.newReal("2"), real_0);
        assertNotNull(divide_0);

        assertFalse(divide_0.isEmpty());
        assertFalse(divide_0.isSingleton());
        assertTrue(divide_0.getLB().isNegativeInifinity());
        assertTrue(divide_0.getUB().isPositiveInifinity());
        assertFalse(divide_0.getLB().isPositiveInifinity());
        assertFalse(divide_0.getUB().isNegativeInifinity());

        instance.assertFacts(instance.geq(real_0, instance.newReal("-10")), instance.leq(real_0, instance.newReal("10")));
        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(real_0.isEmpty());
        assertFalse(real_0.isSingleton());
        assertEquals(-10, real_0.getLB().longValue());
        assertEquals(10, real_0.getUB().longValue());

        instance.assertFacts(instance.eq(divide_0, real_1));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertTrue(real_1.getLB().isNegativeInifinity());
        assertTrue(real_1.getUB().isPositiveInifinity());

        instance.assertFacts(instance.geq(real_0, instance.newReal("5")));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertFalse(real_1.getLB().isNegativeInifinity());
        assertFalse(real_1.getUB().isPositiveInifinity());
        assertEquals(0.2, real_1.getLB().doubleValue(), Double.MIN_NORMAL);
        assertEquals(0.4, real_1.getUB().doubleValue(), Double.MIN_NORMAL);
    }

    /**
     * Test of multiply method, of class ACNetwork.
     */
    @Test
    public void testMultiply_IRealVarArr() {
        LOG.info("multiply");
        IRealVar real_0 = instance.newReal();
        assertNotNull(real_0);
        IRealVar real_1 = instance.newReal();
        assertNotNull(real_1);

        IRealVar multiply_0 = instance.multiply(instance.newReal("2"), real_0);
        assertNotNull(multiply_0);

        assertFalse(multiply_0.isEmpty());
        assertFalse(multiply_0.isSingleton());
        assertTrue(multiply_0.getLB().isNegativeInifinity());
        assertTrue(multiply_0.getUB().isPositiveInifinity());
        assertFalse(multiply_0.getLB().isPositiveInifinity());
        assertFalse(multiply_0.getUB().isNegativeInifinity());

        instance.assertFacts(instance.geq(real_0, instance.newReal("-10")), instance.leq(real_0, instance.newReal("10")));
        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(real_0.isEmpty());
        assertFalse(real_0.isSingleton());
        assertEquals(-10, real_0.getLB().longValue());
        assertEquals(10, real_0.getUB().longValue());

        instance.assertFacts(instance.eq(multiply_0, real_1));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertEquals(-20, real_1.getLB().longValue());
        assertEquals(20, real_1.getUB().longValue());

        instance.assertFacts(instance.geq(real_0, instance.newReal("5")));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertFalse(real_1.getLB().isNegativeInifinity());
        assertFalse(real_1.getUB().isPositiveInifinity());
        assertEquals(10, real_1.getLB().longValue());
        assertEquals(20, real_1.getUB().longValue());
    }

    /**
     * Test of subtract method, of class ACNetwork.
     */
    @Test
    public void testSubtract_IRealVar_IRealVar() {
        LOG.info("subtract");
        IRealVar real_0 = instance.newReal();
        assertNotNull(real_0);

        assertFalse(real_0.isEmpty());
        assertFalse(real_0.isSingleton());
        assertTrue(real_0.getLB().isNegativeInifinity());
        assertTrue(real_0.getUB().isPositiveInifinity());
        assertFalse(real_0.getLB().isPositiveInifinity());
        assertFalse(real_0.getUB().isNegativeInifinity());

        IRealVar real_1 = instance.newReal();
        assertNotNull(real_1);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertTrue(real_1.getLB().isNegativeInifinity());
        assertTrue(real_1.getUB().isPositiveInifinity());
        assertFalse(real_1.getLB().isPositiveInifinity());
        assertFalse(real_1.getUB().isNegativeInifinity());

        IRealVar sub = instance.subtract(real_0, real_1);
        assertNotNull(sub);
        assertFalse(sub.isEmpty());
        assertFalse(sub.isSingleton());
        assertTrue(sub.getLB().isNegativeInifinity());
        assertTrue(sub.getUB().isPositiveInifinity());
        assertFalse(sub.getLB().isPositiveInifinity());
        assertFalse(sub.getUB().isNegativeInifinity());

        IBoolVar eq = instance.eq(sub, instance.newReal("10"));
        assertNotNull(eq);

        instance.assertFacts(eq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(eq.isEmpty());
        assertTrue(eq.isSingleton());
        assertTrue(eq.getAllowedValues().contains(Boolean.TRUE));

        assertFalse(sub.isEmpty());
        assertTrue(sub.isSingleton());
        assertFalse(sub.getLB().isNegativeInifinity());
        assertFalse(sub.getUB().isPositiveInifinity());
        assertFalse(sub.getLB().isPositiveInifinity());
        assertFalse(sub.getUB().isNegativeInifinity());
        assertEquals(10, sub.getLB().longValue());
    }

    /**
     * Test of negate method, of class ACNetwork.
     */
    @Test
    public void testNegate_IRealVar() {
        LOG.info("negate");
        IRealVar int_0 = instance.newReal();
        assertNotNull(int_0);
        IRealVar int_1 = instance.newReal("-10");
        assertNotNull(int_1);

        IRealVar negated_val0 = instance.negate(int_0);
        assertNotNull(negated_val0);
        assertFalse(negated_val0.isEmpty());
        assertFalse(negated_val0.isSingleton());
        assertTrue(negated_val0.getLB().isNegativeInifinity());
        assertTrue(negated_val0.getUB().isPositiveInifinity());
        assertFalse(negated_val0.getLB().isPositiveInifinity());
        assertFalse(negated_val0.getUB().isNegativeInifinity());

        IBoolVar eq = instance.eq(negated_val0, int_1);
        assertNotNull(eq);

        instance.assertFacts(eq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(negated_val0.isEmpty());
        assertTrue(negated_val0.isSingleton());
        assertFalse(negated_val0.getLB().isNegativeInifinity());
        assertFalse(negated_val0.getUB().isPositiveInifinity());
        assertFalse(negated_val0.getLB().isPositiveInifinity());
        assertFalse(negated_val0.getUB().isNegativeInifinity());
        assertEquals(-10, negated_val0.getLB().longValue());

        assertFalse(int_0.isEmpty());
        assertTrue(int_0.isSingleton());
        assertFalse(int_0.getLB().isNegativeInifinity());
        assertFalse(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());
        assertEquals(10, int_0.getLB().longValue());
    }

    /**
     * Test of leq method, of class ACNetwork.
     */
    @Test
    public void testLeq_IRealVar_IRealVar() {
        LOG.info("leq");
        IRealVar real_0 = instance.newReal();
        assertNotNull(real_0);

        assertFalse(real_0.isEmpty());
        assertFalse(real_0.isSingleton());
        assertTrue(real_0.getLB().isNegativeInifinity());
        assertTrue(real_0.getUB().isPositiveInifinity());
        assertFalse(real_0.getLB().isPositiveInifinity());
        assertFalse(real_0.getUB().isNegativeInifinity());

        IRealVar real_1 = instance.newReal();
        assertNotNull(real_1);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertTrue(real_1.getLB().isNegativeInifinity());
        assertTrue(real_1.getUB().isPositiveInifinity());
        assertFalse(real_1.getLB().isPositiveInifinity());
        assertFalse(real_1.getUB().isNegativeInifinity());

        IBoolVar leq = instance.leq(real_0, instance.newReal("10"));
        assertNotNull(leq);

        instance.assertFacts(leq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(leq.isEmpty());
        assertTrue(leq.isSingleton());
        assertTrue(leq.getAllowedValues().contains(Boolean.TRUE));

        assertFalse(real_0.isEmpty());
        assertFalse(real_0.isSingleton());
        assertTrue(real_0.getLB().isNegativeInifinity());
        assertFalse(real_0.getUB().isPositiveInifinity());
        assertFalse(real_0.getLB().isPositiveInifinity());
        assertFalse(real_0.getUB().isNegativeInifinity());
        assertEquals(10, real_0.getUB().longValue());

        instance.assertFacts(instance.leq(real_1, real_0));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertTrue(real_1.getLB().isNegativeInifinity());
        assertFalse(real_1.getUB().isPositiveInifinity());
        assertFalse(real_1.getLB().isPositiveInifinity());
        assertFalse(real_1.getUB().isNegativeInifinity());
        assertEquals(10, real_1.getUB().longValue());
    }

    /**
     * Test of geq method, of class ACNetwork.
     */
    @Test
    public void testGeq_IRealVar_IRealVar() {
        LOG.info("geq");
        IRealVar real_0 = instance.newReal();
        assertNotNull(real_0);

        assertFalse(real_0.isEmpty());
        assertFalse(real_0.isSingleton());
        assertTrue(real_0.getLB().isNegativeInifinity());
        assertTrue(real_0.getUB().isPositiveInifinity());
        assertFalse(real_0.getLB().isPositiveInifinity());
        assertFalse(real_0.getUB().isNegativeInifinity());

        IRealVar real_1 = instance.newReal();
        assertNotNull(real_1);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertTrue(real_1.getLB().isNegativeInifinity());
        assertTrue(real_1.getUB().isPositiveInifinity());
        assertFalse(real_1.getLB().isPositiveInifinity());
        assertFalse(real_1.getUB().isNegativeInifinity());

        IBoolVar geq = instance.geq(real_0, instance.newReal("10"));
        assertNotNull(geq);

        instance.assertFacts(geq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(geq.isEmpty());
        assertTrue(geq.isSingleton());
        assertTrue(geq.getAllowedValues().contains(Boolean.TRUE));

        assertFalse(real_0.isEmpty());
        assertFalse(real_0.isSingleton());
        assertFalse(real_0.getLB().isNegativeInifinity());
        assertTrue(real_0.getUB().isPositiveInifinity());
        assertFalse(real_0.getLB().isPositiveInifinity());
        assertFalse(real_0.getUB().isNegativeInifinity());
        assertEquals(10, real_0.getLB().longValue());

        instance.assertFacts(instance.geq(real_1, real_0));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertFalse(real_1.getLB().isNegativeInifinity());
        assertTrue(real_1.getUB().isPositiveInifinity());
        assertFalse(real_1.getLB().isPositiveInifinity());
        assertFalse(real_1.getUB().isNegativeInifinity());
        assertEquals(10, real_1.getLB().longValue());
    }

    /**
     * Test of lt method, of class ACNetwork.
     */
    @Test
    public void testLt_IRealVar_IRealVar() {
        LOG.info("lt");
        IRealVar real_0 = instance.newReal();
        assertNotNull(real_0);

        assertFalse(real_0.isEmpty());
        assertFalse(real_0.isSingleton());
        assertTrue(real_0.getLB().isNegativeInifinity());
        assertTrue(real_0.getUB().isPositiveInifinity());
        assertFalse(real_0.getLB().isPositiveInifinity());
        assertFalse(real_0.getUB().isNegativeInifinity());

        IRealVar real_1 = instance.newReal();
        assertNotNull(real_1);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertTrue(real_1.getLB().isNegativeInifinity());
        assertTrue(real_1.getUB().isPositiveInifinity());
        assertFalse(real_1.getLB().isPositiveInifinity());
        assertFalse(real_1.getUB().isNegativeInifinity());

        IBoolVar lt = instance.lt(real_0, instance.newReal("10"));
        assertNotNull(lt);

        instance.assertFacts(lt);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(lt.isEmpty());
        assertTrue(lt.isSingleton());
        assertTrue(lt.getAllowedValues().contains(Boolean.TRUE));

        assertFalse(real_0.isEmpty());
        assertFalse(real_0.isSingleton());
        assertTrue(real_0.getLB().isNegativeInifinity());
        assertFalse(real_0.getUB().isPositiveInifinity());
        assertFalse(real_0.getLB().isPositiveInifinity());
        assertFalse(real_0.getUB().isNegativeInifinity());
        assertEquals(10, real_0.getUB().longValue());

        instance.assertFacts(instance.lt(real_1, real_0));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertTrue(real_1.getLB().isNegativeInifinity());
        assertFalse(real_1.getUB().isPositiveInifinity());
        assertFalse(real_1.getLB().isPositiveInifinity());
        assertFalse(real_1.getUB().isNegativeInifinity());
        assertEquals(10, real_1.getUB().longValue());
    }

    /**
     * Test of gt method, of class ACNetwork.
     */
    @Test
    public void testGt_IRealVar_IRealVar() {
        LOG.info("gt");
        IRealVar real_0 = instance.newReal();
        assertNotNull(real_0);

        assertFalse(real_0.isEmpty());
        assertFalse(real_0.isSingleton());
        assertTrue(real_0.getLB().isNegativeInifinity());
        assertTrue(real_0.getUB().isPositiveInifinity());
        assertFalse(real_0.getLB().isPositiveInifinity());
        assertFalse(real_0.getUB().isNegativeInifinity());

        IRealVar real_1 = instance.newReal();
        assertNotNull(real_1);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertTrue(real_1.getLB().isNegativeInifinity());
        assertTrue(real_1.getUB().isPositiveInifinity());
        assertFalse(real_1.getLB().isPositiveInifinity());
        assertFalse(real_1.getUB().isNegativeInifinity());

        IBoolVar geq = instance.gt(real_0, instance.newReal("10"));
        assertNotNull(geq);

        instance.assertFacts(geq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(geq.isEmpty());
        assertTrue(geq.isSingleton());
        assertTrue(geq.getAllowedValues().contains(Boolean.TRUE));

        assertFalse(real_0.isEmpty());
        assertFalse(real_0.isSingleton());
        assertFalse(real_0.getLB().isNegativeInifinity());
        assertTrue(real_0.getUB().isPositiveInifinity());
        assertFalse(real_0.getLB().isPositiveInifinity());
        assertFalse(real_0.getUB().isNegativeInifinity());
        assertEquals(10, real_0.getLB().longValue());

        instance.assertFacts(instance.gt(real_1, real_0));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertFalse(real_1.getLB().isNegativeInifinity());
        assertTrue(real_1.getUB().isPositiveInifinity());
        assertFalse(real_1.getLB().isPositiveInifinity());
        assertFalse(real_1.getUB().isNegativeInifinity());
        assertEquals(10, real_1.getLB().longValue());
    }

    /**
     * Test of eq method, of class ACNetwork.
     */
    @Test
    public void testEq_IRealVar_IRealVar() {
        LOG.info("eq");
        IRealVar real_0 = instance.newReal();
        assertNotNull(real_0);

        assertFalse(real_0.isEmpty());
        assertFalse(real_0.isSingleton());
        assertTrue(real_0.getLB().isNegativeInifinity());
        assertTrue(real_0.getUB().isPositiveInifinity());
        assertFalse(real_0.getLB().isPositiveInifinity());
        assertFalse(real_0.getUB().isNegativeInifinity());

        IRealVar real_1 = instance.newReal();
        assertNotNull(real_1);

        assertFalse(real_1.isEmpty());
        assertFalse(real_1.isSingleton());
        assertTrue(real_1.getLB().isNegativeInifinity());
        assertTrue(real_1.getUB().isPositiveInifinity());
        assertFalse(real_1.getLB().isPositiveInifinity());
        assertFalse(real_1.getUB().isNegativeInifinity());

        IBoolVar eq = instance.eq(real_0, instance.newReal("10"));
        assertNotNull(eq);

        instance.assertFacts(eq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(eq.isEmpty());
        assertTrue(eq.isSingleton());
        assertTrue(eq.getAllowedValues().contains(Boolean.TRUE));

        assertFalse(real_0.isEmpty());
        assertTrue(real_0.isSingleton());
        assertFalse(real_0.getLB().isNegativeInifinity());
        assertFalse(real_0.getUB().isPositiveInifinity());
        assertFalse(real_0.getLB().isPositiveInifinity());
        assertFalse(real_0.getUB().isNegativeInifinity());
        assertEquals(10, real_0.getLB().longValue());

        instance.assertFacts(instance.eq(real_1, real_0));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(real_1.isEmpty());
        assertTrue(real_1.isSingleton());
        assertFalse(real_1.getLB().isNegativeInifinity());
        assertFalse(real_1.getUB().isPositiveInifinity());
        assertFalse(real_1.getLB().isPositiveInifinity());
        assertFalse(real_1.getUB().isNegativeInifinity());
        assertEquals(10, real_1.getLB().longValue());
    }

    /**
     * Test of toReal method, of class ACNetwork.
     */
    @Test
    public void testToReal() {
        LOG.info("toReal");
        IIntVar int_0 = instance.newInt();
        assertNotNull(int_0);
        IRealVar real_0 = instance.newReal("-10");
        assertNotNull(real_0);

        IRealVar to_real = instance.toReal(int_0);
        assertNotNull(to_real);
        assertFalse(to_real.isEmpty());
        assertFalse(to_real.isSingleton());
        assertTrue(to_real.getLB().isNegativeInifinity());
        assertTrue(to_real.getUB().isPositiveInifinity());
        assertFalse(to_real.getLB().isPositiveInifinity());
        assertFalse(to_real.getUB().isNegativeInifinity());

        IBoolVar eq = instance.eq(to_real, real_0);
        assertNotNull(eq);

        instance.assertFacts(eq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(to_real.isEmpty());
        assertTrue(to_real.isSingleton());
        assertFalse(to_real.getLB().isNegativeInifinity());
        assertFalse(to_real.getUB().isPositiveInifinity());
        assertFalse(to_real.getLB().isPositiveInifinity());
        assertFalse(to_real.getUB().isNegativeInifinity());
        assertEquals(-10, to_real.getLB().longValue());

        assertFalse(int_0.isEmpty());
        assertTrue(int_0.isSingleton());
        assertFalse(int_0.getLB().isNegativeInifinity());
        assertFalse(int_0.getUB().isPositiveInifinity());
        assertFalse(int_0.getLB().isPositiveInifinity());
        assertFalse(int_0.getUB().isNegativeInifinity());
        assertEquals(-10, int_0.getLB().longValue());
    }

    /**
     * Test of toInt method, of class ACNetwork.
     */
    @Test
    public void testToInt() {
        LOG.info("toInt");
        IRealVar real_0 = instance.newReal();
        assertNotNull(real_0);
        IIntVar int_0 = instance.newInt("-10");
        assertNotNull(int_0);

        IIntVar to_int = instance.toInt(real_0);
        assertNotNull(to_int);
        assertFalse(to_int.isEmpty());
        assertFalse(to_int.isSingleton());
        assertTrue(to_int.getLB().isNegativeInifinity());
        assertTrue(to_int.getUB().isPositiveInifinity());
        assertFalse(to_int.getLB().isPositiveInifinity());
        assertFalse(to_int.getUB().isNegativeInifinity());

        IBoolVar eq = instance.eq(to_int, int_0);
        assertNotNull(eq);

        instance.assertFacts(eq);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(to_int.isEmpty());
        assertTrue(to_int.isSingleton());
        assertFalse(to_int.getLB().isNegativeInifinity());
        assertFalse(to_int.getUB().isPositiveInifinity());
        assertFalse(to_int.getLB().isPositiveInifinity());
        assertFalse(to_int.getUB().isNegativeInifinity());
        assertEquals(-10, to_int.getLB().longValue());

        assertFalse(real_0.isEmpty());
        assertTrue(real_0.isSingleton());
        assertFalse(real_0.getLB().isNegativeInifinity());
        assertFalse(real_0.getUB().isPositiveInifinity());
        assertFalse(real_0.getLB().isPositiveInifinity());
        assertFalse(real_0.getUB().isNegativeInifinity());
        assertEquals(-10, real_0.getLB().longValue());
    }

    /**
     * Test of newEnum method, of class ACNetwork.
     */
    @Test
    public void testNewEnum() {
        LOG.info("newEnum");
        String a = new String("a");
        String b = new String("b");
        String c = new String("c");
        IEnumVar<Object> enum_0 = instance.newEnum(Arrays.asList(a, b, c));
        assertNotNull(enum_0);
        assertTrue(enum_0.getAllowedValues().contains(a));
        assertTrue(enum_0.getAllowedValues().contains(b));
        assertTrue(enum_0.getAllowedValues().contains(c));
    }

    /**
     * Test of eq method, of class ACNetwork.
     */
    @Test
    public void testEq_IEnumVar_IEnumVar() {
        LOG.info("eq");
        String a = new String("a");
        String b = new String("b");
        String c = new String("c");
        IEnumVar<Object> enum_0 = instance.newEnum(Arrays.asList(a, b, c));
        IEnumVar<Object> enum_1 = instance.newEnum(Arrays.asList(a, b, c));

        IBoolVar eq_0 = instance.eq(enum_0, enum_1);
        assertNotNull(eq_0);

        instance.assertFacts(eq_0);

        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertTrue(enum_0.getAllowedValues().contains(a));
        assertTrue(enum_0.getAllowedValues().contains(b));
        assertTrue(enum_0.getAllowedValues().contains(c));

        assertTrue(enum_1.getAllowedValues().contains(a));
        assertTrue(enum_1.getAllowedValues().contains(b));
        assertTrue(enum_1.getAllowedValues().contains(c));

        IEnumVar<Object> enum_2 = instance.newEnum(Arrays.asList(a));

        IBoolVar eq_1 = instance.eq(enum_1, enum_2);
        assertNotNull(eq_1);

        instance.assertFacts(eq_1);

        propagate = instance.propagate();
        assertTrue(propagate);

        assertTrue(enum_0.getAllowedValues().contains(a));
        assertFalse(enum_0.getAllowedValues().contains(b));
        assertFalse(enum_0.getAllowedValues().contains(c));

        assertTrue(enum_1.getAllowedValues().contains(a));
        assertFalse(enum_1.getAllowedValues().contains(b));
        assertFalse(enum_1.getAllowedValues().contains(c));

        IEnumVar<Object> enum_3 = instance.newEnum(Arrays.asList(a, b));
        IEnumVar<Object> enum_4 = instance.newEnum(Arrays.asList(b, c));

        instance.assertFacts(instance.not(instance.eq(enum_3, enum_4)));

        propagate = instance.propagate();
        assertTrue(propagate);

        assertTrue(enum_3.isSingleton());
        assertTrue(enum_3.getAllowedValues().contains(a));
        assertTrue(enum_4.isSingleton());
        assertTrue(enum_4.getAllowedValues().contains(c));
    }

    /**
     * Test of push and pop methods, of class ACNetwork.
     */
    @Test
    public void testIncrementalNetwork() {
        LOG.info("incremental");
        IIntVar int_0 = instance.newInt();
        IIntVar int_1 = instance.newInt();
        instance.assertFacts(
                instance.geq(int_0, instance.newInt("-10")),
                instance.leq(int_0, instance.newInt("10"))
        );
        boolean propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(int_0.isEmpty());
        assertFalse(int_0.isSingleton());
        assertEquals(-10, int_0.getLB().longValue());
        assertEquals(10, int_0.getUB().longValue());

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());

        instance.push();
        instance.assertFacts(instance.eq(int_0, int_1));
        propagate = instance.propagate();
        assertTrue(propagate);

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertEquals(-10, int_1.getLB().longValue());
        assertEquals(10, int_1.getUB().longValue());

        instance.pop();

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());

        instance.checkFacts(instance.eq(int_0, int_1));

        assertFalse(int_1.isEmpty());
        assertFalse(int_1.isSingleton());
        assertTrue(int_1.getLB().isNegativeInifinity());
        assertTrue(int_1.getUB().isPositiveInifinity());
    }
}
