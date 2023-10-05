package net.time4j.calendar;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import kotlin.text.Typography;
import net.time4j.PlainDate;
import net.time4j.engine.AttributeKey;
import net.time4j.engine.AttributeQuery;
import net.time4j.engine.CalendarEra;
import net.time4j.engine.ChronoCondition;
import net.time4j.engine.ChronoDisplay;
import net.time4j.engine.ChronoException;
import net.time4j.engine.EpochDays;
import net.time4j.format.Attributes;
import net.time4j.format.CalendarText;
import net.time4j.format.TextElement;
import net.time4j.format.TextWidth;

/* loaded from: classes2.dex */
public final class Nengo implements CalendarEra, Serializable {
    private static final Map<String, Nengo> CHINESE_TO_NENGO;
    private static final byte COURT_NORTHERN = 1;
    private static final byte COURT_SOUTHERN = -1;
    private static final byte COURT_STANDARD = 0;
    public static final Nengo HEISEI;
    private static final Map<String, Nengo> KANJI_TO_NENGO;
    private static final TST KOREAN_TO_NENGO;
    public static final Nengo MEIJI;
    private static final String[] MODERN_KEYS;
    private static final Nengo[] MODERN_NENGOS;
    private static final Nengo NENGO_KENMU;
    private static final Nengo NENGO_OEI;
    public static final Nengo NEWEST;
    private static final String NEW_ERA_PROPERTY = "net.time4j.calendar.japanese.supplemental.era";
    private static final Nengo[] NORTHERN_NENGOS;
    private static final Nengo[] OFFICIAL_NENGOS;
    public static final Nengo REIWA;
    private static final TST ROMAJI_TO_NENGO;
    private static final TST RUSSIAN_TO_NENGO;
    public static final AttributeKey<Selector> SELECTOR;
    public static final Nengo SHOWA;
    public static final Nengo TAISHO;
    private static final long serialVersionUID = 5696395761628504723L;
    private final transient String chinese;
    private final byte court;
    private final int index;
    private final transient String kanji;
    private final transient String korean;
    private final transient int relgregyear;
    private final transient String romaji;
    private final transient String russian;
    private final transient long start;

