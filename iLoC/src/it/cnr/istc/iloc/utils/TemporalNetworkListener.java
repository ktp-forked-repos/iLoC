package it.cnr.istc.iloc.utils;

/**
 *
 * @author Riccardo De Benedictis
 */
public interface TemporalNetworkListener {

    public void timePointAdded(int tp);

    public void timePointChange(int tp, double min, double max);

    public void distanceChange(int tp_from, int tp_to, double min, double max);
}
