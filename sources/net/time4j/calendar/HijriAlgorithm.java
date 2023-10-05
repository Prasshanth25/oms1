package net.time4j.calendar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.time4j.base.MathUtils;
import net.time4j.engine.CalendarEra;
import net.time4j.engine.VariantSource;

/* loaded from: classes2.dex */
public enum HijriAlgorithm implements VariantSource {
    EAST_ISLAMIC_CIVIL("islamic-eastc", new int[]{2, 5, 7, 10, 13, 15, 18, 21, 24, 26, 29}, true),
    EAST_ISLAMIC_ASTRO("islamic-easta", new int[]{2, 5, 7, 10, 13, 15, 18, 21, 24, 26, 29}, false),
    WEST_ISLAMIC_CIVIL("islamic-civil", new int[]{2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29}, true),
    WEST_ISLAMIC_ASTRO("islamic-tbla", new int[]{2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29}, false),
    FATIMID_CIVIL("islamic-fatimidc", new int[]{2, 5, 8, 10, 13, 16, 19, 21, 24, 27, 29}, true),
    FATIMID_ASTRO("islamic-fatimida", new int[]{2, 5, 8, 10, 13, 16, 19, 21, 24, 27, 29}, false),
    HABASH_AL_HASIB_CIVIL("islamic-habashalhasibc", new int[]{2, 5, 8, 11, 13, 16, 19, 21, 24, 27, 30}, true),
    HABASH_AL_HASIB_ASTRO("islamic-habashalhasiba", new int[]{2, 5, 8, 11, 13, 16, 19, 21, 24, 27, 30}, false);
    
    private final transient Transformer calsys;
    private static final long LENGTH_OF_30_YEAR_CYCLE = 10631;
    private static final long START_622_07_15 = -492879;
    private static final long START_622_07_16 = (-492879) + 1;
    private static final int MAX_YEAR = 1600;
    private static final long ASTRO_1600_12_29 = 74106;
    private static final long CIVIL_1600_12_29 = 74106 + 1;

    HijriAlgorithm(String str, int[] iArr, boolean z) {
        this.calsys = new Transformer(str, iArr, z, 0);
    }

    @Override // net.time4j.engine.VariantSource
    public String getVariant() {
        return this.calsys.variant;
    }

    public EraYearMonthDaySystem<HijriCalendar> getCalendarSystem(int i) {
        if (i == 0) {
            return this.calsys;
        }
        return new Transformer(HijriAdjustment.of(getVariant(), i).getVariant(), this.calsys.intercalaries, this.calsys.civil, i);
    }

    /* loaded from: classes2.dex */
    public static class Transformer implements EraYearMonthDaySystem<HijriCalendar> {
        private final int adjustment;
        private final boolean civil;
        private final int[] intercalaries;
        private final String variant;

        Transformer(String str, int[] iArr, boolean z, int i) {
            this.variant = str;
            this.intercalaries = iArr;
            this.civil = z;
            this.adjustment = i;
        }

        @Override // net.time4j.calendar.EraYearMonthDaySystem
        public boolean isValid(CalendarEra calendarEra, int i, int i2, int i3) {
            return calendarEra == HijriEra.ANNO_HEGIRAE && i >= 1 && i <= HijriAlgorithm.MAX_YEAR && i2 >= 1 && i2 <= 12 && i3 >= 1 && i3 <= getLengthOfMonth(calendarEra, i, i2);
        }

        @Override // net.time4j.calendar.EraYearMonthDaySystem
        public int getLengthOfMonth(CalendarEra calendarEra, int i, int i2) {
            if (calendarEra == HijriEra.ANNO_HEGIRAE) {
                if (i >= 1 && i <= HijriAlgorithm.MAX_YEAR && i2 >= 1 && i2 <= 12) {
                    if (i2 == 12) {
                        return Arrays.binarySearch(this.intercalaries, ((i - 1) % 30) + 1) >= 0 ? 30 : 29;
                    }
                    return i2 % 2 == 1 ? 30 : 29;
                }
                throw new IllegalArgumentException("Out of bounds: " + i + "/" + i2);
            }
            throw new IllegalArgumentException("Wrong era: " + calendarEra);
        }

