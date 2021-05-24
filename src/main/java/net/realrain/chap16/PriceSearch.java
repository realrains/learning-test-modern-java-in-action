package net.realrain.chap16;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PriceSearch {

    private final List<Shop> shops;
//    private final Executors executors = Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
//        @Override
//        public Thread newThread(Runnable r) {
//            Thread t = new Thread(r);
//            t.setDaemon(true);
//            return t;
//        }
//    });

    public PriceSearch(List<Shop> shops) {
        this.shops = shops;
    }

    public List<String> findPrices(String product) {
        return shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product)))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote))))
//                .map(future -> future.thenApply(Discount::applyDiscount))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}
