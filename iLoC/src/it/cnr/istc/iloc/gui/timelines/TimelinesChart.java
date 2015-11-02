package it.cnr.istc.iloc.gui.timelines;

import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import it.cnr.istc.iloc.gui.SolverManager;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;

/**
 *
 * @author Riccardo De Benedictis
 */
public class TimelinesChart extends ChartPanel {

    private static final JFreeChart CHART = new JFreeChart(new XYPlot());
    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private final Map<Class<? extends IType>, ITimelineVisualizer> visualizers = new IdentityHashMap<>();

    public TimelinesChart() {
        super(CHART);
        setBorder(null);

        Arrays.asList(
                new AgentTimelineVisualizer(),
                new BatteryTimelineVisualizer(),
                new ConsumableResourceTimelineVisualizer(),
                new ImpulsiveAgentTimelineVisualizer(),
                new PropositionalStateTimelineVisualizer(),
                new PropositionalAgentTimelineVisualizer(),
                new PropositionalImpulsiveAgentTimelineVisualizer(),
                new ReusableResourceTimelineVisualizer(),
                new StateVariableTimelineVisualizer()
        ).forEach(visualizer -> {
            visualizers.put(visualizer.getTimelineType(), visualizer);
        });

        addChartMouseListener(new ChartMouseListener() {

            private Object getDataItem(ChartEntity entity) {
                XYItemEntity e = ((XYItemEntity) entity);
                XYDataset ds = e.getDataset();
                if (ds instanceof XYIntervalSeriesCollection) {
                    XYIntervalSeries theSerie = ((XYIntervalSeriesCollection) ds).getSeries(e.getSeriesIndex());
                    return theSerie.getDataItem(e.getItem());
                } else if (ds instanceof XYSeriesCollection) {
                    XYSeries theSerie = ((XYSeriesCollection) ds).getSeries(e.getSeriesIndex());
                    return theSerie.getDataItem(e.getItem());
                } else {
                    return null;
                }
            }

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                if (cme.getEntity() != null && cme.getEntity() instanceof XYItemEntity) {
                    Object dataItem = getDataItem(cme.getEntity());
                    visualizers.values().forEach(visualizer -> {
                        visualizer.mouseClicked(dataItem);
                    });
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
                if (cme.getEntity() != null && cme.getEntity() instanceof XYItemEntity) {
                    setCursor(HAND_CURSOR);
                } else {
                    setCursor(DEFAULT_CURSOR);
                }
            }
        });
    }

    public void addTimelineVisualizer(ITimelineVisualizer timelineVisualizer) {
        visualizers.put(timelineVisualizer.getTimelineType(), timelineVisualizer);
    }

    public void setModel(IModel model) {
        if (model != null) {
            ISolver solver = SolverManager.getInstance().getSolver();
            createTimelines(model, extractObjects(model, solver, solver.getFields().values().stream().filter(field -> !field.isSynthetic()).collect(Collectors.toList()), new ArrayList<>()));
        }
        setBorder(model == null ? BorderFactory.createEtchedBorder(Color.red, null) : BorderFactory.createEtchedBorder());
    }

    private Map<String, IObject> extractObjects(IModel model, IEnvironment environment, Collection<? extends IField> fields, Collection<IObject> found_timelines) {
        Map<String, IObject> c_objects = new LinkedHashMap<>();
        fields.forEach(field -> {
            IObject object = environment.get(model, field.getName());
            if (!found_timelines.contains(object) && getVisualizer(object.getType()) != null) {
                found_timelines.add(object);
                c_objects.put(field.getName(), object);
                Map<String, IObject> o_objects = extractObjects(model, object, object.getType().getFields().values().stream().filter(f -> !f.isSynthetic()).collect(Collectors.toList()), found_timelines);
                o_objects.entrySet().forEach(entry -> {
                    c_objects.put(field.getName() + "." + entry.getKey(), entry.getValue());
                });
            }
        });
        return c_objects;
    }

    private void createTimelines(IModel model, Map<String, IObject> objects) {
        final CombinedDomainXYPlot combined_plot = new CombinedDomainXYPlot(new DateAxis("Time"));
        combined_plot.setGap(3.0);
        combined_plot.setOrientation(PlotOrientation.VERTICAL);
        objects.entrySet().forEach(entry -> {
            ITimelineVisualizer visualizer = getVisualizer(entry.getValue().getType());
            if (visualizer != null) {
                XYPlot p = visualizer.getPlot(model, entry.getValue());
                TextTitle title = new TextTitle(entry.getKey(), new Font("SansSerif", Font.PLAIN, 11), Color.BLACK, RectangleEdge.TOP, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, new RectangleInsets(4, 4, 4, 4));
                XYTitleAnnotation titleAnn = new XYTitleAnnotation(0.01, 1, title, RectangleAnchor.TOP_LEFT);
                p.addAnnotation(titleAnn);
                combined_plot.add(p, 1);
            }
        });
        setChart(new JFreeChart("", new Font("SansSerif", Font.BOLD, 14), combined_plot, false));
    }

    private ITimelineVisualizer getVisualizer(IType type) {
        ITimelineVisualizer visualizer = visualizers.get(type.getClass());
        while (visualizer == null && type.getSuperclass() != null) {
            type = type.getSuperclass();
            visualizer = visualizers.get(type.getClass());
        }
        return visualizer;
    }
}
