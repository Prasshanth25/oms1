package net.time4j.format.expert;

import net.time4j.engine.ChronoElement;
import net.time4j.engine.ChronoEntity;
import net.time4j.engine.Chronology;
import net.time4j.format.expert.ParsedEntity;
import net.time4j.tz.TZID;

/* loaded from: classes2.dex */
public abstract class ParsedEntity<T extends ParsedEntity<T>> extends ChronoEntity<T> {
    abstract <E> E getResult();

    public abstract void put(ChronoElement<?> chronoElement, int i);

    public abstract void put(ChronoElement<?> chronoElement, Object obj);

    public abstract void setResult(Object obj);

    @Override // net.time4j.engine.ChronoEntity
    public /* bridge */ /* synthetic */ ChronoEntity with(ChronoElement chronoElement, int i) {
        return with((ChronoElement<Integer>) chronoElement, i);
    }

    @Override // net.time4j.engine.ChronoEntity
    public /* bridge */ /* synthetic */ ChronoEntity with(ChronoElement chronoElement, Object obj) {
        return with((ChronoElement<ChronoElement>) chronoElement, (ChronoElement) obj);
    }

    @Override // net.time4j.engine.ChronoEntity
    public <V> boolean isValid(ChronoElement<V> chronoElement, V v) {
        if (chronoElement != null) {
            return true;
        }
        throw new NullPointerException("Missing chronological element.");
    }

    @Override // net.time4j.engine.ChronoEntity
    public <V> T with(ChronoElement<V> chronoElement, V v) {
        put((ChronoElement<?>) chronoElement, (Object) v);
        return this;
    }

    @Override // net.time4j.engine.ChronoEntity
    public T with(ChronoElement<Integer> chronoElement, int i) {
        put(chronoElement, i);
        return this;
    }

    @Override // net.time4j.engine.ChronoEntity, net.time4j.engine.ChronoDisplay
    public <V> V getMinimum(ChronoElement<V> chronoElement) {
        return chronoElement.getDefaultMinimum();
    }

    @Override // net.time4j.engine.ChronoEntity, net.time4j.engine.ChronoDisplay
    public <V> V getMaximum(ChronoElement<V> chronoElement) {
        return chronoElement.getDefaultMaximum();
    }

    @Override // net.time4j.engine.ChronoEntity, net.time4j.engine.ChronoDisplay
    public final boolean hasTimezone() {
        return contains(TimezoneElement.TIMEZONE_ID) || contains(TimezoneElement.TIMEZONE_OFFSET);
    }

    @Override // net.time4j.engine.ChronoEntity, net.time4j.engine.ChronoDisplay
    public final TZID getTimezone() {
        Object obj;
        if (contains(TimezoneElement.TIMEZONE_ID)) {
            obj = get(TimezoneElement.TIMEZONE_ID);
        } else {
            obj = contains(TimezoneElement.TIMEZONE_OFFSET) ? get(TimezoneElement.TIMEZONE_OFFSET) : null;
        }
        if (obj instanceof TZID) {
            return (TZID) TZID.class.cast(obj);
        }
        return super.getTimezone();
    }

    /* JADX WARN: Removed duplicated region for block: B:75:0x0028  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean equals(java.lang.Object r7) {
        /*
            r6 = this;
            r0 = 1
            if (r6 != r7) goto L4
            return r0
        L4:
            boolean r1 = r7 instanceof net.time4j.format.expert.ParsedEntity
            r2 = 0
            if (r1 == 0) goto L57
            net.time4j.format.expert.ParsedEntity r7 = (net.time4j.format.expert.ParsedEntity) r7
            java.util.Set r1 = r6.getRegisteredElements()
            java.util.Set r3 = r7.getRegisteredElements()
            int r4 = r1.size()
            int r5 = r3.size()
            if (r4 == r5) goto L1e
            return r2
        L1e:
            java.util.Iterator r1 = r1.iterator()
        L22:
            boolean r4 = r1.hasNext()
            if (r4 == 0) goto L43
            java.lang.Object r4 = r1.next()
            net.time4j.engine.ChronoElement r4 = (net.time4j.engine.ChronoElement) r4
            boolean r5 = r3.contains(r4)
            if (r5 == 0) goto L42
            java.lang.Object r5 = r6.get(r4)
            java.lang.Object r4 = r7.get(r4)
            boolean r4 = r5.equals(r4)
            if (r4 != 0) goto L22
        L42:
            return r2
        L43:
            java.lang.Object r1 = r6.getResult()
            java.lang.Object r7 = r7.getResult()
            if (r1 != 0) goto L52
            if (r7 != 0) goto L50
            goto L51
        L50:
            r0 = 0
        L51:
            return r0
        L52:
            boolean r7 = r1.equals(r7)
            return r7
        L57:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: net.time4j.format.expert.ParsedEntity.equals(java.lang.Object):boolean");
    }

    public final int hashCode() {
        int hashCode = getRegisteredElements().hashCode();
        Object result = getResult();
        return result != null ? hashCode + (result.hashCode() * 31) : hashCode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append('{');
        boolean z = true;
        for (ChronoElement<?> chronoElement : getRegisteredElements()) {
            if (z) {
                z = false;
            } else {
                sb.append(", ");
            }
            sb.append(chronoElement.name());
            sb.append('=');
            sb.append(get(chronoElement));
        }
        sb.append('}');
        Object result = getResult();
        if (result != null) {
            sb.append(">>>result=");
            sb.append(result);
        }
        return sb.toString();
    }

    @Override // net.time4j.engine.ChronoEntity
    public final Chronology<T> getChronology() {
        throw new UnsupportedOperationException("Parsed values do not have any chronology.");
    }
}
