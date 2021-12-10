package net.booru.adventofcode2021;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Day09_2 {

    private static final Point[] OFFSETS = new Point[]{
            Point.of(0, -1),
            Point.of(-1, 0),
            Point.of(0, 1),
            Point.of(1, 0),
    };

    private static final int INVALID = 9;


    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input09"));

        final int dimY = input.size();
        final int dimX = input.get(0).length();

        final HashMap<Point, Integer> heightMap = parseHeightMap(input);
        final HashMap<Point, Integer> basinMap = makeBasinMap(dimY, dimX, heightMap);
        final int product = getBasinSizeProduct(basinMap);

        System.out.println("largest 3 basin product = " + product);
    }

    private static int getBasinSizeProduct(final HashMap<Point, Integer> basinMap) {
        // count the occurrences of basin IDs
        final HashMap<Integer, Integer> basinSizeMap = new HashMap<>();
        basinMap.values().forEach(id -> basinSizeMap.put(id, basinSizeMap.getOrDefault(id, 0) + 1));

        return basinSizeMap.values().stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce(1, (a, b) -> a * b);
    }

    private static HashMap<Point, Integer> makeBasinMap(final int dimY, final int dimX, final HashMap<Point, Integer> heightMap) {
        // find the starting points... the basin low points.
        final HashMap<Point, Integer> basinMap = new HashMap<>();
        int basinId = 1;

        for (int y = 0; y < dimY; y++) {
            for (int x = 0; x < dimX; x++) {
                final Point point = Point.of(x, y);
                final int currentHeight = heightMap.get(point);
                if (isLowPoint(heightMap, point, currentHeight)) {
                    basinMap.put(point, basinId++);
                }
            }
        }

        for (var basinStart : List.copyOf(basinMap.entrySet())) {
            expandBasinRecursive(basinStart.getKey(), basinStart.getValue(), basinMap, heightMap);
        }

        return basinMap;
    }

    private static void expandBasinRecursive(final Point point, final int currentBasinId,
                                             final HashMap<Point, Integer> basinMap,
                                             final HashMap<Point, Integer> heightMap) {
        for (Point offset : OFFSETS) {
            final Point nextPoint = point.add(offset);
            if (basinMap.get(nextPoint) == null && heightMap.getOrDefault(nextPoint, INVALID) < INVALID) {
                basinMap.put(nextPoint, currentBasinId);
                expandBasinRecursive(nextPoint, currentBasinId, basinMap, heightMap);
            }
        }
    }

    private static boolean isLowPoint(final HashMap<Point, Integer> heightMap, final Point point, final int currentHeight) {
        return Arrays.stream(OFFSETS).allMatch(dp -> {
            final int adjacentHeight = heightMap.getOrDefault(point.add(dp), INVALID);
            return currentHeight < adjacentHeight;
        });
    }

    private static HashMap<Point, Integer> parseHeightMap(final List<String> input) {
        final HashMap<Point, Integer> heightMap = new HashMap<>();
        for (int y = 0, inputSize = input.size(); y < inputSize; y++) {
            final int[] row = Arrays.stream(input.get(y).split("")).mapToInt(Integer::parseInt).toArray();
            for (int x = 0; x < row.length; x++) {
                heightMap.put(Point.of(x, y), row[x]);
            }
        }
        return heightMap;
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