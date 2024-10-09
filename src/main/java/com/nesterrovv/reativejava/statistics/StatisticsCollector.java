package com.nesterrovv.reativejava.statistics;

import com.nesterrovv.reativejava.model.Colleague;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

class StatisticsCollector implements Collector<Colleague, StatisticsAccumulator, Statistics> {

    public static StatisticsCollector collect() {
        return new StatisticsCollector();
    }

    @Override
    public Supplier<StatisticsAccumulator> supplier() {
        return StatisticsAccumulator::new;
    }

    @Override
    public BiConsumer<StatisticsAccumulator, Colleague> accumulator() {
        return StatisticsAccumulator::accumulate;
    }

    @Override
    public BinaryOperator<StatisticsAccumulator> combiner() {
        return StatisticsAccumulator::combine;
    }

    @Override
    public Function<StatisticsAccumulator, Statistics> finisher() {
        return StatisticsAccumulator::finish;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.noneOf(Characteristics.class);
    }

}
