package com.facebook.hermes.intl;

import android.os.Build;
import com.facebook.hermes.intl.LocaleMatcher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/* loaded from: classes.dex */
public class LocaleResolver {
    public static HashMap<String, Object> resolveLocale(List<String> list, Object obj, List<String> list2) throws JSRangeErrorException {
        LocaleMatcher.LocaleMatchResult lookupMatch;
        HashMap<String, Object> hashMap = new HashMap<>();
        String javaString = JSObjects.getJavaString(JSObjects.Get(obj, Constants.LOCALEMATCHER));
        if (Build.VERSION.SDK_INT < 24 || javaString.equals(Constants.LOCALEMATCHER_LOOKUP)) {
            lookupMatch = LocaleMatcher.lookupMatch((String[]) list.toArray(new String[list.size()]));
        } else {
            lookupMatch = LocaleMatcher.bestFitMatch((String[]) list.toArray(new String[list.size()]));
        }
        HashSet<String> hashSet = new HashSet();
        for (String str : list2) {
            Object Null = JSObjects.Null();
            Object obj2 = Null;
            if (!lookupMatch.extensions.isEmpty()) {
                obj2 = Null;
                if (lookupMatch.extensions.containsKey(str)) {
                    String str2 = lookupMatch.extensions.get(str);
                    boolean isEmpty = str2.isEmpty();
                    String str3 = str2;
                    if (isEmpty) {
                        str3 = JSObjects.newString("true");
                    }
                    hashSet.add(str);
                    obj2 = str3;
                }
            }
            if (JSObjects.getJavaMap(obj).containsKey(str)) {
                Object Get = JSObjects.Get(obj, str);
                if (JSObjects.isString(Get) && JSObjects.getJavaString(Get).isEmpty()) {
                    Get = JSObjects.newBoolean(true);
                }
                if (!JSObjects.isUndefined(Get) && !Get.equals(obj2)) {
                    hashSet.remove(str);
                    obj2 = Get;
                }
            }
            if (!JSObjects.isNull(obj2)) {
                obj2 = UnicodeExtensionKeys.resolveKnownAliases(str, obj2);
            }
            if (JSObjects.isString(obj2) && !UnicodeExtensionKeys.isValidKeyword(str, JSObjects.getJavaString(obj2), lookupMatch.matchedLocale)) {
                hashMap.put(str, JSObjects.Null());
            } else {
                hashMap.put(str, obj2);
            }
        }
        for (String str4 : hashSet) {
            ArrayList<String> arrayList = new ArrayList<>();
            String javaString2 = JSObjects.getJavaString(UnicodeExtensionKeys.resolveKnownAliases(str4, JSObjects.newString(lookupMatch.extensions.get(str4))));
            if (!JSObjects.isString(javaString2) || UnicodeExtensionKeys.isValidKeyword(str4, JSObjects.getJavaString(javaString2), lookupMatch.matchedLocale)) {
                arrayList.add(javaString2);
                lookupMatch.matchedLocale.setUnicodeExtensions(str4, arrayList);
            }
        }
        hashMap.put("locale", lookupMatch.matchedLocale);
        return hashMap;
    }
}
