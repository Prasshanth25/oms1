package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicReference;
import okhttp3.Cookie$$ExternalSyntheticBackport0;

/* loaded from: classes2.dex */
public abstract class AtomicInitializer<T> implements ConcurrentInitializer<T> {
    private final AtomicReference<T> reference = new AtomicReference<>();

    protected abstract T initialize() throws ConcurrentException;

    @Override // org.apache.commons.lang3.concurrent.ConcurrentInitializer
    public T get() throws ConcurrentException {
        T t = this.reference.get();
        if (t == null) {
            T initialize = initialize();
            return !Cookie$$ExternalSyntheticBackport0.m(this.reference, null, initialize) ? this.reference.get() : initialize;
        }
        return t;
    }
}
