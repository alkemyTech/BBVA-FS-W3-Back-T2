package com.bbva.wallet.utils;

public class RandomUtils {
    public static double getRandom(double min, double max) {
        double random = min + Math.random() * (max - min + 1);
        return Math.floor(random);
    }
}
