package com.nesterrovv.reactivejava.statistics.serial;

import com.nesterrovv.reactivejava.model.Company;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class SerialStatisticsCalculator {

    private SerialStatisticsCalculator() {}

    public static Map<String, Map<String, Double>> calculate(List<Company> companies) {
        return companies.stream().collect(SerialStatisticsCollector.collect());
    }

}
