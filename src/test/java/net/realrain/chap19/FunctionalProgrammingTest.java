package net.realrain.chap19;

import net.realrain.chap1.Apple;
import net.realrain.chap1.Color;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class FunctionalProgrammingTest {

    @Test
    void highOrderFunction() {
        // High Order function
        // - 하나 이상의 함수를 인수로 받음
        // - 함수를 결과로 반환
        Comparator<Apple> weightCompare = Comparator.comparing(Apple::getWeight);
        int result = weightCompare.compare(new Apple(Color.GREEN, 100), new Apple(Color.RED, 50));
        assertEquals(1, result);
    }

    double convertor(double x, double a, double b) {
        return a * x + b;
    }

    DoubleUnaryOperator curriedConvertor(double a, double b) {
        return x -> convertor(x, a, b);
    }

    @Test
    void curring() {
        // f to g
        // f(x, y) = (g(x))(y)
        DoubleUnaryOperator convertor = curriedConvertor(2, 0.5);
        assertEquals(20.5, convertor.applyAsDouble(10));
    }


}
