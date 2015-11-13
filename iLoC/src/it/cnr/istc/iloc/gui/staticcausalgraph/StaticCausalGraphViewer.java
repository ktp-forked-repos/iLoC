package it.cnr.istc.iloc.gui.staticcausalgraph;

import it.cnr.istc.iloc.api.IStaticCausalGraph;
import it.cnr.istc.iloc.api.IStaticCausalGraphListener;
import it.cnr.istc.iloc.api.IType;
import it.cnr.istc.iloc.gui.SolverManager;
import java.awt.geom.Rectangle2D;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.Layout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.render.PolygonRenderer;
import prefuse.render.Renderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.PrefuseLib;
import prefuse.visual.AggregateItem;
import prefuse.visual.AggregateTable;
import prefuse.visual.DecoratorItem;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

/**
 *
 * @author Riccardo De Benedictis
 */
public class StaticCausalGraphViewer extends Display implements IStaticCausalGraphListener {

    private static final String GRAPH = "graph";
    private static final String NODES = "graph.nodes";
    private static final String EDGES = "graph.edges";
    private static final String AGGR = "aggregates";
    private static final String NODE_COST = "node_cost";
    private static final String EDGE_DECORATORS = "edgeDeco";
    private static final String NODE_DECORATORS = "nodeDeco";
    private static final Schema DECORATOR_SCHEMA = PrefuseLib.getVisualItemSchema();

