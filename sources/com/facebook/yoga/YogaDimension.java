package com.facebook.yoga;

/* loaded from: classes.dex */
public enum YogaDimension {
    WIDTH(0),
    HEIGHT(1);
    
    private final int mIntValue;

    YogaDimension(int i) {
        this.mIntValue = i;
    }

    public int intValue() {
        return this.mIntValue;
    }

    public static YogaDimension fromInt(int i) {
        if (i != 0) {
            if (i == 1) {
                return HEIGHT;
            }
            throw new IllegalArgumentException("Unknown enum value: " + i);
        }
        return WIDTH;
    }
}
