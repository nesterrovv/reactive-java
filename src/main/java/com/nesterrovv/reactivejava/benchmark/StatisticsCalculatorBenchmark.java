package com.nesterrovv.reactivejava.benchmark;

import com.nesterrovv.reactivejava.generator.CompanyGenerator;
import com.nesterrovv.reactivejava.model.Company;
import com.nesterrovv.reactivejava.statistics.improvedparallel.ImprovedParallelStatisticsCalculator;
import com.nesterrovv.reactivejava.statistics.parallel.ParallelStatisticsCalculator;
import com.nesterrovv.reactivejava.statistics.serial.SerialStatisticsCalculator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class StatisticsCalculatorBenchmark {

    @Param({"10", "100", "1000", "10000"})
    private int collectionSize;

    private List<Company> companies;

    @Setup(Level.Invocation)
    public void setUp() {
        companies = CompanyGenerator.generate(collectionSize);
    }

    @Benchmark
    public void serialCalculatorBenchmark() {
        SerialStatisticsCalculator.calculate(companies);
    }

    @Benchmark
    public void parallelCalculatorBenchmark() {
        ParallelStatisticsCalculator.calculate(companies);
    }

    @Benchmark
    public void improvedParallelCalculatorBenchmark() {
        ImprovedParallelStatisticsCalculator.calculate(companies);
    }

}
