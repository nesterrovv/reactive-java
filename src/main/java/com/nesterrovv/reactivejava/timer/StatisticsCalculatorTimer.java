package com.nesterrovv.reactivejava.timer;

import com.nesterrovv.reactivejava.generator.CompanyGenerator;
import com.nesterrovv.reactivejava.model.Company;
import com.nesterrovv.reactivejava.statistics.StatisticsCalculator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class StatisticsCalculatorTimer {

    public static final String MILLISECONDS = " ms\n";

    private StatisticsCalculatorTimer() {}

    public static void calculate(int collectionSize) {
        List<Company> companies = CompanyGenerator.generate(collectionSize);

        long startTime;
        long endTime;

        log.info("\nIterative Statistics Calculation:");
        startTime = System.currentTimeMillis();
        StatisticsCalculator.calculateStatisticsIterative(companies);
        endTime = System.currentTimeMillis();
        log.info("Total Time taken (Iterative): " + (endTime - startTime) + MILLISECONDS);

        log.info("Stream API Statistics Calculation:");
        startTime = System.currentTimeMillis();
        StatisticsCalculator.calculateStatisticsWithStreams(companies);
        endTime = System.currentTimeMillis();
        log.info("Total Time taken (Stream API): " + (endTime - startTime) + MILLISECONDS);

        log.info("Custom Collector Statistics Calculation:");
        startTime = System.currentTimeMillis();
        StatisticsCalculator.calculateStatisticsWithCustomCollector(companies);
        endTime = System.currentTimeMillis();
        log.info("Total Time taken (Custom Collector): " + (endTime - startTime) + MILLISECONDS);
    }

}
