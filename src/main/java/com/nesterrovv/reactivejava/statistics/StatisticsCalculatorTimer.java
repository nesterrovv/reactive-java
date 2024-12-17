package com.nesterrovv.reactivejava.statistics;

import com.nesterrovv.reactivejava.generator.CompanyGenerator;
import com.nesterrovv.reactivejava.model.Company;
import com.nesterrovv.reactivejava.statistics.improvedparallel.ImprovedParallelStatisticsCalculator;
import com.nesterrovv.reactivejava.statistics.parallel.ParallelStatisticsCalculator;
import com.nesterrovv.reactivejava.statistics.serial.SerialStatisticsCalculator;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class StatisticsCalculatorTimer {

    public static final String COMPANY = "Company: ";
    public static final String RESPONSIBILITY = "Responsibility: ";
    public static final String PERCENTAGE = "Percentage: ";
    public static final String MILLISECONDS = "ms";

    private StatisticsCalculatorTimer() {}

    @SuppressWarnings("InnerAssignment")
    public static Long calculate(int collectionSize, boolean needPrint, StatisticsType statisticsType) {
        List<Company> companies = CompanyGenerator.generate(collectionSize);
        long startTime;
        long endTime;
        startTime = System.currentTimeMillis();
        Map<String, Map<String, Double>> collection;
        switch (statisticsType) {
            case SERIAL -> collection = SerialStatisticsCalculator.calculate(companies);
            case PARALLEL -> collection
                    = ParallelStatisticsCalculator.calculate(companies);
            case IMPROVED_PARALLEL -> collection = ImprovedParallelStatisticsCalculator.calculate(companies);
            default -> collection = Collections.emptyMap();
        }
        endTime = System.currentTimeMillis();
        log.info(String.format("Total time taken for collection of size %d is %d %s",
                companies.size(), (endTime - startTime), MILLISECONDS)
        );
        if (needPrint) {
            log.info(printAggregatingCollection(collection));
        }
        return (endTime - startTime);
    }

    private static String printAggregatingCollection(Map<String, Map<String, Double>> collection) {
        String[] array = collection.entrySet().parallelStream()
                .map(entry -> {
                    String companyName = entry.getKey();
                    Map<String, Double> statistic = entry.getValue();
                    StringBuilder builder = new StringBuilder(COMPANY + companyName + ": ");
                    String stats = statistic
                            .entrySet()
                            .stream()
                            .map(e -> RESPONSIBILITY + e.getKey() + ", " + PERCENTAGE + e.getValue() + "%")
                            .collect(Collectors.joining("; "));
                    builder.append(stats);
                    return builder.toString();
                })
                .toArray(String[]::new);
        return String.join("\n", array);
    }

}
