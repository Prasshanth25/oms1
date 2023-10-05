package com.swmansion.gesturehandler.core;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: FlingGestureHandler.kt */
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 %2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001%B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\u0010\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\b\u0010\u001e\u001a\u00020\u0018H\u0014J\u0018\u0010\u001f\u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010 \u001a\u00020\u001dH\u0014J\b\u0010!\u001a\u00020\u0018H\u0014J\b\u0010\"\u001a\u00020\u0018H\u0016J\u0010\u0010#\u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u0010\u0010$\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000eX\u0082D¢\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0006\"\u0004\b\u0013\u0010\bR\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006&"}, d2 = {"Lcom/swmansion/gesturehandler/core/FlingGestureHandler;", "Lcom/swmansion/gesturehandler/core/GestureHandler;", "()V", "direction", "", "getDirection", "()I", "setDirection", "(I)V", "failDelayed", "Ljava/lang/Runnable;", "handler", "Landroid/os/Handler;", "maxDurationMs", "", "maxNumberOfPointersSimultaneously", "minAcceptableDelta", "numberOfPointersRequired", "getNumberOfPointersRequired", "setNumberOfPointersRequired", "startX", "", "startY", "activate", "", "force", "", "endFling", NotificationCompat.CATEGORY_EVENT, "Landroid/view/MotionEvent;", "onCancel", "onHandle", "sourceEvent", "onReset", "resetConfig", "startFling", "tryEndFling", "Companion", "react-native-gesture-handler_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class FlingGestureHandler extends GestureHandler<FlingGestureHandler> {
    public static final Companion Companion = new Companion(null);
    private static final int DEFAULT_DIRECTION = 1;
    private static final long DEFAULT_MAX_DURATION_MS = 800;
    private static final long DEFAULT_MIN_ACCEPTABLE_DELTA = 160;
    private static final int DEFAULT_NUMBER_OF_TOUCHES_REQUIRED = 1;
    private Handler handler;
    private int maxNumberOfPointersSimultaneously;
    private float startX;
    private float startY;
    private int numberOfPointersRequired = 1;
    private int direction = 1;
    private final long maxDurationMs = DEFAULT_MAX_DURATION_MS;
    private final long minAcceptableDelta = DEFAULT_MIN_ACCEPTABLE_DELTA;
    private final Runnable failDelayed = new Runnable() { // from class: com.swmansion.gesturehandler.core.FlingGestureHandler$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            FlingGestureHandler.m385failDelayed$lambda0(FlingGestureHandler.this);
        }
    };

    public final int getNumberOfPointersRequired() {
        return this.numberOfPointersRequired;
    }

    public final void setNumberOfPointersRequired(int i) {
        this.numberOfPointersRequired = i;
    }

    public final int getDirection() {
        return this.direction;
    }

    public final void setDirection(int i) {
        this.direction = i;
    }

    /* renamed from: failDelayed$lambda-0 */
    public static final void m385failDelayed$lambda0(FlingGestureHandler this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.fail();
    }

    @Override // com.swmansion.gesturehandler.core.GestureHandler
    public void resetConfig() {
        super.resetConfig();
        this.numberOfPointersRequired = 1;
        this.direction = 1;
    }

    private final void startFling(MotionEvent motionEvent) {
        this.startX = motionEvent.getRawX();
        this.startY = motionEvent.getRawY();
        begin();
        this.maxNumberOfPointersSimultaneously = 1;
        Handler handler = this.handler;
        if (handler == null) {
            this.handler = new Handler(Looper.getMainLooper());
        } else {
            Intrinsics.checkNotNull(handler);
            handler.removeCallbacksAndMessages(null);
        }
        Handler handler2 = this.handler;
        Intrinsics.checkNotNull(handler2);
        handler2.postDelayed(this.failDelayed, this.maxDurationMs);
    }

    private final boolean tryEndFling(MotionEvent motionEvent) {
        if (this.maxNumberOfPointersSimultaneously != this.numberOfPointersRequired || (((this.direction & 1) == 0 || motionEvent.getRawX() - this.startX <= ((float) this.minAcceptableDelta)) && (((this.direction & 2) == 0 || this.startX - motionEvent.getRawX() <= ((float) this.minAcceptableDelta)) && (((this.direction & 4) == 0 || this.startY - motionEvent.getRawY() <= ((float) this.minAcceptableDelta)) && ((this.direction & 8) == 0 || motionEvent.getRawY() - this.startY <= ((float) this.minAcceptableDelta)))))) {
            return false;
        }
        Handler handler = this.handler;
        Intrinsics.checkNotNull(handler);
        handler.removeCallbacksAndMessages(null);
        activate();
        return true;
    }

    @Override // com.swmansion.gesturehandler.core.GestureHandler
    public void activate(boolean z) {
        super.activate(z);
        end();
    }

    private final void endFling(MotionEvent motionEvent) {
        if (tryEndFling(motionEvent)) {
            return;
        }
        fail();
    }

    @Override // com.swmansion.gesturehandler.core.GestureHandler
    protected void onHandle(MotionEvent event, MotionEvent sourceEvent) {
        Intrinsics.checkNotNullParameter(event, "event");
        Intrinsics.checkNotNullParameter(sourceEvent, "sourceEvent");
        int state = getState();
        if (state == 0) {
            startFling(sourceEvent);
        }
        if (state == 2) {
            tryEndFling(sourceEvent);
            if (sourceEvent.getPointerCount() > this.maxNumberOfPointersSimultaneously) {
                this.maxNumberOfPointersSimultaneously = sourceEvent.getPointerCount();
            }
            if (sourceEvent.getActionMasked() == 1) {
                endFling(sourceEvent);
            }
        }
    }

    @Override // com.swmansion.gesturehandler.core.GestureHandler
    protected void onCancel() {
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override // com.swmansion.gesturehandler.core.GestureHandler
    protected void onReset() {
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /* compiled from: FlingGestureHandler.kt */
    @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lcom/swmansion/gesturehandler/core/FlingGestureHandler$Companion;", "", "()V", "DEFAULT_DIRECTION", "", "DEFAULT_MAX_DURATION_MS", "", "DEFAULT_MIN_ACCEPTABLE_DELTA", "DEFAULT_NUMBER_OF_TOUCHES_REQUIRED", "react-native-gesture-handler_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
