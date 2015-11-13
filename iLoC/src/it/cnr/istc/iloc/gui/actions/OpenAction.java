package it.cnr.istc.iloc.gui.actions;

import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.gui.JStatusBar;
import it.cnr.istc.iloc.gui.MainJFrame;
import it.cnr.istc.iloc.gui.SolverManager;
import it.cnr.istc.iloc.gui.SolverManagerListener;
import java.awt.event.ActionEvent;
import java.beans.Beans;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Riccardo De Benedictis
 */
public class OpenAction extends AbstractAction implements SolverManagerListener {

    private MainJFrame frame;
    private ISolver solver;

    public OpenAction() {
        super("Open Project");
        putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("../resources/open-file16.png")));
        putValue(AbstractAction.LARGE_ICON_KEY, new ImageIcon(getClass().getResource("../resources/open-file32.png")));
        if (!Beans.isDesignTime()) {
            SolverManager.getInstance().addSolverManagerListener(this);
        }
    }

    public void setFrame(MainJFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }

                String s = file.getName();
                int i = s.lastIndexOf('.');
                if (i > 0 && i < s.length() - 1) {
                    return s.substring(i + 1).equalsIgnoreCase("iloc");
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "iLoC domain definition language [.iloc]";
            }
        });
        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File[] selectedFiles = fc.getSelectedFiles();
                JStatusBar statusBar = frame.getStatusBar();
                statusBar.setIndeterminate(true);
                if (selectedFiles.length == 1) {
                    statusBar.setTaskMessage("Loading file " + selectedFiles[0].getPath());
                } else {
                    statusBar.setTaskMessage("Loading files");
                }
                solver.read(selectedFiles);
                statusBar.setIndeterminate(false);
                statusBar.setTaskMessage(null);
            } catch (IOException ex) {
                Logger.getLogger(OpenAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void solverChanged(ISolver solver) {
        this.solver = solver;
    }
}
