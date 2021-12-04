package net.booru.adventofcode2021;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day02_2 {

    public static void run() throws IOException {
        final Position position = Files.readAllLines(Path.of("inputs/input02")).stream()
                .map(Command::of)
                .reduce(new Position(0, 0, 0), (p, command) -> command.nextPosition(p), (__, p) -> p);

        System.out.println("Count larger than previous = " + position.getHorizontal() * position.getDepth());
    }

    @Getter
    @AllArgsConstructor
    public static class Position {
        @With
        private final int aim;
        private final int horizontal;
        private final int depth;
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
                    final int horizontal = position.getHorizontal() + iSteps;
                    final int depth = position.getDepth() + position.getAim() * iSteps;
                    return new Position(position.getAim(), horizontal, depth);
                case up:
                    return position.withAim(position.getAim() - iSteps);
                case down:
                    return position.withAim(position.getAim() + iSteps);
                default:
                    throw new IllegalStateException("unknown case");
            }
        }
    }
}
