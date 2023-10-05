package com.facebook.react.viewmanagers;

import android.view.View;
import androidx.core.app.NotificationCompat;
import com.facebook.react.bridge.ColorPropConverter;
import com.facebook.react.uimanager.BaseViewManagerDelegate;
import com.facebook.react.uimanager.BaseViewManagerInterface;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.viewmanagers.AndroidProgressBarManagerInterface;

/* loaded from: classes.dex */
public class AndroidProgressBarManagerDelegate<T extends View, U extends BaseViewManagerInterface<T> & AndroidProgressBarManagerInterface<T>> extends BaseViewManagerDelegate<T, U> {
    public AndroidProgressBarManagerDelegate(BaseViewManagerInterface baseViewManagerInterface) {
        super(baseViewManagerInterface);
    }

    @Override // com.facebook.react.uimanager.BaseViewManagerDelegate, com.facebook.react.uimanager.ViewManagerDelegate
    public void setProperty(T t, String str, Object obj) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1001078227:
                if (str.equals(NotificationCompat.CATEGORY_PROGRESS)) {
                    c = 0;
                    break;
                }
                break;
            case -877170387:
                if (str.equals(ViewProps.TEST_ID)) {
                    c = 1;
                    break;
                }
                break;
            case -676876213:
                if (str.equals("typeAttr")) {
                    c = 2;
                    break;
                }
                break;
            case 94842723:
                if (str.equals(ViewProps.COLOR)) {
                    c = 3;
                    break;
                }
                break;
            case 633138363:
                if (str.equals("indeterminate")) {
                    c = 4;
                    break;
                }
                break;
            case 1118509918:
                if (str.equals("animating")) {
                    c = 5;
                    break;
                }
                break;
            case 1804741442:
                if (str.equals("styleAttr")) {
                    c = 6;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                ((AndroidProgressBarManagerInterface) this.mViewManager).setProgress(t, obj == null ? 0.0d : ((Double) obj).doubleValue());
                return;
            case 1:
                ((AndroidProgressBarManagerInterface) this.mViewManager).setTestID(t, obj == null ? "" : (String) obj);
                return;
            case 2:
                ((AndroidProgressBarManagerInterface) this.mViewManager).setTypeAttr(t, obj != null ? (String) obj : null);
                return;
            case 3:
                ((AndroidProgressBarManagerInterface) this.mViewManager).setColor(t, ColorPropConverter.getColor(obj, t.getContext()));
                return;
            case 4:
                ((AndroidProgressBarManagerInterface) this.mViewManager).setIndeterminate(t, obj != null ? ((Boolean) obj).booleanValue() : false);
                return;
            case 5:
                ((AndroidProgressBarManagerInterface) this.mViewManager).setAnimating(t, obj != null ? ((Boolean) obj).booleanValue() : true);
                return;
            case 6:
                ((AndroidProgressBarManagerInterface) this.mViewManager).setStyleAttr(t, obj != null ? (String) obj : null);
                return;
            default:
                super.setProperty(t, str, obj);
                return;
        }
    }
}
