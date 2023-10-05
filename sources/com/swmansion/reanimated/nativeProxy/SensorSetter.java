package com.swmansion.reanimated.nativeProxy;

import com.facebook.jni.HybridData;

/* loaded from: classes.dex */
public class SensorSetter {
    private final HybridData mHybridData;

    public native void sensorSetter(float[] fArr, int i);

    private SensorSetter(HybridData hybridData) {
        this.mHybridData = hybridData;
    }
}
