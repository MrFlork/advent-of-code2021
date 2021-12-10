package net.booru.adventofcode2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Day10_1 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input10"));

        final int sum = input.stream().mapToInt(Day10_1::getSyntaxErrorScore).sum();

        System.out.println("Score = " + sum);
    }

    public static int getSyntaxErrorScore(String line) {
        final Deque<Character> openBrackets = new LinkedList<>();

        for (int i = 0; i < line.length(); i++) {
            final char chunkChar = line.charAt(i);
            if (isOpening(chunkChar)) {
                openBrackets.push(chunkChar);
            } else {
                final char open = openBrackets.pop();
                if (!isClosing(chunkChar, open)) {
                    return getScore(chunkChar);
                }
            }
        }
        return 0;
    }

    private static int getScore(final char close) {
        switch (close) {
            case ')':
                return 3;
            case ']':
                return 57;
            case '}':
                return 1197;
            case '>':
                return 25137;
        }
        return 0;
    }

    private static boolean isClosing(final char close, final char open) {
        switch (open) {
            case '(':
                return close == ')';
            case '[':
                return close == ']';
            case '{':
                return close == '}';
            case '<':
                return close == '>';
        }
        return false;
    }

    private static boolean isOpening(final char c) {
        return c == '(' || c == '[' || c == '{' || c == '<';
    }
}