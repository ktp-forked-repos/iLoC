package it.cnr.istc.iloc.gui.timelines;

import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.FormulaState;
import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IString;
import it.cnr.istc.iloc.api.IType;
import it.cnr.istc.iloc.types.statevariable.StateVariableType;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalDataItem;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author Riccardo De Benedictis
 */
class StateVariableTimelineVisualizer implements ITimelineVisualizer {

    @Override
    public Class<? extends IType> getTimelineType() {
        return StateVariableType.class;
    }

    @Override
    public XYPlot getPlot(IModel model, IObject object) {
        final List<Number> pulses = new ArrayList<>();
        final List<SVValue> values = new ArrayList<>();
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
            values.add(new SVValue(model, current_formulas.toArray(new IFormula[current_formulas.size()])));
            pulses.add(c_pulses_array[i]);
            if (starting_values.containsKey(c_pulses_array[i])) {
                current_formulas.addAll(starting_values.get(c_pulses_array[i]));
            }
            if (ending_values.containsKey(c_pulses_array[i])) {
                current_formulas.removeAll(ending_values.get(c_pulses_array[i]));
            }
        }

        XYIntervalSeriesCollection collection = new XYIntervalSeriesCollection();

        ValueXYIntervalSeries undefined = new ValueXYIntervalSeries("Undefined");
        ValueXYIntervalSeries sv_values = new ValueXYIntervalSeries("Values");
        ValueXYIntervalSeries conflicts = new ValueXYIntervalSeries("Conflicts");
        for (int i = 0; i < values.size(); i++) {
            double start = pulses.get(i).doubleValue();
            double end = pulses.get(i + 1).doubleValue();
            switch (values.get(i).formulas.length) {
                case 0:
                    undefined.add(start, start, end, 0, 0, 1, values.get(i));
                    break;
                case 1:
                    sv_values.add(start, start, end, 0, 0, 1, values.get(i));
                    break;
                default:
                    conflicts.add(start, start, end, 0, 0, 1, values.get(i));
                    break;
            }
        }
        collection.addSeries(undefined);
        collection.addSeries(sv_values);
        collection.addSeries(conflicts);

        XYBarRenderer renderer = new XYBarRenderer();
        renderer.setSeriesPaint(0, Color.lightGray);
        renderer.setSeriesPaint(1, new Color(100, 250, 100));
        renderer.setSeriesPaint(2, Color.pink);
        renderer.setBarPainter(new ReverseGradientXYBarPainter());
        renderer.setDrawBarOutline(true);
        renderer.setShadowXOffset(2);
        renderer.setShadowYOffset(2);
        renderer.setUseYInterval(true);

        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelPaint(Color.black);
        Font font = new Font("SansSerif", Font.PLAIN, 9);
        renderer.setBaseItemLabelFont(font);
        XYItemLabelGenerator generator = (XYDataset dataset, int series, int item) -> ((ValueXYIntervalDataItem) ((XYIntervalSeriesCollection) dataset).getSeries(series).getDataItem(item)).value.toString();
        ItemLabelPosition itLabPos = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER);
        renderer.setBasePositiveItemLabelPosition(itLabPos);
        for (int i = 0; i < collection.getSeriesCount(); i++) {
            renderer.setSeriesItemLabelGenerator(i, generator);
            renderer.setSeriesItemLabelsVisible(i, true);
            renderer.setSeriesItemLabelPaint(i, Color.black);
            renderer.setSeriesItemLabelFont(i, font);
            renderer.setSeriesPositiveItemLabelPosition(i, itLabPos);
            renderer.setSeriesToolTipGenerator(i, (XYDataset dataset, int series, int item) -> ((ValueXYIntervalDataItem) ((XYIntervalSeriesCollection) dataset).getSeries(series).getDataItem(item)).value.toString());
        }

        XYPlot plot = new XYPlot(collection, null, new NumberAxis(""), renderer);
        plot.getRangeAxis().setVisible(false);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        return plot;
    }

    @Override
    public void mouseClicked(Object dataItem) {
        if (dataItem instanceof ValueXYIntervalDataItem) {
            SVValue value = ((ValueXYIntervalDataItem) dataItem).value;
        }
    }

    private static class SVValue {

        final IModel model;
        final IFormula[] formulas;

        SVValue(IModel model, IFormula... formulas) {
            this.model = model;
            this.formulas = formulas;
        }

        private String toString(IFormula formula) {
            StringBuilder sb = new StringBuilder();
            sb.append(formula.getType().getName()).append("(");
            formula.getType().getFields().values().stream().filter(field -> !field.isSynthetic() && !field.getName().equals(Constants.SCOPE)).forEach(field -> {
                IObject object = formula.get(field.getName());
                sb.append(", ").append(field.getName());
                switch (field.getType().getName()) {
                    case Constants.BOOL:
                        sb.append(" = ").append(model.evaluate((IBool) object));
                        break;
                    case Constants.NUMBER:
                    case Constants.INT:
                    case Constants.REAL:
                        sb.append(" = ").append(new DecimalFormat("#0.##").format(model.evaluate((INumber) object)));
                        break;
                    case Constants.STRING:
                        sb.append(" = ").append(model.evaluate((IString) object));
                        break;
                    default:
                        sb.append(" = ").append(field.getName());
                        break;
                }
            });
            sb.append(")");
            return sb.toString().replace("(, ", "(");
        }

        @Override
        public String toString() {
            switch (formulas.length) {
                case 0:
                    return "";
                case 1:
                    return toString(formulas[0]);
                default:
                    return Stream.of(formulas).map(tk -> toString(tk)).collect(Collectors.joining(", "));
            }
        }
    }

    private static class ValueXYIntervalSeries extends XYIntervalSeries {

        private ValueXYIntervalSeries(Comparable<?> key) {
            super(key);
        }

        public void add(double x, double xLow, double xHigh, double y, double yLow, double yHigh, SVValue value) {
            super.add(new ValueXYIntervalDataItem(x, xLow, xHigh, y, yLow, yHigh, value), true);
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone(); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private static class ValueXYIntervalDataItem extends XYIntervalDataItem {

        private final SVValue value;

        private ValueXYIntervalDataItem(double x, double xLow, double xHigh, double y, double yLow, double yHigh, SVValue value) {
            super(x, xLow, xHigh, y, yLow, yHigh);
            this.value = value;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone(); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
