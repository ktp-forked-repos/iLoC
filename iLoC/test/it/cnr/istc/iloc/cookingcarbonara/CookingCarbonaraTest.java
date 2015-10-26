package it.cnr.istc.iloc.cookingcarbonara;

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
public class CookingCarbonaraTest {

    private static final Logger LOG = Logger.getLogger(CookingCarbonaraTest.class.getName());

    /**
     * Runs all the cooking carbonara tests.
     *
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     * @throws Exception
     */
    @Test
    public void testCookingCarbonara0() throws NoSuchFieldException, ClassNotFoundException, URISyntaxException, IOException, Exception {
        int size = 10;

        int n_plates = 2;
        int n_dishes = 100;

        double[][][] millis = new double[n_plates][n_dishes][size];

        double[][] fastest_time = new double[n_plates][n_dishes];
        double[][] slowest_time = new double[n_plates][n_dishes];
        double[][] mean_time = new double[n_plates][n_dishes];

        for (int n_plate = 1; n_plate <= n_plates; n_plate++) {
            for (int n_dish = 1; n_dish <= n_dishes; n_dish++) {
                LOG.log(Level.INFO, "Solving problem with {0} plates and {1} dishes", new Object[]{n_plate, n_dish});
                for (int i = 0; i < size; i++) {
                    Properties properties = new Properties();
                    ISolver solver = new Solver(properties);
                    solver.read(new File(CookingCarbonaraTest.class.getResource("cooking-carbonara-domain.iloc").getPath()));
                    solver.read(new File(CookingCarbonaraTest.class.getResource("cooking-carbonara-" + n_plate + "-" + new DecimalFormat("000").format(n_dish) + ".iloc").getPath()));

                    long starting_nano = System.nanoTime();
                    boolean solve = solver.solve();
                    long nano_time = System.nanoTime() - starting_nano;

                    assertTrue("cooking-carbonara-" + n_plate + "-" + new DecimalFormat("000").format(n_dish) + ".iloc", solve);
                    millis[n_plate - 1][n_dish - 1][i] = (((double) nano_time) / 1_000_000);
                    System.gc();

                    if (solve) {
                        LOG.log(Level.INFO, "Problem solved in {0} ms", new DecimalFormat("0.00").format(millis[n_plate - 1][n_dish - 1][i]));
                    } else {
                        LOG.log(Level.INFO, "Problem not solved in {0} ms", new DecimalFormat("0.00").format(millis[n_plate - 1][n_dish - 1][i]));
                    }
                }

                fastest_time[n_plate - 1][n_dish - 1] = Double.POSITIVE_INFINITY;
                slowest_time[n_plate - 1][n_dish - 1] = Double.NEGATIVE_INFINITY;
                mean_time[n_plate - 1][n_dish - 1] = 0;

                for (int i = 0; i < size; i++) {
                    if (fastest_time[n_plate - 1][n_dish - 1] > millis[n_plate - 1][n_dish - 1][i]) {
                        fastest_time[n_plate - 1][n_dish - 1] = millis[n_plate - 1][n_dish - 1][i];
                    }
                    if (slowest_time[n_plate - 1][n_dish - 1] < millis[n_plate - 1][n_dish - 1][i]) {
                        slowest_time[n_plate - 1][n_dish - 1] = millis[n_plate - 1][n_dish - 1][i];
                    }
                    mean_time[n_plate - 1][n_dish - 1] += millis[n_plate - 1][n_dish - 1][i];
                }
                mean_time[n_plate - 1][n_dish - 1] /= size;
            }
        }

        for (int n_klin = 1; n_klin <= n_plates; n_klin++) {
            for (int n_piece = 1; n_piece <= n_dishes; n_piece++) {
                LOG.log(Level.INFO, "Cooking Carbonara problem with {0} plates and {1} dishes", new Object[]{n_klin, n_piece});
                LOG.log(Level.INFO, "Problem solved in {0} ms average time", new DecimalFormat("0.00").format(mean_time[n_klin - 1][n_piece - 1]));
                LOG.log(Level.INFO, "Fastest solution in {0} ms", new DecimalFormat("0.00").format(fastest_time[n_klin - 1][n_piece - 1]));
                LOG.log(Level.INFO, "Slowest solution in {0} ms", new DecimalFormat("0.00").format(slowest_time[n_klin - 1][n_piece - 1]));
            }
        }
    }

    /**
     * Runs just one cooking carbonara test.
     *
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     * @throws Exception
     */
    @Test
    public void testCookingCarbonara1() throws NoSuchFieldException, ClassNotFoundException, URISyntaxException, IOException, Exception {
        int size = 10;

        double[] millis = new double[size];

        for (int i = 0; i < size; i++) {
            Properties properties = new Properties();
            ISolver solver = new Solver(properties);

            solver.read(new File(CookingCarbonaraTest.class.getResource("cooking-carbonara-domain.iloc").getPath()));
            solver.read(new File(CookingCarbonaraTest.class.getResource("cooking-carbonara-2-020.iloc").getPath()));

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
            LOG.log(Level.INFO, "Problem solved in {0} ms", new DecimalFormat("0.00").format(millis[i]));
        }
        mean_time /= size;

        LOG.log(Level.INFO, "Problem solved in {0} ms average time", new DecimalFormat("0.00").format(mean_time));
        LOG.log(Level.INFO, "Fastest solution in {0} ms", new DecimalFormat("0.00").format(fastest_time));
        LOG.log(Level.INFO, "Slowest solution in {0} ms", new DecimalFormat("0.00").format(slowest_time));
    }
}
