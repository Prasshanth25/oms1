package com.facebook.react.viewmanagers;

import android.view.View;
import com.facebook.react.bridge.ColorPropConverter;
import com.facebook.react.uimanager.BaseViewManagerDelegate;
import com.facebook.react.uimanager.BaseViewManagerInterface;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.viewmanagers.RNGestureHandlerButtonManagerInterface;

/* loaded from: classes.dex */
public class RNGestureHandlerButtonManagerDelegate<T extends View, U extends BaseViewManagerInterface<T> & RNGestureHandlerButtonManagerInterface<T>> extends BaseViewManagerDelegate<T, U> {
    public RNGestureHandlerButtonManagerDelegate(BaseViewManagerInterface baseViewManagerInterface) {
        super(baseViewManagerInterface);
    }

    @Override // com.facebook.react.uimanager.BaseViewManagerDelegate, com.facebook.react.uimanager.ViewManagerDelegate
    public void setProperty(T t, String str, Object obj) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -2143114526:
                if (str.equals("rippleRadius")) {
                    c = 0;
                    break;
                }
                break;
            case -1609594047:
                if (str.equals(ViewProps.ENABLED)) {
                    c = 1;
                    break;
                }
                break;
            case -775297261:
                if (str.equals("rippleColor")) {
                    c = 2;
                    break;
                }
                break;
            case 1387411372:
                if (str.equals("touchSoundDisabled")) {
                    c = 3;
                    break;
                }
                break;
            case 1686617758:
                if (str.equals("exclusive")) {
                    c = 4;
                    break;
                }
                break;
            case 1825644485:
                if (str.equals("borderless")) {
                    c = 5;
                    break;
                }
                break;
            case 1984457027:
                if (str.equals("foreground")) {
                    c = 6;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                ((RNGestureHandlerButtonManagerInterface) this.mViewManager).setRippleRadius(t, obj != null ? ((Double) obj).intValue() : 0);
                return;
            case 1:
                ((RNGestureHandlerButtonManagerInterface) this.mViewManager).setEnabled(t, obj != null ? ((Boolean) obj).booleanValue() : true);
                return;
            case 2:
                ((RNGestureHandlerButtonManagerInterface) this.mViewManager).setRippleColor(t, ColorPropConverter.getColor(obj, t.getContext()));
                return;
            case 3:
                ((RNGestureHandlerButtonManagerInterface) this.mViewManager).setTouchSoundDisabled(t, obj != null ? ((Boolean) obj).booleanValue() : false);
                return;
            case 4:
                ((RNGestureHandlerButtonManagerInterface) this.mViewManager).setExclusive(t, obj != null ? ((Boolean) obj).booleanValue() : true);
                return;
            case 5:
                ((RNGestureHandlerButtonManagerInterface) this.mViewManager).setBorderless(t, obj != null ? ((Boolean) obj).booleanValue() : false);
                return;
            case 6:
                ((RNGestureHandlerButtonManagerInterface) this.mViewManager).setForeground(t, obj != null ? ((Boolean) obj).booleanValue() : false);
                return;
            default:
                super.setProperty(t, str, obj);
                return;
        }
    }
}
