package it.cnr.istc.iloc.gui.actions;

import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.gui.JStatusBar;
import it.cnr.istc.iloc.gui.MainJFrame;
import it.cnr.istc.iloc.gui.SolverManager;
import it.cnr.istc.iloc.gui.SolverManagerListener;
import java.awt.event.ActionEvent;
import java.beans.Beans;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

/**
 *
 * @author Riccardo De Benedictis
 */
public class SolveAction extends AbstractAction implements SolverManagerListener {

    private static final Logger LOG = Logger.getLogger(SolveAction.class.getName());
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private MainJFrame frame;
    private ISolver solver;

    public SolveAction() {
        super("Solve Problem");
        putValue(AbstractAction.SMALL_ICON, new ImageIcon(SolveAction.class.getResource("/it/cnr/istc/iloc/gui/resources/play16.png")));
        putValue(AbstractAction.LARGE_ICON_KEY, new ImageIcon(SolveAction.class.getResource("/it/cnr/istc/iloc/gui/resources/play32.png")));
        if (!Beans.isDesignTime()) {
            SolverManager.getInstance().addSolverManagerListener(this);
        }
    }

    public void setFrame(MainJFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setEnabled(false);
        EXECUTOR.execute(() -> {
            JStatusBar statusBar = frame.getStatusBar();
            statusBar.setIndeterminate(true);
            statusBar.setTaskMessage("Solving problem");
            long starting_nano = System.nanoTime();
            boolean solved = solver.solve();
            long nano_time = System.nanoTime() - starting_nano;
            setEnabled(solved);
            if (solved) {
                LOG.log(Level.INFO, "Problem solved in {0} ms", new DecimalFormat("0.00").format(((double) nano_time) / 1_000_000));
                statusBar.setTempMessage("Problem solved in " + new DecimalFormat("0.00").format(((double) nano_time) / 1_000_000) + " ms");
            } else {
                LOG.log(Level.INFO, "Detected inconsistency in {0} ms", new DecimalFormat("0.00").format(((double) nano_time) / 1_000_000));
                statusBar.setTempMessage("Detected inconsistency in " + new DecimalFormat("0.00").format(((double) nano_time) / 1_000_000) + " ms");
            }
            statusBar.setIndeterminate(false);
            statusBar.setTaskMessage(null);
        });
    }

    @Override
    public void solverChanged(ISolver solver) {
        this.solver = solver;
    }
}
