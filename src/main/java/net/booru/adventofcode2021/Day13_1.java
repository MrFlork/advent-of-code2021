package net.booru.adventofcode2021;

import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day13_1 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input13"));

        final List<Dot> dots = input.stream().takeWhile(line -> !line.isBlank()).map(Dot::of).collect(Collectors.toList());
        final Fold firstFold = input.stream().dropWhile(line -> !line.isBlank()).map(Fold::of).filter(Objects::nonNull).findFirst().get();

        final var transformedDots = dots.stream().map(firstFold::transform).collect(Collectors.toList());
        final int visibleDots = Set.copyOf(transformedDots).size();

        System.out.println("total visible dots = " + visibleDots);
    }

    @Data
    public static class Fold {
        private final int plane;
        private final Direction direction;

        public static Fold of(String input) {
            if (input.isBlank()) {
                return null;
            }
            final String[] split = input.split(" ")[2].split("=");

            return new Fold(Integer.parseInt(split[1]), Direction.of(split[0]));
        }

        public Dot transform(final Dot dot) {
            // We assume the fold target will be inside the upper or left half
            if (direction == Direction.UP) {
                if (dot.getY() > plane) {
                    return new Dot(dot.getX(), flip(dot.getY()));
                }
            } else if (direction == Direction.LEFT) {
                if (dot.getX() > plane) {
                    return new Dot(flip(dot.getX()), dot.getY());
                }
            }

            return dot;
        }

        private int flip(final int value){
            return plane + (plane - value);
        }
    }

    enum Direction {
        UP, LEFT;

        public static Direction of(final String s) {
            if (s.equals("y")) {
                return UP;
            }
            if (s.equals("x")) {
                return LEFT;
            }
            throw new IllegalArgumentException("Unexpected input = " + s);
        }
    }

    @Data
    public static class Dot {
        private final int x;
        private final int y;

        public static Dot of(final String input) {
            final String[] split = input.split(",");
            return new Dot(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }
    }
}