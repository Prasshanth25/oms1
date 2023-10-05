package com.facebook.react.bridge;

/* loaded from: classes.dex */
public interface ReactCallback {
    void decrementPendingJSCalls();

    void incrementPendingJSCalls();

    void onBatchComplete();
}
