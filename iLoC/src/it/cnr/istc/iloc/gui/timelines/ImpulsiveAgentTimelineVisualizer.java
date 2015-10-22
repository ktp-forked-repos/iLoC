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
import it.cnr.istc.iloc.types.impulsiveagent.ImpulsiveAgentType;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
class ImpulsiveAgentTimelineVisualizer implements ITimelineVisualizer {

    @Override
    public Class<? extends IType> getTimelineType() {
        return ImpulsiveAgentType.class;
    }

    @Override
    public XYPlot getPlot(IModel model, IObject object) {
        XYIntervalSeriesCollection collection = new XYIntervalSeriesCollection();

        ValueXYIntervalSeries actions = new ValueXYIntervalSeries("Actions");

        ArrayList<Double> ends = new ArrayList<>(10);
        ends.add(0.0);

        object.getType().getPredicates().values().stream().flatMap(predicate -> predicate.getInstances().stream().map(instance -> (IFormula) instance)).filter(formula -> formula.getFormulaState() == FormulaState.Active && formula.get(model, Constants.SCOPE) == object).sorted((IFormula f0, IFormula f1) -> Double.compare(model.evaluate((INumber) f0.get(Constants.AT)).doubleValue(), model.evaluate((INumber) f1.get(Constants.AT)).doubleValue())).forEach(formula -> {
            double at = model.evaluate((INumber) formula.get(Constants.AT)).doubleValue();
            double y = getYValue(at, ends);
            actions.add(at, at, at + 1, y, y - 1, y, model, formula);
        });

        collection.addSeries(actions);

        XYBarRenderer renderer = new XYBarRenderer();
        renderer.setSeriesPaint(0, Color.lightGray);
        renderer.setBarPainter(new ReverseGradientXYBarPainter());
        renderer.setDrawBarOutline(true);
        renderer.setShadowXOffset(2);
        renderer.setShadowYOffset(2);
        renderer.setUseYInterval(true);

        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelPaint(Color.black);
        Font font = new Font("SansSerif", Font.PLAIN, 9);
        renderer.setBaseItemLabelFont(font);
        XYItemLabelGenerator generator = (XYDataset dataset, int series, int item) -> ((ValueXYIntervalDataItem) ((XYIntervalSeriesCollection) dataset).getSeries(series).getDataItem(item)).toString();
        ItemLabelPosition itLabPos = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER);
        renderer.setBasePositiveItemLabelPosition(itLabPos);
        for (int i = 0; i < collection.getSeriesCount(); i++) {
            renderer.setSeriesItemLabelGenerator(i, generator);
            renderer.setSeriesItemLabelsVisible(i, true);
            renderer.setSeriesItemLabelPaint(i, Color.black);
            renderer.setSeriesItemLabelFont(i, font);
            renderer.setSeriesPositiveItemLabelPosition(i, itLabPos);
            renderer.setSeriesToolTipGenerator(i, (XYDataset dataset, int series, int item) -> ((ValueXYIntervalDataItem) ((XYIntervalSeriesCollection) dataset).getSeries(series).getDataItem(item)).toString());
        }

        XYPlot plot = new XYPlot(collection, null, new NumberAxis(""), renderer);
        plot.getRangeAxis().setVisible(false);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        return plot;
    }

    @Override
    public void mouseClicked(Object dataItem) {
        if (dataItem instanceof ValueXYIntervalDataItem) {
            IFormula formula = ((ValueXYIntervalDataItem) dataItem).formula;
        }
    }

    private static int getYValue(double at, ArrayList<Double> ats) {
        for (int i = 0; i < ats.size(); i++) {
            if (ats.get(i) <= at) {
                ats.set(i, at);
                return (i * 2) + 1;
            }
        }
        ats.add(at);
        return ((ats.size() - 1) * 2) + 1;
    }

    private static class ValueXYIntervalSeries extends XYIntervalSeries {

        private ValueXYIntervalSeries(Comparable<?> key) {
            super(key);
        }

        public void add(double x, double xLow, double xHigh, double y, double yLow, double yHigh, IModel model, IFormula formula) {
            super.add(new ValueXYIntervalDataItem(x, xLow, xHigh, y, yLow, yHigh, model, formula), true);
        }
    }

    private static class ValueXYIntervalDataItem extends XYIntervalDataItem {

        private final IModel model;
        private final IFormula formula;

        private ValueXYIntervalDataItem(double x, double xLow, double xHigh, double y, double yLow, double yHigh, IModel model, IFormula formula) {
            super(x, xLow, xHigh, y, yLow, yHigh);
            this.model = model;
            this.formula = formula;
        }

        @Override
        public String toString() {
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
    }
}
