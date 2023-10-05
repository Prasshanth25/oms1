package net.time4j;

import androidx.exifinterface.media.ExifInterface;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.time4j.base.GregorianDate;
import net.time4j.base.GregorianMath;
import net.time4j.base.MathUtils;
import net.time4j.base.ResourceLoader;
import net.time4j.engine.AttributeQuery;
import net.time4j.engine.BasicElement;
import net.time4j.engine.ChronoCondition;
import net.time4j.engine.ChronoDisplay;
import net.time4j.engine.ChronoElement;
import net.time4j.engine.ChronoEntity;
import net.time4j.engine.Chronology;
import net.time4j.engine.ElementRule;
import net.time4j.engine.FormattableElement;
import net.time4j.format.Attributes;
import net.time4j.format.CalendarText;
import net.time4j.format.NumericalElement;
import net.time4j.format.OutputContext;
import net.time4j.format.TextAccessor;
import net.time4j.format.TextElement;
import net.time4j.format.TextWidth;
import net.time4j.format.WeekdataProvider;

/* loaded from: classes2.dex */
public final class Weekmodel implements Serializable {
    private static final int BOUNDED_WEEK_OF_MONTH = 3;
    private static final int BOUNDED_WEEK_OF_YEAR = 2;
    private static final int CALENDAR_WEEK_OF_MONTH = 1;
    private static final int CALENDAR_WEEK_OF_YEAR = 0;
    private static final WeekdataProvider LOCALIZED_WEEKDATA;
    private static final long serialVersionUID = 7794495882610436763L;
    private final transient AdjustableElement<Integer, PlainDate> boundWomElement;
    private final transient AdjustableElement<Integer, PlainDate> boundWoyElement;
    private final transient NavigableElement<Weekday> dayOfWeekElement;
    private final transient Set<ChronoElement<?>> elements;
    private final transient Weekday endOfWeekend;
    private final transient Weekday firstDayOfWeek;
    private final transient int minimalDaysInFirstWeek;
    private final transient Weekday startOfWeekend;
    private final transient ChronoCondition<GregorianDate> weekendCondition;
    private final transient AdjustableElement<Integer, PlainDate> womElement;
    private final transient AdjustableElement<Integer, PlainDate> woyElement;
    private static final Map<Locale, Weekmodel> CACHE = new ConcurrentHashMap();
    public static final Weekmodel ISO = new Weekmodel(Weekday.MONDAY, 4, Weekday.SATURDAY, Weekday.SUNDAY);

    static {
        Iterator it = ResourceLoader.getInstance().services(WeekdataProvider.class).iterator();
        LOCALIZED_WEEKDATA = it.hasNext() ? (WeekdataProvider) it.next() : null;
    }

    private Weekmodel(Weekday weekday, int i, final Weekday weekday2, final Weekday weekday3) {
        if (weekday == null) {
            throw new NullPointerException("Missing first day of week.");
        }
        if (i < 1 || i > 7) {
            throw new IllegalArgumentException("Minimal days in first week out of range: " + i);
        } else if (weekday2 == null) {
            throw new NullPointerException("Missing start of weekend.");
        } else {
            if (weekday3 == null) {
                throw new NullPointerException("Missing end of weekend.");
            }
            this.firstDayOfWeek = weekday;
            this.minimalDaysInFirstWeek = i;
            this.startOfWeekend = weekday2;
            this.endOfWeekend = weekday3;
            CalendarWeekElement calendarWeekElement = new CalendarWeekElement("WEEK_OF_YEAR", 0);
            this.woyElement = calendarWeekElement;
            CalendarWeekElement calendarWeekElement2 = new CalendarWeekElement("WEEK_OF_MONTH", 1);
            this.womElement = calendarWeekElement2;
            CalendarWeekElement calendarWeekElement3 = new CalendarWeekElement("BOUNDED_WEEK_OF_YEAR", 2);
            this.boundWoyElement = calendarWeekElement3;
            CalendarWeekElement calendarWeekElement4 = new CalendarWeekElement("BOUNDED_WEEK_OF_MONTH", 3);
            this.boundWomElement = calendarWeekElement4;
            DayOfWeekElement dayOfWeekElement = new DayOfWeekElement();
            this.dayOfWeekElement = dayOfWeekElement;
            this.weekendCondition = new ChronoCondition<GregorianDate>() { // from class: net.time4j.Weekmodel.1
                @Override // net.time4j.engine.ChronoCondition
                public boolean test(GregorianDate gregorianDate) {
                    Weekday valueOf = Weekday.valueOf(GregorianMath.getDayOfWeek(gregorianDate.getYear(), gregorianDate.getMonth(), gregorianDate.getDayOfMonth()));
                    return valueOf == weekday2 || valueOf == weekday3;
                }
            };
            HashSet hashSet = new HashSet();
            hashSet.add(calendarWeekElement);
            hashSet.add(calendarWeekElement2);
            hashSet.add(dayOfWeekElement);
            hashSet.add(calendarWeekElement3);
            hashSet.add(calendarWeekElement4);
            this.elements = Collections.unmodifiableSet(hashSet);
        }
    }

