package net.booru.adventofcode2021;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Day04_1 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input04"));

        final int[] allNumbers = Arrays.stream(input.get(0).split(",")).mapToInt(Integer::parseInt).toArray();

        final List<Board> boards = new ArrayList<>(input.size() / 6 + 1);
        for (int i = 1; i < input.size(); i += 6) {
            boards.add(Board.of(input.subList(i + 1, i + 6)));
        }

        final int winnerScore = playBingo(allNumbers, boards);

        System.out.println("Winner's score = " + winnerScore);
    }

    private static int playBingo(final int[] allNumbers, final List<Board> boards) {
        for (int number : allNumbers) {
            boards.forEach(board -> board.mark(number));

            final List<Board> winners = boards.stream().filter(Board::isWinner).collect(Collectors.toList());
            if (!winners.isEmpty()) {
                // problem description indicates there is exactly 1 winner
                return winners.get(0).getScore(number);
            }
        }

        throw new IllegalStateException("No winner1?"); // does not happen
    }

    @Data
    private static class Position {
        private final int row;
        private final int column;
    }

    @Data
    @AllArgsConstructor
    private static class Entry {
        private final int number;
        private boolean isMarked;
    }


    private static class Board {
        private static final int DIMENSION = 5;

        private final HashMap<Position, Entry> iBoard = new HashMap<>();
        private final HashMap<Integer, Position> iBoardNumberPositions = new HashMap<>();
        private final int[] iMarkedInRowsCount = new int[DIMENSION];
        private final int[] iMarkedInColumnCount = new int[DIMENSION];
        private boolean isWinner = false;

        private void add(final Position position, final Entry entry) {
            iBoard.put(position, entry);
            iBoardNumberPositions.put(entry.getNumber(), position);
        }

        public boolean isWinner() {
            return isWinner;
        }

        public void mark(int number) {
            final Position position = iBoardNumberPositions.get(number);
            if (position != null) {
                iBoard.get(position).setMarked(true);
                iMarkedInColumnCount[position.getColumn()] += 1;
                iMarkedInRowsCount[position.getRow()] += 1;

                isWinner = isWinner ||
                        iMarkedInColumnCount[position.getColumn()] >= DIMENSION ||
                        iMarkedInRowsCount[position.getRow()] >= DIMENSION;
            }
        }

        /**
         * @param number the number that resulted in win
         * @return the sum of all unmarked numbers on the board times the number that resulted in win
         */
        public int getScore(final int number) {
            return iBoard.values().stream()
                    .filter(entry -> !entry.isMarked())
                    .mapToInt(Entry::getNumber)
                    .sum() * number;
        }

        public static Board of(final List<String> boardRows) {
            final Board board = new Board();

            for (int row = 0; row < DIMENSION; row++) {
                final StringTokenizer stringTokenizer = new StringTokenizer(boardRows.get(row));
                for (int column = 0; column < DIMENSION; column++) {
                    final int number = Integer.parseInt(stringTokenizer.nextToken());
                    board.add(new Position(row, column), new Entry(number, false));
                }
            }

            return board;
        }
    }
}