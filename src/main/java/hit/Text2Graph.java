package hit;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Text2Graph {
    Graph cGraph;
    public Text2Graph(Graph cGraph){
        this.cGraph = cGraph;
    }

    public Vector<String> getBridgeWords(String word1, String word2){
        Map<String, Node> graph = cGraph.graph;
        Vector<String> bridgeWords = new Vector<String>();
        if(!graph.containsKey(word1) || !graph.containsKey(word2)){
            return bridgeWords;
        }
        //获得word1的所有出边达到顶点
        for(String word : graph.get(word1).edge.keySet()){
            if(graph.get(word).edge.containsKey(word2)){
                bridgeWords.add(word);
            }
        }
        return bridgeWords;
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
            if(graph.get(word).edge.containsKey(word2)){
                bridgeWords.add(word);
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

    public String generateNewText(String inputText){
        StringBuilder outputText = new StringBuilder();
        String[] words = inputText.split(" ");
        for(int i = 0; i < words.length-1; i++){
            outputText.append(words[i]).append(" ");
            Vector<String> bridgeWords = getBridgeWords(words[i].toLowerCase(), words[i+1].toLowerCase());
            if(!bridgeWords.isEmpty()){
                outputText.append(bridgeWords.get(0)).append(" ");
            }
        }
        outputText.append(words[words.length-1]);
        return outputText.toString();
    }

    public String calcShortestPath(String word1, String word2) {
        Map<String, Map<String, Integer>> Dist = new HashMap<>();
        Map<String, Map<String, String>> Next = new HashMap<>();

        for (String word : cGraph.graph.keySet()) {
            Dist.put(word, new HashMap<>());
            Next.put(word, new HashMap<>());
            for (String toWord : cGraph.graph.get(word).edge.keySet()) {
                Dist.get(word).put(toWord, cGraph.graph.get(word).edge.get(toWord));
                Next.get(word).put(toWord, toWord);
            }
        }

        for (String k : cGraph.graph.keySet()) {
            for (String i : cGraph.graph.keySet()) {
                for (String j : cGraph.graph.keySet()) {
                    int distIK = Dist.get(i).getOrDefault(k, Integer.MAX_VALUE);
                    int distKJ = Dist.get(k).getOrDefault(j, Integer.MAX_VALUE);
                    int distIJ = Dist.get(i).getOrDefault(j, Integer.MAX_VALUE);
                    if (distIK != Integer.MAX_VALUE && distKJ != Integer.MAX_VALUE && distIK + distKJ < distIJ) {
                        Dist.get(i).put(j, distIK + distKJ);
                        Next.get(i).put(j, Next.get(i).get(k));
                    }
                }
            }
        }

        if (word1.equals(word2)) {
            return "Shortest path between " + word1 + " and " + word2 + ": " + word1 + " with length 0";
        }

        if (!Dist.containsKey(word1) || !Dist.get(word1).containsKey(word2)) {
            return "No path found between " + word1 + " and " + word2;
        }

        List<String> path = new ArrayList<>();
        String current = word1;
        while (!current.equals(word2)) {
            path.add(current);
            current = Next.get(current).get(word2);
        }

        path.add(word2);

        int shortestPathLength = Dist.get(word1).get(word2);
        return "Shortest path between " + word1 + " and " + word2 + ": " + String.join("->", path) + " with length " + shortestPathLength;
    }

    public String randomWalk(){
        if(cGraph.graph.isEmpty()){
            System.out.println("No walk found!");
            return null;
        }
        Random rand = new Random();
        List<String> nodes = new ArrayList<>();
        List<String> edges = new ArrayList<>();
        Map<String,List<String>> remind =new HashMap<>();
        String current = new ArrayList<>(cGraph.graph.keySet()).get(rand.nextInt(cGraph.graph.keySet().size()));
        nodes.add(current);
        while(true){
            if(!remind.containsKey(current)){
                List<String> temp = new ArrayList<>();
                remind.put(current,temp);
            }
            List<String> neighborsFormer = new ArrayList<>(cGraph.graph.get(current).edge.keySet());
            List<String> neighborsPassed = remind.get(current);//已经遍历过的neighbor
            List<String> neighbors = neighborsFormer.stream().filter(n -> !neighborsPassed.contains(n)).collect(Collectors.toList());
            //取差集，即未遍历过的neighbor
            System.out.println(neighbors);
            if(neighbors.isEmpty()){
                break;
            }
            String next = neighbors.get(rand.nextInt(neighbors.size()));
            remind.get(current).add(next);
//            if(edges.contains(current+"->"+next)){
//                break;
//            }
            nodes.add(next);
            edges.add(current+"->"+next);
            current = next;
            System.out.println("Nodes: " + nodes);
            System.out.println("Edges: " + edges);
            System.out.println("Continue? (y/n)");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(!input.equals("y")){
                break;
            }
        }
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < nodes.size(); i++){
            builder.append(nodes.get(i));
            if(i<nodes.size()-1){
                builder.append(" ");
            }
        }

        try{
            String filename = "./random_walk.txt";
//            System.out.println("Writing to file: " + filename);
            FileWriter fw = new FileWriter(filename);
            fw.write(builder.toString());
            fw.close();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        return builder.toString();
    }
}
