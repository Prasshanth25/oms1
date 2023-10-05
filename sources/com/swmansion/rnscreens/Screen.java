package com.swmansion.rnscreens;

import android.content.Context;
import android.graphics.Paint;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import com.facebook.react.bridge.GuardedRunnable;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ViewProps;
import com.henninghall.date_picker.props.ModeProp;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Screen.kt */
@Metadata(d1 = {"\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0013\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001:\u0005xyz{|B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010]\u001a\u00020^2\u0006\u0010_\u001a\u00020/J\u0016\u0010`\u001a\u00020^2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020b0aH\u0014J\u0016\u0010c\u001a\u00020^2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020b0aH\u0014J\u0010\u0010d\u001a\u00020\u001b2\u0006\u0010e\u001a\u00020fH\u0002J0\u0010g\u001a\u00020^2\u0006\u0010h\u001a\u00020\u001b2\u0006\u0010i\u001a\u00020/2\u0006\u0010j\u001a\u00020/2\u0006\u0010k\u001a\u00020/2\u0006\u0010l\u001a\u00020/H\u0014J\u000e\u0010m\u001a\u00020^2\u0006\u0010\u0007\u001a\u00020\u0006J\u001a\u0010n\u001a\u00020^2\u0006\u0010o\u001a\u00020/2\b\u0010p\u001a\u0004\u0018\u00010qH\u0016J\u0010\u0010r\u001a\u00020^2\b\u0010G\u001a\u0004\u0018\u000105J\u000e\u0010s\u001a\u00020^2\u0006\u0010t\u001a\u00020\u001bJ\u0018\u0010u\u001a\u00020^2\u0006\u0010v\u001a\u00020/2\u0006\u0010w\u001a\u00020/H\u0002R\"\u0010\u0007\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR \u0010\n\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0013\u0010\u0016\u001a\u0004\u0018\u00010\u00178F¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001a\u001a\u00020\u001bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001c\"\u0004\b\u001d\u0010\u001eR(\u0010 \u001a\u0004\u0018\u00010\u001b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u001b8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u001e\u0010$\u001a\u0004\u0018\u00010\u001bX\u0086\u000e¢\u0006\u0010\n\u0002\u0010&\u001a\u0004\b$\u0010!\"\u0004\b%\u0010#R(\u0010(\u001a\u0004\u0018\u00010\u001b2\b\u0010'\u001a\u0004\u0018\u00010\u001b8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b(\u0010!\"\u0004\b)\u0010#R(\u0010+\u001a\u0004\u0018\u00010\u001b2\b\u0010*\u001a\u0004\u0018\u00010\u001b8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b+\u0010!\"\u0004\b,\u0010#R\u000e\u0010-\u001a\u00020\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010.\u001a\u0004\u0018\u00010/X\u0082\u000e¢\u0006\u0004\n\u0002\u00100R\u0012\u00101\u001a\u0004\u0018\u00010\u001bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010&R\u0012\u00102\u001a\u0004\u0018\u00010/X\u0082\u000e¢\u0006\u0004\n\u0002\u00100R\u0012\u00103\u001a\u0004\u0018\u00010\u001bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010&R\u0010\u00104\u001a\u0004\u0018\u000105X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u00106\u001a\u0004\u0018\u00010\u001bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010&R\u000e\u00107\u001a\u00020\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R$\u00109\u001a\u00020\u001b2\u0006\u00108\u001a\u00020\u001b8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b:\u0010\u001c\"\u0004\b;\u0010\u001eR(\u0010<\u001a\u0004\u0018\u00010/2\b\u0010<\u001a\u0004\u0018\u00010/8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b=\u0010>\"\u0004\b?\u0010@R\u001a\u0010A\u001a\u00020BX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bC\u0010D\"\u0004\bE\u0010FR$\u0010G\u001a\u0004\u0018\u00010/2\b\u0010\u0005\u001a\u0004\u0018\u00010/@BX\u0086\u000e¢\u0006\n\n\u0002\u00100\u001a\u0004\bH\u0010>R\u001a\u0010I\u001a\u00020JX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bK\u0010L\"\u0004\bM\u0010NR\u001a\u0010O\u001a\u00020PX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bQ\u0010R\"\u0004\bS\u0010TR(\u0010U\u001a\u0004\u0018\u00010/2\b\u0010U\u001a\u0004\u0018\u00010/8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\bV\u0010>\"\u0004\bW\u0010@R(\u0010X\u001a\u0004\u0018\u0001052\b\u0010X\u001a\u0004\u0018\u0001058F@FX\u0086\u000e¢\u0006\f\u001a\u0004\bY\u0010Z\"\u0004\b[\u0010\\¨\u0006}"}, d2 = {"Lcom/swmansion/rnscreens/Screen;", "Lcom/swmansion/rnscreens/FabricEnabledViewGroup;", "context", "Lcom/facebook/react/bridge/ReactContext;", "(Lcom/facebook/react/bridge/ReactContext;)V", "<set-?>", "Lcom/swmansion/rnscreens/Screen$ActivityState;", "activityState", "getActivityState", "()Lcom/swmansion/rnscreens/Screen$ActivityState;", "container", "Lcom/swmansion/rnscreens/ScreenContainer;", "getContainer", "()Lcom/swmansion/rnscreens/ScreenContainer;", "setContainer", "(Lcom/swmansion/rnscreens/ScreenContainer;)V", "fragment", "Lcom/swmansion/rnscreens/ScreenFragment;", "getFragment", "()Lcom/swmansion/rnscreens/ScreenFragment;", "setFragment", "(Lcom/swmansion/rnscreens/ScreenFragment;)V", "headerConfig", "Lcom/swmansion/rnscreens/ScreenStackHeaderConfig;", "getHeaderConfig", "()Lcom/swmansion/rnscreens/ScreenStackHeaderConfig;", "isGestureEnabled", "", "()Z", "setGestureEnabled", "(Z)V", "navigationBarHidden", "isNavigationBarHidden", "()Ljava/lang/Boolean;", "setNavigationBarHidden", "(Ljava/lang/Boolean;)V", "isStatusBarAnimated", "setStatusBarAnimated", "Ljava/lang/Boolean;", "statusBarHidden", "isStatusBarHidden", "setStatusBarHidden", "statusBarTranslucent", "isStatusBarTranslucent", "setStatusBarTranslucent", "mNativeBackButtonDismissalEnabled", "mNavigationBarColor", "", "Ljava/lang/Integer;", "mNavigationBarHidden", "mStatusBarColor", "mStatusBarHidden", "mStatusBarStyle", "", "mStatusBarTranslucent", "mTransitioning", "enableNativeBackButtonDismissal", "nativeBackButtonDismissalEnabled", "getNativeBackButtonDismissalEnabled", "setNativeBackButtonDismissalEnabled", "navigationBarColor", "getNavigationBarColor", "()Ljava/lang/Integer;", "setNavigationBarColor", "(Ljava/lang/Integer;)V", "replaceAnimation", "Lcom/swmansion/rnscreens/Screen$ReplaceAnimation;", "getReplaceAnimation", "()Lcom/swmansion/rnscreens/Screen$ReplaceAnimation;", "setReplaceAnimation", "(Lcom/swmansion/rnscreens/Screen$ReplaceAnimation;)V", "screenOrientation", "getScreenOrientation", "stackAnimation", "Lcom/swmansion/rnscreens/Screen$StackAnimation;", "getStackAnimation", "()Lcom/swmansion/rnscreens/Screen$StackAnimation;", "setStackAnimation", "(Lcom/swmansion/rnscreens/Screen$StackAnimation;)V", "stackPresentation", "Lcom/swmansion/rnscreens/Screen$StackPresentation;", "getStackPresentation", "()Lcom/swmansion/rnscreens/Screen$StackPresentation;", "setStackPresentation", "(Lcom/swmansion/rnscreens/Screen$StackPresentation;)V", "statusBarColor", "getStatusBarColor", "setStatusBarColor", "statusBarStyle", "getStatusBarStyle", "()Ljava/lang/String;", "setStatusBarStyle", "(Ljava/lang/String;)V", "changeAccessibilityMode", "", ModeProp.name, "dispatchRestoreInstanceState", "Landroid/util/SparseArray;", "Landroid/os/Parcelable;", "dispatchSaveInstanceState", "hasWebView", "viewGroup", "Landroid/view/ViewGroup;", ViewProps.ON_LAYOUT, "changed", "l", "t", "r", "b", "setActivityState", "setLayerType", "layerType", "paint", "Landroid/graphics/Paint;", "setScreenOrientation", "setTransitioning", "transitioning", "updateScreenSizePaper", "width", "height", "ActivityState", "ReplaceAnimation", "StackAnimation", "StackPresentation", "WindowTraits", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class Screen extends FabricEnabledViewGroup {
    private ActivityState activityState;
    private ScreenContainer<?> container;
    private ScreenFragment fragment;
    private boolean isGestureEnabled;
    private Boolean isStatusBarAnimated;
    private boolean mNativeBackButtonDismissalEnabled;
    private Integer mNavigationBarColor;
    private Boolean mNavigationBarHidden;
    private Integer mStatusBarColor;
    private Boolean mStatusBarHidden;
    private String mStatusBarStyle;
    private Boolean mStatusBarTranslucent;
    private boolean mTransitioning;
    private ReplaceAnimation replaceAnimation;
    private Integer screenOrientation;
    private StackAnimation stackAnimation;
    private StackPresentation stackPresentation;

    /* compiled from: Screen.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Lcom/swmansion/rnscreens/Screen$ActivityState;", "", "(Ljava/lang/String;I)V", "INACTIVE", "TRANSITIONING_OR_BELOW_TOP", "ON_TOP", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum ActivityState {
        INACTIVE,
        TRANSITIONING_OR_BELOW_TOP,
        ON_TOP
    }

    /* compiled from: Screen.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004¨\u0006\u0005"}, d2 = {"Lcom/swmansion/rnscreens/Screen$ReplaceAnimation;", "", "(Ljava/lang/String;I)V", "PUSH", "POP", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum ReplaceAnimation {
        PUSH,
        POP
    }

    /* compiled from: Screen.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\t\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t¨\u0006\n"}, d2 = {"Lcom/swmansion/rnscreens/Screen$StackAnimation;", "", "(Ljava/lang/String;I)V", "DEFAULT", "NONE", "FADE", "SLIDE_FROM_BOTTOM", "SLIDE_FROM_RIGHT", "SLIDE_FROM_LEFT", "FADE_FROM_BOTTOM", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum StackAnimation {
        DEFAULT,
        NONE,
        FADE,
        SLIDE_FROM_BOTTOM,
        SLIDE_FROM_RIGHT,
        SLIDE_FROM_LEFT,
        FADE_FROM_BOTTOM
    }

    /* compiled from: Screen.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Lcom/swmansion/rnscreens/Screen$StackPresentation;", "", "(Ljava/lang/String;I)V", "PUSH", "MODAL", "TRANSPARENT_MODAL", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum StackPresentation {
        PUSH,
        MODAL,
        TRANSPARENT_MODAL
    }

    /* compiled from: Screen.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\n\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n¨\u0006\u000b"}, d2 = {"Lcom/swmansion/rnscreens/Screen$WindowTraits;", "", "(Ljava/lang/String;I)V", "ORIENTATION", "COLOR", "STYLE", "TRANSLUCENT", "HIDDEN", "ANIMATED", "NAVIGATION_BAR_COLOR", "NAVIGATION_BAR_HIDDEN", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum WindowTraits {
        ORIENTATION,
        COLOR,
        STYLE,
        TRANSLUCENT,
        HIDDEN,
        ANIMATED,
        NAVIGATION_BAR_COLOR,
        NAVIGATION_BAR_HIDDEN
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        Intrinsics.checkNotNullParameter(container, "container");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        Intrinsics.checkNotNullParameter(container, "container");
    }

    @Override // android.view.View
    public void setLayerType(int i, Paint paint) {
    }

    public Screen(ReactContext reactContext) {
        super(reactContext);
        this.stackPresentation = StackPresentation.PUSH;
        this.replaceAnimation = ReplaceAnimation.POP;
        this.stackAnimation = StackAnimation.DEFAULT;
        this.isGestureEnabled = true;
        this.mNativeBackButtonDismissalEnabled = true;
        setLayoutParams(new WindowManager.LayoutParams(2));
    }

    public final ScreenFragment getFragment() {
        return this.fragment;
    }

    public final void setFragment(ScreenFragment screenFragment) {
        this.fragment = screenFragment;
    }

    public final ScreenContainer<?> getContainer() {
        return this.container;
    }

    public final void setContainer(ScreenContainer<?> screenContainer) {
        this.container = screenContainer;
    }

    public final ActivityState getActivityState() {
        return this.activityState;
    }

    public final StackPresentation getStackPresentation() {
        return this.stackPresentation;
    }

    public final void setStackPresentation(StackPresentation stackPresentation) {
        Intrinsics.checkNotNullParameter(stackPresentation, "<set-?>");
        this.stackPresentation = stackPresentation;
    }

    public final ReplaceAnimation getReplaceAnimation() {
        return this.replaceAnimation;
    }

    public final void setReplaceAnimation(ReplaceAnimation replaceAnimation) {
        Intrinsics.checkNotNullParameter(replaceAnimation, "<set-?>");
        this.replaceAnimation = replaceAnimation;
    }

    public final StackAnimation getStackAnimation() {
        return this.stackAnimation;
    }

    public final void setStackAnimation(StackAnimation stackAnimation) {
        Intrinsics.checkNotNullParameter(stackAnimation, "<set-?>");
        this.stackAnimation = stackAnimation;
    }

    public final boolean isGestureEnabled() {
        return this.isGestureEnabled;
    }

    public final void setGestureEnabled(boolean z) {
        this.isGestureEnabled = z;
    }

    public final Integer getScreenOrientation() {
        return this.screenOrientation;
    }

    public final Boolean isStatusBarAnimated() {
        return this.isStatusBarAnimated;
    }

    public final void setStatusBarAnimated(Boolean bool) {
        this.isStatusBarAnimated = bool;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (z) {
            updateScreenSizePaper(i3 - i, i4 - i2);
        }
    }

    private final void updateScreenSizePaper(int i, int i2) {
        Context context = getContext();
        if (context == null) {
            throw new NullPointerException("null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
        }
        ReactContext reactContext = (ReactContext) context;
        reactContext.runOnNativeModulesQueueThread(new GuardedRunnable(this, i, i2) { // from class: com.swmansion.rnscreens.Screen$updateScreenSizePaper$1
            final /* synthetic */ int $height;
            final /* synthetic */ int $width;
            final /* synthetic */ Screen this$0;

            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(ReactContext.this);
                this.this$0 = this;
                this.$width = i;
                this.$height = i2;
            }

            @Override // com.facebook.react.bridge.GuardedRunnable
            public void runGuarded() {
                UIManagerModule uIManagerModule = (UIManagerModule) ReactContext.this.getNativeModule(UIManagerModule.class);
                if (uIManagerModule != null) {
                    uIManagerModule.updateNodeSize(this.this$0.getId(), this.$width, this.$height);
                }
            }
        });
    }

    public final ScreenStackHeaderConfig getHeaderConfig() {
        View childAt = getChildAt(0);
        if (childAt instanceof ScreenStackHeaderConfig) {
            return (ScreenStackHeaderConfig) childAt;
        }
        return null;
    }

    public final void setTransitioning(boolean z) {
        if (this.mTransitioning == z) {
            return;
        }
        this.mTransitioning = z;
        boolean hasWebView = hasWebView(this);
        int i = 2;
        if (!hasWebView || getLayerType() == 2) {
            super.setLayerType((!z || hasWebView) ? 0 : 0, null);
        }
    }

    private final boolean hasWebView(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof WebView) {
                return true;
            }
            if ((childAt instanceof ViewGroup) && hasWebView((ViewGroup) childAt)) {
                return true;
            }
        }
        return false;
    }

    public final void setActivityState(ActivityState activityState) {
        Intrinsics.checkNotNullParameter(activityState, "activityState");
        if (activityState == this.activityState) {
            return;
        }
        this.activityState = activityState;
        ScreenContainer<?> screenContainer = this.container;
        if (screenContainer != null) {
            screenContainer.notifyChildUpdate();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public final void setScreenOrientation(String str) {
        int i;
        if (str == null) {
            this.screenOrientation = null;
            return;
        }
        ScreenWindowTraits.INSTANCE.applyDidSetOrientation$react_native_screens_release();
        switch (str.hashCode()) {
            case -1894896954:
                if (str.equals("portrait_down")) {
                    i = 9;
                    break;
                }
                i = -1;
                break;
            case 96673:
                if (str.equals("all")) {
                    i = 10;
                    break;
                }
                i = -1;
                break;
            case 729267099:
                if (str.equals("portrait")) {
                    i = 7;
                    break;
                }
                i = -1;
                break;
            case 1430647483:
                if (str.equals("landscape")) {
                    i = 6;
                    break;
                }
                i = -1;
                break;
            case 1651658175:
                if (str.equals("portrait_up")) {
                    i = 1;
                    break;
                }
                i = -1;
                break;
            case 1730732811:
                if (str.equals("landscape_left")) {
                    i = 8;
                    break;
                }
                i = -1;
                break;
            case 2118770584:
                if (str.equals("landscape_right")) {
                    i = 0;
                    break;
                }
                i = -1;
                break;
            default:
                i = -1;
                break;
        }
        this.screenOrientation = i;
        ScreenFragment screenFragment = this.fragment;
        if (screenFragment != null) {
            ScreenWindowTraits.INSTANCE.setOrientation$react_native_screens_release(this, screenFragment.tryGetActivity());
        }
    }

    public final void changeAccessibilityMode(int i) {
        setImportantForAccessibility(i);
        ScreenStackHeaderConfig headerConfig = getHeaderConfig();
        CustomToolbar toolbar = headerConfig != null ? headerConfig.getToolbar() : null;
        if (toolbar == null) {
            return;
        }
        toolbar.setImportantForAccessibility(i);
    }

    public final String getStatusBarStyle() {
        return this.mStatusBarStyle;
    }

    public final void setStatusBarStyle(String str) {
        if (str != null) {
            ScreenWindowTraits.INSTANCE.applyDidSetStatusBarAppearance$react_native_screens_release();
        }
        this.mStatusBarStyle = str;
        ScreenFragment screenFragment = this.fragment;
        if (screenFragment != null) {
            ScreenWindowTraits.INSTANCE.setStyle$react_native_screens_release(this, screenFragment.tryGetActivity(), screenFragment.tryGetContext());
        }
    }

    public final Boolean isStatusBarHidden() {
        return this.mStatusBarHidden;
    }

    public final void setStatusBarHidden(Boolean bool) {
        if (bool != null) {
            ScreenWindowTraits.INSTANCE.applyDidSetStatusBarAppearance$react_native_screens_release();
        }
        this.mStatusBarHidden = bool;
        ScreenFragment screenFragment = this.fragment;
        if (screenFragment != null) {
            ScreenWindowTraits.INSTANCE.setHidden$react_native_screens_release(this, screenFragment.tryGetActivity());
        }
    }

    public final Boolean isStatusBarTranslucent() {
        return this.mStatusBarTranslucent;
    }

    public final void setStatusBarTranslucent(Boolean bool) {
        if (bool != null) {
            ScreenWindowTraits.INSTANCE.applyDidSetStatusBarAppearance$react_native_screens_release();
        }
        this.mStatusBarTranslucent = bool;
        ScreenFragment screenFragment = this.fragment;
        if (screenFragment != null) {
            ScreenWindowTraits.INSTANCE.setTranslucent$react_native_screens_release(this, screenFragment.tryGetActivity(), screenFragment.tryGetContext());
        }
    }

    public final Integer getStatusBarColor() {
        return this.mStatusBarColor;
    }

    public final void setStatusBarColor(Integer num) {
        if (num != null) {
            ScreenWindowTraits.INSTANCE.applyDidSetStatusBarAppearance$react_native_screens_release();
        }
        this.mStatusBarColor = num;
        ScreenFragment screenFragment = this.fragment;
        if (screenFragment != null) {
            ScreenWindowTraits.INSTANCE.setColor$react_native_screens_release(this, screenFragment.tryGetActivity(), screenFragment.tryGetContext());
        }
    }

    public final Integer getNavigationBarColor() {
        return this.mNavigationBarColor;
    }

    public final void setNavigationBarColor(Integer num) {
        if (num != null) {
            ScreenWindowTraits.INSTANCE.applyDidSetNavigationBarAppearance$react_native_screens_release();
        }
        this.mNavigationBarColor = num;
        ScreenFragment screenFragment = this.fragment;
        if (screenFragment != null) {
            ScreenWindowTraits.INSTANCE.setNavigationBarColor$react_native_screens_release(this, screenFragment.tryGetActivity());
        }
    }

    public final Boolean isNavigationBarHidden() {
        return this.mNavigationBarHidden;
    }

    public final void setNavigationBarHidden(Boolean bool) {
        if (bool != null) {
            ScreenWindowTraits.INSTANCE.applyDidSetNavigationBarAppearance$react_native_screens_release();
        }
        this.mNavigationBarHidden = bool;
        ScreenFragment screenFragment = this.fragment;
        if (screenFragment != null) {
            ScreenWindowTraits.INSTANCE.setNavigationBarHidden$react_native_screens_release(this, screenFragment.tryGetActivity());
        }
    }

    public final boolean getNativeBackButtonDismissalEnabled() {
        return this.mNativeBackButtonDismissalEnabled;
    }

    public final void setNativeBackButtonDismissalEnabled(boolean z) {
        this.mNativeBackButtonDismissalEnabled = z;
    }
}
