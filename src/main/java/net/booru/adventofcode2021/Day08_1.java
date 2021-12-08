package net.booru.adventofcode2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day08_1 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input08"));

        int counter = 0;
        for (String line : input) {
            final String[] digits = line.split(" \\| ")[1].split(" ");
            for (String digit : digits) {
                if (digit.length() == 2 || digit.length() == 3 || digit.length() == 4 || digit.length() == 7 ) {
                    counter++;
                }
            }
        }

        System.out.println(" 1,4,7,8 appears = " + counter );
    }
}