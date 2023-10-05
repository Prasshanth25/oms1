package com.facebook.soloader;

import android.content.Context;
import android.os.Parcel;
import android.os.StrictMode;
import android.util.Log;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.SyncFailedException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class UnpackingSoSource extends DirectorySoSource {
    private static final String DEPS_FILE_NAME = "dso_deps";
    private static final String INSTANCE_LOCK_FILE_NAME = "dso_instance_lock";
    private static final String LOCK_FILE_NAME = "dso_lock";
    private static final String MANIFEST_FILE_NAME = "dso_manifest";
    private static final byte MANIFEST_VERSION = 1;
    protected static final byte STATE_CLEAN = 1;
    protected static final byte STATE_DIRTY = 0;
    private static final String STATE_FILE_NAME = "dso_state";
    private static final String TAG = "fb-UnpackingSoSource";
    @Nullable
    private String[] mAbis;
    protected final Context mContext;
    @Nullable
    protected String mCorruptedLib;
    @Nullable
    protected FileLocker mInstanceLock;
    private final Map<String, Object> mLibsBeingLoaded;

    /* loaded from: classes.dex */
    public interface InputDso extends Closeable {
        int available() throws IOException;

        Dso getDso();

        String getFileName();

        InputStream getStream();

        void write(DataOutput dataOutput, byte[] bArr) throws IOException;
    }

    /* loaded from: classes.dex */
    public static abstract class InputDsoIterator implements Closeable {
        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
        }

        public abstract boolean hasNext();

        @Nullable
        public abstract InputDso next() throws IOException;
    }

    /* loaded from: classes.dex */
    public static abstract class Unpacker implements Closeable {
        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
        }

        public abstract DsoManifest getDsoManifest() throws IOException;

        public abstract InputDsoIterator openDsoIterator() throws IOException;
    }

    protected String getSoNameFromFileName(String str) {
        return str;
    }

    protected abstract Unpacker makeUnpacker(byte b) throws IOException;

    public UnpackingSoSource(Context context, String str) {
        super(getSoStorePath(context, str), 1);
        this.mLibsBeingLoaded = new HashMap();
        this.mContext = context;
    }

    protected UnpackingSoSource(Context context, File file) {
        super(file, 1);
        this.mLibsBeingLoaded = new HashMap();
        this.mContext = context;
    }

    public static File getSoStorePath(Context context, String str) {
        return new File(context.getApplicationInfo().dataDir + "/" + str);
    }

    @Override // com.facebook.soloader.SoSource
    public String[] getSoSourceAbis() {
        String[] strArr = this.mAbis;
        return strArr == null ? super.getSoSourceAbis() : strArr;
    }

    public void setSoSourceAbis(String[] strArr) {
        this.mAbis = strArr;
    }

    /* loaded from: classes.dex */
    public static class Dso {
        public final String hash;
        public final String name;

        public Dso(String str, String str2) {
            this.name = str;
            this.hash = str2;
        }
    }

    /* loaded from: classes.dex */
    public static final class DsoManifest {
        public final Dso[] dsos;

        public DsoManifest(Dso[] dsoArr) {
            this.dsos = dsoArr;
        }

        static final DsoManifest read(DataInput dataInput) throws IOException {
            if (dataInput.readByte() != 1) {
                throw new RuntimeException("wrong dso manifest version");
            }
            int readInt = dataInput.readInt();
            if (readInt < 0) {
                throw new RuntimeException("illegal number of shared libraries");
            }
            Dso[] dsoArr = new Dso[readInt];
            for (int i = 0; i < readInt; i++) {
                dsoArr[i] = new Dso(dataInput.readUTF(), dataInput.readUTF());
            }
            return new DsoManifest(dsoArr);
        }

        public final void write(DataOutput dataOutput) throws IOException {
            dataOutput.writeByte(1);
            dataOutput.writeInt(this.dsos.length);
            int i = 0;
            while (true) {
                Dso[] dsoArr = this.dsos;
                if (i >= dsoArr.length) {
                    return;
                }
                dataOutput.writeUTF(dsoArr[i].name);
                dataOutput.writeUTF(this.dsos[i].hash);
                i++;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class InputDsoStream implements InputDso {
        private final InputStream content;
        private final Dso dso;

        public InputDsoStream(Dso dso, InputStream inputStream) {
            this.dso = dso;
            this.content = inputStream;
        }

        @Override // com.facebook.soloader.UnpackingSoSource.InputDso
        public void write(DataOutput dataOutput, byte[] bArr) throws IOException {
            SysUtil.copyBytes(dataOutput, this.content, Integer.MAX_VALUE, bArr);
        }

        @Override // com.facebook.soloader.UnpackingSoSource.InputDso
        public Dso getDso() {
            return this.dso;
        }

        @Override // com.facebook.soloader.UnpackingSoSource.InputDso
        public String getFileName() {
            return this.dso.name;
        }

        @Override // com.facebook.soloader.UnpackingSoSource.InputDso
        public int available() throws IOException {
            return this.content.available();
        }

        @Override // com.facebook.soloader.UnpackingSoSource.InputDso
        public InputStream getStream() {
            return this.content;
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.content.close();
        }
    }

    public static void writeState(File file, byte b) throws IOException {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(0L);
            randomAccessFile.write(b);
            randomAccessFile.setLength(randomAccessFile.getFilePointer());
            randomAccessFile.getFD().sync();
            randomAccessFile.close();
        } catch (SyncFailedException e) {
            Log.w(TAG, "state file sync failed", e);
        }
    }

    private void deleteUnmentionedFiles(Dso[] dsoArr) throws IOException {
        String[] list = this.soDirectory.list();
        if (list == null) {
            throw new IOException("unable to list directory " + this.soDirectory);
        }
        for (String str : list) {
            if (!str.equals(STATE_FILE_NAME) && !str.equals(LOCK_FILE_NAME) && !str.equals(INSTANCE_LOCK_FILE_NAME) && !str.equals(DEPS_FILE_NAME) && !str.equals(MANIFEST_FILE_NAME)) {
                boolean z = false;
                for (int i = 0; !z && i < dsoArr.length; i++) {
                    if (dsoArr[i].name.equals(getSoNameFromFileName(str))) {
                        z = true;
                    }
                }
                if (!z) {
                    File file = new File(this.soDirectory, str);
                    Log.v(TAG, "deleting unaccounted-for file " + file);
                    SysUtil.dumbDeleteRecursive(file);
                }
            }
        }
    }

    private void extractDso(InputDso inputDso, byte[] bArr) throws IOException {
        Log.i(TAG, "extracting DSO " + inputDso.getDso().name);
        try {
            if (!this.soDirectory.setWritable(true)) {
                throw new IOException("cannot make directory writable for us: " + this.soDirectory);
            }
            extractDsoImpl(inputDso, bArr);
        } finally {
            if (!this.soDirectory.setWritable(false)) {
                Log.w(TAG, "error removing " + this.soDirectory.getCanonicalPath() + " write permission");
            }
        }
    }

    private void extractDsoImpl(InputDso inputDso, byte[] bArr) throws IOException {
        RandomAccessFile randomAccessFile;
        File file = new File(this.soDirectory, inputDso.getFileName());
        RandomAccessFile randomAccessFile2 = null;
        try {
            try {
                if (file.exists() && !file.setWritable(true)) {
                    Log.w(TAG, "error adding write permission to: " + file);
                }
                try {
                    randomAccessFile = new RandomAccessFile(file, "rw");
                } catch (IOException e) {
                    Log.w(TAG, "error overwriting " + file + " trying to delete and start over", e);
                    SysUtil.dumbDeleteRecursive(file);
                    randomAccessFile = new RandomAccessFile(file, "rw");
                }
                randomAccessFile2 = randomAccessFile;
                int available = inputDso.available();
                if (available > 1) {
                    SysUtil.fallocateIfSupported(randomAccessFile2.getFD(), available);
                }
                inputDso.write(randomAccessFile2, bArr);
                randomAccessFile2.setLength(randomAccessFile2.getFilePointer());
                if (!file.setExecutable(true, false)) {
                    throw new IOException("cannot make file executable: " + file);
                }
                if (!file.setWritable(false)) {
                    Log.w(TAG, "error removing " + file + " write permission");
                }
                randomAccessFile2.close();
            } catch (IOException e2) {
                SysUtil.dumbDeleteRecursive(file);
                throw e2;
            }
        } catch (Throwable th) {
            if (!file.setWritable(false)) {
                Log.w(TAG, "error removing " + file + " write permission");
            }
            if (randomAccessFile2 != null) {
                randomAccessFile2.close();
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:143:0x0040 A[Catch: all -> 0x0033, TryCatch #4 {all -> 0x0033, blocks: (B:134:0x002e, B:143:0x0040, B:144:0x0047, B:145:0x0051, B:147:0x0057, B:164:0x00a5, B:139:0x0037, B:150:0x005f, B:152:0x0064, B:154:0x0076, B:157:0x0089, B:158:0x008c, B:162:0x00a0), top: B:182:0x002e }] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0057 A[Catch: all -> 0x0033, TRY_LEAVE, TryCatch #4 {all -> 0x0033, blocks: (B:134:0x002e, B:143:0x0040, B:144:0x0047, B:145:0x0051, B:147:0x0057, B:164:0x00a5, B:139:0x0037, B:150:0x005f, B:152:0x0064, B:154:0x0076, B:157:0x0089, B:158:0x008c, B:162:0x00a0), top: B:182:0x002e }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void regenerate(byte r10, com.facebook.soloader.UnpackingSoSource.DsoManifest r11, com.facebook.soloader.UnpackingSoSource.InputDsoIterator r12) throws java.io.IOException {
        /*
            r9 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "regenerating DSO store "
            r0.<init>(r1)
            java.lang.Class r1 = r9.getClass()
            java.lang.String r1 = r1.getName()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "fb-UnpackingSoSource"
            android.util.Log.v(r1, r0)
            java.io.File r0 = new java.io.File
            java.io.File r2 = r9.soDirectory
            java.lang.String r3 = "dso_manifest"
            r0.<init>(r2, r3)
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile
            java.lang.String r3 = "rw"
            r2.<init>(r0, r3)
            r0 = 1
            if (r10 != r0) goto L3c
            com.facebook.soloader.UnpackingSoSource$DsoManifest r10 = com.facebook.soloader.UnpackingSoSource.DsoManifest.read(r2)     // Catch: java.lang.Throwable -> L33 java.lang.Exception -> L36
            goto L3d
        L33:
            r10 = move-exception
            goto Ld2
        L36:
            r10 = move-exception
            java.lang.String r3 = "error reading existing DSO manifest"
            android.util.Log.i(r1, r3, r10)     // Catch: java.lang.Throwable -> L33
        L3c:
            r10 = 0
        L3d:
            r3 = 0
            if (r10 != 0) goto L47
            com.facebook.soloader.UnpackingSoSource$DsoManifest r10 = new com.facebook.soloader.UnpackingSoSource$DsoManifest     // Catch: java.lang.Throwable -> L33
            com.facebook.soloader.UnpackingSoSource$Dso[] r4 = new com.facebook.soloader.UnpackingSoSource.Dso[r3]     // Catch: java.lang.Throwable -> L33
            r10.<init>(r4)     // Catch: java.lang.Throwable -> L33
        L47:
            com.facebook.soloader.UnpackingSoSource$Dso[] r11 = r11.dsos     // Catch: java.lang.Throwable -> L33
            r9.deleteUnmentionedFiles(r11)     // Catch: java.lang.Throwable -> L33
            r11 = 32768(0x8000, float:4.5918E-41)
            byte[] r11 = new byte[r11]     // Catch: java.lang.Throwable -> L33
        L51:
            boolean r4 = r12.hasNext()     // Catch: java.lang.Throwable -> L33
            if (r4 == 0) goto Lb5
            com.facebook.soloader.UnpackingSoSource$InputDso r4 = r12.next()     // Catch: java.lang.Throwable -> L33
            r5 = 1
            r6 = 0
        L5d:
            if (r5 == 0) goto L8c
            com.facebook.soloader.UnpackingSoSource$Dso[] r7 = r10.dsos     // Catch: java.lang.Throwable -> La9
            int r7 = r7.length     // Catch: java.lang.Throwable -> La9
            if (r6 >= r7) goto L8c
            com.facebook.soloader.UnpackingSoSource$Dso r7 = r4.getDso()     // Catch: java.lang.Throwable -> La9
            java.lang.String r7 = r7.name     // Catch: java.lang.Throwable -> La9
            com.facebook.soloader.UnpackingSoSource$Dso[] r8 = r10.dsos     // Catch: java.lang.Throwable -> La9
            r8 = r8[r6]     // Catch: java.lang.Throwable -> La9
            java.lang.String r8 = r8.name     // Catch: java.lang.Throwable -> La9
            boolean r7 = r8.equals(r7)     // Catch: java.lang.Throwable -> La9
            if (r7 == 0) goto L89
            com.facebook.soloader.UnpackingSoSource$Dso[] r7 = r10.dsos     // Catch: java.lang.Throwable -> La9
            r7 = r7[r6]     // Catch: java.lang.Throwable -> La9
            java.lang.String r7 = r7.hash     // Catch: java.lang.Throwable -> La9
            com.facebook.soloader.UnpackingSoSource$Dso r8 = r4.getDso()     // Catch: java.lang.Throwable -> La9
            java.lang.String r8 = r8.hash     // Catch: java.lang.Throwable -> La9
            boolean r7 = r7.equals(r8)     // Catch: java.lang.Throwable -> La9
            if (r7 == 0) goto L89
            r5 = 0
        L89:
            int r6 = r6 + 1
            goto L5d
        L8c:
            java.io.File r6 = new java.io.File     // Catch: java.lang.Throwable -> La9
            java.io.File r7 = r9.soDirectory     // Catch: java.lang.Throwable -> La9
            java.lang.String r8 = r4.getFileName()     // Catch: java.lang.Throwable -> La9
            r6.<init>(r7, r8)     // Catch: java.lang.Throwable -> La9
            boolean r6 = r6.exists()     // Catch: java.lang.Throwable -> La9
            if (r6 != 0) goto L9e
            r5 = 1
        L9e:
            if (r5 == 0) goto La3
            r9.extractDso(r4, r11)     // Catch: java.lang.Throwable -> La9
        La3:
            if (r4 == 0) goto L51
            r4.close()     // Catch: java.lang.Throwable -> L33
            goto L51
        La9:
            r10 = move-exception
            if (r4 == 0) goto Lb4
            r4.close()     // Catch: java.lang.Throwable -> Lb0
            goto Lb4
        Lb0:
            r11 = move-exception
            r10.addSuppressed(r11)     // Catch: java.lang.Throwable -> L33
        Lb4:
            throw r10     // Catch: java.lang.Throwable -> L33
        Lb5:
            r2.close()
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            java.lang.String r11 = "Finished regenerating DSO store "
            r10.<init>(r11)
            java.lang.Class r11 = r9.getClass()
            java.lang.String r11 = r11.getName()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            android.util.Log.v(r1, r10)
            return
        Ld2:
            r2.close()     // Catch: java.lang.Throwable -> Ld6
            goto Lda
        Ld6:
            r11 = move-exception
            r10.addSuppressed(r11)
        Lda:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.regenerate(byte, com.facebook.soloader.UnpackingSoSource$DsoManifest, com.facebook.soloader.UnpackingSoSource$InputDsoIterator):void");
    }

    protected boolean depsChanged(byte[] bArr, byte[] bArr2) {
        return !Arrays.equals(bArr, bArr2);
    }

    /* JADX WARN: Removed duplicated region for block: B:184:0x009f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:185:0x00a0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean refreshLocked(com.facebook.soloader.FileLocker r11, int r12, byte[] r13) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 247
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.refreshLocked(com.facebook.soloader.FileLocker, int, byte[]):boolean");
    }

    private Runnable createSyncer(final FileLocker fileLocker, final byte[] bArr, final File file, final File file2, final DsoManifest dsoManifest, final Boolean bool) {
        return new Runnable() { // from class: com.facebook.soloader.UnpackingSoSource.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Log.v(UnpackingSoSource.TAG, "starting syncer worker");
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file2, "rw");
                    try {
                        randomAccessFile.write(bArr);
                        randomAccessFile.setLength(randomAccessFile.getFilePointer());
                        randomAccessFile.close();
                        randomAccessFile = new RandomAccessFile(new File(UnpackingSoSource.this.soDirectory, UnpackingSoSource.MANIFEST_FILE_NAME), "rw");
                        try {
                            dsoManifest.write(randomAccessFile);
                            randomAccessFile.close();
                            SysUtil.fsyncRecursive(UnpackingSoSource.this.soDirectory);
                            UnpackingSoSource.writeState(file, (byte) 1);
                            Log.v(UnpackingSoSource.TAG, "releasing dso store lock for " + UnpackingSoSource.this.soDirectory + " (from syncer thread)");
                            fileLocker.close();
                        } finally {
                        }
                    } finally {
                    }
                } catch (IOException e) {
                    if (!bool.booleanValue()) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
    }

    protected byte[] getDepsBlock() throws IOException {
        Parcel obtain = Parcel.obtain();
        Unpacker makeUnpacker = makeUnpacker((byte) 1);
        try {
            Dso[] dsoArr = makeUnpacker.getDsoManifest().dsos;
            obtain.writeByte((byte) 1);
            obtain.writeInt(dsoArr.length);
            for (int i = 0; i < dsoArr.length; i++) {
                obtain.writeString(dsoArr[i].name);
                obtain.writeString(dsoArr[i].hash);
            }
            if (makeUnpacker != null) {
                makeUnpacker.close();
            }
            byte[] marshall = obtain.marshall();
            obtain.recycle();
            return marshall;
        } catch (Throwable th) {
            if (makeUnpacker != null) {
                try {
                    makeUnpacker.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    @Nullable
    protected FileLocker getOrCreateLock(File file, boolean z) throws IOException {
        return SysUtil.getOrCreateLockOnDir(this.soDirectory, file, z);
    }

    /* JADX WARN: Removed duplicated region for block: B:112:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x011f  */
    @Override // com.facebook.soloader.SoSource
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void prepare(int r17) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 308
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.prepare(int):void");
    }

    private Object getLibraryLock(String str) {
        Object obj;
        synchronized (this.mLibsBeingLoaded) {
            obj = this.mLibsBeingLoaded.get(str);
            if (obj == null) {
                obj = new Object();
                this.mLibsBeingLoaded.put(str, obj);
            }
        }
        return obj;
    }

    @Override // com.facebook.soloader.DirectorySoSource, com.facebook.soloader.SoSource
    @Nullable
    public String getLibraryPath(String str) throws IOException {
        File soFileByName = getSoFileByName(str);
        if (soFileByName == null) {
            return null;
        }
        return soFileByName.getCanonicalPath();
    }

    public synchronized void prepare(String str) throws IOException {
        synchronized (getLibraryLock(str)) {
            this.mCorruptedLib = str;
            prepare(2);
        }
    }

    @Override // com.facebook.soloader.DirectorySoSource, com.facebook.soloader.SoSource
    public int loadLibrary(String str, int i, StrictMode.ThreadPolicy threadPolicy) throws IOException {
        int loadLibraryFrom;
        synchronized (getLibraryLock(str)) {
            loadLibraryFrom = loadLibraryFrom(str, i, this.soDirectory, threadPolicy);
        }
        return loadLibraryFrom;
    }
}
