package net.time4j.format;

import androidx.exifinterface.media.ExifInterface;
import java.io.IOException;
import net.time4j.base.MathUtils;

/* loaded from: classes2.dex */
public enum NumberSystem {
    ARABIC("latn") { // from class: net.time4j.format.NumberSystem.1
        @Override // net.time4j.format.NumberSystem
        public boolean contains(char c) {
            return c >= '0' && c <= '9';
        }

        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "0123456789";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return true;
        }

        @Override // net.time4j.format.NumberSystem
        public String toNumeral(int i) {
            if (i < 0) {
                throw new IllegalArgumentException("Cannot convert: " + i);
            }
            return Integer.toString(i);
        }

        @Override // net.time4j.format.NumberSystem
        public int toInteger(String str, Leniency leniency) {
            int parseInt = Integer.parseInt(str);
            if (parseInt >= 0) {
                return parseInt;
            }
            throw new NumberFormatException("Cannot convert negative number: " + str);
        }
    },
    ARABIC_INDIC("arab") { // from class: net.time4j.format.NumberSystem.2
        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "٠١٢٣٤٥٦٧٨٩";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return true;
        }
    },
    ARABIC_INDIC_EXT("arabext") { // from class: net.time4j.format.NumberSystem.3
        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "۰۱۲۳۴۵۶۷۸۹";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return true;
        }
    },
    BENGALI("beng") { // from class: net.time4j.format.NumberSystem.4
        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "০১২৩৪৫৬৭৮৯";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return true;
        }
    },
    DEVANAGARI("deva") { // from class: net.time4j.format.NumberSystem.5
        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "०१२३४५६७८९";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return true;
        }
    },
    DOZENAL("dozenal") { // from class: net.time4j.format.NumberSystem.6
        @Override // net.time4j.format.NumberSystem
        public boolean contains(char c) {
            return (c >= '0' && c <= '9') || c == 8586 || c == 8587;
        }

        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "0123456789↊↋";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return false;
        }

        @Override // net.time4j.format.NumberSystem
        public String toNumeral(int i) {
            if (i < 0) {
                throw new IllegalArgumentException("Cannot convert: " + i);
            }
            return Integer.toString(i, 12).replace('a', (char) 8586).replace('b', (char) 8587);
        }

        @Override // net.time4j.format.NumberSystem
        public int toNumeral(int i, Appendable appendable) throws IOException {
            if (i >= 0) {
                int i2 = 1;
                while (true) {
                    if (i2 > 4) {
                        i2 = 0;
                        break;
                    } else if (i < NumberSystem.D_FACTORS[i2]) {
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i2 > 0) {
                    int i3 = i2 - 1;
                    do {
                        int i4 = i / NumberSystem.D_FACTORS[i3];
                        appendable.append(i4 == 11 ? (char) 8587 : i4 == 10 ? (char) 8586 : (char) (i4 + 48));
                        i -= i4 * NumberSystem.D_FACTORS[i3];
                        i3--;
                    } while (i3 >= 0);
                    return i2;
                }
            }
            return super.toNumeral(i, appendable);
        }

        @Override // net.time4j.format.NumberSystem
        public int toInteger(String str, Leniency leniency) {
            int parseInt = Integer.parseInt(str.replace((char) 8586, 'a').replace((char) 8587, 'b'), 12);
            if (parseInt >= 0) {
                return parseInt;
            }
            throw new NumberFormatException("Cannot convert negative number: " + str);
        }
    },
    ETHIOPIC("ethiopic") { // from class: net.time4j.format.NumberSystem.7
        @Override // net.time4j.format.NumberSystem
        public boolean contains(char c) {
            return c >= 4969 && c <= 4988;
        }

        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "፩፪፫፬፭፮፯፰፱፲፳፴፵፶፷፸፹፺፻፼";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return false;
        }

        /* JADX WARN: Removed duplicated region for block: B:128:0x006e  */
        /* JADX WARN: Removed duplicated region for block: B:130:0x0073  */
        /* JADX WARN: Removed duplicated region for block: B:132:0x0078  */
        /* JADX WARN: Removed duplicated region for block: B:140:0x007b A[SYNTHETIC] */
        @Override // net.time4j.format.NumberSystem
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.lang.String toNumeral(int r11) {
            /*
                r10 = this;
                r0 = 1
                if (r11 < r0) goto L83
                java.lang.String r11 = java.lang.String.valueOf(r11)
                int r1 = r11.length()
                int r1 = r1 - r0
                int r2 = r1 % 2
                if (r2 != 0) goto L20
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                java.lang.String r3 = "0"
                r2.<init>(r3)
                r2.append(r11)
                java.lang.String r11 = r2.toString()
                int r1 = r1 + 1
            L20:
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                r3 = r1
            L26:
                if (r3 < 0) goto L7e
                int r4 = r1 - r3
                char r4 = r11.charAt(r4)
                int r3 = r3 + (-1)
                int r5 = r1 - r3
                char r5 = r11.charAt(r5)
                r6 = 48
                r7 = 0
                if (r5 == r6) goto L3f
                int r5 = r5 + 4920
                char r5 = (char) r5
                goto L40
            L3f:
                r5 = 0
            L40:
                if (r4 == r6) goto L46
                int r4 = r4 + 4929
                char r4 = (char) r4
                goto L47
            L46:
                r4 = 0
            L47:
                int r6 = r3 % 4
                int r6 = r6 / 2
                r8 = 4987(0x137b, float:6.988E-42)
                if (r3 == 0) goto L5b
                if (r6 == 0) goto L58
                if (r5 != 0) goto L55
                if (r4 == 0) goto L5b
            L55:
                r6 = 4987(0x137b, float:6.988E-42)
                goto L5c
            L58:
                r6 = 4988(0x137c, float:6.99E-42)
                goto L5c
            L5b:
                r6 = 0
            L5c:
                r9 = 4969(0x1369, float:6.963E-42)
                if (r5 != r9) goto L6b
                if (r4 != 0) goto L6b
                if (r1 <= r0) goto L6b
                if (r6 == r8) goto L6c
                int r8 = r3 + 1
                if (r8 != r1) goto L6b
                goto L6c
            L6b:
                r7 = r5
            L6c:
                if (r4 == 0) goto L71
                r2.append(r4)
            L71:
                if (r7 == 0) goto L76
                r2.append(r7)
            L76:
                if (r6 == 0) goto L7b
                r2.append(r6)
            L7b:
                int r3 = r3 + (-1)
                goto L26
            L7e:
                java.lang.String r11 = r2.toString()
                return r11
            L83:
                java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = "Can only convert positive numbers: "
                r1.<init>(r2)
                r1.append(r11)
                java.lang.String r11 = r1.toString()
                r0.<init>(r11)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: net.time4j.format.NumberSystem.AnonymousClass7.toNumeral(int):java.lang.String");
        }

        @Override // net.time4j.format.NumberSystem
        public int toInteger(String str, Leniency leniency) {
            int i;
            boolean z = false;
            boolean z2 = false;
            int i2 = 0;
            int i3 = 0;
            int i4 = 1;
            for (int length = str.length() - 1; length >= 0; length--) {
                char charAt = str.charAt(length);
                if (charAt >= 4969 && charAt < 4978) {
                    i = (charAt + 1) - 4969;
                } else if (charAt < 4978 || charAt >= 4987) {
                    if (charAt == 4988) {
                        if (z && i3 == 0) {
                            i3 = 1;
                        }
                        i2 = NumberSystem.addEthiopic(i2, i3, i4);
                        i4 = z ? i4 * 100 : i4 * 10000;
                        z = false;
                        z2 = true;
                    } else if (charAt == 4987) {
                        i2 = NumberSystem.addEthiopic(i2, i3, i4);
                        i4 *= 100;
                        z = true;
                        z2 = false;
                    }
                    i3 = 0;
                } else {
                    i = ((charAt + 1) - 4978) * 10;
                }
                i3 += i;
            }
            return NumberSystem.addEthiopic(i2, ((z || z2) && i3 == 0) ? 1 : i3, i4);
        }
    },
    GUJARATI("gujr") { // from class: net.time4j.format.NumberSystem.8
        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "૦૧૨૩૪૫૬૭૮૯";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return true;
        }
    },
    JAPANESE("jpan") { // from class: net.time4j.format.NumberSystem.9
        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "一二三四五六七八九十百千";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return false;
        }

        @Override // net.time4j.format.NumberSystem
        public String toNumeral(int i) {
            if (i < 1 || i > 9999) {
                throw new IllegalArgumentException("Cannot convert: " + i);
            }
            String digits = getDigits();
            int i2 = i / 1000;
            int i3 = i % 1000;
            int i4 = i3 / 100;
            int i5 = i3 % 100;
            int i6 = i5 / 10;
            int i7 = i5 % 10;
            StringBuilder sb = new StringBuilder();
            if (i2 >= 1) {
                if (i2 > 1) {
                    sb.append(digits.charAt(i2 - 1));
                }
                sb.append((char) 21315);
            }
            if (i4 >= 1) {
                if (i4 > 1) {
                    sb.append(digits.charAt(i4 - 1));
                }
                sb.append((char) 30334);
            }
            if (i6 >= 1) {
                if (i6 > 1) {
                    sb.append(digits.charAt(i6 - 1));
                }
                sb.append((char) 21313);
            }
            if (i7 > 0) {
                sb.append(digits.charAt(i7 - 1));
            }
            return sb.toString();
        }

        @Override // net.time4j.format.NumberSystem
        public int toInteger(String str, Leniency leniency) {
            boolean z;
            String digits = getDigits();
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            for (int length = str.length() - 1; length >= 0; length--) {
                char charAt = str.charAt(length);
                if (charAt != 21313) {
                    if (charAt != 21315) {
                        if (charAt != 30334) {
                            int i5 = 0;
                            while (true) {
                                if (i5 >= 9) {
                                    z = false;
                                    break;
                                } else if (digits.charAt(i5) == charAt) {
                                    int i6 = i5 + 1;
                                    if (i4 == 1) {
                                        i2 += i6 * 1000;
                                        i4 = -1;
                                    } else if (i3 == 1) {
                                        i2 += i6 * 100;
                                        i3 = -1;
                                    } else if (i == 1) {
                                        i2 += i6 * 10;
                                        i = -1;
                                    } else {
                                        i2 += i6;
                                    }
                                    z = true;
                                } else {
                                    i5++;
                                }
                            }
                            if (!z) {
                                throw new IllegalArgumentException("Invalid Japanese numeral: " + str);
                            }
                        } else if (i3 != 0 || i4 != 0) {
                            throw new IllegalArgumentException("Invalid Japanese numeral: " + str);
                        } else {
                            i3++;
                        }
                    } else if (i4 != 0) {
                        throw new IllegalArgumentException("Invalid Japanese numeral: " + str);
                    } else {
                        i4++;
                    }
                } else if (i != 0 || i3 != 0 || i4 != 0) {
                    throw new IllegalArgumentException("Invalid Japanese numeral: " + str);
                } else {
                    i++;
                }
            }
            if (i == 1) {
                i2 += 10;
            }
            if (i3 == 1) {
                i2 += 100;
            }
            return i4 == 1 ? i2 + 1000 : i2;
        }
    },
    KHMER("khmr") { // from class: net.time4j.format.NumberSystem.10
        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "០១២៣៤៥៦៧៨៩";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return true;
        }
    },
    MYANMAR("mymr") { // from class: net.time4j.format.NumberSystem.11
        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "၀၁၂၃၄၅၆၇၈၉";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return true;
        }
    },
    ORYA("orya") { // from class: net.time4j.format.NumberSystem.12
        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "୦୧୨୩୪୫୬୭୮୯";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return true;
        }
    },
    ROMAN("roman") { // from class: net.time4j.format.NumberSystem.13
        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "IVXLCDM";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return false;
        }

        @Override // net.time4j.format.NumberSystem
        public String toNumeral(int i) {
            if (i < 1 || i > 3999) {
                throw new IllegalArgumentException("Out of range (1-3999): " + i);
            }
            StringBuilder sb = new StringBuilder();
            for (int i2 = 0; i2 < NumberSystem.NUMBERS.length; i2++) {
                while (i >= NumberSystem.NUMBERS[i2]) {
                    sb.append(NumberSystem.LETTERS[i2]);
                    i -= NumberSystem.NUMBERS[i2];
                }
            }
            return sb.toString();
        }

        /* JADX WARN: Code restructure failed: missing block: B:229:0x0016, code lost:
            continue;
         */
        @Override // net.time4j.format.NumberSystem
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int toInteger(java.lang.String r11, net.time4j.format.Leniency r12) {
            /*
                Method dump skipped, instructions count: 218
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: net.time4j.format.NumberSystem.AnonymousClass13.toInteger(java.lang.String, net.time4j.format.Leniency):int");
        }

        @Override // net.time4j.format.NumberSystem
        public boolean contains(char c) {
            char upperCase = Character.toUpperCase(c);
            return upperCase == 'I' || upperCase == 'V' || upperCase == 'X' || upperCase == 'L' || upperCase == 'C' || upperCase == 'D' || upperCase == 'M';
        }
    },
    TELUGU("telu") { // from class: net.time4j.format.NumberSystem.14
        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "౦౧౨౩౪౫౬౭౮౯";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return true;
        }
    },
    THAI("thai") { // from class: net.time4j.format.NumberSystem.15
        @Override // net.time4j.format.NumberSystem
        public String getDigits() {
            return "๐๑๒๓๔๕๖๗๘๙";
        }

        @Override // net.time4j.format.NumberSystem
        public boolean isDecimal() {
            return true;
        }
    };
    
    private static final char ETHIOPIC_HUNDRED = 4987;
    private static final char ETHIOPIC_ONE = 4969;
    private static final char ETHIOPIC_TEN = 4978;
    private static final char ETHIOPIC_TEN_THOUSAND = 4988;
    private final String code;
    private static final int[] NUMBERS = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] LETTERS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, "IV", "I"};
    private static final int[] D_FACTORS = {1, 12, 144, 1728, 20736};

    public static boolean isValidRomanCombination(char c, char c2) {
        if (c == 'C') {
            return c2 == 'M' || c2 == 'D';
        } else if (c == 'I') {
            return c2 == 'X' || c2 == 'V';
        } else if (c != 'X') {
            return false;
        } else {
            return c2 == 'C' || c2 == 'L';
        }
    }

    NumberSystem(String str) {
        this.code = str;
    }

    public String toNumeral(int i) {
        if (isDecimal() && i >= 0) {
            int charAt = getDigits().charAt(0) - '0';
            String num = Integer.toString(i);
            StringBuilder sb = new StringBuilder();
            int length = num.length();
            for (int i2 = 0; i2 < length; i2++) {
                sb.append((char) (num.charAt(i2) + charAt));
            }
            return sb.toString();
        }
        throw new IllegalArgumentException("Cannot convert: " + i);
    }

    public int toNumeral(int i, Appendable appendable) throws IOException {
        String numeral = toNumeral(i);
        appendable.append(numeral);
        return numeral.length();
    }

    public final int toInteger(String str) {
        return toInteger(str, Leniency.SMART);
    }

    public int toInteger(String str, Leniency leniency) {
        if (isDecimal()) {
            int charAt = getDigits().charAt(0) - '0';
            StringBuilder sb = new StringBuilder();
            int length = str.length();
            for (int i = 0; i < length; i++) {
                sb.append((char) (str.charAt(i) - charAt));
            }
            int parseInt = Integer.parseInt(sb.toString());
            if (parseInt >= 0) {
                return parseInt;
            }
            throw new NumberFormatException("Cannot convert negative number: " + str);
        }
        throw new NumberFormatException("Cannot convert: " + str);
    }

    public boolean contains(char c) {
        String digits = getDigits();
        int length = digits.length();
        for (int i = 0; i < length; i++) {
            if (digits.charAt(i) == c) {
                return true;
            }
        }
        return false;
    }

    public String getDigits() {
        throw new AbstractMethodError();
    }

    public boolean isDecimal() {
        throw new AbstractMethodError();
    }

    public String getCode() {
        return this.code;
    }

    public static int addEthiopic(int i, int i2, int i3) {
        return MathUtils.safeAdd(i, MathUtils.safeMultiply(i2, i3));
    }

    public static int getValue(char c) {
        if (c != 'C') {
            if (c != 'D') {
                if (c != 'I') {
                    if (c != 'V') {
                        if (c != 'X') {
                            if (c != 'L') {
                                if (c == 'M') {
                                    return 1000;
                                }
                                throw new NumberFormatException("Invalid Roman digit: " + c);
                            }
                            return 50;
                        }
                        return 10;
                    }
                    return 5;
                }
                return 1;
            }
            return 500;
        }
        return 100;
    }
}