    public static Weekmodel of(Weekday weekday, int i) {
        return of(weekday, i, Weekday.SATURDAY, Weekday.SUNDAY);
    }

    public static Weekmodel of(Weekday weekday, int i, Weekday weekday2, Weekday weekday3) {
        if (weekday == Weekday.MONDAY && i == 4 && weekday2 == Weekday.SATURDAY && weekday3 == Weekday.SUNDAY) {
            return ISO;
        }
        return new Weekmodel(weekday, i, weekday2, weekday3);
    }

    public static Weekmodel ofSystem() {
        return of(Locale.getDefault());
    }

    public static Weekmodel of(Locale locale) {
        if (locale.getCountry().isEmpty()) {
            return ISO;
        }
        Map<Locale, Weekmodel> map = CACHE;
        Weekmodel weekmodel = map.get(locale);
        if (weekmodel != null) {
            return weekmodel;
        }
        WeekdataProvider weekdataProvider = LOCALIZED_WEEKDATA;
        if (weekdataProvider == null) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar(locale);
            int firstDayOfWeek = gregorianCalendar.getFirstDayOfWeek();
            return of(Weekday.valueOf(firstDayOfWeek == 1 ? 7 : firstDayOfWeek - 1), gregorianCalendar.getMinimalDaysInFirstWeek());
        }
        Weekmodel weekmodel2 = new Weekmodel(Weekday.valueOf(weekdataProvider.getFirstDayOfWeek(locale)), weekdataProvider.getMinimalDaysInFirstWeek(locale), Weekday.valueOf(weekdataProvider.getStartOfWeekend(locale)), Weekday.valueOf(weekdataProvider.getEndOfWeekend(locale)));
        if (map.size() > 150) {
            map.clear();
        }
        map.put(locale, weekmodel2);
        return weekmodel2;
    }

    public Weekday getFirstDayOfWeek() {
        return this.firstDayOfWeek;
    }

    public int getMinimalDaysInFirstWeek() {
        return this.minimalDaysInFirstWeek;
    }

    public Weekday getStartOfWeekend() {
        return this.startOfWeekend;
    }

    public Weekday getEndOfWeekend() {
        return this.endOfWeekend;
    }

    public Weekday getFirstWorkday() {
        return getEndOfWeekend().next();
    }

    @FormattableElement(format = "w")
    public AdjustableElement<Integer, PlainDate> weekOfYear() {
        return this.woyElement;
    }

    @FormattableElement(format = ExifInterface.LONGITUDE_WEST)
    public AdjustableElement<Integer, PlainDate> weekOfMonth() {
        return this.womElement;
    }

    @FormattableElement(alt = "c", format = "e")
    public NavigableElement<Weekday> localDayOfWeek() {
        return this.dayOfWeekElement;
    }

    public AdjustableElement<Integer, PlainDate> boundedWeekOfYear() {
        return this.boundWoyElement;
    }

    public AdjustableElement<Integer, PlainDate> boundedWeekOfMonth() {
        return this.boundWomElement;
    }

    public ChronoCondition<GregorianDate> weekend() {
        return this.weekendCondition;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Weekmodel) {
            Weekmodel weekmodel = (Weekmodel) obj;
            return this.firstDayOfWeek == weekmodel.firstDayOfWeek && this.minimalDaysInFirstWeek == weekmodel.minimalDaysInFirstWeek && this.startOfWeekend == weekmodel.startOfWeekend && this.endOfWeekend == weekmodel.endOfWeekend;
        }
        return false;
    }

    public int hashCode() {
        return (this.firstDayOfWeek.name().hashCode() * 17) + (this.minimalDaysInFirstWeek * 37);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append(getClass().getName());
        sb.append("[firstDayOfWeek=");
        sb.append(this.firstDayOfWeek);
        sb.append(",minimalDaysInFirstWeek=");
        sb.append(this.minimalDaysInFirstWeek);
        sb.append(",startOfWeekend=");
        sb.append(this.startOfWeekend);
        sb.append(",endOfWeekend=");
        sb.append(this.endOfWeekend);
        sb.append(']');
        return sb.toString();
    }

    public Set<ChronoElement<?>> getElements() {
        return this.elements;
    }

    static Weekday getDayOfWeek(long j) {
        return Weekday.valueOf(MathUtils.floorModulo(j + 5, 7) + 1);
    }

    private Object writeReplace() {
        return new SPX(this, 3);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException {
        throw new InvalidObjectException("Serialization proxy required.");
    }

    /* loaded from: classes2.dex */
    public class DayOfWeekElement extends AbstractDateElement<Weekday> implements NavigableElement<Weekday>, NumericalElement<Weekday>, TextElement<Weekday> {
        private static final long serialVersionUID = 1945670789283677398L;

        @Override // net.time4j.engine.BasicElement, net.time4j.engine.ChronoElement
        public char getSymbol() {
            return 'e';
        }

        @Override // net.time4j.engine.ChronoElement
        public boolean isDateElement() {
            return true;
        }

        @Override // net.time4j.engine.ChronoElement
        public boolean isTimeElement() {
            return false;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        DayOfWeekElement() {
            super("LOCAL_DAY_OF_WEEK");
            Weekmodel.this = r1;
        }

        @Override // net.time4j.engine.ChronoElement
        public Class<Weekday> getType() {
            return Weekday.class;
        }

        @Override // net.time4j.format.NumericalElement
        public int numerical(Weekday weekday) {
            return weekday.getValue(Weekmodel.this);
        }

        @Override // net.time4j.format.NumericalElement
        public int printToInt(Weekday weekday, ChronoDisplay chronoDisplay, AttributeQuery attributeQuery) {
            return numerical(weekday);
        }

        @Override // net.time4j.format.NumericalElement
        public boolean parseFromInt(ChronoEntity<?> chronoEntity, int i) {
            Weekday[] values;
            for (Weekday weekday : Weekday.values()) {
                if (weekday.getValue(Weekmodel.this) == i) {
                    chronoEntity.with((ChronoElement<DayOfWeekElement>) this, (DayOfWeekElement) weekday);
                    return true;
                }
            }
            return false;
        }

        @Override // net.time4j.engine.BasicElement, java.util.Comparator
        public int compare(ChronoDisplay chronoDisplay, ChronoDisplay chronoDisplay2) {
            int value = ((Weekday) chronoDisplay.get(this)).getValue(Weekmodel.this);
            int value2 = ((Weekday) chronoDisplay2.get(this)).getValue(Weekmodel.this);
            if (value < value2) {
                return -1;
            }
            return value == value2 ? 0 : 1;
        }

        @Override // net.time4j.engine.ChronoElement
        public Weekday getDefaultMinimum() {
            return Weekmodel.this.getFirstDayOfWeek();
        }

        @Override // net.time4j.engine.ChronoElement
        public Weekday getDefaultMaximum() {
            return Weekmodel.this.getFirstDayOfWeek().roll(6);
        }

        @Override // net.time4j.NavigableElement
        public ElementOperator<PlainDate> setToNext(Weekday weekday) {
            return new NavigationOperator(this, 9, weekday);
        }

        @Override // net.time4j.NavigableElement
        public ElementOperator<PlainDate> setToPrevious(Weekday weekday) {
            return new NavigationOperator(this, 10, weekday);
        }

        @Override // net.time4j.NavigableElement
        public ElementOperator<PlainDate> setToNextOrSame(Weekday weekday) {
            return new NavigationOperator(this, 11, weekday);
        }

        @Override // net.time4j.NavigableElement
        public ElementOperator<PlainDate> setToPreviousOrSame(Weekday weekday) {
            return new NavigationOperator(this, 12, weekday);
        }

        @Override // net.time4j.format.DisplayElement, net.time4j.engine.BasicElement, net.time4j.engine.ChronoElement
        public String getDisplayName(Locale locale) {
            String str = CalendarText.getIsoInstance(locale).getTextForms().get("L_weekday");
            return str == null ? name() : str;
        }

        @Override // net.time4j.format.TextElement
        public void print(ChronoDisplay chronoDisplay, Appendable appendable, AttributeQuery attributeQuery) throws IOException {
            appendable.append(accessor(attributeQuery, (OutputContext) attributeQuery.get(Attributes.OUTPUT_CONTEXT, OutputContext.FORMAT)).print((Enum) chronoDisplay.get(this)));
        }

        @Override // net.time4j.format.TextElement
        public Weekday parse(CharSequence charSequence, ParsePosition parsePosition, AttributeQuery attributeQuery) {
            int index = parsePosition.getIndex();
            OutputContext outputContext = (OutputContext) attributeQuery.get(Attributes.OUTPUT_CONTEXT, OutputContext.FORMAT);
            Weekday weekday = (Weekday) accessor(attributeQuery, outputContext).parse(charSequence, parsePosition, getType(), attributeQuery);
            if (weekday == null && ((Boolean) attributeQuery.get(Attributes.PARSE_MULTIPLE_CONTEXT, Boolean.TRUE)).booleanValue()) {
                parsePosition.setErrorIndex(-1);
                parsePosition.setIndex(index);
                return (Weekday) accessor(attributeQuery, outputContext == OutputContext.FORMAT ? OutputContext.STANDALONE : OutputContext.FORMAT).parse(charSequence, parsePosition, getType(), attributeQuery);
            }
            return weekday;
        }

        @Override // net.time4j.engine.BasicElement
        protected boolean doEquals(BasicElement<?> basicElement) {
            return getModel().equals(((DayOfWeekElement) basicElement).getModel());
        }

        @Override // net.time4j.engine.BasicElement
        public <T extends ChronoEntity<T>> ElementRule<T, Weekday> derive(Chronology<T> chronology) {
            if (chronology.isRegistered(PlainDate.CALENDAR_DATE)) {
                return new DRule(this);
            }
            return null;
        }

        @Override // net.time4j.engine.BasicElement
        public ChronoElement<?> getParent() {
            return PlainDate.DAY_OF_WEEK;
        }

        private TextAccessor accessor(AttributeQuery attributeQuery, OutputContext outputContext) {
            return CalendarText.getIsoInstance((Locale) attributeQuery.get(Attributes.LANGUAGE, Locale.ROOT)).getWeekdays((TextWidth) attributeQuery.get(Attributes.TEXT_WIDTH, TextWidth.WIDE), outputContext);
        }

        public Weekmodel getModel() {
            return Weekmodel.this;
        }

        private Object readResolve() throws ObjectStreamException {
            return Weekmodel.this.localDayOfWeek();
        }
    }

    /* loaded from: classes2.dex */
    private static class DRule<T extends ChronoEntity<T>> implements ElementRule<T, Weekday> {
        final DayOfWeekElement element;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ ChronoElement getChildAtCeiling(Object obj) {
            return getChildAtCeiling((DRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ ChronoElement getChildAtFloor(Object obj) {
            return getChildAtFloor((DRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Weekday getMaximum(Object obj) {
            return getMaximum((DRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Weekday getMinimum(Object obj) {
            return getMinimum((DRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Weekday getValue(Object obj) {
            return getValue((DRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ boolean isValid(Object obj, Weekday weekday) {
            return isValid((DRule<T>) ((ChronoEntity) obj), weekday);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Object withValue(Object obj, Weekday weekday, boolean z) {
            return withValue((DRule<T>) ((ChronoEntity) obj), weekday, z);
        }

        private DRule(DayOfWeekElement dayOfWeekElement) {
            this.element = dayOfWeekElement;
        }

        public Weekday getValue(T t) {
            return ((PlainDate) t.get(PlainDate.CALENDAR_DATE)).getDayOfWeek();
        }

        public Weekday getMinimum(T t) {
            PlainDate plainDate = (PlainDate) t.get(PlainDate.CALENDAR_DATE);
            if ((plainDate.getDaysSinceEpochUTC() + 1) - plainDate.getDayOfWeek().getValue(this.element.getModel()) < PlainDate.axis().getCalendarSystem().getMinimumSinceUTC()) {
                return Weekday.MONDAY;
            }
            return this.element.getDefaultMinimum();
        }

        public Weekday getMaximum(T t) {
            PlainDate plainDate = (PlainDate) t.get(PlainDate.CALENDAR_DATE);
            if ((plainDate.getDaysSinceEpochUTC() + 7) - plainDate.getDayOfWeek().getValue(this.element.getModel()) > PlainDate.axis().getCalendarSystem().getMaximumSinceUTC()) {
                return Weekday.FRIDAY;
            }
            return this.element.getDefaultMaximum();
        }

        public boolean isValid(T t, Weekday weekday) {
            if (weekday == null) {
                return false;
            }
            try {
                withValue((DRule<T>) t, weekday, false);
                return true;
            } catch (RuntimeException unused) {
                return false;
            }
        }

        public T withValue(T t, Weekday weekday, boolean z) {
            if (weekday == null) {
                throw new IllegalArgumentException("Missing weekday.");
            }
            PlainDate plainDate = (PlainDate) t.get(PlainDate.CALENDAR_DATE);
            long daysSinceUTC = plainDate.getDaysSinceUTC();
            Weekday dayOfWeek = Weekmodel.getDayOfWeek(daysSinceUTC);
            if (weekday == dayOfWeek) {
                return t;
            }
            return (T) t.with(PlainDate.CALENDAR_DATE, plainDate.withDaysSinceUTC((daysSinceUTC + weekday.getValue(this.element.getModel())) - dayOfWeek.getValue(this.element.getModel())));
        }

        public ChronoElement<?> getChildAtFloor(T t) {
            return getChild(t);
        }

        public ChronoElement<?> getChildAtCeiling(T t) {
            return getChild(t);
        }

        private ChronoElement<?> getChild(T t) {
            if (t.contains(PlainTime.WALL_TIME)) {
                return PlainTime.WALL_TIME;
            }
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public class CalendarWeekElement extends AbstractDateElement<Integer> {
        private static final long serialVersionUID = -5936254509996557266L;
        private final int category;

        @Override // net.time4j.engine.ChronoElement
        public boolean isDateElement() {
            return true;
        }

        @Override // net.time4j.engine.BasicElement, net.time4j.engine.ChronoElement
        public boolean isLenient() {
            return true;
        }

        @Override // net.time4j.engine.ChronoElement
        public boolean isTimeElement() {
            return false;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        CalendarWeekElement(String str, int i) {
            super(str);
            Weekmodel.this = r1;
            this.category = i;
        }

        @Override // net.time4j.engine.BasicElement, net.time4j.engine.ChronoElement
        public char getSymbol() {
            int i = this.category;
            if (i != 0) {
                if (i != 1) {
                    return super.getSymbol();
                }
                return 'W';
            }
            return 'w';
        }

        @Override // net.time4j.engine.ChronoElement
        public Class<Integer> getType() {
            return Integer.class;
        }

        @Override // net.time4j.engine.ChronoElement
        public Integer getDefaultMinimum() {
            return 1;
        }

        @Override // net.time4j.engine.ChronoElement
        public Integer getDefaultMaximum() {
            return Integer.valueOf(isYearRelated() ? 52 : 5);
        }

        @Override // net.time4j.format.DisplayElement, net.time4j.engine.BasicElement, net.time4j.engine.ChronoElement
        public String getDisplayName(Locale locale) {
            String str = CalendarText.getIsoInstance(locale).getTextForms().get("L_week");
            return str == null ? name() : str;
        }

        @Override // net.time4j.engine.BasicElement
        protected boolean doEquals(BasicElement<?> basicElement) {
            return getModel().equals(((CalendarWeekElement) basicElement).getModel());
        }

        @Override // net.time4j.engine.BasicElement
        public ChronoElement<?> getParent() {
            return PlainDate.WEEKDAY_IN_MONTH;
        }

        @Override // net.time4j.engine.BasicElement
        public <T extends ChronoEntity<T>> ElementRule<T, Integer> derive(Chronology<T> chronology) {
            if (chronology.isRegistered(PlainDate.CALENDAR_DATE)) {
                if (isBounded()) {
                    return new BWRule(this);
                }
                return new CWRule(this);
            }
            return null;
        }

        private Object readResolve() throws ObjectStreamException {
            Weekmodel model = getModel();
            int i = this.category;
            if (i != 0) {
                if (i != 1) {
                    if (i != 2) {
                        if (i == 3) {
                            return model.boundedWeekOfMonth();
                        }
                        throw new InvalidObjectException("Unknown category: " + this.category);
                    }
                    return model.boundedWeekOfYear();
                }
                return model.weekOfMonth();
            }
            return model.weekOfYear();
        }

        public Weekmodel getModel() {
            return Weekmodel.this;
        }

        public boolean isYearRelated() {
            return this.category % 2 == 0;
        }

        private boolean isBounded() {
            return this.category >= 2;
        }
    }

    /* loaded from: classes2.dex */
    private static class CWRule<T extends ChronoEntity<T>> implements ElementRule<T, Integer> {
        private final CalendarWeekElement owner;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ ChronoElement getChildAtCeiling(Object obj) {
            return getChildAtCeiling((CWRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ ChronoElement getChildAtFloor(Object obj) {
            return getChildAtFloor((CWRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Integer getMaximum(Object obj) {
            return getMaximum((CWRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Integer getMinimum(Object obj) {
            return getMinimum((CWRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Integer getValue(Object obj) {
            return getValue((CWRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ boolean isValid(Object obj, Integer num) {
            return isValid((CWRule<T>) ((ChronoEntity) obj), num);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Object withValue(Object obj, Integer num, boolean z) {
            return withValue((CWRule<T>) ((ChronoEntity) obj), num, z);
        }

        private CWRule(CalendarWeekElement calendarWeekElement) {
            this.owner = calendarWeekElement;
        }

        public Integer getMinimum(T t) {
            return 1;
        }

        public Integer getMaximum(T t) {
            return Integer.valueOf(getMaxCalendarWeek((PlainDate) t.get(PlainDate.CALENDAR_DATE)));
        }

        public ChronoElement<?> getChildAtFloor(T t) {
            return getChild();
        }

        public ChronoElement<?> getChildAtCeiling(T t) {
            return getChild();
        }

        private ChronoElement<?> getChild() {
            return this.owner.getModel().localDayOfWeek();
        }

        public Integer getValue(T t) {
            return Integer.valueOf(getCalendarWeek((PlainDate) t.get(PlainDate.CALENDAR_DATE)));
        }

        public boolean isValid(T t, Integer num) {
            if (num == null) {
                return false;
            }
            int intValue = num.intValue();
            if (!this.owner.isYearRelated() || intValue < 1 || intValue > 52) {
                if (!this.owner.isYearRelated() || intValue == 53) {
                    return intValue >= 1 && intValue <= getMaxCalendarWeek((PlainDate) t.get(PlainDate.CALENDAR_DATE));
                }
                return false;
            }
            return true;
        }

        public T withValue(T t, Integer num, boolean z) {
            PlainDate plainDate = (PlainDate) t.get(PlainDate.CALENDAR_DATE);
            if (num == null || (!z && !isValid((CWRule<T>) t, num))) {
                throw new IllegalArgumentException("Invalid value: " + num + " (context=" + t + ")");
            }
            return (T) t.with(PlainDate.CALENDAR_DATE, setCalendarWeek(plainDate, num.intValue()));
        }

        private int getMaxCalendarWeek(PlainDate plainDate) {
            int dayOfMonth;
            if (this.owner.isYearRelated()) {
                dayOfMonth = plainDate.getDayOfYear();
            } else {
                dayOfMonth = plainDate.getDayOfMonth();
            }
            int firstCalendarWeekAsDay = getFirstCalendarWeekAsDay(plainDate, 0);
            if (firstCalendarWeekAsDay <= dayOfMonth) {
                int firstCalendarWeekAsDay2 = getFirstCalendarWeekAsDay(plainDate, 1) + getLengthOfYM(plainDate, 0);
                if (firstCalendarWeekAsDay2 <= dayOfMonth) {
                    try {
                        int firstCalendarWeekAsDay3 = getFirstCalendarWeekAsDay(plainDate, 1);
                        firstCalendarWeekAsDay2 = getFirstCalendarWeekAsDay(plainDate, 2) + getLengthOfYM(plainDate, 1);
                        firstCalendarWeekAsDay = firstCalendarWeekAsDay3;
                    } catch (RuntimeException unused) {
                        firstCalendarWeekAsDay2 += 7;
                    }
                }
                return (firstCalendarWeekAsDay2 - firstCalendarWeekAsDay) / 7;
            }
            return ((firstCalendarWeekAsDay + getLengthOfYM(plainDate, -1)) - getFirstCalendarWeekAsDay(plainDate, -1)) / 7;
        }

        private int getFirstCalendarWeekAsDay(PlainDate plainDate, int i) {
            Weekday weekdayStart = getWeekdayStart(plainDate, i);
            Weekmodel model = this.owner.getModel();
            int value = weekdayStart.getValue(model);
            return value <= 8 - model.getMinimalDaysInFirstWeek() ? 2 - value : 9 - value;
        }

        private Weekday getWeekdayStart(PlainDate plainDate, int i) {
            if (this.owner.isYearRelated()) {
                return Weekday.valueOf(GregorianMath.getDayOfWeek(plainDate.getYear() + i, 1, 1));
            }
            int year = plainDate.getYear();
            int month = plainDate.getMonth() + i;
            if (month == 0) {
                year--;
                month = 12;
            } else if (month == 13) {
                year++;
                month = 1;
            } else if (month == 14) {
                year++;
                month = 2;
            }
            return Weekday.valueOf(GregorianMath.getDayOfWeek(year, month, 1));
        }

        private int getLengthOfYM(PlainDate plainDate, int i) {
            if (this.owner.isYearRelated()) {
                return GregorianMath.isLeapYear(plainDate.getYear() + i) ? 366 : 365;
            }
            int year = plainDate.getYear();
            int month = plainDate.getMonth() + i;
            if (month == 0) {
                year--;
                month = 12;
            } else if (month == 13) {
                year++;
                month = 1;
            }
            return GregorianMath.getLengthOfMonth(year, month);
        }

        private int getCalendarWeek(PlainDate plainDate) {
            int dayOfMonth;
            if (this.owner.isYearRelated()) {
                dayOfMonth = plainDate.getDayOfYear();
            } else {
                dayOfMonth = plainDate.getDayOfMonth();
            }
            int firstCalendarWeekAsDay = getFirstCalendarWeekAsDay(plainDate, 0);
            if (firstCalendarWeekAsDay <= dayOfMonth) {
                int i = ((dayOfMonth - firstCalendarWeekAsDay) / 7) + 1;
                if ((i >= 53 || (!this.owner.isYearRelated() && i >= 5)) && getFirstCalendarWeekAsDay(plainDate, 1) + getLengthOfYM(plainDate, 0) <= dayOfMonth) {
                    return 1;
                }
                return i;
            }
            return (((dayOfMonth + getLengthOfYM(plainDate, -1)) - getFirstCalendarWeekAsDay(plainDate, -1)) / 7) + 1;
        }

        private PlainDate setCalendarWeek(PlainDate plainDate, int i) {
            int calendarWeek = getCalendarWeek(plainDate);
            return i == calendarWeek ? plainDate : plainDate.withDaysSinceUTC(plainDate.getDaysSinceUTC() + ((i - calendarWeek) * 7));
        }
    }

    /* loaded from: classes2.dex */
    private static class BWRule<T extends ChronoEntity<T>> implements ElementRule<T, Integer> {
        private final CalendarWeekElement owner;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ ChronoElement getChildAtCeiling(Object obj) {
            return getChildAtCeiling((BWRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ ChronoElement getChildAtFloor(Object obj) {
            return getChildAtFloor((BWRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Integer getMaximum(Object obj) {
            return getMaximum((BWRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Integer getMinimum(Object obj) {
            return getMinimum((BWRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Integer getValue(Object obj) {
            return getValue((BWRule<T>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ boolean isValid(Object obj, Integer num) {
            return isValid((BWRule<T>) ((ChronoEntity) obj), num);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Object withValue(Object obj, Integer num, boolean z) {
            return withValue((BWRule<T>) ((ChronoEntity) obj), num, z);
        }

        private BWRule(CalendarWeekElement calendarWeekElement) {
            this.owner = calendarWeekElement;
        }

        public Integer getValue(T t) {
            return Integer.valueOf(getWeek((PlainDate) t.get(PlainDate.CALENDAR_DATE)));
        }

        public Integer getMinimum(T t) {
            return Integer.valueOf(getMinWeek((PlainDate) t.get(PlainDate.CALENDAR_DATE)));
        }

        public Integer getMaximum(T t) {
            return Integer.valueOf(getMaxWeek((PlainDate) t.get(PlainDate.CALENDAR_DATE)));
        }

        public ChronoElement<?> getChildAtFloor(T t) {
            return getChild(t, false);
        }

        public ChronoElement<?> getChildAtCeiling(T t) {
            return getChild(t, true);
        }

        private ChronoElement<?> getChild(T t, boolean z) {
            PlainDate plainDate = (PlainDate) t.get(PlainDate.CALENDAR_DATE);
            NavigableElement<Weekday> localDayOfWeek = this.owner.getModel().localDayOfWeek();
            int intValue = getValue((BWRule<T>) t).intValue();
            if (z) {
                if (intValue >= (this.owner.isYearRelated() ? 52 : 4)) {
                    PlainDate plainDate2 = (PlainDate) plainDate.with(localDayOfWeek, (NavigableElement<Weekday>) t.getMaximum(localDayOfWeek));
                    if (this.owner.isYearRelated()) {
                        if (plainDate2.getDayOfYear() < plainDate.getDayOfYear()) {
                            return PlainDate.DAY_OF_YEAR;
                        }
                    } else if (plainDate2.getDayOfMonth() < plainDate.getDayOfMonth()) {
                        return PlainDate.DAY_OF_MONTH;
                    }
                }
            } else if (intValue <= 1) {
                PlainDate plainDate3 = (PlainDate) plainDate.with(localDayOfWeek, (NavigableElement<Weekday>) t.getMinimum(localDayOfWeek));
                if (this.owner.isYearRelated()) {
                    if (plainDate3.getDayOfYear() > plainDate.getDayOfYear()) {
                        return PlainDate.DAY_OF_YEAR;
                    }
                } else if (plainDate3.getDayOfMonth() > plainDate.getDayOfMonth()) {
                    return PlainDate.DAY_OF_MONTH;
                }
            }
            return localDayOfWeek;
        }

        public boolean isValid(T t, Integer num) {
            if (num == null) {
                return false;
            }
            int intValue = num.intValue();
            PlainDate plainDate = (PlainDate) t.get(PlainDate.CALENDAR_DATE);
            return intValue >= getMinWeek(plainDate) && intValue <= getMaxWeek(plainDate);
        }

        public T withValue(T t, Integer num, boolean z) {
            PlainDate plainDate = (PlainDate) t.get(PlainDate.CALENDAR_DATE);
            if (num == null || (!z && !isValid((BWRule<T>) t, num))) {
                throw new IllegalArgumentException("Invalid value: " + num + " (context=" + t + ")");
            }
            return (T) t.with(PlainDate.CALENDAR_DATE, setWeek(plainDate, num.intValue()));
        }

        private int getWeek(PlainDate plainDate) {
            return getWeek(plainDate, 0);
        }

        private int getMinWeek(PlainDate plainDate) {
            return getWeek(plainDate, -1);
        }

        private int getMaxWeek(PlainDate plainDate) {
            return getWeek(plainDate, 1);
        }

        private int getWeek(PlainDate plainDate, int i) {
            int dayOfMonth;
            if (this.owner.isYearRelated()) {
                dayOfMonth = plainDate.getDayOfYear();
            } else {
                dayOfMonth = plainDate.getDayOfMonth();
            }
            int value = Weekmodel.getDayOfWeek((plainDate.getDaysSinceUTC() - dayOfMonth) + 1).getValue(this.owner.getModel());
            int i2 = value <= 8 - this.owner.getModel().getMinimalDaysInFirstWeek() ? 2 - value : 9 - value;
            if (i == -1) {
                dayOfMonth = 1;
            } else if (i != 0) {
                if (i == 1) {
                    dayOfMonth = getLengthOfYM(plainDate);
                } else {
                    throw new AssertionError("Unexpected: " + i);
                }
            }
            return MathUtils.floorDivide(dayOfMonth - i2, 7) + 1;
        }

        private PlainDate setWeek(PlainDate plainDate, int i) {
            int week = getWeek(plainDate);
            return i == week ? plainDate : plainDate.withDaysSinceUTC(plainDate.getDaysSinceUTC() + ((i - week) * 7));
        }

        private int getLengthOfYM(PlainDate plainDate) {
            if (this.owner.isYearRelated()) {
                return GregorianMath.isLeapYear(plainDate.getYear()) ? 366 : 365;
            }
            return GregorianMath.getLengthOfMonth(plainDate.getYear(), plainDate.getMonth());
        }
    }
}
