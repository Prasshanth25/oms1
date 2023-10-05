package net.time4j.format.expert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.time4j.base.UnixTime;
import net.time4j.engine.AttributeQuery;
import net.time4j.engine.ChronoDisplay;
import net.time4j.engine.ChronoElement;
import net.time4j.format.Attributes;
import net.time4j.format.Leniency;
import net.time4j.format.expert.ZoneLabels;
import net.time4j.tz.NameStyle;
import net.time4j.tz.TZID;
import net.time4j.tz.Timezone;
import net.time4j.tz.ZonalOffset;

/* loaded from: classes2.dex */
public final class TimezoneNameProcessor implements FormatProcessor<TZID> {
    private static final ConcurrentMap<Locale, TZNames> CACHE_ABBREVIATIONS = new ConcurrentHashMap();
    private static final ConcurrentMap<Locale, TZNames> CACHE_ZONENAMES = new ConcurrentHashMap();
    private static final String DEFAULT_PROVIDER = "DEFAULT";
    private static final int MAX = 25;
    private final boolean abbreviated;
    private final FormatProcessor<TZID> fallback;
    private final Leniency lenientMode;
    private final Locale locale;
    private final Set<TZID> preferredZones;
    private final int protectedLength;

    @Override // net.time4j.format.expert.FormatProcessor
    public boolean isNumerical() {
        return false;
    }

    @Override // net.time4j.format.expert.FormatProcessor
    public FormatProcessor<TZID> withElement(ChronoElement<TZID> chronoElement) {
        return this;
    }

    public TimezoneNameProcessor(boolean z) {
        this.abbreviated = z;
        this.fallback = new LocalizedGMTProcessor(z);
        this.preferredZones = null;
        this.lenientMode = Leniency.SMART;
        this.locale = Locale.ROOT;
        this.protectedLength = 0;
    }

    public TimezoneNameProcessor(boolean z, Set<TZID> set) {
        this.abbreviated = z;
        this.fallback = new LocalizedGMTProcessor(z);
        this.preferredZones = Collections.unmodifiableSet(new LinkedHashSet(set));
        this.lenientMode = Leniency.SMART;
        this.locale = Locale.ROOT;
        this.protectedLength = 0;
    }

    private TimezoneNameProcessor(boolean z, FormatProcessor<TZID> formatProcessor, Set<TZID> set, Leniency leniency, Locale locale, int i) {
        this.abbreviated = z;
        this.fallback = formatProcessor;
        this.preferredZones = set;
        this.lenientMode = leniency;
        this.locale = locale;
        this.protectedLength = i;
    }

    @Override // net.time4j.format.expert.FormatProcessor
    public int print(ChronoDisplay chronoDisplay, Appendable appendable, AttributeQuery attributeQuery, Set<ElementPosition> set, boolean z) throws IOException {
        if (!chronoDisplay.hasTimezone()) {
            throw new IllegalArgumentException("Cannot extract timezone name from: " + chronoDisplay);
        }
        TZID timezone = chronoDisplay.getTimezone();
        if (timezone instanceof ZonalOffset) {
            return this.fallback.print(chronoDisplay, appendable, attributeQuery, set, z);
        }
        if (chronoDisplay instanceof UnixTime) {
            Timezone of = Timezone.of(timezone);
            String displayName = of.getDisplayName(getStyle(of.isDaylightSaving((UnixTime) UnixTime.class.cast(chronoDisplay))), z ? this.locale : (Locale) attributeQuery.get(Attributes.LANGUAGE, Locale.ROOT));
            int length = appendable instanceof CharSequence ? ((CharSequence) appendable).length() : -1;
            appendable.append(displayName);
            int length2 = displayName.length();
            if (length != -1 && length2 > 0 && set != null) {
                set.add(new ElementPosition(TimezoneElement.TIMEZONE_ID, length, length + length2));
            }
            return length2;
        }
        throw new IllegalArgumentException("Cannot extract timezone name from: " + chronoDisplay);
    }

