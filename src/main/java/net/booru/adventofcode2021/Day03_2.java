package net.booru.adventofcode2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Day03_2 {

    public static void run() throws IOException {
        final List<String> report = Files.readAllLines(Path.of("inputs/input03"));

        final int bitCount = report.get(0).length();

        final BitCriteria mostCommonBitCriteria = (numberOfOnes, majorityLimit) -> numberOfOnes >= majorityLimit ? '1' : '0';
        final BitCriteria leastCommonBitCriteria = (numberOfOnes, majorityLimit) -> numberOfOnes >= majorityLimit ? '0' : '1';

        final int oxygen = filterReport(report, bitCount, mostCommonBitCriteria);
        final int co2 = filterReport(report, bitCount, leastCommonBitCriteria);

        System.out.println("life support rating = " + (oxygen * co2));
    }

    private static int filterReport(final List<String> report, final int bitCount, final BitCriteria bitCriteria) {
        List<String> filteredReport = report;
        do {
            filteredReport = filterPerColumn(filteredReport, bitCount, bitCriteria);
        } while (filteredReport.size() > 1);

        return Integer.parseInt(filteredReport.get(0), 2);
    }

    private static List<String> filterPerColumn(final List<String> report, final int bitCount, final BitCriteria bitCriteria) {
        List<String> filteredReport = report;

        for (int column = 0; column < bitCount && filteredReport.size() > 1; column++) {
            final int numberOfOnes = getNumberOfOnesInColumn(filteredReport, column);
            final int majorityLimit = (int) Math.ceil(filteredReport.size() / 2.0);
            final char bitToKeep = bitCriteria.of(numberOfOnes, majorityLimit);

            final List<String> update = removeReports(filteredReport, bitToKeep, column);
            filteredReport = update;
        }

        return filteredReport;
    }

    private static List<String> removeReports(final List<String> filteredReport, final char bitToUse, final int column) {
        return filteredReport.stream().filter(
                reportLine -> reportLine.charAt(column) == bitToUse).collect(Collectors.toList());
    }

    private static int getNumberOfOnesInColumn(final List<String> report, final int column) {
        int bitSum = 0;
        for (String binaryString : report) {
            final char bit = binaryString.charAt(column);
            bitSum += bit == '0' ? 0 : 1;
        }
        return bitSum;
    }

    interface BitCriteria {
        char of(int bitSum, int majorityLimit);
    }

}
