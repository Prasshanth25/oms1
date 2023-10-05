package net.time4j.calendar;

import net.time4j.Weekday;
import net.time4j.base.MathUtils;
import net.time4j.calendar.service.StdIntegerDateElement;
import net.time4j.engine.CalendarDate;
import net.time4j.engine.ChronoElement;
import net.time4j.engine.ChronoEntity;
import net.time4j.engine.ChronoOperator;
import net.time4j.engine.ElementRule;
import net.time4j.engine.EpochDays;
import net.time4j.engine.IntElementRule;

/* loaded from: classes2.dex */
public final class WeekdayInMonthElement<T extends ChronoEntity<T> & CalendarDate> extends StdIntegerDateElement<T> implements OrdinalWeekdayElement<T> {
    private static final int LAST = Integer.MAX_VALUE;
    private static final long serialVersionUID = 4275169663905222176L;
    private final transient ChronoElement<Integer> domElement;
    private final transient ChronoElement<Weekday> dowElement;

    public WeekdayInMonthElement(Class<T> cls, ChronoElement<Integer> chronoElement, ChronoElement<Weekday> chronoElement2) {
        super("WEEKDAY_IN_MONTH", cls, 1, chronoElement.getDefaultMaximum().intValue() / 7, 'F', new WeekOperator(true), new WeekOperator(false));
        this.domElement = chronoElement;
        this.dowElement = chronoElement2;
    }

    @Override // net.time4j.calendar.OrdinalWeekdayElement
    public ChronoOperator<T> setToFirst(Weekday weekday) {
        return setTo(1, weekday);
    }

    @Override // net.time4j.calendar.OrdinalWeekdayElement
    public ChronoOperator<T> setToLast(Weekday weekday) {
        return setTo(Integer.MAX_VALUE, weekday);
    }

    @Override // net.time4j.calendar.OrdinalWeekdayElement
    public ChronoOperator<T> setTo(int i, Weekday weekday) {
        return new SetOperator(this, i, weekday);
    }

    public static <T extends ChronoEntity<T> & CalendarDate> ElementRule<T, Integer> getRule(WeekdayInMonthElement<T> weekdayInMonthElement) {
        return new Rule(weekdayInMonthElement);
    }

    /* loaded from: classes2.dex */
    public static class Rule<T extends ChronoEntity<T> & CalendarDate> implements IntElementRule<T> {
        private final WeekdayInMonthElement<T> wim;

        @Override // net.time4j.engine.ElementRule
        public ChronoElement getChildAtCeiling(ChronoEntity chronoEntity) {
            return null;
        }

        @Override // net.time4j.engine.ElementRule
        public ChronoElement getChildAtFloor(ChronoEntity chronoEntity) {
            return null;
        }

        Rule(WeekdayInMonthElement<T> weekdayInMonthElement) {
            this.wim = weekdayInMonthElement;
        }

        @Override // net.time4j.engine.ElementRule
        public Integer getValue(ChronoEntity chronoEntity) {
            return Integer.valueOf(getInt(chronoEntity));
        }

        @Override // net.time4j.engine.ElementRule
        public Integer getMinimum(ChronoEntity chronoEntity) {
            return 1;
        }

        @Override // net.time4j.engine.ElementRule
        public Integer getMaximum(ChronoEntity chronoEntity) {
            return Integer.valueOf(getMax(chronoEntity));
        }

        @Override // net.time4j.engine.ElementRule
        public boolean isValid(ChronoEntity chronoEntity, Integer num) {
            return num != null && isValid(chronoEntity, num.intValue());
        }

        @Override // net.time4j.engine.ElementRule
        public ChronoEntity withValue(ChronoEntity chronoEntity, Integer num, boolean z) {
            if (num != null) {
                return withValue(chronoEntity, num.intValue(), z);
            }
            throw new IllegalArgumentException("Missing value.");
        }

        @Override // net.time4j.engine.IntElementRule
        public boolean isValid(ChronoEntity chronoEntity, int i) {
            return i >= 1 && i <= getMax(chronoEntity);
        }

        @Override // net.time4j.engine.IntElementRule
        public ChronoEntity withValue(ChronoEntity chronoEntity, int i, boolean z) {
            if (isValid(chronoEntity, i)) {
                return chronoEntity.with(this.wim.setTo(i, (Weekday) chronoEntity.get(((WeekdayInMonthElement) this.wim).dowElement)));
            }
            throw new IllegalArgumentException("Invalid value: " + i);
        }

        @Override // net.time4j.engine.IntElementRule
        public int getInt(ChronoEntity chronoEntity) {
            return MathUtils.floorDivide(chronoEntity.getInt(((WeekdayInMonthElement) this.wim).domElement) - 1, 7) + 1;
        }

        private int getMax(ChronoEntity chronoEntity) {
            int i = chronoEntity.getInt(((WeekdayInMonthElement) this.wim).domElement);
            while (true) {
                int i2 = i + 7;
                if (i2 > ((Integer) chronoEntity.getMaximum(((WeekdayInMonthElement) this.wim).domElement)).intValue()) {
                    return MathUtils.floorDivide(i - 1, 7) + 1;
                }
                i = i2;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class SetOperator<T extends ChronoEntity<T> & CalendarDate> implements ChronoOperator<T> {
        private final Weekday dayOfWeek;
        private final long ordinal;
        private final WeekdayInMonthElement<T> wim;

        SetOperator(WeekdayInMonthElement<T> weekdayInMonthElement, int i, Weekday weekday) {
            if (weekday == null) {
                throw new NullPointerException("Missing value.");
            }
            this.wim = weekdayInMonthElement;
            this.ordinal = i;
            this.dayOfWeek = weekday;
        }

        @Override // net.time4j.engine.ChronoOperator
        public ChronoEntity apply(ChronoEntity chronoEntity) {
            int value;
            long floorDivide;
            Weekday weekday = (Weekday) chronoEntity.get(((WeekdayInMonthElement) this.wim).dowElement);
            int i = chronoEntity.getInt(((WeekdayInMonthElement) this.wim).domElement);
            if (this.ordinal == 2147483647L) {
                int intValue = ((Integer) chronoEntity.getMaximum(((WeekdayInMonthElement) this.wim).domElement)).intValue() - i;
                int value2 = weekday.getValue() + (intValue % 7);
                if (value2 > 7) {
                    value2 -= 7;
                }
                int value3 = this.dayOfWeek.getValue() - value2;
                floorDivide = intValue + value3;
                if (value3 > 0) {
                    floorDivide -= 7;
                }
            } else {
                floorDivide = ((this.ordinal - (MathUtils.floorDivide((i + value) - 1, 7) + 1)) * 7) + (this.dayOfWeek.getValue() - weekday.getValue());
            }
            return chronoEntity.with(EpochDays.UTC, ((CalendarDate) chronoEntity).getDaysSinceEpochUTC() + floorDivide);
        }
    }

    /* loaded from: classes2.dex */
    public static class WeekOperator<T extends ChronoEntity<T>> implements ChronoOperator<T> {
        private final boolean backwards;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ChronoOperator
        public /* bridge */ /* synthetic */ Object apply(Object obj) {
            return apply((WeekOperator<T>) ((ChronoEntity) obj));
        }

        WeekOperator(boolean z) {
            this.backwards = z;
        }

        public T apply(T t) {
            long longValue = ((Long) t.get(EpochDays.UTC)).longValue();
            return (T) t.with(EpochDays.UTC, this.backwards ? longValue - 7 : longValue + 7);
        }
    }
}
