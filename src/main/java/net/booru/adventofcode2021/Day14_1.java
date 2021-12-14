package net.booru.adventofcode2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day14_1 {

    public static void run() throws IOException {
        final List<String> inputs = Files.readAllLines(Path.of("inputs/input14"));
        final Map<String, String> rules = inputs.stream().skip(2)
                .map(input -> input.split(" -> "))
                .collect(Collectors.toMap(rule -> rule[0], rule -> rule[1]));

        String polymer = inputs.get(0);
        for (int steps = 0; steps < 10; steps++) {
            final StringBuilder polymerExpansion = new StringBuilder(polymer.length() * 2);

            char right = 0;
            for (int i = 1; i < polymer.length(); i++) {
                final char left = polymer.charAt(i - 1);
                right = polymer.charAt(i);
                final String insert = rules.get("" + left + right);
                polymerExpansion.append(left).append(insert);
            }
            polymerExpansion.append(right);
            polymer = polymerExpansion.toString();
        }

        final Map<Character, Integer> counts = new HashMap<>(polymer.length() * 2);
        for (int i = 0; i < polymer.length(); i++) {
            final char c = polymer.charAt(i);
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }
        final int max = Collections.max(counts.values());
        final int min = Collections.min(counts.values());
        final int difference = max - min;

        System.out.println("Quantity difference = " + difference);
    }
}