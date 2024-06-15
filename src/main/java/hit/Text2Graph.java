package hit;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

/**
 * A class to convert text into a graph and provide various functionalities such as finding
 * bridge words,generating new text, calculating the shortest path, and performing a random walk.
 */
public class Text2Graph {
  private final Graph graph;

  /**
   * Constructor to initialize the Text2Graph with a given graph.
   *
   * @param graph the graph to be used for operations
   */
  public Text2Graph(final Graph graph) {
//    this.graph = new Graph(graph);
    this.graph = graph;
  }

  /**
   * Gets bridge words between two words.
   *
   * @param word1 the first word
   * @param word2 the second word
   * @return a vector of bridge words between word1 and word2
   */
  public Vector<String> getBridgeWords(String word1, String word2) {
    Map<String, Node> graph = this.graph.graph;
    Vector<String> bridgeWords = new Vector<>();

    if (!graph.containsKey(word1) || !graph.containsKey(word2)) {
      return bridgeWords;
    }

    for (String word : graph.get(word1).edge.keySet()) {
      if (graph.get(word).edge.containsKey(word2)) {
        bridgeWords.add(word);
      }
    }
    return bridgeWords;
  }

  /**
   * Queries bridge words between two words and returns a formatted string.
   *
   * @param word1 the first word
   * @param word2 the second word
   * @return a string message listing bridge words or indicating their absence
   */
  public String queryBridgeWords(String word1, String word2) {
    Map<String, Node> graph = this.graph.graph;
    Vector<String> bridgeWords = new Vector<>();
    String message;

    if (!graph.containsKey(word1) || !graph.containsKey(word2)) {
      return String.format("No %s or %s in the graph!", word1, word2);
    }

    for (String word : graph.get(word1).edge.keySet()) {
      if (graph.get(word).edge.containsKey(word2)) {
        bridgeWords.add(word);
      }
    }

    if (bridgeWords.isEmpty()) {
      message = String.format("No bridge words from %s to %s!", word1, word2);
    } else {
      message = "The bridge words from " + word1
              + " to " + word2 + " are: " + String.join(" ", bridgeWords);
    }
    return message;
  }

  /**
   * Generates new text by inserting bridge words between words in the input text.
   *
   * @param inputText the input text
   * @return the generated text with bridge words inserted
   */
  public String generateNewText(String inputText) {
    StringBuilder outputText = new StringBuilder();
    String[] words = inputText.split(" ");

    for (int i = 0; i < words.length - 1; i++) {
      outputText.append(words[i]).append(" ");
      Vector<String> bridgeWords = getBridgeWords(words[i].toLowerCase(),
              words[i + 1].toLowerCase());

      if (!bridgeWords.isEmpty()) {
        outputText.append(bridgeWords.get(0)).append(" ");
      }
    }
    outputText.append(words[words.length - 1]);
    return outputText.toString();
  }

  /**
   * Calculates the shortest path between two words using the Floyd-Warshall algorithm.
   *
   * @param word1 the first word
   * @param word2 the second word
   * @return a string representation of the shortest path and its length
   */
  public String calcShortestPath(String word1, String word2) {
    Map<String, Map<String, Integer>> dist = new HashMap<>();
    Map<String, Map<String, String>> next = new HashMap<>();

    for (String word : graph.graph.keySet()) {
      dist.put(word, new HashMap<>());
      next.put(word, new HashMap<>());

      for (String toWord : graph.graph.get(word).edge.keySet()) {
        dist.get(word).put(toWord, graph.graph.get(word).edge.get(toWord));
        next.get(word).put(toWord, toWord);
      }
    }

    for (String k : graph.graph.keySet()) {
      for (String i : graph.graph.keySet()) {
        for (String j : graph.graph.keySet()) {
          int distIk = dist.get(i).getOrDefault(k, Integer.MAX_VALUE);
          int distKj = dist.get(k).getOrDefault(j, Integer.MAX_VALUE);
          int distIj = dist.get(i).getOrDefault(j, Integer.MAX_VALUE);

          if (distIk != Integer.MAX_VALUE && distKj
                  != Integer.MAX_VALUE && distIk + distKj < distIj) {
            dist.get(i).put(j, distIk + distKj);
            next.get(i).put(j, next.get(i).get(k));
          }
        }
      }
    }

    if (word1.equals(word2)) {
      return "Shortest path between " + word1 + " and " + word2 + ": " + word1 + " with length 0";
    }

    if (!dist.containsKey(word1) || !dist.get(word1).containsKey(word2)) {
      return "No path found between " + word1 + " and " + word2;
    }

    List<String> path = new ArrayList<>();
    String current = word1;

    while (!current.equals(word2)) {
      path.add(current);
      current = next.get(current).get(word2);
    }

    path.add(word2);
    int shortestPathLength = dist.get(word1).get(word2);
    return "Shortest path between " + word1 + " and "
            + word2 + ": " + String.join("->", path) + " with length " + shortestPathLength;
  }

  /**
   * Performs a random walk on the graph and writes the path to a file.
   *
   * @return a string representation of the nodes visited during the walk
   */
  public String randomWalk() {
    if (graph.graph.isEmpty()) {
      System.out.println("No walk found!");
      return null;
    }

    SecureRandom rand = new SecureRandom();
    List<String> nodes = new ArrayList<>();
    List<String> edges = new ArrayList<>();
    Map<String, List<String>> visitedNeighbors = new HashMap<>();
    String current = new ArrayList<>(graph.graph.keySet()).get(
            rand.nextInt(graph.graph.keySet().size()));
    nodes.add(current);

    while (true) {
      if (!visitedNeighbors.containsKey(current)) {
        visitedNeighbors.put(current, new ArrayList<>());
      }

      List<String> neighbors = new ArrayList<>(graph.graph.get(current).edge.keySet());
      neighbors.removeAll(visitedNeighbors.get(current));

      if (neighbors.isEmpty()) {
        break;
      }

      String next = neighbors.get(rand.nextInt(neighbors.size()));
      visitedNeighbors.get(current).add(next);
      nodes.add(next);
      edges.add(current + "->" + next);
      current = next;

      System.out.println("Nodes: " + nodes);
      System.out.println("Edges: " + edges);
      System.out.println("Continue? (y/n)");
      // 修改 randomWalk 方法中的 Scanner 部分
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in,
              StandardCharsets.UTF_8))) {
        Scanner scanner = new Scanner(reader);
        // 现在可以使用 scanner 进行输入操作
        String input = scanner.nextLine();
        if (!input.equals("y")) {
          break;
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }


    }

    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < nodes.size(); i++) {
      builder.append(nodes.get(i));
      if (i < nodes.size() - 1) {
        builder.append(" ");
      }
    }

    try (Writer writer = new OutputStreamWriter(new FileOutputStream("./random_walk.txt"),
            StandardCharsets.UTF_8)) {
      writer.write(builder.toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return builder.toString();
  }
}
