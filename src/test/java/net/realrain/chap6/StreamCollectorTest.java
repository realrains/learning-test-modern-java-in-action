package net.realrain.chap6;

import net.realrain.chap4.Dish;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class StreamCollectorTest {

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
    void countingTest() {
        long numberOfDishes = menu.stream().collect(Collectors.counting());
        assertEquals(numberOfDishes, menu.stream().count());
    }

    @Test
    void minmaxTest() {
        Optional<Dish> maxCaloriesDish = menu.stream().collect(Collectors.maxBy(Comparator.comparing(Dish::getCalories)));
        assertEquals(maxCaloriesDish.get(), menu.stream().max(Comparator.comparing(Dish::getCalories)).get());
    }

    @Test
    void summingIntTest() {
        int totalCalories = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
        assertEquals(totalCalories, menu.stream().mapToInt(Dish::getCalories).sum());
    }

    @Test
    void summarizeStatTest() {
        long totalCalories = menu.stream().mapToInt(Dish::getCalories).sum();
        double averageCalories = menu.stream().collect(Collectors.averagingInt(Dish::getCalories));
        long maxCalories = menu.stream().mapToInt(Dish::getCalories).max().orElse(0);
        long minCalories = menu.stream().mapToInt(Dish::getCalories).min().orElse(0);
        long menuCount = menu.size();

        IntSummaryStatistics stat = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));

        assertEquals(totalCalories, stat.getSum());
        assertEquals(averageCalories, stat.getAverage());
        assertEquals(maxCalories, stat.getMax());
        assertEquals(minCalories, stat.getMin());
        assertEquals(menuCount, stat.getCount());
    }

    @Test
    void joiningTest() {
        String menuNames = menu.stream().map(Dish::getName).collect(Collectors.joining());
        System.out.println(menuNames);
    }

    @Test
    void generalReducingTest() {
        Integer sumCalories = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, Integer::sum));
        assertEquals(sumCalories, menu.stream().mapToInt(Dish::getCalories).sum());
    }

    public enum CaloricLevel { DIET, NORMAL, FAT }

    @Test
    void groupingTest() {
        Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(Collectors.groupingBy(Dish::getType));
        System.out.println(dishesByType);

        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(Collectors.groupingBy(dish -> {
            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
            else return CaloricLevel.FAT;
        }));
        System.out.println(dishesByCaloricLevel);
    }

    @Test
    void groupFilteringTest() {
        Map<Dish.Type, List<Dish>> caloricDishesByType = menu.stream().collect(
                Collectors.groupingBy(Dish::getType,
                        Collectors.filtering(dish -> dish.getCalories() > 500, Collectors.toList())
                )
        );
        System.out.println(caloricDishesByType);
    }

    @Test
    void groupMappingTest() {
        Map<Dish.Type, List<String>> dishNamesByType = menu.stream().collect(
                Collectors.groupingBy(Dish::getType,
                        Collectors.mapping(Dish::getName, Collectors.toList())
                )
        );
        System.out.println(dishNamesByType);
    }

    @Test
    void multiLevelGroupingTest() {
        Function<Dish, CaloricLevel> caloricLevel = dish -> {
            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
            else return CaloricLevel.FAT;
        };

        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeAndCaloricLevel = menu.stream().collect(
                Collectors.groupingBy(Dish::getType,
                        Collectors.groupingBy(caloricLevel)
                )
        );

        System.out.println(dishesByTypeAndCaloricLevel);
    }

    @Test
    void mostCaloricByTypeTest() {
        Map<Dish.Type, Optional<Dish>> mostCaloricByType = menu.stream().collect(
            Collectors.groupingBy(Dish::getType,
                Collectors.maxBy(Comparator.comparingInt(Dish::getCalories))
            )
        );
        System.out.println(mostCaloricByType);
    }

    @Test
    void collectingAndThenTest() {
        Map<Dish.Type, Dish> mostCaloricByType = menu.stream().collect(
                Collectors.groupingBy(Dish::getType,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)),
                                Optional::get
                        )
                )
        );
        System.out.println(mostCaloricByType);
    }

    @Test
    void groupingAndMapping() {
        Function<Dish, CaloricLevel> caloricLevel = dish -> {
            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
            else return CaloricLevel.FAT;
        };

        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType = menu.stream().collect(
                Collectors.groupingBy(Dish::getType,
                        Collectors.mapping(caloricLevel, Collectors.toSet())
                )
        );

        System.out.println(caloricLevelsByType);
    }

    @Test
    void partitioningTest() {
        // partition by predicate (T/F) -> two groups
        Map<Boolean, List<Dish>> partitionedMenu = menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian));
        System.out.println(partitionedMenu);

        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType = menu.stream().collect(
                Collectors.partitioningBy(Dish::isVegetarian,
                        Collectors.groupingBy(Dish::getType)
                )
        );
        System.out.println(vegetarianDishesByType);

        Map<Boolean, Dish> mostCaloricDishByVegetarian = menu.stream().collect(
                Collectors.partitioningBy(Dish::isVegetarian,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)),
                                Optional::get
                        )
                )
        );
        System.out.println(mostCaloricDishByVegetarian);
    }

    boolean isPrime(int candidate) {
        int candidateRoot = (int) Math.sqrt(candidate);
        return IntStream.rangeClosed(2, candidateRoot).noneMatch(i -> candidate % i == 0);
    }

    @Test
    void partitionPrimeTest() {
        Map<Boolean, List<Integer>> partitionedByPrime = IntStream.rangeClosed(2, 100).boxed().collect(
                Collectors.partitioningBy(this::isPrime)
        );

        System.out.println(partitionedByPrime);
    }

}
