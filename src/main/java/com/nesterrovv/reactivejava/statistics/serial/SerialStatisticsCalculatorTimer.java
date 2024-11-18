package com.nesterrovv.reactivejava.statistics.serial;

import com.nesterrovv.reactivejava.generator.CompanyGenerator;
import com.nesterrovv.reactivejava.model.Company;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class SerialStatisticsCalculatorTimer {

    public static final String COMPANY = "Company: ";
    public static final String RESPONSIBILITY = "Responsibility: ";
    public static final String PERCENTAGE = "Percentage: ";
    public static final String MILLISECONDS = "ms";

    private SerialStatisticsCalculatorTimer() {}

    public static Long calculate(int collectionSize, boolean needPrint) {
        List<Company> companies = CompanyGenerator.generate(collectionSize);
        long startTime;
        long endTime;
        startTime = System.currentTimeMillis();
        Map<String, Map<String, Double>> collection
                = SerialStatisticsCalculator.calculateStatisticsWithCustomCollector(companies);
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
        String[] array = collection.entrySet().stream()
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
        return Arrays.toString(array);
    }

}
