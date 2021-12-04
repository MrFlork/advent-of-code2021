package net.booru.adventofcode2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day01_1 {

    public static void run() throws IOException {
        final int[] report =
                Files.readAllLines(Path.of("inputs/input01")).stream()
                        .mapToInt(Integer::parseInt)
                        .toArray();

        int count = 0;
        for (int i = 1; i < report.length; i++) {
            final int previous = report[i - 1];
            final int current = report[i];
            if (current > previous) {
                ++count;
            }
        }

        System.out.println("Count larger than previous = " + count);
    }
}
