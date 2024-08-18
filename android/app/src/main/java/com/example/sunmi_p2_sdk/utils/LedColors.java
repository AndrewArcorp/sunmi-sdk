package com.example.sunmi_p2_sdk.utils;

public enum LedColors {
    RED_LIGHT (1),
    GREEN_LIGHT(2),
    YELLOW_LIGHT(3),
    BLUE_LIGHT(4);

    private final int value;

    LedColors(final int newValue) {
        value = newValue;
    }
    public int getValue() { return value; }
}
