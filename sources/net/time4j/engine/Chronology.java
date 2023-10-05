package net.time4j.engine;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import net.time4j.base.TimeSource;

/* loaded from: classes2.dex */
public class Chronology<T> implements ChronoMerger<T> {
    private static final List<ChronoReference> CHRONOS = new CopyOnWriteArrayList();
    private static final ReferenceQueue<Chronology<?>> QUEUE = new ReferenceQueue<>();
    private final Class<T> chronoType;
    private final List<ChronoExtension> extensions;
    private final Map<ChronoElement<?>, IntElementRule<T>> intRules;
    private final ChronoMerger<T> merger;
    private final Map<ChronoElement<?>, ElementRule<T, ?>> ruleMap;

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> T cast(Object obj) {
        return obj;
    }

    public boolean hasCalendarSystem() {
        return false;
    }

    public Chronology(Class<T> cls) {
        if (cls == null) {
            throw new NullPointerException("Missing chronological type.");
        }
        this.chronoType = cls;
        this.merger = null;
        this.ruleMap = Collections.emptyMap();
        this.extensions = Collections.emptyList();
        this.intRules = Collections.emptyMap();
    }

    public Chronology(Class<T> cls, ChronoMerger<T> chronoMerger, Map<ChronoElement<?>, ElementRule<T, ?>> map, List<ChronoExtension> list) {
        if (cls == null) {
            throw new NullPointerException("Missing chronological type.");
        }
        if (chronoMerger == null) {
            throw new NullPointerException("Missing chronological merger.");
        }
        this.chronoType = cls;
        this.merger = chronoMerger;
        Map<ChronoElement<?>, ElementRule<T, ?>> unmodifiableMap = Collections.unmodifiableMap(map);
        this.ruleMap = unmodifiableMap;
        this.extensions = Collections.unmodifiableList(list);
        HashMap hashMap = new HashMap();
        for (ChronoElement<?> chronoElement : unmodifiableMap.keySet()) {
            if (chronoElement.getType() == Integer.class) {
                ElementRule<T, ?> elementRule = this.ruleMap.get(chronoElement);
                if (elementRule instanceof IntElementRule) {
                    hashMap.put(chronoElement, (IntElementRule) elementRule);
                }
            }
        }
        this.intRules = Collections.unmodifiableMap(hashMap);
    }

    public Class<T> getChronoType() {
        return this.chronoType;
    }

    public Set<ChronoElement<?>> getRegisteredElements() {
        return this.ruleMap.keySet();
    }

    public boolean isRegistered(ChronoElement<?> chronoElement) {
        return chronoElement != null && this.ruleMap.containsKey(chronoElement);
    }

    public boolean isSupported(ChronoElement<?> chronoElement) {
        if (chronoElement == null) {
            return false;
        }
        return isRegistered(chronoElement) || getDerivedRule(chronoElement, false) != null;
    }

    @Override // net.time4j.engine.ChronoMerger
    public T createFrom(TimeSource<?> timeSource, AttributeQuery attributeQuery) {
        if (attributeQuery == null) {
            throw new NullPointerException("Missing attributes.");
        }
        return this.merger.createFrom(timeSource, attributeQuery);
    }

    @Override // net.time4j.engine.ChronoMerger
    public T createFrom(ChronoEntity<?> chronoEntity, AttributeQuery attributeQuery, boolean z, boolean z2) {
        return this.merger.createFrom(chronoEntity, attributeQuery, z, z2);
    }

    @Override // net.time4j.engine.ChronoMerger
    public ChronoDisplay preformat(T t, AttributeQuery attributeQuery) {
        return this.merger.preformat(t, attributeQuery);
    }

    @Override // net.time4j.engine.ChronoMerger
    public Chronology<?> preparser() {
        return this.merger.preparser();
    }

    @Override // net.time4j.engine.ChronoMerger
    public String getFormatPattern(DisplayStyle displayStyle, Locale locale) {
        return this.merger.getFormatPattern(displayStyle, locale);
    }

    @Override // net.time4j.engine.ChronoMerger
    public StartOfDay getDefaultStartOfDay() {
        return this.merger.getDefaultStartOfDay();
    }

    @Override // net.time4j.engine.ChronoMerger
    public int getDefaultPivotYear() {
        return this.merger.getDefaultPivotYear();
    }

    public List<ChronoExtension> getExtensions() {
        return this.extensions;
    }

    public CalendarSystem<T> getCalendarSystem() {
        throw new ChronoException("Calendar system is not available.");
    }

    public CalendarSystem<T> getCalendarSystem(String str) {
        throw new ChronoException("Calendar variant is not available: " + str);
    }

    public final CalendarSystem<T> getCalendarSystem(VariantSource variantSource) {
        return getCalendarSystem(variantSource.getVariant());
    }

