package com.facebook.drawee.components;

/* loaded from: classes.dex */
public class RetryManager {
    private static final int MAX_TAP_TO_RETRY_ATTEMPTS = 4;
    private int mMaxTapToRetryAttempts;
    private int mTapToRetryAttempts;
    private boolean mTapToRetryEnabled;

    public RetryManager() {
        init();
    }

    public static RetryManager newInstance() {
        return new RetryManager();
    }

    public void init() {
        this.mTapToRetryEnabled = false;
        this.mMaxTapToRetryAttempts = 4;
        reset();
    }

    public void reset() {
        this.mTapToRetryAttempts = 0;
    }

    public boolean isTapToRetryEnabled() {
        return this.mTapToRetryEnabled;
    }

    public void setTapToRetryEnabled(boolean tapToRetryEnabled) {
        this.mTapToRetryEnabled = tapToRetryEnabled;
    }

    public void setMaxTapToRetryAttemps(int maxTapToRetryAttemps) {
        this.mMaxTapToRetryAttempts = maxTapToRetryAttemps;
    }

    public boolean shouldRetryOnTap() {
        return this.mTapToRetryEnabled && this.mTapToRetryAttempts < this.mMaxTapToRetryAttempts;
    }

    public void notifyTapToRetry() {
        this.mTapToRetryAttempts++;
    }
}
