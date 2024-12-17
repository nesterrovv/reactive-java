package com.nesterrovv.reactivejava.statistics.parallel;

import com.nesterrovv.reactivejava.model.Company;
import com.nesterrovv.reactivejava.model.JobResponsibility;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ParallelStatisticsCollector
        implements Collector<Company, Map<String, List<JobResponsibility>>, Map<String, Map<String, Double>>> {

    private static final double TO_PERCENTS = 100.0;

    public static ParallelStatisticsCollector collect() {
        return new ParallelStatisticsCollector();
    }

    @Override
    public Supplier<Map<String, List<JobResponsibility>>> supplier() {
        return ConcurrentHashMap::new;
    }

    @Override
    public BiConsumer<Map<String, List<JobResponsibility>>, Company> accumulator() {
        return (map, company) -> {
            List<JobResponsibility> jobResponsibilities = company.getOffices().parallelStream()
                    .flatMap(office -> office.getColleagues().parallelStream())
                    .flatMap(colleague -> colleague.getJobResponsibilities().parallelStream())
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
        return map -> map.entrySet().parallelStream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            var responsibilityCount = entry.getValue().parallelStream()
                                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

                            long totalResponsibilities = responsibilityCount.values().parallelStream()
                                    .mapToLong(Long::longValue)
                                    .sum();

                            return responsibilityCount.entrySet().parallelStream()
                                    .collect(Collectors.toMap(
                                            frequencyEntry -> frequencyEntry.getKey().getTitle(),
                                            frequencyEntry -> (frequencyEntry.getValue() * TO_PERCENTS)
                                                    / totalResponsibilities
                                    ));
                        }
                ));
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }

}
