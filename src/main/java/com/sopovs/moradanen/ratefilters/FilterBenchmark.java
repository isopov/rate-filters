package com.sopovs.moradanen.ratefilters;

import static org.openjdk.jmh.annotations.Threads.MAX;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@Fork(3)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
public class FilterBenchmark {

    @Param({
            "SynchronizedFilter",
            "GuavaFilter",
            "AtomicFilter",
            "SynchronizedDequeFilter",
            "BatchSchedulerFilter",
            "DiscreteSchedulerFilter",
            "AtomicDequeFilter",
    })
    public String filterType;
    private Filter filter;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(".*" + FilterBenchmark.class.getSimpleName() + ".*").build();
        new Runner(opt).run();
    }

    @Setup
    public void setup() {
        filter = Utils.createFilter(filterType);
    }

    @TearDown
    public void tearDown() {
        filter.shutdown();
    }

    @Threads(MAX)
    @Benchmark
    public boolean benchmark() {
        return filter.isSignalAllowed();
    }

}
