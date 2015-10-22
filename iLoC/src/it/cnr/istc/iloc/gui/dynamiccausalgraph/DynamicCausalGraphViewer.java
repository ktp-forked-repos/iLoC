package it.cnr.istc.iloc.gui.dynamiccausalgraph;

import it.cnr.istc.iloc.api.FormulaState;
import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IDynamicCausalGraphListener;
import it.cnr.istc.iloc.api.IFormula;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.layout.Layout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
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
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.PrefuseLib;
import prefuse.visual.DecoratorItem;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DynamicCausalGraphViewer extends Display implements IDynamicCausalGraphListener {

    private static final String GRAPH = "graph";
    private static final String NODES = "graph.nodes";
    private static final String EDGES = "graph.edges";
    private static final String NODE_TYPE = "node_type";
    private static final String EDGE_TYPE = "edge_type";
    private static final String EDGE_DECORATORS = "edgeDeco";
    private static final String NODE_DECORATORS = "nodeDeco";
    private static final Schema DECORATOR_SCHEMA = PrefuseLib.getVisualItemSchema();
    private final Graph g = new Graph(true);
    private final VisualGraph vg;
    private final Map<IFormula, Node> nodes = new IdentityHashMap<>();
    private final Map<IFormula, Map<IFormula, Edge>> edges = new IdentityHashMap<>();
    private final Map<IFormula, Unification> unifications = new IdentityHashMap<>();

    static {
        DECORATOR_SCHEMA.setDefault(VisualItem.INTERACTIVE, false);
        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(128));
        DECORATOR_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma", 7));
    }

    public DynamicCausalGraphViewer() {
        // initialize display and data
        super(new Visualization());

        g.getNodeTable().addColumn(VisualItem.LABEL, String.class);
        g.getNodeTable().addColumn(NODE_TYPE, String.class);
        g.getEdgeTable().addColumn(VisualItem.LABEL, String.class);
        g.getEdgeTable().addColumn(EDGE_TYPE, String.class);

        // add visual data groups
        vg = m_vis.addGraph(GRAPH, g);

        m_vis.setInteractive(EDGES, null, false);
        m_vis.setValue(NODES, null, VisualItem.SHAPE, Constants.SHAPE_ELLIPSE);

        // set up the renderers
        // draw the nodes as basic shapes
        LabelRenderer nodeR = new LabelRenderer(VisualItem.LABEL);
        nodeR.setRoundedCorner(8, 8);

        DefaultRendererFactory drf = new DefaultRendererFactory();
        drf.setDefaultRenderer(nodeR);
        drf.setDefaultEdgeRenderer(new EdgeRenderer(Constants.EDGE_TYPE_CURVE));
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

        ColorAction nFill = new ColorAction(NODES, VisualItem.FILLCOLOR);
        // ColorAction nFill = new DataColorAction(NODES, NODE_TYPE, Constants.NOMINAL, VisualItem.FILLCOLOR);
        nFill.setDefaultColor(ColorLib.gray(255));
        nFill.add(VisualItem.HOVER, ColorLib.gray(200));
        nFill.add(NODE_TYPE + " == \"" + FormulaState.Inactive + "\"", ColorLib.rgb(218, 255, 255));
        nFill.add(NODE_TYPE + " == \"" + FormulaState.Unified + "\"", ColorLib.rgb(212, 251, 212));

        ColorAction eStroke = new ColorAction(EDGES, VisualItem.STROKECOLOR);
        eStroke.setDefaultColor(ColorLib.gray(100));
        eStroke.add(EDGE_TYPE + " == \"p_merge\"", ColorLib.gray(230));
        eStroke.add(EDGE_TYPE + " == \"merge\"", ColorLib.rgb(182, 251, 182));

        ColorAction eFill = new ColorAction(EDGES, VisualItem.FILLCOLOR);
        eFill.setDefaultColor(ColorLib.gray(100));
        eFill.add(EDGE_TYPE + " == \"p_merge\"", ColorLib.gray(230));
        eFill.add(EDGE_TYPE + " == \"merge\"", ColorLib.rgb(182, 251, 182));

        // bundle the color actions
        ActionList colors = new ActionList();
        colors.add(nStroke);
        colors.add(nFill);
        colors.add(eStroke);
        colors.add(eFill);

        // now create the main layout routine
        ActionList layout = new ActionList(Activity.INFINITY);
        layout.add(colors);
        layout.add(new LabelLayout2(EDGE_DECORATORS));
        layout.add(new LabelLayout2(NODE_DECORATORS));
        layout.add(new ForceDirectedLayout(GRAPH, true));
        layout.add(new RepaintAction());
        m_vis.putAction("layout", layout);

        // set up the display
        setHighQuality(true);
        addControlListener(new DragControl());
        addControlListener(new PanControl());
        addControlListener(new ZoomToFitControl());
        addControlListener(new WheelZoomControl());
        addControlListener(new ControlAdapter() {

            @Override
            public void itemEntered(VisualItem vi, MouseEvent me) {
                Display d = (Display) me.getSource();
                if (vi.getSourceTuple() instanceof Node) {
                    Node nodeData = (Node) vi.getSourceTuple();
                    Optional<IFormula> formula = nodes.entrySet().stream().filter(entry -> entry.getValue() == nodeData).map(entry -> entry.getKey()).findAny();
                    formula.ifPresent(f -> {
                        d.setToolTipText("<html>" + f.toString() + "</html>");
                    });
                }
            }

            @Override
            public void itemExited(VisualItem vi, MouseEvent me) {
                Display d = (Display) me.getSource();
                d.setToolTipText(null);
            }
        });
    }

    public void init() {
        synchronized (m_vis) {
            g.getEdges().clear();
            g.getNodes().clear();
            nodes.clear();
            edges.clear();
            unifications.clear();
        }

        // set things running
        m_vis.run("layout");
    }

    @Override
    public void formulaAdded(IFormula formula) {
        synchronized (m_vis) {
            Node n = g.addNode();
            n.set(VisualItem.LABEL, formula.getType().getName());
            n.set(NODE_TYPE, formula.getFormulaState().name());
            nodes.put(formula, n);
            if (formula.getCause() != null) {
                Edge e = g.addEdge(nodes.get(formula.getCause()), n);
                if (!edges.containsKey(formula)) {
                    edges.put(formula, new IdentityHashMap<>());
                }
                edges.get(formula).put(formula.getCause(), e);
            }
        }
    }

    @Override
    public void formulaRemoved(IFormula formula) {
        synchronized (m_vis) {
            g.removeNode(nodes.remove(formula));
        }
    }

    @Override
    public void formulaActivated(IFormula formula) {
        synchronized (m_vis) {
            nodes.get(formula).set(NODE_TYPE, formula.getFormulaState().name());
            if (!edges.containsKey(formula)) {
                edges.put(formula, new IdentityHashMap<>());
            }
        }
    }

    @Override
    public void formulaUnified(IFormula formula, List<IFormula> with, List<IBool> terms) {
        synchronized (m_vis) {
            nodes.get(formula).set(NODE_TYPE, formula.getFormulaState().name());
            unifications.put(formula, new Unification(formula, with, terms));
            if (!edges.containsKey(formula)) {
                edges.put(formula, new IdentityHashMap<>());
            }
            with.stream().forEach(to -> {
                Edge e = g.addEdge(nodes.get(formula), nodes.get(to));
                edges.get(formula).put(to, e);
            });
        }
    }

    @Override
    public void formulaInactivated(IFormula formula) {
        synchronized (m_vis) {
            if (nodes.containsKey(formula)) {
                nodes.get(formula).set(NODE_TYPE, formula.getFormulaState().name());
                if (unifications.containsKey(formula)) {
                    Unification merge = unifications.remove(formula);
                    merge.with.stream().forEach(with -> {
                        g.removeEdge(edges.get(formula).remove(with));
                    });
                    if (edges.get(formula).isEmpty()) {
                        edges.remove(formula);
                    }
                }
            }
        }
    }

    private static class Unification {

        private final IFormula formula;
        private final List<IFormula> with;
        private final List<IBool> terms;

        Unification(IFormula formula, List<IFormula> with, List<IBool> terms) {
            this.formula = formula;
            this.with = with;
            this.terms = terms;
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
}
