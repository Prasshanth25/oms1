package com.facebook.react.views.text;

import android.content.Context;
import android.graphics.Rect;
import android.text.Layout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

/* loaded from: classes.dex */
public class FontMetricsUtil {
    private static final float AMPLIFICATION_FACTOR = 100.0f;
    private static final String CAP_HEIGHT_MEASUREMENT_TEXT = "T";
    private static final String X_HEIGHT_MEASUREMENT_TEXT = "x";

    public static WritableArray getFontMetrics(CharSequence charSequence, Layout layout, TextPaint textPaint, Context context) {
        Rect rect;
        Rect rect2;
        Rect rect3;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        WritableArray createArray = Arguments.createArray();
        TextPaint textPaint2 = new TextPaint(textPaint);
        textPaint2.setTextSize(textPaint2.getTextSize() * AMPLIFICATION_FACTOR);
        int i = 1;
        textPaint2.getTextBounds("T", 0, 1, new Rect());
        double height = (rect.height() / AMPLIFICATION_FACTOR) / displayMetrics.density;
        textPaint2.getTextBounds(X_HEIGHT_MEASUREMENT_TEXT, 0, 1, new Rect());
        double height2 = (rect2.height() / AMPLIFICATION_FACTOR) / displayMetrics.density;
        int i2 = 0;
        while (i2 < layout.getLineCount()) {
            float lineMax = charSequence.length() > 0 && charSequence.charAt(layout.getLineEnd(i2) - i) == '\n' ? layout.getLineMax(i2) : layout.getLineWidth(i2);
            layout.getLineBounds(i2, new Rect());
            WritableMap createMap = Arguments.createMap();
            double d = height;
            createMap.putDouble(X_HEIGHT_MEASUREMENT_TEXT, layout.getLineLeft(i2) / displayMetrics.density);
            createMap.putDouble("y", rect3.top / displayMetrics.density);
            createMap.putDouble("width", lineMax / displayMetrics.density);
            createMap.putDouble("height", rect3.height() / displayMetrics.density);
            createMap.putDouble("descender", layout.getLineDescent(i2) / displayMetrics.density);
            createMap.putDouble("ascender", (-layout.getLineAscent(i2)) / displayMetrics.density);
            createMap.putDouble("baseline", layout.getLineBaseline(i2) / displayMetrics.density);
            createMap.putDouble("capHeight", d);
            createMap.putDouble("xHeight", height2);
            createMap.putString("text", charSequence.subSequence(layout.getLineStart(i2), layout.getLineEnd(i2)).toString());
            createArray.pushMap(createMap);
            i2++;
            height = d;
            i = 1;
        }
        return createArray;
    }
}
