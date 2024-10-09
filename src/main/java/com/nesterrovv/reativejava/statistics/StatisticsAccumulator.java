package com.nesterrovv.reativejava.statistics;

import com.nesterrovv.reativejava.model.Colleague;
import com.nesterrovv.reativejava.model.Color;
import com.nesterrovv.reativejava.model.JobResponsibility;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class StatisticsAccumulator {

    public static final int TO_PERCENTS = 100;
    private Map<Color, Integer> hairColorCount = new HashMap<>();
    private Map<Color, Integer> eyeColorCount = new HashMap<>();
    private Map<String, Integer> issuedByCount = new HashMap<>();
    private int totalWorkload = 0;
    private int totalAge = 0;
    private int count = 0;

    public void accumulate(Colleague colleague) {
        totalAge += colleague.getAge();
        count++;

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

    public StatisticsAccumulator combine(StatisticsAccumulator other) {
        totalAge += other.totalAge;
        count += other.count;

        other.hairColorCount.forEach((key, value) -> hairColorCount.merge(key, value, Integer::sum));
        other.eyeColorCount.forEach((key, value) -> eyeColorCount.merge(key, value, Integer::sum));
        other.issuedByCount.forEach((key, value) -> issuedByCount.merge(key, value, Integer::sum));

        totalWorkload += other.totalWorkload;

        return this;
    }

    public Statistics finish() {
        double averageAge = (double) totalAge / count;
        Map<Color, Double> hairColorPercentage =
                hairColorCount.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey, e -> (double) e.getValue() / count * TO_PERCENTS)
                );
        Map<Color, Double> eyeColorPercentage =
                eyeColorCount.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey, e -> (double) e.getValue() / count * TO_PERCENTS)
                );
        String mostPopularIssuedBy = issuedByCount.entrySet().stream().max(
                Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("None");
        double averageWorkload = (double) totalWorkload / count;

        Statistics statistics = new Statistics();
        statistics.averageAge = averageAge;
        statistics.hairColorPercentage = hairColorPercentage;
        statistics.eyeColorPercentage = eyeColorPercentage;
        statistics.mostPopularIssuedBy = mostPopularIssuedBy;
        statistics.averageWorkload = averageWorkload;

        return statistics;
    }
}
