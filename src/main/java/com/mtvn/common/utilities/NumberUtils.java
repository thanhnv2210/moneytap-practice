package com.mtvn.common.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class NumberUtils {

    public static BigDecimal number_20k = new BigDecimal(20000);
    public static BigDecimal number_25k = new BigDecimal(25000);
    /**
     * Returns whether a >= b
     * @param a
     * @param b
     * @return
     */
    public static boolean gte(BigDecimal a, BigDecimal b) {
        if(a == null || b == null)
            return false;
        return a.compareTo(b) >= 0;
    }

    public static boolean gt(BigDecimal a, BigDecimal b) {
        if(a == null || b == null)
            return false;
        return a.compareTo(b) > 0;
    }

    public static boolean lte(BigDecimal a, BigDecimal b) {
        if(a == null || b == null)
            return false;
        return a.compareTo(b) <= 0;
    }

    public static boolean lt(BigDecimal a, BigDecimal b) {
        if(a == null || b == null)
            return false;
        return a.compareTo(b) < 0;
    }

    public static boolean eq(BigDecimal a, BigDecimal b) {
        if(a == null || b == null)
            return false;
        return a.compareTo(b) == 0;
    }

    public static BigDecimal roundCeiling(BigDecimal a){
        return round(a, RoundingMode.CEILING);
    }

    public static BigDecimal roundHalfEven(BigDecimal a){
        return round(a, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal round(BigDecimal a, RoundingMode roundingMode){
        return round(a, roundingMode, 0);
    }

    public static BigDecimal roundHalfEvenWith2Decimals(BigDecimal a) {
        return round(a, RoundingMode.HALF_EVEN, 2);
    }

    public static BigDecimal roundHalfEvenWithDecimals(BigDecimal a, int scale){
        return round(a, RoundingMode.HALF_EVEN, scale);
    }

    public static BigDecimal roundDown(BigDecimal a){
        return round(a, RoundingMode.DOWN);
    }

    public static BigDecimal round(BigDecimal a, RoundingMode roundingMode, int scale){
        if(a == null)
            return null;
        return a.setScale(scale, roundingMode);
    }

    public static int getMin(Integer... ints) {
        Integer min = null;
        for(Integer i : ints) {
            if(min == null || min > i)
                min = i;
        }
        return min;
    }

    public static String generateRandomInteger(Integer length) {
        if (length < 1) {
            return null;
        }
        Integer limit = (int) Math.pow(10, length - 1);
        Random random = new Random();
        return String.valueOf(limit + random.nextInt(9 * limit));
    }

    public static int toInt(String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
    }

    public static <T extends Number> T getMax(T... numbers) {
        if(numbers == null || numbers.length == 0)
            return null;

        T max = null;
        for(int i = 0 ; i < numbers.length ; i++) {
            if(max == null)
                max = numbers[i];
            else
                max = max.doubleValue() > numbers[i].doubleValue() ? max : numbers[i];
        }
        return max;
    }

    public static <T extends Number> T getMin(T... numbers) {
        if(numbers == null || numbers.length == 0)
            return null;
        T min = null;
        for(int i = 0 ; i < numbers.length ; i++) {
            if(numbers[i] == null)
                continue;
            if(min == null)
                min = numbers[i];
            else
                min = min.doubleValue() < numbers[i].doubleValue() ? min : numbers[i];
        }
        return min;
    }

}
