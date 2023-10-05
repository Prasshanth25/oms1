package com.facebook.hermes.intl;

import android.icu.text.MeasureFormat;
import androidx.autofill.HintConstants;
import com.facebook.react.views.scroll.ReactScrollViewHelper;
import java.text.AttributedCharacterIterator;

/* loaded from: classes.dex */
public interface IPlatformNumberFormatter {

    /* loaded from: classes.dex */
    public enum RoundingType {
        SIGNIFICANT_DIGITS,
        FRACTION_DIGITS,
        COMPACT_ROUNDING
    }

    IPlatformNumberFormatter configure(ILocaleObject<?> iLocaleObject, String str, Style style, CurrencySign currencySign, Notation notation, CompactDisplay compactDisplay) throws JSRangeErrorException;

    String fieldToString(AttributedCharacterIterator.Attribute attribute, double d);

    String format(double d) throws JSRangeErrorException;

    AttributedCharacterIterator formatToParts(double d) throws JSRangeErrorException;

    String[] getAvailableLocales();

    String getDefaultNumberingSystem(ILocaleObject<?> iLocaleObject) throws JSRangeErrorException;

    IPlatformNumberFormatter setCurrency(String str, CurrencyDisplay currencyDisplay) throws JSRangeErrorException;

    IPlatformNumberFormatter setFractionDigits(RoundingType roundingType, int i, int i2);

    IPlatformNumberFormatter setGrouping(boolean z);

    IPlatformNumberFormatter setMinIntergerDigits(int i);

    IPlatformNumberFormatter setSignDisplay(SignDisplay signDisplay);

    IPlatformNumberFormatter setSignificantDigits(RoundingType roundingType, int i, int i2) throws JSRangeErrorException;

    IPlatformNumberFormatter setUnits(String str, UnitDisplay unitDisplay) throws JSRangeErrorException;

    /* loaded from: classes.dex */
    public enum Style {
        DECIMAL,
        PERCENT,
        CURRENCY,
        UNIT;

