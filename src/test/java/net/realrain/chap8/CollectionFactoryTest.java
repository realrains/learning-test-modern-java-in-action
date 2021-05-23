package net.realrain.chap8;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CollectionFactoryTest {

    @Test
    void asListTest() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4); // fixed size array
        assertThrows(UnsupportedOperationException.class, () -> numbers.add(1));
    }

    @Test
    void listOfTest() {
        List<Integer> numbers = List.of(1, 2, 3, 4); // immutable array
        assertThrows(UnsupportedOperationException.class, () -> numbers.add(1));
    }

    @Test
    void mapFactoryTest() {
        Map<String, Integer> simpleMap = Map.of("A", 1, "B", 2, "C", 3);
        Map<String, Integer> largeMap = Map.ofEntries(
                Map.entry("A", 1),
                Map.entry("B", 2),
                Map.entry("C", 3),
                Map.entry("D", 4),
                Map.entry("E", 5)
        );
    }

    @Test
    void removeIfTest() {
        List<Integer> numbers = Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList());
        numbers.removeIf(n -> n % 2 == 0);
        assertEquals(List.of(1, 3, 5), numbers);
    }

    @Test
    void replaceAllTest() {
        List<Integer> numbers = Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList());
        numbers.replaceAll(n -> n * n);
        assertEquals(List.of(1, 4, 9, 16, 25), numbers);
    }

    @Test
    void mapForEachTest() {
        Map<String, Integer> sampleMap = Map.ofEntries(
                Map.entry("A", 1),
                Map.entry("B", 2),
                Map.entry("C", 3),
                Map.entry("D", 4),
                Map.entry("E", 5)
        );

        for (Map.Entry<String, Integer> entry: sampleMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        sampleMap.forEach((k, v) -> System.out.println(k + " " + v));
    }

    @Test
    void sortMapTest() {
        Map<String, Integer> sampleMap = Map.ofEntries(
                Map.entry("C", 3),
                Map.entry("B", 2),
                Map.entry("D", 4),
                Map.entry("E", 5),
                Map.entry("A", 1)
        );

        sampleMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(System.out::println);
    }

    @Test
    void getOrDefaultTest() {
        Map<String, Integer> sampleMap = Map.ofEntries(
                Map.entry("A", 1),
                Map.entry("B", 2),
                Map.entry("C", 3),
                Map.entry("D", 4),
                Map.entry("E", 5)
        );

        assertEquals(1, sampleMap.getOrDefault("A", 26));
        assertEquals(26, sampleMap.getOrDefault("Z", 26));
    }

    @Test
    void mapComputeTest() {
        List<String> words = List.of("facebook", "apple", "amazon", "netflix", "google");
        Map<String, Integer> wordLengthMap = new HashMap<>();
        wordLengthMap.put("apple", 5);

        words.forEach(word -> wordLengthMap.computeIfAbsent(word, s -> {
            System.out.println("Calculate " + s);
            return s.length();
        }));
    }

    @Test
    void mapPutAllTest() {
        Map<String, Integer> mapA = new HashMap<>(Map.ofEntries(
                Map.entry("A", 1),
                Map.entry("B", 2),
                Map.entry("C", 3)
        ));
        Map<String, Integer> mapB = Map.ofEntries(
                Map.entry("C", 3),
                Map.entry("D", 4),
                Map.entry("F", 5)
        );

        mapA.putAll(mapB);

        System.out.println(mapA);
    }

    @Test
    void mapMergeTest() {
        Map<String, Integer> mapA = new HashMap<>(Map.ofEntries(
                Map.entry("A", 1),
                Map.entry("B", 2),
                Map.entry("C", 3)
        ));
        Map<String, Integer> mapB = Map.ofEntries(
                Map.entry("C", 3),
                Map.entry("D", 4),
                Map.entry("F", 5)
        );

        mapB.forEach((k, v) -> mapA.merge(k, v, (ek, ev) -> ev + v));
        System.out.println(mapA);
        assertEquals(6, mapA.getOrDefault("C", 0));
    }
}
