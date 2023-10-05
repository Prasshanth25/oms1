package com.swmansion.gesturehandler.react;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.uimanager.RootView;
import com.facebook.react.views.view.ReactViewGroup;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: RNGestureHandlerRootView.kt */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0014J\u0010\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0006H\u0016J\u0006\u0010\u0010\u001a\u00020\rR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lcom/swmansion/gesturehandler/react/RNGestureHandlerRootView;", "Lcom/facebook/react/views/view/ReactViewGroup;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "_enabled", "", "rootHelper", "Lcom/swmansion/gesturehandler/react/RNGestureHandlerRootHelper;", "dispatchTouchEvent", "ev", "Landroid/view/MotionEvent;", "onAttachedToWindow", "", "requestDisallowInterceptTouchEvent", "disallowIntercept", "tearDown", "Companion", "react-native-gesture-handler_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class RNGestureHandlerRootView extends ReactViewGroup {
    public static final Companion Companion = new Companion(null);
    private boolean _enabled;
    private RNGestureHandlerRootHelper rootHelper;

    public RNGestureHandlerRootView(Context context) {
        super(context);
    }

    @Override // com.facebook.react.views.view.ReactViewGroup, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        RNGestureHandlerRootView rNGestureHandlerRootView = this;
        boolean z = !Companion.hasGestureHandlerEnabledRootView(rNGestureHandlerRootView);
        this._enabled = z;
        if (!z) {
            Log.i(ReactConstants.TAG, "[GESTURE HANDLER] Gesture handler is already enabled for a parent view");
        }
        if (this._enabled && this.rootHelper == null) {
            Context context = getContext();
            if (context == null) {
                throw new NullPointerException("null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
            }
            this.rootHelper = new RNGestureHandlerRootHelper((ReactContext) context, rNGestureHandlerRootView);
        }
    }

    public final void tearDown() {
        RNGestureHandlerRootHelper rNGestureHandlerRootHelper = this.rootHelper;
        if (rNGestureHandlerRootHelper != null) {
            rNGestureHandlerRootHelper.tearDown();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Intrinsics.checkNotNullParameter(ev, "ev");
        if (this._enabled) {
            RNGestureHandlerRootHelper rNGestureHandlerRootHelper = this.rootHelper;
            Intrinsics.checkNotNull(rNGestureHandlerRootHelper);
            if (rNGestureHandlerRootHelper.dispatchTouchEvent(ev)) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z) {
        if (this._enabled) {
            RNGestureHandlerRootHelper rNGestureHandlerRootHelper = this.rootHelper;
            Intrinsics.checkNotNull(rNGestureHandlerRootHelper);
            rNGestureHandlerRootHelper.requestDisallowInterceptTouchEvent(z);
        }
        super.requestDisallowInterceptTouchEvent(z);
    }

    /* compiled from: RNGestureHandlerRootView.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002¨\u0006\u0007"}, d2 = {"Lcom/swmansion/gesturehandler/react/RNGestureHandlerRootView$Companion;", "", "()V", "hasGestureHandlerEnabledRootView", "", "viewGroup", "Landroid/view/ViewGroup;", "react-native-gesture-handler_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean hasGestureHandlerEnabledRootView(ViewGroup viewGroup) {
            UiThreadUtil.assertOnUiThread();
            for (ViewParent parent = viewGroup.getParent(); parent != null; parent = parent.getParent()) {
                if ((parent instanceof RNGestureHandlerEnabledRootView) || (parent instanceof RNGestureHandlerRootView)) {
                    return true;
                }
                if (parent instanceof RootView) {
                    return false;
                }
            }
            return false;
        }
    }
}
