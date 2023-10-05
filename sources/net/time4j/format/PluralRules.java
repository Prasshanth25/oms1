package net.time4j.format;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.time4j.base.ResourceLoader;

/* loaded from: classes2.dex */
public abstract class PluralRules {
    private static final PluralRules FALLBACK_CARDINAL_ENGLISH = new FallbackRules(NumberType.CARDINALS, true, null);
    private static final PluralRules FALLBACK_CARDINAL_OTHER = new FallbackRules(NumberType.CARDINALS, false, null);
    private static final PluralRules FALLBACK_ORDINAL_ENGLISH = new FallbackRules(NumberType.ORDINALS, true, null);
    private static final PluralRules FALLBACK_ORDINAL_OTHER = new FallbackRules(NumberType.ORDINALS, false, null);
    private static final Map<String, PluralRules> CARDINAL_MAP = new ConcurrentHashMap();
    private static final Map<String, PluralRules> ORDINAL_MAP = new ConcurrentHashMap();

    public abstract PluralCategory getCategory(long j);

    public abstract NumberType getNumberType();

    public static PluralRules of(Locale locale, NumberType numberType) {
        Map<String, PluralRules> ruleMap = getRuleMap(numberType);
        if (!ruleMap.isEmpty()) {
            r2 = locale.getCountry().equals("") ? null : ruleMap.get(toKey(locale));
            if (r2 == null) {
                r2 = ruleMap.get(locale.getLanguage());
            }
        }
        return r2 == null ? Holder.PROVIDER.load(locale, numberType) : r2;
    }

    public static void register(Locale locale, PluralRules pluralRules) {
        Map<String, PluralRules> ruleMap = getRuleMap(pluralRules.getNumberType());
        String language = locale.getLanguage();
        if (!locale.getCountry().equals("")) {
            language = toKey(locale);
        }
        ruleMap.put(language, pluralRules);
    }

    /* renamed from: net.time4j.format.PluralRules$1 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$time4j$format$NumberType;

        static {
            int[] iArr = new int[NumberType.values().length];
            $SwitchMap$net$time4j$format$NumberType = iArr;
            try {
                iArr[NumberType.CARDINALS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$net$time4j$format$NumberType[NumberType.ORDINALS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    private static Map<String, PluralRules> getRuleMap(NumberType numberType) {
        int i = AnonymousClass1.$SwitchMap$net$time4j$format$NumberType[numberType.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return ORDINAL_MAP;
            }
            throw new UnsupportedOperationException(numberType.name());
        }
        return CARDINAL_MAP;
    }

    private static String toKey(Locale locale) {
        return locale.getLanguage() + '_' + locale.getCountry();
    }

    /* loaded from: classes2.dex */
    private static class FallbackRules extends PluralRules {
        private final boolean english;
        private final NumberType numType;

        /* synthetic */ FallbackRules(NumberType numberType, boolean z, AnonymousClass1 anonymousClass1) {
            this(numberType, z);
        }

        private FallbackRules(NumberType numberType, boolean z) {
            this.numType = numberType;
            this.english = z;
        }

        @Override // net.time4j.format.PluralRules
        public PluralCategory getCategory(long j) {
            int i = AnonymousClass1.$SwitchMap$net$time4j$format$NumberType[this.numType.ordinal()];
            if (i == 1) {
                return j == 1 ? PluralCategory.ONE : PluralCategory.OTHER;
            } else if (i == 2) {
                if (this.english) {
                    long j2 = j % 10;
                    long j3 = j % 100;
                    if (j2 == 1 && j3 != 11) {
                        return PluralCategory.ONE;
                    }
                    if (j2 == 2 && j3 != 12) {
                        return PluralCategory.TWO;
                    }
                    if (j2 == 3 && j3 != 13) {
                        return PluralCategory.FEW;
                    }
                }
                return PluralCategory.OTHER;
            } else {
                throw new UnsupportedOperationException(this.numType.name());
            }
        }

        @Override // net.time4j.format.PluralRules
        public NumberType getNumberType() {
            return this.numType;
        }
    }

    /* loaded from: classes2.dex */
    private static class FallbackProvider implements PluralProvider {
        private FallbackProvider() {
        }

        /* synthetic */ FallbackProvider(AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // net.time4j.format.PluralProvider
        public PluralRules load(Locale locale, NumberType numberType) {
            boolean equals = locale.getLanguage().equals("en");
            int i = AnonymousClass1.$SwitchMap$net$time4j$format$NumberType[numberType.ordinal()];
            if (i == 1) {
                return equals ? PluralRules.FALLBACK_CARDINAL_ENGLISH : PluralRules.FALLBACK_CARDINAL_OTHER;
            } else if (i == 2) {
                return equals ? PluralRules.FALLBACK_ORDINAL_ENGLISH : PluralRules.FALLBACK_ORDINAL_OTHER;
            } else {
                throw new UnsupportedOperationException(numberType.name());
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class Holder {
        private static final PluralProvider PROVIDER;

        private Holder() {
        }

        static {
            Iterator it = ResourceLoader.getInstance().services(PluralProvider.class).iterator();
            PluralProvider pluralProvider = it.hasNext() ? (PluralProvider) it.next() : null;
            if (pluralProvider == null) {
                pluralProvider = new FallbackProvider(null);
            }
            PROVIDER = pluralProvider;
        }
    }
}
