package net.realrain.chap4;

import com.google.common.collect.Ordering;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StreamChapterTest {

    @Test
    void filterAndSortTest() {
        List<Integer> numbers = Arrays.asList(4, 1, 6, 3, 2, 5);
        List<Integer> evenSortedNumbers = new ArrayList<>();
        Predicate<Integer> isEven = number -> number % 2 == 0;

        for (Integer number: numbers) {
            if (isEven.test(number)) {
                evenSortedNumbers.add(number);
            }
        }

        evenSortedNumbers.sort(Integer::compareTo);

        for (Integer number: evenSortedNumbers) {
            assertEquals(0, number % 2);
        }

        assertTrue(Ordering.from(Integer::compareTo).isOrdered(evenSortedNumbers));
    }

    @Test
    void streamFilterAndSortTest() {
        List<Integer> numbers = Arrays.asList(4, 1, 6, 3, 2, 5);
        List<Integer> evenSortedNumbers = numbers.stream()
                .filter(number -> number % 2 == 0)
                .sorted(Integer::compareTo)
                .collect(Collectors.toList());

        for (Integer number: evenSortedNumbers) {
            assertEquals(0, number % 2);
        }

        assertTrue(Ordering.from(Integer::compareTo).isOrdered(evenSortedNumbers));
    }

    @Test
    void parallelStreamFilterAndSortTest() {
        List<Integer> numbers = Arrays.asList(4, 1, 6, 3, 2, 5);
        List<Integer> evenSortedNumbers = numbers.stream()
                .parallel() // running on multi-thread
                .filter(number -> number % 2 == 0)
                .sorted(Integer::compareTo)
                .collect(Collectors.toList());

        for (Integer number: evenSortedNumbers) {
            assertEquals(0, number % 2);
        }

        assertTrue(Ordering.from(Integer::compareTo).isOrdered(evenSortedNumbers));
    }

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
    void filterHighCaloriesDishNamesTest() {
        List<String> threeHighCaloriesDishes = menu.stream()
                .filter(dish -> dish.getCalories() > 350)
                .map(Dish::getName)
                .limit(3)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList("pork", "beef", "chicken"), threeHighCaloriesDishes);
    }

    @Test
    void streamConsumeSourceOnlyOnceTest() {
        List<Integer> numbers = List.of(1, 2, 3);
        Stream<Integer> numberStream = numbers.stream();
        numberStream.forEach(System.out::println);

        assertThrows(IllegalStateException.class, () -> numberStream.forEach(System.out::println));
    }

}
