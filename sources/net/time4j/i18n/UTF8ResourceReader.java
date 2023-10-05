package net.time4j.i18n;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.Reader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class UTF8ResourceReader extends Reader {
    private BufferedReader internal = null;
    private final PushbackInputStream pis;

    public UTF8ResourceReader(InputStream inputStream) {
        this.pis = new PushbackInputStream(inputStream, 3);
    }

    public String readLine() throws IOException {
        init();
        return this.internal.readLine();
    }

    @Override // java.io.Reader
    public int read(char[] cArr, int i, int i2) throws IOException {
        init();
        return this.internal.read(cArr, i, i2);
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        init();
        return this.internal.ready();
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        BufferedReader bufferedReader = this.internal;
        if (bufferedReader == null) {
            this.pis.close();
        } else {
            bufferedReader.close();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x0023, code lost:
        if (r1[2] == (-65)) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void init() throws java.io.IOException {
        /*
            r6 = this;
            java.io.BufferedReader r0 = r6.internal
            if (r0 == 0) goto L5
            return
        L5:
            r0 = 3
            byte[] r1 = new byte[r0]
            java.io.PushbackInputStream r2 = r6.pis
            r3 = 0
            int r2 = r2.read(r1, r3, r0)
            if (r2 != r0) goto L26
            r0 = r1[r3]
            r4 = -17
            if (r0 != r4) goto L26
            r0 = 1
            r4 = r1[r0]
            r5 = -69
            if (r4 != r5) goto L26
            r4 = 2
            r4 = r1[r4]
            r5 = -65
            if (r4 != r5) goto L26
            goto L27
        L26:
            r0 = 0
        L27:
            if (r0 != 0) goto L30
            if (r2 <= 0) goto L30
            java.io.PushbackInputStream r0 = r6.pis
            r0.unread(r1, r3, r2)
        L30:
            java.io.BufferedReader r0 = new java.io.BufferedReader
            java.io.InputStreamReader r1 = new java.io.InputStreamReader
            java.io.PushbackInputStream r2 = r6.pis
            java.lang.String r3 = "UTF-8"
            r1.<init>(r2, r3)
            r0.<init>(r1)
            r6.internal = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: net.time4j.i18n.UTF8ResourceReader.init():void");
    }
}
