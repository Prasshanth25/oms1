package com.swmansion.reanimated.nativeProxy;

import com.facebook.jni.HybridData;
import com.swmansion.reanimated.NodesManager;

/* loaded from: classes.dex */
public class AnimationFrameCallback implements NodesManager.OnAnimationFrame {
    private final HybridData mHybridData;

    @Override // com.swmansion.reanimated.NodesManager.OnAnimationFrame
    public native void onAnimationFrame(double d);

    private AnimationFrameCallback(HybridData hybridData) {
        this.mHybridData = hybridData;
    }
}