    public static <T> Chronology<T> lookup(Class<T> cls) {
        Chronology chronology;
        try {
            Class.forName(cls.getName(), true, cls.getClassLoader());
            Iterator<ChronoReference> it = CHRONOS.iterator();
            boolean z = false;
            while (true) {
                if (!it.hasNext()) {
                    chronology = null;
                    break;
                }
                chronology = (Chronology) it.next().get();
                if (chronology == null) {
                    z = true;
                } else if (chronology.getChronoType() == cls) {
                    break;
                }
            }
            if (z) {
                purgeQueue();
            }
            return (Chronology) cast(chronology);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void register(Chronology<?> chronology) {
        CHRONOS.add(new ChronoReference(chronology, QUEUE));
    }

    public <V> ElementRule<T, V> getRule(ChronoElement<V> chronoElement) {
        if (chronoElement == null) {
            throw new NullPointerException("Missing chronological element.");
        }
        ElementRule<T, ?> elementRule = this.ruleMap.get(chronoElement);
        if (elementRule == null && (elementRule = getDerivedRule(chronoElement, true)) == null) {
            throw new RuleNotFoundException((Chronology<?>) this, (ChronoElement<?>) chronoElement);
        }
        return (ElementRule) cast(elementRule);
    }

    public IntElementRule<T> getIntegerRule(ChronoElement<Integer> chronoElement) {
        return this.intRules.get(chronoElement);
    }

    private ElementRule<T, ?> getDerivedRule(ChronoElement<?> chronoElement, boolean z) {
        if ((chronoElement instanceof BasicElement) && ChronoEntity.class.isAssignableFrom(getChronoType())) {
            BasicElement basicElement = (BasicElement) BasicElement.class.cast(chronoElement);
            String veto = z ? basicElement.getVeto(this) : null;
            if (veto == null) {
                return (ElementRule) cast(basicElement.derive((Chronology) cast(this)));
            }
            throw new RuleNotFoundException(veto);
        }
        return null;
    }

    private static void purgeQueue() {
        while (true) {
            ChronoReference chronoReference = (ChronoReference) QUEUE.poll();
            if (chronoReference == null) {
                return;
            }
            Iterator<ChronoReference> it = CHRONOS.iterator();
            while (true) {
                if (it.hasNext()) {
                    ChronoReference next = it.next();
                    if (next.name.equals(chronoReference.name)) {
                        CHRONOS.remove(next);
                        break;
                    }
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class Builder<T extends ChronoEntity<T>> {
        final Class<T> chronoType;
        final List<ChronoExtension> extensions;
        final ChronoMerger<T> merger;
        final Map<ChronoElement<?>, ElementRule<T, ?>> ruleMap;
        final boolean time4j;

        public Builder(Class<T> cls, ChronoMerger<T> chronoMerger) {
            if (chronoMerger == null) {
                throw new NullPointerException("Missing chronological merger.");
            }
            this.chronoType = cls;
            this.time4j = cls.getName().startsWith("net.time4j.");
            this.merger = chronoMerger;
            this.ruleMap = new HashMap();
            this.extensions = new ArrayList();
        }

        public static <T extends ChronoEntity<T>> Builder<T> setUp(Class<T> cls, ChronoMerger<T> chronoMerger) {
            if (TimePoint.class.isAssignableFrom(cls)) {
                throw new UnsupportedOperationException("This builder cannot construct a chronology with a time axis, use TimeAxis.Builder instead.");
            }
            return new Builder<>(cls, chronoMerger);
        }

        public <V> Builder<T> appendElement(ChronoElement<V> chronoElement, ElementRule<T, V> elementRule) {
            checkElementDuplicates(chronoElement);
            this.ruleMap.put(chronoElement, elementRule);
            return this;
        }

        public Builder<T> appendExtension(ChronoExtension chronoExtension) {
            if (chronoExtension == null) {
                throw new NullPointerException("Missing chronological extension.");
            }
            if (!this.extensions.contains(chronoExtension)) {
                this.extensions.add(chronoExtension);
            }
            return this;
        }

        public Chronology<T> build() {
            Chronology<T> chronology = new Chronology<>(this.chronoType, this.merger, this.ruleMap, this.extensions);
            Chronology.register(chronology);
            return chronology;
        }

        /* JADX WARN: Removed duplicated region for block: B:53:0x001b  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void checkElementDuplicates(net.time4j.engine.ChronoElement<?> r5) {
            /*
                r4 = this;
                boolean r0 = r4.time4j
                if (r0 == 0) goto L5
                return
            L5:
                if (r5 == 0) goto L47
                java.lang.String r0 = r5.name()
                java.util.Map<net.time4j.engine.ChronoElement<?>, net.time4j.engine.ElementRule<T extends net.time4j.engine.ChronoEntity<T>, ?>> r1 = r4.ruleMap
                java.util.Set r1 = r1.keySet()
                java.util.Iterator r1 = r1.iterator()
            L15:
                boolean r2 = r1.hasNext()
                if (r2 == 0) goto L46
                java.lang.Object r2 = r1.next()
                net.time4j.engine.ChronoElement r2 = (net.time4j.engine.ChronoElement) r2
                boolean r3 = r2.equals(r5)
                if (r3 != 0) goto L32
                java.lang.String r2 = r2.name()
                boolean r2 = r2.equals(r0)
                if (r2 != 0) goto L32
                goto L15
            L32:
                java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = "Element duplicate found: "
                r1.<init>(r2)
                r1.append(r0)
                java.lang.String r0 = r1.toString()
                r5.<init>(r0)
                throw r5
            L46:
                return
            L47:
                java.lang.NullPointerException r5 = new java.lang.NullPointerException
                java.lang.String r0 = "Static initialization problem: Check if given element statically refer to any chronology causing premature class loading."
                r5.<init>(r0)
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: net.time4j.engine.Chronology.Builder.checkElementDuplicates(net.time4j.engine.ChronoElement):void");
        }
    }

    /* loaded from: classes2.dex */
    public static class ChronoReference extends WeakReference<Chronology<?>> {
        private final String name;

        ChronoReference(Chronology<?> chronology, ReferenceQueue<Chronology<?>> referenceQueue) {
            super(chronology, referenceQueue);
            this.name = ((Chronology) chronology).chronoType.getName();
        }
    }
}
