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
package it.cnr.istc.iloc;

import it.cnr.istc.iloc.api.IConstraintNetwork;
import it.cnr.istc.iloc.api.IEnum;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IPredicate;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class SolverTest {

    /**
     * Test of new Solver.
     */
    @Test
    public void testNewSolver() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);
    }

    /**
     * Test of read method of class Solver.
     */
    @Test
    public void testDDLParser() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        solver.read("real a; a == 0; real b; b == 5;");

        IObject a = solver.get("a");
        assertNotNull(a);

        IObject b = solver.get("b");
        assertNotNull(b);

        boolean solve = solver.solve();
        assertTrue(solve);

        IModel model = solver.getConstraintNetwork().getModel();
        assertNotNull(model);

        assertEquals(0, model.evaluate((INumber) a).doubleValue(), 0);

        assertEquals(5, model.evaluate((INumber) b).doubleValue(), 0);
    }

    @Test
    public void testPredicate() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        solver.read("predicate P() {}");

        try {
            IPredicate p = solver.getPredicate("P");
            assertNotNull(p);
        } catch (ClassNotFoundException ex) {
            fail();
        }
    }

    @Test
    public void testStateVariable() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        solver.read("class SV extends StateVariable {}");

        try {
            IType sv = solver.getType("SV");
            assertNotNull(sv);
        } catch (ClassNotFoundException ex) {
            fail();
        }
    }

    /**
     * We assume that different instances of the same type can be differentiated
     * only by their properties. For this reason, two different instances of the
     * same type without any property are considered equal.
     */
    @Test
    public void test00() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        solver.read(""
                + "class Location {\n"
                + "}\n"
                + "\n"
                + "Location city_a = new Location();\n"
                + "Location city_b = new Location();\n"
                + "\n"
                + "city_a == city_b;");

        boolean solve = solver.solve();
        assertTrue(solve);

        IModel model = solver.getConstraintNetwork().getModel();
        assertNotNull(model);

        assertFalse(model.evaluate(solver.getConstraintNetwork().not(solver.get("city_a").eq(solver.get("city_b")))));
        assertTrue(model.evaluate(solver.get("city_a").eq(solver.get("city_b"))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test01() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        solver.read("real x; real y; x > y || x < y;");

        boolean solve = solver.solve();
        assertTrue(solve);

        IModel model = solver.getConstraintNetwork().getModel();
        assertNotNull(model);

        INumber x = solver.get("x");
        assertNotNull(x);

        INumber y = solver.get("y");
        assertNotNull(y);

        Number x_value = model.evaluate(x);
        Number y_value = model.evaluate(y);
        assertTrue(((Comparable<Number>) x_value).compareTo(y_value) != 0);

        assertTrue(model.evaluate(solver.getConstraintNetwork().not(solver.getConstraintNetwork().eq(x, y))));
    }

    @Test
    public void test02() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        try {
            solver.read(new File(SolverTest.class.getResource("test02.iloc").getPath()));
        } catch (IOException ex) {
            Logger.getLogger(SolverTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean solve = solver.solve();
        assertTrue(solve);

        IModel model = solver.getConstraintNetwork().getModel();
        assertNotNull(model);

        INumber dist_ab = solver.get("dist_ab").get("distance");
        assertNotNull(dist_ab);
        assertTrue(model.evaluate(solver.getConstraintNetwork().eq(dist_ab, solver.getConstraintNetwork().newInt("600"))));

        INumber dist_ac = solver.get("dist_ac").get("distance");
        assertNotNull(dist_ac);
        assertTrue(model.evaluate(solver.getConstraintNetwork().eq(dist_ac, solver.getConstraintNetwork().newInt("1000"))));

        INumber dist_bc = solver.get("dist_bc").get("distance");
        assertNotNull(dist_bc);
        assertTrue(model.evaluate(solver.getConstraintNetwork().eq(dist_bc, solver.getConstraintNetwork().newInt("800"))));
    }

    @Test
    public void test03() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        try {
            solver.read(new File(SolverTest.class.getResource("test03.iloc").getPath()));
        } catch (IOException ex) {
            Logger.getLogger(SolverTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean solve = solver.solve();
        assertTrue(solve);

        IModel model = solver.getConstraintNetwork().getModel();
        assertNotNull(model);

        IEnum enum_0 = solver.get("enum_0");
        IEnum enum_1 = solver.get("enum_1");

        IConstraintNetwork network = solver.getConstraintNetwork();
        assertTrue(model.evaluate(network.not(network.eq(enum_0.getVar(), enum_1.getVar()))));
    }

    @Test
    public void test04_0() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        try {
            solver.read(new File(SolverTest.class.getResource("test04.iloc").getPath()));
        } catch (IOException ex) {
            Logger.getLogger(SolverTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean solve = solver.solve();
        assertTrue(solve);

        IModel model = solver.getConstraintNetwork().getModel();
        assertNotNull(model);
    }

    @Test
    public void test04_1() {
        Properties properties = new Properties();
        properties.setProperty("StateVariableLazyScheduling", "true");
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        try {
            solver.read(new File(SolverTest.class.getResource("test04.iloc").getPath()));
        } catch (IOException ex) {
            Logger.getLogger(SolverTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean solve = solver.solve();
        assertTrue(solve);

        IModel model = solver.getConstraintNetwork().getModel();
        assertNotNull(model);
    }

    @Test
    public void test05() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        try {
            solver.read(new File(SolverTest.class.getResource("test05.iloc").getPath()));
        } catch (IOException ex) {
            Logger.getLogger(SolverTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean solve = solver.solve();
        assertTrue(solve);

        IModel model = solver.getConstraintNetwork().getModel();
        assertNotNull(model);
    }

    @Test
    public void test06() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        try {
            solver.read(new File(SolverTest.class.getResource("test06.iloc").getPath()));
        } catch (IOException ex) {
            Logger.getLogger(SolverTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean solve = solver.solve();
        assertTrue(solve);

        IModel model = solver.getConstraintNetwork().getModel();
        assertNotNull(model);
    }

    @Test
    public void test07() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        try {
            solver.read(new File(SolverTest.class.getResource("test07.iloc").getPath()));
        } catch (IOException ex) {
            Logger.getLogger(SolverTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean solve = solver.solve();
        assertTrue(solve);

        IModel model = solver.getConstraintNetwork().getModel();
        assertNotNull(model);
    }

    @Test
    public void test08() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        try {
            solver.read(new File(SolverTest.class.getResource("test08.iloc").getPath()));
        } catch (IOException ex) {
            Logger.getLogger(SolverTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean solve = solver.solve();
        assertTrue(solve);

        IModel model = solver.getConstraintNetwork().getModel();
        assertNotNull(model);
    }

    @Test
    public void test09() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        try {
            solver.read(new File(SolverTest.class.getResource("test09.iloc").getPath()));
        } catch (IOException ex) {
            Logger.getLogger(SolverTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean solve = solver.solve();
        assertTrue(solve);

        IModel model = solver.getConstraintNetwork().getModel();
        assertNotNull(model);
    }

    @Test
    public void test10() {
        Properties properties = new Properties();
        ISolver solver = new Solver(properties);
        assertNotNull(solver);

        try {
            solver.read(new File(SolverTest.class.getResource("test10.iloc").getPath()));
        } catch (IOException ex) {
            Logger.getLogger(SolverTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean solve = solver.solve();
        assertTrue(solve);

        IModel model = solver.getConstraintNetwork().getModel();
        assertNotNull(model);
    }
}
