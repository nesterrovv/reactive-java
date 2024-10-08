package com.nesterrovv.reativejava.statistics;

import com.nesterrovv.reativejava.model.Colleague;
import com.nesterrovv.reativejava.model.Color;
import com.nesterrovv.reativejava.model.JobResponsibility;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public final class StatisticsCalculator {

    public static final String AVERAGE_AGE = "Average Age: ";
    public static final String POPULAR_PASSPORT_ISSUE = "Most Popular Passport Issued By: ";
    public static final String AVERAGE_WORKLOAD = "Average Workload (hours per week): ";
    public static final String HAIR_COLOR = "Hair Color ";
    public static final String EYE_COLOR = "Eye Color ";
    public static final String NONE = "None";
    public static final int TO_PERCENTS = 100;

    private StatisticsCalculator() {}

    // 3.1 Iterative approach
    public static void calculateStatisticsIterative(List<Colleague> colleagues) {
        int totalAge = 0;
        Map<Color, Integer> hairColorCount = new HashMap<>();
        Map<Color, Integer> eyeColorCount = new HashMap<>();
        Map<String, Integer> issuedByCount = new HashMap<>();
        int totalWorkload = 0;

        for (Colleague colleague : colleagues) {
            totalAge += colleague.getAge();

            hairColorCount.put(colleague.getHairColor(),
                    hairColorCount.getOrDefault(colleague.getHairColor(), 0) + 1);
            eyeColorCount.put(colleague.getEyeColor(),
                    eyeColorCount.getOrDefault(colleague.getEyeColor(), 0) + 1);

            String issuedBy = colleague.getPassportData().issuedBy();
            issuedByCount.put(issuedBy, issuedByCount.getOrDefault(issuedBy, 0) + 1);

            for (JobResponsibility responsibility : colleague.getJobResponsibilities()) {
                totalWorkload += responsibility.getHoursPerWeek();
            }
        }

        double averageAge = (double) totalAge / colleagues.size();
        log.info(AVERAGE_AGE + averageAge);

        for (Map.Entry<Color, Integer> entry : hairColorCount.entrySet()) {
            log.info(HAIR_COLOR + entry.getKey() + ": "
                    + ((double) entry.getValue() / colleagues.size() * TO_PERCENTS) + "%");
        }

        for (Map.Entry<Color, Integer> entry : eyeColorCount.entrySet()) {
            log.info(EYE_COLOR + entry.getKey() + ": "
                    + ((double) entry.getValue() / colleagues.size() * TO_PERCENTS) + "%");
        }

        String popularIssuedBy = issuedByCount.entrySet().stream().max(
                Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(NONE);
        log.info(POPULAR_PASSPORT_ISSUE + popularIssuedBy);

        double averageWorkload = (double) totalWorkload / colleagues.size();
        log.info(AVERAGE_WORKLOAD + averageWorkload);
    }

    // 3.2 Stream API with standard collectors
    public static void calculateStatisticsWithStreams(List<Colleague> colleagues) {
        double averageAge = colleagues.stream().collect(Collectors.averagingInt(Colleague::getAge));
        log.info(AVERAGE_AGE + averageAge);

        Map<Color, Double> hairColorPercentage = colleagues.stream().collect(
                Collectors.groupingBy(Colleague::getHairColor, Collectors.collectingAndThen(Collectors.counting(),
                        count -> (double) count / colleagues.size() * TO_PERCENTS)));
        hairColorPercentage.forEach((color, percentage) -> log.info(HAIR_COLOR + color + ": " + percentage + "%"));

        Map<Color, Double> eyeColorPercentage = colleagues.stream().collect(Collectors.groupingBy(
                Colleague::getEyeColor, Collectors.collectingAndThen(Collectors.counting(),
                        count -> (double) count / colleagues.size() * TO_PERCENTS)));
        eyeColorPercentage.forEach((color, percentage) -> log.info(EYE_COLOR + color + ": " + percentage + "%"));

        String popularIssuedBy = colleagues.stream().collect(Collectors.groupingBy(
                colleague -> colleague.getPassportData().issuedBy(), Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(NONE);
        log.info(POPULAR_PASSPORT_ISSUE + popularIssuedBy);

        double averageWorkload = colleagues.stream().collect(Collectors.averagingDouble(
                colleague -> colleague.getJobResponsibilities().stream().mapToInt(
                        JobResponsibility::getHoursPerWeek).sum()));
        log.info(AVERAGE_WORKLOAD + averageWorkload);
    }

    // 3.3 Custom collector
    public static void calculateStatisticsWithCustomCollector(List<Colleague> colleagues) {
        Statistics statistics = colleagues.stream().collect(StatisticsCollector.collect());
        log.info(String.valueOf(statistics));
    }
}
