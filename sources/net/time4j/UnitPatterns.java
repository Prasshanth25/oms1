package net.time4j;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.time4j.base.ResourceLoader;
import net.time4j.format.PluralCategory;
import net.time4j.format.RelativeTimeProvider;
import net.time4j.format.TextWidth;
import net.time4j.format.UnitPatternProvider;

/* loaded from: classes2.dex */
public final class UnitPatterns {
    private static final UnitPatternProvider FALLBACK;
    private static final int MAX_LIST_INDEX = 7;
    private static final int MIN_LIST_INDEX = 2;
    private static final UnitPatternProvider PROVIDER;
    private final Map<IsoUnit, Map<PluralCategory, String>> future;
    private final Map<Weekday, String> lastWeekdays;
    private final Map<Integer, Map<TextWidth, String>> list;
    private final Locale locale;
    private final Map<Weekday, String> nextWeekdays;
    private final String now;
    private final Map<IsoUnit, Map<PluralCategory, String>> past;
    private final Map<IsoUnit, Map<TextWidth, Map<PluralCategory, String>>> patterns;
    private final Map<IsoUnit, Map<PluralCategory, String>> shortFuture;
    private final Map<IsoUnit, Map<PluralCategory, String>> shortPast;
    private final String today;
    private final String tomorrow;
    private final String yesterday;
    private static final ConcurrentMap<Locale, UnitPatterns> CACHE = new ConcurrentHashMap();
    private static final IsoUnit[] UNIT_IDS = {CalendarUnit.YEARS, CalendarUnit.MONTHS, CalendarUnit.WEEKS, CalendarUnit.DAYS, ClockUnit.HOURS, ClockUnit.MINUTES, ClockUnit.SECONDS, ClockUnit.MILLIS, ClockUnit.MICROS, ClockUnit.NANOS};

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v13, types: [net.time4j.format.UnitPatternProvider] */
    static {
        FallbackProvider fallbackProvider = new FallbackProvider(null);
        FALLBACK = fallbackProvider;
        Iterator it = ResourceLoader.getInstance().services(UnitPatternProvider.class).iterator();
        FallbackProvider fallbackProvider2 = it.hasNext() ? (UnitPatternProvider) it.next() : null;
        if (fallbackProvider2 != null) {
            fallbackProvider = fallbackProvider2;
        }
        PROVIDER = fallbackProvider;
    }

