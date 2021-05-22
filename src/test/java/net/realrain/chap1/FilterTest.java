package net.realrain.chap1;

import com.google.common.collect.Ordering;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilterTest {

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
    public void oldStyleFilteringTest() {
        assertEquals(2, Filter.filterGreenApples(apples).size());;
        assertEquals(2, Filter.filterHeavyApples(apples).size());;
    }

    @Test
    public void predicateFilteringTest() {
        assertEquals(2, Filter.filterApples(apples, Filter::isGreenApple).size());
        assertEquals(2, Filter.filterApples(apples, Filter::isHeavyApple).size());
    }

    @Test
    public void lambdaFilteringTest() {
        assertEquals(2, Filter.filterApples(apples, apple -> Color.GREEN.equals(apple.getColor())).size());
        assertEquals(2, Filter.filterApples(apples, apple -> apple.getWeight() > 150).size());
    }

    @Test
    public void streamFilteringTest() {
        assertEquals(2, apples.stream().filter(apple -> Color.GREEN.equals(apple.getColor())).count());
        assertEquals(2, apples.stream().filter(apple -> apple.getWeight() > 150).count());
    }

    @Test
    public void parallelStreamFilteringTest() {
        assertEquals(2, apples.parallelStream().filter(apple -> Color.GREEN.equals(apple.getColor())).count());
        assertEquals(2, apples.parallelStream().filter(apple -> apple.getWeight() > 150).count());
    }

    @Test
    public void defaultSortOfListTest() {
        // List<T>.sort 는 자바 8 에 추가되었다.
        // 그 이전에는 Arrays.sort 를 사용하였는데 이는 기존의 List 인터페이스를 확장하기 어려웠기 때문이다
        // 왜나하면 default 메서드가 없던 시절에는 인터페이스를 수정할 경우,
        // 해당 인터페이스를 구현하는 모든 클래스가 영향을 받아 새로운 메서드를 구현하여야 했기 때문이다.

        apples.sort(Comparator.comparingInt(Apple::getWeight)); // 내부적으로 Arrays.sort 를 호출함

        // GUAVA
        // https://www.baeldung.com/guava-ordering
        assertTrue(Ordering.from(Comparator.comparingInt(Apple::getWeight)).isOrdered(apples));
    }
}
