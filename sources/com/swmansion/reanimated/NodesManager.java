package com.swmansion.reanimated;

import android.view.View;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.GuardedRunnable;
import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.modules.core.ReactChoreographer;
import com.facebook.react.uimanager.GuardedFrameCallback;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.facebook.react.uimanager.ReactShadowNode;
import com.facebook.react.uimanager.ReactStylesDiffMap;
import com.facebook.react.uimanager.UIImplementation;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.UIManagerReanimatedHelper;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcherListener;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.swmansion.reanimated.layoutReanimation.AnimationsManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class NodesManager implements EventDispatcherListener {
    private ReaCompatibility compatibility;
    public double currentFrameTimeMs;
    private AnimationsManager mAnimationManager;
    private final GuardedFrameCallback mChoreographerCallback;
    private final ReactContext mContext;
    private RCTEventEmitter mCustomEventHandler;
    protected final UIManagerModule.CustomEventNamesResolver mCustomEventNamesResolver;
    private final DeviceEventManagerModule.RCTDeviceEventEmitter mEventEmitter;
    private NativeProxy mNativeProxy;
    private ReactApplicationContext mReactApplicationContext;
    private final ReactChoreographer mReactChoreographer;
    private final UIImplementation mUIImplementation;
    private final UIManagerModule mUIManager;
    private final AtomicBoolean mCallbackPosted = new AtomicBoolean();
    private List<OnAnimationFrame> mFrameCallbacks = new ArrayList();
    private ConcurrentLinkedQueue<CopiedEvent> mEventQueue = new ConcurrentLinkedQueue<>();
    public Set<String> uiProps = Collections.emptySet();
    public Set<String> nativeProps = Collections.emptySet();
    private Queue<NativeUpdateOperation> mOperationsInBatch = new LinkedList();
    private boolean mTryRunBatchUpdatesSynchronously = false;

    /* loaded from: classes.dex */
    public interface OnAnimationFrame {
        void onAnimationFrame(double d);
    }

    public void scrollTo(int i, double d, double d2, boolean z) {
        try {
            NativeMethodsHelper.scrollTo(this.mUIManager.resolveView(i), d, d2, z);
        } catch (IllegalViewOperationException e) {
            e.printStackTrace();
        }
    }

    public float[] measure(int i) {
        try {
            return NativeMethodsHelper.measure(this.mUIManager.resolveView(i));
        } catch (IllegalViewOperationException e) {
            e.printStackTrace();
            return new float[]{Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN};
        }
    }

    public NativeProxy getNativeProxy() {
        return this.mNativeProxy;
    }

    public AnimationsManager getAnimationsManager() {
        return this.mAnimationManager;
    }

    public void onCatalystInstanceDestroy() {
        AnimationsManager animationsManager = this.mAnimationManager;
        if (animationsManager != null) {
            animationsManager.onCatalystInstanceDestroy();
        }
        NativeProxy nativeProxy = this.mNativeProxy;
        if (nativeProxy != null) {
            nativeProxy.onCatalystInstanceDestroy();
            this.mNativeProxy = null;
        }
    }

    public void initWithContext(ReactApplicationContext reactApplicationContext) {
        this.mReactApplicationContext = reactApplicationContext;
        this.mNativeProxy = new NativeProxy(reactApplicationContext);
        this.mAnimationManager.setScheduler(getNativeProxy().getScheduler());
        ReaCompatibility reaCompatibility = new ReaCompatibility(reactApplicationContext);
        this.compatibility = reaCompatibility;
        reaCompatibility.registerFabricEventListener(this);
    }

    /* loaded from: classes.dex */
    public final class NativeUpdateOperation {
        public WritableMap mNativeProps;
        public int mViewTag;

        public NativeUpdateOperation(int i, WritableMap writableMap) {
            NodesManager.this = r1;
            this.mViewTag = i;
            this.mNativeProps = writableMap;
        }
    }

    public NodesManager(ReactContext reactContext) {
        this.mContext = reactContext;
        UIManagerModule uIManagerModule = (UIManagerModule) reactContext.getNativeModule(UIManagerModule.class);
        this.mUIManager = uIManagerModule;
        this.mUIImplementation = uIManagerModule.getUIImplementation();
        this.mCustomEventNamesResolver = uIManagerModule.getDirectEventNamesResolver();
        this.mEventEmitter = (DeviceEventManagerModule.RCTDeviceEventEmitter) reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
        this.mReactChoreographer = ReactChoreographer.getInstance();
        this.mChoreographerCallback = new GuardedFrameCallback(reactContext) { // from class: com.swmansion.reanimated.NodesManager.1
            @Override // com.facebook.react.uimanager.GuardedFrameCallback
            protected void doFrameGuarded(long j) {
                NodesManager.this.onAnimationFrame(j);
            }
        };
        uIManagerModule.getEventDispatcher().addListener(this);
        this.mAnimationManager = new AnimationsManager(reactContext, uIManagerModule);
    }

    public void onHostPause() {
        if (this.mCallbackPosted.get()) {
            stopUpdatingOnAnimationFrame();
            this.mCallbackPosted.set(true);
        }
    }

    public boolean isAnimationRunning() {
        return this.mCallbackPosted.get();
    }

    public void onHostResume() {
        if (this.mCallbackPosted.getAndSet(false)) {
            startUpdatingOnAnimationFrame();
        }
    }

    public void startUpdatingOnAnimationFrame() {
        if (this.mCallbackPosted.getAndSet(true)) {
            return;
        }
        this.mReactChoreographer.postFrameCallback(ReactChoreographer.CallbackType.NATIVE_ANIMATED_MODULE, this.mChoreographerCallback);
    }

    private void stopUpdatingOnAnimationFrame() {
        if (this.mCallbackPosted.getAndSet(false)) {
            this.mReactChoreographer.removeFrameCallback(ReactChoreographer.CallbackType.NATIVE_ANIMATED_MODULE, this.mChoreographerCallback);
        }
    }

    public void performOperations() {
        if (this.mOperationsInBatch.isEmpty()) {
            return;
        }
        final Queue<NativeUpdateOperation> queue = this.mOperationsInBatch;
        this.mOperationsInBatch = new LinkedList();
        final boolean z = this.mTryRunBatchUpdatesSynchronously;
        this.mTryRunBatchUpdatesSynchronously = false;
        final Semaphore semaphore = new Semaphore(0);
        this.mContext.runOnNativeModulesQueueThread(new GuardedRunnable(this.mContext.getExceptionHandler()) { // from class: com.swmansion.reanimated.NodesManager.2
            @Override // com.facebook.react.bridge.GuardedRunnable
            public void runGuarded() {
                boolean isOperationQueueEmpty = UIManagerReanimatedHelper.isOperationQueueEmpty(NodesManager.this.mUIImplementation);
                boolean z2 = z && isOperationQueueEmpty;
                if (!z2) {
                    semaphore.release();
                }
                while (!queue.isEmpty()) {
                    NativeUpdateOperation nativeUpdateOperation = (NativeUpdateOperation) queue.remove();
                    ReactShadowNode resolveShadowNode = NodesManager.this.mUIImplementation.resolveShadowNode(nativeUpdateOperation.mViewTag);
                    if (resolveShadowNode != null) {
                        NodesManager.this.mUIManager.updateView(nativeUpdateOperation.mViewTag, resolveShadowNode.getViewClass(), nativeUpdateOperation.mNativeProps);
                    }
                }
                if (isOperationQueueEmpty) {
                    NodesManager.this.mUIImplementation.dispatchViewUpdates(-1);
                }
                if (z2) {
                    semaphore.release();
                }
            }
        });
        if (z) {
            try {
                semaphore.tryAcquire(16L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException unused) {
            }
        }
    }

    public void onAnimationFrame(long j) {
        this.currentFrameTimeMs = j / 1000000.0d;
        while (!this.mEventQueue.isEmpty()) {
            CopiedEvent poll = this.mEventQueue.poll();
            handleEvent(poll.getTargetTag(), poll.getEventName(), poll.getPayload());
        }
        if (!this.mFrameCallbacks.isEmpty()) {
            List<OnAnimationFrame> list = this.mFrameCallbacks;
            this.mFrameCallbacks = new ArrayList(list.size());
            int size = list.size();
            for (int i = 0; i < size; i++) {
                list.get(i).onAnimationFrame(this.currentFrameTimeMs);
            }
        }
        performOperations();
        this.mCallbackPosted.set(false);
        if (this.mFrameCallbacks.isEmpty() && this.mEventQueue.isEmpty()) {
            return;
        }
        startUpdatingOnAnimationFrame();
    }

    public void enqueueUpdateViewOnNativeThread(int i, WritableMap writableMap, boolean z) {
        if (z) {
            this.mTryRunBatchUpdatesSynchronously = true;
        }
        this.mOperationsInBatch.add(new NativeUpdateOperation(i, writableMap));
    }

    public void configureProps(Set<String> set, Set<String> set2) {
        this.uiProps = set;
        this.nativeProps = set2;
    }

    public void postOnAnimation(OnAnimationFrame onAnimationFrame) {
        this.mFrameCallbacks.add(onAnimationFrame);
        startUpdatingOnAnimationFrame();
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcherListener
    public void onEventDispatch(Event event) {
        NativeProxy nativeProxy;
        if (UiThreadUtil.isOnUiThread()) {
            handleEvent(event);
            performOperations();
            return;
        }
        String resolveCustomEventName = this.mCustomEventNamesResolver.resolveCustomEventName(event.getEventName());
        int viewTag = event.getViewTag();
        StringBuilder sb = new StringBuilder();
        sb.append(viewTag);
        sb.append(resolveCustomEventName);
        if (((this.mCustomEventHandler == null || (nativeProxy = this.mNativeProxy) == null || !nativeProxy.isAnyHandlerWaitingForEvent(sb.toString())) ? false : true) | false) {
            this.mEventQueue.offer(new CopiedEvent(event));
        }
        startUpdatingOnAnimationFrame();
    }

    private void handleEvent(Event event) {
        this.mCustomEventNamesResolver.resolveCustomEventName(event.getEventName());
        event.getViewTag();
        RCTEventEmitter rCTEventEmitter = this.mCustomEventHandler;
        if (rCTEventEmitter != null) {
            event.dispatch(rCTEventEmitter);
        }
    }

    private void handleEvent(int i, String str, @Nullable WritableMap writableMap) {
        RCTEventEmitter rCTEventEmitter = this.mCustomEventHandler;
        if (rCTEventEmitter != null) {
            rCTEventEmitter.receiveEvent(i, str, writableMap);
        }
    }

    public UIManagerModule.CustomEventNamesResolver getEventNameResolver() {
        return this.mCustomEventNamesResolver;
    }

    public void registerEventHandler(RCTEventEmitter rCTEventEmitter) {
        this.mCustomEventHandler = rCTEventEmitter;
    }

    public void sendEvent(String str, WritableMap writableMap) {
        this.mEventEmitter.emit(str, writableMap);
    }

    public void updateProps(int i, Map<String, Object> map) {
        JavaOnlyMap javaOnlyMap = new JavaOnlyMap();
        WritableMap createMap = Arguments.createMap();
        WritableMap createMap2 = Arguments.createMap();
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (this.uiProps.contains(key)) {
                addProp(javaOnlyMap, key, value);
                z = true;
            } else if (this.nativeProps.contains(key)) {
                addProp(createMap2, key, value);
                z2 = true;
            } else {
                addProp(createMap, key, value);
                z3 = true;
            }
        }
        if (i != -1) {
            if (z) {
                this.mUIImplementation.synchronouslyUpdateViewOnUIThread(i, new ReactStylesDiffMap(javaOnlyMap));
            }
            if (z2) {
                enqueueUpdateViewOnNativeThread(i, createMap2, true);
            }
            if (z3) {
                WritableMap createMap3 = Arguments.createMap();
                createMap3.putInt("viewTag", i);
                createMap3.putMap("props", createMap);
                sendEvent("onReanimatedPropsChange", createMap3);
            }
        }
    }

    public void synchronouslyUpdateUIProps(int i, ReadableMap readableMap) {
        this.compatibility.synchronouslyUpdateUIProps(i, readableMap);
    }

    public String obtainProp(int i, String str) {
        View resolveView = this.mUIManager.resolveView(i);
        String str2 = "error: unknown propName " + str + ", currently supported: opacity, zIndex";
        if (str.equals(ViewProps.OPACITY)) {
            return Float.toString(Float.valueOf(resolveView.getAlpha()).floatValue());
        }
        return str.equals(ViewProps.Z_INDEX) ? Float.toString(Float.valueOf(resolveView.getElevation()).floatValue()) : str2;
    }

    private static WritableMap copyReadableMap(ReadableMap readableMap) {
        WritableMap createMap = Arguments.createMap();
        createMap.merge(readableMap);
        return createMap;
    }

    private static WritableArray copyReadableArray(ReadableArray readableArray) {
        WritableArray createArray = Arguments.createArray();
        for (int i = 0; i < readableArray.size(); i++) {
            switch (AnonymousClass3.$SwitchMap$com$facebook$react$bridge$ReadableType[readableArray.getType(i).ordinal()]) {
                case 1:
                    createArray.pushBoolean(readableArray.getBoolean(i));
                    break;
                case 2:
                    createArray.pushString(readableArray.getString(i));
                    break;
                case 3:
                    createArray.pushNull();
                    break;
                case 4:
                    createArray.pushDouble(readableArray.getDouble(i));
                    break;
                case 5:
                    createArray.pushMap(copyReadableMap(readableArray.getMap(i)));
                    break;
                case 6:
                    createArray.pushArray(copyReadableArray(readableArray.getArray(i)));
                    break;
                default:
                    throw new IllegalStateException("Unknown type of ReadableArray");
            }
        }
        return createArray;
    }

    /* renamed from: com.swmansion.reanimated.NodesManager$3 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$facebook$react$bridge$ReadableType;

        static {
            int[] iArr = new int[ReadableType.values().length];
            $SwitchMap$com$facebook$react$bridge$ReadableType = iArr;
            try {
                iArr[ReadableType.Boolean.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$facebook$react$bridge$ReadableType[ReadableType.String.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$facebook$react$bridge$ReadableType[ReadableType.Null.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$facebook$react$bridge$ReadableType[ReadableType.Number.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$facebook$react$bridge$ReadableType[ReadableType.Map.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$facebook$react$bridge$ReadableType[ReadableType.Array.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    private static void addProp(WritableMap writableMap, String str, Object obj) {
        if (obj == null) {
            writableMap.putNull(str);
        } else if (obj instanceof Double) {
            writableMap.putDouble(str, ((Double) obj).doubleValue());
        } else if (obj instanceof Integer) {
            writableMap.putInt(str, ((Integer) obj).intValue());
        } else if (obj instanceof Number) {
            writableMap.putDouble(str, ((Number) obj).doubleValue());
        } else if (obj instanceof Boolean) {
            writableMap.putBoolean(str, ((Boolean) obj).booleanValue());
        } else if (obj instanceof String) {
            writableMap.putString(str, (String) obj);
        } else if (obj instanceof ReadableArray) {
            if (!(obj instanceof WritableArray)) {
                writableMap.putArray(str, copyReadableArray((ReadableArray) obj));
            } else {
                writableMap.putArray(str, (ReadableArray) obj);
            }
        } else if (obj instanceof ReadableMap) {
            if (!(obj instanceof WritableMap)) {
                writableMap.putMap(str, copyReadableMap((ReadableMap) obj));
            } else {
                writableMap.putMap(str, (ReadableMap) obj);
            }
        } else {
            throw new IllegalStateException("Unknown type of animated value");
        }
    }
}