    private UnitPatterns(Locale locale) {
        String str;
        String str2;
        String str3;
        String str4;
        Weekday[] values;
        TextWidth[] values2;
        PluralCategory[] values3;
        PluralCategory[] values4;
        PluralCategory[] values5;
        this.locale = locale;
        HashMap hashMap = new HashMap(10);
        HashMap hashMap2 = new HashMap(10);
        HashMap hashMap3 = new HashMap(10);
        HashMap hashMap4 = new HashMap(10);
        HashMap hashMap5 = new HashMap(10);
        HashMap hashMap6 = new HashMap(10);
        IsoUnit[] isoUnitArr = UNIT_IDS;
        int length = isoUnitArr.length;
        int i = 0;
        while (i < length) {
            IsoUnit isoUnit = isoUnitArr[i];
            EnumMap enumMap = new EnumMap(TextWidth.class);
            TextWidth[] values6 = TextWidth.values();
            int length2 = values6.length;
            int i2 = 0;
            while (i2 < length2) {
                IsoUnit[] isoUnitArr2 = isoUnitArr;
                TextWidth textWidth = values6[i2];
                int i3 = length;
                TextWidth[] textWidthArr = values6;
                EnumMap enumMap2 = new EnumMap(PluralCategory.class);
                PluralCategory[] values7 = PluralCategory.values();
                int i4 = length2;
                int length3 = values7.length;
                int i5 = 0;
                while (i5 < length3) {
                    int i6 = length3;
                    PluralCategory pluralCategory = values7[i5];
                    enumMap2.put((EnumMap) pluralCategory, (PluralCategory) lookup(locale, isoUnit, textWidth, pluralCategory));
                    i5++;
                    length3 = i6;
                    values7 = values7;
                }
                enumMap.put((EnumMap) textWidth, (TextWidth) Collections.unmodifiableMap(enumMap2));
                i2++;
                isoUnitArr = isoUnitArr2;
                length = i3;
                values6 = textWidthArr;
                length2 = i4;
            }
            IsoUnit[] isoUnitArr3 = isoUnitArr;
            int i7 = length;
            hashMap.put(isoUnit, Collections.unmodifiableMap(enumMap));
            if (!Character.isDigit(isoUnit.getSymbol())) {
                EnumMap enumMap3 = new EnumMap(PluralCategory.class);
                for (PluralCategory pluralCategory2 : PluralCategory.values()) {
                    enumMap3.put((EnumMap) pluralCategory2, (PluralCategory) lookup(locale, isoUnit, false, false, pluralCategory2));
                }
                hashMap2.put(isoUnit, Collections.unmodifiableMap(enumMap3));
                EnumMap enumMap4 = new EnumMap(PluralCategory.class);
                for (PluralCategory pluralCategory3 : PluralCategory.values()) {
                    enumMap4.put((EnumMap) pluralCategory3, (PluralCategory) lookup(locale, isoUnit, false, true, pluralCategory3));
                }
                hashMap4.put(isoUnit, Collections.unmodifiableMap(enumMap4));
                EnumMap enumMap5 = new EnumMap(PluralCategory.class);
                PluralCategory[] values8 = PluralCategory.values();
                int length4 = values8.length;
                int i8 = 0;
                while (i8 < length4) {
                    PluralCategory pluralCategory4 = values8[i8];
                    enumMap5.put((EnumMap) pluralCategory4, (PluralCategory) lookup(locale, isoUnit, true, false, pluralCategory4));
                    i8++;
                    values8 = values8;
                }
                hashMap3.put(isoUnit, Collections.unmodifiableMap(enumMap5));
                EnumMap enumMap6 = new EnumMap(PluralCategory.class);
                for (PluralCategory pluralCategory5 : PluralCategory.values()) {
                    enumMap6.put((EnumMap) pluralCategory5, (PluralCategory) lookup(locale, isoUnit, true, true, pluralCategory5));
                }
                hashMap5.put(isoUnit, Collections.unmodifiableMap(enumMap6));
            }
            i++;
            isoUnitArr = isoUnitArr3;
            length = i7;
        }
        for (int i9 = 2; i9 <= 7; i9++) {
            Integer valueOf = Integer.valueOf(i9);
            EnumMap enumMap7 = new EnumMap(TextWidth.class);
            for (TextWidth textWidth2 : TextWidth.values()) {
                enumMap7.put((EnumMap) textWidth2, (TextWidth) lookup(locale, textWidth2, valueOf.intValue()));
            }
            hashMap6.put(valueOf, Collections.unmodifiableMap(enumMap7));
        }
        this.patterns = Collections.unmodifiableMap(hashMap);
        this.past = Collections.unmodifiableMap(hashMap2);
        this.future = Collections.unmodifiableMap(hashMap3);
        this.shortPast = Collections.unmodifiableMap(hashMap4);
        this.shortFuture = Collections.unmodifiableMap(hashMap5);
        this.list = Collections.unmodifiableMap(hashMap6);
        EnumMap enumMap8 = new EnumMap(Weekday.class);
        EnumMap enumMap9 = new EnumMap(Weekday.class);
        Weekday[] values9 = Weekday.values();
        int length5 = values9.length;
        int i10 = 0;
        while (true) {
            str = "";
            if (i10 < length5) {
                Weekday weekday = values9[i10];
                enumMap8.put((EnumMap) weekday, (Weekday) "");
                enumMap9.put((EnumMap) weekday, (Weekday) "");
                i10++;
            } else {
                try {
                    break;
                } catch (MissingResourceException unused) {
                    str2 = "";
                    str3 = str2;
                }
            }
        }
        UnitPatternProvider unitPatternProvider = PROVIDER;
        String nowWord = unitPatternProvider.getNowWord(locale);
        if (unitPatternProvider instanceof RelativeTimeProvider) {
            RelativeTimeProvider relativeTimeProvider = (RelativeTimeProvider) RelativeTimeProvider.class.cast(unitPatternProvider);
            String yesterdayWord = relativeTimeProvider.getYesterdayWord(locale);
            try {
                str3 = relativeTimeProvider.getTodayWord(locale);
                try {
                    str = relativeTimeProvider.getTomorrowWord(locale);
                    for (Weekday weekday2 : Weekday.values()) {
                        enumMap8.put((EnumMap) weekday2, (Weekday) relativeTimeProvider.labelForLast(weekday2, locale));
                        enumMap9.put((EnumMap) weekday2, (Weekday) relativeTimeProvider.labelForNext(weekday2, locale));
                    }
                    str4 = str;
                    str = yesterdayWord;
                } catch (MissingResourceException unused2) {
                    str2 = str;
                    str = yesterdayWord;
                    nowWord = FALLBACK.getNowWord(locale);
                    str4 = str2;
                    this.now = nowWord;
                    this.yesterday = str;
                    this.today = str3;
                    this.tomorrow = str4;
                    this.lastWeekdays = Collections.unmodifiableMap(enumMap8);
                    this.nextWeekdays = Collections.unmodifiableMap(enumMap9);
                }
            } catch (MissingResourceException unused3) {
                str2 = "";
                str3 = str2;
            }
        } else {
            str4 = "";
            str3 = str4;
        }
        this.now = nowWord;
        this.yesterday = str;
        this.today = str3;
        this.tomorrow = str4;
        this.lastWeekdays = Collections.unmodifiableMap(enumMap8);
        this.nextWeekdays = Collections.unmodifiableMap(enumMap9);
    }

