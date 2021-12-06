package net.booru.adventofcode2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Day06_1 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input06"));

        final List<Integer> fishTimers = Arrays.stream(input.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        final int DAYS = 80;

        for (int i = 0; i < DAYS; i++) {
            for (int index = 0, size = fishTimers.size(); index < size; index++) {
                final List<Integer> newFishTimers = new ArrayList<>();
                final int timer = fishTimers.get(index);
                if (timer == 0) {
                    newFishTimers.add(8);
                    fishTimers.set(index, 6);
                } else {
                    fishTimers.set(index, timer - 1);
                }

                fishTimers.addAll(newFishTimers);
            }
        }

        System.out.println("lanternfish count = " + fishTimers.size());
    }
}