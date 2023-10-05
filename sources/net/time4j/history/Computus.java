package net.time4j.history;

import net.time4j.PlainDate;
import net.time4j.engine.EpochDays;

/* loaded from: classes2.dex */
public enum Computus {
    WESTERN,
    EASTERN;

    public PlainDate easterSunday(int i) {
        int i2;
        int marchDay = marchDay(i);
        if (marchDay > 31) {
            marchDay -= 31;
            i2 = 4;
        } else {
            i2 = 3;
        }
        if (this == WESTERN && i > 1582) {
            return PlainDate.of(i, i2, marchDay);
        }
        return PlainDate.of(JulianMath.toMJD(i, i2, marchDay), EpochDays.MODIFIED_JULIAN_DATE);
    }

    public int marchDay(int i) {
        int i2;
        int i3;
        if (i < 532) {
            throw new IllegalArgumentException("Out of range: " + i);
        }
        int i4 = i / 100;
        if (this != WESTERN || i <= 1582) {
            i2 = 15;
            i3 = 0;
        } else {
            int i5 = ((i4 * 3) + 3) / 4;
            i2 = (i5 + 15) - (((i4 * 8) + 13) / 25);
            i3 = 2 - i5;
        }
        int i6 = i % 19;
        int i7 = ((i6 * 19) + i2) % 30;
        int i8 = i7 / 29;
        int i9 = (i7 + 21) - (i8 + (((i7 / 28) - i8) * (i6 / 11)));
        return i9 + (7 - ((i9 - (7 - (((i + (i / 4)) + i3) % 7))) % 7));
    }
}
