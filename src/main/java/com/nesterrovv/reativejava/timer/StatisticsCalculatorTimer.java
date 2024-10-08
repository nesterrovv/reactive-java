package com.nesterrovv.reativejava.timer;

import com.nesterrovv.reativejava.generator.ColleagueGenerator;
import com.nesterrovv.reativejava.model.Colleague;
import com.nesterrovv.reativejava.statistics.StatisticsCalculator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class StatisticsCalculatorTimer {

    public static final String MILLISECONDS = " ms\n";

    private StatisticsCalculatorTimer() {}

    public static void calculate(int collectionSize) {
        List<Colleague> colleagues = ColleagueGenerator.generateColleagues(collectionSize);
        //colleagues.forEach(System.out::println);

        long startTime;
        long endTime;

        log.info("\nIterative Statistics Calculation:");
        startTime = System.currentTimeMillis();
        StatisticsCalculator.calculateStatisticsIterative(colleagues);
        endTime = System.currentTimeMillis();
        log.info("Total Time taken (Iterative): " + (endTime - startTime) + MILLISECONDS);

        log.info("Stream API Statistics Calculation:");
        startTime = System.currentTimeMillis();
        StatisticsCalculator.calculateStatisticsWithStreams(colleagues);
        endTime = System.currentTimeMillis();
        log.info("Total Time taken (Stream API): " + (endTime - startTime) + MILLISECONDS);

        log.info("Custom Collector Statistics Calculation:");
        startTime = System.currentTimeMillis();
        StatisticsCalculator.calculateStatisticsWithCustomCollector(colleagues);
        endTime = System.currentTimeMillis();
        log.info("Total Time taken (Custom Collector): " + (endTime - startTime) + MILLISECONDS);
    }

}
