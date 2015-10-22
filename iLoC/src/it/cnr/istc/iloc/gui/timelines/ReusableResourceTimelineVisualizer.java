package it.cnr.istc.iloc.gui.timelines;

import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.FormulaState;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IType;
import it.cnr.istc.iloc.types.reusableresource.ReusableResourceType;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Riccardo De Benedictis
 */
class ReusableResourceTimelineVisualizer implements ITimelineVisualizer {

    @Override
    public Class<? extends IType> getTimelineType() {
        return ReusableResourceType.class;
    }

    @Override
    public XYPlot getPlot(IModel model, IObject object) {
        final List<Number> pulses = new ArrayList<>();
        final List<ResourceValue> values = new ArrayList<>();
        double capacity = model.evaluate((INumber) object.get(ReusableResourceType.CAPACITY)).doubleValue();
        // For each pulse the tokens starting at that pulse
        Map<Number, Collection<IFormula>> starting_values = new HashMap<>();
        // For each pulse the tokens ending at that pulse
        Map<Number, Collection<IFormula>> ending_values = new HashMap<>();
        // The pulses of the timeline
        Set<Number> c_pulses = new HashSet<>();
        c_pulses.add(model.evaluate((INumber) object.getSolver().get(Constants.ORIGIN)));
        c_pulses.add(model.evaluate((INumber) object.getSolver().get(Constants.HORIZON)));

        // Each formula generates a starting pulse and an eneding pulse
        object.getType().getPredicates().values().stream().flatMap(predicate -> predicate.getInstances().stream().map(instance -> (IFormula) instance)).filter(formula -> formula.getFormulaState() == FormulaState.Active && formula.get(model, Constants.SCOPE) == object).forEach(formula -> {
            Number start_pulse = model.evaluate((INumber) formula.get(Constants.START));
            Number end_pulse = model.evaluate((INumber) formula.get(Constants.END));
            c_pulses.add(start_pulse);
            if (!starting_values.containsKey(start_pulse)) {
                starting_values.put(start_pulse, new ArrayList<>());
            }
            starting_values.get(start_pulse).add(formula);
            c_pulses.add(end_pulse);
            if (!ending_values.containsKey(end_pulse)) {
                ending_values.put(end_pulse, new ArrayList<>());
            }
            ending_values.get(end_pulse).add(formula);
        });

        // Sort current pulses
        Number[] c_pulses_array = c_pulses.toArray(new Number[c_pulses.size()]);
        Arrays.sort(c_pulses_array);

        // Push values to timeline according to pulses...
        List<IFormula> current_formulas = new ArrayList<>();
        pulses.add(c_pulses_array[0]);
        if (starting_values.containsKey(c_pulses_array[0])) {
            current_formulas.addAll(starting_values.get(c_pulses_array[0]));
        }
        if (ending_values.containsKey(c_pulses_array[0])) {
            current_formulas.removeAll(ending_values.get(c_pulses_array[0]));
        }
        for (int i = 1; i < c_pulses_array.length; i++) {
            double amount = current_formulas.stream().mapToDouble(formula -> model.evaluate((INumber) formula.get(ReusableResourceType.AMOUNT)).doubleValue()).sum();
            values.add(new ResourceValue(amount, current_formulas.toArray(new IFormula[current_formulas.size()])));
            pulses.add(c_pulses_array[i]);
            if (starting_values.containsKey(c_pulses_array[i])) {
                current_formulas.addAll(starting_values.get(c_pulses_array[i]));
            }
            if (ending_values.containsKey(c_pulses_array[i])) {
                current_formulas.removeAll(ending_values.get(c_pulses_array[i]));
            }
        }

        XYSeriesCollection collection = new XYSeriesCollection();

        XYSeries profile = new XYSeries("Profile");
        profile.add(pulses.get(0), 0);
        for (int i = 0; i < values.size(); i++) {
            profile.add(pulses.get(i), values.get(i).c_amount);
        }
        if (!values.isEmpty()) {
            profile.add(pulses.get(pulses.size() - 1), values.get(values.size() - 1).c_amount);
        }
        profile.add(pulses.get(pulses.size() - 1), 0);
        collection.addSeries(profile);

        XYSeries capacity_series = new XYSeries("Capacity");
        capacity_series.add(pulses.get(0), capacity);
        capacity_series.add(pulses.get(pulses.size() - 1), capacity);
        collection.addSeries(capacity_series);

        XYStepRenderer renderer = new XYStepRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesToolTipGenerator(0, (XYDataset dataset, int series, int item) -> ((XYSeriesCollection) dataset).getSeries(series).getDataItem(item).getY().toString());
        renderer.setSeriesPaint(1, new Color(135, 0, 0));
        renderer.setSeriesStroke(1, new BasicStroke(1.5f));

        XYPlot plot = new XYPlot(collection, null, new NumberAxis(""), renderer);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        return plot;
    }

    @Override
    public void mouseClicked(Object dataItem) {
    }

    static class ResourceValue {

        final double c_amount;
        final IFormula[] formulas;

        ResourceValue(double c_amount, IFormula... formulas) {
            this.c_amount = c_amount;
            this.formulas = formulas;
        }
    }
}
