package hit;

import java.io.*;
import java.util.Scanner;

import org.apache.commons.cli.*;
import com.mxgraph.view.mxGraph;
public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        if (args.length < 1) {
            System.out.println("请指定文件路径作为命令行参数。");
            return;
        }
        String filePath = args[0];
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("指定的文件不存在。");
            System.out.println(filePath);
            return;
        }

        //读入文件并初始化图结构
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String fileContent = stringBuilder.toString();
            fileContent = fileContent.replaceAll("[^a-zA-Z\\s]", " ");
            fileContent = fileContent.toLowerCase();
            System.out.println(fileContent);
            String[] words = fileContent.split(" ");
            for(String word : words){
                graph.addNode(word);
            }
            for(int i=0; i<words.length-1; i++){
                graph.addEdge(words[i], words[i+1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        Text2Graph text2Graph = new Text2Graph(graph);
        while (true) {
            System.out.println("Enter Command (show/exit):");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("exit")) {
                break;
            }
            if (command.equalsIgnoreCase("show")) {
                graph.showDirectedGraph();
            }
            if (command.equalsIgnoreCase("query bridge words")) {
                System.out.println("Enter words to be queried:");
                String word1 = scanner.nextLine();
                String word2 = scanner.nextLine();
                String message = text2Graph.queryBridgeWords(word1, word2);
                System.out.println(message);
            }
        }
    }
}