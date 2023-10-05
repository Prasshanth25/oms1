package org.apache.commons.lang3.builder;

/* loaded from: classes2.dex */
public final class IDKey {
    private final int id;
    private final Object value;

    public IDKey(Object obj) {
        this.id = System.identityHashCode(obj);
        this.value = obj;
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IDKey) {
            IDKey iDKey = (IDKey) obj;
            return this.id == iDKey.id && this.value == iDKey.value;
        }
        return false;
    }
}
