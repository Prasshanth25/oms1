package com.swmansion.rnscreens;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.swmansion.rnscreens.events.HeaderBackButtonClickedEvent;
import com.swmansion.rnscreens.events.ScreenAppearEvent;
import com.swmansion.rnscreens.events.ScreenDisappearEvent;
import com.swmansion.rnscreens.events.ScreenDismissedEvent;
import com.swmansion.rnscreens.events.ScreenTransitionProgressEvent;
import com.swmansion.rnscreens.events.ScreenWillAppearEvent;
import com.swmansion.rnscreens.events.ScreenWillDisappearEvent;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ScreenFragment.kt */
@Metadata(d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0016\u0018\u0000 @2\u00020\u0001:\u0003@ABB\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u000f\b\u0017\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\u0019\u001a\u00020\u00072\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u0018\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u0000H\u0002J\u0010\u0010\u001f\u001a\u00020\u001d2\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u0006\u0010 \u001a\u00020\u001dJ\b\u0010!\u001a\u00020\u001dH\u0002J\b\u0010\"\u001a\u00020\u001dH\u0002J\b\u0010#\u001a\u00020\u001dH\u0002J\b\u0010$\u001a\u00020\u001dH\u0002J\u0016\u0010%\u001a\u00020\u001d2\u0006\u0010&\u001a\u00020\u00122\u0006\u0010'\u001a\u00020\u0007J\u0010\u0010(\u001a\u00020\u001d2\u0006\u0010)\u001a\u00020\u0007H\u0002J\b\u0010*\u001a\u00020\u001dH\u0016J&\u0010+\u001a\u0004\u0018\u00010,2\u0006\u0010-\u001a\u00020.2\b\u0010/\u001a\u0004\u0018\u0001002\b\u00101\u001a\u0004\u0018\u000102H\u0016J\b\u00103\u001a\u00020\u001dH\u0016J\b\u00104\u001a\u00020\u001dH\u0016J\b\u00105\u001a\u00020\u001dH\u0016J\u0006\u00106\u001a\u00020\u001dJ\u0012\u00107\u001a\u00020\u001d2\n\u00108\u001a\u0006\u0012\u0002\b\u00030\u000bJ\u0010\u00109\u001a\u00020\u001d2\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\b\u0010:\u001a\u0004\u0018\u00010;J\b\u0010<\u001a\u0004\u0018\u00010=J\u0012\u0010>\u001a\u00020\u001d2\n\u00108\u001a\u0006\u0012\u0002\b\u00030\u000bJ\b\u0010?\u001a\u00020\u001dH\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u000b0\n8F¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\u000f\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u000b0\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R \u0010\u0013\u001a\u00020\u0004X\u0086.¢\u0006\u0014\n\u0000\u0012\u0004\b\u0014\u0010\u0002\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0005R\u000e\u0010\u0018\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006C"}, d2 = {"Lcom/swmansion/rnscreens/ScreenFragment;", "Landroidx/fragment/app/Fragment;", "()V", "screenView", "Lcom/swmansion/rnscreens/Screen;", "(Lcom/swmansion/rnscreens/Screen;)V", "canDispatchAppear", "", "canDispatchWillAppear", "childScreenContainers", "", "Lcom/swmansion/rnscreens/ScreenContainer;", "getChildScreenContainers", "()Ljava/util/List;", "isTransitioning", "mChildScreenContainers", "", "mProgress", "", "screen", "getScreen$annotations", "getScreen", "()Lcom/swmansion/rnscreens/Screen;", "setScreen", "shouldUpdateOnResume", "canDispatchEvent", NotificationCompat.CATEGORY_EVENT, "Lcom/swmansion/rnscreens/ScreenFragment$ScreenLifecycleEvent;", "dispatchEvent", "", "fragment", "dispatchEventInChildContainers", "dispatchHeaderBackButtonClickedEvent", "dispatchOnAppear", "dispatchOnDisappear", "dispatchOnWillAppear", "dispatchOnWillDisappear", "dispatchTransitionProgress", "alpha", "closing", "dispatchViewAnimationEvent", "animationEnd", "onContainerUpdate", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onResume", "onViewAnimationEnd", "onViewAnimationStart", "registerChildScreenContainer", "screenContainer", "setLastEventDispatched", "tryGetActivity", "Landroid/app/Activity;", "tryGetContext", "Lcom/facebook/react/bridge/ReactContext;", "unregisterChildScreenContainer", "updateWindowTraits", "Companion", "ScreenLifecycleEvent", "ScreensFrameLayout", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public class ScreenFragment extends Fragment {
    public static final Companion Companion = new Companion(null);
    private boolean canDispatchAppear;
    private boolean canDispatchWillAppear;
    private boolean isTransitioning;
    private final List<ScreenContainer<?>> mChildScreenContainers;
    private float mProgress;
    public Screen screen;
    private boolean shouldUpdateOnResume;

    /* compiled from: ScreenFragment.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006¨\u0006\u0007"}, d2 = {"Lcom/swmansion/rnscreens/ScreenFragment$ScreenLifecycleEvent;", "", "(Ljava/lang/String;I)V", "Appear", "WillAppear", "Disappear", "WillDisappear", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum ScreenLifecycleEvent {
        Appear,
        WillAppear,
        Disappear,
        WillDisappear
    }

    /* compiled from: ScreenFragment.kt */
    @Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[ScreenLifecycleEvent.values().length];
            iArr[ScreenLifecycleEvent.WillAppear.ordinal()] = 1;
            iArr[ScreenLifecycleEvent.Appear.ordinal()] = 2;
            iArr[ScreenLifecycleEvent.WillDisappear.ordinal()] = 3;
            iArr[ScreenLifecycleEvent.Disappear.ordinal()] = 4;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static /* synthetic */ void getScreen$annotations() {
    }

    @JvmStatic
    public static final View recycleView(View view) {
        return Companion.recycleView(view);
    }

    public final Screen getScreen() {
        Screen screen = this.screen;
        if (screen != null) {
            return screen;
        }
        Intrinsics.throwUninitializedPropertyAccessException("screen");
        return null;
    }

    public final void setScreen(Screen screen) {
        Intrinsics.checkNotNullParameter(screen, "<set-?>");
        this.screen = screen;
    }

    public ScreenFragment() {
        this.mChildScreenContainers = new ArrayList();
        this.mProgress = -1.0f;
        this.canDispatchWillAppear = true;
        this.canDispatchAppear = true;
        throw new IllegalStateException("Screen fragments should never be restored. Follow instructions from https://github.com/software-mansion/react-native-screens/issues/17#issuecomment-424704067 to properly configure your main activity.");
    }

    public ScreenFragment(Screen screenView) {
        Intrinsics.checkNotNullParameter(screenView, "screenView");
        this.mChildScreenContainers = new ArrayList();
        this.mProgress = -1.0f;
        this.canDispatchWillAppear = true;
        this.canDispatchAppear = true;
        setScreen(screenView);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.shouldUpdateOnResume) {
            this.shouldUpdateOnResume = false;
            ScreenWindowTraits.INSTANCE.trySetWindowTraits$react_native_screens_release(getScreen(), tryGetActivity(), tryGetContext());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        ScreensFrameLayout screensFrameLayout;
        Intrinsics.checkNotNullParameter(inflater, "inflater");
        getScreen().setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        Context context = getContext();
        if (context != null) {
            screensFrameLayout = new ScreensFrameLayout(context);
            screensFrameLayout.addView(recycleView(getScreen()));
        } else {
            screensFrameLayout = null;
        }
        return screensFrameLayout;
    }

    /* compiled from: ScreenFragment.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0007"}, d2 = {"Lcom/swmansion/rnscreens/ScreenFragment$ScreensFrameLayout;", "Landroid/widget/FrameLayout;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "clearFocus", "", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    private static final class ScreensFrameLayout extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ScreensFrameLayout(Context context) {
            super(context);
            Intrinsics.checkNotNullParameter(context, "context");
        }

        @Override // android.view.ViewGroup, android.view.View
        public void clearFocus() {
            if (getVisibility() != 4) {
                super.clearFocus();
            }
        }
    }

    public void onContainerUpdate() {
        updateWindowTraits();
    }

    private final void updateWindowTraits() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            this.shouldUpdateOnResume = true;
        } else {
            ScreenWindowTraits.INSTANCE.trySetWindowTraits$react_native_screens_release(getScreen(), activity, tryGetContext());
        }
    }

    public final Activity tryGetActivity() {
        ScreenFragment fragment;
        FragmentActivity activity;
        FragmentActivity activity2 = getActivity();
        if (activity2 != null) {
            return activity2;
        }
        Context context = getScreen().getContext();
        if (context instanceof ReactContext) {
            ReactContext reactContext = (ReactContext) context;
            if (reactContext.getCurrentActivity() != null) {
                return reactContext.getCurrentActivity();
            }
        }
        for (ScreenContainer<?> container = getScreen().getContainer(); container != null; container = container.getParent()) {
            if ((container instanceof Screen) && (fragment = ((Screen) container).getFragment()) != null && (activity = fragment.getActivity()) != null) {
                return activity;
            }
        }
        return null;
    }

    public final ReactContext tryGetContext() {
        if (getContext() instanceof ReactContext) {
            Context context = getContext();
            if (context != null) {
                return (ReactContext) context;
            }
            throw new NullPointerException("null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
        } else if (getScreen().getContext() instanceof ReactContext) {
            Context context2 = getScreen().getContext();
            if (context2 != null) {
                return (ReactContext) context2;
            }
            throw new NullPointerException("null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
        } else {
            for (ScreenContainer<?> container = getScreen().getContainer(); container != null; container = container.getParent()) {
                if (container instanceof Screen) {
                    Screen screen = (Screen) container;
                    if (screen.getContext() instanceof ReactContext) {
                        Context context3 = screen.getContext();
                        if (context3 != null) {
                            return (ReactContext) context3;
                        }
                        throw new NullPointerException("null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
                    }
                }
            }
            return null;
        }
    }

    public final List<ScreenContainer<?>> getChildScreenContainers() {
        return this.mChildScreenContainers;
    }

    private final boolean canDispatchEvent(ScreenLifecycleEvent screenLifecycleEvent) {
        int i = WhenMappings.$EnumSwitchMapping$0[screenLifecycleEvent.ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i != 4) {
                        throw new NoWhenBranchMatchedException();
                    }
                    if (!this.canDispatchAppear) {
                        return true;
                    }
                } else if (!this.canDispatchWillAppear) {
                    return true;
                }
                return false;
            }
            return this.canDispatchAppear;
        }
        return this.canDispatchWillAppear;
    }

    private final void setLastEventDispatched(ScreenLifecycleEvent screenLifecycleEvent) {
        int i = WhenMappings.$EnumSwitchMapping$0[screenLifecycleEvent.ordinal()];
        if (i == 1) {
            this.canDispatchWillAppear = false;
        } else if (i == 2) {
            this.canDispatchAppear = false;
        } else if (i == 3) {
            this.canDispatchWillAppear = true;
        } else if (i != 4) {
        } else {
            this.canDispatchAppear = true;
        }
    }

    private final void dispatchOnWillAppear() {
        dispatchEvent(ScreenLifecycleEvent.WillAppear, this);
        dispatchTransitionProgress(0.0f, false);
    }

    private final void dispatchOnAppear() {
        dispatchEvent(ScreenLifecycleEvent.Appear, this);
        dispatchTransitionProgress(1.0f, false);
    }

    private final void dispatchOnWillDisappear() {
        dispatchEvent(ScreenLifecycleEvent.WillDisappear, this);
        dispatchTransitionProgress(0.0f, true);
    }

    private final void dispatchOnDisappear() {
        dispatchEvent(ScreenLifecycleEvent.Disappear, this);
        dispatchTransitionProgress(1.0f, true);
    }

    private final void dispatchEvent(ScreenLifecycleEvent screenLifecycleEvent, ScreenFragment screenFragment) {
        ScreenWillAppearEvent screenWillAppearEvent;
        if ((screenFragment instanceof ScreenStackFragment) && screenFragment.canDispatchEvent(screenLifecycleEvent)) {
            Screen screen = screenFragment.getScreen();
            screenFragment.setLastEventDispatched(screenLifecycleEvent);
            int i = WhenMappings.$EnumSwitchMapping$0[screenLifecycleEvent.ordinal()];
            if (i == 1) {
                screenWillAppearEvent = new ScreenWillAppearEvent(screen.getId());
            } else if (i == 2) {
                screenWillAppearEvent = new ScreenAppearEvent(screen.getId());
            } else if (i == 3) {
                screenWillAppearEvent = new ScreenWillDisappearEvent(screen.getId());
            } else if (i != 4) {
                throw new NoWhenBranchMatchedException();
            } else {
                screenWillAppearEvent = new ScreenDisappearEvent(screen.getId());
            }
            Context context = getScreen().getContext();
            if (context == null) {
                throw new NullPointerException("null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
            }
            EventDispatcher eventDispatcherForReactTag = UIManagerHelper.getEventDispatcherForReactTag((ReactContext) context, getScreen().getId());
            if (eventDispatcherForReactTag != null) {
                eventDispatcherForReactTag.dispatchEvent(screenWillAppearEvent);
            }
            screenFragment.dispatchEventInChildContainers(screenLifecycleEvent);
        }
    }

    private final void dispatchEventInChildContainers(ScreenLifecycleEvent screenLifecycleEvent) {
        ScreenFragment fragment;
        ArrayList<ScreenContainer> arrayList = new ArrayList();
        for (Object obj : this.mChildScreenContainers) {
            if (((ScreenContainer) obj).getScreenCount() > 0) {
                arrayList.add(obj);
            }
        }
        for (ScreenContainer screenContainer : arrayList) {
            Screen topScreen = screenContainer.getTopScreen();
            if (topScreen != null && (fragment = topScreen.getFragment()) != null) {
                dispatchEvent(screenLifecycleEvent, fragment);
            }
        }
    }

    public final void dispatchHeaderBackButtonClickedEvent() {
        Context context = getScreen().getContext();
        if (context == null) {
            throw new NullPointerException("null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
        }
        EventDispatcher eventDispatcherForReactTag = UIManagerHelper.getEventDispatcherForReactTag((ReactContext) context, getScreen().getId());
        if (eventDispatcherForReactTag != null) {
            eventDispatcherForReactTag.dispatchEvent(new HeaderBackButtonClickedEvent(getScreen().getId()));
        }
    }

    public final void dispatchTransitionProgress(float f, boolean z) {
        if (this instanceof ScreenStackFragment) {
            if (this.mProgress == f) {
                return;
            }
            float max = Math.max(0.0f, Math.min(1.0f, f));
            this.mProgress = max;
            if (!(max == 0.0f)) {
                r1 = (max != 1.0f ? 0 : 1) != 0 ? 2 : 3;
            }
            short s = (short) r1;
            ScreenContainer<?> container = getScreen().getContainer();
            boolean goingForward = container instanceof ScreenStack ? ((ScreenStack) container).getGoingForward() : false;
            Context context = getScreen().getContext();
            if (context == null) {
                throw new NullPointerException("null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
            }
            EventDispatcher eventDispatcherForReactTag = UIManagerHelper.getEventDispatcherForReactTag((ReactContext) context, getScreen().getId());
            if (eventDispatcherForReactTag != null) {
                eventDispatcherForReactTag.dispatchEvent(new ScreenTransitionProgressEvent(getScreen().getId(), this.mProgress, z, goingForward, s));
            }
        }
    }

    public final void registerChildScreenContainer(ScreenContainer<?> screenContainer) {
        Intrinsics.checkNotNullParameter(screenContainer, "screenContainer");
        this.mChildScreenContainers.add(screenContainer);
    }

    public final void unregisterChildScreenContainer(ScreenContainer<?> screenContainer) {
        Intrinsics.checkNotNullParameter(screenContainer, "screenContainer");
        this.mChildScreenContainers.remove(screenContainer);
    }

    public final void onViewAnimationStart() {
        dispatchViewAnimationEvent(false);
    }

    public void onViewAnimationEnd() {
        dispatchViewAnimationEvent(true);
    }

    private final void dispatchViewAnimationEvent(final boolean z) {
        this.isTransitioning = !z;
        Fragment parentFragment = getParentFragment();
        if (parentFragment == null || ((parentFragment instanceof ScreenFragment) && !((ScreenFragment) parentFragment).isTransitioning)) {
            if (isResumed()) {
                UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.swmansion.rnscreens.ScreenFragment$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScreenFragment.m419dispatchViewAnimationEvent$lambda8(z, this);
                    }
                });
            } else if (z) {
                dispatchOnDisappear();
            } else {
                dispatchOnWillDisappear();
            }
        }
    }

    /* renamed from: dispatchViewAnimationEvent$lambda-8 */
    public static final void m419dispatchViewAnimationEvent$lambda8(boolean z, ScreenFragment this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (z) {
            this$0.dispatchOnAppear();
        } else {
            this$0.dispatchOnWillAppear();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        EventDispatcher eventDispatcherForReactTag;
        super.onDestroy();
        ScreenContainer<?> container = getScreen().getContainer();
        if (container == null || !container.hasScreen(this)) {
            Context context = getScreen().getContext();
            if ((context instanceof ReactContext) && (eventDispatcherForReactTag = UIManagerHelper.getEventDispatcherForReactTag((ReactContext) context, getScreen().getId())) != null) {
                eventDispatcherForReactTag.dispatchEvent(new ScreenDismissedEvent(getScreen().getId()));
            }
        }
        this.mChildScreenContainers.clear();
    }

    /* compiled from: ScreenFragment.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0005¨\u0006\u0006"}, d2 = {"Lcom/swmansion/rnscreens/ScreenFragment$Companion;", "", "()V", "recycleView", "Landroid/view/View;", "view", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        protected final View recycleView(View view) {
            Intrinsics.checkNotNullParameter(view, "view");
            ViewParent parent = view.getParent();
            if (parent != null) {
                ViewGroup viewGroup = (ViewGroup) parent;
                viewGroup.endViewTransition(view);
                viewGroup.removeView(view);
            }
            view.setVisibility(0);
            return view;
        }
    }
}
