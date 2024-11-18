package com.nesterrovv.reactivejava;

import com.nesterrovv.reactivejava.statistics.serial.SerialStatisticsCalculatorTimer;
import com.nesterrovv.reactivejava.utils.CsvFileGenerator;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Main {

    public static final int LARGE_COLLECTION_SIZE = 1000;

    private Main() {}

    public static void main(String[] args) {
        boolean needPrint = false;
        try {
            if (System.getenv("PRINT_COLLECTION").equals("true")) {
                needPrint = true;
            } else {
                getPrintInfoMessage();
            }
        } catch (NullPointerException e) {
            getPrintInfoMessage();
        }
        List<Integer> collectionSizes = new ArrayList<>();
        List<Long> timeTakenList = new ArrayList<>();
        for (int collectionSize = 0; collectionSize < LARGE_COLLECTION_SIZE; collectionSize++) {
            long timeTaken = SerialStatisticsCalculatorTimer.calculate(collectionSize, needPrint);
            collectionSizes.add(collectionSize);
            timeTakenList.add(timeTaken);
        }
        CsvFileGenerator.generateCsvFile(collectionSizes, timeTakenList, "serial.csv");
    }

    private static void getPrintInfoMessage() {
        log.info("For printing aggregate collection, use env variable PRINT_COLLECTION=true");
    }

}
