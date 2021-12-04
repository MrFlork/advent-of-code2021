package net.booru.adventofcode2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day01_2 {
    public static void run() throws IOException {
        final int[] report =
                Files.readAllLines(Path.of("inputs/input01")).stream()
                        .mapToInt(Integer::parseInt)
                        .toArray();

        // a+b+c < b+c+d <=> a < d  <=> report[i] < report[i+3]
        int count = 0;
        for (int i = 0; i < report.length - 3; i++) {
            count += report[i] < report[i + 3] ? 1 : 0;
        }

        System.out.println("Count windows larger than previous = " + count);
    }
}
