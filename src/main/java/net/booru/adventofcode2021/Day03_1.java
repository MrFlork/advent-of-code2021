package net.booru.adventofcode2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day03_1 {

    public static void run() throws IOException {
        final List<String> report = Files.readAllLines(Path.of("inputs/input03"));

        final int[] onesPerColumn = getNumberOfOnesPerColumn(report);

        final int majorityLimit = (int) Math.ceil(report.size() / 2.0);
        final String mostCommonBitString = makeNewBitString(onesPerColumn, numberOfOnes -> numberOfOnes > majorityLimit ? '1' : '0');
        final String leastCommonBitString = makeNewBitString(onesPerColumn, numberOfOnes -> numberOfOnes > majorityLimit ? '0' : '1');

        final int gammaRate = Integer.parseInt(mostCommonBitString, 2);
        final int epsilonRate = Integer.parseInt(leastCommonBitString, 2);

        System.out.println("power consumption = " + (gammaRate * epsilonRate));
    }

    private static int[] getNumberOfOnesPerColumn(final List<String> report) {
        final int numberOfBits = report.get(0).length();
        final int[] bitColumnSum = new int[numberOfBits];
        for (String binaryString : report) {
            for (int i = 0; i < numberOfBits; i++) {
                final char bit = binaryString.charAt(i);
                bitColumnSum[i] += bit == '0' ? 0 : 1;
            }
        }
        return bitColumnSum;
    }

    private static String makeNewBitString(final int[] bitColumnSum, final BitProducer producer) {
        final StringBuilder bitString = new StringBuilder(bitColumnSum.length);
        for (int i = 0; i < bitColumnSum.length; i++) {
            bitString.append(producer.of(bitColumnSum[i]));
        }

        return bitString.toString();
    }

    interface BitProducer {
        char of(int numberOfOnes);
    }
}
