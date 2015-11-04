package it.cnr.istc.iloc.gui.resolutiontree;

import it.cnr.istc.iloc.api.IFlaw;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INode;
import it.cnr.istc.iloc.api.ISolverListener;
import it.cnr.istc.iloc.gui.SolverManager;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.Layout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.activity.Activity;
import prefuse.controls.ControlAdapter;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tree;
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
public class ResolutionTreeView extends Display implements ISolverListener {

    private static final String GRAPH = "tree";
    private static final String NODES = "tree.nodes";
    private static final String EDGES = "tree.edges";
    private static final String NODE_COST_ESTIMATOR_BASED = "node_cost_heuristic_based";
    private static final String NODE_COST_FLAW_NUMBER_BASED = "node_cost_flaw_number_based";
    private static final String NODE_TYPE = "node_type";
    private static final String EDGE_DECORATORS = "edgeDeco";
    private static final String NODE_DECORATORS = "nodeDeco";
    private static final Schema DECORATOR_SCHEMA = PrefuseLib.getVisualItemSchema();
    private final Tree t = new Tree();
    private final VisualGraph vg;
    private final Map<INode, Node> nodes = new IdentityHashMap<>();
    private final ColorAction defaultNodeFill = new ColorAction(NODES, VisualItem.FILLCOLOR);
    private final ColorAction costNodeHeuristicBasedFill = new DataColorAction(NODES, NODE_COST_ESTIMATOR_BASED, Constants.ORDINAL, VisualItem.FILLCOLOR, ColorLib.getHotPalette());
    private final ColorAction costNodeFlawNumberBasedFill = new DataColorAction(NODES, NODE_COST_FLAW_NUMBER_BASED, Constants.ORDINAL, VisualItem.FILLCOLOR, ColorLib.getHotPalette());

