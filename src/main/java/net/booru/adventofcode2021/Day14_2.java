package net.booru.adventofcode2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day14_2 {

    public static void run() throws IOException {
        final List<String> inputs = Files.readAllLines(Path.of("inputs/input14"));
        final Map<String, Character> rules = inputs.stream().skip(2)
                .map(input -> input.split(" -> "))
                .collect(Collectors.toMap(rule -> rule[0], rule -> rule[1].charAt(0)));

        // Main insight:
        //      We only add characters, and only 1 per rule application.
        //      100 or so rules -> not that many pairs of characters.
        //      We can't actually expand (2^40), keep track of how many of
        //        each pair we produce during rule application.
        //      A pair is replaced by 2 new.

        final int STEPS = 40;
        final String polymer = inputs.get(0);

        // Init counts for existing 'elements'
        final Map<Character, Long> elementCounts = new HashMap<>();
        for (int i = 0; i < polymer.length(); i++) {
            add(elementCounts, polymer.charAt(i), 1);
        }

        // Counts for existing pairs
        final Map<String, Long> pairCountsInitial = new HashMap<>();
        for (int i = 1; i < polymer.length(); i++) {
            final String pair = "" + polymer.charAt(i - 1) + polymer.charAt(i);
            add(pairCountsInitial, pair, 1);
        }

        // Produce new pairs, update counts
        Map<String, Long> pairCounts = pairCountsInitial;
        for (int i = 0; i < STEPS; i++) {
            final Map<String, Long> pairCountsNext = new HashMap<>();

            for (var entry : pairCounts.entrySet()) {
                final String pair = entry.getKey();
                final long pairCount = entry.getValue();

                final char element = rules.get(pair);
                add(elementCounts, element, pairCount);

                // produces 2 new pairs from pair and element (per existing pair)
                final String p1 = "" + pair.charAt(0) + element;
                final String p2 = "" + element + pair.charAt(1);
                add(pairCountsNext, p1, pairCount);
                add(pairCountsNext, p2, pairCount);
            }
            pairCounts = pairCountsNext;
        }

        final long max = Collections.max(elementCounts.values());
        final long min = Collections.min(elementCounts.values());
        final long difference = max - min;

        System.out.println("Quantity difference = " + difference);
    }

    private static <T> void add(Map<T, Long> counts, T key, long v) {
        counts.put(key, counts.getOrDefault(key, 0L) + v);
    }
}