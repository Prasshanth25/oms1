package net.time4j.engine;

/* loaded from: classes2.dex */
public interface Normalizer<U> {
    TimeSpan<U> normalize(TimeSpan<? extends U> timeSpan);
}
