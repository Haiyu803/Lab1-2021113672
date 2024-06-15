package hit;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

/**
 * Class representing a directed graph.
 */
public class Graph {
  Map<String, Node> graph;

  public Graph() {
    graph = new HashMap<String, Node>();
  }

  /**
   * Constructor to initialize the graph with another Graph object.
   *
   * @param originalGraph the original graph to be copied
   */
  public Graph(Graph originalGraph) {
    this.graph = new HashMap<>();
    // Copy nodes and edges from originalGraph
    for (Map.Entry<String, Node> entry : originalGraph.graph.entrySet()) {
      String nodeName = entry.getKey();
      Node originalNode = entry.getValue();
      Node newNode = new Node(originalNode.toString()); // Assuming Node has a copy constructor
      graph.put(nodeName, newNode);
    }
  }

  // Adds a node to the graph
  public void addNode(String name) {
    Node node = new Node(name);
    graph.put(name, node);
  }

  /**
   * Adds an edge to the graph.
   *
   * @param in  the starting node of the edge
   * @param out the ending node of the edge
   */
  public void addEdge(String in, String out) {
    Node node = graph.get(in);
    if (!node.edge.containsKey(out)) {
      node.edge.put(out, 1);
    } else {
      node.edge.put(out, node.edge.get(out) + 1);
    }
  }

  /**
   * Displays the directed graph using mxGraph.
   */
  public void showDirectedGraph() {
    mxGraph mxGraphInstance = new mxGraph();
    Map<String, Object> vertexMap = new HashMap<>();
    Object parent = mxGraphInstance.getDefaultParent();
    mxGraphInstance.getModel().beginUpdate();

    try {
      for (String key : graph.keySet()) {
        Object v1 = mxGraphInstance.insertVertex(parent, null, key, 20, 20, 80, 30);
        vertexMap.put(key, v1);
      }

      for (Map.Entry<String, Node> entry : graph.entrySet()) {
        String key = entry.getKey();
        Node node = entry.getValue();
        for (Map.Entry<String, Integer> edge : node.edge.entrySet()) {
          Object v1 = vertexMap.get(key);
          Object v2 = vertexMap.get(edge.getKey());
          mxGraphInstance.insertEdge(parent, null, edge.getValue(), v1, v2);
        }
      }
    } finally {
      mxGraphInstance.getModel().endUpdate();
    }

    mxHierarchicalLayout layout = new mxHierarchicalLayout(mxGraphInstance);
    layout.execute(mxGraphInstance.getDefaultParent());
    mxGraphComponent graphComponent = new mxGraphComponent(mxGraphInstance);
    JFrame frame = new JFrame();
    frame.getContentPane().add(graphComponent, BorderLayout.CENTER);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 1000);
    frame.setVisible(true);
  }
}