    /* loaded from: classes2.dex */
    public enum Selector implements ChronoCondition<Nengo> {
        OFFICIAL { // from class: net.time4j.calendar.Nengo.Selector.1
            @Override // net.time4j.engine.ChronoCondition
            public boolean test(Nengo nengo) {
                return nengo.court != 1;
            }
        },
        MODERN { // from class: net.time4j.calendar.Nengo.Selector.2
            @Override // net.time4j.engine.ChronoCondition
            public boolean test(Nengo nengo) {
                return nengo.index >= Nengo.MEIJI.index;
            }
        },
        EDO_PERIOD { // from class: net.time4j.calendar.Nengo.Selector.3
            @Override // net.time4j.engine.ChronoCondition
            public boolean test(Nengo nengo) {
                return nengo.relgregyear >= 1603 && nengo.relgregyear < 1868;
            }
        },
        AZUCHI_MOMOYAMA_PERIOD { // from class: net.time4j.calendar.Nengo.Selector.4
            @Override // net.time4j.engine.ChronoCondition
            public boolean test(Nengo nengo) {
                return nengo.relgregyear >= 1573 && nengo.relgregyear < 1603;
            }
        },
        MUROMACHI_PERIOD { // from class: net.time4j.calendar.Nengo.Selector.5
            @Override // net.time4j.engine.ChronoCondition
            public boolean test(Nengo nengo) {
                return nengo.relgregyear >= 1336 && nengo.relgregyear < 1573 && nengo.court != 1;
            }
        },
        NORTHERN_COURT { // from class: net.time4j.calendar.Nengo.Selector.6
            @Override // net.time4j.engine.ChronoCondition
            public boolean test(Nengo nengo) {
                return nengo.court == 1;
            }
        },
        SOUTHERN_COURT { // from class: net.time4j.calendar.Nengo.Selector.7
            @Override // net.time4j.engine.ChronoCondition
            public boolean test(Nengo nengo) {
                return nengo.court == -1;
            }
        },
        KAMAKURA_PERIOD { // from class: net.time4j.calendar.Nengo.Selector.8
            @Override // net.time4j.engine.ChronoCondition
            public boolean test(Nengo nengo) {
                return nengo.relgregyear >= 1185 && nengo.relgregyear < 1332;
            }
        },
        HEIAN_PERIOD { // from class: net.time4j.calendar.Nengo.Selector.9
            @Override // net.time4j.engine.ChronoCondition
            public boolean test(Nengo nengo) {
                return nengo.relgregyear >= 794 && nengo.relgregyear < 1185;
            }
        },
        NARA_PERIOD { // from class: net.time4j.calendar.Nengo.Selector.10
            @Override // net.time4j.engine.ChronoCondition
            public boolean test(Nengo nengo) {
                return nengo.relgregyear >= 710 && nengo.relgregyear < 794;
            }
        },
        ASUKA_PERIOD { // from class: net.time4j.calendar.Nengo.Selector.11
            @Override // net.time4j.engine.ChronoCondition
            public boolean test(Nengo nengo) {
                return nengo.relgregyear >= 538 && nengo.relgregyear < 710;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:389:0x0110, code lost:
        if (r14.relgregyear != r8) goto L117;
     */
    /* JADX WARN: Removed duplicated region for block: B:419:0x01a0  */
    /* JADX WARN: Removed duplicated region for block: B:472:0x02ed  */
    static {
        /*
            Method dump skipped, instructions count: 887
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: net.time4j.calendar.Nengo.<clinit>():void");
    }

    private Nengo(int i, long j, String str, String str2, String str3, String str4, String str5, byte b, int i2) {
        if (str.isEmpty()) {
            throw new IllegalArgumentException("Missing kanji.");
        }
        if (str5.isEmpty()) {
            throw new IllegalArgumentException("Missing latin transcription.");
        }
        if (b > 1 || b < -1) {
            throw new IllegalArgumentException("Undefined court byte: " + ((int) b));
        }
        this.relgregyear = i;
        this.start = j;
        this.kanji = str;
        this.chinese = str2;
        this.korean = str3;
        this.russian = str4;
        this.romaji = str5;
        this.court = b;
        this.index = i2;
    }

    public static Nengo ofRelatedGregorianYear(int i) {
        return ofRelatedGregorianYear(i, Selector.OFFICIAL);
    }

    public static Nengo ofRelatedGregorianYear(int i, Selector selector) {
        Nengo nengo;
        Nengo nengo2;
        Nengo nengo3 = null;
        if (i >= 701) {
            int i2 = AnonymousClass1.$SwitchMap$net$time4j$calendar$Nengo$Selector[selector.ordinal()];
            if (i2 != 1) {
                if (i2 == 2) {
                    int lowerBound = getLowerBound(selector);
                    for (int length = OFFICIAL_NENGOS.length - 1; length >= lowerBound; length--) {
                        nengo = OFFICIAL_NENGOS[length];
                        if (nengo.relgregyear <= i) {
                            nengo3 = nengo;
                            break;
                        }
                    }
                } else if (i2 != 3) {
                    if (i2 != 4) {
                        int lowerBound2 = getLowerBound(selector);
                        int upperBound = getUpperBound(selector);
                        Nengo[] nengoArr = OFFICIAL_NENGOS;
                        if (i >= nengoArr[lowerBound2].relgregyear && i <= nengoArr[upperBound + 1].relgregyear) {
                            while (upperBound >= lowerBound2) {
                                nengo = OFFICIAL_NENGOS[upperBound];
                                if (nengo.relgregyear <= i) {
                                    nengo3 = nengo;
                                    break;
                                }
                                upperBound--;
                            }
                        }
                    } else if (i >= 1334 && i <= 1393) {
                        int i3 = NENGO_OEI.index - 1;
                        while (true) {
                            nengo2 = OFFICIAL_NENGOS[i3];
                            if (nengo2.court != -1) {
                                break;
                            } else if (nengo2.relgregyear <= i) {
                                break;
                            } else {
                                i3--;
                            }
                        }
                        nengo3 = nengo2;
                    }
                } else if (i >= 1332 && i <= 1394) {
                    for (int length2 = NORTHERN_NENGOS.length - 1; length2 >= 0; length2--) {
                        nengo2 = NORTHERN_NENGOS[length2];
                        if (nengo2.relgregyear <= i) {
                            nengo3 = nengo2;
                        }
                    }
                }
            } else if (i >= 1873) {
                return ofRelatedGregorianYear(i, Selector.MODERN);
            } else {
                int length3 = OFFICIAL_NENGOS.length - 1;
                int i4 = 0;
                while (i4 <= length3) {
                    int i5 = (i4 + length3) >> 1;
                    if (OFFICIAL_NENGOS[i5].getFirstRelatedGregorianYear() <= i) {
                        i4 = i5 + 1;
                    } else {
                        length3 = i5 - 1;
                    }
                }
                if (i4 != 0) {
                    return OFFICIAL_NENGOS[i4 - 1];
                }
            }
        }
        if (nengo3 != null) {
            return nengo3;
        }
        throw new IllegalArgumentException("Could not find nengo for year=" + i + ", selector=" + selector + ".");
    }

    public static Nengo ofKanji(String str) {
        Nengo nengo = KANJI_TO_NENGO.get(str);
        if (nengo != null) {
            return nengo;
        }
        throw new IllegalArgumentException("Could not find any nengo for Japanese kanji: " + str);
    }

    public static List<Nengo> parseRomaji(String str) {
        String hepburn = hepburn(str, 0);
        TST tst = ROMAJI_TO_NENGO;
        return tst.find(tst.longestPrefixOf(hepburn, 0));
    }

    public static List<Nengo> list() {
        return list(Selector.OFFICIAL);
    }

    public static List<Nengo> list(Selector selector) {
        List asList;
        int i = AnonymousClass1.$SwitchMap$net$time4j$calendar$Nengo$Selector[selector.ordinal()];
        if (i == 1) {
            asList = Arrays.asList(OFFICIAL_NENGOS);
        } else if (i == 3) {
            asList = Arrays.asList(NORTHERN_NENGOS);
        } else {
            int lowerBound = getLowerBound(selector);
            int upperBound = getUpperBound(selector);
            asList = new ArrayList((upperBound - lowerBound) + 1);
            while (lowerBound <= upperBound) {
                asList.add(OFFICIAL_NENGOS[lowerBound]);
                lowerBound++;
            }
        }
        return Collections.unmodifiableList(asList);
    }

    public boolean matches(Selector selector) {
        return selector.test(this);
    }

    public int getFirstRelatedGregorianYear() {
        return this.relgregyear;
    }

    public PlainDate getStart() {
        return PlainDate.of(this.start, EpochDays.UTC);
    }

    public boolean isModern() {
        return this.index >= MEIJI.index;
    }

    public String getDisplayName(Locale locale) {
        return getDisplayName(locale, TextWidth.WIDE);
    }

    public String getDisplayName(Locale locale, TextWidth textWidth) {
        String str;
        if (locale.getLanguage().isEmpty()) {
            return this.romaji;
        }
        int i = this.index;
        if (i < MEIJI.index || i > NEWEST.index || locale.getLanguage().equals("ru")) {
            if (locale.getLanguage().equals("ja")) {
                return this.kanji;
            }
            if (locale.getLanguage().equals("zh")) {
                return this.chinese;
            }
            if (locale.getLanguage().equals("ko")) {
                return this.korean;
            }
            if (locale.getLanguage().equals("ru")) {
                return "Период " + this.russian;
            }
            return this.romaji;
        }
        int i2 = 0;
        while (true) {
            Nengo[] nengoArr = MODERN_NENGOS;
            if (i2 >= nengoArr.length) {
                str = null;
                break;
            } else if (equals(nengoArr[i2])) {
                str = MODERN_KEYS[i2];
                break;
            } else {
                i2++;
            }
        }
        if (str == null) {
            throw new IllegalStateException("Modern nengos need an update.");
        }
        if (textWidth == TextWidth.NARROW) {
            str = str + "_n";
        }
        return CalendarText.getInstance("japanese", locale).getTextForms().get(str);
    }

    public Nengo findNext() {
        if (this.court == 1) {
            int i = this.index;
            Nengo[] nengoArr = NORTHERN_NENGOS;
            if (i == nengoArr.length - 1) {
                return NENGO_OEI;
            }
            return nengoArr[i + 1];
        }
        int i2 = this.index;
        Nengo[] nengoArr2 = OFFICIAL_NENGOS;
        if (i2 == nengoArr2.length - 1) {
            return null;
        }
        return nengoArr2[i2 + 1];
    }

    public Nengo findPrevious() {
        if (this.court == 1) {
            int i = this.index;
            if (i == 0) {
                return OFFICIAL_NENGOS[NENGO_KENMU.index - 1];
            }
            return NORTHERN_NENGOS[i - 1];
        }
        int i2 = this.index;
        if (i2 == 0) {
            return null;
        }
        return OFFICIAL_NENGOS[i2 - 1];
    }

    @Override // net.time4j.engine.CalendarEra
    public String name() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.romaji);
        sb.append(" (");
        Nengo findNext = findNext();
        if (findNext != null) {
            sb.append(this.relgregyear);
            sb.append('-');
            sb.append(findNext.relgregyear);
        } else {
            sb.append("since ");
            sb.append(this.relgregyear);
        }
        sb.append(')');
        return sb.toString();
    }

    public int getValue() {
        int i;
        int i2;
        if (matches(Selector.NORTHERN_COURT)) {
            i = (this.index - NORTHERN_NENGOS.length) + NENGO_OEI.index;
            i2 = SHOWA.index;
        } else {
            i = this.index;
            i2 = SHOWA.index;
        }
        return (i - i2) + 1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Nengo) {
            Nengo nengo = (Nengo) obj;
            return this.relgregyear == nengo.relgregyear && this.start == nengo.start && this.kanji.equals(nengo.kanji) && this.romaji.equals(nengo.romaji) && this.court == nengo.court;
        }
        return false;
    }

    public int hashCode() {
        long j = this.start;
        return (int) (j ^ (j >>> 32));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.romaji);
        sb.append(' ');
        sb.append(this.kanji);
        sb.append(' ');
        Nengo findNext = findNext();
        if (findNext != null) {
            sb.append(this.relgregyear);
            sb.append('-');
            sb.append(findNext.relgregyear);
        } else {
            sb.append("since ");
            sb.append(this.relgregyear);
        }
        if (this.court != 0) {
            sb.append(" (");
            sb.append(this.court == 1 ? 'N' : 'S');
            sb.append(')');
        }
        return sb.toString();
    }

    public long getStartAsDaysSinceEpochUTC() {
        return this.start;
    }

    public int getIndexOfficial() {
        return this.index;
    }

    public static Nengo ofIndexOfficial(int i) {
        return OFFICIAL_NENGOS[i];
    }

    static String hepburn(CharSequence charSequence, int i) {
        int min = Math.min(charSequence.length(), i + 32);
        StringBuilder sb = null;
        for (int i2 = i; i2 < min; i2++) {
            char charAt = charSequence.charAt(i2);
            char c = 362;
            char c2 = 363;
            char c3 = 332;
            char c4 = 333;
            if (i2 == i) {
                if (charAt != 212 && charAt != 244 && charAt != 333) {
                    c3 = Character.toUpperCase(charAt);
                }
                if (charAt != 219 && charAt != 251 && charAt != 363) {
                    c = c3;
                }
            } else {
                if (charAt != 212 && charAt != 244 && charAt != 332) {
                    c4 = Character.toLowerCase(charAt);
                }
                if (charAt != 219 && charAt != 251 && charAt != 362) {
                    c2 = c4;
                }
                c = c2;
            }
            if (charAt == '\'') {
                c = Typography.rightSingleQuote;
            }
            if (charAt == ' ') {
                c = '-';
            }
            if (sb != null || c != charAt) {
                if (sb == null) {
                    sb = new StringBuilder(32);
                    sb.append(charSequence.subSequence(i, i2));
                }
                sb.append(c);
            }
        }
        return sb == null ? charSequence.subSequence(i, min).toString() : sb.toString();
    }

    public static String capitalize(CharSequence charSequence, int i) {
        int min = Math.min(charSequence.length(), i + 32);
        StringBuilder sb = null;
        int i2 = i;
        boolean z = true;
        while (i2 < min) {
            char charAt = charSequence.charAt(i2);
            char upperCase = z ? Character.toUpperCase(charAt) : Character.toLowerCase(charAt);
            boolean z2 = charAt == ' ';
            if (sb != null || upperCase != charAt) {
                if (sb == null) {
                    sb = new StringBuilder(32);
                    sb.append(charSequence.subSequence(i, i2));
                }
                sb.append(upperCase);
            }
            i2++;
            z = z2;
        }
        return sb == null ? charSequence.subSequence(i, min).toString() : sb.toString();
    }

    private static int getUpperBound(Selector selector) {
        switch (selector) {
            case NORTHERN_COURT:
                return NORTHERN_NENGOS.length - 1;
            case SOUTHERN_COURT:
                return NENGO_KENMU.index + 8;
            case EDO_PERIOD:
                return MEIJI.index - 1;
            case AZUCHI_MOMOYAMA_PERIOD:
                return 187;
            case MUROMACHI_PERIOD:
                return 184;
            case KAMAKURA_PERIOD:
                return NENGO_KENMU.index - 1;
            case HEIAN_PERIOD:
                return 102;
            case NARA_PERIOD:
                return 14;
            case ASUKA_PERIOD:
                return 2;
            default:
                return OFFICIAL_NENGOS.length - 1;
        }
    }

    private static int getLowerBound(Selector selector) {
        switch (selector) {
            case MODERN:
                return MEIJI.index;
            case NORTHERN_COURT:
            default:
                return 0;
            case SOUTHERN_COURT:
                return NENGO_KENMU.index;
            case EDO_PERIOD:
                return 188;
            case AZUCHI_MOMOYAMA_PERIOD:
                return 185;
            case MUROMACHI_PERIOD:
                return NENGO_KENMU.index + 1;
            case KAMAKURA_PERIOD:
                return 103;
            case HEIAN_PERIOD:
                return 15;
            case NARA_PERIOD:
                return 3;
        }
    }

    private static Nengo of(int i, boolean z) {
        return z ? NORTHERN_NENGOS[i] : OFFICIAL_NENGOS[i];
    }

    private Object readResolve() throws ObjectStreamException {
        try {
            int i = this.index;
            boolean z = true;
            if (this.court != 1) {
                z = false;
            }
            return of(i, z);
        } catch (ArrayIndexOutOfBoundsException unused) {
            throw new StreamCorruptedException();
        }
    }

    /* loaded from: classes2.dex */
    public static class Element implements TextElement<Nengo>, Serializable {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        static final Element SINGLETON = new Element();
        private static final long serialVersionUID = -1099321098836107792L;

        @Override // net.time4j.engine.ChronoElement
        public char getSymbol() {
            return 'G';
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

        @Override // net.time4j.engine.ChronoElement
        public String name() {
            return "ERA";
        }

        private Element() {
        }

        @Override // net.time4j.format.TextElement
        public void print(ChronoDisplay chronoDisplay, Appendable appendable, AttributeQuery attributeQuery) throws IOException, ChronoException {
            appendable.append(((Nengo) chronoDisplay.get(this)).getDisplayName((Locale) attributeQuery.get(Attributes.LANGUAGE, Locale.ROOT), (TextWidth) attributeQuery.get(Attributes.TEXT_WIDTH, TextWidth.WIDE)));
        }

        /* JADX WARN: Removed duplicated region for block: B:318:0x0189 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:356:0x0223 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:357:0x0224  */
        @Override // net.time4j.format.TextElement
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public net.time4j.calendar.Nengo parse(java.lang.CharSequence r17, java.text.ParsePosition r18, net.time4j.engine.AttributeQuery r19) {
            /*
                Method dump skipped, instructions count: 553
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: net.time4j.calendar.Nengo.Element.parse(java.lang.CharSequence, java.text.ParsePosition, net.time4j.engine.AttributeQuery):net.time4j.calendar.Nengo");
        }

        @Override // net.time4j.engine.ChronoElement
        public Class<Nengo> getType() {
            return Nengo.class;
        }

        @Override // java.util.Comparator
        public int compare(ChronoDisplay chronoDisplay, ChronoDisplay chronoDisplay2) {
            Nengo nengo = (Nengo) chronoDisplay.get(this);
            Nengo nengo2 = (Nengo) chronoDisplay2.get(this);
            if (nengo.start < nengo2.start) {
                return -1;
            }
            if (nengo.start > nengo2.start) {
                return 1;
            }
            return nengo.court == 1 ? nengo2.court == 1 ? 0 : 1 : nengo2.court == 1 ? -1 : 0;
        }

        @Override // net.time4j.engine.ChronoElement
        public Nengo getDefaultMinimum() {
            return Nengo.OFFICIAL_NENGOS[0];
        }

        @Override // net.time4j.engine.ChronoElement
        public Nengo getDefaultMaximum() {
            return Nengo.OFFICIAL_NENGOS[Nengo.OFFICIAL_NENGOS.length - 1];
        }

        @Override // net.time4j.engine.ChronoElement
        public String getDisplayName(Locale locale) {
            String str = CalendarText.getIsoInstance(locale).getTextForms().get("L_era");
            return str == null ? name() : str;
        }

        private Object readResolve() throws ObjectStreamException {
            return SINGLETON;
        }
    }

    /* loaded from: classes2.dex */
    public static class TST {
        private Node root;

        private TST() {
            this.root = null;
        }

        List<Nengo> find(String str) {
            if (str == null || str.length() == 0) {
                return Collections.emptyList();
            }
            Node find = find(this.root, str, 0);
            if (find == null) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableList(find.nengos);
        }

        private static Node find(Node node, String str, int i) {
            if (node == null) {
                return null;
            }
            char charAt = str.charAt(i);
            if (charAt < node.c) {
                return find(node.left, str, i);
            }
            if (charAt > node.c) {
                return find(node.right, str, i);
            }
            return i < str.length() + (-1) ? find(node.mid, str, i + 1) : node;
        }

        void insert(String str, Nengo nengo) {
            if (str.isEmpty()) {
                throw new IllegalArgumentException("Empty key cannot be inserted.");
            }
            this.root = insert(this.root, str, nengo, 0);
        }

        private static Node insert(Node node, String str, Nengo nengo, int i) {
            char charAt = str.charAt(i);
            if (node == null) {
                node = new Node();
                node.c = charAt;
            }
            if (charAt < node.c) {
                node.left = insert(node.left, str, nengo, i);
            } else if (charAt <= node.c) {
                if (i < str.length() - 1) {
                    node.mid = insert(node.mid, str, nengo, i + 1);
                } else {
                    if (node.nengos == null) {
                        node.nengos = new ArrayList();
                    }
                    node.nengos.add(nengo);
                }
            } else {
                node.right = insert(node.right, str, nengo, i);
            }
            return node;
        }

        String longestPrefixOf(CharSequence charSequence, int i) {
            Node node = this.root;
            int length = charSequence.length();
            int i2 = i;
            int i3 = i2;
            while (node != null && i2 < length) {
                char charAt = charSequence.charAt(i2);
                if (charAt < node.c) {
                    node = node.left;
                } else if (charAt > node.c) {
                    node = node.right;
                } else {
                    i2++;
                    if (node.nengos != null) {
                        i3 = i2;
                    }
                    node = node.mid;
                }
            }
            if (i >= i3) {
                return null;
            }
            return charSequence.subSequence(i, i3).toString();
        }
    }

    /* loaded from: classes2.dex */
    public static class Node {
        private char c;
        private Node left;
        private Node mid;
        private List<Nengo> nengos;
        private Node right;

        private Node() {
            this.c = (char) 0;
            this.left = null;
            this.mid = null;
            this.right = null;
            this.nengos = null;
        }
    }
}
