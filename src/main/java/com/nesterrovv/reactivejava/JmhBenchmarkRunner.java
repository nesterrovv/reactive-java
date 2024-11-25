package com.nesterrovv.reactivejava;

import com.nesterrovv.reactivejava.benchmark.StatisticsCalculatorBenchmark;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Slf4j
public final class JmhBenchmarkRunner {

    private JmhBenchmarkRunner() {}

    @SuppressWarnings("UncommentedMain")
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(StatisticsCalculatorBenchmark.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(options).run();
    }

}
