package it.cnr.istc.iloc.gui.timelines;

import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.FormulaState;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IType;
import it.cnr.istc.iloc.types.consumableresource.ConsumableResourceType;
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
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Riccardo De Benedictis
 */
class ConsumableResourceTimelineVisualizer implements ITimelineVisualizer {

    @Override
    public Class<? extends IType> getTimelineType() {
        return ConsumableResourceType.class;
    }

    @Override
    public XYPlot getPlot(IModel model, IObject object) {
        final List<Number> pulses = new ArrayList<>();
        final List<ResourceValue> values = new ArrayList<>();
        double min = model.evaluate((INumber) object.get(ConsumableResourceType.MIN)).doubleValue();
        double max = model.evaluate((INumber) object.get(ConsumableResourceType.MAX)).doubleValue();
        double initial_amount = model.evaluate((INumber) object.get(ConsumableResourceType.INITIAL_AMOUNT)).doubleValue();
        double min_amount = initial_amount;
        double max_amount = initial_amount;
        double c_value = initial_amount;
        // For each pulse the formulas starting at that pulse
        Map<Number, Collection<IFormula>> starting_values = new HashMap<>();
        // For each pulse the formulas ending at that pulse
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
            for (IFormula formula : starting_values.get(c_pulses_array[0])) {
                switch (formula.getType().getName()) {
                    case ConsumableResourceType.PRODUCE_PREDICATE_NAME:
                        max_amount += model.evaluate((INumber) formula.get(ConsumableResourceType.AMOUNT)).doubleValue();
                        break;
                    case ConsumableResourceType.CONSUME_PREDICATE_NAME:
                        max_amount -= model.evaluate((INumber) formula.get(ConsumableResourceType.AMOUNT)).doubleValue();
                        break;
                    default:
                        throw new AssertionError(formula.getType());
                }
            }
        }
        if (ending_values.containsKey(c_pulses_array[0])) {
            current_formulas.removeAll(ending_values.get(c_pulses_array[0]));
            for (IFormula formula : ending_values.get(c_pulses_array[0])) {
                switch (formula.getType().getName()) {
                    case ConsumableResourceType.PRODUCE_PREDICATE_NAME:
                        max_amount += model.evaluate((INumber) formula.get(ConsumableResourceType.AMOUNT)).doubleValue();
                        break;
                    case ConsumableResourceType.CONSUME_PREDICATE_NAME:
                        max_amount -= model.evaluate((INumber) formula.get(ConsumableResourceType.AMOUNT)).doubleValue();
                        break;
                    default:
                        throw new AssertionError(formula.getType());
                }
            }
        }
        for (int i = 1; i < c_pulses_array.length; i++) {
            double m = 0;
            for (IFormula formula : current_formulas) {
                double start = model.evaluate((INumber) formula.get(Constants.START)).doubleValue();
                double end = model.evaluate((INumber) formula.get(Constants.END)).doubleValue();
                double amount = model.evaluate((INumber) formula.get(ConsumableResourceType.AMOUNT)).doubleValue();
                switch (formula.getType().getName()) {
                    case ConsumableResourceType.PRODUCE_PREDICATE_NAME:
                        m += amount / (end - start);
                        break;
                    case ConsumableResourceType.CONSUME_PREDICATE_NAME:
                        m -= amount / (end - start);
                        break;
                    default:
                        throw new AssertionError(formula.getType());
                }
            }
            m *= c_pulses_array[i].doubleValue() - c_pulses_array[i - 1].doubleValue();
            values.add(new ResourceValue(min_amount, max_amount, c_value, c_value + m));
            pulses.add(c_pulses_array[i]);
            c_value += m;
            if (starting_values.containsKey(c_pulses_array[i])) {
                current_formulas.addAll(starting_values.get(c_pulses_array[i]));
                for (IFormula formula : starting_values.get(c_pulses_array[i])) {
                    switch (formula.getType().getName()) {
                        case ConsumableResourceType.PRODUCE_PREDICATE_NAME:
                            max_amount += model.evaluate((INumber) formula.get(ConsumableResourceType.AMOUNT)).doubleValue();
                            break;
                        case ConsumableResourceType.CONSUME_PREDICATE_NAME:
                            min_amount -= model.evaluate((INumber) formula.get(ConsumableResourceType.AMOUNT)).doubleValue();
                            break;
                        default:
                            throw new AssertionError(formula.getType());
                    }
                }
            }
            if (ending_values.containsKey(c_pulses_array[i])) {
                current_formulas.removeAll(ending_values.get(c_pulses_array[i]));
                for (IFormula formula : ending_values.get(c_pulses_array[i])) {
                    switch (formula.getType().getName()) {
                        case ConsumableResourceType.PRODUCE_PREDICATE_NAME:
                            min_amount += model.evaluate((INumber) formula.get(ConsumableResourceType.AMOUNT)).doubleValue();
                            break;
                        case ConsumableResourceType.CONSUME_PREDICATE_NAME:
                            max_amount -= model.evaluate((INumber) formula.get(ConsumableResourceType.AMOUNT)).doubleValue();
                            break;
                        default:
                            throw new AssertionError(formula.getType());
                    }
                }
            }
        }

