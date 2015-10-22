package it.cnr.istc.iloc.gui;

import it.cnr.istc.iloc.api.ISolver;

/**
 *
 * @author Riccardo De Benedictis
 */
public interface SolverManagerListener {

    public void solverChanged(ISolver solver);
}