    public static UnitPatterns of(Locale locale) {
        if (locale == null) {
            throw new NullPointerException("Missing language.");
        }
        ConcurrentMap<Locale, UnitPatterns> concurrentMap = CACHE;
        UnitPatterns unitPatterns = concurrentMap.get(locale);
        if (unitPatterns == null) {
            UnitPatterns unitPatterns2 = new UnitPatterns(locale);
            UnitPatterns putIfAbsent = concurrentMap.putIfAbsent(locale, unitPatterns2);
            return putIfAbsent != null ? putIfAbsent : unitPatterns2;
        }
        return unitPatterns;
    }

    public String getPattern(TextWidth textWidth, PluralCategory pluralCategory, IsoUnit isoUnit) {
        checkNull(textWidth, pluralCategory);
        return this.patterns.get(isoUnit).get(textWidth).get(pluralCategory);
    }

    public String getPatternInPast(PluralCategory pluralCategory, boolean z, IsoUnit isoUnit) {
        checkNull(pluralCategory);
        if (z) {
            return this.shortPast.get(isoUnit).get(pluralCategory);
        }
        return this.past.get(isoUnit).get(pluralCategory);
    }

    public String getPatternInFuture(PluralCategory pluralCategory, boolean z, IsoUnit isoUnit) {
        checkNull(pluralCategory);
        if (z) {
            return this.shortFuture.get(isoUnit).get(pluralCategory);
        }
        return this.future.get(isoUnit).get(pluralCategory);
    }

    public String getNowWord() {
        return this.now;
    }

    public String getYesterdayWord() {
        return this.yesterday;
    }

    public String getTodayWord() {
        return this.today;
    }

    public String getTomorrowWord() {
        return this.tomorrow;
    }

    public String labelForLast(Weekday weekday) {
        return this.lastWeekdays.get(weekday);
    }