    /* JADX WARN: Code restructure failed: missing block: B:321:0x020f, code lost:
        if (r4 != false) goto L88;
     */
    /* JADX WARN: Type inference failed for: r11v2 */
    /* JADX WARN: Type inference failed for: r11v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r11v4 */
    @Override // net.time4j.format.expert.FormatProcessor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void parse(java.lang.CharSequence r19, net.time4j.format.expert.ParseLog r20, net.time4j.engine.AttributeQuery r21, net.time4j.format.expert.ParsedEntity<?> r22, boolean r23) {
        /*
            Method dump skipped, instructions count: 634
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: net.time4j.format.expert.TimezoneNameProcessor.parse(java.lang.CharSequence, net.time4j.format.expert.ParseLog, net.time4j.engine.AttributeQuery, net.time4j.format.expert.ParsedEntity, boolean):void");
    }

    @Override // net.time4j.format.expert.FormatProcessor
    public ChronoElement<TZID> getElement() {
        return TimezoneElement.TIMEZONE_ID;
    }

    @Override // net.time4j.format.expert.FormatProcessor
    public FormatProcessor<TZID> quickPath(ChronoFormatter<?> chronoFormatter, AttributeQuery attributeQuery, int i) {
        return new TimezoneNameProcessor(this.abbreviated, this.fallback, this.preferredZones, (Leniency) attributeQuery.get(Attributes.LENIENCY, Leniency.SMART), (Locale) attributeQuery.get(Attributes.LANGUAGE, Locale.ROOT), ((Integer) attributeQuery.get(Attributes.PROTECTED_CHARACTERS, 0)).intValue());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TimezoneNameProcessor) {
            TimezoneNameProcessor timezoneNameProcessor = (TimezoneNameProcessor) obj;
            if (this.abbreviated == timezoneNameProcessor.abbreviated) {
                Set<TZID> set = this.preferredZones;
                Set<TZID> set2 = timezoneNameProcessor.preferredZones;
                if (set == null) {
                    if (set2 == null) {
                        return true;
                    }
                } else if (set.equals(set2)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        Set<TZID> set = this.preferredZones;
        return (set == null ? 0 : set.hashCode()) + (this.abbreviated ? 1 : 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append(getClass().getName());
        sb.append("[abbreviated=");
        sb.append(this.abbreviated);
        sb.append(", preferredZones=");
        sb.append(this.preferredZones);
        sb.append(']');
        return sb.toString();
    }

    private String extractRelevantKey(CharSequence charSequence, int i, int i2) {
        StringBuilder sb = new StringBuilder();
        for (int i3 = i; i3 < i2; i3++) {
            char charAt = charSequence.charAt(i3);
            if (!Character.isLetter(charAt) && (this.abbreviated || i3 <= i || Character.isDigit(charAt))) {
                break;
            }
            sb.append(charAt);
        }
        return sb.toString().trim();
    }

    private ZoneLabels createZoneNames(Locale locale, boolean z) {
        NameStyle style = getStyle(z);
        ZoneLabels.Node node = null;
        for (TZID tzid : Timezone.getAvailableIDs()) {
            String displayName = Timezone.getDisplayName(tzid, style, locale);
            if (!displayName.equals(tzid.canonical())) {
                node = ZoneLabels.insert(node, displayName, tzid);
            }
        }
        return new ZoneLabels(node);
    }

    private static List<TZID> excludeWinZones(List<TZID> list) {
        if (list.size() > 1) {
            ArrayList arrayList = new ArrayList(list);
            int size = list.size();
            for (int i = 1; i < size; i++) {
                TZID tzid = list.get(i);
                if (tzid.canonical().startsWith("WINDOWS~")) {
                    arrayList.remove(tzid);
                }
            }
            if (!arrayList.isEmpty()) {
                return arrayList;
            }
        }
        return list;
    }

    private List<TZID> resolveUsingPreferred(List<TZID> list, Locale locale, Leniency leniency) {
        boolean z;
        HashMap hashMap = new HashMap();
        hashMap.put(DEFAULT_PROVIDER, new ArrayList());
        Iterator<TZID> it = list.iterator();
        while (true) {
            z = false;
            if (!it.hasNext()) {
                break;
            }
            String canonical = it.next().canonical();
            Set<TZID> set = this.preferredZones;
            int indexOf = canonical.indexOf(126);
            String substring = indexOf >= 0 ? canonical.substring(0, indexOf) : DEFAULT_PROVIDER;
            if (set == null) {
                set = Timezone.getPreferredIDs(locale, leniency.isSmart(), substring);
            }
            Iterator<TZID> it2 = set.iterator();
            while (true) {
                if (it2.hasNext()) {
                    TZID next = it2.next();
                    if (next.canonical().equals(canonical)) {
                        List list2 = (List) hashMap.get(substring);
                        if (list2 == null) {
                            list2 = new ArrayList();
                            hashMap.put(substring, list2);
                        }
                        list2.add(next);
                    }
                }
            }
        }
        List<TZID> list3 = (List) hashMap.get(DEFAULT_PROVIDER);
        if (list3.isEmpty()) {
            hashMap.remove(DEFAULT_PROVIDER);
            Iterator it3 = hashMap.keySet().iterator();
            while (true) {
                if (!it3.hasNext()) {
                    break;
                }
                List<TZID> list4 = (List) hashMap.get((String) it3.next());
                if (!list4.isEmpty()) {
                    z = true;
                    list = list4;
                    break;
                }
            }
            if (!z) {
                list = Collections.emptyList();
            }
            return list;
        }
        return list3;
    }

    private NameStyle getStyle(boolean z) {
        return z ? this.abbreviated ? NameStyle.SHORT_DAYLIGHT_TIME : NameStyle.LONG_DAYLIGHT_TIME : this.abbreviated ? NameStyle.SHORT_STANDARD_TIME : NameStyle.LONG_STANDARD_TIME;
    }

    private static String toString(List<TZID> list) {
        StringBuilder sb = new StringBuilder(list.size() * 16);
        sb.append('{');
        boolean z = true;
        for (TZID tzid : list) {
            if (z) {
                z = false;
            } else {
                sb.append(',');
            }
            sb.append(tzid.canonical());
        }
        sb.append('}');
        return sb.toString();
    }

    /* loaded from: classes2.dex */
    private static class TZNames {
        private final ZoneLabels dstNames;
        private final ZoneLabels stdNames;

        TZNames(ZoneLabels zoneLabels, ZoneLabels zoneLabels2) {
            this.stdNames = zoneLabels;
            this.dstNames = zoneLabels2;
        }

        void search(CharSequence charSequence, int i, List<TZID> list, List<TZID> list2, int[] iArr) {
            String longestPrefixOf = this.stdNames.longestPrefixOf(charSequence, i);
            int length = longestPrefixOf.length();
            iArr[0] = i + length;
            String longestPrefixOf2 = this.dstNames.longestPrefixOf(charSequence, i);
            int length2 = longestPrefixOf2.length();
            iArr[1] = i + length2;
            if (length2 > length) {
                list2.addAll(this.dstNames.find(longestPrefixOf2));
            } else if (length2 < length) {
                list.addAll(this.stdNames.find(longestPrefixOf));
            } else if (length > 0) {
                list.addAll(this.stdNames.find(longestPrefixOf));
                list2.addAll(this.dstNames.find(longestPrefixOf2));
            }
        }
    }
}
