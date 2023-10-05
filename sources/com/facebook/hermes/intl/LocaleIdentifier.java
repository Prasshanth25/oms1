package com.facebook.hermes.intl;

import com.facebook.hermes.intl.LocaleIdTokenizer;
import com.facebook.hermes.intl.ParsedLocaleIdentifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;

/* loaded from: classes.dex */
public class LocaleIdentifier {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    private static void addVariantSubtag(String str, ParsedLocaleIdentifier.ParsedLanguageIdentifier parsedLanguageIdentifier) throws JSRangeErrorException {
        if (parsedLanguageIdentifier.variantSubtagList != null) {
            int binarySearch = Collections.binarySearch(parsedLanguageIdentifier.variantSubtagList, str);
            if (binarySearch < 0) {
                parsedLanguageIdentifier.variantSubtagList.add((binarySearch * (-1)) - 1, str);
                return;
            }
            throw new JSRangeErrorException("Duplicate variant");
        }
        parsedLanguageIdentifier.variantSubtagList = new ArrayList<>();
        parsedLanguageIdentifier.variantSubtagList.add(str);
    }

    public static void replaceLanguageSubtagIfNeeded(StringBuffer stringBuffer, StringBuffer stringBuffer2, StringBuffer stringBuffer3) {
        String[] strArr;
        String[] strArr2;
        String[] strArr3;
        String[] strArr4;
        String[] strArr5;
        String[] strArr6;
        if (LanguageTagsGenerated.languageAliasKeys2 == null) {
            return;
        }
        if (stringBuffer.length() == 2) {
            strArr = LanguageTagsGenerated.languageAliasKeys2;
            strArr2 = LanguageTagsGenerated.languageAliasReplacements2;
            strArr3 = LanguageTagsGenerated.complexLanguageAliasKeys2;
            strArr4 = LanguageTagsGenerated.complexLanguageAliasReplacementsLanguage2;
            strArr5 = LanguageTagsGenerated.complexLanguageAliasReplacementsScript2;
            strArr6 = LanguageTagsGenerated.complexLanguageAliasReplacementsRegion2;
        } else {
            strArr = LanguageTagsGenerated.languageAliasKeys3;
            strArr2 = LanguageTagsGenerated.languageAliasReplacements3;
            strArr3 = LanguageTagsGenerated.complexLanguageAliasKeys3;
            strArr4 = LanguageTagsGenerated.complexLanguageAliasReplacementsLanguage3;
            strArr5 = LanguageTagsGenerated.complexLanguageAliasReplacementsScript3;
            strArr6 = LanguageTagsGenerated.complexLanguageAliasReplacementsRegion3;
        }
        int binarySearch = Arrays.binarySearch(strArr, stringBuffer.toString());
        if (binarySearch >= 0) {
            stringBuffer.delete(0, stringBuffer.length());
            stringBuffer.append(strArr2[binarySearch]);
            return;
        }
        int binarySearch2 = Arrays.binarySearch(strArr3, stringBuffer.toString());
        if (binarySearch2 >= 0) {
            String str = strArr4[binarySearch2];
            String str2 = strArr5[binarySearch2];
            String str3 = strArr6[binarySearch2];
            stringBuffer.delete(0, stringBuffer.length());
            stringBuffer.append(str);
            if (stringBuffer2.length() == 0 && str2 != null) {
                stringBuffer2.append(str2);
            }
            if (stringBuffer3.length() != 0 || str3 == null) {
                return;
            }
            stringBuffer3.append(str3);
        }
    }

    public static String replaceRegionSubtagIfNeeded(StringBuffer stringBuffer) {
        if (LanguageTagsGenerated.regionAliasKeys2 == null) {
            return stringBuffer.toString();
        }
        if (stringBuffer.length() == 2) {
            int binarySearch = Arrays.binarySearch(LanguageTagsGenerated.regionAliasKeys2, stringBuffer.toString());
            if (binarySearch >= 0) {
                return LanguageTagsGenerated.regionAliasReplacements2[binarySearch];
            }
            return stringBuffer.toString();
        }
        int binarySearch2 = Arrays.binarySearch(LanguageTagsGenerated.regionAliasKeys3, stringBuffer.toString());
        if (binarySearch2 >= 0) {
            return LanguageTagsGenerated.regionAliasReplacements3[binarySearch2];
        }
        return stringBuffer.toString();
    }

    public static String canonicalizeLocaleId(String str) throws JSRangeErrorException {
        return LocaleObject.createFromLocaleId(str).toCanonicalTag();
    }

