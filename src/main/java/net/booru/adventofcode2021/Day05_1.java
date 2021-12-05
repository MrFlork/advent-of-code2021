package net.booru.adventofcode2021;

import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Day05_1 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input05"));

        final List<Line> lines = input.stream()
                .map(Line::of)
                .filter(line -> line.isHorizontal() || line.isVertical())
                .collect(Collectors.toList());

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

        public boolean isHorizontal() {
            return start.getY() == end.getY();
        }

        public boolean isVertical() {
            return start.getX() == end.getX();
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

        public long getOverlappingPoints() {
            return iPoints.entrySet().stream().filter(e -> e.getValue() >= 2).count();
        }

        public Diagram(final List<Line> lines) {
            lines.forEach(this::addLine);
        }

        private void addLine(final Line line) {
            final int top = Math.max(line.getStart().getY(), line.getEnd().getY());
            final int bottom = Math.min(line.getStart().getY(), line.getEnd().getY());
            final int left = Math.min(line.getStart().getX(), line.getEnd().getX());
            final int right = Math.max(line.getStart().getX(), line.getEnd().getX());

            // Line is either vertical or horizontal so either left==right or bottom==top
            for (int y = bottom; y <= top; y++) {
                for (int x = left; x <= right; x++) {
                    addPoint(new Point(x, y));
                }
            }
        }

        private void addPoint(final Point point) {
            iPoints.put(point, iPoints.getOrDefault(point, 0) + 1);
        }
    }
}