        @Override // java.lang.Enum
        public String toString() {
            int i = AnonymousClass1.$SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$Style[ordinal()];
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i == 4) {
                            return "unit";
                        }
                        throw new IllegalArgumentException();
                    }
                    return "currency";
                }
                return "percent";
            }
            return "decimal";
        }

        public int getInitialNumberFormatStyle(Notation notation, CurrencySign currencySign) throws JSRangeErrorException {
            int i = AnonymousClass1.$SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$Style[ordinal()];
            if (i != 2) {
                if (i != 3) {
                    return (notation == Notation.SCIENTIFIC || notation == Notation.ENGINEERING) ? 3 : 0;
                } else if (currencySign == CurrencySign.ACCOUNTING) {
                    return 7;
                } else {
                    if (currencySign == CurrencySign.STANDARD) {
                        return 1;
                    }
                    throw new JSRangeErrorException("Unrecognized formatting style requested.");
                }
            }
            return 2;
        }
    }

    /* loaded from: classes.dex */
    public enum Notation {
        STANDARD,
        SCIENTIFIC,
        ENGINEERING,
        COMPACT;

        @Override // java.lang.Enum
        public String toString() {
            int i = AnonymousClass1.$SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$Notation[ordinal()];
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i == 4) {
                            return "compact";
                        }
                        throw new IllegalArgumentException();
                    }
                    return "engineering";
                }
                return "scientific";
            }
            return Constants.COLLATION_STANDARD;
        }
    }

    /* loaded from: classes.dex */
    public enum CompactDisplay {
        SHORT,
        LONG;

        @Override // java.lang.Enum
        public String toString() {
            int i = AnonymousClass1.$SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CompactDisplay[ordinal()];
            if (i != 1) {
                if (i == 2) {
                    return "long";
                }
                throw new IllegalArgumentException();
            }
            return "short";
        }
    }

    /* loaded from: classes.dex */
    public enum SignDisplay {
        AUTO,
        ALWAYS,
        NEVER,
        EXCEPTZERO;

        @Override // java.lang.Enum
        public String toString() {
            int i = AnonymousClass1.$SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$SignDisplay[ordinal()];
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i == 4) {
                            return "exceptZero";
                        }
                        throw new IllegalArgumentException();
                    }
                    return ReactScrollViewHelper.OVER_SCROLL_NEVER;
                }
                return ReactScrollViewHelper.OVER_SCROLL_ALWAYS;
            }
            return "auto";
        }
    }

    /* loaded from: classes.dex */
    public enum UnitDisplay {
        SHORT,
        NARROW,
        LONG;

        @Override // java.lang.Enum
        public String toString() {
            int i = AnonymousClass1.$SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$UnitDisplay[ordinal()];
            if (i != 1) {
                if (i != 2) {
                    if (i == 3) {
                        return "long";
                    }
                    throw new IllegalArgumentException();
                }
                return "narrow";
            }
            return "short";
        }

        public MeasureFormat.FormatWidth getFormatWidth() {
            int i = AnonymousClass1.$SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$UnitDisplay[ordinal()];
            if (i != 2) {
                if (i == 3) {
                    return MeasureFormat.FormatWidth.WIDE;
                }
                return MeasureFormat.FormatWidth.SHORT;
            }
            return MeasureFormat.FormatWidth.NARROW;
        }
    }

    /* loaded from: classes.dex */
    public enum CurrencyDisplay {
        SYMBOL,
        NARROWSYMBOL,
        CODE,
        NAME;

        @Override // java.lang.Enum
        public String toString() {
            int i = AnonymousClass1.$SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CurrencyDisplay[ordinal()];
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i == 4) {
                            return HintConstants.AUTOFILL_HINT_NAME;
                        }
                        throw new IllegalArgumentException();
                    }
                    return "code";
                }
                return "narrowSymbol";
            }
            return "symbol";
        }

        public int getNameStyle() {
            return AnonymousClass1.$SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CurrencyDisplay[ordinal()] != 4 ? 0 : 1;
        }
    }

    /* renamed from: com.facebook.hermes.intl.IPlatformNumberFormatter$1 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CompactDisplay;
        static final /* synthetic */ int[] $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CurrencyDisplay;
        static final /* synthetic */ int[] $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CurrencySign;
        static final /* synthetic */ int[] $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$Notation;
        static final /* synthetic */ int[] $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$SignDisplay;
        static final /* synthetic */ int[] $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$Style;
        static final /* synthetic */ int[] $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$UnitDisplay;

        static {
            int[] iArr = new int[CurrencySign.values().length];
            $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CurrencySign = iArr;
            try {
                iArr[CurrencySign.ACCOUNTING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CurrencySign[CurrencySign.STANDARD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            int[] iArr2 = new int[CurrencyDisplay.values().length];
            $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CurrencyDisplay = iArr2;
            try {
                iArr2[CurrencyDisplay.SYMBOL.ordinal()] = 1;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CurrencyDisplay[CurrencyDisplay.NARROWSYMBOL.ordinal()] = 2;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CurrencyDisplay[CurrencyDisplay.CODE.ordinal()] = 3;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CurrencyDisplay[CurrencyDisplay.NAME.ordinal()] = 4;
            } catch (NoSuchFieldError unused6) {
            }
            int[] iArr3 = new int[UnitDisplay.values().length];
            $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$UnitDisplay = iArr3;
            try {
                iArr3[UnitDisplay.SHORT.ordinal()] = 1;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$UnitDisplay[UnitDisplay.NARROW.ordinal()] = 2;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$UnitDisplay[UnitDisplay.LONG.ordinal()] = 3;
            } catch (NoSuchFieldError unused9) {
            }
            int[] iArr4 = new int[SignDisplay.values().length];
            $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$SignDisplay = iArr4;
            try {
                iArr4[SignDisplay.AUTO.ordinal()] = 1;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$SignDisplay[SignDisplay.ALWAYS.ordinal()] = 2;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$SignDisplay[SignDisplay.NEVER.ordinal()] = 3;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$SignDisplay[SignDisplay.EXCEPTZERO.ordinal()] = 4;
            } catch (NoSuchFieldError unused13) {
            }
            int[] iArr5 = new int[CompactDisplay.values().length];
            $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CompactDisplay = iArr5;
            try {
                iArr5[CompactDisplay.SHORT.ordinal()] = 1;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CompactDisplay[CompactDisplay.LONG.ordinal()] = 2;
            } catch (NoSuchFieldError unused15) {
            }
            int[] iArr6 = new int[Notation.values().length];
            $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$Notation = iArr6;
            try {
                iArr6[Notation.STANDARD.ordinal()] = 1;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$Notation[Notation.SCIENTIFIC.ordinal()] = 2;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$Notation[Notation.ENGINEERING.ordinal()] = 3;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$Notation[Notation.COMPACT.ordinal()] = 4;
            } catch (NoSuchFieldError unused19) {
            }
            int[] iArr7 = new int[Style.values().length];
            $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$Style = iArr7;
            try {
                iArr7[Style.DECIMAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$Style[Style.PERCENT.ordinal()] = 2;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$Style[Style.CURRENCY.ordinal()] = 3;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$Style[Style.UNIT.ordinal()] = 4;
            } catch (NoSuchFieldError unused23) {
            }
        }
    }

    /* loaded from: classes.dex */
    public enum CurrencySign {
        STANDARD,
        ACCOUNTING;

        @Override // java.lang.Enum
        public String toString() {
            int i = AnonymousClass1.$SwitchMap$com$facebook$hermes$intl$IPlatformNumberFormatter$CurrencySign[ordinal()];
            if (i != 1) {
                if (i == 2) {
                    return Constants.COLLATION_STANDARD;
                }
                throw new IllegalArgumentException();
            }
            return "accounting";
        }
    }
}
