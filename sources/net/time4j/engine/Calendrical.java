package net.time4j.engine;

import net.time4j.base.MathUtils;
import net.time4j.engine.Calendrical;

/* loaded from: classes2.dex */
public abstract class Calendrical<U, D extends Calendrical<U, D>> extends TimePoint<U, D> implements CalendarDate {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.TimePoint, java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(Object obj) {
        return compareTo((Calendrical<U, D>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.TimePoint
    public /* bridge */ /* synthetic */ int compareTo(TimePoint timePoint) {
        return compareTo((Calendrical<U, D>) timePoint);
    }

    public <T extends Calendrical<?, T>> T transform(Class<T> cls) {
        String name = cls.getName();
        Chronology lookup = Chronology.lookup(cls);
        if (lookup == null) {
            throw new IllegalArgumentException("Cannot find any chronology for given target type: " + name);
        }
        return (T) transform(lookup.getCalendarSystem(), name);
    }

    public <T extends CalendarVariant<T>> T transform(Class<T> cls, String str) {
        String name = cls.getName();
        Chronology lookup = Chronology.lookup(cls);
        if (lookup == null) {
            throw new IllegalArgumentException("Cannot find any chronology for given target type: " + name);
        }
        return (T) transform(lookup.getCalendarSystem(str), name);
    }

    public <T extends CalendarVariant<T>> T transform(Class<T> cls, VariantSource variantSource) {
        return (T) transform(cls, variantSource.getVariant());
    }

    @Override // net.time4j.engine.Temporal
    public boolean isBefore(CalendarDate calendarDate) {
        return compareByTime(calendarDate) < 0;
    }

    @Override // net.time4j.engine.Temporal
    public boolean isAfter(CalendarDate calendarDate) {
        return compareByTime(calendarDate) > 0;
    }

    @Override // net.time4j.engine.Temporal
    public boolean isSimultaneous(CalendarDate calendarDate) {
        return this == calendarDate || compareByTime(calendarDate) == 0;
    }

    public int compareTo(D d) {
        if (getChronology().getChronoType() != d.getChronology().getChronoType()) {
            throw new ClassCastException("Cannot compare different types of dates, use instance of EpochDays as comparator instead.");
        }
        return compareByTime(d);
    }

    @Override // net.time4j.engine.TimePoint
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof Calendrical)) {
            Calendrical calendrical = (Calendrical) obj;
            return getChronology().getChronoType() == calendrical.getChronology().getChronoType() && getDaysSinceEpochUTC() == calendrical.getDaysSinceEpochUTC();
        }
        return false;
    }

    @Override // net.time4j.engine.TimePoint
    public int hashCode() {
        long daysSinceEpochUTC = getDaysSinceEpochUTC();
        return (int) (daysSinceEpochUTC ^ (daysSinceEpochUTC >>> 32));
    }

    public D plus(CalendarDays calendarDays) {
        long safeAdd = MathUtils.safeAdd(getDaysSinceEpochUTC(), calendarDays.getAmount());
        try {
            return getCalendarSystem().transform(safeAdd);
        } catch (IllegalArgumentException e) {
            ArithmeticException arithmeticException = new ArithmeticException("Out of range: " + safeAdd);
            arithmeticException.initCause(e);
            throw arithmeticException;
        }
    }

    public D minus(CalendarDays calendarDays) {
        return plus(CalendarDays.of(MathUtils.safeNegate(calendarDays.getAmount())));
    }

    public long getDaysSinceEpochUTC() {
        return getCalendarSystem().transform((CalendarSystem<D>) ((D) getContext()));
    }

    public int compareByTime(CalendarDate calendarDate) {
        int i = (getDaysSinceEpochUTC() > calendarDate.getDaysSinceEpochUTC() ? 1 : (getDaysSinceEpochUTC() == calendarDate.getDaysSinceEpochUTC() ? 0 : -1));
        if (i < 0) {
            return -1;
        }
        return i == 0 ? 0 : 1;
    }

    private CalendarSystem<D> getCalendarSystem() {
        return getChronology().getCalendarSystem();
    }

    private <T> T transform(CalendarSystem<T> calendarSystem, String str) {
        long daysSinceEpochUTC = getDaysSinceEpochUTC();
        if (calendarSystem.getMinimumSinceUTC() > daysSinceEpochUTC || calendarSystem.getMaximumSinceUTC() < daysSinceEpochUTC) {
            throw new ArithmeticException("Cannot transform <" + daysSinceEpochUTC + "> to: " + str);
        }
        return calendarSystem.transform(daysSinceEpochUTC);
    }
}
