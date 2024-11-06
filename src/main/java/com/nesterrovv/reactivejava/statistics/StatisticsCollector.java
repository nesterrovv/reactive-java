package com.nesterrovv.reactivejava.statistics;

import com.nesterrovv.reactivejava.model.Company;
import com.nesterrovv.reactivejava.model.JobResponsibility;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class StatisticsCollector implements Collector<Company, Map<String, List<JobResponsibility>>, Map<String, Map<String, Double>>> {

    public static StatisticsCollector collect() {
        return new StatisticsCollector();
    }

    @Override
    public Supplier<Map<String, List<JobResponsibility>>> supplier() {
        return HashMap<String, List<JobResponsibility>>::new;
    }

    @Override
    public BiConsumer<Map<String, List<JobResponsibility>>, Company> accumulator() {
        return (map, company) -> {
            List<JobResponsibility> jobResponsibilities = company.getOffices().stream()
                    .flatMap(office -> office.getColleagues().stream())
                    .flatMap(colleague -> colleague.getJobResponsibilities().stream())
                    .collect(Collectors.toList());

            map.put(company.getTitle(), jobResponsibilities);
        };
    }

    @Override
    public BinaryOperator<Map<String, List<JobResponsibility>>> combiner() {
        return (map1, map2) -> {
            map2.putAll(map1);
            return map2;
        };
    }

    @Override
    public Function<Map<String, List<JobResponsibility>>, Map<String, Map<String, Double>>> finisher() {
        return map -> map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            var responsibilityCount = entry.getValue().stream()
                                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

                            long totalResponsibilities = responsibilityCount.values().stream()
                                    .mapToLong(Long::longValue)
                                    .sum();

                            return responsibilityCount.entrySet().stream()
                                    .collect(Collectors.toMap(
                                            frequencyEntry -> frequencyEntry.getKey().getTitle(),
                                            frequencyEntry -> (frequencyEntry.getValue() * 100.0) / totalResponsibilities
                                    ));
                        }
                ));
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }

}
