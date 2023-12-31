package com.facebook.cache.common;

import android.net.Uri;

/* loaded from: classes.dex */
public interface CacheKey {
    boolean containsUri(Uri uri);

    boolean equals(Object o);

    String getUriString();

    int hashCode();

    boolean isResourceIdForDebugging();

    String toString();
}
