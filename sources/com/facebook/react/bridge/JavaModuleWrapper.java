package com.facebook.react.bridge;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.config.ReactFeatureFlags;
import com.facebook.systrace.SystraceMessage;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class JavaModuleWrapper {
    private static final String TAG = "JavaModuleWrapper";
    private final JSInstance mJSInstance;
    private final ModuleHolder mModuleHolder;
    private final ArrayList<NativeModule.NativeMethod> mMethods = new ArrayList<>();
    private final ArrayList<MethodDescriptor> mDescs = new ArrayList<>();

    /* loaded from: classes.dex */
    public class MethodDescriptor {
        Method method;
        String name;
        String signature;
        String type;

        public MethodDescriptor() {
            JavaModuleWrapper.this = r1;
        }
    }

    public JavaModuleWrapper(JSInstance jSInstance, ModuleHolder moduleHolder) {
        this.mJSInstance = jSInstance;
        this.mModuleHolder = moduleHolder;
    }

    public BaseJavaModule getModule() {
        return (BaseJavaModule) this.mModuleHolder.getModule();
    }

    public String getName() {
        return this.mModuleHolder.getName();
    }

    private void findMethods() {
        Method[] declaredMethods;
        com.facebook.systrace.Systrace.beginSection(0L, "findMethods");
        HashSet hashSet = new HashSet();
        Class<?> cls = this.mModuleHolder.getModule().getClass();
        Class<? super Object> superclass = cls.getSuperclass();
        if (ReactModuleWithSpec.class.isAssignableFrom(superclass)) {
            cls = superclass;
        }
        for (Method method : cls.getDeclaredMethods()) {
            ReactMethod reactMethod = (ReactMethod) method.getAnnotation(ReactMethod.class);
            if (reactMethod != null) {
                String name = method.getName();
                if (hashSet.contains(name)) {
                    throw new IllegalArgumentException("Java Module " + getName() + " method name already registered: " + name);
                }
                MethodDescriptor methodDescriptor = new MethodDescriptor();
                JavaMethodWrapper javaMethodWrapper = new JavaMethodWrapper(this, method, reactMethod.isBlockingSynchronousMethod());
                methodDescriptor.name = name;
                methodDescriptor.type = javaMethodWrapper.getType();
                if (methodDescriptor.type == BaseJavaModule.METHOD_TYPE_SYNC) {
                    methodDescriptor.signature = javaMethodWrapper.getSignature();
                    methodDescriptor.method = method;
                }
                this.mMethods.add(javaMethodWrapper);
                this.mDescs.add(methodDescriptor);
            }
        }
        com.facebook.systrace.Systrace.endSection(0L);
    }

    public List<MethodDescriptor> getMethodDescriptors() {
        if (this.mDescs.isEmpty()) {
            findMethods();
        }
        return this.mDescs;
    }

    public NativeMap getConstants() {
        if (ReactFeatureFlags.warnOnLegacyNativeModuleSystemUse) {
            String str = TAG;
            ReactSoftExceptionLogger.logSoftException(str, new ReactNoCrashSoftException("Calling getConstants() on Java NativeModule (name = \"" + this.mModuleHolder.getName() + "\", className = " + this.mModuleHolder.getClassName() + ")."));
        }
        if (this.mModuleHolder.getHasConstants()) {
            String name = getName();
            SystraceMessage.beginSection(0L, "JavaModuleWrapper.getConstants").arg("moduleName", name).flush();
            ReactMarker.logMarker(ReactMarkerConstants.GET_CONSTANTS_START, name);
            BaseJavaModule module = getModule();
            com.facebook.systrace.Systrace.beginSection(0L, "module.getConstants");
            Map<String, Object> constants = module.getConstants();
            com.facebook.systrace.Systrace.endSection(0L);
            com.facebook.systrace.Systrace.beginSection(0L, "create WritableNativeMap");
            ReactMarker.logMarker(ReactMarkerConstants.CONVERT_CONSTANTS_START, name);
            try {
                return Arguments.makeNativeMap(constants);
            } finally {
                ReactMarker.logMarker(ReactMarkerConstants.CONVERT_CONSTANTS_END, name);
                com.facebook.systrace.Systrace.endSection(0L);
                ReactMarker.logMarker(ReactMarkerConstants.GET_CONSTANTS_END, name);
                SystraceMessage.endSection(0L).flush();
            }
        }
        return null;
    }

    public void invoke(int i, ReadableNativeArray readableNativeArray) {
        if (ReactFeatureFlags.warnOnLegacyNativeModuleSystemUse) {
            String str = TAG;
            ReactSoftExceptionLogger.logSoftException(str, new ReactNoCrashSoftException("Calling method on Java NativeModule (name = \"" + this.mModuleHolder.getName() + "\", className = " + this.mModuleHolder.getClassName() + ")."));
        }
        ArrayList<NativeModule.NativeMethod> arrayList = this.mMethods;
        if (arrayList == null || i >= arrayList.size()) {
            return;
        }
        if (ReactFeatureFlags.warnOnLegacyNativeModuleSystemUse) {
            String str2 = TAG;
            ReactSoftExceptionLogger.logSoftException(str2, new ReactNoCrashSoftException("Calling " + this.mDescs.get(i).name + "() on Java NativeModule (name = \"" + this.mModuleHolder.getName() + "\", className = " + this.mModuleHolder.getClassName() + ")."));
        }
        this.mMethods.get(i).invoke(this.mJSInstance, readableNativeArray);
    }
}
