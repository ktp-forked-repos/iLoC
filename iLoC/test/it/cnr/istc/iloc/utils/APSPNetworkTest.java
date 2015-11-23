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
package it.cnr.istc.iloc.utils;

import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class APSPNetworkTest {

    @Test
    public void testNewTemporalNetwork() {
        APSPNetwork network = new APSPNetwork();
        assertNotNull(network);
    }

    @Test
    public void testNewVariable() {
        APSPNetwork network = new APSPNetwork();
        assertNotNull(network);

        int v0 = network.newVariable();
        Assert.assertEquals(0, v0);
        int v1 = network.newVariable();
        Assert.assertEquals(1, v1);
    }

    @Test
    public void testPropagate() {
        APSPNetwork network = new APSPNetwork();
        assertNotNull(network);

        int v0 = network.newVariable();
        assertEquals(0, v0);
        int v1 = network.newVariable();
        assertEquals(1, v1);

        boolean propagate = network.propagate();
        assertTrue(propagate);
    }

    @Test
    public void testAddConstraints() {
        APSPNetwork network = new APSPNetwork();
        assertNotNull(network);

        int a = network.newVariable();
        assertEquals(0, a);
        int b = network.newVariable();
        assertEquals(1, b);
        int c = network.newVariable();
        assertEquals(2, c);
        int d = network.newVariable();
        assertEquals(3, d);
        int e = network.newVariable();
        assertEquals(4, e);
        int or = network.newVariable();
        assertEquals(5, or);

        network.addConstraint(a, b, Double.NEGATIVE_INFINITY, 1);
        network.addConstraint(b, c, Double.NEGATIVE_INFINITY, 1);
        network.addConstraint(c, d, Double.NEGATIVE_INFINITY, 1);
        network.addConstraint(d, or, Double.NEGATIVE_INFINITY, 1);
        network.addConstraint(a, e, 0, 1);
        network.addConstraint(e, or, Double.NEGATIVE_INFINITY, 1);

        boolean propagate = network.propagate();
        assertTrue(propagate);

        System.out.println(network);
    }
}
