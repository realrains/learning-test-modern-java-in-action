package net.realrain.chap7;

import java.util.concurrent.RecursiveTask;

public class ForkJoinAccumulator extends RecursiveTask<Long> {
    private final long[] numbers;
    private final int start;
    private final int end;
    public static final long THRESHOLD = 10000;

    public ForkJoinAccumulator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    public ForkJoinAccumulator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially();
        }
        ForkJoinAccumulator leftTask = new ForkJoinAccumulator(numbers, start, start + length / 2);
        leftTask.fork();

        ForkJoinAccumulator rightTask = new ForkJoinAccumulator(numbers, start + length / 2, end);
        Long rightResult = rightTask.compute();
        Long leftResult = leftTask.join();
        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }
}
