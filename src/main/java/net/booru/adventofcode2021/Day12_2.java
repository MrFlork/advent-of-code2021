package net.booru.adventofcode2021;

import com.google.common.collect.HashMultiset;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day12_2 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input12"));

        final Map<Node, List<Node>> graph = makeGraph(input);
        int totalPaths = depthFirstSearch(Node.start, HashMultiset.create(), graph, false);

        System.out.println("total paths = " + totalPaths);
    }

    private static int depthFirstSearch(final Node node, final HashMultiset<Node> visitedNodes,
                                        final Map<Node, List<Node>> graph, final boolean wasSecondVisitTaken) {
        if (node.equals(Node.end)) {
            return 1;
        }

        boolean isSecondVisitTaken = wasSecondVisitTaken;
        final HashMultiset<Node> visitedNodesNext;
        if (node.getNodeType() == NodeType.SmallCave) {
            visitedNodesNext = HashMultiset.create(visitedNodes);
            visitedNodesNext.add(node);
            if (visitedNodesNext.count(node) == 2) {
                isSecondVisitTaken = true;
            }
        } else {
            visitedNodesNext = visitedNodes;
        }

        // if second visit is not yet taken, also include SmallCave that have 1 visit => limit 2
        final int limit = isSecondVisitTaken ? 1 : 2;

        int pathCount = 0;
        for (Node neighbor : graph.get(node)) {
            if (neighbor != Node.start && visitedNodes.count(neighbor) < limit) {
                pathCount += depthFirstSearch(neighbor, visitedNodesNext, graph, isSecondVisitTaken);
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

    private static Node makeNode(final String name) {
        if (name.equals("start")) {
            return Node.start;
        } else if (name.equals("end")) {
            return Node.end;
        } else if (Character.isUpperCase(name.charAt(0))) {
            return new Node(NodeType.BigCave, name);
        }

        return new Node(NodeType.SmallCave, name);
    }

    enum NodeType {
        BigCave, SmallCave, Start, End
    }

    @Data
    public static class Node {
        public static final Node start = new Node(NodeType.Start, "start");
        public static final Node end = new Node(NodeType.End, "end");
        private final NodeType nodeType;
        private final String name;
    }
}