package org.devio.rn.splashscreen;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.WindowManager;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class SplashScreen {
    private static WeakReference<Activity> mActivity;
    private static Dialog mSplashDialog;

    public static void show(final Activity activity, final int i, final boolean z) {
        if (activity == null) {
            return;
        }
        mActivity = new WeakReference<>(activity);
        activity.runOnUiThread(new Runnable() { // from class: org.devio.rn.splashscreen.SplashScreen.1
            @Override // java.lang.Runnable
            public void run() {
                if (activity.isFinishing()) {
                    return;
                }
                SplashScreen.mSplashDialog = new Dialog(activity, i);
                SplashScreen.mSplashDialog.setContentView(R.layout.launch_screen);
                SplashScreen.mSplashDialog.setCancelable(false);
                if (z) {
                    SplashScreen.setActivityAndroidP(SplashScreen.mSplashDialog);
                }
                if (SplashScreen.mSplashDialog.isShowing()) {
                    return;
                }
                SplashScreen.mSplashDialog.show();
            }
        });
    }

    public static void show(Activity activity, boolean z) {
        show(activity, z ? R.style.SplashScreen_Fullscreen : R.style.SplashScreen_SplashTheme, z);
    }

    public static void show(Activity activity) {
        show(activity, false);
    }

    public static void hide(final Activity activity) {
        if (activity == null) {
            WeakReference<Activity> weakReference = mActivity;
            if (weakReference == null) {
                return;
            }
            activity = weakReference.get();
        }
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() { // from class: org.devio.rn.splashscreen.SplashScreen.2
            @Override // java.lang.Runnable
            public void run() {
                if (SplashScreen.mSplashDialog == null || !SplashScreen.mSplashDialog.isShowing()) {
                    return;
                }
                boolean isDestroyed = activity.isDestroyed();
                if (!activity.isFinishing() && !isDestroyed) {
                    SplashScreen.mSplashDialog.dismiss();
                }
                SplashScreen.mSplashDialog = null;
            }
        });
    }

    public static void setActivityAndroidP(Dialog dialog) {
        if (Build.VERSION.SDK_INT < 28 || dialog == null || dialog.getWindow() == null) {
            return;
        }
        dialog.getWindow().addFlags(512);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.layoutInDisplayCutoutMode = 1;
        dialog.getWindow().setAttributes(attributes);
    }
}