    public String labelForNext(Weekday weekday) {
        return this.nextWeekdays.get(weekday);
    }

    public String getListPattern(TextWidth textWidth, int i) {
        if (textWidth != null) {
            if (i >= 2 && i <= 7) {
                return this.list.get(Integer.valueOf(i)).get(textWidth);
            }
            return lookup(this.locale, textWidth, i);
        }
        throw new NullPointerException("Missing width.");
    }

    private static void checkNull(PluralCategory pluralCategory) {
        if (pluralCategory == null) {
            throw new NullPointerException("Missing plural category.");
        }
    }

    private static void checkNull(TextWidth textWidth, PluralCategory pluralCategory) {
        if (textWidth == null) {
            throw new NullPointerException("Missing text width.");
        }
        checkNull(pluralCategory);
    }

    private static char getID(IsoUnit isoUnit) {
        char symbol = isoUnit.getSymbol();
        if (isoUnit == ClockUnit.MINUTES) {
            return 'N';
        }
        return symbol;
    }

    private static String lookup(Locale locale, IsoUnit isoUnit, TextWidth textWidth, PluralCategory pluralCategory) {
        try {
            return lookup(PROVIDER, locale, getID(isoUnit), textWidth, pluralCategory);
        } catch (MissingResourceException unused) {
            return lookup(FALLBACK, locale, getID(isoUnit), textWidth, pluralCategory);
        }
    }

    private static String lookup(UnitPatternProvider unitPatternProvider, Locale locale, char c, TextWidth textWidth, PluralCategory pluralCategory) {
        if (c != '3') {
            if (c != '6') {
                if (c != '9') {
                    if (c != 'D') {
                        if (c != 'H') {
                            if (c != 'S') {
                                if (c != 'W') {
                                    if (c != 'Y') {
                                        if (c != 'M') {
                                            if (c == 'N') {
                                                return unitPatternProvider.getMinutePattern(locale, textWidth, pluralCategory);
                                            }
                                            throw new UnsupportedOperationException("Unit-ID: " + c);
                                        }
                                        return unitPatternProvider.getMonthPattern(locale, textWidth, pluralCategory);
                                    }
                                    return unitPatternProvider.getYearPattern(locale, textWidth, pluralCategory);
                                }
                                return unitPatternProvider.getWeekPattern(locale, textWidth, pluralCategory);
                            }
                            return unitPatternProvider.getSecondPattern(locale, textWidth, pluralCategory);
                        }
                        return unitPatternProvider.getHourPattern(locale, textWidth, pluralCategory);
                    }
                    return unitPatternProvider.getDayPattern(locale, textWidth, pluralCategory);
                }
                return unitPatternProvider.getNanoPattern(locale, textWidth, pluralCategory);
            }
            return unitPatternProvider.getMicroPattern(locale, textWidth, pluralCategory);
        }
        return unitPatternProvider.getMilliPattern(locale, textWidth, pluralCategory);
    }

    private static String lookup(Locale locale, IsoUnit isoUnit, boolean z, boolean z2, PluralCategory pluralCategory) {
        try {
            return lookup(PROVIDER, locale, getID(isoUnit), z, z2, pluralCategory);
        } catch (MissingResourceException unused) {
            return lookup(FALLBACK, locale, getID(isoUnit), z, z2, pluralCategory);
        }
    }

