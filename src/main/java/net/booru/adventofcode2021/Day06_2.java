package net.booru.adventofcode2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day06_2 {
    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input06"));

        final HashMap<Integer, Long> timerCounters = new HashMap<>();
        Arrays.stream(input.get(0).split(","))
                .map(Integer::parseInt)
                .forEach(initialTimer -> timerCounters.put(initialTimer, timerCounters.getOrDefault(initialTimer, 0L) + 1));

        for (int day = 0; day < 256; day++) {
            // get zero-timer count before updating larger counters (which may create new zero counters)
            final long nofZeroCounters = timerCounters.getOrDefault(0, 0L);
            timerCounters.remove(0);

            // order is important since we increase the 'timer-1' counter
            for (int timer = 1; timer < 9; timer++) {
                final Long count = timerCounters.get(timer);
                if (count != null) {
                    timerCounters.remove(timer);
                    timerCounters.put(timer - 1, timerCounters.getOrDefault(timer - 1, 0L) + count);
                }
            }

            // transform the zero-counters to 6 and 8 counters.
            if (nofZeroCounters > 0) {
                timerCounters.put(6, timerCounters.getOrDefault(6, 0L) + nofZeroCounters);
                timerCounters.put(8, timerCounters.getOrDefault(8, 0L) + nofZeroCounters);
            }
        }

        System.out.println("lanternfish count = " + timerCounters.values().stream().mapToLong(a -> a).sum());
    }
}