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
            company.getOffices().stream()  // Итерируем по офисам
                    .forEach(office -> {
                                List<JobResponsibility> currentJobResponsibilities = map.getOrDefault(company.getTitle(), new ArrayList<>());

                                office.getColleagues().stream()
                                        .flatMap(colleague -> colleague.getJobResponsibilities().stream())
                                        .forEach(currentJobResponsibilities::add);

                                map.put(company.getTitle(), currentJobResponsibilities); // Собираем их в список

                            }
                    );
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
        return map -> {
            Map<String, Map<String, Double>> statistics = new HashMap<>();

            map.entrySet().stream().forEach( entry -> {
                var responsibilityCount = entry.getValue().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

                long totalResponsibilities = responsibilityCount.values().stream()
                        .mapToLong(Long::longValue)
                        .sum();

                Map<String, Double> responsibilityFrequency = responsibilityCount.entrySet().stream()
                        .collect(Collectors.toMap(
                                frequencyEntry -> frequencyEntry.getKey().getTitle(),
                                frequencyEntry -> (frequencyEntry.getValue() * 100.0) / totalResponsibilities
                        ));

                statistics.put(entry.getKey(), responsibilityFrequency);
            });

            return statistics;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }

}
