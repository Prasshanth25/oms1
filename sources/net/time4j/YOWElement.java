package net.time4j;

import java.io.ObjectStreamException;
import net.time4j.base.GregorianMath;
import net.time4j.base.MathUtils;
import net.time4j.engine.ChronoElement;
import net.time4j.engine.ChronoEntity;
import net.time4j.engine.ChronoOperator;
import net.time4j.engine.ElementRule;
import net.time4j.engine.EpochDays;
import net.time4j.engine.UnitRule;

/* loaded from: classes2.dex */
public final class YOWElement extends AbstractDateElement<Integer> {
    private static final long serialVersionUID = -6907291758376370420L;
    private final transient ElementOperator<PlainDate> nextAdjuster;
    private final transient ElementOperator<PlainDate> previousAdjuster;
    private static final UnitRule U_RULE = new URule();
    static final YOWElement INSTANCE = new YOWElement("YEAR_OF_WEEKDATE");

    @Override // net.time4j.engine.BasicElement, net.time4j.engine.ChronoElement
    public char getSymbol() {
        return 'Y';
    }

    @Override // net.time4j.engine.ChronoElement
    public boolean isDateElement() {
        return true;
    }

    @Override // net.time4j.engine.BasicElement
    protected boolean isSingleton() {
        return true;
    }

    @Override // net.time4j.engine.ChronoElement
    public boolean isTimeElement() {
        return false;
    }

    private YOWElement(String str) {
        super(str);
        this.previousAdjuster = new YOWRollingAdjuster(-1L);
        this.nextAdjuster = new YOWRollingAdjuster(1L);
    }

    @Override // net.time4j.engine.ChronoElement
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override // net.time4j.engine.ChronoElement
    public Integer getDefaultMinimum() {
        return PlainDate.MIN_YEAR;
    }

    @Override // net.time4j.engine.ChronoElement
    public Integer getDefaultMaximum() {
        return PlainDate.MAX_YEAR;
    }

    @Override // net.time4j.AbstractDateElement, net.time4j.AdjustableElement
    public ElementOperator<PlainDate> decremented() {
        return this.previousAdjuster;
    }

    @Override // net.time4j.AbstractDateElement, net.time4j.AdjustableElement
    public ElementOperator<PlainDate> incremented() {
        return this.nextAdjuster;
    }

    public static <T extends ChronoEntity<T>> ElementRule<T, Integer> elementRule(Class<T> cls) {
        return new ERule();
    }

    public static <T extends ChronoEntity<T>> UnitRule<T> unitRule() {
        return U_RULE;
    }

    public static int getFirstCalendarWeekAsDayOfYear(PlainDate plainDate, int i) {
        return getFirstCalendarWeekAsDayOfYear(plainDate.getYear() + i);
    }

    public static int getFirstCalendarWeekAsDayOfYear(int i) {
        int value = Weekday.valueOf(GregorianMath.getDayOfWeek(i, 1, 1)).getValue(Weekmodel.ISO);
        return value <= 8 - Weekmodel.ISO.getMinimalDaysInFirstWeek() ? 2 - value : 9 - value;
    }

    public static int getLengthOfYear(PlainDate plainDate, int i) {
        return GregorianMath.isLeapYear(plainDate.getYear() + i) ? 366 : 365;
    }

    public static int getWeekOfYear(PlainDate plainDate) {
        int dayOfYear = plainDate.getDayOfYear();
        int firstCalendarWeekAsDayOfYear = getFirstCalendarWeekAsDayOfYear(plainDate, 0);
        if (firstCalendarWeekAsDayOfYear <= dayOfYear) {
            int i = ((dayOfYear - firstCalendarWeekAsDayOfYear) / 7) + 1;
            if (i < 53 || getFirstCalendarWeekAsDayOfYear(plainDate, 1) + getLengthOfYear(plainDate, 0) > dayOfYear) {
                return i;
            }
            return 1;
        }
        return (((dayOfYear + getLengthOfYear(plainDate, -1)) - getFirstCalendarWeekAsDayOfYear(plainDate, -1)) / 7) + 1;
    }