    private static String lookup(UnitPatternProvider unitPatternProvider, Locale locale, char c, boolean z, boolean z2, PluralCategory pluralCategory) {
        if (!z2 || !(unitPatternProvider instanceof RelativeTimeProvider)) {
            if (c != 'D') {
                if (c != 'H') {
                    if (c != 'S') {
                        if (c != 'W') {
                            if (c != 'Y') {
                                if (c != 'M') {
                                    if (c == 'N') {
                                        return unitPatternProvider.getMinutePattern(locale, z, pluralCategory);
                                    }
                                    throw new UnsupportedOperationException("Unit-ID: " + c);
                                }
                                return unitPatternProvider.getMonthPattern(locale, z, pluralCategory);
                            }
                            return unitPatternProvider.getYearPattern(locale, z, pluralCategory);
                        }
                        return unitPatternProvider.getWeekPattern(locale, z, pluralCategory);
                    }
                    return unitPatternProvider.getSecondPattern(locale, z, pluralCategory);
                }
                return unitPatternProvider.getHourPattern(locale, z, pluralCategory);
            }
            return unitPatternProvider.getDayPattern(locale, z, pluralCategory);
        }
        RelativeTimeProvider relativeTimeProvider = (RelativeTimeProvider) RelativeTimeProvider.class.cast(unitPatternProvider);
        if (c != 'D') {
            if (c != 'H') {
                if (c != 'S') {
                    if (c != 'W') {
                        if (c != 'Y') {
                            if (c != 'M') {
                                if (c == 'N') {
                                    return relativeTimeProvider.getShortMinutePattern(locale, z, pluralCategory);
                                }
                                throw new UnsupportedOperationException("Unit-ID: " + c);
                            }
                            return relativeTimeProvider.getShortMonthPattern(locale, z, pluralCategory);
                        }
                        return relativeTimeProvider.getShortYearPattern(locale, z, pluralCategory);
                    }
                    return relativeTimeProvider.getShortWeekPattern(locale, z, pluralCategory);
                }
                return relativeTimeProvider.getShortSecondPattern(locale, z, pluralCategory);
            }
            return relativeTimeProvider.getShortHourPattern(locale, z, pluralCategory);
        }
        return relativeTimeProvider.getShortDayPattern(locale, z, pluralCategory);
    }

    private static String lookup(Locale locale, TextWidth textWidth, int i) {
        try {
            return PROVIDER.getListPattern(locale, textWidth, i);
        } catch (MissingResourceException unused) {
            return FALLBACK.getListPattern(locale, textWidth, i);
        }
    }

    /* loaded from: classes2.dex */
    private static class FallbackProvider implements UnitPatternProvider {
        @Override // net.time4j.format.UnitPatternProvider
        public String getNowWord(Locale locale) {
            return "now";
        }

        private FallbackProvider() {
        }

        /* synthetic */ FallbackProvider(AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getYearPattern(Locale locale, TextWidth textWidth, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getEnglishPattern("year", "yr", "y", textWidth, pluralCategory);
            }
            return getUnitPattern("y");
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getMonthPattern(Locale locale, TextWidth textWidth, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getEnglishPattern("month", "mth", "m", textWidth, pluralCategory);
            }
            return getUnitPattern("m");
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getWeekPattern(Locale locale, TextWidth textWidth, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getEnglishPattern("week", "wk", "w", textWidth, pluralCategory);
            }
            return getUnitPattern("w");
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getDayPattern(Locale locale, TextWidth textWidth, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getEnglishPattern("day", "day", "d", textWidth, pluralCategory);
            }
            return getUnitPattern("d");
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getHourPattern(Locale locale, TextWidth textWidth, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getEnglishPattern("hour", "hr", "h", textWidth, pluralCategory);
            }
            return getUnitPattern("h");
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getMinutePattern(Locale locale, TextWidth textWidth, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getEnglishPattern("minute", "min", "m", textWidth, pluralCategory);
            }
            return getUnitPattern("min");
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getSecondPattern(Locale locale, TextWidth textWidth, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getEnglishPattern("second", "sec", "s", textWidth, pluralCategory);
            }
            return getUnitPattern("s");
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getYearPattern(Locale locale, boolean z, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getRelativeEnglishPattern("year", z, pluralCategory);
            }
            return getRelativePattern("y", z);
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getMonthPattern(Locale locale, boolean z, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getRelativeEnglishPattern("month", z, pluralCategory);
            }
            return getRelativePattern("m", z);
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getWeekPattern(Locale locale, boolean z, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getRelativeEnglishPattern("week", z, pluralCategory);
            }
            return getRelativePattern("w", z);
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getDayPattern(Locale locale, boolean z, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getRelativeEnglishPattern("day", z, pluralCategory);
            }
            return getRelativePattern("d", z);
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getHourPattern(Locale locale, boolean z, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getRelativeEnglishPattern("hour", z, pluralCategory);
            }
            return getRelativePattern("h", z);
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getMinutePattern(Locale locale, boolean z, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getRelativeEnglishPattern("minute", z, pluralCategory);
            }
            return getRelativePattern("min", z);
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getSecondPattern(Locale locale, boolean z, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getRelativeEnglishPattern("second", z, pluralCategory);
            }
            return getRelativePattern("s", z);
        }

