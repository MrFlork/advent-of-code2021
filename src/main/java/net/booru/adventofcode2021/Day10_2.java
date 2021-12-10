package net.booru.adventofcode2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Day10_2 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input10"));

        final long[] scores = input.stream()
                .filter(Day10_2::isUncorrupted)
                .map(Day10_2::makeChunkCompletion)
                .mapToLong(Day10_2::getScoreForEnding)
                .sorted().toArray();

        final long middleScore = scores[scores.length / 2];

        System.out.println("Score = " + middleScore);
    }

    private static String makeChunkCompletion(final String line) {
        final StringBuilder ending = new StringBuilder();
        final Deque<Character> openBrackets = new LinkedList<>();

        for (int i = 0; i < line.length(); i++) {
            final char chunkChar = line.charAt(i);
            if (isOpening(chunkChar)) {
                openBrackets.push(chunkChar);
            } else {
                openBrackets.pop();
            }
        }

        while (!openBrackets.isEmpty()) {
            ending.append(getProperClose(openBrackets.pop()));
        }

        return ending.toString();
    }

    public static boolean isUncorrupted(String line) {
        final Deque<Character> brackets = new LinkedList<>();
        for (int i = 0; i < line.length(); i++) {
            final char chunkChar = line.charAt(i);
            if (isOpening(chunkChar)) {
                brackets.push(chunkChar);
            } else {
                final char open = brackets.pop();
                if (!isClosing(chunkChar, open)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static long getScoreForEnding(final String ending) {
        long sum = 0;
        for (int i = 0; i < ending.length(); i++) {
            sum = 5 * sum + getScoreForCloser(ending.charAt(i));
        }
        return sum;
    }

    private static long getScoreForCloser(final char close) {
        switch (close) {
            case ')':
                return 1;
            case ']':
                return 2;
            case '}':
                return 3;
            case '>':
                return 4;
        }
        return 0;
    }

    private static char getProperClose(final char open) {
        switch (open) {
            case '(':
                return ')';
            case '[':
                return ']';
            case '{':
                return '}';
            case '<':
                return '>';
        }
        return 0;
    }

    private static boolean isClosing(final char close, final char open) {
        return getProperClose(open) == close;
    }

    private static boolean isOpening(final char c) {
        return c == '(' || c == '[' || c == '{' || c == '<';
    }
}