package net.time4j.calendar;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.text.ParsePosition;
import java.util.Locale;
import net.time4j.calendar.EastAsianCalendar;
import net.time4j.engine.AttributeQuery;
import net.time4j.engine.ChronoDisplay;
import net.time4j.engine.ChronoElement;
import net.time4j.engine.ChronoException;
import net.time4j.engine.ElementRule;
import net.time4j.format.Attributes;
import net.time4j.format.TextElement;

/* loaded from: classes2.dex */
public class EastAsianST<D extends EastAsianCalendar<?, D>> implements TextElement<SolarTerm>, ElementRule<D, SolarTerm>, Serializable {
    private static final EastAsianST SINGLETON = new EastAsianST();
    private static final long serialVersionUID = 4572549754637955194L;

    @Override // net.time4j.engine.ChronoElement
    public char getSymbol() {
        return (char) 0;
    }

    @Override // net.time4j.engine.ChronoElement
    public boolean isDateElement() {
        return true;
    }

    @Override // net.time4j.engine.ChronoElement
    public boolean isLenient() {
        return false;
    }

    @Override // net.time4j.engine.ChronoElement
    public boolean isTimeElement() {
        return false;
    }

    public boolean isValid(D d, SolarTerm solarTerm) {
        return solarTerm != null;
    }

    @Override // net.time4j.engine.ChronoElement
    public String name() {
        return "SOLAR_TERM";
    }

    EastAsianST() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ ChronoElement getChildAtCeiling(Object obj) {
        return getChildAtCeiling((EastAsianST<D>) ((EastAsianCalendar) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ ChronoElement getChildAtFloor(Object obj) {
        return getChildAtFloor((EastAsianST<D>) ((EastAsianCalendar) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ SolarTerm getMaximum(Object obj) {
        return getMaximum((EastAsianST<D>) ((EastAsianCalendar) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ SolarTerm getMinimum(Object obj) {
        return getMinimum((EastAsianST<D>) ((EastAsianCalendar) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ SolarTerm getValue(Object obj) {
        return getValue((EastAsianST<D>) ((EastAsianCalendar) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ boolean isValid(Object obj, SolarTerm solarTerm) {
        return isValid((EastAsianST<D>) ((EastAsianCalendar) obj), solarTerm);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ElementRule
    public /* bridge */ /* synthetic */ Object withValue(Object obj, SolarTerm solarTerm, boolean z) {
        return withValue((EastAsianST<D>) ((EastAsianCalendar) obj), solarTerm, z);
    }

    public static <D extends EastAsianCalendar<?, D>> EastAsianST<D> getInstance() {
        return SINGLETON;
    }

    @Override // net.time4j.engine.ChronoElement
    public Class<SolarTerm> getType() {
        return SolarTerm.class;
    }

    @Override // java.util.Comparator
    public int compare(ChronoDisplay chronoDisplay, ChronoDisplay chronoDisplay2) {
        return ((SolarTerm) chronoDisplay.get(this)).compareTo((SolarTerm) chronoDisplay2.get(this));
    }

    @Override // net.time4j.engine.ChronoElement
    public SolarTerm getDefaultMinimum() {
        return SolarTerm.MINOR_01_LICHUN_315;
    }

    @Override // net.time4j.engine.ChronoElement
    public SolarTerm getDefaultMaximum() {
        return SolarTerm.MAJOR_12_DAHAN_300;
    }

    @Override // net.time4j.engine.ChronoElement
    public String getDisplayName(Locale locale) {
        String language = locale.getLanguage();
        return language.equals("zh") ? locale.getCountry().equals("TW") ? "節氣" : "节气" : language.equals("ko") ? "절기" : language.equals("vi") ? "tiết khí" : language.equals("ja") ? "節気" : language.isEmpty() ? "jieqi" : "jiéqì";
    }

    @Override // net.time4j.format.TextElement
    public void print(ChronoDisplay chronoDisplay, Appendable appendable, AttributeQuery attributeQuery) throws IOException, ChronoException {
        appendable.append(((SolarTerm) chronoDisplay.get(this)).getDisplayName((Locale) attributeQuery.get(Attributes.LANGUAGE, Locale.ROOT)));
    }

    @Override // net.time4j.format.TextElement
    public SolarTerm parse(CharSequence charSequence, ParsePosition parsePosition, AttributeQuery attributeQuery) {
        Locale locale = (Locale) attributeQuery.get(Attributes.LANGUAGE, Locale.ROOT);
        int length = charSequence.length();
        if (parsePosition.getIndex() >= length) {
            parsePosition.setErrorIndex(length);
            return null;
        }
        return SolarTerm.parse(charSequence, locale, parsePosition);
    }

    public SolarTerm getValue(D d) {
        return SolarTerm.of(d.getCalendarSystem().midnight(d.getDaysSinceEpochUTC() + 1));
    }

    public SolarTerm getMinimum(D d) {
        EastAsianCS calendarSystem = d.getCalendarSystem();
        return SolarTerm.of(calendarSystem.midnight(calendarSystem.newYear(d.getCycle(), d.getYear().getNumber()) + 1));
    }

    public SolarTerm getMaximum(D d) {
        EastAsianCS calendarSystem = d.getCalendarSystem();
        return SolarTerm.of(calendarSystem.midnight(calendarSystem.newYear(d.getCycle(), d.getYear().getNumber()) + d.lengthOfYear()));
    }

    public D withValue(D d, SolarTerm solarTerm, boolean z) {
        if (solarTerm == null) {
            throw new IllegalArgumentException("Missing solar term.");
        }
        return (D) d.with(solarTerm.sinceNewYear());
    }

    public ChronoElement<?> getChildAtFloor(D d) {
        throw new AbstractMethodError();
    }

    public ChronoElement<?> getChildAtCeiling(D d) {
        throw new AbstractMethodError();
    }

    protected Object readResolve() throws ObjectStreamException {
        return SINGLETON;
    }
}
