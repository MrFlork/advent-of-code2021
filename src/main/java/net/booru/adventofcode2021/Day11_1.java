package net.booru.adventofcode2021;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11_1 {
    public static final List<Point> OFFSETS = List.of(
            Point.of(0, -1),
            Point.of(-1, 0),
            Point.of(0, 1),
            Point.of(1, 0),

            Point.of(1, 1),
            Point.of(1, -1),
            Point.of(-1, -1),
            Point.of(-1, 1)
    );
    public static int DIM_Y;
    public static int DIM_X;

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input11"));
        final HashMap<Point, Integer> energyMap = parseEnergyMap(input);
        final int flashSum = IntStream.range(0, 100).map(i -> stepOnce(energyMap)).sum();

        System.out.println("total flashes = " + flashSum);
    }

    public static int stepOnce(final HashMap<Point, Integer> energyMap) {
        final List<Point> points = new ArrayList<>(energyMap.keySet());

        // Increase energy by 1
        points.forEach(point -> energyMap.put(point, energyMap.get(point) + 1));

        // Collect unique points to flash
        final Set<Point> toFlash = points.stream()
                .filter(point -> energyMap.get(point) > 9)
                .collect(Collectors.toCollection(HashSet::new));

        // Distribute flash
        final Set<Point> flashedPoints = new HashSet<>(DIM_Y * DIM_X);
        while (!toFlash.isEmpty()) {
            final Point flashPoint = toFlash.iterator().next();
            toFlash.remove(flashPoint);
            flashedPoints.add(flashPoint);

            OFFSETS.stream()
                    .map(flashPoint::add)
                    .filter(adjacentPoint -> energyMap.get(adjacentPoint) != null)
                    .forEach(adjacentPoint -> {
                        final int updatedEnergy = energyMap.get(adjacentPoint) + 1;
                        energyMap.put(adjacentPoint, updatedEnergy);

                        final boolean isAlreadyFlashed = flashedPoints.contains(adjacentPoint);
                        if (updatedEnergy > 9 && !isAlreadyFlashed) {
                            toFlash.add(adjacentPoint);
                        }
                    });
        }

        // Reset flashed
        flashedPoints.forEach(point -> energyMap.put(point, 0));

        return flashedPoints.size();
    }

    public static HashMap<Point, Integer> parseEnergyMap(final List<String> input) {
        DIM_Y = input.size();
        DIM_X = input.get(0).length();

        final HashMap<Point, Integer> map = new HashMap<>();
        for (int y = 0; y < DIM_Y; y++) {
            final int[] row = Arrays.stream(input.get(y).split("")).mapToInt(Integer::parseInt).toArray();
            for (int x = 0; x < DIM_X; x++) {
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