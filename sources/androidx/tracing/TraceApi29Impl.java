package androidx.tracing;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class TraceApi29Impl {
    private TraceApi29Impl() {
    }

    public static boolean isEnabled() {
        return android.os.Trace.isEnabled();
    }

    public static void beginAsyncSection(String str, int i) {
        android.os.Trace.beginAsyncSection(str, i);
    }

    public static void endAsyncSection(String str, int i) {
        android.os.Trace.endAsyncSection(str, i);
    }

    public static void setCounter(String str, int i) {
        android.os.Trace.setCounter(str, i);
    }
}
