package hit;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a node in the graph.
 */
class Node {
  // Node name
  public String name;
  // Edges from this node
  public Map<String, Integer> edge;

  public Node(String name) {
    this.name = name;
    this.edge = new HashMap<String, Integer>();
  }
}