        @Override // net.time4j.calendar.EraYearMonthDaySystem
        public int getLengthOfYear(CalendarEra calendarEra, int i) {
            if (calendarEra == HijriEra.ANNO_HEGIRAE) {
                if (i >= 1 && i <= HijriAlgorithm.MAX_YEAR) {
                    return Arrays.binarySearch(this.intercalaries, ((i - 1) % 30) + 1) >= 0 ? 355 : 354;
                }
                throw new IllegalArgumentException("Out of bounds: yearOfEra=" + i);
            }
            throw new IllegalArgumentException("Wrong era: " + calendarEra);
        }

        /* JADX WARN: Code restructure failed: missing block: B:145:0x0083, code lost:
            if (java.util.Arrays.binarySearch(r7.intercalaries, ((r0 - 1) % 30) + 1) >= 0) goto L34;
         */
        /* JADX WARN: Code restructure failed: missing block: B:147:0x0086, code lost:
            r2 = 29;
         */
        /* JADX WARN: Code restructure failed: missing block: B:149:0x008b, code lost:
            if ((r3 % 2) == 1) goto L34;
         */
        /* JADX WARN: Code restructure failed: missing block: B:150:0x008d, code lost:
            if (r9 <= r2) goto L43;
         */
        /* JADX WARN: Code restructure failed: missing block: B:151:0x008f, code lost:
            r3 = r3 + 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:152:0x0091, code lost:
            if (r3 <= 12) goto L42;
         */
        /* JADX WARN: Code restructure failed: missing block: B:153:0x0093, code lost:
            r0 = r0 + 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:154:0x0096, code lost:
            r1 = r3;
         */
        /* JADX WARN: Code restructure failed: missing block: B:155:0x0097, code lost:
            r9 = 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:156:0x0099, code lost:
            r1 = r3;
         */
        /* JADX WARN: Code restructure failed: missing block: B:158:0x00a0, code lost:
            return net.time4j.calendar.HijriCalendar.of(r7.variant, r0, r1, r9);
         */
        @Override // net.time4j.engine.CalendarSystem
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public net.time4j.calendar.HijriCalendar transform(long r8) {
            /*
                r7 = this;
                int r0 = r7.adjustment
                long r0 = (long) r0
                long r0 = net.time4j.base.MathUtils.safeAdd(r8, r0)
                boolean r2 = r7.civil
                if (r2 == 0) goto L10
                long r2 = net.time4j.calendar.HijriAlgorithm.access$400()
                goto L14
            L10:
                long r2 = net.time4j.calendar.HijriAlgorithm.access$500()
            L14:
                int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                if (r4 < 0) goto La1
                boolean r4 = r7.civil
                if (r4 == 0) goto L21
                long r4 = net.time4j.calendar.HijriAlgorithm.access$600()
                goto L25
            L21:
                long r4 = net.time4j.calendar.HijriAlgorithm.access$700()
            L25:
                int r6 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
                if (r6 > 0) goto La1
                long r8 = net.time4j.base.MathUtils.safeSubtract(r0, r2)
                long r0 = net.time4j.calendar.HijriAlgorithm.access$800()
                long r0 = r8 / r0
                r2 = 30
                long r0 = r0 * r2
                int r0 = net.time4j.base.MathUtils.safeCast(r0)
                r1 = 1
                int r0 = r0 + r1
                long r2 = net.time4j.calendar.HijriAlgorithm.access$800()
                long r8 = r8 % r2
                int r9 = (int) r8
                r8 = 1
            L44:
                r2 = 30
                if (r8 >= r2) goto L5d
                int[] r3 = r7.intercalaries
                int r3 = java.util.Arrays.binarySearch(r3, r8)
                if (r3 < 0) goto L53
                r3 = 355(0x163, float:4.97E-43)
                goto L55
            L53:
                r3 = 354(0x162, float:4.96E-43)
            L55:
                if (r9 <= r3) goto L5d
                int r9 = r9 - r3
                int r0 = r0 + 1
                int r8 = r8 + 1
                goto L44
            L5d:
                r8 = 1
                r3 = 1
            L5f:
                r4 = 29
                r5 = 12
                if (r8 >= r5) goto L76
                int r6 = r8 % 2
                if (r6 != 0) goto L6c
                r6 = 29
                goto L6e
            L6c:
                r6 = 30
            L6e:
                if (r9 <= r6) goto L76
                int r9 = r9 - r6
                int r3 = r3 + 1
                int r8 = r8 + 1
                goto L5f
            L76:
                int r9 = r9 + r1
                if (r3 != r5) goto L89
                int r8 = r0 + (-1)
                int r8 = r8 % r2
                int r8 = r8 + r1
                int[] r6 = r7.intercalaries
                int r8 = java.util.Arrays.binarySearch(r6, r8)
                if (r8 < 0) goto L86
                goto L8d
            L86:
                r2 = 29
                goto L8d
            L89:
                int r8 = r3 % 2
                if (r8 != r1) goto L86
            L8d:
                if (r9 <= r2) goto L99
                int r3 = r3 + 1
                if (r3 <= r5) goto L96
                int r0 = r0 + 1
                goto L97
            L96:
                r1 = r3
            L97:
                r9 = 1
                goto L9a
            L99:
                r1 = r3
            L9a:
                java.lang.String r8 = r7.variant
                net.time4j.calendar.HijriCalendar r8 = net.time4j.calendar.HijriCalendar.of(r8, r0, r1, r9)
                return r8
            La1:
                java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = "Out of supported range: "
                r1.<init>(r2)
                r1.append(r8)
                java.lang.String r8 = r1.toString()
                r0.<init>(r8)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: net.time4j.calendar.HijriAlgorithm.Transformer.transform(long):net.time4j.calendar.HijriCalendar");
        }

        @Override // net.time4j.engine.CalendarSystem
        public long transform(HijriCalendar hijriCalendar) {
            int year = hijriCalendar.getYear();
            int value = hijriCalendar.getMonth().getValue();
            int dayOfMonth = hijriCalendar.getDayOfMonth();
            if (year >= 1 && year <= HijriAlgorithm.MAX_YEAR && value >= 1 && value <= 12 && dayOfMonth >= 1 && dayOfMonth <= 30) {
                int i = year - 1;
                long j = (i / 30) * HijriAlgorithm.LENGTH_OF_30_YEAR_CYCLE;
                int i2 = (i % 30) + 1;
                for (int i3 = 1; i3 < i2; i3++) {
                    j += Arrays.binarySearch(this.intercalaries, i3) >= 0 ? 355L : 354L;
                }
                for (int i4 = 1; i4 < value; i4++) {
                    j += i4 % 2 == 0 ? 29L : 30L;
                }
                if (dayOfMonth == 30 && ((value == 12 && Arrays.binarySearch(this.intercalaries, i2) < 0) || (value != 12 && value % 2 == 0))) {
                    throw new IllegalArgumentException("Invalid day-of-month: " + hijriCalendar);
                }
                return MathUtils.safeSubtract(((this.civil ? HijriAlgorithm.START_622_07_16 : HijriAlgorithm.START_622_07_15) + (j + dayOfMonth)) - 1, this.adjustment);
            }
            throw new IllegalArgumentException("Out of supported range: " + hijriCalendar);
        }

        @Override // net.time4j.engine.CalendarSystem
        public long getMinimumSinceUTC() {
            return MathUtils.safeSubtract(this.civil ? HijriAlgorithm.START_622_07_16 : HijriAlgorithm.START_622_07_15, this.adjustment);
        }

        @Override // net.time4j.engine.CalendarSystem
        public long getMaximumSinceUTC() {
            return MathUtils.safeSubtract(this.civil ? HijriAlgorithm.CIVIL_1600_12_29 : HijriAlgorithm.ASTRO_1600_12_29, this.adjustment);
        }

        @Override // net.time4j.engine.CalendarSystem
        public List<CalendarEra> getEras() {
            return Collections.singletonList(HijriEra.ANNO_HEGIRAE);
        }
    }
}
