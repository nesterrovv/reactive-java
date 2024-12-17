package com.nesterrovv.reactivejava;

import com.nesterrovv.reactivejava.statistics.StatisticsCalculatorTimer;
import com.nesterrovv.reactivejava.statistics.StatisticsType;
import com.nesterrovv.reactivejava.utils.CsvFileGenerator;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CustomBenchmarkRunner {

    public static final int LARGE_COLLECTION_SIZE = 10000;

    private CustomBenchmarkRunner() {}

    @SuppressWarnings("UncommentedMain")
    public static void main(String[] args) {
        boolean needPrint = false;
        StatisticsType statisticsType = null;
        try {
            if (System.getenv("PRINT_COLLECTION").equals("true")) {
                needPrint = true;
            } else {
                getPrintInfoMessage();
            }
            statisticsType = StatisticsType.valueOf(System.getenv("STATISTICS_TYPE").toUpperCase());
        } catch (NullPointerException e) {
            getPrintInfoMessage();
        }
        List<Integer> collectionSizes = new ArrayList<>();
        List<Long> timeTakenList = new ArrayList<>();
        if (statisticsType == null) {
            statisticsType = StatisticsType.PARALLEL;
            log.warn("You need to specify statistics calculation type: {}, {} or {} "
                            + "via environment variable STATISTICS_TYPE. PARALLEL has been chosen by default.",
                    StatisticsType.SERIAL, StatisticsType.PARALLEL, StatisticsType.IMPROVED_PARALLEL);
        }
        for (int collectionSize = 0; collectionSize < LARGE_COLLECTION_SIZE; collectionSize++) {
            long timeTaken = StatisticsCalculatorTimer.calculate(collectionSize, needPrint, statisticsType);
            collectionSizes.add(collectionSize);
            timeTakenList.add(timeTaken);
        }
        CsvFileGenerator.generateCsvFile(collectionSizes, timeTakenList, "parallel.csv");
    }

    private static void getPrintInfoMessage() {
        log.info("For printing aggregate collection, use env variable PRINT_COLLECTION=true");
    }

}
