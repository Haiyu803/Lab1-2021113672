package hit;

import java.util.Map;
import java.util.Vector;

public class Text2Graph {
    Graph cGraph;
    public Text2Graph(Graph cGraph){
        this.cGraph = cGraph;
    }

    public String queryBridgeWords(String word1, String word2){
        Map<String, Node> graph = cGraph.graph;
        Vector<String> bridgeWords = new Vector<String>();
        String message = "";
        if(!graph.containsKey(word1) || !graph.containsKey(word2)){
            message = String.format("No %s or %s in the graph!", word1, word2);
            return message;
        }
        //获得word1的所有出边达到顶点
        for(String word : graph.get(word1).edge.keySet()){
//            System.out.println("yes!");
            if(graph.get(word).edge.containsKey(word2)){
                bridgeWords.add(word);
//                System.out.println("yes");
//                System.out.println(bridgeWords.size());
            }
        }
        if (bridgeWords.isEmpty()){
            message = String.format("No bridge words from %s to %s!", word1, word2);
            return message;
        }else {
            StringBuilder builder = new StringBuilder();
            builder.append("The bridge words from ").append(word1).append(" to ").append(word2).append(" are: ");
            for (String bridgeWord : bridgeWords) {
                builder.append(bridgeWord).append(" ");
            }
            message = builder.toString();
            return message;
        }


    }

//    public String generateNewText(String inputText){
//
//    }
//
//    public String calcShortestPath(String word1, String word2){
//
//    }
//
//    public String randomWalk(){
//
//    }
}
