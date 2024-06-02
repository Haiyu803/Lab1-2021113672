package hit;
import javax.swing.JFrame;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.view.mxGraph;
import com.mxgraph.swing.mxGraphComponent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class Node{
    //存储节点名称
    public String name;
    // 存储边
    public Map<String, Integer> edge;

    public Node(String name){
        this.name = name;
        this.edge = new HashMap<String, Integer>();
    }
}

public class Graph {
    Map<String, Node> graph;
    public Graph(){
        graph = new HashMap<String, Node>();
    }

    //可能有的没有出边，需要有一个空的Node用于绘图
    public void addNode(String name){
        Node node = new Node(name);
        graph.put(name, node);
    }

    public void addEdge(String in, String out){
        //获得in的node
        Node node = graph.get(in);
        if (!node.edge.containsKey(out)) {
            node.edge.put(out, 1);
        } else{
            node.edge.put(out, node.edge.get(out) + 1);
        }
    }

    public void showDirectedGraph(){
        mxGraph m_graph = new mxGraph();
        Map<String, Object> vertexMap = new HashMap<>();
        //初始化
        Object parent = m_graph.getDefaultParent();
        m_graph.getModel().beginUpdate();
        //创建绘图的节点和边
        try {
            for(String name : graph.keySet()){
                Object vertex = m_graph.insertVertex(parent, null, name, 20, 20, 80, 30);
                vertexMap.put(name, vertex);
            }
            for(String nameIn : graph.keySet()){
                for(String nameOut : graph.get(nameIn).edge.keySet()){
                    m_graph.insertEdge(parent, null, graph.get(nameIn).edge.get(nameOut),
                            vertexMap.get(nameIn), vertexMap.get(nameOut));
                }
            }
        } finally {
            m_graph.getModel().endUpdate();
        }

        // layout
        mxHierarchicalLayout layout = new mxHierarchicalLayout(m_graph);
        layout.execute(m_graph.getDefaultParent());
        mxGraphComponent graphComponent = new mxGraphComponent(m_graph);
        JFrame frame = new JFrame();
        frame.getContentPane().add(graphComponent, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 1000);
        frame.setVisible(true);        // layout

    }



}


