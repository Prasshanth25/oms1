package net.time4j.calendar;

import net.time4j.Weekday;
import net.time4j.Weekmodel;
import net.time4j.base.MathUtils;
import net.time4j.engine.CalendarDate;
import net.time4j.engine.CalendarSystem;
import net.time4j.engine.ChronoElement;
import net.time4j.engine.ChronoFunction;
import net.time4j.engine.ElementRule;

/* loaded from: classes2.dex */
class WeekdayRule<D extends CalendarDate> implements ElementRule<D, Weekday> {
    private final ChronoFunction<D, CalendarSystem<D>> calsysFunc;
    private final Weekmodel stdWeekmodel;

    public ChronoElement<?> getChildAtCeiling(D d) {
        return null;
    }

    public ChronoElement<?> getChildAtFloor(D d) {
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ ChronoElement getChildAtCeiling(Object obj) {
        return getChildAtCeiling((WeekdayRule<D>) ((CalendarDate) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ ChronoElement getChildAtFloor(Object obj) {
        return getChildAtFloor((WeekdayRule<D>) ((CalendarDate) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ Weekday getMaximum(Object obj) {
        return getMaximum((WeekdayRule<D>) ((CalendarDate) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ Weekday getMinimum(Object obj) {
        return getMinimum((WeekdayRule<D>) ((CalendarDate) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ Weekday getValue(Object obj) {
        return getValue((WeekdayRule<D>) ((CalendarDate) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ boolean isValid(Object obj, Weekday weekday) {
        return isValid((WeekdayRule<D>) ((CalendarDate) obj), weekday);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ Object withValue(Object obj, Weekday weekday, boolean z) {
        return withValue((WeekdayRule<D>) ((CalendarDate) obj), weekday, z);
    }

    public WeekdayRule(Weekmodel weekmodel, ChronoFunction<D, CalendarSystem<D>> chronoFunction) {
        this.stdWeekmodel = weekmodel;
        this.calsysFunc = chronoFunction;
    }

    public Weekday getValue(D d) {
        return getWeekday(d.getDaysSinceEpochUTC());
    }

    public Weekday getMinimum(D d) {
        CalendarSystem<D> apply = this.calsysFunc.apply(d);
        if ((d.getDaysSinceEpochUTC() + 1) - getValue((WeekdayRule<D>) d).getValue(this.stdWeekmodel) < apply.getMinimumSinceUTC()) {
            return getWeekday(apply.getMinimumSinceUTC());
        }
        return this.stdWeekmodel.getFirstDayOfWeek();
    }

    public Weekday getMaximum(D d) {
        CalendarSystem<D> apply = this.calsysFunc.apply(d);
        if ((d.getDaysSinceEpochUTC() + 7) - getValue((WeekdayRule<D>) d).getValue(this.stdWeekmodel) > apply.getMaximumSinceUTC()) {
            return getWeekday(apply.getMaximumSinceUTC());
        }
        return this.stdWeekmodel.getFirstDayOfWeek().roll(6);
    }

    public boolean isValid(D d, Weekday weekday) {
        if (weekday == null) {
            return false;
        }
        long daysSinceEpochUTC = (d.getDaysSinceEpochUTC() + weekday.getValue(this.stdWeekmodel)) - getValue((WeekdayRule<D>) d).getValue(this.stdWeekmodel);
        CalendarSystem<D> apply = this.calsysFunc.apply(d);
        return daysSinceEpochUTC >= apply.getMinimumSinceUTC() && daysSinceEpochUTC <= apply.getMaximumSinceUTC();
    }

    public D withValue(D d, Weekday weekday, boolean z) {
        if (weekday == null) {
            throw new IllegalArgumentException("Missing weekday.");
        }
        int value = getValue((WeekdayRule<D>) d).getValue(this.stdWeekmodel);
        long daysSinceEpochUTC = (d.getDaysSinceEpochUTC() + weekday.getValue(this.stdWeekmodel)) - value;
        CalendarSystem<D> apply = this.calsysFunc.apply(d);
        if (daysSinceEpochUTC >= apply.getMinimumSinceUTC() && daysSinceEpochUTC <= apply.getMaximumSinceUTC()) {
            return apply.transform(daysSinceEpochUTC);
        }
        throw new IllegalArgumentException("New day out of supported range.");
    }

    private static Weekday getWeekday(long j) {
        return Weekday.valueOf(MathUtils.floorModulo(j + 5, 7) + 1);
    }
}
