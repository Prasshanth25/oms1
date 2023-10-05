package net.time4j.calendar;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.text.ParsePosition;
import java.util.Locale;
import net.time4j.CalendarDateElement;
import net.time4j.CalendarUnit;
import net.time4j.PlainDate;
import net.time4j.engine.AttributeQuery;
import net.time4j.engine.CalendarEra;
import net.time4j.engine.ChronoDisplay;
import net.time4j.engine.ChronoElement;
import net.time4j.engine.ChronoEntity;
import net.time4j.engine.ChronoException;
import net.time4j.engine.Chronology;
import net.time4j.engine.ElementRule;
import net.time4j.engine.FormattableElement;
import net.time4j.format.Attributes;
import net.time4j.format.CalendarText;
import net.time4j.format.DisplayElement;
import net.time4j.format.TextElement;
import net.time4j.format.TextWidth;

/* loaded from: classes2.dex */
public enum KoreanEra implements CalendarEra {
    DANGI;
    
    private final transient ChronoElement<KoreanEra> eraElement = new EraElement();
    private final transient ChronoElement<Integer> yearOfEraElement = new YearOfEraElement();

    KoreanEra() {
    }

    public String getDisplayName(Locale locale) {
        return getDisplayName(locale, TextWidth.WIDE);
    }

    public String getDisplayName(Locale locale, TextWidth textWidth) {
        return CalendarText.getInstance("dangi", locale).getEras(textWidth).print(this);
    }

    @FormattableElement(format = "G")
    public ChronoElement<KoreanEra> era() {
        return this.eraElement;
    }

    @FormattableElement(format = "y")
    public ChronoElement<Integer> yearOfEra() {
        return this.yearOfEraElement;
    }

    /* loaded from: classes2.dex */
    public static class EraElement extends DisplayElement<KoreanEra> implements TextElement<KoreanEra> {
        private static final long serialVersionUID = -5179188137244162427L;

        @Override // net.time4j.engine.BasicElement, net.time4j.engine.ChronoElement
        public char getSymbol() {
            return 'G';
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

        private EraElement() {
            super("ERA");
        }

        @Override // net.time4j.engine.ChronoElement
        public Class<KoreanEra> getType() {
            return KoreanEra.class;
        }

        @Override // net.time4j.engine.ChronoElement
        public KoreanEra getDefaultMinimum() {
            return KoreanEra.DANGI;
        }

        @Override // net.time4j.engine.ChronoElement
        public KoreanEra getDefaultMaximum() {
            return KoreanEra.DANGI;
        }

        @Override // net.time4j.format.TextElement
        public void print(ChronoDisplay chronoDisplay, Appendable appendable, AttributeQuery attributeQuery) throws IOException, ChronoException {
            appendable.append(KoreanEra.DANGI.getDisplayName((Locale) attributeQuery.get(Attributes.LANGUAGE, Locale.ROOT), (TextWidth) attributeQuery.get(Attributes.TEXT_WIDTH, TextWidth.WIDE)));
        }

        @Override // net.time4j.format.TextElement
        public KoreanEra parse(CharSequence charSequence, ParsePosition parsePosition, AttributeQuery attributeQuery) {
            Locale locale = (Locale) attributeQuery.get(Attributes.LANGUAGE, Locale.ROOT);
            boolean booleanValue = ((Boolean) attributeQuery.get(Attributes.PARSE_CASE_INSENSITIVE, Boolean.TRUE)).booleanValue();
            boolean booleanValue2 = ((Boolean) attributeQuery.get(Attributes.PARSE_PARTIAL_COMPARE, Boolean.FALSE)).booleanValue();
            int index = parsePosition.getIndex();
            String displayName = KoreanEra.DANGI.getDisplayName(locale, (TextWidth) attributeQuery.get(Attributes.TEXT_WIDTH, TextWidth.WIDE));
            int max = Math.max(Math.min(displayName.length() + index, charSequence.length()), index);
            if (max > index) {
                String charSequence2 = charSequence.subSequence(index, max).toString();
                if (booleanValue) {
                    displayName = displayName.toLowerCase(locale);
                    charSequence2 = charSequence2.toLowerCase(locale);
                }
                if (displayName.equals(charSequence2) || (booleanValue2 && displayName.startsWith(charSequence2))) {
                    parsePosition.setIndex(max);
                    return KoreanEra.DANGI;
                }
            }
            parsePosition.setErrorIndex(index);
            return null;
        }

        @Override // net.time4j.engine.BasicElement
        public <T extends ChronoEntity<T>> ElementRule<T, KoreanEra> derive(Chronology<T> chronology) {
            if (chronology.isRegistered(PlainDate.COMPONENT)) {
                return new EraRule();
            }
            return null;
        }

        private Object readResolve() throws ObjectStreamException {
            return KoreanEra.DANGI.era();
        }
    }

    /* loaded from: classes2.dex */
    private static class EraRule implements ElementRule<ChronoEntity<?>, KoreanEra> {
        private EraRule() {
        }

        @Override // net.time4j.engine.ElementRule
        public KoreanEra getValue(ChronoEntity<?> chronoEntity) {
            return KoreanEra.DANGI;
        }

