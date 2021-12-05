package net.booru.adventofcode2021;

import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Day05_2 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input05"));

        final List<Line> lines = input.stream().map(Line::of).collect(Collectors.toList());
        final Diagram diagram = new Diagram(lines);

        System.out.println("Number of overlapping points = " + diagram.getOverlappingPoints());
    }

    @Data
    private static class Line {
        private final Point start;
        private final Point end;

        public static Line of(final String string) {
            final String[] split = string.split(" -> ");
            return new Line(Point.of(split[0]), Point.of(split[1]));
        }

        public List<Point> getCoveredPoints() {
            final List<Point> points = new ArrayList<>();

            // 0, 1 or -1 since orthogonal or 45 degrees
            final int stepX = (int) Math.signum(end.getX() - start.getX());
            final int stepY = (int) Math.signum(end.getY() - start.getY());

            int x = start.getX();
            int y = start.getY();
            while (true) {
                points.add(new Point(x, y));
                if (x == end.getX() && y == end.getY()) {
                    break;
                }
                x += stepX;
                y += stepY;
            }

            return points;
        }
    }

    @Data
    private static class Point {
        private final int x;
        private final int y;

        public static Point of(final String string) {
            final String[] split = string.split(",");
            return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }
    }

    private static class Diagram {
        private final HashMap<Point, Integer> iPoints = new HashMap<>();

        public Diagram(final List<Line> lines) {
            for (Line line : lines) {
                for (Point point : line.getCoveredPoints()) {
                    iPoints.put(point, iPoints.getOrDefault(point, 0) + 1);
                }
            }
        }

        public long getOverlappingPoints() {
            return iPoints.entrySet().stream().filter(e -> e.getValue() >= 2).count();
        }
    }
}