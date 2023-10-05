package net.time4j.format.expert;

import java.io.IOException;
import java.util.Set;
import net.time4j.base.MathUtils;
import net.time4j.engine.AttributeQuery;
import net.time4j.engine.ChronoDisplay;
import net.time4j.engine.ChronoElement;
import net.time4j.format.Attributes;
import net.time4j.format.Leniency;

/* loaded from: classes2.dex */
public final class TwoDigitYearProcessor implements FormatProcessor<Integer> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final ChronoElement<Integer> element;
    private final Leniency lenientMode;
    private final int pivotYear;
    private final int protectedLength;
    private final int reserved;
    private final char zeroDigit;

    @Override // net.time4j.format.expert.FormatProcessor
    public boolean isNumerical() {
        return true;
    }

    public TwoDigitYearProcessor(ChronoElement<Integer> chronoElement) {
        if (chronoElement.name().startsWith("YEAR")) {
            this.element = chronoElement;
            this.reserved = 0;
            this.zeroDigit = '0';
            this.lenientMode = Leniency.SMART;
            this.protectedLength = 0;
            this.pivotYear = 100;
            return;
        }
        throw new IllegalArgumentException("Year element required: " + chronoElement);
    }

    private TwoDigitYearProcessor(ChronoElement<Integer> chronoElement, int i, char c, Leniency leniency, int i2, int i3) {
        this.element = chronoElement;
        this.reserved = i;
        this.zeroDigit = c;
        this.lenientMode = leniency;
        this.protectedLength = i2;
        this.pivotYear = i3;
    }

    @Override // net.time4j.format.expert.FormatProcessor
    public int print(ChronoDisplay chronoDisplay, Appendable appendable, AttributeQuery attributeQuery, Set<ElementPosition> set, boolean z) throws IOException {
        char charValue;
        int i = chronoDisplay.getInt(this.element);
        if (i < 0) {
            if (i == Integer.MIN_VALUE) {
                throw new IllegalArgumentException("Format context has no year: " + chronoDisplay);
            }
            throw new IllegalArgumentException("Negative year cannot be printed as two-digit-year: " + i);
        }
        if (getPivotYear(z, attributeQuery) != 100) {
            i = MathUtils.floorModulo(i, 100);
        }
        String num = Integer.toString(i);
        if (z) {
            charValue = this.zeroDigit;
        } else {
            charValue = ((Character) attributeQuery.get(Attributes.ZERO_DIGIT, '0')).charValue();
        }
        int i2 = 0;
        if (charValue != '0') {
            int i3 = charValue - '0';
            char[] charArray = num.toCharArray();
            for (int i4 = 0; i4 < charArray.length; i4++) {
                charArray[i4] = (char) (charArray[i4] + i3);
            }
            num = new String(charArray);
        }
        int length = appendable instanceof CharSequence ? ((CharSequence) appendable).length() : -1;
        if (i < 10) {
            appendable.append(charValue);
            i2 = 1;
        }
        appendable.append(num);
        int length2 = i2 + num.length();
        if (length != -1 && length2 > 0 && set != null) {
            set.add(new ElementPosition(this.element, length, length + length2));
        }
        return length2;
    }

    /* JADX WARN: Removed duplicated region for block: B:147:0x00b5  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x00cd  */
    @Override // net.time4j.format.expert.FormatProcessor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void parse(java.lang.CharSequence r11, net.time4j.format.expert.ParseLog r12, net.time4j.engine.AttributeQuery r13, net.time4j.format.expert.ParsedEntity<?> r14, boolean r15) {
        /*
            r10 = this;
            int r0 = r11.length()
            int r1 = r12.getPosition()
            r2 = 0
            if (r15 == 0) goto Le
            int r3 = r10.protectedLength
            goto L1e
        Le:
            net.time4j.engine.AttributeKey<java.lang.Integer> r3 = net.time4j.format.Attributes.PROTECTED_CHARACTERS
            java.lang.Integer r4 = java.lang.Integer.valueOf(r2)
            java.lang.Object r3 = r13.get(r3, r4)
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r3 = r3.intValue()
        L1e:
            if (r3 <= 0) goto L21
            int r0 = r0 - r3
        L21:
            if (r1 < r0) goto L3e
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r13 = "Missing digits for: "
            r11.<init>(r13)
            net.time4j.engine.ChronoElement<java.lang.Integer> r13 = r10.element
            java.lang.String r13 = r13.name()
            r11.append(r13)
            java.lang.String r11 = r11.toString()
            r12.setError(r1, r11)
            r12.setWarning()
            return
        L3e:
            if (r15 == 0) goto L43
            net.time4j.format.Leniency r4 = r10.lenientMode
            goto L4d
        L43:
            net.time4j.engine.AttributeKey<net.time4j.format.Leniency> r4 = net.time4j.format.Attributes.LENIENCY
            net.time4j.format.Leniency r5 = net.time4j.format.Leniency.SMART
            java.lang.Object r4 = r13.get(r4, r5)
            net.time4j.format.Leniency r4 = (net.time4j.format.Leniency) r4
        L4d:
            boolean r4 = r4.isStrict()
            r5 = 9
            if (r4 == 0) goto L57
            r4 = 2
            goto L59
        L57:
            r4 = 9
        L59:
            if (r15 == 0) goto L5e
            char r6 = r10.zeroDigit
            goto L70
        L5e:
            net.time4j.engine.AttributeKey<java.lang.Character> r6 = net.time4j.format.Attributes.ZERO_DIGIT
            r7 = 48
            java.lang.Character r7 = java.lang.Character.valueOf(r7)
            java.lang.Object r6 = r13.get(r6, r7)
            java.lang.Character r6 = (java.lang.Character) r6
            char r6 = r6.charValue()
        L70:
            int r7 = r10.reserved
            if (r7 <= 0) goto L8f
            if (r3 > 0) goto L8f
            r3 = r1
            r7 = 0
        L78:
            if (r3 >= r0) goto L88
            char r8 = r11.charAt(r3)
            int r8 = r8 - r6
            if (r8 < 0) goto L88
            if (r8 > r5) goto L88
            int r7 = r7 + 1
            int r3 = r3 + 1
            goto L78
        L88:
            int r3 = r10.reserved
            int r7 = r7 - r3
            int r4 = java.lang.Math.min(r4, r7)
        L8f:
            int r3 = r1 + 2
            int r4 = r4 + r1
            int r0 = java.lang.Math.min(r0, r4)
            r4 = 1
            r7 = r1
            r8 = 0
        L99:
            if (r7 >= r0) goto Lb3
            char r9 = r11.charAt(r7)
            int r9 = r9 - r6
            if (r9 < 0) goto Lab
            if (r9 > r5) goto Lab
            int r8 = r8 * 10
            int r8 = r8 + r9
            int r7 = r7 + 1
            r4 = 0
            goto L99
        Lab:
            if (r4 == 0) goto Lb3
            java.lang.String r11 = "Digit expected."
            r12.setError(r1, r11)
            return
        Lb3:
            if (r7 >= r3) goto Lcd
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r13 = "Not enough digits found for: "
            r11.<init>(r13)
            net.time4j.engine.ChronoElement<java.lang.Integer> r13 = r10.element
            java.lang.String r13 = r13.name()
            r11.append(r13)
            java.lang.String r11 = r11.toString()
            r12.setError(r1, r11)
            return
        Lcd:
            if (r7 != r3) goto Ld7
            int r11 = r10.getPivotYear(r15, r13)
            int r8 = toYear(r8, r11)
        Ld7:
            net.time4j.engine.ChronoElement<java.lang.Integer> r11 = r10.element
            r14.put(r11, r8)
            r12.setPosition(r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: net.time4j.format.expert.TwoDigitYearProcessor.parse(java.lang.CharSequence, net.time4j.format.expert.ParseLog, net.time4j.engine.AttributeQuery, net.time4j.format.expert.ParsedEntity, boolean):void");
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TwoDigitYearProcessor) {
            return this.element.equals(((TwoDigitYearProcessor) obj).element);
        }
        return false;
    }

    public int hashCode() {
        return this.element.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append(getClass().getName());
        sb.append("[element=");
        sb.append(this.element.name());
        sb.append(']');
        return sb.toString();
    }

    @Override // net.time4j.format.expert.FormatProcessor
    public ChronoElement<Integer> getElement() {
        return this.element;
    }

    @Override // net.time4j.format.expert.FormatProcessor
    public FormatProcessor<Integer> withElement(ChronoElement<Integer> chronoElement) {
        return this.element == chronoElement ? this : new TwoDigitYearProcessor(chronoElement);
    }

    @Override // net.time4j.format.expert.FormatProcessor
    public FormatProcessor<Integer> quickPath(ChronoFormatter<?> chronoFormatter, AttributeQuery attributeQuery, int i) {
        return new TwoDigitYearProcessor(this.element, i, ((Character) attributeQuery.get(Attributes.ZERO_DIGIT, '0')).charValue(), (Leniency) attributeQuery.get(Attributes.LENIENCY, Leniency.SMART), ((Integer) attributeQuery.get(Attributes.PROTECTED_CHARACTERS, 0)).intValue(), ((Integer) attributeQuery.get(Attributes.PIVOT_YEAR, Integer.valueOf(chronoFormatter.getChronology().getDefaultPivotYear()))).intValue());
    }

    private static int toYear(int i, int i2) {
        int i3;
        if (i >= i2 % 100) {
            i3 = (i2 / 100) - 1;
        } else {
            i3 = i2 / 100;
        }
        return (i3 * 100) + i;
    }

    private int getPivotYear(boolean z, AttributeQuery attributeQuery) {
        int intValue = z ? this.pivotYear : ((Integer) attributeQuery.get(Attributes.PIVOT_YEAR, Integer.valueOf(this.pivotYear))).intValue();
        if (intValue >= 100) {
            return intValue;
        }
        throw new IllegalArgumentException("Pivot year must not be smaller than 100: " + intValue);
    }
}