        @Override // net.time4j.engine.ElementRule
        public KoreanEra getMinimum(ChronoEntity<?> chronoEntity) {
            return KoreanEra.DANGI;
        }

        @Override // net.time4j.engine.ElementRule
        public KoreanEra getMaximum(ChronoEntity<?> chronoEntity) {
            return KoreanEra.DANGI;
        }

        @Override // net.time4j.engine.ElementRule
        public boolean isValid(ChronoEntity<?> chronoEntity, KoreanEra koreanEra) {
            return koreanEra == KoreanEra.DANGI;
        }

        @Override // net.time4j.engine.ElementRule
        public ChronoEntity<?> withValue(ChronoEntity<?> chronoEntity, KoreanEra koreanEra, boolean z) {
            if (isValid(chronoEntity, koreanEra)) {
                return chronoEntity;
            }
            throw new IllegalArgumentException("Invalid Korean era: " + koreanEra);
        }

        @Override // net.time4j.engine.ElementRule
        public ChronoElement<?> getChildAtFloor(ChronoEntity<?> chronoEntity) {
            throw new AbstractMethodError("Never called.");
        }

        @Override // net.time4j.engine.ElementRule
        public ChronoElement<?> getChildAtCeiling(ChronoEntity<?> chronoEntity) {
            throw new AbstractMethodError("Never called.");
        }
    }

    /* loaded from: classes2.dex */
    public static class YearOfEraElement extends DisplayElement<Integer> {
        private static final long serialVersionUID = -7864513245908399367L;

        @Override // net.time4j.engine.BasicElement, net.time4j.engine.ChronoElement
        public char getSymbol() {
            return 'y';
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

        private YearOfEraElement() {
            super("YEAR_OF_ERA");
        }

        @Override // net.time4j.engine.ChronoElement
        public Class<Integer> getType() {
            return Integer.class;
        }

        @Override // net.time4j.engine.ChronoElement
        public Integer getDefaultMinimum() {
            return 3978;
        }

        @Override // net.time4j.engine.ChronoElement
        public Integer getDefaultMaximum() {
            return 5332;
        }

        @Override // net.time4j.engine.BasicElement
        public <T extends ChronoEntity<T>> ElementRule<T, Integer> derive(Chronology<T> chronology) {
            if (chronology.isRegistered(PlainDate.COMPONENT)) {
                return new GregorianYearOfEraRule();
            }
            return null;
        }

        private Object readResolve() throws ObjectStreamException {
            return KoreanEra.DANGI.yearOfEra();
        }
    }

    /* loaded from: classes2.dex */
    private static class GregorianYearOfEraRule implements ElementRule<ChronoEntity<?>, Integer> {
        private GregorianYearOfEraRule() {
        }

        @Override // net.time4j.engine.ElementRule
        public Integer getValue(ChronoEntity<?> chronoEntity) {
            return Integer.valueOf(getInt(chronoEntity));
        }

        @Override // net.time4j.engine.ElementRule
        public Integer getMinimum(ChronoEntity<?> chronoEntity) {
            return -999997666;
        }

        @Override // net.time4j.engine.ElementRule
        public Integer getMaximum(ChronoEntity<?> chronoEntity) {
            return 1000002332;
        }

        @Override // net.time4j.engine.ElementRule
        public boolean isValid(ChronoEntity<?> chronoEntity, Integer num) {
            if (num == null) {
                return false;
            }
            return num.intValue() >= getMinimum(chronoEntity).intValue() && num.intValue() <= getMaximum(chronoEntity).intValue();
        }

        /* JADX WARN: Type inference failed for: r3v3, types: [net.time4j.engine.ChronoEntity<?>, net.time4j.engine.ChronoEntity] */
        @Override // net.time4j.engine.ElementRule
        public ChronoEntity<?> withValue(ChronoEntity<?> chronoEntity, Integer num, boolean z) {
            if (num == null) {
                throw new IllegalArgumentException("Missing year of era.");
            }
            if (isValid(chronoEntity, num)) {
                int i = getInt(chronoEntity);
                return chronoEntity.with((ChronoElement<CalendarDateElement>) PlainDate.COMPONENT, (CalendarDateElement) ((PlainDate) ((PlainDate) chronoEntity.get(PlainDate.COMPONENT)).plus(num.intValue() - i, CalendarUnit.YEARS)));
            }
            throw new IllegalArgumentException("Invalid year of era: " + num);
        }

        @Override // net.time4j.engine.ElementRule
        public ChronoElement<?> getChildAtFloor(ChronoEntity<?> chronoEntity) {
            throw new AbstractMethodError("Never called.");
        }

        @Override // net.time4j.engine.ElementRule
        public ChronoElement<?> getChildAtCeiling(ChronoEntity<?> chronoEntity) {
            throw new AbstractMethodError("Never called.");
        }

        private int getInt(ChronoEntity<?> chronoEntity) {
            return ((PlainDate) chronoEntity.get(PlainDate.COMPONENT)).getYear() + 2333;
        }
    }
}
