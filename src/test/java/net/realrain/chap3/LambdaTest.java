package net.realrain.chap3;

import com.google.common.collect.Ordering;
import net.realrain.chap1.Apple;
import net.realrain.chap1.Color;
import net.realrain.chap2.BehaviorParameterization;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

class LambdaTest {

    @Test
    void functionalInterfaceTest() {
        // functional interface is interface which have only one abstract method.
        //
        // public interface Runnable {
        //    public abstract void run();
        // }
        //
        Runnable r1 = () -> System.out.println("Hello World");
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World 2");
            }
        };

        r1.run();
        r2.run();
    }

    @Test
    void processFileTest() throws IOException {
        String filename = "./src/test/resources/data.txt";
        assertEquals("Hello World", Lambda.processFile(filename));
    }

    @Test
    void parameterizedProcessFileTest() throws IOException {
        String filename = "./src/test/resources/data.txt";
        assertEquals("Hello World", Lambda.parameterizedProcessFile(br -> br.readLine(), filename));
        assertEquals("Hello WorldHello Java", Lambda.parameterizedProcessFile(br -> br.readLine() + br.readLine(), filename));
    }

    @Test
    void predicateInterfaceTest() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        assertEquals(Arrays.asList(2, 4, 6), BehaviorParameterization.filter(numbers, n -> n % 2 == 0));
    }

    @Test
    void consumerInterfaceTest() {
        Consumer<String> stringConsumer = s -> System.out.println("Hello " + s);
        List.of("Java", "Kotlin", "Scala").forEach(stringConsumer);
    }

    @Test
    void functionInterfaceTest() {
        Function<String, Integer> stringCounter = s -> s.length();
        for (String word: List.of("Java", "Kotlin", "Scala")) {
            assertEquals(word.length(), stringCounter.apply(word));
        }
    }

    @Test
    void lambdaCapturingTest() {
        Integer freeVariable = 1234; // captured variable should be final or threat like a final variable.
        Runnable runnable = () -> {
            System.out.println(freeVariable); // capturing, is not closure
        };
//        freeVariable = 3456; <- compile error!
    }

    @Test
    void methodReferenceTest() {
        List<String> words = List.of("", "hello", "world");
        assertEquals(1, BehaviorParameterization.filter(words, String::isEmpty).size());
    }

    @Test
    void methodReferenceTypesTest() {
        // method reference rules
        // 1. refer static method
        // from: (args) -> ClassName.staticMethod(args)
        // to: ClassName::staticMethod
        Predicate<String> stringPredicate = StringUtils::isBlank;
        assertTrue(stringPredicate.test("      "));

        // 2. refer instance method
        // from: (args, rest) -> args.instanceMethod(rest)
        // to: ClassName::instanceMethod
        Function<String, String> stringFunction = String::new;
        assertEquals("Hello", stringFunction.apply("Hello"));

        // 3. refer instance method of existing instance
        // from: (args) -> expr.instanceMethod(args)
        // to: expr::instanceMethod
        final Integer exist = 100;
        Function<Integer, Integer> integerPredicate = exist::compareTo;
        assertEquals(0, integerPredicate.apply(100));
    }

    @Test
    void constructorReferenceTest() {
        Supplier<String> stringSupplier = String::new;
        assertEquals("", stringSupplier.get());

        Function<String, Integer> stringFunction = String::length;
        assertEquals(5, stringFunction.apply("hello"));

        BiFunction<Color, Integer, Apple> appleFunction = Apple::new;
        Apple apple = appleFunction.apply(Color.RED, 100);
        assertEquals(Color.RED, apple.getColor());
        assertEquals(100, apple.getWeight());
    }

    @Test
    void simplifyBehaviorParameterTest() {
        List<Apple> apples = Arrays.asList(
                new Apple(Color.RED, 100),
                new Apple(Color.GREEN, 20),
                new Apple(Color.RED, 150)
        );


        apples.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        });

        apples.sort((o1, o2) -> o1.getWeight().compareTo(o2.getWeight()));

        apples.sort(Comparator.comparing(apple -> apple.getWeight()));

        apples.sort(Comparator.comparing(Apple::getWeight));

        assertTrue(Ordering.from(Comparator.comparing(Apple::getWeight)).isOrdered(apples));
    }

    @Test
    void reverseComparatorTest() {
        List<Apple> apples = Arrays.asList(
                new Apple(Color.RED, 100),
                new Apple(Color.GREEN, 20),
                new Apple(Color.RED, 150)
        );

        apples.sort(Comparator.comparing(Apple::getWeight).reversed());
        assertFalse(Ordering.from(Comparator.comparing(Apple::getWeight)).isOrdered(apples));
        assertTrue(Ordering.from(Comparator.comparing(Apple::getWeight).reversed()).isOrdered(apples));
    }

    @Test
    void comparatorChainingTest() {
        List<Apple> apples = Arrays.asList(
                new Apple(Color.RED, 100),
                new Apple(Color.GREEN, 20),
                new Apple(Color.RED, 150),
                new Apple(Color.GREEN, 30)
        );

        apples.sort(Comparator.comparing(Apple::getWeight)
                .reversed()
                .thenComparing(Apple::getColor));

        apples.forEach(apple -> System.out.println(apple.getColor() + " " + apple.getWeight()));

        assertFalse(Ordering.from(Comparator.comparing(Apple::getWeight)).isOrdered(apples));
        assertTrue(Ordering.from(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor)).isOrdered(apples));
    }

    @Test
    void predicateCompositionTest() {
        List<Apple> apples = Arrays.asList(
                new Apple(Color.RED, 100),
                new Apple(Color.GREEN, 20),
                new Apple(Color.RED, 150),
                new Apple(Color.GREEN, 30)
        );

        Predicate<Apple> isHeavy = apple -> apple.getWeight() > 100;
        Predicate<Apple> isLight = isHeavy.negate();
        Predicate<Apple> isRed = apple -> Color.RED.equals(apple.getColor());
        Predicate<Apple> isRedAndLight = isLight.and(isRed);

        assertEquals(1, BehaviorParameterization.filter(apples, isRedAndLight).size());
    }

    @Test
    void functionCompositionTest() {
        Function<Integer, Integer> add1 = x -> x + 1;
        Function<Integer, Integer> multiply2 = x -> x * 2;

        Function<Integer, Integer> multiply2AfterAdd1 = multiply2.compose(add1);
        assertEquals(22, multiply2AfterAdd1.apply(10));

        Function<Integer, Integer> add1AfterMultiply2 = multiply2.andThen(add1);
        assertEquals(21, add1AfterMultiply2.apply(10));
    }
}
