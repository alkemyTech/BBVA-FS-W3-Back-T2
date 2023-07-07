package com.bbva.wallet.utils;

import java.util.Random;

public class CbuUtil {
    public static String generateCbu() {
        StringBuilder number = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < 22; i++) {
            int digit = random.nextInt(10);
            number.append(digit);
        }

        return number.toString();
    }
}
