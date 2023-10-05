package androidx.core.provider;

import android.os.Handler;
import android.os.Looper;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class CalleeHandler {
    private CalleeHandler() {
    }

    public static Handler create() {
        if (Looper.myLooper() == null) {
            return new Handler(Looper.getMainLooper());
        }
        return new Handler();
    }
}
