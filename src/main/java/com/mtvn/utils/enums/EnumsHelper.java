package com.mtvn.utils.enums;

public class EnumsHelper {

    public static <T extends ValueEnum<P>, P> T fromValue(T[] values, P value) {
        for (T valueEnum : values) {
            if (valueEnum.getValue().equals(value)) {
                return valueEnum;
            }
        }
        return null;
    }
}