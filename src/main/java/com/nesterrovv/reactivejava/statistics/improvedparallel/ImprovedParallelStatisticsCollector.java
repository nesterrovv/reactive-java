package com.nesterrovv.reactivejava.statistics.improvedparallel;

import com.nesterrovv.reactivejava.model.Company;
import com.nesterrovv.reactivejava.model.JobResponsibility;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ImprovedParallelStatisticsCollector
        implements Collector<Company, Map<String, List<JobResponsibility>>, Map<String, Map<String, Double>>> {

    private static final double TO_PERCENTS = 100.0;
    private final ForkJoinPool forkJoinPool;

    public ImprovedParallelStatisticsCollector(int parallelism) {
        this.forkJoinPool = new ForkJoinPool(parallelism);
    }

    public static ImprovedParallelStatisticsCollector collect(int parallelism) {
        return new ImprovedParallelStatisticsCollector(parallelism);
    }

    @Override
    public Supplier<Map<String, List<JobResponsibility>>> supplier() {
        return ConcurrentHashMap::new;
    }

    @Override
    public BiConsumer<Map<String, List<JobResponsibility>>, Company> accumulator() {
        return (map, company) -> {
            List<JobResponsibility> jobResponsibilities = forkJoinPool.invoke(new JobResponsibilitiesTask(company));
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
        return map -> forkJoinPool.invoke(new SplitAndComputeTask(map));
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }

    private static class JobResponsibilitiesTask extends RecursiveTask<List<JobResponsibility>> {

        private final Company company;

        JobResponsibilitiesTask(Company company) {
            this.company = company;
        }

        @Override
        protected List<JobResponsibility> compute() {
            return company.getOffices().parallelStream()
                    .flatMap(office -> office.getColleagues().parallelStream())
                    .flatMap(colleague -> colleague.getJobResponsibilities().parallelStream())
                    .collect(Collectors.toList());
        }
    }

    private static class SplitAndComputeTask extends RecursiveTask<Map<String, Map<String, Double>>> {

        private final Map<String, List<JobResponsibility>> map;

        SplitAndComputeTask(Map<String, List<JobResponsibility>> map) {
            this.map = map;
        }

        @Override
        protected Map<String, Map<String, Double>> compute() {
            int splitSize = Math.max(1, map.size() / Runtime.getRuntime().availableProcessors());
            List<Map.Entry<String, List<JobResponsibility>>> entries = List.copyOf(map.entrySet());

            List<RecursiveTask<Map<String, Map<String, Double>>>> tasks = entries.stream()
                    .collect(Collectors.groupingBy(e -> entries.indexOf(e) / splitSize))
                    .values().stream()
                    .map(SplitTask::new)
                    .collect(Collectors.toList());

            invokeAll(tasks);

            return tasks.stream()
                    .map(RecursiveTask::join)
                    .flatMap(result -> result.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }

    private static class SplitTask extends RecursiveTask<Map<String, Map<String, Double>>> {

        private final List<Map.Entry<String, List<JobResponsibility>>> entries;

        SplitTask(List<Map.Entry<String, List<JobResponsibility>>> entries) {
            this.entries = entries;
        }

        @Override
        protected Map<String, Map<String, Double>> compute() {
            return entries.stream().collect(Collectors.toMap(
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
    }
}