        XYSeriesCollection collection = new XYSeriesCollection();

        XYSeries profile = new XYSeries("Profile");
        for (int i = 0; i < values.size(); i++) {
            profile.add(pulses.get(i), values.get(i).initial_amount);
        }
        if (!values.isEmpty()) {
            profile.add(pulses.get(pulses.size() - 1), values.get(values.size() - 1).final_amount);
        }
        collection.addSeries(profile);

        XYSeries min_series = new XYSeries("Min");
        min_series.add(pulses.get(0), min);
        min_series.add(pulses.get(pulses.size() - 1), min);
        collection.addSeries(min_series);

        XYSeries max_series = new XYSeries("Max");
        max_series.add(pulses.get(0), max);
        max_series.add(pulses.get(pulses.size() - 1), max);
        collection.addSeries(max_series);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesShapesVisible(2, false);
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesToolTipGenerator(0, (XYDataset dataset, int series, int item) -> ((XYSeriesCollection) dataset).getSeries(series).getDataItem(item).getY().toString());
        renderer.setSeriesPaint(1, new Color(135, 0, 0));
        renderer.setSeriesStroke(1, new BasicStroke(1.5f));
        renderer.setSeriesPaint(2, new Color(135, 0, 0));
        renderer.setSeriesStroke(2, new BasicStroke(1.5f));

        XYPlot plot = new XYPlot(collection, null, new NumberAxis(""), renderer);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        XYSeriesCollection step_collection = new XYSeriesCollection();

        XYSeries min_amount_series = new XYSeries("Min amount");
        for (int i = 0; i < values.size(); i++) {
            min_amount_series.add(pulses.get(i), values.get(i).min_amount);
        }
        if (!values.isEmpty()) {
            min_amount_series.add(pulses.get(pulses.size() - 1), values.get(values.size() - 1).min_amount);
        }
        step_collection.addSeries(min_amount_series);

        XYSeries max_amount_series = new XYSeries("Max amount");
        for (int i = 0; i < values.size(); i++) {
            max_amount_series.add(pulses.get(i), values.get(i).max_amount);
        }
        if (!values.isEmpty()) {
            max_amount_series.add(pulses.get(pulses.size() - 1), values.get(values.size() - 1).max_amount);
        }
        step_collection.addSeries(max_amount_series);

        XYStepRenderer step_renderer = new XYStepRenderer();
        step_renderer.setSeriesPaint(0, Color.GRAY);
        step_renderer.setSeriesToolTipGenerator(0, (XYDataset dataset, int series, int item) -> ((XYSeriesCollection) dataset).getSeries(series).getDataItem(item).getY().toString());
        step_renderer.setSeriesPaint(1, Color.GRAY);
        step_renderer.setSeriesToolTipGenerator(1, (XYDataset dataset, int series, int item) -> ((XYSeriesCollection) dataset).getSeries(series).getDataItem(item).getY().toString());

        plot.setDataset(1, step_collection);
        plot.setRenderer(1, step_renderer);

        return plot;
    }

    @Override
    public void mouseClicked(Object dataItem) {
    }

    static class ResourceValue {

        final Number max_amount;
        final Number min_amount;
        final Number initial_amount;
        final Number final_amount;

        ResourceValue(Number min_amount, Number max_amount, Number initial_amount, Number final_amount) {
            this.min_amount = min_amount;
            this.max_amount = max_amount;
            this.initial_amount = initial_amount;
            this.final_amount = final_amount;
        }
    }
}
