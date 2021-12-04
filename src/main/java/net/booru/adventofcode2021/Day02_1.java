package net.booru.adventofcode2021;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day02_1 {

    public static void run() throws IOException {
        final Position resultingPosition = Files.readAllLines(Path.of("inputs/input02")).stream()
                .map(Command::of)
                .reduce(new Position(0, 0), (p, command) -> command.nextPosition(p), (__, p) -> p);

        System.out.println("Count larger than previous = " + resultingPosition.getX() * resultingPosition.getY());
    }

    @Data
    @With
    @AllArgsConstructor
    public static class Position {
        private final int x;
        private final int y;
    }

    public enum Direction {
        forward, up, down
    }

    @AllArgsConstructor
    public static class Command {
        private final Direction iDirection;
        private final int iSteps;

        public static Command of(final String in) {
            final String[] parts = in.split(" ");
            return new Command(Direction.valueOf(parts[0]), Integer.parseInt(parts[1]));
        }

        public Position nextPosition(final Position position) {
            switch (iDirection) {
                case forward:
                    return position.withX(position.x + iSteps);
                case up:
                    return position.withY(position.y - iSteps);
                case down:
                    return position.withY(position.y + iSteps);
                default:
                    throw new IllegalStateException("unknown case");
            }
        }
    }
}
