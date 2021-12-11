package net.booru.adventofcode2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class Day11_2 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input11"));
        final HashMap<Day11_1.Point, Integer> energyMap = Day11_1.parseEnergyMap(input);

        final int maxCount = Day11_1.DIM_X * Day11_1.DIM_Y;

        int syncStep = 0;
        while(true) {
            syncStep++;
            final int flashCount = Day11_1.stepOnce(energyMap);
            if (flashCount == maxCount) {
                break;
            }
        }

        System.out.println("synchronized flashes at: " + syncStep);
    }

}
