package net.booru.adventofcode2021;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day09_1 {

    public static final int INVALID = 9;
    public static final Point[] OFFSETS = new Point[]{
            Point.of(0, -1),
            Point.of(-1, 0),
            Point.of(0, 1),
            Point.of(1, 0),
    };

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input09"));

        final int dimY = input.size();
        final int dimX = input.get(0).length();

        final HashMap<Point, Integer> map = parseHeightMap(input);
        int riskLevelSum = getRiskLevelSum(dimY, dimX, map);

        System.out.println("riskLevel sum = " + riskLevelSum);
    }

    private static int getRiskLevelSum(final int dimY, final int dimX, final HashMap<Point, Integer> map) {
        int riskLevelSum = 0;
        for (int y = 0; y < dimY; y++) {
            for (int x = 0; x < dimX; x++) {
                final Point point = Point.of(x, y);
                final int currentHeight = map.get(point);

                riskLevelSum += isLowPoint(map, point, currentHeight) ? currentHeight + 1 : 0;
            }
        }
        return riskLevelSum;
    }

    private static boolean isLowPoint(final HashMap<Point, Integer> heightMap, final Point point, final int currentHeight) {
        return Arrays.stream(OFFSETS).allMatch(dp -> {
            final int adjacentHeight = heightMap.getOrDefault(point.add(dp), INVALID);
            return currentHeight < adjacentHeight;
        });
    }

    public static HashMap<Point, Integer> parseHeightMap(final List<String> input) {
        final HashMap<Point, Integer> map = new HashMap<>();
        for (int y = 0, inputSize = input.size(); y < inputSize; y++) {
            final int[] row = Arrays.stream(input.get(y).split("")).mapToInt(Integer::parseInt).toArray();
            for (int x = 0; x < row.length; x++) {
                map.put(Point.of(x, y), row[x]);
            }
        }
        return map;
    }

    @Data
    @AllArgsConstructor
    public static class Point {
        private final int x;
        private final int y;

        public static Point of(int x, int y) {
            return new Point(x, y);
        }

        public Point add(Point p) {
            return Point.of(x + p.x, y + p.y);
        }
    }

}