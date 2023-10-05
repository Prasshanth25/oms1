package com.bumptech.glide.load.data.mediastore;

import java.io.File;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class FileService {
    public boolean exists(File file) {
        return file.exists();
    }

    public long length(File file) {
        return file.length();
    }

    public File get(String str) {
        return new File(str);
    }
}
