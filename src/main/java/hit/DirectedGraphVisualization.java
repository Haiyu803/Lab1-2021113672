package hit;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;

public class DirectedGraphVisualization {

    public static void main(String[] args) {
        // 创建图形对象
        mxGraph graph = new mxGraph();

        // 创建图形容器
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // 创建节点和边
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            Object v1 = graph.insertVertex(parent, null, "Node 1", 20, 20, 80, 30);
            Object v2 = graph.insertVertex(parent, null, "Node 2", 180, 150, 80, 30);
            graph.insertEdge(parent, null, "", v1, v2);
        } finally {
            graph.getModel().endUpdate();
        }

        // 将图形添加到图形容器
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        frame.getContentPane().add(graphComponent);

        // 显示图形
        frame.setVisible(true);
    }
}
