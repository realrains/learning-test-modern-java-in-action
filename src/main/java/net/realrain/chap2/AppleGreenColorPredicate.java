package net.realrain.chap2;

import net.realrain.chap1.Apple;
import net.realrain.chap1.Color;

public class AppleGreenColorPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return Color.GREEN.equals(apple.getColor());
    }
}