    private Object readResolve() throws ObjectStreamException {
        return INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class YOWRollingAdjuster extends ElementOperator<PlainDate> {
        private final long amount;
        private final ChronoOperator<PlainTimestamp> yowTS;

        private YOWRollingAdjuster(long j) {
            super(YOWElement.INSTANCE, 8);
            this.amount = j;
            this.yowTS = new ChronoOperator<PlainTimestamp>() { // from class: net.time4j.YOWElement.YOWRollingAdjuster.1
                @Override // net.time4j.engine.ChronoOperator
                public PlainTimestamp apply(PlainTimestamp plainTimestamp) {
                    return (PlainTimestamp) YOWElement.unitRule().addTo(plainTimestamp, YOWRollingAdjuster.this.amount);
                }
            };
        }

        @Override // net.time4j.engine.ChronoOperator
        public PlainDate apply(PlainDate plainDate) {
            return (PlainDate) YOWElement.unitRule().addTo(plainDate, this.amount);
        }

        @Override // net.time4j.ElementOperator
        public ChronoOperator<PlainTimestamp> onTimestamp() {
            return this.yowTS;
        }
    }

    /* loaded from: classes2.dex */
    private static class URule<T extends ChronoEntity<T>> implements UnitRule<T> {
        private URule() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.UnitRule
        public /* bridge */ /* synthetic */ Object addTo(Object obj, long j) {
            return addTo((URule<T>) ((ChronoEntity) obj), j);
        }

        public T addTo(T t, long j) {
            if (j == 0) {
                return t;
            }
            int safeCast = MathUtils.safeCast(MathUtils.safeAdd(((Integer) t.get(YOWElement.INSTANCE)).intValue(), j));
            PlainDate plainDate = (PlainDate) t.get(PlainDate.CALENDAR_DATE);
            int weekOfYear = plainDate.getWeekOfYear();
            Weekday dayOfWeek = plainDate.getDayOfWeek();
            if (weekOfYear == 53) {
                weekOfYear = ((Integer) PlainDate.of(safeCast, 26, dayOfWeek).getMaximum(Weekmodel.ISO.weekOfYear())).intValue();
            }
            return (T) t.with(PlainDate.CALENDAR_DATE, PlainDate.of(safeCast, weekOfYear, dayOfWeek));
        }

        @Override // net.time4j.engine.UnitRule
        public long between(T t, T t2) {
            PlainDate plainDate = (PlainDate) t.get(PlainDate.CALENDAR_DATE);
            PlainDate plainDate2 = (PlainDate) t2.get(PlainDate.CALENDAR_DATE);
            long intValue = ((Integer) plainDate2.get(YOWElement.INSTANCE)).intValue() - ((Integer) plainDate.get(YOWElement.INSTANCE)).intValue();
            int i = (intValue > 0L ? 1 : (intValue == 0L ? 0 : -1));
            if (i != 0) {
                int weekOfYear = YOWElement.getWeekOfYear(plainDate);
                int weekOfYear2 = YOWElement.getWeekOfYear(plainDate2);
                if (i > 0 && weekOfYear > weekOfYear2) {
                    intValue--;
                } else if (i < 0 && weekOfYear < weekOfYear2) {
                    intValue++;
                }
                int i2 = (intValue > 0L ? 1 : (intValue == 0L ? 0 : -1));
                if (i2 == 0 || weekOfYear != weekOfYear2) {
                    return intValue;
                }
                int value = plainDate.getDayOfWeek().getValue();
                int value2 = plainDate2.getDayOfWeek().getValue();
                if (i2 > 0 && value > value2) {
                    intValue--;
                } else if (i2 < 0 && value < value2) {
                    intValue++;
                }
                int i3 = (intValue > 0L ? 1 : (intValue == 0L ? 0 : -1));
                if (i3 != 0 && value == value2 && t.contains(PlainTime.WALL_TIME) && t2.contains(PlainTime.WALL_TIME)) {
                    PlainTime plainTime = (PlainTime) t.get(PlainTime.WALL_TIME);
                    PlainTime plainTime2 = (PlainTime) t2.get(PlainTime.WALL_TIME);
                    return (i3 <= 0 || !plainTime.isAfter(plainTime2)) ? (i3 >= 0 || !plainTime.isBefore(plainTime2)) ? intValue : intValue + 1 : intValue - 1;
                }
                return intValue;
            }
            return intValue;
        }
    }

    /* loaded from: classes2.dex */
    public static class ERule<T extends ChronoEntity<T>> implements ElementRule<T, Integer> {
        private ERule() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ ChronoElement getChildAtCeiling(Object obj) {
            return getChildAtCeiling((ERule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ ChronoElement getChildAtFloor(Object obj) {
            return getChildAtFloor((ERule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Integer getMaximum(Object obj) {
            return getMaximum((ERule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Integer getMinimum(Object obj) {
            return getMinimum((ERule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Integer getValue(Object obj) {
            return getValue((ERule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ boolean isValid(Object obj, Integer num) {
            return isValid((ERule<T>) ((ChronoEntity) obj), num);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Object withValue(Object obj, Integer num, boolean z) {
            return withValue((ERule<T>) ((ChronoEntity) obj), num, z);
        }

        public Integer getValue(T t) {
            PlainDate plainDate = (PlainDate) t.get(PlainDate.CALENDAR_DATE);
            int year = plainDate.getYear();
            int dayOfYear = plainDate.getDayOfYear();
            int firstCalendarWeekAsDayOfYear = YOWElement.getFirstCalendarWeekAsDayOfYear(plainDate, 0);
            if (firstCalendarWeekAsDayOfYear > dayOfYear) {
                year--;
            } else if (((dayOfYear - firstCalendarWeekAsDayOfYear) / 7) + 1 >= 53 && YOWElement.getFirstCalendarWeekAsDayOfYear(plainDate, 1) + YOWElement.getLengthOfYear(plainDate, 0) <= dayOfYear) {
                year++;
            }
            return Integer.valueOf(year);
        }

        public Integer getMinimum(T t) {
            return YOWElement.INSTANCE.getDefaultMinimum();
        }

        public Integer getMaximum(T t) {
            return YOWElement.INSTANCE.getDefaultMaximum();
        }

        public boolean isValid(T t, Integer num) {
            int intValue;
            return num != null && (intValue = num.intValue()) >= -999999999 && intValue <= 999999999;
        }

        public T withValue(T t, Integer num, boolean z) {
            if (num == null) {
                throw new IllegalArgumentException("Missing element value.");
            }
            return (T) t.with(PlainDate.CALENDAR_DATE, setYearOfWeekdate((PlainDate) t.get(PlainDate.CALENDAR_DATE), num.intValue()));
        }

        public ChronoElement<?> getChildAtFloor(T t) {
            return getChild();
        }

        public ChronoElement<?> getChildAtCeiling(T t) {
            return getChild();
        }

        private ChronoElement<?> getChild() {
            return Weekmodel.ISO.weekOfYear();
        }

        private static PlainDate setYearOfWeekdate(PlainDate plainDate, int i) {
            int firstCalendarWeekAsDayOfYear = YOWElement.getFirstCalendarWeekAsDayOfYear(i);
            int weekOfYear = YOWElement.getWeekOfYear(plainDate);
            long transform = EpochDays.UNIX.transform(GregorianMath.toMJD(i, 1, 1), EpochDays.MODIFIED_JULIAN_DATE) + (firstCalendarWeekAsDayOfYear - 1) + ((weekOfYear - 1) * 7) + (plainDate.getDayOfWeek().getValue(Weekmodel.ISO) - 1);
            if (weekOfYear == 53) {
                if (((YOWElement.getFirstCalendarWeekAsDayOfYear(i + 1) + (GregorianMath.isLeapYear(i) ? 366 : 365)) - firstCalendarWeekAsDayOfYear) / 7 < 53) {
                    transform -= 7;
                }
            }
            return plainDate.withDaysSinceUTC(transform - 730);
        }
    }
}
