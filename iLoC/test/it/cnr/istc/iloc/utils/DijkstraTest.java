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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class DijkstraTest {

    @Test
    public void testNewDijkstra() {
        Dijkstra<String> dijkstra = new Dijkstra<>();
        assertNotNull(dijkstra);
    }

    @Test
    public void testNewVertex() {
        Dijkstra<String> dijkstra = new Dijkstra<>();
        assertNotNull(dijkstra);

        dijkstra.addVertex("a", Double.POSITIVE_INFINITY);
        dijkstra.addVertex("b", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testNewEdge() {
        Dijkstra<String> dijkstra = new Dijkstra<>();
        assertNotNull(dijkstra);

        String origin = "origin";
        String a = "a";
        String b = "b";
        String c = "c";
        String or = "or";
        dijkstra.addVertex(origin, 0);
        dijkstra.addVertex(a, 0);
        dijkstra.addVertex(b, Double.POSITIVE_INFINITY);
        dijkstra.addVertex(c, 0);
        dijkstra.addVertex(or, Double.POSITIVE_INFINITY);

        dijkstra.addEdge(a, b, 1);
        dijkstra.addEdge(b, or, 1);
        dijkstra.addEdge(c, or, 1);

        dijkstra.dijkstra(origin);

        assertEquals(1d, dijkstra.getDistance(or), 0);
    }

    @Test
    public void testCyclicNetwork() {
        Dijkstra<String> dijkstra = new Dijkstra<>();
        assertNotNull(dijkstra);

        String origin = "origin";
        String a = "a";
        String b = "b";
        String c = "c";

        dijkstra.addVertex(origin, 0);
        dijkstra.addVertex(a, 0);
        dijkstra.addVertex(b, Double.POSITIVE_INFINITY);
        dijkstra.addVertex(c, Double.POSITIVE_INFINITY);

        dijkstra.addEdge(a, b, 1);
        dijkstra.addEdge(b, c, 1);
        dijkstra.addEdge(c, b, 1);

        dijkstra.dijkstra(origin);
    }
}
