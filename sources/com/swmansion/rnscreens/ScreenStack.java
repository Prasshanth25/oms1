package com.swmansion.rnscreens;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import androidx.fragment.app.FragmentTransaction;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.swmansion.rnscreens.Screen;
import com.swmansion.rnscreens.events.StackFinishTransitioningEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.ranges.RangesKt;

/* compiled from: ScreenStack.kt */
@Metadata(d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010#\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0010\u0018\u0000 A2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002ABB\u000f\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010!\u001a\u00020\u00022\u0006\u0010\"\u001a\u00020\u001cH\u0014J\u000e\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u0002J\u0010\u0010&\u001a\u00020$2\u0006\u0010'\u001a\u00020(H\u0014J\b\u0010)\u001a\u00020$H\u0002J\b\u0010*\u001a\u00020$H\u0002J \u0010+\u001a\u00020\u000b2\u0006\u0010'\u001a\u00020(2\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/H\u0014J\u0010\u00100\u001a\u00020$2\u0006\u00101\u001a\u00020-H\u0016J\u0012\u00102\u001a\u00020\u000b2\b\u0010%\u001a\u0004\u0018\u000103H\u0016J\b\u00104\u001a\u00020$H\u0014J\f\u00105\u001a\u00060\bR\u00020\u0000H\u0002J\b\u00106\u001a\u00020$H\u0016J\u0006\u00107\u001a\u00020$J\u0014\u00108\u001a\u00020$2\n\u00109\u001a\u00060\bR\u00020\u0000H\u0002J\b\u0010:\u001a\u00020$H\u0016J\u0010\u0010;\u001a\u00020$2\u0006\u0010<\u001a\u00020\u0019H\u0016J\u0010\u0010=\u001a\u00020$2\u0006\u00101\u001a\u00020-H\u0016J\u0010\u0010>\u001a\u00020$2\u0006\u00101\u001a\u00020-H\u0016J\u0012\u0010?\u001a\u00020$2\b\u0010@\u001a\u0004\u0018\u00010\u0002H\u0002R\u0018\u0010\u0006\u001a\f\u0012\b\u0012\u00060\bR\u00020\u00000\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0018\u0010\t\u001a\f\u0012\b\u0012\u00060\bR\u00020\u00000\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0014\u001a\u0012\u0012\u0004\u0012\u00020\u00020\u0015j\b\u0012\u0004\u0012\u00020\u0002`\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0002X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u001b\u001a\u00020\u001c8F¢\u0006\u0006\u001a\u0004\b\u001d\u0010\u001eR\u0016\u0010\u001f\u001a\u0004\u0018\u00010\u001c8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b \u0010\u001e¨\u0006C"}, d2 = {"Lcom/swmansion/rnscreens/ScreenStack;", "Lcom/swmansion/rnscreens/ScreenContainer;", "Lcom/swmansion/rnscreens/ScreenStackFragment;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "drawingOpPool", "", "Lcom/swmansion/rnscreens/ScreenStack$DrawingOp;", "drawingOps", "goingForward", "", "getGoingForward", "()Z", "setGoingForward", "(Z)V", "isDetachingCurrentScreen", "mDismissed", "", "mRemovalTransitionStarted", "mStack", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "mTopScreen", "previousChildrenCount", "", "reverseLastTwoChildren", "rootScreen", "Lcom/swmansion/rnscreens/Screen;", "getRootScreen", "()Lcom/swmansion/rnscreens/Screen;", "topScreen", "getTopScreen", "adapt", "screen", "dismiss", "", "screenFragment", "dispatchDraw", "canvas", "Landroid/graphics/Canvas;", "dispatchOnFinishTransitioning", "drawAndRelease", "drawChild", "child", "Landroid/view/View;", "drawingTime", "", "endViewTransition", "view", "hasScreen", "Lcom/swmansion/rnscreens/ScreenFragment;", "notifyContainerUpdate", "obtainDrawingOp", "onUpdate", "onViewAppearTransitionEnd", "performDraw", "op", "removeAllScreens", "removeScreenAt", "index", "removeView", "startViewTransition", "turnOffA11yUnderTransparentScreen", "visibleBottom", "Companion", "DrawingOp", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class ScreenStack extends ScreenContainer<ScreenStackFragment> {
    public static final Companion Companion = new Companion(null);
    private final List<DrawingOp> drawingOpPool;
    private List<DrawingOp> drawingOps;
    private boolean goingForward;
    private boolean isDetachingCurrentScreen;
    private final Set<ScreenStackFragment> mDismissed;
    private boolean mRemovalTransitionStarted;
    private final ArrayList<ScreenStackFragment> mStack;
    private ScreenStackFragment mTopScreen;
    private int previousChildrenCount;
    private boolean reverseLastTwoChildren;

    /* compiled from: ScreenStack.kt */
    @Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[Screen.StackAnimation.values().length];
            iArr[Screen.StackAnimation.DEFAULT.ordinal()] = 1;
            iArr[Screen.StackAnimation.NONE.ordinal()] = 2;
            iArr[Screen.StackAnimation.FADE.ordinal()] = 3;
            iArr[Screen.StackAnimation.SLIDE_FROM_RIGHT.ordinal()] = 4;
            iArr[Screen.StackAnimation.SLIDE_FROM_LEFT.ordinal()] = 5;
            iArr[Screen.StackAnimation.SLIDE_FROM_BOTTOM.ordinal()] = 6;
            iArr[Screen.StackAnimation.FADE_FROM_BOTTOM.ordinal()] = 7;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public ScreenStack(Context context) {
        super(context);
        this.mStack = new ArrayList<>();
        this.mDismissed = new HashSet();
        this.drawingOpPool = new ArrayList();
        this.drawingOps = new ArrayList();
    }

    public final boolean getGoingForward() {
        return this.goingForward;
    }

    public final void setGoingForward(boolean z) {
        this.goingForward = z;
    }

    public final void dismiss(ScreenStackFragment screenFragment) {
        Intrinsics.checkNotNullParameter(screenFragment, "screenFragment");
        this.mDismissed.add(screenFragment);
        performUpdatesNow();
    }

    @Override // com.swmansion.rnscreens.ScreenContainer
    public Screen getTopScreen() {
        ScreenStackFragment screenStackFragment = this.mTopScreen;
        if (screenStackFragment != null) {
            return screenStackFragment.getScreen();
        }
        return null;
    }

    public final Screen getRootScreen() {
        int screenCount = getScreenCount();
        for (int i = 0; i < screenCount; i++) {
            Screen screenAt = getScreenAt(i);
            if (!CollectionsKt.contains(this.mDismissed, screenAt.getFragment())) {
                return screenAt;
            }
        }
        throw new IllegalStateException("Stack has no root screen set");
    }

    @Override // com.swmansion.rnscreens.ScreenContainer
    public ScreenStackFragment adapt(Screen screen) {
        Intrinsics.checkNotNullParameter(screen, "screen");
        return new ScreenStackFragment(screen);
    }

    @Override // android.view.ViewGroup
    public void startViewTransition(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.startViewTransition(view);
        this.mRemovalTransitionStarted = true;
    }

    @Override // android.view.ViewGroup
    public void endViewTransition(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.endViewTransition(view);
        if (this.mRemovalTransitionStarted) {
            this.mRemovalTransitionStarted = false;
            dispatchOnFinishTransitioning();
        }
    }

    public final void onViewAppearTransitionEnd() {
        if (this.mRemovalTransitionStarted) {
            return;
        }
        dispatchOnFinishTransitioning();
    }

    private final void dispatchOnFinishTransitioning() {
        Context context = getContext();
        if (context == null) {
            throw new NullPointerException("null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
        }
        EventDispatcher eventDispatcherForReactTag = UIManagerHelper.getEventDispatcherForReactTag((ReactContext) context, getId());
        if (eventDispatcherForReactTag != null) {
            eventDispatcherForReactTag.dispatchEvent(new StackFinishTransitioningEvent(getId()));
        }
    }

    @Override // com.swmansion.rnscreens.ScreenContainer
    public void removeScreenAt(int i) {
        TypeIntrinsics.asMutableCollection(this.mDismissed).remove(getScreenAt(i).getFragment());
        super.removeScreenAt(i);
    }

    @Override // com.swmansion.rnscreens.ScreenContainer
    public void removeAllScreens() {
        this.mDismissed.clear();
        super.removeAllScreens();
    }

    @Override // com.swmansion.rnscreens.ScreenContainer
    public boolean hasScreen(ScreenFragment screenFragment) {
        return super.hasScreen(screenFragment) && !CollectionsKt.contains(this.mDismissed, screenFragment);
    }

    @Override // com.swmansion.rnscreens.ScreenContainer
    public void onUpdate() {
        boolean z;
        Screen screen;
        ScreenStackFragment screenStackFragment;
        Screen screen2;
        this.isDetachingCurrentScreen = false;
        int size = this.mScreenFragments.size() - 1;
        Screen.StackAnimation stackAnimation = null;
        final ScreenStackFragment screenStackFragment2 = null;
        ScreenStackFragment screenStackFragment3 = null;
        if (size >= 0) {
            while (true) {
                int i = size - 1;
                Object obj = this.mScreenFragments.get(size);
                Intrinsics.checkNotNullExpressionValue(obj, "mScreenFragments[i]");
                ScreenStackFragment screenStackFragment4 = (ScreenStackFragment) obj;
                if (!this.mDismissed.contains(screenStackFragment4)) {
                    if (screenStackFragment2 == null) {
                        screenStackFragment2 = screenStackFragment4;
                    } else {
                        screenStackFragment3 = screenStackFragment4;
                    }
                    if (!Companion.isTransparent(screenStackFragment4)) {
                        break;
                    }
                }
                if (i < 0) {
                    break;
                }
                size = i;
            }
        }
        boolean z2 = true;
        if (!CollectionsKt.contains(this.mStack, screenStackFragment2)) {
            ScreenStackFragment screenStackFragment5 = this.mTopScreen;
            if (screenStackFragment5 == null || screenStackFragment2 == null) {
                if (screenStackFragment5 == null && screenStackFragment2 != null) {
                    stackAnimation = Screen.StackAnimation.NONE;
                    this.goingForward = true;
                }
                z = true;
            } else {
                z = (screenStackFragment5 != null && this.mScreenFragments.contains(screenStackFragment5)) || (screenStackFragment2.getScreen().getReplaceAnimation() == Screen.ReplaceAnimation.PUSH);
                if (z) {
                    stackAnimation = screenStackFragment2.getScreen().getStackAnimation();
                } else {
                    ScreenStackFragment screenStackFragment6 = this.mTopScreen;
                    if (screenStackFragment6 != null && (screen2 = screenStackFragment6.getScreen()) != null) {
                        stackAnimation = screen2.getStackAnimation();
                    }
                }
            }
        } else {
            ScreenStackFragment screenStackFragment7 = this.mTopScreen;
            if (screenStackFragment7 != null && !Intrinsics.areEqual(screenStackFragment7, screenStackFragment2)) {
                ScreenStackFragment screenStackFragment8 = this.mTopScreen;
                if (screenStackFragment8 != null && (screen = screenStackFragment8.getScreen()) != null) {
                    stackAnimation = screen.getStackAnimation();
                }
                z = false;
            }
            z = true;
        }
        FragmentTransaction createTransaction = createTransaction();
        if (stackAnimation != null) {
            if (z) {
                switch (WhenMappings.$EnumSwitchMapping$0[stackAnimation.ordinal()]) {
                    case 1:
                        createTransaction.setCustomAnimations(R.anim.rns_default_enter_in, R.anim.rns_default_enter_out);
                        break;
                    case 2:
                        createTransaction.setCustomAnimations(R.anim.rns_no_animation_20, R.anim.rns_no_animation_20);
                        break;
                    case 3:
                        createTransaction.setCustomAnimations(R.anim.rns_fade_in, R.anim.rns_fade_out);
                        break;
                    case 4:
                        createTransaction.setCustomAnimations(R.anim.rns_slide_in_from_right, R.anim.rns_slide_out_to_left);
                        break;
                    case 5:
                        createTransaction.setCustomAnimations(R.anim.rns_slide_in_from_left, R.anim.rns_slide_out_to_right);
                        break;
                    case 6:
                        createTransaction.setCustomAnimations(R.anim.rns_slide_in_from_bottom, R.anim.rns_no_animation_medium);
                        break;
                    case 7:
                        createTransaction.setCustomAnimations(R.anim.rns_fade_from_bottom, R.anim.rns_no_animation_350);
                        break;
                }
            } else {
                switch (WhenMappings.$EnumSwitchMapping$0[stackAnimation.ordinal()]) {
                    case 1:
                        createTransaction.setCustomAnimations(R.anim.rns_default_exit_in, R.anim.rns_default_exit_out);
                        break;
                    case 2:
                        createTransaction.setCustomAnimations(R.anim.rns_no_animation_20, R.anim.rns_no_animation_20);
                        break;
                    case 3:
                        createTransaction.setCustomAnimations(R.anim.rns_fade_in, R.anim.rns_fade_out);
                        break;
                    case 4:
                        createTransaction.setCustomAnimations(R.anim.rns_slide_in_from_left, R.anim.rns_slide_out_to_right);
                        break;
                    case 5:
                        createTransaction.setCustomAnimations(R.anim.rns_slide_in_from_right, R.anim.rns_slide_out_to_left);
                        break;
                    case 6:
                        createTransaction.setCustomAnimations(R.anim.rns_no_animation_medium, R.anim.rns_slide_out_to_bottom);
                        break;
                    case 7:
                        createTransaction.setCustomAnimations(R.anim.rns_no_animation_250, R.anim.rns_fade_to_bottom);
                        break;
                }
            }
        }
        this.goingForward = z;
        if (z && screenStackFragment2 != null && Companion.needsDrawReordering(screenStackFragment2) && screenStackFragment3 == null) {
            this.isDetachingCurrentScreen = true;
        }
        Iterator<ScreenStackFragment> it = this.mStack.iterator();
        while (it.hasNext()) {
            ScreenStackFragment next = it.next();
            if (!this.mScreenFragments.contains(next) || this.mDismissed.contains(next)) {
                createTransaction.remove(next);
            }
        }
        Iterator it2 = this.mScreenFragments.iterator();
        while (it2.hasNext() && (screenStackFragment = (ScreenStackFragment) it2.next()) != screenStackFragment3) {
            if (screenStackFragment != screenStackFragment2 && !this.mDismissed.contains(screenStackFragment)) {
                createTransaction.remove(screenStackFragment);
            }
        }
        if (screenStackFragment3 != null && !screenStackFragment3.isAdded()) {
            Iterator it3 = this.mScreenFragments.iterator();
            while (it3.hasNext()) {
                ScreenStackFragment screenStackFragment9 = (ScreenStackFragment) it3.next();
                if (z2) {
                    if (screenStackFragment9 == screenStackFragment3) {
                        z2 = false;
                    }
                }
                createTransaction.add(getId(), screenStackFragment9).runOnCommit(new Runnable() { // from class: com.swmansion.rnscreens.ScreenStack$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScreenStack.m420onUpdate$lambda2$lambda1(ScreenStackFragment.this);
                    }
                });
            }
        } else if (screenStackFragment2 != null && !screenStackFragment2.isAdded()) {
            createTransaction.add(getId(), screenStackFragment2);
        }
        this.mTopScreen = screenStackFragment2;
        this.mStack.clear();
        this.mStack.addAll(this.mScreenFragments);
        turnOffA11yUnderTransparentScreen(screenStackFragment3);
        createTransaction.commitNowAllowingStateLoss();
    }

    /* renamed from: onUpdate$lambda-2$lambda-1 */
    public static final void m420onUpdate$lambda2$lambda1(ScreenStackFragment screenStackFragment) {
        Screen screen;
        if (screenStackFragment == null || (screen = screenStackFragment.getScreen()) == null) {
            return;
        }
        screen.bringToFront();
    }

    private final void turnOffA11yUnderTransparentScreen(ScreenStackFragment screenStackFragment) {
        ScreenStackFragment screenStackFragment2;
        if (this.mScreenFragments.size() > 1 && screenStackFragment != null && (screenStackFragment2 = this.mTopScreen) != null && Companion.isTransparent(screenStackFragment2)) {
            for (ScreenStackFragment screenStackFragment3 : CollectionsKt.asReversed(CollectionsKt.slice(this.mScreenFragments, RangesKt.until(0, this.mScreenFragments.size() - 1)))) {
                screenStackFragment3.getScreen().changeAccessibilityMode(4);
                if (Intrinsics.areEqual(screenStackFragment3, screenStackFragment)) {
                    break;
                }
            }
        }
        Screen topScreen = getTopScreen();
        if (topScreen != null) {
            topScreen.changeAccessibilityMode(0);
        }
    }

    @Override // com.swmansion.rnscreens.ScreenContainer
    protected void notifyContainerUpdate() {
        for (ScreenStackFragment screenStackFragment : this.mStack) {
            screenStackFragment.onContainerUpdate();
        }
    }

    @Override // com.swmansion.rnscreens.ScreenContainer, android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        if (this.isDetachingCurrentScreen) {
            this.isDetachingCurrentScreen = false;
            this.reverseLastTwoChildren = true;
        }
        super.removeView(view);
    }

    private final void drawAndRelease() {
        List<DrawingOp> list = this.drawingOps;
        this.drawingOps = new ArrayList();
        for (DrawingOp drawingOp : list) {
            drawingOp.draw();
            this.drawingOpPool.add(drawingOp);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        super.dispatchDraw(canvas);
        if (this.drawingOps.size() < this.previousChildrenCount) {
            this.reverseLastTwoChildren = false;
        }
        this.previousChildrenCount = this.drawingOps.size();
        if (this.reverseLastTwoChildren && this.drawingOps.size() >= 2) {
            List<DrawingOp> list = this.drawingOps;
            Collections.swap(list, list.size() - 1, this.drawingOps.size() - 2);
        }
        drawAndRelease();
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View child, long j) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(child, "child");
        List<DrawingOp> list = this.drawingOps;
        DrawingOp obtainDrawingOp = obtainDrawingOp();
        obtainDrawingOp.setCanvas(canvas);
        obtainDrawingOp.setChild(child);
        obtainDrawingOp.setDrawingTime(j);
        list.add(obtainDrawingOp);
        return true;
    }

    public final void performDraw(DrawingOp drawingOp) {
        Canvas canvas = drawingOp.getCanvas();
        Intrinsics.checkNotNull(canvas);
        super.drawChild(canvas, drawingOp.getChild(), drawingOp.getDrawingTime());
    }

    private final DrawingOp obtainDrawingOp() {
        return this.drawingOpPool.isEmpty() ? new DrawingOp() : (DrawingOp) CollectionsKt.removeLast(this.drawingOpPool);
    }

    /* compiled from: ScreenStack.kt */
    @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0015\u001a\u00020\u0016R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¨\u0006\u0017"}, d2 = {"Lcom/swmansion/rnscreens/ScreenStack$DrawingOp;", "", "(Lcom/swmansion/rnscreens/ScreenStack;)V", "canvas", "Landroid/graphics/Canvas;", "getCanvas", "()Landroid/graphics/Canvas;", "setCanvas", "(Landroid/graphics/Canvas;)V", "child", "Landroid/view/View;", "getChild", "()Landroid/view/View;", "setChild", "(Landroid/view/View;)V", "drawingTime", "", "getDrawingTime", "()J", "setDrawingTime", "(J)V", "draw", "", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public final class DrawingOp {
        private Canvas canvas;
        private View child;
        private long drawingTime;

        public DrawingOp() {
            ScreenStack.this = r1;
        }

        public final Canvas getCanvas() {
            return this.canvas;
        }

        public final void setCanvas(Canvas canvas) {
            this.canvas = canvas;
        }

        public final View getChild() {
            return this.child;
        }

        public final void setChild(View view) {
            this.child = view;
        }

        public final long getDrawingTime() {
            return this.drawingTime;
        }

        public final void setDrawingTime(long j) {
            this.drawingTime = j;
        }

        public final void draw() {
            ScreenStack.this.performDraw(this);
            this.canvas = null;
            this.child = null;
            this.drawingTime = 0L;
        }
    }

    /* compiled from: ScreenStack.kt */
    @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002¨\u0006\b"}, d2 = {"Lcom/swmansion/rnscreens/ScreenStack$Companion;", "", "()V", "isTransparent", "", "fragment", "Lcom/swmansion/rnscreens/ScreenStackFragment;", "needsDrawReordering", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean isTransparent(ScreenStackFragment screenStackFragment) {
            return screenStackFragment.getScreen().getStackPresentation() == Screen.StackPresentation.TRANSPARENT_MODAL;
        }

        public final boolean needsDrawReordering(ScreenStackFragment screenStackFragment) {
            return screenStackFragment.getScreen().getStackAnimation() == Screen.StackAnimation.SLIDE_FROM_BOTTOM || screenStackFragment.getScreen().getStackAnimation() == Screen.StackAnimation.FADE_FROM_BOTTOM;
        }
    }
}
