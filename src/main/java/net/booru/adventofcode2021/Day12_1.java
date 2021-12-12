package net.booru.adventofcode2021;

import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day12_1 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input12"));

        final Map<Node, List<Node>> graph = makeGraph(input);
        int totalPaths = depthFirstSearch(Start.instance, new HashSet<>(), graph);

        System.out.println("total paths = " + totalPaths);
    }

    private static int depthFirstSearch(final Node node, final Set<Node> visitedNodes, final Map<Node, List<Node>> graph) {
        if (node.equals(End.instance)) {
            return 1;
        }

        if (node.isVisitOnce()) {
            visitedNodes.add(node);
        }

        int pathCount = 0;
        for (Node neighbour : graph.get(node)) {
            if (!visitedNodes.contains(neighbour)) {
                pathCount += depthFirstSearch(neighbour, new HashSet<>(visitedNodes), graph);
            }
        }

        return pathCount;
    }

    private static Map<Node, List<Node>> makeGraph(final List<String> input) {
        final Map<Node, List<Node>> graph = new HashMap<>(input.size());

        for (String edge : input) {
            final String[] split = edge.split("-");
            final Node node1 = makeNode(split[0]);
            final Node node2 = makeNode(split[1]);
            graph.putIfAbsent(node1, new ArrayList<>());
            graph.putIfAbsent(node2, new ArrayList<>());
            graph.get(node1).add(node2);
            graph.get(node2).add(node1);
        }

        return graph;
    }

    private static Node makeNode(final String input) {
        if (input.equals("start")) {
            return Start.instance;
        } else if (input.equals("end")) {
            return End.instance;
        } else if (Character.isUpperCase(input.charAt(0))) {
            return new BigCave(input);
        }

        return new SmallCave(input);
    }

    public interface Node {
        default boolean isVisitOnce() {
            return true;
        }
    }

    @Data
    public static class BigCave implements Node {
        private final String name;

        @Override
        public boolean isVisitOnce() {
            return false;
        }
    }

    @Data
    public static class SmallCave implements Node {
        private final String name;
    }

    @Data
    public static class Start implements Node {
        public static final Start instance = new Start();
        private final String name = "start";
    }

    @Data
    public static class End implements Node {
        public static final End instance = new End();
        private final String name = "end";
    }

}