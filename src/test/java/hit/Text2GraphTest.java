package hit;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Text2GraphTest {

    @Test
    public void testGenerateNewText_EmptyString() {
        Graph graph = new Graph();
        Text2Graph text2Graph = new Text2Graph(graph);
        String result = text2Graph.generateNewText("");
        assertEquals("", result);
    }

    @Test
    public void testGenerateNewText_SingleWord() {
        Graph graph = new Graph();
        Text2Graph text2Graph = new Text2Graph(graph);
        String result = text2Graph.generateNewText("hello");
        assertEquals("hello", result);
    }

    @Test
    public void testGenerateNewText_TwoWords() {
        Graph graph = new Graph();
        graph.addNode("hello");
        graph.addNode("world");

        Text2Graph text2Graph = new Text2Graph(graph);
        String result = text2Graph.generateNewText("hello world");
        assertEquals("hello world", result);
    }

    @Test
    public void testGenerateNewText_MultipleWords() {
        Graph graph = new Graph();
        graph.addNode("this");
        graph.addNode("is");
        graph.addNode("a");
        graph.addNode("test");
        graph.addNode("bridge1");
        graph.addNode("bridge2");
        graph.addNode("bridge3");
        graph.addEdge("this", "bridge1");
        graph.addEdge("bridge1", "is");
        graph.addEdge("is", "bridge2");
        graph.addEdge("bridge2", "a");
        graph.addEdge("a", "bridge3");
        graph.addEdge("bridge3", "test");

        Text2Graph text2Graph = new Text2Graph(graph);
        String result = text2Graph.generateNewText("this is a test");
        assertEquals("this bridge1 is bridge2 a bridge3 test", result);
    }

    @Test
    public void testGenerateNewText_TwoWords_singleBridgeWords() {
        Graph graph = new Graph();
        graph.addNode("hello");
        graph.addNode("world");
        graph.addNode("bridge");
        graph.addEdge("hello", "bridge");
        graph.addEdge("bridge", "world");

        Text2Graph text2Graph = new Text2Graph(graph);
        String result = text2Graph.generateNewText("hello world");
        assertEquals("hello bridge world", result);
    }
}
