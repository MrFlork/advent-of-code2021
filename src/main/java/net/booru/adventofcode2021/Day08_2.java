package net.booru.adventofcode2021;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("ALL")
public class Day08_2 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input08"));

        // segments -> digit
        final Map<Integer, Set<String>> mapping = new HashMap<>();

        // Note: I consider this a "failed" solution... it was the no-brain path.

        int sumOfDisplays = 0;
        for (String line : input) {
            final String[] lineSplit = line.split(" \\| ");
            final String[] digits = lineSplit[0].split(" ");
            final List<Set<String>> unknownDigits = new ArrayList<>();

            for (String digit : digits) {
                final String[] split = digit.split("");
                Arrays.sort(split);
                final Set<String> segments = Set.of(split);
                if (segments.size() == 2) {
                    mapping.put(1, segments);
                } else if (segments.size() == 3) {
                    mapping.put(7, segments);
                } else if (segments.size() == 4) {
                    mapping.put(4, segments);
                } else if (segments.size() == 7) {
                    mapping.put(8, segments);
                } else {
                    unknownDigits.add(segments);
                }
            }

            //-----------------------------
            // for 6 segments (0,6,9)

            final Set<String> actualA = Sets.difference(mapping.get(7), mapping.get(1));

            // G = 9 - 4 - A, exactly 1 match
            final Set<String> filterG = Sets.union(mapping.get(4), actualA);
            final Set<String> actualG = unknownDigits.stream()
                    .filter(d -> d.size() == 6)
                    .map(digit -> Sets.difference(digit, filterG))
                    .filter(d -> d.size() == 1)
                    .findFirst().get();

            // 9 = G + A + 4
            final Set<String> number9 = Sets.union(Sets.union(actualG, actualA), mapping.get(4));
            mapping.put(9, number9);
            unknownDigits.remove(number9);

            // 6 + 1 == 8, exactly 1 match
            final Set<String> number6 = unknownDigits.stream()
                    .filter(d -> d.size() == 6)
                    .filter(digit -> Sets.union(digit, mapping.get(1)).size() == 7) // number8
                    .findFirst().get();
            mapping.put(6, number6);
            unknownDigits.remove(number6);

            final Set<String> number0 = unknownDigits.stream()
                    .filter(d -> d.size() == 6)
                    .findFirst().get();
            mapping.put(0, number0);
            unknownDigits.remove(number0);

            //-----------------------------
            // for 5 segments  (2,3)

            final Set<String> actualE = Sets.difference(mapping.get(8), number9);

            // 6 - E == 5
            final Set<String> number5 = Sets.difference(number6, actualE);
            mapping.put(5, number5);
            unknownDigits.remove(number5);

            // 3 + 1 == 3
            final Set<String> number3 = unknownDigits.stream()
                    .filter(d -> d.size() == 5)
                    .filter(digit -> Sets.union(digit, mapping.get(1)).equals(digit)) // number3 is unchanged after adding 1
                    .findFirst().get();
            mapping.put(3, number3);
            unknownDigits.remove(number3);

            // only one remaining
            final Set<String> number2 = unknownDigits.get(0);
            mapping.put(2, number2);
            unknownDigits.remove(number2);


            //-----------------------------
            // Displayed digits

            final Map<Set<String>, Integer> reverseMap = new HashMap<>();
            mapping.entrySet().forEach(e -> reverseMap.put(e.getValue(), e.getKey()));

            final int[] displayDigits = Arrays.stream(lineSplit[1].split(" "))
                    .map(digit -> reverseMap.get(Set.of(digit.split(""))))
                    .mapToInt(i -> i)
                    .toArray();

            final int displayValue = Integer.parseInt("" + displayDigits[0] + "" + displayDigits[1] + "" + displayDigits[2] + "" + displayDigits[3]);
            sumOfDisplays += displayValue;
        }

        System.out.println(" sum of displayed numbers = " + sumOfDisplays);
    }
}