    static {
        DECORATOR_SCHEMA.setDefault(VisualItem.INTERACTIVE, false);
        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(128));
        DECORATOR_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma", 7));
    }
    private CostFunction costFunction = CostFunction.Default;
    private final Graph g = new Graph(true);
    private final VisualGraph vg;
    private final AggregateTable at;
    private final Map<IType, AggregateItem> types = new IdentityHashMap<>();
    private final Map<IStaticCausalGraph.INode, Node> nodes = new IdentityHashMap<>();
    private final Map<IStaticCausalGraph.IEdge, Edge> edges = new IdentityHashMap<>();
    private final ColorAction defaultNodeFill = new ColorAction(NODES, VisualItem.FILLCOLOR);
    private final ColorAction costNodeFill = new DataColorAction(NODES, NODE_COST, Constants.ORDINAL, VisualItem.FILLCOLOR, ColorLib.getHotPalette());

    public StaticCausalGraphViewer() {
        // initialize display and data
        super(new Visualization());

        g.getNodeTable().addColumn(VisualItem.LABEL, String.class);
        g.getNodeTable().addColumn(NODE_COST, Integer.class);
        g.getEdgeTable().addColumn(VisualItem.LABEL, String.class);

        // add visual data groups
        vg = m_vis.addGraph(GRAPH, g);

        m_vis.setInteractive(EDGES, null, false);
        m_vis.setValue(NODES, null, VisualItem.SHAPE, Constants.SHAPE_ELLIPSE);

        at = m_vis.addAggregates(AGGR);
        at.addColumn(VisualItem.POLYGON, float[].class);

        // set up the renderers
        // draw the nodes as basic shapes
        LabelRenderer nodeR = new LabelRenderer(VisualItem.LABEL);
        nodeR.setRoundedCorner(8, 8);

        // draw aggregates as polygons with curved edges
        Renderer polyR = new PolygonRenderer(Constants.POLY_TYPE_CURVE);
        ((PolygonRenderer) polyR).setCurveSlack(0.15f);

        DefaultRendererFactory drf = new DefaultRendererFactory();
        drf.setDefaultRenderer(nodeR);
        drf.setDefaultEdgeRenderer(new EdgeRenderer(Constants.EDGE_TYPE_CURVE));
        drf.add("ingroup('aggregates')", polyR);
        m_vis.setRendererFactory(drf);

        // adding decorators, one group for the nodes, one for the edges
        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(50));
        m_vis.addDecorators(EDGE_DECORATORS, EDGES, DECORATOR_SCHEMA);

        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(128));
        m_vis.addDecorators(NODE_DECORATORS, NODES, DECORATOR_SCHEMA);

        // set up the visual operators
        // first set up all the color actions
        ColorAction nStroke = new ColorAction(NODES, VisualItem.STROKECOLOR);
        nStroke.setDefaultColor(ColorLib.gray(100));
        nStroke.add(VisualItem.HOVER, ColorLib.gray(50));

        defaultNodeFill.setDefaultColor(ColorLib.gray(255));
        defaultNodeFill.add(VisualItem.HOVER, ColorLib.gray(200));

        costNodeFill.add(VisualItem.HOVER, ColorLib.gray(200));
        costNodeFill.setEnabled(false);

        ColorAction eStroke = new ColorAction(EDGES, VisualItem.STROKECOLOR);
        eStroke.setDefaultColor(ColorLib.gray(100));

        ColorAction eFill = new ColorAction(EDGES, VisualItem.FILLCOLOR);
        eFill.setDefaultColor(ColorLib.gray(100));

        ColorAction aStroke = new ColorAction(AGGR, VisualItem.STROKECOLOR);
        aStroke.setDefaultColor(ColorLib.gray(200));
        aStroke.add(VisualItem.HOVER, ColorLib.rgb(100, 255, 100));

        ColorAction aFill = new ColorAction(AGGR, VisualItem.FILLCOLOR);
        aFill.setDefaultColor(ColorLib.gray(220));

        // bundle the color actions
        ActionList colors = new ActionList();
        colors.add(nStroke);
        colors.add(defaultNodeFill);
        colors.add(costNodeFill);
        colors.add(eStroke);
        colors.add(eFill);
        colors.add(aStroke);
        colors.add(aFill);

        // now create the main layout routine
        ActionList layout = new ActionList(Activity.INFINITY);
        layout.add(colors);
        layout.add(new LabelLayout2(EDGE_DECORATORS));
        layout.add(new LabelLayout2(NODE_DECORATORS));
        layout.add(new ForceDirectedLayout(GRAPH, true));
        layout.add(new AggregateLayout(AGGR));
        layout.add(new RepaintAction());
        m_vis.putAction("layout", layout);

        // set up the display
        setHighQuality(true);
        addControlListener(new AggregateDragControl());
        addControlListener(new PanControl());
        addControlListener(new ZoomToFitControl());
        addControlListener(new WheelZoomControl());
    }

    public void init() {
        synchronized (m_vis) {
            g.getEdges().clear();
            g.getNodes().clear();
            at.clear();
            nodes.clear();
            edges.clear();
        }

        // set things running
        m_vis.run("layout");
    }

    public void setCostFunction(CostFunction costFunction) {
        this.costFunction = costFunction;
        synchronized (m_vis) {
            switch (costFunction) {
                case Default:
                    defaultNodeFill.setEnabled(true);
                    costNodeFill.setEnabled(false);
                    break;
                case Estimator:
                    nodes.keySet().forEach(node -> {
                        nodes.get(node).set(NODE_COST, (int) -SolverManager.getInstance().getSolver().getEstimator().estimate(node));
                    });
                    defaultNodeFill.setEnabled(false);
                    costNodeFill.setEnabled(true);
                    break;
                default:
                    throw new AssertionError(costFunction.name());
            }
        }
    }

    @Override
    public void typeAdded(IType type) {
        synchronized (m_vis) {
            AggregateItem aitem = (AggregateItem) at.addItem();
            types.put(type, aitem);
        }
    }

    @Override
    public void typeRemoved(IType type) {
        synchronized (m_vis) {
            if (types.containsKey(type)) {
                at.removeRow(types.remove(type).getRow());
            }
        }
    }

    @Override
    public void nodeAdded(IStaticCausalGraph.INode node) {
        synchronized (m_vis) {
            Node n = g.addNode();
            n.set(NODE_COST, 0);
            if (node instanceof IStaticCausalGraph.IPredicateNode) {
                n.setString(VisualItem.LABEL, ((IStaticCausalGraph.IPredicateNode) node).getPredicate().getName());
                if (((IStaticCausalGraph.IPredicateNode) node).getPredicate().getEnclosingScope() instanceof IType) {
                    types.get((IType) ((IStaticCausalGraph.IPredicateNode) node).getPredicate().getEnclosingScope()).addItem(m_vis.getVisualItem(NODES, n));
                }
            } else if (node instanceof IStaticCausalGraph.IDisjunctNode) {
                n.setString(VisualItem.LABEL, "AND");
            } else if (node instanceof IStaticCausalGraph.IDisjunctionNode) {
                n.setString(VisualItem.LABEL, "OR");
            } else if (node instanceof IStaticCausalGraph.INoOpNode) {
                n.setString(VisualItem.LABEL, "no-op");
            }
            nodes.put(node, n);
        }
    }

    @Override
    public void nodeRemoved(IStaticCausalGraph.INode node) {
        synchronized (m_vis) {
            if (nodes.containsKey(node)) {
                g.removeNode(nodes.remove(node));
            }
        }
    }

    @Override
    public void edgeAdded(IStaticCausalGraph.IEdge edge) {
        synchronized (m_vis) {
            Edge e = g.addEdge(nodes.get(edge.getSource()), nodes.get(edge.getTarget()));
            edges.put(edge, e);
        }
    }

    @Override
    public void edgeRemoved(IStaticCausalGraph.IEdge edge) {
        synchronized (m_vis) {
            if (edges.containsKey(edge)) {
                g.removeEdge(edges.get(edge));
            }
        }
    }

    /**
     * Set label positions. Labels are assumed to be DecoratorItem instances,
     * decorating their respective nodes. The layout simply gets the bounds of
     * the decorated node and assigns the label coordinates to the center of
     * those bounds.
     */
    private static class LabelLayout2 extends Layout {

        LabelLayout2(String group) {
            super(group);
        }

        @Override
        public void run(double frac) {
            Iterator<?> iter = m_vis.items(m_group);
            while (iter.hasNext()) {
                DecoratorItem decorator = (DecoratorItem) iter.next();
                VisualItem decoratedItem = decorator.getDecoratedItem();
                Rectangle2D bounds = decoratedItem.getBounds();

                double x = bounds.getCenterX();
                double y = bounds.getCenterY();

                setX(decorator, null, x);
                setY(decorator, null, y);
            }
        }
    }

    public enum CostFunction {
        Default, Estimator
    }
}
