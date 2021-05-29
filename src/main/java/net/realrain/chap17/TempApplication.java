package net.realrain.chap17;

import java.util.concurrent.Flow;

public class TempApplication {
    public static void app() {
        getTemperatures("New York").subscribe(new TempSubscriber());
        getCelsiusTemperatures("Seoul").subscribe(new TempSubscriber());
    }

    public static Flow.Publisher<TempInfo> getTemperatures(String town) {
        return subscriber -> subscriber.onSubscribe(new TempSubscription(subscriber, town));
    }

    public static Flow.Publisher<TempInfo> getCelsiusTemperatures(String town) {
        return subscriber -> {
            TempProcessor processor = new TempProcessor();
            processor.subscribe(subscriber);
            processor.onSubscribe(new TempSubscription(processor, town));
        };
    }
}
