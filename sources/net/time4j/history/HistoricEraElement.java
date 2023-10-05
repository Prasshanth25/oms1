package net.time4j.history;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.text.ParsePosition;
import java.util.Locale;
import net.time4j.PlainDate;
import net.time4j.engine.AttributeQuery;
import net.time4j.engine.BasicElement;
import net.time4j.engine.ChronoDisplay;
import net.time4j.engine.ChronoElement;
import net.time4j.engine.ChronoEntity;
import net.time4j.engine.ChronoException;
import net.time4j.engine.Chronology;
import net.time4j.engine.ElementRule;
import net.time4j.format.Attributes;
import net.time4j.format.CalendarText;
import net.time4j.format.DisplayElement;
import net.time4j.format.TextAccessor;
import net.time4j.format.TextElement;
import net.time4j.format.TextWidth;
import net.time4j.history.internal.HistoricAttribute;

/* loaded from: classes2.dex */
public final class HistoricEraElement extends DisplayElement<HistoricEra> implements TextElement<HistoricEra> {
    private static final Locale LATIN = new Locale("la");
    private static final long serialVersionUID = 5200533417265981438L;
    private final ChronoHistory history;

    @Override // net.time4j.engine.BasicElement, net.time4j.engine.ChronoElement
    public char getSymbol() {
        return 'G';
    }

    @Override // net.time4j.engine.ChronoElement
    public boolean isDateElement() {
        return true;
    }

    @Override // net.time4j.engine.ChronoElement
    public boolean isTimeElement() {
        return false;
    }

    public HistoricEraElement(ChronoHistory chronoHistory) {
        super("ERA");
        this.history = chronoHistory;
    }

    @Override // net.time4j.engine.ChronoElement
    public Class<HistoricEra> getType() {
        return HistoricEra.class;
    }

    @Override // net.time4j.engine.ChronoElement
    public HistoricEra getDefaultMinimum() {
        return HistoricEra.BC;
    }

    @Override // net.time4j.engine.ChronoElement
    public HistoricEra getDefaultMaximum() {
        return HistoricEra.AD;
    }

    @Override // net.time4j.format.TextElement
    public void print(ChronoDisplay chronoDisplay, Appendable appendable, AttributeQuery attributeQuery) throws IOException {
        appendable.append(accessor(attributeQuery).print((Enum) chronoDisplay.get(this)));
    }

    @Override // net.time4j.format.TextElement
    public HistoricEra parse(CharSequence charSequence, ParsePosition parsePosition, AttributeQuery attributeQuery) {
        return (HistoricEra) accessor(attributeQuery).parse(charSequence, parsePosition, getType(), attributeQuery);
    }

    @Override // net.time4j.engine.BasicElement
    public <T extends ChronoEntity<T>> ElementRule<T, HistoricEra> derive(Chronology<T> chronology) {
        if (chronology.isRegistered(PlainDate.COMPONENT)) {
            return new Rule(this.history);
        }
        return null;
    }

    @Override // net.time4j.engine.BasicElement
    protected boolean doEquals(BasicElement<?> basicElement) {
        return this.history.equals(((HistoricEraElement) basicElement).history);
    }

    private TextAccessor accessor(AttributeQuery attributeQuery) {
        TextWidth textWidth = (TextWidth) attributeQuery.get(Attributes.TEXT_WIDTH, TextWidth.WIDE);
        if (((Boolean) attributeQuery.get(HistoricAttribute.LATIN_ERA, Boolean.FALSE)).booleanValue()) {
            CalendarText calendarText = CalendarText.getInstance("historic", LATIN);
            String[] strArr = new String[1];
            strArr[0] = textWidth != TextWidth.WIDE ? "a" : "w";
            return calendarText.getTextForms(this, strArr);
        }
        CalendarText isoInstance = CalendarText.getIsoInstance((Locale) attributeQuery.get(Attributes.LANGUAGE, Locale.ROOT));
        if (((Boolean) attributeQuery.get(HistoricAttribute.COMMON_ERA, Boolean.FALSE)).booleanValue()) {
            String[] strArr2 = new String[2];
            strArr2[0] = textWidth != TextWidth.WIDE ? "a" : "w";
            strArr2[1] = "alt";
            return isoInstance.getTextForms(this, strArr2);
        }
        return isoInstance.getEras(textWidth);
    }

    private Object readResolve() throws ObjectStreamException {
        return this.history.era();
    }

    /* loaded from: classes2.dex */
    private static class Rule<C extends ChronoEntity<C>> implements ElementRule<C, HistoricEra> {
        private final ChronoHistory history;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ ChronoElement getChildAtCeiling(Object obj) {
            return getChildAtCeiling((Rule<C>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ ChronoElement getChildAtFloor(Object obj) {
            return getChildAtFloor((Rule<C>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ HistoricEra getMaximum(Object obj) {
            return getMaximum((Rule<C>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ HistoricEra getMinimum(Object obj) {
            return getMinimum((Rule<C>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ HistoricEra getValue(Object obj) {
            return getValue((Rule<C>) ((ChronoEntity) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ boolean isValid(Object obj, HistoricEra historicEra) {
            return isValid((Rule<C>) ((ChronoEntity) obj), historicEra);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // net.time4j.engine.ElementRule
        public /* bridge */ /* synthetic */ Object withValue(Object obj, HistoricEra historicEra, boolean z) {
            return withValue((Rule<C>) ((ChronoEntity) obj), historicEra, z);
        }

        Rule(ChronoHistory chronoHistory) {
            this.history = chronoHistory;
        }

        public HistoricEra getValue(C c) {
            try {
                return this.history.convert((PlainDate) c.get(PlainDate.COMPONENT)).getEra();
            } catch (IllegalArgumentException e) {
                throw new ChronoException(e.getMessage(), e);
            }
        }

        public HistoricEra getMinimum(C c) {
            HistoricEra value = getValue((Rule<C>) c);
            return value == HistoricEra.AD ? HistoricEra.BC : value;
        }

        public HistoricEra getMaximum(C c) {
            HistoricEra value = getValue((Rule<C>) c);
            return value == HistoricEra.BC ? HistoricEra.AD : value;
        }

        public boolean isValid(C c, HistoricEra historicEra) {
            if (historicEra == null) {
                return false;
            }
            try {
                return this.history.convert((PlainDate) c.get(PlainDate.COMPONENT)).getEra() == historicEra;
            } catch (IllegalArgumentException unused) {
                return false;
            }
        }

        public C withValue(C c, HistoricEra historicEra, boolean z) {
            if (historicEra == null) {
                throw new IllegalArgumentException("Missing era value.");
            }
            if (this.history.convert((PlainDate) c.get(PlainDate.COMPONENT)).getEra() == historicEra) {
                return c;
            }
            throw new IllegalArgumentException(historicEra.name());
        }

        public ChronoElement<?> getChildAtFloor(C c) {
            throw new UnsupportedOperationException("Never called.");
        }

        public ChronoElement<?> getChildAtCeiling(C c) {
            throw new UnsupportedOperationException("Never called.");
        }
    }
}
