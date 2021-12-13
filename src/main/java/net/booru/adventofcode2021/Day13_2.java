package net.booru.adventofcode2021;

import com.google.common.base.Strings;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day13_2 {

    public static void run() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("inputs/input13"));

        final List<Day13_1.Dot> dots = input.stream().takeWhile(line -> !line.isBlank()).map(Day13_1.Dot::of).collect(Collectors.toList());
        final List<Day13_1.Fold> folds = input.stream().dropWhile(line -> !line.isBlank()).map(Day13_1.Fold::of).filter(Objects::nonNull).collect(Collectors.toList());

        List<Day13_1.Dot> currentDots = dots;
        for (var fold : folds) {
            currentDots = currentDots.stream().map(fold::transform).collect(Collectors.toList());
        }
        System.out.println("Code = ");
        print(currentDots, null);
    }

    private static void print(final List<Day13_1.Dot> dots, final Day13_1.Fold fold) {
        final int dimX = dots.stream().mapToInt(Day13_1.Dot::getX).max().getAsInt();
        final int dimY = dots.stream().mapToInt(Day13_1.Dot::getY).max().getAsInt();

        final HashMap<Day13_1.Dot, Day13_1.Dot> map = new HashMap<>();
        dots.forEach(d -> map.put(d, d));
        System.out.println("--------------------------------------");
        for (int y = 0; y <= dimY; y++) {
            if (fold != null && fold.getDirection() == Day13_1.Direction.UP && fold.getPlane() == y) {
                System.out.println(Strings.repeat("-", dimX + 1));
            }
            for (int x = 0; x <= dimX; x++) {
                if (fold != null && fold.getDirection() == Day13_1.Direction.LEFT && fold.getPlane() == x) {
                    System.out.print("|");
                }
                final Day13_1.Dot dot = map.get(new Day13_1.Dot(x, y));
                System.out.print("" + (dot != null ? "#" : " "));
            }
            System.out.println("");
        }
        System.out.println("");
    }
}