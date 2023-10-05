package com.henninghall.date_picker.props;

import com.facebook.react.bridge.Dynamic;
import com.henninghall.date_picker.LocaleUtils;
import java.util.Locale;

/* loaded from: classes.dex */
public class LocaleProp extends Prop<Locale> {
    public static final String name = "locale";
    private String languageTag;

    public LocaleProp() {
        super(getDefaultLocale());
        this.languageTag = getDefaultLanguageTag();
    }

    private static Locale getDefaultLocale() {
        return LocaleUtils.getLocale(getDefaultLanguageTag());
    }

    private static String getDefaultLanguageTag() {
        return Locale.getDefault().toLanguageTag().replace('-', '_');
    }

    public String getLanguageTag() {
        return this.languageTag;
    }

    @Override // com.henninghall.date_picker.props.Prop
    public Locale toValue(Dynamic dynamic) {
        String replace = dynamic.asString().replace('-', '_');
        this.languageTag = replace;
        return LocaleUtils.getLocale(replace);
    }
}
