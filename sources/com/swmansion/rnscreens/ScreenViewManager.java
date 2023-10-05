package com.swmansion.rnscreens;

import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.viewmanagers.RNSScreenManagerDelegate;
import com.facebook.react.viewmanagers.RNSScreenManagerInterface;
import com.swmansion.rnscreens.Screen;
import com.swmansion.rnscreens.events.HeaderBackButtonClickedEvent;
import com.swmansion.rnscreens.events.ScreenAppearEvent;
import com.swmansion.rnscreens.events.ScreenDisappearEvent;
import com.swmansion.rnscreens.events.ScreenDismissedEvent;
import com.swmansion.rnscreens.events.ScreenTransitionProgressEvent;
import com.swmansion.rnscreens.events.ScreenWillAppearEvent;
import com.swmansion.rnscreens.events.ScreenWillDisappearEvent;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ScreenViewManager.kt */
@ReactModule(name = ScreenViewManager.REACT_CLASS)
@Metadata(d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b$\b\u0007\u0018\u0000 @2\b\u0012\u0004\u0012\u00020\u00020\u00012\b\u0012\u0004\u0012\u00020\u00020\u0003:\u0001@B\u0005¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\tH\u0014J\u000e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00020\u0006H\u0014J\u0014\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u000e0\fH\u0016J\b\u0010\u000f\u001a\u00020\rH\u0016J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0015H\u0007J\u001a\u0010\u0016\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u001a\u0010\u0019\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0018\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0018H\u0017J\u001c\u0010\u001c\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00022\b\u0010\u0017\u001a\u0004\u0018\u00010\u001dH\u0016J\u001a\u0010\u001e\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u001a\u0010\u001f\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0018\u0010 \u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010!\u001a\u00020\u0018H\u0017J\u001f\u0010\"\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\b\u0010#\u001a\u0004\u0018\u00010\u0015H\u0017¢\u0006\u0002\u0010$J\u0018\u0010%\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010&\u001a\u00020\u0018H\u0017J\u001a\u0010'\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u001a\u0010(\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\b\u0010)\u001a\u0004\u0018\u00010\rH\u0017J\u001a\u0010*\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\b\u0010+\u001a\u0004\u0018\u00010\rH\u0017J\u001a\u0010,\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\b\u0010\u0017\u001a\u0004\u0018\u00010\rH\u0016J\u001a\u0010-\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0017\u001a\u00020\u0014H\u0016J\u001a\u0010.\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u001a\u0010/\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u001a\u00100\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\b\u0010\u0017\u001a\u0004\u0018\u00010\rH\u0016J\u001a\u00101\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\b\u0010)\u001a\u0004\u0018\u00010\rH\u0017J\u001a\u00102\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\b\u00103\u001a\u0004\u0018\u00010\rH\u0017J\u001a\u00104\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\b\u00105\u001a\u0004\u0018\u00010\rH\u0017J\u001f\u00106\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\b\u00107\u001a\u0004\u0018\u00010\u0015H\u0017¢\u0006\u0002\u0010$J\u0018\u00108\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u00109\u001a\u00020\u0018H\u0017J\u001a\u0010:\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\b\u0010;\u001a\u0004\u0018\u00010\rH\u0017J\u0018\u0010<\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010=\u001a\u00020\u0018H\u0017J\u001c\u0010>\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00022\b\u0010\u0017\u001a\u0004\u0018\u00010\rH\u0016J\u001a\u0010?\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0017\u001a\u00020\u0015H\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006A"}, d2 = {"Lcom/swmansion/rnscreens/ScreenViewManager;", "Lcom/facebook/react/uimanager/ViewGroupManager;", "Lcom/swmansion/rnscreens/Screen;", "Lcom/facebook/react/viewmanagers/RNSScreenManagerInterface;", "()V", "mDelegate", "Lcom/facebook/react/uimanager/ViewManagerDelegate;", "createViewInstance", "reactContext", "Lcom/facebook/react/uimanager/ThemedReactContext;", "getDelegate", "getExportedCustomDirectEventTypeConstants", "", "", "", "getName", "setActivityState", "", "view", "activityState", "", "", "setCustomAnimationOnSwipe", "value", "", "setFullScreenSwipeEnabled", "setGestureEnabled", "gestureEnabled", "setGestureResponseDistance", "Lcom/facebook/react/bridge/ReadableMap;", "setHideKeyboardOnSwipe", "setHomeIndicatorHidden", "setNativeBackButtonDismissalEnabled", "nativeBackButtonDismissalEnabled", "setNavigationBarColor", "navigationBarColor", "(Lcom/swmansion/rnscreens/Screen;Ljava/lang/Integer;)V", "setNavigationBarHidden", "navigationBarHidden", "setPreventNativeDismiss", "setReplaceAnimation", "animation", "setScreenOrientation", "screenOrientation", "setSheetAllowedDetents", "setSheetCornerRadius", "setSheetExpandsWhenScrolledToEdge", "setSheetGrabberVisible", "setSheetLargestUndimmedDetent", "setStackAnimation", "setStackPresentation", "presentation", "setStatusBarAnimation", "statusBarAnimation", "setStatusBarColor", "statusBarColor", "setStatusBarHidden", "statusBarHidden", "setStatusBarStyle", "statusBarStyle", "setStatusBarTranslucent", "statusBarTranslucent", "setSwipeDirection", "setTransitionDuration", "Companion", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class ScreenViewManager extends ViewGroupManager<Screen> implements RNSScreenManagerInterface<Screen> {
    public static final Companion Companion = new Companion(null);
    public static final String REACT_CLASS = "RNSScreen";
    private final ViewManagerDelegate<Screen> mDelegate = new RNSScreenManagerDelegate(this);

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setCustomAnimationOnSwipe(Screen screen, boolean z) {
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setFullScreenSwipeEnabled(Screen screen, boolean z) {
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setGestureResponseDistance(Screen screen, ReadableMap readableMap) {
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setHideKeyboardOnSwipe(Screen screen, boolean z) {
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setHomeIndicatorHidden(Screen screen, boolean z) {
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setPreventNativeDismiss(Screen screen, boolean z) {
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setSheetAllowedDetents(Screen view, String str) {
        Intrinsics.checkNotNullParameter(view, "view");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setSheetCornerRadius(Screen screen, float f) {
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setSheetExpandsWhenScrolledToEdge(Screen screen, boolean z) {
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setSheetGrabberVisible(Screen screen, boolean z) {
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setSheetLargestUndimmedDetent(Screen view, String str) {
        Intrinsics.checkNotNullParameter(view, "view");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setSwipeDirection(Screen screen, String str) {
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setTransitionDuration(Screen screen, int i) {
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Screen createViewInstance(ThemedReactContext reactContext) {
        Intrinsics.checkNotNullParameter(reactContext, "reactContext");
        return new Screen(reactContext);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    public void setActivityState(Screen view, float f) {
        Intrinsics.checkNotNullParameter(view, "view");
        setActivityState(view, (int) f);
    }

    @ReactProp(name = "activityState")
    public final void setActivityState(Screen view, int i) {
        Intrinsics.checkNotNullParameter(view, "view");
        if (i == -1) {
            return;
        }
        if (i == 0) {
            view.setActivityState(Screen.ActivityState.INACTIVE);
        } else if (i == 1) {
            view.setActivityState(Screen.ActivityState.TRANSITIONING_OR_BELOW_TOP);
        } else if (i != 2) {
        } else {
            view.setActivityState(Screen.ActivityState.ON_TOP);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:72:0x0015, code lost:
        if (r4.equals("formSheet") != false) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x001e, code lost:
        if (r4.equals("fullScreenModal") != false) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0027, code lost:
        if (r4.equals("containedTransparentModal") != false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0030, code lost:
        if (r4.equals("containedModal") != false) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0039, code lost:
        if (r4.equals("modal") != false) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x003b, code lost:
        r4 = com.swmansion.rnscreens.Screen.StackPresentation.MODAL;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x004f, code lost:
        if (r4.equals("transparentModal") != false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0051, code lost:
        r4 = com.swmansion.rnscreens.Screen.StackPresentation.TRANSPARENT_MODAL;
     */
    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    @com.facebook.react.uimanager.annotations.ReactProp(name = "stackPresentation")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setStackPresentation(com.swmansion.rnscreens.Screen r3, java.lang.String r4) {
        /*
            r2 = this;
            java.lang.String r0 = "view"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
            if (r4 == 0) goto L57
            int r0 = r4.hashCode()
            switch(r0) {
                case -76271493: goto L49;
                case 3452698: goto L3e;
                case 104069805: goto L33;
                case 438078970: goto L2a;
                case 955284238: goto L21;
                case 1171936146: goto L18;
                case 1798290171: goto Lf;
                default: goto Le;
            }
        Le:
            goto L57
        Lf:
            java.lang.String r0 = "formSheet"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L57
            goto L3b
        L18:
            java.lang.String r0 = "fullScreenModal"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L57
            goto L3b
        L21:
            java.lang.String r0 = "containedTransparentModal"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L57
            goto L51
        L2a:
            java.lang.String r0 = "containedModal"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L57
            goto L3b
        L33:
            java.lang.String r0 = "modal"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L57
        L3b:
            com.swmansion.rnscreens.Screen$StackPresentation r4 = com.swmansion.rnscreens.Screen.StackPresentation.MODAL
            goto L53
        L3e:
            java.lang.String r0 = "push"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L57
            com.swmansion.rnscreens.Screen$StackPresentation r4 = com.swmansion.rnscreens.Screen.StackPresentation.PUSH
            goto L53
        L49:
            java.lang.String r0 = "transparentModal"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L57
        L51:
            com.swmansion.rnscreens.Screen$StackPresentation r4 = com.swmansion.rnscreens.Screen.StackPresentation.TRANSPARENT_MODAL
        L53:
            r3.setStackPresentation(r4)
            return
        L57:
            com.facebook.react.bridge.JSApplicationIllegalArgumentException r3 = new com.facebook.react.bridge.JSApplicationIllegalArgumentException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Unknown presentation type "
            r0.<init>(r1)
            r0.append(r4)
            java.lang.String r4 = r0.toString()
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.swmansion.rnscreens.ScreenViewManager.setStackPresentation(com.swmansion.rnscreens.Screen, java.lang.String):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:110:0x006a, code lost:
        if (r4.equals("simple_push") != false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0021, code lost:
        if (r4.equals(com.facebook.hermes.intl.Constants.COLLATION_DEFAULT) != false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0040, code lost:
        if (r4.equals("flip") != false) goto L34;
     */
    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    @com.facebook.react.uimanager.annotations.ReactProp(name = "stackAnimation")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setStackAnimation(com.swmansion.rnscreens.Screen r3, java.lang.String r4) {
        /*
            r2 = this;
            java.lang.String r0 = "view"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
            if (r4 == 0) goto L81
            int r0 = r4.hashCode()
            switch(r0) {
                case -1418955385: goto L64;
                case -427095442: goto L59;
                case -349395819: goto L4e;
                case 3135100: goto L43;
                case 3145837: goto L3a;
                case 3387192: goto L2f;
                case 182437661: goto L24;
                case 1544803905: goto L1b;
                case 1601504978: goto Lf;
                default: goto Le;
            }
        Le:
            goto L6d
        Lf:
            java.lang.String r0 = "slide_from_bottom"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L6d
            com.swmansion.rnscreens.Screen$StackAnimation r4 = com.swmansion.rnscreens.Screen.StackAnimation.SLIDE_FROM_BOTTOM
            goto L83
        L1b:
            java.lang.String r0 = "default"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L6d
            goto L81
        L24:
            java.lang.String r0 = "fade_from_bottom"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L6d
            com.swmansion.rnscreens.Screen$StackAnimation r4 = com.swmansion.rnscreens.Screen.StackAnimation.FADE_FROM_BOTTOM
            goto L83
        L2f:
            java.lang.String r0 = "none"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L6d
            com.swmansion.rnscreens.Screen$StackAnimation r4 = com.swmansion.rnscreens.Screen.StackAnimation.NONE
            goto L83
        L3a:
            java.lang.String r0 = "flip"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L6d
            goto L81
        L43:
            java.lang.String r0 = "fade"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L6d
            com.swmansion.rnscreens.Screen$StackAnimation r4 = com.swmansion.rnscreens.Screen.StackAnimation.FADE
            goto L83
        L4e:
            java.lang.String r0 = "slide_from_right"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L6d
            com.swmansion.rnscreens.Screen$StackAnimation r4 = com.swmansion.rnscreens.Screen.StackAnimation.SLIDE_FROM_RIGHT
            goto L83
        L59:
            java.lang.String r0 = "slide_from_left"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L6d
            com.swmansion.rnscreens.Screen$StackAnimation r4 = com.swmansion.rnscreens.Screen.StackAnimation.SLIDE_FROM_LEFT
            goto L83
        L64:
            java.lang.String r0 = "simple_push"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L6d
            goto L81
        L6d:
            com.facebook.react.bridge.JSApplicationIllegalArgumentException r3 = new com.facebook.react.bridge.JSApplicationIllegalArgumentException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Unknown animation type "
            r0.<init>(r1)
            r0.append(r4)
            java.lang.String r4 = r0.toString()
            r3.<init>(r4)
            throw r3
        L81:
            com.swmansion.rnscreens.Screen$StackAnimation r4 = com.swmansion.rnscreens.Screen.StackAnimation.DEFAULT
        L83:
            r3.setStackAnimation(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.swmansion.rnscreens.ScreenViewManager.setStackAnimation(com.swmansion.rnscreens.Screen, java.lang.String):void");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    @ReactProp(defaultBoolean = true, name = "gestureEnabled")
    public void setGestureEnabled(Screen view, boolean z) {
        Intrinsics.checkNotNullParameter(view, "view");
        view.setGestureEnabled(z);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    @ReactProp(name = "replaceAnimation")
    public void setReplaceAnimation(Screen view, String str) {
        Screen.ReplaceAnimation replaceAnimation;
        Intrinsics.checkNotNullParameter(view, "view");
        if (str == null ? true : Intrinsics.areEqual(str, "pop")) {
            replaceAnimation = Screen.ReplaceAnimation.POP;
        } else if (!Intrinsics.areEqual(str, "push")) {
            throw new JSApplicationIllegalArgumentException("Unknown replace animation type " + str);
        } else {
            replaceAnimation = Screen.ReplaceAnimation.PUSH;
        }
        view.setReplaceAnimation(replaceAnimation);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    @ReactProp(name = "screenOrientation")
    public void setScreenOrientation(Screen view, String str) {
        Intrinsics.checkNotNullParameter(view, "view");
        view.setScreenOrientation(str);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    @ReactProp(name = "statusBarAnimation")
    public void setStatusBarAnimation(Screen view, String str) {
        Intrinsics.checkNotNullParameter(view, "view");
        view.setStatusBarAnimated(Boolean.valueOf((str == null || Intrinsics.areEqual(ViewProps.NONE, str)) ? false : true));
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    @ReactProp(customType = "Color", name = "statusBarColor")
    public void setStatusBarColor(Screen view, Integer num) {
        Intrinsics.checkNotNullParameter(view, "view");
        view.setStatusBarColor(num);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    @ReactProp(name = "statusBarStyle")
    public void setStatusBarStyle(Screen view, String str) {
        Intrinsics.checkNotNullParameter(view, "view");
        view.setStatusBarStyle(str);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    @ReactProp(name = "statusBarTranslucent")
    public void setStatusBarTranslucent(Screen view, boolean z) {
        Intrinsics.checkNotNullParameter(view, "view");
        view.setStatusBarTranslucent(Boolean.valueOf(z));
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    @ReactProp(name = "statusBarHidden")
    public void setStatusBarHidden(Screen view, boolean z) {
        Intrinsics.checkNotNullParameter(view, "view");
        view.setStatusBarHidden(Boolean.valueOf(z));
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    @ReactProp(customType = "Color", name = "navigationBarColor")
    public void setNavigationBarColor(Screen view, Integer num) {
        Intrinsics.checkNotNullParameter(view, "view");
        view.setNavigationBarColor(num);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    @ReactProp(name = "navigationBarHidden")
    public void setNavigationBarHidden(Screen view, boolean z) {
        Intrinsics.checkNotNullParameter(view, "view");
        view.setNavigationBarHidden(Boolean.valueOf(z));
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenManagerInterface
    @ReactProp(name = "nativeBackButtonDismissalEnabled")
    public void setNativeBackButtonDismissalEnabled(Screen view, boolean z) {
        Intrinsics.checkNotNullParameter(view, "view");
        view.setNativeBackButtonDismissalEnabled(z);
    }

    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        Map<String, Object> of = MapBuilder.of(ScreenDismissedEvent.EVENT_NAME, MapBuilder.of("registrationName", "onDismissed"), ScreenWillAppearEvent.EVENT_NAME, MapBuilder.of("registrationName", "onWillAppear"), ScreenAppearEvent.EVENT_NAME, MapBuilder.of("registrationName", "onAppear"), ScreenWillDisappearEvent.EVENT_NAME, MapBuilder.of("registrationName", "onWillDisappear"), ScreenDisappearEvent.EVENT_NAME, MapBuilder.of("registrationName", "onDisappear"), HeaderBackButtonClickedEvent.EVENT_NAME, MapBuilder.of("registrationName", "onHeaderBackButtonClicked"), ScreenTransitionProgressEvent.EVENT_NAME, MapBuilder.of("registrationName", "onTransitionProgress"));
        Intrinsics.checkNotNullExpressionValue(of, "of(\n            ScreenDi…itionProgress\")\n        )");
        return of;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public ViewManagerDelegate<Screen> getDelegate() {
        return this.mDelegate;
    }

    /* compiled from: ScreenViewManager.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lcom/swmansion/rnscreens/ScreenViewManager$Companion;", "", "()V", "REACT_CLASS", "", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
