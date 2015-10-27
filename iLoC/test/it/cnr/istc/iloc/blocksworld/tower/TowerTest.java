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
package it.cnr.istc.iloc.blocksworld.tower;

import it.cnr.istc.iloc.Solver;
import it.cnr.istc.iloc.api.ISolver;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class TowerTest {

    private static final Logger LOG = Logger.getLogger(TowerTest.class.getName());

    /**
     * Runs all the Blocksworld Tower tests.
     *
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     * @throws Exception
     */
    @Test
    public void testTower() throws NoSuchFieldException, ClassNotFoundException, URISyntaxException, IOException, Exception {
        int size = 10;

        int n_goals = 10;

        double[][] millis = new double[n_goals][size];

        double[] fastest_time = new double[n_goals];
        double[] slowest_time = new double[n_goals];
        double[] mean_time = new double[n_goals];

        for (int c_goals = 1; c_goals <= n_goals; c_goals++) {
            LOG.log(Level.INFO, "Solving problem with {0} goals", new Object[]{c_goals});
            for (int i = 0; i < size; i++) {
                Properties properties = new Properties();
                ISolver solver = new Solver(properties);
                solver.read(new File(TowerTest.class.getResource("tower" + new DecimalFormat("00").format(c_goals) + ".iloc").getPath()));

                long starting_nano = System.nanoTime();
                boolean solve = solver.solve();
                long nano_time = System.nanoTime() - starting_nano;

                assertTrue("tower" + new DecimalFormat("00").format(c_goals) + ".iloc", solve);
                millis[c_goals - 1][i] = (((double) nano_time) / 1_000_000);
                System.gc();

                LOG.log(Level.INFO, "Problem solved in {0} ms", new DecimalFormat("0.00").format(millis[c_goals - 1][i]));
            }

            fastest_time[c_goals - 1] = Double.POSITIVE_INFINITY;
            slowest_time[c_goals - 1] = Double.NEGATIVE_INFINITY;
            mean_time[c_goals - 1] = 0;

            for (int i = 0; i < size; i++) {
                if (fastest_time[c_goals - 1] > millis[c_goals - 1][i]) {
                    fastest_time[c_goals - 1] = millis[c_goals - 1][i];
                }
                if (slowest_time[c_goals - 1] < millis[c_goals - 1][i]) {
                    slowest_time[c_goals - 1] = millis[c_goals - 1][i];
                }
                mean_time[c_goals - 1] += millis[c_goals - 1][i];
            }
            mean_time[c_goals - 1] /= size;
        }

        for (int c_goals = 1; c_goals <= n_goals; c_goals++) {
            LOG.log(Level.INFO, "Problem solved in {0} ms average time", new DecimalFormat("0.00").format(mean_time[c_goals - 1]));
            LOG.log(Level.INFO, "Fastest solution in {0} ms", new DecimalFormat("0.00").format(fastest_time[c_goals - 1]));
            LOG.log(Level.INFO, "Slowest solution in {0} ms", new DecimalFormat("0.00").format(slowest_time[c_goals - 1]));
        }
    }

    /**
     * Runs just one Blocksworld Tower test.
     *
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     * @throws Exception
     */
    @Test
    public void testTower1() throws NoSuchFieldException, ClassNotFoundException, URISyntaxException, IOException, Exception {
        int size = 10;

        double[] millis = new double[size];

        for (int i = 0; i < size; i++) {
            Properties properties = new Properties();
            ISolver solver = new Solver(properties);

            solver.read(new File(TowerTest.class.getResource("tower06.iloc").getPath()));

            long starting_nano = System.nanoTime();
            boolean solve = solver.solve();
            long nano_time = System.nanoTime() - starting_nano;

            assertTrue(solve);
            millis[i] = (((double) nano_time) / 1_000_000);

            LOG.log(Level.INFO, "Problem solved in {0} ms", new DecimalFormat("0.00").format(millis[i]));
        }

        double fastest_time = Double.POSITIVE_INFINITY;
        double slowest_time = Double.NEGATIVE_INFINITY;
        double mean_time = 0;

        for (int i = 0; i < size; i++) {
            if (fastest_time > millis[i]) {
                fastest_time = millis[i];
            }
            if (slowest_time < millis[i]) {
                slowest_time = millis[i];
            }
            mean_time += millis[i];
        }
        mean_time /= size;

        LOG.log(Level.INFO, "Problem solved in {0} ms average time", new DecimalFormat("0.00").format(mean_time));
        LOG.log(Level.INFO, "Fastest solution in {0} ms", new DecimalFormat("0.00").format(fastest_time));
        LOG.log(Level.INFO, "Slowest solution in {0} ms", new DecimalFormat("0.00").format(slowest_time));
    }
}
