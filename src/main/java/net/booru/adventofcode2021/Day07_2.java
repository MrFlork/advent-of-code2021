package net.booru.adventofcode2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day07_2 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input07"));

        final int[] positions = Arrays.stream(input.get(0).split(",")).mapToInt(Integer::parseInt).toArray();
        final int max = IntStream.of(positions).max().getAsInt();
        final int min = IntStream.of(positions).min().getAsInt();

        int bestCost = Integer.MAX_VALUE;
        for (int alignment = min; alignment <= max; alignment++) {
            int cost = 0;
            for (int p : positions) {
                final int x = Math.abs(alignment - p);
                final int n = ((x + 1) * x) / 2;
                cost += n;
            }
            bestCost = Math.min(cost, bestCost);
        }

        System.out.println("fuel = " + bestCost);
    }
}