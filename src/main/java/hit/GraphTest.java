package hit;

public class GraphTest {
    public static void main(String[] args) {
        Graph graph = new Graph();

        // 添加节点
        graph.addNode("Node 1");
        graph.addNode("Node 2");
        graph.addNode("Node 3");

        // 添加边
        graph.addEdge("Node 1", "Node 2");
        graph.addEdge("Node 2", "Node 3");
        graph.addEdge("Node 3", "Node 1");
        graph.addEdge("Node 1", "Node 2");
        graph.addEdge("Node 2", "Node 1");
        // 绘制图形
        graph.showDirectedGraph();
    }
}