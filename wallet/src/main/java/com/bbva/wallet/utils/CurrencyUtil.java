package com.bbva.wallet.utils;

import com.bbva.wallet.enums.Currency;

public class CurrencyUtil {
    public static Currency getRandomCurrency() {
        return (Math.random() < 0.5) ? Currency.ARS : Currency.USD;
    }

}
