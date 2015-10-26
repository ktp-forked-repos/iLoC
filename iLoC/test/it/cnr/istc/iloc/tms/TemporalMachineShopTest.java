package it.cnr.istc.iloc.tms;

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
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Riccardo De Benedictis
 */
public class TemporalMachineShopTest {

    private static final Logger LOG = Logger.getLogger(TemporalMachineShopTest.class.getName());

    @BeforeClass
    public static void setUpClass() {
        LOG.info("Testing Temporal Machine Shop");
    }

    /**
     * Runs all the temporal machine shop tests.
     *
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     * @throws Exception
     */
    @Test
    public void testTemporalMachineShop0() throws NoSuchFieldException, ClassNotFoundException, URISyntaxException, IOException, Exception {
        int size = 100;

        int n_klins = 5;
        int n_pieces = 100;

        double[][][] millis = new double[n_klins][n_pieces][size];

        double[][] fastest_time = new double[n_klins][n_pieces];
        double[][] slowest_time = new double[n_klins][n_pieces];
        double[][] mean_time = new double[n_klins][n_pieces];

        for (int n_klin = 1; n_klin <= n_klins; n_klin++) {
            for (int n_piece = 1; n_piece <= n_pieces; n_piece++) {
                LOG.log(Level.INFO, "Solving problem with {0} klins and {1} pieces", new Object[]{n_klin, n_piece});
                for (int i = 0; i < size; i++) {
                    Properties properties = new Properties();
                    ISolver solver = new Solver(properties);
                    solver.read(new File(TemporalMachineShopTest.class.getResource("tms-domain.iloc").getPath()));
                    solver.read(new File(TemporalMachineShopTest.class.getResource("tms-" + n_klin + "-" + new DecimalFormat("000").format(n_piece) + ".iloc").getPath()));

                    long starting_nano = System.nanoTime();
                    boolean solve = solver.solve();
                    long nano_time = System.nanoTime() - starting_nano;

                    assertTrue("tms-" + n_klin + "-" + new DecimalFormat("000").format(n_piece) + ".iloc", solve);
                    millis[n_klin - 1][n_piece - 1][i] = (((double) nano_time) / 1_000_000);
                    System.gc();

                    LOG.log(Level.INFO, "Problem solved in {0} ms", new DecimalFormat("0.00").format(millis[n_klin - 1][n_piece - 1][i]));
                }

                fastest_time[n_klin - 1][n_piece - 1] = Double.POSITIVE_INFINITY;
                slowest_time[n_klin - 1][n_piece - 1] = Double.NEGATIVE_INFINITY;
                mean_time[n_klin - 1][n_piece - 1] = 0;

                for (int i = 0; i < size; i++) {
                    if (fastest_time[n_klin - 1][n_piece - 1] > millis[n_klin - 1][n_piece - 1][i]) {
                        fastest_time[n_klin - 1][n_piece - 1] = millis[n_klin - 1][n_piece - 1][i];
                    }
                    if (slowest_time[n_klin - 1][n_piece - 1] < millis[n_klin - 1][n_piece - 1][i]) {
                        slowest_time[n_klin - 1][n_piece - 1] = millis[n_klin - 1][n_piece - 1][i];
                    }
                    mean_time[n_klin - 1][n_piece - 1] += millis[n_klin - 1][n_piece - 1][i];
                }
                mean_time[n_klin - 1][n_piece - 1] /= size;
            }
        }

        for (int n_klin = 1; n_klin <= n_klins; n_klin++) {
            for (int n_piece = 1; n_piece <= n_pieces; n_piece++) {
                LOG.log(Level.INFO, "Temporal Machine Shop problem with {0} klins and {1} pieces", new Object[]{n_klin, n_piece});
                LOG.log(Level.INFO, "Problem solved in {0} ms average time", new DecimalFormat("0.00").format(mean_time[n_klin - 1][n_piece - 1]));
                LOG.log(Level.INFO, "Fastest solution in {0} ms", new DecimalFormat("0.00").format(fastest_time[n_klin - 1][n_piece - 1]));
                LOG.log(Level.INFO, "Slowest solution in {0} ms", new DecimalFormat("0.00").format(slowest_time[n_klin - 1][n_piece - 1]));
            }
        }
    }

    /**
     * Runs just one temporal machine shop test.
     *
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     * @throws Exception
     */
    @Test
    public void testTemporalMachineShop1() throws NoSuchFieldException, ClassNotFoundException, URISyntaxException, IOException, Exception {
        int size = 10;

        double[] millis = new double[size];

        for (int i = 0; i < size; i++) {
            Properties properties = new Properties();
            ISolver solver = new Solver(properties);

            solver.read(new File(TemporalMachineShopTest.class.getResource("tms-domain.iloc").getPath()));
            solver.read(new File(TemporalMachineShopTest.class.getResource("tms-2-010.iloc").getPath()));

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
