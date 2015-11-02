package it.cnr.istc.iloc.gui;

import it.cnr.istc.iloc.Solver;
import it.cnr.istc.iloc.api.ISolver;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Riccardo De Benedictis
 */
public class SolverManager {

    private static SolverManager instance;

    public static SolverManager getInstance() {
        if (instance == null) {
            instance = new SolverManager();
        }
        return instance;
    }
    private ISolver solver;
    private final Collection<SolverManagerListener> listeners = new ArrayList<>();

    private SolverManager() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(getClass().getResource("iloc.properties").getFile()));
        } catch (IOException ex) {
            Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.solver = new Solver(properties);
        listeners.forEach(listener -> {
            listener.solverChanged(solver);
        });
    }

    public void resetEnvironment() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(getClass().getResource("iloc.properties").getFile()));
        } catch (IOException ex) {
            Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.solver = new Solver(properties);
        listeners.forEach(listener -> {
            listener.solverChanged(solver);
        });
    }

    public ISolver getSolver() {
        return solver;
    }

    public void addSolverManagerListener(SolverManagerListener listener) {
        listeners.add(listener);
        listener.solverChanged(solver);
    }

    public void removeSolverManagerListener(SolverManagerListener listener) {
        listeners.remove(listener);
    }
}
