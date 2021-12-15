package net.booru.adventofcode2021;

import lombok.Data;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day15_1 {

    public static final Point[] OFFSETS = new Point[]{
            Point.of(0, -1),
            Point.of(-1, 0),
            Point.of(0, 1),
            Point.of(1, 0),
    };


    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input15"));

        // The graph is a matrix of Node, all neighbors are connected
        final int rows = input.size();
        final int columns = input.get(0).length();
        final int[][] graph = makeGraph(input, rows, columns);

        final Point start = Point.of(0, 0);
        final Point end = Point.of(rows - 1, columns - 1);

        // shortest "path" where the risk level of a node is the cost to travel to it
        final int riskLevelSum = lowestRisk(start, end, graph);

        System.out.println("Risk Level sum = " + riskLevelSum);
    }

    // dijkstra with the path stuff
    private static int lowestRisk(final Point start, final Point end, final int[][] riskLevels) {
        final int columns = riskLevels.length;
        final int rows = riskLevels[0].length;

        final Set<Point> openNodes = initOpenNodes(columns, rows);
        final int[][] riskFromStart = initRiskFromStart(start, columns, rows);

        while (!openNodes.isEmpty()) {
            final Point currentNode = getMinNode(openNodes, riskFromStart);
            openNodes.remove(currentNode);

            for (Point neighbor : getNotVisitedNeighbors(currentNode, openNodes, columns, rows)) {
                final int candidate = riskFromStart[currentNode.x][currentNode.y] + riskLevels[neighbor.x][neighbor.y];
                if (candidate < riskFromStart[neighbor.x][neighbor.y]) {
                    riskFromStart[neighbor.x][neighbor.y] = candidate;
                }
            }
        }

        return riskFromStart[end.x][end.y];
    }


    private static Set<Point> initOpenNodes(final int columns, final int rows) {

        final Set<Point> nodes = new HashSet<>(rows * columns * 2);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                nodes.add(Point.of(x, y));
            }
        }
        return nodes;
    }

    private static int[][] initRiskFromStart(final Point start, final int columns, final int rows) {
        final int[][] riskFromStart = new int[columns][rows];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                riskFromStart[x][y] = Integer.MAX_VALUE;
            }
        }
        riskFromStart[start.x][start.y] = 0;
        return riskFromStart;
    }

    @Nonnull
    private static Point getMinNode(final Set<Point> openNodes, final int[][] distFromStart) {
        Point minPoint = openNodes.iterator().next();
        int minValue = distFromStart[minPoint.x][minPoint.y];
        for (Point point : openNodes) {
            final int value = distFromStart[point.x][point.y];
            if (value < minValue) {
                minPoint = point;
                minValue = value;
            }
        }
        return minPoint;
    }

    private static List<Point> getNotVisitedNeighbors(final Point currentNode, final Set<Point> openNodes, final int columns, final int rows) {
        List<Point> neighbors = new ArrayList<>(4);
        for (Point offset : OFFSETS) {
            final Point neighbor = offset.add(currentNode);
            if (neighbor.x >= 0 && neighbor.x < columns && neighbor.y >= 0 && neighbor.y < rows && openNodes.contains(neighbor)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    private static int[][] makeGraph(final List<String> matrixLines, final int rows, final int columns) {
        final int[][] graph = new int[columns][rows];
        for (int y = 0; y < rows; y++) {
            final String row = matrixLines.get(y);
            for (int x = 0; x < columns; x++) {
                final int riskLevel = Character.getNumericValue(row.charAt(x));
                graph[x][y] = riskLevel;
            }
        }

        return graph;
    }

    @Data
    private static class Point {
        public final int x;
        public final int y;

        public static Point of(final int x, final int y) {
            return new Point(x, y);
        }

        public Point add(final Point p) {
            return new Point(x + p.x, y + p.y);
        }
    }

}
