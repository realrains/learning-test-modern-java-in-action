package net.realrain.chap16;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrentTest {

    @Test
    void simpleFuture() {
        // Before Java8
        ExecutorService executor = Executors.newCachedThreadPool();

        Future<Double> future = executor.submit(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                // doSomethingLongComputation();
                return 1.00;
            }
        });

        // doSomethingElse();

        try {
            Double result = future.get(1, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    void priceAsync() {
        Shop shop = new Shop("BestShop");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
        long invocationTime = System.nanoTime();
        System.out.println("invocation returned after " + (invocationTime - start) / 1000000 + "ms");

        // doSomething

        try {
            double price = futurePrice.get();
            System.out.println("Price is " +  price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long retrievalTime = System.nanoTime();
        System.out.println("invocation returned after " + (retrievalTime - start) / 1000000 + "ms");
    }

    @Test
    void searchPrice() {
        PriceSearch priceSearch = new PriceSearch(Arrays.asList(
                new Shop("ShopA"),
                new Shop("ShopB"),
                new Shop("ShopC"),
                new Shop("ShopD"),
                new Shop("ShopE")
        ));

        long start = System.nanoTime();
        System.out.println(priceSearch.findPrices("myPhone13S"));
        long ends = System.nanoTime();
        System.out.println("Done in " + (ends - start) / 1000000 + "ms");
    }


}
