package net.realrain.chap15;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrentTest {

    Integer f(Integer x) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return x;
    }

    Integer g(Integer x) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return x;
    }

    @Test
    void threadJoin() throws InterruptedException {
        int[] result = new int[] {0, 0};

        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result[0] = 1000;
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result[1] = 1000;
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(result[0] + result[1]);
    }

    @Test
    void future() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> result1 = executorService.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1000;
        });

        Future<Integer> result2 = executorService.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1000;
        });

        System.out.println(result1.get() + result2.get());
    }

    @Test
    void completableFuture() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<Integer> a = new CompletableFuture<>();
        executorService.submit(() -> a.complete(f(1000)));
        int b = g(1000);
        System.out.println(a.get() + b); // wait b

        executorService.shutdown();
    }

    @Test
    void combineCompletableFuture() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        CompletableFuture<Integer> a = new CompletableFuture<>();
        CompletableFuture<Integer> b = new CompletableFuture<>();
        CompletableFuture<Integer> c = a.thenCombine(b, Integer::sum);

        executorService.submit(() -> a.complete(f(1000)));
        executorService.submit(() -> b.complete(g(1000)));

        System.out.println(c.get());
        executorService.shutdown();
    }


}
