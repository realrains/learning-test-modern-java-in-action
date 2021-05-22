package net.realrain.chap5;

import net.realrain.chap4.Dish;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StreamTest {

    List<Dish> menu = Arrays.asList(
            new Dish("pork", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruits", true, 120, Dish.Type.OTHER),
            new Dish("pizza", true, 550, Dish.Type.OTHER),
            new Dish("prawns", false, 300, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH)
    );

    @Test
    void intStreamTest() {
        int expect = 0;
        for (var dish: menu) {
            expect += dish.getCalories();
        }

        int sum = menu.stream()
                .mapToInt(Dish::getCalories) // IntStream
                .sum();

        assertEquals(expect, sum);
    }

    @Test
    void restoreIntStream() {
        Stream<Integer> integerStream = menu.stream().mapToInt(Dish::getCalories).boxed();
    }

    @Test
    void optionalIntTest() {
        int expect = 0;
        for (var dish: menu) {
            if (dish.getCalories() > expect) {
                expect = dish.getCalories();
            }
        }

        OptionalInt optionalMax = menu.stream()
                .mapToInt(Dish::getCalories)
                .max();

        int max = optionalMax.orElse(0);

        assertEquals(expect, max);
    }

    @Test
    void rangedIntStream() {
        IntStream evenNumbers = IntStream.rangeClosed(1, 100).filter(n -> n % 2 == 0);
        assertEquals(50, evenNumbers.count());
    }

    @Test
    void generatePythagorasTriplesTest() {
        IntStream.rangeClosed(1, 100)
            .boxed() // IntStream 은 결과로 IntStream 만 허용하므로 boxing
            .flatMap(
                a ->
                    IntStream.rangeClosed(a, 100)
                        .mapToObj(b -> new double[] {a, b, Math.sqrt(a * a + b * b)}))
            .filter(triple -> triple[2] % 1 == 0)
            .forEach(triple -> System.out.println(Arrays.toString(triple)));
    }

    @Test
    void streamOfTest() {
        Stream<String> stream = Stream.of("Modern", "Java", "in", "Action");
        stream.map(String::toUpperCase).forEach(System.out::println);
    }

    @Test
    void nullableStreamTest() {
        Stream.of("config", "home", "user")
            .flatMap(key -> Stream.ofNullable(System.getProperty(key))) // skip null values
            .forEach(System.out::println);
    }

    @Test
    void arrayStreamTest() {
        var numbers = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assertEquals(55, Arrays.stream(numbers).sum());
    }

    @Test
    void fileStreamTest() {
        long uniqueWords = 0;

        // autoClosable
        try (Stream<String> lines = Files.lines(Paths.get("./src/test/resources/words.txt"), Charset.defaultCharset())) {
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(5, uniqueWords);
    }

    @Test
    void unboundStreamTest() {
        assertEquals(5050, IntStream.iterate(1, n -> n + 1).limit(100).sum());

        // fibonacci
        Stream.iterate(new int[] {0, 1}, f -> new int[] {f[1], f[0] + f[1]})
                .map(f -> f[1])
                .limit(10)
                .forEach(System.out::println);
    }

    @Test
    void unboundStreamWithPredicateTest() {
        assertEquals(5050, IntStream.iterate(1, n -> n + 1).takeWhile(n -> n <= 100).sum());
        assertEquals(5050, IntStream.iterate(1, n -> n <= 100, n -> n + 1).sum());
    }

    @Test
    void unboundStreamWithSupplierTest() {
        Stream.generate(Math::random).limit(10).forEach(System.out::println);
    }
}
