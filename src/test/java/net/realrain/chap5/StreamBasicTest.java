package net.realrain.chap5;

import net.realrain.chap4.Dish;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class StreamBasicTest {
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
    void predicateFilteringTest() {
        assertEquals(4L,  menu.stream().filter(Dish::isVegetarian).count());
    }

    @Test
    void distinctElementFilteringTest() {
        List<Dish.Type> distinctTypes = menu.stream().map(Dish::getType).distinct().collect(Collectors.toList());
        assertEquals(3L, distinctTypes.size());
        assertTrue(Arrays.asList(Dish.Type.values()).containsAll(distinctTypes));
    }

    @Test
    void takeWhileTest() {
        List<Dish> dishes = menu.stream().takeWhile(dish -> dish.getCalories() > 350).collect(Collectors.toList());
        assertEquals(4, dishes.size());
    }

    @Test
    void dropWhileTest() {
        assertEquals(5, menu.stream().dropWhile(dish -> dish.getCalories() > 350).count());
    }

    @Test
    void skipTest() {
        List<Dish> dishes = menu.stream()
                .filter(menu -> menu.getCalories() > 300)
                .skip(2)
                .collect(Collectors.toList());

        assertEquals(5, dishes.size());
    }

    @Test
    void mapTest() {
        List<Integer> menuNameLength = menu.stream()
                .map(Dish::getName)
                .map(String::length)
                .collect(Collectors.toList());

        List<String> menuNames = Arrays.asList("pork", "beef", "chicken", "french fries", "rice", "season fruits", "pizza", "prawns", "salmon");
        List<Integer> expect = new ArrayList<>();
        for (var name: menuNames) {
            expect.add(name.length());
        }

        assertEquals(expect, menuNameLength);
    }

    @Test
    void flatMapTest() {
        String[] wordArray = {"Goodbye", "World"};
        List<String> uniqueCharacters = Arrays.stream(wordArray)
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());

        assertEquals(Arrays.asList("GodbyeWrl".split("")), uniqueCharacters);
    }

    @Test
    void createCombinationTest() {
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(4, 5, 6);

        List<int[]> combinations = numbers1.stream()
                .flatMap(i -> numbers2.stream().map(j -> new int[]{i, j}))
                .collect(Collectors.toList());

        combinations.forEach(pair -> System.out.println(Arrays.toString(pair)));

        assertEquals(9, combinations.size());
    }

    @Test
    void createCombinationAndFilterTest() {
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(4, 5, 6);

        List<int[]> filteredCombinations = numbers1.stream()
                .flatMap(i -> numbers2.stream().map(j -> new int[]{i, j}))
                .filter(pair -> pair[0] + pair[1] == 7)
                .collect(Collectors.toList());

        filteredCombinations.forEach(pair -> System.out.println(Arrays.toString(pair)));

        assertEquals(3, filteredCombinations.size());
    }

    @Test
    void matchTest() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        assertTrue(numbers.stream().allMatch(number -> number > 0));
        assertFalse(numbers.stream().allMatch(number -> number % 2 == 0));

        assertTrue(numbers.stream().anyMatch(number -> number >= 5));
        assertFalse(numbers.stream().anyMatch(number -> number >= 6));

        assertTrue(numbers.stream().noneMatch(number -> number > 5));
        assertFalse(numbers.stream().noneMatch(number -> number % 2 == 0));
    }

    @Test
    void findAnyTest() {
        Optional<Dish> vegetarianDish = menu.stream()
                .filter(Dish::isVegetarian)
                .findAny();

        assertTrue(vegetarianDish.isPresent());
        assertTrue(vegetarianDish.get().isVegetarian());
    }

    @Test
    void reduceTest() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(55, numbers.stream().reduce(0, (acc, curr) -> acc + curr));
        assertEquals(55, numbers.stream().reduce(0, Integer::sum));
        assertEquals(55, numbers.stream().reduce(Integer::sum).orElse(0));

        List<Integer> emptyNumbers = Collections.emptyList();
        assertEquals(9999, emptyNumbers.stream().reduce(Integer::sum).orElse(9999));
    }

    @Test
    void findMinMax() {
        List<Integer> numbers = Arrays.asList(1, 5, 3, 2, 6, 4, 8);
        assertEquals(8, numbers.stream().reduce(Integer.MIN_VALUE, (max, curr) -> max < curr ? curr : max));
        assertEquals(1, numbers.stream().reduce(Integer.MAX_VALUE, (min, curr) -> min < curr ? min : curr));

        assertEquals(8, numbers.stream().reduce(Integer::max).get());
        assertEquals(1, numbers.stream().reduce(Integer::min).get());
    }

}
