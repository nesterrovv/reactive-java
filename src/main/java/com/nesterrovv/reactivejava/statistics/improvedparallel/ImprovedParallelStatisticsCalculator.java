package com.nesterrovv.reactivejava.statistics.improvedparallel;

import com.nesterrovv.reactivejava.model.Company;
import java.util.List;
import java.util.Map;

public final class ImprovedParallelStatisticsCalculator {

    public static final int PARALLELISM_LEVEL = 5;

    private ImprovedParallelStatisticsCalculator() {}

    public static Map<String, Map<String, Double>> calculate(List<Company> companies) {
        return companies.parallelStream().collect(ImprovedParallelStatisticsCollector.collect(PARALLELISM_LEVEL));
    }

}
