package com.facebook.react.views.text;

import android.content.Context;
import android.os.Build;
import android.text.BoringLayout;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.LruCache;
import com.facebook.react.bridge.ReactNoCrashSoftException;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ReactStylesDiffMap;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.yoga.YogaConstants;
import com.facebook.yoga.YogaMeasureMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class TextLayoutManager {
    private static final boolean DEFAULT_INCLUDE_FONT_PADDING = true;
    private static final boolean ENABLE_MEASURE_LOGGING = false;
    private static final String HYPHENATION_FREQUENCY_KEY = "android_hyphenationFrequency";
    private static final String INCLUDE_FONT_PADDING_KEY = "includeFontPadding";
    private static final String INLINE_VIEW_PLACEHOLDER = "0";
    private static final String MAXIMUM_NUMBER_OF_LINES_KEY = "maximumNumberOfLines";
    private static final String TAG = "TextLayoutManager";
    private static final String TEXT_BREAK_STRATEGY_KEY = "textBreakStrategy";
    private static final int spannableCacheSize = 100;
    private static final TextPaint sTextPaintInstance = new TextPaint(1);
    private static final Object sSpannableCacheLock = new Object();
    private static final LruCache<ReadableNativeMap, Spannable> sSpannableCache = new LruCache<>(100);
    private static final ConcurrentHashMap<Integer, Spannable> sTagToSpannableCache = new ConcurrentHashMap<>();

    public static boolean isRTL(ReadableMap readableMap) {
        ReadableMap map;
        ReadableArray array = readableMap.getArray("fragments");
        return array != null && array.size() > 0 && (map = array.getMap(0).getMap("textAttributes")) != null && TextAttributeProps.getLayoutDirection(map.getString(ViewProps.LAYOUT_DIRECTION)) == 1;
    }

    public static void setCachedSpannabledForTag(int i, Spannable spannable) {
        sTagToSpannableCache.put(Integer.valueOf(i), spannable);
    }

    public static void deleteCachedSpannableForTag(int i) {
        sTagToSpannableCache.remove(Integer.valueOf(i));
    }

    private static void buildSpannableFromFragment(Context context, ReadableArray readableArray, SpannableStringBuilder spannableStringBuilder, List<SetSpanOperation> list) {
        int i;
        int i2 = 0;
        for (int size = readableArray.size(); i2 < size; size = i) {
            ReadableMap map = readableArray.getMap(i2);
            int length = spannableStringBuilder.length();
            TextAttributeProps fromReadableMap = TextAttributeProps.fromReadableMap(new ReactStylesDiffMap(map.getMap("textAttributes")));
            spannableStringBuilder.append((CharSequence) TextTransform.apply(map.getString("string"), fromReadableMap.mTextTransform));
            int length2 = spannableStringBuilder.length();
            int i3 = map.hasKey("reactTag") ? map.getInt("reactTag") : -1;
            if (map.hasKey(ViewProps.IS_ATTACHMENT) && map.getBoolean(ViewProps.IS_ATTACHMENT)) {
                list.add(new SetSpanOperation(spannableStringBuilder.length() - 1, spannableStringBuilder.length(), new TextInlineViewPlaceholderSpan(i3, (int) PixelUtil.toPixelFromSP(map.getDouble("width")), (int) PixelUtil.toPixelFromSP(map.getDouble("height")))));
            } else if (length2 >= length) {
                if (fromReadableMap.mIsAccessibilityLink) {
                    list.add(new SetSpanOperation(length, length2, new ReactClickableSpan(i3)));
                }
                if (fromReadableMap.mIsColorSet) {
                    list.add(new SetSpanOperation(length, length2, new ReactForegroundColorSpan(fromReadableMap.mColor)));
                }
                if (fromReadableMap.mIsBackgroundColorSet) {
                    list.add(new SetSpanOperation(length, length2, new ReactBackgroundColorSpan(fromReadableMap.mBackgroundColor)));
                }
                if (!Float.isNaN(fromReadableMap.getLetterSpacing())) {
                    list.add(new SetSpanOperation(length, length2, new CustomLetterSpacingSpan(fromReadableMap.getLetterSpacing())));
                }
                list.add(new SetSpanOperation(length, length2, new ReactAbsoluteSizeSpan(fromReadableMap.mFontSize)));
                if (fromReadableMap.mFontStyle == -1 && fromReadableMap.mFontWeight == -1 && fromReadableMap.mFontFamily == null) {
                    i = size;
                } else {
                    i = size;
                    list.add(new SetSpanOperation(length, length2, new CustomStyleSpan(fromReadableMap.mFontStyle, fromReadableMap.mFontWeight, fromReadableMap.mFontFeatureSettings, fromReadableMap.mFontFamily, context.getAssets())));
                }
                if (fromReadableMap.mIsUnderlineTextDecorationSet) {
                    list.add(new SetSpanOperation(length, length2, new ReactUnderlineSpan()));
                }
                if (fromReadableMap.mIsLineThroughTextDecorationSet) {
                    list.add(new SetSpanOperation(length, length2, new ReactStrikethroughSpan()));
                }
                if (fromReadableMap.mTextShadowOffsetDx != 0.0f || fromReadableMap.mTextShadowOffsetDy != 0.0f) {
                    list.add(new SetSpanOperation(length, length2, new ShadowStyleSpan(fromReadableMap.mTextShadowOffsetDx, fromReadableMap.mTextShadowOffsetDy, fromReadableMap.mTextShadowRadius, fromReadableMap.mTextShadowColor)));
                }
                if (!Float.isNaN(fromReadableMap.getEffectiveLineHeight())) {
                    list.add(new SetSpanOperation(length, length2, new CustomLineHeightSpan(fromReadableMap.getEffectiveLineHeight())));
                }
                list.add(new SetSpanOperation(length, length2, new ReactTagSpan(i3)));
                i2++;
            }
            i = size;
            i2++;
        }
    }

    public static Spannable getOrCreateSpannableForText(Context context, ReadableMap readableMap, ReactTextViewManagerCallback reactTextViewManagerCallback) {
        return createSpannableFromAttributedString(context, readableMap, reactTextViewManagerCallback);
    }

    private static Spannable createSpannableFromAttributedString(Context context, ReadableMap readableMap, ReactTextViewManagerCallback reactTextViewManagerCallback) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        ArrayList arrayList = new ArrayList();
        buildSpannableFromFragment(context, readableMap.getArray("fragments"), spannableStringBuilder, arrayList);
        for (int i = 0; i < arrayList.size(); i++) {
            ((SetSpanOperation) arrayList.get((arrayList.size() - i) - 1)).execute(spannableStringBuilder, i);
        }
        if (reactTextViewManagerCallback != null) {
            reactTextViewManagerCallback.onPostProcessSpannable(spannableStringBuilder);
        }
        return spannableStringBuilder;
    }

    private static Layout createLayout(Spannable spannable, BoringLayout.Metrics metrics, float f, YogaMeasureMode yogaMeasureMode, boolean z, int i, int i2) {
        int i3;
        int length = spannable.length();
        boolean z2 = yogaMeasureMode == YogaMeasureMode.UNDEFINED || f < 0.0f;
        float desiredWidth = metrics == null ? Layout.getDesiredWidth(spannable, sTextPaintInstance) : Float.NaN;
        if (metrics == null && (z2 || (!YogaConstants.isUndefined(desiredWidth) && desiredWidth <= f))) {
            int ceil = (int) Math.ceil(desiredWidth);
            if (Build.VERSION.SDK_INT < 23) {
                return new StaticLayout(spannable, sTextPaintInstance, ceil, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, z);
            }
            return StaticLayout.Builder.obtain(spannable, 0, length, sTextPaintInstance, ceil).setAlignment(Layout.Alignment.ALIGN_NORMAL).setLineSpacing(0.0f, 1.0f).setIncludePad(z).setBreakStrategy(i).setHyphenationFrequency(i2).build();
        } else if (metrics != null && (z2 || metrics.width <= f)) {
            int i4 = metrics.width;
            if (metrics.width < 0) {
                String str = TAG;
                ReactSoftExceptionLogger.logSoftException(str, new ReactNoCrashSoftException("Text width is invalid: " + metrics.width));
                i3 = 0;
            } else {
                i3 = i4;
            }
            return BoringLayout.make(spannable, sTextPaintInstance, i3, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, metrics, z);
        } else if (Build.VERSION.SDK_INT < 23) {
            return new StaticLayout(spannable, sTextPaintInstance, (int) f, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, z);
        } else {
            StaticLayout.Builder hyphenationFrequency = StaticLayout.Builder.obtain(spannable, 0, length, sTextPaintInstance, (int) f).setAlignment(Layout.Alignment.ALIGN_NORMAL).setLineSpacing(0.0f, 1.0f).setIncludePad(z).setBreakStrategy(i).setHyphenationFrequency(i2);
            if (Build.VERSION.SDK_INT >= 28) {
                hyphenationFrequency.setUseLineSpacingFromFallbacks(true);
            }
            return hyphenationFrequency.build();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:286:0x00c4, code lost:
        if (r3 > r21) goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x00e4, code lost:
        if (r1 > r23) goto L107;
     */
    /* JADX WARN: Removed duplicated region for block: B:290:0x00ce  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x00f0  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x0173  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x0178  */
    /* JADX WARN: Removed duplicated region for block: B:331:0x017e  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x0181  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long measureText(android.content.Context r18, com.facebook.react.bridge.ReadableMap r19, com.facebook.react.bridge.ReadableMap r20, float r21, com.facebook.yoga.YogaMeasureMode r22, float r23, com.facebook.yoga.YogaMeasureMode r24, com.facebook.react.views.text.ReactTextViewManagerCallback r25, float[] r26) {
        /*
            Method dump skipped, instructions count: 484
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.text.TextLayoutManager.measureText(android.content.Context, com.facebook.react.bridge.ReadableMap, com.facebook.react.bridge.ReadableMap, float, com.facebook.yoga.YogaMeasureMode, float, com.facebook.yoga.YogaMeasureMode, com.facebook.react.views.text.ReactTextViewManagerCallback, float[]):long");
    }

    public static WritableArray measureLines(Context context, ReadableMap readableMap, ReadableMap readableMap2, float f) {
        Spannable orCreateSpannableForText = getOrCreateSpannableForText(context, readableMap, null);
        TextPaint textPaint = sTextPaintInstance;
        return FontMetricsUtil.getFontMetrics(orCreateSpannableForText, createLayout(orCreateSpannableForText, BoringLayout.isBoring(orCreateSpannableForText, textPaint), f, YogaMeasureMode.EXACTLY, readableMap2.hasKey("includeFontPadding") ? readableMap2.getBoolean("includeFontPadding") : true, TextAttributeProps.getTextBreakStrategy(readableMap2.getString("textBreakStrategy")), TextAttributeProps.getTextBreakStrategy(readableMap2.getString(HYPHENATION_FREQUENCY_KEY))), textPaint, context);
    }
}
