package com.nesterrovv.reactivejava.statistics.parallel;

import com.nesterrovv.reactivejava.model.Company;
import java.util.List;
import java.util.Map;

public final class ParallelStatisticsCalculator {

    private ParallelStatisticsCalculator() {}

    public static Map<String, Map<String, Double>> calculate(List<Company> companies) {
        return companies.parallelStream().collect(ParallelStatisticsCollector.collect());
    }

}