    static {
        DECORATOR_SCHEMA.setDefault(VisualItem.INTERACTIVE, false);
        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(128));
        DECORATOR_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma", 7));
    }

    public ResolutionTreeView() {
        // initialize display and data
        super(new Visualization());

        t.getNodeTable().addColumn(VisualItem.LABEL, String.class);
        t.getNodeTable().addColumn(NODE_TYPE, String.class);
        t.getNodeTable().addColumn(NODE_COST_ESTIMATOR_BASED, Integer.class);
        t.getNodeTable().addColumn(NODE_COST_FLAW_NUMBER_BASED, Integer.class);
        t.getEdgeTable().addColumn(VisualItem.LABEL, String.class);

        // add visual data groups
        vg = m_vis.addGraph(GRAPH, t);

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

        defaultNodeFill.add(VisualItem.HOVER, ColorLib.gray(200));
        defaultNodeFill.add(NODE_TYPE + " == \"default\"", ColorLib.rgb(230, 230, 250));
        defaultNodeFill.add(NODE_TYPE + " == \"current\"", ColorLib.rgb(255, 255, 204));
        defaultNodeFill.add(NODE_TYPE + " == \"inconsistent\"", ColorLib.rgb(255, 69, 0));
        defaultNodeFill.add(NODE_TYPE + " == \"solution\"", ColorLib.rgb(152, 251, 152));
        defaultNodeFill.add(NODE_TYPE + " == \"optimum\"", ColorLib.rgb(164, 255, 164));

        costNodeHeuristicBasedFill.add(VisualItem.HOVER, ColorLib.gray(200));
        costNodeHeuristicBasedFill.setEnabled(false);

        costNodeFlawNumberBasedFill.add(VisualItem.HOVER, ColorLib.gray(200));
        costNodeFlawNumberBasedFill.setEnabled(false);

        ColorAction eStroke = new ColorAction(EDGES, VisualItem.STROKECOLOR);
        eStroke.setDefaultColor(ColorLib.gray(100));

        ColorAction eFill = new ColorAction(EDGES, VisualItem.FILLCOLOR);
        eFill.setDefaultColor(ColorLib.gray(100));

        // bundle the color actions
        ActionList colors = new ActionList();
        colors.add(nStroke);
        colors.add(defaultNodeFill);
        colors.add(costNodeHeuristicBasedFill);
        colors.add(costNodeFlawNumberBasedFill);
        colors.add(eStroke);
        colors.add(eFill);

        NodeLinkTreeLayout treeLayout = new NodeLinkTreeLayout(GRAPH, Constants.ORIENT_TOP_BOTTOM, 50, 250, 250);
        treeLayout.setLayoutAnchor(new Point2D.Double());

        // now create the main layout routine
        ActionList layout = new ActionList(Activity.INFINITY);
        layout.add(colors);
        layout.add(new LabelLayout2(EDGE_DECORATORS));
        layout.add(new LabelLayout2(NODE_DECORATORS));
        layout.add(treeLayout);
        layout.add(new RepaintAction());
        m_vis.putAction("layout", layout);

        // set up the display
        setHighQuality(true);
        addControlListener(new PanControl());
        addControlListener(new ZoomToFitControl());
        addControlListener(new WheelZoomControl());
        addControlListener(new ControlAdapter() {

            @Override
            public void itemEntered(VisualItem vi, MouseEvent me) {
                Display d = (Display) me.getSource();
                if (vi.getSourceTuple() instanceof Node) {
                    Node nodeData = (Node) vi.getSourceTuple();
                    Optional<INode> node = nodes.entrySet().stream().filter(entry -> entry.getValue() == nodeData).map(entry -> entry.getKey()).findAny();
                    node.ifPresent(n -> {
                        d.setToolTipText("<html>" + String.join("<br>", n.getFlaws().stream().sorted((IFlaw f1, IFlaw f2) -> Double.compare(f1.getEstimatedCost(), f2.getEstimatedCost())).map(flaw -> "(" + flaw.getEstimatedCost() + ") " + flaw.getClass().getSimpleName()).toArray(String[]::new)) + "</html>");
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

    public void setCostFunction(CostFunction costFunction) {
        synchronized (m_vis) {
            switch (costFunction) {
                case Default:
                    defaultNodeFill.setEnabled(true);
                    costNodeHeuristicBasedFill.setEnabled(false);
                    costNodeFlawNumberBasedFill.setEnabled(false);
                    break;
                case HeuristicBased:
                    defaultNodeFill.setEnabled(false);
                    costNodeHeuristicBasedFill.setEnabled(true);
                    costNodeFlawNumberBasedFill.setEnabled(false);
                    break;
                case FlawNumberBased:
                    defaultNodeFill.setEnabled(false);
                    costNodeHeuristicBasedFill.setEnabled(false);
                    costNodeFlawNumberBasedFill.setEnabled(true);
                    break;
                default:
                    throw new AssertionError(costFunction.name());
            }
        }
    }

    public void init() {
        synchronized (m_vis) {
            t.getEdges().clear();
            t.getNodes().clear();
            nodes.clear();
            Node root = t.addRoot();
            INode node = SolverManager.getInstance().getSolver().getCurrentNode();
            root.set(VisualItem.LABEL, Integer.toString(node.getFlaws().size()));
            root.set(NODE_TYPE, "default");
            root.set(NODE_COST_ESTIMATOR_BASED, (int) -(node.getKnownCost() + node.getFlaws().stream().mapToDouble(flaw -> flaw.getEstimatedCost()).sum()));
            root.set(NODE_COST_FLAW_NUMBER_BASED, -node.getFlaws().size());
            Rectangle2D bounds = m_vis.getVisualItem(NODES, root).getBounds();
            panToAbs(new Point2D.Double(bounds.getCenterX(), bounds.getCenterY()));
            nodes.put(node, root);
        }

        // set things running
        m_vis.run("layout");
    }

    @Override
    public void contentChanged(IModel model, INode n) {
        synchronized (m_vis) {
            if (nodes.containsKey(n)) {
                nodes.get(n).set(NODE_TYPE, "current");
                nodes.get(n).set(VisualItem.LABEL, Integer.toString(n.getFlaws().size()));
                nodes.get(n).set(NODE_COST_ESTIMATOR_BASED, (int) -(n.getKnownCost() + n.getFlaws().stream().mapToDouble(flaw -> flaw.getEstimatedCost()).sum()));
                nodes.get(n).set(NODE_COST_FLAW_NUMBER_BASED, -n.getFlaws().size());
            }
        }
    }

    @Override
    public void currentNode(INode n) {
        synchronized (m_vis) {
            nodes.get(n).set(NODE_TYPE, "current");
            Rectangle2D bounds = m_vis.getVisualItem(NODES, nodes.get(n)).getBounds();
            panToAbs(new Point2D.Double(bounds.getCenterX(), bounds.getCenterY()));
        }
    }

    @Override
    public void inconsistentNode(INode n) {
        synchronized (m_vis) {
            nodes.get(n).set(NODE_TYPE, "inconsistent");
        }
    }

    @Override
    public void solutionNode(IModel model, INode n) {
        synchronized (m_vis) {
            nodes.get(n).set(NODE_TYPE, "solution");
        }
    }

    @Override
    public void bestNode(IModel model, INode n) {
        synchronized (m_vis) {
            nodes.get(n).set(NODE_TYPE, "optimum");
        }
    }

    @Override
    public void branch(INode n, List<INode> childs) {
        synchronized (m_vis) {
            nodes.get(n).set(NODE_TYPE, "default");
            childs.forEach(child -> {
                Node c_c = t.addChild(nodes.get(n));
                c_c.set(NODE_TYPE, "default");
                c_c.set(VisualItem.LABEL, Integer.toString(n.getFlaws().size()));
                c_c.set(NODE_COST_ESTIMATOR_BASED, (int) -(child.getKnownCost() + child.getFlaws().stream().mapToDouble(flaw -> flaw.getEstimatedCost()).sum()));
                c_c.set(NODE_COST_FLAW_NUMBER_BASED, -child.getFlaws().size());
                c_c.getParentEdge().set(VisualItem.LABEL, child.getResolvers().stream().map(res -> res.getClass().getSimpleName()).collect(Collectors.toList()).toString());
                nodes.put(child, c_c);
            });
        }
    }

    public enum CostFunction {

        Default, HeuristicBased, FlawNumberBased;
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
            synchronized (m_vis) {
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
}
