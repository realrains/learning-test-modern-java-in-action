package net.realrain.chap2;

import net.realrain.chap1.Apple;
import net.realrain.chap1.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class BehaviorParameterizationTest {
    private List<Apple> apples;

    @BeforeEach
    void setApples() {
        apples = Arrays.asList(
                new Apple(Color.GREEN, 50),
                new Apple(Color.GREEN, 150),
                new Apple(Color.RED, 200),
                new Apple(Color.RED, 300));
    }

    @Test
    void filterGreenAppleTest() {
        assertEquals(2, BehaviorParameterization.filterGreenApples(apples).size());
    }

    @Test
    void filterApplesByColorTest() {
        assertEquals(2, BehaviorParameterization.filterApplesByColor(apples, Color.GREEN).size());
        assertEquals(2, BehaviorParameterization.filterApplesByColor(apples, Color.RED).size());
    }

    @Test
    void filterApplesByWeightTest() {
        assertEquals(3, BehaviorParameterization.filterApplesByWeight(apples, 100).size());
        assertEquals(2, BehaviorParameterization.filterApplesByWeight(apples, 150).size());
        assertEquals(0, BehaviorParameterization.filterApplesByWeight(apples, 300).size());
    }

    @Test
    void filterApplesByApplePredicateTest() {
        assertEquals(2, BehaviorParameterization.filterApples(apples, new AppleGreenColorPredicate()).size());
        assertEquals(2, BehaviorParameterization.filterApples(apples, new AppleHeavyWeightPredicate()).size());
    }

    @Test
    void prettyFormatTest() {
        BehaviorParameterization.prettyPrintApple(apples, apple -> String.format("Color : %s, Weight : %d", apple.getColor().toString(), apple.getWeight()));
    }

    @Test
    void genericFilterTest() {
        assertEquals(2, BehaviorParameterization.filter(apples, apple -> Color.RED.equals(apple.getColor())).size());
    }

    @Test
    void sortByComparatorTest() {
        apples.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        });
    }

    @Test
    void runnableTest() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World!");
            }
        });
    }

    @Test
    void executorServiceTest() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> threadName = executorService.submit(() -> Thread.currentThread().getName());
        System.out.println(threadName.get());
    }
}