    /* JADX WARN: Removed duplicated region for block: B:138:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x008b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static void parseUnicodeExtensions(java.lang.CharSequence r3, com.facebook.hermes.intl.LocaleIdTokenizer r4, com.facebook.hermes.intl.ParsedLocaleIdentifier r5) throws com.facebook.hermes.intl.JSRangeErrorException, com.facebook.hermes.intl.LocaleIdTokenizer.LocaleIdSubtagIterationFailed {
        /*
            boolean r0 = r4.hasMoreSubtags()
            if (r0 == 0) goto La5
            com.facebook.hermes.intl.LocaleIdTokenizer$LocaleIdSubtag r0 = r4.nextSubtag()
            java.util.ArrayList<java.lang.CharSequence> r1 = r5.unicodeExtensionAttributes
            if (r1 != 0) goto L93
            java.util.TreeMap<java.lang.String, java.util.ArrayList<java.lang.String>> r1 = r5.unicodeExtensionKeywords
            if (r1 != 0) goto L93
        L12:
            boolean r1 = r0.isUnicodeExtensionAttribute()
            if (r1 == 0) goto L38
            java.util.ArrayList<java.lang.CharSequence> r1 = r5.unicodeExtensionAttributes
            if (r1 != 0) goto L23
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r5.unicodeExtensionAttributes = r1
        L23:
            java.util.ArrayList<java.lang.CharSequence> r1 = r5.unicodeExtensionAttributes
            java.lang.String r0 = r0.toString()
            r1.add(r0)
            boolean r0 = r4.hasMoreSubtags()
            if (r0 != 0) goto L33
            return
        L33:
            com.facebook.hermes.intl.LocaleIdTokenizer$LocaleIdSubtag r0 = r4.nextSubtag()
            goto L12
        L38:
            boolean r1 = r0.isUnicodeExtensionKey()
            if (r1 == 0) goto L81
            java.util.TreeMap<java.lang.String, java.util.ArrayList<java.lang.String>> r1 = r5.unicodeExtensionKeywords
            if (r1 != 0) goto L49
            java.util.TreeMap r1 = new java.util.TreeMap
            r1.<init>()
            r5.unicodeExtensionKeywords = r1
        L49:
            java.lang.String r0 = r0.toString()
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.util.TreeMap<java.lang.String, java.util.ArrayList<java.lang.String>> r2 = r5.unicodeExtensionKeywords
            r2.put(r0, r1)
            boolean r0 = r4.hasMoreSubtags()
            if (r0 != 0) goto L5e
            return
        L5e:
            com.facebook.hermes.intl.LocaleIdTokenizer$LocaleIdSubtag r0 = r4.nextSubtag()
        L62:
            boolean r2 = r0.isUnicodeExtensionKeyTypeItem()
            if (r2 == 0) goto L7b
            java.lang.String r0 = r0.toString()
            r1.add(r0)
            boolean r0 = r4.hasMoreSubtags()
            if (r0 != 0) goto L76
            return
        L76:
            com.facebook.hermes.intl.LocaleIdTokenizer$LocaleIdSubtag r0 = r4.nextSubtag()
            goto L62
        L7b:
            boolean r1 = r0.isUnicodeExtensionKey()
            if (r1 != 0) goto L49
        L81:
            boolean r1 = r0.isExtensionSingleton()
            if (r1 == 0) goto L8b
            parseExtensions(r3, r0, r4, r5)
            return
        L8b:
            com.facebook.hermes.intl.JSRangeErrorException r3 = new com.facebook.hermes.intl.JSRangeErrorException
            java.lang.String r4 = "Malformed sequence expected."
            r3.<init>(r4)
            throw r3
        L93:
            com.facebook.hermes.intl.JSRangeErrorException r4 = new com.facebook.hermes.intl.JSRangeErrorException
            r5 = 1
            java.lang.Object[] r5 = new java.lang.Object[r5]
            r0 = 0
            r5[r0] = r3
            java.lang.String r3 = "Duplicate unicode extension sequence in [%s]"
            java.lang.String r3 = java.lang.String.format(r3, r5)
            r4.<init>(r3)
            throw r4
        La5:
            com.facebook.hermes.intl.JSRangeErrorException r3 = new com.facebook.hermes.intl.JSRangeErrorException
            java.lang.String r4 = "Extension sequence expected."
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.hermes.intl.LocaleIdentifier.parseUnicodeExtensions(java.lang.CharSequence, com.facebook.hermes.intl.LocaleIdTokenizer, com.facebook.hermes.intl.ParsedLocaleIdentifier):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0075  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static void parseTransformedExtensionFields(java.lang.CharSequence r4, com.facebook.hermes.intl.LocaleIdTokenizer r5, com.facebook.hermes.intl.LocaleIdTokenizer.LocaleIdSubtag r6, com.facebook.hermes.intl.ParsedLocaleIdentifier r7) throws com.facebook.hermes.intl.JSRangeErrorException, com.facebook.hermes.intl.LocaleIdTokenizer.LocaleIdSubtagIterationFailed {
        /*
            boolean r0 = r6.isTranformedExtensionTKey()
            if (r0 == 0) goto L6f
            java.util.TreeMap<java.lang.String, java.util.ArrayList<java.lang.String>> r0 = r7.transformedExtensionFields
            r1 = 0
            r2 = 1
            if (r0 != 0) goto L5f
            java.util.TreeMap<java.lang.String, java.util.ArrayList<java.lang.String>> r0 = r7.transformedExtensionFields
            if (r0 != 0) goto L17
            java.util.TreeMap r0 = new java.util.TreeMap
            r0.<init>()
            r7.transformedExtensionFields = r0
        L17:
            java.lang.String r6 = r6.toString()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.TreeMap<java.lang.String, java.util.ArrayList<java.lang.String>> r3 = r7.transformedExtensionFields
            r3.put(r6, r0)
            boolean r6 = r5.hasMoreSubtags()
            if (r6 == 0) goto L4f
            com.facebook.hermes.intl.LocaleIdTokenizer$LocaleIdSubtag r6 = r5.nextSubtag()
        L2f:
            boolean r3 = r6.isTranformedExtensionTValueItem()
            if (r3 == 0) goto L48
            java.lang.String r6 = r6.toString()
            r0.add(r6)
            boolean r6 = r5.hasMoreSubtags()
            if (r6 != 0) goto L43
            return
        L43:
            com.facebook.hermes.intl.LocaleIdTokenizer$LocaleIdSubtag r6 = r5.nextSubtag()
            goto L2f
        L48:
            boolean r0 = r6.isTranformedExtensionTKey()
            if (r0 != 0) goto L17
            goto L6f
        L4f:
            com.facebook.hermes.intl.JSRangeErrorException r5 = new com.facebook.hermes.intl.JSRangeErrorException
            java.lang.Object[] r6 = new java.lang.Object[r2]
            r6[r1] = r4
            java.lang.String r4 = "Malformated transformed key in : %s"
            java.lang.String r4 = java.lang.String.format(r4, r6)
            r5.<init>(r4)
            throw r5
        L5f:
            com.facebook.hermes.intl.JSRangeErrorException r5 = new com.facebook.hermes.intl.JSRangeErrorException
            java.lang.Object[] r6 = new java.lang.Object[r2]
            r6[r1] = r4
            java.lang.String r4 = "Duplicate transformed extension sequence in [%s]"
            java.lang.String r4 = java.lang.String.format(r4, r6)
            r5.<init>(r4)
            throw r5
        L6f:
            boolean r0 = r6.isExtensionSingleton()
            if (r0 == 0) goto L79
            parseExtensions(r4, r6, r5, r7)
            return
        L79:
            com.facebook.hermes.intl.JSRangeErrorException r4 = new com.facebook.hermes.intl.JSRangeErrorException
            java.lang.String r5 = "Malformed extension sequence."
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.hermes.intl.LocaleIdentifier.parseTransformedExtensionFields(java.lang.CharSequence, com.facebook.hermes.intl.LocaleIdTokenizer, com.facebook.hermes.intl.LocaleIdTokenizer$LocaleIdSubtag, com.facebook.hermes.intl.ParsedLocaleIdentifier):void");
    }

    static void parseTransformedExtensions(CharSequence charSequence, LocaleIdTokenizer localeIdTokenizer, ParsedLocaleIdentifier parsedLocaleIdentifier) throws JSRangeErrorException, LocaleIdTokenizer.LocaleIdSubtagIterationFailed {
        if (!localeIdTokenizer.hasMoreSubtags()) {
            throw new JSRangeErrorException("Extension sequence expected.");
        }
        LocaleIdTokenizer.LocaleIdSubtag nextSubtag = localeIdTokenizer.nextSubtag();
        if (nextSubtag.isUnicodeLanguageSubtag()) {
            parseLanguageId(charSequence, localeIdTokenizer, nextSubtag, true, parsedLocaleIdentifier);
        } else if (nextSubtag.isTranformedExtensionTKey()) {
            parseTransformedExtensionFields(charSequence, localeIdTokenizer, nextSubtag, parsedLocaleIdentifier);
        } else {
            throw new JSRangeErrorException(String.format("Unexpected token [%s] in transformed extension sequence [%s]", nextSubtag.toString(), charSequence));
        }
    }

    static void parsePrivateUseExtensions(CharSequence charSequence, LocaleIdTokenizer localeIdTokenizer, ParsedLocaleIdentifier parsedLocaleIdentifier) throws JSRangeErrorException, LocaleIdTokenizer.LocaleIdSubtagIterationFailed {
        if (!localeIdTokenizer.hasMoreSubtags()) {
            throw new JSRangeErrorException("Extension sequence expected.");
        }
        LocaleIdTokenizer.LocaleIdSubtag nextSubtag = localeIdTokenizer.nextSubtag();
        if (parsedLocaleIdentifier.puExtensions == null) {
            parsedLocaleIdentifier.puExtensions = new ArrayList<>();
        }
        while (nextSubtag.isPrivateUseExtension()) {
            parsedLocaleIdentifier.puExtensions.add(nextSubtag.toString());
            if (!localeIdTokenizer.hasMoreSubtags()) {
                return;
            }
            nextSubtag = localeIdTokenizer.nextSubtag();
        }
        throw new JSRangeErrorException("Tokens are not expected after pu extension.");
    }

    static void parseOtherExtensions(CharSequence charSequence, LocaleIdTokenizer localeIdTokenizer, ParsedLocaleIdentifier parsedLocaleIdentifier, char c) throws JSRangeErrorException, LocaleIdTokenizer.LocaleIdSubtagIterationFailed {
        if (!localeIdTokenizer.hasMoreSubtags()) {
            throw new JSRangeErrorException("Extension sequence expected.");
        }
        LocaleIdTokenizer.LocaleIdSubtag nextSubtag = localeIdTokenizer.nextSubtag();
        if (parsedLocaleIdentifier.otherExtensionsMap == null) {
            parsedLocaleIdentifier.otherExtensionsMap = new TreeMap<>();
        }
        ArrayList<String> arrayList = new ArrayList<>();
        parsedLocaleIdentifier.otherExtensionsMap.put(new Character(c), arrayList);
        while (nextSubtag.isOtherExtension()) {
            arrayList.add(nextSubtag.toString());
            if (!localeIdTokenizer.hasMoreSubtags()) {
                return;
            }
            nextSubtag = localeIdTokenizer.nextSubtag();
        }
        if (nextSubtag.isExtensionSingleton()) {
            parseExtensions(charSequence, nextSubtag, localeIdTokenizer, parsedLocaleIdentifier);
            return;
        }
        throw new JSRangeErrorException("Malformed sequence expected.");
    }

    static void parseExtensions(CharSequence charSequence, LocaleIdTokenizer.LocaleIdSubtag localeIdSubtag, LocaleIdTokenizer localeIdTokenizer, ParsedLocaleIdentifier parsedLocaleIdentifier) throws JSRangeErrorException, LocaleIdTokenizer.LocaleIdSubtagIterationFailed {
        if (!localeIdTokenizer.hasMoreSubtags()) {
            throw new JSRangeErrorException("Extension sequence expected.");
        }
        char charAt = localeIdSubtag.toString().charAt(0);
        if (charAt == 'u') {
            parseUnicodeExtensions(charSequence, localeIdTokenizer, parsedLocaleIdentifier);
        } else if (charAt == 't') {
            parseTransformedExtensions(charSequence, localeIdTokenizer, parsedLocaleIdentifier);
        } else if (charAt == 'x') {
            parsePrivateUseExtensions(charSequence, localeIdTokenizer, parsedLocaleIdentifier);
        } else {
            parseOtherExtensions(charSequence, localeIdTokenizer, parsedLocaleIdentifier, charAt);
        }
    }

    static boolean handleExtensions(CharSequence charSequence, LocaleIdTokenizer localeIdTokenizer, LocaleIdTokenizer.LocaleIdSubtag localeIdSubtag, boolean z, ParsedLocaleIdentifier parsedLocaleIdentifier) throws JSRangeErrorException, LocaleIdTokenizer.LocaleIdSubtagIterationFailed {
        if (z && localeIdSubtag.isTranformedExtensionTKey()) {
            parseTransformedExtensionFields(charSequence, localeIdTokenizer, localeIdSubtag, parsedLocaleIdentifier);
            return true;
        } else if (localeIdSubtag.isExtensionSingleton()) {
            if (!z) {
                parseExtensions(charSequence, localeIdSubtag, localeIdTokenizer, parsedLocaleIdentifier);
                return true;
            }
            throw new JSRangeErrorException(String.format("Extension singletons in transformed extension language tag: %s", charSequence));
        } else {
            return false;
        }
    }

    static void parseLanguageId(CharSequence charSequence, LocaleIdTokenizer localeIdTokenizer, LocaleIdTokenizer.LocaleIdSubtag localeIdSubtag, boolean z, ParsedLocaleIdentifier parsedLocaleIdentifier) throws JSRangeErrorException, LocaleIdTokenizer.LocaleIdSubtagIterationFailed {
        ParsedLocaleIdentifier.ParsedLanguageIdentifier parsedLanguageIdentifier = new ParsedLocaleIdentifier.ParsedLanguageIdentifier();
        if (z) {
            parsedLocaleIdentifier.transformedLanguageIdentifier = parsedLanguageIdentifier;
        } else {
            parsedLocaleIdentifier.languageIdentifier = parsedLanguageIdentifier;
        }
        try {
            if (!localeIdSubtag.isUnicodeLanguageSubtag()) {
                throw new JSRangeErrorException(String.format("Language subtag expected at %s: %s", localeIdSubtag.toString(), charSequence));
            }
            parsedLanguageIdentifier.languageSubtag = localeIdSubtag.toLowerString();
            if (localeIdTokenizer.hasMoreSubtags()) {
                LocaleIdTokenizer.LocaleIdSubtag nextSubtag = localeIdTokenizer.nextSubtag();
                if (handleExtensions(charSequence, localeIdTokenizer, nextSubtag, z, parsedLocaleIdentifier)) {
                    return;
                }
                if (nextSubtag.isUnicodeScriptSubtag()) {
                    parsedLanguageIdentifier.scriptSubtag = nextSubtag.toTitleString();
                    if (!localeIdTokenizer.hasMoreSubtags()) {
                        return;
                    }
                    nextSubtag = localeIdTokenizer.nextSubtag();
                }
                if (nextSubtag.isUnicodeRegionSubtag()) {
                    parsedLanguageIdentifier.regionSubtag = nextSubtag.toUpperString();
                    if (!localeIdTokenizer.hasMoreSubtags()) {
                        return;
                    }
                    nextSubtag = localeIdTokenizer.nextSubtag();
                }
                while (!handleExtensions(charSequence, localeIdTokenizer, nextSubtag, z, parsedLocaleIdentifier)) {
                    if (!nextSubtag.isUnicodeVariantSubtag()) {
                        throw new JSRangeErrorException(String.format("Unknown token [%s] found in locale id: %s", nextSubtag.toString(), charSequence));
                    }
                    addVariantSubtag(nextSubtag.toString(), parsedLanguageIdentifier);
                    if (!localeIdTokenizer.hasMoreSubtags()) {
                        return;
                    }
                    nextSubtag = localeIdTokenizer.nextSubtag();
                }
            }
        } catch (LocaleIdTokenizer.LocaleIdSubtagIterationFailed unused) {
            throw new JSRangeErrorException(String.format("Locale Identifier subtag iteration failed: %s", charSequence));
        }
    }

    static ParsedLocaleIdentifier parseLocaleId(String str, LocaleIdTokenizer localeIdTokenizer) throws JSRangeErrorException {
        ParsedLocaleIdentifier parsedLocaleIdentifier = new ParsedLocaleIdentifier();
        try {
            if (!localeIdTokenizer.hasMoreSubtags()) {
                throw new JSRangeErrorException(String.format("Language subtag not found: %s", str));
            }
            parseLanguageId(str, localeIdTokenizer, localeIdTokenizer.nextSubtag(), false, parsedLocaleIdentifier);
            return parsedLocaleIdentifier;
        } catch (LocaleIdTokenizer.LocaleIdSubtagIterationFailed unused) {
            throw new JSRangeErrorException(String.format("Locale Identifier subtag iteration failed: %s", str));
        }
    }

    public static ParsedLocaleIdentifier parseLocaleId(String str) throws JSRangeErrorException {
        int binarySearch;
        if (LanguageTagsGenerated.regularGrandfatheredKeys != null && (binarySearch = Arrays.binarySearch(LanguageTagsGenerated.regularGrandfatheredKeys, str.toString())) >= 0) {
            str = LanguageTagsGenerated.regularGrandfatheredReplacements[binarySearch];
        }
        String lowerCase = str.toLowerCase();
        return parseLocaleId(lowerCase, new LocaleIdTokenizer(lowerCase));
    }
}
