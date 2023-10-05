package net.time4j.history;

import java.util.Locale;
import net.time4j.base.MathUtils;
import net.time4j.engine.CalendarEra;
import net.time4j.engine.ChronoElement;
import net.time4j.format.CalendarText;
import net.time4j.format.TextWidth;

/* loaded from: classes2.dex */
public enum HistoricEra implements CalendarEra {
    BC,
    AD,
    HISPANIC,
    BYZANTINE,
    AB_URBE_CONDITA;

    public String getDisplayName(Locale locale, TextWidth textWidth) {
        return CalendarText.getIsoInstance(locale).getEras(textWidth).print(this);
    }

    public String getAlternativeName(Locale locale, TextWidth textWidth) {
        CalendarText isoInstance = CalendarText.getIsoInstance(locale);
        ChronoElement<HistoricEra> era = ChronoHistory.ofFirstGregorianReform().era();
        String[] strArr = new String[2];
        strArr[0] = textWidth == TextWidth.WIDE ? "w" : "a";
        strArr[1] = "alt";
        return isoInstance.getTextForms(era, strArr).print(this);
    }

    /* renamed from: net.time4j.history.HistoricEra$1 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$time4j$history$HistoricEra;

        static {
            int[] iArr = new int[HistoricEra.values().length];
            $SwitchMap$net$time4j$history$HistoricEra = iArr;
            try {
                iArr[HistoricEra.BC.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$net$time4j$history$HistoricEra[HistoricEra.AD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$net$time4j$history$HistoricEra[HistoricEra.HISPANIC.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$net$time4j$history$HistoricEra[HistoricEra.BYZANTINE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$net$time4j$history$HistoricEra[HistoricEra.AB_URBE_CONDITA.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public int annoDomini(int i) {
        try {
            int i2 = AnonymousClass1.$SwitchMap$net$time4j$history$HistoricEra[ordinal()];
            if (i2 != 1) {
                if (i2 != 2) {
                    if (i2 != 3) {
                        if (i2 != 4) {
                            if (i2 == 5) {
                                return MathUtils.safeSubtract(i, 753);
                            }
                            throw new UnsupportedOperationException(name());
                        }
                        return MathUtils.safeSubtract(i, 5508);
                    }
                    return MathUtils.safeSubtract(i, 38);
                }
                return i;
            }
            return MathUtils.safeSubtract(1, i);
        } catch (ArithmeticException unused) {
            throw new IllegalArgumentException("Out of range: " + i);
        }
    }

    public int yearOfEra(HistoricEra historicEra, int i) {
        int annoDomini = historicEra.annoDomini(i);
        try {
            int i2 = AnonymousClass1.$SwitchMap$net$time4j$history$HistoricEra[ordinal()];
            if (i2 != 1) {
                if (i2 != 2) {
                    if (i2 != 3) {
                        if (i2 != 4) {
                            if (i2 == 5) {
                                return MathUtils.safeAdd(annoDomini, 753);
                            }
                            throw new UnsupportedOperationException(name());
                        }
                        return MathUtils.safeAdd(annoDomini, 5508);
                    }
                    return MathUtils.safeAdd(annoDomini, 38);
                }
                return annoDomini;
            }
            return MathUtils.safeSubtract(1, annoDomini);
        } catch (ArithmeticException unused) {
            throw new IllegalArgumentException("Out of range: " + i);
        }
    }
}
