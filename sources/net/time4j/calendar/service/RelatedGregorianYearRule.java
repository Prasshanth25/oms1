package net.time4j.calendar.service;

import java.util.Collections;
import java.util.Map;
import net.time4j.base.MathUtils;
import net.time4j.engine.CalendarSystem;
import net.time4j.engine.CalendarVariant;
import net.time4j.engine.ChronoElement;
import net.time4j.engine.ChronoEntity;
import net.time4j.engine.ElementRule;
import net.time4j.engine.EpochDays;

/* loaded from: classes2.dex */
public final class RelatedGregorianYearRule<T extends ChronoEntity<T>> implements ElementRule<T, Integer> {
    private static final String KEY_CALENDRICAL = "calendrical";
    private final ChronoElement<Integer> dayOfYear;
    private final Map<String, ? extends CalendarSystem<T>> map;

    public ChronoElement<?> getChildAtCeiling(T t) {
        return null;
    }

    public ChronoElement<?> getChildAtFloor(T t) {
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ ChronoElement getChildAtCeiling(Object obj) {
        return getChildAtCeiling((RelatedGregorianYearRule<T>) ((ChronoEntity) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ ChronoElement getChildAtFloor(Object obj) {
        return getChildAtFloor((RelatedGregorianYearRule<T>) ((ChronoEntity) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ Integer getMaximum(Object obj) {
        return getMaximum((RelatedGregorianYearRule<T>) ((ChronoEntity) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ Integer getMinimum(Object obj) {
        return getMinimum((RelatedGregorianYearRule<T>) ((ChronoEntity) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ Integer getValue(Object obj) {
        return getValue((RelatedGregorianYearRule<T>) ((ChronoEntity) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ boolean isValid(Object obj, Integer num) {
        return isValid((RelatedGregorianYearRule<T>) ((ChronoEntity) obj), num);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ Object withValue(Object obj, Integer num, boolean z) {
        return withValue((RelatedGregorianYearRule<T>) ((ChronoEntity) obj), num, z);
    }

    public RelatedGregorianYearRule(CalendarSystem<T> calendarSystem, ChronoElement<Integer> chronoElement) {
        this.map = Collections.singletonMap(KEY_CALENDRICAL, calendarSystem);
        this.dayOfYear = chronoElement;
    }

    public RelatedGregorianYearRule(Map<String, ? extends CalendarSystem<T>> map, ChronoElement<Integer> chronoElement) {
        this.map = map;
        this.dayOfYear = chronoElement;
    }

    public Integer getValue(T t) {
        return toGregorianYear(getCalendarSystem(t).transform((CalendarSystem<T>) ((T) t.with(this.dayOfYear, 1))));
    }

    public Integer getMinimum(T t) {
        CalendarSystem<T> calendarSystem = getCalendarSystem(t);
        return toGregorianYear(calendarSystem.transform((CalendarSystem<T>) ((T) calendarSystem.transform(calendarSystem.getMinimumSinceUTC()).with(this.dayOfYear, 1))));
    }

    public Integer getMaximum(T t) {
        CalendarSystem<T> calendarSystem = getCalendarSystem(t);
        return toGregorianYear(calendarSystem.transform((CalendarSystem<T>) ((T) calendarSystem.transform(calendarSystem.getMaximumSinceUTC()).with(this.dayOfYear, 1))));
    }

    public boolean isValid(T t, Integer num) {
        return getValue((RelatedGregorianYearRule<T>) t).equals(num);
    }

    public T withValue(T t, Integer num, boolean z) {
        if (isValid((RelatedGregorianYearRule<T>) t, num)) {
            return t;
        }
        throw new IllegalArgumentException("The related gregorian year is read-only.");
    }

    private CalendarSystem<T> getCalendarSystem(T t) {
        if (t instanceof CalendarVariant) {
            return this.map.get(((CalendarVariant) CalendarVariant.class.cast(t)).getVariant());
        }
        return this.map.get(KEY_CALENDRICAL);
    }

    private static Integer toGregorianYear(long j) {
        long j2;
        long safeAdd = MathUtils.safeAdd(EpochDays.MODIFIED_JULIAN_DATE.transform(j, EpochDays.UTC), 678881L);
        long floorDivide = MathUtils.floorDivide(safeAdd, 146097);
        int floorModulo = MathUtils.floorModulo(safeAdd, 146097);
        if (floorModulo == 146096) {
            j2 = (floorDivide + 1) * 400;
        } else {
            int i = floorModulo / 36524;
            int i2 = floorModulo % 36524;
            int i3 = i2 / 1461;
            int i4 = i2 % 1461;
            if (i4 == 1460) {
                j2 = (floorDivide * 400) + (i * 100) + ((i3 + 1) * 4);
            } else {
                j2 = (floorDivide * 400) + (i * 100) + (i3 * 4) + (i4 / 365);
                if (((((i4 % 365) + 31) * 5) / 153) + 2 > 12) {
                    j2++;
                }
            }
        }
        return Integer.valueOf(MathUtils.safeCast(j2));
    }
}