        private static String getEnglishPattern(String str, String str2, String str3, TextWidth textWidth, PluralCategory pluralCategory) {
            int i = AnonymousClass1.$SwitchMap$net$time4j$format$TextWidth[textWidth.ordinal()];
            if (i != 1) {
                if (i == 2 || i == 3) {
                    return getPluralPattern(str2, pluralCategory);
                }
                if (i == 4) {
                    return "{0}" + str3;
                }
                throw new UnsupportedOperationException(textWidth.name());
            }
            return getPluralPattern(str, pluralCategory);
        }

        private static String getPluralPattern(String str, PluralCategory pluralCategory) {
            String str2 = pluralCategory == PluralCategory.ONE ? "" : "s";
            return "{0} " + str + str2;
        }

        private static String getUnitPattern(String str) {
            return "{0} " + str;
        }

        private static String getRelativeEnglishPattern(String str, boolean z, PluralCategory pluralCategory) {
            String str2 = pluralCategory == PluralCategory.ONE ? "" : "s";
            if (z) {
                return "in {0} " + str + str2;
            }
            return "{0} " + str + str2 + " ago";
        }

        private static String getRelativePattern(String str, boolean z) {
            StringBuilder sb = new StringBuilder();
            sb.append(z ? "+" : "-");
            sb.append("{0} ");
            sb.append(str);
            return sb.toString();
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getListPattern(Locale locale, TextWidth textWidth, int i) {
            if (i < 2) {
                throw new IllegalArgumentException("Size must be greater than 1.");
            }
            StringBuilder sb = new StringBuilder(i * 5);
            for (int i2 = 0; i2 < i; i2++) {
                sb.append('{');
                sb.append(i2);
                sb.append('}');
                if (i2 < i - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getMilliPattern(Locale locale, TextWidth textWidth, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getEnglishPattern("millisecond", "msec", "ms", textWidth, pluralCategory);
            }
            return getUnitPattern("ms");
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getMicroPattern(Locale locale, TextWidth textWidth, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getEnglishPattern("microsecond", "µsec", "µs", textWidth, pluralCategory);
            }
            return getUnitPattern("µs");
        }

        @Override // net.time4j.format.UnitPatternProvider
        public String getNanoPattern(Locale locale, TextWidth textWidth, PluralCategory pluralCategory) {
            if (locale.getLanguage().equals("en")) {
                return getEnglishPattern("nanosecond", "nsec", "ns", textWidth, pluralCategory);
            }
            return getUnitPattern("ns");
        }
    }

    /* renamed from: net.time4j.UnitPatterns$1 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$time4j$format$TextWidth;

        static {
            int[] iArr = new int[TextWidth.values().length];
            $SwitchMap$net$time4j$format$TextWidth = iArr;
            try {
                iArr[TextWidth.WIDE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$net$time4j$format$TextWidth[TextWidth.ABBREVIATED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$net$time4j$format$TextWidth[TextWidth.SHORT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$net$time4j$format$TextWidth[TextWidth.NARROW.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }
}
