package net.realrain.chap7;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamTest {

    double measureElapsedTime (Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        long end = System.nanoTime();
        return (double)(end - start) / 1000000000;
    }

    @Test
    void sequentialSumTest() {
        Function<Long, Long> accSum = n -> Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .reduce(0L, Long::sum);

        double elapsedTime = measureElapsedTime(() -> accSum.apply(100_000_00L));
        System.out.println(elapsedTime);
    }

    @Test
    void parallelSumTest() {
        Function<Long, Long> accSum = n -> Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()
                .reduce(0L, Long::sum);

        double elapsedTime = measureElapsedTime(() -> accSum.apply(100_000_00L));
        System.out.println(elapsedTime);
    }

    @Test
    void parallelSumTest2() {
        Function<Long, Long> accSum = n -> LongStream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()
                .reduce(0L, Long::sum);

        double elapsedTime = measureElapsedTime(() -> accSum.apply(100_000_00L));
        System.out.println(elapsedTime);
    }

    @Test
    void parallelSumTest3() {
        Function<Long, Long> accSum = n -> LongStream.rangeClosed(1, n)
                .parallel()
                .reduce(0L, Long::sum);

        double elapsedTime = measureElapsedTime(() -> accSum.apply(100_000_00L));
        System.out.println(elapsedTime);
    }

    @Test
    void forkJoinSumTest() {
        long[] numbers = LongStream.rangeClosed(1, 100_000_00L).toArray();
        ForkJoinAccumulator task = new ForkJoinAccumulator(numbers);

        double elapsedTime = measureElapsedTime(() -> new ForkJoinPool().invoke(task));
        System.out.println(elapsedTime);
    }
}
