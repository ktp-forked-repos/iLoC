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
package it.cnr.istc.iloc.aixia.dc2014;

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
public class AIxIATest {

    private static final Logger LOG = Logger.getLogger(AIxIATest.class.getName());

    /**
     * Runs all the AIxIA tests.
     *
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     * @throws Exception
     */
    @Test
    public void testAIxIA0() throws NoSuchFieldException, ClassNotFoundException, URISyntaxException, IOException, Exception {
        int size = 10;

        int n_pics = 5;
        int n_winds = 4;

        double[][][] millis = new double[n_pics][n_winds][size];

        double[][] fastest_time = new double[n_pics][n_winds];
        double[][] slowest_time = new double[n_pics][n_winds];
        double[][] mean_time = new double[n_pics][n_winds];

        for (int n_pic = 1; n_pic <= n_pics; n_pic++) {
            for (int n_wind = 1; n_wind <= n_winds; n_wind++) {
                LOG.log(Level.INFO, "Solving problem with {0} pictures and {1} visibility windows", new Object[]{n_pic, n_wind});
                for (int i = 0; i < size; i++) {
                    Properties properties = new Properties();
                    ISolver solver = new Solver(properties);
                    solver.read(
                            new File(AIxIATest.class.getResource("Location.iloc").getPath()),
                            new File(AIxIATest.class.getResource("Map.iloc").getPath()),
                            new File(AIxIATest.class.getResource("Navigator.iloc").getPath()),
                            new File(AIxIATest.class.getResource("Visibility.iloc").getPath()),
                            new File(AIxIATest.class.getResource("Robot.iloc").getPath())
                    );
                    solver.read(new File(AIxIATest.class.getResource("aixia_problem_" + n_pic + "pic_" + n_wind + "wind.iloc").getPath()));

                    long starting_nano = System.nanoTime();
                    boolean solve = solver.solve();
                    long nano_time = System.nanoTime() - starting_nano;

                    assertTrue("aixia_problem_" + n_pic + "pic_" + n_wind + "wind.iloc", solve);
                    millis[n_pic - 1][n_wind - 1][i] = (((double) nano_time) / 1_000_000);
                    System.gc();

                    LOG.log(Level.INFO, "Problem solved in {0} ms", new DecimalFormat("0.00").format(millis[n_pic - 1][n_wind - 1][i]));
                }

                fastest_time[n_pic - 1][n_wind - 1] = Double.POSITIVE_INFINITY;
                slowest_time[n_pic - 1][n_wind - 1] = Double.NEGATIVE_INFINITY;
                mean_time[n_pic - 1][n_wind - 1] = 0;

                for (int i = 0; i < size; i++) {
                    if (fastest_time[n_pic - 1][n_wind - 1] > millis[n_pic - 1][n_wind - 1][i]) {
                        fastest_time[n_pic - 1][n_wind - 1] = millis[n_pic - 1][n_wind - 1][i];
                    }
                    if (slowest_time[n_pic - 1][n_wind - 1] < millis[n_pic - 1][n_wind - 1][i]) {
                        slowest_time[n_pic - 1][n_wind - 1] = millis[n_pic - 1][n_wind - 1][i];
                    }
                    mean_time[n_pic - 1][n_wind - 1] += millis[n_pic - 1][n_wind - 1][i];
                }
                mean_time[n_pic - 1][n_wind - 1] /= size;
            }
        }

        for (int n_pic = 1; n_pic <= n_pics; n_pic++) {
            for (int n_wind = 1; n_wind <= n_winds; n_wind++) {
                LOG.log(Level.INFO, "Problem solved in {0} ms average time", new DecimalFormat("0.00").format(mean_time[n_pic - 1][n_wind - 1]));
                LOG.log(Level.INFO, "Fastest solution in {0} ms", new DecimalFormat("0.00").format(fastest_time[n_pic - 1][n_wind - 1]));
                LOG.log(Level.INFO, "Slowest solution in {0} ms", new DecimalFormat("0.00").format(slowest_time[n_pic - 1][n_wind - 1]));
            }
        }
    }

    /**
     * Runs just one AIxIA test.
     *
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     * @throws Exception
     */
    @Test
    public void testAIxIA1() throws NoSuchFieldException, ClassNotFoundException, URISyntaxException, IOException, Exception {
        int size = 10;

        double[] millis = new double[size];

        for (int i = 0; i < size; i++) {
            Properties properties = new Properties();
            ISolver solver = new Solver(properties);

            solver.read(
                    new File(AIxIATest.class.getResource("Location.iloc").getPath()),
                    new File(AIxIATest.class.getResource("Map.iloc").getPath()),
                    new File(AIxIATest.class.getResource("Navigator.iloc").getPath()),
                    new File(AIxIATest.class.getResource("Visibility.iloc").getPath()),
                    new File(AIxIATest.class.getResource("Robot.iloc").getPath())
            );
            solver.read(new File(AIxIATest.class.getResource("aixia_problem_5pic_4wind.iloc").getPath()));

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
