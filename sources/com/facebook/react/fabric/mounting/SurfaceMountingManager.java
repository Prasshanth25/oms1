package com.facebook.react.fabric.mounting;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.facebook.common.logging.FLog;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.RetryableMountingLayerException;
import com.facebook.react.bridge.SoftAssertions;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.mapbuffer.ReadableMapBuffer;
import com.facebook.react.config.ReactFeatureFlags;
import com.facebook.react.fabric.GuardedFrameCallback;
import com.facebook.react.fabric.events.EventEmitterWrapper;
import com.facebook.react.fabric.mounting.MountingManager;
import com.facebook.react.fabric.mounting.mountitems.MountItem;
import com.facebook.react.modules.core.ReactChoreographer;
import com.facebook.react.touch.JSResponderHandler;
import com.facebook.react.uimanager.IViewGroupManager;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.facebook.react.uimanager.ReactOverflowViewWithInset;
import com.facebook.react.uimanager.ReactRoot;
import com.facebook.react.uimanager.ReactStylesDiffMap;
import com.facebook.react.uimanager.RootView;
import com.facebook.react.uimanager.RootViewManager;
import com.facebook.react.uimanager.StateWrapper;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewManagerRegistry;
import com.facebook.react.views.view.ReactMapBufferViewManager;
import com.facebook.react.views.view.ReactViewManagerWrapper;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class SurfaceMountingManager {
    private static final boolean SHOW_CHANGED_VIEW_HIERARCHIES = false;
    public static final String TAG = "SurfaceMountingManager";
    private JSResponderHandler mJSResponderHandler;
    private MountingManager.MountItemExecutor mMountItemExecutor;
    private RemoveDeleteTreeUIFrameCallback mRemoveDeleteTreeUIFrameCallback;
    private RootViewManager mRootViewManager;
    private final int mSurfaceId;
    private Set<Integer> mTagSetForStoppedSurface;
    @Nullable
    private ThemedReactContext mThemedReactContext;
    private ViewManagerRegistry mViewManagerRegistry;
    private volatile boolean mIsStopped = false;
    private volatile boolean mRootViewAttached = false;
    private ConcurrentHashMap<Integer, ViewState> mTagToViewState = new ConcurrentHashMap<>();
    private ConcurrentLinkedQueue<MountItem> mOnViewAttachItems = new ConcurrentLinkedQueue<>();
    private final Stack<Integer> mReactTagsToRemove = new Stack<>();
    private final Set<Integer> mErroneouslyReaddedReactTags = new HashSet();

    public SurfaceMountingManager(int i, JSResponderHandler jSResponderHandler, ViewManagerRegistry viewManagerRegistry, RootViewManager rootViewManager, MountingManager.MountItemExecutor mountItemExecutor, ThemedReactContext themedReactContext) {
        this.mSurfaceId = i;
        this.mJSResponderHandler = jSResponderHandler;
        this.mViewManagerRegistry = viewManagerRegistry;
        this.mRootViewManager = rootViewManager;
        this.mMountItemExecutor = mountItemExecutor;
        this.mThemedReactContext = themedReactContext;
    }

    public boolean isStopped() {
        return this.mIsStopped;
    }

    public void attachRootView(View view, ThemedReactContext themedReactContext) {
        this.mThemedReactContext = themedReactContext;
        addRootView(view);
    }

    public int getSurfaceId() {
        return this.mSurfaceId;
    }

    public boolean isRootViewAttached() {
        return this.mRootViewAttached;
    }

    @Nullable
    public ThemedReactContext getContext() {
        return this.mThemedReactContext;
    }

    private static void logViewHierarchy(ViewGroup viewGroup, boolean z) {
        int id = viewGroup.getId();
        String str = TAG;
        FLog.e(str, "  <ViewGroup tag=" + id + " class=" + viewGroup.getClass().toString() + ">");
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            String str2 = TAG;
            FLog.e(str2, "     <View idx=" + i + " tag=" + viewGroup.getChildAt(i).getId() + " class=" + viewGroup.getChildAt(i).getClass().toString() + ">");
        }
        String str3 = TAG;
        FLog.e(str3, "  </ViewGroup tag=" + id + ">");
        if (z) {
            FLog.e(str3, "Displaying Ancestors:");
            for (ViewParent parent = viewGroup.getParent(); parent != null; parent = parent.getParent()) {
                ViewGroup viewGroup2 = parent instanceof ViewGroup ? (ViewGroup) parent : null;
                int id2 = viewGroup2 == null ? -1 : viewGroup2.getId();
                String str4 = TAG;
                FLog.e(str4, "<ViewParent tag=" + id2 + " class=" + parent.getClass().toString() + ">");
            }
        }
    }

    public boolean getViewExists(int i) {
        Set<Integer> set = this.mTagSetForStoppedSurface;
        if (set == null || !set.contains(Integer.valueOf(i))) {
            ConcurrentHashMap<Integer, ViewState> concurrentHashMap = this.mTagToViewState;
            if (concurrentHashMap == null) {
                return false;
            }
            return concurrentHashMap.containsKey(Integer.valueOf(i));
        }
        return true;
    }

    public void executeOnViewAttach(MountItem mountItem) {
        this.mOnViewAttachItems.add(mountItem);
    }

    private void addRootView(final View view) {
        if (isStopped()) {
            return;
        }
        this.mTagToViewState.put(Integer.valueOf(this.mSurfaceId), new ViewState(this.mSurfaceId, view, new ReactViewManagerWrapper.DefaultViewManager(this.mRootViewManager), true));
        Runnable runnable = new Runnable() { // from class: com.facebook.react.fabric.mounting.SurfaceMountingManager.1
            @Override // java.lang.Runnable
            public void run() {
                if (SurfaceMountingManager.this.isStopped()) {
                    return;
                }
                if (view.getId() == SurfaceMountingManager.this.mSurfaceId) {
                    String str = SurfaceMountingManager.TAG;
                    ReactSoftExceptionLogger.logSoftException(str, new IllegalViewOperationException("Race condition in addRootView detected. Trying to set an id of [" + SurfaceMountingManager.this.mSurfaceId + "] on the RootView, but that id has already been set. "));
                } else if (view.getId() != -1) {
                    FLog.e(SurfaceMountingManager.TAG, "Trying to add RootTag to RootView that already has a tag: existing tag: [%d] new tag: [%d]", Integer.valueOf(view.getId()), Integer.valueOf(SurfaceMountingManager.this.mSurfaceId));
                    throw new IllegalViewOperationException("Trying to add a root view with an explicit id already set. React Native uses the id field to track react tags and will overwrite this field. If that is fine, explicitly overwrite the id field to View.NO_ID before calling addRootView.");
                }
                view.setId(SurfaceMountingManager.this.mSurfaceId);
                View view2 = view;
                if (view2 instanceof ReactRoot) {
                    ((ReactRoot) view2).setRootViewTag(SurfaceMountingManager.this.mSurfaceId);
                }
                SurfaceMountingManager.this.mRootViewAttached = true;
                SurfaceMountingManager.this.executeViewAttachMountItems();
            }
        };
        if (UiThreadUtil.isOnUiThread()) {
            runnable.run();
        } else {
            UiThreadUtil.runOnUiThread(runnable);
        }
    }

    public void executeViewAttachMountItems() {
        this.mMountItemExecutor.executeItems(this.mOnViewAttachItems);
    }

    public void stopSurface() {
        String str = TAG;
        FLog.e(str, "Stopping surface [" + this.mSurfaceId + "]");
        if (isStopped()) {
            return;
        }
        this.mIsStopped = true;
        for (ViewState viewState : this.mTagToViewState.values()) {
            if (viewState.mStateWrapper != null) {
                viewState.mStateWrapper.destroyState();
                viewState.mStateWrapper = null;
            }
            if (viewState.mEventEmitter != null) {
                viewState.mEventEmitter.destroy();
                viewState.mEventEmitter = null;
            }
        }
        Runnable runnable = new Runnable() { // from class: com.facebook.react.fabric.mounting.SurfaceMountingManager.2
            @Override // java.lang.Runnable
            public void run() {
                for (ViewState viewState2 : SurfaceMountingManager.this.mTagToViewState.values()) {
                    SurfaceMountingManager.this.onViewStateDeleted(viewState2);
                }
                SurfaceMountingManager surfaceMountingManager = SurfaceMountingManager.this;
                surfaceMountingManager.mTagSetForStoppedSurface = surfaceMountingManager.mTagToViewState.keySet();
                SurfaceMountingManager.this.mTagToViewState = null;
                SurfaceMountingManager.this.mJSResponderHandler = null;
                SurfaceMountingManager.this.mRootViewManager = null;
                SurfaceMountingManager.this.mMountItemExecutor = null;
                SurfaceMountingManager.this.mOnViewAttachItems.clear();
                if (ReactFeatureFlags.enableViewRecycling) {
                    SurfaceMountingManager.this.mViewManagerRegistry.onSurfaceStopped(SurfaceMountingManager.this.mSurfaceId);
                }
                String str2 = SurfaceMountingManager.TAG;
                FLog.e(str2, "Surface [" + SurfaceMountingManager.this.mSurfaceId + "] was stopped on SurfaceMountingManager.");
            }
        };
        if (UiThreadUtil.isOnUiThread()) {
            runnable.run();
        } else {
            UiThreadUtil.runOnUiThread(runnable);
        }
    }

    public void addViewAt(int i, int i2, int i3) {
        UiThreadUtil.assertOnUiThread();
        if (isStopped()) {
            return;
        }
        ViewState viewState = getViewState(i);
        if (!(viewState.mView instanceof ViewGroup)) {
            String str = "Unable to add a view into a view that is not a ViewGroup. ParentTag: " + i + " - Tag: " + i2 + " - Index: " + i3;
            FLog.e(TAG, str);
            throw new IllegalStateException(str);
        }
        ViewGroup viewGroup = (ViewGroup) viewState.mView;
        ViewState viewState2 = getViewState(i2);
        View view = viewState2.mView;
        if (view == null) {
            throw new IllegalStateException("Unable to find view for viewState " + viewState2 + " and tag " + i2);
        }
        ViewParent parent = view.getParent();
        if (parent != null) {
            boolean z = parent instanceof ViewGroup;
            int id = z ? ((ViewGroup) parent).getId() : -1;
            ReactSoftExceptionLogger.logSoftException(TAG, new IllegalStateException("addViewAt: cannot insert view [" + i2 + "] into parent [" + i + "]: View already has a parent: [" + id + "]  Parent: " + parent.getClass().getSimpleName() + " View: " + view.getClass().getSimpleName()));
            if (z) {
                ((ViewGroup) parent).removeView(view);
            }
            this.mErroneouslyReaddedReactTags.add(Integer.valueOf(i2));
        }
        try {
            getViewGroupManager(viewState).addView(viewGroup, view, i3);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("addViewAt: failed to insert view [" + i2 + "] into parent [" + i + "] at index " + i3, e);
        }
    }

    public void removeViewAt(int i, int i2, int i3) {
        if (isStopped()) {
            return;
        }
        if (this.mErroneouslyReaddedReactTags.contains(Integer.valueOf(i))) {
            ReactSoftExceptionLogger.logSoftException(TAG, new IllegalViewOperationException("removeViewAt tried to remove a React View that was actually reused. This indicates a bug in the Differ (specifically instruction ordering). [" + i + "]"));
            return;
        }
        UiThreadUtil.assertOnUiThread();
        ViewState nullableViewState = getNullableViewState(i2);
        if (nullableViewState == null) {
            ReactSoftExceptionLogger.logSoftException(MountingManager.TAG, new IllegalStateException("Unable to find viewState for tag: [" + i2 + "] for removeViewAt"));
        } else if (!(nullableViewState.mView instanceof ViewGroup)) {
            String str = "Unable to remove a view from a view that is not a ViewGroup. ParentTag: " + i2 + " - Tag: " + i + " - Index: " + i3;
            FLog.e(TAG, str);
            throw new IllegalStateException(str);
        } else {
            ViewGroup viewGroup = (ViewGroup) nullableViewState.mView;
            if (viewGroup == null) {
                throw new IllegalStateException("Unable to find view for tag [" + i2 + "]");
            }
            IViewGroupManager<ViewGroup> viewGroupManager = getViewGroupManager(nullableViewState);
            View childAt = viewGroupManager.getChildAt(viewGroup, i3);
            int id = childAt != null ? childAt.getId() : -1;
            if (id != i) {
                int childCount = viewGroup.getChildCount();
                int i4 = 0;
                while (true) {
                    if (i4 >= childCount) {
                        i4 = -1;
                        break;
                    } else if (viewGroup.getChildAt(i4).getId() == i) {
                        break;
                    } else {
                        i4++;
                    }
                }
                if (i4 == -1) {
                    FLog.e(TAG, "removeViewAt: [" + i + "] -> [" + i2 + "] @" + i3 + ": view already removed from parent! Children in parent: " + childCount);
                    return;
                }
                logViewHierarchy(viewGroup, true);
                ReactSoftExceptionLogger.logSoftException(TAG, new IllegalStateException("Tried to remove view [" + i + "] of parent [" + i2 + "] at index " + i3 + ", but got view tag " + id + " - actual index of view: " + i4));
                i3 = i4;
            }
            try {
                viewGroupManager.removeViewAt(viewGroup, i3);
            } catch (RuntimeException e) {
                int childCount2 = viewGroupManager.getChildCount(viewGroup);
                logViewHierarchy(viewGroup, true);
                throw new IllegalStateException("Cannot remove child at index " + i3 + " from parent ViewGroup [" + viewGroup.getId() + "], only " + childCount2 + " children in parent. Warning: childCount may be incorrect!", e);
            }
        }
    }

    public void removeDeleteTreeAt(int i, int i2, int i3) {
        if (isStopped()) {
            return;
        }
        UiThreadUtil.assertOnUiThread();
        ViewState nullableViewState = getNullableViewState(i2);
        if (nullableViewState == null) {
            ReactSoftExceptionLogger.logSoftException(MountingManager.TAG, new IllegalStateException("Unable to find viewState for tag: [" + i2 + "] for removeDeleteTreeAt"));
        } else if (!(nullableViewState.mView instanceof ViewGroup)) {
            String str = "Unable to remove+delete a view from a view that is not a ViewGroup. ParentTag: " + i2 + " - Tag: " + i + " - Index: " + i3;
            FLog.e(TAG, str);
            throw new IllegalStateException(str);
        } else {
            ViewGroup viewGroup = (ViewGroup) nullableViewState.mView;
            if (viewGroup == null) {
                throw new IllegalStateException("Unable to find view for tag [" + i2 + "]");
            }
            IViewGroupManager<ViewGroup> viewGroupManager = getViewGroupManager(nullableViewState);
            View childAt = viewGroupManager.getChildAt(viewGroup, i3);
            int id = childAt != null ? childAt.getId() : -1;
            if (id != i) {
                int childCount = viewGroup.getChildCount();
                int i4 = 0;
                while (true) {
                    if (i4 >= childCount) {
                        i4 = -1;
                        break;
                    } else if (viewGroup.getChildAt(i4).getId() == i) {
                        break;
                    } else {
                        i4++;
                    }
                }
                if (i4 == -1) {
                    FLog.e(TAG, "removeDeleteTreeAt: [" + i + "] -> [" + i2 + "] @" + i3 + ": view already removed from parent! Children in parent: " + childCount);
                    return;
                }
                logViewHierarchy(viewGroup, true);
                ReactSoftExceptionLogger.logSoftException(TAG, new IllegalStateException("Tried to remove+delete view [" + i + "] of parent [" + i2 + "] at index " + i3 + ", but got view tag " + id + " - actual index of view: " + i4));
                i3 = i4;
            }
            try {
                viewGroupManager.removeViewAt(viewGroup, i3);
                runDeferredTagRemovalAndDeletion();
                this.mReactTagsToRemove.push(Integer.valueOf(i));
            } catch (RuntimeException e) {
                int childCount2 = viewGroupManager.getChildCount(viewGroup);
                logViewHierarchy(viewGroup, true);
                throw new IllegalStateException("Cannot remove child at index " + i3 + " from parent ViewGroup [" + viewGroup.getId() + "], only " + childCount2 + " children in parent. Warning: childCount may be incorrect!", e);
            }
        }
    }

    private void runDeferredTagRemovalAndDeletion() {
        if (this.mReactTagsToRemove.empty()) {
            if (this.mRemoveDeleteTreeUIFrameCallback == null) {
                this.mRemoveDeleteTreeUIFrameCallback = new RemoveDeleteTreeUIFrameCallback(this.mThemedReactContext);
            }
            ReactChoreographer.getInstance().postFrameCallback(ReactChoreographer.CallbackType.IDLE_EVENT, this.mRemoveDeleteTreeUIFrameCallback);
        }
    }

    public void createView(String str, int i, @Nullable Object obj, @Nullable StateWrapper stateWrapper, @Nullable EventEmitterWrapper eventEmitterWrapper, boolean z) {
        if (isStopped()) {
            return;
        }
        ViewState nullableViewState = getNullableViewState(i);
        if (nullableViewState == null || nullableViewState.mView == null) {
            createViewUnsafe(str, i, obj, stateWrapper, eventEmitterWrapper, z);
        }
    }

    public void createViewUnsafe(String str, int i, @Nullable Object obj, @Nullable StateWrapper stateWrapper, @Nullable EventEmitterWrapper eventEmitterWrapper, boolean z) {
        ReactViewManagerWrapper reactViewManagerWrapper;
        View view;
        Object reactStylesDiffMap = obj instanceof ReadableMap ? new ReactStylesDiffMap((ReadableMap) obj) : obj;
        if (z) {
            if (obj instanceof ReadableMapBuffer) {
                reactViewManagerWrapper = ReactMapBufferViewManager.INSTANCE;
            } else {
                reactViewManagerWrapper = new ReactViewManagerWrapper.DefaultViewManager(this.mViewManagerRegistry.get(str));
            }
            view = reactViewManagerWrapper.createView(i, this.mThemedReactContext, reactStylesDiffMap, stateWrapper, this.mJSResponderHandler);
        } else {
            reactViewManagerWrapper = null;
            view = null;
        }
        ViewState viewState = new ViewState(i, view, reactViewManagerWrapper);
        viewState.mCurrentProps = reactStylesDiffMap;
        viewState.mStateWrapper = stateWrapper;
        viewState.mEventEmitter = eventEmitterWrapper;
        this.mTagToViewState.put(Integer.valueOf(i), viewState);
    }

    public void updateProps(int i, Object obj) {
        if (isStopped()) {
            return;
        }
        ViewState viewState = getViewState(i);
        if (obj instanceof ReadableMap) {
            obj = new ReactStylesDiffMap((ReadableMap) obj);
        }
        viewState.mCurrentProps = obj;
        View view = viewState.mView;
        if (view == null) {
            throw new IllegalStateException("Unable to find view for tag [" + i + "]");
        }
        ((ReactViewManagerWrapper) Assertions.assertNotNull(viewState.mViewManager)).updateProperties(view, viewState.mCurrentProps);
    }

    @Deprecated
    public void receiveCommand(int i, int i2, @Nullable ReadableArray readableArray) {
        if (isStopped()) {
            return;
        }
        ViewState nullableViewState = getNullableViewState(i);
        if (nullableViewState == null) {
            throw new RetryableMountingLayerException("Unable to find viewState for tag: [" + i + "] for commandId: " + i2);
        } else if (nullableViewState.mViewManager == null) {
            throw new RetryableMountingLayerException("Unable to find viewManager for tag " + i);
        } else if (nullableViewState.mView == null) {
            throw new RetryableMountingLayerException("Unable to find viewState view for tag " + i);
        } else {
            nullableViewState.mViewManager.receiveCommand(nullableViewState.mView, i2, readableArray);
        }
    }

    public void receiveCommand(int i, String str, @Nullable ReadableArray readableArray) {
        if (isStopped()) {
            return;
        }
        ViewState nullableViewState = getNullableViewState(i);
        if (nullableViewState == null) {
            throw new RetryableMountingLayerException("Unable to find viewState for tag: " + i + " for commandId: " + str);
        } else if (nullableViewState.mViewManager == null) {
            throw new RetryableMountingLayerException("Unable to find viewState manager for tag " + i);
        } else if (nullableViewState.mView == null) {
            throw new RetryableMountingLayerException("Unable to find viewState view for tag " + i);
        } else {
            nullableViewState.mViewManager.receiveCommand(nullableViewState.mView, str, readableArray);
        }
    }

    public void sendAccessibilityEvent(int i, int i2) {
        if (isStopped()) {
            return;
        }
        ViewState viewState = getViewState(i);
        if (viewState.mViewManager == null) {
            throw new RetryableMountingLayerException("Unable to find viewState manager for tag " + i);
        } else if (viewState.mView == null) {
            throw new RetryableMountingLayerException("Unable to find viewState view for tag " + i);
        } else {
            viewState.mView.sendAccessibilityEvent(i2);
        }
    }

    public void updateLayout(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (isStopped()) {
            return;
        }
        ViewState viewState = getViewState(i);
        if (viewState.mIsRoot) {
            return;
        }
        View view = viewState.mView;
        if (view == null) {
            throw new IllegalStateException("Unable to find View for tag: " + i);
        }
        view.measure(View.MeasureSpec.makeMeasureSpec(i5, 1073741824), View.MeasureSpec.makeMeasureSpec(i6, 1073741824));
        ViewParent parent = view.getParent();
        if (parent instanceof RootView) {
            parent.requestLayout();
        }
        ViewState viewState2 = getViewState(i2);
        IViewGroupManager<?> viewGroupManager = viewState2.mViewManager != null ? viewState2.mViewManager.getViewGroupManager() : null;
        if (viewGroupManager == null || !viewGroupManager.needsCustomLayoutForChildren()) {
            view.layout(i3, i4, i5 + i3, i6 + i4);
        }
        int i8 = i7 == 0 ? 4 : 0;
        if (view.getVisibility() != i8) {
            view.setVisibility(i8);
        }
    }

    public void updatePadding(int i, int i2, int i3, int i4, int i5) {
        UiThreadUtil.assertOnUiThread();
        if (isStopped()) {
            return;
        }
        ViewState viewState = getViewState(i);
        if (viewState.mIsRoot) {
            return;
        }
        View view = viewState.mView;
        if (view == null) {
            throw new IllegalStateException("Unable to find View for tag: " + i);
        }
        ReactViewManagerWrapper reactViewManagerWrapper = viewState.mViewManager;
        if (reactViewManagerWrapper == null) {
            throw new IllegalStateException("Unable to find ViewManager for view: " + viewState);
        }
        reactViewManagerWrapper.setPadding(view, i2, i3, i4, i5);
    }

    public void updateOverflowInset(int i, int i2, int i3, int i4, int i5) {
        if (isStopped()) {
            return;
        }
        ViewState viewState = getViewState(i);
        if (viewState.mIsRoot) {
            return;
        }
        View view = viewState.mView;
        if (view == null) {
            throw new IllegalStateException("Unable to find View for tag: " + i);
        } else if (view instanceof ReactOverflowViewWithInset) {
            ((ReactOverflowViewWithInset) view).setOverflowInset(i2, i3, i4, i5);
        }
    }

    public void updateState(int i, @Nullable StateWrapper stateWrapper) {
        UiThreadUtil.assertOnUiThread();
        if (isStopped()) {
            return;
        }
        ViewState viewState = getViewState(i);
        StateWrapper stateWrapper2 = viewState.mStateWrapper;
        viewState.mStateWrapper = stateWrapper;
        ReactViewManagerWrapper reactViewManagerWrapper = viewState.mViewManager;
        if (reactViewManagerWrapper == null) {
            throw new IllegalStateException("Unable to find ViewManager for tag: " + i);
        }
        Object updateState = reactViewManagerWrapper.updateState(viewState.mView, viewState.mCurrentProps, stateWrapper);
        if (updateState != null) {
            reactViewManagerWrapper.updateExtraData(viewState.mView, updateState);
        }
        if (stateWrapper2 != null) {
            stateWrapper2.destroyState();
        }
    }

    public void updateEventEmitter(int i, EventEmitterWrapper eventEmitterWrapper) {
        UiThreadUtil.assertOnUiThread();
        if (isStopped()) {
            return;
        }
        ViewState viewState = this.mTagToViewState.get(Integer.valueOf(i));
        if (viewState == null) {
            viewState = new ViewState(i, (View) null, (ReactViewManagerWrapper) null);
            this.mTagToViewState.put(Integer.valueOf(i), viewState);
        }
        EventEmitterWrapper eventEmitterWrapper2 = viewState.mEventEmitter;
        viewState.mEventEmitter = eventEmitterWrapper;
        if (eventEmitterWrapper2 != eventEmitterWrapper && eventEmitterWrapper2 != null) {
            eventEmitterWrapper2.destroy();
        }
        if (viewState.mPendingEventQueue != null) {
            for (ViewEvent viewEvent : viewState.mPendingEventQueue) {
                if (viewEvent.canCoalesceEvent()) {
                    eventEmitterWrapper.invokeUnique(viewEvent.getEventName(), viewEvent.getParams(), viewEvent.getCustomCoalesceKey());
                } else {
                    eventEmitterWrapper.invoke(viewEvent.getEventName(), viewEvent.getParams(), viewEvent.getEventCategory());
                }
            }
            viewState.mPendingEventQueue = null;
        }
    }

    public synchronized void setJSResponder(int i, int i2, boolean z) {
        UiThreadUtil.assertOnUiThread();
        if (isStopped()) {
            return;
        }
        if (!z) {
            this.mJSResponderHandler.setJSResponder(i2, null);
            return;
        }
        ViewState viewState = getViewState(i);
        View view = viewState.mView;
        if (i2 != i && (view instanceof ViewParent)) {
            this.mJSResponderHandler.setJSResponder(i2, (ViewParent) view);
        } else if (view == null) {
            SoftAssertions.assertUnreachable("Cannot find view for tag [" + i + "].");
        } else {
            if (viewState.mIsRoot) {
                SoftAssertions.assertUnreachable("Cannot block native responder on [" + i + "] that is a root view");
            }
            this.mJSResponderHandler.setJSResponder(i2, view.getParent());
        }
    }

    public void onViewStateDeleted(ViewState viewState) {
        if (viewState.mStateWrapper != null) {
            viewState.mStateWrapper.destroyState();
            viewState.mStateWrapper = null;
        }
        if (viewState.mEventEmitter != null) {
            viewState.mEventEmitter.destroy();
            viewState.mEventEmitter = null;
        }
        ReactViewManagerWrapper reactViewManagerWrapper = viewState.mViewManager;
        if (viewState.mIsRoot || reactViewManagerWrapper == null) {
            return;
        }
        reactViewManagerWrapper.onDropViewInstance(viewState.mView);
    }

    public void deleteView(int i) {
        UiThreadUtil.assertOnUiThread();
        if (isStopped()) {
            return;
        }
        ViewState nullableViewState = getNullableViewState(i);
        if (nullableViewState == null) {
            String str = MountingManager.TAG;
            ReactSoftExceptionLogger.logSoftException(str, new IllegalStateException("Unable to find viewState for tag: " + i + " for deleteView"));
            return;
        }
        this.mTagToViewState.remove(Integer.valueOf(i));
        onViewStateDeleted(nullableViewState);
    }

    public void preallocateView(String str, int i, @Nullable Object obj, @Nullable StateWrapper stateWrapper, @Nullable EventEmitterWrapper eventEmitterWrapper, boolean z) {
        UiThreadUtil.assertOnUiThread();
        if (!isStopped() && getNullableViewState(i) == null) {
            createViewUnsafe(str, i, obj, stateWrapper, eventEmitterWrapper, z);
        }
    }

    @Nullable
    public EventEmitterWrapper getEventEmitter(int i) {
        ViewState nullableViewState = getNullableViewState(i);
        if (nullableViewState == null) {
            return null;
        }
        return nullableViewState.mEventEmitter;
    }

    public View getView(int i) {
        ViewState nullableViewState = getNullableViewState(i);
        View view = nullableViewState == null ? null : nullableViewState.mView;
        if (view != null) {
            return view;
        }
        throw new IllegalViewOperationException("Trying to resolve view with tag " + i + " which doesn't exist");
    }

    private ViewState getViewState(int i) {
        ViewState viewState = this.mTagToViewState.get(Integer.valueOf(i));
        if (viewState != null) {
            return viewState;
        }
        throw new RetryableMountingLayerException("Unable to find viewState for tag " + i + ". Surface stopped: " + isStopped());
    }

    @Nullable
    public ViewState getNullableViewState(int i) {
        ConcurrentHashMap<Integer, ViewState> concurrentHashMap = this.mTagToViewState;
        if (concurrentHashMap == null) {
            return null;
        }
        return concurrentHashMap.get(Integer.valueOf(i));
    }

    private static IViewGroupManager<ViewGroup> getViewGroupManager(ViewState viewState) {
        if (viewState.mViewManager == null) {
            throw new IllegalStateException("Unable to find ViewManager for view: " + viewState);
        }
        return viewState.mViewManager.getViewGroupManager();
    }

    public void printSurfaceState() {
        FLog.e(TAG, "Views created for surface {%d}:", Integer.valueOf(getSurfaceId()));
        for (ViewState viewState : this.mTagToViewState.values()) {
            Integer num = null;
            String name = viewState.mViewManager != null ? viewState.mViewManager.getName() : null;
            View view = viewState.mView;
            View view2 = view != null ? (View) view.getParent() : null;
            if (view2 != null) {
                num = Integer.valueOf(view2.getId());
            }
            FLog.e(TAG, "<%s id=%d parentTag=%s isRoot=%b />", name, Integer.valueOf(viewState.mReactTag), num, Boolean.valueOf(viewState.mIsRoot));
        }
    }

    public void enqueuePendingEvent(int i, ViewEvent viewEvent) {
        ViewState viewState;
        UiThreadUtil.assertOnUiThread();
        ConcurrentHashMap<Integer, ViewState> concurrentHashMap = this.mTagToViewState;
        if (concurrentHashMap == null || (viewState = concurrentHashMap.get(Integer.valueOf(i))) == null) {
            return;
        }
        Assertions.assertCondition(viewState.mEventEmitter == null, "Only queue pending events when event emitter is null for the given view state");
        if (viewState.mPendingEventQueue == null) {
            viewState.mPendingEventQueue = new LinkedList();
        }
        viewState.mPendingEventQueue.add(viewEvent);
    }

    /* loaded from: classes.dex */
    public static class ViewState {
        @Nullable
        public ReadableMap mCurrentLocalData;
        @Nullable
        public Object mCurrentProps;
        @Nullable
        public EventEmitterWrapper mEventEmitter;
        final boolean mIsRoot;
        @Nullable
        public Queue<ViewEvent> mPendingEventQueue;
        final int mReactTag;
        @Nullable
        public StateWrapper mStateWrapper;
        @Nullable
        final View mView;
        @Nullable
        final ReactViewManagerWrapper mViewManager;

        private ViewState(int i, @Nullable View view, @Nullable ReactViewManagerWrapper reactViewManagerWrapper) {
            this(i, view, reactViewManagerWrapper, false);
        }

        private ViewState(int i, @Nullable View view, @Nullable ReactViewManagerWrapper reactViewManagerWrapper, boolean z) {
            this.mCurrentProps = null;
            this.mCurrentLocalData = null;
            this.mStateWrapper = null;
            this.mEventEmitter = null;
            this.mPendingEventQueue = null;
            this.mReactTag = i;
            this.mView = view;
            this.mIsRoot = z;
            this.mViewManager = reactViewManagerWrapper;
        }

        public String toString() {
            boolean z = this.mViewManager == null;
            return "ViewState [" + this.mReactTag + "] - isRoot: " + this.mIsRoot + " - props: " + this.mCurrentProps + " - localData: " + this.mCurrentLocalData + " - viewManager: " + this.mViewManager + " - isLayoutOnly: " + z;
        }
    }

    /* loaded from: classes.dex */
    public static class ViewEvent {
        private final boolean mCanCoalesceEvent;
        private final int mCustomCoalesceKey;
        private final int mEventCategory;
        private final String mEventName;
        @Nullable
        private WritableMap mParams;

        public ViewEvent(String str, @Nullable WritableMap writableMap, int i, boolean z, int i2) {
            this.mEventName = str;
            this.mParams = writableMap;
            this.mEventCategory = i;
            this.mCanCoalesceEvent = z;
            this.mCustomCoalesceKey = i2;
        }

        public String getEventName() {
            return this.mEventName;
        }

        public boolean canCoalesceEvent() {
            return this.mCanCoalesceEvent;
        }

        public int getCustomCoalesceKey() {
            return this.mCustomCoalesceKey;
        }

        public int getEventCategory() {
            return this.mEventCategory;
        }

        @Nullable
        public WritableMap getParams() {
            return this.mParams;
        }
    }

    /* loaded from: classes.dex */
    public class RemoveDeleteTreeUIFrameCallback extends GuardedFrameCallback {
        private static final long FRAME_TIME_MS = 16;
        private static final long MAX_TIME_IN_FRAME = 9;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private RemoveDeleteTreeUIFrameCallback(ReactContext reactContext) {
            super(reactContext);
            SurfaceMountingManager.this = r1;
        }

        private boolean haveExceededNonBatchedFrameTime(long j) {
            return FRAME_TIME_MS - ((System.nanoTime() - j) / 1000000) < MAX_TIME_IN_FRAME;
        }

        /* JADX WARN: Removed duplicated region for block: B:159:0x00a3 A[Catch: all -> 0x00f1, TryCatch #0 {all -> 0x00f1, blocks: (B:131:0x0007, B:133:0x0013, B:135:0x0035, B:136:0x0056, B:138:0x0061, B:141:0x0069, B:143:0x0072, B:145:0x0078, B:150:0x0084, B:153:0x0094, B:159:0x00a3, B:160:0x00ac, B:162:0x00c2, B:156:0x009b), top: B:175:0x0007, inners: #1 }] */
        @Override // com.facebook.react.fabric.GuardedFrameCallback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void doFrameGuarded(long r12) {
            /*
                Method dump skipped, instructions count: 283
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.fabric.mounting.SurfaceMountingManager.RemoveDeleteTreeUIFrameCallback.doFrameGuarded(long):void");
        }
    }
}
