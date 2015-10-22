package it.cnr.istc.iloc.gui.timelines;

import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IType;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author Riccardo De Benedictis
 */
public interface ITimelineVisualizer {

    public Class<? extends IType> getTimelineType();

    public XYPlot getPlot(IModel state, IObject object);

    public void mouseClicked(Object dataItem);
}
