package com.facebook.imagepipeline.memory;

import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public interface PoolBackend<T> {
    @Nullable
    T get(int size);

    int getSize(T item);

    @Nullable
    T pop();

    void put(T item);
}
