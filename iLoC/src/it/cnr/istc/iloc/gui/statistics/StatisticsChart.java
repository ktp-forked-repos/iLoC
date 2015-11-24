package it.cnr.istc.iloc.gui.statistics;

import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INode;
import it.cnr.istc.iloc.api.ISolverListener;
import it.cnr.istc.iloc.gui.SolverManager;
import java.awt.BasicStroke;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Riccardo De Benedictis
 */
public class StatisticsChart extends ChartPanel implements ISolverListener {

    private static final JFreeChart CHART = new JFreeChart(new XYPlot());
    private final XYSeriesCollection solver_collection = new XYSeriesCollection();
    private final XYSeriesCollection heuristics_collection = new XYSeriesCollection();
    private final XYSeriesCollection memory_collection = new XYSeriesCollection();
    private final XYSeries fringe = new XYSeries("Fringe");
    private final XYSeries nodes = new XYSeries("Nodes");
    private final XYSeries flaws = new XYSeries("Flaws");
    private final XYSeries depth = new XYSeries("Depth");
    private final XYSeries heuristic = new XYSeries("Heuristic");
    private final XYSeries max_memory = new XYSeries("Max memory");
    private final XYSeries total_memory = new XYSeries("Total memory");
    private final XYSeries used_memory = new XYSeries("Used memory");
    private int c_step = 0;

    public StatisticsChart() {
        super(CHART);
        setBorder(BorderFactory.createEtchedBorder());

        final CombinedDomainXYPlot combined_plot = new CombinedDomainXYPlot(new NumberAxis(""));
        combined_plot.setGap(3.0);
        combined_plot.setOrientation(PlotOrientation.VERTICAL);

        solver_collection.addSeries(fringe);
        solver_collection.addSeries(nodes);
        solver_collection.addSeries(depth);

        XYLineAndShapeRenderer solver_renderer = new XYLineAndShapeRenderer();
        solver_renderer.setSeriesShapesVisible(0, false);
        solver_renderer.setSeriesStroke(0, new BasicStroke(1.5f));
        solver_renderer.setSeriesShapesVisible(1, false);
        solver_renderer.setSeriesStroke(1, new BasicStroke(1.5f));
        solver_renderer.setSeriesShapesVisible(2, false);
        solver_renderer.setSeriesStroke(2, new BasicStroke(1.5f));

        XYPlot solver_plot = new XYPlot(solver_collection, null, new NumberAxis(""), solver_renderer);
        solver_plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        combined_plot.add(solver_plot);

        heuristics_collection.addSeries(flaws);
        heuristics_collection.addSeries(heuristic);

        XYLineAndShapeRenderer heuristics_renderer = new XYLineAndShapeRenderer();
        heuristics_renderer.setSeriesShapesVisible(0, false);
        heuristics_renderer.setSeriesStroke(0, new BasicStroke(1.5f));
        heuristics_renderer.setSeriesShapesVisible(1, false);
        heuristics_renderer.setSeriesStroke(1, new BasicStroke(1.5f));
        heuristics_renderer.setSeriesShapesVisible(2, false);
        heuristics_renderer.setSeriesStroke(2, new BasicStroke(1.5f));
        heuristics_renderer.setSeriesShapesVisible(3, false);
        heuristics_renderer.setSeriesStroke(3, new BasicStroke(1.5f));

        XYPlot heuristics_plot = new XYPlot(heuristics_collection, null, new NumberAxis(""), heuristics_renderer);
        heuristics_plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        combined_plot.add(heuristics_plot);

        memory_collection.addSeries(max_memory);
        memory_collection.addSeries(total_memory);
        memory_collection.addSeries(used_memory);

        XYLineAndShapeRenderer memory_renderer = new XYLineAndShapeRenderer();
        memory_renderer.setSeriesShapesVisible(0, false);
        memory_renderer.setSeriesStroke(0, new BasicStroke(1.5f));
        memory_renderer.setSeriesShapesVisible(1, false);
        memory_renderer.setSeriesStroke(1, new BasicStroke(1.5f));
        memory_renderer.setSeriesShapesVisible(2, false);
        memory_renderer.setSeriesStroke(2, new BasicStroke(1.5f));
        memory_renderer.setSeriesShapesVisible(3, false);
        memory_renderer.setSeriesStroke(3, new BasicStroke(1.5f));

        XYPlot memory_plot = new XYPlot(memory_collection, null, new NumberAxis(""), memory_renderer);
        memory_plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        combined_plot.add(memory_plot);

        setChart(new JFreeChart("", new Font("SansSerif", Font.BOLD, 14), combined_plot, true));
    }

    public void init() {
        c_step = 0;
        nodes.clear();
        fringe.clear();
        depth.clear();
        flaws.clear();
        heuristic.clear();
    }

    @Override
    public void contentChanged(IModel model, INode n) {
        newStep(n);
    }

    @Override
    public void currentNode(INode n) {
        newStep(n);
    }

    private void newStep(INode n) {
        nodes.add(c_step, SolverManager.getInstance().getSolver().getSearchSpaceSize());
        fringe.add(c_step, SolverManager.getInstance().getSolver().getFringeSize());
        depth.add(c_step, n.getLevel());

        flaws.add(c_step, n.getFlaws().size());
        heuristic.add(c_step, n.getKnownCost() + n.getFlaws().stream().mapToDouble(flaw -> flaw.getEstimatedCost()).sum());

        max_memory.add(c_step, Runtime.getRuntime().maxMemory());
        total_memory.add(c_step, Runtime.getRuntime().totalMemory());
        used_memory.add(c_step, Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

        c_step++;
    }

    @Override
    public void inconsistentNode(INode n) {
    }

    @Override
    public void solutionNode(IModel model, INode n) {
    }

    @Override
    public void bestNode(IModel model, INode n) {
    }

    @Override
    public void branch(INode n, List<INode> childs) {
    }
}
