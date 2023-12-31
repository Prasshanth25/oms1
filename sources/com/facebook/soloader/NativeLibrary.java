package com.facebook.soloader;

import android.util.Log;
import java.util.List;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class NativeLibrary {
    private static final String TAG = "com.facebook.soloader.NativeLibrary";
    @Nullable
    private List<String> mLibraryNames;
    private final Object mLock = new Object();
    private Boolean mLoadLibraries = true;
    private boolean mLibrariesLoaded = false;
    @Nullable
    private volatile UnsatisfiedLinkError mLinkError = null;

    protected void initialNativeCheck() throws UnsatisfiedLinkError {
    }

    protected NativeLibrary(List<String> list) {
        this.mLibraryNames = list;
    }

    @Nullable
    public boolean loadLibraries() {
        synchronized (this.mLock) {
            if (!this.mLoadLibraries.booleanValue()) {
                return this.mLibrariesLoaded;
            }
            try {
                List<String> list = this.mLibraryNames;
                if (list != null) {
                    for (String str : list) {
                        SoLoader.loadLibrary(str);
                    }
                }
                initialNativeCheck();
                this.mLibrariesLoaded = true;
                this.mLibraryNames = null;
            } catch (UnsatisfiedLinkError e) {
                Log.e(TAG, "Failed to load native lib (initial check): ", e);
                this.mLinkError = e;
                this.mLibrariesLoaded = false;
            }
            this.mLoadLibraries = false;
            return this.mLibrariesLoaded;
        }
    }

    public void ensureLoaded() throws UnsatisfiedLinkError {
        if (!loadLibraries()) {
            throw this.mLinkError;
        }
    }

    @Nullable
    public UnsatisfiedLinkError getError() {
        return this.mLinkError;
    }
}
