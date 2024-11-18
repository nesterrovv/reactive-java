package com.nesterrovv.reactivejava.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvFileGenerator {

    private CsvFileGenerator() {}

    public static void generateCsvFile(List<Integer> collectionSizes, List<Long> timeTakenList, String fileName) {
        if (collectionSizes.size() != timeTakenList.size()) {
            throw new IllegalArgumentException("Collection size list and time taken list must have the same size.");
        }
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("Collection Size,Time Taken (ms)\n");
            for (int i = 0; i < collectionSizes.size(); i++) {
                writer.append(timeTakenList.get(i).toString())
                        .append(",")
                        .append(collectionSizes.get(i).toString())
                        .append("\n");
            }
        } catch (IOException e) {
            log.error("Error while writing CSV file: {}", e.getMessage());
        }
    }

}
